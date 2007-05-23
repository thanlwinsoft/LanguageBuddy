/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * -----------------------------------------------------------------------
 */
/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.views;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Vector;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.WorkspaceLanguageManager;
import org.thanlwinsoft.languagetest.eclipse.editors.TestItemEditor;
import org.thanlwinsoft.languagetest.eclipse.editors.TestModuleEditor;
import org.thanlwinsoft.languagetest.language.test.meta.MetaNode;
import org.thanlwinsoft.schemas.languagetest.module.ConfigType;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.module.MetaDataType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;

/**
 * @author keith
 *
 */
public class MetaDataView extends ViewPart implements ISelectionChangedListener
{
    private Tree dataTree = null;
    private TreeViewer viewer = null;
    private ITreeContentProvider provider = null;
    private MetaDataLabelProvider labelProvider = null;
    private MetaDataCellModifier cellModifier = null;
    private Display display = null;
    private  CellEditor [] editors = null;;
    private HashSet selectionProviders = null;
    public final static String DEFAULT_LANG_CONFIG = 
        "/org/thanlwinsoft/languagetest/language/text/DefaultLangConfig.xml";
    public final static String ID = 
        "org.thanlwinsoft.languagetest.eclipse.views.MetaDataView";
    private final static String COL = "Tag";
    
    public MetaDataView()
    {
        selectionProviders = new HashSet();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent)
    {
        this.display = parent.getDisplay();
        Group treeComposite = new Group(parent, SWT.SHADOW_ETCHED_IN);
        treeComposite.setLayout(new FillLayout());
        dataTree = new Tree(treeComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
        dataTree.setEnabled(true);
        viewer = new TreeViewer(dataTree);
        TreeColumn tc = new TreeColumn(dataTree, SWT.LEAD);
        tc.setWidth(200);
        tc.setResizable(true);
        tc.setText(MessageUtil.getString("MetaDataColumn"));
        viewer.setColumnProperties(new String[]{COL});
        provider = new MetaDataContentProvider();
        labelProvider = new MetaDataLabelProvider(display);
        cellModifier = new MetaDataCellModifier();
        editors = new CellEditor[]{ new CheckboxCellEditor(dataTree, SWT.LEAD) };
        viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
        viewer.setContentProvider(provider);
        viewer.setLabelProvider(labelProvider);
        viewer.setCellModifier(cellModifier);
        viewer.setCellEditors(editors);
        ConfigType [] config = loadConfig();
        viewer.setInput(config);
        viewer.refresh();
        dataTree.showColumn(tc);
        //dataTree.redraw();
        IEditorPart editor = getSite().getPage().getActiveEditor();
        if (editor instanceof TestModuleEditor)
        {
            TestModuleEditor tme = (TestModuleEditor)editor;
            TestItemEditor tie = (TestItemEditor) tme.getAdapter(TestItemEditor.class);
            addSelectionProvider(tie);
        }
    }
    
    public void addSelectionProvider(ISelectionProvider provider)
    {
        provider.addSelectionChangedListener(this);
        selectionProviders.add(provider);
    }
    
    public void setTestItem(TestItemType testItem)
    {
        cellModifier.setTestItem(testItem);
        labelProvider.setTestItem(testItem);
        viewer.refresh();
        viewer.expandAll();
    }
    
    protected ConfigType [] loadConfig()
    {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject [] projects = workspace.getRoot().getProjects();
        Vector <ConfigType> config = new Vector<ConfigType>(projects.length + 1);
        for (IProject p : projects)
        {
            if (!p.isOpen()) continue;
            ConfigType c = WorkspaceLanguageManager.getProjectLangConfig(p);
            if (c != null)
                config.add(c);
        }
        try
        {
            InputStream is = LanguageTestPlugin.getDefault().getBundle()
                .getResource(DEFAULT_LANG_CONFIG).openStream();
            XmlOptions options = new XmlOptions();
            options.setCharacterEncoding("UTF-8");
            options.setLoadUseDefaultResolver();
            options.setDocumentType(LanguageModuleDocument.type);
            LanguageModuleDocument langDoc = 
                LanguageModuleDocument.Factory.parse(is);
            if (langDoc != null && langDoc.getLanguageModule() != null && 
                langDoc.getLanguageModule().isSetConfig())
            {
                ConfigType c = langDoc.getLanguageModule().getConfig();
                config.add(c);
            }
        }
        catch (IOException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, e.getMessage(), e);
        }
        catch (XmlException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, e.getMessage(), e);
        }
        return config.toArray(new ConfigType[config.size()]);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus()
    {
        
    }

    public class MetaDataCellModifier implements ICellModifier
    {
        TestItemType testItem = null;
        public MetaDataCellModifier()
        {
            
        }
        public void setTestItem(TestItemType ti)
        {
            this.testItem = ti;
        }
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
         */
        public boolean canModify(Object element, String property)
        {
            if (element instanceof MetaNode)
            {
                return !((MetaNode)element).hasChildren();
            }
            return false;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
         */
        public Object getValue(Object element, String property)
        {
            if (element instanceof MetaNode)
            {
                if (testItem == null) 
                    return Boolean.FALSE;
                return ((MetaNode)element).isSetOnItem(testItem);
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
         */
        public void modify(Object element, String property, Object value)
        {
            TreeItem treeItem = null;
            if (element instanceof TreeItem)
            {
                treeItem = (TreeItem)element;
                element = treeItem.getData();
            }
            
            if (element instanceof MetaNode && value instanceof Boolean && 
                testItem != null)
            {
                MetaNode mn = (MetaNode)element;
                boolean state = ((Boolean)value).booleanValue();
                mn.setOnItem(testItem, state);
                IEditorPart editor = getSite().getPage().getActiveEditor();
                if (editor instanceof TestModuleEditor)
                {
                    TestModuleEditor tme = (TestModuleEditor)editor;
                    TestItemEditor tie = (TestItemEditor) tme.getAdapter(TestItemEditor.class);
                    tme.setDirty(true);
                }
                if (treeItem != null)
                    treeItem.setImage(labelProvider.getColumnImage(element, 0));
            }
        }
        
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event)
    {
        if (event.getSelection() instanceof IStructuredSelection)
        {
            IStructuredSelection ss = (IStructuredSelection)event.getSelection();
            if (ss.getFirstElement() instanceof TestItemType)
            {
                TestItemType ti = (TestItemType)ss.getFirstElement();
                if (!dataTree.isDisposed())
                    setTestItem(ti);
            }
        }
    }
    public Display getDisplay()
    {
        return display;
    }
}
