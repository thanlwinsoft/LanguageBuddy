/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/TestModuleUtilities.java,v $
 *  Version:       $Revision: 1.4 $
 *  Last Modified: $Date: 2004/05/06 16:24:56 $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2004 Keith Stribley <jungleglacier@snc.co.uk>
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

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DecimalFormat;
import javax.sound.sampled.AudioFormat;
import javax.swing.filechooser.FileFilter;


import languagetest.language.test.UserConfig;
import languagetest.language.test.TestItem;
import languagetest.language.test.TestModule;
/**
 *
 * @author  keith
 */
public class TestModuleUtilities
{
    // assume max file length is 31, 3 char for number, 4char for extension
    private static int MAX_NAME_LENGTH = 24;
    private static Pattern pattern = Pattern.compile("[^a-zA-Z0-9_\\- ]+");
    private static FileFilter moduleFileFilter = null;
    public final static String MODULE_FILE_EXTENSION = ".xml";

    /** Creates a new instance of TestModuleUtilities */
    public TestModuleUtilities()
    {
        
    }
    /** generate a sound file based on the current native text
     */
    public static File createSoundForTestItem(TestItem currentTestItem)
    {
        TestModule currentModule = currentTestItem.getModule();
        File newSoundFile = null;
        String name = currentTestItem.getNativeText();
        // strip problem characters
        Matcher m = pattern.matcher(name);
        name = m.replaceAll("");
        
        if (name.length() > MAX_NAME_LENGTH) 
        {
            name = name.substring(0, MAX_NAME_LENGTH);
        }
        else if (name.length() == 0) 
        {
            // default to the module name if name is empty
            name = currentModule.getFile().getName().substring(0, 
                currentModule.getFile().getName().indexOf('.'));
        }
        int index = 1;
        String ext = UserConfig.getCurrent().getDefaultSoundExtension();
        DecimalFormat formater = new DecimalFormat("000");
        File dataDir = getModuleDataDirectory(currentModule);
        String pathStem = File.separator + name;
        newSoundFile = new File(dataDir,
            pathStem + ext);
        while (newSoundFile.exists() )
        {
            newSoundFile = new File(dataDir,
            pathStem + formater.format(index) + ext);
            index++;
        }
        return newSoundFile;
    }
    
    public static File getModuleDataDirectory(TestModule currentModule)
    {
        String pathStem = currentModule.getFile().getName().substring(0,
            currentModule.getFile().getName()
            .lastIndexOf(MODULE_FILE_EXTENSION));
        File dir = new File(currentModule.getFile().getParentFile(),pathStem);
        // if dir does not exist create it now
        if (dir.exists() || dir.mkdirs()) return dir;
        else return null;
    }
    
    public static long roundOffsetToFrame(AudioFormat format, long newOffset)
    {
        float bytesPerFrame = (float)format.getFrameSize();
        return (long)(Math.floor((float)newOffset / 
                            (float)bytesPerFrame)) * format.getFrameSize();
    }
    
    public static FileFilter moduleFileFilter()
    {
        if (moduleFileFilter == null)
        {
            moduleFileFilter = new FileFilter() 
            {
                public boolean accept(File f)
                {
                    if (f.getName().toLowerCase().endsWith(MODULE_FILE_EXTENSION)) return true;
                    if (f.isDirectory())
                    {
                        // check this isn't a directory of a module
                        File modCheck = new File(f.getParent(),
                            f.getName() + MODULE_FILE_EXTENSION);
                        if (modCheck.exists()) return false;
                        return true;
                    }
                    return false;
                }
                public String getDescription() { return "XML Files"; }
            };
        }
        return moduleFileFilter;
    }
}
