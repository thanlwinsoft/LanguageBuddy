/*
 * UniversalLocale.java
 *
 * Created on April 13, 2004, 11:31 AM
 */

package languagetest.language.test;

import languagetest.language.text.Iso15924;
import languagetest.language.text.Iso3166;
import languagetest.language.text.Iso639;
/**
 * A version of locale that allows empty language and country strings
 * @author  keith
 */
public class UniversalLanguage 
{
    
    /** Creates a new instance of UniversalLocale */
    private String langCode = "";
    private String countryCode = "";
    private String variant = "";
    private String script = "";
    private String description = "";
    private String completeCode = "";
    public static final int NATIVE_LANG = 0;
    public static final int FOREIGN_LANG = 1;
    private boolean valid = false;
    
    public UniversalLanguage(String code)
    {
        this.completeCode = code;
        int scriptStart = code.indexOf('[');
        int scriptStop = code.indexOf(']');
        if (scriptStart > -1 && scriptStop > scriptStart)
        {
            this.script = code.substring(scriptStart + 1, scriptStop);
        }
        else
        {
            scriptStart = code.length();
        }
        int firstUnderScore = code.indexOf('_');
        if (firstUnderScore > -1)
        {
            this.langCode = code.substring(0,firstUnderScore); 
            int secondUnderScore = code.indexOf('_',firstUnderScore + 1);
            if (secondUnderScore > -1)
            {
                countryCode = code.substring(firstUnderScore + 1,
                                             secondUnderScore);
                variant = code.substring(secondUnderScore + 1, scriptStart);
            }
            else
            {
                countryCode = code.substring(firstUnderScore + 1, 
                                               scriptStart);                
            }
        }
        else // appears to only have a language code
        {
            this.langCode = code.substring(0, scriptStart);
        }
        this.description = generateDescription();
        this.valid = Iso639.isValidCode(langCode);
    }
    
    public UniversalLanguage(String language, String country, 
                             String variant, String script)
    {
        this.langCode = language;
        this.countryCode = country;
        this.variant = variant;
        this.script = script;
        this.valid = Iso639.isValidCode(langCode);
        
        this.description = generateDescription();
        StringBuffer b = new StringBuffer();
        b.delete(0, b.length());
        
        b.append(langCode);
        if (countryCode.length()>0 || variant.length() > 0)
        {
            b.append('_');
            b.append(countryCode);
        }
        if (variant.length() > 0)
        {
            b.append('_');
            b.append(variant);
        }
        if (script.length() > 0)
        {
            b.append('[');
            b.append(script);
            b.append(']');
        }
        completeCode = b.toString();
        b.delete(0, b.length());
    }
    
    protected String generateDescription()
    {
        StringBuffer b = new StringBuffer(Iso639.getDescription(langCode));
        if (countryCode.length()>0)
        {
            b.append('(');
            b.append(Iso3166.getDescriptionFromCode(countryCode));
            if (variant.length() > 0)
            {
                b.append(',');
                b.append(variant);
            }
            b.append(')');            
        }
        else if (variant.length() > 0)
        {
            b.append('(');
            b.append(variant);
            b.append(')');    
        }
        if (script.length()>0)
        {
            b.append(" [Script: ");
            b.append(Iso15924.getDescription(script));
            b.append(']');
        }
        return b.toString();
    }
    
    public String getCountryCode()
    {
        return countryCode;
    }
    
    public String getLanguageCode()
    {
        return langCode;
    }
    
    public String getCode()
    {
        
        return completeCode;
    }
    public String getVariant()
    {
        return variant;
    }
    public String getScriptCode()
    {
        return script;
    }
    public boolean equals(Object o)
    {
        if (o instanceof UniversalLanguage)
        {
            return ((UniversalLanguage)o).getCode().equals(getCode());
        }
        return false;
    }
    public int hashCode()
    {
        return completeCode.hashCode();
    }
    public String getDescription()
    {
        return description;
    }
    /**
     * The user may prefer a different description to the default generated one
     */
    public void setDescription(String newDesc)
    {
        description = newDesc;
    }
    public boolean isValid()
    {
        return valid;
    }
    
    public String toString()
    {
        return description;
    }
}
