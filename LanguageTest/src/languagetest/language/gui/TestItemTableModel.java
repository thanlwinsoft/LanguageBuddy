/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/TestItemTableModel.java,v $
 *  Version:       $Revision: 1.3 $
 *  Last Modified: $Date: 2004/06/20 11:50:49 $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2003 Keith Stribley <jungleglacier@snc.co.uk>
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

package languagetest.language.gui;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import languagetest.language.test.NativeComparator;
import languagetest.language.test.ForeignComparator;
import languagetest.language.test.TestItem;
import languagetest.language.test.TestModule;
import languagetest.language.test.LanguageConfig;
/**
 *
 * @author  Keith Stribley
 */
public class TestItemTableModel extends org.jfree.ui.SortableTableModel
    implements ListSelectionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1081601588492338715L;
	public final static String MODULE_COL_NAME = "Module";
    public final static String NATIVE_COL_NAME = "English";
    public final static String FOREIGN_COL_NAME = "Burmese";
    public final static String EDIT_COL_NAME = "Edit";
    public final static String PLAY_COL_NAME = "Play";
    private Vector moduleOrder = null;
    private Vector nativeOrder = null;
    private Vector foreignOrder = null;
    private JTable table = null;
    private TestItem selectedItem = null;
    /** Creates a new instance of TestItemTableModel */
    public TestItemTableModel(SortedSet moduleList)
    {        
        sortTableData(moduleList);
    }
    
    public void setTable(JTable table)
    {
        this.table = table;
    }
    
    public void sortTableData(SortedSet moduleList)
    {
        moduleOrder = new Vector();
        // use set to do sorting
        TreeSet nativeSet = new TreeSet(new NativeComparator());
        TreeSet foreignSet = new TreeSet(new ForeignComparator());
        // populate list
        Iterator i = moduleList.iterator();
        while (i.hasNext())
        {
            // should always be TestModule but check anyway
            Object next = i.next();
            if (next.getClass() == TestModule.class)
            {
                TestModule mod = (TestModule)next;
                moduleOrder.addAll(mod.getTestList());
            }
            else
            {
                System.out.println("Unexpected class: " + next.getClass().getName());
            }
        }
        nativeSet.addAll(moduleOrder);
        foreignSet.addAll(moduleOrder);
        nativeOrder = new Vector(nativeSet);
        foreignOrder = new Vector(foreignSet);
        
    }
    
    public int getRowCount()
    {
        
        return moduleOrder.size();
    }
    
    protected Vector getOrderVector(int sortingColumn)
    {
        Vector order = null;
        switch (sortingColumn)
        {
            case TestItemTable.MODULE_COL:
                order = moduleOrder;
                break;
            case TestItemTable.NATIVE_COL:
                order = nativeOrder;
                break;
            case TestItemTable.FOREIGN_COL:
                order = foreignOrder;
                break;
            default:
                order = moduleOrder;
        }
        return order;
    }
    
    public Object getValueAt(int row, int col)
    {
        Vector order = getOrderVector(getSortingColumn());
        
        Object rowObject = null;
        if (order.size() > 0 && row < order.size())
        {
            if (getAscending()) rowObject = order.elementAt(row);
            else 
            {
                rowObject = order.elementAt(order.size() - row - 1);
            }
        }
        return rowObject;
    }
    
    public String getColumnName(int col)
    {
        String name = null;
        LanguageConfig lConfig = LanguageConfig.getCurrent();
        switch (col)
        {
            case TestItemTable.MODULE_COL:
                name = MODULE_COL_NAME;
                break;
            case TestItemTable.NATIVE_COL:
                name = lConfig.getNativeLanguage().getDescription();
                break;
            case TestItemTable.FOREIGN_COL:
                name = lConfig.getForeignLanguage().getDescription();
                break;
            case TestItemTable.EDIT_COL:
                name = EDIT_COL_NAME;
                break;
            case TestItemTable.PLAY_COL:
                name = PLAY_COL_NAME;
                break;
        }
        return name;
    }
    
    public int getColumnCount()
    {
        return TestItemTable.NUM_COLS;
    }
 
    /**
     * Method to define which columns are sortable
     * @param column  the column (zero-based index).
     *
     * @return boolean.
     */
    public boolean isSortable(int col)
    {
        boolean sortable;
        switch (col)
        {
            case TestItemTable.MODULE_COL:
            case TestItemTable.NATIVE_COL:
            case TestItemTable.FOREIGN_COL:
                sortable = true;
                break;
            default:
                sortable = false;
        }
        return sortable;
    }
    
    public boolean isCellEditable(int rowIndex, int col)
    {
        boolean editable = false;
        switch (col)
        {
            
            case TestItemTable.EDIT_COL:
            case TestItemTable.PLAY_COL:
                editable = true;
                break;
            default:
                editable = false;
        }
        return editable;
    }
    
    public void sortByColumn(int col, boolean ascending)
    {
        // try to maintain the same item selected during sort
        if (selectedItem != null)
        {
            Vector order = getOrderVector(col);
            int newIndex = order.indexOf(selectedItem);
            if (!ascending) newIndex = getRowCount() - 1 - newIndex;
            // now perform sort and reapply selection
            super.sortByColumn(col, ascending);
            if (table != null)
            {
                table.setRowSelectionInterval(newIndex, newIndex);
            }
        }
        else
        {
            // now defer to parent class
            super.sortByColumn(col, ascending);
        }
    }
    
    public void valueChanged(ListSelectionEvent lse)
    {
        if (table == null) return;
        int row = table.getSelectedRow();
        if (row > -1)
        {
            selectedItem = (TestItem)getValueAt(row, TestItemTable.MODULE_COL);
        }
        else
        {
            selectedItem = null;
        }
    }
}
