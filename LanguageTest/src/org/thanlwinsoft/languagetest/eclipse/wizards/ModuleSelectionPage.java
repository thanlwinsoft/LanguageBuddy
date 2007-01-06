/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * @author keith
 *
 */
public class ModuleSelectionPage extends WizardPage
{
    /**
     * @param pageName
     */
    public ModuleSelectionPage(String pageName)
    {
        super(pageName);
    }
    private Group mainGroup = null;  //  @jve:decl-index=0:visual-constraint="10,0"
    private Button singleModuleRadioButton = null;
    private Button revisionRadioButton = null;
    private Button selectModuleRadioButton = null;
    private RowLayout rowLayout = null;
    private ScrolledComposite scrolledComposite = null;
    private Tree tree = null;
    private RowData treeRowData = null;
    private CheckboxTreeViewer viewer = null;
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        this.setDescription(MessageUtil.getString("SelectTestItems"));
        this.setErrorMessage(MessageUtil.getString("NoTestItemsSelection"));
        mainGroup = new Group(parent, SWT.CENTER);
        mainGroup.setText("Choose Test Modules");
        mainGroup.setSize(new Point(138, 47));
        rowLayout = new RowLayout();
        rowLayout.type = SWT.VERTICAL;
        rowLayout.fill = true;
        mainGroup.setLayout(rowLayout);
        revisionRadioButton = new Button(mainGroup, SWT.RADIO);
        revisionRadioButton.setText(MessageUtil.getString("RevisionModulesRadio"));
        revisionRadioButton
                .addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
                {
                    public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
                    {
                        setPageComplete(revisionRadioButton.getSelection());
                        if (revisionRadioButton.getSelection())
                            setErrorMessage(null);
                    }
                });
        
        selectModuleRadioButton = new Button(mainGroup, SWT.RADIO);
        selectModuleRadioButton.setText(MessageUtil.getString("SelectModuleRadio"));
        selectModuleRadioButton
                .addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
                {
                    public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
                    {
                        if (selectModuleRadioButton.getSelection())
                        {
                            tree.setEnabled(true);
                            if (tree.getSelectionCount() > 0)
                            {
                                setPageComplete(true);
                                setErrorMessage(null);
                            }
                            else
                            {
                                setErrorMessage(MessageUtil.getString("SelectModulesMessage"));
                                setPageComplete(false);
                            }
                        }
                    }
                });
        singleModuleRadioButton = new Button(mainGroup, SWT.RADIO);
        singleModuleRadioButton.setText(MessageUtil.getString("SingleModuleRadio"));
        singleModuleRadioButton
                .addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
                {
                    public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
                    {
                        if (singleModuleRadioButton.getSelection())
                        {
                            tree.setEnabled(true);
                        }
                    }
                });
        singleModuleRadioButton.setVisible(false);
        createScrolledComposite();
        mainGroup.addControlListener(new org.eclipse.swt.events.ControlListener()
        {
            private boolean inProgress = false; 
            public void controlResized(org.eclipse.swt.events.ControlEvent e)
            {
                if (inProgress) return;
                inProgress = true;
                Rectangle r = mainGroup.getBounds();
                if (treeRowData != null && r.width > 50 && r.height > 100)
                {
                    treeRowData.width = r.width - 50;//Math.max(treeRowData.width, r.width - 20);
                    treeRowData.height = r.height - 120;//Math.max(treeRowData.height, r.width - 100);
                    scrolledComposite.setLayoutData(treeRowData);
                }
                mainGroup.pack();
                inProgress = false;
            }
            public void controlMoved(org.eclipse.swt.events.ControlEvent e)
            {
            }
        });
        mainGroup.pack();
        viewer.expandAll();
        setPageComplete(false);
        setControl(mainGroup);
    }
    /**
     * This method initializes scrolledComposite	
     *
     */
    private void createScrolledComposite()
    {
        scrolledComposite = new ScrolledComposite(mainGroup, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        
        tree = new Tree(scrolledComposite, SWT.MULTI | SWT.CHECK);
        tree.setHeaderVisible(false);
        tree.setEnabled(false);
        tree.setLinesVisible(true);
        TreeColumn nameColumn = new TreeColumn(tree, SWT.NONE);
        nameColumn.setWidth(300);
        nameColumn.setResizable(true);
        nameColumn.setText(MessageUtil.getString("NameColumn"));
        /*
        TreeColumn itemsColumn = new TreeColumn(tree, SWT.NONE);
        itemsColumn.setWidth(60);
        itemsColumn.setResizable(true);
        itemsColumn.setText(MessageUtil.getString("ItemsColumn"));
        TreeColumn testColumn = new TreeColumn(tree, SWT.NONE);
        testColumn.setWidth(20);
        testColumn.setResizable(true);
        testColumn.setText(MessageUtil.getString("TestColumn"));
        */
        viewer = new CheckboxTreeViewer(tree);
        viewer.setCellEditors(new CellEditor[] { 
                new CheckboxCellEditor()});
        
        ModuleContentProvider provider = new ModuleContentProvider();
        viewer.setContentProvider(provider);
        viewer.setLabelProvider(new LabelProvider() {

            /* (non-Javadoc)
             * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
             */
            public String getText(Object element)
            {
                if (element instanceof IResource)
                {
                    return ((IResource)element).getName();
                }
                return super.getText(element);
            }
            
        });
        viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
        viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event)
            {
                if (event.getSelection() instanceof IStructuredSelection)
                {
                    if (viewer.getCheckedElements().length > 0)
                    {
                        setErrorMessage(null);
                        setPageComplete(true);
                    }
                    else
                    {
                        setPageComplete(false);
                    }
                }
            }
        });

        scrolledComposite.setContent(tree);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        treeRowData = new RowData(SWT.DEFAULT,200);
        scrolledComposite.setLayoutData(treeRowData);
        //scrolledComposite.setMinSize(200, 200);
        //scrolledComposite.setMinSize(tree.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }
    
    public boolean isRevisionTest()
    {
        return revisionRadioButton.getSelection();
    }
    public Object [] getSelectedModules()
    {
        TreeItem [] treeItems = tree.getSelection();
        Vector v = new Vector(treeItems.length);
        for (int i = 0; i < treeItems.length; i++)
        {
            if (treeItems[i].getData() instanceof IFile)
            {
                v.add(treeItems[i].getData());
            }
        }    
        return v.toArray();
    }
    //public class ModuleSelectionModifier extends 
}
