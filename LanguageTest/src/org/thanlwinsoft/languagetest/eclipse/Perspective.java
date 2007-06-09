/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/LanguageTest/src/org/thanlwinsoft/languagetest/eclipse/Perspective.java $
 *  Revision        $LastChangedRevision: 852 $
 *  Last Modified:  $LastChangedDate: 2007-06-09 16:02:23 +0700 (Sat, 09 Jun 2007) $
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

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.thanlwinsoft.languagetest.eclipse.views.ChartHistoryView;
import org.thanlwinsoft.languagetest.eclipse.views.MetaDataView;
import org.thanlwinsoft.languagetest.eclipse.views.RecordingView;


public class Perspective implements IPerspectiveFactory 
{
    public final static String FOLDER_BOTTOM = 
        "org.thanlwinsoft.languagetest.FolderBottom";
    public final static String FOLDER_LEFT = 
        "org.thanlwinsoft.languagetest.FolderLeft";
    public final static String FOLDER_RIGHT = 
        "org.thanlwinsoft.languagetest.FolderRight";
    public final static String TEST_VIEW = 
        "org.thanlwinsoft.languagetest.TestView";
    public final static String RECORDING = 
        "org.thanlwinsoft.languagetest.Recording";
    public final static String USER_RESULTS = 
        "org.thanlwinsoft.languagetest.UserResults";
    public final static String NAVIGATOR = 
        "org.thanlwinsoft.languagetest.Navigator";
    public final static String TEST_HISTORY_VIEW = 
        "org.thanlwinsoft.languagetest.TestHistoryView";
    public void createInitialLayout(IPageLayout layout) 
    {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(true);
        layout.getViewLayout(layout.getEditorArea()).setCloseable(false);
        
        IFolderLayout folderBottom = layout.createFolder(FOLDER_BOTTOM, 
                IPageLayout.BOTTOM, 0.7f, editorArea);
        
        folderBottom.addView(TEST_VIEW);
        folderBottom.addView(RecordingView.ID);
        folderBottom.addView(MetaDataView.ID);
        //layout.addStandaloneViewPlaceholder(USER_RESULTS,  
        //        IPageLayout.RIGHT, 0.8f, editorArea, true);
        IFolderLayout folderLeft = layout.createFolder(FOLDER_LEFT,
                IPageLayout.LEFT, 0.25f, editorArea);
        folderLeft.addView("org.eclipse.ui.views.ResourceNavigator");
        folderLeft.addView(TEST_HISTORY_VIEW);
        
        IPlaceholderFolderLayout folderRight = layout.createPlaceholderFolder(FOLDER_RIGHT,
                IPageLayout.RIGHT, 0.3f, editorArea);
        folderRight.addPlaceholder(ChartHistoryView.ID);
	}
    
    
}
