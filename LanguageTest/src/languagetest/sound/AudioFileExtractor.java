/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/sound/AudioFileExtractor.java,v $
 *  Version:       $Revision: 1.2 $
 *  Last Modified: $Date: 2004/03/24 04:50:30 $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2004 Keith Stribley <jungleglacier@snc.co.uk>
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

package languagetest.sound;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.SwingUtilities;
import org.tritonus.share.sampled.AudioUtils;
/**
 *
 * @author  keith
 */
public class AudioFileExtractor implements Runnable
{
    private ExtractionParameters parameters = null;
    private AudioInputStream interimIS = null;
    private AudioInputStream oldIS = null;
    private Thread extractionThread = null;
    private boolean run = true;
    private final AudioFileExtractor.Listener listener;
    /** Creates a new instance of AudioFileExtractor */
    public AudioFileExtractor(AudioFileExtractor.Listener listener) 
    {
        this.listener = listener;
    }
    
    
    
     /** Extracts a section of audio data from one file and stores it in another.
     * @param oldFile Old audiofile
     * @param newFile file name for extracted data
     * @param startOffset millisec offset of start of data to extract
     * @param endOffset millisec offset of end of data to extract
     * @return length in millisec of data extracted
     */    
    synchronized public void extractFile(File oldFile, File newFile, long startOffset, long endOffset)
        throws PreviousExtractionNotFinishedException, UnsupportedAudioFileException,
            IOException
    {
        if (parameters != null) throw new PreviousExtractionNotFinishedException();
        
        oldIS = AudioSystem.getAudioInputStream(oldFile);
        AudioFormat.Encoding newEnc = Recorder.encodingFromExtension(newFile);
        // go via PCM since it has well defined frame rates etc
        if (oldIS.getFormat().getEncoding() == AudioFormat.Encoding.PCM_SIGNED)
        {
            interimIS = oldIS;
        }
        else if (AudioSystem.isConversionSupported
                (AudioFormat.Encoding.PCM_SIGNED, oldIS.getFormat()))
        {
            interimIS = AudioSystem.getAudioInputStream
                (AudioFormat.Encoding.PCM_SIGNED, oldIS);
        }
        else
        {
            throw new UnsupportedAudioFileException("Cannot convert " + 
                oldIS.getFormat().getEncoding() + " to " + newEnc);
        }
        
        long startByteOffset = 
            AudioUtils.millis2BytesFrameAligned(startOffset, interimIS.getFormat());
        long byteLength = AudioSystem.NOT_SPECIFIED;
        if (endOffset > 0)
        {
            byteLength = -startByteOffset +
                AudioUtils.millis2BytesFrameAligned(endOffset, interimIS.getFormat());
        }
        
        parameters = 
            new AudioFileExtractor.ExtractionParameters(newFile, oldFile, 
                startByteOffset, byteLength);
        if (extractionThread == null)
        {
            extractionThread = new Thread(this);
            extractionThread.start();
        }
        
    }
    
    public void close()
    {
        run = false;
    }
    
    public void run()
    {
        while (run)
        {
            if (parameters == null)
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    
                }
                continue;
            }
            
            try
            {
                // seek to start of stream
                byte [] readBuffer = new byte[1024];
                long readPosition = 0;
                File tempAudioFile = File.createTempFile("LangTestRec",".raw");
                tempAudioFile.deleteOnExit();
                OutputStream outputStream = 
                    new BufferedOutputStream(new FileOutputStream(tempAudioFile));
                long newLength = 0;
                long read = 0;
                int nullReadCount = 0;
                while (read > -1)
                {
                    long length = readBuffer.length;
                    // split read over start boundary to making writing easy
                    if (readPosition < parameters.getStartByteOffset() && 
                        readPosition + length > parameters.getStartByteOffset())
                    {
                        length = (int)(parameters.getStartByteOffset() - readPosition);
                    }
                    
                    if (/*interimIS.markSupported() && */
                        readPosition < parameters.getStartByteOffset())
                    {
                        read = interimIS.skip(length);
                    }
                    else
                    {
                       
                        read = interimIS.read(readBuffer, 0, (int)length);
                    }
                    if (read > -1)
                    {
                        if (readPosition >= parameters.getStartByteOffset())
                        {
                            // write out to buffer
                            outputStream.write(readBuffer, 0, (int)read);
                            newLength += read;
                        }
                        readPosition += read;
                        if (read > 0)
                        {
                            nullReadCount = 0; // reset null counter
                            final int progress = (int)
                                ((100 * (float)readPosition) /
                                 (float)(parameters.getStartByteOffset() + 
                                 parameters.getByteLength()));
                            SwingUtilities.invokeLater(new Runnable()
                                {
                                    public void run()
                                    {
                                        listener.showProgress(progress);
                                    }
                                });
                        }
                        else
                        {
                            nullReadCount++;
                            if (nullReadCount > 1000)
                            {
                                System.out.println("AudioFileExtractor empty reads aborting");
                                break;
                            }
                            try
                            {
                                Thread.sleep(10);
                            }
                            catch (InterruptedException e) {};
                        }
                    }
                    if (parameters.getByteLength() > 0 && 
                        readPosition >= parameters.getStartByteOffset() + 
                        parameters.getByteLength())
                    {
                        break; // finished
                    }
                }
                
                SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            listener.showProgress(-1);
                        }
                    });
                // pad if newLength not whole number of frames
                    
                // the boundary may have been bigger than the file, but the 
                // current readPosition must be the smaller of the real data 
                // length and the end position requested
                parameters.setByteLength(newLength);
                long frameLength = newLength / interimIS.getFormat().getFrameSize();
                long frameAlignedByteLength = 
                    frameLength * interimIS.getFormat().getFrameSize();
                if (frameAlignedByteLength != newLength)
                {
                    // padd last frame to make it complete
                    frameAlignedByteLength += interimIS.getFormat().getFrameSize();
                    frameLength++;
                    while (frameAlignedByteLength > newLength)
                    {
                        outputStream.write('\0');
                        newLength++;
                    }
                }
                // now create a new AudioInputSteam from the old truncated as 
                // required
                outputStream.close();
                if (interimIS != oldIS)
                {
                    interimIS.close();
                }
                oldIS.close();
                InputStream rawIS = 
                    new BufferedInputStream(new FileInputStream(tempAudioFile));
                AudioInputStream extractedIS = 
                    new AudioInputStream(rawIS, interimIS.getFormat(), 
                        frameLength);
                // finally convert to new output stream
                AudioInputStream translatedIS = 
                    AudioSystem.getAudioInputStream( 
                        Recorder.encodingFromExtension(parameters.getNewFile()),
                        extractedIS);
                OutputStream oStream =
                    new NullStripOutputStream(new FileOutputStream(parameters.getNewFile()));
                int wrote = 
                    AudioSystem.write(translatedIS, 
                        Recorder.fileFormatFromExtension(parameters.getNewFile()),
                        oStream);
                rawIS.close();
                extractedIS.close();
                translatedIS.close();
                oStream.close();
                SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            listener.showProgress(100);
                        }
                    });
                System.out.println("Extracted " + wrote + " uncompressed: " + 
                                       parameters.getByteLength()); 
                // calculate extracted length
                final long wroteMs = 
                    AudioUtils.bytes2Millis(parameters.getByteLength(), 
                                            extractedIS.getFormat());

                // now close the various is
                translatedIS = null;
                rawIS = null;
                extractedIS = null;
                oldIS = null;
                interimIS = null;                
                // finished with temporary file so delete it now
                tempAudioFile.delete();
                // notify gui of finish
                final File extractedFile = parameters.getNewFile();
                // delete parameters before telling gui to extract next one
                synchronized (this)                
                {
                    parameters = null;
                }

                SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            listener.extractionFinished(extractedFile, wroteMs);
                        }
                    }
                );
            }
            catch (FileNotFoundException e)
            {
                synchronized (this)
                {
                    parameters = null;
                }
                System.out.println(e.getMessage());
                final File extractedFile = parameters.getNewFile();
                final String message = "File not found.";
                SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            listener.extractionFailed(extractedFile, 
                                message);
                        }
                    });
            }
            catch (IOException e)
            {
                synchronized (this)
                {
                    parameters = null;
                }
                System.out.println(e.getMessage());
                final File extractedFile = parameters.getNewFile();
                final String message = e.getLocalizedMessage();
                SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            listener.extractionFailed(extractedFile, 
                                message);
                        }
                    });
            }
            
        }
        
    }
    
    class ExtractionParameters
    {
        private File oldFile;
        private File newFile;
        private long startByteOffset;
        private long byteLength;
        //private long extractedLength = AudioSystem.NOT_SPECIFIED;
        ExtractionParameters(File newFile, File oldFile, 
                             long startByteOffset, long byteLength)
        {
            this.oldFile = oldFile;
            this.newFile = newFile;
            this.startByteOffset = startByteOffset;
            this.byteLength = byteLength;
        }
        public File getOldFile() { return oldFile; }
        public File getNewFile() { return newFile; }
        public long getStartByteOffset() { return startByteOffset; }
        public long getByteLength() { return byteLength; }
        //public void setExtractedLength(long length) { extractedLength = length;}
        public void setByteLength(long newLength) { byteLength = newLength; }
    }
    public class PreviousExtractionNotFinishedException 
        extends java.lang.Exception
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 8945418933344598071L;

		PreviousExtractionNotFinishedException()
        {
            super();
        }
    }
    public interface Listener 
    {
        void showProgress(int percent);
        void extractionFinished(File extractedFile, long msLengthExtracted);
        void extractionFailed(File extractedFile, String reason);
    }
}
