/**
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/language/test/TestItem.java $
 *  Revision        $LastChangedRevision: 855 $
 *  Last Modified:  $LastChangedDate: 2007-06-10 07:02:09 +0700 (Sun, 10 Jun 2007) $
 *  Last Change by: $LastChangedBy: keith $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2003, 2007 Keith Stribley <devel@thanlwinsoft.org>
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

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.graphics.FontData;

/**
 * @author keith
 *
 */
public interface TestItem
{
    /**
     * @return
     */
    long getModuleCreationTime();
    /**
     * @return
     */
    int getModuleId();
    /**
     * @return
     */
    long getCreationTime();

    /**
     * @return
     */
    String getCreator();

    /**
     * @return
     */
    String getForeignText();

    /**
     * @return
     */
    String getNativeText();

    /**
     * @return
     */
    IPath getSoundFile();
    
    /**
     * offset in ms
     * @return
     */
    long getPlayStart();
    
    /**
     * end offset in ms
     */
    long getPlayEnd();

    /**
     * @return
     */
    int getTestCount();

    /**
     * @return
     */
    boolean isPassed();

    /**
     * 
     */
    void reset();

    /**
     * 
     */
    void setPassed(boolean pass);
    
    /** Retieve the font data to render the native text
     * @return FontData or null
     */
    FontData getNativeFontData();
    /** Retrieve the font data to render the foreign text 
     * May be null.
     * @return FontData or null
     */
    FontData getForeignFontData();
    /**
     * @return
     */
    IPath getModulePath();
    
    /**
     * Path to the image file or null if there is no associated image
     * @return
     */
    IPath getImagePath();
}
