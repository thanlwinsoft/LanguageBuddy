/**
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/eclipse/widgets/SoundPlayer.java $
 *  Revision        $LastChangedRevision: 1388 $
 *  Last Modified:  $LastChangedDate: 2009-01-31 19:32:00 +0700 (Sat, 31 Jan 2009) $
 *  Last Change by: $LastChangedBy: keith $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
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
 * -----------------------------------------------------------------------
 *  This code is heavily based around the JavaZOOM player.
 *  
 *  JavaZOOM : jlgui@javazoom.net
 *            http://www.javazoom.net
 */
package org.thanlwinsoft.eclipse.widgets;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scale;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.sound.AudioPlayListener;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;


/**
 * @author keith
 *
 */
public class SoundPlayer extends Composite implements BasicPlayerListener
{
    /** Fractional position to seek to */
    private double posValue = 0.0;
    /*-- JavaSound Members --*/
    public static final int INIT = 0;
    public static final int OPEN = 1;
    public static final int PLAY = 2;
    public static final int PAUSE = 3;
    public static final int STOP = 4;
    private int playerState = INIT;
    private long secondsAmount = 0;
    private long msAmount = 0;
    private long msStopValue = -1;
    private BasicController theSoundPlayer = null;
    private Map<?,?> audioInfo = null;
    private boolean posValueJump = false;

    private String currentFileOrURL = null;
    private String titleText = "";
    private boolean currentIsFile;
    private long lengthInSecond = -1l;
    private long lengthInMs = -1;
    private boolean isRealLength = false;
    //private long lastBytesRead = 0l;
    // gui
    private Scale slider = null;
    private Display display = null;
    private Set<AudioPlayListener> audioPlayListenerSet = 
        new HashSet<AudioPlayListener>();

    public SoundPlayer(Composite parent)
    {
        super(parent, SWT.NONE);
        display = parent.getShell().getDisplay();
        RowLayout layout = new RowLayout();
        layout.type = SWT.VERTICAL;
        layout.spacing = 0;
        layout.fill = true;
        setLayout(layout);
        Composite buttonRow = new Composite(this, SWT.NONE);
        RowLayout buttonLayout = new RowLayout();
        buttonLayout.type = SWT.HORIZONTAL;
        buttonLayout.spacing = 0;
        buttonLayout.fill = true;
        buttonRow.setLayout(buttonLayout);
        Button play = new Button(buttonRow, SWT.NONE);
        Button pause = new Button(buttonRow, SWT.NONE);
        Button stop = new Button(buttonRow, SWT.NONE);

        //play.setImage(idPlay.createImage());
        ImageRegistry ir = LanguageTestPlugin.getDefault().getImageRegistry();
        play.setImage(ir.get("Play"));
        play.setToolTipText(MessageUtil.getString("SoundPlayer_Play"));
        pause.setImage(ir.get("Pause"));
        pause.setToolTipText(MessageUtil.getString("SoundPlayer_Pause"));
        stop.setImage(ir.get("Stop"));
        stop.setToolTipText(MessageUtil.getString("SoundPlayer_Stop"));
        slider = new Scale(this, SWT.HORIZONTAL);
        slider.setMinimum(0);
        slider.setMaximum(600);
        slider.setSelection(0);
        slider.setSize(50, SWT.DEFAULT);
        slider.addSelectionListener( new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e){}
            public void widgetSelected(SelectionEvent e) { seek(); }
        });

        play.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {}
            public void widgetSelected(SelectionEvent e)
            {
                play();
            }});
        pause.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {}
            public void widgetSelected(SelectionEvent e)
            {
                pause();
            }});
        stop.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {}
            public void widgetSelected(SelectionEvent e)
            {
                stop();
            }});

        slider.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e)
            {
                seek();
            }});
        // sound intialisation
        BasicPlayer bplayer = new BasicPlayer();
        // Register the front-end to low-level player events.
        bplayer.addBasicPlayerListener(this);
        // Adds controls for front-end to low-level player.
        setController(bplayer);

        playerState = STOP;
        // debug testing only
        //setFile("/home/keith/ogg/Fierce/Simply Worship 1/01 - Track 1.ogg");
    }

    /**
     * 
     */
    protected void stop()
    {
        if ((playerState == PLAY) || (playerState == PAUSE))
        {
            try
            {
                theSoundPlayer.stop();
            }
            catch (BasicPlayerException e1)
            {
                LanguageTestPlugin.log(IStatus.INFO, "Cannot stop", e1);
            }
            playerState = STOP;
        }
    }

    /**
     * 
     */
    protected void pause()
    {
        if (playerState == PLAY)
        {
            try
            {
                theSoundPlayer.pause();
            }
            catch (BasicPlayerException e1)
            {
                LanguageTestPlugin.log(IStatus.ERROR,"Cannot pause",e1);
            }
            playerState = PAUSE;
        }
        else if (playerState == PAUSE)
        {
            try
            {
                theSoundPlayer.resume();
            }
            catch (BasicPlayerException e1)
            {
                LanguageTestPlugin.log(IStatus.ERROR, "Cannot resume",e1);
            }
            playerState = PLAY;
        }
    }

    /**
     * 
     */
    public void play()
    {
        Job job = new Job("Play") 
        {

            protected IStatus run(IProgressMonitor monitor)
            {
                runPlay();
                return Status.OK_STATUS;
            }

        };
        job.schedule();
    }

    protected void runPlay()
    {
        try
        {
//          Resume is paused.
            if (playerState == PAUSE)
            {
                try
                {
                    theSoundPlayer.resume();
                }
                catch (BasicPlayerException e1)
                {
                    LanguageTestPlugin.log(IStatus.ERROR,"Cannot resume",e1);
                }
                playerState = PLAY;

            } 
            // Stop if playing.
            else if (playerState == PLAY)
            {
                try
                {
                    theSoundPlayer.stop();
                }
                catch (BasicPlayerException e1)
                {
                    LanguageTestPlugin.log(IStatus.ERROR,"Cannot stop",e1);
                }
                playerState = PLAY;
                secondsAmount = 0;

                if (currentFileOrURL != null)
                {
                    try
                    {           
                        if (currentIsFile == true) theSoundPlayer.open(openFile(currentFileOrURL));
                        else theSoundPlayer.open(new URL(currentFileOrURL));
                        processSeek();
                        theSoundPlayer.play();
                    } 
                    catch (Exception ex)
                    {
                        LanguageTestPlugin.log(IStatus.ERROR,"Cannot read file : " + currentFileOrURL,ex);
                    }
                }
            } 
            else if ((playerState == STOP) || (playerState == OPEN))
            {
                try
                {
                    theSoundPlayer.stop();
                }
                catch (BasicPlayerException e1)
                {
                    LanguageTestPlugin.log(IStatus.ERROR,"Stop failed",e1);
                }
                if (currentFileOrURL != null)
                {
                    try
                    {
                        if (currentIsFile == true) theSoundPlayer.open(openFile(currentFileOrURL));
                        else theSoundPlayer.open(new URL(currentFileOrURL));
                        processSeek();
                        theSoundPlayer.play();
                        //titleText = currentSongName.toUpperCase();

                        // Get bitrate, samplingrate, channels, time in the following order :
                        // PlaylistItem, BasicPlayer (JavaSound SPI), Manual computation.
                        int bitRate = -1;
                        //if (currentPlaylistItem != null) bitRate = currentPlaylistItem.getBitrate(); 
                        if ((bitRate <= 0) && (audioInfo.containsKey("bitrate"))) bitRate = ((Integer) audioInfo.get("bitrate")).intValue();
                        if ((bitRate <= 0) && (audioInfo.containsKey("audio.framerate.fps")) && (audioInfo.containsKey("audio.framesize.bytes")))
                        {
                            float FR = ((Float)audioInfo.get("audio.framerate.fps")).floatValue();
                            int FS = ((Integer)audioInfo.get("audio.framesize.bytes")).intValue();                          
                            bitRate = Math.round(FS * FR * 8);
                        }
                        int channels = -1;
                        //if (currentPlaylistItem != null) channels = currentPlaylistItem.getChannels();
                        if ((channels <= 0) &&(audioInfo.containsKey("audio.channels"))) channels = ((Integer) audioInfo.get("audio.channels")).intValue();
                        float sampleRate = -1.0f;
                        //if (currentPlaylistItem != null) sampleRate = currentPlaylistItem.getSamplerate();
                        if ((sampleRate <= 0) && (audioInfo.containsKey("audio.samplerate.hz"))) sampleRate = ((Float) audioInfo.get("audio.samplerate.hz")).floatValue();
                        lengthInSecond = -1L;          
                        //if (currentPlaylistItem != null) lenghtInSecond = currentPlaylistItem.getLength();
                        if ((lengthInSecond <= 0) && (audioInfo.containsKey("duration"))) lengthInSecond = ((Long) audioInfo.get("duration")).longValue()/1000000;
                        if ((lengthInSecond <= 0) && (audioInfo.containsKey("audio.length.bytes")))
                        {
                            // Try to compute time length.
                            lengthInSecond = (long) Math.round(getTimeLengthEstimation(audioInfo)/1000);
                        }
                        if (lengthInSecond < 0)
                        {
                            lengthInSecond = 120; // 2min
                            isRealLength = false;
                        }
                        else isRealLength = true;

                        final int lengthMax = (int)lengthInSecond;
                        display.asyncExec (new Runnable () {
                            public void run () {
                                slider.setMaximum(lengthMax);
                            }
                        });

                        bitRate = Math.round(( bitRate/ 1000));
                        if (bitRate > 999)
                        {
                            bitRate = (int) (bitRate/100); 
                        }
                    }
                    catch (BasicPlayerException bpe)
                    {
                        LanguageTestPlugin.log(IStatus.INFO,"Stream error :" + currentFileOrURL,bpe);
                    }
                    catch (MalformedURLException mue)
                    {
                        LanguageTestPlugin.log(IStatus.INFO,"Stream error :" + currentFileOrURL,mue);
                    }

                    // Set pan/gain.
//                  try
//                  {
//                  theSoundPlayer.setGain(((double) gainValue / (double) maxGain));
//                  theSoundPlayer.setPan((float) balanceValue);  
//                  }
//                  catch (BasicPlayerException e2)
//                  {
//                  LanguageTestPlugin.log(IStatus.INFO,"Cannot set control",e2);
//                  }     

                    playerState = PLAY;
                    LanguageTestPlugin.log(IStatus.INFO,titleText);
                }
            }
            else
            {
                LanguageTestPlugin.log(IStatus.INFO,"Play ignored " + playerState);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LanguageTestPlugin.log(IStatus.ERROR,"play error " + e.getLocalizedMessage(),e);
        }
    }



    /**
     * 
     */
    protected void seek()
    {
        if (playerState == PAUSE || playerState == STOP)
        {
            posValue =
                (double) slider.getSelection() / (double) slider.getMaximum();
            // processSeek();
        }
    }

    /**
     * Seek to the specified offset in ms
     * @param msInterval
     */
    public void seek(long msInterval)
    {
        if (playerState == PAUSE || playerState == STOP)
        {
            if (lengthInMs < 0)
            {
                lengthInMs = getTimeLengthEstimation(audioInfo);
            }
            // the actual seeking is done in the play thread
            if (msInterval > 0 && lengthInMs > 0)
            {
                posValue = (double)msInterval / (double)lengthInMs;
            }
            else
            {
                posValue = 0;
            }
        }
    }
    /**
     * Stop after playing msInterval Miliseconds. This is relative to the seek
     * position, NOT the start of the file.
     * @param msInterval
     */
    public void stopAfter(long msInterval)
    {
        msStopValue = msInterval;
    }

    public void setFile(String file)
    {
        stop();
        currentIsFile = true;
        currentFileOrURL = file;
        lengthInMs = -1;
        lengthInSecond = -1;
        msAmount = 0;
        msStopValue = -1;
        secondsAmount = 0;
        if (file != null)
            LanguageTestPlugin.log(IStatus.INFO, "SoundPlayer setFile " + file);
        slider.setSelection(0);
    }


    // methods from JLPlayer

    /*-----------------------------------------*/
    /*--    BasicPlayerListener Interface    --*/
    /*-----------------------------------------*/

    /**
     * Open callback, stream is ready to play.
     */
    @SuppressWarnings("unchecked")
	public void opened(Object stream, Map properties)
    {
        audioInfo = properties;
        LanguageTestPlugin.log(IStatus.INFO, properties.toString());
    }

    /**
     * Progress callback while playing.
     */
    @SuppressWarnings("unchecked")
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties)
    {
        int byteslength = -1;
        long total = -1;
        msAmount = microseconds / 1000;
        if (lengthInMs <= 0)
            lengthInMs = getTimeLengthEstimation(audioInfo);
        // If it fails then try again with JavaSound SPI.
        if (total <=0) total = (long) Math.round(lengthInMs/1000);
        // If it fails again then it might be stream => Total = -1  
        if (total <=0) total = -1;
        if (audioInfo.containsKey("audio.length.bytes"))
        {
            byteslength = ((Integer) audioInfo.get("audio.length.bytes")).intValue();           
        }
        float progress = -1.0f;
        if ((bytesread > 0) && ((byteslength > 0))) progress = bytesread*1.0f/byteslength*1.0f; 

        if (audioInfo.containsKey("audio.type"))
        {
            String audioformat = (String) audioInfo.get("audio.type");
            if (audioformat.equalsIgnoreCase("mp3"))
            {
                // Shoutcast stream title.
                if (properties.containsKey("mp3.shoutcast.metadata.StreamTitle"))
                {
                    String shoutTitle = ((String) properties.get("mp3.shoutcast.metadata.StreamTitle")).trim();
                    if (shoutTitle.length()>0)
                    {                   
                        titleText = shoutTitle;
                    }
                }
                // Equalizer
                if (total > 0) secondsAmount = (long) (total*progress);
                else secondsAmount = -1;
            }
            else if (audioformat.equalsIgnoreCase("wave"))
            {
                secondsAmount = (long) (total*progress);            
            }
            else
            {
                secondsAmount = (long) Math.round(microseconds/1000000);
            }
        }
        else
        {
            secondsAmount = (long) Math.round(microseconds/1000000);

        }       
        if (secondsAmount < 0) secondsAmount = (long) Math.round(microseconds/1000000);
        //lastBytesRead = bytesread;

        final int sliderPos = (int)secondsAmount;
        // Update PosBar location.
        try
        {
            //slider.setSelection(sliderPos);
            Iterator<AudioPlayListener> i = audioPlayListenerSet.iterator();
            long msPosition = secondsAmount * 1000;
            long msTotalLength = getLengthInSeconds() * 1000;
            while (i.hasNext())
            {
                i.next().playPosition(msPosition, msTotalLength);
            }
            display.asyncExec (new Runnable () {
                public void run () {
                    int oldMax = slider.getMaximum();
                    if (sliderPos > oldMax)
                        slider.setMaximum(oldMax + 60); // add 1 minute
                    slider.setSelection(sliderPos);
                }
            });
        }
        catch (Exception e)
        {
            LanguageTestPlugin.log(IStatus.ERROR, "slider update failed " + sliderPos,e);
        }
        if (msStopValue >= 0 && msAmount >= msStopValue)
        {
            try
            {
                theSoundPlayer.stop();
            }
            catch (BasicPlayerException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING,
                    "SoundPlayer failed to stop", e);
            }
        }
    }

    /**
     * Notification callback.
     */
    public void stateUpdated(BasicPlayerEvent event)
    {
        LanguageTestPlugin.log(IStatus.INFO, "Event:" + event);
        /*-- End Of Media reached --*/
        int state = event.getCode();
        Object obj = event.getDescription();
        if (state == BasicPlayerEvent.EOM)
        {
            if ((playerState == PAUSE) || (playerState == PLAY))
            {
                
            }
        }
        else if (state == BasicPlayerEvent.PLAYING)
        {
            //lastScrollTime = System.currentTimeMillis(); 
            posValueJump = false;       
        }
        else if (state == BasicPlayerEvent.SEEKING)
        {
            posValueJump = true;    
        }
        else if (state == BasicPlayerEvent.OPENING)
        {
            if ((obj instanceof URL) || (obj instanceof InputStream))
            {
                LanguageTestPlugin.log(IStatus.INFO, "PLEASE WAIT ... BUFFERING ...");     

            }

        }
    }

    /**
     * A handle to the BasicPlayer, plugins may control the player through
     * the controller (play, stop, ...)
     */
    public void setController(BasicController controller)
    {
        theSoundPlayer = controller;
    }

    /**
     * Process seek feature.
     */
    protected void processSeek()
    {
        try
        {
            if (posValue >= 1.0)
                posValue = 0;
            if (audioInfo.containsKey("audio.type"))
            {
                String type = (String) audioInfo.get("audio.type");
                // Seek support for MP3.
                if ((type.equalsIgnoreCase("mp3")) && (audioInfo.containsKey("audio.length.bytes")))
                {
                    long skipBytes = (long) Math.round(((Integer) audioInfo.get("audio.length.bytes")).intValue()*posValue);
                    LanguageTestPlugin.log(IStatus.INFO, "Seek value (MP3) : "+skipBytes);
                    theSoundPlayer.seek(skipBytes);   
                }
                // Seek support for WAV.            
                else if ((type.equalsIgnoreCase("wave")) && (audioInfo.containsKey("audio.length.bytes")))
                {
                    long skipBytes = (long) Math.round(((Integer) audioInfo.get("audio.length.bytes")).intValue()*posValue);
                    LanguageTestPlugin.log(IStatus.INFO, "Seek value (WAVE) : "+skipBytes);
                    theSoundPlayer.seek(skipBytes);   
                }
                else posValueJump = false;
            }
            else posValueJump = false;
        } 
        catch (BasicPlayerException ioe)
        {
            LanguageTestPlugin.log(IStatus.ERROR, "Cannot skip",ioe);
            posValueJump = false;
        }   
    }

    /**
     * Try to compute time length in milliseconds.
     */
    @SuppressWarnings("unchecked")
	public long getTimeLengthEstimation(Map properties)
    {
        long milliseconds = -1;
        int byteslength = -1;
        if (properties != null)
        {
            if (properties.containsKey("audio.length.bytes"))
            {
                byteslength = ((Integer) properties.get("audio.length.bytes")).intValue();            
            }
            if (properties.containsKey("duration"))
            {
                milliseconds = (int) (((Long) properties.get("duration")).longValue())/1000;            
            }
            else
            {
                // Try to compute duration
                int bitspersample = -1;
                int channels = -1;
                float samplerate = -1.0f;
                int framesize = -1;          
                if (properties.containsKey("audio.samplesize.bits"))
                {
                    bitspersample = ((Integer) properties.get("audio.samplesize.bits")).intValue(); 
                }
                if (properties.containsKey("audio.channels"))
                {
                    channels = ((Integer) properties.get("audio.channels")).intValue(); 
                }
                if (properties.containsKey("audio.samplerate.hz"))
                {
                    samplerate = ((Float) properties.get("audio.samplerate.hz")).floatValue(); 
                }
                if (properties.containsKey("audio.framesize.bytes"))
                {
                    framesize = ((Integer) properties.get("audio.framesize.bytes")).intValue(); 
                }
                if (bitspersample > 0)
                {
                    milliseconds = (int) (1000.0f*byteslength/(samplerate * channels * (bitspersample/8))); 
                } 
                else
                {
                    milliseconds = (int)(1000.0f*byteslength/(samplerate*framesize)); 
                }           
            }
        }
        //LanguageTestPlugin.log(IStatus.INFO, "Bytes: " + byteslength + " ms: " + milliseconds);
        return milliseconds;
    }

    /**
     * Returns a File from a filename.
     */
    protected File openFile(String file)
    {
        return new File(file);
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    public void dispose()
    {
        stop();
        super.dispose();
    }

    // KRS: The next 2 methods have not been tested yet

    public long getPosition()
    {
        if (posValueJump) return -1l;
        return secondsAmount;
    }
    public long getLengthInSeconds()
    {
        if (isRealLength)
            return lengthInSecond;
        return -1l;
    }
    public void addPlayListener(AudioPlayListener listener)
    {
        audioPlayListenerSet.add(listener);
    }
    public void removePlayListener(AudioPlayListener listener)
    {
        audioPlayListenerSet.remove(listener);
    }
}
