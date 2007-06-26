/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/eclipse/WorkbenchProject.java $
 *  Revision        $LastChangedRevision: 853 $
 *  Last Modified:  $LastChangedDate: 2007-06-09 23:56:07 +0700 (Sat, 09 Jun 2007) $
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
package org.thanlwinsoft.eclipse;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IProjectActionFilter;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.natures.LanguageModuleNature;
import org.thanlwinsoft.languagetest.eclipse.natures.LanguageUserNature;

/**
 * An IWorkbenchAdapter that represents IProject.
 */
public class WorkbenchProject extends WorkbenchResource implements
        IProjectActionFilter 
{
    HashMap<String, String> natureMap = new HashMap<String, String>();
    /**
     *  Answer the appropriate base image to use for the passed resource, optionally
     *  considering the passed open status as well iff appropriate for the type of
     *  passed resource
     */
    protected ImageDescriptor getBaseImage(IResource resource) 
    {
        IProject project = (IProject) resource;
        boolean isOpen = project.isOpen();
        String baseKey = isOpen ? ISharedImages.IMG_OBJ_FOLDER
                : ISharedImages.IMG_OBJ_ELEMENT;
        
        try 
        {
            if (isOpen)
            {
                if (project.getDescription().hasNature(LanguageModuleNature.ID))
                {
                    return LanguageTestPlugin.getImageDescriptor("/icons/moduleProject.png");
                    
                }
                else if (project.getDescription().hasNature(LanguageUserNature.ID))
                {
                    return LanguageTestPlugin.getImageDescriptor("/icons/user.png");
                }
            }
            else
            {
                if (!natureMap.containsKey(project.getName()))
                {
                    IJobManager jobMan = Job.getJobManager();
                    IProgressMonitor pm = jobMan.createProgressGroup();
                    try
                    {
                        pm.beginTask("Project Nature", IProgressMonitor.UNKNOWN);
                        project.open(pm);
                        IProjectDescription desc = project.getDescription(); 
                        if (desc.hasNature(LanguageModuleNature.ID))
                        {
                            natureMap.put(project.getName(), LanguageModuleNature.ID);
                        }
                        else if (desc.hasNature(LanguageUserNature.ID))
                        {
                            natureMap.put(project.getName(), LanguageUserNature.ID);
                        }
                        else
                        {
                            natureMap.put(project.getName(), "");
                        }
                        project.close(pm);
                    }
                    finally
                    {
                        pm.done();
                    }
                }
                String nature = natureMap.get(project.getName()).toString();
                if (nature.equals(LanguageModuleNature.ID))
                {
                    return LanguageTestPlugin.getImageDescriptor("/icons/moduleClosed.png");
                }
                else if (nature.equals(LanguageUserNature.ID))
                {
                    return LanguageTestPlugin.getImageDescriptor("/icons/userClosed.png");
                }
            }
        } 
        catch (CoreException e) 
        {
            LanguageTestPlugin.log(IStatus.WARNING, "Error reading nature", e);
        }
        
        return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(baseKey);
    }

    /**
     * Returns the children of this container.
     */
    public Object[] getChildren(Object o) 
    {
        IProject project = (IProject) o;
        if (project.isOpen()) 
        {
            try 
            {
                return project.members();
            } 
            catch (CoreException e) 
            {
                //don't get the children if there are problems with the project
            }
        }
        return NO_CHILDREN;
    }

    /**
     * Returns whether the specific attribute matches the state of the target
     * object.
     *
     * @param target the target object
     * @param name the attribute name
     * @param value the attriute value
     * @return <code>true</code> if the attribute matches; <code>false</code> otherwise
     */
    public boolean testAttribute(Object target, String name, String value) 
    {
        if (!(target instanceof IProject)) 
        {
            return false;
        }
        IProject proj = (IProject) target;
        if (name.equals(NATURE)) 
        {
            try {
                return proj.isAccessible() && proj.hasNature(value);
            } 
            catch (CoreException e) 
            {
                return false;
            }
        } else if (name.equals(OPEN)) {
            value = value.toLowerCase();
            return (proj.isOpen() == value.equals("true"));//$NON-NLS-1$
        }
        return super.testAttribute(target, name, value);
    }
}
