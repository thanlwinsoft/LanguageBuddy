/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse;

import java.io.File;

import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
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
}
