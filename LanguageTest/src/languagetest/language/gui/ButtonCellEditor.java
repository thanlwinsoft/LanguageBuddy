/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/ButtonCellEditor.java,v $
 *  Version:       $Revision: 1.3 $
 *  Last Modified: $Date: 2004/06/20 11:50:46 $
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

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JTable;
/**
 *
 * @author  Keith Stribley
 */
public class ButtonCellEditor extends javax.swing.DefaultCellEditor
{
    private int currentRow;
    private int currentCol;
    /** Creates a new instance of ButtonCellEditor */
    public ButtonCellEditor(JButton b)
    {
        super(new JCheckBox()); //Unfortunately, the constructor
                                //expects a check box, combo box,
                                //or text field.
            editorComponent = b;
            setClickCountToStart(1); //This is usually 1 or 2.

            //Must do this so that editing stops when appropriate.
            
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
             
        }

        public void fireEditingStopped() {
            super.fireEditingStopped();
        }

        public Object getCellEditorValue() {
            return null;
        }

        public Component getTableCellEditorComponent(JTable table, 
                                                     Object value,
                                                     boolean isSelected,
                                                     int row,
                                                     int column) {
            
            currentRow = row;
            currentCol = column;
            //if (isSelected)
            {
                ((JButton)editorComponent).doClick();
            }
            return editorComponent;
        }

        public int getRow() { return currentRow; }
        public int getColumn() { return currentCol; }
}
