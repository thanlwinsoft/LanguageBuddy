/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
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
/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test.meta;

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.thanlwinsoft.languagetest.language.test.TestItemFilter;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType;
import org.thanlwinsoft.schemas.languagetest.module.TagType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;

/**
 * @author keith
 *
 */
public class MetaFilter implements TestItemFilter
{
    private Collection<IPath>metaSet = null;
    public enum Mode
    {
        ALL, ANY
    }
    private Mode mode = Mode.ANY;
    
    public MetaFilter(IPath [] paths, Mode m)
    {
        this.metaSet = new Vector<IPath>();
        this.metaSet.addAll(Arrays.asList(paths));
        this.mode = m;
    }
    
    public void setMode(Mode m)
    {
        this.mode = m;
    }
    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.language.test.TestItemFilter#chooseItem(org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType, org.thanlwinsoft.schemas.languagetest.module.TestItemType)
     */
    public boolean chooseItem(LanguageModuleType module, TestItemType item)
    {
        int matchCount = 0;
        for (TagType t : item.getTagArray())
        {
            IPath path = new Path(t.getRef());
            if (metaSet.contains(path))
                matchCount++;
        }
        switch (mode)
        {
        case ALL:
            if (matchCount == metaSet.size())
                return true;
            break;
        case ANY:
            if (matchCount > 0)
                return true;
            break;
        }
        return false;
    }

}