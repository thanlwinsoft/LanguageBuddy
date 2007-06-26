/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/editors/TestModuleActions.java $
 *  Revision        $LastChangedRevision: 852 $
 *  Last Modified:  $LastChangedDate: 2007-06-09 16:02:23 +0700 (Sat, 09 Jun 2007) $
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
package org.thanlwinsoft.languagetest.eclipse.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

/**
 * @author keith
 *
 */
public class TestModuleActions extends MultiPageEditorActionBarContributor
{
    private Action copyAction; 
    private Action saveAction;
    private TestModuleEditor editor = null;
    private IEditorPart page = null;
    
    public TestModuleActions()
    {
        copyAction = new Action() {
            public void run()
            {
                //textViewer.getTextWidget().copy();
                // TODO: do something useful
                if (page instanceof TestItemEditor)
                {
                    
                }
            }
        };
        saveAction = new Action() {
            public void run()
            {
                if (editor != null)
                {
                    IJobManager jobMan = Platform.getJobManager();
                    IProgressMonitor pm = jobMan.createProgressGroup();
                    editor.doSave(pm);
                }
            }
        };
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToMenu(org.eclipse.jface.action.IMenuManager)
     */
    public void contributeToMenu(IMenuManager menuManager)
    {
        super.contributeToMenu(menuManager);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorActionBarContributor#init(org.eclipse.ui.IActionBars, org.eclipse.ui.IWorkbenchPage)
     */
    public void init(IActionBars bars, IWorkbenchPage page)
    {        
        bars.setGlobalActionHandler(ActionFactory.SAVE.getId(), saveAction);
        bars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);
        super.init(bars, page);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorActionBarContributor#setActiveEditor(org.eclipse.ui.IEditorPart)
     */
    public void setActiveEditor(IEditorPart targetEditor)
    {
        if (targetEditor instanceof TestModuleEditor)
        {
            editor = (TestModuleEditor)targetEditor;
        }
        else
        {
            editor = null;
        }
        
        super.setActiveEditor(targetEditor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.MultiPageEditorActionBarContributor#setActivePage(org.eclipse.ui.IEditorPart)
     */
    public void setActivePage(IEditorPart activeEditor)
    {
        page = activeEditor;
    }
    
}
