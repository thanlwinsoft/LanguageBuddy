/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.natures.LanguageModuleNature;

/**
 * @author keith
 *
 */
public class ModuleContentProvider implements ITreeContentProvider
{
    public final static String EXTENSION = NewLangModuleWizardPage.EXTENSION;
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
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement)
    {
        Object [] children = null;
        try
        {
            if (parentElement instanceof IContainer)
            {
                IResource[] members = ((IContainer)parentElement).members();
                Vector foldersModules = new Vector(members.length);
                for (int i = 0; i < members.length; i++)
                {
                    if (members[i] instanceof IContainer)
                    {
                        if (((IContainer)members[i]).members().length > 0)
                        {
                            foldersModules.add(members[i]);
                        }
                    }
                    else if (members[i].getName().endsWith(EXTENSION) &&
                             members[i].getName().startsWith(".") == false)
                    {
                        foldersModules.add(members[i]);
                    }
                }
                children = foldersModules.toArray();
            }
        }
        catch (CoreException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, e.getLocalizedMessage(), e);
        }
        return children;
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
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element)
    {
        if (element instanceof IContainer)
        {
            try
            {
                IContainer c = (IContainer)element; 
                if (c.isAccessible() && c.members().length > 0)
                    return true;
            }
            catch (CoreException e) 
            {
                LanguageTestPlugin.log(IStatus.WARNING, 
                                       e.getLocalizedMessage(), e);
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement)
    {
        if (inputElement instanceof Object []) 
            return (Object[])inputElement;
        if (inputElement instanceof IContainer)
        {
            try
            {
                IResource[] members = ((IContainer)inputElement).members();
                ArrayList l = new ArrayList(members.length);
                for (int i = 0; i < members.length; i++)
                {
                    if (members[i].isAccessible())
                    {
                        if (members[i] instanceof IProject)
                        {
                            IProject p = (IProject)members[i];
                            if (p.hasNature(LanguageModuleNature.ID))
                            {
                                l.add(p);
                            }
                        }
                        else l.add(members[i]);
                    }
                }
                return l.toArray();
            }
            catch (CoreException e) 
            {
                LanguageTestPlugin.log(IStatus.WARNING, 
                                       e.getLocalizedMessage(), e);
            }
        }
        return null;
    }

}
