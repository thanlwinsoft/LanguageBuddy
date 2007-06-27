/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/util/RelativePath.java,v $
 *  Version:       $Revision: 855 $
 *  Last Modified: $Date: 2007-06-10 07:02:09 +0700 (Sun, 10 Jun 2007) $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2003 Keith Stribley <tech@thanlwinsoft.org>
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

package org.thanlwinsoft.languagetest.util;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import org.eclipse.core.runtime.IStatus;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;

/**
 *
 * @author  keith
 */
public class RelativePath
{
    static final String dirSeparator = new String("/");
        /** Method to determine the path of a relative to b */
    static public String aToB(File a, File b)
    {
        int minTokens = 0;
        String relativePath = "";
        String aToken, bToken;
        String pathFromBase = "";
        int upLevels = 0;
        int commonTokens = 0;
        // always use the same character so files can be used on all 
        // platforms
        
        if (File.listRoots().length > 1) minTokens = 1;
        try
        {
            StringTokenizer aTokens = new StringTokenizer(a.getCanonicalPath(), File.separator);
            StringTokenizer bTokens = new StringTokenizer(b.getCanonicalPath(), File.separator);
            while (aTokens.hasMoreTokens() && bTokens.hasMoreTokens())
            {
                aToken = aTokens.nextToken();
                bToken = bTokens.nextToken();
                // break out of the loop if tokens aren't identical
                if (aToken.compareTo(bToken)!=0) 
                {
                    upLevels = 1;
                    pathFromBase = aToken;
                    if (aTokens.hasMoreTokens())
                    {
                        pathFromBase += dirSeparator;
                    }
                    break;
                }
                else
                {
                    commonTokens++;
                }
            }
            if (commonTokens <= minTokens)
            {
                // nothing in common so give cannonical path
                relativePath = a.getCanonicalPath();
            }
            else
            {
                // calculate the number of levels to go up
                upLevels += bTokens.countTokens();
                // subtract one from this if b isn't a directory
                if (!b.isDirectory()) upLevels--;
                for (int i=0; i<upLevels; i++)
                {
                    relativePath += ".." + dirSeparator;
                }
                // add the additional levels that a has above the shared base path
                while (aTokens.hasMoreTokens())
                {
                    pathFromBase += aTokens.nextToken();
                    // don't want to add path separator if this is last token
                    if (aTokens.hasMoreTokens()) pathFromBase += dirSeparator;
                }
                relativePath += pathFromBase;
            }
        }
        catch (NoSuchElementException nsee)
        {
            // shouldn't happen because we're checking hasMoreTokens all the time
            LanguageTestPlugin.log(IStatus.WARNING, "RelativePath", nsee);
            // default to absolute path, which doesn't throw exception
            relativePath = a.getAbsolutePath();
        }
        catch (IOException ioe)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "RelativePath", ioe);
            // default to absolute path, which doesn't throw exception
            relativePath = a.getAbsolutePath();
        }
        return relativePath;
    }
    
}