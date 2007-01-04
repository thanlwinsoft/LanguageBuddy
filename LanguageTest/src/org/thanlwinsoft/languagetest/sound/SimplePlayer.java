/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/sound/SimplePlayer.java,v $
 *  Version:       $Revision: 704 $
 *  Last Modified: $Date: 2007-01-05 05:50:38 +0700 (Fri, 05 Jan 2007) $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2003 Keith Stribley <jungleglacier@snc.co.uk>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * -----------------------------------------------------------------------
 */

package org.thanlwinsoft.languagetest.sound;

import java.io.*;
import java.util.Vector;
import javax.sound.sampled.*;
import java.util.Iterator;
/** Class to play audio files with ability to pause and move the play position.
 * The play can optionally be restricted to a time window within the file.
 * @author keith
 */
public class SimplePlayer implements AudioPlayer, Runnable, LineListener
{
    private static int BUFFER_SIZE = 8820;       //44100 kbit/s
    //private static int SKIP_BUFFER_SIZE = 1048576;       //44100 kbit/s
    private static int SOURCE_LINE_BUFFER = 44100;
    private static int MARK_INVALID_SIZE = 16777216;
    private float framesPerSec = 44100; // set properly later
    private int bytesPerFrame = 4;// set properly later
    private int bytesPerSec = 176400;
    private File currentFile = null;
    private SourceDataLine outputLine = null;
    private AudioInputStream input = null;
    final static int UNINITIALISED_MODE = 0;
    final static int PLAYING_MODE = 1;
    final static int PAUSED_MODE = 2;
    final static int STOPED_MODE = 3;
    final static int CLOSE_MODE = 4;
    private int mode = UNINITIALISED_MODE;
    private long seekBytes = 0;
    private long startByteOffset = 0;
    private long stopByteOffset = -1;
    private long offset = 0;
    private long markOffset  = 0;
    private Thread playThread = null;
    private Vector listeners = null;
    private long totalLength = -1;
    private boolean initialised = false;
    private boolean boundsChanged = false;
    /** Creates a new instance of SimplePlayer */
    public SimplePlayer() 
    {
        listeners = new Vector();
    }
    
    /** Stops any file currently playing and opens the new audio file ready for 
     * play back.
     * @param audioFile File to open
     * @return Returns true if file openned successfully for play back.
     */    
    public boolean open(File file, long msLengthHint, boolean forceReopen)
    {
        if (currentFile != null)
        {
            // clip already loaded
            if (!forceReopen && file.compareTo(currentFile)==0) 
            {
                // stop play ready for a setBounds command
                setMode(STOPED_MODE);
                return true;
            }
            else 
            {
                if (getMode() == CLOSE_MODE)
                {
                    // previous mode has still not stopped 
                    return false; // ignore request
                }
                else if (!stopThread()) return false; // unload previous clip
            }
        }
        try 
        {
            openInputStream(file);
            
            bytesPerFrame = input.getFormat().getFrameSize();
            framesPerSec = input.getFormat().getFrameRate();
            bytesPerSec = bytesPerFrame * (int)framesPerSec;
            System.out.println("Frame size: " + bytesPerFrame +
                               " Frame Rate: " + framesPerSec);
            // reset offsets
            offset = 0;
            startByteOffset = 0;
            if (input.getFrameLength() > 0)
            {
                stopByteOffset = input.getFrameLength() * bytesPerFrame;
                totalLength = stopByteOffset;
            }
            else
            {
                stopByteOffset = -1;
                totalLength = -1;
            }
            System.out.println("Input file length = " + bytesToMs(totalLength));
            //input obtained, now mark start so we can go back to it
            markOffset = 0;
            if (input.markSupported())
            {
                input.mark(MARK_INVALID_SIZE);
            }
           
        }
        catch (UnsupportedAudioFileException uafe)
        {
            System.out.println(uafe);
            return false;
        }
        catch (IOException ioe)
        {
            System.out.println(ioe);
            return false;
        }
        currentFile = file;
       
        //System.out.println("FrameLength = " + input.getFrameLength());
        setInitialised(0);
        setMode(UNINITIALISED_MODE);
        // spawn thread to do rest of initialisation which may block
        playThread = new Thread(this);
        playThread.start();
        return true;
    }
        
    protected void openInputStream(File audioFile) 
        throws IOException, UnsupportedAudioFileException
    {
        input = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat audioFormat = input.getFormat();
        System.out.println( "Play input audio format=" +  audioFormat);
        System.out.println("Frame length=" + input.getFrameLength());
//              Convert compressed audio data to uncompressed PCM format.
        if ( audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED )
        {
            AudioFormat newFormat = 
                new AudioFormat( AudioFormat.Encoding.PCM_SIGNED,
                                 audioFormat.getSampleRate(),
                                 16,
                                 audioFormat.getChannels(),
                                 audioFormat.getChannels() * 2,
                                 audioFormat.getSampleRate(),
                                 false );
            System.out.println( "Converting audio format to " + newFormat );
            AudioInputStream newStream = AudioSystem.getAudioInputStream( newFormat, input );
            audioFormat = newFormat;
            input = newStream;
        }
    }
   
    /** Called when play button pressed on GUI. If the player is stoped it will cause
     * play to start.
     */    
    public void play()
    {
        if (getMode() != CLOSE_MODE)
        {
            setMode(PLAYING_MODE);
        }
    }
    
    /** Method to close the audio file and playback lines.
     * @return Returns true if file and lines closed successfully.
     */    
    public boolean close()
    {
        setMode(CLOSE_MODE);
        // check whether there is a thread to close
        if (playThread != null) 
        {    
            // wait for play thread to close
            int waitCount = 0;
            while (playThread.isAlive())
            {
                sleep();
                waitCount++;
                // prevent infinite loop
                if (waitCount > 100)
                {
                    System.out.println("Player thread failed to stop!");
                    return false;
                }
            }
            playThread = null;
        }
        if (outputLine != null) outputLine.close();
        System.out.println("Output Line closed");
        outputLine = null;
        return true;
    }
    
    private boolean stopThread()
    {
        setMode(CLOSE_MODE);
        // check whether there is a thread to close
        if (playThread == null) return true;
        // wait for play thread to close
        int waitCount = 0;
        while (playThread.isAlive())
        {
            sleep();
            waitCount++;
            // prevent infinite loop
            if (waitCount > 100)
            {
                System.out.println("Player thread failed to stop!");
                return false;
            }
        }
        playThread = null;
        return true;
    }
    
    /** Called when fast forward button is pressed on GUI.
     * When play is paused it will advance the play position in the file.
     * @param amount Amount to adance in ms.
     * @return Current play position in ms.
     */    
    public void fastForward(long amount)
    {
        if (getMode() == PAUSED_MODE)
        {
            addSeekBytes(msToBytes(amount));
        }
    }
    
    public void pause()
    {
        switch (getMode())
        {
            case PAUSED_MODE:
                setMode(PLAYING_MODE);
                break;
            case PLAYING_MODE:
                setMode(PAUSED_MODE);
                break;
        }
        
    }
    
    /** Called when rewind button is pressed on GUI.
     * When play is paused it will decurement the play position in the file.
     * @param amount Amount to rewind in ms.
     * @return New play position in ms.
     */  
    public void rewind(long amount)
    {
        if (getMode() == PAUSED_MODE)
        {
            addSeekBytes(- msToBytes(amount));
        }
    }
    
    public long getStartMs() 
    { 
        return bytesToMs(getStartByteOffset()); 
    }
    public long getDurationMs() 
    { 
        return bytesToMs(getStopByteOffset() - getStartByteOffset()); 
    }
    
    public File getCurrentFile()
    {
        return currentFile;
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
                if (getMode() == PAUSED_MODE)
                {
                    // bounds have changed so reset to stop mode
                    setMode(STOPED_MODE);
                }
                // record change so that current play state doesn't get lost
                // between threads
                boundsChanged = true;
            }
        }
    }
    
    public void stop()
    {
        if (getMode() != CLOSE_MODE)
        {
            setMode(STOPED_MODE);
        }
    }
    
    /**
     * This is the thread that actually plays the data.
     * To avoid threading problems and avoid hanging the GUI all manipulation
     * of the audio output line is controlled in this thread. No calls to outputLine
     * should be made in any methods not confined to this thread.
     * The variables startOffsetByte, stopOffsetByte, offset and mode are all
     * modified in both this thread and the GUI thread. Hence synchronzed 
     * getters and setters are used to access them.
     */
    public void run() 
    {
        System.out.println("Player thread spawned");
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, 
                                               input.getFormat()); // format is an AudioFormat object
        int oldMode = UNINITIALISED_MODE; // for debugging
        if (!AudioSystem.isLineSupported(info)) 
        {
            // Handle the error.
            setMode(CLOSE_MODE);
            System.out.println("Audio Line not supported");
        }
        else
        {
            // Obtain and open the line.
            try 
            {
                // these methods may block hence they are in player thread not in
                // open method called by gui
                // trigger start of counter
                if (outputLine == null || outputLine.isOpen() )
                {
                    setInitialised(0);
                    System.out.println("Openning Line...");
                    outputLine = (SourceDataLine) AudioSystem.getLine(info);
                    outputLine.open(input.getFormat(), SOURCE_LINE_BUFFER);
                    showPlayPosition();
                    System.out.println("Audio connection openned");
                }
                // while line was being openned start and stop may have been set
                setInitialised(1);
                seekToStart();
                setInitialised(100);
            } 
            catch (LineUnavailableException ex) 
            {
                    // Handle the error.
                //... 
                System.out.println(ex);
                setMode(CLOSE_MODE);
            }
        }
        byte buffer[] = new byte[BUFFER_SIZE];
        do
        {
            if (getMode() != oldMode)
            {
                oldMode = getMode();
                System.out.println("Mode change to " + oldMode);
            }
            // This is the state machine of the player
            switch (getMode())
            {
                
                case PLAYING_MODE:
                    try
                    {
                        // check that current position hasn't been left before
                        // the start
                        if (isBeforeStart())
                        {
                            // skip to start before sending any more data
                            setInitialised(1);
                            seekToStart();
                            setInitialised(100);
                        }
                        // We don't want to block on the write
                        // since if we then stop the line
                        // the thread will block indefinitely
                        // Therefore see how empty the buffer is
                        // and read data in accordingly
                        int bufferSpace = outputLine.available();
                        if (bufferSpace > BUFFER_SIZE) 
                        {
                            bufferSpace = BUFFER_SIZE;
                        }
                        // round to nearest frame
                        bufferSpace = roundOffsetToFrame(bufferSpace);

                        // if buffer isn't already topped up
                        if (bufferSpace > 0)
                        {
                            //System.out.println("buffer space ="+ bufferSpace);
                            int bytesRead = input.read(buffer,0,bufferSpace);
                            if (bytesRead > 0)
                            {
                                bytesRead = roundOffsetToFrame(bytesRead);

                                int wrote = outputLine.write(buffer, 0, bytesRead);
                                if (wrote != bytesRead)
                                {
                                    System.out.println("Wrote " + wrote + "/" + bytesRead);
                                }
                                incrementOffset(wrote);
                            }
                            else
                            {
                                // no more data
                                System.out.println("Byte length = " + offset);
                                // end of file
                                if (bytesRead == -1)
                                {
                                    synchronized (this)
                                    {
                                        stopByteOffset = offset;
                                    }
                                    System.out.println("Asked for:" + bufferSpace +
                                                       " Got " + bytesRead);
                                }
                            }
                            // now that there is at least some data in the
                            // buffer start the line
                            // check if end has been reached
                            if (hasReachedEnd())
                            {
                                /*
                                // for a very short clip buffer might not have 
                                // been filled so in that case start play now
                                if (!outputLine.isRunning()) outputLine.start();
                                // wait for line to play out data in buffer
                                // before resetting
                                do
                                {
                                    sleep();
                                    showPlayPosition();
                                } while (outputLine.isActive());
                                 */
                                // if bounds have changed don't set stop mode
                                // since the next play may already have been 
                                // called
                                synchronized (this)
                                {
                                    if (!boundsChanged)
                                    {   
                                        setMode(STOPED_MODE);
                                    }
                                }
                                seekToStart();
                                setInitialised(100);
                            }
                        }
                        // otherwise buffer is full
                        else
                        {
                            // wait for some data to be used up
                            sleep();
                        }
                        // if play not started start it now
                        if (!outputLine.isRunning())
                        {
                            // in this case play hasn't started so now there is 
                            // a buffer's worth of data start play
                            outputLine.start();
                                // allow to take effect
                            sleep();
                            
                            
                        }
                        showPlayPosition();
                    }
                    catch (IOException ie)
                    {
                        System.out.println(ie);
                        setMode(CLOSE_MODE);
                    }
                    
                    //sleep();
                    break;
                case PAUSED_MODE:
                    if (outputLine.isRunning()) outputLine.stop();
                    try
                    {
                        long currentSeek = getSeekBytes();
                        if (currentSeek > 0)
                        {

                            if (currentSeek > input.available() ||
                                (getStopByteOffset() > 0 && 
                                 currentSeek > getStopByteOffset()))
                            {                  
                                setMode(STOPED_MODE);             
                                seekToStart();
                            }
                            else
                            {
                                // skip will take initialisation state below 100%
                                skip(currentSeek);         
                            }                            
                            // make sure that initialised is set back to true
                            setInitialised(100); 
                            // position moved so flush data in buffer
                            outputLine.stop();
                            outputLine.flush(); 
                        }
                        else if (currentSeek < 0) // to go back, reset to start and skip forward
                        {
                            long targetOffset = getOffset() - getSeekBytes();
                            seekToStart();                                                       
                            if (getOffset() > getStartByteOffset())
                            {
                                skip(targetOffset); 
                                
                            }
                            // make sure that initialised is set back to true
                            setInitialised(100); 
                            // position moved so flush data in buffer
                            outputLine.stop();
                            outputLine.flush();                            
                        }
                        showPlayPosition();
                    }
                    catch (IOException ie)
                    {
                        System.out.println(ie);
                        setMode(CLOSE_MODE);
                    }
                    sleep();
                    break;
                case STOPED_MODE:
                    // Stopped mode should only be entered if stop is pressed.
                    if (outputLine.isRunning()) 
                    {
                        System.out.println("Stopping");
                        outputLine.stop();
                        outputLine.flush();
                        seekToStart();
                        // make sure that initialised is set back to true
                        setInitialised(100); 
                    }
                    else if (getOffset() != getStartByteOffset())
                    {             
                        outputLine.stop();
                    	outputLine.flush();
                        seekToStart();
                        // make sure that initialised is set back to true
                        setInitialised(100); 
                    }
                    else
                    {
                        sleep();
                    }
                    break;
                case CLOSE_MODE:
                    break;
                default:
                    sleep();
            }
            
        } while (mode != CLOSE_MODE);
        // tidy up
        setInitialised(0);
        if (outputLine != null)
        {
            if (outputLine.isActive())
            {
                outputLine.drain();
                outputLine.stop();
            }
            else
            {
                outputLine.stop();
                outputLine.flush();
            }
        }
        try
        {
            input.close();
            input = null;
            currentFile = null;
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        setMode(UNINITIALISED_MODE);
        System.out.println("Player thread finished");
    }
    
    /** Method calls the playPosition method on each registered 
     * AudioPlayListener.
     * The method takes into account that the current play position will be 
     * behind the current offset value by the amount the amount of unused data
     * in the outputLine buffer.
     */
    protected void showPlayPosition()
    {
        Iterator l = listeners.iterator();
        while (l.hasNext())
        {
            ((AudioPlayListener)l.next())
                .playPosition(bytesToMs(getOffset() - SOURCE_LINE_BUFFER + 
                                                        outputLine.available()), 
                              bytesToMs(totalLength));
        }
    }
    /** 
     * Method to seek to start of play window within the current file.
     * This should only be called from player thread.
     */
    private void seekToStart()
    {
        try
        {
            synchronized (this)
            {
                boundsChanged = false;
            }
            setInitialised(1); // only just started so inform listeners
            do
            {
                // stop start changing during execution
                final long targetStart = getStartByteOffset();
                if (getOffset() > targetStart) // need to rewind in someway
                {
                    if (!input.markSupported() || markOffset > targetStart || 
                        getOffset() >= markOffset + MARK_INVALID_SIZE)
                    {
                        // need to reopen file
                        input.close();
                        input = null;
                        openInputStream(currentFile);
                        markOffset = 0;
                        System.out.println("Reopenning audiofile");
                    }
                    else
                    {
                        input.reset();
                        System.out.println("Resetting audiofile");
                    }
                    setOffset(markOffset);
                }            
                seekBytes = 0;                       
                skip(targetStart);
                synchronized (this)
                {
                    // if start byte offset hasn't been changed set it to current
                    // seek position in case there is a discripancy due to frame
                    // sizes
                    if (targetStart == startByteOffset)
                    {
                        // set to this because may not be same as seek position
                        startByteOffset = getOffset(); 
                        // exit loop
                        break;
                    }
                }
                // check on when to exit loop is inside synchronised block
            } while (getMode() != CLOSE_MODE);
            
            // now reset mark to start
            if (input.markSupported())
            {
                input.mark(MARK_INVALID_SIZE);
                markOffset = getOffset();
            }
            
            showPlayPosition();
        }
        catch (UnsupportedAudioFileException uafe)
        {
            // unlikely unless file has changed on disk
            System.out.println(uafe);
            setMode(CLOSE_MODE);
        }
        catch (IOException ie)
        {
            System.out.println(ie);
            setMode(CLOSE_MODE);
        }
    }
    
    private void skip(long targetOffset) throws IOException
    {
         //byte buffer[] = new byte[SKIP_BUFFER_SIZE];
         int zeroSkips = 0;
         double skipDistance = (double)(targetOffset - getOffset());
         long skipProgress = 0;
         System.out.println("Skipping " + skipDistance + " bytes...");
         //      get within a frame of desired start position
         while (getOffset() <= targetOffset - bytesPerFrame &&
                getMode() != CLOSE_MODE)
         {
             long skipped = 0;
             long targetSkip = targetOffset - getOffset();
             // limit skip to a 1 seconds worth to show progress
             if (targetSkip > bytesPerSec) targetSkip = bytesPerSec;
             skipped = input.skip(roundOffsetToFrame(targetSkip));
             //  if skipped isn't making any progress then break to prevent 
             // infinite loop
             if (skipped <= 0) 
             {
                if (++zeroSkips > 10) 
                {
                    System.out.println("Only skipped " + skipped + " not " + targetSkip);                  
                    return;
                } 
                sleep(); // wait a bit
             }
             else
             {
                 incrementOffset(skipped);
                 skipProgress += skipped;
                 zeroSkips = 0; 
                 setInitialised((int)Math.floor((double)skipProgress * 100.0 /
                                                skipDistance));
             }
         }
         System.out.println("Skipped to " + targetOffset);
    }
    
    private void sleep()
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
    
    synchronized protected void setMode(int newMode)
    {
        // if the mode has been set to close don't allow any transition
        // except to uninitialised
        if (mode == CLOSE_MODE && newMode != UNINITIALISED_MODE) return;
        mode = newMode;
    }
    
    synchronized protected void addSeekBytes(long bytes)
    {
        seekBytes += bytes;
    }
    
    synchronized protected long getSeekBytes()
    {
        long currentSeek = seekBytes;
        seekBytes = 0;
        return currentSeek;
    }
    
    synchronized protected int getMode()
    {
        return mode;
    }
    /**
     * Returns the current position in the input file of data that has not yet
     * been read into the output buffer.
     * At one point offset was modified in both GUI and player threads, but
     * this is no longer the case, so method does not need to be 
     * synchronzed anymore.
     * @return offset from start of audio file in bytes
     */
    protected long getOffset()
    {
        return offset;
    }
	/**
	 * Sets the current position in the input file of data that has not yet
	 * been read into the output buffer.
	 * At one point offset was modified in both GUI and player threads, but
	 * this is no longer the case, so method does not need to be 
	 * synchronzed anymore.
	 * @param newOffset current offset from start of audio file in bytes
	 */
    protected void setOffset(long newOffset)
    {
        offset = newOffset;
        //System.out.println("Play offset: " + newOffset);
    }
    /**
     * Sets the current position in the input file of data that has not yet
	 * been read into the output buffer.
	 * At one point offset was modified in both GUI and player threads, but
	 * this is no longer the case, so method does not need to be 
	 * synchronzed anymore.
     * @param deltaOffset additional bytes to increment offset by
     */
    protected void incrementOffset(long deltaOffset)
    {
    	offset += deltaOffset;
        //System.out.println("Play offset: " + offset);
    }
    /**
     * Gets the offset of the start of the play window within the file. The value
     * of this variable is set in the GUI thread by setBounds(). This method is 
     * synchronized to allow the player thread to access the variable without 
     * problems.
     * @return First byte to be read in play window
     */
    synchronized protected long getStartByteOffset()
    {
    	return startByteOffset;
    }
    
    protected int roundOffsetToFrame(int newOffset)
    {
        return (int)(Math.floor((float)newOffset / 
                            (float)bytesPerFrame)) * bytesPerFrame;
    }
    
    protected long roundOffsetToFrame(long newOffset)
        {
            return (long)(Math.floor((float)newOffset / 
                                (float)bytesPerFrame)) * bytesPerFrame;
        }
    
    protected synchronized boolean hasReachedEnd()
    {
        boolean atEnd = false;
        if (getStopByteOffset() > -1)
        {
            if (offset >= getStopByteOffset()) atEnd = true;
        }
        return atEnd;
    }
    
    protected synchronized boolean isBeforeStart()
    {
        boolean beforeStart = false;
        if (offset < getStartByteOffset() - bytesPerFrame) beforeStart = true;
        return beforeStart;
    }
    
	/**
	 * Gets the offset of the end of the play window within the file. The value
	 * of this variable is set in the GUI thread by setBounds(). This method is 
	 * synchronized to allow the player thread to access the variable without 
	 * problems.
	 * @return First byte to be read in play window
	 */
    synchronized protected long getStopByteOffset()
    {
    	return stopByteOffset;
    }
    
   protected long bytesToMs(long value)
   {
        // treat -1 specially
        if (value == -1)  return value;
        float byteOffset = (float)value;
        float secondsOffset = byteOffset / 
            (float)(bytesPerFrame * framesPerSec);
        return (long)(secondsOffset * 1000);
   }
   
   protected long msToBytes(long value)
   {
        // treat -1 specially
        if (value == -1)  return value;
        float secOffset = ((float)value) / 1000;
        float byteOffset = secOffset * bytesPerFrame * framesPerSec;
        return (long)(byteOffset);
   }
   
   public void addPlayListener(AudioPlayListener listener)
   {
       listeners.add(listener);
   }   
   
   public void update(javax.sound.sampled.LineEvent lineEvent) 
   {
        if (lineEvent.getType() == LineEvent.Type.STOP)
        {
            // if in play mode must have stopped because of lack of data
            // otherwise stop will be due to pause
            if (getMode() == PLAYING_MODE) 
            {
                setMode(STOPED_MODE);
            } 
            System.out.println("Play has stopped");
            showPlayPosition();
        }
   }
   
   public synchronized boolean isInitialised()
   {
       return initialised;
   }
   
   protected synchronized void setInitialised(int percent)
   {
       if ((percent == 100) && (mode == UNINITIALISED_MODE)) 
       {
           mode = STOPED_MODE;
           initialised = true;
       }
       else
       {
           initialised = false;
       }
       Iterator l = listeners.iterator();
       while (l.hasNext())
       {
           ((AudioPlayListener)l.next()).initialisationProgress(percent);
       }
   }
   
   public boolean open(File file)
   {
       return open(file, AudioSystem.NOT_SPECIFIED, false);
   }
   
   public boolean isActive()
   {
       return outputLine.isActive();
   }
   
}
