/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/ModuleEditorPanel.java,v $
 *  Version:       $Revision: 1.12 $
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


import org.jfree.ui.FontChooserDialog;

import javax.swing.JLabel;
import java.awt.Font;
import java.util.TreeSet;
import java.util.Set;
import java.util.Iterator;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import languagetest.language.test.UserConfig;
import languagetest.language.test.TestItem;
import languagetest.language.test.TestModule;
import languagetest.language.test.LanguageConfig;
import languagetest.language.test.UniversalLanguage;


/**
 *
 * @author  keith
 */
public class ModuleEditorPanel extends javax.swing.JPanel implements EditorSubPanel
{
    private TestModule currentModule = null;
    private MainEditorPanel mainEditorPanel = null;
    private UniversalLanguage nativeLanguage = null;
    /** Creates new form ModuleEditorPanel */
    public ModuleEditorPanel(MainEditorPanel mainEditorPanel)
    {
        this.mainEditorPanel = mainEditorPanel;
        initComponents();
        categoryPanel.setVisible(false); // since it isn't implemented yet
    }
    
    public void show(TestModule module)
    {
        
        currentModule = module;
        
        moduleFileNameField.setText(module.getFile().getName());
        moduleFileNameField.setToolTipText(module.getFile().getAbsolutePath());
        // Initialise the list of languages configured for the module
        DefaultListModel ndlm = new DefaultListModel();
        Iterator i = currentModule.getNativeLanguages().iterator();
        while (i.hasNext())
        {
            ndlm.addElement(i.next());
        }
        nLangList.setModel(ndlm);
        DefaultListModel fdlm = new DefaultListModel();
        i = currentModule.getForeignLanguages().iterator();
        while (i.hasNext())
        {
            fdlm.addElement(i.next());
        }
        fLangList.setModel(fdlm);
        // initialise selection
        UniversalLanguage currentNative = 
            LanguageConfig.getCurrent().getNativeLanguage();
        // initialise list of languages that can be added
        DefaultComboBoxModel dcbm = 
            new DefaultComboBoxModel();
        // only list languages not already added
        i = UserConfig.getCurrent().getNativeLanguages().iterator();
        if (i.hasNext())
        {
            dcbm.addElement("Choose a language...");
            while (i.hasNext())
            {
                UniversalLanguage ul = (UniversalLanguage)i.next();
                if (!ndlm.contains(ul))
                {
                    dcbm.addElement(ul);
                }
            }
        }
        else
        {
            dcbm.addElement("None to add");
        }
        nLangCombo.setModel(dcbm);
        nLangCombo.setToolTipText("List of languages not already added. Use Tools->Configure to add more languages.");
        dcbm = new DefaultComboBoxModel();        
        i = UserConfig.getCurrent().getForeignLanguages().iterator();
        if (i.hasNext())
        {
            dcbm.addElement("Choose a language...");
            while (i.hasNext())
            {
                UniversalLanguage ul = (UniversalLanguage)i.next();
                if (!fdlm.contains(ul))
                {
                    dcbm.addElement(ul);
                }
            }
        }
        else
        {
            dcbm.addElement("None to add");
        }
        
        fLangCombo.setModel(dcbm);
        fLangCombo.setToolTipText("List of languages not already added. Use Tools->Configure to add more languages.");
        
        // try to select native language, otherwise first in selection
        if (ndlm.contains(currentNative))
        {
            nLangList.setSelectedValue(currentNative, true);
        }
        else if (ndlm.size()>0)
        {
            nLangList.setSelectedIndex(0);
            currentNative = (UniversalLanguage)nLangList.getSelectedValue();
        }
        else
        {
            currentNative = null;
        }
        if (currentNative != null)
        {
            // set module name for currently selected language
            moduleNameField.setText(module.getName(currentNative));
        }
        UniversalLanguage currentForeign = 
            LanguageConfig.getCurrent().getForeignLanguage();
        if (fdlm.contains(currentForeign))
        {
            fLangList.setSelectedValue(currentForeign,true);
        }
        numItemsField.setText(Integer.toString(module.getTestList().size()));

    }
    
    public TestModule getCurrentModule()
    {
        return currentModule;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()//GEN-BEGIN:initComponents
    {
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        modNameLabel = new javax.swing.JLabel();
        fileNameLabel = new javax.swing.JLabel();
        numItemsLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        moduleNameField = new javax.swing.JTextField();
        moduleFileNameField = new javax.swing.JLabel();
        numItemsField = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        nLangList = new javax.swing.JList();
        nFontLabel = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        nFontButton = new javax.swing.JButton();
        nRemove = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        nAddLabel = new javax.swing.JLabel();
        nLangCombo = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        fLangList = new javax.swing.JList();
        fFontLabel = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        fFontButton = new javax.swing.JButton();
        fRemove = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        fAddLabel = new javax.swing.JLabel();
        fLangCombo = new javax.swing.JComboBox();
        categoryPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        newCat = new javax.swing.JButton();
        newSubCat = new javax.swing.JButton();
        renameCat = new javax.swing.JButton();
        deleteCat = new javax.swing.JButton();
        globalCat = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel6 = new javax.swing.JPanel();
        cleanUpAudio = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setBorder(new javax.swing.border.TitledBorder("Module Properties"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(0, 1, 1, 0));

        modNameLabel.setText("Module Name:");
        jPanel3.add(modNameLabel);

        fileNameLabel.setText("File name:");
        jPanel3.add(fileNameLabel);

        numItemsLabel.setText("Number of items:");
        jPanel3.add(numItemsLabel);

        jPanel1.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel4.setLayout(new java.awt.GridLayout(0, 1, 1, 0));

        moduleNameField.setColumns(10);
        moduleNameField.setText("Untitled");
        moduleNameField.setToolTipText("Module Name which appears in Module Tree window. This must be set for each known language.");
        moduleNameField.setMaximumSize(new java.awt.Dimension(110, 2147483647));
        moduleNameField.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                moduleNameFieldFocusLost(evt);
            }
        });

        jPanel4.add(moduleNameField);

        moduleFileNameField.setText("Untitled.xml");
        jPanel4.add(moduleFileNameField);

        numItemsField.setText("0");
        jPanel4.add(numItemsField);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        add(jPanel1);

        jPanel7.setLayout(new java.awt.GridLayout(1, 2));

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.Y_AXIS));

        jPanel8.setBorder(new javax.swing.border.TitledBorder("Known Languages"));
        nLangList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        nLangList.addListSelectionListener(new javax.swing.event.ListSelectionListener()
        {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt)
            {
                nLangListValueChanged(evt);
            }
        });

        jScrollPane2.setViewportView(nLangList);

        jPanel8.add(jScrollPane2);

        nFontLabel.setText(" ");
        nFontLabel.setAlignmentX(0.5F);
        jPanel8.add(nFontLabel);

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.X_AXIS));

        nFontButton.setText("Set Font");
        nFontButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                nFontButtonActionPerformed(evt);
            }
        });

        jPanel10.add(nFontButton);

        nRemove.setText("Remove");
        nRemove.setToolTipText("Remove selected language from module.");
        nRemove.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                nRemoveActionPerformed(evt);
            }
        });

        jPanel10.add(nRemove);

        jPanel8.add(jPanel10);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        nAddLabel.setText("Add:");
        jPanel12.add(nAddLabel);

        nLangCombo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                nLangComboActionPerformed(evt);
            }
        });

        jPanel12.add(nLangCombo);

        jPanel8.add(jPanel12);

        jPanel7.add(jPanel8);

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.Y_AXIS));

        jPanel9.setBorder(new javax.swing.border.TitledBorder("New Language Variants"));
        fLangList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        fLangList.addListSelectionListener(new javax.swing.event.ListSelectionListener()
        {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt)
            {
                fLangListValueChanged(evt);
            }
        });

        jScrollPane3.setViewportView(fLangList);

        jPanel9.add(jScrollPane3);

        fFontLabel.setText(" ");
        fFontLabel.setAlignmentX(0.5F);
        jPanel9.add(fFontLabel);

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.X_AXIS));

        fFontButton.setText("Set Font");
        fFontButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fFontButtonActionPerformed(evt);
            }
        });

        jPanel11.add(fFontButton);

        fRemove.setText("Remove");
        fRemove.setToolTipText("Remove selected language from module.");
        fRemove.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fRemoveActionPerformed(evt);
            }
        });

        jPanel11.add(fRemove);

        jPanel9.add(jPanel11);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        fAddLabel.setText("Add:");
        jPanel13.add(fAddLabel);

        fLangCombo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fLangComboActionPerformed(evt);
            }
        });

        jPanel13.add(fLangCombo);

        jPanel9.add(jPanel13);

        jPanel7.add(jPanel9);

        add(jPanel7);

        categoryPanel.setLayout(new java.awt.BorderLayout());

        categoryPanel.setBorder(new javax.swing.border.TitledBorder("Module Categories"));
        categoryPanel.setToolTipText("Not implemented yet.");
        jPanel5.setLayout(new java.awt.GridLayout(0, 1));

        newCat.setText("New Category");
        newCat.setToolTipText("Adds new category at the same level as the currently selected category");
        newCat.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                newCatActionPerformed(evt);
            }
        });

        jPanel5.add(newCat);

        newSubCat.setText("New Sub-Category");
        newSubCat.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                newSubCatActionPerformed(evt);
            }
        });

        jPanel5.add(newSubCat);

        renameCat.setText("Rename Category");
        renameCat.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                renameCatActionPerformed(evt);
            }
        });

        jPanel5.add(renameCat);

        deleteCat.setText("Delete Category");
        deleteCat.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                deleteCatActionPerformed(evt);
            }
        });

        jPanel5.add(deleteCat);

        globalCat.setText("Global Category");
        globalCat.setToolTipText("Make this Category Visible in All modules");
        globalCat.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                globalCatActionPerformed(evt);
            }
        });

        jPanel5.add(globalCat);

        categoryPanel.add(jPanel5, java.awt.BorderLayout.EAST);

        jTree1.setToolTipText("Not implemented yet.");
        jScrollPane1.setViewportView(jTree1);

        categoryPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(categoryPanel);

        jPanel6.setLayout(new java.awt.BorderLayout());

        cleanUpAudio.setText("Clean up Audio Files");
        cleanUpAudio.setToolTipText("Create one audio file per test item containing only the relevant time sample. The sound files are placed in a directory with the same name as this module.");
        cleanUpAudio.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cleanUpAudioActionPerformed(evt);
            }
        });

        jPanel6.add(cleanUpAudio, java.awt.BorderLayout.NORTH);

        add(jPanel6);

    }//GEN-END:initComponents

    private void fRemoveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fRemoveActionPerformed
    {//GEN-HEADEREND:event_fRemoveActionPerformed
        if (fLangList.getSelectedValue() != null)
        {
            UniversalLanguage ul = (UniversalLanguage)
                fLangList.getSelectedValue();
            currentModule.removeForeignLanguage(ul);
            // remove from list and add it to the combo list so that it can 
            // be added back
            ((DefaultListModel)fLangList.getModel()).removeElement(ul);
            ((DefaultComboBoxModel)fLangCombo.getModel()).addElement(ul);
            // now prompt whether all the items should have this removed
            if (JOptionPane.showConfirmDialog(this,
                "Do you want to permanently remove all the entries for \n" + 
                ul.getDescription() + " from all the Test Items in this module?",
                "Remove Entries for Language?", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            {
                Iterator i = currentModule.getTestList().iterator();
                while (i.hasNext())
                {
                    TestItem item = (TestItem)i.next();
                    item.setForeign(null,ul);
                }
            }
        }
    }//GEN-LAST:event_fRemoveActionPerformed

    private void nRemoveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nRemoveActionPerformed
    {//GEN-HEADEREND:event_nRemoveActionPerformed
        if (nLangList.getSelectedValue() != null)
        {
            UniversalLanguage ul = (UniversalLanguage)
                nLangList.getSelectedValue();
            currentModule.removeNativeLanguage(ul);
            // remove from list and add it to the combo list so that it can 
            // be added back
            ((DefaultListModel)nLangList.getModel()).removeElement(ul);
            ((DefaultComboBoxModel)nLangCombo.getModel()).addElement(ul);
            // now prompt whether all the items should have this removed
            if (JOptionPane.showConfirmDialog(this,
                "Do you want to permanently remove all the entries for \n" + 
                ul.getDescription() + " from all the Test Items in this module?",
                "Remove Entries for Language?", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            {
                Iterator i = currentModule.getTestList().iterator();
                while (i.hasNext())
                {
                    TestItem item = (TestItem)i.next();
                    item.setNative(null,ul);
                }
            }
        }
    }//GEN-LAST:event_nRemoveActionPerformed

    private void fLangListValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_fLangListValueChanged
    {//GEN-HEADEREND:event_fLangListValueChanged
        if (fLangList.getSelectedValue() != null)
        {
            UniversalLanguage ul = (UniversalLanguage)
                fLangList.getSelectedValue();
            fFontLabel.setFont(currentModule.getForeignFont(ul));
            fFontLabel.setText(currentModule.getForeignFont(ul).getName());
            fFontLabel.setToolTipText(currentModule.getForeignFont(ul).getName());
        }
        else
        {
            fFontLabel.setText("");
            fFontLabel.setToolTipText("");
        }
    }//GEN-LAST:event_fLangListValueChanged

    private void nLangListValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_nLangListValueChanged
    {//GEN-HEADEREND:event_nLangListValueChanged
        if (nLangList.getSelectedValue() != null)
        {
            UniversalLanguage ul = (UniversalLanguage)
                nLangList.getSelectedValue();
            nFontLabel.setFont(currentModule.getNativeFont(ul));
            nFontLabel.setText(currentModule.getNativeFont(ul).getName());
            nFontLabel.setToolTipText(currentModule.getNativeFont(ul).getName());
            moduleNameField.setText(currentModule.getName(ul));
            moduleNameField.setEditable(true);
            nativeLanguage = ul;
        }
        else
        {
            nFontLabel.setText("");
            nFontLabel.setToolTipText("");
            nativeLanguage = null;
            moduleNameField.setEditable(false);
        }
    }//GEN-LAST:event_nLangListValueChanged

    private void fLangComboActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fLangComboActionPerformed
    {//GEN-HEADEREND:event_fLangComboActionPerformed
        if (fLangCombo.getSelectedItem() != null && 
            fLangCombo.getSelectedItem() instanceof UniversalLanguage)
        {
            UniversalLanguage ul = (UniversalLanguage)
                fLangCombo.getSelectedItem();
            DefaultListModel fdlm = (DefaultListModel)fLangList.getModel();
            if (fdlm.size() > 0)
            {
                 UniversalLanguage existingL = (UniversalLanguage)fdlm.get(0);
                 if (!ul.getLanguageCode().equals(existingL.getLanguageCode()))
                 {
                     if (JOptionPane.showConfirmDialog(this, 
                        "Are you sure that you want to add the language " + 
                        ul.getDescription() + "?\n" +
                        "It appears to be completely different to: " +
                        existingL.getDescription() + "\n" +
                        "You are advised to only include script variants " + 
                        "of the same language within \n" +
                        "one module, because you can only store one " +
                        "audio recording per Test Item.", "Unexpected language",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
                     {
                        return;
                     }
                 }
                fdlm.addElement(ul);
                ((DefaultComboBoxModel)fLangCombo.getModel()).removeElement(ul);
                currentModule.setForeignFont(UserConfig.getCurrent()
                    .getForeignDefaultFont(ul),ul);
            }
        }
    }//GEN-LAST:event_fLangComboActionPerformed

    private void fFontButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fFontButtonActionPerformed
    {//GEN-HEADEREND:event_fFontButtonActionPerformed
        if (fLangList.getSelectedValue() != null)
        {
            UniversalLanguage ul = (UniversalLanguage)
                fLangList.getSelectedValue();
            currentModule.setForeignFont
        		(chooseFont(currentModule.getForeignFont(ul),
        		 "Choose font for " + ul.getDescription(), 
                         fFontLabel));
            if (currentModule.getForeignFont(ul) != null)
            {
                fFontLabel.setFont(currentModule.getForeignFont(ul));
                fFontLabel.setText(currentModule.getForeignFont(ul).getName());
                fFontLabel.setToolTipText(currentModule.getForeignFont(ul).getName());
            }
            else
            {
                fFontLabel.setText(" ");
                fFontLabel.setToolTipText("None");
            }
        }
    }//GEN-LAST:event_fFontButtonActionPerformed

    private void nLangComboActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nLangComboActionPerformed
    {//GEN-HEADEREND:event_nLangComboActionPerformed
        if (nLangCombo.getSelectedItem() != null &&
            nLangCombo.getSelectedItem() instanceof UniversalLanguage)
        {
            UniversalLanguage ul = (UniversalLanguage)
                nLangCombo.getSelectedItem();
            ((DefaultListModel)nLangList.getModel())
                .addElement(ul);
            ((DefaultComboBoxModel)nLangCombo.getModel()).removeElement(ul);
            currentModule.setNativeFont(UserConfig.getCurrent()
                .getNativeDefaultFont(ul),ul);
        }
    }//GEN-LAST:event_nLangComboActionPerformed

    private void nFontButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nFontButtonActionPerformed
    {//GEN-HEADEREND:event_nFontButtonActionPerformed
        if (nLangList.getSelectedValue() != null)
        {
            UniversalLanguage ul = (UniversalLanguage)
                nLangList.getSelectedValue();
            currentModule.setNativeFont
        		(chooseFont(currentModule.getNativeFont(ul),
        		 "Choose font for " + ul.getDescription(), 
                         nFontLabel));
            if (currentModule.getNativeFont(ul) != null)
            {
                nFontLabel.setFont(currentModule.getNativeFont(ul));
                nFontLabel.setText(currentModule.getNativeFont(ul).getName());
                nFontLabel.setToolTipText(currentModule.getNativeFont(ul).getName());
            }
            else
            {
                nFontLabel.setText(" ");
                nFontLabel.setToolTipText("None");
            }
        }
    }//GEN-LAST:event_nFontButtonActionPerformed

    private void cleanUpAudioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cleanUpAudioActionPerformed
    {//GEN-HEADEREND:event_cleanUpAudioActionPerformed
        // Attempt to clean up the audio files to make them faster to play
        // back.
        Set moduleSet = new TreeSet();
        moduleSet.add(currentModule);
        new ModuleCleanUpManager(moduleSet,mainEditorPanel.getMainFrame());
    }//GEN-LAST:event_cleanUpAudioActionPerformed

    private void moduleNameFieldFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_moduleNameFieldFocusLost
    {//GEN-HEADEREND:event_moduleNameFieldFocusLost
        // Add your handling code here:
        storeChanges();
    }//GEN-LAST:event_moduleNameFieldFocusLost

    protected Font chooseFont(Font currentFont, String prompt, JLabel label)
    {
        Font newFont = null;
        FontChooserDialog chooser = 
            new FontChooserDialog(mainEditorPanel.getMainFrame(), 
                                  prompt, true, currentFont);
        chooser.setSize(400, 400);
        chooser.setVisible(true);
        if (chooser.isCancelled())
        {
            newFont = currentFont;
        }
        else
        {
            newFont = chooser.getSelectedFont();
            if (newFont != null)
            {
                    label.setFont(newFont);
                    label.setText(newFont.getName());
                    label.setToolTipText(newFont.getName() + 
                                        " (" + 
                                        newFont.getSize() + 
                                        "Pt)");
            }	
        }
        return newFont;
    }    
    
    private void newCatActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newCatActionPerformed
    {//GEN-HEADEREND:event_newCatActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_newCatActionPerformed
    
    private void newSubCatActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newSubCatActionPerformed
    {//GEN-HEADEREND:event_newSubCatActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_newSubCatActionPerformed

    private void renameCatActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_renameCatActionPerformed
    {//GEN-HEADEREND:event_renameCatActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_renameCatActionPerformed

    private void deleteCatActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteCatActionPerformed
    {//GEN-HEADEREND:event_deleteCatActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_deleteCatActionPerformed

    private void globalCatActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_globalCatActionPerformed
    {//GEN-HEADEREND:event_globalCatActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_globalCatActionPerformed
    
    protected void storeChanges()
    {
        
        if (currentModule != null && nativeLanguage != null)
        {
            
            // font changes will be done separately
            if (currentModule.getName(nativeLanguage).equals(moduleNameField.getText()))
            {
                
            }
            else
            {
                // need to remove the module from the list and reinsert
                // stop this be called twice
                TestModule changingModule = currentModule;
                // set currentModule to null since act of removing module
                // will cause this function to be called again before we
                // have finished the first run
                // selection may have already been lost if user has clicked on 
                // another tree node
                boolean isSelected = mainEditorPanel.isSelected(currentModule);
                currentModule = null;
                mainEditorPanel.getMainFrame().removeModule(currentModule);
                changingModule.setName(moduleNameField.getText(), nativeLanguage);
                // reinsert
                mainEditorPanel.getMainFrame().insertModule(changingModule);
                currentModule = changingModule;
                if (isSelected)
                {
                    mainEditorPanel.setSelectionToModule(currentModule);
                }
            }
        }
    }
    
    /** Called to rename a module */
    public void commitChanges()
    {
        storeChanges();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel categoryPanel;
    private javax.swing.JButton cleanUpAudio;
    private javax.swing.JButton deleteCat;
    private javax.swing.JLabel fAddLabel;
    private javax.swing.JButton fFontButton;
    private javax.swing.JLabel fFontLabel;
    private javax.swing.JComboBox fLangCombo;
    private javax.swing.JList fLangList;
    private javax.swing.JButton fRemove;
    private javax.swing.JLabel fileNameLabel;
    private javax.swing.JToggleButton globalCat;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTree jTree1;
    private javax.swing.JLabel modNameLabel;
    private javax.swing.JLabel moduleFileNameField;
    private javax.swing.JTextField moduleNameField;
    private javax.swing.JLabel nAddLabel;
    private javax.swing.JButton nFontButton;
    private javax.swing.JLabel nFontLabel;
    private javax.swing.JComboBox nLangCombo;
    private javax.swing.JList nLangList;
    private javax.swing.JButton nRemove;
    private javax.swing.JButton newCat;
    private javax.swing.JButton newSubCat;
    private javax.swing.JLabel numItemsField;
    private javax.swing.JLabel numItemsLabel;
    private javax.swing.JButton renameCat;
    // End of variables declaration//GEN-END:variables
    
}
