/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * @author keith
 *
 */
public class LanguageModuleNature implements IProjectNature
{
    private IProject project = null;
    public final static String ID = 
    	"org.thanlwinsoft.languagetest.eclipse.natures.LanguageModuleNature";
    public LanguageModuleNature()
    {
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.core.resources.IProjectNature#configure()
     */
    public void configure() throws CoreException
    {
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.IProjectNature#deconfigure()
     */
    public void deconfigure() throws CoreException
    {
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.IProjectNature#getProject()
     */
    public IProject getProject()
    {
        return project;
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
     */
    public void setProject(IProject project)
    {
        this.project = project;
    }

}
