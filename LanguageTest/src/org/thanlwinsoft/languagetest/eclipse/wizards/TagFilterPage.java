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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.SelectionEvent;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.language.test.TestItemFilter;
import org.thanlwinsoft.languagetest.language.test.meta.MetaFilter;

/**
 * @author keith
 *
 */
public class TagFilterPage extends WizardPage
{
    private Group mainGroup;
    private RowLayout rowLayout;
    private Button enableFilter;
    private TagFilterComposite filterComposite;
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
        enableFilter = new Button(mainGroup, SWT.CHECK);
        enableFilter.setText(MessageUtil.getString("TagFilterEnable"));
        enableFilter
            .addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
            {
                public void widgetSelected(SelectionEvent e)
                {
                    if (filterComposite != null && 
                        filterComposite.isDisposed() == false)
                    {
                        boolean enabled = enableFilter.getSelection();
                        filterComposite.setEnabled(enabled);
                        anyTagsButton.setEnabled(enabled);
                        allTagsButton.setEnabled(enabled);
                    }
                    validate();
                }
            });
        anyTagsButton = new Button(mainGroup, SWT.RADIO);
        anyTagsButton.setText(MessageUtil.getString("AnyTagsMatchFilter"));
        anyTagsButton.setSelection(true);
        anyTagsButton.setEnabled(false);
        allTagsButton = new Button(mainGroup, SWT.RADIO);
        allTagsButton.setText(MessageUtil.getString("AllTagsMatchFilter"));
        allTagsButton.setEnabled(false);
        filterComposite = new TagFilterComposite(mainGroup, SWT.H_SCROLL | 
                                                 SWT.V_SCROLL | SWT.BORDER);
        filterComposite.setEnabled(false);
    }
    
    protected boolean validate()
    {
        setPageComplete(true);
        return true;
    }
    
    public boolean isFilterEnabled()
    {
        return enableFilter.getSelection();
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
            TestItemFilter tagFilter = 
                new MetaFilter(filterComposite.getCheckedTagPaths(), 
                               getFilterMode());
            return tagFilter;
        }
        return null;
    }
}
