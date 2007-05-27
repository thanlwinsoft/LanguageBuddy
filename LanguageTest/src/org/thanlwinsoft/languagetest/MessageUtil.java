/**
 * 
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
