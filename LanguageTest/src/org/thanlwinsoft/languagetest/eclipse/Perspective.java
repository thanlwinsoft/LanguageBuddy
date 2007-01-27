package org.thanlwinsoft.languagetest.eclipse;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.thanlwinsoft.languagetest.eclipse.views.RecordingView;


public class Perspective implements IPerspectiveFactory 
{
    public final static String FOLDER_BOTTOM = 
        "org.thanlwinsoft.languagetest.FolderBottom";
    public final static String FOLDER_LEFT = 
        "org.thanlwinsoft.languagetest.FolderLeft";
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
        //layout.addStandaloneViewPlaceholder(USER_RESULTS,  
        //        IPageLayout.RIGHT, 0.8f, editorArea, true);
        IFolderLayout folderLeft = layout.createFolder(FOLDER_LEFT,
                IPageLayout.LEFT, 0.25f, editorArea);
        folderLeft.addView("org.eclipse.ui.views.ResourceNavigator");
        folderLeft.addView(TEST_HISTORY_VIEW);
	}
    
    
}
