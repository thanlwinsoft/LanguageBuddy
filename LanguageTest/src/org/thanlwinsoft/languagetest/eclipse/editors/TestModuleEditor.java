/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.editors;

import java.io.IOException;
import java.io.InputStream;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleDocument;

/**
 * @author keith
 *
 */
public class TestModuleEditor extends MultiPageEditorPart
    implements IReusableEditor
{
    private boolean isDirty = false;
    private LanguageModuleDocument currentDoc = null;
    private TestItemEditor testItemEditor = null;
    
    public TestModuleEditor()
    {
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave(IProgressMonitor monitor)
    {
        // TODO Auto-generated method stub
        if (getEditorInput() instanceof IFileEditorInput && currentDoc != null)
        {
            IFileEditorInput input = (IFileEditorInput)getEditorInput();
            XmlOptions options = new XmlOptions();
            options.setCharacterEncoding("UTF-8");
            options.setSavePrettyPrint();
            String errorMsg = "";
            try
            {
                input.getFile().setContents(currentDoc.newInputStream(options),
                                            0, monitor);
            }
            catch (CoreException e)
            {
                LanguageTestPlugin.log(IStatus.ERROR, "Error saving " +
                        input.getName(), e);
                errorMsg = e.getLocalizedMessage();
            }
            finally 
            {
                if (currentDoc == null)
                {
                    MessageBox mBox = new MessageBox(getSite().getShell(), 
                                                 SWT.ICON_ERROR | SWT.OK);
                    mBox.setText(MessageUtil.getString("TestModuleWriteError"));
                    mBox.setMessage(MessageUtil.getString("TestModuleWriteErrorMsg" +
                                    errorMsg, input.getName()));
                    mBox.open();
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    public void doSaveAs()
    {
        // TODO Auto-generated method stub

    }
    
    protected LanguageModuleDocument getDocument()
    {
        return this.currentDoc;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
     */
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException
    {
        setSite(site);
        setInput(input);
        //super.init(site, input);
    }

    protected void setDirty(boolean dirty)
    {
        isDirty = dirty;
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#isDirty()
     */
    public boolean isDirty()
    {
        // TODO Auto-generated method stub
        return isDirty;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    public boolean isSaveAsAllowed()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus()
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
     */
    protected void createPages()
    {
        try
        {
            testItemEditor = new TestItemEditor(this);
            addPage(testItemEditor, this.getEditorInput());
        }
        catch (PartInitException e)
        {
            LanguageTestPlugin.log(IStatus.ERROR, e.getLocalizedMessage(), e);
        }
    }
    
    public void setInput(IEditorInput input)
    {
        //if (input.equals(getEditorInput())) return;
        super.setInput(input);
        String errorMsg = "";
        try
        {
            currentDoc = null;
            if (input instanceof IStorageEditorInput)
            {
                IStorageEditorInput storageInput = (IStorageEditorInput)input;
                InputStream is = storageInput.getStorage().getContents();
                XmlOptions options = new XmlOptions();
                options.setCharacterEncoding("UTF-8");
                options.setLoadUseDefaultResolver();
                options.setDocumentType(LanguageModuleDocument.type);
                currentDoc = LanguageModuleDocument.Factory.parse(is);
                if (testItemEditor != null)
                {
                    testItemEditor.setModule(currentDoc);
                }
            }
            else
            {
                LanguageTestPlugin.log(IStatus.WARNING, "Unexpected input");
            }
        }
        catch (CoreException e)
        {
            LanguageTestPlugin.log(IStatus.ERROR, "Error setting input",e);
            errorMsg = e.getLocalizedMessage();
            currentDoc = null;
        }
        catch (XmlException e)
        {
            LanguageTestPlugin.log(IStatus.ERROR, "XML error " + 
                                   input.getName(), e);
            errorMsg = e.getLocalizedMessage();
            currentDoc = null;
        }
        catch (IOException e)
        {
            LanguageTestPlugin.log(IStatus.ERROR, "Error reading " + 
                    input.getName(), e);
            errorMsg = e.getLocalizedMessage();
            currentDoc = null;
        }
        finally 
        {
            if (currentDoc == null)
            {
                MessageBox mBox = new MessageBox(getSite().getShell(), 
                                             SWT.ICON_ERROR | SWT.OK);
                mBox.setText(MessageUtil.getString("TestModuleReadError"));
                mBox.setMessage(MessageUtil.getString("TestModuleReadErrorMsg",
                                input.getName()) + errorMsg);
                mBox.open();
            }
        }
    }

}
