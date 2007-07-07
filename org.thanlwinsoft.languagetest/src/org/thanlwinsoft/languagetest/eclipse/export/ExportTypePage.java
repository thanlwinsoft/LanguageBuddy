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
package org.thanlwinsoft.languagetest.eclipse.export;

import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;

/**
 * @author keith
 *
 */
public class ExportTypePage extends WizardPage
{
    final static String EXT_POINT = "org.thanlwinsoft.languagetest.xslexport";
    
    private Vector exporters = new Vector();
    private List exporterList = null;
    private Button autoOpen = null;
    private ExportWizard wizard = null;
    private ExporterDetails current = null;
    private boolean isAutoOpen = true;
    /**
     * @param pageName
     */
    public ExportTypePage(String pageName, ExportWizard wizard)
    {
        super(pageName);
        this.wizard = wizard;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        //parent.setLayout(new FillLayout());
        Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
        RowLayout layout = new RowLayout();
        layout.type = SWT.VERTICAL;
        layout.fill = true;
        group.setLayout(layout);
        
        setTitle(MessageUtil.getString("ExportTypeTitle"));
        setDescription(MessageUtil.getString("ExportTypeInstructions"));
        exporterList = new List(group, SWT.SINGLE);
        autoOpen = new Button(group, SWT.CHECK);
        autoOpen.setSelection(true);
        autoOpen.setText(MessageUtil.getString("OpenConvertedFile"));
        autoOpen.setToolTipText(MessageUtil.getString("OpenConvertedFile"));
        
        this.setControl(group);
        IExtensionPoint point = 
            Platform.getExtensionRegistry().getExtensionPoint(EXT_POINT);
        IExtension[] extensions = point.getExtensions();
        int exporterIndex = -1;
        for (int i = 0; i < extensions.length; i++)
        {
            IConfigurationElement ce[] = extensions[i].getConfigurationElements();
            for (int j = 0; j < ce.length; j++)
            {
                ExporterDetails details = new ExporterDetails();
                details.name = ce[j].getAttribute("name");
                details.stylesheet = ce[j].getAttribute("xslt");
                details.extension = ce[j].getAttribute("extension");
                details.isMultiLingual = Boolean.parseBoolean(ce[j].getAttribute("multiLingual"));
                WizardPage page = null;
                try
                {   
                    if (ce[j].getAttribute("wizardPage") != null)
                    {
                        Object o = ce[j].createExecutableExtension("wizardPage");
                        if (o instanceof WizardPage && o instanceof ExporterProperties)
                        {
                            details.page = (WizardPage)o;
                            details.properties = (ExporterProperties)o;
                            details.properties.setEnabled(false);
                            details.properties.setXslt(details.stylesheet);
                            wizard.addPage(details.page);
                        }
                    }
                }
                catch (CoreException e)
                {
                    LanguageTestPlugin.log(IStatus.WARNING, e.getLocalizedMessage(), e);
                }
                exporters.add(details);
                exporterList.add(details.name);
                if (details.extension.equals(wizard.getDefaultExtension()))
                {
                    exporterIndex = exporters.size() - 1;
                    current = details;
                }
            }
            if (exporterIndex > -1) exporterList.select(exporterIndex);
            setExporter(current);
        }
        exporterList.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {}
            public void widgetSelected(SelectionEvent e)
            {
                if (exporterList.getSelectionIndex() > -1)
                {
                    ExporterDetails d = (ExporterDetails)exporters.elementAt(exporterList.getSelectionIndex());
                    setExporter(d);
                }
            }
        });
    }
    private void setExporter(ExporterDetails d)
    {
        if (current != null && current != d)
        {
            if (current.page != null)
            {
                current.page.setVisible(false);
                current.properties.setEnabled(false);
            }
        }
        
        current = d;
        if (d != null)
        {   
            this.setPageComplete(true);
            if (d.page != null)
            {
                d.properties.setEnabled(true);
            }
        }
        else this.setPageComplete(false);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
     */
    public IWizardPage getNextPage()
    {
        if (current != null && current.page != null)
        {
            return current.page;
        }
        return null;
    }
    protected ExporterDetails getSelectedExporter()
    {
        // this is called just before a conversion, so cache auto open status
        // before widgets are disposed
        isAutoOpen = autoOpen.getSelection();
        return current;
    }
    protected boolean isAutoOpen()
    {
        if (!autoOpen.isDisposed())
            isAutoOpen = autoOpen.getSelection();
        return isAutoOpen;
    }
    class ExporterDetails
    {
        String name = "";
        String extension = "";
        String stylesheet = null;
        boolean isMultiLingual = true;
        WizardPage page = null;
        ExporterProperties properties = null;
    }
}
