/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/test/NativeComparator.java,v $
 *  Version:       $Revision: 1.3 $
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

/**
 *
 * @author  Keith Stribley
 */
public class NativeComparator implements java.util.Comparator
{
    
    /** Creates a new instance of NativeComparator */
    public NativeComparator()
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
            comp = itemA.getNativeText()
                .compareToIgnoreCase(itemB.getNativeText());
            if (comp == 0 && itemA != itemB)
            {
                // if both objects have the same native string, then generate
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
