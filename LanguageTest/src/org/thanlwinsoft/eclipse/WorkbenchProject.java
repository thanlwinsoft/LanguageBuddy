/**
 * 
 */
package org.thanlwinsoft.eclipse;
/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
//package org.eclipse.ui.internal.ide.model;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IProjectActionFilter;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
//import org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages;
//import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
//import org.eclipse.ui.internal.ide.misc.OverlayIcon;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.natures.LanguageModuleNature;
import org.thanlwinsoft.languagetest.eclipse.natures.LanguageUserNature;

/**
 * An IWorkbenchAdapter that represents IProject.
 */
public class WorkbenchProject extends WorkbenchResource implements
        IProjectActionFilter {
    HashMap imageCache = new HashMap(11);
    HashMap natureMap = new HashMap();
    /**
     *  Answer the appropriate base image to use for the passed resource, optionally
     *  considering the passed open status as well iff appropriate for the type of
     *  passed resource
     */
    protected ImageDescriptor getBaseImage(IResource resource) {
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
                    IJobManager jobMan = Platform.getJobManager();
                    IProgressMonitor pm = jobMan.createProgressGroup();
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
//            String[] natureIds = project.getDescription().getNatureIds();
//            for (int i = 0; i < natureIds.length; ++i) {
//                // Have to use a cache because OverlayIcon does not define its own equality criteria,
//                // so WorkbenchLabelProvider would always create a new image otherwise.
//                String imageKey = natureIds[i];
//                ImageDescriptor overlayImage = (ImageDescriptor) imageCache
//                        .get(imageKey);
//                if (overlayImage != null) 
//                {
//                    return overlayImage;
//                }
//                    ImageDescriptor natureImage = IDEWorkbenchPlugin
//                            .getDefault().getProjectImageRegistry()
//                            .getNatureImage(natureIds[i]);
//                    if (natureImage != null) {
//                        ImageDescriptor baseImage = IDEInternalWorkbenchImages
//                                .getImageDescriptor(baseKey);
//                        overlayImage = new OverlayIcon(baseImage,
//                                new ImageDescriptor[][] { { natureImage } },
//                                new Point(16, 16));
//                        imageCache.put(imageKey, overlayImage);
//                        return overlayImage;
//                    }
//            }
        } 
        catch (CoreException e) 
        {
            LanguageTestPlugin.log(IStatus.WARNING, "Error reading nature", e);
        }
        
        return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(baseKey);
        //return IDEInternalWorkbenchImages.getImageDescriptor(baseKey);
    }

    /**
     * Returns the children of this container.
     */
    public Object[] getChildren(Object o) {
        IProject project = (IProject) o;
        if (project.isOpen()) {
            try {
                return project.members();
            } catch (CoreException e) {
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
    public boolean testAttribute(Object target, String name, String value) {
        if (!(target instanceof IProject)) {
            return false;
        }
        IProject proj = (IProject) target;
        if (name.equals(NATURE)) {
            try {
                return proj.isAccessible() && proj.hasNature(value);
            } catch (CoreException e) {
                return false;
            }
        } else if (name.equals(OPEN)) {
            value = value.toLowerCase();
            return (proj.isOpen() == value.equals("true"));//$NON-NLS-1$
        }
        return super.testAttribute(target, name, value);
    }
}
