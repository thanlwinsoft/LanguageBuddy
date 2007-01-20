/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.search;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.search.core.text.TextSearchRequestor;
import org.eclipse.search.core.text.TextSearchScope;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.thanlwinsoft.languagetest.MessageUtil;

/**
 * Query LanguageModule TestItems
 * @author keith
 *
 */
public class TestItemQuery implements ISearchQuery
{
    private TestItemSearchEngine engine = null;
    private TextSearchScope scope = null;
    private Pattern searchPattern;
    private String [] langCodes;
    public TestItemQuery(TestItemSearchEngine engine, TextSearchScope scope, 
                         Pattern pattern, String [] langCodes)
    {
        this.engine = engine;
        this.scope = scope;
        this.searchPattern = pattern;
        this.langCodes = langCodes;
        engine.setQuery(this);
    }
    /* (non-Javadoc)
     * @see org.eclipse.search.ui.ISearchQuery#canRerun()
     */
    public boolean canRerun()
    {
        
        return true;
    }

    
    /* (non-Javadoc)
     * @see org.eclipse.search.ui.ISearchQuery#canRunInBackground()
     */
    public boolean canRunInBackground()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.ISearchQuery#getLabel()
     */
    public String getLabel()
    {
        return MessageUtil.getString("TestItemQueryLabel",searchPattern.pattern());
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.ISearchQuery#getSearchResult()
     */
    public ISearchResult getSearchResult()
    {
        
        return engine.getResult();
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.ISearchQuery#run(org.eclipse.core.runtime.IProgressMonitor)
     */
    public IStatus run(IProgressMonitor monitor)
                    throws OperationCanceledException
    {
        TextSearchRequestor requestor = null;
        engine.getResult().removeAll();
        return engine.search(scope, requestor, searchPattern, monitor);
    }
    public final String [] getLangCodes()
    {
        return langCodes;
    }
}
