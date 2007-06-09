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

package org.thanlwinsoft.languagetest.eclipse.charts.old;

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.pdf.PDFRendererImpl;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
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
public class PDFChartGenerator  /*implements IWorkbenchWindowActionDelegate, IViewActionDelegate*/
{
    private IWorkbenchWindow window = null;
    private IViewPart view = null;
    public PDFChartGenerator()
    {
        
    }
    /**
     * Generates a pdf chart to a file
     */
    public static void generateChart(Chart cm, String fileName){
        //Tell chart engine that we are running in stand alone mode.  Note running in an eclipse environment.
        //System.setProperty("STANDALONE", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        
        // preload problematic class
        org.apache.batik.transcoder.SVGAbstractTranscoder transcoder = null;
        javax.xml.parsers.DocumentBuilderFactory dbf = null;
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

//    /* (non-Javadoc)
//     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
//     */
//    public void init(IWorkbenchWindow window)
//    {
//        this.window = window;
//    }

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
            if (cm == null)
                cm = createHSChart();
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
//    /* (non-Javadoc)
//     * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
//     */
//    public void init(IViewPart view)
//    {
//        this.view = view;
//    }   
 
    /**
     * Creates a simple horizontal stack bar chart model.
     * @return chart model
     */
    protected static final Chart createHSChart( )
    {
        ChartWithAxes cwaBar = ChartWithAxesImpl.create( );
        cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );

        Plot p = cwaBar.getPlot( );
        p.getClientArea( ).setBackground( ColorDefinitionImpl.create( 255,
                255,
                225 ) );
        cwaBar.getTitle( )
                .getLabel( )
                .getCaption( )
                .setValue( "Simple Bar Chart" ); //$NON-NLS-1$
        cwaBar.setUnitSpacing( 20 );

        Legend lg = cwaBar.getLegend( );
        LineAttributes lia = lg.getOutline( );
        lg.getText( ).getFont( ).setSize( 16 );
        lia.setStyle( LineStyle.SOLID_LITERAL );
        lg.getInsets( ).set( 10, 5, 0, 0 );
        lg.getOutline( ).setVisible( false );
        lg.setAnchor( Anchor.NORTH_LITERAL );
        lg.setItemType( LegendItemType.CATEGORIES_LITERAL );

        // X-Axis
        Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];

        xAxisPrimary.setType( AxisType.TEXT_LITERAL );
        xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
        xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
        xAxisPrimary.getTitle( ).setVisible( true );

        // Y-Axis
        Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );
        yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
        yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
        yAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 90 );

        // Data Set
        TextDataSet categoryValues = TextDataSetImpl.create( new String[]{
                "Item 1", "Item 2", "Item 3"} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        NumberDataSet orthoValues = NumberDataSetImpl.create( new double[]{
                25, 35, 15
        } );

        // X-Series
        Series seCategory = SeriesImpl.create( );
        seCategory.setDataSet( categoryValues );

        SeriesDefinition sdX = SeriesDefinitionImpl.create( );
        sdX.getSeriesPalette( ).update( 0 );
        xAxisPrimary.getSeriesDefinitions( ).add( sdX );
        sdX.getSeries( ).add( seCategory );

        // Y-Series
        BarSeries bs = (BarSeries) BarSeriesImpl.create( );
        bs.setDataSet( orthoValues );
        bs.setRiserOutline( null );
        bs.setSeriesIdentifier( "Highlight" ); //$NON-NLS-1$
        bs.getLabel( ).setVisible( true );
        bs.setLabelPosition( Position.INSIDE_LITERAL );
        SeriesDefinition sdY = SeriesDefinitionImpl.create( );
        yAxisPrimary.getSeriesDefinitions( ).add( sdY );
        sdY.getSeries( ).add( bs );

        return cwaBar;
    }

    public static void main(String [] arg)
    {
        Chart cm = null;
        String filename = "test.pdf";
        if (arg.length > 0)
        {
            Serializer serializer = null;
            File file = new File(arg[0]);
            if (file.exists())
            {
                filename = arg[0] + ".pdf";
                try
                {
                    FileInputStream fis = new FileInputStream(file);
                    serializer = SerializerImpl.instance( );
                    cm = serializer.read(fis);
                    fis.close();
                } 
                catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
            else
            {
                System.out.println(arg[0] + " doesn't exist");
            }
        }
        if (cm == null)
            cm = createHSChart();
        generateChart(cm, filename);
    }
}

