/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/sound/AudioPlayer.java,v $
 *  Version:       $Revision: 1.4 $
 *  Last Modified: $Date: 2004/06/20 11:51:45 $
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

package languagetest.sound;

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
