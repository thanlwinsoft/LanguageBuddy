/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.Perspective;

/**
 * @author keith
 *
 */
public class StartTestWizard extends Wizard
{
    private ModuleSelectionPage moduleSelectionPage = null;  //  @jve:decl-index=0:
    private TestTypePage testTypePage = null;
    
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
        addPage(testTypePage);
        addPage(moduleSelectionPage);
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
        IViewPart testView = page.findView(Perspective.TEST_VIEW);
        try
        {
            page.showView(Perspective.TEST_VIEW);
            IViewReference viewRef = (IViewReference)page.getReference(testView);
            
            if (page.isPageZoomed() == false)
            {
                // should we add a page listener to watch for when the zoom is lost?
                page.toggleZoom(viewRef);
            }
        }
        catch (PartInitException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING,"startTest failed",e);
        }
        return true;
    }
}
