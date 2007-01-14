/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.views;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

/**
 * @author keith
 *
 */
public class TestHistoryView extends ViewPart
{
    private TreeViewer viewer = null;
    private Tree tree = null;
    private ITreeContentProvider provider = null;
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent)
    {
        Group mainGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
        mainGroup.setLayout(new FillLayout());
        tree = new Tree(mainGroup, SWT.V_SCROLL | SWT.H_SCROLL);
        viewer = new TreeViewer(tree);
        
        provider = new TestHistoryProvider();
        viewer.setContentProvider(provider);
        viewer.setLabelProvider(new TestHistoryLabelProvider(provider));
        viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
        viewer.refresh();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus()
    {
        // TODO Auto-generated method stub

    }

}
