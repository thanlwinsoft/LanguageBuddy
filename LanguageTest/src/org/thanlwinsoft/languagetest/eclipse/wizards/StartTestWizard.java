/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.Perspective;
import org.thanlwinsoft.languagetest.eclipse.editors.TestModuleEditor;
import org.thanlwinsoft.languagetest.eclipse.views.TestView;
import org.thanlwinsoft.languagetest.language.test.Test;
import org.thanlwinsoft.languagetest.language.test.TestManager;
import org.thanlwinsoft.languagetest.language.test.TestType;
import org.thanlwinsoft.languagetest.language.test.TestOptions;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.views.navigator.ResourceNavigator;

/**
 * @author keith
 *
 */
public class StartTestWizard extends Wizard
{
    private ModuleSelectionPage moduleSelectionPage = null;  //  @jve:decl-index=0:
    private TestTypePage testTypePage = null;
    private TagFilterPage tagFilterPage;
    
    public StartTestWizard()
    {
        super();
    }
    /**
     * Adding the page to the wizard.
     */
    public void addPages()
    {
        testTypePage = new TestTypePage("Test Type");
        moduleSelectionPage = new ModuleSelectionPage("Select Modules");
        tagFilterPage = new TagFilterPage();
        addPage(testTypePage);
        addPage(moduleSelectionPage);
        addPage(tagFilterPage);
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish()
    {
        if (testTypePage.isPageComplete() && 
            moduleSelectionPage.isPageComplete())
        {
            return startTest();
        }
        return false;
    }
    private boolean startTest()
    {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow(); 
        IWorkbenchPage page = window.getActivePage(); 
        try
        {
            page.showView(Perspective.TEST_VIEW);
            TestView testView = (TestView)page.findView(Perspective.TEST_VIEW);
            TestManager manager = new TestManager(
                    testTypePage.getUser(),
                    testTypePage.getNativeLanguage().getCode(),
                    testTypePage.getForeignLanguage().getCode());
            ScopedPreferenceStore prefs = testTypePage.getPrefStore(testTypePage.getUser());
            prefs.setValue(TestTypePage.NATIVE_TEST_LANG_PREF, testTypePage.getNativeLanguage().getCode());
            prefs.setValue(TestTypePage.FOREIGN_TEST_LANG_PREF, testTypePage.getForeignLanguage().getCode());
            
            try
            {
                prefs.save();
            }
            catch (IOException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, e.getLocalizedMessage(), e);
            }
            
            Test test = null;
            TestType testType = testTypePage.getTestType();
            TestOptions options = new TestOptions(testType);
            if (moduleSelectionPage.isRevisionTest())
            {
                test = manager.revisionTest(options);
            }
            else
            {
                test = manager.createTestFromModuleList
                    (moduleSelectionPage.getSelectedModules(), options);
            }
            if (test != null)
            {
                if (testTypePage.isSetMaxTestItems())
                    test.pruneTestToLimit(testTypePage.getMaxTestItems());
                IViewReference viewRef = (IViewReference)page.getReference(testView);
                if (viewRef != null && page.isPageZoomed() == false)
                {
                    // should we add a page listener to watch for when the zoom is lost?
                    page.toggleZoom(viewRef);
                }
                testView.startTest(manager, test);
            }
            else
            {
                MessageDialog.openInformation(window.getShell(),
                        MessageUtil.getString("NoTestItemsTitle"),
                        MessageUtil.getString("NoTestItemsDesc"));
                return false;
            }
           
        }
        catch (PartInitException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING,"startTest failed",e);
        }
        return true;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#createPageControls(org.eclipse.swt.widgets.Composite)
     */
    public void createPageControls(Composite pageContainer)
    {
        super.createPageControls(pageContainer);
        IWorkbenchPage page = PlatformUI.getWorkbench()
        .getActiveWorkbenchWindow().getActivePage();
        Object [] items = null;
        IWorkbenchPart activePart = page.getActivePart();
        if (activePart instanceof TestModuleEditor)
        {
            TestModuleEditor editor = (TestModuleEditor)activePart;
            if (editor.getEditorInput() instanceof IFileEditorInput)
            {
                items = new IFile[] {
                     ((IFileEditorInput)editor.getEditorInput()).getFile()
                };
            }
        }
        ResourceNavigator view = (ResourceNavigator)page.findView("org.eclipse.ui.views.ResourceNavigator");
        if (items == null && view != null)
        {
            ITreeSelection selection = (ITreeSelection)view.getTreeViewer().getSelection();
            if (selection != null)
            {
                items = selection.toArray();
            }
        }
        if (items != null) 
            moduleSelectionPage.select(items);
    }
}
