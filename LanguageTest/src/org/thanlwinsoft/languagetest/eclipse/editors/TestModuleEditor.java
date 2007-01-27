/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.editors;

import java.io.IOException;
import java.io.InputStream;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IShowEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.Perspective;
import org.thanlwinsoft.languagetest.eclipse.views.RecordingView;
import org.thanlwinsoft.languagetest.eclipse.views.TestView;
import org.thanlwinsoft.languagetest.language.test.XmlBeansTestModule;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.TestItemType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

/**
 * @author keith
 *
 */
public class TestModuleEditor extends MultiPageEditorPart
    implements IReusableEditor, IShowEditorInput
{
    public final static int LANG_PAGE_INDEX = 0;
    public final static int TEST_ITEM_PAGE_INDEX = 1;
    private boolean isDirty = false;
    private LanguageModuleDocument currentDoc = null;
    private TestItemEditor testItemEditor = null;
    private ModuleLanguagePart languagePart = null;
    public final static String XSL_FILENAME = XmlBeansTestModule.XSL_FILENAME;
    public final static String XSL_TARGET = XmlBeansTestModule.XSL_TARGET;
    public final static String XSL_DATA = XmlBeansTestModule.XSL_DATA;
    
    public TestModuleEditor()
    {
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.MultiPageEditorPart#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter)
    {
        System.out.println(adapter.toString());
        if (adapter.equals(testItemEditor.getClass()) ||
            adapter.equals(ISelectionProvider.class))
            return testItemEditor;
        if (adapter.equals(languagePart.getClass()))
            return languagePart;
        return super.getAdapter(adapter);
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave(IProgressMonitor monitor)
    {
        if (getEditorInput() instanceof IFileEditorInput && currentDoc != null)
        {
            Document domDoc = currentDoc.getLanguageModule().getDomNode().getOwnerDocument();
            if (domDoc != null)
            {
                boolean hasXsl = false;
                if (domDoc.hasChildNodes())
                {
                    Node child = domDoc.getFirstChild();
                    while (child != null)
                    {
                        if (child.getNodeType() == 
                            Node.PROCESSING_INSTRUCTION_NODE &&
                            ((ProcessingInstruction)child).getTarget().equals(XSL_TARGET))
                        {
                            hasXsl = true;
                            break;
                        }
                        child = child.getNextSibling();
                    }
                }
                if (!hasXsl)
                {
                    Node xslNode = domDoc.createProcessingInstruction(XSL_TARGET, XSL_DATA);
                    domDoc.insertBefore(xslNode, domDoc.getFirstChild());
                }
            }
            IFileEditorInput input = (IFileEditorInput)getEditorInput();
            XmlOptions options = new XmlOptions();
            options.setCharacterEncoding("UTF-8");
            options.setSavePrettyPrint();
            String errorMsg = "";
            try
            {
                input.getFile().setContents(currentDoc.newInputStream(options),
                                            0, monitor);
                setDirty(false);
                firePropertyChange(PROP_DIRTY);
                IFile xslFile = input.getFile().getParent().getFile(new Path(XSL_FILENAME));
                if (!xslFile.exists())
                {
                    InputStream source = getClass().getResourceAsStream
                        ("/org/thanlwinsoft/languagetest/language/text/" + XSL_FILENAME);
                    xslFile.create(source, true, monitor);
                    source.close();
                }
            }
            catch (CoreException e)
            {
                LanguageTestPlugin.log(IStatus.ERROR, "Error saving " +
                        input.getName(), e);
                errorMsg = e.getLocalizedMessage();
            }
            catch (IOException e)
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

    }
    
    public LanguageModuleDocument getDocument()
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

    protected void setLanguageChanged()
    {
        setDirty(true);
        testItemEditor.setupLangColumns();
    }
    
    public void setDirty(boolean dirty)
    {
        if (dirty) updateTestView();
        isDirty = dirty;
        firePropertyChange(PROP_DIRTY);
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#isDirty()
     */
    public boolean isDirty()
    {
        return isDirty;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    public boolean isSaveAsAllowed()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus()
    {
        

    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
     */
    protected void createPages()
    {
        try
        {
            testItemEditor = new TestItemEditor(this);
            languagePart = new ModuleLanguagePart(this);
            addPage(languagePart, getEditorInput());
            addPage(testItemEditor, getEditorInput());
            setPageText(0, MessageUtil.getString("LanguagesTab"));
            setPageText(1, MessageUtil.getString("TestItemEditor"));
            if (getEditorInput() != null)
            {
                languagePart.setInput(getEditorInput());
                testItemEditor.setModule(currentDoc);
            }
            //setActivePage(1);
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
        setPartName(input.getName());
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
                if (languagePart != null)
                {
                    languagePart.setInput(input);
                }
                setPartName(input.getName());
                updateTestView();
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
    protected void updateTestView()
    {
        IViewPart testViewPart = getEditorSite().getPage()
            .findView(Perspective.TEST_VIEW);
        if (testViewPart != null)
        {
            TestView testView = (TestView)testViewPart;
            if (getDocument() != null && testItemEditor != null)
            {
                testView.setTestModule(getDocument().getLanguageModule());
                TestItemType item = testItemEditor.getSelectedItem();
                if (item != null)
                {
                    testView.setTestItem(item);
                }
            }
        }
        IViewPart recordingPart = getEditorSite().getPage().findView(RecordingView.ID);
        if (recordingPart instanceof ISelectionChangedListener)
        {
            ISelectionChangedListener scl = (ISelectionChangedListener)recordingPart;
            testItemEditor.addSelectionChangedListener(scl);
        }
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.IShowEditorInput#showEditorInput(org.eclipse.ui.IEditorInput)
     */
    public void showEditorInput(IEditorInput editorInput)
    {
        if (editorInput.equals(getEditorInput()) == false)
        {
            if (isDirty()) doSave(null);
            setInput(editorInput);
        }
    }
    /*
     * @see WorkbenchPart#firePropertyChange(int)
     */
    protected void firePropertyChange(int property) 
    {
        super.firePropertyChange(property);
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.MultiPageEditorPart#setActivePage(int)
     */
    public void setActivePage(int pageIndex)
    {
        super.setActivePage(pageIndex);
    }
    /** Select a specific TestItem e.g. from a search result */
    public void selectTestItem(int i)
    {
        testItemEditor.selectItem(i);
    }
}
