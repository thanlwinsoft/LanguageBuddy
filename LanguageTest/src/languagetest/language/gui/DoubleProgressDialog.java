/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/DoubleProgressDialog.java,v $
 *  Version:       $Revision: 1.3 $
 *  Last Modified: $Date: 2004/12/18 05:10:36 $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2004 Keith Stribley <jungleglacier@snc.co.uk>
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

import javax.swing.JProgressBar;
import javax.swing.JOptionPane;
/**
 *
 * @author  keith
 */
public class DoubleProgressDialog extends javax.swing.JDialog
{
    private boolean cancelRequested = false;
    /** Creates new form DoubleProgressDialog */
    public DoubleProgressDialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()//GEN-BEGIN:initComponents
    {
        jPanel1 = new javax.swing.JPanel();
        subLabel = new javax.swing.JLabel();
        subProgressBar = new javax.swing.JProgressBar();
        mainLabel = new javax.swing.JLabel();
        mainProgressBar = new javax.swing.JProgressBar();
        moduleLabel = new javax.swing.JLabel();
        moduleProgressBar = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setBorder(new javax.swing.border.TitledBorder("Operation Progress"));
        subLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        subLabel.setText(" ");
        subLabel.setAlignmentX(0.5F);
        jPanel1.add(subLabel);

        jPanel1.add(subProgressBar);

        mainLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mainLabel.setText(" ");
        mainLabel.setAlignmentX(0.5F);
        jPanel1.add(mainLabel);

        jPanel1.add(mainProgressBar);

        moduleLabel.setText("Overall Progress");
        moduleLabel.setAlignmentX(0.5F);
        jPanel1.add(moduleLabel);

        jPanel1.add(moduleProgressBar);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelButtonActionPerformed(evt);
            }
        });

        jPanel2.add(cancelButton);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        // Add your handling code here:
        confirmCancel();
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void confirmCancel()
    {
        if (JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel the Module Clean Up?" +
            "This may leave your modules in an unpredictable state.",
            "Module Clean Up", JOptionPane.WARNING_MESSAGE)==
            JOptionPane.YES_OPTION)
        {
            cancelRequested = true;
        }
    }
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)//GEN-FIRST:event_closeDialog
    {
        confirmCancel();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        new DoubleProgressDialog(new javax.swing.JFrame(), true).setVisible(true);
    }
    public void setMainLabel(String text)
    {
        mainLabel.setText(text);
    }
    public void setSubLabel(String text)
    {
        subLabel.setText(text);
    }
    public void setMainProgress(int progress, int length)
    {
        setProgress(mainProgressBar, progress, length);
    }
    public void setSubProgress(int progress, int length)
    {
        setProgress(subProgressBar, progress, length);
    }
    public void setModProgress(int progress, int length)
    {
        setProgress(moduleProgressBar, progress, length);
    }
    
    public void setProgress(JProgressBar progressBar, int progress, int length)
    {
        if (progress < 0) 
        {
            progressBar.setIndeterminate(true);
        }
        else
        {
            progressBar.setIndeterminate(false);
            progressBar.setMinimum(0);
            progressBar.setMaximum(length);
            progressBar.setValue(progress);
        }
    }
    
    public boolean isCancelRequested() { return cancelRequested; }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel mainLabel;
    private javax.swing.JProgressBar mainProgressBar;
    private javax.swing.JLabel moduleLabel;
    private javax.swing.JProgressBar moduleProgressBar;
    private javax.swing.JLabel subLabel;
    private javax.swing.JProgressBar subProgressBar;
    // End of variables declaration//GEN-END:variables
    
}
