/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/ModuleCleanUpManager.java,v $
 *  Version:       $Revision: 1.4 $
 *  Last Modified: $Date: 2004/12/18 05:10:37 $
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

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import java.util.HashMap;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.JComponent;
import javax.sound.sampled.AudioSystem;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import languagetest.sound.AudioFileExtractor;
//import languagetest.sound.Listener;
import languagetest.language.test.TestModule;
import languagetest.language.test.TestItem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.jfree.ui.RefineryUtilities;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import org.jfree.io.IOUtils;

/** Class to move module sound files into a directory with the same name as the
 * module name.
 * @author keith
 */
public class ModuleCleanUpManager 
    implements AudioFileExtractor.Listener
{
    int itemCount = 0;
    int modCount = 0;
    int totalModules = 0;
    Iterator iModule = null;
    Iterator iItem = null;
    TestModule testModule = null;
    TestItem testItem = null;
    File moduleDir = null;
    DoubleProgressDialog progressDialog = null;
    AudioFileExtractor extractor = null;
    java.awt.Frame parentFrame = null;
    HashSet usedFiles = null;
    HashSet unusedFiles = null;
    HashMap movedFiles = null;
    /** Creates a new instance of ModuleCleanUpManager
     * @param modules Set of modules to clean up
     * @param parent Parent frame
     */
    public ModuleCleanUpManager(Set modules, java.awt.Frame parent)
    {
        this.parentFrame = parent;
        JTextPane message = new JTextPane();
        JScrollPane msgPane = null;
        try 
        {
            message.setPage(getClass().getResource("/languagetest/language/text/ModuleCleanup.html"));
            message.setEditable(false);
            msgPane = new JScrollPane(message);
            msgPane.setPreferredSize(new Dimension(450, 300));
            message.addPropertyChangeListener("page", new PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent evt)
                {
                    if (evt.getSource() instanceof JComponent)
                    {
                        JComponent c = (JComponent)evt.getSource();
                        c.invalidate();
                        c.paint(c.getGraphics());
                    }
                }
            });
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            message.setText(
            "This utility will move all files linked to your loaded modules\n" +
            "into a sub-folder with the same name as the module. If a test \n" +
            "module only uses part of a sound file, then a new sound file \n" +
            "will be created containing just the section of interest.\n\n" +
            "If several modules share the same audio file, then you should \n" + 
            "make sure that all these modules are loaded and run clean up \n" +
            "from the Tools Menu. Do you want to start the clean up now?");
        }
        if (JOptionPane.showConfirmDialog(parent, 
            msgPane,
            "Module Clean Up", JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE) 
            == JOptionPane.YES_OPTION)
        {
            usedFiles = new HashSet();
            unusedFiles = new HashSet();
            movedFiles = new HashMap();
            extractor = new AudioFileExtractor(this);
            iModule = modules.iterator();
            while (iModule.hasNext() && testItem == null)
            {
                testModule = (TestModule)iModule.next();
                moduleDir = TestModuleUtilities.getModuleDataDirectory(testModule);
                iItem = testModule.getTestList().iterator();
                if (iItem.hasNext())
                {
                    testItem = (TestItem)iItem.next();
                }
            }
            if (testItem != null)
            {
                progressDialog = new DoubleProgressDialog(parent, true);
                progressDialog.setSize(400, 200);
                RefineryUtilities.centerDialogInParent(progressDialog);
                progressDialog.setTitle("Module Clean Up Progress");
                progressDialog.setMainLabel("Cleaning up: " + testModule.getName());
                progressDialog.setSubLabel("Item progress for: " + 
                                           testItem.getName());
                progressDialog.setMainProgress(0, testModule.getTestList().size());
                progressDialog.setSubProgress(0, 100); 
                totalModules = modules.size();
                progressDialog.setModProgress(0, totalModules); 
                if (!extractItem())
                {
                    processNext();
                }
            }
        }
    }
    
    protected void processNext()
    {
        do
        {
            if (!nextItem()) break;
        } while (!extractItem());
    }
    
    /** Extracts a new sound file for the current item from the original
     *  containing just the data within the marked bounds.
     */    
    protected boolean extractItem()
    {
        File oldFile = testItem.getSoundFile();
        File newFile = null;
        if (oldFile == null) 
        {
            // move onto next item
            return false;
        }
        if (!oldFile.exists())
        {
            if (movedFiles.containsKey(oldFile))
            {
                oldFile = (File)movedFiles.get(oldFile);
                testItem.setSoundFile(oldFile);
                // fall through to next stage
            }
            else
            {
                // move onto next item
                return false;
            }
        }
        if (testItem.getPlayStart() == 0 && testItem.getPlayEnd() == -1)
        {
            // no need for extraction
            // check that file is in module directory
            if (!oldFile.getParentFile().equals(moduleDir))
            {
                try
                {
                    // attempt to move it
                    newFile = new File(moduleDir, oldFile.getName());
                    // if file already exists need to get a new name
                    if (newFile.exists())
                    {
                        newFile = TestModuleUtilities
                            .createSoundForTestItem(testItem);
                    }
                    // if file is already used by another module, copy instead
                    if (usedFiles.contains(oldFile)) 
                    {
                        BufferedReader reader = 
                            new BufferedReader(new FileReader(oldFile));
                        BufferedWriter writer = 
                            new BufferedWriter(new FileWriter(newFile));
                        IOUtils.getInstance().copyWriter(reader, writer);
                        reader.close();
                        writer.close();
                        setWanted(newFile);
                        testItem.setSoundFile(newFile);
                    }
                    else
                    {
                        
                        if (oldFile.renameTo(newFile))
                        {
                            setWanted(newFile);
                            testItem.setSoundFile(newFile);
                            movedFiles.put(oldFile, newFile);
                        }
                        else
                        {
                            setWanted(oldFile);
                        }
                    }
                }
                catch (SecurityException e)
                {
                    setWanted(oldFile);
                }
                catch (FileNotFoundException e)
                {
                    setWanted(oldFile);
                    System.out.println(e.getMessage());
                }
                catch (IOException e)
                {
                    setWanted(oldFile);
                    System.out.println(e.getMessage());
                }
            }
            else
            {
                setWanted(oldFile);
            }
            // move onto next item
            return false;
        }
        else // need to extract
        {
            newFile = TestModuleUtilities.createSoundForTestItem(testItem);
            try
            {
                extractor.extractFile(oldFile, newFile, testItem.getPlayStart(),
                    testItem.getPlayEnd());
                if (!progressDialog.isVisible())
                {
                    progressDialog.setVisible(true);
                }
                return true;
            }
            catch (AudioFileExtractor.PreviousExtractionNotFinishedException e)
            {
                System.out.println(e.getMessage());
                setWanted(oldFile);
                // move onto next item
                return false;
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Extraction failed for" +
                    newFile.getAbsolutePath() + "\n" + e.getLocalizedMessage(),
                    "Module Cleanup", JOptionPane.WARNING_MESSAGE);
                setWanted(oldFile);
                // move onto next item
                return false;   
            }
            catch (UnsupportedAudioFileException e)
            {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Extraction failed for" +
                    newFile.getAbsolutePath() + "\n" + e.getLocalizedMessage(),
                    "Module Cleanup", JOptionPane.WARNING_MESSAGE);
                setWanted(oldFile);
                // move onto next item
                return false;
            }
        }
    }
    
    /** Invoked by the extraction thread using
     *  {@link SwingUtilities.invokeLater()}
     * if an error occurs during the extraction process.
     * @param extractedFile File that the data was being extracted to.
     * @param reason Reason for failure.
     */    
    public void extractionFailed(java.io.File extractedFile, String reason)
    {
        JOptionPane.showMessageDialog(parentFrame, "Extraction failed for" +
            extractedFile.getAbsolutePath() + "\n" + reason,
            "Module Cleanup", JOptionPane.WARNING_MESSAGE);
        setWanted(testItem.getSoundFile());
        if (progressDialog.isCancelRequested())
        {
            progressDialog.setVisible(false);
        }
        else
        {
            processNext();
        }
        
    }
    
    /** Records that one of the test items is using this file. If the file is 
     * already marked as unwanted, then it will be moved to the wanted set.
     * @param file File to mark as wanted.
     */    
    protected void setWanted(File file)
    {
        usedFiles.add(file);
        if (unusedFiles.contains(file))
        {
            unusedFiles.remove(file);
        }
    }
    
    /** Records that one of the test items is no longer using this file. If 
     * the file is already marked as wanted, then the wanted/unwanted sets will n
     * ot be changed.
     * @param file File to mark as unwanted by the current module.
     */    
    protected void setUnwanted(File file)
    {
        if (!usedFiles.contains(file))
        {
            unusedFiles.add(file);
        }
    }
    
    /** Invoked by the extraction thread using 
     * {@link SwingUtilities.invokeLater()}
     * if the extraction process completes successfuly.
     * @param extractedFile File that was extracted.
     * @param msLengthExtracted The length of the new file created in 
     * milliseconds.
     */    
    public void extractionFinished(java.io.File extractedFile, 
                                   long msLengthExtracted)
    {
        setUnwanted(testItem.getSoundFile());
        setWanted(extractedFile);
        testItem.setSoundFile(extractedFile);
        testItem.setPlayStart(0);
        testItem.setPlayEnd(AudioSystem.NOT_SPECIFIED);
        testItem.setSoundLength(msLengthExtracted);
        if (progressDialog.isCancelRequested())
        {
            progressDialog.setVisible(false);
        }
        else
        {
            processNext();
        }
    }
    
    /** Invoked by the extraction thread using 
     * {@link SwingUtilities.invokeLater()}
     * to show the progress in seeking through the audio file. This is used to
     * update the progress bar.
     * @param percent Percent progress in extraction of current file.
     */    
    public void showProgress(int percent)
    {
        if (percent < 0) progressDialog.setSubLabel("Compressing...");
        progressDialog.setSubProgress(percent, 100);
    }
    
    /** Finds the next test item to process. It moves onto the next module in 
     * the if all the items in the current module have been processed.
     */    
    protected boolean nextItem()
    {
        testItem = null;
        if (iItem.hasNext())
        {
            testItem = (TestItem)iItem.next();
            itemCount++;
            progressDialog.setMainProgress(itemCount, 
                testModule.getTestList().size());
        }
        else
        {
            // need to move to next module
            // save changes to current one first for safety sake
            testModule.save();
            while (iModule.hasNext() && testItem == null)
            {
                testModule = (TestModule)iModule.next();
                progressDialog.setModProgress(++modCount, totalModules); 
                itemCount = 0;
                progressDialog.setMainLabel
                    ("Cleaning up: " + testModule.getName());
                progressDialog.setMainProgress
                    (0, testModule.getTestList().size());
                moduleDir = 
                    TestModuleUtilities.getModuleDataDirectory(testModule);
                iItem = testModule.getTestList().iterator();
                if (iItem.hasNext())
                {
                    testItem = (TestItem)iItem.next();
                }
            }
        }
        if (testItem == null)
        {
            // finished
            cleanUpFinished();
            return false;
        }
        else
        {
            progressDialog.setSubProgress(0, 100);         
            progressDialog.setSubLabel("Item progress for: " + testItem.getName());
            return true;
        }
    }
    
    protected void cleanUpFinished()
    {
        final int LEAVE_OPTION = 0;
        final int DELETE_OPTION = 1;
        final int MOVE_OPTION = 2;
        progressDialog.setVisible(false);
        // don't need to ask about deleting if nothing to delete
        if (unusedFiles.size() > 0) 
        {
            // ask user whether he/she wants to delete the unwanted files
            JList fileList = new JList(unusedFiles.toArray());
            JScrollPane pane = new JScrollPane(fileList);
            Object [] message = { 
                "Clean up has finished.",
                "Several files no longer appear to be used by your loaded modules:",
                pane,
                "What do you want to do with these files?",
                "a) Leave them as is filling up your hard disk.",
                "b) Permanently delete them. Make sure they are not used by any " + 
                "unloaded modules!",
                "c) Move them to a quarantine directory. You can then delete them" + 
                " at your leisure once you know that you really don't need them " +
                "any more."
            };
            String title = "Clean up finished";
            Object [] options = { "Leave", "Delete", "Move" };
            int answer = 
                JOptionPane.showOptionDialog(parentFrame, message, title, 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, null, options, 
                    options[LEAVE_OPTION]);
            switch (answer)
            {
                case DELETE_OPTION:
                    message = new Object [] {
                        "Are you sure that you want to permanently delete these files?",
                        "You should only delete them if you are sure that they are not used by any unloaded modules.",
                        pane,
                        "This action cannot be undone."
                    };
                    if (JOptionPane.showConfirmDialog(parentFrame, message, title,
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)
                            == JOptionPane.YES_OPTION)
                    {
                        Iterator i = unusedFiles.iterator();
                        while (i.hasNext())
                        {
                            File f = (File)i.next();
                            if (f.delete() == false)
                            {
                                JOptionPane.showMessageDialog(parentFrame, 
                                    "Failed to delete:" + f.getAbsolutePath(),
                                    "Module Clean Up: Delete", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                    break;
                case MOVE_OPTION:
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("Select a quarantine directory");
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (chooser.showOpenDialog(parentFrame) == 
                        JFileChooser.APPROVE_OPTION)
                    {
                        File newFolder = chooser.getSelectedFile();
                        if (newFolder.isDirectory()) 
                        {
                            System.out.println("Some how user chose a file that wasn't a directory");
                        }
                        Iterator i = unusedFiles.iterator();
                        while (i.hasNext())
                        {
                            File f = (File)i.next();
                            try 
                            {
                                File newFile = new File(newFolder, f.getName());
                                if (f.renameTo(newFile) == false)
                                {
                                    JOptionPane.showMessageDialog(parentFrame, 
                                        "Failed to move:" + f.getAbsolutePath(),
                                        "Module Clean Up: Move", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                            catch (SecurityException e)
                            {
                                JOptionPane.showMessageDialog(parentFrame, 
                                    "Failed to move:" + f.getAbsolutePath() + "\n" + 
                                    e.getLocalizedMessage(),
                                    "Module Clean Up: Move", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                    break;

            }
            
        }
        // free up sets
        usedFiles.clear();
        usedFiles = null;
        unusedFiles.clear();
        unusedFiles = null;
        movedFiles.clear();
        movedFiles = null;
    }
}
