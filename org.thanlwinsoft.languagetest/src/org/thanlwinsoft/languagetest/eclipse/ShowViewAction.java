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
package org.thanlwinsoft.languagetest.eclipse;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceAction;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.thanlwinsoft.languagetest.MessageUtil;

/**
 * @author keith
 *
 */
public class ShowViewAction implements IWorkbenchAction
{
    private String viewId = null;
    private String label = "";
    private boolean enabled = true;
    private String perspectiveId = null;
    public ShowViewAction(String viewId, String label)
    {
        this.viewId = viewId;
        this.label = label;
    }
    public ShowViewAction(String perspectiveId, String viewId, String label)
    {
        this.viewId = viewId;
        this.label = label;
        this.perspectiveId = perspectiveId;
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
     */
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
     */
    public void addPropertyChangeListener(IPropertyChangeListener listener)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#getAccelerator()
     */
    public int getAccelerator()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#getActionDefinitionId()
     */
    public String getActionDefinitionId()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#getDescription()
     */
    public String getDescription()
    {
        return MessageUtil.getString("ShowView");
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#getDisabledImageDescriptor()
     */
    public ImageDescriptor getDisabledImageDescriptor()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#getHelpListener()
     */
    public HelpListener getHelpListener()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#getHoverImageDescriptor()
     */
    public ImageDescriptor getHoverImageDescriptor()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#getId()
     */
    public String getId()
    {
        return viewId;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#getMenuCreator()
     */
    public IMenuCreator getMenuCreator()
    {
        
        return new IMenuCreator() {
            private Menu menu = null;
            private MenuItem item = null;
            public void dispose()
            {
                if (item != null) item.dispose();
                item = null;
            }

            public Menu getMenu(Control parent)
            {
                if (menu == null || item == null)
                {
                    menu = new Menu(parent);
                    item = new MenuItem(menu, SWT.PUSH);
                    item.setText(getText());
                }
                return menu;
            }

            public Menu getMenu(Menu parent)
            {
                if (menu == null || item == null)
                {
                    item = new MenuItem(parent, SWT.PUSH);
                    item.setText(getText());
                }
                return menu;
            }};
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#getStyle()
     */
    public int getStyle()
    {
        
        return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#getText()
     */
    public String getText()
    {
        
        return label;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#getToolTipText()
     */
    public String getToolTipText()
    {
        return label;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#isChecked()
     */
    public boolean isChecked()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#isEnabled()
     */
    public boolean isEnabled()
    {
        return enabled;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#isHandled()
     */
    public boolean isHandled()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
     */
    public void removePropertyChangeListener(IPropertyChangeListener listener)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run()
    {
        IWorkbenchPage page = 
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try
        {
            if (perspectiveId != null && 
                page.getPerspective().getId().equals(perspectiveId) == false)
            {
                IPerspectiveDescriptor pd = 
                    PlatformUI.getWorkbench().getPerspectiveRegistry()
                    .findPerspectiveWithId(perspectiveId);
                if (pd != null)
                    page.setPerspective(pd);
            }
            page.showView(viewId);
        }
        catch (PartInitException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#runWithEvent(org.eclipse.swt.widgets.Event)
     */
    public void runWithEvent(Event event)
    {
        run();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setAccelerator(int)
     */
    public void setAccelerator(int keycode)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setActionDefinitionId(java.lang.String)
     */
    public void setActionDefinitionId(String id)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setChecked(boolean)
     */
    public void setChecked(boolean checked)
    {
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setDescription(java.lang.String)
     */
    public void setDescription(String text)
    {
        this.label = text;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setDisabledImageDescriptor(org.eclipse.jface.resource.ImageDescriptor)
     */
    public void setDisabledImageDescriptor(ImageDescriptor newImage)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setHelpListener(org.eclipse.swt.events.HelpListener)
     */
    public void setHelpListener(HelpListener listener)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setHoverImageDescriptor(org.eclipse.jface.resource.ImageDescriptor)
     */
    public void setHoverImageDescriptor(ImageDescriptor newImage)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setId(java.lang.String)
     */
    public void setId(String id)
    {
        this.viewId = id;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setImageDescriptor(org.eclipse.jface.resource.ImageDescriptor)
     */
    public void setImageDescriptor(ImageDescriptor newImage)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setMenuCreator(org.eclipse.jface.action.IMenuCreator)
     */
    public void setMenuCreator(IMenuCreator creator)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setText(java.lang.String)
     */
    public void setText(String text)
    {
        this.label = text;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#setToolTipText(java.lang.String)
     */
    public void setToolTipText(String text)
    {
           
    }


}
