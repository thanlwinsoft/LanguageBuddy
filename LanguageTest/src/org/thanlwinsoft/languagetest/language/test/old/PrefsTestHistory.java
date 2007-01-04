/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/test/PrefsTestHistory.java,v $
 *  Version:       $Revision: 1.3 $
 *  Last Modified: $Date: 2004/03/24 04:50:07 $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2003 Keith Stribley <jungleglacier@snc.co.uk>
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

package org.thanlwinsoft.languagetest.language.test;

import java.io.File;
import java.util.prefs.Preferences;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.prefs.BackingStoreException;
import java.text.DecimalFormat;
import java.io.IOException;
/**
 *
 * @author  keith
 */
public class PrefsTestHistory implements TestHistory
{
    private Preferences historyPrefs = null; 
    private DecimalFormat testIndexFormat = null;
    private static final String HISTORY_NODE = "TestHistory";
    private static final String MODULE_PATH = "modulePath";
    //private static final String ITEMS = "Items";
    //private static final String CREATION_TIME = "creationTime";
    private static final String TEST_COUNT = "testCount";
    private static final String PASS_COUNT = "passCount";
    private static final String TEST_DATE = "date";
    private static final String TEST_RESULT = "pass";
    //private static final long MS_IN_DAY = 24*3600000;
    /** Creates a new instance of TestHistory */
    public PrefsTestHistory(String userName) 
    {
        Preferences packagePref = 
            Preferences.userNodeForPackage(this.getClass());
        historyPrefs = packagePref.node(UserConfig.PROFILES + "/" + userName + "/" 
            + escapeCode(LanguageConfig.getCurrent().getNativeLangCode()) + ":"
            + escapeCode(LanguageConfig.getCurrent().getForeignLangCode()) 
            + "/" + HISTORY_NODE);
        System.out.println(LanguageConfig.getCurrent().getNativeLangCode());
        System.out.println(LanguageConfig.getCurrent().getForeignLangCode());
        testIndexFormat = new DecimalFormat("00");
    }
    
    public int getModuleCount()
    {
        int count = -1;
        try
        {
            String [] names = historyPrefs.childrenNames();
            count = names.length;
        }
        catch (BackingStoreException bse)
        {
            System.out.println(bse);
        }
        return count;
    }
    
    private String escapeCode(String text)
    {
        return text.replace('_','-');
    }
    
    public ItemHistory getHistoryItem(TestItem item, TestType type)
    {
        Preferences itemNode = nodeFromItem(item).node(type.getCode());
        try
        {
        String [] keys = itemNode.childrenNames();
        if (keys.length > 0)
        {
            return new PrefsItemHistory(itemNode, itemNode.parent(),
                itemNode.parent().parent(), itemNode.parent().parent().parent());
        }
        }
        catch (BackingStoreException bse)
        {
            System.out.println(bse);
        }
        return null; // no history data
    }
    
    public void saveResult(TestItem item, TestType type, long testTime, boolean pass)
    {
        Preferences itemNode = nodeFromItem(item).node(type.getCode());
        int testCount = itemNode.getInt(TEST_COUNT, 0);
        int passCount = itemNode.getInt(PASS_COUNT, 0);
        int index = testCount;
        if (index > getMaxResults())
        {
            index = getMaxResults();
        }
        if (alreadyTestedToday(itemNode, testCount, testTime, type))
        {
            // if result has already been tested today, then only store result
            // if result is worse. This is to avoid short term memory from
            // distorting results. However, do not increment testCount.
            if (!pass)
            {
                Preferences resultNode = 
                    itemNode.node(testIndexFormat.format(index));
                resultNode.putLong(TEST_DATE, testTime);
                resultNode.putBoolean(TEST_RESULT, pass);
            }
        }
        else
        {
            testCount++;
            if (pass) passCount++;
            if (testCount > getMaxResults())
            {
                index = getMaxResults();
                // need to move up results
                for (int i=1; i<getMaxResults(); i++)
                {
                    Preferences overwrite = 
                        itemNode.node(testIndexFormat.format(i));
                    Preferences oldResult = 
                        itemNode.node(testIndexFormat.format(i+1));
                    overwrite.putLong(TEST_DATE, oldResult.getLong(TEST_DATE,-1));
                    overwrite.putBoolean(TEST_RESULT, 
                                         oldResult.getBoolean(TEST_RESULT, false));
                }
            }
            else
            {
                index++;
            }
            itemNode.putInt(TEST_COUNT, testCount);
            itemNode.putInt(PASS_COUNT, passCount);
            Preferences resultNode = itemNode.node(testIndexFormat.format(testCount));
            resultNode.putLong(TEST_DATE, testTime);
            resultNode.putBoolean(TEST_RESULT, pass);
        }
    }
    protected boolean alreadyTestedToday(Preferences node, int testCount, 
                                         long timeNow, TestType type)
    {
        int index = testCount; 
        if (testCount == 0) return false; // no stored results
        if (testCount > getMaxResults()) index = getMaxResults();
        long timeSinceLastTest = timeNow - 
            node.node(testIndexFormat.format(index))
            .getLong(TEST_DATE, -1);
        if (timeSinceLastTest < 
            UserConfig.getCurrent().getMinRetestPeriod(type)) 
        {
            return true;
        }
        return false;
    }
    
    protected Preferences nodeFromItem(TestItem item)
    {
        String creator = item.getCreator();
        // make sure that creator is never empty
        if (creator == null || creator.length() < 1) creator = "Unknown";
        return nodeFromModule(item.getModule()).node(creator + "/" + 
                              Long.toString(item.getCreationTime()));
    }
    protected Preferences nodeFromModule(TestModule module)
    {
        String nodeName = Integer.toHexString(module.getUniqueId())  + ":" + 
                          Long.toString(module.getCreationTime());
        String modPath = "";
        try
        {
            modPath = module.getFile().getCanonicalPath();
        }
        catch (IOException ioe)
        {
            System.out.println(ioe);
        }
        try
        {
            if (historyPrefs.nodeExists(nodeName))
            {
                // TBD check data
                String oldPath = historyPrefs.node(nodeName)
                    .get(MODULE_PATH, modPath);          
                if (!(oldPath.equals(modPath)))
                {
                    System.out.println
                        ("Warning: module path has changed since last test"
                        + oldPath +
                        " -> " + modPath);
                }
            }
            else
            {
                historyPrefs.node(nodeName).put(MODULE_PATH, modPath);
            }
        }
        catch (java.util.prefs.BackingStoreException bse)
        {
            System.out.println(bse);
            historyPrefs.node(nodeName).put(MODULE_PATH, modPath);
        }
        return historyPrefs.node(nodeName);
        
    }
    static protected int getMaxResults()
    {
        return 10;
    }
    
    public Iterator iterator(TestType type)
    {
        return new ItemIterator(historyPrefs, type);
    }
    
    public void savePermanent() throws TestHistoryStorageException
    {
        try
        {
            historyPrefs.flush();            
        }
        catch (BackingStoreException bse)
        {
            throw new TestHistoryStorageException(bse);
        }
    }
    
    public void ignoreItem(TestItemProperties item, TestType type, boolean ignore) throws TestHistoryStorageException
    {
        // not implemented at present
    }
    
    public void deleteItem(TestItemProperties item) throws TestHistoryStorageException
    {
    }
    
    public void deleteItemType(TestItemProperties item, TestType type) throws TestHistoryStorageException
    {
    }
    
    public class ItemIterator implements java.util.Iterator
    {
        private Preferences historyNode = null;
        private Preferences moduleNode = null;
        private Preferences authorNode = null;
        private Preferences itemNode = null;
        private Preferences testNode = null;
        private List moduleArray;
        private List authorArray;
        private List itemArray;
        private Iterator iModule = null;
        private Iterator iAuthor = null;
        private Iterator iItem = null;
        private TestType type;
        protected ItemIterator(Preferences historyNode, TestType type)
        {
            this.historyNode = historyNode;
            this.type = type;
            try
            {
                moduleArray = Arrays.asList(historyNode.childrenNames());
                iModule = moduleArray.iterator();
            }
            catch (BackingStoreException bse)
            {
                System.out.println(bse);
            }
        }
        public boolean hasNext()
        {
            if (testNode != null) return true; // has already been called
            while (testNode == null)
            {
                if (getNextTestNode()) return true;
                else if (getNextAuthor()) return true;
                else if (iModule.hasNext())
                {
                    moduleNode = historyNode.node(iModule.next().toString());
                    try
                    {
                        authorArray = Arrays.asList(moduleNode.childrenNames());
                        iAuthor = authorArray.iterator();

                        if (getNextAuthor()) return true;
                        // otherwise loop and try next module
                    }
                    catch (BackingStoreException bse)
                    {
                        System.out.println(bse);
                        return false;
                    }                
                }
                else return false; // no more modules to parse
            }
            return true;
        }
        
        protected boolean getNextAuthor()
        {
            try
            {
                if (iAuthor != null && iAuthor.hasNext())
                {
                    authorNode = moduleNode.node(iAuthor.next().toString());
                    // an author node should not be empty
                    try
                    {
                        itemArray = Arrays.asList(authorNode.childrenNames());
                        iItem = itemArray.iterator();
                        if (getNextTestNode()) return true;
                    }
                    catch (BackingStoreException bse)
                    {
                        System.out.println(bse);
                    }
                }
            }
            catch (IllegalStateException ise)
            {
                // this probably indicates a node has legitimately been removed
                // because it is no longer valid
            }
            return false;
        }
        
        protected boolean getNextTestNode()
        {
            try
            {
                while (testNode == null) 
                {
                    if (iItem != null && iItem.hasNext())
                    {
                        itemNode = 
                            authorNode.node(iItem.next().toString());
                        try
                        {
                            if (itemNode != null)
                            {
                                if (itemNode.nodeExists(type.getCode()))
                                {
                                    testNode = itemNode.node(type.getCode());
                                }
                            }
                            else
                            {
                                System.out.println("missing item");
                            }
                        }
                        catch (BackingStoreException bse)
                        {
                            System.out.println(bse);
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }
            catch (IllegalStateException ise)
            {
                // this probably indicates a node has legitimately been removed
                // because it is no longer valid
            }
            return (testNode != null) ? true : false;
        }
        
        public Object next()
        {
            ItemHistory historyItem = null;
            if (testNode == null)
            {
                if (!hasNext()) throw new java.util.NoSuchElementException(); 
            }
            historyItem = new PrefsItemHistory(testNode, itemNode, 
                                               authorNode, moduleNode);
            testNode = null; // record that next has taken item
            return historyItem;
        }
        
        public void remove()
        {
            System.out.println("ItemIterator.remove() not implemented!");
        }        
    }
    class PrefsItemHistory extends ItemHistory
    {
        Preferences moduleNode;
        Preferences itemNode;
        
        protected PrefsItemHistory (Preferences testNode, Preferences itemNode, 
                                    Preferences authorNode, Preferences moduleNode)
        {
            consecutivePassCount = 0;
            lastFailDate = -1;
            lastPassDate = -1;
            lastTestDate = -1;
            long firstTestDate = -1;
            moduleFile = new File(moduleNode.get(MODULE_PATH,""));
            resultTable = new Object[getMaxResults()][2];
            try
            {
                int split = moduleNode.name().indexOf(':');
                String idString = moduleNode.name().substring(0,split);
                String timeString = moduleNode.name().substring(split + 1);
                moduleId = Integer.parseInt(idString, 16);
                moduleCreationTime = Long.parseLong(timeString);
                creationTime = Long.parseLong(itemNode.name());
                author = authorNode.name();
                testCount = itemNode.getInt(TEST_COUNT, 0);
                passCount = itemNode.getInt(PASS_COUNT, 0);
                //System.out.println(idString + " : " + timeString + " " + 
                //    author + " " + itemNode.name());
                this.moduleNode = moduleNode;
                this.itemNode = itemNode;
            }
            catch (NumberFormatException nfe) // shouldn't happen
            {
                System.out.println(nfe);
            }
            int recentPasses = 0;
            for (int i=1; i<=getMaxResults(); i++)
            {
                try
                {
                    if (!testNode.nodeExists(testIndexFormat.format(i))) 
                    {
                        // hack to fix effect of old bug
                        if (testCount < i - 1)
                        {
                            testCount = i - 1;
                            passCount = recentPasses;
                            testNode.putInt(TEST_COUNT, testCount);
                            testNode.putInt(PASS_COUNT, passCount);
                        }
                        break;
                    }
                    Preferences testEntry = 
                        testNode.node(testIndexFormat.format(i));
                    lastTestDate = testEntry.getLong(TEST_DATE, -1);
                    resultTable[i-1] [0] = new Date(lastTestDate);
                    if (testEntry.getBoolean(TEST_RESULT, false))
                    {
                        consecutivePassCount++;
                        recentPasses++;
                        lastPassDate = lastTestDate;
                        resultTable[i-1] [1] = new Boolean(true);
                    }
                    else
                    {
                        consecutivePassCount = 0;
                        lastFailDate = lastTestDate;
                        resultTable[i-1] [1] = new Boolean(false);
                    }
                    // initialise firstTestDate on first iteration
                    if (i == 1) firstTestDate = lastTestDate;
                }
                catch (BackingStoreException bse)
                {
                    System.out.println(bse);
                    break;
                }                
            }
            // check that lastFailDate has been initialised, otherwise set it to
            // date of first test
            if (lastFailDate == -1)
            {
                lastFailDate = firstTestDate;
            }
        }
        public Preferences getItemNode() { return itemNode;}
        public Preferences getModuleNode() { return moduleNode; }
        public void removeModuleHistory()
        {
            // check that node has not allready been removed
            try
            {
                if (moduleNode.parent()
                    .nodeExists(moduleNode.name()))
                {
                    moduleNode.removeNode();
                }
            }
            catch (BackingStoreException bse)
            {
                System.out.println(bse);
            }
        }
    }
}
