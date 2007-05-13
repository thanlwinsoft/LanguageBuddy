/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;


import java.util.Properties;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;

/**
 * @author keith
 *
 */
public class NewUserProjectAction implements IWorkbenchWindowActionDelegate, IIntroAction
{
    private IWorkbenchWindow window;
    /**
     * The constructor.
     */
    public NewUserProjectAction()
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
        openWizard();
    }
    
    protected void openWizard()
    {
//      Create the wizard
        NewUserWizard wizard = new NewUserWizard(); 
        WizardDialog wizardDialog = 
            new WizardDialog(window.getWorkbench().getActiveWorkbenchWindow().getShell(), 
                wizard);
        wizardDialog.setMinimumPageSize(300, 300);
        //wizard.setDialog(wizardDialog);
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
    /* (non-Javadoc)
     * @see org.eclipse.ui.intro.config.IIntroAction#run(org.eclipse.ui.intro.IIntroSite, java.util.Properties)
     */
    public void run(IIntroSite site, Properties params)
    {
        this.window = site.getPage().getWorkbenchWindow();
        openWizard();
    }

}
