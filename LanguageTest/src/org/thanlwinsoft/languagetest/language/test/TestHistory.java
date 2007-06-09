/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/LanguageTest/src/org/thanlwinsoft/languagetest/language/test/TestHistory.java $
 *  Revision        $LastChangedRevision: 852 $
 *  Last Modified:  $LastChangedDate: 2007-06-09 16:02:23 +0700 (Sat, 09 Jun 2007) $
 *  Last Change by: $LastChangedBy: keith $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2003 Keith Stribley <devel@thanlwinsoft.org>
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
 */

package org.thanlwinsoft.languagetest.language.test;

/**
 * An implementation of the TestHistory interface represents a user's test
 * history for one module.
 * @author  keith
 */
public interface TestHistory
{
    
    public ItemHistory getHistoryItem(TestItem item, TestType type) throws
        TestHistoryStorageException;
    //public int getModuleCount();
    //public Iterator iterator(TestType type);
    public void saveResult(TestItem item, TestType type, 
                           long testTime, boolean pass) 
                           throws TestHistoryStorageException;
    public void ignoreItem(TestItemProperties item, TestType type, boolean ignore)
        throws TestHistoryStorageException;
    public void savePermanent() throws TestHistoryStorageException;
    //public void deleteItemType(TestItemProperties item, TestType type) throws TestHistoryStorageException;
    //public void deleteItem(TestItemProperties item) throws TestHistoryStorageException;
    
}
