/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * @author keith
 *
 */
public class StartTestAction extends Action implements IWorkbenchWindowActionDelegate
{
    private IWorkbenchWindow window = null;
    
    /**
     * The constructor.
     */
    public StartTestAction()
    {
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
     */
    public void dispose()
    {
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
     */
    public void init(IWorkbenchWindow window)
    {
        this.window = window;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action)
    {
        if (window == null)
        {
            window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        }
        StartTestWizard wizard = new StartTestWizard(); 
        WizardDialog wizardDialog = 
            new WizardDialog(window.getWorkbench().getActiveWorkbenchWindow().getShell(), 
                wizard);
        wizardDialog.setMinimumPageSize(300, 300);
        wizardDialog.open();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        
    }

}
