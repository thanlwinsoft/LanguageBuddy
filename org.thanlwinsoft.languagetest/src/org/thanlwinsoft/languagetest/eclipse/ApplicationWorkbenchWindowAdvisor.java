/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/ApplicationWorkbenchWindowAdvisor.java $
 *  Revision        $LastChangedRevision: 1238 $
 *  Last Modified:  $LastChangedDate: 2008-07-14 13:49:25 +0700 (Mon, 14 Jul 2008) $
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


//import org.eclipse.core.resources.IWorkspaceRoot;
//import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
//import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.cheatsheets.ICheatSheetViewer;
import org.eclipse.ui.cheatsheets.OpenCheatSheetAction;
import org.eclipse.ui.views.navigator.ResourceNavigator;
import org.osgi.service.prefs.Preferences;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.eclipse.EclipseToJavaPrefAdapter;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(750, 550));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowPerspectiveBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);
        configurer.setTitle(MessageUtil.getString("dialogTitle"));
        IPreferencesService service = Platform.getPreferencesService();
        if (service != null)
        {
            ConfigurationScope configScope = new ConfigurationScope();
            Preferences configurationNode = 
                configScope.getNode("org.thanlwinsoft.languagetest");
            try
            {
                //new org.thanlwinsoft.doccharconvert.Config
                    new EclipseToJavaPrefAdapter(configurationNode);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        ImageDescriptor idPlay = LanguageTestPlugin.getImageDescriptor
            ("/icons/playMini.png");
        ImageDescriptor idPause = LanguageTestPlugin.getImageDescriptor
            ("/icons/pauseMini.png");
        ImageDescriptor idStop = LanguageTestPlugin.getImageDescriptor
            ("/icons/stopMini.png");
        ImageRegistry ir = LanguageTestPlugin.getDefault().getImageRegistry();
        if (ir != null)
        {
            ir.put("Play", idPlay);
            ir.put("Pause", idPause);
            ir.put("Stop", idStop);
        }
        else
        {
            LanguageTestPlugin.log(IStatus.ERROR, "No Image Registry!");
        }
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createWindowContents(org.eclipse.swt.widgets.Shell)
     */
    public void createWindowContents(Shell shell)
    {
        super.createWindowContents(shell);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
     */
    public void postWindowOpen()
    {
        super.postWindowOpen();

        IWorkbenchWindow w = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (w != null)
        {
			try
			{
	            IViewPart nav;// = w.getActivePage().findView(EditPerspective.NAVIGATOR);
	            nav = w.getActivePage().showView(EditPerspective.NAVIGATOR,
						null, IWorkbenchPage.VIEW_VISIBLE);
	            if (nav instanceof ResourceNavigator)
	            {
	            	// hard coded patterns, since they don't seem to use the 
	            	// extension point values for a custom navigator
	                ResourceNavigator rn = (ResourceNavigator)nav;
	                String [] filters = {
	                		"*.project",
	                		"*.languages.xml",
	                		"*.xsl",
	                		"*.settings"
	                };
	                rn.getPatternFilter().setPatterns(filters);
	                String [] p = rn.getPatternFilter().getPatterns();
	                rn.setFiltersPreference(filters);
	            }
	            IViewPart cheatSheet = null;
				cheatSheet = w.getActivePage().showView("org.eclipse.ui.cheatsheets.views.CheatSheetView",
						null, IWorkbenchPage.VIEW_VISIBLE);
				if (cheatSheet != null)
	            {
					final IAction a = new OpenCheatSheetAction("org.thanlwinsoft.languagetest.cheatsheets.cheatsheet");
					w.getShell().getDisplay().asyncExec(new Runnable() {

						@Override
						public void run()
						{
							a.run();
						}});
					
	            	//ICheatSheetViewer csv = (ICheatSheetViewer)cheatSheet;
	            	//csv.setInput("org.thanlwinsoft.languagetest.cheatsheets.cheatsheet");
	            }
			}
			catch (PartInitException e)
			{
				LanguageTestPlugin.log(IStatus.WARNING, "Failed to show cheat sheet",e);
			}
            
        }
    }

    

    
}
