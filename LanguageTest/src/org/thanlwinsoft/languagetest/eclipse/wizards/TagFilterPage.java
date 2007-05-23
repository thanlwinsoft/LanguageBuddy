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

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.views.MetaDataContentProvider;
import org.thanlwinsoft.languagetest.eclipse.views.MetaDataLabelProvider;

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
    private Object contentProvider;
    private MetaDataLabelProvider labelProvider;

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
                        validate();
                    }
                });
        filterComposite = new ScrolledComposite(mainGroup, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        tree = new Tree(filterComposite, SWT.MULTI | SWT.CHECK);
        tree.setHeaderVisible(false);
        tree.setEnabled(false);
        tree.setLinesVisible(true);
        TreeColumn nameColumn = new TreeColumn(tree, SWT.NONE);
        nameColumn.setWidth(300);
        nameColumn.setResizable(true);
        nameColumn.setText(MessageUtil.getString("FilterColumn"));
        viewer = new CheckboxTreeViewer(tree);
        contentProvider = new MetaDataContentProvider();
        labelProvider = new MetaDataLabelProvider(parent.getDisplay());
    }
    
    protected boolean validate()
    {
        return true;
    }
}
