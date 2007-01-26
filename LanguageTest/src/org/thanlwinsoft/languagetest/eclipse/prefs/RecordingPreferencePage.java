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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
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

    private StringFieldEditor argumentEditor = null;
    private FileFieldEditor fileEditor = null;
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
        fileEditor = new FileFieldEditor(SoundRecorder.MP3_CONVERTER_PREF, 
                MessageUtil.getString("MP3ConverterProgram"), 
                false, getFieldEditorParent());
        if (Platform.getOS().equals(Platform.OS_WIN32))
        {
            fileEditor.setFileExtensions(new String[] {".exe"});
        }
        fileEditor.setEmptyStringAllowed(true);
        addField(fileEditor); 
        argumentEditor =
            new StringFieldEditor(SoundRecorder.MP3_CONV_ARG_PREF, 
                    MessageUtil.getString("MP3ConverterArguments"), 
                    SWT.NONE, getFieldEditorParent());
        argumentEditor.setEmptyStringAllowed(true);
        addField(argumentEditor);
        
       
        overwriteEditor = new BooleanFieldEditor(SoundRecorder.OVERWRITE_PREF_KEY, 
                MessageUtil.getString("OverwriteRecordingsWithoutAsking"), 
                SWT.NONE, getFieldEditorParent());
        addField(overwriteEditor);
        
        adjustGridLayout();
        
    }

    
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#adjustGridLayout()
     */
    protected void adjustGridLayout()
    {
        adjustTextControl(fileEditor);
        adjustTextControl(argumentEditor);
    }



    private void adjustTextControl(StringFieldEditor editor)
    {
        Text text = (Text)editor.getTextControl(getFieldEditorParent());
        
        if (text.getLayoutData() instanceof GridData)
        {
            GridData gd = ((GridData)text.getLayoutData());
            gd.widthHint = 300;
            text.setLayoutData(gd);
        }
        
    }
    
    protected void initializeDefaults()
    {
        getPreferenceStore().setDefault(SoundRecorder.MP3_CONVERTER_PREF, "lame");
        getPreferenceStore().setDefault(SoundRecorder.MP3_CONV_ARG_PREF,
            " -V2 \"{0}\" \"{1}\"");
        getPreferenceStore().setDefault(SoundRecorder.OVERWRITE_PREF_KEY, false);
        
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
