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
package org.thanlwinsoft.languagetest.eclipse.chart;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.DataPackageImpl;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.chart.view.ChartHistoryProvider;
import org.thanlwinsoft.schemas.languagetest.history.ItemType;
import org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType;
import org.thanlwinsoft.schemas.languagetest.history.ResultType;
import org.thanlwinsoft.languagetest.language.test.TestType;

/**
 * @author keith
 *
 */
public class CumulativeTestPassChart implements ChartHistoryProvider
{
    public final static int CUMULATIVE_PASSES = 0;
    public final static int TEST_COUNT = 1;
    
    private final static int NEW_BIN = 0; // count of new items on one day
    private final static int TEST_BIN = 1; // count of items tested on one day
    private final static int PASS_BIN = 2; // count of items passed 1st time on one day
    private final static int DELTA_PASS_BIN = 3; // difference between previous pass state and todays
    private final static int NUM_BINS = 4;
    
    private int type = CUMULATIVE_PASSES;
    private SortedMap resultMap = null; //<long time, int[]>
    
    private DateTimeDataSet timeValues = null;
    private NumberDataSet passValues = null;
    private NumberDataSet totalValues = null;
    
    public final static String CHART_LABEL_SIZE_PREF = "ChartLabelSize";
    public final static String CHART_TITLE_SIZE_PREF = "ChartTitleSize";
    
    public CumulativeTestPassChart()
    {
        reset();
        LanguageTestPlugin.getPrefStore().setDefault(CHART_LABEL_SIZE_PREF, 8.0);
        LanguageTestPlugin.getPrefStore().setDefault(CHART_TITLE_SIZE_PREF, 9.0);
        
    }
    
    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.eclipse.views.ChartHistoryProvider#createChart()
     */
    public Chart createChart()
    {
        if (resultMap.size() == 0) return null;
        
        float titleFontSize = LanguageTestPlugin.getPrefStore().getFloat(CHART_TITLE_SIZE_PREF);
        float labelFontSize = LanguageTestPlugin.getPrefStore().getFloat(CHART_LABEL_SIZE_PREF);
        
        ChartWithAxes cwaScatter = ChartWithAxesImpl.create( );

        // Plot
        cwaScatter.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
        cwaScatter.getPlot( ).getClientArea( ).getOutline( ).setVisible( false );
        cwaScatter.getPlot( )
                .getClientArea( )
                .setBackground( ColorDefinitionImpl.create( 255, 255, 225 ) );

        // Title
        String title = "";
        switch (type)
        {
        case CUMULATIVE_PASSES:
            title = MessageUtil.getString("CumulativeGraphTitle");
            break;
        case TEST_COUNT:
            title = MessageUtil.getString("TestCountGraphTitle");
            break;
        }
        cwaScatter.getTitle( )
                .getLabel( )
                .getCaption( )
                .setValue( title );//$NON-NLS-1$
        FontDefinition tfd = 
            FontDefinitionImpl.copyInstance(cwaScatter.getTitle().getLabel().getCaption().getFont());
        tfd.setSize(titleFontSize);
        cwaScatter.getTitle().getLabel().getCaption().setFont(tfd);
        //Legend
        cwaScatter.getLegend( ).setVisible( true );
        FontDefinition lfd =  FontDefinitionImpl.copyInstance(cwaScatter.getLegend().getText().getFont());
        lfd.setSize(labelFontSize);
        cwaScatter.getLegend().getText().setFont(lfd);
        cwaScatter.getLegend().setAnchor(Anchor.EAST_LITERAL);
        
        // X-Axis
        Axis xAxisPrimary = ( (ChartWithAxesImpl) cwaScatter ).getPrimaryBaseAxes( )[0];
        //xAxisPrimary.setType( AxisType.LINEAR_LITERAL );
        xAxisPrimary.setType( AxisType.DATE_TIME_LITERAL );
        xAxisPrimary.getLabel( )
                .getCaption( )
                .setColor( ColorDefinitionImpl.GREEN( ).darker( ) );
        xAxisPrimary.getLabel().getCaption().setFont(FontDefinitionImpl.copyInstance(lfd));

        xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
        xAxisPrimary.getMajorGrid( )
                .getLineAttributes( )
                .setStyle( LineStyle.DOTTED_LITERAL );
        xAxisPrimary.getMajorGrid( )
                .getLineAttributes( )
                .setColor( ColorDefinitionImpl.GREY( ) );
        xAxisPrimary.getMajorGrid( ).getLineAttributes( ).setVisible( true );
        xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
        xAxisPrimary.getTitle().getCaption().setValue(MessageUtil.getString("DateAxisTitle"));
        xAxisPrimary.getTitle( ).setVisible( true );
        FontDefinition fd = FontDefinitionImpl.copyInstance(
                xAxisPrimary.getTitle().getCaption().getFont());
        fd.setSize(labelFontSize);
        xAxisPrimary.getTitle().getCaption().setFont(fd);
        // Y-Axis
        Axis yAxisPrimary = ( (ChartWithAxesImpl) cwaScatter ).getPrimaryOrthogonalAxis( xAxisPrimary );
        yAxisPrimary.getLabel( )
                .getCaption( )
                .setColor( ColorDefinitionImpl.BLUE( ) );
        yAxisPrimary.getLabel().getCaption().setFont(FontDefinitionImpl.copyInstance(lfd));
        yAxisPrimary.setType( AxisType.LINEAR_LITERAL );

        yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
        yAxisPrimary.getMajorGrid( )
                .getLineAttributes( )
                .setStyle( LineStyle.DOTTED_LITERAL );
        yAxisPrimary.getMajorGrid( )
                .getLineAttributes( )
                .setColor( ColorDefinitionImpl.GREY( ) );
        yAxisPrimary.getMajorGrid( ).getLineAttributes( ).setVisible( true );

        yAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
        NumberDataElement yOrigin = NumberDataElementImpl.create(0.0);
        yAxisPrimary.getOrigin().setValue(yOrigin);
        
        // Data Set
        //NumberDataSet timeValues = NumberDataSetImpl.create(getTimeArray());
        timeValues = DateTimeDataSetImpl.create(getTimeArray());
        
        passValues = null;
        totalValues = null;
        switch (type)
        {
        case CUMULATIVE_PASSES:
            passValues = NumberDataSetImpl.create(getIntegratedArray(DELTA_PASS_BIN));
            totalValues = NumberDataSetImpl.create(getIntegratedArray(NEW_BIN));
            break;
        case TEST_COUNT:
            passValues = NumberDataSetImpl.create(getIntArray(PASS_BIN));
            totalValues = NumberDataSetImpl.create(getIntArray(TEST_BIN));
            break;
        default:
            // for testing only
        }
        
        // X-Series
        Series seBase = SeriesImpl.create( );
        seBase.setDataSet( timeValues );

        SeriesDefinition sdX = SeriesDefinitionImpl.create( );
        xAxisPrimary.getSeriesDefinitions( ).add( sdX );
        sdX.getSeries( ).add( seBase );

        // Y-Series
        ScatterSeries ss = (ScatterSeries) ScatterSeriesImpl.create( );
        for ( int i = 0; i < ss.getMarkers( ).size( ); i++ )
        {
            ( (Marker) ss.getMarkers( ).get( i ) ).setType( MarkerType.TRIANGLE_LITERAL );
        }
        
        ss.getLabel( ).setVisible( false );
        ss.setSeriesIdentifier(MessageUtil.getString("NumItemsPassed"));
        ss.setDataSet( passValues );
        ss.getLineAttributes().setColor(ColorDefinitionImpl.GREEN());
        ss.getLineAttributes().setStyle(LineStyle.SOLID_LITERAL);
        ss.getLineAttributes().setVisible(true);
        ss.setPaletteLineColor(true);
        
        ScatterSeries ss2 = (ScatterSeries) ScatterSeriesImpl.create( );
        for ( int i = 0; i < ss2.getMarkers( ).size( ); i++ )
        {
            Marker marker = (Marker) ss2.getMarkers( ).get( i );
            marker.setType( MarkerType.DIAMOND_LITERAL );
        }
        ss2.getLabel( ).setVisible( false );
        ss2.setSeriesIdentifier(MessageUtil.getString("TotalNumItems"));
        ss2.setDataSet( totalValues );
        ss2.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
        ss2.getLineAttributes().setStyle(LineStyle.SOLID_LITERAL);
        ss2.getLineAttributes().setVisible(true);
        ss2.setPaletteLineColor(true);

        SeriesDefinition sdY = SeriesDefinitionImpl.create( );
        yAxisPrimary.getSeriesDefinitions( ).add( sdY );
        sdY.getSeriesPalette( ).update( ColorDefinitionImpl.GREEN() );
        sdY.getSeries( ).add( ss );
        
        SeriesDefinition sdY2 = SeriesDefinitionImpl.create( );
        yAxisPrimary.getSeriesDefinitions( ).add( sdY2 );
        sdY2.getSeriesPalette( ).update( ColorDefinitionImpl.BLUE() );
        sdY2.getSeries( ).add( ss2 );
        
        return cwaScatter;
    }

    /**
     * @param pass_bin2
     * @return
     */
    private double [] getIntArray(int bin)
    {
        double [] values = new double [resultMap.size()];
        Iterator iBin = resultMap.entrySet().iterator();
        int i = 0;
        while (iBin.hasNext())
        {
            Map.Entry me = (Map.Entry)iBin.next();
            int [] bins = (int[])me.getValue();
            values[i++] = bins[bin];
        }
        return values;
    }

    /**
     * @param new_bin2
     * @return
     */
    private double [] getIntegratedArray(int bin)
    {
        double [] values = new double [resultMap.size()];
        Iterator iBin = resultMap.entrySet().iterator();
        int i = 0;
        int sum = 0;
        while (iBin.hasNext())
        {
            Map.Entry me = (Map.Entry)iBin.next();
            int [] bins = (int[])me.getValue();
            sum +=  bins[bin];
            values[i++] = sum;
        }
        return values;
    }

    /**
     * @return
     */
    private long [] getTimeArray()
    {
        long [] values = new long [resultMap.size()];
        if (resultMap.size() == 0) return values;
        Iterator iBin = resultMap.keySet().iterator();
        int i = 0;
        while (iBin.hasNext())
        {
            Date date = (Date)iBin.next();
            values[i++] = date.getTime();
        }
        return values;
    }
    
    /**
     * @return
     */
    private double [] getDayArray()
    {
        double [] values = new double [resultMap.size()];
        if (resultMap.size() == 0) return values;
        Iterator iBin = resultMap.keySet().iterator();
        int i = 0;
        final double MSEC_IN_DAY = 3600 * 24 * 1000;
        values[i++] = 0;
        Date startDate = (Date)iBin.next();
        while (iBin.hasNext())
        {
            Date date = (Date)iBin.next();
            values[i++] = (date.getTime() - startDate.getTime()) / MSEC_IN_DAY;
        }
        return values;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.eclipse.views.ChartHistoryProvider#parse(org.thanlwinsoft.schemas.languagetest.ModuleHistoryType, org.thanlwinsoft.schemas.languagetest.TestType)
     */
    public void parse(ModuleHistoryType testHistory, TestType type)
    {
        for (int i = 0; i < testHistory.sizeOfItemArray(); i++)
        {
            ItemType item = testHistory.getItemArray(i);
            org.thanlwinsoft.schemas.languagetest.history.TestType tt = null;
            if (TestType.LISTENING_FOREIGN_NATIVE.equals(type))
            {
                tt = item.getFL();
            }
            else if (TestType.READING_FOREIGN_NATIVE.equals(type))
            {
                tt = item.getFR();
            }
            else if (TestType.READING_NATIVE_FOREIGN.equals(type))
            {
                tt = item.getNR();
            }
            if (tt != null)
            {
                processItemResults(tt);
            }
        }
    }
    
    protected void processItemResults(org.thanlwinsoft.schemas.languagetest.history.TestType tt)
    {
        boolean prevResult = false;
        Date prevDate = new Date(0);
        // This assumes that results are sorted with most recent at the end
        for (int j = 0; j < tt.sizeOfResultArray(); j++)
        {
            ResultType result = tt.getResultArray(j);
            Date date = new Date(result.getTime());
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            // round to the nearest day
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            int [] bins = null;
            if (resultMap.containsKey(calendar.getTime()))
            {
                bins = (int [])resultMap.get(calendar.getTime());
            }
            else
            {
                bins = new int [NUM_BINS];
                Arrays.fill(bins, 0);
                resultMap.put(calendar.getTime(), bins);
            }
            if (j == 0)
            {
                bins[NEW_BIN]++;
            }
            if (prevDate.equals(calendar.getTime()) == false)
            {
                bins[TEST_BIN]++;
                if (result.getPass()) bins[PASS_BIN]++;
                if (prevResult != result.getPass())
                {
                    int delta = (result.getPass()) ? 1 : -1;
                    bins[DELTA_PASS_BIN] += delta;
                }
            }
            prevResult = result.getPass();
            prevDate = calendar.getTime();
        }
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.eclipse.views.ChartHistoryProvider#reset()
     */
    public void reset()
    {
        resultMap = new TreeMap();
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.eclipse.views.ChartHistoryProvider#getTypes()
     */
    public String[] getTypes()
    {
        final String [] typeNames = new String [] {
          MessageUtil.getString("CumulativePasses"),      
          MessageUtil.getString("TestCount")//,  "Testing"
        };
        return typeNames;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.eclipse.views.ChartHistoryProvider#setType(int)
     */
    public void setType(int type)
    {
        this.type = type;
    }

}
