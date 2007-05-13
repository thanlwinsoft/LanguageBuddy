/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.search;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.search.core.text.TextSearchRequestor;
import org.eclipse.search.core.text.TextSearchScope;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.FileTextSearchScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.WorkspaceLanguageManager;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.swt.widgets.Button;

/**
 * @author keith
 *
 */
public class LanguageTestItemSearch extends DialogPage implements ISearchPage
{
    private Group mainGroup;
    private Label searchLabel = null;
    private Text searchText = null;
    private Group langGroup = null;
    private List langList = null;
    private String [] langCodes = null;
    private HashMap fontMap = null;
    private ISearchPageContainer container = null;
    private Composite optionsComposite = null;
    private Group optionsGroup = null;
    private Button caseCheckBox = null;
    private final static String LAST_SEARCH = "Search.Last";
    
    public LanguageTestItemSearch()
    {
        
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.search.ui.ISearchPage#performAction()
     */
    public boolean performAction()
    {
        if (searchText.getText().length() > 0)
        {
            int [] langSelection = langList.getSelectionIndices();
            
            HashSet langSet = new HashSet(langSelection.length);
            for (int i = 0; i < langSelection.length; i++)
            {
                langSet.add(langCodes[langSelection[i]]);
            }
            TestItemSearchEngine engine = new TestItemSearchEngine(langSet);
            TextSearchScope scope = FileTextSearchScope.newWorkspaceScope(new String[] {"*.xml"}, false);
            int patternOptions = 0;
            Pattern searchPattern = null;
            if (caseCheckBox.getSelection() == false)
            {
                patternOptions |= Pattern.CASE_INSENSITIVE;
                patternOptions |= Pattern.UNICODE_CASE;
                searchPattern = Pattern.compile(searchText.getText(), patternOptions);
            }
            else
            {
                searchPattern = Pattern.compile(searchText.getText());
            }
            LanguageTestPlugin.getPrefStore().setValue(LAST_SEARCH, searchText.getText());
            try
            {
                LanguageTestPlugin.getPrefStore().save();
            }
            catch (IOException e)
            {
                LanguageTestPlugin.log(IStatus.ERROR, e.getLocalizedMessage(), e);
            }
            //IJobManager jobMan = Platform.getJobManager();
            //IProgressMonitor monitor = jobMan.createProgressGroup();
            final TestItemQuery query = new TestItemQuery(engine, scope, searchPattern, langCodes);
            //NewSearchUI searchUI = new NewSearchUI();
            container = null;
            if (container == null)
            {
                NewSearchUI.runQueryInBackground(query);
                return true;
            }
            final IRunnableContext context = container.getRunnableContext();

                IStatus status = NewSearchUI.runQueryInForeground(context, query);
                if (!status.isOK())
                {
                    
                    MessageDialog.openError(getShell(), 
                        MessageUtil.getString("SearchError"), 
                        status.getMessage());
                    return false;
                }
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.ISearchPage#setContainer(org.eclipse.search.ui.ISearchPageContainer)
     */
    public void setContainer(ISearchPageContainer container)
    {
        this.container = container;
        ISelection s = container.getSelection();
        if (s instanceof IStructuredSelection)
        {
            IStructuredSelection ss = (IStructuredSelection)s;
            System.out.println(ss.getFirstElement());
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        mainGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
        searchLabel = new Label(mainGroup, SWT.NONE);
        searchLabel.setText(MessageUtil.getString("SearchLabel"));
        
        searchText = new Text(mainGroup, SWT.BORDER);
        searchText.setToolTipText(MessageUtil.getString("SearchTextToolTip"));
        searchText.setText(LanguageTestPlugin.getPrefStore().getString(LAST_SEARCH));
        RowLayout layout = new RowLayout();
        layout.fill = true;
        layout.justify = true;
        layout.type = SWT.VERTICAL;
        
        mainGroup.setLayout(layout);
        createOptionsComposite();
        this.setControl(mainGroup);
        searchText.setFocus();
        
    }

    /**
     * This method initializes langGroup	
     *
     */
    private void createLangGroup()
    {
        langGroup = new Group(optionsComposite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        langGroup.setLayout(layout);
        //langLabel = new Label(langGroup, SWT.NONE);
        langGroup.setText(MessageUtil.getString("SearchLangLabel"));
        langList = new List(langGroup, SWT.MULTI);
        
        LangType [] langs = WorkspaceLanguageManager.findUserLanguages();
        langCodes = new String[langs.length];
        fontMap = new HashMap();
        for (int i = 0; i < langs.length; i++)
        {
            UniversalLanguage ul = new UniversalLanguage(langs[i].getLang());
            langList.add(ul.getDescription());
            langCodes[i] = ul.getCode();
            FontData fd = new FontData(
                            langs[i].getFont(),
                            langs[i].getFontSize().intValue(),
                            SWT.NORMAL);
            fontMap.put(ul.getCode(), fd);
        }
        langList.selectAll();
        langList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                int s = langList.getSelectionIndex();
                if (s > -1 && s < langCodes.length)
                {
                    FontData fd = (FontData)fontMap.get(langCodes[s]);
                    Font font = LanguageTestPlugin.getFont(fd);
                    searchText.setFont(font);
                    mainGroup.pack();
                }
            }
        });
        langList.setToolTipText(MessageUtil.getString("LangFontToolTip"));
    }

    /**
     * This method initializes optionsComposite	
     *
     */
    private void createOptionsComposite()
    {
        optionsComposite = new Composite(mainGroup, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        optionsComposite.setLayout(layout);
        createLangGroup();
        createOptionsGroup();
    }

    /**
     * This method initializes optionsGroup	
     *
     */
    private void createOptionsGroup()
    {
        optionsGroup = new Group(optionsComposite, SWT.NONE);
        optionsGroup.setLayout(new GridLayout());
        optionsGroup.setText(MessageUtil.getString("SearchOptions"));
        caseCheckBox = new Button(optionsGroup, SWT.CHECK);
        caseCheckBox.setText(MessageUtil.getString("CaseSensitive"));
        
    }

}
