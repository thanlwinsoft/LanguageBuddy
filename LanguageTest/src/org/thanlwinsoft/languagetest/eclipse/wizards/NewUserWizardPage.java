/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.thanlwinsoft.languagetest.MessageUtil;

/**
 * @author keith
 *
 */
public class NewUserWizardPage extends WizardPage implements ModifyListener
{
    private Shell shell = null;
    
    private Text userName = null;

    public NewUserWizardPage()
    {
        super("New User");
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        setTitle(MessageUtil.getString("NewUserPageTitle"));
        setDescription(MessageUtil.getString("NewUserPageDesc"));
        shell = parent.getShell();
        Group mainControl  = new Group(parent, SWT.SHADOW_ETCHED_IN);
        RowLayout mainLayout = new RowLayout();
        
        mainLayout.type = SWT.VERTICAL;
        mainLayout.fill = true;
        mainControl.setLayout(mainLayout);
        
        final Label userLabel = new Label(mainControl, SWT.LEFT | SWT.WRAP);
        userLabel.setText(MessageUtil.getString("UserName"));
        userName = new Text(mainControl, SWT.LEFT);
        userName.addModifyListener(this);
        
        final Label instructionsLabel = new Label(mainControl, SWT.LEFT | SWT.WRAP);
        instructionsLabel.setText(MessageUtil.getString("CreateUserInstructions"));
        this.setControl(mainControl);
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
     */
    public void modifyText(ModifyEvent e)
    {
        if (userName.getText() != null &&
            userName.getText().length() > 0)
        {
            IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject myProject = myWorkspaceRoot.getProject(getUserName());
            if (myProject.exists() == false)
            {
                setDescription(MessageUtil.getString("NewUserPageDesc"));
                this.setPageComplete(true);
            }
            else
            {
                setDescription(MessageUtil.getString("UserExists", 
                                                     userName.getText()));
                setPageComplete(false);
            }
        }
        else setPageComplete(false);
    }
    
    public String getUserName()
    {
        return userName.getText();
    }
}
