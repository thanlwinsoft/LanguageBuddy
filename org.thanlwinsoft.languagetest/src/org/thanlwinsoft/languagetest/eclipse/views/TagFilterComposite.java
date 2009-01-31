/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
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
/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.views;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.search.core.text.TextSearchScope;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.FileTextSearchScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.editors.TestModuleEditor;
import org.thanlwinsoft.languagetest.eclipse.search.TestItemQuery;
import org.thanlwinsoft.languagetest.eclipse.search.TestItemSearchEngine;
import org.thanlwinsoft.languagetest.eclipse.workspace.MetaDataManager;
import org.thanlwinsoft.languagetest.eclipse.workspace.WorkspaceLanguageManager;
import org.thanlwinsoft.languagetest.language.test.TestItemFilter;
import org.thanlwinsoft.languagetest.language.test.meta.MetaFilter;
import org.thanlwinsoft.languagetest.language.test.meta.MetaNode;
import org.thanlwinsoft.schemas.languagetest.module.ConfigType;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.thanlwinsoft.schemas.languagetest.module.MetaDataType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;
import org.thanlwinsoft.schemas.languagetest.module.TagType;

/**
 * @author keith
 *
 */
public class TagFilterComposite extends ScrolledComposite implements ICheckStateListener
{

    private Tree tree;
    private CheckboxTreeViewer viewer;
    private MetaDataContentProvider contentProvider;
    private MetaDataLabelProvider labelProvider;
    private Object [] rootElements = null;
    private IPath [] checkedPaths = null;
    private TestItemType testItem = null;
    private Menu popup;
    private MenuAction addTagAction;
    private MenuAction deleteTagAction;
    private MenuAction editTagAction;
    private MenuAction searchTagAction;
    /**
     * @param parent
     * @param style
     */
    public TagFilterComposite(Composite parent, int style)
    {
        super(parent, style);
        init();
        createMenu();
    }
    protected void init()
    {
        tree = new Tree(this, SWT.MULTI | SWT.CHECK);
        tree.setHeaderVisible(false);
        tree.setEnabled(true);
        tree.setLinesVisible(true);
        TreeColumn nameColumn = new TreeColumn(tree, SWT.NONE);
        nameColumn.setWidth(400);
        nameColumn.setResizable(true);
        nameColumn.setText(MessageUtil.getString("FilterColumn"));
        tree.showColumn(nameColumn);
        viewer = new CheckboxTreeViewer(tree);
        viewer.setCellEditors(new CellEditor[] { 
                new CheckboxCellEditor()});
        contentProvider = new MetaDataContentProvider();
        labelProvider = new MetaDataLabelProvider(this.getDisplay());
        viewer.setContentProvider(contentProvider);
        viewer.setLabelProvider(labelProvider);
        
        viewer.addCheckStateListener(this);
        refresh();
        setContent(tree);
        setExpandHorizontal(true);
        setExpandVertical(true);
        if (getParent().getLayout() instanceof RowLayout)
        {
            RowData treeRowData = new RowData(SWT.DEFAULT,200);
            setLayoutData(treeRowData);
        }
        else if (getParent().getLayout() instanceof GridLayout)
        {
            GridData treeGridData = new GridData(GridData.FILL);
            treeGridData.minimumHeight = 200;
            treeGridData.heightHint = 200;
            setLayoutData(treeGridData);
        }
        tree.setToolTipText(getTagPathDescription());
    }
    
    protected void refresh()
    {
        ConfigType [] config = MetaDataManager.loadConfig();
        // convert the config array into the top level MetaNodes
        // otherwise we can't compare MetaNodes from TreePaths
        rootElements = contentProvider.getElements(config);
        viewer.setInput(rootElements);
        viewer.refresh();
        viewer.expandAll();
    }
    
    protected void createMenu()
    {
        popup = new Menu(tree);
        addTagAction = new MenuAction(MessageUtil.getString("AddTagAction")) {
            public void run()
            {
                addItem(tree.getSelection());
            }
        };
        MenuItem addTag = new MenuItem(popup, SWT.PUSH);
        addTag.setText(addTagAction.getText());
        addTag.addSelectionListener(addTagAction);
        
        editTagAction = new MenuAction(MessageUtil.getString("EditTagAction")) {
            public void run()
            {
                editItem(tree.getSelection());
            }
        };
        MenuItem editTag = new MenuItem(popup, SWT.PUSH);
        editTag.setText(editTagAction.getText());
        editTag.addSelectionListener(editTagAction);
        
        deleteTagAction = new MenuAction(MessageUtil.getString("DeleteTagAction")) {
            public void run()
            {
                deleteItem(tree.getSelection());
            }
        };
        MenuItem deleteTag = new MenuItem(popup, SWT.PUSH);
        deleteTag.setText(deleteTagAction.getText());
        deleteTag.addSelectionListener(deleteTagAction);
        
        searchTagAction = new MenuAction(MessageUtil.getString("SearchTagAction")) {
            public void run()
            {
                searchForTagItems(tree.getSelection());
            }
        };
        MenuItem searchTag = new MenuItem(popup, SWT.PUSH);
        searchTag.setText(searchTagAction.getText());
        searchTag.addSelectionListener(searchTagAction);
        
        // enable it for right click
        popup.setEnabled(true);
        tree.setMenu(popup);
//        tree.addMouseListener(new MouseListener()
//        {
//            public void mouseDoubleClick(MouseEvent e) { }
//            public void mouseDown(MouseEvent e)
//            {
//                if (e.button == 3)
//                    popup.setVisible(true);
//            }
//            public void mouseUp(MouseEvent e) {}
//        });
    }
    

    /**
     * @param selection
     */
    protected void searchForTagItems(TreeItem[] selection)
    {
        Vector <IPath> tags = new Vector<IPath>(selection.length);
        for (TreeItem item : selection)
        {
            if (item.getData() instanceof MetaNode)
            {
                tags.add(((MetaNode)item.getData()).toPath());
            }
        }
        if (tags.size() == 0) return; // nothing to do

        TestItemFilter [] filters = { 
            new MetaFilter(tags.toArray(new IPath[tags.size()]), 
                           MetaFilter.Mode.ALL)
        };
        LangType [] langCodes = WorkspaceLanguageManager.findUserLanguages();
        HashSet<String> langSet = new HashSet<String>(langCodes.length);
        for (int i = 0; i < langCodes.length; i++)
        {
            langSet.add(langCodes[i].getLang());
        }
        String [] langs = langSet.toArray(new String[langSet.size()]);
        TestItemSearchEngine engine = new TestItemSearchEngine(langSet, filters);
        TextSearchScope scope = 
            FileTextSearchScope.newWorkspaceScope(new String[] {"*.xml"}, false);
        Pattern searchPattern = Pattern.compile("");
        final TestItemQuery query = 
            new TestItemQuery(engine, scope, searchPattern, langs);
        NewSearchUI.runQueryInBackground(query);
        
    }
    /**
     * @param selection
     */
    protected void deleteItem(TreeItem[] selection)
    {
        boolean deleted = false;
        for (TreeItem item : selection)
        {
            if (item.getData() instanceof MetaNode)
            {
                MetaNode mn = (MetaNode)item.getData();
                if (MessageDialog.openQuestion(getShell(), 
                    MessageUtil.getString("DeleteTagTitle"), 
                    MessageUtil.getString("DeleteTagQuestion", mn.getPath())))
                {
                    if (!MetaDataManager.deleteNode(mn))
                    {
                        MessageDialog.openWarning(getShell(), 
                                MessageUtil.getString("DeleteTagTitle"), 
                                MessageUtil.getString("TagDeleteFailed", 
                                                      mn.getPath()));
                    }
                    else deleted = true;
                }
            }
        }
        if (deleted)
        {
            refresh();
        }
    }
    /**
     * @param selection
     */
    protected void editItem(TreeItem[] selection)
    {
        EditTagDialog dialog = new EditTagDialog(this.getShell(), 
                MessageUtil.getString("EditTagTitle"), 
                MessageUtil.getString("EditTagDesc"), false);
        dialog.setSelection(selection);
        if (dialog.open() == 0)
        {
            MetaNode node = dialog.getMetaNode();
            if (MetaDataManager.saveNode(node))
            {
                refresh();
            }
            else
            {
                MessageDialog.openWarning(getShell(), 
                    MessageUtil.getString("EditTagTitle"), 
                    MessageUtil.getString("TagEditFailed", node.getPath()));
            }
        }
    }
    /**
     * @param selection
     */
    protected void addItem(TreeItem[] selection)
    {
        EditTagDialog dialog = new EditTagDialog(this.getShell(), 
                MessageUtil.getString("AddTagTitle"), 
                MessageUtil.getString("AddTagDesc"), true);
        dialog.setSelection(selection);
        if (dialog.open() == 0)
        {
            MetaNode node = dialog.getMetaNode();
            if (MetaDataManager.saveNode(node))
            {
                refresh();
            }
            else
            {
                MessageDialog.openWarning(getShell(), 
                    MessageUtil.getString("AddTagTitle"), 
                    MessageUtil.getString("TagCreateFailed", node.getPath()));
            }
        }
    }
    
    public MetaNode [] getCheckedNodes()
    {
        Object [] elements = viewer.getCheckedElements();
        MetaNode [] nodes = new MetaNode[elements.length];
        
        for (int i = 0; i < elements.length; i++)
        {
            if (elements[i] instanceof MetaNode)
            {
                nodes[i] = (MetaNode)elements[i];
            }
        }
        return nodes;
    }
    
    public IPath [] getCheckedTagPaths()
    {
        if (tree.isDisposed())
            return checkedPaths;
        Object [] elements = viewer.getCheckedElements();
        List <IPath>paths = new Vector<IPath>(elements.length);
        for (int i = 0; i < elements.length; i++)
        {
            if (elements[i] instanceof MetaNode)
            {
            	MetaNode mn = (MetaNode)elements[i];
            	if (!mn.hasChildren())
            		paths.add(mn.toPath());
            }
        }
        return paths.toArray(new IPath[paths.size()]);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ICheckStateListener#checkStateChanged(org.eclipse.jface.viewers.CheckStateChangedEvent)
     */
    public void checkStateChanged(CheckStateChangedEvent event)
    {
        if (event.getElement() instanceof MetaNode)
        {
            MetaNode mn = (MetaNode)event.getElement();
            if (mn.hasChildren())
            {
                // set all the children to this state
                //setChildrenState(mn, event.getChecked());
                IPath path = mn.toPath();
                Object [] segments = new Object[path.segmentCount()];
                segments[0] = rootElements;
                MetaNode n = mn;
                for (int i = path.segmentCount() - 1; i >= 0 ; i--)
                {
                    segments[i] = n;
                    n = mn.getParent();
                }
                TreePath tp = new TreePath(segments);
                viewer.expandToLevel(tp, CheckboxTreeViewer.ALL_LEVELS);
                viewer.setSubtreeChecked(tp, event.getChecked());
                // normally branches will not have been checked, but it may
                // have once been a leaf, so force unchecking
                if (event.getChecked() == false)
                    mn.setOnItem(testItem, event.getChecked());
                if (testItem != null)
                    setChildrenState(mn, event.getChecked());
            }
            else
            {
                // only set the test item on leaves
                if (testItem != null)
                    mn.setOnItem(testItem, event.getChecked());
                
            }
            if (testItem != null)
            {
                IEditorPart editor = PlatformUI.getWorkbench()
                    .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                if (editor instanceof TestModuleEditor)
                {
                    TestModuleEditor tme = (TestModuleEditor)editor;
                    tme.setDirty(true);
                }
            }
        }
        checkedPaths = getCheckedTagPaths();
        tree.setToolTipText(getTagPathDescription());
    }
    
    public String getTagPathDescription()
    {
        StringBuilder sb = new StringBuilder();
        if (checkedPaths == null || checkedPaths.length == 0)
        {
            sb.append(MessageUtil.getString("NoTagsSelectedTooltip"));
        }
        else
        {
            if (checkedPaths.length == 1)
                sb.append(MessageUtil.getString("OneTagSelected"));
            else
                sb.append(MessageUtil.getString("TagsSelected",
                        Integer.toString(checkedPaths.length)));
            final String eol = System.getProperty("line.separator"); 
            for (IPath p : checkedPaths)
            {
                if (sb.length() > 0)
                    sb.append(eol);
                sb.append(p);
            }
        }
        return sb.toString();
    }
    
    /** set children of a branch node to the same state */
    protected void setChildrenState(MetaNode mn, boolean state)
    {
        for (MetaNode child : mn.getChildren())
        {
            if (child.hasChildren())
            {
                setChildrenState(child, state);
            }
            else
            {
                // only set the test item on leaves
                child.setOnItem(testItem, state);
            }
        }
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        tree.setEnabled(enabled);
        super.setEnabled(enabled);
    }
    /**
     * Create a TreePath made up of MetaNode segments. 
     * Only the ID is important, the descriptions can be left out.
     */
    static TreePath pathToTreePath(IPath p)
    {
        Object [] segments = new Object[p.segmentCount()];
        MetaDataType metaData = null;
        MetaNode mn = null;
        for (int i = 0; i < p.segmentCount(); i++)
        {
            metaData = MetaDataType.Factory.newInstance();
            metaData.setMetaId(p.segment(i));
            mn = new MetaNode(mn, metaData);
            segments[i] = mn;
        }
        return new TreePath(segments);
    }
    
    public void setTestItem(TestItemType ti)
    {
        this.testItem = null;
        labelProvider.setTestItem(ti);
        viewer.setSubtreeChecked(viewer.getTree().getTopItem(), false);
        if (ti != null && ti.sizeOfTagArray() > 0)
        {
            for (TagType t : ti.getTagArray())
            {
                TreePath element = pathToTreePath(new Path(t.getRef()));
                viewer.setChecked(element, true);
            }
        }
        this.testItem = ti;
        checkedPaths = getCheckedTagPaths();
        tree.setToolTipText(getTagPathDescription());
    }
    
    public void setSelectedTags(IPath [] tags)
    {
    	for (IPath t : tags)
        {
            TreePath element = pathToTreePath(t);
            if (!viewer.setChecked(element, true))
            	LanguageTestPlugin.log(IStatus.WARNING, "Tag " + t + " may not exist anymore.");
        }
    	checkedPaths = tags;
    }
    
    class MenuAction extends Action implements SelectionListener 
    {
        public MenuAction(String text)
        {
            super(text);
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
            run();
        }
        
    }
}
