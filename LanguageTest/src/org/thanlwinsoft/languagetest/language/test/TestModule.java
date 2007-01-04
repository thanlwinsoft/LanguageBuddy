/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test;

import java.io.File;

/**
 * @author keith
 *
 */
public interface TestModule
{

    /**
     * @return
     */
    long getCreationTime();

    /**
     * @return
     */
    File getFile();

    /**
     * @return
     */
    int getUniqueId();

}
