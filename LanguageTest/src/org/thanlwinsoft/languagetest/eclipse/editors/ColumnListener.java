/**
 * 
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
