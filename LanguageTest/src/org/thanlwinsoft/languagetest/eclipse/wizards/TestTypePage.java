/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.workspace.WorkspaceLanguageManager;
import org.thanlwinsoft.languagetest.language.test.TestType;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.languagetest.language.text.Iso639;
import org.thanlwinsoft.schemas.languagetest.module.LangTypeType;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * @author keith
 *
 */
public class TestTypePage extends WizardPage
{
    Composite parent = null;
    private Group group = null;
    private Label label = null;
    private Combo userCombo = null;
    private Label nativeLabel = null;
    private Combo nativeCombo = null;
    private Label foreignLabel = null;
    private Combo foreignCombo = null;
    private UniversalLanguage [] nativeLangs = null;
    private UniversalLanguage [] foreignLangs = null;
    private Label testTypeLabel = null;
    private Button flashCardRadio = null;
    private Button readingRadio = null;
    private Button writingRadio = null;
    private Button listeningRadio = null;
    private Label paddingLabel1 = null;
    private Label paddingLabel2 = null;
    private Label paddingLabel3 = null;
    private Combo maxItemsCombo = null;
    private Label maxItemsLabel = null;
    public final static String NATIVE_TEST_LANG_PREF = "NativeTestLang";
    public final static String FOREIGN_TEST_LANG_PREF = "ForeignTestLang";
    private final static int [] MAX_ITEMS = { -1, 10, 25, 50, 100, 200, 500 }; 
    
    /**
     * @param pageName
     */
    protected TestTypePage(String pageName)
    {
        super(pageName);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        this.parent = parent;
        createGroup();
        setControl(group);
        checkFields();
    }

    /**
     * This method initializes group	
     *
     */
    private void createGroup()
    {
        group = new Group(parent, SWT.NONE);
        label = new Label(group, SWT.NONE);
        label.setText(MessageUtil.getString("Username"));
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        createUserCombo();
        nativeLabel = new Label(group, SWT.NONE);
        nativeLabel.setText(MessageUtil.getString("NativeLang"));
        createNativeCombo();
        foreignLabel = new Label(group, SWT.NONE);
        foreignLabel.setText(MessageUtil.getString("ForeignLang"));
        createForeignCombo();
        testTypeLabel = new Label(group, SWT.NONE);
        testTypeLabel.setText(MessageUtil.getString("TestTypeLabel"));
        flashCardRadio = new Button(group, SWT.RADIO);
        flashCardRadio.setEnabled(false);
        flashCardRadio
                .addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
                {
                    public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
                    {
                        checkFields();
                    }
                });
        paddingLabel1 = new Label(group, SWT.NONE);
        paddingLabel1.setVisible(false);
        readingRadio = new Button(group, SWT.RADIO);
        readingRadio.setEnabled(false);
        readingRadio.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                checkFields();
            }
        });
        paddingLabel2 = new Label(group, SWT.NONE);
        paddingLabel2.setVisible(false);
        writingRadio = new Button(group, SWT.RADIO);
        writingRadio.setEnabled(false);
        writingRadio.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                checkFields();
            }
        });
        paddingLabel3 = new Label(group, SWT.NONE);
        paddingLabel3.setVisible(false);
        listeningRadio = new Button(group, SWT.RADIO);
        listeningRadio.setEnabled(false);
        listeningRadio.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                checkFields();
            }
        });
        maxItemsLabel = new Label(group, SWT.NONE);
        maxItemsLabel.setText(MessageUtil.getString("MaxTestItems"));
        createMaxItemsCombo();
        
        setRadioText();
    }

    /**
     * Set the radio text for the currently selected languages.
     *
     */
    private void setRadioText()
    {
        int ni = nativeCombo.getSelectionIndex();
        int fi = foreignCombo.getSelectionIndex(); 
        if (ni > -1 && fi > -1)
        {
            String nativeCode = nativeLangs[ni].getLanguageCode();
            String nativeLang = Iso639.getDescription(nativeCode);
            String foreignCode = foreignLangs[fi].getLanguageCode();
            String foreignLang = Iso639.getDescription(foreignCode);
            flashCardRadio.setText(MessageUtil.getString("TestTypeFlashCards",
                nativeLang, foreignLang));
            readingRadio.setText(MessageUtil.getString("TestTypeReading",
                    nativeLang, foreignLang));
            writingRadio.setText(MessageUtil.getString("TestTypeWriting",
                    nativeLang, foreignLang));
            listeningRadio.setText(MessageUtil.getString("TestTypeListening",
                    nativeLang, foreignLang));
            flashCardRadio.setEnabled(true);
            readingRadio.setEnabled(true);
            writingRadio.setEnabled(true);
            listeningRadio.setEnabled(true);
            group.pack();
        }
    }
    /**
     * Check whether the field entries are valid
     */
    private void checkFields()
    {
        if (userCombo.getSelectionIndex() > -1 &&
            nativeCombo.getSelectionIndex() > -1 &&
            foreignCombo.getSelectionIndex() > -1 &&
            (flashCardRadio.getSelection() ||
             readingRadio.getSelection() ||
             writingRadio.getSelection() ||
             listeningRadio.getSelection()))
        {
            setPageComplete(true);
        }
        else 
        {
            setMessage(MessageUtil.getString("IncompleteTestType"));
            setPageComplete(false);
        }
    }
    /**
     * This method initializes userCombo	
     *
     */
    private void createUserCombo()
    {
        userCombo = new Combo(group, SWT.NONE);
        IProject [] userProjects = WorkspaceLanguageManager.findUserProjects();
        String [] userNames = new String[userProjects.length];
        for (int i = 0; i < userProjects.length; i++)
        {
            userNames[i] = userProjects[i].getName();
        }
        userCombo.setItems(userNames);
        if (userNames.length == 1) userCombo.select(0);
        if (userNames.length == 0)
        {
            MessageDialog.openWarning(group.getShell(), 
                MessageUtil.getString("NoUsersTitle"),
                MessageUtil.getString("NoUsersMessage"));
        }
    }

    protected ScopedPreferenceStore getPrefStore(IProject userProject)
    {
        ProjectScope configScope = new ProjectScope(userProject);
        ScopedPreferenceStore prefStore = 
            new ScopedPreferenceStore(configScope, LanguageTestPlugin.ID);
        
        return prefStore;
    }
    /**
     * This method initializes nativeCombo	
     *
     */
    private void createNativeCombo()
    {
        nativeCombo = new Combo(group, SWT.NONE);
        IProject userProject = WorkspaceLanguageManager.getUserProject();
        LangTypeType.Enum type = LangTypeType.NATIVE;
        HashMap map = WorkspaceLanguageManager.findActiveLanguages(userProject, type);
        Iterator ie = map.entrySet().iterator();
        String [] items = new String[map.size()];
        nativeLangs = new UniversalLanguage[map.size()];
        String prevLang = getPrefStore(userProject).getString(FOREIGN_TEST_LANG_PREF);
        int selection = 0;
        int i = 0;
        while (ie.hasNext())
        {
            Map.Entry entry = (Map.Entry)ie.next();
            nativeLangs[i] = new UniversalLanguage(entry.getKey().toString());
            items[i] = nativeLangs[i].getDescription();
            if (nativeLangs[i].getCode().equals(prevLang))
                selection = i;
            i++;
        }
        nativeCombo.setItems(items);
        if (items.length > 0) 
        {
            nativeCombo.select(selection);
        }
        nativeCombo.addSelectionListener(new org.eclipse.swt.events.SelectionListener()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                setRadioText();
            }
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e)
            {
            }
        });
    }

    /**
     * This method initializes foreignCombo	
     *
     */
    private void createForeignCombo()
    {
        foreignCombo = new Combo(group, SWT.NONE);
        IProject userProject = WorkspaceLanguageManager.getUserProject();
        LangTypeType.Enum type = LangTypeType.FOREIGN;
        HashMap map = WorkspaceLanguageManager.findActiveLanguages(userProject, type);
        Iterator ie = map.entrySet().iterator();
        String [] items = new String[map.size()];
        foreignLangs = new UniversalLanguage[map.size()];
        int i = 0;
        
        String prevLang = getPrefStore(userProject).getString(FOREIGN_TEST_LANG_PREF);
        int selection = 0;
        while (ie.hasNext())
        {
            Map.Entry entry = (Map.Entry)ie.next();
            foreignLangs[i] = new UniversalLanguage(entry.getKey().toString());
            items[i] = foreignLangs[i].getDescription();
            if (foreignLangs[i].getCode().equals(prevLang))
                selection = i;
            i++;
        }
        foreignCombo.setItems(items);
        if (items.length > 0) 
        {
            foreignCombo.select(selection);
        }
        foreignCombo
                .addSelectionListener(new SelectionListener()
                {
                    public void widgetSelected(SelectionEvent e)
                    {
                        setRadioText();
                    }
                    public void widgetDefaultSelected(SelectionEvent e)
                    {
                    }
                });
    }
    /**
     * This method initializes maxItemsCombo    
     *
     */
    private void createMaxItemsCombo()
    {
        maxItemsCombo = new Combo(group, SWT.NONE);
        maxItemsCombo.setItems(new String [] {
                MessageUtil.getString("UnlimitedItems"),
                MessageUtil.getString(Integer.toString(MAX_ITEMS[1])),
                MessageUtil.getString(Integer.toString(MAX_ITEMS[2])),
                MessageUtil.getString(Integer.toString(MAX_ITEMS[3])),
                MessageUtil.getString(Integer.toString(MAX_ITEMS[4])),
                MessageUtil.getString(Integer.toString(MAX_ITEMS[5])),
                MessageUtil.getString(Integer.toString(MAX_ITEMS[6]))
            }
        );
        maxItemsCombo.select(0);// default to unlimited
    }

    /**
     * @return
     */
    public IProject getUser()
    {
        int userIndex = userCombo.getSelectionIndex();
        if (userIndex > -1)
        {
            return WorkspaceLanguageManager.findUserProjects()[userIndex];
        }
        return null;
    }

    /**
     * @return
     */
    public UniversalLanguage getNativeLanguage()
    {
        return nativeLangs[nativeCombo.getSelectionIndex()];
    }
    /**
     * @return
     */
    public UniversalLanguage getForeignLanguage()
    {
        return foreignLangs[foreignCombo.getSelectionIndex()];
    }
    
    public TestType getTestType()
    {
        if (flashCardRadio.getSelection())
        {
            return TestType.FLIP_CARD;
        }
        else if (readingRadio.getSelection())
        {
            return TestType.READING_FOREIGN_NATIVE;
        }
        else if (writingRadio.getSelection())
        {
            return TestType.READING_NATIVE_FOREIGN;
        }
        else if (listeningRadio.getSelection())
        {
            return TestType.LISTENING_FOREIGN_NATIVE;
        }
        return null;
    }
    
    public boolean isSetMaxTestItems()
    {
        return (getMaxTestItems() > -1)? true : false;
    }
    
    public int getMaxTestItems()
    {
        if (maxItemsCombo.getSelectionIndex() > -1)
            return MAX_ITEMS[maxItemsCombo.getSelectionIndex()];
        else
            return -1;
    }
}
