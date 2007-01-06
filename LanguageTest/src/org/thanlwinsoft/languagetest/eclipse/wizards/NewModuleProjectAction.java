/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * @author keith
 *
 */
public class NewModuleProjectAction implements IWorkbenchWindowActionDelegate 
{
    private IWorkbenchWindow window;
    /**
     * The constructor.
     */
    public NewModuleProjectAction()
    {
    }
    /**
     * The action has been activated. The argument of the
     * method represents the 'real' action sitting
     * in the workbench UI.
     * @see IWorkbenchWindowActionDelegate#run
     */
    public void run(IAction action) 
    {
        //      Create the wizard
        
        NewModuleProjectWizard wizard = new NewModuleProjectWizard(); 
        WizardDialog wizardDialog = 
            new WizardDialog(window.getWorkbench().getActiveWorkbenchWindow().getShell(), 
                wizard);
        wizardDialog.setMinimumPageSize(400, 300);
        wizardDialog.open();
    }

    /**
     * Selection in the workbench has been changed. We 
     * can change the state of the 'real' action here
     * if we want, but this can only happen after 
     * the delegate has been created.
     * @see IWorkbenchWindowActionDelegate#selectionChanged
     */
    public void selectionChanged(IAction action, ISelection selection) 
    {
    }

    /**
     * We can use this method to dispose of any system
     * resources we previously allocated.
     * @see IWorkbenchWindowActionDelegate#dispose
     */
    public void dispose() 
    {
    }

    /**
     * We will cache window object in order to
     * be able to provide parent shell for the message dialog.
     * @see IWorkbenchWindowActionDelegate#init
     */
    public void init(IWorkbenchWindow window) 
    {
        this.window = window;
    }

}
