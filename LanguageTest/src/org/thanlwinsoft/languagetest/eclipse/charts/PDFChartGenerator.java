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

package org.thanlwinsoft.languagetest.eclipse.charts;

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

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.pdf.PDFRendererImpl;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.views.ChartHistoryView;

import com.ibm.icu.util.ULocale;

/**
 * Example class that generates a PDF file based on a BIRT Chart Model.  
 *
 */
public class PDFChartGenerator  implements IWorkbenchWindowActionDelegate, IViewActionDelegate
{
    private IWorkbenchWindow window = null;
    private IViewPart view = null;
    public PDFChartGenerator()
    {
        
    }
    /**
     * Generates a pdf chart to a file
     */
    public void generateChart(Chart cm, String fileName){
        //Tell chart engine that we are running in stand alone mode.  Note running in an eclipse environment.
        System.setProperty("STANDALONE", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        
        
        //Create the pdf renderer
        IDeviceRenderer idr = new PDFRendererImpl();

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
            idr.setProperty( IDeviceRenderer.FILE_IDENTIFIER, fileName ); //$NON-NLS-1$

            //generate the chart
            gr.render( idr, gcs );
        }
        catch ( ChartException ce )
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
            if (cm != null)
            {
                FileDialog dialog = new FileDialog(chView.getSite().getShell(), SWT.SAVE);
                dialog.setText(MessageUtil.getString("ExportChartToPdf"));
                dialog.setFileName("TestHistoryChart.pdf");
                dialog.setFilterExtensions(new String[] { "*.pdf", "*.*"});
                dialog.setFilterNames(new String[] { 
                        MessageUtil.getString("AcrobatReaderPDF"), 
                        MessageUtil.getString("All")});
                String fileName = dialog.open();
                if (fileName != null)
                {
                    generateChart(cm, fileName);
                    Program p = Program.findProgram("*.pdf");
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

