/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * -----------------------------------------------------------------------
 */
/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.export;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * @author keith
 *
 */
public class ExportAction implements IWorkbenchWindowActionDelegate
{
    private IWorkbenchWindow window = null;
    private String defaultExtension = null;
    private ISelection selection = null;
    private IFile [] files = null;
    public ExportAction()
    {
        
    }
    public ExportAction(IFile file, String extension)
    {
        this.defaultExtension = extension;
        this.files = new IFile [] { file};
        StructuredSelection ss = new StructuredSelection(files);
        selection = ss;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run()
    {
        Shell shell = null;
        ExportWizard wizard = new ExportWizard(defaultExtension);
        
        if (window == null)
        {
            shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            if (selection instanceof IStructuredSelection)
            {
                wizard.init(PlatformUI.getWorkbench(), 
                        (IStructuredSelection)selection);
            }
        }
        else 
        {
            shell = window.getShell();
            if (selection instanceof IStructuredSelection)
            {
                wizard.init(window.getWorkbench(), (IStructuredSelection)selection);
            }
        }
        WizardDialog wizardDialog = new WizardDialog(shell, wizard);
        wizardDialog.setMinimumPageSize(100, 100);
        wizardDialog.open();
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
        run();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        this.selection =  selection;
    }

}
