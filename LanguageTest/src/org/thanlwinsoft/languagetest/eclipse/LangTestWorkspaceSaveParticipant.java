/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/LanguageTest/src/org/thanlwinsoft/languagetest/eclipse/LangTestWorkspaceSaveParticipant.java $
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
package org.thanlwinsoft.languagetest.eclipse;

import java.io.File;

//import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

/**
 * @author keith
 *
 */
public class LangTestWorkspaceSaveParticipant implements ISaveParticipant
{

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.ISaveParticipant#doneSaving(org.eclipse.core.resources.ISaveContext)
     */
    public void doneSaving(ISaveContext context)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.ISaveParticipant#prepareToSave(org.eclipse.core.resources.ISaveContext)
     */
    public void prepareToSave(ISaveContext context) throws CoreException
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.ISaveParticipant#rollback(org.eclipse.core.resources.ISaveContext)
     */
    public void rollback(ISaveContext context)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.ISaveParticipant#saving(org.eclipse.core.resources.ISaveContext)
     */
    public void saving(ISaveContext context) throws CoreException
    {
        switch (context.getKind()) 
        {
        case ISaveContext.FULL_SAVE:
           LanguageTestPlugin myPluginInstance = LanguageTestPlugin.getDefault();
           // save the plug-in state
           int saveNumber = context.getSaveNumber();
           String saveFileName = "save-" + Integer.toString(saveNumber);
           File f = myPluginInstance.getStateLocation().append(saveFileName).toFile();
           // if we fail to write, an exception is thrown and we do not update the path
           myPluginInstance.writeImportantState(f);
           context.map(new Path("save"), new Path(saveFileName));
           context.needSaveNumber();
           break;
        case ISaveContext.PROJECT_SAVE:
           // get the project related to this save operation
           //IProject project = context.getProject();
           
           // save its information, if necessary
           break;
        case ISaveContext.SNAPSHOT:
           // This operation needs to be really fast because
           // snapshots can be requested frequently by the
           // workspace.
           break;
        }
    }

}
