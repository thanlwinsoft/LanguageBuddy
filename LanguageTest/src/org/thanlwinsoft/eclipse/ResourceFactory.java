/**
 * 
 */
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
package org.thanlwinsoft.eclipse;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

/**
 * The ResourceFactory is used to save and recreate an IResource object.
 * As such, it implements the IPersistableElement interface for storage
 * and the IElementFactory interface for recreation.
 *
 * @see IMemento
 * @see IPersistableElement
 * @see IElementFactory
 */
public class ResourceFactory implements IElementFactory, IPersistableElement {

    // These persistence constants are stored in XML.  Do not
    // change them.
    private static final String TAG_PATH = "path";//$NON-NLS-1$

    private static final String TAG_TYPE = "type";//$NON-NLS-1$

    private static final String FACTORY_ID = "org.eclipse.ui.internal.model.ResourceFactory";//$NON-NLS-1$

    // IPersistable data.
    private IResource res;

    /**
     * Create a ResourceFactory.  This constructor is typically used
     * for our IElementFactory side.
     */
    public ResourceFactory() {
    }

    /**
     * Create a ResourceFactory.  This constructor is typically used
     * for our IPersistableElement side.
     */
    public ResourceFactory(IResource input) {
        res = input;
    }

    /**
     * @see IElementFactory
     */
    public IAdaptable createElement(IMemento memento) {
        // Get the file name.
        String fileName = memento.getString(TAG_PATH);
        if (fileName == null) {
            return null;
        }

        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        String type = memento.getString(TAG_TYPE);
        if (type == null) {
            // Old format memento. Create an IResource using findMember. 
            // Will return null for resources in closed projects.
            res = root.findMember(new Path(fileName));
        } else {
            int resourceType = Integer.parseInt(type);

            if (resourceType == IResource.ROOT) {
                res = root;
            } else if (resourceType == IResource.PROJECT) {
                res = root.getProject(fileName);
            } else if (resourceType == IResource.FOLDER) {
                res = root.getFolder(new Path(fileName));
            } else if (resourceType == IResource.FILE) {
                res = root.getFile(new Path(fileName));
            }
        }
        return res;
    }

    /**
     * @see IPersistableElement
     */
    public String getFactoryId() {
        return FACTORY_ID;
    }

    /**
     * @see IPersistableElement
     */
    public void saveState(IMemento memento) {
        memento.putString(TAG_PATH, res.getFullPath().toString());
        memento.putString(TAG_TYPE, Integer.toString(res.getType()));
    }
}
