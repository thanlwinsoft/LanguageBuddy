/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/TestItemTable.java,v $
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

import java.util.SortedSet;
import java.util.Iterator;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.Component;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.Icon;
import javax.swing.event.ListSelectionListener;
import languagetest.language.test.TestItem;
import languagetest.language.test.TestModule;
/**
 *
 * @author  Keith Stribley
 */
public class TestItemTable extends org.jfree.ui.SortableTable
    implements TableCellRenderer 
{
    public static final int MODULE_COL = 0;
    public static final int NATIVE_COL = 1;
    public static final int FOREIGN_COL = 2;
    public static final int PLAY_COL = 3;
    public static final int EDIT_COL = 4;
    public static final int NUM_COLS = 5;
    public static final float TABLE_FONT_SCALE = (float)0.6;
    private DefaultTableCellRenderer moduleName;
    private DefaultTableCellRenderer langText;
    private JButton editButton;
    private JButton playButton;
    private ButtonCellEditor editButtonEditor;
    private ButtonCellEditor playButtonEditor;
    private VocabTablePanel tablePanel;
    private Icon playIcon = null;
    private int maxComponentHeight = 0;
    private AudioProgressListener audioListener = null;
    
    /** Creates a new instance of TestItemTable */
    public TestItemTable(SortedSet moduleList, VocabTablePanel tablePanel)
    {
        super(new TestItemTableModel(moduleList));
        this.tablePanel = tablePanel;
        moduleName = new DefaultTableCellRenderer();
        langText = new DefaultTableCellRenderer();
        editButton = new ButtonRenderer("Edit");
        editButton.setToolTipText("Edit Test Item");
        playIcon = new javax.swing.ImageIcon(getClass()
            .getResource("/languagetest/sound/icons/play.png"));
        playButton = new ButtonRenderer(playIcon);
        
        langText.setOpaque(true);
        moduleName.setOpaque(true);
        editButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                editActionPerformed();
            }
        });
        playButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                playActionPerformed();
            }
        });
        editButtonEditor = new ButtonCellEditor(editButton);
        playButtonEditor = new ButtonCellEditor(playButton);
        setDefaultEditor(TestItem.class, editButtonEditor);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        audioListener = new AudioProgressListener(tablePanel.getAudio());
        tablePanel.getAudio().addPlayListener(audioListener);
        // tweak layout
        tweak(moduleList);
        // listen for selection events
        ((TestItemTableModel)getModel()).setTable(this);
        getSelectionModel()
            .addListSelectionListener((TestItemTableModel)getModel());
    }
    
    protected void tweak(SortedSet moduleList)
    {
        // find row height - since defaults are usually too small
        // this needs to be done before layout starts, because if you change 
        // row height during redraw you get buttons drawn where they 
        // shouldn't be
        Iterator i = moduleList.iterator();
        langText.setText("Dummy");// component needs some text to have a height
        while (i.hasNext())
        {
            TestModule module = (TestModule)i.next();// resize height if necessary
            
            langText.setFont(module.getForeignFont()
                             .deriveFont(TABLE_FONT_SCALE * 
                                         module.getForeignFont().getSize()));
            maxComponentHeight = 
                Math.max(maxComponentHeight, 
                         (int)langText.getPreferredSize().getHeight());
            langText.setFont(module.getNativeFont()
                             .deriveFont(TABLE_FONT_SCALE * 
                                         module.getNativeFont().getSize()));
            maxComponentHeight = 
                Math.max(maxComponentHeight, 
                         (int)langText.getPreferredSize().getHeight());
            maxComponentHeight = 
                Math.max(maxComponentHeight, playIcon.getIconHeight());
        }
        if (getRowHeight() < maxComponentHeight)
        {
            setRowHeight(maxComponentHeight);
        }
        // stop edit button swallowing all the space
        setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        //setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        getColumnModel().getColumn(MODULE_COL)
            .setPreferredWidth(80);
        getColumnModel().getColumn(NATIVE_COL)
            .setPreferredWidth(300);
        getColumnModel().getColumn(FOREIGN_COL)
            .setPreferredWidth(300);
        getColumnModel().getColumn(EDIT_COL)
            .setMaxWidth(60);
        getColumnModel().getColumn(EDIT_COL)
            .setPreferredWidth(60);
        getColumnModel().getColumn(PLAY_COL)
            .setMaxWidth(playIcon.getIconWidth());
        getColumnModel().getColumn(PLAY_COL)
            .setPreferredWidth(playIcon.getIconWidth());
        doLayout();
        
        resizeAndRepaint();
    }
    
    public TableCellEditor getCellEditor(int row, int col)
    {
        if (col == EDIT_COL)
        {
            return editButtonEditor;
        }
        else if (col == PLAY_COL)
        {
            return playButtonEditor;
        }
        else return super.getCellEditor(row,col);
    }
    
    public javax.swing.table.TableCellRenderer getCellRenderer(int row, int col)
    {
        return this;
    }
    
    
    public void editActionPerformed()
    {
        Object item = dataModel
            .getValueAt(editButtonEditor.getRow(), 
                        editButtonEditor.getColumn());
        if (item.getClass() == TestItem.class)
        {
            tablePanel.editItem((TestItem)item);
        }
        editButtonEditor.fireEditingStopped();
    }
    
    public void playActionPerformed()
    {
        Object item = dataModel
            .getValueAt(playButtonEditor.getRow(), 
                        playButtonEditor.getColumn());
        if (item.getClass() == TestItem.class)
        {
            TestItem tItem = (TestItem)item;
            if (tItem.getSoundFile() != null)
            {
                boolean isOpen = tablePanel.getAudio().open(tItem.getSoundFile());
                if (isOpen)
                {
                    tablePanel.getAudio().setBounds(tItem.getPlayStart(), 
                                                    tItem.getPlayEnd() - 
                                                    tItem.getPlayStart());
                    audioListener.checkProgress();
                    tablePanel.getAudio().play();   
                    
                }
                else
                {
                    System.out.println("Can't play");
                }
            }
        }
        playButtonEditor.fireEditingStopped();
    }
    
    public java.awt.Component getTableCellRendererComponent(JTable jTable, 
                                                            Object value,
                                                            boolean isSelected,
                                                            boolean hasFocus,
                                                            int row,
                                                            int column)
    {
        Component component = null;
        if (value == null) return null;
        if (value.getClass() == TestItem.class)
        {
            TestItem item = (TestItem)value;
            switch (column)
            {
                case MODULE_COL:
                    if (isSelected)
                    {
                        moduleName.setBackground(selectionBackground);
                    }
                    else
                    {
                        moduleName.setBackground(Color.white);
                    }
                    moduleName.setText(item.getModule().getName());
                    component = moduleName;
                    break;
                case NATIVE_COL:
                    if (isSelected)
                    {
                        langText.setBackground(selectionBackground);
                    }
                    else
                    {
                        langText.setBackground(Color.white);
                    }
                    langText.setText(item.getNativeText());
                    langText.setFont(item.getModule().getNativeFont()
                                     .deriveFont(TABLE_FONT_SCALE * 
                                     item.getModule().getNativeFont().getSize()));
                    component = langText;
                    break;
                case FOREIGN_COL:
                    if (isSelected)
                    {
                        langText.setBackground(selectionBackground);
                    }
                    else
                    {
                        langText.setBackground(Color.white);
                    }
                    langText.setText(item.getForeignText());
                    langText.setFont(item.getModule().getForeignFont()
                                     .deriveFont(TABLE_FONT_SCALE * 
                                     item.getModule().getForeignFont().getSize()));
                    component = langText;
                    break;   
                case EDIT_COL:
                    component = editButton;
                    break;
                case PLAY_COL:
                    if (item.getSoundFile() == null || 
                        !item.getSoundFile().exists())
                    {
                        playButton.setEnabled(false);
                    }
                    else
                    {
                        playButton.setEnabled(true);
                    }
                    component = playButton;
                    break;
            }
        }
        else
        {
            component = 
                jTable.getDefaultRenderer(value.getClass())
                    .getTableCellRendererComponent(jTable, value, isSelected, 
                                                   hasFocus, row, column);
        }
        return component;
    }
    
    
    
    
    
   protected class ButtonRenderer extends JButton     
   {
        ButtonRenderer(String text)
        {
            super(text);
        }
        ButtonRenderer(Icon icon)
        {
            super(icon);
        }
        // override methods as per DefaultTableCellRenderer
        //public void repaint() {}
        //public void repaint(long tm, int x, int y, int width, int height) {}
       // public void repaint(Rectangle r) {}
        //public void validate() {}
        //public void revalidate() {}
        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) 
        {
            // Strings get interned...
            if (propertyName=="text") {
                super.firePropertyChange(propertyName, oldValue, newValue);
            }
        }
        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) { }
    }

    
    
}
