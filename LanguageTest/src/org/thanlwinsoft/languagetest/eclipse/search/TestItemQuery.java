/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/LanguageTest/src/org/thanlwinsoft/languagetest/eclipse/search/TestItemQuery.java $
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
        return MessageUtil.getString("TestItemQueryLabel",
                searchPattern.pattern() + engine.getFilterDescription());
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
