/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/TestPanel.java,v $
 *  Version:       $Revision: 1.13 $
 *  Last Modified: $Date: 2005/03/25 04:49:26 $
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

import java.util.Enumeration;
import java.io.File;
import java.io.StringReader;
import java.io.IOException;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.StyleSheet;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLDocument;
import languagetest.language.test.UserConfig;
import languagetest.language.test.TestHistoryStorageException;
import languagetest.language.test.TestItem;
import languagetest.language.test.TestType;
import languagetest.language.test.LanguageConfig;
import languagetest.language.test.Test;
import languagetest.sound.AudioPlayer;
/**
 *
 * @author  keith
 */
public class TestPanel extends javax.swing.JPanel implements ActionListener
{
    private static final int MAX_FLIP_REPEATS = 99;
    private static final int MAX_FLIP_TIMEOUT = 60; // secs
    private MainFrame mainFrame = null;
    private Test test = null;
    private AudioPlayer clipPlayer = null;
    private AudioProgressListener audioListener = null;
    private static final long STEP_SIZE = 1000; // 1sec
    private TestItem item = null;
    private String foreignAnswer = null;
    private String nativeAnswer = null;
    private boolean soundFileOpen = false;
    private ImageScaler imageScaler = null;
    private int picDividerLocation = -1;
    private LanguageConfig lc = null;
    private boolean hideForeign = true;
    private boolean hideNative = true;
    private double fudge = 0.7;
    private Timer timer = null;
    private HTMLEditorKit hek = null;
    private int timeElapsed = 0;
    private int flipTimeout = 10;
    private Pattern ampPattern = null;
    private Pattern ltPattern = null;
    private Pattern gtPattern = null;
    private Pattern nlPattern = null;
    /** Creates new form TestPanel */
    public TestPanel(MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
        hek = new HTMLEditorKit();
        foreignAnswer = new String("");
        nativeAnswer = new String("");
        initComponents();
        clipPlayer = mainFrame.getAudio();
        audioListener = new AudioProgressListener(clipPlayer, playProgress, true);
        clipPlayer.addPlayListener(audioListener);
        imageScaler = new ImageScaler(pictureLabel, picturePane);
        picDividerLocation = hSplitPane.getDividerLocation();
        lc = LanguageConfig.getCurrent();
        // set a default paragraph style
        /*
        Style def = StyleContext.getDefaultStyleContext().
                                        getStyle(StyleContext.DEFAULT_STYLE);
        Style centre = nativeText.addStyle("centre", def);
        StyleConstants.setAlignment(centre,StyleConstants.ALIGN_CENTER);
        nativeText.setLogicalStyle(centre);
        centre = foreignText.addStyle("centre", def);
        StyleConstants.setAlignment(centre,StyleConstants.ALIGN_CENTER);
        foreignText.setLogicalStyle(centre);
         */
        ampPattern = Pattern.compile("&",Pattern.MULTILINE);
        ltPattern = Pattern.compile("<", Pattern.MULTILINE);
        gtPattern = Pattern.compile(">", Pattern.MULTILINE);
        nlPattern = Pattern.compile("\n", Pattern.MULTILINE);
    }
    
    public Test getTest() { return test; }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()//GEN-BEGIN:initComponents
    {
        controlPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        showAnswerButton = new javax.swing.JToggleButton();
        markPanel = new javax.swing.JPanel();
        correctButton = new javax.swing.JButton();
        wrongButton = new javax.swing.JButton();
        timeoutPanel = new javax.swing.JPanel();
        timeOutSpinner = new javax.swing.JSpinner();
        repeatsPanel = new javax.swing.JPanel();
        repeatSpinner = new javax.swing.JSpinner();
        playPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        pauseButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        playProgress = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        progressText = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        editButton = new javax.swing.JButton();
        ignoreButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        hSplitPane = new javax.swing.JSplitPane();
        vSplitPane = new javax.swing.JSplitPane();
        nativePanel = new javax.swing.JPanel();
        nativeScroll = new javax.swing.JScrollPane();
        nativeText = new javax.swing.JTextPane();
        foreignPanel = new javax.swing.JPanel();
        foreignScroll = new javax.swing.JScrollPane();
        foreignText = new javax.swing.JTextPane();
        picturePane = new javax.swing.JScrollPane();
        pictureLabel = new javax.swing.JLabel();
        titlePanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        controlPanel.setLayout(new javax.swing.BoxLayout(controlPanel, javax.swing.BoxLayout.Y_AXIS));

        controlPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        controlPanel.setMaximumSize(new java.awt.Dimension(140, 1200));
        controlPanel.setPreferredSize(new java.awt.Dimension(130, 400));
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.Y_AXIS));

        showAnswerButton.setText("Show Answer");
        showAnswerButton.setMinimumSize(new java.awt.Dimension(130, 25));
        showAnswerButton.setPreferredSize(new java.awt.Dimension(130, 25));
        showAnswerButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                showAnswerButtonActionPerformed(evt);
            }
        });

        jPanel8.add(showAnswerButton);

        controlPanel.add(jPanel8);

        markPanel.setLayout(new java.awt.GridLayout(2, 1));

        markPanel.setBorder(new javax.swing.border.TitledBorder("Mark yourself:"));
        markPanel.setAlignmentX(0.0F);
        markPanel.setMaximumSize(new java.awt.Dimension(130, 89));
        correctButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/language/icons/correct.png")));
        correctButton.setText("Correct");
        correctButton.setRolloverEnabled(false);
        correctButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                correctButtonActionPerformed(evt);
            }
        });

        markPanel.add(correctButton);

        wrongButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/language/icons/wrong.png")));
        wrongButton.setText("Wrong");
        wrongButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                wrongButtonActionPerformed(evt);
            }
        });

        markPanel.add(wrongButton);

        controlPanel.add(markPanel);

        timeoutPanel.setLayout(new java.awt.BorderLayout());

        timeoutPanel.setBorder(new javax.swing.border.TitledBorder("Timeout (s)"));
        timeoutPanel.setAlignmentX(0.0F);
        timeoutPanel.setMaximumSize(new java.awt.Dimension(130, 45));
        timeoutPanel.setPreferredSize(new java.awt.Dimension(120, 45));
        timeoutPanel.add(timeOutSpinner, java.awt.BorderLayout.CENTER);

        controlPanel.add(timeoutPanel);

        repeatsPanel.setLayout(new java.awt.BorderLayout());

        repeatsPanel.setBorder(new javax.swing.border.TitledBorder("Max repeats"));
        repeatsPanel.setAlignmentX(0.0F);
        repeatsPanel.setMaximumSize(new java.awt.Dimension(130, 45));
        repeatsPanel.setPreferredSize(new java.awt.Dimension(120, 45));
        repeatsPanel.add(repeatSpinner, java.awt.BorderLayout.CENTER);

        controlPanel.add(repeatsPanel);

        playPanel.setLayout(new java.awt.BorderLayout());

        playPanel.setBorder(new javax.swing.border.TitledBorder("Play Sample"));
        playPanel.setAlignmentX(0.0F);
        playPanel.setMaximumSize(new java.awt.Dimension(130, 65));
        playPanel.setPreferredSize(new java.awt.Dimension(120, 65));
        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        jPanel6.setToolTipText("");
        jPanel6.setMaximumSize(new java.awt.Dimension(120, 20));
        jPanel6.setMinimumSize(new java.awt.Dimension(60, 16));
        jPanel6.setPreferredSize(new java.awt.Dimension(100, 20));
        pauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/sound/icons/pauseMini.png")));
        pauseButton.setToolTipText("Pause");
        pauseButton.setMinimumSize(new java.awt.Dimension(16, 16));
        pauseButton.setOpaque(false);
        pauseButton.setPreferredSize(new java.awt.Dimension(16, 16));
        pauseButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pauseButtonActionPerformed(evt);
            }
        });

        jPanel6.add(pauseButton);

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/sound/icons/playMini.png")));
        playButton.setToolTipText("Play");
        playButton.setMinimumSize(new java.awt.Dimension(16, 16));
        playButton.setPreferredSize(new java.awt.Dimension(16, 16));
        playButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                playButtonActionPerformed(evt);
            }
        });

        jPanel6.add(playButton);

        stopButton.setFont(new java.awt.Font("Dialog", 0, 12));
        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/sound/icons/stopMini.png")));
        stopButton.setToolTipText("Stop");
        stopButton.setMaximumSize(new java.awt.Dimension(20, 20));
        stopButton.setMinimumSize(new java.awt.Dimension(16, 16));
        stopButton.setPreferredSize(new java.awt.Dimension(16, 16));
        stopButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                stopButtonActionPerformed(evt);
            }
        });

        jPanel6.add(stopButton);

        playPanel.add(jPanel6, java.awt.BorderLayout.CENTER);

        playProgress.setPreferredSize(new java.awt.Dimension(110, 14));
        playPanel.add(playProgress, java.awt.BorderLayout.SOUTH);

        controlPanel.add(playPanel);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(new javax.swing.border.TitledBorder("Test Status"));
        jPanel3.setAlignmentX(0.0F);
        jPanel3.setMaximumSize(new java.awt.Dimension(130, 400));
        jPanel3.setPreferredSize(new java.awt.Dimension(130, 100));
        progressText.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        progressText.setEditable(false);
        progressText.setRows(5);
        progressText.setTabSize(4);
        progressText.setWrapStyleWord(true);
        progressText.setAlignmentX(0.0F);
        jPanel3.add(progressText, java.awt.BorderLayout.CENTER);

        controlPanel.add(jPanel3);

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.Y_AXIS));

        editButton.setText("Edit Item");
        editButton.setToolTipText("Edit Current Test Item");
        editButton.setMaximumSize(new java.awt.Dimension(130, 25));
        editButton.setPreferredSize(new java.awt.Dimension(130, 25));
        editButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editButtonActionPerformed(evt);
            }
        });

        jPanel9.add(editButton);

        ignoreButton.setText("Ignore Item");
        ignoreButton.setToolTipText("Remove the item from this test and subsequent revision tests. You can re-enable an item in the Test Editor using the User Stats dialog.");
        ignoreButton.setMaximumSize(new java.awt.Dimension(130, 25));
        ignoreButton.setOpaque(false);
        ignoreButton.setPreferredSize(new java.awt.Dimension(130, 25));
        ignoreButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ignoreButtonActionPerformed(evt);
            }
        });

        jPanel9.add(ignoreButton);

        jLabel1.setText(" ");
        jPanel9.add(jLabel1);

        cancelButton.setText("Stop test");
        cancelButton.setToolTipText("Abort test. Existing results will still be saved.");
        cancelButton.setMaximumSize(new java.awt.Dimension(130, 25));
        cancelButton.setPreferredSize(new java.awt.Dimension(130, 25));
        cancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelButtonActionPerformed(evt);
            }
        });

        jPanel9.add(cancelButton);

        controlPanel.add(jPanel9);

        add(controlPanel, java.awt.BorderLayout.EAST);

        jPanel2.setLayout(new java.awt.BorderLayout());

        hSplitPane.setDividerLocation(400);
        hSplitPane.setDividerSize(5);
        hSplitPane.setResizeWeight(0.5);
        vSplitPane.setDividerSize(5);
        vSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        vSplitPane.setResizeWeight(0.5);
        vSplitPane.addComponentListener(new java.awt.event.ComponentAdapter()
        {
            public void componentResized(java.awt.event.ComponentEvent evt)
            {
                vSplitPaneComponentResized(evt);
            }
        });

        nativePanel.setLayout(new javax.swing.BoxLayout(nativePanel, javax.swing.BoxLayout.Y_AXIS));

        nativePanel.setBorder(new javax.swing.border.TitledBorder("English"));
        nativePanel.addComponentListener(new java.awt.event.ComponentAdapter()
        {
            public void componentResized(java.awt.event.ComponentEvent evt)
            {
                nativePanelComponentResized(evt);
            }
        });

        nativeScroll.setViewportView(nativeText);

        nativePanel.add(nativeScroll);

        vSplitPane.setTopComponent(nativePanel);

        foreignPanel.setLayout(new javax.swing.BoxLayout(foreignPanel, javax.swing.BoxLayout.Y_AXIS));

        foreignPanel.setBorder(new javax.swing.border.TitledBorder("Burmese"));
        foreignPanel.addComponentListener(new java.awt.event.ComponentAdapter()
        {
            public void componentResized(java.awt.event.ComponentEvent evt)
            {
                foreignPanelComponentResized(evt);
            }
        });

        foreignScroll.setViewportView(foreignText);

        foreignPanel.add(foreignScroll);

        vSplitPane.setBottomComponent(foreignPanel);

        hSplitPane.setLeftComponent(vSplitPane);

        picturePane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        picturePane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        picturePane.setMinimumSize(new java.awt.Dimension(0, 0));
        pictureLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pictureLabel.setAlignmentX(0.5F);
        pictureLabel.setIconTextGap(0);
        picturePane.setViewportView(pictureLabel);

        hSplitPane.setRightComponent(picturePane);

        jPanel2.add(hSplitPane, java.awt.BorderLayout.CENTER);

        titlePanel.setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeTitleBackground"));
        titlePanel.setBorder(new javax.swing.border.EtchedBorder());
        titleLabel.setBackground(new java.awt.Color(102, 204, 255));
        titleLabel.setFont(new java.awt.Font("Dialog", 1, 18));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Language Test");
        titlePanel.add(titleLabel);

        jPanel2.add(titlePanel, java.awt.BorderLayout.NORTH);

        add(jPanel2, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void vSplitPaneComponentResized(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_vSplitPaneComponentResized
    {//GEN-HEADEREND:event_vSplitPaneComponentResized
        // if vertial pane is resized redo html tables
        // we don't monitor pane size because redrawing the table may itself
        // cause the pane to resize causing a loop
        if (hideForeign)
        {
            hideForeign();
        }
        else
        {
            displayForeign();
        }
        if (hideNative)
        {
            hideNative();
        }
        else
        {
            displayNative();
        }
    }//GEN-LAST:event_vSplitPaneComponentResized

    private void foreignPanelComponentResized(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_foreignPanelComponentResized
    {//GEN-HEADEREND:event_foreignPanelComponentResized
        
    }//GEN-LAST:event_foreignPanelComponentResized

    private void nativePanelComponentResized(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_nativePanelComponentResized
    {//GEN-HEADEREND:event_nativePanelComponentResized
        
    }//GEN-LAST:event_nativePanelComponentResized

    private void ignoreButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ignoreButtonActionPerformed
    {//GEN-HEADEREND:event_ignoreButtonActionPerformed
        // Add your handling code here:
        try
        {
            test.removeCurrentItem();
            if (test.getType() != TestType.FLIP_CARD)
            {
                UserConfig.getCurrent().getTestHistory()
                    .ignoreItem(item, test.getType(), true);
                
                nextTestItem();
            }
        }
        catch (TestHistoryStorageException thse)
        {
            JOptionPane.showMessageDialog(this, "Failed to set ignore flag:\n" +
                thse.getMessage(), "Test History Error", 
                JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_ignoreButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editButtonActionPerformed
    {//GEN-HEADEREND:event_editButtonActionPerformed
        // Add your handling code here:
        // stop any playing sound, but leave buttons enabled ready for when
        // we resume
        clipPlayer.stop(); 
        if (timer != null)
        {
            timer.stop();
        }
        mainFrame.editModules(item);
    }//GEN-LAST:event_editButtonActionPerformed

    /*
    private void setPlayPosition(long position)
    {
        int value = (int)(position / 1000);
        playProgress.setValue(value);
    }*/
    
    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pauseButtonActionPerformed
    {//GEN-HEADEREND:event_pauseButtonActionPerformed
        // Add your handling code here:
        audioListener.checkProgress();
        clipPlayer.pause();
        
    }//GEN-LAST:event_pauseButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_playButtonActionPerformed
    {//GEN-HEADEREND:event_playButtonActionPerformed
        // Add your handling code here:
        audioListener.checkProgress();
        clipPlayer.play();
    }//GEN-LAST:event_playButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_stopButtonActionPerformed
    {//GEN-HEADEREND:event_stopButtonActionPerformed
        // Add your handling code here:
        audioListener.checkProgress();
        clipPlayer.stop();
    }//GEN-LAST:event_stopButtonActionPerformed

//    private void fastForwardButtonActionPerformed(java.awt.event.ActionEvent evt)                                                  
//    {                                                      
//        // Add your handling code here:
//        audioListener.checkProgress();
//        clipPlayer.fastForward(STEP_SIZE);
//    } 
    
//private void rewindButtonActionPerformed(java.awt.event.ActionEvent evt)                                             
//    {                                                 
//        // Add your handling code here:
//        audioListener.checkProgress();
//        clipPlayer.rewind(STEP_SIZE);
//    }   
    
    
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        // Add your handling code here:
        if (JOptionPane.showConfirmDialog
                (this, "Are you sure you abort the test?", 
                "Confirm Cancel Test",
                JOptionPane.YES_NO_OPTION,
                 JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
        {
            if (timer != null)
            {
                timer.stop();
                timer = null;
                UserConfig.getCurrent().setFlipPeriod
                    (((Number)timeOutSpinner.getValue()).intValue());
                UserConfig.getCurrent().setMaxFlipRepeats
                    (((Number)repeatSpinner.getValue()).intValue());
            }
            mainFrame.finishTest(test);
        }
        
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void wrongButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_wrongButtonActionPerformed
    {//GEN-HEADEREND:event_wrongButtonActionPerformed
        // Add your handling code here:
        test.setPassStatus(false);
        nextTestItem();
    }//GEN-LAST:event_wrongButtonActionPerformed

    private void correctButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_correctButtonActionPerformed
    {//GEN-HEADEREND:event_correctButtonActionPerformed
        // Add your handling code here:
        test.setPassStatus(true);
        nextTestItem();
    }//GEN-LAST:event_correctButtonActionPerformed

    private void showAnswerButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showAnswerButtonActionPerformed
    {//GEN-HEADEREND:event_showAnswerButtonActionPerformed
        // normal mode
        if (test.getType() == TestType.FLIP_CARD)
        {
            if (showAnswerButton.isSelected())
            {
                timer.stop();
            }
            else
            {
                timer.start();
            }                
        }
        else
        {
            if (showAnswerButton.isSelected())
            {
                switch (test.getType().getId())
                {
                    case TestType.LISTENING_FOREIGN_NATIVE_ID:
                        displayForeign();
                        displayNative(); 
                        displayPicture();
                        break;
                    case TestType.READING_FOREIGN_NATIVE_ID:
                        displayNative();   
                        displayPicture();
                        playSound();
                        break;
                    case TestType.READING_NATIVE_FOREIGN_ID:
                        displayForeign();
                        playSound();
                        break;
                }
                correctButton.setEnabled(true);
                wrongButton.setEnabled(true);
            }
            else
            {
                switch (test.getType().getId())
                {
                    case TestType.LISTENING_FOREIGN_NATIVE_ID:
                        hideForeign();
                        hideNative();                    
                        break;
                    case TestType.READING_FOREIGN_NATIVE_ID:
                        hideNative();                       
                        break;
                    case TestType.READING_NATIVE_FOREIGN_ID:
                        hideForeign();                    
                        break;
                }
                correctButton.setEnabled(false);
                wrongButton.setEnabled(false);
            }
        }
    }//GEN-LAST:event_showAnswerButtonActionPerformed

    
    
    public void initialise(Test theTest)
    {
        titleLabel.setText("Language Test for " +
            UserConfig.getCurrent().getUserName()
            );
        this.test = theTest;
        // set titles for test panels
        TitledBorder border = (TitledBorder)nativePanel.getBorder();
        lc = LanguageConfig.getCurrent();
        border.setTitle(lc.getNativeLanguageName());
        border = (TitledBorder)foreignPanel.getBorder();
        border.setTitle(lc.getForeignLanguageName());
        nativePanel.repaint();
        foreignPanel.repaint();
        if (theTest.getType() == TestType.FLIP_CARD)
            
        {
            showAnswerButton.setText("Pause");
            //markLabel.setText("Timeout (s)");
            markPanel.setVisible(false);
            correctButton.setVisible(false);
            wrongButton.setVisible(false);
            timeoutPanel.setVisible(true);
            timeOutSpinner.setVisible(true);
            flipTimeout = UserConfig.getCurrent().getFlipPeriod();
            // bounds check
            if (flipTimeout < 1 || flipTimeout > MAX_FLIP_TIMEOUT) 
            {
                System.out.println("Invalid flip timeout count: " + flipTimeout);
                flipTimeout = 5;// arbitrary
            }
            SpinnerNumberModel nm = 
                new SpinnerNumberModel(flipTimeout,1,MAX_FLIP_TIMEOUT,1);
            timeOutSpinner.setModel(nm);
            repeatsPanel.setVisible(true);
            repeatSpinner.setVisible(true);
            int repeats = UserConfig.getCurrent().getMaxFlipRepeats();
            // bounds check
            if (repeats < 0 || repeats > MAX_FLIP_REPEATS) 
            {
                System.out.println("Invalid flip repeat count: " + repeats);
                repeats = 2; // arbitrary
            }
            SpinnerNumberModel rnm = 
                new SpinnerNumberModel(repeats,0,MAX_FLIP_REPEATS,1);
            repeatSpinner.setModel(rnm);
            test.setMaxRepeats(repeats);
            timer = new Timer(1000, this);
            timer.start();
            timeOutSpinner.addChangeListener(new ChangeListener()
            {
                public void stateChanged(ChangeEvent e)
                {
                    timeOutChanged();
                }
            });
            repeatSpinner.addChangeListener(new ChangeListener()
            {
                public void stateChanged(ChangeEvent e)
                {
                    repeatChanged();
                }
            });
        }
        else
        {
            showAnswerButton.setText("Show Answer");
            markPanel.setVisible(true);
            correctButton.setVisible(true);
            wrongButton.setVisible(true);
            timeoutPanel.setVisible(false);
            timeOutSpinner.setVisible(false);
            repeatsPanel.setVisible(false);
            repeatSpinner.setVisible(false);
        }
        nextTestItem();
    }
    
    protected void timeOutChanged()
    {
        int newDelay = ((Number)timeOutSpinner.getValue()).intValue();
        flipTimeout = newDelay;
        System.out.println("Set delay " + newDelay);        
    }
    
    protected void repeatChanged()
    {
        int newRepeatCount = ((Number)repeatSpinner.getValue()).intValue();
        test.setMaxRepeats(newRepeatCount);
    }
    
    public void reloadTestItem()
    {
        if (hideForeign == false) // value is displayed
        {
            // force redisplay
            displayForeign();
        }
        if (hideNative == false) // value is displayed
        {
            // force redisplay
            displayNative();
            displayPicture();
        }
        if (timer != null && !showAnswerButton.isSelected())            
        {
            timer.start();
        }
        openSoundFile();
    }
    
    protected void nextTestItem()
    {
        nativeAnswer = "";
        foreignAnswer = "";
        hideForeign();
        hideNative();
        item = test.getNextItem();
        disableSound();
        if (item == null) 
        {
            if (timer != null)
            {
                UserConfig.getCurrent().setFlipPeriod
                    (((Number)timeOutSpinner.getValue()).intValue());
                UserConfig.getCurrent().setMaxFlipRepeats
                    (((Number)repeatSpinner.getValue()).intValue());
                timer.stop();
                timer = null;
            }
            // end of test
            mainFrame.finishTest(test);
        }
        else
        {
            openSoundFile();
            switch (test.getType().getId())
            {
                case TestType.LISTENING_FOREIGN_NATIVE_ID:
                    playSound();              
                    audioListener.checkProgress();
                    break;
                case TestType.READING_FOREIGN_NATIVE_ID:
                    displayForeign();   
                    break;
                case TestType.READING_NATIVE_FOREIGN_ID:
                    displayNative();
                    displayPicture();
                    break;
                case TestType.FLIP_CARD_ID:
                    playSound();              
                    audioListener.checkProgress();
                    displayNative();
                    displayForeign();  
                    displayPicture();
                    break;
            }
            correctButton.setEnabled(false);
            wrongButton.setEnabled(false);
            showAnswerButton.setSelected(false);
            if (test.getType() == TestType.FLIP_CARD)
            {
                progressText.setText
                    ("User: " + UserConfig.getCurrent().getUserName() +
                     "\nUndisplayed items: " + test.getUntested() + 
                     " / " + test.getNumTests() +
                     "\nDisplayed: " + test.getTestCount() + 
                     "\nRedisplays: " + test.getNumRetests());
            }
            else
            {
                progressText.setText
                    ("User: " + UserConfig.getCurrent().getUserName() +
                     "\nPassed: " + test.getPassCount() +
                     "/" + test.getNumTests() + 
                     "\nAttempted: " + test.getTestCount() + 
                     "\nRetests: " + test.getNumRetests() +
                     "\nUntested: " + test.getUntested());
            }
        }
    }
    
    
    protected synchronized void openSoundFile()
    {
        System.out.println("TestPanel open sound file");
        File soundFile = item.getSoundFile();
        if (soundFile != null && !test.isAudioDisabled())
        {
            // reset listener progress
            audioListener.initialisationProgress(0);
            soundFileOpen = clipPlayer.open(soundFile);
            if (soundFileOpen)
            {
                clipPlayer.setBounds(item.getPlayStart(), 
                                     item.getPlayEnd() - 
                                     item.getPlayStart());
                System.out.println("TestPanel sound file open");
            }
            else
            {
                System.out.println("Failed to open " + 
                                    soundFile.getAbsolutePath());
                disableSound();
            }
        }
        else
        {
            soundFileOpen = false;
            disableSound();
        }
    }
    
    protected synchronized void playSound()
    {
        // if audio file failed to open then clip player won't have a current
        // file, so this effectively checks that file was openned correctly
        if (item.getSoundFile() != null && soundFileOpen)
        {
            System.out.println("TestPanel play");
            audioListener.checkProgress();
            clipPlayer.play();
            //rewindButton.setEnabled(true);
            playButton.setEnabled(true);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
            //fastForwardButton.setEnabled(true);    
        }
    }
    
    protected void disableSound()
    {
        clipPlayer.stop();
        audioListener.hideInitProgress();
        //rewindButton.setEnabled(false);
        playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        //fastForwardButton.setEnabled(false);    
        playProgress.setValue(0);
    }
    
    protected void hideForeign()
    {
        hideForeign = true;
        //foreignText.setText(foreignAnswer);
        foreignAnswer = "";
        setHtmlText(foreignScroll, foreignText, foreignAnswer, null);
        foreignText.setEditable(true);
    }
    
    protected void hideNative()
    {
        hideNative = true;
        //nativeText.setText(nativeAnswer);
        nativeAnswer = "";
        setHtmlText(nativeScroll, nativeText, nativeAnswer, null);
        nativeText.setEditable(true);
        try
        {
            if (pictureLabel.getIcon() != null)
            {
                imageScaler.setPicture(null);
                picDividerLocation = hSplitPane.getDividerLocation();
            }
            hSplitPane.setDividerLocation(
                hSplitPane.getMaximumDividerLocation());
        }
        catch (IOException ioe)
        {
            System.out.println(ioe.getLocalizedMessage());
        }
    }
    
    protected void displayForeign()
    {
        hideForeign = false;
        foreignAnswer = foreignText.getText();
        Font font = item.getModule().getForeignFont(lc.getForeignLanguage());
        /*
        if (font!=null)
        {
            foreignText.setFont(font);
        }
        foreignText.setText(item.getForeignText(lc.getForeignLanguage()));
         */
        setHtmlText(foreignScroll, foreignText, 
                    item.getForeignText(lc.getForeignLanguage()), font);
        foreignText.setEditable(false);
    }
    
    protected void setHtmlText(JScrollPane scroll, JTextPane pane, 
                               String text, Font font)
    {
        // quote characters that will confuse html
        text = ampPattern.matcher(text).replaceAll("&amp;");
        text = ltPattern.matcher(text).replaceAll("&lt;");
        text = gtPattern.matcher(text).replaceAll("&gt;");
        // fix line breaks
        text = nlPattern.matcher(text).replaceAll("<br>");
        
        // now try to detect legit tags and put them back
        //text.replaceAll("&lt;(/?a-zA-Z+.*)&gt;","<\\1>");
        StringBuffer html = new StringBuffer();
        html.append("<html><head><style>");
        html.append("body {margin:0px; padding:0px; text-align: center;} ");
        html.append("table { margin:0px; padding:0px; border-width:0px;} ");
        html.append("tr { margin:0px; padding:0px; border-width:0px;} ");
        html.append("td { text-align: center; vertical-align: middle; ");
        if (font != null)
        {
            html.append("font-family:");
            html.append(font.getFamily());
            html.append("; font-size: ");
            html.append(font.getSize());
            html.append("pt; ");
        }
        html.append("width:");
        double width = Math.floor((scroll.getWidth() 
            - scroll.getBorder().getBorderInsets(scroll).left
            - scroll.getBorder().getBorderInsets(scroll).right) * fudge);
        html.append(width);
        html.append("px; height:");
        double height = Math.floor((scroll.getHeight() 
            - scroll.getBorder().getBorderInsets(scroll).top
            - scroll.getBorder().getBorderInsets(scroll).bottom) * fudge);
        
        html.append(height);
        html.append("px; }</style></head>\n<body><table cellspacing='0' cellpadding='0'><tr><td>");
        html.append(text);
        html.append("</td></tr></table></body></html>");
        StringReader sr = new StringReader(html.toString());
        //System.out.println(html.toString());
        try
        {
            pane.setEditorKit(hek);
            pane.read(sr, hek.createDefaultDocument());
            /*
            StyleSheet ss = ((HTMLDocument)pane.getDocument()).getStyleSheet();
            Enumeration rules = ss.getStyleNames();
            while (rules.hasMoreElements()) {
                String name = (String) rules.nextElement();
                Style rule = ss.getStyle(name);
                System.out.println(rule.toString());
            }
             */
            // measure current fudge factor !
//            fudge = width / (scroll.getWidth() 
//            - scroll.getBorder().getBorderInsets(scroll).left
//            - scroll.getBorder().getBorderInsets(scroll).right);
//            
//            System.out.println(fudge);
        }
        catch (IOException ioe)
        {
            System.out.println(ioe.getLocalizedMessage());
        }
        sr.close();
    }
    
    protected void displayNative()
    {
        hideNative = false;
        nativeAnswer = nativeText.getText();
        Font font = item.getModule().getNativeFont(lc.getNativeLanguage());
        /*
        if (font != null)
        {
            nativeText.setFont(font);
        }
        nativeText.setText(item.getNativeText(lc.getNativeLanguage()));
         */
        nativeText.setEditable(false);
        setHtmlText(nativeScroll, nativeText, 
                    item.getNativeText(lc.getNativeLanguage()),font);
        
    }
    
    protected void displayPicture()
    {
        try
        {
            
            if (item.getPictureFile() == null)
            {
                hSplitPane.setDividerLocation(hSplitPane.getMaximumDividerLocation());
                imageScaler.setPicture(null);
            }
            else
            {
                if (picDividerLocation > hSplitPane.getMaximumDividerLocation())
                {
                    picDividerLocation = (int)(0.7 * 
                        (double)hSplitPane.getMaximumDividerLocation());
                }
                hSplitPane.setDividerLocation(picDividerLocation);
                imageScaler.setPicture(item.getPictureFile());
             }
        }
        catch (IOException ioe)
        {
            System.out.println(ioe.getLocalizedMessage());
        }
    }
    
    /**
     * Fired by timer during flip card tests
     */
    public void actionPerformed(java.awt.event.ActionEvent e)
    {
        if (++timeElapsed >= flipTimeout)
        {
            // only move on if sound clip has finished
            if (audioListener.getMsPosition() - clipPlayer.getStartMs() <= 0)
            {
                test.setPassStatus(false);
                System.out.println("Next...");
                nextTestItem();
                timeElapsed = 0;
            }
        }
    }    


        
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JButton correctButton;
    private javax.swing.JButton editButton;
    private javax.swing.JPanel foreignPanel;
    private javax.swing.JScrollPane foreignScroll;
    private javax.swing.JTextPane foreignText;
    private javax.swing.JSplitPane hSplitPane;
    private javax.swing.JButton ignoreButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel markPanel;
    private javax.swing.JPanel nativePanel;
    private javax.swing.JScrollPane nativeScroll;
    private javax.swing.JTextPane nativeText;
    private javax.swing.JButton pauseButton;
    private javax.swing.JLabel pictureLabel;
    private javax.swing.JScrollPane picturePane;
    private javax.swing.JButton playButton;
    private javax.swing.JPanel playPanel;
    private javax.swing.JProgressBar playProgress;
    private javax.swing.JTextArea progressText;
    private javax.swing.JSpinner repeatSpinner;
    private javax.swing.JPanel repeatsPanel;
    private javax.swing.JToggleButton showAnswerButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JSpinner timeOutSpinner;
    private javax.swing.JPanel timeoutPanel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JSplitPane vSplitPane;
    private javax.swing.JButton wrongButton;
    // End of variables declaration//GEN-END:variables
    
}
