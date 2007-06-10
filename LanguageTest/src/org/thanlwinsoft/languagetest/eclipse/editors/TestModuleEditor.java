/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/LanguageTest/src/org/thanlwinsoft/languagetest/eclipse/editors/TestModuleEditor.java $
 *  Revision        $LastChangedRevision: 855 $
 *  Last Modified:  $LastChangedDate: 2007-06-10 07:02:09 +0700 (Sun, 10 Jun 2007) $
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
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
//import org.apache.xmlbeans.xml.stream.Comment;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
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
import org.thanlwinsoft.languagetest.eclipse.workspace.WorkspaceLanguageManager;
import org.thanlwinsoft.languagetest.language.test.XmlBeansTestModule;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;
import org.w3c.dom.Document;
import org.w3c.dom.Comment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
    public final static String LANG_MODULE_FORMAT_VERSION = "2.0.0";
    
    public final static String VERSION = 
        LanguageTestPlugin.getDefault().getBundle().getHeaders().get("Bundle-Version").toString();
    public final static String PLATFORM = Platform.getOS() + " " + Platform.getOSArch(); 
    
    public TestModuleEditor()
    {
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.MultiPageEditorPart#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter)
    {
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
            currentDoc.getLanguageModule().setFormatVersion(LANG_MODULE_FORMAT_VERSION);
            String userName = "";
            IProject userProject = WorkspaceLanguageManager.getUserProject(); 
            if (userProject != null) userName = userProject.getName();
            String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date());

            String commentText = MessageUtil.getString("LastWritten", 
                    LanguageTestPlugin.getDefault().toString(), VERSION, 
                    PLATFORM, date, userName);
            Document domDoc = currentDoc.getLanguageModule().getDomNode().getOwnerDocument();
            
            if (domDoc != null)
            {
                String uri = currentDoc.getLanguageModule().getDomNode().getNamespaceURI();
                String langModuleElementName = 
                    currentDoc.getLanguageModule().getDomNode().getLocalName();
                NodeList nodes = domDoc.getElementsByTagNameNS(uri, 
                        langModuleElementName);
                Comment comment = null;
                if (nodes != null && nodes.getLength() > 0)
                {
                    Node nodeBefore = nodes.item(0).getPreviousSibling();
                    if (nodeBefore != null && nodeBefore.getNodeType() == Node.COMMENT_NODE)
                    {
                        comment = (Comment)nodeBefore;
                        comment.setData(commentText);
                    }
                }
                if (comment == null)
                {
                    comment = domDoc.createComment(commentText);
                    domDoc.insertBefore(comment, 
                        currentDoc.getLanguageModule().getDomNode());
                }
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
                if (input == null)
                {
                    LanguageTestPlugin.log(IStatus.ERROR, "No input");
                    return;
                }
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
            setActivePage(1);
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
