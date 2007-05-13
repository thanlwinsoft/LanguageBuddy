/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.wizard.Wizard;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.WorkspaceLanguageManager;
import org.thanlwinsoft.schemas.languagetest.module.LangTypeType;

/**
 * @author keith
 *
 */
public class NewUserWizard extends Wizard
{
    private NewUserWizardPage userPage = null;
    private NewLanguagePage nativeLanguagePage = null;
    private NewLanguagePage foreignLanguagePage = null;
    /**
     * Constructor for NewLangModuleWizard.
     */
    public NewUserWizard() {
        super();
        setNeedsProgressMonitor(true);
    }
    
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages()
    {
        userPage = new NewUserWizardPage();
        addPage(userPage);
        nativeLanguagePage = new NewLanguagePage(MessageUtil.getString("NewNativeLangPageTitle"),
                MessageUtil.getString("NewNativeLangPageDesc"),
                LangTypeType.NATIVE);
        foreignLanguagePage = new NewLanguagePage(MessageUtil.getString("NewForeignLangPageTitle"),
                MessageUtil.getString("NewForeignLangPageDesc"),
                LangTypeType.FOREIGN);
        
        addPage(nativeLanguagePage);
        addPage(foreignLanguagePage);
        super.addPages();
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish()
    {
        if (userPage.getUserName() == null ||
            nativeLanguagePage.getLangCode() == null ||
            foreignLanguagePage.getLangCode() == null)
            return false;
        
        return createUserProject();
    }
    protected boolean createUserProject()
    {
        boolean success = false;
        try
        {
        IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IProject myProject = myWorkspaceRoot.getProject(userPage.getUserName());
        if (myProject.exists() == false)
        {
            myProject.create(null);
        }
        else 
        {
            Status status = new Status(IStatus.ERROR, LanguageTestPlugin.ID,
                    IStatus.OK, MessageUtil.getString("UserExistsTitle"), null);
            ErrorDialog.openError(getShell(), 
                    MessageUtil.getString("UserExistsTitle"),
                    MessageUtil.getString("UserExistsDesc"), status);
            return false;
        }
        // open if necessary
        if (myProject.exists() && !myProject.isOpen())
           myProject.open(null);
        IProjectDescription description = myProject.getDescription();
        String[] natures = description.getNatureIds();
        String[] newNatures = new String[natures.length + 1];
        System.arraycopy(natures, 0, newNatures, 0, natures.length);
        newNatures[natures.length] = "org.thanlwinsoft.languagetest.eclipse.natures.LanguageUserNature";
        description.setNatureIds(newNatures);
        IJobManager jobMan = Platform.getJobManager();
        IProgressMonitor pm = jobMan.createProgressGroup();
        myProject.setDescription(description, pm);
        WorkspaceLanguageManager.addLanguage(myProject, LangTypeType.NATIVE, 
                nativeLanguagePage.getUL(), nativeLanguagePage.getFontData(), pm);
        WorkspaceLanguageManager.addLanguage(myProject, LangTypeType.FOREIGN, 
                foreignLanguagePage.getUL(), foreignLanguagePage.getFontData(), pm);
        
        
        success = true;
        }
        catch (CoreException e)
        {
            System.out.println(e);
        }
        return success;
    }
}
