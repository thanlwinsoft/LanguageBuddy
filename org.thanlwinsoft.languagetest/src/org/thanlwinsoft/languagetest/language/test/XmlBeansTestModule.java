/* -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/language/test/XmlBeansTestModule.java $
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
package org.thanlwinsoft.languagetest.language.test;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType;

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
    public final static String XSL_FILENAME = "LanguageTest.xsl";
    public final static String XSL_TARGET = "xml-stylesheet";
    public final static String XSL_DATA = "href=\"" + XSL_FILENAME + "\" type=\"text/xsl\"";
    public final static String FOLDER_EXT = ".lmf";
    
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
