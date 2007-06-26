/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/language/test/XmlBeansTestItem.java $
 *  Revision        $LastChangedRevision: 855 $
 *  Last Modified:  $LastChangedDate: 2007-06-10 07:02:09 +0700 (Sun, 10 Jun 2007) $
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
package org.thanlwinsoft.languagetest.language.test;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.FontData;
import org.thanlwinsoft.schemas.languagetest.module.ForeignLangType;
import org.thanlwinsoft.schemas.languagetest.module.NativeLangType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;

/**
 * @author keith
 *
 */
public class XmlBeansTestItem implements TestItem
{
    protected int moduleId;
    protected long moduleCreationTime;
    protected IPath modulePath;
    protected long creationTime;
    protected String creator;
    protected String foreignText;
    protected String nativeText;
    protected int testCount = 0;
    protected boolean passed = false;
    protected IPath soundFile = null;
    protected long playStart = 0;
    protected long playEnd = -1;
    protected IPath imagePath = null;
    protected FontData nativeFontData = null;
    protected FontData foreignFontData = null;
    
    public XmlBeansTestItem(TestItemType ti, String nativeLang, String foreignLang)
    {
        creationTime = ti.getCreationTime();
        creator = ti.getCreator();
        NativeLangType [] nit = ti.getNativeLangArray();
        for (int i = 0; i < nit.length; i++)
        {
            if (nit[i].getLang().equals(nativeLang))
            {
                nativeText = nit[i].getStringValue();
                break;
            }
        }
        ForeignLangType [] fit = ti.getForeignLangArray();
        for (int i = 0; i < fit.length; i++)
        {
            if (fit[i].getLang().equals(foreignLang))
            {
                foreignText = fit[i].getStringValue();
                break;
            }
        }
        if (ti.isSetSoundFile() && ti.getSoundFile().getStringValue() != null && 
            ti.getSoundFile().getStringValue().length() > 0)
        {
            soundFile = new Path(ti.getSoundFile().getStringValue());
            playStart = ti.getSoundFile().getStart();
            playEnd = ti.getSoundFile().getEnd();
        }
        if (ti.isSetImg() && ti.getImg() != null && ti.getImg().length() > 0)
        {
            imagePath = new Path(ti.getImg());
        }
    }
    public void setNativeFontData(FontData fd)
    {
        nativeFontData = fd;
    }
    public void setForeignFontData(FontData fd)
    {
        foreignFontData = fd;
    }
    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItem#getCreationTime()
     */
    public long getCreationTime()
    {
        
        return creationTime;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItem#getCreator()
     */
    public String getCreator()
    {
        return creator;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItem#getForeignText()
     */
    public String getForeignText()
    {
        return foreignText;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItem#getNativeText()
     */
    public String getNativeText()
    {
        return nativeText;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItem#getSoundFile()
     */
    public IPath getSoundFile()
    {
        return soundFile;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItem#getTestCount()
     */
    public int getTestCount()
    {
        return testCount;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItem#isPassed()
     */
    public boolean isPassed()
    {
        return passed;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItem#reset()
     */
    public void reset()
    {
        testCount = 0;
        passed = false;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItem#setPassed(boolean)
     */
    public void setPassed(boolean pass)
    {
        testCount++;
        passed = pass;
    }
    
    public long getPlayStart()
    {
        return playStart;
    }
    public long getPlayEnd()
    {
        return playEnd;
    }
    public IPath getImagePath()
    {
        return imagePath;
    }
    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItem#getForeignFontData()
     */
    public FontData getForeignFontData()
    {
        return foreignFontData;
    }
    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItem#getNativeFontData()
     */
    public FontData getNativeFontData()
    {
        return nativeFontData;
    }
    protected void setModule(TestModule module)
    {
        moduleCreationTime = module.getCreationTime();
        moduleId = module.getUniqueId();
        modulePath = module.getPath();
    }
    public int getModuleId() { return moduleId; }
    public long getModuleCreationTime() { return moduleCreationTime; }
    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItem#getModulePath()
     */
    
    public IPath getModulePath()
    {
        return modulePath;
    }
    
    
}
