/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/TestItemStats.java,v $
 *  Version:       $Revision: 1.4 $
 *  Last Modified: $Date: 2004/12/18 05:10:38 $
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

import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import languagetest.language.test.UserConfig;
import languagetest.language.test.TestHistoryStorageException;
import languagetest.language.test.TestItem;
import languagetest.language.test.TestHistory;
import languagetest.language.test.TestType;
import languagetest.language.test.ItemHistory;
/**
 *
 * @author  keith
 */
public class TestItemStats extends javax.swing.JDialog
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8784601867113773553L;
	private static final Object [] COLUMN_NAMES = new String []
    {
        "Test Date", "Result"
    } ;
    private TestItem item = null;
    /** Creates new form TestItemStats */
    public TestItemStats(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        testType.addItem(TestType.getById(0));
        testType.addItem(TestType.getById(1));
        testType.addItem(TestType.getById(2));
        this.setSize(400,450);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    private void initComponents()//GEN-BEGIN:initComponents
    {
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        resetButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        testType = new javax.swing.JComboBox();
        itemName = new javax.swing.JLabel();
        userName = new javax.swing.JLabel();
        testCount = new javax.swing.JLabel();
        passCount = new javax.swing.JLabel();
        percentPasses = new javax.swing.JLabel();
        itemStatus = new javax.swing.JLabel();
        enabledCheckBox = new javax.swing.JCheckBox();

        setTitle("Test Item Statistics");
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                closeDialog(evt);
            }
        });

        resultTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {"-", null},
                {"-", null},
                {"-", null},
                {"-", null},
                {"-", null}
            },
            new String []
            {
                "Test Date", "Result"
            }
        )
        {
            /**
			 * 
			 */
			private static final long serialVersionUID = -4923248823465087421L;
			Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(resultTable);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        resetButton.setText("Reset Statistics");
        resetButton.setEnabled(false);
        resetButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                resetButtonActionPerformed(evt);
            }
        });

        jPanel1.add(resetButton);

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                okButtonActionPerformed(evt);
            }
        });

        jPanel1.add(okButton);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.BorderLayout(5, 0));

        jPanel2.setBorder(new javax.swing.border.TitledBorder("Summary Statistics"));
        jPanel3.setLayout(new java.awt.GridLayout(0, 1));

        jLabel13.setText("Test Type:");
        jPanel3.add(jLabel13);

        jLabel1.setText("Item name:");
        jPanel3.add(jLabel1);

        jLabel3.setText("Statistics for User:");
        jPanel3.add(jLabel3);

        jLabel4.setText("Total Test Count:");
        jPanel3.add(jLabel4);

        jLabel7.setText("Total Pass Count:");
        jPanel3.add(jLabel7);

        jLabel9.setText("Percent passes:");
        jPanel3.add(jLabel9);

        jLabel11.setText("Item status:");
        jPanel3.add(jLabel11);

        jPanel2.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel4.setLayout(new java.awt.GridLayout(0, 1));

        testType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                testTypeActionPerformed(evt);
            }
        });

        jPanel4.add(testType);

        itemName.setText("Item Name");
        jPanel4.add(itemName);

        userName.setText("Default User");
        jPanel4.add(userName);

        testCount.setText("0");
        jPanel4.add(testCount);

        passCount.setText("0");
        jPanel4.add(passCount);

        percentPasses.setText("0%");
        jPanel4.add(percentPasses);

        itemStatus.setText("Unlearnt");
        jPanel4.add(itemStatus);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        enabledCheckBox.setText("Include in revision tests");
        enabledCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                enabledCheckBoxActionPerformed(evt);
            }
        });

        jPanel2.add(enabledCheckBox, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        pack();
    }//GEN-END:initComponents

    private void enabledCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_enabledCheckBoxActionPerformed
    {//GEN-HEADEREND:event_enabledCheckBoxActionPerformed
        TestHistory h = UserConfig.getCurrent().getTestHistory();
        try
        {
            if (enabledCheckBox.isSelected())
            {
                h.ignoreItem(item, getCurrentTestType(), false);
            }
            else
            {
                h.ignoreItem(item, getCurrentTestType(), true);
            }
        }
        catch (TestHistoryStorageException thse)
        {
            JOptionPane.showConfirmDialog(this, thse.getMessage(), 
                "Error Writing Test History", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_enabledCheckBoxActionPerformed

    private void testTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_testTypeActionPerformed
    {//GEN-HEADEREND:event_testTypeActionPerformed
        // Add your handling code here:
        loadItemStats(getCurrentTestType());
    }//GEN-LAST:event_testTypeActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_resetButtonActionPerformed
    {//GEN-HEADEREND:event_resetButtonActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_resetButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_okButtonActionPerformed
    {//GEN-HEADEREND:event_okButtonActionPerformed
        // Add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed
    
    protected TestType getCurrentTestType()
    {
        return TestType.getById(testType.getSelectedIndex());
    }
    
    public void initialise(TestItem item)
    {
        this.item = item;
        userName.setText(UserConfig.getCurrent().getUserName());
        itemName.setText(item.getName());
        loadItemStats(TestType.getById(testType.getSelectedIndex()));
        
    }
    
    protected void loadItemStats(TestType type)
    {
        DefaultTableModel model = (DefaultTableModel)resultTable.getModel();
        if (item == null) return; // can't load if no item
        ItemHistory history = null;
        try
        {
            history = UserConfig.getCurrent().getTestHistory()
                .getHistoryItem(item, type);
        }
        catch (TestHistoryStorageException thse)
        {
            JOptionPane.showConfirmDialog(this, thse.getMessage(), 
                "Error Reading Test History", JOptionPane.WARNING_MESSAGE);
        }
        if (history != null)
        {
            passCount.setText(Integer.toString(history.getPassCount()));
            testCount.setText(Integer.toString(history.getTestCount()));
            if (history.getTestCount() > 0)
            {
                percentPasses.setText(Integer.toString(100 * history.getPassCount() /
                    history.getTestCount()) + "%");
            }
            else
            {
                percentPasses.setText("0%");
            }
            model.setDataVector(history.getResultTable(), COLUMN_NAMES);
            if (history.isDisabled())
            {
                enabledCheckBox.setSelected(false);
            }
            else
            {
                enabledCheckBox.setSelected(true);
            }
            itemStatus.setText(history.getItemStatus(type));
        }
        else
        {
            model.setDataVector(null,COLUMN_NAMES);
        }
    }
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        new TestItemStats(new javax.swing.JFrame(), true).setVisible(true);
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox enabledCheckBox;
    private javax.swing.JLabel itemName;
    private javax.swing.JLabel itemStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel passCount;
    private javax.swing.JLabel percentPasses;
    private javax.swing.JButton resetButton;
    private javax.swing.JTable resultTable;
    private javax.swing.JLabel testCount;
    private javax.swing.JComboBox testType;
    private javax.swing.JLabel userName;
    // End of variables declaration//GEN-END:variables
    
}
