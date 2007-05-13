/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.views;

import java.io.IOException;
import java.util.Vector;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.thanlwinsoft.languagetest.eclipse.natures.LanguageUserNature;
import org.thanlwinsoft.schemas.languagetest.history.ItemType;
import org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryDocument;
import org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType;
import org.thanlwinsoft.schemas.languagetest.history.ResultType;
import org.thanlwinsoft.schemas.languagetest.history.TestType;

/**
 * @author keith
 *
 */
public class TestHistoryProvider implements ITreeContentProvider
{
    private IContainer root = null;
    public TestHistoryProvider()
    {
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement)
    {
        if (parentElement instanceof IContainer)
        {
            try
            {
                IResource [] members = ((IContainer)parentElement).members();
                Vector v = new Vector(members.length);
                for (int i = 0; i < members.length; i++)
                {
                    if (members[i] instanceof IFile)
                    {
                        IFile f = (IFile)members[i];
                        try
                        {
                            ModuleHistoryDocument doc =
                                ModuleHistoryDocument.Factory.parse(f.getContents());
                            ModuleHistoryType mh = doc.getModuleHistory();
                            XmlFamily xf = new XmlFamily(null, mh);
                            xf.file = f;
                            v.add(xf);
                        }
                        catch (XmlException e)
                        {
                            // ignore, its just a normal file
                        }
                        catch (IOException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        catch (CoreException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    // exclude hidden files
                    else if (!members[i].getName().startsWith("."))
                    {
                            v.add(members[i]);
                    }
                }
                return v.toArray();
            }
            catch (CoreException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else if (parentElement instanceof IResource)
        {
            if (parentElement instanceof IFile)
            {
                try
                {
                    ModuleHistoryDocument doc =
                        ModuleHistoryDocument.Factory.parse(((IFile)parentElement).getContents());
                    return doc.getModuleHistory().getItemArray();
                }
                catch (XmlException e)
                {
                    // ignore, its just a normal file
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (CoreException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        }
        else if (parentElement instanceof XmlFamily)
        {
            XmlFamily xf = (XmlFamily)parentElement;
            if (xf.child instanceof ModuleHistoryType)
            {
                ItemType [] items = 
                    ((ModuleHistoryType)xf.child).getItemArray();
                XmlFamily [] families = new XmlFamily[items.length];
                for (int i = 0; i < items.length; i++)
                {
                    families[i] = new XmlFamily(xf, items[i]);
                }
                return families;
            }
            else if (xf.child instanceof ItemType)
            {
                Vector v = new Vector(3);
                ItemType item = (ItemType)xf.child;
                if (item.isSetFL()) v.add(new XmlFamily(xf, item.getFL()));
                if (item.isSetFR()) v.add(new XmlFamily(xf, item.getFR()));
                if (item.isSetNR()) v.add(new XmlFamily(xf, item.getNR()));
                
                return v.toArray();
            }
            else if (xf.child instanceof TestType)
            {
                ResultType [] results = ((TestType)xf.child).getResultArray();
                XmlFamily[] families = new XmlFamily[results.length];
                for (int i = 0; i < results.length; i++)
                {
                    families[i] = new XmlFamily(xf, results[i]);
                }
                return families;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element)
    {
        if (element instanceof IResource)
        {
            return ((IResource)element).getParent();
        }
        if (element instanceof XmlFamily)
        {
            XmlFamily xf = ((XmlFamily)element).parent;
            if (xf == null)
            {
                return xf.file.getParent();
            }
            else return xf.child;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element)
    {
        if (element instanceof IContainer)
        {
            return true;
        }
        else if (element instanceof XmlFamily)
        {
            XmlFamily xf = (XmlFamily)element;
            if (xf.child instanceof ModuleHistoryType)
            {
                ModuleHistoryType thm = (ModuleHistoryType)xf.child;
                return (thm.sizeOfItemArray() > 0);
            }
            else if (xf.child instanceof ItemType)
            {
                ItemType it = (ItemType)xf.child;
                return (it.isSetFL() || it.isSetFR() || it.isSetNR());
            }
            else if (xf.child instanceof TestType)
            {
                TestType tt = (TestType)xf.child;
                return (tt.sizeOfResultArray() > 0);
            }
            else if (xf.child instanceof ResultType)
            {
                //ResultType rt = (ResultType)element;
                return false;
            }
        }
//        else if (element instanceof XmlObject)
//        {
//            XmlObject xo = (XmlObject)element;
//            return xo.getDomNode().hasChildNodes();
//        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement)
    {
        if (inputElement instanceof IWorkspaceRoot)
        {
            IWorkspaceRoot r = (IWorkspaceRoot)inputElement;
            root = r;
            IResource[] projects = null;
            try
            {
                projects = r.members();
            
                Vector v = new Vector(projects.length);
                for (int i = 0; i < projects.length; i++)
                {
                    try
                    {
                        if (projects[i].isAccessible() && 
                            projects[i].getProject().hasNature(LanguageUserNature.ID))
                        {
                            v.add(projects[i]);
                        }
                    }
                    catch (CoreException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return v.toArray();
            }
            catch (CoreException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        else if (inputElement instanceof IContainer)
        {
            IProject p = ((IContainer)inputElement).getProject();
            try
            {
                if (p.hasNature(LanguageUserNature.ID))
                {
                    root = p;
                    return p.members();
                }
            }
            catch (CoreException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
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
        if (newInput instanceof IContainer)
        {
            root = (IContainer) newInput;
        }
    }
    public class XmlFamily
    {
        XmlFamily parent;
        XmlObject child;
        IFile file;
        public XmlFamily(XmlFamily parent, XmlObject child)
        {
            this.parent = parent;
            this.child = child;
            if (parent != null)
                this.file = parent.file;
        }
    }
}
