/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/UserConfigDialog.java,v $
 *  Version:       $Revision: 1.7 $
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

import java.io.File;
import java.awt.event.ItemEvent;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.InputVerifier;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.util.prefs.Preferences;
import java.util.Vector;
import org.jfree.ui.RefineryUtilities;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.Mixer;
import languagetest.language.test.UserConfig;
import languagetest.language.test.TestType;
import languagetest.language.test.LanguageConfig;
/**
 *
 * @author  keith
 */
public class UserConfigDialog extends javax.swing.JDialog
{
    private UserConfig config = null;
    private LanguageSelectionPanel langPanel = null;
    private final static long MS_IN_HOUR = 3600000;
    private final static long MS_IN_DAY = 86400000;
    private final static String WINDOWS_EXE = ".exe";
    private final static String WAV_EXT = ".wav";
    private final static String MP3_EXT = ".mp3";
    private final static String OGG_EXT = ".ogg";
    private JFileChooser exeChooser = null;
    private MainFrame mf = null;
    /** Creates new form UserConfigDialog */
    public UserConfigDialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        if (parent instanceof MainFrame)
        {
            mf = (MainFrame)parent;
        }
        langPanel = new LanguageSelectionPanel(this, mf);
        jTabbedPane1.addTab("Language", langPanel);
        testTypeCombo.addItem(TestType.getById(0));
        testTypeCombo.addItem(TestType.getById(1));
        testTypeCombo.addItem(TestType.getById(2));
        
        InputVerifier verifier = new UserConfigDialog.IntegerInputVerifier();
        minNumPassesField.setInputVerifier(verifier);
        initialRevPeriodField.setInputVerifier(verifier);
        shortTermPeriodField.setInputVerifier(verifier);
        shortTermRevPeriodField.setInputVerifier(verifier);
        longTermPeriodField.setInputVerifier(verifier);
        longTermRevPeriodField.setInputVerifier(verifier);
        minTimeBetweenTestsField.setInputVerifier(verifier);
        config = UserConfig.getCurrent();
        if (config != null)
        {
            this.setTitle(config.getUserName() + "'s User Config");        
            userNameField.setText(config.getUserName());
            configPath.setText(config.getConfigPath().getAbsolutePath());
            configPath.setToolTipText(config.getConfigPath().getAbsolutePath());
            testTypeCombo.setSelectedIndex(0);            
        }
        if (LanguageConfig.getCurrent() == null)
        {
            jTabbedPane1.setSelectedComponent(langPanel);
        }
        rcField.setText(SystemHandler.getInstance().getRecMixerCommand());
        vcField.setText(SystemHandler.getInstance().getVolMixerCommand());
        cmField.setText(SystemHandler.getInstance().getCharMapCommand());
        
        if (mf != null)
        {
            Vector recMixers = mf.getLineControl().getRecMixers();
            int selectIndex = 0;
            for (int i = 0; i<recMixers.size(); i++)
            {
                Mixer.Info mi = (Mixer.Info)recMixers.get(i);
                recMixerCombo.addItem(mi.getName() + ": " + mi.getDescription());    
                if (mi == mf.getLineControl().getRecMixer().getMixerInfo())
                    selectIndex = i;
            }
            if (recMixers.size() > 0) recMixerCombo.setSelectedIndex(selectIndex);
            Vector playMixers = mf.getLineControl().getPlayMixers();
            selectIndex = 0;
            for (int i = 0; i<playMixers.size(); i++)
            {
                Mixer.Info mi = (Mixer.Info)playMixers.get(i);
                playMixerCombo.addItem(mi.getName() + ": " + mi.getDescription()); 
                if (mi == mf.getLineControl().getPlayMixer().getMixerInfo())
                    selectIndex = i;
            }
            if (playMixers.size() > 0) playMixerCombo.setSelectedIndex(selectIndex);
        }
        String soundExt = UserConfig.getCurrent().getDefaultSoundExtension();
        if (soundExt.equals(WAV_EXT)) recFormatCombo.setSelectedIndex(0);
        else if (soundExt.equals(MP3_EXT)) recFormatCombo.setSelectedIndex(1);
        initRevisionParameters(TestType.getById(0));
        pack();
        RefineryUtilities.centerDialogInParent(this);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    private void initComponents()//GEN-BEGIN:initComponents
    {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        userNameLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        userNameField = new javax.swing.JTextField();
        configPath = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        gOkButton = new javax.swing.JButton();
        revisionPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        rOkButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        testTypeCombo = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        minNumPassesField = new javax.swing.JTextField();
        initialRevPeriodField = new javax.swing.JTextField();
        shortTermPeriodField = new javax.swing.JTextField();
        shortTermRevPeriodField = new javax.swing.JTextField();
        longTermPeriodField = new javax.swing.JTextField();
        longTermRevPeriodField = new javax.swing.JTextField();
        minTimeBetweenTestsField = new javax.swing.JTextField();
        systemPanel = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        charMapLabel = new javax.swing.JLabel();
        volLabel = new javax.swing.JLabel();
        recLabel = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        cmField = new javax.swing.JTextField();
        vcField = new javax.swing.JTextField();
        rcField = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        cmButton = new javax.swing.JButton();
        vcButton = new javax.swing.JButton();
        rcButton = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        epOkButton = new javax.swing.JButton();
        audioPanel = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        playMixerLabel = new javax.swing.JLabel();
        recMixerLabel = new javax.swing.JLabel();
        recFormatLabel = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        playMixerCombo = new javax.swing.JComboBox();
        recMixerCombo = new javax.swing.JComboBox();
        recFormatCombo = new javax.swing.JComboBox();
        jPanel19 = new javax.swing.JPanel();
        audioOkButton = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                closeDialog(evt);
            }
        });

        generalPanel.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel6.setBorder(new javax.swing.border.TitledBorder("User"));
        jPanel8.setLayout(new java.awt.GridLayout(0, 1));

        jPanel8.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        userNameLabel.setText("User Name");
        jPanel8.add(userNameLabel);

        jLabel9.setText("Config Path");
        jPanel8.add(jLabel9);

        jPanel6.add(jPanel8, java.awt.BorderLayout.WEST);

        jPanel9.setLayout(new java.awt.GridLayout(0, 1));

        jPanel9.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        userNameField.setColumns(10);
        userNameField.setAlignmentX(0.0F);
        jPanel9.add(userNameField);

        jPanel9.add(configPath);

        jPanel6.add(jPanel9, java.awt.BorderLayout.CENTER);

        generalPanel.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        gOkButton.setText("OK");
        gOkButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                gOkButtonActionPerformed(evt);
            }
        });

        jPanel7.add(gOkButton);

        generalPanel.add(jPanel7, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("General", generalPanel);

        revisionPanel.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        rOkButton.setText("OK");
        rOkButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rOkButtonActionPerformed(evt);
            }
        });

        jPanel5.add(rOkButton);

        revisionPanel.add(jPanel5, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(new javax.swing.border.TitledBorder("Revision Parameters"));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Test Type");
        jPanel1.add(jLabel1);

        testTypeCombo.setToolTipText("Choose Test Type to configure");
        testTypeCombo.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                testTypeComboItemStateChanged(evt);
            }
        });

        jPanel1.add(testTypeCombo);

        jPanel2.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.GridLayout(0, 1, 5, 5));

        jPanel3.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        jPanel3.setAlignmentY(0.0F);
        jPanel3.setMaximumSize(null);
        jLabel2.setText("Min. No. of Passes before 'learnt'");
        jPanel3.add(jLabel2);

        jLabel3.setText("Initial Revision Period");
        jPanel3.add(jLabel3);

        jLabel4.setText("Short Term Period");
        jPanel3.add(jLabel4);

        jLabel5.setText("Short Term Revision Period");
        jPanel3.add(jLabel5);

        jLabel6.setText("Long Term Period");
        jPanel3.add(jLabel6);

        jLabel7.setText("Long Term Revision Period");
        jPanel3.add(jLabel7);

        jLabel8.setText("Min. Time between Tests");
        jPanel3.add(jLabel8);

        jPanel2.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel31.setLayout(new java.awt.GridLayout(0, 1, 5, 5));

        jPanel31.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        jPanel31.setAlignmentY(0.0F);
        jPanel31.add(jLabel21);

        jLabel31.setText("Days");
        jPanel31.add(jLabel31);

        jLabel41.setText("Days");
        jPanel31.add(jLabel41);

        jLabel51.setText("Days");
        jPanel31.add(jLabel51);

        jLabel61.setText("Days");
        jPanel31.add(jLabel61);

        jLabel71.setText("Days");
        jPanel31.add(jLabel71);

        jLabel81.setText("Hours");
        jPanel31.add(jLabel81);

        jPanel2.add(jPanel31, java.awt.BorderLayout.EAST);

        jPanel4.setLayout(new java.awt.GridLayout(0, 1, 5, 5));

        jPanel4.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        jPanel4.setAlignmentY(0.0F);
        minNumPassesField.setToolTipText("Number of consecutive test passes before an item is considered 'learnt'.");
        jPanel4.add(minNumPassesField);

        initialRevPeriodField.setToolTipText("The initial period at which 'learnt' items will be retested.");
        jPanel4.add(initialRevPeriodField);

        shortTermPeriodField.setToolTipText("Period since last failure used for Short Term Revision testing.");
        jPanel4.add(shortTermPeriodField);

        shortTermRevPeriodField.setToolTipText("Revision period used after the last failure exceeds the 'Short Term Period' period.");
        jPanel4.add(shortTermRevPeriodField);

        longTermPeriodField.setToolTipText("Period since last failure used for Long Term Revision testing.");
        jPanel4.add(longTermPeriodField);

        longTermRevPeriodField.setToolTipText("Revision period used after the last failure exceeds the 'Long Term Period' period.");
        jPanel4.add(longTermRevPeriodField);

        minTimeBetweenTestsField.setToolTipText("This is the minimum time between tests before the result of a retest will be stored. ");
        jPanel4.add(minTimeBetweenTestsField);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        revisionPanel.add(jPanel2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Revision Test", revisionPanel);

        systemPanel.setLayout(new java.awt.BorderLayout());

        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.X_AXIS));

        jPanel13.setBorder(new javax.swing.border.TitledBorder("External Programs"));
        jPanel15.setLayout(new java.awt.GridLayout(0, 1));

        charMapLabel.setText("Character Map");
        jPanel15.add(charMapLabel);

        volLabel.setText("Volume Control");
        volLabel.setToolTipText("");
        jPanel15.add(volLabel);

        recLabel.setText("Recording Control");
        jPanel15.add(recLabel);

        jPanel13.add(jPanel15);

        jPanel14.setLayout(new java.awt.GridLayout(0, 1));

        jPanel14.setMinimumSize(new java.awt.Dimension(100, 57));
        jPanel14.setPreferredSize(new java.awt.Dimension(100, 57));
        cmField.setToolTipText("Character Map Program");
        cmField.setAlignmentX(0.0F);
        jPanel14.add(cmField);

        vcField.setToolTipText("Mixer Volume Control Program");
        vcField.setAlignmentX(0.0F);
        jPanel14.add(vcField);

        rcField.setToolTipText("Recording Mixer Control Program (may be same as Volume Control)");
        rcField.setAlignmentX(0.0F);
        jPanel14.add(rcField);

        jPanel13.add(jPanel14);

        jPanel12.setLayout(new java.awt.GridLayout(0, 1));

        cmButton.setText("Browse...");
        cmButton.setAlignmentX(1.0F);
        cmButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmButtonActionPerformed(evt);
            }
        });

        jPanel12.add(cmButton);

        vcButton.setText("Browse...");
        vcButton.setAlignmentX(1.0F);
        vcButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                vcButtonActionPerformed(evt);
            }
        });

        jPanel12.add(vcButton);

        rcButton.setText("Browse...");
        rcButton.setAlignmentX(1.0F);
        rcButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rcButtonActionPerformed(evt);
            }
        });

        jPanel12.add(rcButton);

        jPanel13.add(jPanel12);

        jPanel11.add(jPanel13, java.awt.BorderLayout.NORTH);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        epOkButton.setText("OK");
        epOkButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                epOkButtonActionPerformed(evt);
            }
        });

        jPanel16.add(epOkButton);

        jPanel11.add(jPanel16, java.awt.BorderLayout.SOUTH);

        systemPanel.add(jPanel11, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("System", systemPanel);

        audioPanel.setLayout(new java.awt.BorderLayout());

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.X_AXIS));

        jPanel10.setBorder(new javax.swing.border.TitledBorder("Audio Settings"));
        jPanel17.setLayout(new java.awt.GridLayout(3, 1));

        playMixerLabel.setText("Playing mixer:");
        jPanel17.add(playMixerLabel);

        recMixerLabel.setText("Recording mixer:");
        jPanel17.add(recMixerLabel);

        recFormatLabel.setText("Recording format:");
        jPanel17.add(recFormatLabel);

        jPanel10.add(jPanel17);

        jPanel18.setLayout(new java.awt.GridLayout(3, 1));

        jPanel18.add(playMixerCombo);

        jPanel18.add(recMixerCombo);

        recFormatCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "WAV", "MP3" }));
        recFormatCombo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                recFormatComboActionPerformed(evt);
            }
        });

        jPanel18.add(recFormatCombo);

        jPanel10.add(jPanel18);

        audioPanel.add(jPanel10, java.awt.BorderLayout.NORTH);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        audioOkButton.setText("Ok");
        audioOkButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                audioOkButtonActionPerformed(evt);
            }
        });

        jPanel19.add(audioOkButton);

        audioPanel.add(jPanel19, java.awt.BorderLayout.SOUTH);

        jPanel20.setLayout(new java.awt.BorderLayout());

        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setText("Warning: changing mixers is an experimental feature. The mixer is set for the current session only.");
        jTextArea1.setWrapStyleWord(true);
        jPanel20.add(jTextArea1, java.awt.BorderLayout.CENTER);

        audioPanel.add(jPanel20, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Audio", audioPanel);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void recFormatComboActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_recFormatComboActionPerformed
    {//GEN-HEADEREND:event_recFormatComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_recFormatComboActionPerformed

    private void audioOkButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_audioOkButtonActionPerformed
    {//GEN-HEADEREND:event_audioOkButtonActionPerformed
        if (saveConfig())
        {
            setVisible(false);
        }
    }//GEN-LAST:event_audioOkButtonActionPerformed

    private void epOkButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_epOkButtonActionPerformed
    {//GEN-HEADEREND:event_epOkButtonActionPerformed
        // Add your handling code here:
        if (saveConfig())
        {
            setVisible(false);
        }
    }//GEN-LAST:event_epOkButtonActionPerformed

    private File getExeFile(String oldFile)
    {
        if (exeChooser == null)
        {
            FileFilter filter = new ExeFileFilter();
        
            exeChooser = new JFileChooser();
            exeChooser.setFileFilter(filter);
        }
        exeChooser.setSelectedFile(new File(oldFile));
        
        int returnVal = exeChooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            return exeChooser.getSelectedFile();
        }
        return null;
    }
    
    private void rcButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rcButtonActionPerformed
    {//GEN-HEADEREND:event_rcButtonActionPerformed
        // open file dialog
        File command = getExeFile(rcField.getText());
        if (command != null)
        {
            rcField.setText(command.getAbsolutePath());
        }
    }//GEN-LAST:event_rcButtonActionPerformed

    private void vcButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_vcButtonActionPerformed
    {//GEN-HEADEREND:event_vcButtonActionPerformed
        // open file dialog
        File command = getExeFile(vcField.getText());
        if (command != null)
        {
            vcField.setText(command.getAbsolutePath());
        }
    }//GEN-LAST:event_vcButtonActionPerformed

    private void cmButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmButtonActionPerformed
    {//GEN-HEADEREND:event_cmButtonActionPerformed
        // open file dialog
        File command = getExeFile(cmField.getText());
        if (command != null)
        {
            cmField.setText(command.getAbsolutePath());
        }
    }//GEN-LAST:event_cmButtonActionPerformed

    private void testTypeComboItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_testTypeComboItemStateChanged
    {//GEN-HEADEREND:event_testTypeComboItemStateChanged
        // Save or load the test type
        if (evt.getItem() instanceof TestType)
        {
            TestType type = (TestType)evt.getItem();

            if (evt.getStateChange() == ItemEvent.DESELECTED)
            {
                saveRevisionParameters(type);
            }
            else
            {
                initRevisionParameters(type);
            }
        }
    }//GEN-LAST:event_testTypeComboItemStateChanged

    private void gOkButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_gOkButtonActionPerformed
    {//GEN-HEADEREND:event_gOkButtonActionPerformed
        if (saveConfig())
        {
            setVisible(false);
        }
    }//GEN-LAST:event_gOkButtonActionPerformed

    private void rOkButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rOkButtonActionPerformed
    {//GEN-HEADEREND:event_rOkButtonActionPerformed
        if (saveConfig())
        {
            setVisible(false);
        }
    }//GEN-LAST:event_rOkButtonActionPerformed
    
    protected void initRevisionParameters(TestType type)
    {        
        if (config == null) return; // it is null during intialisation
        minNumPassesField.setText(
            Integer.toString(config.getLearntPassCount(type)));
        initialRevPeriodField.setText(
            Long.toString(config.getInitialRevisionPeriod(type) / MS_IN_DAY));
        shortTermPeriodField.setText(
            Long.toString(config.getShortTermPeriod(type) / MS_IN_DAY));
        shortTermRevPeriodField.setText(
            Long.toString(config.getShortTermRevisionPeriod(type) / MS_IN_DAY));
        minTimeBetweenTestsField.setText(
            Long.toString(config.getMinRetestPeriod(type) / MS_IN_HOUR));
        longTermPeriodField.setText(
            Long.toString(config.getLongTermPeriod(type) / MS_IN_DAY));
        longTermRevPeriodField.setText(
            Long.toString(config.getLongTermRevisionPeriod(type) / MS_IN_DAY));
    }
    
    protected boolean saveConfig()
    {
        if (!userNameField.getText().equals(config.getUserName()))
        {
            if (!config.rename(userNameField.getText()))
            {
                JOptionPane.showMessageDialog(this,
                    "Check that the new user name does not already exist.\n" +
                    "You also need write permission for:\n" +
                    UserConfig.generateConfigPath(userNameField.getText()),                    
                    "Rename Failure",
                    JOptionPane.WARNING_MESSAGE);
                userNameField.setText(config.getUserName());
                return false;
            }
            else
            {
                this.setTitle(config.getUserName() + "'s User Config");
            }
        }
        // save external programs
        Preferences packagePref = 
                Preferences.userNodeForPackage(this.getClass());
        packagePref.put(SystemHandler.CHAR_MAP, cmField.getText());
        packagePref.put(SystemHandler.PLAY_MIXER, vcField.getText());
        packagePref.put(SystemHandler.REC_MIXER, rcField.getText());
        if (mf != null && mf.getLineControl() != null && 
            mf.getLineControl().getPlayMixer() != null &&
            mf.getLineControl().getRecMixer() != null )
        {
            Object m = mf.getLineControl().getPlayMixers().get(playMixerCombo.getSelectedIndex());
            if (m instanceof Mixer.Info && 
                m != mf.getLineControl().getPlayMixer().getMixerInfo())
                mf.getLineControl().setPlayMixer((Mixer.Info)m);
            m = mf.getLineControl().getRecMixers().get(recMixerCombo.getSelectedIndex());
            if (m instanceof Mixer.Info && 
                m != mf.getLineControl().getRecMixer().getMixerInfo())
                mf.getLineControl().setRecMixer((Mixer.Info)m);
        }
        else System.out.println("Error setting mixers");
        switch (recFormatCombo.getSelectedIndex())
        {
            case 0:
                UserConfig.getCurrent().setDefaultSoundExtension(WAV_EXT);
                break;
            case 1:
                UserConfig.getCurrent().setDefaultSoundExtension(MP3_EXT);
                break;
            case 2:
                UserConfig.getCurrent().setDefaultSoundExtension(OGG_EXT);
                break;
        }
        TestType type = TestType.getById(testTypeCombo.getSelectedIndex());
        saveRevisionParameters(type);
        return langPanel.checkLocale();
    }
    
    protected void saveRevisionParameters(TestType type)
    {        
        if (config == null) return; // it is null during intialisation
        config.setLearntPassCount(type,parseInt(minNumPassesField));
        config.setInitialRevisionPeriod(type, parseInt(initialRevPeriodField)
            * MS_IN_DAY);
        config.setShortTermPeriod(type, parseInt(shortTermPeriodField)
            * MS_IN_DAY);
        config.setShortTermRevisionPeriod(type, parseInt(shortTermRevPeriodField)
            * MS_IN_DAY);
        config.setMinRetestPeriod(type, parseInt(minTimeBetweenTestsField)
            * MS_IN_HOUR);
        config.setLongTermPeriod(type, parseInt(longTermPeriodField)
            * MS_IN_DAY);
        config.setLongTermRevisionPeriod(type, parseInt(longTermRevPeriodField)
            * MS_IN_DAY);
    }
    
    protected int parseInt(JTextField field)
    {
        int i = 1; // need default
        try
        {
            i = Integer.parseInt(field.getText());
        }
        catch (NumberFormatException nfe)
        {
             i = 1;
             System.out.println(nfe.getMessage());
        }
        return i;
    }
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_closeDialog
        if (LanguageConfig.getCurrent() != null)
        {
            setVisible(false);
            dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(this, 
                "You must specify a valid language configuration",
                "User Config",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        new UserConfigDialog(new javax.swing.JFrame(), true).setVisible(true);
    }
    
    protected class IntegerInputVerifier extends javax.swing.InputVerifier
    {
        private JComponent lastMsgComponent = null;
        public boolean shouldYieldFocus(javax.swing.JComponent input) 
        {
            return check(input);
        }
                
        public boolean verify(javax.swing.JComponent input)
        {
            boolean ok = check(input);
            
            return ok;
        }
        protected boolean check(javax.swing.JComponent input)
        {
            try
            {
                if (JTextField.class.isInstance(input))
                {
                    int i = Integer.parseInt(((JTextField)input).getText());
                    if (i > 0)
                    {
                        lastMsgComponent = null;
                        return true;
                    }
                }
                else
                {
                    lastMsgComponent = null;
                    return true;
                }
            }
            catch (NumberFormatException nfe)
            {
                // fall through
            }
            if (input != lastMsgComponent) 
            {
                lastMsgComponent = input;
                final JComponent component = input;
                SwingUtilities.invokeLater(new Runnable() 
                {
                    public void run()
                    {
                        JOptionPane.showMessageDialog(component, 
                            "Please Enter a Positive Integer");
                    }
                });
            }
            return false;
        }
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton audioOkButton;
    private javax.swing.JPanel audioPanel;
    private javax.swing.JLabel charMapLabel;
    private javax.swing.JButton cmButton;
    private javax.swing.JTextField cmField;
    private javax.swing.JLabel configPath;
    private javax.swing.JButton epOkButton;
    private javax.swing.JButton gOkButton;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JTextField initialRevPeriodField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField longTermPeriodField;
    private javax.swing.JTextField longTermRevPeriodField;
    private javax.swing.JTextField minNumPassesField;
    private javax.swing.JTextField minTimeBetweenTestsField;
    private javax.swing.JComboBox playMixerCombo;
    private javax.swing.JLabel playMixerLabel;
    private javax.swing.JButton rOkButton;
    private javax.swing.JButton rcButton;
    private javax.swing.JTextField rcField;
    private javax.swing.JComboBox recFormatCombo;
    private javax.swing.JLabel recFormatLabel;
    private javax.swing.JLabel recLabel;
    private javax.swing.JComboBox recMixerCombo;
    private javax.swing.JLabel recMixerLabel;
    private javax.swing.JPanel revisionPanel;
    private javax.swing.JTextField shortTermPeriodField;
    private javax.swing.JTextField shortTermRevPeriodField;
    private javax.swing.JPanel systemPanel;
    private javax.swing.JComboBox testTypeCombo;
    private javax.swing.JTextField userNameField;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JButton vcButton;
    private javax.swing.JTextField vcField;
    private javax.swing.JLabel volLabel;
    // End of variables declaration//GEN-END:variables
    
    class ExeFileFilter extends FileFilter
    {
        boolean isWindows = false;
        SecurityManager sm = null;
        public ExeFileFilter()
        {
            if (System.getProperty("os.name").indexOf("Windows")>-1)
                isWindows = true;
            sm = System.getSecurityManager();
        }
        public boolean accept(File f)
        {
            if (isWindows)       
            {
                if (f.getName().toLowerCase().endsWith(WINDOWS_EXE)) 
                     return true;
            }
            try
            {
                if (f.isDirectory()) return true;
                if (sm != null)
                {
                    sm.checkExec(f.getAbsolutePath());
                    return true;
                }
                else
                {
                    if (f.isFile() && f.canRead()) return true;
                }
            }
            catch (SecurityException e)
            {
                System.out.println(e.getMessage());
            }
            return false;
        }
        public String getDescription() { return "Executable Files"; }
    }
}
