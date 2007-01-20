/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableFontProvider;
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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
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
import org.thanlwinsoft.languagetest.eclipse.editors.ColumnListener;
import org.thanlwinsoft.languagetest.eclipse.editors.TestItemEditor;
import org.thanlwinsoft.languagetest.eclipse.editors.TestItemSorter;
import org.thanlwinsoft.languagetest.eclipse.editors.TestModuleEditor;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.LangEntryType;
import org.thanlwinsoft.schemas.languagetest.TestItemType;

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
    private String [] colLangId = null;
    public final static String ROW_FONT_PREF = TestItemEditor.ROW_FONT_PREF;
    public final static String TABLE_FONT_PREF = TestItemEditor.TABLE_FONT_PREF;
    private static int ROW_FONT_SIZE = 12;
    private static int TABLE_FONT_SIZE = 14;
    TestItemSorter sorter = new TestItemSorter();
    
    public TestItemSearchViewPage()
    {
        LanguageTestPlugin.getPrefStore().setDefault(TABLE_FONT_PREF, TABLE_FONT_SIZE);
        LanguageTestPlugin.getPrefStore().setDefault(ROW_FONT_PREF, ROW_FONT_SIZE);
        
        TABLE_FONT_SIZE = LanguageTestPlugin.getPrefStore().getInt(TABLE_FONT_PREF);
        ROW_FONT_SIZE = LanguageTestPlugin.getPrefStore().getInt(ROW_FONT_PREF);
    }
    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#clear()
     */
    protected void clear()
    {
//        result = null;
//        provider = null;
//        tableViewer = null;
//        treeViewer = null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#configureTableViewer(org.eclipse.jface.viewers.TableViewer)
     */
    protected void configureTableViewer(TableViewer viewer)
    {
        if (viewer.getTable().isDisposed()) return;
        //System.out.println(this + "ConfigureTableViewer " + viewer);
        if (viewer.getContentProvider() == null)
        {
            provider = new TableContentProvider();
            viewer.setContentProvider(provider);
            viewer.setLabelProvider(new TestItemTableLabelProvider());
            viewer.getTable().setHeaderVisible(true);
            FontData fd = JFaceResources.getDialogFont().getFontData()[0];
            // deliberately up the size because if the font size is too small
            // bigger table cell fonts are truncated
            FontData fdBig = new FontData(fd.getName(), TABLE_FONT_SIZE, fd.getStyle());
            Font font = LanguageTestPlugin.getFont(fdBig);                                 
            viewer.getTable().setFont(font);
            
        }
        this.tableViewer = viewer;
        if (result != null)
        {
//          for some reason this needs to be async to get anything displayed
            updateTableViewerInputs(true);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#configureTreeViewer(org.eclipse.jface.viewers.TreeViewer)
     */
    protected void configureTreeViewer(TreeViewer viewer)
    {
        //System.out.println(this + "ConfigureTreeViewer " + viewer);
        this.treeViewer = viewer;
        if (viewer.getTree().isDisposed()) return;
        if (viewer.getContentProvider() == null)
        {
            viewer.setContentProvider(new TestItemSearchTreeProvider());
            viewer.setLabelProvider(new TestItemTableLabelProvider());
        }
        if (result != null)
        {
            // for some reason this needs to be async to get anything displayed
            updateTreeViewerInputs(true);
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
        if (result != null)
        {
            updateTreeViewerInputs(true);
            updateTableViewerInputs(true);
        }
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

    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#getLabel()
     */
    public String getLabel()
    {
        if (result != null)
        {
            result.getLabel();
        }
        return super.getLabel();
    }

    private void updateTableViewerInputs(boolean async)
    {
        Runnable runnable = new Runnable() {

            public void run()
            {
                //System.out.println("updateTableViewer" + result.getMatchCount());
                if (tableViewer != null && !tableViewer.getTable().isDisposed())
                {
                    if (tableViewer.getTable().getColumnCount() == 0)
                    {
                        int width = 200;
                        if (getControl() != null)
                        {
                            // allow room for a scroll bar
                            int totalWidth = getControl().getSize().x - 25;
                            width = (int)Math.floor((float)totalWidth / 
                                    (float)(result.getLanguageCount() + 1));
                        }
                        Iterator i = result.getLanguages().iterator();
                        colLangId = new String[result.getLanguageCount()];
                        int c = 0;
                        Font font = JFaceResources.getTextFont();
                        
                        while (i.hasNext())
                        {
                            TableColumn tc = new TableColumn(tableViewer.getTable(), SWT.LEFT);
                            tc.setResizable(true);
                            UniversalLanguage lang = new UniversalLanguage(i.next().toString());
                            tc.setText(lang.getDescription());
                            tc.setToolTipText(lang.getDescription());
                            //tc.setText(MessageUtil.getString("Matches"));
                            //tc.setToolTipText(MessageUtil.getString("Matches"));
                            tc.setWidth(width);
                            tc.addSelectionListener(new ColumnListener(tableViewer, sorter, 
                                            c, lang.getCode(), lang.getICUlocaleID()));
                            colLangId[c++] = lang.getCode();
                        }
                        TableColumn tc = new TableColumn(tableViewer.getTable(), SWT.LEFT);
                        tc.setResizable(true);
                        tc.setText(MessageUtil.getString("FileColName"));
                        tc.setToolTipText(MessageUtil.getString("FileColName"));
                        tc.setWidth(width);
                        tc.addSelectionListener(new ColumnListener(tableViewer, sorter, 
                                                c, "File", null));
                    }
                    Object [] files = result.getElements();
                    Vector v = new Vector(files.length);
                    for (int i = 0; i < files.length; i++)
                    {
                        Match [] matches = result.getMatches(files[i]);
                        for (int j = 0; j < matches.length; j++)
                        {
                            v.add(matches[j]);
                        }
                    }
                    tableViewer.setInput(v);
                    
                    tableViewer.refresh(true);
                    tableViewer.getTable().redraw();

                    tableViewer.getTable().setToolTipText(result.getLabel());
                    //System.out.println("updatedTableViewer" + tableViewer.getTable().getItemCount() + tableViewer);
                }
                //else System.out.println("no table viewer update");
            }
        };
        if (async)
            this.getControl().getDisplay().asyncExec(runnable);
        else 
        {
            runnable.run();
        }
    }

    private void updateTreeViewerInputs(boolean async)
    {
        Runnable runnable = new Runnable() {

            public void run()
            {
                //System.out.println("updateTreeViewer" + result.getMatchCount());
                if (treeViewer != null && !treeViewer.getTree().isDisposed())
                {
                    
                    treeViewer.setInput(result.getElements());
                    treeViewer.refresh();
                    treeViewer.getTree().redraw();
                    treeViewer.getTree().setToolTipText(result.getLabel());
                }
                //else System.out.println("no tree viewer update");
                
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
            //System.out.println("handleSearchResult " + result);
        }
        if (result != null)
        {
            updateTreeViewerInputs(true);
            updateTableViewerInputs(true);
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
            //System.out.println("setInput" + newSearch);
            result.addListener(new ISearchResultListener() {
                public void searchResultChanged(SearchResultEvent e)
                {
                    handleSearchResultChanged(e);
                }
            });
            updateTreeViewerInputs(false);
            updateTableViewerInputs(false);
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
    public class TestItemTableLabelProvider extends LabelProvider implements ITableLabelProvider, IFontProvider, ITableFontProvider
    {
        private Image image = null;
        private HashMap fontMap = new HashMap();
        private Font font = null;
        public TestItemTableLabelProvider()
        {
            image = LanguageTestPlugin.getImageDescriptor("icons/module.png").createImage();
        }
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex)
        {
            if (element instanceof TestItemMatch && 
                columnIndex == result.getLanguageCount())
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
                if (result == null || columnIndex == result.getLanguageCount())
                {
                    if (match.getElement() instanceof IFile)
                    {
                        IFile f = (IFile)match.getElement();
                        return f.getName();
                    }
                    return "";
                }
                if (colLangId == null || columnIndex > colLangId.length) 
                    return match.toString();
                String langCode = colLangId[columnIndex];
                // last column is the filename
                if (columnIndex == colLangId.length)
                    return ((IFile)match.getElement()).getName();
                
                TestItemType ti = match.getTestItem();
                for (int i = 0; i < ti.sizeOfNativeLangArray(); i++)
                {
                    LangEntryType entry = ti.getNativeLangArray(i);
                    if (entry.getLang().equals(langCode))
                    {
                        return entry.getStringValue();
                    }
                }
                for (int i = 0; i < ti.sizeOfForeignLangArray(); i++)
                {
                    LangEntryType entry = ti.getForeignLangArray(i);
                    if (entry.getLang().equals(langCode))
                    {
                        return entry.getStringValue();
                    }
                }
            }
            return "";
        }

        

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
         */
        public void dispose()
        {
            image.dispose();
        }
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IFontProvider#getFont(java.lang.Object)
         */
        public Font getFont(Object element)
        {
            if (element instanceof TestItemMatch)
            {
                TestItemMatch m = (TestItemMatch)element;
                FontData fd = m.getFontData();
                fd.setHeight(ROW_FONT_SIZE);
                return getFontFromData(fd);
            }
            return JFaceResources.getTextFont();
        }
        private Font getFontFromData(FontData fd)
        {
            return LanguageTestPlugin.getFont(fd);
        }
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableFontProvider#getFont(java.lang.Object, int)
         */
        public Font getFont(Object element, int columnIndex)
        {
            if (element instanceof TestItemMatch && colLangId != null && 
                columnIndex < colLangId.length)
            {
                TestItemMatch m = (TestItemMatch)element;
                FontData fd = (FontData)m.getFontMap().get(colLangId[columnIndex]);
                if (fd != null)
                {
                    fd.setHeight(ROW_FONT_SIZE);
                    return getFontFromData(fd);
                }
            }
            return JFaceResources.getDialogFont();
        }

        
        
    }
}
