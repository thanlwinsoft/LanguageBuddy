/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;

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
    private Composite parent = null;
    private Group mainGroup = null;  //  @jve:decl-index=0:visual-constraint="10,0"
    private Button singleModuleRadioButton = null;
    private Button revisionRadioButton = null;
    private Button selectModuleRadioButton = null;
    private RowLayout rowLayout = null;
    private ScrolledComposite scrolledComposite = null;
    private Tree tree = null;
    private CheckboxTreeViewer viewer = null;
    private Label maxItemsLabel = null;
    private Combo maxItemsCombo = null;
    private final static int [] MAX_ITEMS = { -1, 10, 25, 50, 100, 200, 500 }; 
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        this.parent = parent;
        mainGroup = new Group(parent, SWT.CENTER);
        mainGroup.setText("Choose Test Modules");
        mainGroup.setSize(new Point(138, 47));
        rowLayout = new RowLayout();
        mainGroup.setLayout(rowLayout);
        singleModuleRadioButton = new Button(mainGroup, SWT.RADIO);
        singleModuleRadioButton.setText(MessageUtil.getString("SingleModuleRadio"));
        singleModuleRadioButton
                .addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
                {
                    public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
                    {
                        if (singleModuleRadioButton.getSelection())
                            tree.setEnabled(true);
                    }
                });
        revisionRadioButton = new Button(mainGroup, SWT.RADIO);
        revisionRadioButton.setText(MessageUtil.getString("RevisionModulesRadio"));
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
                        }
                    }
                });
        createScrolledComposite();
        maxItemsLabel = new Label(mainGroup, SWT.NONE);
        maxItemsLabel.setText(MessageUtil.getString("MaxTestItems"));
        createMaxItemsCombo();
    }
    /**
     * This method initializes scrolledComposite	
     *
     */
    private void createScrolledComposite()
    {
        scrolledComposite = new ScrolledComposite(mainGroup, SWT.NONE);
        tree = new Tree(scrolledComposite, SWT.MULTI);
        tree.setHeaderVisible(true);
        tree.setEnabled(false);
        tree.setLinesVisible(true);
        TreeColumn nameColumn = new TreeColumn(tree, SWT.NONE);
        nameColumn.setWidth(100);
        nameColumn.setResizable(true);
        nameColumn.setText(MessageUtil.getString("NameColumn"));
        TreeColumn itemsColumn = new TreeColumn(tree, SWT.NONE);
        itemsColumn.setWidth(60);
        itemsColumn.setResizable(true);
        itemsColumn.setText(MessageUtil.getString("ItemsColumn"));
        TreeColumn testColumn = new TreeColumn(tree, SWT.NONE);
        testColumn.setWidth(20);
        testColumn.setResizable(true);
        testColumn.setText(MessageUtil.getString("TestColumn"));
        viewer = new CheckboxTreeViewer(tree);
        //viewer.setCellEditors(new CellEditor[] { null, null, 
        //        new CheckboxCellEditor()});
        ModuleContentProvider provider = new ModuleContentProvider();
        viewer.setContentProvider(provider);
        viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
        viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event)
            {
                if (event.getSelection() instanceof IStructuredSelection)
                {
                    if (viewer.getCheckedElements().length > 0)
                    {
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
    }
    /**
     * This method initializes maxItemsCombo	
     *
     */
    private void createMaxItemsCombo()
    {
        maxItemsCombo = new Combo(mainGroup, SWT.NONE);
        maxItemsCombo.setItems(new String [] {
                MessageUtil.getString("UnlimitedItems"),
                MessageUtil.getString(Integer.toString(MAX_ITEMS[1])),
                MessageUtil.getString(Integer.toString(MAX_ITEMS[2])),
                MessageUtil.getString(Integer.toString(MAX_ITEMS[3])),
                MessageUtil.getString(Integer.toString(MAX_ITEMS[4])),
                MessageUtil.getString(Integer.toString(MAX_ITEMS[5])),
                MessageUtil.getString(Integer.toString(MAX_ITEMS[6]))
        }
        );
    }

}
