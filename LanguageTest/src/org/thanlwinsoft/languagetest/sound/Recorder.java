/*
 *  Copyright (C) 2004 Keith Stribley <tech@thanlwinsoft.org>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 *  MA 02110-1301 USA
 */

package org.thanlwinsoft.languagetest.sound;

import java.util.Vector;
import java.util.Iterator;
import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
//import org.tritonus.sampled.convert.javalayer.MpegFormatConversionProvider;
import org.tritonus.share.sampled.AudioFileTypes;

/**
 *
 * @author  keith
 */
public class Recorder implements Runnable
{
    private final int READ_BUFFER_SIZE = 176400;  // one second at 44100Hz
    private final int INPUT_BUFFER_SIZE = 1764000; // 10 seconds
    private File outputFile = null;
    private OutputStream outputStream = null;
    private TargetDataLine targetLine = null;
    private AudioInputStream inputStream = null;
    private AudioFormat rawFormat = null;
    private File targetFile = null;
    private AudioFormat.Encoding targetEncoding = null;
    private AudioFileFormat.Type targetFileFormat = null;
    private boolean hasFinished = false;
    private boolean stop = false;
    private int dumpLevel = 0;
    private long lengthInFrames = -1;
    private long lengthInMs = -1;
    private boolean initialised = false;
    private boolean recordError = false;
    private StringBuffer errorDescription = null;
    private LineController lineController = null;
    private Vector listeners = null;
    private AudioFileFormat.Type vorbisFileFormat = null;
    /** Creates a new instance of Recorder */
    public Recorder(LineController lineController)
    {
        this.lineController = lineController;
        errorDescription = new StringBuffer();
        rawFormat = lineController.getRecLineFormat();
        listeners = new Vector();
    }
    
    public synchronized boolean isFinished() { return hasFinished; }
    public synchronized boolean isInitialised() { return initialised; }
    public synchronized boolean isRecordError() { return recordError; }
    public synchronized long getRecordingMsLength() { return lengthInMs; }
    public synchronized String getErrorDescription() 
    { 
        return errorDescription.toString(); 
    }
    public AudioFileFormat.Type getVorbisFileFormat() { return vorbisFileFormat; }
    public boolean initialise(File targetFile)
    {
        boolean formatOk = false;
        synchronized (this)
        {
            // return false if already initialised
            if (initialised) 
            {
                recordException("Recorder not finished previous session");
                return false;
            }
            hasFinished = false;
            stop = false;
        }
        // intialise raw format if it isn't already
        if (rawFormat == null) rawFormat = lineController.getRecLineFormat();
        // check format is still not null otherwise we can't proceed
        if (rawFormat == null)
        {
            errorDescription.delete(0, errorDescription.length());
            errorDescription.append(lineController.getError());
            return false;
        }
        
        targetEncoding = encodingFromExtension(targetFile);
        targetFileFormat = fileFormatFromExtension(targetFile);
        if (targetEncoding != null && targetFileFormat != null) 
        {
            formatOk = true;
        }
//        else if (targetFile.getName().toLowerCase().endsWith(".ogg"))
//        {   
//            targetEncoding = org.tritonus.share.sampled.Encodings.getEncoding("VORBIS")
//            targetFileFormat = AudioFileFormat.getType("VORBIS","ogg");
//            formatOk = true;
//        }
        if (formatOk)
        {
            if (!AudioSystem.isConversionSupported(targetEncoding, 
                rawFormat))
            {
                formatOk = false;
            }
            else if (!AudioSystem.isFileTypeSupported(targetFileFormat))
            {
                formatOk = false;
            }
        }
        if (formatOk)
        {
            this.targetFile = targetFile;
            new Thread(this).start();
            // reset error index
            recordError = false;
            errorDescription.delete(0, errorDescription.length());
        }
        
        return formatOk;
    }
    
    public static AudioFormat.Encoding encodingFromExtension(File file)
    {
        AudioFormat.Encoding encoding = null;
        if (file.getName().toLowerCase().endsWith(".mp3"))
        {
//            encoding = MpegFormatConversionProvider.MPEG1L3;            
        }
        else if (file.getName().toLowerCase().endsWith(".wav"))
        {
            encoding = AudioFormat.Encoding.PCM_SIGNED;
        }
        else if (file.getName().toLowerCase().endsWith(".au"))
        {   
            encoding = AudioFormat.Encoding.PCM_SIGNED;
        }
        return encoding;
    }
    public static AudioFileFormat.Type fileFormatFromExtension(File file)
    {
        AudioFileFormat.Type fileFormat = null;
        if (file.getName().toLowerCase().endsWith(".mp3"))
        {
            fileFormat = AudioFileTypes.getType("MP3", "mp3");
        }
        else if (file.getName().toLowerCase().endsWith(".wav"))
        {
            fileFormat = AudioFileFormat.Type.WAVE;
        }
        else if (file.getName().toLowerCase().endsWith(".au"))
        {   
            fileFormat = AudioFileFormat.Type.AU;
        }
        return fileFormat;
    }
        
    protected synchronized void recordException(String message)
    {
        recordError = true;
        errorDescription.append(message);
    }
    
    protected void reinit(File outFile) 
        throws LineUnavailableException, IllegalArgumentException,
            SecurityException, FileNotFoundException        
    {
        this.outputFile = outFile;
        
        if (targetLine == null)
        {
            if (!lineController.linesAreAvailable())
                lineController.openLines();
            if (lineController.linesAreAvailable())
            {
                rawFormat = lineController.getRecLineFormat();
                targetLine = lineController.getTargetDataLine();
            }
            else
            {
                errorDescription.append("Unable to get line for recording");
            }
        }
        if (targetLine != null)
        {
            targetLine.open(rawFormat, INPUT_BUFFER_SIZE);
            outputStream = 
                new BufferedOutputStream(new FileOutputStream(outFile));
            inputStream = new AudioInputStream(targetLine);
            synchronized (this)
            {
                initialised = true;
            }    
        }
    }
    
    public void addPlayListener(AudioPlayListener listener)
    {
        listeners.add(listener);
    }
    
    public void run()
    {
        try
        {
            File tempAudioFile = File.createTempFile("LangTestRec",".raw");
            tempAudioFile.deleteOnExit();
            reinit(tempAudioFile);
            
            int totalWritten = 0;
            int read = 0;
            // check that initalisation was successful
            if (!initialised) 
            {
                read = -1;
                System.out.println("Recorder initialisation failed");
            }
            else System.out.println("Line open");
            while (read > -1)
            {
                byte [] readBuffer = new byte[READ_BUFFER_SIZE];
                int targetRead = inputStream.available();
                if (targetRead > readBuffer.length) 
                {
                    targetRead = readBuffer.length;
                }
                if (targetRead > 0)
                {
                    read = inputStream.read(readBuffer, 0, targetRead);
                }
                else
                {
                    read = 0;
                }
                if (read > 0)
                {
                    outputStream.write(readBuffer, 0, read);
                    totalWritten += read; 
                    playPosition(totalWritten);
                }
                else
                {
                    try
                    {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e)
                    {

                    }
                }
                synchronized (this)
                {
                    if (stop) break;
                }
//                wrote = AudioSystem.write(inputStream, fileFormat, outputStream);
//                System.out.println("Wrote " + wrote + " bytes " 
//                   + inputStream.getFrameLength() + " frames");
            } 
            lengthInFrames = -1;
            if (inputStream != null && inputStream.getFormat() != null)
            {
                lengthInFrames = 
                    totalWritten / inputStream.getFormat().getFrameSize();
                lengthInMs = (long)(((float)lengthInFrames * 1000) / 
                                inputStream.getFormat().getFrameRate());
            }
            System.out.println("Recorded " + lengthInFrames + " frames");
            if (targetLine != null)
            {
                targetLine.stop();
                targetLine.flush();
                targetLine.close();
            }
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            convert(targetFile, targetEncoding, targetFileFormat);
            inputStream = null;
            outputStream = null;
            targetFileFormat = null;
        }
        catch (LineUnavailableException e)
        {
            System.out.println(e.getMessage());
            recordException(e.getLocalizedMessage());
        }
        catch (SecurityException e)
        {
            System.out.println(e.getMessage());
            recordException(e.getLocalizedMessage());
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
            recordException(e.getLocalizedMessage());
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
            recordException(e.getLocalizedMessage());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            recordException(e.getLocalizedMessage());
        }
        synchronized (this) 
        { 
            hasFinished = true; 
            initialised = false;
        }
    }    
    
    public synchronized void stop()
    {
        if (targetLine != null)
        {
            targetLine.stop();
            System.out.println("stop");
        }
    }
    
    public synchronized void start()
    {
        if (targetLine != null) 
        {
            targetLine.start();
            System.out.println("start");
        }
    }
    
    public synchronized void finish()
    {
        stop = true;
    }
    
    public void close()
    {
        if (targetLine != null)
        {
            synchronized (this)
            {
                if (!hasFinished)
                {
                    // if line still running close it, but wait for it to
                    // finish
                    stop = true;
                    targetLine.close();
                }
                else
                {                    
                    targetLine.close();
                    targetLine = null;
                }
            }
        }
    }
    
    protected boolean convert(File newFile, AudioFormat.Encoding encoding,
                        AudioFileFormat.Type newFileFormat)
    {
        boolean success = false;
        try
        {
            InputStream iStream = 
                new BufferedInputStream(new FileInputStream(outputFile));
            AudioInputStream ieStream = null;
            AudioInputStream ilStream = new AudioInputStream(iStream, 
                rawFormat, 
                lengthInFrames);
//            AudioFormat.Encoding [] encodings = 
//                AudioSystem.getTargetEncodings(ilStream.getFormat());     
//            for (int i = 0; i<encodings.length; i++)
//                System.out.println(encodings[i].toString());
                        
            if (ilStream.getFormat().getEncoding() !=
                encoding)
            {
                ieStream = AudioSystem.getAudioInputStream(encoding, ilStream);
            }
            else
            {
                ieStream = ilStream;
            }
//            // check supported formats 
//            AudioFileFormat.Type [] supportedFormats = 
//                AudioSystem.getAudioFileTypes(ieStream);
//            for (int i = 0; i<supportedFormats.length; i++)
//            {
//                System.out.println(supportedFormats[i].toString());
//            }
            OutputStream oStream = 
                new NullStripOutputStream(new FileOutputStream(newFile));
            int wrote = AudioSystem.write(ieStream, newFileFormat, oStream);
            iStream.close();
            ilStream.close();
            ieStream.close();
            oStream.close();
            success = true;
            System.out.println("Converted " + wrote + " bytes "
                + ieStream.getFrameLength() + " frames");
            outputFile.delete();
            outputFile = null;
        }
//        catch (UnsupportedAudioFileException e)
//        {
//            System.out.println(e.getMessage());
//        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
            recordException(e.getLocalizedMessage());
            dumpAudioFileFormats();
            System.out.println(encoding);
            System.out.println(rawFormat);
            System.out.println(newFileFormat);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            recordException(e.getLocalizedMessage());
        }
        return success;
    }
 
    
    public boolean convert(File wavFile, File newFile, AudioFormat.Encoding encoding,
                        AudioFileFormat.Type newFileFormat)
    {
        boolean success = false;
        try
        {
            
            AudioInputStream ieStream = null;
            AudioInputStream ilStream = AudioSystem.getAudioInputStream(wavFile);
            AudioFormat.Encoding [] encodings = 
                AudioSystem.getTargetEncodings(ilStream.getFormat());     
            for (int i = 0; i<encodings.length; i++)
                System.out.println(encodings[i].toString());
            dumpAudioFileFormats(ilStream);            
            if (!ilStream.getFormat().getEncoding().equals(encoding))
            {
                ieStream = AudioSystem.getAudioInputStream(encoding, ilStream);
                // check supported formats 
                dumpAudioFileFormats(ieStream);
            }
            else
            {
                ieStream = ilStream;
            }
            OutputStream oStream = new NullStripOutputStream(new FileOutputStream(newFile));
            int wrote = AudioSystem.write(ieStream, newFileFormat, oStream);
            ilStream.close();
            ieStream.close();
            oStream.close();
            success = true;
            System.out.println("Converted " + wrote + " bytes "
                + ieStream.getFrameLength() + " frames");
            
        }
        catch (UnsupportedAudioFileException e)
        {
            System.out.println(e.getMessage());
            recordException(e.getLocalizedMessage());
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
            recordException(e.getLocalizedMessage());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            recordException(e.getLocalizedMessage());
        }
        return success;
    }
 
    
    public void dumpAudioFileFormats()
    {
        AudioFileFormat.Type [] supportedFormats = 
                AudioSystem.getAudioFileTypes();
        for (int i = 0; i<supportedFormats.length; i++)
        {
            if (supportedFormats[i].toString().equals("Vorbis"))
            {
                vorbisFileFormat = supportedFormats[i];
            }
            System.out.println("FileFormat:" + supportedFormats[i].toString());
        }       
    }
    
    public void dumpAudioFileFormats(AudioInputStream stream)
    {
        AudioFileFormat.Type [] supportedFormats = 
                AudioSystem.getAudioFileTypes(stream);
        for (int i = 0; i<supportedFormats.length; i++)
        {
            System.out.println("FileFormat:" + supportedFormats[i].toString());
        }       
    }
    
    public void dumpAudioEncodings(AudioFormat sourceFormat)
    {
        AudioFormat.Encoding [] supportedFormats = 
                AudioSystem.getTargetEncodings(sourceFormat);
        for (int i = 0; i<supportedFormats.length; i++)
        {
            System.out.println("TargetEncoding:" + supportedFormats[i].toString() +
                " for " + sourceFormat.toString());
            dumpAudioFormats(supportedFormats[i], sourceFormat);
        }       
    }
    
    public void dumpAudioFormats(AudioFormat.Encoding encoding, 
        AudioFormat sourceFormat)
    {
        String spaces = "    ";
        dumpLevel++;
        AudioFormat [] supportedFormats = 
                AudioSystem.getTargetFormats(encoding, sourceFormat);
        for (int i = 0; i<supportedFormats.length; i++)
        {
            System.out.println(spaces.substring(0,dumpLevel) + 
                " TargetFormat:" + supportedFormats[i].toString());
            if (dumpLevel < 2)
            {
                dumpAudioEncodings(supportedFormats[i]);
            }
        }       
        dumpLevel--;
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        LineController lineController = new LineController(LineController.REC_MODE);
        lineController.run();
        if (!lineController.openLines())
        {
            System.out.println("Failed to open lines");
            System.exit(2);
        }
        Recorder recorder = new Recorder(lineController);
        recorder.dumpAudioFileFormats();
        while (!recorder.initialise(new File("sample.wav")))
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {

            }
        }
        while (!recorder.isInitialised() && !recorder.isRecordError())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {

            }
        }
        if (recorder.isRecordError())
            System.exit(1);
        recorder.start();
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            
        }
        recorder.stop();
        recorder.finish();
        while (!recorder.isFinished())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                
            }
        }
        //recorder.convert(new File("sample.wav"), new File("sample.mp3"), 
        //    MpegFormatConversionProvider.MPEG1L3,  
        //    AudioFileTypes.getType("MP3", "mp3"));
//        recorder.convert(new File("sample.wav"), new File("sample.ogg"), 
//            AudioFormat.Encoding.PCM_SIGNED,  
//            recorder.getVorbisFileFormat());
//        recorder.convertA2B(new File("sample.mp3"), 
//            MpegFormatConversionProvider.MPEG1L3, 
//            AudioFileTypes.getType("MP3", "mp3"));
        
//        recorder.convert(new File("sample.ogg"), 
//             org.tritonus.share.sampled.Encodings.getEncoding("VORBIS"), 
//             AudioSystem.getAudioFileTypes()[4]);
        recorder = null;
        lineController.closeLines();
        System.out.println("Recorder finished");
        System.exit(0);
    }
    
    /** Method calls the playPosition method on each registered 
     * AudioPlayListener.
     */
    public synchronized void playPosition(int bytes)
    {
        long position = (long)(1000.0 * (double)bytes / 
            (double)(rawFormat.getFrameSize() * rawFormat.getFrameRate()));
        Iterator l = listeners.iterator();
        while (l.hasNext())
        {
            ((AudioPlayListener)l.next())
                .playPosition(position, -1);
        }
    }
}
