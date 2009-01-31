/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/language/test/NativeComparator.java $
 *  Revision        $LastChangedRevision: 1388 $
 *  Last Modified:  $LastChangedDate: 2009-01-31 19:32:00 +0700 (Sat, 31 Jan 2009) $
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
 *
 * @author  Keith Stribley
 */
public class NativeComparator implements java.util.Comparator<TestItem>
{
    
    /** Creates a new instance of NativeComparator */
    public NativeComparator()
    {
    }
    
    public int compare(TestItem itemA, TestItem itemB)
    {
        int comp = -1;
        if (itemA != null && itemB != null)
        {
	        comp = itemA.getNativeText()
	            .compareToIgnoreCase(itemB.getNativeText());
	        if (comp == 0 && itemA != itemB)
	        {
	            // if both objects have the same native string, then generate
	            // an arbitrary, but repeatable order from the hash code
	            comp = itemA.hashCode() - itemB.hashCode();
	        }
        }
        else if (itemA == null)
        {
            if (itemB == null) comp = 0;
            else comp = 1;
        }
        else
        {
            comp = -1;
        }
        return comp;
    }
    
    
}
