/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/TestItemEditorPanel.java,v $
 *  Version:       $Revision: 1.10 $
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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.text.DefaultEditorKit;
import javax.swing.border.TitledBorder;
import javax.sound.sampled.AudioSystem;


import org.jfree.ui.FontChooserDialog;
import languagetest.language.test.TestItem;
import languagetest.language.test.TestComponent;
import languagetest.language.test.TestModule;
import languagetest.language.test.LanguageConfig;
import languagetest.language.test.UniversalLanguage;
import languagetest.sound.AudioPlayer;
import languagetest.sound.Recorder;
/**
 *
 * @author  keith
 */
public class TestItemEditorPanel extends javax.swing.JPanel 
    implements EditorSubPanel, javax.swing.event.DocumentListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7010060844657094747L;
	private static long ADJUST_TIME = 250; // ms
    private boolean changing = false;
    private MainEditorPanel mainEditorPanel = null;
    private MainFrame mainFrame = null;
    private TestModule currentModule = null;
    private TestItem currentTestItem = null;
    private AudioPlayer audio = null;
    private File audioPath = null;
    private AudioProgressListener audioListener = null;
    //private JPanel returnPanel = null;
    private DecimalFormat df2 = null;
    private DecimalFormat df3 = null;
    private DateFormat dateFormat = null;
    private TestItemStats itemStats = null;
    private File picturePath = null;
    private ImageScaler imageScaler = null;
    private boolean recording = false;
    private Recorder recorder = null;
    private Timer recordTimer = null;
    private TextActionHandler nativeHandler = null;
    private TextActionHandler foreignHandler = null;
    private TextActionHandler activeActionHandler = null;
    private JPopupMenu nativePopup = null;
    private JPopupMenu foreignPopup = null;
    private UniversalLanguage nativeLang = null;
    private UniversalLanguage foreignLang = null;
    private boolean nameChanged = false;
    private final javax.swing.ImageIcon recordIcon = 
            new javax.swing.ImageIcon(getClass()
            .getResource("/languagetest/sound/icons/record.png"));
    private final javax.swing.ImageIcon recordLockedIcon = 
            new javax.swing.ImageIcon(getClass()
            .getResource("/languagetest/sound/icons/recordLock.png"));
    /** Creates new form ModuleEditorPanel */
    public TestItemEditorPanel(MainEditorPanel mainEditorPanel) 
    {
        this.mainEditorPanel = mainEditorPanel;
        this.mainFrame = mainEditorPanel.getMainFrame();
        initComponents();
        df2 = new DecimalFormat("00");
        df3 = new DecimalFormat("000");
        dateFormat = DateFormat.getDateTimeInstance();
        audio = mainFrame.getAudio();
        audioListener = new AudioProgressListener(audio, playProgressBar, false);
        audio.addPlayListener(audioListener);
        recorder = new Recorder(mainFrame.getLineControl());
        recorder.addPlayListener(audioListener);
        final TestItemEditorPanel theEditor = this;
        recordTimer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                theEditor.checkRecorderStatus();
            };
        });
        imageScaler = new ImageScaler(this.pictureButton, this.picturePane);
        nativeHandler = new TextActionHandler(this.nativeText);
        foreignHandler = new TextActionHandler(this.foreignText);
        nativePopup = new JPopupMenu();
        foreignPopup = new JPopupMenu();
        nativeText.getDocument().addDocumentListener(this);
        foreignText.getDocument().addDocumentListener(this);
        
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()//GEN-BEGIN:initComponents
    {
        audioPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        audioFileField = new javax.swing.JTextField();
        audioFileButton = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        markStartButton = new javax.swing.JButton();
        decrementStartButton = new javax.swing.JButton();
        startField = new javax.swing.JTextField();
        incrementStartButton = new javax.swing.JButton();
        decrementEndButton = new javax.swing.JButton();
        endField = new javax.swing.JTextField();
        incrementEndButton = new javax.swing.JButton();
        markEndButton = new javax.swing.JButton();
        audioControlPanel = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        recordButton = new javax.swing.JButton();
        playProgressBar = new javax.swing.JProgressBar();
        catScrollPane = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        itemLabel = new javax.swing.JLabel();
        speaker = new javax.swing.JComboBox();
        moveUp = new javax.swing.JButton();
        moveDown = new javax.swing.JButton();
        userStats = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSplitPaneH = new javax.swing.JSplitPane();
        jSplitPaneV = new javax.swing.JSplitPane();
        nativePanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        nativeText = new javax.swing.JTextPane();
        foreignPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        foreignText = new javax.swing.JTextPane();
        picturePane = new javax.swing.JScrollPane();
        pictureButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setBorder(new javax.swing.border.TitledBorder("Test Item"));
        audioPanel.setLayout(new javax.swing.BoxLayout(audioPanel, javax.swing.BoxLayout.Y_AXIS));

        audioPanel.setBorder(new javax.swing.border.TitledBorder("Audio clip"));
        audioPanel.setEnabled(false);
        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.X_AXIS));

        jPanel9.setMaximumSize(new java.awt.Dimension(2147483647, 93));
        audioFileField.setToolTipText("File of audio clip");
        audioFileField.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                audioFileFieldFocusLost(evt);
            }
        });
        audioFileField.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                audioFileFieldKeyTyped(evt);
            }
        });

        jPanel9.add(audioFileField);

        audioFileButton.setText("Browse...");
        audioFileButton.setToolTipText("Browse for audio file");
        audioFileButton.setEnabled(false);
        audioFileButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                audioFileButtonActionPerformed(evt);
            }
        });

        jPanel9.add(audioFileButton);

        audioPanel.add(jPanel9);

        jPanel10.setLayout(new java.awt.GridLayout(1, 0));

        markStartButton.setText("Start");
        markStartButton.setToolTipText("Set start to current play position.");
        markStartButton.setEnabled(false);
        markStartButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                markStartButtonActionPerformed(evt);
            }
        });

        jPanel10.add(markStartButton);

        decrementStartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/sound/icons/rewind.png")));
        decrementStartButton.setToolTipText("Move start back");
        decrementStartButton.setEnabled(false);
        decrementStartButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                decrementStartButtonActionPerformed(evt);
            }
        });

        jPanel10.add(decrementStartButton);

        startField.setText("0:00.0");
        startField.setEnabled(false);
        startField.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                startFieldFocusLost(evt);
            }
        });

        jPanel10.add(startField);

        incrementStartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/sound/icons/fastforward.png")));
        incrementStartButton.setToolTipText("Move start forward");
        incrementStartButton.setEnabled(false);
        incrementStartButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                incrementStartButtonActionPerformed(evt);
            }
        });

        jPanel10.add(incrementStartButton);

        decrementEndButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/sound/icons/rewind.png")));
        decrementEndButton.setToolTipText("Move end back");
        decrementEndButton.setEnabled(false);
        decrementEndButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                decrementEndButtonActionPerformed(evt);
            }
        });

        jPanel10.add(decrementEndButton);

        endField.setEnabled(false);
        endField.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                endFieldFocusLost(evt);
            }
        });

        jPanel10.add(endField);

        incrementEndButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/sound/icons/fastforward.png")));
        incrementEndButton.setToolTipText("Move end forward");
        incrementEndButton.setEnabled(false);
        incrementEndButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                incrementEndButtonActionPerformed(evt);
            }
        });

        jPanel10.add(incrementEndButton);

        markEndButton.setText("End");
        markEndButton.setToolTipText("Set end to current play position.");
        markEndButton.setEnabled(false);
        markEndButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                markEndButtonActionPerformed(evt);
            }
        });

        jPanel10.add(markEndButton);

        audioPanel.add(jPanel10);

        audioControlPanel.setLayout(new java.awt.GridLayout(1, 0));

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/sound/icons/play.png")));
        playButton.setToolTipText("Play audio clip");
        playButton.setEnabled(false);
        playButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                playButtonActionPerformed(evt);
            }
        });

        audioControlPanel.add(playButton);

        pauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/sound/icons/pause.png")));
        pauseButton.setToolTipText("Pause playback/recording at current position");
        pauseButton.setEnabled(false);
        pauseButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pauseButtonActionPerformed(evt);
            }
        });

        audioControlPanel.add(pauseButton);

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/sound/icons/stop.png")));
        stopButton.setToolTipText("Stop playback/recording and rewind");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                stopButtonActionPerformed(evt);
            }
        });

        audioControlPanel.add(stopButton);

        recordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/languagetest/sound/icons/recordLock.png")));
        recordButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                recordButtonActionPerformed(evt);
            }
        });

        audioControlPanel.add(recordButton);

        audioPanel.add(audioControlPanel);

        audioPanel.add(playProgressBar);

        add(audioPanel, java.awt.BorderLayout.SOUTH);

        add(catScrollPane, java.awt.BorderLayout.EAST);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        itemLabel.setText("First Created: ? Last Edited: ?");
        jPanel1.add(itemLabel);

        jPanel1.add(speaker);

        moveUp.setText("Move Up");
        moveUp.setToolTipText("Move Phrase Up in Conversation Sequence");
        moveUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                moveUpActionPerformed(evt);
            }
        });

        jPanel1.add(moveUp);

        moveDown.setText("Move Down");
        moveDown.setToolTipText("Move phrase down in conversation sequence");
        moveDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                moveDownActionPerformed(evt);
            }
        });

        jPanel1.add(moveDown);

        userStats.setText("User Stats");
        userStats.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                userStatsActionPerformed(evt);
            }
        });

        jPanel1.add(userStats);

        add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jSplitPaneH.setDividerLocation(450);
        jSplitPaneH.setDividerSize(5);
        jSplitPaneH.setResizeWeight(0.5);
        jSplitPaneV.setDividerSize(5);
        jSplitPaneV.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPaneV.setResizeWeight(0.5);
        jSplitPaneV.setContinuousLayout(true);
        nativePanel.setLayout(new java.awt.BorderLayout());

        nativePanel.setBorder(new javax.swing.border.TitledBorder("English"));
        nativePanel.setToolTipText("Right Click to change language");
        nativePanel.setEnabled(false);
        nativePanel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                nativePanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                nativePanelMouseReleased(evt);
            }
        });

        nativeText.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                nativeTextFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                nativeTextFocusLost(evt);
            }
        });

        jScrollPane2.setViewportView(nativeText);

        nativePanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPaneV.setLeftComponent(nativePanel);

        foreignPanel.setLayout(new java.awt.BorderLayout());

        foreignPanel.setBorder(new javax.swing.border.TitledBorder("Burmese"));
        foreignPanel.setToolTipText("Right Click to change language");
        foreignPanel.setEnabled(false);
        foreignPanel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                foreignPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                foreignPanelMouseReleased(evt);
            }
        });

        foreignText.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                foreignTextFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                foreignTextFocusLost(evt);
            }
        });

        jScrollPane3.setViewportView(foreignText);

        foreignPanel.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jSplitPaneV.setRightComponent(foreignPanel);

        jSplitPaneH.setLeftComponent(jSplitPaneV);

        picturePane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        picturePane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        picturePane.addComponentListener(new java.awt.event.ComponentAdapter()
        {
            public void componentResized(java.awt.event.ComponentEvent evt)
            {
                picturePaneComponentResized(evt);
            }
        });

        pictureButton.setText("Add picture");
        pictureButton.setToolTipText("Click to change picture");
        pictureButton.setBorder(null);
        pictureButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pictureButtonActionPerformed(evt);
            }
        });

        picturePane.setViewportView(pictureButton);

        jSplitPaneH.setRightComponent(picturePane);

        jPanel2.add(jSplitPaneH, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void recordButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_recordButtonActionPerformed
    {//GEN-HEADEREND:event_recordButtonActionPerformed
        // Add your handling code here:
        
        if (recording)
        {
            recorder.start();
            
            recordButton.setIcon(recordIcon);
        }
        else
        {
            // stop existing audio play
            audio.stop();
            audio.close();
            if (currentTestItem.getSoundFile() == null)
            {
                // prime file dialog with a reasonable suggestion
                audioPath = TestModuleUtilities.createSoundForTestItem(currentTestItem);
                // may need to create directories
                if ((!audioPath.getParentFile().exists() &&
                    audioPath.getParentFile().mkdirs() == false)||
                    audioPath.getParentFile().isFile())
                {
                    audioPath = null;
                }
                // choose a file
                chooseFile(false);
            }
            // no point proceeding if file not writable
            if (currentTestItem.getSoundFile() == null ||
                (currentTestItem.getSoundFile().exists() &&
                 !currentTestItem.getSoundFile().canWrite()))
            {
                JOptionPane.showMessageDialog(this,
                    "You must choose a sound file which can be written to.");
                currentTestItem.setSoundFile(null);
                audioFileField.setText("");
                recording = false;
                //recordButton.setSelected(false);
                recordButton.setIcon(recordLockedIcon);
            }
            else if (currentTestItem.getSoundFile().exists())
            {
                if (JOptionPane.showConfirmDialog(this, 
                    "Do you want to overwrite the existing sound file?") ==
                    JOptionPane.YES_OPTION)
                {
                    initRecorder();
                    recordButton.setIcon(recordIcon);
                }
                else
                {
                    recording = false;
                    //recordButton.setSelected(false);
                    recordButton.setIcon(recordLockedIcon);
                }
            }
            else // file doesn't exist so OK to proceed
            {
                initRecorder();
                recordButton.setIcon(recordIcon);
            }
        }
    }//GEN-LAST:event_recordButtonActionPerformed

    private void nativePanelMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_nativePanelMouseReleased
    {//GEN-HEADEREND:event_nativePanelMouseReleased
        showPopupIfTriggered(evt, nativePopup);
    }//GEN-LAST:event_nativePanelMouseReleased

    private void nativePanelMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_nativePanelMousePressed
    {//GEN-HEADEREND:event_nativePanelMousePressed
        showPopupIfTriggered(evt, nativePopup);
    }//GEN-LAST:event_nativePanelMousePressed

    private void foreignPanelMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_foreignPanelMouseReleased
    {//GEN-HEADEREND:event_foreignPanelMouseReleased
        showPopupIfTriggered(evt, foreignPopup);
    }//GEN-LAST:event_foreignPanelMouseReleased

    private void foreignPanelMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_foreignPanelMousePressed
    {//GEN-HEADEREND:event_foreignPanelMousePressed
        showPopupIfTriggered(evt, foreignPopup);
    }//GEN-LAST:event_foreignPanelMousePressed

    protected void showPopupIfTriggered(MouseEvent evt, JPopupMenu popup)
    {
        if (evt.isPopupTrigger())
        {
            popup.show(evt.getComponent(),
                       evt.getX(), evt.getY());
        }
    }
    
    private void foreignTextFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_foreignTextFocusGained
    {//GEN-HEADEREND:event_foreignTextFocusGained
        activeActionHandler = foreignHandler;
    }//GEN-LAST:event_foreignTextFocusGained

    private void nativeTextFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_nativeTextFocusGained
    {//GEN-HEADEREND:event_nativeTextFocusGained
        activeActionHandler = nativeHandler;
    }//GEN-LAST:event_nativeTextFocusGained

    
    
    protected void initRecorder()
    {
        recording = true;
        mainEditorPanel.setMsgText("Openning recorder...");
        disableAudioPlayer();
        // record status needs to be changed from disable
        
        recordButton.setIcon(recordIcon);
        recordButton.setEnabled(false);
        //recordButton.setSelected(true);
        recordTimer.start();
        // check file type is valid
        if (!recorder.initialise(currentTestItem.getSoundFile()))
        {
            JOptionPane.showMessageDialog(this,
                 "Initialisation error\n" + recorder.getErrorDescription(), 
                 "Recorder", JOptionPane.WARNING_MESSAGE);
            recording = false;
            //recordButton.setSelected(false);
            recordButton.setEnabled(true);  
            recordButton.setIcon(recordLockedIcon);
            
            mainEditorPanel.setMsgText("");
        }
        
    }
    
    private void picturePaneComponentResized(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_picturePaneComponentResized
    {//GEN-HEADEREND:event_picturePaneComponentResized
        // Add your handling code here:
    }//GEN-LAST:event_picturePaneComponentResized

    private void pictureButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pictureButtonActionPerformed
    {//GEN-HEADEREND:event_pictureButtonActionPerformed
        // Add your handling code here:
        JFileChooser chooser = new JFileChooser();
        FileFilter filter = new FileFilter() 
        {
            public boolean accept(File f)
            {
                if (f.getName().toLowerCase().endsWith(".png")) return true;
                if (f.getName().toLowerCase().endsWith(".jpg")) return true;
                if (f.getName().toLowerCase().endsWith(".jpeg")) return true;
                if (f.getName().toLowerCase().endsWith(".gif")) return true;
                if (f.isDirectory()) return true;
                return false;
            }
            public String getDescription() { return "Image Files (*.png; *.jpg; *.jpeg; *.gif)"; }
        };
        chooser.setFileFilter(filter);
        // open in last directory browsed to or directory of current file
        if (picturePath == null)
        {
            if (currentTestItem.getPictureFile() != null &&
                currentTestItem.getPictureFile().exists())
            {
                picturePath = currentTestItem.getPictureFile();
            }
            else
            {
                picturePath = currentModule.getFile().getParentFile();
            }
        }        
        // Note a null input into setCurrentDirectory just uses default dir
        chooser.setCurrentDirectory(picturePath);
        // default to previous file
        if (picturePath.isFile())
        {
            chooser.setSelectedFile(picturePath);
        }
        int returnVal = chooser.showOpenDialog(this);
        try
        {
            if(returnVal == JFileChooser.APPROVE_OPTION) 
            {
                
                currentTestItem.setPictureFile(chooser.getSelectedFile());
                imageScaler.setPicture(currentTestItem.getPictureFile());
                pictureButton.setText(null);
                
            }
            else
            {
                // give option to remove existing picture if user cancels
                if (currentTestItem.getPictureFile() != null &&
                    JOptionPane.showConfirmDialog(this,
                    "Do you want to remove the existing picture", "Choose Picture",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                {
                    imageScaler.setPicture(null);
                    pictureButton.setText("Add Picture");
                    currentTestItem.setPictureFile(null);
                }
            }
        }
        catch (MalformedURLException mue)
        {
            System.out.println(mue.getMessage());
        }
        catch (IOException ioe)
        {
            // tbd should we warn user explicitly?
            System.out.println(ioe.getMessage());
        }
    }//GEN-LAST:event_pictureButtonActionPerformed


    
    private void userStatsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_userStatsActionPerformed
    {//GEN-HEADEREND:event_userStatsActionPerformed
        // Add your handling code here:
        if (itemStats == null)
        {
            itemStats = new TestItemStats(mainFrame, false);
        }
        itemStats.initialise(currentTestItem);
        itemStats.setVisible(true);
    }//GEN-LAST:event_userStatsActionPerformed

    private void moveUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_moveUpActionPerformed
    {//GEN-HEADEREND:event_moveUpActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_moveUpActionPerformed

    private void moveDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_moveDownActionPerformed
    {//GEN-HEADEREND:event_moveDownActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_moveDownActionPerformed

    private void foreignTextFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_foreignTextFocusLost
    {//GEN-HEADEREND:event_foreignTextFocusLost
        // Add your handling code here:
        //saveTestItemChanges();
        
    }//GEN-LAST:event_foreignTextFocusLost

    private void nativeTextFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_nativeTextFocusLost
    {//GEN-HEADEREND:event_nativeTextFocusLost
        // Add your handling code here:
        //saveTestItemChanges();
        if (nameChanged)
        {
            TestComponent oldComponent = mainEditorPanel.getSelected();
            if (currentModule != null)
            {
                mainFrame.getTreeModel().reload(currentModule.getTreeNode());
                if (oldComponent != null)
                {
                    mainEditorPanel.setSelection(oldComponent);
                }
            }            
            nameChanged = false;
        }
    }//GEN-LAST:event_nativeTextFocusLost

    private void audioFileFieldKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_audioFileFieldKeyTyped
    {//GEN-HEADEREND:event_audioFileFieldKeyTyped
        // Add your handling code here:
        saveTestItemChanges();
        audioFileChanged();

    }//GEN-LAST:event_audioFileFieldKeyTyped

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
                int returnVal = chooser.showOpenDialog(this);
                if(returnVal == JFileChooser.APPROVE_OPTION) 
                {
                    module.setFile(chooser.getSelectedFile());
                }
            }
            else
            {
                // user said no so give up trying to save
                break;
            }
        }
    }
    
    private void audioFileButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_audioFileButtonActionPerformed
    {//GEN-HEADEREND:event_audioFileButtonActionPerformed
        chooseFile(true);
    }//GEN-LAST:event_audioFileButtonActionPerformed

    public File chooseAudioFile(File initialFile, String title, boolean openDialog)
    {
        File chosenFile = null;
        JFileChooser chooser = new JFileChooser();
        if (!openDialog) chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        FileFilter filter = new FileFilter() 
        {
            public boolean accept(File f)
            {
                if (f.getName().toLowerCase().endsWith(".wav")) return true;
                if (f.getName().toLowerCase().endsWith(".mp3")) return true;
                if (f.getName().toLowerCase().endsWith(".ogg")) return true;
                if (f.isDirectory()) return true;
                return false;
            }
            public String getDescription() { return "Sound Files (*.wav; *.mp3; *.ogg)"; }
        };
        chooser.setFileFilter(filter);
        // Note a null input into setCurrentDirectory just uses default dir
        chooser.setCurrentDirectory(initialFile);
        // default to previous file
        if (!initialFile.exists() || initialFile.isFile())
        {
            chooser.setSelectedFile(audioPath);
        }
        chooser.setDialogTitle(title);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            chosenFile = chooser.getSelectedFile();
        }
        return chosenFile;
    }
    
    protected void chooseFile(boolean openDialog)
    {
            // Add your handling code here:
        
        // open in last directory browsed to or directory of current file
        if (audioPath == null)
        {
            if (currentTestItem.getSoundFile() != null &&
                currentTestItem.getSoundFile().exists())
            {
                audioPath = currentTestItem.getSoundFile();
            }
            else
            {
                audioPath = currentModule.getFile().getParentFile();
            }
        }        
        
        File newFile = chooseAudioFile(audioPath, "Select Audio File", 
            openDialog);
        if (newFile != null)
        {
            currentTestItem.setSoundFile(newFile);
            audioFileField.setText(currentTestItem.getSoundFile()
                                   .getAbsolutePath());
            audioPath = newFile;
            audioFileChanged();
        }
    }
    
    private void audioFileFieldFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_audioFileFieldFocusLost
    {//GEN-HEADEREND:event_audioFileFieldFocusLost
        // Add your handling code here:
        saveTestItemChanges();
        audioFileChanged();
    }//GEN-LAST:event_audioFileFieldFocusLost

    protected void audioFileChanged()
    {
        if (currentTestItem.getSoundFile() != null &&
            currentTestItem.getSoundFile().exists())
        {
            // if file start/end are still at default values try to set so
            // more intelligent value, otherwise leave as is
            if (currentTestItem.getPlayStart() == TestItem.DEFAULT_PLAY_START && 
                currentTestItem.getPlayEnd() == TestItem.DEFAULT_PLAY_END)
            {
                // if file is already loaded chances are that a sensible guess
                // at the next audio position is at the end of the previous clip
                if (currentTestItem.getSoundFile().equals(audio.getCurrentFile()))
                {
                    long newStart = audio.getStartMs() + audio.getDurationMs();
                    startField.setText(milliSec2Min(newStart));
                    endField.setText("");
                    audio.setBounds(newStart, TestItem.DEFAULT_PLAY_END);
                }
                else
                {
                    audio.open(currentTestItem.getSoundFile(), 
                               currentTestItem.getSoundLength(), false);
                    startField.setText("00:00.00");
                    endField.setText("");
                    audio.setBounds(TestItem.DEFAULT_PLAY_START, 
                                    TestItem.DEFAULT_PLAY_END);
                }
            }
            else
            {
                // use existing bounds
                audio.open(currentTestItem.getSoundFile(), 
                           currentTestItem.getSoundLength(), false);
                audio.setBounds(currentTestItem.getPlayStart(),
                                currentTestItem.getPlayEnd() 
                                - currentTestItem.getPlayStart());
            }
            enableAudioPlayer();
        }
        else
        {
            disableAudioPlayer();
        }        
    }
    
    private void playButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_playButtonActionPerformed
    {//GEN-HEADEREND:event_playButtonActionPerformed
        audioListener.checkProgress();
        if (recording)
        {
            
        }
        else
        {
            audio.play();
        }
    }//GEN-LAST:event_playButtonActionPerformed

    
    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pauseButtonActionPerformed
    {//GEN-HEADEREND:event_pauseButtonActionPerformed
        // Add your handling code here:
        audioListener.checkProgress();
        if (recording)
        {
            recorder.stop();
        }
        else
        {
            audio.pause();
        }
    }//GEN-LAST:event_pauseButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_stopButtonActionPerformed
    {//GEN-HEADEREND:event_stopButtonActionPerformed
        // Add your handling code here:
        audioListener.checkProgress();
        if (recording)
        {
            recorder.stop();
            recordTimer.start();
            recorder.finish();
            //recordButton.setSelected(false);
            recordButton.setIcon(recordLockedIcon);
            recording = false;
            disableAudioPlayer();
            //recordButton.setEnabled(false);
        }
        else
        {
            audio.stop();
        }
    }//GEN-LAST:event_stopButtonActionPerformed

    private void markEndButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_markEndButtonActionPerformed
    {//GEN-HEADEREND:event_markEndButtonActionPerformed
        // Add your handling code here:
        endField.setText(milliSec2Min(audioListener.getMsPosition()));
        setPlayWindow(0, 0);
        saveTestItemChanges();
    }//GEN-LAST:event_markEndButtonActionPerformed

    private void incrementEndButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_incrementEndButtonActionPerformed
    {//GEN-HEADEREND:event_incrementEndButtonActionPerformed
        // Add your handling code here:
        setPlayWindow(0, ADJUST_TIME);
        saveTestItemChanges();
    }//GEN-LAST:event_incrementEndButtonActionPerformed

    private void decrementEndButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_decrementEndButtonActionPerformed
    {//GEN-HEADEREND:event_decrementEndButtonActionPerformed
        // Add your handling code here:
        setPlayWindow(0, - ADJUST_TIME);
        saveTestItemChanges();
    }//GEN-LAST:event_decrementEndButtonActionPerformed

    private void incrementStartButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_incrementStartButtonActionPerformed
    {//GEN-HEADEREND:event_incrementStartButtonActionPerformed
        // Add your handling code here:
        setPlayWindow(ADJUST_TIME, 0);
        saveTestItemChanges();
    }//GEN-LAST:event_incrementStartButtonActionPerformed

    private void endFieldFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_endFieldFocusLost
    {//GEN-HEADEREND:event_endFieldFocusLost
        // Add your handling code here:
        setPlayWindow(0, 0);
        saveTestItemChanges();
    }//GEN-LAST:event_endFieldFocusLost

    private void startFieldFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_startFieldFocusLost
    {//GEN-HEADEREND:event_startFieldFocusLost
        // Add your handling code here:
        setPlayWindow(0, 0);
        saveTestItemChanges();
    }//GEN-LAST:event_startFieldFocusLost

    private void decrementStartButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_decrementStartButtonActionPerformed
    {//GEN-HEADEREND:event_decrementStartButtonActionPerformed
        // Add your handling code here:
        setPlayWindow(- ADJUST_TIME, 0);
        saveTestItemChanges();
        
    }//GEN-LAST:event_decrementStartButtonActionPerformed

    private void markStartButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_markStartButtonActionPerformed
    {//GEN-HEADEREND:event_markStartButtonActionPerformed
        // Add your handling code here:
        startField.setText(milliSec2Min(audioListener.getMsPosition()));
        setPlayWindow(0, 0);
        saveTestItemChanges();
    }//GEN-LAST:event_markStartButtonActionPerformed

	protected Font chooseFont(Font currentFont, String prompt, JLabel label)
	{
		Font newFont = null;
		FontChooserDialog chooser = 
			new FontChooserDialog(mainFrame, prompt, true, currentFont);
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

    
    
    protected void setPlayWindow(long deltaStart, long deltaEnd)
    {
        long newStart = min2MilliSec(startField.getText()) + deltaStart;
        long newEnd = min2MilliSec(endField.getText()) + deltaEnd;
        // check bounds
        if (newStart < 0) newStart = 0;
        if (newEnd < newStart && newEnd > 0) newEnd = newStart + ADJUST_TIME;
        endField.setText(milliSec2Min(newEnd));
        startField.setText(milliSec2Min(newStart));
        audio.setBounds(newStart, newEnd - newStart);
    }
    
    protected void clearItemEntries()
    {
        audioFileField.setText("");
        foreignText.setText("");
        nativeText.setText("");
        startField.setText("");
        endField.setText("");
        audioFileField.setEnabled(false);
        audioFileButton.setEnabled(false);
        foreignText.setEnabled(false);
        nativeText.setEnabled(false);
        //deleteTestItemButton.setEnabled(false);
        foreignPanel.setEnabled(false);
        nativePanel.setEnabled(false);
        audioPanel.setEnabled(false);
        disableAudioPlayer();
        
        playProgressBar.setValue(0);
        audio.stop();
        currentTestItem = null;
    }
    
    
    
    protected void setItemEntries(TestItem item)
    {
        changing = true;
        if (item.getSoundFile() == null)
        {
            audioFileField.setText("");
            disableAudioPlayer();
        }
        else 
        {
            try
            {
                audioFileField.setText(item.getSoundFile().getCanonicalPath());
                if (item.getSoundFile().exists())
                {
                    audio.open(item.getSoundFile(), 
                               item.getSoundLength(), false);
                    audio.setBounds(item.getPlayStart(), 
                                    item.getPlayEnd() - item.getPlayStart());
                    enableAudioPlayer();
                }
                else
                {
                    disableAudioPlayer();
                }
            }
            catch (IOException e)
            {
                System.out.println(e);
                audioFileField.setText("");
            }
        }
        try
        {
            if (item.getPictureFile()!=null)
            {
            
                imageScaler.setPicture(item.getPictureFile());
                pictureButton.setText(null);
            }
            else
            {
                pictureButton.setIcon(null);
                imageScaler.setPicture(null);
                pictureButton.setText("Add Picture");
            }   
        }
        catch (MalformedURLException mue)
        {
            System.out.println(mue.getMessage());
        }
        catch (IOException ioe)
        {
            // tbd should we warn user explicitly?
            System.out.println(ioe.getMessage());
        }
        
        playProgressBar.setValue(0);
        foreignText.setText(item.getForeignText(foreignLang));
        nativeText.setText(item.getNativeText(nativeLang));
        changeBorderTitle(nativePanel,nativeLang.getDescription());        
        changeBorderTitle(foreignPanel,foreignLang.getDescription());
        
        startField.setText(milliSec2Min(item.getPlayStart()));
        endField.setText(milliSec2Min(item.getPlayEnd()));
        currentTestItem = item;
        audioFileField.setEnabled(true);
        audioFileButton.setEnabled(true);
        foreignText.setEnabled(true);
        nativeText.setEnabled(true);
        foreignPanel.setEnabled(true);
        nativePanel.setEnabled(true);
        audioPanel.setEnabled(true);
        //deleteTestItemButton.setEnabled(true);
        changing = false;
    }
    
    protected void enableAudioPlayer()
    {
        playButton.setEnabled(true);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
        recordButton.setEnabled(true);
        markStartButton.setEnabled(true);
        markEndButton.setEnabled(true);
        startField.setEnabled(true);
        endField.setEnabled(true);
        decrementStartButton.setEnabled(true);
        incrementStartButton.setEnabled(true);
        decrementEndButton.setEnabled(true);
        incrementEndButton.setEnabled(true);
        recordButton.setSelected(false);
        recordButton.setEnabled(true);
    }
    
    protected void disableAudioPlayer()
    {
        playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        recordButton.setEnabled(false);
        markStartButton.setEnabled(false);
        markEndButton.setEnabled(false);
        startField.setEnabled(false);
        endField.setEnabled(false);
        decrementStartButton.setEnabled(false);
        incrementStartButton.setEnabled(false);
        decrementEndButton.setEnabled(false);
        incrementEndButton.setEnabled(false);
        recordButton.setSelected(false);
        recordButton.setEnabled(true);
        playProgressBar.setValue(0); // clear progress bar
    }

    protected void enableRecorder()
    {
        recordButton.setSelected(true);
        recordButton.setEnabled(true);
        markStartButton.setEnabled(false);
        markEndButton.setEnabled(false);
        startField.setEnabled(false);
        endField.setEnabled(false);
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
        decrementStartButton.setEnabled(false);
        incrementStartButton.setEnabled(false);
        decrementEndButton.setEnabled(false);
        incrementEndButton.setEnabled(false);
    }
    
    protected void saveTestItemChanges()
    {
        if (currentTestItem != null && currentModule != null)
        {
            // integrity check
            //assert (currentModule == currentTestItem.getModule());
            
            String oldName = currentTestItem.getName();
            
            currentTestItem.setNative(nativeText.getText(), nativeLang);
            currentTestItem.setForeign(foreignText.getText(), foreignLang);
            if (audioFileField.getText().length() > 0)
            {
                File newSoundFile = new File(audioFileField.getText());
                if (!newSoundFile.equals(currentTestItem.getSoundFile()))
                {
                    currentTestItem.setSoundFile(newSoundFile);
                    // file has changed so we no longer know the length for sure
                    currentTestItem.setSoundLength(AudioSystem.NOT_SPECIFIED);
                }
                currentTestItem.setPlayStart(min2MilliSec(startField.getText()));
                currentTestItem.setPlayEnd(min2MilliSec(endField.getText()));
            }
            else
            {
                currentTestItem.setSoundFile(null);
                currentTestItem.setSoundLength(AudioSystem.NOT_SPECIFIED);
            }
            
            
            // check for name change
            if (currentTestItem.getName().equals(oldName) == false)
            {
                nameChanged = true;
                /*
                // since the tree is changed currentTestItem may become reset
                // therefore store a local copy of the item
                TestItem changingItem = currentTestItem;
                TestModule changingModule = currentModule;
                
                // record whether item is selected (it may have just lost 
                // selection triggering this save)
                boolean wasSelected = mainEditorPanel.isSelected(changingItem);
                
                mainFrame.getTreeModel().reload(changingModule.getTreeNode());
                // restore selection if necessary
                if (wasSelected)
                {
                    mainEditorPanel.setSelectionToItem(changingItem);
                }
                 */
            }
             
        }
    }
    
    public void close()
    {
        audio.close();
        recorder.close();
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
    
    protected String milliSec2Min(long ms)
    {
        String min = "";
        if (ms < 0) return min;
        long minutes = ms / 60000;
        long msRem = ms - minutes * 60000;
        long sec = msRem / 1000;
        msRem = msRem - sec * 1000;
        min = Long.toString(minutes) + ":" + df2.format(sec) + 
            "." + df3.format(msRem);
        return min;
    }
    
    public void commitChanges()
    {
        saveTestItemChanges();
        // stop recording if it is in progress
        if (recording)
        {
            recorder.stop();
            recorder.finish();
            recording = false;
        }
    }    
    /**
     * Method to initialise panel to show a new item.
     */
    public void show(TestItem item)
    {
        if (item == null)
        {
            // this is a way to force a reload next time show is called with
            // a real item
            currentTestItem = null;
        }
        else if (item != currentTestItem)
        {
            currentTestItem = item;
            currentModule = item.getModule();
            // choose a suitable language
            if (!currentModule.getNativeLanguages().contains(nativeLang))
            {
                if (currentModule.getNativeLanguages()
                    .contains(LanguageConfig.getCurrent().getNativeLanguage()))
                {
                    nativeLang = LanguageConfig.getCurrent().getNativeLanguage();
                }
                else
                {
                    nativeLang = (UniversalLanguage)
                        currentModule.getNativeLanguages().iterator().next();
                }
            }
            
            if (!currentModule.getForeignLanguages().contains(foreignLang))
            {
                if (currentModule.getForeignLanguages()
                    .contains(LanguageConfig.getCurrent().getForeignLanguage()))
                {
                    foreignLang = LanguageConfig.getCurrent().getForeignLanguage();
                }
                else
                {
                    foreignLang = (UniversalLanguage)
                        currentModule.getForeignLanguages().iterator().next();
                }
            }
            
            nativeText.setFont(item.getModule().getNativeFont(nativeLang));
            foreignText.setFont(item.getModule().getForeignFont(foreignLang));
            String itemDescription = 
                "Created: " + 
                dateFormat.format(new Date(item.getCreationTime()))
                + " By: " + item.getCreator();
            itemLabel.setText(itemDescription);
            itemLabel.setToolTipText(itemDescription);
            // initialise text contents
            setItemEntries(item);
            if (item.getConversation() == null)
            {
                moveUp.setVisible(false);
                moveDown.setVisible(false);
                speaker.setVisible(false);
            }
            else
            {
                moveUp.setVisible(true);
                moveDown.setVisible(true);
                speaker.setVisible(true);
            }
            // make sure item is selected
            mainEditorPanel.setSelectionToItem(item);
            // reset the actionHandlers 
            nativeHandler.reset();
            foreignHandler.reset();
            // initialise stats if visible
            if (itemStats != null) itemStats.initialise(item);    
            // create popup menus
            nativePopup.removeAll();
            Iterator n = currentModule.getNativeLanguages().iterator();
            while (n.hasNext())
            {
                UniversalLanguage ul = (UniversalLanguage)n.next();
                JMenuItem mi = nativePopup.add(ul.toString());
                mi.addActionListener(new LanguageActionListener(ul,true));
            }
            foreignPopup.removeAll();
            Iterator f = currentModule.getForeignLanguages().iterator();
            while (f.hasNext())
            {
                UniversalLanguage ul = (UniversalLanguage)f.next();
                JMenuItem mi = foreignPopup.add(ul.toString());
                mi.addActionListener(new LanguageActionListener(ul,false));
            }
        }
    }
    
    /**
     * Method called by record timer to monitor recorder startup or close 
     * status
     */
    public void checkRecorderStatus()
    {
        if (recording) // waiting for open
        {
            if (recorder.isInitialised())
            {
                mainEditorPanel.setMsgText("");
                enableRecorder();
                recordTimer.stop();
            }
            // if there was an error with the recorder exit recoring state
            // and display error message
            else if (recorder.isRecordError())
            {
                mainEditorPanel.setMsgText("");
                JOptionPane.showMessageDialog(this, 
                    "There was an error recording\n" + 
                    recorder.getErrorDescription(), "Record Error",
                    JOptionPane.WARNING_MESSAGE);
                recording = false;
                disableAudioPlayer();
                recordTimer.stop();
            }
        }
        else // waiting for stop
        {
            if (recorder.isFinished())
            {
                mainEditorPanel.setMsgText("");
                recordTimer.stop();
                if (recorder.isRecordError())
                {
                    mainEditorPanel.setMsgText("");
                    JOptionPane.showMessageDialog(this, 
                        "There was an error recording\n" + 
                        recorder.getErrorDescription(), "Record Error",
                        JOptionPane.WARNING_MESSAGE);
                    disableAudioPlayer();
                }
                else
                {
                    // force reopen of file
                    audio.open(currentTestItem.getSoundFile(), 
                               recorder.getRecordingMsLength(), true);
                    currentTestItem.setPlayStart(0);
                    currentTestItem.setPlayEnd(-1);
                    currentTestItem.setSoundLength(recorder.getRecordingMsLength());
                    audio.setBounds(currentTestItem.getPlayStart(), 
                                    currentTestItem.getPlayEnd()
                                    - currentTestItem.getPlayStart());
                    startField.setText(
                        milliSec2Min(currentTestItem.getPlayStart()));
                    endField.setText(
                        milliSec2Min(currentTestItem.getPlayEnd()));
                    enableAudioPlayer();
                }
            }
        }
    }
    
    public void copy(ActionEvent evt)
    {
        if (activeActionHandler != null)
        {
            activeActionHandler.getActionByName(DefaultEditorKit.copyAction)
                .actionPerformed(evt);
        }
    }
    
    public void paste(ActionEvent evt)
    {
        if (activeActionHandler != null)
        {
            activeActionHandler.getActionByName(DefaultEditorKit.pasteAction)
                .actionPerformed(evt);
        }
    }
    
    public void cut(ActionEvent evt)
    {
        if (activeActionHandler != null)
        {
            activeActionHandler.getActionByName(DefaultEditorKit.cutAction)
                .actionPerformed(evt);
        }
    }
    
    public void redo(ActionEvent evt)
    {
        if (activeActionHandler != null)
        {
            activeActionHandler.getActionByName(TextActionHandler.REDO)
                .actionPerformed(evt);
        }
    }
    
    public void undo(ActionEvent evt)
    {
        if (activeActionHandler != null)
        {
            activeActionHandler.getActionByName(TextActionHandler.UNDO)
                .actionPerformed(evt);
        }
    }
    
    protected class LanguageActionListener implements ActionListener
    {
        boolean isNative = true;
        UniversalLanguage language = null;
        
        LanguageActionListener(UniversalLanguage ul, boolean isNative)
        {
            this.language = ul;
            this.isNative = isNative;
        }
        
        public void actionPerformed(ActionEvent e)
        {
            if (isNative)
            {
                // save old value
                currentTestItem.setNative(nativeText.getText(),nativeLang);
                // set new language
                nativeText.setText(currentTestItem.getNativeText(language));
                nativeText.setFont(currentModule.getNativeFont(language));
                changeBorderTitle(nativePanel,language.getDescription());                
                nativePanel.invalidate();
                nativePanel.getParent().validate();
                nativeLang = language;
            }
            else
            {
                // save old value
                currentTestItem.setForeign(foreignText.getText(),foreignLang);
                // set new language
                foreignText.setText(currentTestItem.getForeignText(language));
                foreignText.setFont(currentModule.getForeignFont(language));
                changeBorderTitle(foreignPanel,language.getDescription());      
                foreignPanel.getParent().validate();
                foreignLang = language;
            }
        }
        
    }
    
    protected void changeBorderTitle(JPanel panel, String title)
    {
        TitledBorder border = (TitledBorder)panel.getBorder();
        border.setTitle(title);
        panel.setBorder(border);
        panel.getParent().repaint();
    }
    
    public void changedUpdate(javax.swing.event.DocumentEvent e)
    {
        if (!changing) saveTestItemChanges();
    }
    
    public void insertUpdate(javax.swing.event.DocumentEvent e)
    {
        if (!changing) saveTestItemChanges();
    }
    
    public void removeUpdate(javax.swing.event.DocumentEvent e)
    {
        if (!changing) saveTestItemChanges();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel audioControlPanel;
    private javax.swing.JButton audioFileButton;
    private javax.swing.JTextField audioFileField;
    private javax.swing.JPanel audioPanel;
    private javax.swing.JScrollPane catScrollPane;
    private javax.swing.JButton decrementEndButton;
    private javax.swing.JButton decrementStartButton;
    private javax.swing.JTextField endField;
    private javax.swing.JPanel foreignPanel;
    private javax.swing.JTextPane foreignText;
    private javax.swing.JButton incrementEndButton;
    private javax.swing.JButton incrementStartButton;
    private javax.swing.JLabel itemLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPaneH;
    private javax.swing.JSplitPane jSplitPaneV;
    private javax.swing.JButton markEndButton;
    private javax.swing.JButton markStartButton;
    private javax.swing.JButton moveDown;
    private javax.swing.JButton moveUp;
    private javax.swing.JPanel nativePanel;
    private javax.swing.JTextPane nativeText;
    private javax.swing.JButton pauseButton;
    private javax.swing.JButton pictureButton;
    private javax.swing.JScrollPane picturePane;
    private javax.swing.JButton playButton;
    private javax.swing.JProgressBar playProgressBar;
    private javax.swing.JButton recordButton;
    private javax.swing.JComboBox speaker;
    private javax.swing.JTextField startField;
    private javax.swing.JButton stopButton;
    private javax.swing.JButton userStats;
    // End of variables declaration//GEN-END:variables
    
}
