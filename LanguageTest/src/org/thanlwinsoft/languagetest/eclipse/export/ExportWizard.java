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

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;

/**
 * @author keith
 *
 */
public class ExportWizard extends Wizard implements IExportWizard
{
    private IFile [] files = null;
    private ExportTypePage typePage = null;
    private Shell shell = null;
    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish()
    {
        ExportTypePage.ExporterDetails ed = typePage.getSelectedExporter();
        if (ed == null)
            return false;
//      Create a transform factory instance.
        TransformerFactory tfactory = TransformerFactory.newInstance();
        // Create a transformer from the stylesheets
        Transformer transformer = null;
        try 
        {
            InputStream xsltIs = this.getClass().getResourceAsStream(ed.stylesheet);
            
            transformer = tfactory.newTransformer(new StreamSource(xsltIs));
            if (ed.properties != null)
            {
                String [] parameters = ed.properties.getParameters();
                String [] values = ed.properties.getValues();
                if (parameters != null && parameters.length == values.length)
                {
                    for (int i = 0; i < parameters.length; i++)
                    {
                        transformer.setParameter(parameters[i], values[i]);
                    }
                }
            }
        }
        catch (TransformerConfigurationException tce)
        {
            System.out.println(tce.toString());
            System.out.println(tce.getLocationAsString());
            return false;
        }
        for (int i = 0; i < files.length; i++)
        {
            if (files[i] == null || files[i].isAccessible() == false)
                continue;
            FileOutputStream os = null;
            InputStream is = null;
            try
            {
                IPath outputPath = files[i].getRawLocation()
                    .removeFileExtension().addFileExtension(ed.extension);
                if (ed.properties.convert(files[i].getRawLocation().toOSString(), 
                        outputPath.toOSString()))
                    continue;
                
                is = files[i].getContents();
                
                
                os = new FileOutputStream(outputPath.toFile());
                xslTransform(transformer, is, new BufferedOutputStream(os));
                
            }
            catch (CoreException e)
            {
                LanguageTestPlugin.log(IStatus.ERROR, e.getMessage(), e);
            } 
            catch (FileNotFoundException e)
            {
                MessageDialog.openError(shell, 
                        MessageUtil.getString("ExportErrorTitle"),
                        MessageUtil.getString("ExportErrorMessage", 
                                              e.getLocalizedMessage()));
                LanguageTestPlugin.log(IStatus.ERROR, e.getMessage(), e);
            }
            finally
            {
                try
                {
                    if (is != null) is.close();
                    if (os != null) os.close();
                }
                catch (IOException e)
                {
                    LanguageTestPlugin.log(IStatus.ERROR, e.getMessage(), e);
                }
            }
        }
        
        return true;
    }
    
    public boolean xslTransform(Transformer transformer, InputStream is, OutputStream os)
    {
        boolean success = false;
        try
        {
            StreamSource ss = new StreamSource(is);
            StreamResult sr = new StreamResult(os);
            transformer.transform(ss, sr);
            success = true;
        }
        catch (TransformerException e)
        {
            MessageDialog.openError(shell, 
                    MessageUtil.getString("ExportErrorTitle"),
                    MessageUtil.getString("ExportErrorMessage", 
                                          e.getLocalizedMessage()));
            e.printStackTrace();
            LanguageTestPlugin.log(IStatus.ERROR, e.getMessage(), e);
        }
        return success;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages()
    {
        typePage = new ExportTypePage(MessageUtil.getString("ExporterTypePage"), this);
        addPage(typePage);
        super.addPages();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        Iterator si = selection.iterator();
        int i = 0;
        files = new IFile[selection.size()];
        while (si.hasNext())
        {
            Object o = si.next();
            if (o instanceof IFile)
            {
                files[i] = (IFile)o;
            }
        }
        shell = workbench.getDisplay().getActiveShell();
    }
    protected IFile [] getFiles()
    {
        return files;
    }
}
