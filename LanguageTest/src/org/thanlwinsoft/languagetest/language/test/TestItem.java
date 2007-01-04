/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test;

import org.eclipse.core.runtime.IPath;

/**
 * @author keith
 *
 */
public interface TestItem
{

    

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

}
