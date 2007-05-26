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

import java.util.LinkedList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.WorkspaceLanguageManager;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.languagetest.language.test.meta.MetaNode;
import org.thanlwinsoft.schemas.languagetest.module.LangType;

public class EditTagDialog extends MessageDialog
{

    /**
     * 
     */
    //private final TagFilterComposite composite;
    private Combo langCombo;
    private Text tagId;
    private Text tagDesc;
    private LinkedList<MetaNode> metaNodeList;
    private int parentNodeLevel = 0;
    private UniversalLanguage language = null;
    private boolean isNew;
    private boolean idDescInSync = true;
    /**
     * @param parentShell
     * @param dialogTitle
     * @param dialogMessage
     * @param isNew
     */
    public EditTagDialog(Shell parentShell, String dialogTitle, 
                         String dialogMessage, boolean isNew)
    {
        super(parentShell, dialogTitle, null, dialogMessage,
              MessageDialog.NONE, new String[]
              {
                MessageUtil.getString("OK"), MessageUtil.getString("Cancel")
              }, 0);
        this.isNew = isNew;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite c = (Composite)super.createDialogArea(parent);
        //c.setSize(400, SWT.DEFAULT);
        Group group = new Group(c, SWT.SHADOW_ETCHED_IN);
        RowLayout layout = new RowLayout(SWT.VERTICAL);
        layout.fill = true;
        layout.justify = true;
        group.setLayout(layout);
        final Combo parentPathCombo = new Combo(group, SWT.LEAD);
        parentPathCombo.setLayoutData(new RowData(400, SWT.DEFAULT));
        parentPathCombo.setToolTipText(MessageUtil.getString("TagParentPath"));
        final ComboViewer pathCV = new ComboViewer(parentPathCombo);
        pathCV.setContentProvider(new ArrayContentProvider());
        parentPathCombo.setEnabled(isNew);
        tagId = new Text(group, SWT.LEAD);
        tagId.setEnabled(isNew);
        if (!isNew)
        {
            tagId.setText(metaNodeList.getLast().getId());
        }
        langCombo = new Combo(group, SWT.LEAD);
        ComboViewer langCV = new ComboViewer(langCombo);
        langCV.setContentProvider(new ArrayContentProvider());
        tagDesc = new Text(group, SWT.LEAD);
        LangType [] langs = WorkspaceLanguageManager.findUserLanguages();
        final UniversalLanguage [] uLangs = new UniversalLanguage[langs.length];
        for (int i = 0; i < langs.length; i++)
        {
            UniversalLanguage ul = new UniversalLanguage(langs[i].getLang());
            uLangs[i] = ul;
        }
        langCV.setInput(uLangs);
        langCombo.select(0);
        language = uLangs[0];

        pathCV.setInput(getPathList(language.getCode()));
        if (isNew)
            parentPathCombo.select(parentNodeLevel);
        else
            parentPathCombo.select(0);
        parentPathCombo.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e){} 
            public void widgetSelected(SelectionEvent e)
            {
                parentNodeLevel = parentPathCombo.getSelectionIndex();
                
            }
        });
        langCombo.addSelectionListener(new SelectionListener(){
            public void widgetDefaultSelected(SelectionEvent e){}
            public void widgetSelected(SelectionEvent e)
            {
                int i = langCombo.getSelectionIndex();
                if (i > -1)
                {
                    language = uLangs[i];
                    String langName = 
                        new UniversalLanguage(uLangs[i].getLanguageCode()).getDescription();
                    tagDesc.setToolTipText(MessageUtil.getString("TagDescription", langName));
                    pathCV.setInput(getPathList(language.getCode()));
                }
            }});
        tagId.setToolTipText(MessageUtil.getString("TagID"));
        tagId.addModifyListener(new ModifyListener(){

            public void modifyText(ModifyEvent e)
            {
                // if the tagDesc is null, set it to the same as the id
                if (tagDesc.getText().length() == 0 || idDescInSync)
                {
                    tagDesc.setText(tagId.getText());
                }
            }});
        langCombo.setToolTipText(MessageUtil.getString("TagLanguage"));
        tagDesc.setToolTipText(MessageUtil.getString("TagDescription", 
            new UniversalLanguage(uLangs[0].getLanguageCode()).getDescription()));
        if (isNew == false && metaNodeList.size() > 0)
        {
            tagDesc.setText(metaNodeList.getLast().getName(language.getCode()));
            if (tagDesc.getText().equals(tagId.getText()))
                idDescInSync = true;
            else
                idDescInSync = false;
        }
        tagDesc.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e)
            {
                if (tagDesc.getText().equals(tagId.getText()) || 
                    tagDesc.getText().length() == 0)
                    idDescInSync = true;
                else
                    idDescInSync = false;
            }});
        c.pack();
        return c;
    }
    private String [] getPathList(String langCode)
    {
        
        if (metaNodeList != null)
        {
            if (isNew)
            {
                String [] paths = new String[metaNodeList.size() + 1];
                parentNodeLevel = metaNodeList.size();
                int i = 0;
                paths[i++] = "/";
                for (MetaNode mn : metaNodeList)
                {
                    paths[i++] = mn.getPath(langCode);
                }
                return paths;
            }
            else
            {
                if (metaNodeList.size() > 1)
                {
                    parentNodeLevel = metaNodeList.size() - 1;
                    MetaNode mn = metaNodeList.get(metaNodeList.size() - 2);
                    return new String [] {mn.getPath(langCode)};
                }
                return new String [] {"/"};
            }
        }
        
        return null;
    }
    public void setSelection(TreeItem [] selection)
    {
        if (selection.length > 0)
        {
            metaNodeList = new LinkedList<MetaNode>();
            Object o = selection[0].getData();
            if (o instanceof MetaNode)
            {
                MetaNode mn = (MetaNode)o;
                metaNodeList.push(mn);
                while (mn.getParent() != null)
                {
                    mn = mn.getParent();
                    metaNodeList.push(mn);
                }
            }
        }
    }
}