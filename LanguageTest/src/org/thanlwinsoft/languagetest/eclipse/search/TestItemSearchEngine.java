/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.core.text.TextSearchEngine;
import org.eclipse.search.core.text.TextSearchRequestor;
import org.eclipse.search.core.text.TextSearchScope;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.search.ui.text.Match;
import org.eclipse.search.ui.text.MatchEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.schemas.languagetest.LangEntryType;
import org.thanlwinsoft.schemas.languagetest.LangType;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleType;
import org.thanlwinsoft.schemas.languagetest.TestItemType;

/**
 * @author keith
 *
 */
public class TestItemSearchEngine extends TextSearchEngine
{
    private HashSet langSet = null;
    private TestItemSearchResult searchResult = null;
    // internal variables for TestModule currently being processed
    private HashMap fontMap = null;
    private LanguageModuleType lm = null;
    private int maxLanguages = 0;
    
    public TestItemSearchEngine(HashSet langSet)
    {
        this.langSet = langSet;
        
    }
    public TestItemSearchResult getResult()
    {
        return searchResult;
    }
    public void setQuery(TestItemQuery query)
    {
        searchResult = new TestItemSearchResult(query);
        maxLanguages = 0;
        lm = null;
        fontMap = null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.search.core.text.TextSearchEngine#search(org.eclipse.search.core.text.TextSearchScope, org.eclipse.search.core.text.TextSearchRequestor, java.util.regex.Pattern, org.eclipse.core.runtime.IProgressMonitor)
     */
    public IStatus search(TextSearchScope scope, TextSearchRequestor requestor,
                    Pattern searchPattern, IProgressMonitor monitor)
    {
        MultiStatus status = new MultiStatus(LanguageTestPlugin.ID, 0, "TestItemSearch", null);
        return search(scope.evaluateFilesInScope(status), requestor, searchPattern, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.core.text.TextSearchEngine#search(org.eclipse.core.resources.IFile[], org.eclipse.search.core.text.TextSearchRequestor, java.util.regex.Pattern, org.eclipse.core.runtime.IProgressMonitor)
     */
    public IStatus search(IFile[] scope, TextSearchRequestor requestor,
                    Pattern searchPattern, IProgressMonitor monitor)
    {
        for (int i = 0; i < scope.length; i++)
        {
            try
            {
                LanguageModuleDocument doc =
                    LanguageModuleDocument.Factory.parse(scope[i].getContents());
                lm = doc.getLanguageModule();
                fontMap = null;
                if (lm == null) continue;
                for (int j = 0; j < lm.sizeOfTestItemArray(); j++)
                {
                    TestItemType ti = lm.getTestItemArray(j);
                    
                    for (int k = 0; k < ti.sizeOfNativeLangArray(); k++)
                    {
                        LangEntryType let = ti.getNativeLangArray(k);
                        if (langSet == null || langSet.contains(let.getLang()))
                        {
                            index(scope[i], j, ti, let, searchPattern);
                            monitor.worked(1);
                        }
                    }
                    for (int k = 0; k < ti.sizeOfForeignLangArray(); k++)
                    {
                        LangEntryType let = ti.getForeignLangArray(k);
                        if (langSet == null || langSet.contains(let.getLang()))
                        {
                            index(scope[i], j, ti, let, searchPattern);
                            monitor.worked(1);
                        }
                    }
                }
            }
            catch(IOException e)
            {
                
            }
            catch (XmlException e)
            {
                //e.printStackTrace();
            }
            catch (CoreException e)
            {
                
                e.printStackTrace();
            }
        }
        
        IStatus status = new Status(IStatus.OK, LanguageTestPlugin.ID, IStatus.OK, 
                        MessageUtil.getString("SearchOKStatus"), null);
        return status;
    }
    private void index(IFile file, int item, TestItemType testItem, 
                       LangEntryType let, 
                       Pattern searchPattern)
    {
        Matcher m = searchPattern.matcher(let.getStringValue());
//   we don't distinguish between more than one match within an item
        //while (m.find())
        if (m.find()) 
        {
            if (fontMap == null)
            {
                getFontMap();
            }
            Match match = new TestItemMatch(file, item, testItem, let, 
                                            m.start(), m.end() - m.start(), 
                                            fontMap);
            searchResult.addMatch(match);
        }
    }
    private void getFontMap()
    {
        if (lm != null)
        {
            fontMap = new HashMap(lm.sizeOfLangArray());
            for (int i = 0; i < lm.sizeOfLangArray(); i++)
            {
                LangType lt = lm.getLangArray(i);
                FontData fd = new FontData(lt.getFont(), 
                                lt.getFontSize().intValue(), SWT.NORMAL);
                fontMap.put(lt.getLang(), fd);
            }
            maxLanguages = Math.max(maxLanguages, lm.sizeOfLangArray());
        }
    }
}
