/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.WorkspaceLanguageManager;
import org.thanlwinsoft.schemas.languagetest.module.LangTypeType;

/**
 * @author keith
 *
 */
public class AddLanguageWizard extends Wizard
{
    private NewLanguagePage languagePage = null;
    private IStructuredSelection selection = null;
    public AddLanguageWizard()
    {
        super();
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages()
    {
        languagePage = new NewLanguagePage();
        addPage(languagePage);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizard#performFinish()
     */
    public boolean performFinish()
    {
        if (languagePage.isPageComplete())
        {
            if (selection == null)
            {
                ISelection baseSelection =
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
                if (baseSelection instanceof IStructuredSelection)
                {
                    selection = (IStructuredSelection)baseSelection;
                }
            }
            if (selection != null && selection.getFirstElement() instanceof IResource)
            {
                IResource resource = (IResource)selection.getFirstElement();
                IProject project = resource.getProject();
                if (project == null) return true;
                IJobManager jobMan = Platform.getJobManager();
                IProgressMonitor pm = jobMan.createProgressGroup();
                WorkspaceLanguageManager.addLanguage(project, 
                                languagePage.getLangType(), 
                                languagePage.getUL(), 
                                languagePage.getFontData(), pm);
                
            }
            return true;
        }
        return false;
    }
    
    /**
     * We will accept the selection in the workbench to see if
     * we can initialize from it.
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) 
    {
        this.selection = selection;
    }
}
