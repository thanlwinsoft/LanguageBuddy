/**
 * 
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
import org.thanlwinsoft.schemas.languagetest.LanguageModuleDocument;

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
