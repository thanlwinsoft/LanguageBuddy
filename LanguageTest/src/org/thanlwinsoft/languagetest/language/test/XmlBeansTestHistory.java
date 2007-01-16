/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.thanlwinsoft.schemas.languagetest.ItemType;
import org.thanlwinsoft.schemas.languagetest.ModuleHistoryDocument;
import org.thanlwinsoft.schemas.languagetest.ModuleHistoryType;
import org.thanlwinsoft.schemas.languagetest.ResultType;

/**
 * @author keith
 *
 */
public class XmlBeansTestHistory implements TestHistory
{
    private IFile file = null;
    private ModuleHistoryDocument doc = null;
    
    
    public XmlBeansTestHistory(IFile file) throws XmlException, IOException, CoreException
    {
        construct(file, null);
    }
    
    public XmlBeansTestHistory(IFile file, IPath modulePath) throws XmlException, IOException, CoreException
    {
        construct(file, modulePath);
    }
    private void construct(IFile file, IPath modulePath) throws XmlException, IOException, CoreException
    {
        this.file = file;
        if (file.isAccessible())
        {
            doc = ModuleHistoryDocument.Factory.parse(file.getContents());
        }
        else
        {
            XmlOptions options = new XmlOptions();
            options.setCharacterEncoding("UTF-8");
            options.setSavePrettyPrint();
            doc = ModuleHistoryDocument.Factory.newInstance(options);
            ModuleHistoryType history = doc.addNewModuleHistory();
            if (modulePath == null) 
                throw new IllegalArgumentException("TestHistory cannot be created witout a module path");
            
            history.setPath(modulePath.toPortableString());
        }
    }
    
//    /* (non-Javadoc)
//     * @see org.thanlwinsoft.languagetest.language.test.TestHistory#deleteItem(org.thanlwinsoft.languagetest.language.test.TestItemProperties)
//     */
//    public void deleteItem(TestItemProperties item)
//            throws TestHistoryStorageException
//    {
//        throw new TestHistoryStorageException("Not implemented");
//    }

//    /* (non-Javadoc)
//     * @see org.thanlwinsoft.languagetest.language.test.TestHistory#deleteItemType(org.thanlwinsoft.languagetest.language.test.TestItemProperties, org.thanlwinsoft.languagetest.language.test.TestType)
//     */
//    public void deleteItemType(TestItemProperties item, TestType type)
//            throws TestHistoryStorageException
//    {
//        throw new TestHistoryStorageException("Not implemented");
//    }
    
    /** 
     * find the results for the specified TestType
     * 
     */
    private org.thanlwinsoft.schemas.languagetest.TestType 
        findItem(TestItemProperties item, TestType type)
    {
        ItemType [] items = doc.getModuleHistory().getItemArray();
        for (int i = 0; i < items.length; i++)
        {
            if (items[i].getCreated() == item.getCreationTime() &&
                items[i].getAuthor() == item.getCreator())
            {
                org.thanlwinsoft.schemas.languagetest.TestType testType = null;
                if (type.equals(TestType.READING_FOREIGN_NATIVE))
                {
                    testType = items[i].getFR();
                }
                else if (type.equals(TestType.LISTENING_FOREIGN_NATIVE))
                {
                    testType = items[i].getFL();
                }
                else  if (type.equals(TestType.READING_NATIVE_FOREIGN))
                {
                    testType = items[i].getNR();
                }
                else return null;
                return testType;
            }
        }
        return null;
    }
    
    private org.thanlwinsoft.schemas.languagetest.TestType 
    findItem(TestItem item, TestType type)
    {
        ModuleHistoryType history = doc.getModuleHistory();
        ItemType [] items = history.getItemArray();
        for (int i = 0; i < items.length; i++)
        {
            if (items[i].getCreated() == item.getCreationTime() &&
                items[i].getAuthor().equals(item.getCreator()))
            {
                org.thanlwinsoft.schemas.languagetest.TestType testType = null;
                if (type.equals(TestType.READING_FOREIGN_NATIVE))
                {
                    testType = items[i].getFR();
                    if (testType == null)
                        testType = items[i].addNewFR();
                }
                else if (type.equals(TestType.LISTENING_FOREIGN_NATIVE))
                {
                    testType = items[i].getFL();
                    if (testType == null)
                        testType = items[i].addNewFL();
                }
                else  if (type.equals(TestType.READING_NATIVE_FOREIGN))
                {
                    testType = items[i].getNR();
                    if (testType == null)
                        testType = items[i].addNewNR();
                }
                else return null;
                
                return testType;
            }
        }
        return null;
    }


    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestHistory#getHistoryItem(org.thanlwinsoft.languagetest.language.test.TestItem, org.thanlwinsoft.languagetest.language.test.TestType)
     */
    public ItemHistory getHistoryItem(TestItem item, TestType type)
            throws TestHistoryStorageException
    {
        org.thanlwinsoft.schemas.languagetest.TestType testType = 
            findItem(item, type);
        if (testType == null) return null;
        ItemHistory ih = new ItemHistory();
        ih.author = item.getCreator();
        ih.creationTime = item.getCreationTime();
        
        ih.disabled = testType.getDisabled();
        ih.testCount = testType.sizeOfResultArray();
        
        boolean allPasses = true;
        ih.consecutivePassCount = 0;
        for (int j = ih.testCount - 1; j >= 0; j--)
        {
            ResultType r = testType.getResultArray(j);
            if (r.getPass())
            {
                if (allPasses)
                {
                    ih.consecutivePassCount++;
                }
                if (ih.lastPassDate == -1)
                    ih.lastPassDate = r.getTime();
                ih.passCount++;
            }
            else
            {
                allPasses = false;
                if (ih.lastFailDate == -1)
                    ih.lastFailDate = r.getTime();
            }
            if (ih.lastTestDate == -1)
                ih.lastTestDate = r.getTime();
        }
        return ih;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestHistory#getModuleCount()
     */
//    public int getModuleCount()
//    {
//        // TODO Auto-generated method stub
//        return 0;
//    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestHistory#ignoreItem(org.thanlwinsoft.languagetest.language.test.TestItemProperties, org.thanlwinsoft.languagetest.language.test.TestType, boolean)
     */
    public void ignoreItem(TestItemProperties item, TestType type,
            boolean ignore) throws TestHistoryStorageException
    {
        org.thanlwinsoft.schemas.languagetest.TestType t = findItem(item, type);
        t.setDisabled(ignore);
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestHistory#iterator(org.thanlwinsoft.languagetest.language.test.TestType)
     */
    public Iterator iterator(TestType type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestHistory#savePermanent()
     */
    public void savePermanent() throws TestHistoryStorageException
    {
        try
        {
            XmlOptions options = new XmlOptions();
            options.setCharacterEncoding("UTF-8");
            options.setSavePrettyPrint();
            IContainer parent = file.getParent();
            if (!(parent.exists()) || !(parent.isAccessible()))
            {
                if (file.getParent() instanceof IFolder)
                {
                    IFolder folder = (IFolder)file.getParent();
                    folder.create(true, true, null);
                }
            }
            doc.save(file.getRawLocation().toFile(), options);
            file.refreshLocal(0, null);
        }
        catch (IOException e)
        {
            throw new TestHistoryStorageException(e);
        } 
        catch (CoreException e)
        {
            throw new TestHistoryStorageException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestHistory#saveResult(org.thanlwinsoft.languagetest.language.test.TestItem, org.thanlwinsoft.languagetest.language.test.TestType, long, boolean)
     */
    public void saveResult(TestItem item, TestType type, long testTime,
            boolean pass) throws TestHistoryStorageException
    {
        org.thanlwinsoft.schemas.languagetest.TestType t = findItem(item, type);
        if (t == null)
        {
            ModuleHistoryType history = doc.getModuleHistory();
            ItemType itemResult = history.addNewItem();
            itemResult.setAuthor(item.getCreator());
            itemResult.setCreated(item.getCreationTime());
            
            if (type.equals(TestType.LISTENING_FOREIGN_NATIVE))
            {
                t = itemResult.addNewFL();
            }
            else if (type.equals(TestType.READING_FOREIGN_NATIVE))
            {
                t = itemResult.addNewFR();
            }
            else if (type.equals(TestType.READING_NATIVE_FOREIGN))
            {
                t = itemResult.addNewNR();
            }
            else return; // nothing to save
        }
        ResultType r = t.addNewResult();
        r.setPass(pass);
        r.setTime(testTime);
        savePermanent();
    }
    public IPath getModulePath() 
    {
        return new Path(doc.getModuleHistory().getPath());
    }
}
