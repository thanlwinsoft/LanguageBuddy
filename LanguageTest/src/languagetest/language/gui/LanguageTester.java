/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/LanguageTester.java,v $
 *  Version:       $Revision: 1.1.1.1 $
 *  Last Modified: $Date: 2003/06/06 13:40:39 $
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

import java.io.*;


import languagetest.sound.SimplePlayer;

/**
 *
 * @author  keith
 */
public class LanguageTester
{
    
    /** Creates a new instance of LanguageTester */
    public LanguageTester()
    {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        SimplePlayer player = new SimplePlayer();
       
        if (player.open(new File("/home/keith/Documents/Burma/Burmese/Recordings/Number0.wav")))
        {
            player.play();
            player.close();
        }
        else
        {
            System.out.println("Failed to setup file for playback\n");
        }
        System.out.println("Finished");
        System.exit(0);
    }
    
}
