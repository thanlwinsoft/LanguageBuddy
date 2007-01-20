/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.search;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.search.ui.text.AbstractTextSearchViewPage;
import org.eclipse.search.ui.text.Match;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.editors.TestModuleEditor;

/**
 * @author keith
 *
 */
public class TestItemSearchViewPage extends AbstractTextSearchViewPage
{
    private IStructuredContentProvider provider;
    private TreeViewer treeViewer = null;
    private TableViewer tableViewer = null;
    private TestItemSearchResult result = null;
    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#clear()
     */
    protected void clear()
    {
        result = null;
        provider = null;
        tableViewer = null;
        treeViewer = null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#configureTableViewer(org.eclipse.jface.viewers.TableViewer)
     */
    protected void configureTableViewer(TableViewer viewer)
    {
        if (viewer.getTable().isDisposed()) return;
        
        if (viewer.getContentProvider() == null)
        {
            provider = new TableContentProvider();
            if (viewer.getTable().getColumnCount() == 0)
            {
                TableColumn tc = new TableColumn(viewer.getTable(), SWT.LEFT);
                tc.setResizable(true);
                tc.setText(MessageUtil.getString("Matches"));
                tc.setToolTipText(MessageUtil.getString("Matches"));
                tc.setWidth(200);
                tc = new TableColumn(viewer.getTable(), SWT.LEFT);
                tc.setResizable(true);
                tc.setText(MessageUtil.getString("File"));
                tc.setToolTipText(MessageUtil.getString("File"));
                tc.setWidth(100);
            }
            viewer.setContentProvider(provider);
            viewer.setLabelProvider(new TestItemTableLabelProvider());
            viewer.getTable().setHeaderVisible(true);
            viewer.setColumnProperties(new String[]{ "Matches" });
        }
        this.tableViewer = viewer;
        if (result != null)
        {
            updateViewerInputs(true);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#configureTreeViewer(org.eclipse.jface.viewers.TreeViewer)
     */
    protected void configureTreeViewer(TreeViewer viewer)
    {
        this.treeViewer = viewer;
        if (viewer.getTree().isDisposed()) return;
        if (viewer.getContentProvider() == null)
        {
            viewer.setContentProvider(new TestItemSearchTreeProvider());
            viewer.setLabelProvider(new LabelProvider());
        }
        if (result != null)
        {
            updateViewerInputs(true);
        }
    }

    

    

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#getCurrentMatch()
     */
    public Match getCurrentMatch()
    {

        ISelection s = null;
        if (tableViewer != null)
        {
            s = tableViewer.getSelection();
        }
        if (s == null && treeViewer != null)
        {
            s = treeViewer.getSelection();
        }
        if (s != null && s instanceof IStructuredSelection)
        {
            IStructuredSelection ss = (IStructuredSelection)s;
            Object o = ss.getFirstElement();
            if (o instanceof Match)
                return (Match)o;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#getCurrentMatchLocation(org.eclipse.search.ui.text.Match)
     */
    public IRegion getCurrentMatchLocation(Match match)
    {
        return new Region(match.getOffset(), match.getLength());
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#elementsChanged(java.lang.Object[])
     */
    protected void elementsChanged(Object[] objects)
    {
        
    }
    
    public class TestItemSearchTreeProvider implements ITreeContentProvider
    {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
         */
        public Object[] getChildren(Object parentElement)
        {
            if (parentElement instanceof IFile)
            {
                return result.getMatches(parentElement);
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
         */
        public Object getParent(Object element)
        {
            if (element instanceof Match)
            {
                return ((Match)element).getElement();
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
         */
        public boolean hasChildren(Object element)
        {
            if (result != null && element instanceof IFile)
            {
                return (result.getMatches(element).length > 0);
            }
            return false;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object inputElement)
        {
            if (inputElement instanceof Object[])
            {
                return (Object[])inputElement;
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose()
        {
            
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
            //viewer.setInput(newInput);
        }
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#restoreState(org.eclipse.ui.IMemento)
     */
    public void restoreState(IMemento memento)
    {
        
        super.restoreState(memento);
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#saveState(org.eclipse.ui.IMemento)
     */
    public void saveState(IMemento memento)
    {
       
        super.saveState(memento);
    }

    private void updateViewerInputs(boolean async)
    {
        Runnable runnable = new Runnable() {

            public void run()
            {
                if (treeViewer != null && !treeViewer.getTree().isDisposed())
                {
                    if (treeViewer.getContentProvider() == null)
                    {
                        treeViewer.setContentProvider(new TestItemSearchTreeProvider());
                    }
                    treeViewer.setInput(result.getElements());
                    treeViewer.refresh();
                    treeViewer.getTree().redraw();
                }
                if (tableViewer != null && !tableViewer.getTable().isDisposed())
                {
                    if (tableViewer.getContentProvider() == null)
                    {
                        tableViewer.setContentProvider(new TableContentProvider());
                    }
                    Object [] files = result.getElements();
                    Vector v = new Vector(2 * files.length);
                    for (int i = 0; i < files.length; i++)
                    {
                        Match [] matches = result.getMatches(files[i]);
                        for (int j = 0; j < matches.length; j++)
                        {
                            v.add(matches[j]);
                        }
                    }
                    try
                    {
                        tableViewer.setInput(v);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    
                    tableViewer.refresh(true);
                    tableViewer.getTable().redraw();
                }
            }
        };
        if (async)
            this.getControl().getDisplay().asyncExec(runnable);
        else 
        {
            runnable.run();
        }
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#handleSearchResultChanged(org.eclipse.search.ui.SearchResultEvent)
     */
    protected void handleSearchResultChanged(SearchResultEvent e)
    {
        if (e.getSource() instanceof TestItemSearchResult)
        {
            result = (TestItemSearchResult)e.getSource();
        }
        if (result != null)
        {
            updateViewerInputs(true);
        }
        
        super.handleSearchResultChanged(e);
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#setInput(org.eclipse.search.ui.ISearchResult, java.lang.Object)
     */
    public void setInput(ISearchResult newSearch, Object viewState)
    {
        if (newSearch instanceof TestItemSearchResult)
        {
            result = (TestItemSearchResult)newSearch;
            result.addListener(new ISearchResultListener() {
                public void searchResultChanged(SearchResultEvent e)
                {
                    handleSearchResultChanged(e);
                }
            });
            updateViewerInputs(false);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#showMatch(org.eclipse.search.ui.text.Match, int, int, boolean)
     */
    protected void showMatch(Match match, int currentOffset, int currentLength, boolean activate) throws PartInitException
    {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IFileEditorInput input = new FileEditorInput((IFile)match.getElement());
        IEditorPart part = page.openEditor(input, "org.thanlwinsoft.languagetest.eclipse.editors.TestModuleEditor");
        if (part instanceof TestModuleEditor)
        {
            TestModuleEditor editor = (TestModuleEditor)part;
            editor.setActivePage(TestModuleEditor.TEST_ITEM_PAGE_INDEX);
            editor.selectTestItem(((TestItemMatch)match).getTestItemIndex());
        }
    }

    public class TableContentProvider implements IStructuredContentProvider
    {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object inputElement)
        {
            if (inputElement instanceof Collection)
            {
                return ((Collection)inputElement).toArray();
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose()
        {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
            // TODO Auto-generated method stub
            
        }
        
    }
    public class TestItemTableLabelProvider extends LabelProvider implements ITableLabelProvider
    {
        private Image image = null;
        public TestItemTableLabelProvider()
        {
            image = LanguageTestPlugin.getImageDescriptor("icons/module.png").createImage();
        }
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex)
        {
            if (element instanceof TestItemMatch && columnIndex == 1)
            {
                return image;
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex)
        {
            if (element instanceof TestItemMatch)
            {
                TestItemMatch match = (TestItemMatch)element;
                switch (columnIndex)
                {
                case 0:
                    return element.toString();
                case 1:
                    return ((IFile)match.getElement()).getName();
                }
                
            }
            return null;
        }

        

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
         */
        public void dispose()
        {
            image.dispose();
        }

        
        
    }
}
