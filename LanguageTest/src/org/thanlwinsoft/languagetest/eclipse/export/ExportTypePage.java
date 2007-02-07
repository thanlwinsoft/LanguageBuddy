/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
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
    private ExportWizard wizard = null;
    private ExporterDetails current = null;
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
        exporterList = new List(parent, SWT.SINGLE);
        this.setControl(exporterList);
        IExtensionPoint point = 
            Platform.getExtensionRegistry().getExtensionPoint(EXT_POINT);
        IExtension[] extensions = point.getExtensions();
        for (int i = 0; i < extensions.length; i++)
        {
            IConfigurationElement ce[] = extensions[i].getConfigurationElements();
            for (int j = 0; j < ce.length; j++)
            {
                ExporterDetails details = new ExporterDetails();
                details.name = ce[j].getAttribute("name");
                details.stylesheet = ce[j].getAttribute("xslt");
                details.extension = ce[j].getAttribute("extension");
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
            }
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
            }});
    }
    private void setExporter(ExporterDetails d)
    {
        if (current != null)
        {
            if (current.page != null)
            {
                current.page.setVisible(false);
                d.properties.setEnabled(false);
            }
        }
        
        current = d;
        if (d != null)
        {   
            this.setPageComplete(true);
            if (d.page != null)
            {
                d.page.setVisible(true);
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
        return current;
    }
    class ExporterDetails
    {
        String name = "";
        String extension = "";
        String stylesheet = null;
        WizardPage page = null;
        ExporterProperties properties = null;
    }
}
