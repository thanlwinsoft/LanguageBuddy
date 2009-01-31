/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/ApplicationActionBarAdvisor.java $
 *  Revision        $LastChangedRevision: 1388 $
 *  Last Modified:  $LastChangedDate: 2009-01-31 19:32:00 +0700 (Sat, 31 Jan 2009) $
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

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.thanlwinsoft.languagetest.eclipse.WelcomeAction;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.views.MetaDataView;
import org.thanlwinsoft.languagetest.eclipse.views.RecordingView;
import org.thanlwinsoft.languagetest.eclipse.views.TestView;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;


public class ApplicationActionBarAdvisor extends ActionBarAdvisor 
{
    //  Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
    private IWorkbenchAction exitAction;
    private IWorkbenchAction aboutAction;
    private IWorkbenchAction cutAction;
    private IWorkbenchAction copyAction;
    private IWorkbenchAction pasteAction;
    private IWorkbenchAction newAction;
    private IWorkbenchAction newWizardAction;
    private IWorkbenchAction preferencesAction;
    private IWorkbenchAction undoAction;
    private IWorkbenchAction saveAction;
    private IWorkbenchAction saveAllAction;
    private IWorkbenchAction closeAction;
    private IWorkbenchAction closeAllAction;
    private IWorkbenchAction showViewAction;
    private IWorkbenchAction showTestViewAction;
    private IWorkbenchAction showRecordingViewAction;
    private IWorkbenchAction showMetaDataViewAction;
    
    private IWorkbenchAction newWindowAction;
    //private OpenViewAction openViewAction;
    //private Action messagePopupAction;
    //private Action conversionWizardAction;
    private IWorkbenchAction newEditorAction;
    private ShowViewAction showNavigatorViewAction;
    private IWorkbenchAction exportAction;
    
    private WelcomeAction welcomeAction;
	private IWorkbenchAction printAction;
    
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
//      Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        
        register(ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window));
        
        copyAction = ActionFactory.COPY.create(window);
        register(copyAction);
        
        cutAction = ActionFactory.CUT.create(window);
        register(cutAction);
        
        pasteAction = ActionFactory.PASTE.create(window);
        register(pasteAction);
        
        printAction = ActionFactory.PRINT.create(window);
        register(printAction);
        
        newWizardAction = ActionFactory.NEW_WIZARD_DROP_DOWN.create(window);
        register(newWizardAction);
        
        newAction = ActionFactory.NEW.create(window);
        register(newAction);
        
        undoAction = ActionFactory.UNDO.create(window);
        register(undoAction);
        
        saveAction = ActionFactory.SAVE.create(window);
        register(saveAction);
        
        saveAllAction = ActionFactory.SAVE_ALL.create(window);
        register(saveAllAction);
        
        exportAction = ActionFactory.EXPORT.create(window);
        register(exportAction);
        
        closeAction = ActionFactory.CLOSE.create(window);
        register(closeAction);
        
        closeAllAction = ActionFactory.CLOSE_ALL.create(window);
        register(closeAllAction);
        
        preferencesAction = ActionFactory.PREFERENCES.create(window); 
        register(preferencesAction);
        
        showViewAction = ActionFactory.SHOW_VIEW_MENU.create(window);
        register(showViewAction);
        newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
        register(newWindowAction);
        
        newEditorAction = ActionFactory.NEW_EDITOR.create(window);
        register(newEditorAction);
        
        showTestViewAction = new ShowViewAction(EditPerspective.ID, TestView.ID, 
                        MessageUtil.getString("ShowTestView"));
//        showHistoryViewAction = new ShowViewAction(HistoryPerspective.ID, 
//             TestHistoryView.ID, MessageUtil.getString("ShowHistoryView"));
        showNavigatorViewAction = new ShowViewAction(
            EditPerspective.ID, "org.thanlwinsoft.languagetest.Navigator", 
                        MessageUtil.getString("ShowNavigatorView"));
        showRecordingViewAction = new ShowViewAction(EditPerspective.ID, 
            RecordingView.ID, MessageUtil.getString("ShowRecordingView"));
        
//        showHistoryGraphViewAction = new ShowViewAction(
//                HistoryPerspective.ID, ChartHistoryView.ID, 
//                MessageUtil.getString("ShowHistoryGraphView"));
        
        showMetaDataViewAction = new ShowViewAction(EditPerspective.ID, 
            MetaDataView.ID, MessageUtil.getString("ShowMetaDataView"));
        
        register(showTestViewAction);
        //register(showHistoryViewAction);
        register(showNavigatorViewAction);
        register(showRecordingViewAction);
        //register(showHistoryGraphViewAction);
        
        welcomeAction = new WelcomeAction();
        register(welcomeAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) 
    {
        MenuManager fileMenu = 
            new MenuManager(MessageUtil.getString("Menu_File"), 
                            IWorkbenchActionConstants.M_FILE);
        
        MenuManager editMenu = 
            new MenuManager(MessageUtil.getString("Menu_Edit"), 
                            IWorkbenchActionConstants.M_EDIT);
        MenuManager projectMenu = 
            new MenuManager(MessageUtil.getString("Menu_Window"), 
                            IWorkbenchActionConstants.M_PROJECT);
        
        MenuManager windowMenu = 
            new MenuManager(MessageUtil.getString("Menu_Window"), 
                            IWorkbenchActionConstants.M_WINDOW);
        MenuManager helpMenu = 
            new MenuManager(MessageUtil.getString("Menu_Help"), 
                            IWorkbenchActionConstants.M_HELP);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(projectMenu);
        GroupMarker gm = new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS);
        menuBar.add(gm);
        menuBar.add(windowMenu);
        
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(helpMenu);
        
        // File
        fileMenu.add(saveAction);
        fileMenu.add(saveAllAction);
        fileMenu.add(closeAction);
        fileMenu.add(closeAllAction);
        fileMenu.add(printAction);
        fileMenu.add(exportAction);
        
        fileMenu.add(new GroupMarker("File/additions"));
        fileMenu.add(new GroupMarker("org.thanlwinsoft.languagetest.PDFChartGenerator"));
        fileMenu.add(new Separator());
        //fileMenu.add(messagePopupAction);
        //fileMenu.add(openViewAction);
        fileMenu.add(newWizardAction);
        fileMenu.add(newAction);
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        
        editMenu.add(undoAction);
        editMenu.add(cutAction);
        editMenu.add(copyAction);
        editMenu.add(pasteAction);
        editMenu.add(new GroupMarker("org.eclipse.ui.edit.text.gotoLastEditPosition"));


        windowMenu.add(preferencesAction);
        //windowMenu.add(showViewAction);
        //windowMenu.add(newWindowAction);
        windowMenu.add(newEditorAction);
        windowMenu.add(showTestViewAction);
        //windowMenu.add(showHistoryViewAction);
        windowMenu.add(showNavigatorViewAction);
        windowMenu.add(showRecordingViewAction);
        //windowMenu.add(showHistoryGraphViewAction);
        windowMenu.add(showMetaDataViewAction);
        // Help
        helpMenu.add(welcomeAction);
        helpMenu.add(aboutAction);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface.action.ICoolBarManager)
     */
    //@Override
    protected void fillCoolBar(ICoolBarManager coolBar)
    {
        //CoolBarManager coolBar = new CoolBarManager();
        IToolBarManager toolBar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolBar, "main"));
        toolBar.add(saveAction);
        toolBar.add(undoAction);
        toolBar.add(cutAction);
        toolBar.add(copyAction);
        toolBar.add(pasteAction);
        toolBar.add(printAction);
        toolBar.add(new GroupMarker("Bar/additions"));
        //toolBar.add(preferencesAction);
    }
    
}
