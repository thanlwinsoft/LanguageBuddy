/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/LanguageTest/src/org/thanlwinsoft/eclipse/WorkbenchAdapterFactory.java $
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
package org.thanlwinsoft.eclipse;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * Dispenses adapters for various core objects.
 * Returns IWorkbenchAdapter adapters, used for displaying,
 * navigating, and populating menus for core objects.
 */
public class WorkbenchAdapterFactory implements IAdapterFactory 
{
    //private Object workspaceAdapter = new WorkbenchWorkspace();

    private Object rootAdapter = new WorkbenchRootResource();

    private Object projectAdapter = new WorkbenchProject();

    private Object folderAdapter = new WorkbenchFolder();

    private Object fileAdapter = new WorkbenchFile();

    private Object resourceFactory = new ResourceFactory();
    /**
     * Returns the IActionFilter for an object.
     */
    protected Object getActionFilter(Object o) 
    {
        if (o instanceof IResource) 
        {
            switch (((IResource) o).getType()) 
            {
            case IResource.FILE:
                return fileAdapter;
            case IResource.FOLDER:
                return folderAdapter;
            case IResource.PROJECT:
                return projectAdapter;
            }
        }
        return null;
    }

    /**
     * Returns an object which is an instance of the given class
     * associated with the given object. Returns <code>null</code> if
     * no such object can be found.
     *
     * @param o the adaptable object being queried
     *   (usually an instance of <code>IAdaptable</code>)
     * @param adapterType the type of adapter to look up
     * @return a object castable to the given adapter type, 
     *    or <code>null</code> if this adapter provider 
     *    does not have an adapter of the given type for the
     *    given object
     */
    public Object getAdapter(Object o, Class adapterType) 
    {
        if (adapterType.isInstance(o)) {
            return o;
        }
        if (adapterType == IWorkbenchAdapter.class) {
            return getWorkbenchElement(o);
        }
        if (adapterType == IPersistableElement.class) {
            return getPersistableElement(o);
        }
        if (adapterType == IElementFactory.class) {
            return getElementFactory(o);
        }
        if (adapterType == IActionFilter.class) {
            return getActionFilter(o);
        }
        if (adapterType == IUndoContext.class) {
            return getUndoContext(o);
        }
        return null;
    }

    /**
     * Returns the collection of adapter types handled by this
     * provider.
     * <p>
     * This method is generally used by an adapter manager
     * to discover which adapter types are supported, in advance
     * of dispatching any actual <code>getAdapter</code> requests.
     * </p>
     *
     * @return the collection of adapter types
     */
    public Class[] getAdapterList() 
    {
        return new Class[] { IWorkbenchAdapter.class, IElementFactory.class,
                IPersistableElement.class, IActionFilter.class, IUndoContext.class };
    }

    /**
     * Returns an object which is an instance of IElementFactory
     * associated with the given object. Returns <code>null</code> if
     * no such object can be found.
     */
    protected Object getElementFactory(Object o) 
    {
        if (o instanceof IResource) 
        {
            return resourceFactory;
        }
        return null;
    }

    /**
     * Returns an object which is an instance of IPersistableElement
     * associated with the given object. Returns <code>null</code> if
     * no such object can be found.
     */
    protected Object getPersistableElement(Object o) 
    {
        if (o instanceof IResource) 
        {
            return new ResourceFactory((IResource) o);
        }
        return null;
    }

    /**
     * Returns an object which is an instance of IWorkbenchAdapter
     * associated with the given object. Returns <code>null</code> if
     * no such object can be found.
     */
    protected Object getWorkbenchElement(Object o) 
    {
        if (o instanceof IResource) 
        {
            switch (((IResource) o).getType()) 
            {
            case IResource.FILE:
                return fileAdapter;
            case IResource.FOLDER:
                return folderAdapter;
            case IResource.PROJECT:
                return projectAdapter;
            }
        }
        if (o instanceof IWorkspaceRoot) 
        {
            return rootAdapter;
        }

        return null;
    }
    
    /**
     * Returns the IUndoContext for an object.
     */
    protected Object getUndoContext(Object o) {
        if (o instanceof IWorkspace) {
            return PlatformUI.getWorkbench().getOperationSupport().getUndoContext();
        }
        return null;
    }
}
