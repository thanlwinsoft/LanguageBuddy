/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/test/TestHistory.java,v $
 *  Version:       $Revision: 1.4 $
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

package languagetest.language.test;

import java.util.Iterator;
/**
 *
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
