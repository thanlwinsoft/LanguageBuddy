/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * -----------------------------------------------------------------------
 */
/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.views;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.language.test.meta.MetaNode;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;

public class MetaDataLabelProvider implements ITableLabelProvider, ILabelProvider
{
    /**
     * 
     */
    private Display display;
    private TestItemType testItem = null;
    public MetaDataLabelProvider(Display display)
    {
        this.display = display;
        
    }
    public void setTestItem(TestItemType ti)
    {
        this.testItem = ti;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
     */
    public Image getImage(Object element)
    {
        ImageDescriptor idBranch = 
            LanguageTestPlugin.getImageDescriptor("/icons/MetaDataBranch.png");
        ImageDescriptor idLeaf = 
            LanguageTestPlugin.getImageDescriptor("/icons/MetaDataLeaf.png");
        ImageDescriptor idBranchOff = 
            LanguageTestPlugin.getImageDescriptor("/icons/MetaDataBranchDisabled.png");
        ImageDescriptor idLeafOff = 
            LanguageTestPlugin.getImageDescriptor("/icons/MetaDataLeafDisabled.png");
        if (element instanceof MetaNode)
        {
            MetaNode mn = (MetaNode)element;
            if (testItem == null || mn.isSetOnItem(testItem))
            {
                if (mn.hasChildren())
                    return idBranch.createImage(display);
                else
                    return idLeaf.createImage(display);
            }
            else
            {
                if (mn.hasChildren())
                    return idBranchOff.createImage(display);
                else
                    return idLeafOff.createImage(display);
                
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
     */
    public String getText(Object element)
    {
        if (element instanceof MetaNode)
        {
            MetaNode mn = (MetaNode)element;
            return mn.getName();
        }
        return element.toString();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    public void addListener(ILabelProviderListener listener)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
     */
    public void dispose()
    {
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
     */
    public boolean isLabelProperty(Object element, String property)
    {
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    public void removeListener(ILabelProviderListener listener)
    {
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object element, int columnIndex)
    {
        return getImage(element);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex)
    {
        return getText(element);
    }
    
}