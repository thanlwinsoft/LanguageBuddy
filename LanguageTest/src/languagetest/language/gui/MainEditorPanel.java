/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/MainEditorPanel.java,v $
 *  Version:       $Revision: 1.10 $
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

import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.List;
import java.util.Hashtable;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.CardLayout;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.ProgressMonitor;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.util.prefs.Preferences;

import org.jfree.ui.about.ProjectInfo;
import org.jfree.ui.about.AboutFrame;
import org.jfree.ui.about.Licences;
import org.jfree.ui.about.Contributor;
import org.jfree.ui.about.Library;
import languagetest.language.test.UserConfig;
import languagetest.language.test.DelimitedTestImport;
import languagetest.language.test.TestItem;
import languagetest.language.test.TestComponent;
import languagetest.language.test.TestModule;
import languagetest.util.XsltTransformer;
/**
 *
 * @author  keith
 */
public class MainEditorPanel extends javax.swing.JPanel implements TreeSelectionListener
{
    //private static long ADJUST_TIME = 250; // ms
    private static String NO_MODULE_CARD = "NoModulePanel";
    private static String MODULE_CARD = "ModulePanel";
    private static String TEST_ITEM_CARD = "TestItemPanel";
    private static String CONVERSATION_CARD = "ConversationPanel";
    private static String CVS_TAG_PREFIX = "LanguageTest_";
    private static String HTML_EXT = ".html";
    private static String CSV_EXT = ".csv";
    private static final int RECENT_FILES_INDEX = 6;
    private MainFrame mainFrame = null;
    private TestModule currentModule = null;
    private TestItem currentTestItem = null;
    private JPanel returnPanel = null;
    private EditorSubPanel currentSubPanel = null;
    private NoModuleSelectionPanel noModulePanel = null;
    private ModuleEditorPanel modulePanel = null;
    private TestItemEditorPanel testItemPanel = null;
    private ConversationEditorPanel conversationPanel = null;
    private CardLayout layout = null;
    private File exportDir = null;
    private XsltTransformer csvTransformer = null;
    private XsltTransformer htmlTransformer = null;
    private ItemTransferHandler transferHandler = null;
    /** Creates new form ModuleEditorPanel */
    public MainEditorPanel(MainFrame mainFrame) 
    {
        this.mainFrame = mainFrame;
        initComponents();
        moduleTree.getSelectionModel().setSelectionMode
            (javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);
        moduleTree.setModel(mainFrame.getTreeModel());
        moduleTree.addTreeSelectionListener(this);
        noModulePanel = new NoModuleSelectionPanel();
        modulePanel = new ModuleEditorPanel(this);
        testItemPanel = new TestItemEditorPanel(this);
        conversationPanel = new ConversationEditorPanel(this);
        editPanel.add(noModulePanel, NO_MODULE_CARD);
        editPanel.add(modulePanel, MODULE_CARD);
        editPanel.add(testItemPanel, TEST_ITEM_CARD);
        editPanel.add(conversationPanel, CONVERSATION_CARD);
        layout = (CardLayout)editPanel.getLayout();
        layout.show(editPanel,NO_MODULE_CARD);
        currentSubPanel = noModulePanel;
        transferHandler = new ItemTransferHandler();
        moduleTree.setTransferHandler(transferHandler);
        moduleTree.setDragEnabled(true);
        // sort out edit menu
        
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()//GEN-BEGIN:initComponents
    {
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenu = new javax.swing.JMenu();
        newModule = new javax.swing.JMenuItem();
        newTestItem = new javax.swing.JMenuItem();
        newConversation = new javax.swing.JMenuItem();
        openModule = new javax.swing.JMenuItem();
        closeModule = new javax.swing.JMenuItem();
        saveModules = new javax.swing.JMenuItem();
        deleteItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        importItem = new javax.swing.JMenuItem();
        saveAs = new javax.swing.JMenuItem();
        exportMenu = new javax.swing.JMenu();
        htmlExport = new javax.swing.JMenuItem();
        csvExport = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        closeEditor = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        redoItem = new javax.swing.JMenuItem();
        undoItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        cutItem = new javax.swing.JMenuItem();
        copyItem = new javax.swing.JMenuItem();
        pasteItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        recMixer = new javax.swing.JMenuItem();
        playMixer = new javax.swing.JMenuItem();
        charMap = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        modCleanUp = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        config = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        about = new javax.swing.JMenuItem();
        buttonPanel = new javax.swing.JPanel();
        msgLabel = new javax.swing.JLabel();
        loadModuleButton = new javax.swing.JButton();
        newModuleButton = new javax.swing.JButton();
        unloadModuleButton = new javax.swing.JButton();
        newTestItemButton = new javax.swing.JButton();
        deleteTestItemButton = new javax.swing.JButton();
        doneButton = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        moduleTree = new javax.swing.JTree();
        editPanel = new javax.swing.JPanel();

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");
        newMenu.setMnemonic('n');
        newMenu.setText("New");
        newModule.setMnemonic('m');
        newModule.setText("New Module");
        newModule.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                newModuleActionPerformed(evt);
            }
        });

        newMenu.add(newModule);

        newTestItem.setText("New Test Item");
        newTestItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                newTestItemActionPerformed(evt);
            }
        });

        newMenu.add(newTestItem);

        newConversation.setMnemonic('c');
        newConversation.setText("New Conversation");
        newConversation.setEnabled(false);
        newMenu.add(newConversation);

        fileMenu.add(newMenu);

        openModule.setMnemonic('o');
        openModule.setText("Open Module");
        openModule.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                openModuleActionPerformed(evt);
            }
        });

        fileMenu.add(openModule);

        closeModule.setMnemonic('c');
        closeModule.setText("Close Module");
        closeModule.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                closeModuleActionPerformed(evt);
            }
        });

        fileMenu.add(closeModule);

        saveModules.setMnemonic('s');
        saveModules.setText("Save Modules");
        saveModules.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveModulesActionPerformed(evt);
            }
        });

        fileMenu.add(saveModules);

        deleteItem.setMnemonic('d');
        deleteItem.setText("Delete Test Item");
        deleteItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                deleteItemActionPerformed(evt);
            }
        });

        fileMenu.add(deleteItem);

        fileMenu.add(jSeparator3);

        fileMenu.add(jSeparator6);

        importItem.setMnemonic('i');
        importItem.setText("Import...");
        importItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                importItemActionPerformed(evt);
            }
        });

        fileMenu.add(importItem);

        saveAs.setText("Save As...");
        saveAs.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveAsActionPerformed(evt);
            }
        });

        fileMenu.add(saveAs);

        exportMenu.setMnemonic('e');
        exportMenu.setText("Export as ...");
        htmlExport.setText("HTML");
        htmlExport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                htmlExportActionPerformed(evt);
            }
        });

        exportMenu.add(htmlExport);

        csvExport.setText("Text - CSV");
        csvExport.setToolTipText("Comma Separated Values");
        csvExport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                csvExportActionPerformed(evt);
            }
        });

        exportMenu.add(csvExport);

        fileMenu.add(exportMenu);

        fileMenu.add(jSeparator4);

        closeEditor.setMnemonic('t');
        closeEditor.setText("Save & Start Test");
        closeEditor.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                closeEditorActionPerformed(evt);
            }
        });

        fileMenu.add(closeEditor);

        jMenuBar1.add(fileMenu);

        editMenu.setText("Edit");
        redoItem.setText("Redo");
        redoItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                redoItemActionPerformed(evt);
            }
        });

        editMenu.add(redoItem);

        undoItem.setText("Undo");
        undoItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                undoItemActionPerformed(evt);
            }
        });

        editMenu.add(undoItem);

        editMenu.add(jSeparator5);

        cutItem.setText("Cut");
        cutItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cutItemActionPerformed(evt);
            }
        });

        editMenu.add(cutItem);

        copyItem.setText("Copy");
        copyItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                copyItemActionPerformed(evt);
            }
        });

        editMenu.add(copyItem);

        pasteItem.setText("Paste");
        pasteItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pasteItemActionPerformed(evt);
            }
        });

        editMenu.add(pasteItem);

        jMenuBar1.add(editMenu);

        toolsMenu.setText("Tools");
        recMixer.setText("Record Volumes...");
        recMixer.setToolTipText("Open Record Mixer");
        recMixer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                recMixerActionPerformed(evt);
            }
        });

        toolsMenu.add(recMixer);

        playMixer.setText("Play Volume...");
        playMixer.setToolTipText("Open Play Mixer");
        playMixer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                playMixerActionPerformed(evt);
            }
        });

        toolsMenu.add(playMixer);

        charMap.setText("Character Map...");
        charMap.setToolTipText("Open Character Map");
        charMap.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                charMapActionPerformed(evt);
            }
        });

        toolsMenu.add(charMap);

        toolsMenu.add(jSeparator1);

        modCleanUp.setText("Clean Up Loaded Modules");
        modCleanUp.setToolTipText("Extract wanted segments from sound files and move all sound files into one directory per module.");
        modCleanUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                modCleanUpActionPerformed(evt);
            }
        });

        toolsMenu.add(modCleanUp);

        toolsMenu.add(jSeparator2);

        config.setText("Configure...");
        config.setToolTipText("Open Configuration Dialog");
        config.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                configActionPerformed(evt);
            }
        });

        toolsMenu.add(config);

        jMenuBar1.add(toolsMenu);

        helpMenu.setText("Help");
        about.setText("About");
        about.setToolTipText("About Language Test");
        about.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                aboutActionPerformed(evt);
            }
        });

        helpMenu.add(about);

        jMenuBar1.add(helpMenu);

        setLayout(new java.awt.BorderLayout());

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        buttonPanel.add(msgLabel);

        loadModuleButton.setText("Open Module...");
        loadModuleButton.setToolTipText("Open a new or existing test module");
        loadModuleButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                loadModuleButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(loadModuleButton);

        newModuleButton.setText("New Module...");
        newModuleButton.setToolTipText("Create a new module.");
        newModuleButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                newModuleButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(newModuleButton);

        unloadModuleButton.setText("Close Module");
        unloadModuleButton.setEnabled(false);
        unloadModuleButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                unloadModuleButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(unloadModuleButton);

        newTestItemButton.setText("New Item");
        newTestItemButton.setToolTipText("Add a new test item to the selected module");
        newTestItemButton.setEnabled(false);
        newTestItemButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                newTestItemButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(newTestItemButton);

        deleteTestItemButton.setText("Delete Item");
        deleteTestItemButton.setToolTipText("Delete current test item");
        deleteTestItemButton.setEnabled(false);
        deleteTestItemButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                deleteTestItemButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(deleteTestItemButton);

        doneButton.setText("Done");
        doneButton.setToolTipText("Save changes to modules and return to Test Selection Panel");
        doneButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                doneButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(doneButton);

        add(buttonPanel, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setDividerSize(5);
        jSplitPane1.setResizeWeight(0.3);
        jSplitPane1.setContinuousLayout(true);
        moduleTree.setToolTipText("Click on a Module or Test Item to edit it.");
        jScrollPane1.setViewportView(moduleTree);

        jSplitPane1.setLeftComponent(jScrollPane1);

        editPanel.setLayout(new java.awt.CardLayout());

        editPanel.setMinimumSize(new java.awt.Dimension(200, 323));
        editPanel.setOpaque(false);
        jSplitPane1.setRightComponent(editPanel);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void saveAsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveAsActionPerformed
    {//GEN-HEADEREND:event_saveAsActionPerformed
        if (currentModule == null) return;
        JFileChooser chooser = new JFileChooser();
        FileFilter filter = TestModuleUtilities.moduleFileFilter();

        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(currentModule.getFile());
        chooser.setSelectedFile(currentModule.getFile());
        int returnVal = chooser.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            UserConfig.getCurrent().getRecentFilesList().remove(currentModule);
            currentModule.setFile(chooser.getSelectedFile());
            UserConfig.getCurrent().getRecentFilesList().add(currentModule);
        }
    }//GEN-LAST:event_saveAsActionPerformed

    private void pasteItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pasteItemActionPerformed
    {//GEN-HEADEREND:event_pasteItemActionPerformed
        if (currentSubPanel == this.testItemPanel)
        {
            testItemPanel.paste(evt);
        }
    }//GEN-LAST:event_pasteItemActionPerformed

    private void copyItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_copyItemActionPerformed
    {//GEN-HEADEREND:event_copyItemActionPerformed
        if (currentSubPanel == this.testItemPanel)
        {
            testItemPanel.copy(evt);
        }
    }//GEN-LAST:event_copyItemActionPerformed

    private void cutItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cutItemActionPerformed
    {//GEN-HEADEREND:event_cutItemActionPerformed
        if (currentSubPanel == this.testItemPanel)
        {
            testItemPanel.cut(evt);
        }
    }//GEN-LAST:event_cutItemActionPerformed

    private void undoItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_undoItemActionPerformed
    {//GEN-HEADEREND:event_undoItemActionPerformed
        if (currentSubPanel == this.testItemPanel)
        {
            testItemPanel.undo(evt);
        }
    }//GEN-LAST:event_undoItemActionPerformed

    private void redoItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_redoItemActionPerformed
    {//GEN-HEADEREND:event_redoItemActionPerformed
        if (currentSubPanel == this.testItemPanel)
        {
            testItemPanel.redo(evt);
        }
    }//GEN-LAST:event_redoItemActionPerformed

    protected void export(XsltTransformer transformer, URL xsltFile, 
                          final String ext, boolean currentLangOnly)
    {
        if (currentModule == null) return;
        if (transformer == null)
        {
            transformer = new XsltTransformer();
            try
            {
                if (!transformer.createTransformer(xsltFile.openStream()))
                {
                    transformer = null;
                }
            }
            catch (java.io.IOException e)
            {
                JOptionPane.showMessageDialog(this,e.getLocalizedMessage());
                transformer = null;
            }
        }
        if (transformer == null)
        {
            JOptionPane.showMessageDialog(this,"Failed to initialise exporter");
        }
        else
        {
            FileFilter filter = new FileFilter() 
            {
                public boolean accept(File f)
                {
                    if (f.getName().toLowerCase().endsWith(ext)) return true;
                    if (f.isDirectory()) return true;
                    return false;
                }
                public String getDescription() { return "*" + ext; }
            };
            File outputFile =
                transformer.fileFromXml(currentModule.getFile(),exportDir,ext);
            JFileChooser chooser = new JFileChooser();
            
            chooser.setFileFilter(filter);
            chooser.setSelectedFile(outputFile);
            chooser.setDialogTitle("Export Module");
            int returnVal = chooser.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) 
            {
                outputFile = chooser.getSelectedFile();
                transformer.generateToFile
                    (currentModule.createXmlDoc(outputFile.getParentFile(), 
                                                currentLangOnly),
                     outputFile);
                exportDir = currentModule.getFile().getParentFile();
            }
        }
    }
    
    private void csvExportActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_csvExportActionPerformed
    {//GEN-HEADEREND:event_csvExportActionPerformed
        // Add your handling code here:
        URL xslUrl = getClass().getResource("/uk/co/dabsol/stribley/language/text/LanguageExportCsv.xsl");
        
        export(csvTransformer, xslUrl, CSV_EXT, true);
    }//GEN-LAST:event_csvExportActionPerformed

    private void htmlExportActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_htmlExportActionPerformed
    {//GEN-HEADEREND:event_htmlExportActionPerformed
        // Add your handling code here:
        URL xslUrl = getClass().getResource("/uk/co/dabsol/stribley/language/text/LanguageTest.xsl");
        export(htmlTransformer, xslUrl, HTML_EXT, false);
    }//GEN-LAST:event_htmlExportActionPerformed

    private void importItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_importItemActionPerformed
    {//GEN-HEADEREND:event_importItemActionPerformed
        // Add your handling code here:
        if (currentModule != null)
        {
            File chosenFile = null;
            JFileChooser chooser = new JFileChooser();
            FileFilter filter = new FileFilter() 
            {
                public boolean accept(File f)
                {
                    if (f.getName().toLowerCase().endsWith(".txt")) return true;
                    if (f.getName().toLowerCase().endsWith(".csv")) return true;
                    if (f.getName().toLowerCase().endsWith(".dat")) return true;
                    if (f.isDirectory()) return true;
                    return false;
                }
                public String getDescription() { return "Text Delimited"; }
            };
            chooser.setFileFilter(filter);

            chooser.setDialogTitle("Import Items");
            int returnVal = chooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File csvFile = chooser.getSelectedFile();
                DelimitedTestImport importer = 
                    new DelimitedTestImport(csvFile, currentModule);
                importer.parse();
                DefaultTreeModel model =
                    (DefaultTreeModel)moduleTree.getModel();
                model.reload(currentModule.getTreeNode());
            }            
        }
    }//GEN-LAST:event_importItemActionPerformed

    private void modCleanUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_modCleanUpActionPerformed
    {//GEN-HEADEREND:event_modCleanUpActionPerformed
        // force sub panels to be saved before cleanup
        moduleTree.clearSelection();
        
        new ModuleCleanUpManager(mainFrame.getModules(), mainFrame);
    }//GEN-LAST:event_modCleanUpActionPerformed

    private void configActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_configActionPerformed
    {//GEN-HEADEREND:event_configActionPerformed
        // Add your handling code here:
        UserConfigDialog dialog = 
            SystemHandler.getInstance().getConfigDialog(mainFrame);
        dialog.setVisible(true);
        mainFrame.setUserName();
        // force sub panels to be reloaded after config changes
        moduleTree.clearSelection();
    }//GEN-LAST:event_configActionPerformed

    private void charMapActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_charMapActionPerformed
    {//GEN-HEADEREND:event_charMapActionPerformed
        // Try to run the system's character map
        SystemHandler.getInstance().openCharMap();
        
    }//GEN-LAST:event_charMapActionPerformed

    private void playMixerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_playMixerActionPerformed
    {//GEN-HEADEREND:event_playMixerActionPerformed
        // Try to run the system's mixer
        SystemHandler.getInstance().openVolMixer();        
    }//GEN-LAST:event_playMixerActionPerformed

    private void recMixerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_recMixerActionPerformed
    {//GEN-HEADEREND:event_recMixerActionPerformed
        // Try to run the system's mixer
        SystemHandler.getInstance().openRecMixer();
        
    }//GEN-LAST:event_recMixerActionPerformed

    private void aboutActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_aboutActionPerformed
    {//GEN-HEADEREND:event_aboutActionPerformed
        // Add your handling code here:
        URL url = this.getClass().getResource(
            "/uk/co/dabsol/stribley/language/icons/languageTest.png");
        // if this was checked out with a tag then it should be substituted here
        String cvsVersion = "$Name:  $";
        String jarVersion = this.getClass().getPackage().getImplementationVersion();
        String version = jarVersion + " (Development Build)";
        // assume release builds are tagged in the form LanguageTest_x.x.x
        int nameStart = cvsVersion.indexOf(CVS_TAG_PREFIX);
        int nameEnd = cvsVersion.lastIndexOf('$');
        if (nameStart > -1 && nameEnd > nameStart + 1)
        {
            version = cvsVersion.substring(nameStart + CVS_TAG_PREFIX.length(),
                                           nameEnd);
        }
        // it looks better to have dots than underscores so replace them here
        version.replace('_', '.');
        ProjectInfo pInfo = new ProjectInfo(
            "Language Test",                            // name,
            version,                                     //String version,
            "A language learning tool",                   // String info,
            (new ImageIcon(url)).getImage(),            //Image logo,
            "Keith Stribley 2003,2004" ,               //String copyright,
            "GNU Public License - Version 2",           //String licenceName,
            Licences.GPL);                                       //String licenceText
        List contributors = new Vector();
        contributors.add(new Contributor("Keith Stribley","keith@snc.co.uk"));
        List libraries = new Vector();
        Library lib = new Library("jcommon", "0.9.1 (modified)", 
            Licences.LGPL, "http://www.jfree.org/");
        libraries.add(lib);
        lib = new Library("Tritonus", "", 
            Licences.LGPL, "http://www.tritonus.org/");
        libraries.add(lib);
        lib = new Library("Lame", "3.95.1", 
            Licences.LGPL, "http://www.mp3dev.org/ (Optional install)");
        libraries.add(lib);
        pInfo.setContributors(contributors);
        pInfo.setLibraries(libraries);
        AboutFrame aboutFrame = new AboutFrame("About Language Test", pInfo);
        aboutFrame.setVisible(true);
    }//GEN-LAST:event_aboutActionPerformed

    private void deleteItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteItemActionPerformed
    {//GEN-HEADEREND:event_deleteItemActionPerformed
        // Add your handling code here:
        deleteTestItem();
    }//GEN-LAST:event_deleteItemActionPerformed

    private void newTestItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newTestItemActionPerformed
    {//GEN-HEADEREND:event_newTestItemActionPerformed
        // Add your handling code here:
        newTestItem();
    }//GEN-LAST:event_newTestItemActionPerformed

    private void newModuleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newModuleActionPerformed
    {//GEN-HEADEREND:event_newModuleActionPerformed
        // Add your handling code here:
        newModule();
    }//GEN-LAST:event_newModuleActionPerformed

    private void closeEditorActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeEditorActionPerformed
    {//GEN-HEADEREND:event_closeEditorActionPerformed
        // Add your handling code here:
        done();
    }//GEN-LAST:event_closeEditorActionPerformed

    private void saveModulesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveModulesActionPerformed
    {//GEN-HEADEREND:event_saveModulesActionPerformed
        // Add your handling code here:
        saveAllModules();
    }//GEN-LAST:event_saveModulesActionPerformed

    private void closeModuleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeModuleActionPerformed
    {//GEN-HEADEREND:event_closeModuleActionPerformed
        // Add your handling code here:
        unloadModule();
    }//GEN-LAST:event_closeModuleActionPerformed

    private void openModuleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_openModuleActionPerformed
    {//GEN-HEADEREND:event_openModuleActionPerformed
        // Add your handling code here:
        loadModule();
    }//GEN-LAST:event_openModuleActionPerformed

    private void newModuleButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newModuleButtonActionPerformed
    {//GEN-HEADEREND:event_newModuleButtonActionPerformed
        // Add your handling code here:
        // Add your handling code here:
        newModule();
    }//GEN-LAST:event_newModuleButtonActionPerformed

    private void newModule()
    {
        TestModule newModule = mainFrame.newModule();
        if (newModule != null)
        {
            TreePath path = new TreePath
                    (mainFrame.getTreeModel().getPathToRoot(newModule.getTreeNode()));
            moduleTree.setSelectionPath(path);
        }
    }
    
    private void unloadModuleButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_unloadModuleButtonActionPerformed
    {//GEN-HEADEREND:event_unloadModuleButtonActionPerformed
         // Add your handling code here:
         unloadModule();
    }//GEN-LAST:event_unloadModuleButtonActionPerformed

    private void unloadModule()
    {
         if (currentModule != null)
         {
             currentSubPanel.commitChanges();
             // save changes to disk
             saveModule(currentModule);
             mainFrame.removeModule(currentModule);
             UserConfig.getCurrent().getRecentFilesList().refresh();
         }
    }
    
    private void deleteTestItemButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteTestItemButtonActionPerformed
    {//GEN-HEADEREND:event_deleteTestItemButtonActionPerformed
        // Add your handling code here:
        deleteTestItem();
    }//GEN-LAST:event_deleteTestItemButtonActionPerformed

    private void deleteTestItem()
    {
        if (currentTestItem != null)
        {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete \"" + 
                                              currentTestItem.getName() + "\"",
                                          "Confirm Delete", JOptionPane.YES_NO_OPTION,
                                          JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            {
                TestModule oldModule = currentModule;
                currentModule.removeTestItem(currentTestItem);
                mainFrame.getTreeModel().removeNodeFromParent
                            (currentTestItem.getTreeNode());
                currentTestItem = null;
                TreePath path = new TreePath
                    (mainFrame.getTreeModel().getPathToRoot(oldModule.getTreeNode()));
                moduleTree.setSelectionPath(path);
            }
        }
    }
    
    private void loadModuleButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_loadModuleButtonActionPerformed
    {//GEN-HEADEREND:event_loadModuleButtonActionPerformed
        // Add your handling code here:
        loadModule();
    }//GEN-LAST:event_loadModuleButtonActionPerformed

    private void loadModule()
    {
        TestModule newModule = mainFrame.loadModule();
        if (newModule != null)
        {
            setSelectionToModule(newModule);            
        }
    }
    
    private void newTestItemButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newTestItemButtonActionPerformed
    {//GEN-HEADEREND:event_newTestItemButtonActionPerformed
        newTestItem();
    }//GEN-LAST:event_newTestItemButtonActionPerformed

    private void newTestItem()
    {
        // Add your handling code here:
        if (currentModule != null)
        {
            System.out.println("Creating new Test Item");
            TestItem newTestItem = new TestItem(currentModule);
            currentModule.insertTestItem(newTestItem);
            mainFrame.getTreeModel().reload();
            currentSubPanel.commitChanges();
            setSelectionToItem(newTestItem);
            System.out.println("Created new Test Item");
        }
        else
        {
            System.out.println("No module selected");
        }
    }
    
    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_doneButtonActionPerformed
    {//GEN-HEADEREND:event_doneButtonActionPerformed
        done();
    }//GEN-LAST:event_doneButtonActionPerformed

    private void done()
    {
        saveAllModules();
        moduleTree.clearSelection();
        mainFrame.setJMenuBar(null);
        // remove recent files to force it to be reloaded next time
        fileMenu.remove(RECENT_FILES_INDEX);
        // if we are in middle of test resume test
        if (returnPanel.getClass() == TestPanel.class)
        {
            mainFrame.resumeTest();
        }
        else
        {
            testItemPanel.close();
            mainFrame.selectTest();
        }
    }
    
    public void saveAllModules()
    {
        // save modules
        Iterator i = mainFrame.getModules().iterator();
        while (i.hasNext())
        {
            TestModule module = (TestModule)i.next();
            saveModule(module);
        }        
    }
    
    private void saveModule(TestModule module)
    {
        while (!module.save())
        {
            // if failed to save, try asking user for another file name
            if (JOptionPane.showConfirmDialog
                (this, 
                 "There was an error saving module '" 
                 + module.getName() + "', perhaps it is read only.\n<" 
                 + module.getFile().getAbsolutePath()
                 + ">\nDo you want to save it to a different file?\nIf you choose 'No' you risk loosing data.", 
                 "Error saving Module",
                 JOptionPane.YES_NO_OPTION,
                 JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION)
            {
                JFileChooser chooser = new JFileChooser();
                FileFilter filter = TestModuleUtilities.moduleFileFilter();
                
                chooser.setFileFilter(filter);
                chooser.setCurrentDirectory(module.getFile());
                chooser.setSelectedFile(module.getFile());
                int returnVal = chooser.showSaveDialog(this);
                if(returnVal == JFileChooser.APPROVE_OPTION) 
                {                    
                    UserConfig.getCurrent().getRecentFilesList().remove(currentModule);
                    module.setFile(chooser.getSelectedFile());                    
                    UserConfig.getCurrent().getRecentFilesList().add(currentModule);
                }
            }
            else
            {
                // user said no so give up trying to save
                break;
            }
        }
    }
    

    public void valueChanged(javax.swing.event.TreeSelectionEvent treeSelectionEvent)
    {
        System.out.println(treeSelectionEvent.getNewLeadSelectionPath());
        if (!transferHandler.isDragging() && 
            (treeSelectionEvent.getNewLeadSelectionPath() !=
            treeSelectionEvent.getOldLeadSelectionPath()))
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                moduleTree.getLastSelectedPathComponent();
            
            // data should already be saved, changes here may crash drag
            //currentSubPanel.commitChanges();
            
            // reset item here, since its only valid for one of cards
            currentTestItem = null; 
            System.out.println("Selection change to: " + node);
            if (node == null)
            {
                showNoModuleCard();
                testItemPanel.show(null); // need to force reset on panel
                noModulePanel.antiAlias();
                return;
            }
            Object nodeInfo = node.getUserObject();
            if (nodeInfo.getClass() == TestModule.class) 
            {
                layout.show(editPanel, MODULE_CARD);
                currentModule = (TestModule)nodeInfo;
                currentSubPanel = modulePanel;
                // menus
                closeModule.setEnabled(true);
                deleteItem.setEnabled(false);
                newTestItem.setEnabled(true);
                newConversation.setEnabled(true);
                // buttons
                newTestItemButton.setEnabled(true);
                unloadModuleButton.setEnabled(true);
                loadModuleButton.setVisible(false);
                unloadModuleButton.setVisible(true);
                newModuleButton.setVisible(false);
                newTestItemButton.setVisible(true);
                deleteTestItemButton.setVisible(false);
                importItem.setEnabled(true);
                exportMenu.setEnabled(true);
                modulePanel.show(currentModule);
                testItemPanel.show(null); // need to force reset on panel
            }
            else if (nodeInfo.getClass() == TestItem.class)
            {
                layout.show(editPanel, TEST_ITEM_CARD);
                currentTestItem = (TestItem)nodeInfo;
                currentModule = currentTestItem.getModule();
                currentSubPanel = testItemPanel;
                // menus
                closeModule.setEnabled(true);
                deleteItem.setEnabled(true);
                newTestItem.setEnabled(true);
                newConversation.setEnabled(true);
                // buttons
                newTestItemButton.setEnabled(true);
                deleteTestItemButton.setEnabled(true);
                unloadModuleButton.setEnabled(true);
                loadModuleButton.setVisible(false);
                unloadModuleButton.setVisible(false);
                newModuleButton.setVisible(false);
                newTestItemButton.setVisible(true);
                deleteTestItemButton.setVisible(true);
                importItem.setEnabled(true);
                exportMenu.setEnabled(true);
                testItemPanel.show(currentTestItem);
            }
            else
            {
                showNoModuleCard();
                testItemPanel.show(null); // need to force reset on panel
            }                
        }
    }    
    
    private void showNoModuleCard()
    {
        layout.show(editPanel,NO_MODULE_CARD);
        currentSubPanel = noModulePanel;
        currentModule = null;
        // menus
        closeModule.setEnabled(true);
        deleteItem.setEnabled(false);
        newTestItem.setEnabled(false);
        newConversation.setEnabled(false);
        // buttons
        newTestItemButton.setEnabled(false);
        unloadModuleButton.setEnabled(false);
        loadModuleButton.setVisible(true);
        unloadModuleButton.setVisible(false);
        newModuleButton.setVisible(true);
        newTestItemButton.setVisible(false);
        deleteTestItemButton.setVisible(false);
        importItem.setEnabled(false);
        exportMenu.setEnabled(false);
    }
    
    public boolean isSelected(TestComponent item)
    {
        boolean selected = false;
        TreePath path = new TreePath
            (mainFrame.getTreeModel().getPathToRoot(item.getTreeNode()));
        if (path.equals(moduleTree.getSelectionPath())) selected = true;
        return selected;
    }
    
    public TestComponent getSelected()
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                moduleTree.getLastSelectedPathComponent();
        if (node.getUserObject() instanceof TestComponent)
        {
            return (TestComponent)node.getUserObject();
        }
        return null;
    }
    
    
    public void setSelection(TestComponent item)
    {
        TreePath path = new TreePath
            (mainFrame.getTreeModel().getPathToRoot(item.getTreeNode()));
        moduleTree.setSelectionPath(path);
    }
    
    public void setSelectionToItem(TestItem item)
    {
        TreePath path = new TreePath
            (mainFrame.getTreeModel().getPathToRoot(item.getTreeNode()));
        moduleTree.setSelectionPath(path);
    }
    
    public void setSelectionToModule(TestModule module)
    {
        TreePath path = new TreePath
            (mainFrame.getTreeModel().getPathToRoot(module.getTreeNode()));
        moduleTree.setSelectionPath(path);
        // expand the children
        moduleTree.expandPath(path);
    }
    
    protected long min2MilliSec(String min)
    {
        long ms = -1;
        
        int colonIndex = min.indexOf(':');
        try
        {
            if (colonIndex > -1)
            {
                long mins = Long.parseLong(min.substring(0,colonIndex));
                double secs = Double.parseDouble(min.substring(colonIndex + 1));
                if (secs > 60) ms = -1;
                else ms = mins * 60000 + (long)(secs * 1000);
            }
            else
            {
                ms = (long)(1000 * Double.parseDouble(min));
            }
            
        }
        catch (NumberFormatException e)
        {
            ms = -1;
        }
        return ms;
    }
    
    
    
    public void initialise(JPanel previousPanel, TestItem editItem)
    {
        noModulePanel.antiAlias();
        returnPanel = previousPanel;
        moduleTree.expandRow(0); // show the modules
        setMsgText("");
        if (returnPanel.getClass() == TestPanel.class)
        {
            doneButton.setText("Resume Test");            
        }
        else
        {
            doneButton.setText("Done");
        }
        if (editItem != null)
        {
            setSelectionToItem(editItem);
        }
        fileMenu.add(UserConfig.getCurrent().getRecentFilesList().getMenu(), 
            RECENT_FILES_INDEX);
        mainFrame.setJMenuBar(jMenuBar1);
        
    }
    
    public MainFrame getMainFrame() 
    { 
        return mainFrame; 
    }
    
    public void setMsgText(String text)
    {
        msgLabel.setText(text);
    }
    
    public File chooseAudioFile(File initialFile, String title, boolean openDialog)
    {
        return testItemPanel.chooseAudioFile(initialFile, title, openDialog);
    }
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem about;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JMenuItem charMap;
    private javax.swing.JMenuItem closeEditor;
    private javax.swing.JMenuItem closeModule;
    private javax.swing.JMenuItem config;
    private javax.swing.JMenuItem copyItem;
    private javax.swing.JMenuItem csvExport;
    private javax.swing.JMenuItem cutItem;
    private javax.swing.JMenuItem deleteItem;
    private javax.swing.JButton deleteTestItemButton;
    private javax.swing.JButton doneButton;
    private javax.swing.JMenu editMenu;
    private javax.swing.JPanel editPanel;
    private javax.swing.JMenu exportMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem htmlExport;
    private javax.swing.JMenuItem importItem;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton loadModuleButton;
    private javax.swing.JMenuItem modCleanUp;
    private javax.swing.JTree moduleTree;
    private javax.swing.JLabel msgLabel;
    private javax.swing.JMenuItem newConversation;
    private javax.swing.JMenu newMenu;
    private javax.swing.JMenuItem newModule;
    private javax.swing.JButton newModuleButton;
    private javax.swing.JMenuItem newTestItem;
    private javax.swing.JButton newTestItemButton;
    private javax.swing.JMenuItem openModule;
    private javax.swing.JMenuItem pasteItem;
    private javax.swing.JMenuItem playMixer;
    private javax.swing.JMenuItem recMixer;
    private javax.swing.JMenuItem redoItem;
    private javax.swing.JMenuItem saveAs;
    private javax.swing.JMenuItem saveModules;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JMenuItem undoItem;
    private javax.swing.JButton unloadModuleButton;
    // End of variables declaration//GEN-END:variables
    
}
