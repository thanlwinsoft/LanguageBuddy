package org.thanlwinsoft.languagetest.eclipse.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleDocument;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "lm.xml". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class NewLangModuleWizard extends Wizard implements INewWizard {
	private NewLangModuleWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for NewLangModuleWizard.
	 */
	public NewLangModuleWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new NewLangModuleWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		final String containerName = page.getContainerName();
		final String fileName = page.getFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() 
        {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try 
                {
					doFinish(containerName, fileName, monitor);
				} 
                catch (CoreException e) 
                {
                    e.printStackTrace();
					throw new InvocationTargetException(e);
				} 
                finally 
                {
					monitor.done();
				}
			}
		};
		try 
        {
			getContainer().run(true, false, op);
		} 
        catch (InterruptedException e) {
            e.printStackTrace();
			return false;
		} 
        catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", 
                                    realException.getMessage());
			return false;
		}
        try
        {
            ResourcesPlugin.getWorkspace().getRoot().refreshLocal(10, null);
        } 
        catch (CoreException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return true;
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	private void doFinish(
		String containerName,
		String fileName,
		IProgressMonitor monitor)
		throws CoreException {
		// create a sample file
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream();
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try 
                {
					IDE.openEditor(page, file, true);
				} 
                catch (PartInitException e) 
                {
				}
			}
		});
		monitor.worked(1);
	}
	
	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream openContentStream() 
    {
        InputStream is = null;
        try
        {
            
            LanguageModuleDocument doc = LanguageModuleDocument.Factory.newInstance();
            if (doc != null)
            {
                doc.addNewLanguageModule();
                doc.getLanguageModule().setCreationTime(new Date().getTime());
                doc.getLanguageModule().setId(Integer.toHexString(doc.hashCode()));
                XmlOptions options = new XmlOptions();
                options.setCharacterEncoding("UTF-8");
                options.setSavePrettyPrint();
                is = doc.newInputStream(options);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
          
        }
        if (is == null)
        {
            String contents =
                "This is the initial file contents for *.lmx file that should be word-sorted in the Preview page of the multi-page editor";
              is = new ByteArrayInputStream(contents.getBytes());
        }
        return is;
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "LanguageTest", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}