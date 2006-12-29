/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.wizards;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author keith
 *
 */
public class StartTestWizard extends Wizard
{
    private ModuleSelectionPage moduleSelectionPage = null;  //  @jve:decl-index=0:
    private TestTypePage testTypePage = null;
    
    public StartTestWizard()
    {
        super();
    }
    /**
     * Adding the page to the wizard.
     */
    public void addPages()
    {
        testTypePage = new TestTypePage("Test Type");
        moduleSelectionPage = new ModuleSelectionPage("Select Modules");
        addPage(testTypePage);
        addPage(moduleSelectionPage);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish()
    {
        // TODO Auto-generated method stub
        return false;
    }

}
