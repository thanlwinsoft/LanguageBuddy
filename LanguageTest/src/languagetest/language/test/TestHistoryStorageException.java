/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/test/TestHistoryStorageException.java,v $
 *  Version:       $Revision: 1.1 $
 *  Last Modified: $Date: 2003/12/26 13:21:54 $
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

package languagetest.language.test;

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
