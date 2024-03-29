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
package org.thanlwinsoft.languagetest.eclipse.views;

import java.io.IOException;
import java.util.Vector;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.natures.LanguageUserNature;
import org.thanlwinsoft.languagetest.language.test.TestManager;
import org.thanlwinsoft.languagetest.language.test.XmlBeansTestModule;
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
    @SuppressWarnings("unused")
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
                Vector<Object> v = new Vector<Object>(members.length);
                for (int i = 0; i < members.length; i++)
                {
                    if (members[i] instanceof IFile && 
                    	members[i].getFileExtension().equalsIgnoreCase(
                    			XmlBeansTestModule.TEST_MODULE_EXT))
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
                Vector<XmlFamily> v = new Vector<XmlFamily>(3);
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
                //return xf.file.getParent();
            	return null;
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
        	final IPath historyPath = new Path(TestManager.HISTORY_DIR);
            IWorkspaceRoot r = (IWorkspaceRoot)inputElement;
            root = r;
            IResource[] projects = null;
            try
            {
                projects = r.members();
            
                Vector<IFolder> v = new Vector<IFolder>(projects.length);
                for (int i = 0; i < projects.length; i++)
                {
                    try
                    {
                        if (projects[i].isAccessible() && 
                            projects[i].getProject().hasNature(LanguageUserNature.ID))
                        {
                        	IProject p = (IProject)projects[i];
                        	if (p.exists(historyPath))
                        	{
                        		IFolder historyFolder = p.getFolder(historyPath);
                        		if (historyFolder.isAccessible())
                        			v.add(historyFolder);
                        	}
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
                LanguageTestPlugin.log(IStatus.WARNING, 
                		"Error in TestHistoryProvider for " 
                		+ ((p == null)? "" : p.getName()), e);
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
