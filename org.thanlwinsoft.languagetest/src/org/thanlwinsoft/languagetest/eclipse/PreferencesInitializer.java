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
package org.thanlwinsoft.languagetest.eclipse;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.thanlwinsoft.languagetest.eclipse.prefs.RecordingPreferencePage;



public class PreferencesInitializer extends AbstractPreferenceInitializer
{
    public static final int DEFAULT_FONT_SIZE = 12;
    public static final String TEST_FONT_SIZE = "TestFontSize";
    
    private IPreferenceStore prefStore = null;
    public PreferencesInitializer()
    {
        
    }
    public IPreferenceStore getPrefStore()
    {
        if (prefStore == null) initializeDefaultPreferences();
        return prefStore;
    }
    
    public void initializeDefaultPreferences()
    {
//        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        prefStore = LanguageTestPlugin.getPrefStore();
//        ConfigurationScope configScope = new ConfigurationScope();
//        prefStore = new ScopedPreferenceStore(configScope, 
//            "org.thanlwinsoft.languagetest");
//        IWorkspace workspace = ResourcesPlugin.getWorkspace();
//        File pluginStateLocation = 
//            LanguageTestPlugin.getDefault().getStateLocation().toFile();
//        String wsParent = workspace.getRoot().getLocation().toFile().getParent();
//        String installArea = System.getProperty("osgi.install.area");
        
        prefStore.setDefault(TEST_FONT_SIZE, DEFAULT_FONT_SIZE);
        RecordingPreferencePage.initializeDefaults();
    }

}
