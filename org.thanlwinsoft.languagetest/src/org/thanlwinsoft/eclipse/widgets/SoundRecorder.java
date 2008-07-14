/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 *  MA 02110-1301 USA
 * -----------------------------------------------------------------------
 */
/**
 * 
 */
package org.thanlwinsoft.eclipse.widgets;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.layout.GridData;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.editors.TestItemEditor;
import org.thanlwinsoft.languagetest.eclipse.editors.TestModuleEditor;
import org.thanlwinsoft.languagetest.eclipse.prefs.RecordingPreferencePage;
import org.thanlwinsoft.languagetest.eclipse.views.TestView;
import org.thanlwinsoft.languagetest.language.test.XmlBeansTestModule;
import org.thanlwinsoft.languagetest.sound.AudioPlayListener;
import org.thanlwinsoft.languagetest.sound.LineController;
import org.thanlwinsoft.languagetest.sound.Recorder;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Composite containing a sound recorder
 * the actual playback is done using the player in the TestView ControlPanel.
 * @author keith
 *
 */
public class SoundRecorder extends Composite implements ISelectionChangedListener, AudioPlayListener
{
    private Button recordButton = null;
    private Button stopButton = null;
    private Scale recordScale = null;
    private Spinner startSpinner = null;
    private Button startButton = null;
    private Spinner endSpinner = null;
    private Button endButton = null;
    private Button playButton = null;
    private Button deleteButton = null;
    private LineController lineController = null;
    private Recorder recorder = null;  //  @jve:decl-index=0:
    public final static String LINE_CONTROLLER = "LineController";  //  @jve:decl-index=0:
    private static final int WAV = 0;
    private static final int MP3 = 1;
    private static final int OGG = 2;
    private static final int NUM_ENCODINGS = 3;
    private static final int SPINNER2MS = 100; // spinner uses intervals of 0.1s
    private static final int INITIAL_LIMIT = 300; // 30s
    private long lengthInMs = -1;
    private static final String [] EXTENSIONS = new String[] { "wav", "mp3", "ogg" };
    private static final String [] FILTERS = new String[] { "*.wav", "*.mp3", "*.ogg" };
    private static final String [] TYPES = new String[] { "Wave", "MP3", "OGG Vorbis" };
    private Text fileNameText = null;
    private Button browseButton = null;
    private Combo encodingCombo = null;
    private ComboViewer comboViewer = null;
    private final static int MAX_SOUND_FILES = 10000;
    private Display display = null;
    private boolean settingItem = false;
    private final DateFormat tempFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    
    public static final String WAVTOMP3_CONVERTER_PREF = "WavToMP3Converter";
    public static final String WAVTOMP3_CONV_ARG_PREF = "WavToMP3ConverterArguments";
    public static final String MP3TOWAV_CONVERTER_PREF = "MP3ToWavConverter";
    public static final String MP3TOWAV_CONV_ARG_PREF = "MP3ToWavConverterArguments";
    public static final String WAVTOOGG_CONVERTER_PREF = "WavToOggConverter";
    public static final String WAVTOOGG_CONV_ARG_PREF = "WavToOggConverterArguments";
    public static final String OGGTOWAV_CONVERTER_PREF = "OGGToWavConverter";
    public static final String OGGTOWAV_CONV_ARG_PREF = "OGGToWavConverterArguments";
    
    
    public final static String OVERWRITE_PREF_KEY = "OverwriteAudioNoPrompt";
    private Label spacerLabel;
    public SoundRecorder(Composite parent, int style)
    {
        super(parent, style);
        initialize();
        RecordingPreferencePage.initializeDefaults();
        this.display = parent.getDisplay();
    }

    private void initialize()
    {
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData = new GridData();
        gridData.horizontalSpan = 5;
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.widthHint = -1;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 7;
        recordButton = new Button(this, SWT.NONE);
        recordButton.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/org/thanlwinsoft/languagetest/sound/icons/recordMini.png")));
        recordButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                record();
            }
        });
        recordButton.setToolTipText(MessageUtil.getString("RecordTooltip"));
        
        stopButton = new Button(this, SWT.NONE);
        stopButton.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/org/thanlwinsoft/languagetest/sound/icons/stopMini.png")));
        stopButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                stop();
            }
        });
        stopButton.setToolTipText(MessageUtil.getString("StopTooltip"));
        
        recordScale = new Scale(this, SWT.NONE);
        recordScale.setPageIncrement(10);
        recordScale.setMinimum(0);
        recordScale.setMaximum(INITIAL_LIMIT);
        recordScale.setIncrement(1);
        recordScale.setLayoutData(gridData);
        playButton = new Button(this, SWT.NONE);
        playButton.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/org/thanlwinsoft/languagetest/sound/icons/playMini.png")));
        playButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                play();
            }
        });
        playButton.setToolTipText(MessageUtil.getString("PlayTooltip"));
        deleteButton = new Button(this, SWT.NONE);
        deleteButton.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/org/thanlwinsoft/languagetest/sound/icons/rubbishBin16.png")));
        deleteButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                File file = getAudioFile();
                if (file != null && MessageDialog.openConfirm(getShell(), 
                        MessageUtil.getString("ConfirmDeleteTitle"), 
                        MessageUtil.getString("ConfirmDeleteMessage", 
                                file.getName())))
                {
                    file.delete();
                    fileNameText.setText("");
                    fileNameText.setToolTipText("");
                    setEnabledStatus();
                }
            }
        });
        deleteButton.setToolTipText(MessageUtil.getString("DeleteAudioTooltip"));
        
        startButton = new Button(this, SWT.LEFT);
        startSpinner = new Spinner(this, SWT.NONE);
        startSpinner.setValues(0, 0, INITIAL_LIMIT, 1, 1, 10);
        spacerLabel = new Label(this, SWT.LEAD);
        GridData spacerGridData = new GridData();
        spacerGridData.grabExcessHorizontalSpace = true;
        spacerGridData.minimumWidth = 50;
        spacerGridData.horizontalAlignment = SWT.FILL;
        spacerLabel.setText("");
        spacerLabel.setLayoutData(spacerGridData);
        endSpinner = new Spinner(this, SWT.NONE);
        endSpinner.setValues(0, 0, INITIAL_LIMIT, 1, 1, 10);
        endButton = new Button(this, SWT.RIGHT);
        // Visual Editor doesn't like direct calls to MessageUtil in setText
        createEncodingCombo();
        GridData fileNameGridData = new GridData();
        fileNameGridData.horizontalSpan = 3;
        fileNameGridData.horizontalAlignment = GridData.FILL;
        fileNameText = new Text(this, SWT.BORDER);
        fileNameText.setLayoutData(fileNameGridData);
        fileNameText.setToolTipText(MessageUtil.getString("AudioFileNameTooltip"));
        GridData browseGridData = new GridData();
        browseGridData.horizontalSpan = 2;
        browseButton = new Button(this, SWT.NONE);
        browseButton.setLayoutData(browseGridData);
        browseButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
                dialog.setFilterExtensions(FILTERS);
                dialog.setFilterNames(TYPES);
                if (fileNameText.getText().length() > 0)
                {
                    dialog.setFileName(fileNameText.getText());
                }
                fileNameText.setText(dialog.open());
                fileNameText.setToolTipText(fileNameText.getText());
            }
        });
        String s = MessageUtil.getString("PlaybackStartButton");
        startButton.setText(s);
        s = MessageUtil.getString("PlaybackStartTooltip");
        startButton.setToolTipText(s);
        startButton.setLayoutData(gridData1);
        startButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                int value = recordScale.getSelection();
                if (value < endSpinner.getSelection() || 
                    endSpinner.getSelection() <= 0)
                {
                    startSpinner.setSelection(value);
                    updateTestItem();
                    spacerLabel.setText("");
                    spacerLabel.setToolTipText("");
                }
                else
                {
                    spacerLabel.setText(MessageUtil.getString("EndBeforeStart"));
                    spacerLabel.setToolTipText(MessageUtil.getString("EndBeforeStart"));
                    spacerLabel.redraw();
                }
            }
        });
        s = MessageUtil.getString("PlaybackEndButton");
        endButton.setText(s);
        s = MessageUtil.getString("PlaybackEndTooltip");
        endButton.setToolTipText(s);
        endButton.setLayoutData(gridData2);
        endButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                int value = recordScale.getSelection();
                if (value > 0 && value > startSpinner.getSelection())
                {
                    endSpinner.setSelection(value);
                    updateTestItem();
                    spacerLabel.setText("");
                    spacerLabel.setToolTipText("");
                    spacerLabel.redraw();
                }
                else
                {
                    spacerLabel.setText(MessageUtil.getString("EndBeforeStart"));
                    spacerLabel.setToolTipText(MessageUtil.getString("EndBeforeStart"));
                    spacerLabel.redraw();
                }
            }
        });
        s = MessageUtil.getString("Browse");
        browseButton.setText(s);
        startSpinner.addModifyListener(new ModifyListener(){
            public void modifyText(ModifyEvent e)
            {
                setLimits();
                updateTestItem();
            }});
        endSpinner.addModifyListener(new ModifyListener(){
            public void modifyText(ModifyEvent e)
            {
                setLimits();
                updateTestItem();
            }});
        this.setLayout(gridLayout);
        setSize(new Point(300, 200));

        this.lineController = new LineController(LineController.REC_MODE);
        Job job = new Job(LINE_CONTROLLER) {
            public boolean belongsTo(Object family) {
                return family == LINE_CONTROLLER;
             }

            protected IStatus run(IProgressMonitor monitor)
            {
                lineController.run();
                return new Status(IStatus.OK, LanguageTestPlugin.ID, 
                                IStatus.OK, LINE_CONTROLLER, null);
            }
            
        };
        job.setPriority(Job.LONG);
        job.schedule();
        
        IEditorPart editor = 
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor instanceof TestModuleEditor)
        {
            ISelectionProvider sp = (ISelectionProvider)editor.getAdapter(ISelectionProvider.class);
            if (sp != null)
            {
                sp.addSelectionChangedListener(this);
            }
        }
    }

    /**
     * 
     */
    protected void stop()
    {
        if (recorder != null)
        {
            
            recorder.stop();
            recorder.finish();
            
            if (recorder.isRecordError())
            {
                LanguageTestPlugin.log(IStatus.ERROR, recorder.getErrorDescription());
                MessageDialog.openError(this.getShell(), 
                    MessageUtil.getString("RecordingError"), 
                    recorder.getErrorDescription());
            }
            lineController.closeLines();
            recorder = null;
            IProgressMonitor pm = 
                Job.getJobManager().createProgressGroup();
            try
            {
            	IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IWorkspaceRoot root = workspace.getRoot(); 
                pm.beginTask("Refresh after conversion", 
                    IProgressMonitor.UNKNOWN);
                // TODO make this less expensive, by only refreshing the
                // relevant directory
                root.refreshLocal(IResource.DEPTH_INFINITE, pm);
            }
            catch (CoreException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING,
                    "Refresh error", e);
            }
            updateTestItem();
        }
        else
        {
            SoundPlayer player = getSoundPlayer();
            if (player != null)
            {
                player.stop();
            }
        }
    }

    /**
     * 
     */
    protected void record()
    {
        if (lineController != null)
        {
            if (!lineController.linesAreAvailable())
            {
                lineController.openLines();
            }
            if (recorder == null)
            {
            	// setting the spinner resets the recorder, so this must be 
            	// before the recorder is created
                startSpinner.setSelection(0);
                endSpinner.setSelection(0);
                recorder = new Recorder(lineController);
                recorder.addPlayListener(this);
            }
            else return;
            
            File targetFile = getAudioFile();
            boolean overwriteOK = LanguageTestPlugin.getPrefStore().getBoolean(OVERWRITE_PREF_KEY);
            
            if (targetFile == null)
            {
                MessageDialog.openError(getShell(), 
                                MessageUtil.getString("NoAudioFileTitle"), 
                                MessageUtil.getString("NoAudioFileMessage"));
                return;
            }
            else if (targetFile.exists() && overwriteOK == false)
            {
//                final String [] buttons = {
//                    MessageUtil.getString("Yes"),
//                    MessageUtil.getString("No"),
//                    MessageUtil.getString("Cancel")
//                };
                MessageDialogWithToggle dialog = 
                    MessageDialogWithToggle.openYesNoCancelQuestion( 
                                    getShell(), MessageUtil.getString("OverwriteRecordingTitle"),
                                MessageUtil.getString("OverwriteRecording", 
                                targetFile.getAbsolutePath()),
                                MessageUtil.getString("AlwaysOverwrite"),
                                false,                
                                LanguageTestPlugin.getPrefStore(),
                                OVERWRITE_PREF_KEY);
                int choice = 0;
                LanguageTestPlugin.getPrefStore().setValue(OVERWRITE_PREF_KEY,
                        dialog.getToggleState());
                try
                {
                    LanguageTestPlugin.getPrefStore().save();
                } 
                catch (IOException e)
                {
                    LanguageTestPlugin.log(IStatus.WARNING, 
                            e.getLocalizedMessage(), e);
                }
                choice = dialog.getReturnCode();
                
                if (choice != 2) 
                    return;
            }
            
            recorder.initialise(targetFile);
            setEnabledStatus();
            
            if (recorder.isInitialised())
            {
                recorder.start();
            }
            else
            {
                Job job = new Job("Record") {
                    
                    protected IStatus run(IProgressMonitor monitor)
                    {
                        if (recorder.isInitialised())
                        {
                            recorder.start();
                        }
                        else
                        {
                            this.schedule(100);
                        }
                        return new Status(IStatus.OK, LanguageTestPlugin.ID, 
                                          IStatus.OK, "Record", null);
                    }
                    
                };
                job.schedule(100);
            }
            
        }
    }
    
    protected void updateTestItem()
    {
        // don't update the test item while switch from one to another
        if (settingItem)
            return;
        IEditorPart editor = 
            PlatformUI.getWorkbench().getActiveWorkbenchWindow()
            .getActivePage().getActiveEditor();
        if (editor instanceof TestModuleEditor)
        {
            TestModuleEditor tme = (TestModuleEditor)editor;
            if (tme.getEditorInput() instanceof IFileEditorInput)
            {
                TestItemEditor tie = (TestItemEditor)tme.getAdapter(TestItemEditor.class);
                ISelection selection = tie.getSelection();
                if (tie != null && selection instanceof IStructuredSelection)
                {
                    TestItemType [] items = tie.getSelectedItems();
                    if (items.length > 0)
                    {
                        TestItemType ti = items[0];
                        if (ti.getSoundFile() == null)
                                ti.addNewSoundFile();
                        IPath path = new Path(fileNameText.getText());
                        if (path.isAbsolute())
                        {
                            IPath modulePath = ((IFileEditorInput)tme.getEditorInput()).getFile().getRawLocation();
                            if (modulePath.removeLastSegments(1).isPrefixOf(path))
                            {
                                path = path.removeFirstSegments(modulePath.segmentCount() - 1);
                                fileNameText.setText(path.toString());
                            }
                        }
                        ti.getSoundFile().setStringValue(fileNameText.getText());
                        if (startSpinner.getSelection() > 0)
                            ti.getSoundFile().setStart(startSpinner.getSelection() * SPINNER2MS);
                        else if (ti.getSoundFile().isSetStart())
                            ti.getSoundFile().unsetStart();
                        if (endSpinner.getSelection() > 0)
                            ti.getSoundFile().setEnd(endSpinner.getSelection() * SPINNER2MS);
                        else if (ti.getSoundFile().isSetEnd())
                            ti.getSoundFile().unsetEnd();
                        if (lengthInMs > -1)
                        {
                            ti.getSoundFile().setLength(lengthInMs);
                        }
                        tme.setDirty(true);
                        tie.setSelection(selection);
                    }
                }
            }
        }
    }
    
    protected File getAudioFile()
    {
        File file = null;
        IFile moduleFile = null;
        String moduleBaseName = "";
        
        String fileName = "rec" + tempFormat.format(new Date()) + ".wav";
        IEditorPart editor = 
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor instanceof TestModuleEditor)
        {
            TestModuleEditor tme = (TestModuleEditor)editor;
            if (tme.getEditorInput() instanceof IFileEditorInput)
            {
                moduleFile = ((IFileEditorInput)tme.getEditorInput()).getFile();
                int extIndex = 
                    moduleFile.getName().indexOf(moduleFile.getFileExtension());
                if (extIndex > 0)
                {
                    moduleBaseName = moduleFile.getName().substring(0, extIndex - 1);
                }
                else 
                {
                    moduleBaseName = moduleFile.getName();
                }
                TestItemEditor tie = (TestItemEditor)tme.getAdapter(TestItemEditor.class);
                if (tie != null && tie.getSelection() instanceof IStructuredSelection)
                {
                    tie.addSelectionChangedListener(this);
                    TestItemType [] items = tie.getSelectedItems();
                    if (items.length > 0 && items[0].sizeOfNativeLangArray() > 0)
                    {
                        String nativeText = items[0].getNativeLangArray(0).getStringValue();
                        if (moduleFile.getParent().getFullPath().isValidSegment(nativeText))
                        {
                            fileName = nativeText + "." + EXTENSIONS[WAV];
                        }
                    }
                }
            }
        }
        if (fileNameText.getText().length() > 0)
        {
            // should we use portable string or OS string?
            // this is probably more versatile
            IPath path = new Path(fileNameText.getText());
            if (path.isAbsolute())
            {
                file = new File(fileNameText.getText());
            }
            else
            {
                file = moduleFile.getParent().getFile(path).getRawLocation().toFile();
            }
        }
        else if (moduleFile != null)
        {
            IContainer moduleParent = (IContainer)moduleFile.getParent();
            IPath folderPath = new Path(moduleBaseName + "." + XmlBeansTestModule.FOLDER_EXT);
            IFolder folder = moduleParent.getFolder(folderPath);
            if (!folder.exists())
            {
                IJobManager jobMan = Job.getJobManager();
                IProgressMonitor pm = jobMan.createProgressGroup();
                try
                {
                    folder.create(IResource.NONE, true, pm);
                }
                catch (CoreException e)
                {
                    MessageDialog.openError(getShell(), MessageUtil.getString("FailedToCreateFolderTitle"), 
                                    MessageUtil.getString("FailedToCreateFolderMessage",
                                                    folder.getFullPath().toOSString(),
                                                    e.getLocalizedMessage()));
                }
            }
            if (folder.isAccessible())
            {
                IFile testFile = folder.getFile(fileName);
                int i = 0;
                // find a unique name that doesn't yet exist
                // give up eventually if max sane number is reached
                while (testFile.exists() && i++ < MAX_SOUND_FILES)
                {
                    fileName = moduleBaseName + i + "." + EXTENSIONS[WAV];
                    testFile = folder.getFile(fileName);
                }
                if (testFile != null)
                {
                    file = testFile.getRawLocation().toFile();
                    int moduleSegments = moduleFile.getParent().getFullPath().segmentCount();
                    IPath relativePath = 
                        testFile.getFullPath().removeFirstSegments(moduleSegments);
                    fileNameText.setText(relativePath.toPortableString());
                    fileNameText.setToolTipText(relativePath.toPortableString());
                }
            }
            encodingCombo.select(WAV);
        }
        return file;
    }

    /**
     * play the audio file
     */
    protected void play()
    {
        SoundPlayer player = getSoundPlayer();
        if (player != null)
        {
            player.setFile(getAudioFile().getAbsolutePath());
            setLimits();
            player.play();
        }
    }
    /**
     * set the player limits from the start/end spinners
     * @param player
     */
    protected void setLimits()
    {
        SoundPlayer player = getSoundPlayer();
        if (player == null) return;
        // spinners use intervals of 0.1s
        long startMs = startSpinner.getSelection() * SPINNER2MS;
        long endMs = endSpinner.getSelection() * SPINNER2MS;
        long thumbMs = recordScale.getSelection() * SPINNER2MS;
        if (endMs > 0 && endMs < startMs)
        {
            if (spacerLabel.getText().length() == 0)
            {
            spacerLabel.setText(MessageUtil.getString("EndBeforeStart"));
            spacerLabel.setToolTipText(MessageUtil.getString("EndBeforeStart"));
            spacerLabel.redraw();
            }
            endMs = -1;
        }
        else
        {
            spacerLabel.setText("");
            spacerLabel.setToolTipText("");
        }
        // if the thumb is in between start and end, then start at the thumb
        // give a 1 second tolerance at the end
        if (thumbMs + 1000 < endMs && thumbMs > startMs)
        {
            startMs = thumbMs;
        }
        player.seek(startMs);
        if (endMs > 0)
        {
            // this is elapsed from seek position
            player.stopAfter(endMs - startMs);
        }
        else
        {
            player.stopAfter(-1);
        }
    }
    
    protected SoundPlayer getSoundPlayer()
    {
        SoundPlayer player = null;
        IWorkbenchPage page = 
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        TestView view = (TestView)page.findView(TestView.ID);
        if (view == null)
        {
            try
            {
                view = (TestView)page.showView(TestView.ID, null, IWorkbenchPage.VIEW_CREATE);
            }
            catch (PartInitException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, e.getLocalizedMessage(), e);
            }
        }
        if (view != null)
        {
            player = (SoundPlayer)view.getAdapter(SoundPlayer.class);
            player.addPlayListener(this);
        }
        return player;
    }

    /**
     * This method initializes encodingCombo	
     *
     */
    private void createEncodingCombo()
    {
        GridData gridData3 = new GridData();
        gridData3.horizontalSpan = 2;
        encodingCombo = new Combo(this, SWT.NONE);
        encodingCombo.setLayoutData(gridData3);
        encodingCombo.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {}

            public void widgetSelected(SelectionEvent e)
            {
                convertIfNeeded();
            }});
        comboViewer = new ComboViewer(encodingCombo);
        comboViewer.add(TYPES);
        encodingCombo.setToolTipText(MessageUtil.getString("AudioFormatTooltip"));
    }

    /**
     * This compares the requested format with the current audio format.
     * If the 2 don't match a conversion is initiatied. This may take it to an
     * intermediate format. After each conversion, this is recalled to initiate
     * the next stage in the conversion.
     */
    private void convertIfNeeded()
    {
        int fileEncoding = getFileEncoding();
        int selectedEncoding = encodingCombo.getSelectionIndex();
        if (fileEncoding != selectedEncoding)
        {
            switch (fileEncoding)
            {
            case WAV:
                switch (selectedEncoding)
                {
                case MP3:
                    convertWavToMp3();
                    break;
                case OGG:
                    convertWavToOgg();
                    break;
                }
                break;
            case MP3:
                switch (selectedEncoding)
                {
                case OGG:
                case WAV:
                    convertMp3ToWav();
                    break;
                }
                break;
            case OGG:
                switch (selectedEncoding)
                {
                case MP3:
                case WAV:
                    convertOggToWav();
                    break;
                }
                break;
            }
        }
    }
    
    private int getFileEncoding()
    {
        File file = getAudioFile();
        int encoding = WAV;
        for (int i = 0; i < NUM_ENCODINGS; i++)
        {
            if (file.getName().endsWith(EXTENSIONS[i]))
                return i;
        }
        return encoding;
    }
    
    /**
     * Convert file from Wave to MP3
     */
    protected void convertWavToMp3()
    {
        final String converter = 
            LanguageTestPlugin.getPrefStore().getString(WAVTOMP3_CONVERTER_PREF);
        String argumentTemplate = 
            LanguageTestPlugin.getPrefStore().getString(WAVTOMP3_CONV_ARG_PREF);
        externalConverter(EXTENSIONS[MP3], converter, argumentTemplate);
    }
    
    /**
     * Convert file from MP3 to Wave
     */
    protected void convertMp3ToWav()
    {
        final String converter = 
            LanguageTestPlugin.getPrefStore().getString(MP3TOWAV_CONVERTER_PREF);
        String argumentTemplate = 
            LanguageTestPlugin.getPrefStore().getString(MP3TOWAV_CONV_ARG_PREF);
        externalConverter(EXTENSIONS[WAV], converter, argumentTemplate);
    }
    
    /**
     * Convert from Wave to Ogg Vorbis
     */
    protected void convertWavToOgg()
    {
        final String converter = 
            LanguageTestPlugin.getPrefStore().getString(WAVTOOGG_CONVERTER_PREF);
        String argumentTemplate = 
            LanguageTestPlugin.getPrefStore().getString(WAVTOOGG_CONV_ARG_PREF);
        externalConverter(EXTENSIONS[OGG], converter, argumentTemplate);
        
    }
    
    /**
     * Convert from Ogg to Wave
     */
    protected void convertOggToWav()
    {
        final String converter = 
            LanguageTestPlugin.getPrefStore().getString(OGGTOWAV_CONVERTER_PREF);
        String argumentTemplate = 
            LanguageTestPlugin.getPrefStore().getString(OGGTOWAV_CONV_ARG_PREF);
        externalConverter(EXTENSIONS[WAV], converter, argumentTemplate);
    }
    
    protected void externalConverter(final String newExtension, 
            final String converter, final String argumentTemplate)
    {
        final File oldFile = getAudioFile();
        IPath path = new Path(getAudioFile().getAbsolutePath());
        IPath basePath = path.removeFileExtension();
        
        final IPath newPath = basePath.addFileExtension(newExtension);
        final String newFilePath = newPath.toOSString();
        final String allArgs = MessageFormat.format(argumentTemplate, new Object[] 
           {path.toOSString(),
            newFilePath });
        StringTokenizer st = new StringTokenizer(allArgs, " ", false);
        final String [] arguments = new String[st.countTokens() + 1];
        arguments[0] = converter;
        for (int i = 1; i < arguments.length; i++)
        {
            arguments[i] = st.nextToken();
        }
        final Display display = getShell().getDisplay();
        // Lame checks for term environment variable
        final String [] environment = { "TERM=xterm" }; 
        System.out.println(converter + allArgs);
        if (converter.length() > 0)
        {
            
            Job job = new Job(converter) {
                
                public IStatus run(IProgressMonitor monitor)
                {
                    IStatus s = null;
                    int retValue = -1;
                    StringBuilder errorBuilder = new StringBuilder();
                    try
                    {
                        Process p = Runtime.getRuntime().exec(arguments, environment);
                        retValue = p.waitFor();
                        BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        while (br.ready())
                        {
                            errorBuilder.append(br.readLine());
                        }
                        br.close();
                        br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        while (br.ready())
                        {
                            errorBuilder.append(br.readLine());
                        }
                        br.close();
                        System.out.println(errorBuilder.toString());
                    }
                    catch (IOException e)
                    {
                        errorBuilder.append(e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                    catch (InterruptedException e)
                    {
                        errorBuilder.append(e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (retValue != 0)
                        {
                            final String errorMsg = errorBuilder.toString();
                            display.asyncExec (new Runnable () {
                                public void run () {
                                    MessageDialog.openWarning(getShell(), 
                                        MessageUtil.getString("ConversionProcessFailedTitle"), 
                                        MessageUtil.getString("ConversionProcessFailedTitle",
                                                        converter, allArgs,errorMsg));
                                 }
                            });
                            s = new Status(IStatus.ERROR, LanguageTestPlugin.ID,
                                    IStatus.OK, errorMsg, null);
                        }
                        else
                        {
                            // update gui
                            display.asyncExec (new Runnable () {
                                public void run () {
                                    fileNameText.setText(newPath.toString());
                                    fileNameText.setToolTipText(newPath.toString());
                                    System.out.println(newPath.toString());
                                    updateTestItem();
                                    // may need a second conversion
                                    convertIfNeeded();
                                    setEnabledStatus();
                                    IWorkspace workspace = ResourcesPlugin.getWorkspace();
                                    IWorkspaceRoot root = workspace.getRoot(); 
                                    if (root.getRawLocation().isPrefixOf(newPath))
                                    {
                                        IFile f = root.getFile(newPath.removeFirstSegments(root.getRawLocation().segmentCount()));
                                        IProgressMonitor pm = 
                                            Job.getJobManager().createProgressGroup();
                                        try
                                        {
                                            pm.beginTask("Refresh after conversion", 
                                                IProgressMonitor.UNKNOWN);
                                            f.refreshLocal(IResource.DEPTH_INFINITE, pm);
                                        }
                                        catch (CoreException e)
                                        {
                                            LanguageTestPlugin.log(IStatus.WARNING,
                                                "Refresh error", e);
                                        }
                                        finally
                                        {
                                            pm.done();
                                        }
                                    }
                                }
                            });
                            oldFile.delete(); // delete old format
                            s = new Status(IStatus.OK, LanguageTestPlugin.ID,
                                           IStatus.OK, converter, null);
                        }
                    }
                    return s;
                }
            };
            job.setPriority(Job.BUILD);
            job.schedule();
        }
        else
        {
            MessageDialog.openInformation(getShell(), 
                            MessageUtil.getString("NoMP3ConverterTitle"), 
                            MessageUtil.getString("NoMP3ConverterMessage"));
        }
    }

    

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     * called when TestItem selection changes in the TestItemEditor
     */
    public void selectionChanged(SelectionChangedEvent event)
    {
        stop();
        if (fileNameText.isDisposed())
            return;
        settingItem = true;
        try
        {
        if (event.getSelection() instanceof StructuredSelection)
        {
            StructuredSelection ss = (StructuredSelection)event.getSelection();
            TestItemType ti = null;
            lengthInMs = -1;
            if (ss.getFirstElement() instanceof TestItemType)
            {
                ti = (TestItemType)ss.getFirstElement();
            }
            if (ti != null && ti.isSetSoundFile()
                && ti.getSoundFile().getStringValue() != null)
            {
                spacerLabel.setText("");
                fileNameText.setText(ti.getSoundFile().getStringValue());
                fileNameText.setToolTipText(ti.getSoundFile().getStringValue());
                IPath path = new Path(ti.getSoundFile().getStringValue());
                for (int i = 0; i < EXTENSIONS.length; i++)
                {
                    if (EXTENSIONS[i].equalsIgnoreCase(path.getFileExtension()))
                    {
                        encodingCombo.select(i);
                        break;
                    }
                }
                int max = INITIAL_LIMIT;
                if (ti.getSoundFile().isSetLength())
                {
                    max = (int)(ti.getSoundFile().getLength() / SPINNER2MS);
                }
                startSpinner.setMaximum(max);
                endSpinner.setMaximum(max);
                if (ti.getSoundFile().isSetEnd())
                {
                    int value = (int)(ti.getSoundFile().getEnd() / SPINNER2MS);
                    if (max < value)
                    {
                        startSpinner.setMaximum(max);
                        endSpinner.setMaximum(max);
                    }
                    endSpinner.setSelection(value);
                }
                else endSpinner.setSelection(0);
                if (ti.getSoundFile().isSetStart())
                {
                    int value = (int)(ti.getSoundFile().getStart() / SPINNER2MS);
                    if (max < value)
                    {
                        max = value;
                        startSpinner.setMaximum(max);
                        endSpinner.setMaximum(max);
                    }
                    startSpinner.setSelection(value);
                }
                else startSpinner.setSelection(0);
                
            }
            else
            {
                fileNameText.setText("");
                fileNameText.setToolTipText("");
                encodingCombo.select(WAV);
                startSpinner.setSelection(0);
                endSpinner.setSelection(0);
            }
            setEnabledStatus();
        }
        }
        finally
        {
            settingItem = false;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    public void dispose()
    {
        IEditorPart editor = 
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor instanceof TestModuleEditor)
        {
            ISelectionProvider sp = (ISelectionProvider)editor.getAdapter(ISelectionProvider.class);
            if (sp != null)
            {
                sp.removeSelectionChangedListener(this);
            }
        }
        SoundPlayer player = getSoundPlayer();
        if (player != null)
        {
            player.removePlayListener(this);
        }
        super.dispose();
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.sound.AudioPlayListener#initialisationProgress(int)
     */
    public void initialisationProgress(int percent)
    {
        
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.sound.AudioPlayListener#playPosition(long, long)
     */
    public void playPosition(long msPosition, long msTotalLength)
    {
        // use 0.1 sec as interval
        final int sTotalLength = (int)(msTotalLength/SPINNER2MS);
        final int sPosition = (int)(msPosition/SPINNER2MS);
        if (msTotalLength > 0) lengthInMs = msTotalLength;
        display.asyncExec (new Runnable () {
            public void run () {
                if (recordScale.isDisposed()) return;
                int oldMax = recordScale.getMaximum();
                if (sTotalLength > 0 && oldMax != sTotalLength)
                {
                    recordScale.setMaximum(sTotalLength);
                    startSpinner.setMaximum(sTotalLength);
                    endSpinner.setMaximum(sTotalLength);
                }
                if (sPosition > oldMax)
                {
                    recordScale.setMaximum(oldMax + 600); // add 1 minute
                    startSpinner.setMaximum(oldMax + 600);
                    endSpinner.setMaximum(oldMax + 600);
                }
                recordScale.setSelection(sPosition);
            }
         });
    }
    private void setEnabledStatus()
    {
        boolean enabled = false;
        if (fileNameText.getText().length() > 0)
        {
            enabled = true;
        }
        deleteButton.setEnabled(enabled);
        startButton.setEnabled(enabled);
        endButton.setEnabled(enabled);
        startSpinner.setEnabled(enabled);
        endSpinner.setEnabled(enabled);
        playButton.setEnabled(enabled);
    }
}
