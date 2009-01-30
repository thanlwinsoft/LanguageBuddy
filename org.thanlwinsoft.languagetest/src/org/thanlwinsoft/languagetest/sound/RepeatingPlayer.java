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

import javax.sound.sampled.AudioSystem;
/**
 *
 * @author  keith
 */
public class RepeatingPlayer implements AudioPlayer, AudioPlayListener
{
    // don't make the source buffer too big, since it controls the resolution
    // with which one can pause clips
    private final static int SOURCE_LINE_BUFFER = 17640; // 0.1 seconds
    private InputLineController inputController = null;
    private OutputLineController outputController = null;
    private Vector<AudioPlayListener> listeners = null;
    private LineController lineController = null;
    /** Creates a new instance of RepeatingPlayer */
    public RepeatingPlayer(LineController lineController)
    {
        listeners = new Vector<AudioPlayListener>();
        this.lineController = lineController;
    }
    
        
    public void addPlayListener(AudioPlayListener listener)
    {
        listeners.add(listener);
    }
    
    public boolean close()
    {
        boolean closed = true;
        if (inputController != null) inputController.stopThread();
        if (outputController != null) closed = outputController.close();
        return closed;
    }
    
    public void fastForward(long amount)
    {
        if (inputController != null) inputController.seek(amount);
    }
    
    public java.io.File getCurrentFile()
    {
        if (inputController != null) return inputController.getAudioFile();
        else return null;
    }
    
    public long getDurationMs()
    {
        if (inputController != null) return inputController.getDurationMs();
        return 0;
    }
    
    public long getStartMs()
    {
        if (inputController != null) return inputController.getStartMs();
        return 0;
    }
    
    public boolean isInitialised()
    {
        if (inputController != null && outputController.open()) return true;
        else return false;
    }
    
    public boolean open(java.io.File file)
    {
        return open(file, AudioSystem.NOT_SPECIFIED, false);
    }
    
    public boolean open(java.io.File file, long msLengthHint, boolean forceReopen) 
    {
        if (inputController != null)
        {
            if (forceReopen || !file.equals(inputController.getAudioFile()))
            {
                inputController.stop();
                inputController.stopThread();
                inputController.setOutputController(null);
                inputController.setListener(null);
                inputController = null;                
            }
        }
        if (inputController == null)
        {
            inputController = new InputLineController(file, msLengthHint, 
                lineController);
            // now create output controller if there isn't already one
            if (outputController == null)
            {
                outputController = 
                        new OutputLineController(lineController, 
                                                 SOURCE_LINE_BUFFER);
                outputController.setListener(this);
                new Thread(outputController).start();
            }
            inputController.setOutputController(outputController);
            inputController.setListener(this);
            // now that all pointers are initialised, start the thread
            // (If thread started before outputController set, some commands
            // may be lost)
            new Thread(inputController).start();
        }
        
        return true;
    }
    
    public void pause()
    {
        if (inputController != null) inputController.pause();
    }
    
    public void play()
    {
        if (inputController != null) inputController.play();
    }
    
    public void rewind(long amount)
    {
        if (inputController != null) inputController.seek(-amount);
    }
    
    public void setBounds(long start, long duration)
    {
        if (inputController != null) inputController.setBounds(start, duration);
    }
    
    public void stop()
    {
        if (inputController != null) inputController.stop();
    }
    /** Method calls the playPosition method on each registered 
     * AudioPlayListener.
     */
    public synchronized void playPosition(long position, long length)
    {
        Iterator<AudioPlayListener> l = listeners.iterator();
        while (l.hasNext())
        {
            ((AudioPlayListener)l.next())
                .playPosition(position, length);
        }
    }
    
    public synchronized void initialisationProgress(int percent)
    {
        int percentToShow = percent;
        Iterator<AudioPlayListener> l = listeners.iterator();
        if (outputController != null)
        {
            if (outputController.isOpen() == false) percentToShow = -1;
            while (l.hasNext())
            {
                ((AudioPlayListener)l.next())
                    .initialisationProgress(percentToShow);
            }
        }
    }
    
    public boolean isActive()
    {
        if (outputController != null)
        {
            return outputController.isActive();
        }
        return false;
    }
}
