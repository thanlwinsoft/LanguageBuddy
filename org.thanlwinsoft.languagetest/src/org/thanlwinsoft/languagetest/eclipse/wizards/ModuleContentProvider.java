/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/wizards/ModuleContentProvider.java $
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

import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.natures.LanguageModuleNature;
import org.thanlwinsoft.languagetest.eclipse.natures.LanguageUserNature;
import org.thanlwinsoft.languagetest.language.test.TestManager;
import org.thanlwinsoft.languagetest.language.test.XmlBeansTestModule;

/**
 * @author keith
 *
 */
public class ModuleContentProvider implements ITreeContentProvider
{
    public final static String EXTENSION = NewLangModuleWizardPage.EXTENSION;
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose()
    {
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement)
    {
        Object [] children = null;
        try
        {
            if (parentElement instanceof IContainer)
            {
                IResource[] members = ((IContainer)parentElement).members();
                Vector<IResource> foldersModules = new Vector<IResource>(members.length);
                for (int i = 0; i < members.length; i++)
                {
                	String extension = members[i].getFileExtension();
                    if (members[i] instanceof IContainer)
                    {
                    	if (members[i].getName().equals(TestManager.HISTORY_DIR))
                    		continue;
                        if (((IContainer)members[i]).members().length > 0 &&
                        	members[i].getName().startsWith(".") == false &&
                        	(extension == null || !(extension
                        	.equalsIgnoreCase(XmlBeansTestModule.FOLDER_EXT))))
                        {
                            foldersModules.add(members[i]);
                        }
                    }
                    else if (extension != null && 
                    		 extension.equalsIgnoreCase(EXTENSION) &&
                             members[i].getName().startsWith(".") == false)
                    {
                        foldersModules.add(members[i]);
                    }
                }
                children = foldersModules.toArray();
            }
        }
        catch (CoreException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, e.getLocalizedMessage(), e);
        }
        return children;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element)
    {
        if (element instanceof IResource)
        {
            return ((IResource)element).getParent();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element)
    {
        if (element instanceof IContainer)
        {
            try
            {
                IContainer c = (IContainer)element; 
                if (c.isAccessible() && c.members().length > 0)
                    return true;
            }
            catch (CoreException e) 
            {
                LanguageTestPlugin.log(IStatus.WARNING, 
                                       e.getLocalizedMessage(), e);
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement)
    {
        if (inputElement instanceof Object []) 
            return (Object[])inputElement;
        if (inputElement instanceof IContainer)
        {
            try
            {
                IResource[] members = ((IContainer)inputElement).members();
                ArrayList<IResource> l = new ArrayList<IResource>(members.length);
                for (int i = 0; i < members.length; i++)
                {
                    if (members[i].isAccessible())
                    {
                        if (members[i] instanceof IProject)
                        {
                            IProject p = (IProject)members[i];
                            if (p.hasNature(LanguageModuleNature.ID) ||
                            	p.hasNature(LanguageUserNature.ID))
                            {
                                l.add(p);
                            }
                        }
                        else l.add(members[i]);
                    }
                }
                return l.toArray();
            }
            catch (CoreException e) 
            {
                LanguageTestPlugin.log(IStatus.WARNING, 
                                       e.getLocalizedMessage(), e);
            }
        }
        return null;
    }

}
