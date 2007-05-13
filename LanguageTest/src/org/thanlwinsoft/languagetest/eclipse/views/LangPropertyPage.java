/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.views;

import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.WorkspaceLanguageManager;
import org.thanlwinsoft.languagetest.eclipse.editors.LanguageTable;
import org.thanlwinsoft.languagetest.eclipse.wizards.AddLanguageAction;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.thanlwinsoft.schemas.languagetest.module.LangTypeType;

/**
 * @author keith
 *
 */
public class LangPropertyPage extends PropertyPage
{
    private Group mainGroup = null;
    private LanguageTable nLangTable = null;
    private LanguageTable fLangTable = null;
    private Label nLabel = null;
    private Label fLabel = null;
    private IProject project = null;
    private Button addLangButton = null;
    private Button deleteLangButton = null;
    private LanguageTable lastFocusedTable = null;
    
    public LangPropertyPage()
    {
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPropertyPage#setElement(org.eclipse.core.runtime.IAdaptable)
     */
    public void setElement(IAdaptable element)
    {
        if (element instanceof IProject)
        {
            project = (IProject)element;
        }
        else if (element instanceof IResource)
        {
            project = ((IResource)element).getProject();
        }
        else project = null;
        if (project != null)
        {
            setLanguages();
            setValid(true);
        }
        //super.setElement(element);
    }

    
    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    protected void performApply()
    {
        
    }



    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#performCancel()
     */
    public boolean performCancel()
    {
        return true;
    }



    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    protected void performDefaults()
    {
        
    }



    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    public boolean performOk()
    {
        if (project != null && nLangTable != null && fLangTable != null)
        {
            nLangTable.saveProjectLangs(project, null, false);
            fLangTable.saveProjectLangs(project, null, false);
        }
        return true;
    }



    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.IPreferencePage#okToLeave()
     */
    public boolean okToLeave()
    {
        return true;
    }

    
    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite parent)
    {
        mainGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
        //this.setControl(mainGroup);
        RowLayout layout = new RowLayout();
        layout.type = SWT.VERTICAL;
        mainGroup.setLayout(layout);
        //ScrolledComposite sc = new ScrolledComposite(mainGroup, SWT.H_SCROLL | SWT.V_SCROLL);
        //sc.setLayout(new FillLayout());
        nLabel = new Label(mainGroup, SWT.LEFT);
        nLangTable = new LanguageTable(mainGroup, SWT.SHADOW_ETCHED_IN);
        fLabel = new Label(mainGroup, SWT.LEFT);
        fLangTable = new LanguageTable(mainGroup, SWT.SHADOW_ETCHED_IN);
        nLabel.setText(MessageUtil.getString("NativeLanguages"));
        fLabel.setText(MessageUtil.getString("ForeignLanguages"));
        Composite buttonRow = new Composite(mainGroup, SWT.LEFT);
        buttonRow.setLayout(new RowLayout());
        addLangButton = new Button(buttonRow, SWT.LEFT);
        addLangButton.setText(MessageUtil.getString("AddLangButton"));
        addLangButton.setToolTipText(MessageUtil.getString("AddLangToolTip"));
        
        addLangButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e)
            {

            }

            public void widgetSelected(SelectionEvent e)
            {
                new AddLanguageAction().run(null);
                setLanguages();
            }});
        deleteLangButton = new Button(buttonRow, SWT.LEFT);
        deleteLangButton.setText(MessageUtil.getString("DeleteLangButton"));
        deleteLangButton.setToolTipText(MessageUtil.getString("DeleteLangToolTip"));
        deleteLangButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e)
            {
                
            }

            public void widgetSelected(SelectionEvent e)
            {
                LangType deletable = null;
                if (lastFocusedTable != null)
                {
                    deletable = lastFocusedTable.getSelectedLang();
                }
                
                if (deletable != null)
                {
                    boolean reallyDelete = 
                        MessageDialog.openConfirm(mainGroup.getShell(), 
                                    MessageUtil.getString("ConfirmDeleteLangTitle"), 
                                    MessageUtil.getString("ConfirmDeleteLangMessage",
                                    new UniversalLanguage(deletable.getLang()).getDescription()));
                    if (reallyDelete)
                    {
                        IJobManager jobMan = Platform.getJobManager();
                        IProgressMonitor monitor = jobMan.createProgressGroup();
                        WorkspaceLanguageManager.removeLanguage(project, deletable, monitor);
                    }
                }
                else
                {
                    MessageDialog.openInformation(mainGroup.getShell(), 
                                    MessageUtil.getString("NoLangSelectedTitle"), 
                                    MessageUtil.getString("NoLangSelectedMessage"));
                    
                }
                setLanguages();
            }});
        setLanguages();
        noDefaultAndApplyButton();
        nLangTable.addTraverseListener(new TraverseListener () {

            public void keyTraversed(TraverseEvent e)
            {
                lastFocusedTable = nLangTable;
            }});
        nLangTable.addFocusListener(new FocusListener(){

            public void focusGained(FocusEvent e)
            {
                lastFocusedTable = nLangTable;
            }

            public void focusLost(FocusEvent e)
            {
                
            }});
        fLangTable.addFocusListener(new FocusListener(){

            public void focusGained(FocusEvent e)
            {
                lastFocusedTable = fLangTable;
            }

            public void focusLost(FocusEvent e)
            {
                
            }});
        return mainGroup;
    }
    private void setLanguages()
    {
        if (project != null)
        {
            LangType [] langs = WorkspaceLanguageManager.findLanguages(project);
            if (nLangTable != null && fLangTable != null)
            {
                try
                {
                    HashSet nativeLang = new HashSet();
                    HashSet foreignLang = new HashSet();
                    for (int i = 0; i < langs.length; i++)
                    {
                        if (langs[i].getType().equals(LangTypeType.NATIVE))
                        {
                            nativeLang.add(langs[i]);
                        }
                        else if (langs[i].getType().equals(LangTypeType.FOREIGN))
                        {
                            foreignLang.add(langs[i]);
                        }
                    }
                    LangType [] nLangs = (LangType [])nativeLang.toArray(new LangType[nativeLang.size()]);
                    LangType [] fLangs = (LangType [])foreignLang.toArray(new LangType[foreignLang.size()]);
                    
                    nLangTable.setProjectLangs(nLangs);
                    fLangTable.setProjectLangs(fLangs);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            this.setValid(true);
            this.setTitle(MessageUtil.getString("LangPropertyPageTitle"));
            this.setMessage(MessageUtil.getString("LangPropertyPageMessage"));
        }
        
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.dialogs.PropertyPage#getElement()
     */
    public IAdaptable getElement()
    {
        return project;
    }
}
