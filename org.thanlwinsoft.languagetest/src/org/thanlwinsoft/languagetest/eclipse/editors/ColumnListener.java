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
package org.thanlwinsoft.languagetest.eclipse.editors;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author keith
 *
 */
public class ColumnListener implements SelectionListener
{
    private TableViewer tableViewer = null;
    private TestItemSorter sorter = null;
    private int columnIndex = -1;
    private String column = null;
    private String localeID = null;
    /**
     * A listener to detect clicks on column headings to initiate TestItem sorting
     * @param viewer
     * @param sorter
     * @param columnIndex
     * @param colId
     * @param localeId
     */
    public ColumnListener(TableViewer viewer, TestItemSorter sorter, int columnIndex, String colId, String localeId)
    {
        this.tableViewer = viewer;
        this.sorter = sorter;
        this.columnIndex = columnIndex;
        this.column = colId;
        this.localeID = localeId;
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        if (e.widget instanceof TableColumn)
        {
           if (tableViewer.getTable().getSortColumn() == e.widget)
           {
               int oldDir = tableViewer.getTable().getSortDirection();
               int newDir = SWT.None;
               
               switch (oldDir)
               {
               case SWT.DOWN:
                   newDir = SWT.UP;
                   break;
               case SWT.UP:    
                   newDir = SWT.NONE;
                   break;
               case SWT.NONE:
                   newDir = SWT.DOWN;
                   break;
               }
               sorter.setSortDirection(newDir);
               tableViewer.getTable().setSortDirection(newDir);
               tableViewer.setComparator(null);
               if (newDir != SWT.NONE)
                   tableViewer.setComparator(sorter);
               //tableViewer.getTable().redraw();
           }
           else
           {
               tableViewer.getTable().setSortColumn((TableColumn)e.widget);
               sorter.setSortColumn(columnIndex, column);
               sorter.setLocale(localeID);
               sorter.setSortDirection(SWT.DOWN);
               tableViewer.getTable().setSortDirection(SWT.DOWN);
               tableViewer.setComparator(null);
               tableViewer.setComparator(sorter);
               //tableViewer.getTable().redraw();
           }
        }
    }

}
