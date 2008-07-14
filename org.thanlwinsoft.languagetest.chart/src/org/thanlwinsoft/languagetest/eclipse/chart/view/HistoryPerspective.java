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
package org.thanlwinsoft.languagetest.eclipse.chart.view;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.thanlwinsoft.languagetest.chart.view.ChartHistoryView;
import org.thanlwinsoft.languagetest.eclipse.views.TestHistoryView;

/**
 * @author keith
 *
 */
public class HistoryPerspective implements IPerspectiveFactory
{
    public final static String ID = 
        "org.thanlwinsoft.languagetest.eclipse.HistoryPerspective";
    public final static String FOLDER_LEFT = 
        "org.thanlwinsoft.languagetest.FolderLeft";
    public final static String FOLDER_RIGHT = 
        "org.thanlwinsoft.languagetest.FolderRight";
    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
     */
    public void createInitialLayout(IPageLayout layout)
    {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        IFolderLayout folderLeft = layout.createFolder(FOLDER_LEFT,
            IPageLayout.LEFT, 0.25f, editorArea);
        folderLeft.addView(TestHistoryView.ID);
    
        IFolderLayout folderRight = layout.createFolder(FOLDER_RIGHT,
            IPageLayout.RIGHT, 0.75f, editorArea);
        folderRight.addView(ChartHistoryView.ID);
    }

}
