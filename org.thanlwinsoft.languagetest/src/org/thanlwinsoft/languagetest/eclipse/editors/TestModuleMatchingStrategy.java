/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/editors/TestModuleMatchingStrategy.java $
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
package org.thanlwinsoft.languagetest.eclipse.editors;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument;

/**
 * @author keith
 *
 */
public class TestModuleMatchingStrategy implements IEditorMatchingStrategy
{
    public TestModuleMatchingStrategy() {}
    /* (non-Javadoc)
     * @see org.eclipse.ui.IEditorMatchingStrategy#matches(org.eclipse.ui.IEditorReference, org.eclipse.ui.IEditorInput)
     */
    public boolean matches(IEditorReference editorRef, IEditorInput input)
    {
        try
        {
            if (input instanceof IFileEditorInput)
            {
                IFileEditorInput fileInput = (IFileEditorInput)input;
                if (fileInput.getFile().getContentDescription().getContentType()
                    .getId().equals("org.eclipse.core.runtime.xml"))
                {
                	IContentDescription cd = fileInput.getFile().getContentDescription();
                    IContentType ct = cd.getContentType();
                    ct.getName();
                    try
                    {
                        LanguageModuleDocument.Factory.parse(fileInput.getFile().getContents());
                        return true;
                    } 
                    catch (XmlException e) {}
                    catch (IOException e) {}
                }
            }
        }
        catch (CoreException e) {};
        return false;
    }

}
