/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.views;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.natures.LanguageUserNature;

/**
 * @author keith
 *
 */
public class TestHistoryView extends ViewPart
{
    private TreeViewer viewer = null;
    private Tree tree = null;
    private ITreeContentProvider provider = null;
    public final static String ID = "org.thanlwinsoft.languagetest.TestHistoryView";
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
        
        IResourceChangeListener listener = new IResourceChangeListener(){

            public void resourceChanged(IResourceChangeEvent event)
            {
                IResource r = event.getResource();
                if (r.getProject() == null) 
                {
                    viewer.refresh();
                    tree.redraw();
                }
                try
                {
                    if (r.getProject().isAccessible() &&
                        r.getProject().hasNature(LanguageUserNature.ID)) 
                    {
                        viewer.refresh();
                        tree.redraw();
                    }
                }
                catch (CoreException e)
                {
                    LanguageTestPlugin.log(IStatus.WARNING,e.getLocalizedMessage(),e);
                }
            }
            
        };
        ResourcesPlugin.getWorkspace().addResourceChangeListener(
           listener, IResourceChangeEvent.POST_CHANGE);

    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus()
    {
        // TODO Auto-generated method stub

    }

}
