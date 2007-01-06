/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleType;

/**
 * XmlBeansTestModule holds the details about a module for use in a test.
 * The module itself is not retained in memory after construction, but the 
 * details in the module are sufficient to reload the module.
 * @author keith
 *
 */
public class XmlBeansTestModule implements TestModule
{
    int uniqueId = -1;
    long creationTime = -1;
    IPath path = null;
    public XmlBeansTestModule(IPath path) throws XmlException, IOException
    {
        BufferedInputStream is = 
            new BufferedInputStream(new FileInputStream(path.toFile()));
        LanguageModuleDocument doc = 
            LanguageModuleDocument.Factory.parse(is);
        load(path, doc.getLanguageModule());
    }
    public XmlBeansTestModule(IPath path, LanguageModuleType module)
    {
        load(path, module);
    }
    
    private void load(IPath path, LanguageModuleType module)
    {
        creationTime = module.getCreationTime();
        try
        {
            uniqueId = Integer.parseInt(module.getId(),16);
        }
        catch (NumberFormatException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, e.getLocalizedMessage(),e);
        }
        this.path = path;
    }
    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestModule#getCreationTime()
     */
    public long getCreationTime()
    {
        return creationTime;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestModule#getFile()
     */
    public IPath getPath()
    {
        return path;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestModule#getUniqueId()
     */
    public int getUniqueId()
    {
        return uniqueId;
    }
    
    
}
