/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse;

import java.io.File;

import org.eclipse.core.resources.IProject;
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
           IProject project = context.getProject();
           
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
