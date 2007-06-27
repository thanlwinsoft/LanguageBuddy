/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/editors/ModuleLanguagePart.java $
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
package org.thanlwinsoft.languagetest.eclipse.editors;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.workspace.WorkspaceLanguageManager;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.thanlwinsoft.schemas.languagetest.module.LangTypeType;

/**
 * @author keith
 *
 */
public class ModuleLanguagePart extends EditorPart implements ModifyListener {

	private TestModuleEditor parent = null;
    private FormToolkit toolkit = null;
    private LanguageTable nativeTable = null;
    private LanguageTable foreignTable = null;
    private IProject project = null;
	public ModuleLanguagePart(TestModuleEditor parent)
    {
        super();
        this.parent = parent;
    }
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) 
	{
		parent.doSave(monitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() 
	{
		parent.doSaveAs();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException 
	{
		setSite(site);
        setPartName(MessageUtil.getString("LanguagesTab"));
        setInput(input);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty() {
        if (nativeTable.isDirty() || foreignTable.isDirty())
        {
            parent.setDirty(true);
        }
		return parent.isDirty();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		
		return parent.isSaveAsAllowed();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) 
	{
        toolkit = new FormToolkit(parent.getDisplay());
        
        final ScrolledForm form = toolkit.createScrolledForm(parent);
        form.getBody().setLayout(new ColumnLayout());
        form.setText(MessageUtil.getString("ModuleLanguages"));
        ExpandableComposite nec = toolkit.createExpandableComposite(form.getBody(), 
                        ExpandableComposite.TWISTIE | 
                        ExpandableComposite.EXPANDED);
        nec.setText(MessageUtil.getString("NativeLanguages"));
        nec.setToolTipText(MessageUtil.getString("NativeLanguageDesc"));
        
        nativeTable = new LanguageTable(nec, SWT.SHADOW_ETCHED_IN);
        nativeTable.addModifyListener(this);
        toolkit.adapt(nativeTable);
        nec.setClient(nativeTable);
        
        ExpandableComposite fec = toolkit.createExpandableComposite(form.getBody(),
                        ExpandableComposite.TWISTIE| 
                        ExpandableComposite.EXPANDED);
        fec.setText(MessageUtil.getString("ForeignLanguages"));
        fec.setToolTipText(MessageUtil.getString("ForeignLanguageDesc"));
        foreignTable = new LanguageTable(fec, SWT.SHADOW_ETCHED_IN);
        foreignTable.addModifyListener(this);
        toolkit.adapt(foreignTable);
        fec.setClient(foreignTable);
        
        nec.addExpansionListener(new ExpansionAdapter() 
        {
            public void expansionStateChanged(ExpansionEvent e) {
                form.reflow(true);
            }
        });
        fec.addExpansionListener(new ExpansionAdapter() 
        {
            public void expansionStateChanged(ExpansionEvent e) {
                form.reflow(true);
            }
        });
        FormText introText = toolkit.createFormText(form.getBody(), true);
        introText.setText(MessageUtil.getString("ModuleLanguageIntro"), 
                          true, false);
        
	}
    /**
     * Set the Input for the Language Tables. The input is expected to be 
     * a FileEditorInput for a Language Module file.
     * @param input
     */
    public void setInput(Object input)
    {
        project = null;
        if (input instanceof FileEditorInput)
        {
            FileEditorInput fei = (FileEditorInput)input;
            project = fei.getFile().getProject();
            if (parent.getDocument() == null) return;
            LangType[] enabled = parent.getDocument().getLanguageModule().getLangArray();
            HashSet enabledNative = new HashSet();
            HashSet enabledForeign = new HashSet();
            // TBD: optimise to have one call to find Active languages that
            // supplies both in one call
            HashMap nLangs = 
                WorkspaceLanguageManager.findActiveLanguages(project, 
                LangTypeType.NATIVE);
            
            HashMap fLangs = 
                WorkspaceLanguageManager.findActiveLanguages(project, 
                LangTypeType.FOREIGN);
            // Loop over module languages and see which languages are enabled
            // for this module. Add languages which are not already in the 
            // project to the project list.
            boolean added = false;
            for (int i = 0; i < enabled.length; i++)
            {
                if (enabled[i].getType().equals(LangTypeType.NATIVE))
                {
                    // remove link to source file by copying other wise strange
                    // effects happen
                    enabledNative.add(enabled[i].copy());
                    if (nLangs.containsKey(enabled[i].getLang()) == false)
                    {
                        WorkspaceLanguageManager.addLanguage(project, enabled[i],
                                        null);
                        added = true;
                    }
                }
                else
                {
                    enabledForeign.add(enabled[i].copy());
                    if (fLangs.containsKey(enabled[i].getLang()) == false)
                    {
                        WorkspaceLanguageManager.addLanguage(project, enabled[i],
                                        null);
                        added = true;
                    }
                }
            }
            // reget the active languages incase some languages were added
            if (added)
            {
                nLangs = 
                    WorkspaceLanguageManager.findActiveLanguages(project, 
                    LangTypeType.NATIVE);
                fLangs = 
                    WorkspaceLanguageManager.findActiveLanguages(project, 
                    LangTypeType.FOREIGN);
            }
            nativeTable.setProjectLangs(nLangs);
            foreignTable.setProjectLangs(fLangs);
            nativeTable.setModuleLangs((LangType[])
                enabledNative.toArray(new LangType[enabledNative.size()]));
            foreignTable.setModuleLangs((LangType[])
                enabledForeign.toArray(new LangType[enabledForeign.size()]));
        }
        
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() 
	{
        //form.setFocus();
	}
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
     */
    public void modifyText(ModifyEvent e)
    {
        LangType [] nLangArray = nativeTable.getModuleLangs();
        LangType [] fLangArray = foreignTable.getModuleLangs();
        LangType [] langArray = new LangType[nLangArray.length + fLangArray.length];
        int i = 0;
        for (; i < nLangArray.length; i++)
        {
            langArray[i] = (LangType)nLangArray[i].copy();
        }
        for (; i < langArray.length; i++)
        {
            langArray[i] = (LangType)fLangArray[i - nLangArray.length].copy();
        }
        parent.getDocument().getLanguageModule().setLangArray(langArray);
        nativeTable.saveProjectLangs(project, null, true);
        foreignTable.saveProjectLangs(project, null, true);
        parent.setLanguageChanged();
        parent.firePropertyChange(PROP_DIRTY);
    }

}