/* -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/search/TestItemMatch.java $
 *  Revision        $LastChangedRevision: 852 $
 *  Last Modified:  $LastChangedDate: 2007-06-09 16:02:23 +0700 (Sat, 09 Jun 2007) $
 *  Last Change by: $LastChangedBy: keith $
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
package org.thanlwinsoft.languagetest.eclipse.search;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.search.ui.text.Match;
import org.eclipse.swt.graphics.FontData;
import org.thanlwinsoft.schemas.languagetest.module.LangEntryType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;

/**
 * @author keith
 *
 */
public class TestItemMatch extends Match
{
    private int itemIndex = 0;
    private String langCode = "";
    private String entry = null;
    private HashMap fontMap = null;
    private TestItemType testItem = null;
    public TestItemMatch(IFile file, int item, TestItemType testItem, 
                         LangEntryType let, int offset, 
                         int length, HashMap fontMap)
    {
        super(file, offset, length);
        this.itemIndex = item;
        this.langCode = let.getLang();
        this.entry = let.getStringValue(); 
        this.testItem = (TestItemType)testItem.copy();
        this.fontMap = fontMap;
    }
    public String toString()
    {
        return entry;
    }
    public FontData getFontData()
    {
        return (FontData)fontMap.get(langCode);
    }
    /* (non-Javadoc)
     * @see org.eclipse.search.ui.text.Match#getBaseUnit()
     */
    public int getBaseUnit()
    {
        return super.getBaseUnit();
    }
    /**
     * @return
     */
    public int getTestItemIndex()
    {
        return itemIndex;
    }
    /**
     * Retreive the map of language codes to fonts
     * @return
     */
    public Map getFontMap()
    {
        return fontMap;
    }
    public TestItemType getTestItem()
    {
        return testItem;
    }
}
