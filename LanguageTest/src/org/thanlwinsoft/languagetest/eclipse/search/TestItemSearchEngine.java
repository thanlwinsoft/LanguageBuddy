/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.search;

import java.io.IOException;
import java.io.InputStream;
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
import org.thanlwinsoft.languagetest.language.test.TestItemFilter;
import org.thanlwinsoft.schemas.languagetest.module.LangEntryType;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;

/**
 * @author keith
 *
 */
public class TestItemSearchEngine extends TextSearchEngine
{
    private HashSet langSet = null;
    private TestItemFilter [] filters = null;
    private TestItemSearchResult searchResult = null;
    // internal variables for TestModule currently being processed
    private HashMap fontMap = null;
    private LanguageModuleType lm = null;
    private int maxLanguages = 0;
    
    public TestItemSearchEngine(HashSet langSet, TestItemFilter[] filters)
    {
        this.langSet = langSet;
        this.filters = filters;
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
            InputStream is = null;
            try
            {
                is = scope[i].getContents();
                LanguageModuleDocument doc =
                    LanguageModuleDocument.Factory.parse(is);
                lm = doc.getLanguageModule();
                fontMap = null;
                if (lm == null) continue;
                for (int j = 0; j < lm.sizeOfTestItemArray(); j++)
                {
                    TestItemType ti = lm.getTestItemArray(j);
                    boolean isMatch = false;
                    for (int k = 0; k < ti.sizeOfNativeLangArray() && !isMatch; k++)
                    {
                        LangEntryType let = ti.getNativeLangArray(k);
                        if (langSet == null || langSet.contains(let.getLang()))
                        {
                            isMatch = index(scope[i], j, lm, ti, let, searchPattern);
                        }
                    }
                    for (int k = 0; k < ti.sizeOfForeignLangArray() && !isMatch; k++)
                    {
                        LangEntryType let = ti.getForeignLangArray(k);
                        if (langSet == null || langSet.contains(let.getLang()))
                        {
                            isMatch = index(scope[i], j, lm, ti, let, searchPattern);
                        }
                    }
                    monitor.worked(1);
                }
            }
            catch(IOException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, "IOException while searching", e);
            }
            catch (XmlException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, "XmlException while searching", e);
            }
            catch (CoreException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, "CoreException while searching", e);
                e.printStackTrace();
            }
            finally
            {
                if (is != null)
                {
                    try
                    {
                        is.close();
                    }
                    catch (IOException e)
                    {
                        LanguageTestPlugin.log(IStatus.WARNING, "IOException while searching", e);
                        e.printStackTrace();
                    }
                }
            }
        }
        
        IStatus status = new Status(IStatus.OK, LanguageTestPlugin.ID, IStatus.OK, 
                        MessageUtil.getString("SearchOKStatus"), null);
        return status;
    }
    private boolean index(IFile file, int item, LanguageModuleType module, 
                       TestItemType testItem, LangEntryType let, 
                       Pattern searchPattern)
    {
        boolean isMatch = false;
        Matcher m = searchPattern.matcher(let.getStringValue());
        //   we don't distinguish between more than one match within an item
        if (m.find()) 
        {
            isMatch = true;
            for (TestItemFilter f : filters)
            {
                isMatch &= f.chooseItem(module, testItem);
            }
            if (isMatch)
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
        return isMatch;
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
    public String getFilterDescription()
    {
        if (filters.length == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (TestItemFilter f : filters)
        {
            if (sb.length() > 1) sb.append(" ");
            sb.append(f.getDescription());
        }
        sb.append("]");
        return sb.toString();
    }
}
