/*
 * TestItemProperties.java
 *
 * Created on February 9, 2004, 4:35 PM
 */

package languagetest.language.test;

import java.io.File;
/**
 *
 * @author  keith
 */
public interface TestItemProperties
{
    public long getModuleCreationTime();
    public int getModuleId();
    public long getCreationTime();
    public String getCreator();
    public File getModuleFile();
    
}
