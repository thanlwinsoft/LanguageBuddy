/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/language/test/ForeignComparator.java $
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
 *
 * @author  Keith Stribley
 */
public class ForeignComparator implements java.util.Comparator
{
    
    /** Creates a new instance of ForeignComparator */
    public ForeignComparator()
    {
    }
    
    public int compare(Object a, Object b)
    {
        int comp = -1;
        if (a.getClass() == TestItem.class &&
            b.getClass() == TestItem.class)
        {
            TestItem itemA = (TestItem)a;
            TestItem itemB = (TestItem)b;
            comp = itemA.getForeignText().compareTo(itemB.getForeignText());
            if (comp == 0 && itemA != itemB)
            {
                // if both objects have the same foreign string, then generate
                // an arbitrary, but repeatable order from the hash code
                comp = a.hashCode() - b.hashCode();
            }
        }
        else if (a == null)
        {
            if (b == null) comp = 0;
            else comp = 1;
        }
        else
        {
            comp = a.toString().compareTo(b.toString());
        }
        return comp;
    }
    
    
}
