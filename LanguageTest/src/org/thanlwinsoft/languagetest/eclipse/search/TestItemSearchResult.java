/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/LanguageTest/src/org/thanlwinsoft/languagetest/eclipse/search/TestItemSearchResult.java $
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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.IEditorMatchAdapter;
import org.eclipse.search.ui.text.IFileMatchAdapter;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;

/**
 * @author keith
 *
 */
public class TestItemSearchResult extends AbstractTextSearchResult
{
    private ISearchQuery query;
    
    private SortedSet languages = new TreeSet();
    public TestItemSearchResult(TestItemQuery query)
    {
        super();
        for (int i = 0; i < query.getLangCodes().length; i++)
        {
            languages.add(query.getLangCodes()[i]);
        }
        this.query = query;
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchResult#getEditorMatchAdapter()
     */
    public IEditorMatchAdapter getEditorMatchAdapter()
    {
        return new EditorMatchAdapter();
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchResult#getFileMatchAdapter()
     */
    public IFileMatchAdapter getFileMatchAdapter()
    {
        return new FileMatchAdapter();
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.ISearchResult#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor()
    {
        return LanguageTestPlugin.getImageDescriptor("icons/testItemSearch.png");
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.ISearchResult#getLabel()
     */
    public String getLabel()
    {
        String queryString = "";
        if (query != null)
        {
            queryString = query.getLabel();
        }
        return MessageUtil.getString("ResultMatches", queryString,
                                     Integer.toString(getMatchCount()));
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchResult#getElements()
     */
    public Object[] getElements()
    {
        return super.getElements();
    }
    /* (non-Javadoc)
     * @see org.eclipse.search.ui.ISearchResult#getQuery()
     */
    public ISearchQuery getQuery()
    {
        return query;
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.ISearchResult#getTooltip()
     */
    public String getTooltip()
    {
        return getLabel();
    }
    public class FileMatchAdapter implements IFileMatchAdapter
    {

        /* (non-Javadoc)
         * @see org.eclipse.search.ui.text.IFileMatchAdapter#computeContainedMatches(org.eclipse.search.ui.text.AbstractTextSearchResult, org.eclipse.core.resources.IFile)
         */
        public Match[] computeContainedMatches(AbstractTextSearchResult result, IFile file)
        {
            return result.getMatches(file);           
        }

        /* (non-Javadoc)
         * @see org.eclipse.search.ui.text.IFileMatchAdapter#getFile(java.lang.Object)
         */
        public IFile getFile(Object element)
        {
            if (element instanceof TestItemMatch)
            {
                TestItemMatch m = (TestItemMatch)element;
                if (m.getElement() instanceof IFile)
                {
                    return (IFile)m.getElement();
                }
            }
            else if (element instanceof IFile) return (IFile)element;
            return null;
        }
        
    }
    
    public class EditorMatchAdapter implements IEditorMatchAdapter
    {

        /* (non-Javadoc)
         * @see org.eclipse.search.ui.text.IEditorMatchAdapter#computeContainedMatches(org.eclipse.search.ui.text.AbstractTextSearchResult, org.eclipse.ui.IEditorPart)
         */
        public Match[] computeContainedMatches(AbstractTextSearchResult result, IEditorPart editor)
        {
            IEditorInput input = editor.getEditorInput();
            if (input instanceof IFileEditorInput)
            {
                IFile file = ((IFileEditorInput)input).getFile();
                return result.getMatches(file);
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.search.ui.text.IEditorMatchAdapter#isShownInEditor(org.eclipse.search.ui.text.Match, org.eclipse.ui.IEditorPart)
         */
        public boolean isShownInEditor(Match match, IEditorPart editor)
        {
            IEditorInput input = editor.getEditorInput();
            if (input instanceof IFileEditorInput)
            {
                IFile file = ((IFileEditorInput)input).getFile();
                return match.getElement().equals(file);
            }
            return false;
        }
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchResult#addMatch(org.eclipse.search.ui.text.Match)
     */
    public void addMatch(Match match)
    {
        if (match instanceof TestItemMatch)
        {
            Map fontMap = ((TestItemMatch)match).getFontMap();
            if (fontMap != null)
            {
                Set langIds = fontMap.keySet();
                Iterator i = langIds.iterator();
                while (i.hasNext())
                {
                    Object o = i.next();
                    if (!languages.contains(o))
                    {
                        languages.add(o);
                    }
                }
            }
        }
        super.addMatch(match);
    }
    
    public int getLanguageCount()
    {
        return languages.size();
    }
    public Set getLanguages()
    {
        return languages;
    }
}
