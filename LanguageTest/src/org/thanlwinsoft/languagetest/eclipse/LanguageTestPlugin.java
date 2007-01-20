/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

/**
 * @author keith
 *
 */
public class LanguageTestPlugin extends AbstractUIPlugin
{
    public static final String ID = "org.thanlwinsoft.languagetest";
//  The shared instance.
    private static LanguageTestPlugin plugin;
    private static ScopedPreferenceStore prefStore = null;
    
    /**
     * The constructor.
     */
    public LanguageTestPlugin() 
    {
        plugin = this;
    }

    /**
     * This method is called upon plug-in activation
     */
    public void start(BundleContext context) throws Exception 
    {
        super.start(context);
        ISaveParticipant saveParticipant = new LangTestWorkspaceSaveParticipant();
        ISavedState lastState =
           ResourcesPlugin.getWorkspace().addSaveParticipant(this, saveParticipant);
        if (lastState == null)
           return;
        IPath location = lastState.lookup(new Path("save"));
        if (location == null)
           return;
        // the plugin instance should read any important state from the file.
        File f = getStateLocation().append(location).toFile();
        readStateFrom(f);

    }
    
    protected void readStateFrom(File target) 
    {
    }
    protected void writeImportantState(File target) 
    {
        
    }


    /**
     * This method is called when the plug-in is stopped
     */
    public void stop(BundleContext context) throws Exception 
    {
        try
        {
            prefStore.save();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        super.stop(context);
        plugin = null;
    }

    /**
     * Returns the shared instance.
     */
    public static LanguageTestPlugin getDefault() 
    {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path.
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) 
    {
        return AbstractUIPlugin.imageDescriptorFromPlugin("LanguageTest", path);
    }
    
    public static void log(int status, String msg)
    {
        
        LanguageTestPlugin.log(status, msg, null);
    }
    
    public static void log(int status, String msg, Throwable exception)
    {
        if (msg == null)
        {
            msg = "Unknown error ";
        }
        try
        {
        Status s = new Status(status, ID, 
                Status.OK, msg, exception);
        getDefault().getLog().log(s);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("[" + status + "]" + msg);
            e.printStackTrace();
        }
    }
    public static Font getFont(FontData fd)
    {
        FontRegistry r = JFaceResources.getFontRegistry();
        if (!r.hasValueFor(fd.toString()))
        {
            r.put(fd.toString(), new FontData[]{fd});
        }
        return r.get(fd.toString());
    }
    
    public static ScopedPreferenceStore getPrefStore()
    {
        if (prefStore == null)
        {
            ConfigurationScope configScope = new ConfigurationScope();
            prefStore = new ScopedPreferenceStore(configScope, ID);
        }
        return prefStore;
    }
}
