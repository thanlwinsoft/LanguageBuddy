/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.runtime.IPath;
import org.thanlwinsoft.schemas.languagetest.ItemType;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.ModuleHistoryDocument;
import org.thanlwinsoft.schemas.languagetest.ModuleHistoryType;
import org.thanlwinsoft.schemas.languagetest.ResultType;

/**
 * @author keith
 *
 */
public class XmlBeansTestHistory implements TestHistory
{
    private IPath path = null;
    private ModuleHistoryDocument doc = null;
    
    public XmlBeansTestHistory(IPath path) throws XmlException, IOException
    {
        this.path = path;
        if (path.toFile().exists())
        {
        BufferedInputStream is = 
            new BufferedInputStream(new FileInputStream(path.toFile()));
        doc = ModuleHistoryDocument.Factory.parse(is);
        ModuleHistoryType history = doc.getModuleHistory();
        }
        else
        {
            XmlOptions options = new XmlOptions();
            options.setCharacterEncoding("UTF-8");
            options.setSavePrettyPrint();
            doc = ModuleHistoryDocument.Factory.newInstance(options);
        }
    }
    
    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestHistory#deleteItem(org.thanlwinsoft.languagetest.language.test.TestItemProperties)
     */
    public void deleteItem(TestItemProperties item)
            throws TestHistoryStorageException
    {
        throw new TestHistoryStorageException("Not implemented");
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestHistory#deleteItemType(org.thanlwinsoft.languagetest.language.test.TestItemProperties, org.thanlwinsoft.languagetest.language.test.TestType)
     */
    public void deleteItemType(TestItemProperties item, TestType type)
            throws TestHistoryStorageException
    {
        throw new TestHistoryStorageException("Not implemented");
    }
    
    private org.thanlwinsoft.schemas.languagetest.TestType 
        findItem(TestItemProperties item, TestType type)
    {
        ItemType ti = null;
        ItemType [] items = doc.getModuleHistory().getItemArray();
        for (int i = 0; i < items.length; i++)
        {
            if (items[i].getCreated() == item.getCreationTime() &&
                items[i].getAuthor() == item.getCreator())
            {
                ti = items[i];
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
        ItemType ti = null;
        ItemType [] items = doc.getModuleHistory().getItemArray();
        for (int i = 0; i < items.length; i++)
        {
            if (items[i].getCreated() == item.getCreationTime() &&
                items[i].getAuthor() == item.getCreator())
            {
                ti = items[i];
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


    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestHistory#getHistoryItem(org.thanlwinsoft.languagetest.language.test.TestItem, org.thanlwinsoft.languagetest.language.test.TestType)
     */
    public ItemHistory getHistoryItem(TestItem item, TestType type)
            throws TestHistoryStorageException
    {
        org.thanlwinsoft.schemas.languagetest.TestType testType = 
            findItem(item, type);
        
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
    public int getModuleCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

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
        doc.save(path.toFile(), options);
        }
        catch (IOException e)
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
        ResultType r = t.addNewResult();
        r.setPass(pass);
        r.setTime(testTime);
    }

}
