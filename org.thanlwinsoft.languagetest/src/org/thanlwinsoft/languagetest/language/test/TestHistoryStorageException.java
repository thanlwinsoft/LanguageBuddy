/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/language/test/TestHistoryStorageException.java $
 *  Revision        $LastChangedRevision: 852 $
 *  Last Modified:  $LastChangedDate: 2007-06-09 16:02:23 +0700 (Sat, 09 Jun 2007) $
 *  Last Change by: $LastChangedBy: keith $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2003 Keith Stribley <devel@thanlwinsoft.org>
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
 *
 * @author  keith
 */
public class TestHistoryStorageException extends java.lang.Exception
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -1613205654091920607L;
	/**
     * Creates a new instance of <code>TestHistoryStorageException</code> without detail message.
     */
    public TestHistoryStorageException()
    {
    }
    
    
    /**
     * Constructs an instance of <code>TestHistoryStorageException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TestHistoryStorageException(String msg)
    {
        super(msg);
    }
    public TestHistoryStorageException(Exception e)
    {
        super(e.getMessage());
    }
}
