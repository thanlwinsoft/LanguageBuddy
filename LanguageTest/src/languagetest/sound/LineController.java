/*
 * LineController.java
 *
 * Created on February 16, 2004, 9:01 PM
 */

package languagetest.sound;


import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Line;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import java.util.Vector;

/**
 *
 * @author  keith
 */
public class LineController implements Runnable
{
    private static final float MAX_SAMPLE_RATE = 44100;
    private TargetDataLine targetDataLine = null;
    private SourceDataLine sourceDataLine = null;
    private boolean linesOpen = false;
    // temporary format used when testing for availability
    private AudioFormat testFormat = null; 
    private AudioFormat recFormat = null;
    private AudioFormat playFormat = null;
    private Mixer recMixer = null;
    private Mixer playMixer = null;
    
    private Vector recMixers = null;
    private Vector playMixers = null;
    private float maxSampleRate = AudioSystem.NOT_SPECIFIED;
    private String error = null;
    /** Creates a new instance of LineController */
    public LineController()
    {
        maxSampleRate = MAX_SAMPLE_RATE; // could be configurable in future
        new Thread(this).start();
        recMixers = new Vector();
        playMixers = new Vector();
    }
    
    public Vector getPlayMixers()
    {
        return playMixers;
    }
    
    public Vector getRecMixers()
    {
        return recMixers;
    }
    
    public void run()
    {
        int count = 0;
        DataLine.Info recLineInfo = null;
        DataLine.Info playLineInfo = null;
        while (!linesOpen)
        {
            count++;
            Mixer mixer = null;
            Mixer.Info [] mixers = AudioSystem.getMixerInfo();
            int i = 0; // mixer index
            int playIndex = -1;
            int recIndex = -1;
            if (count < 2)
                System.out.println("Getting info for " + mixers.length + " mixers...");
            // only loop over mixers once, if we already have a play back line
            // skip this step
            for (i=0; (i<mixers.length); i++)
            {
                if (count < 2) System.out.println("\n" + mixers[i].getName() + " (" + 
                        mixers[i].getDescription() + ")");
                mixer = AudioSystem.getMixer(mixers[i]);
                try
                {
                    DataLine.Info testLi = null;
                    // check for play lines
                    Line.Info [] li = mixer.getSourceLineInfo();
                    if ((li != null)&&(sourceDataLine == null))
                    {
                        for (int l=0; l<li.length; l++)
                        {
                            try
                            {
                                Line line = mixer.getLine(li[l]);
                                if (line instanceof SourceDataLine)
                                {
                                    testLi = getLineInfo(line);
                                    System.out.println("Play Line: " + testLi);
                                    if (testLi != null) 
                                    {
                                        // compare with existing recFormat to see
                                        // whether we prefer the one from this line
                                        playFormat = compareFormats(playFormat, testFormat);
                                        if (playFormat == testFormat)
                                        {
                                            System.out.println("chose FR" + testFormat.getSampleRate() + 
                                                    " Ch" + testFormat.getChannels());
                                            playIndex = i;
                                            playLineInfo = testLi;
                                        }
                                    }
                                }
                            }
                            catch (IllegalArgumentException e)
                            {
                                System.out.println(e);
                            }
                        }
                        if (li.length > 0) playMixers.add(mixer.getMixerInfo());
                    }
                    // check for recording lines
                    li = mixer.getTargetLineInfo();
                    if (li != null)
                    {
                        if (li.length == 0 && count < 2)
                            System.out.println("No target lines for recording.");
                            
                        for (int l=0; l<li.length; l++)
                        {
                            try
                            {
                            Line line = mixer.getLine(li[l]);
                            if (line instanceof TargetDataLine)
                            {
                                testLi = getLineInfo(line);
                                System.out.println("Rec Line: " + testLi);
                                if (testLi != null) 
                                {
                                    // compare with existing recFormat to see
                                    // whether we prefer the one from this line
                                    recFormat = compareFormats(recFormat, testFormat);
                                    
                                    if (recFormat == testFormat)
                                    {
                                        recIndex = i;
                                        recLineInfo = testLi;
                                        System.out.println("chose " + testFormat);
                                    }
                                }
                            }
                            }
                            catch (IllegalArgumentException e)
                            {
                                System.out.println(e);
                            }
                        }
                        if (li.length > 0) recMixers.add(mixer.getMixerInfo());
                    }
                    // it is best to use the same mixer for recording and
                    // playback, so if that is the case break now
                    if (playIndex == i && recIndex == i)
                    {                    
                        //break;
                    }
                                        
                }
                catch (LineUnavailableException e)
                {
                    System.out.println(e.getLocalizedMessage());
                    error = e.getLocalizedMessage();
                }
            } // end of loop over mixers
            // always get play lines, so we can play back even if we can't 
            // record
            if (playIndex > -1)
            {
                System.out.println("Getting lines...");

                try
                {
                    playMixer = AudioSystem.getMixer(mixers[playIndex]);
                    synchronized (this)
                    {
                        sourceDataLine = 
                            (SourceDataLine) playMixer.getLine(playLineInfo);
                    }
                    System.out.println("" + 
                        playMixer.getMixerInfo().getName() + "\n" + playLineInfo + "\n" +
                        playFormat);
                    
                    if (recIndex > -1)
                    {
                        recMixer = AudioSystem.getMixer(mixers[recIndex]);
                        synchronized (this)
                        {   
                            targetDataLine = 
                                (TargetDataLine) recMixer.getLine(recLineInfo);
                        }
                        System.out.println("" + 
                            recMixer.getMixerInfo().getName() + "\n" + recLineInfo + "\n" +
                            recFormat);
                        synchronized (this) 
                        {
                            linesOpen = true;
                        }
                        System.out.println("Lines available");
                    }
                }
                catch (LineUnavailableException e)
                {
                    System.out.println(e.getLocalizedMessage());
                    error = e.getLocalizedMessage();
                }
            }
            else
            {
                if (count++ < 2)
                {
                    System.out.println("No lines available at present");
                    error = "No lines available at present";
                }
                try
                {
                    Thread.sleep(5000);
                }
                catch (InterruptedException e)
                {
                    break;
                }
            }
        }
    }
    
    public synchronized void setPlayMixer(Mixer.Info mi)
    {
        playMixer = AudioSystem.getMixer(mi);
        Line.Info [] li = playMixer.getSourceLineInfo();
        Line.Info testLi, playLineInfo = null;
        if (sourceDataLine != null && sourceDataLine.isOpen()) 
        {
            sourceDataLine.close();
            sourceDataLine = null;
        }
        if (li != null)
        {
            playFormat = null;
            for (int l=0; l<li.length; l++)
            {
                try
                {
                    Line line = playMixer.getLine(li[l]);
                    if (line instanceof SourceDataLine)
                    {
                        testLi = getLineInfo(line);
                        playFormat = compareFormats(playFormat, testFormat);
                        playLineInfo = testLi;
                    }
                }
                catch (IllegalArgumentException e)
                {
                    System.out.println(e);
                }
                catch (LineUnavailableException e)
                {
                    System.out.println(e);
                }
                
            }
            try
            {
                sourceDataLine = 
                    (SourceDataLine) playMixer.getLine(playLineInfo);
            }
            catch (LineUnavailableException e)
            {
                System.out.println(e);
                playMixer = null;
            }
        }
        else sourceDataLine = null;
    }
    
    public synchronized void setRecMixer(Mixer.Info mi)
    {
        recMixer = AudioSystem.getMixer(mi);
        Line.Info [] li = recMixer.getTargetLineInfo();
        Line.Info testLi, recLineInfo = null;
        if (targetDataLine != null && targetDataLine.isOpen()) 
        {
            targetDataLine.close();
            targetDataLine = null;
        }
        if (li != null)
        {
            recFormat = null;
            for (int l=0; l<li.length; l++)
            {
                try
                {
                    Line line = recMixer.getLine(li[l]);
                    if (line instanceof TargetDataLine)
                    {
                        testLi = getLineInfo(line);
                        recFormat = compareFormats(recFormat, testFormat);
                        recLineInfo = testLi;
                    }
                }
                catch (IllegalArgumentException e)
                {
                    System.out.println(e);
                }
                catch (LineUnavailableException e)
                {
                    System.out.println(e);
                }
            }
            try
            {
                targetDataLine = 
                    (TargetDataLine) recMixer.getLine(recLineInfo);
            }
            catch (LineUnavailableException e)
            {
                System.out.println(e);
                recMixer = null;
            }
        }
        else targetDataLine = null;
    }
    
    protected AudioFormat compareFormats(AudioFormat bestSoFar, AudioFormat test)
    {
        
        if (bestSoFar == null ||
            (test.getSampleRate() > 
             bestSoFar.getSampleRate()) ||
            (test.getChannels() >
             bestSoFar.getChannels()))
        {
            bestSoFar = test;
            System.out.println("chose " + test);
        }
        return bestSoFar;
    }
    
    /**
     * Examines the formats available for the given line and chooses the one
     * closest to what we want.
     * 
     */
    protected DataLine.Info getLineInfo(Line line)
        throws LineUnavailableException
    {
        AudioFormat refFormat = null;
        DataLine.Info supportedInfo = null;
        Line.Info classInfo = line.getLineInfo();
        DataLine dl = (DataLine)line;
        AudioFormat [] formats = 
            ((DataLine.Info)dl.getLineInfo()).getFormats();
        if (formats.length > 0) refFormat = formats[0];

        for (int i=0; i<formats.length; i++)
        {
            //System.out.println(formats[i]);
            if (refFormat.getChannels() < formats[i].getChannels())
            {
                refFormat = formats[i];
            }
            else if (formats[i].getSampleRate() > refFormat.getSampleRate() &&
                     formats[i].getSampleRate() <= MAX_SAMPLE_RATE)
            {
                refFormat = formats[i];                             
            }
            // all other factors being equal, prefer PCM_SIGNED
            else if (formats[i].getEncoding() == AudioFormat.Encoding.PCM_SIGNED &&
                     formats[i].getSampleRate() == refFormat.getSampleRate() &&
                     formats[i].getChannels() == refFormat.getChannels())
            {
                refFormat = formats[i];
            }
        }
        // if the sample rate is not specified then specify it now
        if (refFormat != null)
        {
            System.out.println(">>>" + refFormat);
            if (refFormat.getSampleRate() == AudioSystem.NOT_SPECIFIED)
            {
                float frameRate = maxSampleRate * 
                    (refFormat.getSampleSizeInBits() * 
                     refFormat.getChannels()) /
                    (refFormat.getFrameSize() * 8);
                refFormat = new AudioFormat(
                    refFormat.getEncoding(), 
                    maxSampleRate, 
                    refFormat.getSampleSizeInBits(), 
                    refFormat.getChannels(), 
                    refFormat.getFrameSize(),
                    frameRate,
                    refFormat.isBigEndian());
            }
            
            if (line instanceof TargetDataLine)
            {
                supportedInfo = new DataLine.Info(TargetDataLine.class, 
                                                  refFormat);
                testFormat = refFormat;
            }
            else if (line instanceof SourceDataLine)
            {
                supportedInfo = new DataLine.Info(SourceDataLine.class, 
                                                  refFormat);
                testFormat = refFormat;
            }                
        }
        return supportedInfo;
    }
    
    public synchronized boolean linesAreAvailable(){return linesOpen;}
    public synchronized SourceDataLine getSourceDataLine()
    {
        return sourceDataLine;
    }
    public synchronized TargetDataLine getTargetDataLine()
    {
        return targetDataLine;
    }
    public AudioFormat getRecLineFormat()
    {
        return recFormat;
    }
    
    public AudioFormat getPlayLineFormat()
    {
        return playFormat;
    }
    public Mixer getRecMixer()
    {
        return recMixer;
    }
    
    public Mixer getPlayMixer()
    {
        return playMixer;
    }
    public boolean lineError() 
    {
        return (error == null) ? false : true;
    }
    public String getError()
    {
        return error;
    }
    public void clearError()
    {
        error = null;
    }
}
