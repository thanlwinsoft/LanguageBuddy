/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/AudioProgressListener.java,v $
 *  Version:       $Revision: 1.6 $
 *  Last Modified: $Date: 2004/06/20 11:50:46 $
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

package languagetest.language.gui;

import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.JProgressBar;


import languagetest.sound.AudioPlayer;
import languagetest.sound.AudioPlayListener;

/**
 *
 * @author  Keith Stribley
 */
public class AudioProgressListener implements AudioPlayListener, Runnable
{
    private static final int DECIDE_PERIOD = 500;
    private static final int SLEEP_PERIOD = 100;
    private JProgressBar progressBar = null;
    private ProgressMonitor initMonitor = null;
    private AudioPlayer audio = null;
    private long pseudoLength = 2000;
    private long currentPosition = -1;
    private boolean showPlayWindowOnly = false;
    private int percentInitialised  = 0;
    private int oldValue = 0;
    private final static String INIT_PROGRESS_TITLE = 
        "Preparing to play Audio File:";
    private final static String OPENNING_SOUND_NOTE =
        "Please close any other programs that may be locking the sound system.";
    private final static String SEEK_NOTE = 
        "Seeking to start position in audio file.";
    
    // debug 
    static long oldPos = 0;
    
    /** Creates a new instance of AudioProgressListener */
    public AudioProgressListener(AudioPlayer player, JProgressBar progressBar, 
                                 boolean showPlayWindowOnly)
    {
        this.audio = player;
        this.progressBar = progressBar;
        this.showPlayWindowOnly = showPlayWindowOnly;
    }
    /** Creates a new instance of AudioProgressListener */
    public AudioProgressListener(AudioPlayer player)
    {
        this.audio = player;
        this.progressBar = null;
    }
    
    /**
     * This should only be called from GUI thread
     */
    synchronized public int checkProgress()
    {
        if (initMonitor == null)
        {
            // if audio isn't initialised and it actually has a file to try to
            // initialise, then start a progress monitor
            if (percentInitialised <100 && audio.getCurrentFile() != null)
            {
                initMonitor = new ProgressMonitor(  progressBar,
                                                    INIT_PROGRESS_TITLE,
                                                    OPENNING_SOUND_NOTE,
                                                    -1,100);
                initMonitor.setMillisToDecideToPopup(DECIDE_PERIOD);
                initMonitor.setProgress(0);
                System.out.println("Started audio init monitor");
                // create a thread to make sure setProgress is called regularly
                Thread watchdog = new Thread(this);
                watchdog.start();
            }
        }
        else
        {
            if (initMonitor.isCanceled() || getInitProgress() == 100)
            {
                initMonitor.close();
                System.out.println("Closed audio init monitor");
                initMonitor = null;
            }
        }
        return percentInitialised;
    }
    
    public void hideInitProgress()
    {
        if (initMonitor != null)
        {
            initMonitor.close();
            System.out.println("Closed audio init monitor");
            initMonitor = null;
        }
    }
    
    /** Method called to indicate progress of initialisation
     * e.g. seeking to start of audio clip.
     * @param percent Percentage of initialisation that has been completed.
     */    
    public void initialisationProgress(int percent)
    {
        if (percent == 0)
        {
            // reset pseudorange
            //pseudoLength = 2000;
        }
        synchronized (this)
        {
            percentInitialised = percent;
        }
//        if (percent < 100)
//        {
//            System.out.println("Audio Init " + percent + "%");
//        }
        showInitProgress(percent);
    }
    
    protected void showInitProgress(int percent)
    {
        if (initMonitor != null)
        {      
            final int percentProgress = percent;
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    
                    // check init monitor hasn't been killed in mean time
                    if (initMonitor != null)
                    {
                        initMonitor.setProgress(percentProgress);
                        if (percentProgress >= 100)
                        {
                            initMonitor.close();
                            initMonitor = null;
                        }
                        else if (percentProgress > 1)
                        {
                            initMonitor.setNote(SEEK_NOTE);
                        }
                    }
                }
            });
        }
    }
    
    /**
     * Retrieve the current position in the file in MS. 
     * This method is assumed to be called in the GUI thread.
     */
    public long getMsPosition()
    {
        return currentPosition;
    }
    
    /*
     * 
     */
    /** Method to indicate progress of audio playback or recording.
     * @param msPosition Position in milliseconds
     * @param msTotalLength Total length of clip in milliseconds or -1 if not known.
     */    
    public void playPosition(long msPosition, long msTotalLength)
    {
        // debug 
        
        /*
        if (oldPos != msPosition)
        {            
            System.out.println("Play Pos: " + msPosition + " " + msTotalLength);
            oldPos = msPosition;
        }
         */
        int value = 0;
        // check there is a bar to show progress on
        if (progressBar == null) return;
        if (showPlayWindowOnly)
        {
            value = (int)(msPosition  - audio.getStartMs());
            pseudoLength = (int)audio.getDurationMs();
            if (pseudoLength < value)
            {
                pseudoLength = (int)Math.ceil((double)value/1000.0) * 1000;
            }
        }
        else
        {
            value = (int)msPosition;
            if (msTotalLength < 0) // length not known
            {
                // this expression is designed to always show an increase, but
                // its size gets logarithmically smaller as the time period gets
                // longer
                pseudoLength = 1000;
                value = (int)(1000.0 * (1.0 - Math.exp( - (double)msPosition / 60000.0)));
                //System.out.println(value);
            }
            else // simple case
            {
                pseudoLength = msTotalLength;
            }
        }
        if (oldValue != value)
        {
            oldValue = value;
            final int totalLength = (int)pseudoLength;
            final int newValue = value;
            final int position = (int)msPosition;
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    progressBar.setMinimum(0);
                    progressBar.setMaximum(totalLength);
                    progressBar.setValue(newValue);
                    // since this is run in GUI thread, don't need to synchronise
                    // this call:
                    currentPosition = position;
                }
            });        
        }
    }
    /**
     * Thread to Monitor progress it is used to ensure that setProgress is 
     * incremented on monitor after decide period is over even if audio 
     * thread is hung
     */
    public void run()
    {
        int runTime = 0;
        int previousProgress = 0;
        while (getInitProgress() < 100)
        {
            try
            {
                Thread.sleep(SLEEP_PERIOD);
                runTime += SLEEP_PERIOD;
            }
            catch (InterruptedException ie)
            {
                
            }
            synchronized (this)
            {
                if (initMonitor == null) break;
                else
                {
                    if (percentInitialised <= previousProgress && 
                        runTime > DECIDE_PERIOD)
                    {
                        // if progress hasn't changed increment it artificially
                        // to show progress dialog how slow it is going - it
                        // seems to need an increment after DECIDE_PERIOD has 
                        // elapsed to be able to measure time
                        showInitProgress(percentInitialised + 1);
                    }
                    else
                    {
                        previousProgress = percentInitialised;
                    }
                }
            }
        }
    }
    
    synchronized public int getInitProgress()
    {
        return percentInitialised;
    }
}
