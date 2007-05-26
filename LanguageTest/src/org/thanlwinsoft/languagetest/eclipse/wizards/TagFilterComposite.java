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
package org.thanlwinsoft.languagetest.eclipse.wizards;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.views.MetaDataContentProvider;
import org.thanlwinsoft.languagetest.eclipse.views.MetaDataLabelProvider;
import org.thanlwinsoft.languagetest.language.test.meta.MetaDataManager;
import org.thanlwinsoft.languagetest.language.test.meta.MetaNode;
import org.thanlwinsoft.schemas.languagetest.module.ConfigType;

/**
 * @author keith
 *
 */
public class TagFilterComposite extends ScrolledComposite implements ICheckStateListener
{

    private Tree tree;
    private CheckboxTreeViewer viewer;
    private MetaDataContentProvider contentProvider;
    private MetaDataLabelProvider labelProvider;
    private Object [] rootElements = null;
    private IPath [] checkedPaths = null;
    /**
     * @param parent
     * @param style
     */
    public TagFilterComposite(Composite parent, int style)
    {
        super(parent, style);
        init();
    }
    protected void init()
    {
        tree = new Tree(this, SWT.MULTI | SWT.CHECK);
        tree.setHeaderVisible(false);
        tree.setEnabled(true);
        tree.setLinesVisible(true);
        TreeColumn nameColumn = new TreeColumn(tree, SWT.NONE);
        nameColumn.setWidth(400);
        nameColumn.setResizable(true);
        nameColumn.setText(MessageUtil.getString("FilterColumn"));
        tree.showColumn(nameColumn);
        viewer = new CheckboxTreeViewer(tree);
        viewer.setCellEditors(new CellEditor[] { 
                new CheckboxCellEditor()});
        contentProvider = new MetaDataContentProvider();
        labelProvider = new MetaDataLabelProvider(this.getDisplay());
        viewer.setContentProvider(contentProvider);
        viewer.setLabelProvider(labelProvider);
        ConfigType [] config = MetaDataManager.loadConfig();
        // convert the config array into the top level MetaNodes
        // otherwise we can't compare MetaNodes from TreePaths
        rootElements = contentProvider.getElements(config);
        viewer.setInput(rootElements);
        viewer.refresh();
        viewer.addCheckStateListener(this);
        viewer.expandAll();
        setContent(tree);
        setExpandHorizontal(true);
        setExpandVertical(true);
        if (getParent().getLayout() instanceof RowLayout)
        {
            RowData treeRowData = new RowData(SWT.DEFAULT,200);
            setLayoutData(treeRowData);
        }
        else if (getParent().getLayout() instanceof GridLayout)
        {
            GridData treeGridData = new GridData(GridData.FILL);
            treeGridData.minimumHeight = 200;
            treeGridData.heightHint = 200;
            setLayoutData(treeGridData);
        }
    }
    

    public MetaNode [] getCheckedNodes()
    {
        Object [] elements = viewer.getCheckedElements();
        MetaNode [] nodes = new MetaNode[elements.length];
        
        for (int i = 0; i < elements.length; i++)
        {
            if (elements[i] instanceof MetaNode)
            {
                nodes[i] = (MetaNode)elements[i];
            }
        }
        return nodes;
    }
    
    public IPath [] getCheckedTagPaths()
    {
        if (tree.isDisposed())
            return checkedPaths;
        Object [] elements = viewer.getCheckedElements();
        List <IPath>paths = new Vector<IPath>(elements.length);
        for (int i = 0; i < elements.length; i++)
        {
            if (elements[i] instanceof MetaNode)
            {
                paths.add(((MetaNode)elements[i]).toPath());
            }
        }
        return paths.toArray(new IPath[paths.size()]);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ICheckStateListener#checkStateChanged(org.eclipse.jface.viewers.CheckStateChangedEvent)
     */
    public void checkStateChanged(CheckStateChangedEvent event)
    {
        if (event.getElement() instanceof MetaNode)
        {
            MetaNode mn = (MetaNode)event.getElement();
            if (mn.hasChildren())
            {
                // set all the children to this state
                //setChildrenState(mn, event.getChecked());
                IPath path = mn.toPath();
                Object [] segments = new Object[path.segmentCount()];
                segments[0] = rootElements;
                MetaNode n = mn;
                for (int i = path.segmentCount() - 1; i >= 0 ; i--)
                {
                    segments[i] = n;
                    n = mn.getParent();
                }
                TreePath tp = new TreePath(segments);
                viewer.setSubtreeChecked(tp, event.getChecked());
            }
        }
        checkedPaths = getCheckedTagPaths();
    }
    /** set children of a branch node to the same state */
    protected void setChildrenState(MetaNode mn, boolean state)
    {
        for (MetaNode child : mn.getChildren())
        {
            viewer.setChecked(child, state);
            
            if (child.hasChildren())
            {
                setChildrenState(child, state);
            }
        }
    }
    
}
