/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.FontData;
import org.thanlwinsoft.schemas.languagetest.ForeignLangType;
import org.thanlwinsoft.schemas.languagetest.NativeLangType;
import org.thanlwinsoft.schemas.languagetest.TestItemType;

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
    protected int playStart = 0;
    protected int playEnd = -1;
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
        if (ti.isSetSoundFile())
        {
            soundFile = new Path(ti.getSoundFile().getStringValue());
            playStart = ti.getSoundFile().getStart();
            playEnd = ti.getSoundFile().getEnd();
        }
        if (ti.isSetImg())
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
    
    public int getPlayStart()
    {
        return playStart;
    }
    public int getPlayEnd()
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
