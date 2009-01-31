/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
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
package org.thanlwinsoft.languagetest.eclipse.views;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.natures.LanguageUserNature;

/**
 * @author keith
 *
 */
public class TestHistoryView extends ViewPart
{
    private TreeViewer viewer = null;
    private Tree tree = null;
    private ITreeContentProvider provider = null;
    public final static String ID = "org.thanlwinsoft.languagetest.TestHistoryView";
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent)
    {
        Group mainGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
        mainGroup.setLayout(new FillLayout());
        tree = new Tree(mainGroup, SWT.V_SCROLL | SWT.H_SCROLL);
        viewer = new TreeViewer(tree);
        viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
        
        provider = new TestHistoryProvider();
        viewer.setContentProvider(provider);
        viewer.setLabelProvider(new TestHistoryLabelProvider(provider));
        viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
        viewer.refresh();
        
        IResourceChangeListener listener = new IResourceChangeListener(){

            public void resourceChanged(IResourceChangeEvent event)
            {
                IResource r = event.getResource();
                if (r.getProject() == null) 
                {
                    viewer.refresh();
                    tree.redraw();
                }
                try
                {
                    if (r.getProject().isAccessible() &&
                        r.getProject().hasNature(LanguageUserNature.ID)) 
                    {
                        viewer.refresh();
                        tree.redraw();
                    }
                }
                catch (CoreException e)
                {
                    LanguageTestPlugin.log(IStatus.WARNING,e.getLocalizedMessage(),e);
                }
            }
            
        };
        ResourcesPlugin.getWorkspace().addResourceChangeListener(
           listener, IResourceChangeEvent.POST_CHANGE);

    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus()
    {
        // TODO Auto-generated method stub

    }

}
