/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;

/**
 * @author keith
 *
 */
public class TestTypePage extends WizardPage
{
    Composite parent = null;
    private Group group = null;
    private Label label = null;
    private Combo combo = null;
    private Label nativeLabel = null;
    private Combo nativeCombo = null;
    private Label foreignLabel = null;
    private Combo foreignCombo = null;
    /**
     * @param pageName
     */
    protected TestTypePage(String pageName)
    {
        super(pageName);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        // TODO Auto-generated method stub
        this.parent = parent;
        createGroup();
        
    }

    /**
     * This method initializes group	
     *
     */
    private void createGroup()
    {
        group = new Group(parent, SWT.NONE);
        label = new Label(group, SWT.NONE);
        label.setText("Label");
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        createCombo();
        nativeLabel = new Label(group, SWT.NONE);
        nativeLabel.setText("Label");
        createNativeCombo();
        foreignLabel = new Label(group, SWT.NONE);
        foreignLabel.setText("Label");
        createForeignCombo();
        
    }

    /**
     * This method initializes combo	
     *
     */
    private void createCombo()
    {
        combo = new Combo(group, SWT.NONE);
    }

    /**
     * This method initializes nativeCombo	
     *
     */
    private void createNativeCombo()
    {
        nativeCombo = new Combo(group, SWT.NONE);
    }

    /**
     * This method initializes foreignCombo	
     *
     */
    private void createForeignCombo()
    {
        foreignCombo = new Combo(group, SWT.NONE);
    }

}
