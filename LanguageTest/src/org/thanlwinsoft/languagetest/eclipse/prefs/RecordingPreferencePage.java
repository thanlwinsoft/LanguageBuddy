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
package org.thanlwinsoft.languagetest.eclipse.prefs;

import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.thanlwinsoft.eclipse.widgets.SoundRecorder;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;

/**
 * @author keith
 *
 */
public class RecordingPreferencePage extends FieldEditorPreferencePage 
implements IWorkbenchPreferencePage
{

    private StringFieldEditor wavToMp3ArgumentEditor = null;
    private FileFieldEditor wavToMp3FileEditor = null;
    private StringFieldEditor mp3ToWavArgumentEditor = null;
    private FileFieldEditor mp3ToWavFileEditor = null;
    
    private BooleanFieldEditor overwriteEditor = null;
    /**
     * @param arg0
     */
    public RecordingPreferencePage()
    {
        super(FieldEditorPreferencePage.GRID);
        
    }
    
    
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite parent)
    {
        return super.createContents(parent);
    }



    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    protected void createFieldEditors()
    {
        Label introLabel = new Label(getFieldEditorParent(), SWT.WRAP);
        introLabel.setText(MessageUtil.getString("ConverterIntro"));
        GridData gd = new GridData();
        gd.horizontalSpan = 3;
        introLabel.setLayoutData(gd);
        addConverterFieldEditors(SoundRecorder.WAVTOMP3_CONVERTER_PREF,
                "WavToMP3ConverterProgram",
                SoundRecorder.WAVTOMP3_CONV_ARG_PREF,
                "WavToMP3ConverterArguments");
        addConverterFieldEditors(SoundRecorder.MP3TOWAV_CONVERTER_PREF,
                "MP3ToWavConverterProgram",
                SoundRecorder.MP3TOWAV_CONV_ARG_PREF,
                "MP3ToWavConverterArguments");
        addConverterFieldEditors(SoundRecorder.WAVTOOGG_CONVERTER_PREF,
                "WavToOggConverterProgram",
                SoundRecorder.WAVTOOGG_CONV_ARG_PREF,
                "WavToOggConverterArguments");
        addConverterFieldEditors(SoundRecorder.OGGTOWAV_CONVERTER_PREF,
                "OggToWavConverterProgram",
                SoundRecorder.OGGTOWAV_CONV_ARG_PREF,
                "OggToWavConverterArguments");
        
        overwriteEditor = new BooleanFieldEditor(SoundRecorder.OVERWRITE_PREF_KEY, 
                MessageUtil.getString("OverwriteRecordingsWithoutAsking"), 
                SWT.NONE, getFieldEditorParent());
        addField(overwriteEditor);
        overwriteEditor.fillIntoGrid(getFieldEditorParent(), 3);
        
        adjustGridLayout();
        
    }

    private void addConverterFieldEditors(String convPref, String convLabelId,
                                          String argPref, String argLabelId)
    {
        wavToMp3FileEditor = new FileFieldEditor(convPref, 
                MessageUtil.getString(convLabelId), 
                false, getFieldEditorParent());
        if (Platform.getOS().equals(Platform.OS_WIN32))
        {
            wavToMp3FileEditor.setFileExtensions(new String[] {".exe"});
        }
        wavToMp3FileEditor.setEmptyStringAllowed(true);
        addField(wavToMp3FileEditor); 
        wavToMp3FileEditor.fillIntoGrid(getFieldEditorParent(), 3);
        wavToMp3ArgumentEditor =
            new StringFieldEditor(argPref, 
                    MessageUtil.getString(argLabelId), 
                    SWT.NONE, getFieldEditorParent());
        wavToMp3ArgumentEditor.setEmptyStringAllowed(true);
        
        addField(wavToMp3ArgumentEditor);
        wavToMp3ArgumentEditor.fillIntoGrid(getFieldEditorParent(), 3);
        
        adjustTextControl(wavToMp3FileEditor);
        adjustTextControl(wavToMp3ArgumentEditor);
        
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#adjustGridLayout()
     */
    protected void adjustGridLayout()
    {
        Layout layout = getFieldEditorParent().getLayout();
        if (layout instanceof GridLayout)
        {
            GridLayout gl = (GridLayout)layout;
            gl.numColumns = 3;
        }
    }



    private void adjustTextControl(StringFieldEditor editor)
    {
        Text text = (Text)editor.getTextControl(getFieldEditorParent());
        
        if (text.getLayoutData() instanceof GridData)
        {
            GridData gd = ((GridData)text.getLayoutData());
            gd.widthHint = 150;
            text.setLayoutData(gd);
        }
        
    }
    
    public static void initializeDefaults()
    {
        String exePath = "";
        String exeExt = "";
        if (Platform.getOS().equals(Platform.OS_WIN32))
        {
           exeExt = ".exe";
        }
        else if (Platform.getOS().equals(Platform.OS_LINUX))
        {
            exePath = "/usr/bin/";
        }
        ScopedPreferenceStore prefStore = LanguageTestPlugin.getPrefStore();
        prefStore.setDefault(SoundRecorder.WAVTOMP3_CONVERTER_PREF, 
                exePath + "lame" + exeExt);
        prefStore.setDefault(SoundRecorder.WAVTOMP3_CONV_ARG_PREF,
            " -V2 {0} {1}");
        prefStore.setDefault(SoundRecorder.MP3TOWAV_CONVERTER_PREF, 
                exePath + "lame" + exeExt);
        prefStore.setDefault(SoundRecorder.MP3TOWAV_CONV_ARG_PREF,
            " --decode {0} {1}");
        prefStore.setDefault(SoundRecorder.WAVTOOGG_CONVERTER_PREF, 
                exePath + "oggenc" + exeExt);
        prefStore.setDefault(SoundRecorder.WAVTOOGG_CONV_ARG_PREF,
            " -q 5 {0} -o {1}");
        prefStore.setDefault(SoundRecorder.OGGTOWAV_CONVERTER_PREF, 
                exePath + "oggdec" + exeExt);
        prefStore.setDefault(SoundRecorder.OGGTOWAV_CONV_ARG_PREF,
            " {0} -o {1}");
        prefStore.setDefault(SoundRecorder.OVERWRITE_PREF_KEY, false);
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    protected void performApply()
    {
        super.performApply();
        try
        {
            LanguageTestPlugin.getPrefStore().save();
        } 
        catch (IOException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, e.getLocalizedMessage(), e);
        }
    }



    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
     */
    public boolean performOk()
    {
        try
        {
            LanguageTestPlugin.getPrefStore().save();
        } 
        catch (IOException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, e.getLocalizedMessage(), e);
        }
        return super.performOk();
    }



    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
     */
    protected IPreferenceStore doGetPreferenceStore()
    {
        return LanguageTestPlugin.getPrefStore();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench)
    {
        initializeDefaults();
    }



    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performDefaults()
     */
    protected void performDefaults()
    {
        initializeDefaults();
        super.performDefaults();
    }

}
