/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/wizards/NewUserWizard.java $
 *  Revision        $LastChangedRevision: 1388 $
 *  Last Modified:  $LastChangedDate: 2009-01-31 19:32:00 +0700 (Sat, 31 Jan 2009) $
 *  Last Change by: $LastChangedBy: keith $
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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.workspace.WorkspaceLanguageManager;
import org.thanlwinsoft.schemas.languagetest.module.LangTypeType;

/**
 * @author keith
 * 
 */
public class NewUserWizard extends Wizard implements INewWizard

{
    private NewUserWizardPage userPage = null;

    private NewLanguagePage nativeLanguagePage = null;

    private NewLanguagePage foreignLanguagePage = null;

    /**
     * Constructor for NewLangModuleWizard.
     */
    public NewUserWizard()
    {
        super();
        setNeedsProgressMonitor(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages()
    {
        userPage = new NewUserWizardPage();
        addPage(userPage);
        nativeLanguagePage =
            new NewLanguagePage(
                MessageUtil.getString("NewNativeLangPageTitle"), MessageUtil
                    .getString("NewNativeLangPageDesc"), LangTypeType.NATIVE);
        foreignLanguagePage =
            new NewLanguagePage(MessageUtil
                .getString("NewForeignLangPageTitle"), MessageUtil
                .getString("NewForeignLangPageDesc"), LangTypeType.FOREIGN);

        addPage(nativeLanguagePage);
        addPage(foreignLanguagePage);
        super.addPages();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish()
    {
        if (userPage.getUserName() == null
            || nativeLanguagePage.getLangCode() == null
            || foreignLanguagePage.getLangCode() == null)
            return false;

        return createUserProject();
    }

    protected boolean createUserProject()
    {
        boolean success = false;
        try
        {
            IWorkspaceRoot myWorkspaceRoot =
                ResourcesPlugin.getWorkspace().getRoot();
            IProject myProject =
                myWorkspaceRoot.getProject(userPage.getUserName());
            if (myProject.exists() == false)
            {
                myProject.create(null);
            }
            else
            {
                Status status =
                    new Status(IStatus.ERROR, LanguageTestPlugin.ID,
                        IStatus.OK, MessageUtil.getString("UserExistsTitle"),
                        null);
                ErrorDialog.openError(getShell(), MessageUtil
                    .getString("UserExistsTitle"), MessageUtil
                    .getString("UserExistsDesc"), status);
                return false;
            }
            // open if necessary
            if (myProject.exists() && !myProject.isOpen())
                myProject.open(null);
            IProjectDescription description = myProject.getDescription();
            String[] natures = description.getNatureIds();
            String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 0, natures.length);
            newNatures[natures.length] =
                "org.thanlwinsoft.languagetest.eclipse.natures.LanguageUserNature";
            description.setNatureIds(newNatures);
            IJobManager jobMan = Job.getJobManager();
            IProgressMonitor pm = jobMan.createProgressGroup();
            myProject.setDescription(description, pm);
            WorkspaceLanguageManager.addLanguage(myProject,
                LangTypeType.NATIVE, nativeLanguagePage.getUL(),
                nativeLanguagePage.getFontData(), pm);
            WorkspaceLanguageManager.addLanguage(myProject,
                LangTypeType.FOREIGN, foreignLanguagePage.getUL(),
                foreignLanguagePage.getFontData(), pm);

            success = true;
        }
        catch (CoreException e)
        {
            LanguageTestPlugin.log(IStatus.ERROR,
                "Error creating user project", e);
        }
        return success;
    }

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		
	}
}
