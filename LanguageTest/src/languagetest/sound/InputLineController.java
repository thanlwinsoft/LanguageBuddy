/*
 * InputLineController.java
 *
 * Created on February 6, 2004, 3:05 PM
 */

package languagetest.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 *
 * @author  keith
 */
public class InputLineController implements Runnable
{
    final static int PAUSED = 0;
    final static int SEEKING = 1;
    final static int PLAYING = 2;
    final static int PLAY_AFTER_SEEK = 3;
    final static int SEEK_TO_START = 4;
    final static int PLAY_FROM_START = 5;
    
    private final static long MAX_SINGLE_SEEK = 1; // seconds
    private final static int MARK_INVALID_SIZE = 16777216;
    private final static int CACHE_SIZE = 30; // seconds
    private final static float READ_BUFFER_LENGTH_SEC = (float)1; // 1 second 
    private boolean stop = false;
    private long startByteOffset = 0;
    private long stopByteOffset = -1;
    private long bytesPerFrame = -1;
    private float framesPerSec = -1;
    private long bytesPerSec = -1;
    private long markPosition = -1;
    private int maxSeek = 0;
    private int state = SEEK_TO_START;
    private long playPosition = 0;
    private long readPosition = 0;
    private long seekTarget = 0;
    private long seekStart = 0;
    private long totalLength = -1;
    private long boundsBegin = 0;
    private long boundsDuration = -1;
    private long preBuffer = 0;
    private File audioFile = null;
    private AudioInputStream input = null;
    private AudioInputStream fileStream = null;
    private OutputLineController outputController = null;
    private CacheBuffer cacheBuffer = null;
    private byte [] readBuffer = null;
    private int unwrittenStart = 0;
    private int unwrittenLength = 0;
    private AudioPlayListener listener = null;
    private AudioFormat outputFormat = null;
    private LineController lineController = null;
    private long msLengthHint = AudioSystem.NOT_SPECIFIED;
    /** Creates a new instance of InputLineController */
    public InputLineController(File file, long msLengthHint, LineController lineController) 
    {
        this.audioFile = file;
        this.lineController = lineController;
        this.outputFormat = lineController.getPlayLineFormat();
        this.msLengthHint = msLengthHint;
    }
    
    protected void init() throws IOException, UnsupportedAudioFileException
    {
        while (lineController.getSourceDataLine()==null)
        {
            if (stop) return;
            sleep();
        }
        openInputStream(audioFile);
        bytesPerFrame = input.getFormat().getFrameSize();
        framesPerSec = input.getFormat().getFrameRate();
        bytesPerSec = bytesPerFrame * (int)framesPerSec;
        System.out.println("Frame size: " + bytesPerFrame +
                           " Frame Rate: " + framesPerSec);
        maxSeek = (int)(MAX_SINGLE_SEEK * bytesPerSec);
        // reset offsets
        startByteOffset = 0;
        if (input.getFrameLength() > 0)
        {
            stopByteOffset = input.getFrameLength() * bytesPerFrame;
            totalLength = stopByteOffset;
        }
        else if (msLengthHint > 0) // use hint for now
        {
            stopByteOffset = msToBytes(msLengthHint);
            totalLength = stopByteOffset;
        }
        else
        {
            stopByteOffset = -1;
            totalLength = -1;
        }
        System.out.println("Input file length = " + bytesToMs(totalLength));
        //input obtained, now mark start so we can go back to it
        markPosition = 0;
        if (input.markSupported())
        {
            input.mark(MARK_INVALID_SIZE);
        }
        else
        {
            try
            {
                cacheBuffer = new CacheBuffer((int)(bytesPerSec * CACHE_SIZE));
            }
            catch (OutOfMemoryError e)
            {
                System.out.println(e.getMessage());
            }
        }
        int bufferLength = (int)(bytesPerSec * READ_BUFFER_LENGTH_SEC);
        
        readBuffer = new byte[roundOffsetToFrame(bufferLength)];
        unwrittenStart = 0;
        unwrittenLength = 0;
        // set bounds properly now parameters are known
        setBounds(boundsBegin, boundsDuration);
    }
    
    public File getAudioFile()
    {
        return audioFile;
    }
    
    public synchronized void setOutputController(OutputLineController c)
    {
        if (state == PLAYING)
        {
            state = PAUSED;
        }
        this.outputController = c;
        if (c != null)
        {
            this.preBuffer = c.getBufferSize() / 2;
        }
    }
    
    
    
    protected void openInputStream(File audioFile) 
        throws IOException, UnsupportedAudioFileException
    {
        fileStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat audioFormat = fileStream.getFormat();
        System.out.println( "Play input audio format=" +  audioFormat);
        System.out.println("Frame length=" + fileStream.getFrameLength());
//              Convert compressed audio data to uncompressed PCM format.
        
        
            
        if (lineController != null &&
            !audioFormat.matches(lineController.getPlayLineFormat()))
        {
            // perhaps it supports the format dirrectly
            if (((DataLine.Info)
                lineController.getSourceDataLine().getLineInfo())
                .isFormatSupported(audioFormat))
            {
                // line supports this format directly 
                input = fileStream;
                outputFormat = audioFormat;
                if (outputController != null)
                {
                    outputController.setFormat(audioFormat);
                    outputController.open();
                }
            }
            else 
            {
                // make a PCM_SIGNED version of the format and see if that is 
                // supported
                int frameSize = 
                    lineController.getPlayLineFormat().getSampleSizeInBits() *
                    audioFormat.getChannels() / 8;
                outputFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
                    audioFormat.getSampleRate(), 
                    lineController.getPlayLineFormat().getSampleSizeInBits(), // not sure about this one
                    audioFormat.getChannels(), 
                    frameSize,
                    audioFormat.getSampleRate(), 
                    lineController.getPlayLineFormat().isBigEndian()); 
            
                if (AudioSystem.isConversionSupported(outputFormat, audioFormat) &&
                    ((DataLine.Info)
                     lineController.getSourceDataLine().getLineInfo())
                     .isFormatSupported(outputFormat))
                {
                    System.out.println( "Converting audio format to " + outputFormat );
                    input = AudioSystem.getAudioInputStream( outputFormat, fileStream );
                    if (outputController != null)
                    {
                        outputController.setFormat(outputFormat);
                        outputController.open();
                    }
                }
                else
                {
                    System.out.println("Conversion not supported " + audioFormat
                            + "->" + outputFormat);
                    input = null;
                    throw new UnsupportedAudioFileException("Conversion not supported "
                        + audioFormat
                        + "->" + outputFormat);
                    
                }
            }
        }
        else
        {
            outputFormat = lineController.getPlayLineFormat();
            input = fileStream;
            if (outputController != null)
            {
                outputController.setFormat(audioFormat);
                outputController.open();
            }
        }
        playPosition = 0;
        readPosition = 0;

    }
    
    public void stopThread()
    {
        stop = true;
    }
    
    public synchronized void stop()
    {
        switch (state)
        {
            case PLAYING:
                if (outputController != null)
                {
                    outputController.stop();
                } // fall through for rest of actions
            case PAUSED:         
                // if it was playing or just paused then output should be
                // flushed
                if (outputController != null)
                {
                    outputController.flush();
                } 
                // fall through for rest of actions - the remaining cases should
                // already have been flushed or drained
            case SEEKING:
            case PLAY_AFTER_SEEK:
                state = SEEK_TO_START;
                unwrittenStart = 0;
                unwrittenLength = 0;
                setSeekTarget(startByteOffset);
                break;
            case SEEK_TO_START:
                break;
            case PLAY_FROM_START:
                state = SEEK_TO_START;
                break;
            default:
                break;
        }
    }
    
    public void run()
    {
        int oldState = 0;
        try
        {
            init();
            while (!stop)
            {
                int instantaneousState;
                synchronized (this)
                {
                    instantaneousState = state;
                }
                if (oldState != instantaneousState)
                {
                    System.out.println("InputLineController state "
                        + stateToString(instantaneousState) + " " 
                        + this.hashCode());
                    oldState = instantaneousState;
                }
                switch (instantaneousState)
                {
                    case PAUSED:
                        showPlayPosition();
                        sleep();
                        break;
                    case SEEKING:
                    case PLAY_AFTER_SEEK:
                    case SEEK_TO_START:
                    case PLAY_FROM_START:
                        showPlayPosition();
                        if (input.markSupported())
                        {
                            seekWithMark();            
                        }
                        else
                        {
                            seekWithCache();
                        }
                        break;
                    case PLAYING:
                        playState();
                        break;
                    default:
                        sleep();
                }
            }
            // tidy up before thread exits
            if (input != null) input.close();
            // are we direcly using the fileStream or is it going through 
            // a second stream that we need to close as well
            if (input != fileStream)
            {
                fileStream.close();
                fileStream = null;
            }
            input = null;
        }
        // TBD give user warning
        catch (UnsupportedAudioFileException uafe)
        {
            System.out.println(uafe.getMessage());
        }
        catch (IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
        if (outputController != null)
        {
            outputController.stop();
        }
        // set the buffers to null to free memory
        if (cacheBuffer != null)
        {
            cacheBuffer.clear();
            cacheBuffer = null;
        }
        readBuffer = null;
        outputController = null;
        listener = null;
        audioFile = null;
        System.out.println("InputLineController thread finished");
    }    
    
    public synchronized void seek(long amount)
    {
        switch (state)
        {
            case PAUSED:
                // a seek after a pause should be flushed
                if (outputController != null)
                {
                    outputController.flush();
                }
                seekTarget = playPosition;
                state = SEEKING;
            case SEEKING:
            case PLAY_AFTER_SEEK:
            case SEEK_TO_START:
            case PLAY_FROM_START:
                long newSeek = seekTarget + msToBytes(amount);
                if (newSeek >= startByteOffset && 
                    (newSeek < stopByteOffset || stopByteOffset < 0))
                {
                    setSeekTarget(newSeek);
                }
                break;
            default:
                // do nothing
                break;
        }
    }
    
    public synchronized void pause()
    {
        switch (state)
        {
            case PAUSED:
            case SEEKING:
            case SEEK_TO_START:
                if (outputController != null)
                {
                    outputController.stop();
                }
                break;
            case PLAY_AFTER_SEEK:
                state = SEEKING;
                break;
            case PLAY_FROM_START:
                state = SEEK_TO_START;
                break;
            case PLAYING:
                state = PAUSED;
                if (outputController != null)
                {
                    outputController.stop();
                }
                break;
            default:
                break;
        }
    }
    
    void setSeekTarget(long target)
    {
        seekStart = playPosition;
        if (seekStart > target) seekStart = 0;
        seekTarget = roundOffsetToFrame(target);
    }
    
    public synchronized void play()
    {
        // if there is still data in the buffer use that up
        // we must have paused before the current state
        if (outputController != null && 
            (outputController.getBufferSize() != 
             outputController.writeSpaceAvailable()))
        {
            switch (state)
            {
                case PAUSED:
                    state = PLAYING;
                    outputController.keepPlaying();
                    break;
                case SEEKING:
                    outputController.playToEnd();
                    break;
                case SEEK_TO_START:
                    outputController.playToEnd();
                    break;
                case PLAY_AFTER_SEEK:
                    break;
                case PLAY_FROM_START:
                    break;
                case PLAYING:
                    break;
                default:
                    break;    
            }
        }
        else
        {
            switch (state)
            {
                case PAUSED:
                    state = PLAYING;
                    break;
                case SEEKING:
                    state = PLAY_AFTER_SEEK;
                    break;
                case SEEK_TO_START:
                    state = PLAY_FROM_START;
                    break;
                case PLAY_AFTER_SEEK:
                    break;
                case PLAY_FROM_START:
                    break;
                case PLAYING:
                    break;
                default:
                    break;
            }
        }
    }
    
    public void sleep()
    {
        try
        {
            Thread.sleep(10);
        }
        catch (InterruptedException e)
        {
            // just return immediately
        }
    }
    
    protected void seekWithMark() 
        throws IOException, UnsupportedAudioFileException
    {
        boolean seekFinished = false;
        long thisSeek = seekTarget - playPosition;
        if (thisSeek == 0)
        {
            if (playPosition == startByteOffset)
            {
                input.mark(MARK_INVALID_SIZE);
                markPosition = playPosition;
            }
            synchronized (this)
            {
                // change state
                switch (state)
                {
                    case PLAY_AFTER_SEEK:
                    case PLAY_FROM_START:
                        state = PLAYING;
                        break;
                    default:
                        state = PAUSED;
                }
                showInitialisationProgress();
            }
        }
        if (thisSeek < 0) // rewind
        {
            if (input.markSupported())
            {
                if (markPosition > 0 && markPosition <= seekTarget)
                {
                    input.reset();
                    playPosition = markPosition;
                }
                else
                {
                    openInputStream(audioFile);
                }
            }
            else if (cacheBuffer.isCached(seekTarget) && 
                     readPosition == cacheBuffer.getWriteOffset())
            {
                playPosition = seekTarget;
            }
            else
            {
                openInputStream(audioFile);
            }
        }
        else // forward skip
        {
            if (markPosition > seekTarget) 
            {
                input.reset();
                playPosition = markPosition;
            }
            else
            {                               
                if (thisSeek > maxSeek) thisSeek = maxSeek;
                thisSeek = roundOffsetToFrame(thisSeek);
                // don't skip over start. Break the skip so start can be marked
                if ((playPosition + thisSeek) > startByteOffset)
                {
                    thisSeek = startByteOffset - playPosition;
                    long skipped = input.skip(thisSeek);
                    if (skipped == thisSeek) input.mark(MARK_INVALID_SIZE);
                    playPosition += skipped;
                    markPosition = playPosition;
                }
                else
                {
                    long skipped = input.skip(thisSeek);
                    playPosition += skipped;
                }
            }
        }
    }
    
    
    
    protected void seekWithCache() 
        throws IOException, UnsupportedAudioFileException
    {
        boolean seekFinished = false;
        int thisSeek = (int)(seekTarget - playPosition);
        thisSeek = roundOffsetToFrame(thisSeek);
        if (thisSeek == 0)
        {
            synchronized (this)
            {
                // change state
                switch (state)
                {
                    case PLAY_AFTER_SEEK:
                    case PLAY_FROM_START:
                        state = PLAYING;
                        break;
                    default:
                        state = PAUSED;
                }
                showInitialisationProgress();
                if (cacheBuffer != null && 
                    cacheBuffer.getWriteOffset() != playPosition &&
                    !cacheBuffer.isCached(playPosition))
                {
                    // if cache is not already initialised for current play
                    // position, init it now ready for playing
                    cacheBuffer.reinit(playPosition);
                    readPosition = playPosition;
                }
            }
        }
        else if (thisSeek < 0) // rewind
        {
            if (cacheBuffer != null && 
                cacheBuffer.isCached(seekTarget) && 
                readPosition == cacheBuffer.getWriteOffset())
            {
                playPosition = seekTarget;
            }
            else
            {
                openInputStream(audioFile);
            }
        }
        else // forward skip
        {
            // use cache
            if (cacheBuffer != null && 
                cacheBuffer.isCached(seekTarget) && 
                readPosition == cacheBuffer.getWriteOffset())
            {
                playPosition = seekTarget;
            }
            else // real seek
            {
                if (thisSeek > maxSeek) thisSeek = maxSeek;
                thisSeek = roundOffsetToFrame(thisSeek);
                // don't skip over start. Break the skip so cache can be started
                if ((playPosition < startByteOffset)&&
	            (playPosition + thisSeek >= startByteOffset))
                {
                    thisSeek = (int)(startByteOffset - playPosition);
                    // reinit cache
                    long skipped = input.skip(thisSeek);
                    if (cacheBuffer != null && skipped == thisSeek) 
                    {
                        cacheBuffer.reinit(startByteOffset);
                    }
                    playPosition += skipped;
                    readPosition = playPosition;
                }
                // after start, read into cache
                else if(cacheBuffer != null && 
                        (readPosition == cacheBuffer.getWriteOffset()) && 
                        (readPosition >= startByteOffset))
                {
                    int skipped = input.read(readBuffer, 0, thisSeek);
                    if (skipped > 0)
                    {
                        cacheBuffer.write(readBuffer, skipped);
                        playPosition += skipped;
                        readPosition = playPosition;
                    }
                    else // end of buffer reached
                    {
                        endOfBufferReached();
                    }
                }
                else // plain skip
                {
                    int skipped = (int)input.skip(thisSeek);
                    if (skipped == 0) // try a read instead
                    {
                        skipped = input.read(readBuffer, 0, thisSeek);
                    }
                    // check that there wasn't a failed read (-1 return)
                    if (skipped > 0)
                    {
                        playPosition += skipped;
                        readPosition = playPosition;
                    }
                    else // end of buffer reached
                    {
                        endOfBufferReached();
                        sleep();
                    }
                }
                    
            }
            
        }
        showInitialisationProgress();
    }
    
    protected void endOfBufferReached()
    {
        synchronized (this)
        {
            state = SEEK_TO_START;
            totalLength = playPosition;
            if (startByteOffset > totalLength) 
            {
                startByteOffset = totalLength;
            }
            if (stopByteOffset > totalLength) 
            {
                stopByteOffset = totalLength;
            }
        }
    }
    
    protected void playState() throws IOException
    {
        if (outputController != null &&
            outputController.open())
        {   
            
            if (unwrittenLength > 0)
            {
                synchronized (this)
                {
                    if (outputController != null)
                    { 
                        // write may fail if not playing!
                        outputController.keepPlaying();
                        int written = outputController.write(readBuffer, unwrittenStart, 
                                                             unwrittenLength);
                        if (written == unwrittenLength)
                        {
                            unwrittenStart = 0;
                            unwrittenLength = 0;
                        }
                        else
                        {
                            unwrittenStart += written;
                            unwrittenLength -= written;
                            
                        }
                    }
                }
                if (unwrittenLength > 0) sleep();
                return;
            }
             
            
            int bytesToRead = outputController.writeSpaceAvailable();
            if (bytesToRead > readBuffer.length) 
            {
                bytesToRead = readBuffer.length;
            }
            synchronized (this)
            {
                if (outputController != null)
                {                    
                    if (stopByteOffset > -1 &&
                        playPosition + bytesToRead >= stopByteOffset)
                    {
                        bytesToRead = (int)(stopByteOffset - playPosition);
                        outputController.playToEnd();
                    }/*
                    else if (outputController.getBufferSize() - 
                             outputController.writeSpaceAvailable() <
                             preBuffer)
                    {
                        // don't start playing yet, wait for more data in output
                        // buffer
                    }*/
                    else
                    {
                        outputController.keepPlaying();
                    }
                }
            }
            bytesToRead = roundOffsetToFrame(bytesToRead);
            // if buffer is full try sleeping
            if (bytesToRead == 0) sleep();
            
            if (input.markSupported())
            {
                // check mark will still be valid after read
                if (markPosition + MARK_INVALID_SIZE <= 
                    playPosition + bytesToRead)
                {
                    // remark so we still have a mark for small rewinds
                    markPosition = playPosition;
                    input.mark(MARK_INVALID_SIZE);
                }
            }
            // otherwise the data may be read straight from cache
            else if (cacheBuffer != null && cacheBuffer.isCached(playPosition))
            {
                int read = 0;
                if (bytesToRead + playPosition <= readPosition)
                {
                    read = cacheBuffer.read(readBuffer, playPosition, 
                                                 (int)bytesToRead);
                }
                else // limit this write to date in cache
                {
                    read = cacheBuffer.read(readBuffer, playPosition, 
                                            (int)(readPosition - playPosition));
                }
                playPosition += read;
                synchronized (this)
                {
                    if (outputController != null)
                    {
                        int wrote = outputController.write(readBuffer, 0, read);
                        if (wrote != read)
                        {
                            unwrittenStart = wrote;
                            unwrittenLength = read - wrote;
                            System.out.println("Output failed to write "
                                 + (read - wrote) + " bytes");
                            outputController.flush();
                        }
                    }
                }
                // already finished reading
                bytesToRead = 0;
            }
            // data is not in cache so need to read it directly from stream
            if (bytesToRead > 0)
            {    
                int read = input.read(readBuffer, 0, bytesToRead);
                if (read == -1)
                {
                    // end of stream
                    totalLength = playPosition;
                    synchronized (this)
                    {
                        stopByteOffset = totalLength;                        
                    }
                }
                else
                {
                    int bytesToWrite = roundOffsetToFrame(read);
                    if (bytesToWrite != read)
                    {
                        System.out.println("Only read " + read + " using " + 
                            bytesToWrite);
                        
                    }
                    playPosition += read;
                    synchronized (this)
                    {
                        if (outputController != null)
                        {
                            int wrote = outputController.write(readBuffer, 
                                                               0, bytesToWrite);
                            if (wrote != read)
                            {
                                unwrittenStart = wrote;
                                unwrittenLength = read - wrote;
                                System.out.println("Output failed to write " + 
                                    (read - wrote) + " bytes");
                                outputController.flush();
                            }
                        }
                    }
                    // sort out cache
                    if (cacheBuffer != null && 
                        cacheBuffer.getWriteOffset() == readPosition)
                    {
                        cacheBuffer.write(readBuffer,  read);
                        readPosition = playPosition;
                    }
                }
            }
            else
            {
                sleep();
            }  
            showPlayPosition();
        }
        else
        {
            sleep(); // wait for open
        }
        if (stopByteOffset > -1 && 
            (playPosition > stopByteOffset - bytesPerFrame))
        {
            synchronized (this)
            {
                seekTarget = startByteOffset;
                state = SEEK_TO_START;
                // reset unwritten
                unwrittenStart = 0;
                unwrittenLength = 0;
            }
        }
    }
    
    
    
    
    
     /**
     * Method to restrict play of the sound file to the specified time window
     * within the file. The method will adjust the offsets appropriately if they 
     * are out of range. 
     * The actual offsets used can be retrieved using the @see getStartMs()
     * and @see getDurationMs() methods.
     * @param start Start time in ms from the beginning of the file
     * @param duration Length of play window in ms
     */
    public void setBounds(long start, long duration)
    {
        long newStart; 
        long newStop;
        // store bounds for later use if parameters not initialised yet
        boundsBegin = start;
        boundsDuration = duration;
        // can't set properly if bytesPerFrame and framesPerSec not initialised
        // setBounds will be called again when they are
        if (bytesPerFrame < 0 || framesPerSec < 0) return;
        if (start > 0)
        {
            newStart = roundOffsetToFrame(msToBytes(start));
        }
        else
        {
            newStart = 0;
        }
        if (duration > 0)
        {
            newStop = roundOffsetToFrame(newStart + 
                                         msToBytes(duration));
        }
        else
        {
            if (totalLength > 0)
            {
                newStop = totalLength;
            }
            else
            {
                newStop = -1;
            }
        }
        // protect the section where we change the valuses used by the
        // play thread.
            
        synchronized (this)
        {
            if (newStart != startByteOffset || newStop != stopByteOffset)
            {
            
                startByteOffset = newStart;
                stopByteOffset = newStop;
                switch (state)
                {
                    case PAUSED:
                        if ((playPosition > stopByteOffset && stopByteOffset > -1)
                            || playPosition < startByteOffset)
                        {
                            if (outputController != null)
                            {
                                outputController.flush();
                            }
                            setSeekTarget (startByteOffset);
                            state = SEEK_TO_START;
                        }
                        break;
                    case PLAY_FROM_START:
                    case SEEK_TO_START:
                        setSeekTarget(startByteOffset);
                        break;
                    case SEEKING:
                        if ((seekTarget > stopByteOffset) && (stopByteOffset > -1))
                        {
                            setSeekTarget(startByteOffset);
                            state = SEEK_TO_START;
                        }
                        if (seekTarget < startByteOffset)
                        {
                            setSeekTarget(startByteOffset);
                            state = SEEK_TO_START;
                        }
                        break;
                    case PLAY_AFTER_SEEK:
                        if (seekTarget > stopByteOffset && stopByteOffset > -1)
                        {
                            setSeekTarget(startByteOffset);
                            state = PLAY_FROM_START;
                        }
                        if (seekTarget < startByteOffset)
                        {
                            setSeekTarget(startByteOffset);
                            state = PLAY_FROM_START;
                        }
                        break;
                    case PLAYING:
                        if ((playPosition > stopByteOffset && stopByteOffset > -1)||
                            playPosition < startByteOffset)
                        {
                            if (outputController != null)
                            {
                                outputController.stop();
                                outputController.flush();
                            }
                            setSeekTarget(startByteOffset);
                            state = SEEK_TO_START;
                        }
                        break;
                }
            }
        }
    }
    
    protected int roundOffsetToFrame(int newOffset)
    {
        return (int)(Math.floor((float)newOffset / 
                            (float)bytesPerFrame)) * (int)bytesPerFrame;
    }
    
    protected long roundOffsetToFrame(long newOffset)
    {
        return (long)(Math.floor((float)newOffset / 
                            (float)bytesPerFrame)) * bytesPerFrame;
    }
    
    protected long bytesToMs(long value)
    {
        // treat -1 specially
        if (value < 0)  return AudioSystem.NOT_SPECIFIED;
        float byteOffset = (float)value;
        float secondsOffset = byteOffset / 
            (float)(bytesPerFrame * framesPerSec);
        return (long)(secondsOffset * 1000);
    }
   
    protected long msToBytes(long value)
    {
        // if neg values aren't handled then rewind doesn't work!
        //if (value < 0)  return AudioSystem.NOT_SPECIFIED;
        float secOffset = ((float)value) / 1000;
        float byteOffset = secOffset * bytesPerFrame * framesPerSec;
        return (long)(byteOffset);
    }
    
    public synchronized long getStartMs() 
    { 
        return bytesToMs(startByteOffset); 
    }
    public synchronized long getDurationMs() 
    { 
        return bytesToMs(stopByteOffset - startByteOffset); 
    }
    /** Method calls the playPosition method on each registered 
     * AudioPlayListener.
     */
    protected synchronized void showPlayPosition()
    {
        if (listener != null)
        {
            if (outputController != null && outputController.isOpen())
            {
                listener.initialisationProgress(100);
            }
            // correct position for data still in buffer
            long buffered = outputController.getBufferSize()
                - outputController.writeSpaceAvailable();
            long position = playPosition - buffered;
            // if rewind has already started then playPosition is invalid
            if (playPosition <= startByteOffset && buffered > 0)
            {
                //System.out.println("still " + playPosition + " " + buffered);
                position = stopByteOffset - buffered;
            }
            listener.playPosition(bytesToMs(position), 
                                  bytesToMs(totalLength));
        }
    }
    public synchronized void setListener(AudioPlayListener listener)
    {
        this.listener = listener;
    }
    protected void showInitialisationProgress()
    {
        if (listener != null)
        {
            long buffered = outputController.getBufferSize()
                - outputController.writeSpaceAvailable();
            // play hasn't finished yet, so don't show init progress yet
            if (buffered > 0) 
            {
                showPlayPosition();
            }
            else
            {
                int progress = 0; 
                if (seekTarget == seekStart) progress = 100;
                else
                {
                    progress = (int)(100 * (playPosition - seekStart) / 
                        (seekTarget - seekStart));
                }
                listener.initialisationProgress(progress);
            }
        }
    }
    
    protected String stateToString(int s)
    {
        String stateName = "Unknown";
        switch (s)
        {
            case PAUSED:
                stateName = "PAUSED";
                break;
            case SEEKING:
                stateName = "SEEKING";
                break;
            case PLAYING:
                stateName = "PLAYING";
                break;
            case PLAY_AFTER_SEEK:
                stateName = "PLAY_AFTER_SEEK";
                break;
            case SEEK_TO_START:
                stateName = "SEEK_TO_START";
                break;
            case PLAY_FROM_START:
                stateName = "PLAY_FROM_START";
                break;
        }
        return stateName;
    }
    
}
