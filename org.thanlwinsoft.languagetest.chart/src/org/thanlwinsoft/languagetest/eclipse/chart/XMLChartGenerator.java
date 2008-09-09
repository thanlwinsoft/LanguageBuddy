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

/***********************************************************************
 * Copyright (c) 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.batik.css.engine.value.svg.ImageRenderingManager;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.pdf.PDFRendererImpl;
import org.eclipse.birt.chart.device.svg.SVGRendererImpl;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.chart.view.ChartHistoryView;

import com.ibm.icu.util.ULocale;

/**
 * Example class that generates a PDF file based on a BIRT Chart Model.  
 *
 */
public class XMLChartGenerator extends Action implements IWorkbenchWindowActionDelegate, IViewActionDelegate
{
    private IWorkbenchWindow window = null;
    private IViewPart view = null;
    private Shell shell = null;
    private ImageData imageData = null;
    public XMLChartGenerator()
    {
        
        
        
    }
    /**
     * Generates a pdf chart to a file
     */
    public void generateChart(Chart cm, String fileName){
        

        try
        {
            Serializer serializer = null;
            File file = new File(fileName);
            if ( file.exists( ) )
            {
                MessageBox box = new MessageBox( shell,
                        SWT.ICON_WARNING | SWT.YES | SWT.NO );
                box.setText( MessageUtil.getString("SaveXMLSource"));
                box.setMessage( MessageUtil.getString("FileAlreadyExistsReplace", fileName));
                if ( box.open( ) != SWT.YES )
                {
                    return;
                }
                file.delete();
            }

            serializer = SerializerImpl.instance( );
            try
            {
                FileOutputStream fos =new FileOutputStream( file ); 
                serializer.write(cm, fos);
                fos.close();
            }
            catch ( IOException ioe )
            {
                ioe.printStackTrace( );
            }
            
        }
        catch ( Throwable ce )
        {
            ce.printStackTrace( );
        }       
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
     */
    public void dispose()
    {
        // TODO Auto-generated method stub
        
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
                this.shell = chView.getSite().getShell();
                FileDialog dialog = new FileDialog(shell, SWT.SAVE);
                dialog.setText(MessageUtil.getString("ExportChartToXML"));
                dialog.setFileName("TestHistoryChart.chart");
                dialog.setFilterExtensions(new String[] { "*.chart", "*.*"});
                dialog.setFilterNames(new String[] { 
                        MessageUtil.getString("XML Chart"), 
                        MessageUtil.getString("All")});
                String fileName = dialog.open();
                if (fileName != null)
                {
                    generateChart(cm, fileName);
//                    Program p = Program.findProgram("*.svg");
//                    p.execute(fileName);
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
        Program p = Program.findProgram(".xml");
        if (p != null)
            imageData = p.getImageData();
        Image image = new Image(view.getSite().getShell().getDisplay(), imageData);
        JFaceResources.getImageRegistry().put("XML", image);
        this.setImageDescriptor(JFaceResources.getImageRegistry().getDescriptor("XML"));
        
    }   
    
}

