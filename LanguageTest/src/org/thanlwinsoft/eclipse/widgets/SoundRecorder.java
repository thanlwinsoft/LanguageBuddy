/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
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
/**
 * 
 */
package org.thanlwinsoft.eclipse.widgets;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.thanlwinsoft.languagetest.eclipse.views.TestView;
import org.thanlwinsoft.languagetest.language.test.XmlBeansTestModule;
import org.thanlwinsoft.languagetest.sound.LineController;
import org.thanlwinsoft.languagetest.sound.Recorder;
import org.thanlwinsoft.schemas.languagetest.TestItemType;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;

/**
 * Composite containing a sound recorder
 * the actual playback is done using the player in the TestView ControlPanel.
 * @author keith
 *
 */
public class SoundRecorder extends Composite
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
    
    private static final String [] EXTENSIONS = new String[] { ".wav", ".mp3", ".ogg" };
    private static final String [] FILTERS = new String[] { "*.wav", "*.mp3", "*.ogg" };
    private static final String [] TYPES = new String[] { "Wave .wav", "MP3 .mp3", "Vorbis .ogg" };
    private Text fileNameText = null;
    private Button browseButton = null;
    private Combo encodingCombo = null;
    private ComboViewer comboViewer = null;
    private final static int MAX_SOUND_FILES = 10000;
    public static final String MP3_CONVERTER_PREF = "MP3Converter";
    public static final String MP3_CONV_ARG_PREF = "MP3ConverterArguments";
    
    public final static String OVERWRITE_PREF_KEY = "OverwriteAudioNoPrompt";
    public SoundRecorder(Composite parent, int style)
    {
        super(parent, style);
        initialize();
    }

    private void initialize()
    {
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData = new GridData();
        gridData.horizontalSpan = 4;
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.widthHint = -1;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        recordButton = new Button(this, SWT.NONE);
        recordButton.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/org/thanlwinsoft/languagetest/sound/icons/recordMini.png")));
        recordButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                record();
            }
        });
        stopButton = new Button(this, SWT.NONE);
        stopButton.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/org/thanlwinsoft/languagetest/sound/icons/stopMini.png")));
        stopButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                stop();
            }
        });
        recordScale = new Scale(this, SWT.NONE);
        recordScale.setPageIncrement(10);
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
        deleteButton = new Button(this, SWT.NONE);
        deleteButton.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/org/thanlwinsoft/languagetest/sound/icons/rubbishBin16.png")));
        deleteButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                System.out.println("widgetSelected()"); // TODO Auto-generated Event stub widgetSelected()
            }
        });
        startButton = new Button(this, SWT.LEFT);
        startSpinner = new Spinner(this, SWT.NONE);
        endSpinner = new Spinner(this, SWT.NONE);
        endButton = new Button(this, SWT.RIGHT);
        // Visual Editor doesn't like direct calls to MessageUtil in setText
        createEncodingCombo();
        GridData fileNameGridData = new GridData();
        fileNameGridData.horizontalSpan = 3;
        fileNameGridData.horizontalAlignment = GridData.FILL;
        fileNameText = new Text(this, SWT.BORDER);
        fileNameText.setLayoutData(fileNameGridData);
        browseButton = new Button(this, SWT.NONE);
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
        startButton.setLayoutData(gridData1);
        startButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                System.out.println("widgetSelected()"); // TODO Auto-generated Event stub widgetSelected()
            }
        });
        s = MessageUtil.getString("PlaybackEndButton");
        endButton.setText(s);
        endButton.setLayoutData(gridData2);
        endButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                System.out.println("widgetSelected()"); // TODO Auto-generated Event stub widgetSelected()
            }
        });
        s = MessageUtil.getString("Browse");
        browseButton.setText(s);
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
            setTestItem();
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
                recorder = new Recorder(lineController);
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
                final String [] buttons = {
                    MessageUtil.getString("Yes"),
                    MessageUtil.getString("No"),
                    MessageUtil.getString("Cancel")
                };
                MessageDialogWithToggle dialog = 
                    MessageDialogWithToggle.openYesNoCancelQuestion( 
                                    getShell(), MessageUtil.getString("OverwriteRecordingTitle"),
                                MessageUtil.getString("OverwriteRecorderingMessage", 
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
    
    protected void setTestItem()
    {
        IEditorPart editor = 
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor instanceof TestModuleEditor)
        {
            TestModuleEditor tme = (TestModuleEditor)editor;
            if (tme.getEditorInput() instanceof IFileEditorInput)
            {
                TestItemEditor tie = (TestItemEditor)tme.getAdapter(TestItemEditor.class);
                
                if (tie != null && tie.getSelection() instanceof IStructuredSelection)
                {
                    TestItemType [] items = tie.getSelectedItems();
                    if (items.length > 0)
                    {
                        TestItemType ti = items[0];
                        if (ti.getSoundFile() == null)
                                ti.addNewSoundFile();
                        ti.getSoundFile().setStringValue(fileNameText.getText());
                        tme.setDirty(true);
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
        String fileName = "recording.wav";
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
                    TestItemType [] items = tie.getSelectedItems();
                    if (items.length > 0 && items[0].sizeOfNativeLangArray() > 0)
                    {
                        String nativeText = items[0].getNativeLangArray(0).getStringValue();
                        IPath path = new Path(fileName);
                        if (moduleFile.getParent().getFullPath().isValidSegment(nativeText))
                        {
                            fileName = nativeText + EXTENSIONS[WAV];
                        }
                    }
                }
            }
        }
        if (fileNameText.getText().length() > 0)
        {
            // should we use portable string or osstring?
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
            IPath folderPath = new Path(moduleBaseName + XmlBeansTestModule.FOLDER_EXT);
            IFolder folder = moduleParent.getFolder(folderPath);
            if (!folder.exists())
            {
                IJobManager jobMan = Platform.getJobManager();
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
                    fileName = moduleBaseName + i + EXTENSIONS[WAV];
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
     * 
     */
    protected void play()
    {
        SoundPlayer player = getSoundPlayer();
        if (player != null)
        {
            player.setFile(getAudioFile().getAbsolutePath());
            player.play();
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
                int fileEncoding = getFileEncoding();
                int selectedEncoding = encodingCombo.getSelectionIndex();
                if (fileEncoding != selectedEncoding)
                {
                    switch (selectedEncoding)
                    {
                    case WAV:
                        break;
                    case MP3:
                        convertToMp3();
                        break;
                    case OGG:
                        convertToOgg();
                        break;
                    }
                }
            }});
        comboViewer = new ComboViewer(encodingCombo);
        comboViewer.add(TYPES);
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
     * 
     */
    protected void convertToMp3()
    {
        final String converter = 
            LanguageTestPlugin.getPrefStore().getString(MP3_CONVERTER_PREF);
        String arguments = 
            LanguageTestPlugin.getPrefStore().getString(MP3_CONV_ARG_PREF);
        IPath path = new Path(getAudioFile().getAbsolutePath());
        final IPath newPath = path.removeFileExtension();
        newPath.addFileExtension(EXTENSIONS[MP3]);
        final String allArgs = MessageFormat.format(arguments, new Object[] 
           {path.toOSString(),
            newPath.toOSString()});
        final Display display = getShell().getDisplay();
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
                        Process p = Runtime.getRuntime().exec(converter+allArgs);
                        //Process p = Runtime.getRuntime().exec(new String[] {converter, allArgs});
                        retValue = p.waitFor();
                        BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        
                        while (br.ready())
                        {
                            errorBuilder.append(br.readLine());
                        }
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
                        }
                        else
                        {
                            fileNameText.setText(newPath.toPortableString());
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

    /**
     * 
     */
    protected void convertToOgg()
    {
        
        
    }

}
