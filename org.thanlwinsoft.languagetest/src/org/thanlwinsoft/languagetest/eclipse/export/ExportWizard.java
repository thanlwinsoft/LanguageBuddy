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

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.editors.TestModuleEditor;
import org.thanlwinsoft.languagetest.eclipse.print.LanguagePairDialog;
import org.thanlwinsoft.languagetest.eclipse.workspace.WorkspaceLanguageManager;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.module.LangType;

/**
 * @author keith
 *
 */
public class ExportWizard extends Wizard implements IExportWizard
{
    private IFile [] files = null;
    private ExportTypePage typePage = null;
    private Shell shell = null;
    private Display display = null;
    private String defaultExtension = null;
    public ExportWizard()
    {
        
    }
    /**
     * @param defaultExtension
     */
    public ExportWizard(String defaultExtension)
    {
        this.defaultExtension = defaultExtension;
    }
    
    protected String getDefaultExtension()
    {
        return defaultExtension;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish()
    {
        final ExportTypePage.ExporterDetails ed = typePage.getSelectedExporter();
        if (ed == null)
            return false;
        Vector<String> parameters = new Vector<String>();
        Vector<String> values = new Vector<String>();
        if (files.length == 0)
        {
            MessageDialog.openWarning(shell, MessageUtil.getString("NoFiles"), 
                    MessageUtil.getString("NoFilesToExportMessage"));
            return true;
        }
        else if (ed.isMultiLingual == false)
        {
        	LangType[] langs = WorkspaceLanguageManager.findUserLanguages();
        	LanguagePairDialog lp = new LanguagePairDialog(shell, langs);
        	if (lp.needDialog())
        	{
        		lp.open();
        	}
        	parameters.add("nativeLang");
        	values.add(lp.getNativeLang().getLang());
        	parameters.add("foreignLang");
        	values.add(lp.getForeignLang().getLang());
        	UniversalLanguage ul = new UniversalLanguage(lp.getNativeLang().getLang());
        	parameters.add("nativeLangDesc");
        	values.add(ul.getDescription());
        	parameters.add("foreignLangDesc");
        	ul = new UniversalLanguage(lp.getForeignLang().getLang());
        	values.add(ul.getDescription());
        }
        if (ed.properties != null)
        {
            // these must be retreived in the display thread
            parameters.addAll(Arrays.asList(ed.properties.getParameters()));
            values.addAll(Arrays.asList(ed.properties.getValues()));
        }
        final String [] theParameters = parameters.toArray(new String[parameters.size()]);
        final String [] theValues = values.toArray(new String[values.size()]);
        
        Job job = new Job("Export")
        {

            protected IStatus run(IProgressMonitor monitor)
            {
                int s = IStatus.ERROR;
                if (convert(ed, theParameters, theValues))
                    s = IStatus.OK;
                return new Status(s, LanguageTestPlugin.ID, 0, "Export", null);
            }
            
        };
        job.setPriority(Job.BUILD);
        job.schedule();
        return true;
    }
    
    private boolean convert(final ExportTypePage.ExporterDetails ed, 
            String [] parameters, String [] values)
    {
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
            LanguageTestPlugin.log(IStatus.WARNING, "Error converting " +
                tce.getMessageAndLocation(), tce);
            return false;
        }
        for (int i = 0; i < files.length; i++)
        {
            if (files[i] == null || files[i].isAccessible() == false)
                continue;
            FileOutputStream os = null;
            InputStream is = null;
            final IFile outputFile = files[i];
            final IPath outputPath = files[i].getRawLocation()
                .removeFileExtension().addFileExtension(ed.extension);
        
            try
            {
                if (outputPath.toFile().exists())
                {
                    final int fIndex = i;
                    Runnable r = new Runnable() {
                        public void run()
                        {
                            Shell shell = 
                                PlatformUI.getWorkbench().getDisplay().getActiveShell();
                            if (MessageDialog.openQuestion(shell, 
                                MessageUtil.getString("FileAlreadyExistsTitle"), 
                                MessageUtil.getString("FileAlreadyExistsReplace", 
                                                       outputPath.toOSString()))
                                                       == false)
                            {
                                files[fIndex] = null;
                            }
                        }
                    };
                    display.syncExec(r);
                    if (files[i] == null)
                        continue;
                }
                if (ed.properties != null &&
                    ed.properties.convert(files[i].getRawLocation().toOSString(), 
                                          outputPath.toOSString(),
                                          parameters, values))
                {
                    continue;
                }
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
                    Runnable r = new Runnable() {
                        public void run()
                        {
                            if (typePage.isAutoOpen())
                            {
                                Program p = Program.findProgram("." + ed.extension);
                                if (p == null)
                                    p = Program.findProgram("*." + ed.extension);
                                if (p == null)
                                    p = Program.findProgram("." + ed.extension.toUpperCase());
                                if (p == null)
                                    p = Program.findProgram("." + ed.extension.toLowerCase());
                                if (p == null)
                                    p = Program.findProgram(ed.extension.toUpperCase());
                                if (p == null)
                                    p = Program.findProgram(ed.extension.toLowerCase());
                                if (p != null)
                                    p.execute(outputPath.toOSString());
                                else Program.launch(outputPath.toOSString());
                            }
                            try
                            {
                                outputFile.getParent().refreshLocal(1, null);
                            }
                            catch (CoreException e)
                            {
                                LanguageTestPlugin.log(IStatus.ERROR, e.getMessage(), e);
                            }
                        }
                    };
                    PlatformUI.getWorkbench().getDisplay().asyncExec(r);
                }
                catch (IOException e)
                {
                    LanguageTestPlugin.log(IStatus.ERROR, e.getMessage(), e);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
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
        Iterator<?> si = selection.iterator();
        HashSet<IFile> set = new HashSet<IFile>();
        files = new IFile[selection.size()];
        while (si.hasNext())
        {
            Object o = si.next();
            if (o instanceof IFile)
            {
                IFile f = (IFile)o;
                if (f.getFileExtension().equals("xml"))
                    set.add(f);
            }
        }
        shell = workbench.getDisplay().getActiveShell();
        display = shell.getDisplay();
        if (set.size() == 0)
        {
            IEditorPart editor = workbench.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            if (editor instanceof TestModuleEditor &&
                editor.getEditorInput() instanceof FileEditorInput)
            {
                set.add(((FileEditorInput)editor.getEditorInput()).getFile());
            }
        }
        files = (IFile[])set.toArray(new IFile[set.size()]);
        if (files.length == 0)
        {
            MessageDialog.openWarning(shell, MessageUtil.getString("NoFiles"), 
                    MessageUtil.getString("NoFilesToExportMessage"));
        }
        
    }
    protected IFile [] getFiles()
    {
        return files;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
     */
    public IWizardPage getNextPage(IWizardPage page)
    {
        if (page instanceof ExportTypePage)
        {
            return ((ExportTypePage)page).getSelectedExporter().page;
        }
        return super.getNextPage(page);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#getPageCount()
     */
    public int getPageCount()
    {
        return super.getPageCount();
    }
}
