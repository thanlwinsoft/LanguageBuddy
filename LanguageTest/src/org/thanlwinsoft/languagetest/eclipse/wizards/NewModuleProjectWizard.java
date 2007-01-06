/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.views.navigator.ResourceNavigator;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.Perspective;

/**
 * @author keith
 *
 */
public class NewModuleProjectWizard extends Wizard implements INewWizard
{
    private NewModuleProjectPage moduleProjectPage = null;
    public NewModuleProjectWizard()
    {
        super();
        setNeedsProgressMonitor(true);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    //@Override
    public void addPages()
    {
        moduleProjectPage = new NewModuleProjectPage();
        addPage(moduleProjectPage);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish()
    {
        final IProject project = moduleProjectPage.getProjectHandle();
        final String projectName = moduleProjectPage.getProjectName();
        if (project != null)
        {
            //IWorkspaceRunnable wr;
            IRunnableWithProgress op = new IRunnableWithProgress() {
                public void run(IProgressMonitor monitor) throws InvocationTargetException {
                    try {
                        doFinish(project, projectName, monitor);
                    } catch (CoreException e) {
                        throw new InvocationTargetException(e);
                    } finally {
                        monitor.done();
                    }
                }
            };
            try 
            {
                getContainer().run(true, false, op);
            } 
            catch (InterruptedException e) 
            {
                return false;
            } 
            catch (InvocationTargetException e) 
            {
                Throwable realException = e.getTargetException();
                MessageDialog.openError(getShell(), "Error", realException.getMessage());
                return false;
            }
            
            // attempt to find out why it doesn't work
            IViewPart vp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(Perspective.NAVIGATOR);
            if (vp instanceof ResourceNavigator)
            {
                ResourceNavigator rn = (ResourceNavigator)vp;
                IWorkspace workspace = ResourcesPlugin.getWorkspace(); 
                //rn.getViewer().setContentProvider(new FileContentProvider());
                //rn.getViewer().setLabelProvider(new ProjectFileLabelProvider());
                rn.selectReveal(new StructuredSelection(workspace.getRoot()));
                rn.getViewer().refresh();
            }
            // 
            IAdapterManager am = Platform.getAdapterManager();
            if (am.hasAdapter(project, IWorkbenchAdapter.class.getName()) == false)
            {
                
            }
        }
        return true;
    }
    public void doFinish(IProject project, String projectName, IProgressMonitor monitor) throws CoreException
    {
        if (project.exists() == false)
        {
            project.create(monitor);
        }

        project.open(monitor);
        IProjectDescription description = project.getDescription();
        String[] natures = description.getNatureIds();
        String[] newNatures = new String[natures.length + 1];
        System.arraycopy(natures, 0, newNatures, 0, natures.length);
        newNatures[natures.length] = "org.thanlwinsoft.languagetest.eclipse.natures.LanguageModuleNature";
        IWorkspace workspace = ResourcesPlugin.getWorkspace(); 
        IStatus status = workspace.validateNatureSet(natures);
        
        
        // check the status and decide what to do
        if (status.getCode() == IStatus.OK) 
        {
            description.setNatureIds(newNatures);
            
            project.setDescription(description, monitor);
        } 
        else 
        {
            // raise a user error
            LanguageTestPlugin.log(status.getCode(), "Error adding LanguageModuleNature");
        }
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        
    }
}
