/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/test/Test.java,v $
 *  Version:       $Revision: 1.8 $
 *  Last Modified: $Date: 2004/12/18 05:11:07 $
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

package languagetest.language.test;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;

/** Class to hold all the items for a test. It holds all the properties
 * applied to the test.
 * @author keith
 */
public class Test
{
    private boolean repeatTillLearnt;
    private List testList;
    private List remainingTests;
    private Random random;
    private TestItem currentTest;
    private int passCount = 0;
    private int testCount = 0;
    private int retestCount = 0;
    private int firstTimePasses = 0;
    private int untested = 0;
    private TestType type = null;
    private boolean audioDisabled = false;
    private boolean retest = false;
    private int maxRepeats = 100;
    /** Creates a new instance of Test from a list of modules.
     * @param modules Modules to add items from.
     * @param type Test Type
     * @param repeatTillLearnt Flag whether items that are marked wrong should be repeatedly
     * tested until they are correct.
     * @param useHistory Flag whether the history should be checked before adding items
     * from the list of modules. If this is false then all items in the
     * module for the current language configuration will be added.
     */
    public Test(SortedSet modules, TestType type, boolean repeatTillLearnt, 
                boolean useHistory)
    {
        this.type = type;
        this.repeatTillLearnt = repeatTillLearnt;
        this.testList = new LinkedList();
        TestHistory history = UserConfig.getCurrent().getTestHistory();
        Iterator i = modules.iterator();
        boolean useH = useHistory;
        if (type == TestType.FLIP_CARD) useH = false;
        while (i.hasNext())
        {
            TestModule module = (TestModule)i.next();
            if (module.isSelected())
            {
                Set moduleList = module.getTestList();
                Iterator t = moduleList.iterator();
                while (t.hasNext())
                {
                    TestItem item = (TestItem)t.next();                    
                    addItemIfAppropriate(item, history, useH); 
                }
            }
        }
        finishInit();
    }
    
    /**
     * Method to check all the different parameters that deterine whether an 
     * item should really be included in a test.
     * @param item Test Item to add
     * @param history History Object to check for past performance
     * @param useHistory boolean as to whether history should be checked
     */    
    protected void addItemIfAppropriate(TestItem item, TestHistory history, 
                                        boolean useHistory)
    {
        // check that the item has data for the current language
        if ((item.getNativeLanguages()
             .contains(LanguageConfig.getCurrent()
                       .getNativeLanguage())) &&
           (item.getForeignLanguages()
            .contains(LanguageConfig.getCurrent()
                      .getForeignLanguage())))
        {
            if (type == TestType.LISTENING_FOREIGN_NATIVE &&
                item.getSoundFile() == null)
            {
                // can't do listening test if no audio file!
            }
            else
            {
                // now check test history for the item
                ItemHistory hItem = null;
                if (useHistory)
                {
                    try
                    {
                        hItem = history.getHistoryItem(item, type);
                    }
                    catch (TestHistoryStorageException thse)
                    {
                        System.out.println(thse.getMessage());
                        hItem = null;
                    }
                }
                if (hItem == null || (hItem.isTestDue(type) && 
                                      !hItem.isDisabled()))
                {
                    testList.add(item);
                    item.reset(); // reset pass flags
                }
            }
        }
    }
    
    /** Sets disabled flag to disable playing of audio data even if its exists.
     * @param disabled Boolean flag.
     */    
    public void setAudioDisabled(boolean disabled)
    {
        this.audioDisabled = disabled;
    }
    
    /** Tests whether audio disabled flag is set.
     * @return True if audio disabled.
     */    
    public boolean isAudioDisabled()
    {
        return this.audioDisabled;
    }
    
    /** Revision Test constructor. The list for the test is prepared by an outside
     * method and passed directly into the class.
     * @param items List of test items for test
     * @param type Test Type
     * @param repeatTillLearnt Flag whether items that are marked wrong should be repeatedly
     * tested until they are correct.
     */
    public Test(LinkedList items, TestType type, boolean repeatTillLearnt)
    {
        this.type = type;
        this.repeatTillLearnt = repeatTillLearnt;
        this.testList =items;
        
        finishInit();
    }
    
    /** helper function to do initialisation tasks common to all test types */
    protected void finishInit()
    {
        this.random = new Random();
        // take copy of list
        remainingTests = new LinkedList(testList);
        System.out.println("Number test items: " + testList.size());  
        untested = testList.size();
    }
    /** Repeat the test for all items that were not passed first time*/
    public void retestUnknown()
    {
        retest = true;
        List oldList = testList;
        passCount = 0; 
        testCount = 0;
        retestCount = 0;
        firstTimePasses = 0;
        testList = new LinkedList();
        Iterator i = oldList.iterator();
        while (i.hasNext())
        {
            TestItem item = (TestItem)i.next();
            if (item.isPassed() == false || item.getTestCount() > 1)
            {
                item.reset();
                testList.add(item);
            }
        }
        remainingTests = new LinkedList(testList);
        untested = testList.size();
    }
    
    /** Tests whether retest flag is set.
     * @return True if the test has already been run once.
     */    
    public boolean isRetest()
    {
        return retest;
    }
    
    public void pruneTestToLimit(int maxTests)
    {
        while (remainingTests.size() > maxTests)
        {
            int number = random.nextInt(remainingTests.size());
            remainingTests.remove(number);
        }
        untested = remainingTests.size();
        testList = new LinkedList(remainingTests);
    }
    
    /** Gets the next item in the test. The actual item returned will be random
     * though it will never be the same as the previous item unless there is only
     * one item left.
     * @return The next test item to test the user with.
     */    
    public TestItem getNextItem()
    {
        if (remainingTests.size() == 0) return null;
        TestItem previousTest = currentTest;
        // prevent the same test being repeated immediately
        do
        {
            // nextInt seems to favour repeating tests around the same are in
            // the test list so use double instead - will be slower, but this
            // is hardly a time critical loop
            //int number = random.nextInt(remainingTests.size());
            int number = (int)((double)remainingTests.size() * Math.random());
            currentTest = (TestItem)remainingTests.get(number);
            // if only one test is left, have to repeat it immediately
            if (remainingTests.size()<2) break;
        } while (currentTest == previousTest);
        if (currentTest.getTestCount()>0) retestCount++;
        return currentTest;
    }
    /** Called from the GUI to set the users test result.
     * @param passed True if user got answer correct.
     */    
    public void setPassStatus(boolean passed)
    {
        testCount++;
        if (passed) 
        {
            if (currentTest.getTestCount()==0)
            {
                firstTimePasses++;
                untested--;
            }
            currentTest.pass();
            remainingTests.remove(currentTest);
            passCount++;                        
        }
        else
        {
            if (currentTest.getTestCount()==0)
            {
                untested--;
            }
            currentTest.fail();
            if (!repeatTillLearnt || currentTest.getTestCount()>maxRepeats)
            {
                remainingTests.remove(currentTest);
            }
        }
    }
    /** Removes the current item from the test if the user has chosen to ignore it. */    
    public void removeCurrentItem()
    {
        // remove item from both test lists
        remainingTests.remove(currentTest);
        testList.remove(currentTest);
    }
    /** Getter for test type.
     * @return The type of test this is.
     */    
    public TestType getType() { return type; }
    /** Total number of passes.
     * @return Number of test items passed.
     */    
    public int getPassCount() { return passCount; }
    /** Getter for total number of tests including retests.
     * @return Number of tests performed by calling getNextItem
     */    
    public int getTestCount() { return testCount; }
    /** Number of items in this test.
     * @return Number of items in this test.
     */    
    public int getNumTests() { return testList.size(); }
    /** Number of retests performed.
     * @return Number of retests performed.
     */    
    public int getNumRetests() { return retestCount; }
    /** Number of retests performed.
     * @return Number of retests performed.
     */    
    public int getUntested() { return untested; }
    /** Number of tests passed first time.
     * @return Number of tests passed first time.
     */    
    public int getNumFirstTimePasses() { return firstTimePasses; }
    /** Gives access to the the Test Item currently being shown to user.
     * @return Current test item that was returned by last call to getNextItem.
     */    
    public TestItem getCurrentItem() { return currentTest; }
    /** Provides an iterator over all the items in the test. This is designed for
     * setting flags on items, not for performing the test itself.
     * @return Iterator for internal linked list.
     */    
    public Iterator getItemIterator() { return testList.iterator(); }
    /**
     * Set max number of redisplays in flip card mode
     */
    public void setMaxRepeats(int max)
    {
        maxRepeats = max;
    }
    
}
