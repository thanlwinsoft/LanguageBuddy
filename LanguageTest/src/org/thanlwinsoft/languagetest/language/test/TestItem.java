/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.graphics.FontData;

/**
 * @author keith
 *
 */
public interface TestItem
{
    /**
     * @return
     */
    long getModuleCreationTime();
    /**
     * @return
     */
    int getModuleId();
    /**
     * @return
     */
    long getCreationTime();

    /**
     * @return
     */
    String getCreator();

    /**
     * @return
     */
    String getForeignText();

    /**
     * @return
     */
    String getNativeText();

    /**
     * @return
     */
    IPath getSoundFile();

    /**
     * @return
     */
    int getTestCount();

    /**
     * @return
     */
    boolean isPassed();

    /**
     * 
     */
    void reset();

    /**
     * 
     */
    void setPassed(boolean pass);
    
    /** Retieve the font data to render the native text
     * @return FontData or null
     */
    FontData getNativeFontData();
    /** Retrieve the font data to render the foreign text 
     * May be null.
     * @return FontData or null
     */
    FontData getForeignFontData();
    /**
     * @return
     */
    IPath getModulePath();
    

}
