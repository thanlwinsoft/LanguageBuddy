/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/LanguageTest/src/org/thanlwinsoft/languagetest/eclipse/TestModuleAdapter.java $
 *  Revision        $LastChangedRevision: 852 $
 *  Last Modified:  $LastChangedDate: 2007-06-09 16:02:23 +0700 (Sat, 09 Jun 2007) $
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
 */
package org.thanlwinsoft.languagetest.eclipse;



import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.thanlwinsoft.languagetest.eclipse.editors.TestItemEditor;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.thanlwinsoft.schemas.languagetest.module.LangTypeType;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType;

/**
 * @author keith
 *
 */
public class TestModuleAdapter
{

    public static Font getFont(LanguageModuleType module, String langId)
    {
        Font font = null;
        LangType [] langs = module.getLangArray();
        for (int i = 0; i < langs.length; i++)
        {
            if (langs[i].getLang().equals(langId))
            {
                String faceName = langs[i].getFont(); 
                int size = langs[i].getFontSize().intValue();
                Display display = PlatformUI.getWorkbench().getDisplay();
                // it isn't wise to change the size, because windows doesn't
                // resize the table cells properly
                size = display.getSystemFont().getFontData()[0].getHeight();
                size = LanguageTestPlugin.getPrefStore().getInt(TestItemEditor.ROW_FONT_PREF);
                FontData fontData = new FontData(faceName, size, SWT.NORMAL);
                font = LanguageTestPlugin.getFont(fontData);
            }
        }
        return font;
    }
    
    public static LangType [] getLangs(LanguageModuleType module, LangTypeType.Enum type)
    {
        LangType[] enabled = module.getLangArray();
        HashSet langs = new HashSet();
        for (int i = 0; i < enabled.length; i++)
        {
            if (enabled[i].getType().equals(type))
            {
                langs.add(enabled[i]);
            }
        }
        return (LangType[])langs.toArray();
    }
}
