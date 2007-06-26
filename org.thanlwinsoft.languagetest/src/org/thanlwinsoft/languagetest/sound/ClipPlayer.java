/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/sound/ClipPlayer.java,v $
 *  Version:       $Revision: 852 $
 *  Last Modified: $Date: 2007-06-09 16:02:23 +0700 (Sat, 09 Jun 2007) $
 * -----------------------------------------------------------------------
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
 * -----------------------------------------------------------------------
 */

package org.thanlwinsoft.languagetest.sound;


import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
/**
 *
 * @author  keith
 */
public class ClipPlayer implements LineListener, AudioPlayer
{
    private AudioInputStream input = null;
    private Clip clip = null;
    private int startFrame = 0;
    private int endFrame = -1;
    private long microSecPerFrame;
    private File currentFile = null;
    //private static final long STEP_SIZE = 100000; // 0.1sec
    private LineListener listener = null;
    boolean userStop = false;
    /** Creates a new instance of ClipPlayer */
    public ClipPlayer(LineListener listener)
    {
        this.listener = listener;
    }
    
    public void play()
    {
        if (clip == null) return;
        System.out.println("Frame Pos: " + clip.getFramePosition());
        if (clip.getFramePosition() == clip.getFrameLength() ||
            clip.getFramePosition() == endFrame)
        {
            // rewind to start if clip finished
            //clip.setFramePosition(startFrame);
        }
        clip.start();
    }
    public boolean open(File file, long msLengthHint, boolean forceReopen)
    {
        if (currentFile != null)
        {
            // clip already loaded
            if (!forceReopen && file.compareTo(currentFile)==0) 
                return true;
            else close(); // unload previous clip
        }
        try 
        {
            input = AudioSystem.getAudioInputStream(file);
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
        DataLine.Info info = new DataLine.Info(Clip.class, 
                                               input.getFormat()); // format is an AudioFormat object
        if (!AudioSystem.isLineSupported(info)) 
        {
            // Handle the error.
            return false;
        }
        // Obtain and open the output line.
        try 
        {
            clip = (Clip) AudioSystem.getLine(info);
            clip.addLineListener(this);
            clip.open(input);
        } 
        catch (LineUnavailableException ex) 
        {
                // Handle the error.
            //... 
            System.out.println(ex);
            return false;
        }
        catch (IOException e)
        {
            System.out.println(e);
            return false;
        }
        clip.loop(0);
        microSecPerFrame = clip.getMicrosecondLength() / (long)clip.getFrameLength();
        startFrame = 0;
        endFrame = -1;
        return true;
    }
    
    public boolean close()
    {
        if (clip != null)
        {
            clip.drain();
            clip.stop();
            clip.close();
            clip.removeLineListener(this);
            clip = null;
            currentFile = null;
            try
            {
                input.close();
                input = null;
            }
            catch (IOException e)
            {
                System.out.println(e);
                return false;
            }            
        }
        return true;
    }
    
    public void setBounds(long start, long duration)
    {
        if (clip == null) return;
        if (start > 0)
        {
            startFrame = (int)(start / microSecPerFrame);
        }
        else startFrame = 0;
        if (duration > 0)
        {
            endFrame = startFrame + (int)(duration / microSecPerFrame);
            if (endFrame > clip.getFrameLength()) endFrame = -1;
        }
        else endFrame = -1;
        clip.setLoopPoints(startFrame, endFrame);
    }
    
    public void rewind(long amount)
    {
        if (clip == null) return ;
        int newPosition = clip.getFramePosition() 
            - (int)(amount / microSecPerFrame);
        if (newPosition < 0) newPosition = 0;
        clip.setFramePosition(newPosition);
        //return clip.getMicrosecondPosition();
    }
    
    public void fastForward(long amount)
    {
        if (clip == null) return ;
        int newPosition = clip.getFramePosition() 
            + (int)(amount / microSecPerFrame);
        if (newPosition < 0) newPosition = 0;
        clip.setFramePosition(newPosition);
        //return clip.getMicrosecondPosition();
    }
    
    /**
     * Returns position play stopped/started at
     */
    public void pause()
    {
        if (clip == null) return ;
        //long position = 0;
        if (clip.isRunning())
        {
            userStop = true;
            clip.stop();
            //clip.flush();
            //position = clip.getMicrosecondPosition();
        }
        else
        {
            //position = clip.getMicrosecondPosition();
            clip.start();
        }
        //return position;
    }
    
    public void stop()
    {
        if (clip == null) return;
        userStop = true;
        clip.stop();
        clip.setFramePosition(startFrame);
    }
    
    public long clipLength()
    {
        if (clip == null) return 0;
        return clip.getFrameLength() * microSecPerFrame;
    }
    
    synchronized public void update(javax.sound.sampled.LineEvent lineEvent)
    {
        if (lineEvent.getType() == LineEvent.Type.STOP)
        {
            System.out.println("Stopped at:" + clip.getFramePosition());
            // if userStop not set then must have played to end
            if (!userStop)
            {
                clip.stop();
                clip.setFramePosition(startFrame);
            }
            userStop = false;
        }
        if (listener != null) // call listener on gui
        {
            listener.update(lineEvent);
        }

    }
    
    synchronized public long getPlayPosition()
    {
        return clip.getMicrosecondPosition();
    }
    
    public void addPlayListener(AudioPlayListener listener)
    {
    }
    
    public File getCurrentFile()
    {
        return currentFile;
    }
    
    public long getDurationMs()
    {
        return (long)(endFrame - startFrame) * microSecPerFrame;
    }
    
    public long getStartMs()
    {
        return (long)startFrame * microSecPerFrame;
    }
    
    public boolean isInitialised()
    {
        return (clip == null) ? false : true;
    }
    
    public boolean open(File file)
    {
        return open(file, AudioSystem.NOT_SPECIFIED, false);
    }
    
    public boolean isActive()
    {
        return clip.isActive();
    }
    
}
