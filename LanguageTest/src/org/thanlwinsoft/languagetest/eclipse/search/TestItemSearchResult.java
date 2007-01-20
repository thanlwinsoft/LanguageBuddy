/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.search;

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
    public TestItemSearchResult(ISearchQuery query)
    {
        super();
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
        // TODO Auto-generated method stub
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
}
