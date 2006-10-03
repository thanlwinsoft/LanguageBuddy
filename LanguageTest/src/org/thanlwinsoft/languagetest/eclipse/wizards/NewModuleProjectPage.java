/**
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
