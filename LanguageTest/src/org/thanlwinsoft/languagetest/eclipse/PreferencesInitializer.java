package org.thanlwinsoft.languagetest.eclipse;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;



public class PreferencesInitializer extends AbstractPreferenceInitializer
{
    public static final int DEFAULT_FONT_SIZE = 12;
    public static final String TEST_FONT_SIZE = "TestFontSize";
    
    private ScopedPreferenceStore prefStore = null;
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
        
        ConfigurationScope configScope = new ConfigurationScope();
        prefStore = new ScopedPreferenceStore(configScope, 
            "org.thanlwinsoft.languagetest");
//        IWorkspace workspace = ResourcesPlugin.getWorkspace();
//        File pluginStateLocation = 
//            LanguageTestPlugin.getDefault().getStateLocation().toFile();
//        String wsParent = workspace.getRoot().getLocation().toFile().getParent();
//        String installArea = System.getProperty("osgi.install.area");
        
        prefStore.setDefault(TEST_FONT_SIZE, DEFAULT_FONT_SIZE);
    }

}
