/* -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/wizards/NewModuleProjectPage.java $
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
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.thanlwinsoft.languagetest.MessageUtil;

/**
 * @author keith
 *
 */
public class NewModuleProjectPage extends WizardNewProjectCreationPage
{
    public NewModuleProjectPage()
    {
        super("LanguageModules");
        setTitle(MessageUtil.getString("CreateProject"));
        setDescription(MessageUtil.getString("CreateProjectDesc"));
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.dialogs.WizardNewProjectCreationPage#validatePage()
     */
    protected boolean validatePage()
    {
        // TODO Auto-generated method stub
        return super.validatePage();
    }

    
    
}
