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

package org.thanlwinsoft.languagetest.eclipse.chart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.fop.svg.PDFTranscoder;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.svg.SVGRendererImpl;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.osgi.framework.Bundle;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.chart.view.ChartHistoryView;

import com.ibm.icu.util.ULocale;

/**
 * Example class that generates a PDF file based on a BIRT Chart Model.  
 *
 */
public class SVGChartGenerator  implements IWorkbenchWindowActionDelegate, IViewActionDelegate
{
    private String defaultExtension = "svg";
    private IWorkbenchWindow window = null;
    private IViewPart view = null;
    private String [] filterExtensions = new String[] { 
            "*.png", "*.svg", "*.jpg", "*.pdf","*.*"};
    private String[] filterNames = new String[] {
            MessageUtil.getString("File_SVG"), 
            MessageUtil.getString("File_PNG"),
            MessageUtil.getString("File_JPEG"),
            MessageUtil.getString("File_PDF"),
            MessageUtil.getString("File_All")};
    public SVGChartGenerator()
    {
        
    }
    protected SVGChartGenerator(String ext, String name)
    {
        this.defaultExtension = ext;
        this.filterExtensions = new String [] { ext, "*.*" };
        this.filterNames = new String [] { 
                name, MessageUtil.getString("File_All")};
    }
    /**
     * Generates a chart to an svg file.
     * The SVG is then converted to the another format if requested.
     */
    public void generateChart(Chart cm, String fileName){
        //Tell chart engine that we are running in stand alone mode.  Note running in an eclipse environment.
        System.setProperty("STANDALONE", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        
        String svnFileName = fileName;
        IPath path = new Path(fileName);
        if (!"svg".equals(path.getFileExtension()))
        {
            svnFileName = path.removeFileExtension().toOSString();
            if (!svnFileName.endsWith("."))
                svnFileName += ".";
            svnFileName += "svg";
        }
        //Create the pdf renderer
        IDeviceRenderer idr = new SVGRendererImpl();
        

        try
        {
            RunTimeContext rtc = new RunTimeContext( );
            rtc.setULocale( ULocale.getDefault( ) );
            
            final Generator gr = Generator.instance( );
            GeneratedChartState gcs = null;
            //Set the chart size
            Bounds bo = BoundsImpl.create( 0, 0, 450, 300 );
            gcs = gr.build( idr.getDisplayServer( ), cm, bo, null, rtc, null );

            //Specify the file to write to. 
            idr.setProperty( IDeviceRenderer.FILE_IDENTIFIER, svnFileName ); //$NON-NLS-1$

            //generate the chart
            gr.render( idr, gcs );
            if (fileName.endsWith(".jpg"))
                exportSvgToJPEG(svnFileName, fileName);
            else if (fileName.endsWith(".png"))
                exportSvgToPNG(svnFileName, fileName);
            else if (fileName.endsWith(".pdf"))
                exportSvgToPDF(svnFileName, fileName);
        }
        catch ( ChartException ce )
        {
            ce.printStackTrace( );
        }       
    }
    
    private void exportSvgToPNG(String svnFileName, String fileName)
    {
        try
        {
            //Bundle batik = Platform.getBundle("org.apache.batik");
            Bundle xerces = Platform.getBundle("org.apache.xerces");
            xerces.loadClass("org.apache.xerces.parsers.SAXParser");
            
            // Create a PNG transcoder
            PNGTranscoder t = new PNGTranscoder();
            
            // Create the transcoder input.
            String svgURI = new File(svnFileName).toURI().toString();
            TranscoderInput input = new TranscoderInput(svgURI);
    
            // Create the transcoder output.
            OutputStream ostream;
            try
            {
                ostream = new FileOutputStream(fileName);
                TranscoderOutput output = new TranscoderOutput(ostream);
    
                // Save the image.
                t.transcode(input, output);
    
                // Flush and close the stream.
                ostream.flush();
                ostream.close();
            } 
            catch (FileNotFoundException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
            } 
            catch (IOException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
            } 
            catch (TranscoderException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
            }
        
        } 
        catch (ClassNotFoundException e1)
        {
            LanguageTestPlugin.log(IStatus.ERROR, "No SaxParser", e1);
        }
    }
    
    private void exportSvgToPDF(String svnFileName, String fileName)
    {
        try
        {
            Bundle batikBundle = Platform.getBundle("org.apache.fop");
            batikBundle.loadClass("org.apache.fop.svg.PDFTranscoder");
            batikBundle.loadClass("org.apache.batik.transcoder.TranscoderInput");
            batikBundle.loadClass("org.apache.batik.transcoder.TranscoderOutput");
            // Create a PNG transcoder
            PDFTranscoder t = new PDFTranscoder();// throws an exception
            
            // Create the transcoder input.
            String svgURI = new File(svnFileName).toURI().toString();
            
            TranscoderInput input = new TranscoderInput(svgURI);
            
            // Create the transcoder output.
            OutputStream ostream;
        
            ostream = new FileOutputStream(fileName);
            TranscoderOutput output = new TranscoderOutput(ostream);
            // preload class
            batikBundle.loadClass("org.apache.batik.transcoder.TranscoderInput");
            // Save the image.
            t.transcode(input, output);

            // Flush and close the stream.
            ostream.flush();
            ostream.close();
        } 
        catch (FileNotFoundException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        } 
        catch (IOException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        } 
        catch (TranscoderException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        }
        catch (ClassNotFoundException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        }
        catch (Throwable e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        }
        
    }
    
    
    private void exportSvgToJPEG(String svnFileName, String fileName)
    {
//      Create a JPEG transcoder
        JPEGTranscoder t = new JPEGTranscoder();

        // Set the transcoding hints.
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
                             new Float(.8));

        // Create the transcoder input.
        String svgURI = new File(svnFileName).toURI().toString();
        TranscoderInput input = new TranscoderInput(svgURI);

        // Create the transcoder output.
        OutputStream ostream;
        try
        {
            ostream = new FileOutputStream(fileName);
            TranscoderOutput output = new TranscoderOutput(ostream);

            // Save the image.
            t.transcode(input, output);

            // Flush and close the stream.
            ostream.flush();
            ostream.close();
        } 
        catch (FileNotFoundException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        } 
        catch (IOException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        } 
        catch (TranscoderException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        }
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
     */
    public void dispose()
    {
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
     */
    public void init(IWorkbenchWindow window)
    {
        this.window = window;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action)
    {
        //org.eclipse.birt.chart.examples.api.pdf.PDFChartGenerator.main();
        
        ChartHistoryView chView = null;
        if (window != null)
        {
            view = window.getActivePage().findView(ChartHistoryView.ID);
        }
        if (view instanceof ChartHistoryView)
        {
            chView = (ChartHistoryView)view;
            
        }
        
        if (chView != null)
        {
            Chart cm = chView.getChart();
            //if (cm == null)
            //    cm = PDFChartGenerator.createHSChart();
            if (cm != null)
            {
                FileDialog dialog = new FileDialog(chView.getSite().getShell(), SWT.SAVE);
                dialog.setText(MessageUtil.getString("ExportChartToSVG"));
                dialog.setFileName("TestHistoryChart." + defaultExtension);
                dialog.setFilterExtensions(filterExtensions);
                dialog.setFilterNames(filterNames);

                String fileName = dialog.open();
                if (fileName != null)
                {
                    IPath path = new Path(fileName);
                    if (path.toFile().exists() &&
                        MessageDialog.openQuestion(view.getSite().getShell(), 
                            MessageUtil.getString("FileAlreadyExistsTitle"), 
                            MessageUtil.getString("FileAlreadyExistsReplace",
                                                  path.toOSString())) == false)
                    {
                        return;
                    }
                    generateChart(cm, fileName);
                    //Control [] dialogKids = dialog.getChildren();
                    String extension = path.getFileExtension();
                    Program p = Program.findProgram("." + extension);
                    if (p != null)
                        p.execute(fileName);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
     */
    public void init(IViewPart view)
    {
        this.view = view;
    }   
    
}

