/**
 * 
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
