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
package org.thanlwinsoft.languagetest.language.test;

/**
 * Class to hold the various options for a test
 * @author keith
 *
 */
public class TestOptions
{
    protected TestType type = null;
    protected boolean useHistory = true;
    protected boolean repeatUntilLearnt = true;
    protected boolean includeNew = true;
    private TestItemFilter [] filters = new TestItemFilter[0];
    public TestOptions(TestType type)
    {
        this.type = type;
    }
    public TestOptions(TestType type, boolean useHistory, boolean repeatUntilLearnt)
    {
        this.type = type;
        this.useHistory = useHistory;
        this.repeatUntilLearnt = repeatUntilLearnt;
    }
    public void setFilters(TestItemFilter [] filters)
    {
        this.filters = null;
    }
    public TestItemFilter [] getFilters()
    {
        return filters;
    }
}
