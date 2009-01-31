/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 *  MA 02110-1301 USA
 * -----------------------------------------------------------------------
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;


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
//            Object o = ((IStructuredSelection)selection).getFirstElement();
//            if (o instanceof IResource)
//            {
//                IContainer container = null;
//                if (o instanceof IContainer)
//                {
//                    container = (IContainer)o;
//                }
//                else
//                {
//                    container = ((IResource)o).getParent();
//                }
//            }
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
