/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
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
package org.thanlwinsoft.languagetest.eclipse.editors;

import java.util.HashSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.search.TestItemMatch;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;


/**
 * @author keith
 *
 */
public class TestItemSorter extends ViewerComparator
{
    private String column = null;
    private int columnIndex = -1;
    private int sortFactor = 1;
    private Collator icuCollator = null;
    private HashSet<ULocale> locales;
    public TestItemSorter()
    {
        locales = new HashSet<ULocale>();
        ULocale [] available = Collator.getAvailableULocales();
        for (ULocale l : available)
            locales.add(l);
    }
    public void setSortColumn(int colIndex, String col)
    {
       this.columnIndex = colIndex;
       this.column = col;
    }
    public void setLocale(String localeID)
    {
        if (localeID == null)
            icuCollator = Collator.getInstance();
        else
        {
            ULocale locale = new ULocale(localeID);
            if (!locales.contains(locale))
            {
                int firstSep = localeID.indexOf('_');
                if (firstSep > -1)
                {
                    String langOnly = localeID.substring(0, firstSep);
                    locale = new ULocale(langOnly);
                    if (!locales.contains(locale))
                    {
                        LanguageTestPlugin.log(IStatus.WARNING,
                                "No Collator found for " + localeID);
                    }
                }
                
            }
            icuCollator = Collator.getInstance(locale);
        }
    }
    
    
    public void setSortDirection(int d)
    {
        if (d == SWT.DOWN)
            sortFactor = 1;
        else if (d == SWT.UP)
            sortFactor = -1;
        else
            sortFactor = 0;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public int compare(Viewer viewer, Object e1, Object e2)
    {
        TestItemType i1 = null;
        TestItemType i2 = null;
        if (e1 instanceof TestItemType && e2 instanceof TestItemType)
        {
            i1 = (TestItemType)e1;
            i2 = (TestItemType)e2;
        }
        if (e1 instanceof TestItemMatch && e2 instanceof TestItemMatch)
        {
            i1 = ((TestItemMatch)e1).getTestItem();
            i2 = ((TestItemMatch)e2).getTestItem();
        }
        if (i1 != null && i2 != null)
        {
            if (column.equals(TestItemEditor.CREATION_DATE))
            {
                if (i1.getCreationTime() < i2.getCreationTime())
                    return -sortFactor;
                else if (i1.getCreationTime() == i2.getCreationTime())
                    return 0;
                return sortFactor;
            }
            if (viewer instanceof TableViewer && columnIndex > -1)
            {
                TableViewer tv = (TableViewer)viewer;
                ITableLabelProvider tlp = (ITableLabelProvider)tv.getLabelProvider();
                String s1 = tlp.getColumnText(e1, columnIndex);
                String s2 = tlp.getColumnText(e2, columnIndex);
                if (s1 == null) s1 = "";
                if (s2 == null) s2 = "";
                if (icuCollator != null)
                {
                    return sortFactor * icuCollator.compare(s1, s2);
                }
                return sortFactor * super.compare(null, e1, e2);
            }
        }
        return sortFactor * super.compare(viewer, e1, e2);
    }

}
