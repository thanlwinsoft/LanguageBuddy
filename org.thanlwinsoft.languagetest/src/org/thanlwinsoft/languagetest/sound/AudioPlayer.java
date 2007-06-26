/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/sound/AudioPlayer.java,v $
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
/**
 *
 * @author  keith
 */
public interface AudioPlayer
{
    public boolean open(File file);
    public boolean open(File file, long msLengthHint, boolean forceReopen);
    public boolean close();
    public void play();
    public void rewind(long amount);
    public void fastForward(long amount);
    public void pause();
    public void stop();
    public void setBounds(long start, long duration);
    public void addPlayListener(AudioPlayListener listener);
    public File getCurrentFile();
    public long getStartMs();
    public long getDurationMs();
    public boolean isInitialised();
    public boolean isActive();
}
