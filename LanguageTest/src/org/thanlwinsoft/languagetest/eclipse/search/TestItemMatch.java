/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.search;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.search.ui.text.Match;
import org.eclipse.swt.graphics.FontData;
import org.thanlwinsoft.schemas.languagetest.LangEntryType;
import org.thanlwinsoft.schemas.languagetest.TestItemType;

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
        // TODO Auto-generated method stub
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
