/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/MainFrame.java,v $
 *  Version:       $Revision: 1.14 $
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

import java.awt.CardLayout;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Vector;
import java.util.TreeMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Date;
import java.text.DateFormat;
import java.util.prefs.Preferences;
import java.util.prefs.PreferenceChangeListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jfree.ui.RefineryUtilities;
import languagetest.language.test.UserConfig;
import languagetest.language.test.TestHistoryStorageException;
import languagetest.language.test.TestItem;
import languagetest.language.test.TestType;
import languagetest.language.test.TestModule;
import languagetest.language.test.LanguageConfig;
import languagetest.language.test.UniversalLanguage;
import languagetest.language.test.Test;
import languagetest.sound.RepeatingPlayer;
import languagetest.sound.AudioPlayer;
import languagetest.sound.LineController;
/**
 *
 * @author  keith
 */
public class MainFrame extends javax.swing.JFrame 
    implements PreferenceChangeListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8309872984125554091L;
	private final String TEST_SELECTION_PANEL = "TestSelectionPanel";
    private final String TEST_PANEL = "TestPanel";
    private final String RESULTS_PANEL = "ResultsPanel";
    private final String EDIT_PANEL = "EditPanel";
    private final String TABLE_PANEL  = "TablePanel";
    private final String LOCK = "Lock";
    private JPanel currentPanel = null;
    private TestSelectionPanel selectionPanel = null;
    private TestPanel testPanel = null;
    private ResultsPanel resultsPanel = null;
    private MainEditorPanel editPanel = null;
    private VocabTablePanel tablePanel = null;
    private CardLayout layout = null;
    private SortedSet modules = null;
    private DefaultMutableTreeNode topTreeNode = null;
    private DefaultTreeModel treeModel = null;
    private long startTime = -1;
    private Preferences prefs = null;
    private AudioPlayer audio = null;
    private Hashtable moduleIdTable = null;
    private SwitchUser switchUser = null;
    //private static LanguageConfig langConfig = null;
    //private static UserConfig userConfig = null;
    private LineController lineControl = null;
    //private FileFilter moduleFileFilter = null;
    /** Creates new form MainFrame */
    public MainFrame(SortedSet modules)
    {
        // try to set the look and feel to the system one, but don't worry
        // if it fails
//        try
//        {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        }
//        catch (ClassNotFoundException e)
//        {
//            System.out.println(e.getMessage());
//        }
//        catch (InstantiationException e)
//        {
//            System.out.println(e.getMessage());
//        }        
//        catch (IllegalAccessException e)
//        {
//            System.out.println(e.getMessage());
//        }
//        catch (UnsupportedLookAndFeelException e)
//        {
//            System.out.println(e.getMessage());
//        }
        this.modules = modules;
        lineControl = new LineController();
        audio = new RepeatingPlayer(lineControl);
        initComponents();
        layout = (CardLayout)getContentPane().getLayout();
        
        // setup tree before creating panels
        topTreeNode = new DefaultMutableTreeNode("Loaded Test Modules");
        treeModel = new DefaultTreeModel(topTreeNode);
        moduleIdTable = new Hashtable();
        Iterator i = modules.iterator();
        while (i.hasNext())
        {
            TestModule module = (TestModule)i.next();
            moduleIdTable.put(new Integer(module.getUniqueId()), module);
            topTreeNode.add(module.getTreeNode());
        }

        
        testPanel = new TestPanel(this);
        selectionPanel = new TestSelectionPanel(this);
        resultsPanel = new ResultsPanel(this);
        editPanel = new MainEditorPanel(this);
        tablePanel = new VocabTablePanel(this);
        
        selectionPanel.setVisible(true);
        getContentPane().add(selectionPanel,TEST_SELECTION_PANEL);
        getContentPane().add(testPanel,TEST_PANEL);
        getContentPane().add(resultsPanel,RESULTS_PANEL);
        getContentPane().add(editPanel,EDIT_PANEL);
        getContentPane().add(tablePanel,TABLE_PANEL);
        layout.show(getContentPane(),TEST_SELECTION_PANEL);
        currentPanel = selectionPanel;
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/languagetest/language/icons/languageTest.png")).getImage());
        
        RenderingHints rHint = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Graphics2D g2d = (Graphics2D)this.getGraphics();
        g2d.addRenderingHints(rHint);
        
        switchUser = new SwitchUser(this, true);
        //switchUser.setSize(300,200);
        
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()//GEN-BEGIN:initComponents
    {

        getContentPane().setLayout(new java.awt.CardLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Language Tester");
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                exitForm(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt)
            {
                formWindowClosed(evt);
            }
        });

        pack();
    }//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        // Add your handling code here:
    }//GEN-LAST:event_formWindowClosed
    
    public void confirmExit()
    {
        String msg = "Are you sure that you want to exit?";
        if (currentPanel == editPanel && modules.size() > 0)
        {
            msg = "Do you want to save the changes to your modules?";
            int ans = JOptionPane.showConfirmDialog
                        (this, msg, 
                        "Confirm Language Tester Exit",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                         JOptionPane.QUESTION_MESSAGE);
            switch (ans)
            {
                case JOptionPane.YES_OPTION:
                    editPanel.saveAllModules();  
                    close();
                    break;
                case JOptionPane.NO_OPTION:
                    close();
                    break;
                default:// do nothing
                    break;
            }
            
        }
        else if (currentPanel == testPanel)
        {
            msg = "Are sure that you want to exit and abort your test?";
            if (JOptionPane.showConfirmDialog
                        (this, msg, 
                        "Confirm Language Tester Exit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)
            {
                resultsPanel.initialise(testPanel.getTest());
                resultsPanel.saveResults();
                close();      
            }
        }
        else 
        {
            int ans = JOptionPane.showConfirmDialog
                (this, msg, 
                "Confirm Language Tester Exit",
                JOptionPane.YES_NO_OPTION,
                 JOptionPane.QUESTION_MESSAGE);
            if (ans == JOptionPane.YES_OPTION)
            {
                if (currentPanel == resultsPanel)
                {
                    resultsPanel.saveResults();
                }
                close();
            }
        }
    }
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_exitForm
        confirmExit();
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        System.getProperties().list(System.out);
        /*
         // List Network interfaces
        try
        {
            for (Enumeration e = NetworkInterface.getNetworkInterfaces(); 
                 e.hasMoreElements() ;) 
            {
                System.out.println(e.nextElement());
            }
        }
        catch (SocketException e)
        {
            System.out.println(e);
        }
        */
        TreeSet modules = new TreeSet();
        for (int i=0; i<args.length; i++)
        {
            File file = new File(args[i]);
            if (file.exists() && file.canRead())
            {
                try
                {
                    TestModule module = new TestModule(file);
                    modules.add(module);
                }
                catch (java.io.FileNotFoundException e)
                {
                    System.out.println(e);
                }
                catch (TestModule.ParseException pe)
                {
                    System.out.println(pe);
                }
            }
        }
        
        MainFrame mainFrame = new MainFrame(modules);
        mainFrame.setSize(790,550);
        //mainFrame.setVisible(true);
        mainFrame.chooseUser();
    }
    
    public void chooseUser()
    {
        // choose the user
        if (UserConfig.getCurrent() != null)
        {
            // save existing config
            try
            {
                UserConfig.getCurrent().save();
            }
            catch (TestHistoryStorageException e)
            {
                JOptionPane.showMessageDialog(this, 
                    "There were errors saving the test history:\n" +
                    e.getLocalizedMessage(), "Test History Error",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
        else 
        {
            // this is first time run so check there is not another instance running
            prefs = Preferences.userNodeForPackage(this.getClass());
            startTime = new Date().getTime();
            long lockTime = prefs.getLong(LOCK, startTime);
            if (lockTime != startTime && JOptionPane.showConfirmDialog(this,
                    "Another instance of Language Test appears to be still " +
                    "running or it may have crashed last time.\n" +
                    "Last started at: " + 
                    DateFormat.getInstance().format(new Date(lockTime)) +
                    "\n" +
                    "It is advisable to only run one version of Language Test" +
                    " at a time.\nAre you sure that you want to continue?",
                    "Language Test", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION)
            {
                prefs = null;
                close();
            }            
            else
            {
                prefs.putLong(LOCK,startTime);
                prefs.addPreferenceChangeListener(this);
            }
        }
        RefineryUtilities.centerDialogInParent(switchUser);
        switchUser.setVisibleOnLoad();
        if (UserConfig.getCurrent() != null)
        {
            this.setUserName();
            selectionPanel.setUserName();
            if (LanguageConfig.getCurrent() == null)
            {
                selectionPanel.setConfig();
            }
            selectionPanel.populateLanguageLists();
        }
    }
    
    public void setUserName()
    {
        if (UserConfig.getCurrent() != null)
            this.setTitle("Language Test: " + UserConfig.getCurrent().getUserName());
    }
    
    public void startTest(Test theTest)
    {
        checkAudioStatus();
        testPanel.initialise(theTest);
        layout.show(getContentPane(),TEST_PANEL);
        currentPanel = testPanel;
    }
    
    public void resumeTest()
    {
        testPanel.reloadTestItem();
        layout.show(getContentPane(),TEST_PANEL);
        currentPanel = testPanel;
    }
    
    public void finishTest(Test theTest)
    {
        if (theTest.getType() == TestType.FLIP_CARD)
        {
            layout.show(getContentPane(),TEST_SELECTION_PANEL);
            currentPanel = selectionPanel;
        }
        else
        {
            resultsPanel.initialise(theTest);
            layout.show(getContentPane(),RESULTS_PANEL);
            currentPanel = resultsPanel;
        }
        // close the audio line
        audio.close();
        
    }
    
    public void selectTest()
    {
        layout.show(getContentPane(),TEST_SELECTION_PANEL);
        currentPanel = selectionPanel;
        selectionPanel.initialise();
    }
    
    public void vocabTable()
    {
        checkAudioStatus();
        layout.show(getContentPane(),TABLE_PANEL);
        tablePanel.initialise();
        currentPanel = tablePanel;
    }
    
    
    public void close()
    {
        // save prefs
        SystemHandler.getInstance().closeExternalPrograms();
        if (UserConfig.getCurrent() != null)
        {
            try
            {
                UserConfig.getCurrent().save();
            }
            catch (TestHistoryStorageException e)
            {
                JOptionPane.showMessageDialog(this, 
                    "There were errors saving the test history:\n" +
                    e.getLocalizedMessage(), "Test History Error",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
        if (prefs != null)
        {
            prefs.remove(LOCK);
        }
        System.exit(0);
    }
    
    public void editModules(TestItem itemToEdit)
    {
        checkAudioStatus();
        editPanel.initialise(currentPanel, itemToEdit);
        layout.show(getContentPane(),EDIT_PANEL);
        currentPanel = editPanel;
    }
    
    public SortedSet getModules()
    {
        return modules;
    }
    
    public DefaultTreeModel getTreeModel()
    {
        return treeModel;
    }
    
    public AudioPlayer getAudio()
    {
        return audio;
    }
    
    public TestModule newModule()
    {
        TestModule newModule = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(TestModuleUtilities.moduleFileFilter());
        File currentDir = UserConfig.getCurrent().getModulePath();
        if (currentDir != null && currentDir.exists())
        {
            chooser.setCurrentDirectory(currentDir);
        }
        chooser.setDialogTitle("New Module");
        int returnVal = chooser.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            try
            {
                File newFile = chooser.getSelectedFile();
                if (!newFile.getName()
                    .endsWith(TestModuleUtilities.MODULE_FILE_EXTENSION))
                {
                    newFile = new File(newFile.getAbsolutePath() + 
                                       TestModuleUtilities.MODULE_FILE_EXTENSION);
                }
                newModule = new TestModule(newFile);
                insertModule(newModule);
                TreeMap missingAudioFiles = newModule.getMissingFileList();
                if (!missingAudioFiles.isEmpty())
                {
                    JOptionPane.showMessageDialog
                        (this,
                         missingAudioFiles.keySet().toArray(),
                         "Warning missing audio files:",
                         JOptionPane.WARNING_MESSAGE);
                }
                UserConfig.getCurrent().getRecentFilesList().add(newModule);
            }
            catch (java.io.FileNotFoundException e)
            {
                System.out.println(e);
                JOptionPane.showMessageDialog
                    (this,
                     "There was an error loading the module:\n"
                     + e.getMessage(),
                     "Open New Module:",
                     JOptionPane.WARNING_MESSAGE);
            }
            catch (TestModule.ParseException pe)
            {
                System.out.println(pe);
                JOptionPane.showMessageDialog
                    (this,
                     "There was an error loading the module:\n"
                     + pe.getMessage(),
                     "Open New Module:",
                     JOptionPane.WARNING_MESSAGE);
            }
        }
        return newModule;
    }
    
    
    
    public TestModule loadModule()
    {
        TestModule newModule = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(TestModuleUtilities.moduleFileFilter());
        File currentDir = UserConfig.getCurrent().getModulePath();
        if (currentDir != null && currentDir.exists())
        {
            chooser.setCurrentDirectory(currentDir);
        }
        // allow multiple selections
        chooser.setMultiSelectionEnabled(true);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            UserConfig.getCurrent().setModulePath(chooser.getCurrentDirectory());
            // open each selected file in turn
            File files[] = chooser.getSelectedFiles();
            for (int f = 0; f<files.length; f++)
            {
                try
                {
                    if (files[f].isFile() && files[f].canRead())
                    {
                        newModule = new TestModule(files[f]);
                        insertModule(newModule);
                        
                        TreeMap missingAudioFiles = newModule.getMissingFileList();
                        // if there are missing files, ask the user whether
                        // he/she wants to find them again
                        if (!missingAudioFiles.isEmpty())
                        {

                            if (JOptionPane.showConfirmDialog
                                (this,
                                 "" +  missingAudioFiles.size() + " files were missing. Do you want to try to relocate them now?",
                                 "Missing audio/picture files:",
                                 JOptionPane.YES_NO_OPTION,
                                 JOptionPane.WARNING_MESSAGE) ==
                                 JOptionPane.YES_OPTION)
                            {
                                Iterator missingFiles = missingAudioFiles.keySet().iterator();
                                Iterator items = missingAudioFiles.values().iterator();
                                while (missingFiles.hasNext() && items.hasNext())
                                {

                                    File missing = (File)missingFiles.next();
                                    File newFile = null;
                                    do
                                    {
                                        newFile = editPanel.chooseAudioFile(missing.getParentFile(), 
                                            "Refind: " + missing.getAbsolutePath(), true);
                                        if (newFile == null) break; // user canceled
                                        // check that the name is the same
                                        if (!newFile.getName().equalsIgnoreCase(missing.getName()) &&
                                            JOptionPane.showConfirmDialog
                                                (this,
                                                 "Your new selection:\n" + newFile.getName() + 
                                                 "\nhas a different name to the original:\n" + 
                                                 missing.getName()
                                                 +"\nAre you sure that this is the same file?",
                                                 "Confirm file selection",
                                                 JOptionPane.YES_NO_OPTION,
                                                 JOptionPane.WARNING_MESSAGE) ==
                                                 JOptionPane.NO_OPTION)
                                        {
                                            newFile = null;
                                        }
                                    } while (newFile != null &&
                                             !newFile.exists());
                                    Vector itemList = (Vector)items.next();
                                    if (newFile != null)
                                    {
                                        for (int i = 0; i<itemList.size(); i++)
                                        {
                                            TestItem fixItem = (TestItem)itemList.get(i);
                                            if (fixItem.getSoundFile().equals(missing))
                                            {
                                                fixItem.setSoundFile(newFile);
                                            }
                                            else if (fixItem.getPictureFile().equals(missing))
                                            {
                                                fixItem.setPictureFile(newFile);
                                            }
                                        }
                                    }
                                }
                                // save changes
                                newModule.save();
                            }
                        }
                        UserConfig.getCurrent().getRecentFilesList().add(newModule);
                        // check languages installed
                        checkLanguages(newModule, 
                                       UniversalLanguage.NATIVE_LANG);
                        checkLanguages(newModule,
                                       UniversalLanguage.FOREIGN_LANG);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this,
                            "Unable to read file:\n" +
                            files[f].getAbsolutePath(),"File not found",
                            JOptionPane.WARNING_MESSAGE);
                    }
                }
                catch (java.io.FileNotFoundException e)
                {
                    System.out.println(e);
                    JOptionPane.showMessageDialog
                        (this,
                         "There was an error loading the module:\n"
                         + e.getMessage(),
                         "Open Module:",
                         JOptionPane.WARNING_MESSAGE);
                }
                catch (TestModule.ParseException pe)
                {
                    System.out.println(pe);
                    JOptionPane.showMessageDialog
                        (this,
                         "There was an error loading the module:\n"
                         + pe.getMessage(),
                         "Open Module:",
                         JOptionPane.WARNING_MESSAGE);
                }
            }
            UserConfig.getCurrent().getRecentFilesList().refresh();
        }
        return newModule;
    }
    
    protected void checkLanguages(TestModule module, int langType)
    {
        Iterator i = module.getLanguages(langType).iterator();
        Set userLangs = UserConfig.getCurrent().getLanguages(langType);
        
        while (i.hasNext())
        {
            UniversalLanguage lang = (UniversalLanguage)i.next();
            if (!userLangs.contains(lang))
            {
                JOptionPane.showConfirmDialog(this, 
                    "The Module \"" + module.getName() + 
                    "\" contains entries using the language " +
                    lang.getDescription() + ". " +
                    "Do you want to add this to your list of available languages?",
                    "Unconfigured language",JOptionPane.YES_NO_OPTION);
                    
                UserConfig.getCurrent().addLanguage(lang,langType,
                    module.getFont(lang,langType));
            }
        }
    }
    
    public void removeModule(TestModule module)
    {
        if (module != null)
        {
            if (!modules.remove(module))
            {
                System.out.println("Module " + module + " not found!");
            }
            if (moduleIdTable.remove(new Integer(module.getUniqueId())) == null)
            {
                System.out.println("Module " + module + " not in ID table!");
            }            
            
            if (module.getTreeNode().getParent() != null)
            {
                treeModel.removeNodeFromParent(module.getTreeNode());
            }
        }
        System.out.println("There are now " + modules.size() + " modules loaded.");
    }
    
    public void insertModule(TestModule newModule)
    {
        
        SortedSet duplicateCandidates = modules.tailSet(newModule);
            if (duplicateCandidates.size() > 0 && 
                newModule.equals(duplicateCandidates.first()))
            {
                // module already loaded
                // TBD prompt inform user
                System.out.println("Module " + newModule + 
                                   " already loaded.");
            }
            else
            {            
                // add to id table
                moduleIdTable.put(new Integer(newModule.getUniqueId()),
                                  newModule);
            
                // now add to tree

                if (duplicateCandidates.size()==0) // no modules after it so add on end
                {
                    topTreeNode.add(newModule.getTreeNode());
                }
                else
                {
                    TestModule nextModule = (TestModule)duplicateCandidates.first();
                    // get index of first module that goes after it
                    int index = topTreeNode.getIndex(nextModule.getTreeNode());
                    topTreeNode.insert(newModule.getTreeNode(), index);
                }
                treeModel.reload();
                if (!modules.add(newModule))
                {
                    System.out.println("Module " + newModule + " already in list!");
                }
            }
        selectionPanel.setStartButtonState();
    }
    
    public TestModule getModuleById(int id, long creationTime)
    {
        Integer mId = new Integer(id);
        TestModule module = (TestModule)moduleIdTable.get(mId);
        if (module != null)
        {
            if (module.getCreationTime() == creationTime)
            {   
                return module;
            }
            else 
            {
                System.out.println("Module time mismatch");
            }
        }
        return null;
    }
    
    public File getModulePath()
    {
        return UserConfig.getCurrent().getModulePath();
    }
    
    public void setModulePath(File newPath)
    {
        UserConfig.getCurrent().setModulePath(newPath);
    }
    
    public LineController getLineControl() 
    {
        return lineControl;
    }
    
    protected void checkAudioStatus()
    {
        if (lineControl.lineError())
        {
            JOptionPane.showMessageDialog(this, 
                "Warning: failed to initalise sound system. \nSound will be disabled until lines become available.",
                "Sound System", JOptionPane.WARNING_MESSAGE);
            // don't want to warn user more than once
            lineControl.clearError();
        }
    }
    
    public void preferenceChange(java.util.prefs.PreferenceChangeEvent evt)
    {
        if (evt.getKey().equals(LOCK))
        {
            if (evt.getNewValue() != null &&
                !evt.getNewValue().equals(Long.toString(startTime)))
            {
                if (JOptionPane.showConfirmDialog(this, 
                    "Another instance of Language Test has been started.\n" + 
                    "Are you sure that you want to continue using this instance?",
                    "Language Test", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION)
                {
                    prefs = null; // no longer own lock so don't delete it
                    confirmExit();
                }
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
