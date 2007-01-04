/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test;

import org.eclipse.core.runtime.IPath;

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
    IPath getPath();

    /**
     * @return
     */
    int getUniqueId();

}
