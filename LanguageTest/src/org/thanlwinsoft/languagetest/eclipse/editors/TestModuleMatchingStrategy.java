/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;

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
                    return true;
                }
            }
        }
        catch (CoreException e) {};
        return false;
    }

}
