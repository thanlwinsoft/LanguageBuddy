/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.editors;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.Perspective;
import org.thanlwinsoft.languagetest.eclipse.TestModuleAdapter;
import org.thanlwinsoft.languagetest.eclipse.export.ExportAction;
import org.thanlwinsoft.languagetest.eclipse.views.MetaDataView;
import org.thanlwinsoft.languagetest.eclipse.views.RecordingView;
import org.thanlwinsoft.languagetest.eclipse.views.TestView;
import org.thanlwinsoft.languagetest.eclipse.workspace.WorkspaceLanguageManager;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.module.ForeignLangType;
import org.thanlwinsoft.schemas.languagetest.module.LangEntryType;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.thanlwinsoft.schemas.languagetest.module.LangTypeType;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType;
import org.thanlwinsoft.schemas.languagetest.module.NativeLangType;
import org.thanlwinsoft.schemas.languagetest.module.SoundFileType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;

/**
 * @author keith
 *
 */
public class TestItemEditor extends EditorPart implements ISelectionProvider
{
    private TestModuleEditor parent = null;
    private TableViewer tableViewer = null;
    private int langCount = 0;
    private int nativeLangCount = 0;
    private IFile moduleFile = null;
    private Menu [] langMenu = new Menu[3];
    // fixed columns are created first, then language columns
    // setColumnOrder is then used to move the language columns before the
    // Creator and Creation columns. This is needed because it doesn't seem
    // possible to remove columns, only to add them.
    private static final int SOUND_COL_ID = 0;
    private static final int PICTURE_COL_ID = 1;
    private static final int CREATOR_COL_ID = 2;
    private static final int LANG_COL_OFFSET = 2;
    private static final int CREATION_DATE_ID = 3;
    private static final int NUM_NON_LANG_COL = 4;
    private static final int SOUND_COL_WIDTH = 20;
    private static final int PICTURE_COL_WIDTH = 20;
    private static final int CREATOR_COL_WIDTH = 100;
    private static final int CREATION_COL_WIDTH = 100;
    private static final int LANG_COL_WIDTH = 200;
    public static final String SOUND_COL = "Sound";
    public static final String PICTURE_COL = "Picture";
    public static final String CREATOR_COL = "Creator";
    public static final String CREATION_DATE = "CDate";
    
    private TableColumn soundCol = null;
    private TableColumn pictureCol = null;
    private TableColumn creatorCol = null;
    private TableColumn cDateCol = null;
    private Vector<String> langIds = null;
//    private String lastPropertyChanged = null;
    
    private TestItemCellModifier cellModifier = null;
    private TestItemLabelProvider labelProvider = null;
    
    private ClipboardAction copyAction = null;
    private ClipboardAction cutAction = null;
    private ClipboardAction pasteAction = null;
    private MenuItem cutLangItem = null;
    private MenuItem copyLangItem = null;
    private MenuItem pasteLangItem = null;
    private Action insertAction = null;
    private Menu popup = null;
    private TestItemSorter sorter = null;
    public final static String ROW_FONT_PREF = "TableRowFontSize";
    public final static String TABLE_FONT_PREF = "TableFontSize";
    private static int ROW_FONT_SIZE = 12;
    private static int TABLE_FONT_SIZE = 14;
    
    public TestItemEditor(TestModuleEditor parent)
    {
        super();
        this.parent = parent;
        this.setPartName(MessageUtil.getString("TestItemEditor"));
        this.setContentDescription(MessageUtil.getString("TestItemEditor"));
        langIds = new Vector<String>(2,2);// lang size may grow, but 2 is minimum
        LanguageTestPlugin.getPrefStore().setDefault(TABLE_FONT_PREF, TABLE_FONT_SIZE);
        LanguageTestPlugin.getPrefStore().setDefault(ROW_FONT_PREF, ROW_FONT_SIZE);
        
        TABLE_FONT_SIZE = LanguageTestPlugin.getPrefStore().getInt(TABLE_FONT_PREF);
        ROW_FONT_SIZE = LanguageTestPlugin.getPrefStore().getInt(ROW_FONT_PREF);
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

    /** get the Parent Editor
     * 
     * @return TestModuleEditor
     */
    public TestModuleEditor getParent()
    {
        return parent;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parentControl)
    {
        FillLayout layout = new FillLayout();
        parentControl.setLayout(layout);
        
        tableViewer = new TableViewer(parentControl, SWT.H_SCROLL | SWT.V_SCROLL
        		| SWT.MULTI | SWT.FULL_SELECTION); // need full selecion on Windows
        tableViewer.setContentProvider(new TestItemContentProvider());
        labelProvider = new TestItemLabelProvider();
        tableViewer.setLabelProvider(labelProvider);
        // set a tall font, so rows are high enough
        FontData fd = JFaceResources.getDialogFont().getFontData()[0];
        FontData tallFont = new FontData(fd.getName(), TABLE_FONT_SIZE, fd.getStyle());
        Font font = LanguageTestPlugin.getFont(tallFont);
        tableViewer.getTable().setFont(font);
        tableViewer.getTable().setHeaderVisible(true);
        cellModifier = new TestItemCellModifier();
        tableViewer.setCellModifier(cellModifier);
        sorter = new TestItemSorter();
        IWorkbenchPage page = getEditorSite().getPage();
        IViewPart testView = page.findView(Perspective.TEST_VIEW);
        if (testView != null)
            ((TestView)testView).addSelectionProvider(tableViewer);
        RecordingView recordingView = (RecordingView)page.findView(RecordingView.ID);
        if (recordingView != null)
            addSelectionChangedListener(recordingView.getRecorder());
        MetaDataView metaView = (MetaDataView)page.findView(MetaDataView.ID);
        if (metaView != null)
            addSelectionChangedListener(metaView);
        
        soundCol = new TableColumn(tableViewer.getTable(), SWT.LEFT);
        soundCol.setText(MessageUtil.getString("SoundColumn"));
        soundCol.setToolTipText(MessageUtil.getString("SoundColumn"));
        soundCol.setResizable(true);
        soundCol.setMoveable(true);
        soundCol.setWidth(SOUND_COL_WIDTH);
        soundCol.addSelectionListener(new ColumnListener(tableViewer, 
                        sorter, SOUND_COL_ID, SOUND_COL, null));
        pictureCol = new TableColumn(tableViewer.getTable(), SWT.LEFT);
        pictureCol.setText(MessageUtil.getString("PictureColumn"));
        pictureCol.setToolTipText(MessageUtil.getString("PictureColumn"));
        pictureCol.setResizable(true);
        pictureCol.setMoveable(true);
        pictureCol.setWidth(PICTURE_COL_WIDTH);
        pictureCol.addSelectionListener(new ColumnListener(tableViewer, 
                        sorter, PICTURE_COL_ID, PICTURE_COL, null));
        creatorCol = new TableColumn(tableViewer.getTable(), SWT.LEFT);
        creatorCol.setText(MessageUtil.getString("CreatorColumn"));
        creatorCol.setToolTipText(MessageUtil.getString("CreatorColumn"));
        creatorCol.setResizable(true);
        creatorCol.setMoveable(true);
        creatorCol.setWidth(CREATOR_COL_WIDTH);
        creatorCol.addSelectionListener(new ColumnListener(tableViewer, 
                        sorter, CREATOR_COL_ID, CREATOR_COL, null));
        cDateCol = new TableColumn(tableViewer.getTable(), SWT.LEFT);
        cDateCol.setText(MessageUtil.getString("CDateColumn"));
        cDateCol.setToolTipText(MessageUtil.getString("CDateColumn"));
        cDateCol.setResizable(true);
        cDateCol.setMoveable(true);
        cDateCol.setWidth(CREATION_COL_WIDTH);
        cDateCol.addSelectionListener(new ColumnListener(tableViewer, 
                        sorter, CREATION_DATE_ID, CREATION_DATE, null));
        makeActions();
        enableActions();
        
        
        popup = new Menu(tableViewer.getControl());
        MenuItem insertItem = new MenuItem(popup, SWT.PUSH);
        insertItem.setText(MessageUtil.getString("InsertItemBefore"));
        
        insertItem.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e){}
            public void widgetSelected(SelectionEvent e)
            {
                int i = tableViewer.getTable().getSelectionIndex();
                if (i < 0) i = tableViewer.getTable().getItemCount();
                insertItem(i);
            }
        });
        MenuItem insertItemAfter = new MenuItem(popup, SWT.PUSH);
        insertItemAfter.setText(MessageUtil.getString("InsertItemAfter"));
        
        insertItemAfter.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e){}
            public void widgetSelected(SelectionEvent e)
            {
                int i = tableViewer.getTable().getSelectionIndex() + 1;
                if (i < 0) i = tableViewer.getTable().getItemCount();
                insertItem(i);
            }
        });
        MenuItem deleteItem = new MenuItem(popup, SWT.PUSH);
        deleteItem.setText(MessageUtil.getString("DeleteItem"));
        
        deleteItem.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e){}
            public void widgetSelected(SelectionEvent e)
            {
                deleteSelection();
            }
        });
        new MenuItem(popup, SWT.SEPARATOR);
        MenuItem cutItem = new MenuItem(popup, SWT.PUSH);
        cutItem.setText(cutAction.getText());
        cutItem.addSelectionListener(cutAction);
        MenuItem copyItem = new MenuItem(popup, SWT.PUSH);
        copyItem.setText(copyAction.getText());
        copyItem.addSelectionListener(copyAction);
        MenuItem pasteItem = new MenuItem(popup, SWT.PUSH);
        pasteItem.setText(pasteAction.getText());
        pasteItem.addSelectionListener(pasteAction);
        cutLangItem = new MenuItem(popup, SWT.CASCADE);
        cutLangItem.setText(MessageUtil.getString("CutFromLang"));
        copyLangItem = new MenuItem(popup, SWT.CASCADE);
        copyLangItem.setText(MessageUtil.getString("CopyFromLang"));
        pasteLangItem = new MenuItem(popup, SWT.CASCADE);
        pasteLangItem.setText(MessageUtil.getString("PasteIntoLang"));
        
        new MenuItem(popup, SWT.SEPARATOR);
        MenuItem export = new MenuItem(popup, SWT.PUSH);
        export.setText(MessageUtil.getString("ExportTypeTitle"));
        
        export.addSelectionListener(new SelectionListener(){
            public void widgetDefaultSelected(SelectionEvent e) {}
            public void widgetSelected(SelectionEvent e)
            {
                if (parent.getEditorInput() instanceof FileEditorInput)
                {
                    IFile f = ((FileEditorInput)parent.getEditorInput()).getFile();
                    ExportAction a = new ExportAction(f, "pdf");
                    a.run();
                }
            }});
        
        popup.setEnabled(true);
        // the table should not be initialized until after the popup 
        // menu is created becasue setupLangColumns uses some of the items
        tableViewer.setInput(parent.getDocument());
        tableViewer.getTable().setData(parent.getDocument());
        tableViewer.refresh();
        tableViewer.getTable().pack();

        tableViewer.getTable().addMouseListener(new MouseListener()
        {
            public void mouseDoubleClick(MouseEvent e) { }
            public void mouseDown(MouseEvent e)
            {
                if (e.button == 3)
                    popup.setVisible(true);
            }
            public void mouseUp(MouseEvent e) {}
        });
        tableViewer.getTable().addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {}
            public void keyReleased(KeyEvent e)
            {
                if (e.keyCode == SWT.F2)
                {
                    TestItemType item = getSelectedItem();
                    if (item != null && langCount > 0)
                    {
                        tableViewer.editElement(item, NUM_NON_LANG_COL);
                    }
                }
            }
        });
        
        tableViewer.setComparator(null);
        
    }
    
    private void insertItem(int i)
    {
        TestItemType item = parent.getDocument().getLanguageModule().insertNewTestItem(i);
        // item = parent.getDocument().getLanguageModule().addNewTestItem();
        item.setCreationTime((new Date()).getTime());
        IProject userProject = WorkspaceLanguageManager.getUserProject(); 
        if (userProject != null)
            item.setCreator(userProject.getName());
        else
            item.setCreator(System.getProperty("user.name"));
        parent.setDirty(true);
        parent.firePropertyChange(PROP_DIRTY);
        tableViewer.refresh();
        tableViewer.getTable().select(i);
        if (langCount > 0)
            tableViewer.editElement(item, NUM_NON_LANG_COL);
    }
    
    public void deleteSelection()
    {
        int [] selection = tableViewer.getTable().getSelectionIndices();
        LanguageModuleType lm = parent.getDocument().getLanguageModule();
        // work backwards so indices remain valid after deletion
        Arrays.sort(selection);
        for (int i = selection.length - 1; i >= 0; i--)
        {
            lm.removeTestItem(selection[i]);
        }
        tableViewer.getTable().deselectAll();
//        TestItemType item = getSelectedItem();
//        if (item != null)
//        {
//            TestItemType [] items = parent.getDocument().getLanguageModule().getTestItemArray();
//            for (int i = 0; i < items.length; i++)
//            {
//                if (item.equals(items[i]))
//                {
//                    tableViewer.setSelection(null);
//                    parent.getDocument().getLanguageModule().removeTestItem(i);
//                    break;
//                }
//            }
            parent.setDirty(true);
            parent.firePropertyChange(PROP_DIRTY);
            tableViewer.refresh();
            tableViewer.getTable().redraw();
//        }
    }
    
    public void selectItem(int i)
    {
        if (i < tableViewer.getTable().getItemCount())
        {
            tableViewer.getTable().setSelection(i);
            tableViewer.getTable().showSelection();
        }
    }
    
    public void pasteItems(TestItemType [] items, String langCode)
    {
        int [] selection = tableViewer.getTable().getSelectionIndices(); 
        int langIndex = langIds.indexOf(langCode);
        
        if (selection.length == items.length)
        {
            LanguageModuleType lm = parent.getDocument().getLanguageModule();
            for (int i = 0; i < items.length; i++)
            {
                TestItemType target = lm.getTestItemArray(selection[i]);
                if (langCode.equals(PICTURE_COL))
                {
                    target.setImg(items[i].getImg());
                }
                else if (langCode.equals(SOUND_COL))
                {
                    target.setSoundFile(items[i].getSoundFile());
                }
                else 
                {
                    LangEntryType let = null;
                    int k;
                    // this should get the entry that matches the requested 
                    // language if that isn't found, then default to whatever
                    // is left - the user may want to copy and paste between
                    // languages
                    for (k = 0; k < items[i].sizeOfNativeLangArray(); k++)
                    {
                        let = items[i].getNativeLangArray(k);
                        if (let.getLang().equals(langCode))
                            break;
                    }
                    if (let == null || !let.getLang().equals(langCode))
                    {
                        for (k = 0; k < items[i].sizeOfForeignLangArray(); k++)
                        {
                            let = items[i].getForeignLangArray(k);
                            if (let.getLang().equals(langCode))
                                break;
                        }
                    }
                    // was anything found? If not go to next item
                    if (let == null) continue;
                    
                    if (langIndex < nativeLangCount)
                    {
                        // look for existing entry in target item
                        int j;
                        for (j = 0; j < target.sizeOfNativeLangArray(); j++)
                        {
                            
                            NativeLangType lt = target.getNativeLangArray(j);
                            if (lt.getLang().equals(langCode))
                            {
                                lt.setStringValue(let.getStringValue());
                                break;
                            }
                        }
                        // if an existing entry has been set, this check will fail
                        if (j == target.sizeOfNativeLangArray())
                        {
                            // there is no item yet, so create one now
                            NativeLangType nlt = target.addNewNativeLang();
                            nlt.setLang(langCode);
                            nlt.setStringValue(let.getStringValue());
                        }
                    }
                    else // foreign
                    {
                        // look for existing entry in target item
                        int j;
                        for (j = 0; j < target.sizeOfForeignLangArray(); j++)
                        {
                            
                            ForeignLangType lt = target.getForeignLangArray(j);
                            if (lt.getLang().equals(langCode))
                            {
                                lt.setStringValue(let.getStringValue());
                                break;
                            }
                        }
                        if (j == target.sizeOfForeignLangArray())
                        {
                            // there is no item yet, so create one now
                            ForeignLangType nlt = target.addNewForeignLang();
                            nlt.setLang(langCode);
                            nlt.setStringValue(let.getStringValue());
                        }
                    }
                }
            }
            parent.setDirty(true);
            parent.firePropertyChange(PROP_DIRTY);
            tableViewer.refresh();
        }
        else
        {
            MessageDialog.openInformation(this.getSite().getShell(), 
                    MessageUtil.getString("PasteSelectionMismatchTitle"), 
                    MessageUtil.getString("PasteSelectionMismatchDesc",
                            Integer.toString(selection.length), 
                            Integer.toString(items.length)));
        }
    }
    
    public void pasteItems(String [] items, String langCode)
    {
        int [] selection = tableViewer.getTable().getSelectionIndices(); 
        int langIndex = langIds.indexOf(langCode);
        
        if (selection.length == items.length)
        {
            LanguageModuleType lm = parent.getDocument().getLanguageModule();
            for (int i = 0; i < items.length; i++)
            {
                TestItemType target = lm.getTestItemArray(selection[i]);
                if (langCode.equals(PICTURE_COL))
                {
                    target.setImg(items[i]);
                }
                else if (langCode.equals(SOUND_COL))
                {
                    if (target.isSetSoundFile())
                    {
                        target.getSoundFile().setStringValue(items[i]);
                    }
                    else
                    {
                        SoundFileType sft = target.addNewSoundFile();
                        sft.setStringValue(items[i]);
                    }
                }
                else 
                {
                    
                    if (langIndex < nativeLangCount)
                    {
                        // look for existing entry in target item
                        int j;
                        for (j = 0; j < target.sizeOfNativeLangArray(); j++)
                        {
                            
                            NativeLangType lt = target.getNativeLangArray(j);
                            if (lt.getLang().equals(langCode))
                            {
                                lt.setStringValue(items[i]);
                                break;
                            }
                        }
                        // if an existing entry has been set, this check will fail
                        if (j == target.sizeOfNativeLangArray())
                        {
                            // there is no item yet, so create one now
                            NativeLangType nlt = target.addNewNativeLang();
                            nlt.setLang(langCode);
                            nlt.setStringValue(items[i]);
                        }
                    }
                    else // foreign
                    {
                        // look for existing entry in target item
                        int j;
                        for (j = 0; j < target.sizeOfForeignLangArray(); j++)
                        {
                            
                            ForeignLangType lt = target.getForeignLangArray(j);
                            if (lt.getLang().equals(langCode))
                            {
                                lt.setStringValue(items[i]);
                                break;
                            }
                        }
                        if (j == target.sizeOfForeignLangArray())
                        {
                            // there is no item yet, so create one now
                            ForeignLangType nlt = target.addNewForeignLang();
                            nlt.setLang(langCode);
                            nlt.setStringValue(items[i]);
                        }
                    }
                }
            }
            parent.setDirty(true);
            parent.firePropertyChange(PROP_DIRTY);
            tableViewer.refresh();
        }
        else
        {
            MessageDialog.openInformation(this.getSite().getShell(), 
                    MessageUtil.getString("PasteSelectionMismatchTitle"), 
                    MessageUtil.getString("PasteSelectionMismatchDesc",
                            Integer.toString(selection.length), 
                            Integer.toString(items.length)));
        }
    }
    
    public void pasteItems(TestItemType [] items)
    {
        int [] selection = tableViewer.getTable().getSelectionIndices(); 
        String message = MessageUtil.getString("PasteBeforeOrAfter");
        String [] buttonText;
        if (selection.length == items.length)
        {
            message = MessageUtil.getString("PasteOverwriteBeforeOrAfter");
            buttonText = new String[4];
            buttonText[0] = MessageUtil.getString("PasteBefore");
            buttonText[1] = MessageUtil.getString("PasteAfter");
            buttonText[2] = MessageUtil.getString("Overwrite");
            buttonText[3] = MessageUtil.getString("Cancel");
        }
        else
        {
            buttonText = new String[3];
            buttonText[0] = MessageUtil.getString("InsertBefore");
            buttonText[1] = MessageUtil.getString("InsertAfter");
            buttonText[2] = MessageUtil.getString("Cancel");
        }
        MessageDialog dialog = new MessageDialog(this.getSite().getShell(),
                MessageUtil.getString("PasteTitle"), null, message,
                MessageDialog.QUESTION, buttonText, 1);
        int choice = dialog.open();
        int insertionIndex = 0;
        
        LanguageModuleType lm = parent.getDocument().getLanguageModule();
        if (choice == buttonText.length - 1) return ;
        switch (choice)
        {
        case 0:
            if (selection.length > 0)
            {
                insertionIndex = selection[0];
            }
            break;
        case 1:
            
            if (selection.length == 0)
                insertionIndex = tableViewer.getTable().getItemCount();
            else
                insertionIndex = selection[selection.length - 1] + 1;
            break;
        case 2:
            for (int i = 0; i < items.length; i++)
            {
                lm.setTestItemArray(selection[i], items[i]);
            }
            parent.setDirty(true);
            parent.firePropertyChange(PROP_DIRTY);
            tableViewer.refresh();
            return;
        }
        for (int i = 0; i < items.length; i++)
        {
            lm.insertNewTestItem(insertionIndex + i);
            lm.setTestItemArray(insertionIndex + i, items[i]);
        }
        parent.setDirty(true);
        parent.firePropertyChange(PROP_DIRTY);
        tableViewer.refresh();
    }
    
    protected void enableActions()
    {
        IActionBars bar = this.getEditorSite().getActionBars();
        bar.setGlobalActionHandler(
                ActionFactory.COPY.getId(),
                copyAction);
        bar.setGlobalActionHandler("InsertTestItem", insertAction);
        bar.updateActionBars();
    }
    
    protected TestItemType getSelectedItem()
    {
        TestItemType item = null;
        ISelection s = tableViewer.getSelection();
        if (s instanceof StructuredSelection)
        {
            StructuredSelection ss = (StructuredSelection)s;
            if (ss.getFirstElement() instanceof TestItemType)
            {
                item = (TestItemType)ss.getFirstElement();
            }
        }
        return item;
    }
    
    public TestItemType [] getSelectedItems()
    {
        TestItemType [] items = null;
        ISelection s = tableViewer.getSelection();
        if (s instanceof StructuredSelection)
        {
            StructuredSelection ss = (StructuredSelection)s;
            items = new TestItemType[ss.size()];
            int row = 0;
            Iterator i = ss.iterator();
            while (i.hasNext())
            {
                Object o = i.next();
                if (ss.getFirstElement() instanceof TestItemType)
                {
                    items[row++] = (TestItemType)o;
                }
            }
        }
        return items;
    }
    
    protected void setModule(LanguageModuleDocument doc)
    {
        if (tableViewer != null)
        {
        	tableViewer.setInput(doc);
            tableViewer.getTable().setData(doc);
            IEditorInput ei =parent.getEditorInput(); 
            if (ei instanceof FileEditorInput)
            {
                FileEditorInput fei = (FileEditorInput)ei;
                ResourceAttributes ra = fei.getFile().getResourceAttributes();
                if (ra.isReadOnly())
                {
                    
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus()
    {
    	tableViewer.getTable().setFocus();
        enableActions();
        popup.setVisible(true);
    }
    
    protected void setupLangColumns()
    {
        if (parent.getDocument() == null || tableViewer == null) return;
        LangType [] langs = parent.getDocument().getLanguageModule().getLangArray();
        int maxColumns = Math.max(NUM_NON_LANG_COL + langs.length, 
                tableViewer.getTable().getColumnCount());
        String [] colProperties = new String[maxColumns];
        int [] colOrder = new int[maxColumns];
        colProperties[PICTURE_COL_ID] = PICTURE_COL;
        colProperties[SOUND_COL_ID] = SOUND_COL;
        colProperties[CREATOR_COL_ID] = CREATOR_COL;
        colProperties[CREATION_DATE_ID] = CREATION_DATE;
        colOrder[PICTURE_COL_ID] = PICTURE_COL_ID;
        colOrder[SOUND_COL_ID] = SOUND_COL_ID;
        colOrder[CREATOR_COL_ID + langs.length] = CREATOR_COL_ID;
        colOrder[CREATION_DATE_ID + langs.length] = CREATION_DATE_ID;
        CellEditor [] editors = new CellEditor[maxColumns];
        IEditorInput input = parent.getEditorInput();
        moduleFile = null;
        if (input instanceof IFileEditorInput)
        {
            moduleFile = ((IFileEditorInput)input).getFile();
        }
        editors[PICTURE_COL_ID] = new PictureCellEditor(tableViewer.getTable(),
        		SWT.LEFT, moduleFile);
        editors[SOUND_COL_ID] = new SoundCellEditor(tableViewer.getTable(),
        		SWT.LEFT, moduleFile);
        editors[CREATOR_COL_ID] = null;
        editors[CREATION_DATE_ID] = null;
        for (int i = 0; i < langMenu.length; i++)
        {
            if (langMenu[i] != null)
            {
                langMenu[i].dispose();
            }
        }
        langMenu[0] = new Menu(cutLangItem);
        langMenu[1] = new Menu(copyLangItem);
        langMenu[2] = new Menu(pasteLangItem);
        cutLangItem.setMenu(langMenu[0]);
        copyLangItem.setMenu(langMenu[1]);
        pasteLangItem.setMenu(langMenu[2]);
        
        langIds.clear();
        langCount = langs.length;
        nativeLangCount = 0;
        for (int i = 0; i < langs.length; i++)
        {
            LangType lang = langs[i];
            if (lang.getType().equals(LangTypeType.NATIVE)) nativeLangCount++;
            TableColumn column = null;
            colOrder[LANG_COL_OFFSET + i] = NUM_NON_LANG_COL + i;
            try
            {
                UniversalLanguage ul = new UniversalLanguage(lang.getLang());
                final String ulCode = ul.getCode();
                final String localeCode = ul.getICUlocaleID();
                final int colIndex = NUM_NON_LANG_COL + i;
                if (tableViewer.getTable().getColumnCount() <= i + NUM_NON_LANG_COL)
                {
                    column = new TableColumn(tableViewer.getTable(), SWT.LEFT);
                    column.addSelectionListener(new ColumnListener(tableViewer, 
                                    sorter, colIndex, ulCode, localeCode));
                }
                else
                {
                    column = tableViewer.getTable().getColumn(i + NUM_NON_LANG_COL);
                }
            
                column.setText(ul.getDescription());
                for (int m = 0; m < langMenu.length; m++)
                {
                    MenuItem langItem = new MenuItem(langMenu[m], SWT.PUSH);
                    langItem.setText(ul.getDescription());
                    langItem.addSelectionListener(new LangMenuListener(ul.getCode()));
                }
            }
            catch(IllegalArgumentException e)
            {
            	column.setText(lang.getLang());
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                System.out.println(e);
            }
            column.setResizable(true);
            column.setWidth(LANG_COL_WIDTH);
            langIds.add(i, lang.getLang());
            colProperties[i + NUM_NON_LANG_COL] = lang.getLang();
            
            editors[i + NUM_NON_LANG_COL] = new TextCellEditor(tableViewer.getTable());
            editors[i + NUM_NON_LANG_COL].setValidator(cellModifier);
            

        }
        for (int j = NUM_NON_LANG_COL + langs.length; 
             j < tableViewer.getTable().getColumnCount(); j++)
        {
            TableColumn unusedColumn = tableViewer.getTable().getColumn(j);
            unusedColumn.setWidth(0);
            unusedColumn.setResizable(false);
            colProperties[j] = "unused" + j;
            editors[j] = null;
            colOrder[j] = j;
        }
        // remaining items to language menu
        for (int m = 0; m < langMenu.length; m++)
        {
            MenuItem langItem = new MenuItem(langMenu[m], SWT.PUSH);
            langItem.setText(MessageUtil.getString("PictureColumn"));
            langItem.addSelectionListener(new LangMenuListener(PICTURE_COL));
            langItem = new MenuItem(langMenu[m], SWT.PUSH);
            langItem.setText(MessageUtil.getString("SoundColumn"));
            langItem.addSelectionListener(new LangMenuListener(SOUND_COL));
        }
        
        tableViewer.setColumnProperties(colProperties);
        tableViewer.setCellEditors(editors);
        tableViewer.getTable().setColumnOrder(colOrder);
        tableViewer.refresh();
                
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
                LanguageModuleDocument doc = 
                    (LanguageModuleDocument)inputElement;
                TestItemType lastTI = TestItemType.Factory.newInstance();
                Object [] elements = Arrays.copyOf(
                        doc.getLanguageModule().getTestItemArray(), 
                        doc.getLanguageModule().sizeOfTestItemArray() + 1);
                elements[doc.getLanguageModule().sizeOfTestItemArray()] = lastTI;
                return elements;
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
            if (tableViewer != null)
            {
                setupLangColumns();
                parent.updateTestView();
            }
        }
        
    }
    
    
    protected class TestItemLabelProvider implements ITableLabelProvider,  
        ITableFontProvider
    {
    	private HashSet<ILabelProviderListener> listeners = null;
    	public TestItemLabelProvider()
    	{
    		listeners = new HashSet<ILabelProviderListener>();
    	}
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
            try
            {
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
                else if (columnIndex == CREATOR_COL_ID)
                {
                    return testItem.getCreator();
                }
                else if (columnIndex == CREATION_DATE_ID)
                {
                    if (testItem.isSetCreationTime())
                    {
                        long time = testItem.getCreationTime();
                        DateFormat df = new SimpleDateFormat();
                        return df.format(new Date(time));
                    }
                    else return "";
                }
                else if (columnIndex < NUM_NON_LANG_COL + langCount)
                {
                    NativeLangType [] nLangs = testItem.getNativeLangArray();
                    Object langId = langIds.elementAt(columnIndex - NUM_NON_LANG_COL);
                    int i;
                    for (i = 0; i < nLangs.length; i++)
                    {
                        if (nLangs[i].getLang().equals(langId))
                            return nLangs[i].getStringValue();
                    }
                    ForeignLangType [] fLangs = testItem.getForeignLangArray();
                    for (i = 0; i < fLangs.length; i++)
                    {
                        if (fLangs[i].getLang().equals(langId))
                            return fLangs[i].getStringValue();
                    }
                    return "";
                }
                else
                {
                    LanguageTestPlugin.log(IStatus.WARNING,
                            "Unexpected column index " + columnIndex);
                }
            }
            }
            catch (IllegalArgumentException e)
            {
            	LanguageTestPlugin.log(IStatus.WARNING, 
            			e.getLocalizedMessage(), e);
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void addListener(ILabelProviderListener listener)
        {
        	listeners.add(listener);
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
         */
        public void dispose()
        {
            
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
         */
        public boolean isLabelProperty(Object element, String property)
        {
            
            return true;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void removeListener(ILabelProviderListener listener)
        {
        	listeners.remove(listener);
        }
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableFontProvider#getFont(java.lang.Object, int)
         */
        public Font getFont(Object element, int columnIndex)
        {
            if (columnIndex < NUM_NON_LANG_COL || 
                columnIndex >= NUM_NON_LANG_COL + langIds.size())
            {
                return getSite().getShell().getDisplay().getSystemFont();
            }
            if (parent != null && parent.getDocument() != null)
            {
                int id = columnIndex - NUM_NON_LANG_COL;
                return TestModuleAdapter.getFont(parent.getDocument().getLanguageModule(),
                        langIds.elementAt(id).toString());
            }
            return null;
        }
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#isSaveOnCloseNeeded()
     */
    public boolean isSaveOnCloseNeeded()
    {
        return isDirty();
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    public void dispose()
    {
        IViewPart testView = getEditorSite().getPage().findView(Perspective.TEST_VIEW);
        if (testView != null)
            tableViewer.removeSelectionChangedListener((TestView)testView);
        copyAction.dispose();
        cutAction.dispose();
        pasteAction.dispose();
        super.dispose();
    }
    
    public class TestItemCellModifier implements ICellModifier, 
        ICellEditorValidator
    {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
         */
        public boolean canModify(Object element, String property)
        {
            if (element instanceof TestItemType)
            {
                if (property.equals(CREATION_DATE) || 
                    property.equals(CREATOR_COL) ||
                    property.startsWith("unused"))
                {
                    return false;
                }
                return true;
            }
            return false;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
         */
        public Object getValue(Object element, String property)
        {
            if (element instanceof TestItemType)
            {
                TestItemType testItem = (TestItemType)element;
                if (property.equals(CREATION_DATE))
                {
                    return new Date(testItem.getCreationTime());
                }
                else if (property.equals(CREATOR_COL))
                {
                    return testItem.getCreator();
                }
                else if (property.equals(SOUND_COL))
                {
                	if (testItem.isSetSoundFile())
                		return testItem.getSoundFile().getStringValue();
                	else return new String("");
                }
                else if (property.equals(PICTURE_COL))
                {
                	if (testItem.isSetImg())
                		return testItem.getImg();
                	else return new String("");
                }
                else
                {
                    // set the font
                    int col = NUM_NON_LANG_COL + langIds.indexOf(property);
                    CellEditor editor = tableViewer.getCellEditors()[col];
                    Control control = editor.getControl();
                    if (control != null)
                    {
                        Font font = labelProvider.getFont(element, col);
                        control.setFont(font);
                        final int nextColumn = col + 1;
                        if (nextColumn < tableViewer.getTable().getColumnCount())
                        {
                            KeyListener kl = new CellKeyListener(editor, nextColumn);
                            control.addKeyListener(kl);
                        }
                    }
                    
                    for (int i = 0; i <testItem.sizeOfNativeLangArray(); i++)
                    {
                        NativeLangType lang = testItem.getNativeLangArray(i);
                        if (lang.getLang().equals(property))
                        {
                            return lang.getStringValue();
                        }
                    }
                    for (int i = 0; i <testItem.sizeOfForeignLangArray(); i++)
                    {
                        ForeignLangType lang = testItem.getForeignLangArray(i);
                        if (lang.getLang().equals(property))
                        {
                            return lang.getStringValue();
                        }
                    }
                    return "";
                }
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
         */
        public void modify(Object element, String property, Object value)
        {
        	Object data = element;
            //lastPropertyChanged = property;
        	if (element instanceof TableItem)
        	{
        		TableItem tableItem = (TableItem)element;
        		data = tableItem.getData();
        	}
        	
            if (data instanceof TestItemType)
            {
            	Object oldValue = getValue(data, property);
            	if (oldValue != null)
            	{
            		if (oldValue.equals(value))
            			return;// no change
            	}
            	else if (value == null)
            	{
            		return; // still null
            	}
                TestItemType testItem = (TestItemType)data;
                if (property.equals(CREATION_DATE))
                {
                    
                }
                else if (property.equals(CREATOR_COL))
                {
                    
                }
                else if (property.equals(SOUND_COL))
                {
                    
                    String soundFilePath = getRelativePath(value.toString());
                    
                    if (testItem.isSetSoundFile() &&
                        testItem.getSoundFile().getStringValue().equals(soundFilePath))
                    {
                        return; // unchanged
                    }
                    SoundFileType sft = SoundFileType.Factory.newInstance();
                    sft.setStringValue(soundFilePath);
                    if (soundFilePath == null || soundFilePath.length() == 0)
                    {
                        testItem.setSoundFile(null);
                    }
                    else testItem.setSoundFile(sft);
                }
                else if (property.equals(PICTURE_COL))
                {
                	if (value != null)
                    {
                        String imgPath = getRelativePath(value.toString());
                        if (testItem.isSetImg() && testItem.getImg().equals(imgPath)) return;
                		if (imgPath == null || imgPath.length() == 0) testItem.setImg(null);
                        else testItem.setImg(imgPath); 
                    }
                    else testItem.setImg(null);
                }
                else
                {
                    int langIndex = langIds.indexOf(property);
                    boolean set = false;
                    for (int i = 0; i <testItem.sizeOfNativeLangArray(); i++)
                    {
                        NativeLangType lang = testItem.getNativeLangArray(i);
                        if (lang.getLang().equals(property))
                        {
                            if (lang.getStringValue().equals(value))
                                return;
                            else
                            {
                                lang.setStringValue(value.toString());
                                set = true;
                                break;
                            }
                        }
                    }
                    for (int i = 0; i <testItem.sizeOfForeignLangArray(); i++)
                    {
                        ForeignLangType lang = testItem.getForeignLangArray(i);
                        if (lang.getLang().equals(property))
                        {
                            if (lang.getStringValue().equals(value))
                                return;
                            else
                            {
                                lang.setStringValue(value.toString());
                                set = true;
                                break;
                            }
                        }
                    }
                    if (!set && value != null && value.toString().length() > 0)
                    {
                        if (langIndex < nativeLangCount)
                        {
                            NativeLangType lang = testItem.addNewNativeLang();
                            lang.setLang(property);
                            lang.setStringValue(value.toString());
                            
                        }
                        else
                        {
                            ForeignLangType lang = testItem.addNewForeignLang();
                            lang.setLang(property);
                            lang.setStringValue(value.toString());
                        }
                    }
                }
                // is this a place holder empty node for new entries?
                if (testItem.getDomNode().getParentNode() == null)
                {
                    TestItemType ti = parent.getDocument().getLanguageModule().addNewTestItem();
                    ti.setCreator(testItem.getCreator());
                    ti.setCreationTime(new Date().getTime());
                    ti.setNativeLangArray(testItem.getNativeLangArray());
                    ti.setForeignLangArray(testItem.getForeignLangArray());
                    ti.setImg(testItem.getImg());
                    ti.setSoundFile(testItem.getSoundFile());
                    //ISelection oldSelection = tableViewer.getSelection();
                    tableViewer.refresh();
                    StructuredSelection ss = new StructuredSelection(ti);
                    tableViewer.setSelection(ss, true);
                }
                else
                {
                    tableViewer.update(data, new String[] {property});                    
                }
                parent.setDirty(true);
            }
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
         */
        public String isValid(Object value)
        {
            return null;
        }
        private String getRelativePath(String fullPath)
        {
            File f = new File(fullPath);
            if (!f.exists()) return null;
            IPath path = new Path(fullPath);
            IPath mp = moduleFile.getRawLocation().removeLastSegments(1);
            if (mp.getDevice().endsWith(path.getDevice()) && mp.isPrefixOf(path))
            {
                path = path.removeFirstSegments(mp.segmentCount());
                path = path.setDevice("");
            }
            return path.toPortableString();
        }
    }
    
    private void makeActions()
    {
//      properties
//        copyAction = new Action() {
//            public void run()
//            {
//                TestItemType item = getSelectedItem();
//                if (item != null && lastPropertyChanged != null)
//                {
//                    CellEditor ce = tableViewer.getCellEditors()[NUM_NON_LANG_COL]; 
//                    ce.activate();
//                    ce.performCopy();
//                }
//            }
//        };
//        copyAction.setText(MessageUtil.getString("copy.text"));
//        copyAction.setToolTipText(MessageUtil.getString("copy.tooltip"));
//        copyAction.setEnabled(false);
        
        copyAction = new ClipboardAction(this, ClipboardAction.COPY);
        cutAction = new ClipboardAction(this, ClipboardAction.CUT);
        pasteAction = new ClipboardAction(this, ClipboardAction.PASTE);
        
        insertAction = new Action() {
            public void run()
            {
                insertItem(tableViewer.getTable().getSelectionIndex());
            }
        };
        insertAction.setEnabled(true);
        insertAction.setText(MessageUtil.getString("InsertRow"));
        insertAction.setToolTipText(MessageUtil.getString("InsertRowToolTip"));
    }
    
    public class CellKeyListener implements KeyListener, ICellEditorListener
    {
        private int nextColumn = -1;
        private CellEditor cellEditor = null;
        public CellKeyListener(CellEditor editor, int nextCol) 
        {
            this.nextColumn = nextCol;
            this.cellEditor = editor;

            editor.addListener(this);
            editor.getControl().addKeyListener(this);
        }
        public void keyPressed(KeyEvent e)  
        { 
            if (e.keyCode == '\r')
            {
                tableViewer.editElement(getSelectedItem(), nextColumn);
            }
        }

        public void keyReleased(KeyEvent e)
        {
            if (e.keyCode == SWT.TAB)
            {
                tableViewer.editElement(getSelectedItem(), nextColumn);
            }
        }
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellEditorListener#applyEditorValue()
         */
        public void applyEditorValue()
        {
            removeListeners();
        }
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellEditorListener#cancelEditor()
         */
        public void cancelEditor()
        {
            removeListeners();
        }
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellEditorListener#editorValueChanged(boolean, boolean)
         */
        public void editorValueChanged(boolean oldValidState, boolean newValidState)
        {
            
        }
        private void removeListeners()
        {
            if (cellEditor instanceof TextCellEditor)
            {
                TextCellEditor tce = (TextCellEditor)cellEditor;
                tce.getControl().removeKeyListener(this);
            }
            cellEditor.removeListener(this);
        }
    }
    public class LangMenuListener implements SelectionListener
    {
        private String langCode = null;
        public LangMenuListener(String langCode)
        {
            this.langCode = langCode; 
        }
        /* (non-Javadoc)
         * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
         */
        public void widgetDefaultSelected(SelectionEvent e)
        {
            
        }

        /* (non-Javadoc)
         * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
         */
        public void widgetSelected(SelectionEvent e)
        {
            if (e.widget instanceof MenuItem)
            {
                MenuItem mi = (MenuItem)e.widget;
                MenuItem pi = mi.getParent().getParentItem();
                ClipboardAction ca = null;
                if (pi == cutLangItem)
                {
                    ca = cutAction;
                }
                else if (pi == copyLangItem)
                {
                    ca = copyAction;
                }
                else if (pi == pasteLangItem)
                {
                    ca = pasteAction;
                }
                else return; // don't know what this is
                
                ca.setLangCode(langCode);
                ca.run();
                ca.setLangCode(null);
            }
        }
        
    }
    
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener)
    {
        tableViewer.addSelectionChangedListener(listener);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
     */
    public ISelection getSelection()
    {
        
        return tableViewer.getSelection();
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    public void removeSelectionChangedListener(ISelectionChangedListener listener)
    {
        tableViewer.removeSelectionChangedListener(listener);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
     */
    public void setSelection(ISelection selection)
    {
        tableViewer.refresh();
        tableViewer.getTable().redraw();
        tableViewer.setSelection(selection);
    }
}
