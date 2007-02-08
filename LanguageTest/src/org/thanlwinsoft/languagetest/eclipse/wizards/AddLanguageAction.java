/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * @author keith
 *
 */
public class AddLanguageAction implements IWorkbenchWindowActionDelegate
{
    private IWorkbenchWindow window = null;
    private ISelection selection = null;
    
    public AddLanguageAction()
    {
        super();
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
        Shell shell = null;
        AddLanguageWizard wizard = new AddLanguageWizard();
        if (window == null)
        {
            shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        }
        else shell = window.getShell();
        WizardDialog wizardDialog = new WizardDialog(shell, wizard);
        wizardDialog.setMinimumPageSize(400, 300);
        wizardDialog.open();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        this.selection = selection;
    }

}
