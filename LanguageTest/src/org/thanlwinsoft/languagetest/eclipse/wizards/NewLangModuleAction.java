/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;


import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * @author keith
 *
 */
public class NewLangModuleAction implements IWorkbenchWindowActionDelegate
{
    private IWorkbenchWindow window;
    private ISelection selection;
    /**
     * The constructor.
     */
    public NewLangModuleAction()
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
        if (window == null)
        {
            window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        }
        NewLangModuleWizard wizard = new NewLangModuleWizard(); 
        WizardDialog wizardDialog = 
            new WizardDialog(window.getWorkbench().getActiveWorkbenchWindow().getShell(), 
                wizard);
        if (selection != null && selection instanceof IStructuredSelection)
        {
            Object o = ((IStructuredSelection)selection).getFirstElement();
            if (o instanceof IResource)
            {
                IContainer container = null;
                if (o instanceof IContainer)
                {
                    container = (IContainer)o;
                }
                else
                {
                    container = ((IResource)o).getParent();
                }
            }
            wizard.init(window.getWorkbench(), (IStructuredSelection)selection);
        }
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
        this.selection = selection;
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
