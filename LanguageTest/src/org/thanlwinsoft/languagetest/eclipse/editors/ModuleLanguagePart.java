/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.editors;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.WorkspaceLanguageManager;
import org.thanlwinsoft.schemas.languagetest.LangType;
import org.thanlwinsoft.schemas.languagetest.LangTypeType;

/**
 * @author keith
 *
 */
public class ModuleLanguagePart extends EditorPart {

	private TestModuleEditor parent = null;
    private ScrolledForm form = null;
    private FormToolkit toolkit = null;
    private LanguageTable nativeTable = null;
    private LanguageTable foreignTable = null;
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
        
        //Composite nc = toolkit.createComposite(form.getBody());
       // nc.setLayout(new TableWrapLayout());
        //nec.setClient(nc);
        nativeTable = new LanguageTable(nec, SWT.SHADOW_ETCHED_IN);
        toolkit.adapt(nativeTable);
        nec.setClient(nativeTable);
        
        ExpandableComposite fec = toolkit.createExpandableComposite(form.getBody(),
                        ExpandableComposite.TWISTIE| 
                        ExpandableComposite.EXPANDED);
        fec.setText(MessageUtil.getString("ForeignLanguages"));
        fec.setToolTipText(MessageUtil.getString("ForeignLanguageDesc"));
        //Composite fc = toolkit.createComposite(form.getBody(), SWT.WRAP);
        //fc.setLayout(new TableWrapLayout());
        //fec.setClient(fc);
        //fec.setTextClient(null);
        foreignTable = new LanguageTable(fec, SWT.SHADOW_ETCHED_IN);
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
        
        this.form = form;
	}
    
    public void setInput(Object input)
    {
        IProject project = null;
        if (input instanceof FileEditorInput)
        {
            FileEditorInput fei = (FileEditorInput)input;
            project = fei.getFile().getProject();
            if (parent.getDocument() == null) return;
            LangType[] enabled = parent.getDocument().getLanguageModule().getLangArray();
            HashSet enabledNative = new HashSet();
            HashSet enabledForeign = new HashSet();
            HashMap nLangs = 
                WorkspaceLanguageManager.findActiveLanguages(project, 
                LangTypeType.NATIVE);
            
            HashMap fLangs = 
                WorkspaceLanguageManager.findActiveLanguages(project, 
                LangTypeType.FOREIGN);
            boolean added = false;
            for (int i = 0; i < enabled.length; i++)
            {
                if (enabled[i].getType().equals(LangTypeType.NATIVE))
                {
                    enabledNative.add(enabled[i]);
                    if (nLangs.containsKey(enabled[i].getLang()) == false)
                    {
                        WorkspaceLanguageManager.addLanguage(project, enabled[i],
                                        null);
                        added = true;
                    }
                }
                else
                {
                    enabledForeign.add(enabled[i]);
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
                enabledNative.toArray(new LangType[enabledForeign.size()]));
        }
        
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() 
	{
        //form.setFocus();
	}

}
