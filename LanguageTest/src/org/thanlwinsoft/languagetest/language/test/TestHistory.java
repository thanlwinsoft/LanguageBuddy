/*
 * -----------------------------------------------------------------------
 *  File:          $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/LanguageTest/src/org/thanlwinsoft/languagetest/language/test/TestHistory.java $
 *  Version:       $Revision: 704 $
 *  Last Modified: $Date: 2007-01-05 05:50:38 +0700 (Fri, 05 Jan 2007) $
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

import java.util.Iterator;
/**
 * An implementation of the TestHistory interface represents a user's test
 * history for one module.
 * @author  keith
 */
public interface TestHistory
{
    
    public ItemHistory getHistoryItem(TestItem item, TestType type) throws
        TestHistoryStorageException;
    public int getModuleCount();
    public Iterator iterator(TestType type);
    public void saveResult(TestItem item, TestType type, 
                           long testTime, boolean pass) 
                           throws TestHistoryStorageException;
    public void ignoreItem(TestItemProperties item, TestType type, boolean ignore)
        throws TestHistoryStorageException;
    public void savePermanent() throws TestHistoryStorageException;
    public void deleteItemType(TestItemProperties item, TestType type) throws TestHistoryStorageException;
    public void deleteItem(TestItemProperties item) throws TestHistoryStorageException;
    
}
