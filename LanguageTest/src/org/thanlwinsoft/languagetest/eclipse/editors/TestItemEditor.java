/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.editors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.Perspective;
import org.thanlwinsoft.languagetest.eclipse.TestModuleAdapter;
import org.thanlwinsoft.languagetest.eclipse.views.TestView;
import org.thanlwinsoft.schemas.languagetest.ForeignLangType;
import org.thanlwinsoft.schemas.languagetest.LangType;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.NativeLangType;
import org.thanlwinsoft.schemas.languagetest.SoundFileType;
import org.thanlwinsoft.schemas.languagetest.TestItemType;

/**
 * @author keith
 *
 */
public class TestItemEditor extends EditorPart
{
    private TestModuleEditor parent = null;
    private TableViewer tableViewer = null;
    private int langCount = 0;
    private static final int SOUND_COL_ID = 0;
    private static final int PICTURE_COL_ID = 1;
    private static final int CREATOR_COL_ID = 2;
    private static final int LANG_COL_OFFSET = 2;
    private static final int CREATION_DATE_ID = 3;
    private static final String SOUND_COL = "Sound";
    private static final String PICTURE_COL = "Picture";
    private static final String CREATOR_COL = "Creator";
    private static final String CREATION_DATE = "CDate";
    
    private TableColumn soundCol = null;
    private TableColumn pictureCol = null;
    private TableColumn creatorCol = null;
    private TableColumn cDateCol = null;
    private TableColumn [] langCols = null;
    private String [] langIds = null;
    
    public TestItemEditor(TestModuleEditor parent)
    {
        super();
        this.parent = parent;
        this.setPartName(MessageUtil.getString("TestItemEditor"));
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave(IProgressMonitor monitor)
    {
        parent.doSave(monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    public void doSaveAs()
    {
        parent.doSaveAs();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
     */
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException
    {
        this.setSite(site);
        setupLangColumns();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#isDirty()
     */
    public boolean isDirty()
    {
        
        return parent.isDirty();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    public boolean isSaveAsAllowed()
    {
        
        return parent.isSaveAsAllowed();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parentControl)
    {
        FillLayout layout = new FillLayout();
        parentControl.setLayout(layout);
        
        tableViewer = new TableViewer(parentControl, SWT.H_SCROLL | SWT.V_SCROLL);
        tableViewer.setContentProvider(new TestItemContentProvider());
        tableViewer.setLabelProvider(new TestItemLabelProvider());
        tableViewer.getTable().setHeaderVisible(true);
        IViewPart testView = getEditorSite().getPage().findView(Perspective.TEST_VIEW);
        tableViewer.addSelectionChangedListener((TestView)testView);
        soundCol = new TableColumn(tableViewer.getTable(), SWT.LEFT);
        soundCol.setText(MessageUtil.getString("SoundColumn"));
        soundCol.setToolTipText(MessageUtil.getString("SoundColumn"));
        soundCol.setResizable(true);
        soundCol.setMoveable(true);
        soundCol.setWidth(20);
        pictureCol = new TableColumn(tableViewer.getTable(), SWT.LEFT);
        pictureCol.setText(MessageUtil.getString("PictureColumn"));
        pictureCol.setToolTipText(MessageUtil.getString("PictureColumn"));
        pictureCol.setResizable(true);
        pictureCol.setMoveable(true);
        pictureCol.setWidth(20);
        creatorCol = new TableColumn(tableViewer.getTable(), SWT.LEFT);
        creatorCol.setText(MessageUtil.getString("CreatorColumn"));
        creatorCol.setToolTipText(MessageUtil.getString("CreatorColumn"));
        creatorCol.setResizable(true);
        creatorCol.setMoveable(true);
        creatorCol.setWidth(100);
        cDateCol = new TableColumn(tableViewer.getTable(), SWT.LEFT);
        cDateCol.setText(MessageUtil.getString("CDateColumn"));
        cDateCol.setToolTipText(MessageUtil.getString("CDateColumn"));
        cDateCol.setResizable(true);
        cDateCol.setMoveable(true);
        cDateCol.setWidth(100);
        tableViewer.setInput(parent.getDocument());
        tableViewer.getTable().setData(parent.getDocument());
        tableViewer.refresh();
    }
    
    protected void setModule(LanguageModuleDocument doc)
    {
        if (tableViewer != null)
        {
            tableViewer.getTable().setData(doc);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus()
    {
        // TODO Auto-generated method stub

    }
    
    protected void setupLangColumns()
    {
        if (parent.getDocument() == null || tableViewer == null) return;
        LangType [] langs = parent.getDocument().getLanguageModule().getLangArray();
        if (langCols != null)
        {
            for (int i = 0; i < langs.length; i++)
            {
                langCols[i].dispose();
            }
        }
        langCols = new TableColumn[langs.length];
        langIds = new String[langs.length];
        for (int i = 0; i < langs.length; i++)
        {
            LangType lang = langs[i];
            langCols[i] = new TableColumn(tableViewer.getTable(), SWT.LEFT,
                                          LANG_COL_OFFSET + i);
            langCols[i].setText(lang.getLang());
            langCols[i].setResizable(true);
            langCols[i].setWidth(200);
            langIds[i] = lang.getLang();
        }
    }
    
    protected class TestItemContentProvider implements IStructuredContentProvider
    {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object inputElement)
        {
            if (inputElement instanceof LanguageModuleDocument)
            {
                LanguageModuleDocument doc = (LanguageModuleDocument)inputElement;
                return doc.getLanguageModule().getTestItemArray();
            }
            // TODO Auto-generated method stub
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
            if (tableViewer != null)
            {
                setupLangColumns();
                tableViewer.refresh();
            }
        }
        
    }
    protected class TestItemLabelProvider implements ITableLabelProvider
    {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex)
        {
            // TODO Auto-generated method stub
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex)
        {
            // TODO Auto-generated method stub
            int numLangs = langCols.length;
            if (element instanceof TestItemType)
            {
                TestItemType testItem = (TestItemType)element;
                if (columnIndex == SOUND_COL_ID)
                {
                    SoundFileType soundFile = testItem.getSoundFile();
                    if (soundFile == null) return "";
                    return soundFile.getStringValue();
                }
                else if (columnIndex == PICTURE_COL_ID)
                {
                    return testItem.getImg();
                }
                else if (columnIndex == numLangs + CREATOR_COL_ID)
                {
                    return testItem.getCreator();
                }
                else if (columnIndex == numLangs + CREATION_DATE_ID)
                {
                    long time = testItem.getCreationTime();
                    DateFormat df = new SimpleDateFormat();
                    return df.format(new Date(time));
                }
                else if (columnIndex < LANG_COL_OFFSET + 
                         testItem.getNativeLangArray().length)
                {
                    NativeLangType langType = testItem.getNativeLangArray(columnIndex - LANG_COL_OFFSET);
                    return langType.getStringValue();
                }
                else if (columnIndex < LANG_COL_OFFSET + 
                        testItem.getNativeLangArray().length + 
                        testItem.getForeignLangArray().length)
                {
                    int offset = columnIndex - LANG_COL_OFFSET - 
                        testItem.getNativeLangArray().length;
                    ForeignLangType langType = testItem.getForeignLangArray(offset);
                    return langType.getStringValue();
                }
                else
                {
                    LanguageTestPlugin.log(IStatus.WARNING,
                            "Unexpected column index " + columnIndex);
                }
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void addListener(ILabelProviderListener listener)
        {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
         */
        public void dispose()
        {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
         */
        public boolean isLabelProperty(Object element, String property)
        {
            // TODO Auto-generated method stub
            return false;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void removeListener(ILabelProviderListener listener)
        {
            // TODO Auto-generated method stub
            
        }
        
    }
    public class FontProvider implements ITableFontProvider
    {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableFontProvider#getFont(java.lang.Object, int)
         */
        public Font getFont(Object element, int columnIndex)
        {
            if (columnIndex < LANG_COL_OFFSET || 
                columnIndex >= LANG_COL_OFFSET + langIds.length)
            {
                return getSite().getShell().getDisplay().getSystemFont();
            }
            if (parent != null && parent.getDocument() != null)
            {
                int id = columnIndex - LANG_COL_OFFSET;
                return TestModuleAdapter.getFont(parent.getDocument().getLanguageModule(),
                        langIds[id]);
            }
            // TODO Auto-generated method stub
            return null;
        }
        
    }
}
