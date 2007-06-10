/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/LanguageTest/src/org/thanlwinsoft/languagetest/eclipse/LanguageTestPlugin.java $
 *  Revision        $LastChangedRevision: 855 $
 *  Last Modified:  $LastChangedDate: 2007-06-10 07:02:09 +0700 (Sun, 10 Jun 2007) $
 *  Last Change by: $LastChangedBy: keith $
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
        //System.setProperty("STANDALONE", "true");
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
//        try
//        {
//            prefStore.save();
//        }
//        catch (IOException e)
//        {
//            System.out.println(e);
//        }
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
        return AbstractUIPlugin.imageDescriptorFromPlugin(ID, path);
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
