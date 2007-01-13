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
    static public ResourceBundle getMsgResource()
    {
        return ResourceBundle.getBundle(resourceBase);
    }
    static public String getString(String id)
    {
        ResourceBundle r = getMsgResource();
        if (r == null) return id;
        try
        {
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
        ResourceBundle r = getMsgResource();
        if (r == null) return id + " " + argA;
        Object [] args = {argA};
        try
        {
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
        ResourceBundle r = getMsgResource();
        if (r == null) return id + " " + argA + " " + argB;
        Object [] args = {argA, argB };
        try
        {
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
        ResourceBundle r = getMsgResource();
        if (r == null) return id + " " + argA + " " + argB + " " + argC;
        Object [] args = {argA, argB, argC };
        try
        {
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
        ResourceBundle r = getMsgResource();
        if (r == null) return id + " " + argA + " " + argB + " " + argC + " " + argD;
        Object [] args = {argA, argB, argC, argD };
        try
        {
            String baseString = r.getString(id);
            return MessageFormat.format(baseString, args);
        }
        catch (MissingResourceException mre)
        {
            LanguageTestPlugin.log(IStatus.WARNING, mre.getLocalizedMessage(), mre);
            return id + " " + argA + " " + argB + " " + argC + " " + argD;
        }
    }
}
