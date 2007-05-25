/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.ui.PlatformUI;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType;
import org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryDocument;
import org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType;

/**
 * @author keith
 *
 */
public class TestManager
{
    private String nativeLang = null;
    private String foreignLang = null;
    private IProject userProject = null;
    private UserConfig config = null;
    public final static String LANG_SEPARATOR = "-";
    public final static String ID_DATA_SEPARATOR = "-";
    public final static String HISTORY_DIR = "TestHistory";
    public final static String HISTORY_EXT = ".xml";
    /** Construct a test manager for a given user and language selectio
     * 
     * @param userProject
     * @param nativeLang
     * @param foreignLang
     */
    public TestManager(IProject userProject, String nativeLang, String foreignLang)
    {
        this.nativeLang = nativeLang;
        this.foreignLang = foreignLang;
        this.userProject = userProject;
        this.config = new UserConfig(userProject);
    }
    /** Create a test from the items in the selected modules
     * 
     * @param modules
     * @param options
     * @return Test object or null if no Test Items were found
     */
    public Test createTestFromModuleList(Object [] modules, TestOptions options)
    {
        options.includeNew = true;
        LinkedList items = new LinkedList();
        for (int i = 0; i < modules.length; i++)
        {
            if (modules[i] instanceof IFile)
            {
                IFile f = (IFile)modules[i];
                try
                {
                    LanguageModuleDocument doc =
                        LanguageModuleDocument.Factory.parse(f.getContents());
                    LanguageModuleType module = doc.getLanguageModule();
                    TestHistory history = getModuleTestHistory(module, f.getFullPath());
                    addModuleItems(items, history, module, f.getRawLocation(), options);
                }
                catch (IOException e)
                {
                    LanguageTestPlugin.log(IStatus.WARNING, 
                            "Unable to parse " + f.getName(),e);
                } 
                catch (XmlException e)
                {
                    LanguageTestPlugin.log(IStatus.WARNING, 
                            "Unable to parse " + f.getName(),e);
                } 
                catch (CoreException e)
                {
                    LanguageTestPlugin.log(IStatus.WARNING, 
                            "Unable to parse " + f.getName(),e);
                }
            }
        }
        Test test = null;
        if (items.size() > 0)
        {
            test = new Test(items, options.type, options.repeatUntilLearnt);
        }
        return test;
    }
    /** Create a revision test based on the users test history 
     * 
     * @param options
     * @return Test object or null if no Test Items were found
     */    
    public Test revisionTest(TestOptions options)
    {
        Test test = null;
        options.includeNew = false;
        LinkedList items = new LinkedList();
        try
        {
            IFolder historyFolder = userProject.getFolder(HISTORY_DIR);
            if (historyFolder == null) return null;
            IFolder langHistory = historyFolder.getFolder(nativeLang + 
                    LANG_SEPARATOR + foreignLang);
            if (langHistory != null)
            {
                IResource [] members = langHistory.members();
                for (int i = 0; i < members.length; i++)
                {
                    if (members[i].isAccessible() && members[i] instanceof IFile)
                    {
                        IFile f = (IFile)members[i];
                        try
                        {
                            ModuleHistoryDocument histDoc =
                                ModuleHistoryDocument.Factory.parse(f.getContents());
                            ModuleHistoryType historyType = histDoc.getModuleHistory();
                            LanguageModuleType module =
                                getModuleFromHistory(historyType);
                            TestHistory history = new XmlBeansTestHistory(f);
                            addModuleItems(items, history, module, 
                                    new Path(historyType.getPath()), options);
                        } 
                        catch (XmlException e)
                        {
                            LanguageTestPlugin.log(IStatus.WARNING, 
                                    "Unable to parse " + f.getName(),e);
                        } 
                        catch (IOException e)
                        {
                            LanguageTestPlugin.log(IStatus.WARNING, 
                                    "Unable to parse " + f.getName(),e);
                        }
                        
                    }
                }
            }
        } 
        catch (CoreException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, 
                    e.getLocalizedMessage(), e);
        }
        finally
        {
            if (items.size() > 0)
            {
                test = new Test(items, options.type, options.repeatUntilLearnt);
            }
        }
        return test;
    }
    private LanguageModuleType getModuleFromHistory(ModuleHistoryType history) 
        throws XmlException, IOException, CoreException
    {
        LanguageModuleType module = null;
        String path = history.getPath();
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IFile moduleFile = workspace.getRoot()
            .getFileForLocation(new Path(path));
        if (moduleFile == null)
        {
            MessageDialog.openWarning(PlatformUI.getWorkbench()
                    .getActiveWorkbenchWindow().getShell(), 
                MessageUtil.getString("ModuleNotFoundTitle"), 
                MessageUtil.getString("ModuleNotFoundMessage", 
                        moduleFile.getLocation().toOSString()));
        }
        else
        {
            LanguageModuleDocument modDoc = 
                LanguageModuleDocument.Factory.parse(
                        moduleFile.getContents());
            if (modDoc != null)
            {
                module = modDoc.getLanguageModule();
            }
        }
        return module;
    }
    
    public TestHistory getModuleTestHistory(LanguageModuleType module, IPath modulePath)
    {
        return getTestHistory(module.getId(), module.getCreationTime(), modulePath);
    }
    public TestHistory getModuleTestHistory(TestModule module)
    {
        String id = Integer.toHexString(module.getUniqueId());
        return getTestHistory(id, module.getCreationTime(), module.getPath());
    }
    public TestHistory getTestHistory(String moduleId, long moduleCreationTime, IPath modulePath)
    {
        
        XmlBeansTestHistory history = null;
        if (userProject != null)
        {
            try
            {
                IFolder historyRoot = userProject.getFolder(HISTORY_DIR);
                if (!historyRoot.exists())
                    historyRoot.create(false, true, null);
                IFolder historyFolder = 
                    historyRoot.getFolder(nativeLang + LANG_SEPARATOR + foreignLang);
                if (historyFolder != null)
                {
                        if (!historyFolder.exists())
                        {
                            historyFolder.create(true, true, null);
                        }
                        IFile historyFile = historyFolder.getFile(moduleId + 
                                ID_DATA_SEPARATOR + moduleCreationTime + HISTORY_EXT);
                        if (historyFile != null)
                            history = new XmlBeansTestHistory(historyFile, modulePath);
                    
                }
            }
            catch (CoreException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, 
                        "Error reading history: " + e.getLocalizedMessage(),e);
            }
            catch (XmlException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, 
                        "Error reading history: "  + e.getLocalizedMessage(),e);
            } 
            catch (IOException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, 
                    "Error reading history: "  + e.getLocalizedMessage(),e);
            }
        }
        return history;
    }
    /** add the items for a selected module
     * 
     * @param items
     * @param path
     * @param module
     * @param options
     */
    protected void addModuleItems(LinkedList items, TestHistory history, 
            LanguageModuleType module, IPath modulePath, TestOptions options)
    {
        XmlBeansTestModule xbtm = new XmlBeansTestModule(modulePath, module);
        // find the module's fonts for the relevant languages
        FontData nativeFD = null;
        FontData foreignFD = null;
        for (int l = 0; l < module.sizeOfLangArray(); l++)
        {
            LangType lt = module.getLangArray(l);
            if (lt.getLang().equals(nativeLang) && lt.getFont() != null)
            {
                nativeFD = new FontData(lt.getFont(), 
                                        lt.getFontSize().intValue(), 
                                        SWT.NORMAL);
            }
            else if(lt.getLang().equals(foreignLang) && lt.getFont() != null)
            {
                foreignFD = new FontData(lt.getFont(), 
                                         lt.getFontSize().intValue(), 
                                         SWT.NORMAL);
            } 
        }
        // add the items
        for (int i = 0; i < module.sizeOfTestItemArray(); i++)
        {
            TestItemType ti = module.getTestItemArray(i);
            // test the item against the filters
            boolean choose = true;
            for (TestItemFilter f : options.getFilters())
            {
                choose &= f.chooseItem(module, ti);
                if (!choose) break;
            }
            if (!choose) continue;
            // see if it matches the type of test
            XmlBeansTestItem xbti = new XmlBeansTestItem(ti, nativeLang, foreignLang);
            if (xbti.getNativeText() != null && xbti.getForeignText() != null)
            {
                if (options.type.equals(TestType.LISTENING_FOREIGN_NATIVE) &&
                    xbti.getImagePath() == null)
                {
                    continue;
                }
                xbti.setNativeFontData(nativeFD);
                xbti.setForeignFontData(foreignFD);
                xbti.setModule(xbtm);
                if (options.useHistory)
                {
                    addIfAppropriate(history, items, xbti, options);
                }
                else
                {
                    items.add(xbti);
                }
            }
        }
    }
    /**
     * @param history
     * @param items
     * @param xbti
     * @param options
     */
    private void addIfAppropriate(TestHistory history, LinkedList items, XmlBeansTestItem xbti, TestOptions options)
    {
        try
        {
            ItemHistory hi = history.getHistoryItem(xbti, options.type);
            if (hi == null || hi.testCount == 0)
            {
                if (options.includeNew)
                    items.add(xbti);
                return;
            }
            if ((hi.isTestDue(options.type, config) && 
                 !hi.isDisabled()))
            {
                items.add(xbti);
            }
        }
        catch (TestHistoryStorageException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, e.getLocalizedMessage(),e);
        }
    }
    public String getNativeLang() { return nativeLang; }
    public String getForeignLang() { return foreignLang; }
    
    /**
     * Generate a description for a pair of language codes separated by a '-'
     * @param langPair
     * @return the description
     */
    public static String getLangPairDescription(String langPair)
    {
        int separator = langPair.indexOf('-');
        if (separator > -1 && separator < langPair.length() - 1)
        {
            UniversalLanguage ulA = new UniversalLanguage(langPair.substring(0, separator));
            UniversalLanguage ulB = new UniversalLanguage(langPair.substring(separator + 1));
            return ulA.generateDescription() + " / " + ulB.generateDescription();
        }
        else
        {
            return langPair;
        }
    }
    
}
