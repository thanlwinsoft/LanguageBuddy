/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.search;

import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.search.core.text.TextSearchEngine;
import org.eclipse.search.core.text.TextSearchRequestor;
import org.eclipse.search.core.text.TextSearchScope;
import org.eclipse.search.ui.text.Match;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.schemas.languagetest.LangEntryType;
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
    public TestItemSearchEngine(HashSet langSet)
    {
        this.langSet = langSet;
    }
    public TestItemSearchResult getResult()
    {
        return searchResult;
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
        //requestor.beginReporting();
        searchResult = new TestItemSearchResult(); 
        for (int i = 0; i < scope.length; i++)
        {
            try
            {
                //if (!requestor.acceptFile(scope[i])) continue;
                
                LanguageModuleDocument doc =
                    LanguageModuleDocument.Factory.parse(scope[i].getContents());
                LanguageModuleType lm = doc.getLanguageModule();
                if (lm == null) continue;
                for (int j = 0; j < lm.sizeOfTestItemArray(); j++)
                {
                    TestItemType ti = lm.getTestItemArray(j);
                    
                    for (int k = 0; k < ti.sizeOfNativeLangArray(); k++)
                    {
                        LangEntryType let = ti.getNativeLangArray(k);
                        if (langSet == null || langSet.contains(let.getLang()))
                        {
                            index(scope[i], j, let, searchPattern, searchResult);
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
        //requestor.endReporting();
        return null;
    }
    private void index(IFile file, int item, LangEntryType let, 
                       Pattern searchPattern, TestItemSearchResult result)
    {
        Matcher m = searchPattern.matcher(let.getStringValue());
        while (m.find())
        {
            m.start();
            Match match = new TestItemMatch(file, item, let, 
                                            m.start(), m.end() - m.start());
            result.addMatch(match);
            
        }
    }
}
