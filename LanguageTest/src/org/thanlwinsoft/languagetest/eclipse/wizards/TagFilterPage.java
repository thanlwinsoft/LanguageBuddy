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

import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.views.MetaDataContentProvider;
import org.thanlwinsoft.languagetest.eclipse.views.MetaDataLabelProvider;
import org.thanlwinsoft.languagetest.language.test.TestItemFilter;
import org.thanlwinsoft.languagetest.language.test.meta.MetaDataManager;
import org.thanlwinsoft.languagetest.language.test.meta.MetaFilter;
import org.thanlwinsoft.languagetest.language.test.meta.MetaNode;
import org.thanlwinsoft.schemas.languagetest.module.ConfigType;

/**
 * @author keith
 *
 */
public class TagFilterPage extends WizardPage
{
    private Group mainGroup;
    private RowLayout rowLayout;
    private Button enableFilterButton;
    private ScrolledComposite filterComposite;
    private Tree tree;
    private CheckboxTreeViewer viewer;
    private MetaDataContentProvider contentProvider;
    private MetaDataLabelProvider labelProvider;
    private Button anyTagsButton;
    private Button allTagsButton;

    public TagFilterPage()
    {
        super("TagFilterPage");
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        this.setDescription(MessageUtil.getString("TagFilter"));
        mainGroup = new Group(parent, SWT.CENTER);
        setControl(mainGroup);
        rowLayout = new RowLayout();
        rowLayout.type = SWT.VERTICAL;
        rowLayout.fill = true;
        mainGroup.setLayout(rowLayout);
        enableFilterButton = new Button(mainGroup, SWT.CHECK);
        enableFilterButton.setText(MessageUtil.getString("TagFilterEnable"));
        enableFilterButton
                .addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
                {
                    public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
                    {
                        if (tree != null && tree.isDisposed() == false)
                            tree.setEnabled(enableFilterButton.getSelection());
                        validate();
                    }
                });
        anyTagsButton = new Button(mainGroup, SWT.RADIO);
        anyTagsButton.setText(MessageUtil.getString("AnyTagsMatchFilter"));
        allTagsButton = new Button(mainGroup, SWT.RADIO);
        allTagsButton.setText(MessageUtil.getString("AllTagsMatchFilter"));
        
        filterComposite = new ScrolledComposite(mainGroup, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        tree = new Tree(filterComposite, SWT.MULTI | SWT.CHECK);
        tree.setHeaderVisible(false);
        tree.setEnabled(false);
        tree.setLinesVisible(true);
        TreeColumn nameColumn = new TreeColumn(tree, SWT.NONE);
        nameColumn.setWidth(300);
        nameColumn.setResizable(true);
        nameColumn.setText(MessageUtil.getString("FilterColumn"));
        tree.showColumn(nameColumn);
        viewer = new CheckboxTreeViewer(tree);
        viewer.setCellEditors(new CellEditor[] { 
                new CheckboxCellEditor()});
        contentProvider = new MetaDataContentProvider();
        labelProvider = new MetaDataLabelProvider(parent.getDisplay());
        viewer.setContentProvider(contentProvider);
        viewer.setLabelProvider(labelProvider);
        ConfigType [] config = MetaDataManager.loadConfig();
        viewer.setInput(config);
        viewer.refresh();
        filterComposite.setContent(tree);
        filterComposite.setExpandHorizontal(true);
        filterComposite.setExpandVertical(true);
        RowData treeRowData = new RowData(SWT.DEFAULT,200);
        filterComposite.setLayoutData(treeRowData);
    }
    
    protected boolean validate()
    {
        setPageComplete(true);
        return true;
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
    
    protected IPath [] getCheckedTagPaths()
    {
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
    
    public boolean isFilterEnabled()
    {
        return enableFilterButton.getSelection();
    }
    
    protected MetaFilter.Mode getFilterMode()
    {
        if (allTagsButton.getSelection()) return MetaFilter.Mode.ALL;
        return MetaFilter.Mode.ANY;
    }
    public TestItemFilter getFilter()
    {
        if (isFilterEnabled())
        {
            TestItemFilter tagFilter = new MetaFilter(getCheckedTagPaths(),
                    getFilterMode());
            return tagFilter;
        }
        return null;
    }
}
