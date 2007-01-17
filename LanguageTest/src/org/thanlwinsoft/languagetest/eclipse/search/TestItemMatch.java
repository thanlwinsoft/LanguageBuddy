/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.search;

import org.eclipse.core.resources.IFile;
import org.eclipse.search.ui.text.Match;
import org.thanlwinsoft.schemas.languagetest.LangEntryType;

/**
 * @author keith
 *
 */
public class TestItemMatch extends Match
{
    private int itemIndex = 0;
    private String langCode = "";
    public TestItemMatch(IFile file, int item, LangEntryType let, int offset, int length)
    {
        super(file, offset, length);
        this.itemIndex = item;
        this.langCode = let.getLang();
    }
    
}
