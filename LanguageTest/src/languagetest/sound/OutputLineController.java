/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/sound/OutputLineController.java,v $
 *  Version:       $Revision: 1.6 $
 *  Last Modified: $Date: 2004/12/18 05:16:27 $
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
 * OutputLineController.java
 * This uses the term output to mean Output from the program to the speakers
 * i.e. it controls the line used for playing sound.
 * Created on February 6, 2004, 12:02 PM
 */

package languagetest.sound;

import java.util.BitSet;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;

/**
 *
 * @author  keith
 */
public class OutputLineController implements javax.sound.sampled.LineListener, 
    java.lang.Runnable
{
    final static int REINIT_BIT = 0;
    final static int OPEN_BIT = 1;
    final static int PLAY_BIT = 2;
    final static int DRAIN_BIT = 3;
    final static int FLUSH_BIT = 4;
    final static int NUM_BITS = 5;
/*    
    final static int CLOSED = 0;
    final static int WAITING_FOR_OPEN = 1;
    final static int START_ON_OPEN = 2;
    final static int WAITING_FOR_START = 3;
    final static int IS_OPEN = 4;
    final static int WAITING_FOR_STOP = 5;
    final static int PLAYING = 6;
    final static int OPEN_AND_START = 7;
    final static int OPEN = 8;
    final static int WAITING_FOR_CLOSE = 9;
    final static int CLOSE_ON_STOP = 10;
    final static int CLOSE = 11;
    final static int INIT = 12;
    final static int REINIT_ON_CLOSE = 13;
    final static int START = 14;
    final static int PLAY_TO_END = 15;
*/    
    private SourceDataLine outputLine = null;
    private BitSet targetState = null;
    private BitSet actualState = null;
    private BitSet requestedState = null;
    private AudioFormat format = null;
    private int bufferSize = -1;
    private boolean exit = false;
    private AudioPlayListener listener = null;
    private LineController lineController;
    private int idleCount = 0;
    private final static int SLEEP_TIME = 10; //ms 
    // multiple of sleep time before line times out and closes
    private final static int MAX_IDLE_BEFORE_CLOSE = 3000; 
    /** Creates a new instance of OutputLineController */
    public OutputLineController(LineController lineController, int bufferSize ) 
    {
        this.format = null;
        this.bufferSize = bufferSize;
        this.lineController = lineController;
        targetState = new BitSet(NUM_BITS);
        actualState = new BitSet(NUM_BITS);
        requestedState = new BitSet(NUM_BITS);
        targetState.set(REINIT_BIT);
    }
    
    public void setFormat(AudioFormat newFormat)         
    {
        if (newFormat == null || (format != null && newFormat.matches(format)))
        {
            // do nothing
        }
        else
        {
            format = newFormat;
            setTarget(REINIT_BIT);
            clearTarget(DRAIN_BIT);
            clearTarget(PLAY_BIT);
        }
    }
    
    /* synchronized setter
     * @param bit index of bit to set
     */
    protected  void setTarget(int bit)
    {
        synchronized (this)
        {
            if (!targetState.get(bit))
            {
                targetState.set(bit); 
                System.out.println("OutputLineController target: " 
                   + stateToString(targetState));
            }
        }
    }

     /* synchronized clear
      * @param bit index of bit to clear
     */
    protected void clearTarget(int bit)
    {
        synchronized (this)
        {
            if (targetState.get(bit))
            {                
                targetState.clear(bit);  
                System.out.println("OutputLineController target: " 
                    + stateToString(targetState));
            }
        }        
    }

    /* synchronized setter
     * @param bit index of bit to set
     */
    protected void setRequested(int bit)
    {
        synchronized (this)
        {
            requestedState.set(bit);  
        }
        System.out.println("OutputLineController request: " 
            + stateToString(requestedState));
    }

     /* synchronized clear
      * @param bit index of bit to clear
     */
    protected void clearRequested(int bit)
    {
        synchronized (this)
        {
            requestedState.clear(bit);     
        }
        System.out.println("OutputLineController request: " 
            + stateToString(requestedState));
    }



    protected void  init()
    {
        synchronized(this)
        {
            if (outputLine != null)
            {
                // close old line
                // double check it isn't open
                if (outputLine.isOpen()) outputLine.close();
                outputLine.removeLineListener(this);
                outputLine = null;
            }
        }  
            //outputLine = lineController.getSourceDataLine();
            //if (!lineController.getPlayLineFormat().matches(format))
            {
                DataLine.Info newInfo = 
                    new DataLine.Info(SourceDataLine.class, format);
                if (lineController.getPlayMixer().isLineSupported(newInfo))
                {
                    try
                    {
                        SourceDataLine newLine = 
                            (SourceDataLine)
                            lineController.getPlayMixer().getLine(newInfo);
                        // only synchronize now we have got the line (which
                        // could block)
                        synchronized (this)
                        {
                            outputLine = newLine;
                        }
                    }
                    catch (LineUnavailableException e)
                    {
                        System.out.println(e.getMessage());
                        outputLine = null;
                    }
                }
            }
            if (outputLine != null)
            {
                outputLine.addLineListener(this);
                // auto open
                setTarget(OPEN_BIT);
                System.out.println("OutputLineController inititialised");
            }
    }
    
    
    public  void update(javax.sound.sampled.LineEvent event)
    {
        synchronized (this)
        {
            if (event.getType() == LineEvent.Type.OPEN)
            {
                actualState.set(OPEN_BIT);
            }
            else if (event.getType() == LineEvent.Type.CLOSE)
            {
                actualState.clear(OPEN_BIT);
            }
            else if (event.getType() == LineEvent.Type.START)
            {
                actualState.set(PLAY_BIT);
            }
            else if (event.getType() == LineEvent.Type.STOP)
            {
                actualState.clear(PLAY_BIT);
                if (targetState.get(DRAIN_BIT))
                {
                    clearTarget(DRAIN_BIT);
                    clearTarget(PLAY_BIT);
                }
            }
        }
        
        System.out.println("OutputLineController Event: " 
                + event.getType().toString() + " Target: " 
                + stateToString(targetState));

    }
    /** 
     * All calls on outputLine are done inside iternal thread
     * targetState is called by multiple threads, so it is always synchronized.
     */
    public void run()
    {
        int waitCount = 0;
        System.out.println("OutputLineController thread started");
        while (exit == false)
        {
            // instantaneous target, state
            BitSet iTarget;
            BitSet iState;
            synchronized (this)
            {
                iTarget = (BitSet)targetState.clone();
                iState = (BitSet)actualState.clone();
                if (outputLine != null)
                {
                    if (outputLine.isOpen() != actualState.get(OPEN_BIT))
                    {
                        // actualState.set(OPEN_BIT, outputLine.isOpen());
                        System.out.println("OutputLineController open mismatch" + 
                            stateToString(actualState));
                    }
                    if (outputLine.isRunning() != actualState.get(PLAY_BIT))
                    {
                        // actualState.set(PLAY_BIT, outputLine.isRunning());
                        System.out.println("OutputLineController play mismatch" + 
                            stateToString(actualState));
                    }
                }
                else
                {
                    // output line not initialised so sleep and try again
                    sleep();
                    if ((format != null) && 
                        (lineController.getSourceDataLine() != null))
                        //lineController.linesAreAvailable())
                    {
                        init();
                    }
                    continue;
                }
            }
            if (!requestedState.equals(iState)) // wait for action to take place
            {
                waitCount++;
                if (waitCount > 100)
                {
                    System.out.println("Waiting for " + stateToString(iState)
                        + " -> " + stateToString(requestedState));
                    waitCount = 0;
                    // clear requested state to force retry
                    requestedState = (BitSet)iState.clone();
                }
                else
                {
                    sleep();
                }
            }
            else 
            {
                waitCount = 0;
                if (iTarget.get(REINIT_BIT))
                {
                    if (iState.get(PLAY_BIT))
                    {
                        clearRequested(PLAY_BIT);
                        outputLine.stop();
                    }
                    else if (iState.get(OPEN_BIT))
                    {
                        outputLine.drain();
                        clearRequested(OPEN_BIT);
                        outputLine.close();
                    }
                    else 
                    {
                        // line is now closed so init can proceed
                        // init now in progress so clear bit to stop it being
                        // repeated
                        clearTarget(REINIT_BIT);
                        if ((format != null) && 
                            (lineController.getSourceDataLine() != null))
                            //lineController.linesAreAvailable())
                        {
                            init();
                        }
                    }
                }
                else if (iTarget.get(OPEN_BIT)) // now deal with open state
                {
                    if (iState.get(OPEN_BIT))  // line is already open
                    {
                        // open target met, so check play bits
                        
                        if (!iState.get(PLAY_BIT)) // is line not playing
                        {
                            if (iTarget.get(PLAY_BIT))
                            {
                                // neet to start play
                                idleCount = 0;
                                setRequested(PLAY_BIT);
                                outputLine.start();
                            }
                            else if (iTarget.get(FLUSH_BIT))
                            {
                                // play is stopped as required, but still need to flush
                                clearTarget(FLUSH_BIT);
                                outputLine.flush();
                            }
                            // play not requested, so leave line open for awhile
                            else 
                            {
                                sleep();
                                idleCount++;
                                if (idleCount > MAX_IDLE_BEFORE_CLOSE)
                                {
                                    // if the line is not used for awhile, close
                                    // it to give other applications access
                                    System.out.println("Line idle - closing");
                                    clearTarget(OPEN_BIT);
                                }
                            }
                        }
                        // line is playing but target is not playing
                        else if (iState.get(PLAY_BIT) && (!iTarget.get(PLAY_BIT)))
                        {
                            // need to stop play
                            clearRequested(PLAY_BIT);
                            outputLine.stop();
                        }
                        else // let it continue playing
                        {
                            if (!outputLine.isActive())
                            {
                                idleCount++;
                                if (idleCount > MAX_IDLE_BEFORE_CLOSE)
                                {
                                    // if the line is not used for awhile, close
                                    // it to give other applications access
                                    System.out.println("Line idle - closing");
                                    clearTarget(OPEN_BIT);
                                }
                            }
                            else
                            {
                                idleCount = 0;
                            }
                            sleep();
                        }
                        
                    }
                    else // need to open
                    {
                        idleCount = 0;
                        // line is still open, so close it now
                        setRequested(OPEN_BIT);
                        try
                        {
                            listener.initialisationProgress(-1);
                            outputLine.open(format, bufferSize);
                            listener.initialisationProgress(0);
                        }
                        catch (LineUnavailableException lue)
                        {
                            System.out.println(lue.getMessage());
                            sleep();
                        }
                    }
                }
                // to have got this far, target is for line to be closed 
                // If it is still running then stop it first
                else if (iState.get(PLAY_BIT))
                {
                    clearRequested(OPEN_BIT);
                    outputLine.stop();
                }
                // to have got this far, line is stopped, 
                // target is for line to be closed 
                else if (iState.get(OPEN_BIT))
                {
                    // line is still open, so close it now
                    clearRequested(OPEN_BIT);
                    // always flush before close
                    outputLine.flush();
                    outputLine.close();
                }
                else
                {
                    sleep();
                }
            }
        }
    }
//            switch (instantaneousState)
//            {
//                case INIT:
//                    if (format != null) init();
//                    break;
//                case REINIT_ON_CLOSE:
//                    break;
//                case OPEN_AND_START:
//                    synchronized (this)
//                    {
//                        targetState = START_ON_OPEN;
//                    }
//                    try
//                    {
//                        listener.initialisationProgress(-1);
//                        outputLine.open(format, bufferSize);
//                        listener.initialisationProgress(0);
//                    }
//                    catch (LineUnavailableException lue)
//                    {
//                        System.out.println(lue.getMessage());
//                        sleep();
//                    }
//                    break;
//                case OPEN:
//                    synchronized (this)
//                    {
//                        targetState = WAITING_FOR_OPEN;
//                    }
//                    try
//                    {
//                        listener.initialisationProgress(-1);
//                        outputLine.open(format, bufferSize);
//                        listener.initialisationProgress(0);
//                    }
//                    catch (LineUnavailableException lue)
//                    {
//                        System.out.println(lue.getMessage());
//                        sleep();
//                    }
//                    break;
//                    // the rest can fall through
//                case CLOSE:
//                    synchronized (this)
//                    {
//                        targetState = WAITING_FOR_CLOSE;
//                    }
//                    outputLine.close();                    
//                    break;
//                case START:
//                    synchronized (this)
//                    {
//                        targetState = WAITING_FOR_START;
//                    }
//                    outputLine.start();
//                    break;
//                case CLOSED:
//                case WAITING_FOR_OPEN:
//                case START_ON_OPEN:
//                case WAITING_FOR_START:
//                case IS_OPEN:
//                case WAITING_FOR_STOP:
//                case PLAYING:
//                case WAITING_FOR_CLOSE:
//                case CLOSE_ON_STOP:
//                    sleep();
//                    break;
//                default:
//                    System.out.println("OutputLineController Unknown state: " 
//                        + targetState);
//                    sleep();
//            }
//        }
//        System.out.println("OutputLineController thread finished");

    
    public synchronized boolean open()
    {
        setTarget(OPEN_BIT);
        return actualState.get(OPEN_BIT);
    }
    
    public synchronized boolean close()
    {
        clearTarget(DRAIN_BIT);
        clearTarget(PLAY_BIT);
        clearTarget(OPEN_BIT);
        return !actualState.get(OPEN_BIT);
    }
    
    public synchronized boolean keepPlaying()
    {
        clearTarget(DRAIN_BIT);
        setTarget(PLAY_BIT);
        setTarget(OPEN_BIT);
        return actualState.get(PLAY_BIT);
    }
    
    public void playToEnd()
    {
        setTarget(DRAIN_BIT);
        setTarget(PLAY_BIT);
        setTarget(OPEN_BIT);
    }
    
    public void stop()
    {
        clearTarget(PLAY_BIT);
        clearTarget(DRAIN_BIT);
    }
    
    public void flush()
    {
        setTarget(FLUSH_BIT);
    }
    
    public synchronized boolean isOpen()
    {
        return (actualState.get(OPEN_BIT) && !targetState.get(REINIT_BIT));
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
    
    public synchronized int write(byte [] data, int offset, int length)
    {
        int wrote = 0;         
        if (actualState.get(OPEN_BIT))
        {
            // prevent blocking by checking available
            int lengthToWrite = outputLine.available();
            if (lengthToWrite > length) lengthToWrite = length;
            wrote = outputLine.write(data, offset, lengthToWrite);
        }
        return wrote;
    }
    
    public synchronized int writeSpaceAvailable()
    {
        int writeSpace = 0;
        if (actualState.get(OPEN_BIT))
        {
            writeSpace = outputLine.available();
        }
        return writeSpace;
    }
    
    public synchronized int getBufferSize()
    {
        int writeSpace = 0;
        if (actualState.get(OPEN_BIT))
        {
            writeSpace = outputLine.getBufferSize();
        }
        return writeSpace;
    }
    
    public synchronized void setListener(AudioPlayListener listener)
    {
        this.listener = listener;
    }
    
    protected String stateToString(BitSet s)
    {
        StringBuffer stateString = new StringBuffer();
        if (s.get(REINIT_BIT))
        {
            stateString.append("REINIT,");
        }
        if (s.get(OPEN_BIT))
        {
            stateString.append("OPEN,");
        }
        else
        {
            stateString.append("CLOSED,");
        }
        if (s.get(PLAY_BIT))
        {
            stateString.append("STARTED");
        }
        else
        {
            stateString.append("STOPPED");
        }
        if (s.get(DRAIN_BIT))
        {
            stateString.append(",DRAIN");
        }
        return stateString.toString();
    }
    
    public synchronized boolean isActive()
    {
        System.out.println(outputLine.isActive() + " R" + 
            outputLine.isActive() + " Req " + requestedState.get(PLAY_BIT) +
            " Drain" + requestedState.get(DRAIN_BIT) + " Act" + 
            actualState.get(PLAY_BIT));
        if (outputLine.isActive())
        {
            return true;
        }
        return false;
    }
}
