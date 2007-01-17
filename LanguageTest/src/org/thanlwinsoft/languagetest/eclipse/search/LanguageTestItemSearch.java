/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.search;

import java.util.HashSet;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.search.core.text.TextSearchRequestor;
import org.eclipse.search.core.text.TextSearchScope;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.text.FileTextSearchScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.WorkspaceLanguageManager;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.LangType;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.List;

/**
 * @author keith
 *
 */
public class LanguageTestItemSearch extends DialogPage implements ISearchPage
{
    private Group mainGroup;
    private Label searchLabel = null;
    private Text searchText = null;
    private Group filterGroup = null;
    private Label langLabel = null;
    private List langList = null;
    private String [] langCodes = null;
    private ISearchPageContainer container = null;
    
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
            TextSearchRequestor requestor = null;
            Pattern searchPattern = Pattern.compile(searchText.getText());
            IJobManager jobMan = Platform.getJobManager();
            IProgressMonitor monitor = jobMan.createProgressGroup();
            engine.search(scope, requestor, searchPattern, monitor);
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
        RowLayout layout = new RowLayout();
        layout.fill = true;
        layout.justify = true;
        layout.type = SWT.VERTICAL;
        
        mainGroup.setLayout(layout);
        createFilterGroup();
        this.setControl(mainGroup);
    }

    /**
     * This method initializes filterGroup	
     *
     */
    private void createFilterGroup()
    {
        filterGroup = new Group(mainGroup, SWT.NONE);
        filterGroup.setLayout(new GridLayout());
        langLabel = new Label(filterGroup, SWT.NONE);
        langLabel.setText(MessageUtil.getString("SearchLangLabel"));
        langList = new List(filterGroup, SWT.NONE);
        LangType [] langs = WorkspaceLanguageManager.findUserLanguages();
        langCodes = new String[langs.length];
        for (int i = 0; i < langs.length; i++)
        {
            UniversalLanguage ul = new UniversalLanguage(langs[i].getLang());
            langList.add(ul.getDescription());
            langCodes[i] = ul.getCode();
        }
        
    }

}
