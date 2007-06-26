/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/MessageUtil.java $
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
package org.thanlwinsoft.languagetest;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IStatus;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;

/**
 * Message Utility for retrieving localised strings.
 * @author keith
 *
 */
public class MessageUtil
{
    private static String resourceBase = "org/thanlwinsoft/languagetest/messages";
    static public ResourceBundle getMsgResource() throws MissingResourceException
    {
        return ResourceBundle.getBundle(resourceBase);
    }
    static public String getString(String id)
    {
        try
        {
            ResourceBundle r = getMsgResource();
            if (r == null) return id;
            return r.getString(id);
        }
        catch (MissingResourceException mre)
        {
            LanguageTestPlugin.log(IStatus.WARNING, mre.getLocalizedMessage(), mre);
            return id;
        }
    }
    static public String getString(String id, String argA)
    {
        Object [] args = {argA};
        try
        {
            ResourceBundle r = getMsgResource();
            if (r == null) return id + " " + argA;
            String baseString = r.getString(id);
            return MessageFormat.format(baseString, args);
        }
        catch (MissingResourceException mre)
        {
            LanguageTestPlugin.log(IStatus.WARNING, mre.getLocalizedMessage(), mre);
            return id + " " + argA;
        }
    }
    static public String getString(String id, String argA, String argB)
    {
        Object [] args = {argA, argB };
        try
        {
            ResourceBundle r = getMsgResource();
            if (r == null) return id + " " + argA + " " + argB;
            String baseString = r.getString(id);
            return MessageFormat.format(baseString, args);
        }
        catch (MissingResourceException mre)
        {
            LanguageTestPlugin.log(IStatus.WARNING, mre.getLocalizedMessage(), mre);
            return id + " " + argA + " " + argB;
        }
    }
    static public String getString(String id, String argA, 
                    String argB, String argC)
    {
        Object [] args = {argA, argB, argC };
        try
        {
            ResourceBundle r = getMsgResource();
            if (r == null) return id + " " + argA + " " + argB + " " + argC;
            String baseString = r.getString(id);
            return MessageFormat.format(baseString, args);
        }
        catch (MissingResourceException mre)
        {
            LanguageTestPlugin.log(IStatus.WARNING, mre.getLocalizedMessage(), mre);
            return id + " " + argA + " " + argB + " " + argC;
        }
    }
    static public String getString(String id, String argA, 
                    String argB, String argC, String argD)
    {
        Object [] args = {argA, argB, argC, argD };
        try
        {
            ResourceBundle r = getMsgResource();
            if (r == null) return id + " " + argA + " " + argB + " " + argC + " " + argD;
            String baseString = r.getString(id);
            return MessageFormat.format(baseString, args);
        }
        catch (MissingResourceException mre)
        {
            LanguageTestPlugin.log(IStatus.WARNING, mre.getLocalizedMessage(), mre);
            return id + " " + argA + " " + argB + " " + argC + " " + argD;
        }
    }
    static public String getString(String id, String argA, 
            String argB, String argC, String argD, String argE)
    {
        Object [] args = {argA, argB, argC, argD, argE };
        try
        {
            ResourceBundle r = getMsgResource();
            if (r == null)
                return id + " " + argA + " " + argB + " " + argC + " " + argD +
                    " " + argE;
            String baseString = r.getString(id);
            return MessageFormat.format(baseString, args);
        }
        catch (MissingResourceException mre)
        {
            LanguageTestPlugin.log(IStatus.WARNING, mre.getLocalizedMessage(), mre);
            return id + " " + argA + " " + argB + " " + argC + " " + argD + " "
                + argE;
        }
    }
}
