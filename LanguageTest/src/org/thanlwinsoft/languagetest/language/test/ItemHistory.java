/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/test/ItemHistory.java,v $
 *  Version:       $Revision: 706 $
 *  Last Modified: $Date: 2007-01-07 06:30:20 +0700 (Sun, 07 Jan 2007) $
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

package org.thanlwinsoft.languagetest.language.test;

import java.io.File;
import java.util.Date;

import org.eclipse.core.resources.IProject;
/**
 *
 * @author  keith
 */
public class ItemHistory implements TestItemProperties
{
    protected File moduleFile = null;
    protected int moduleId = 0;
    protected long creationTime = -1;
    protected long moduleCreationTime = -1;
    protected long lastFailDate = -1;
    protected long lastPassDate = -1;
    protected long lastTestDate = -1;
    protected int testCount = 0;
    protected int passCount = 0;
    protected String author = "Unknown";
    protected int consecutivePassCount = 0;
    protected Object [][] resultTable = null;
    protected boolean disabled = false;
    /** Creates a new instance of ItemHistory */
    public ItemHistory()
    {
        
    }
    public long getLastFailDate() { return lastFailDate; }
    public long getLastPassDate() { return lastPassDate; }
    public int getConsecutivePassCount() { return consecutivePassCount; }
    public long getLastTestDate() { return lastTestDate; } 
    public File getModuleFile() { return moduleFile; }
    public int getModuleId() { return moduleId; }
    public long getModuleCreationTime() { return moduleCreationTime; }
    public long getCreationTime() { return creationTime; }
    public String getCreator() { return author; }
    public Object [][] getResultTable() { return resultTable; }
    public int getPassCount() { return passCount; }
    public int getTestCount() { return testCount; }
    public boolean isDisabled() { return disabled; }
    public boolean isTestDue(TestType type, UserConfig config)
    {
        long timeNow = new Date().getTime();
        boolean testDue = false;
        // decide whether to include item or not
        if (consecutivePassCount < config.getLearntPassCount(type))
        {
            if (timeNow - lastTestDate > 
                config.getMinRetestPeriod(type))
            {
                testDue = true;
            }
        }
        else if (timeNow - lastFailDate
                 < config.getShortTermPeriod(type))
        {
            if (timeNow - lastPassDate >
                config.getInitialRevisionPeriod(type))
            {
                testDue = true;
            }
        }
        else if (timeNow - lastFailDate
                 < config.getLongTermPeriod(type))
        {
            if (timeNow - lastPassDate >
                config.getShortTermRevisionPeriod(type))
            {
                testDue = true;
            }
        }
        else
        {
            if (timeNow - lastPassDate >
                config.getLongTermRevisionPeriod(type))
            {
                testDue = true;
            }
        }
        return testDue;
    }
    public void removeModuleHistory()
    {
        System.out.println("ItemHistory.removeModuleHistory() not implemented");
        throw new UnsupportedOperationException("ItemHistory.removeModuleHistory()");
    }
    public String getItemStatus(TestType type, IProject userProject)
    {
        String status = null;
        UserConfig config = new UserConfig(userProject);
        long timeNow = new Date().getTime();
        if (consecutivePassCount >= config.getLearntPassCount(type))
        {
            if (timeNow - lastFailDate
                 < config.getShortTermPeriod(type))
            {
                status =  "Recently learnt";
            }
            else if (timeNow - lastFailDate
                < config.getLongTermPeriod(type))
            {   
                status =  "Learnt for short term";
            }
            else
            {
                status =  "Learnt for long term";
            }
        }
        else
        {
            if (passCount > config.getLearntPassCount(type))
            {
                status = "Needs revision";
            }
            else if (testCount > 0)
            {
                status = "Still learning";
            }
            else
            {
                status = "Unlearnt";
            }
        }
        return status;
    }
}
