/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/NoModuleSelectionPanel.java,v $
 *  Version:       $Revision: 1.4 $
 *  Last Modified: $Date: 2004/12/18 05:10:37 $
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

import java.io.IOException;
import java.awt.Window;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
/**
 *
 * @author  keith
 */
public class NoModuleSelectionPanel extends javax.swing.JPanel 
    implements EditorSubPanel, java.beans.PropertyChangeListener
{
    
    /** Creates new form NoModuleSelectionPanel */
    public NoModuleSelectionPanel() 
    {
        initComponents();
        try
        {
            welcomePane.addPropertyChangeListener(this);
            welcomePane.setPage(getClass().getResource("/uk/co/dabsol/stribley/language/text/LanguageModuleEditorWelcome.html"));
        }
        catch (IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
        
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent evt)
    {
        if (evt.getPropertyName().equals("page"))
        {
            welcomePane.repaint();
            System.out.println(evt.getPropertyName() + " " + evt.getOldValue() 
                + " -> " + evt.getNewValue());
        }
    }    
    
    public void antiAlias()
    {
        RenderingHints rHint = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Graphics2D g2d = (Graphics2D)welcomePane.getGraphics();
        g2d.addRenderingHints(rHint);
        rHint = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.addRenderingHints(rHint);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    private void initComponents()//GEN-BEGIN:initComponents
    {
        welcomePane = new javax.swing.JTextPane();

        setLayout(new java.awt.BorderLayout());

        welcomePane.setBackground(new java.awt.Color(204, 204, 204));
        welcomePane.setEditable(false);
        welcomePane.setText("Welcome to the Language Module Editor.\n\n1. Click on the Module that you want to edit or use the File menu to Open another module or create a New one..\n2. If you cannot see any Test Items in the module, click just to the left of the module name to expand the tree.\n3. Click the New Test Item button or select an existing Test Item to edit it.");
        welcomePane.setOpaque(false);
        add(welcomePane, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    public void commitChanges()
    {
        // nothing to do
    }    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane welcomePane;
    // End of variables declaration//GEN-END:variables
    
}
