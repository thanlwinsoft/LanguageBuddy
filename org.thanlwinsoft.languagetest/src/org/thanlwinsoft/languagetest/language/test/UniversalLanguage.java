/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/language/test/UniversalLanguage.java $
 *  Revision        $LastChangedRevision: 936 $
 *  Last Modified:  $LastChangedDate: 2007-08-03 05:14:14 +0700 (Fri, 03 Aug 2007) $
 *  Last Change by: $LastChangedBy: keith $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2004 Keith Stribley <devel@thanlwinsoft.org>
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

import java.util.regex.Pattern;

import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.language.text.Iso15924;
import org.thanlwinsoft.languagetest.language.text.Iso3166;
import org.thanlwinsoft.languagetest.language.text.Iso639;
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
    private String encoding = "";
    private String description = "";
    private String completeCode = "";
    private static final Pattern langPattern = Pattern.compile("[a-z]{2,3}");
    private static final Pattern countryPattern = Pattern.compile("[A-Z]{2}");
    private static final Pattern variantPattern = Pattern.compile("[A-Za-z0-9]*");
    private static final Pattern scriptPattern = Pattern.compile("[A-Z][a-z]{3}");
    private static final Pattern encodingPattern = Pattern.compile("[A-Za-z0-9-_]*");
    
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
        	String fullScript = code.substring(scriptStart + 1, scriptStop);
        	int dotIndex = fullScript.indexOf('.');
        	if (dotIndex > -1)
        	{
        		this.script = fullScript.substring(0, dotIndex);
        		this.encoding = fullScript.substring(dotIndex + 1);
        	}
        	else this.script = fullScript;            
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
        validate();
    }
    
    public UniversalLanguage(String language, String country, 
                             String variant, String script, String encoding)
    	throws IllegalArgumentException
    {
    	createFromParts(language, country, variant, script, encoding);
    }
    public UniversalLanguage(String [] parts)
    	throws IllegalArgumentException
	{
    	if (parts.length > 0) langCode = parts[0];
    	if (parts.length > 1) countryCode = parts[1];
    	if (parts.length > 2) variant = parts[2];
    	if (parts.length > 3) script = parts[3];
    	if (parts.length > 4) encoding = parts[4];

    	createFromParts(langCode, countryCode, variant, script, encoding);
	}
    
    protected void createFromParts(String language, String country, 
            String variant, String script, String encoding)
    {
        this.langCode = language;
        this.countryCode = country;
        this.variant = variant;
        this.script = script;
        this.encoding = encoding;
        
        validate();
        
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
            if (encoding.length() > 0)
            {
            	b.append('.');
            	b.append(encoding);
            }
            b.append(']');
        }
        completeCode = b.toString();
        b.delete(0, b.length());
    }
    
    private boolean validate() throws IllegalArgumentException
    {
    	valid = langPattern.matcher(langCode).matches();
    	valid &= Iso639.isValidCode(langCode);
    	if (!valid)
    	{
    		throw new IllegalArgumentException(MessageUtil.getString("InvalidLanguage", 
    				toString()));
    	}
    	if (countryCode.length() > 0)
    	{
    		valid &= countryPattern.matcher(countryCode).matches();
    		if (!valid)
        	{
        		throw new IllegalArgumentException(MessageUtil.getString("InvalidCountry", 
        				countryCode));
        	}
    	}
    	valid &= variantPattern.matcher(variant).matches();
    	if (!valid)
    	{
    		throw new IllegalArgumentException(MessageUtil.getString("InvalidVariant", 
    				variant));
    	}
        if (script.length() > 0)
        {
        	valid &= scriptPattern.matcher(script).matches();
        	if (!valid)
        	{
        		throw new IllegalArgumentException(MessageUtil.getString("InvalidScript", 
        				script));
        	}
        	valid &= encodingPattern.matcher(encoding).matches();
        	if (!valid)
        	{
        		throw new IllegalArgumentException(MessageUtil.getString("InvalidEncoding", 
        				encoding));
        	}
        }
    	return valid;
    }
    
    public String getICUlocaleID()
    {
        if (countryCode.length()> 0)
        {
            if (script.length() > 0)
            {
                return langCode + "_" + script + "_" + countryCode;
            }
            else
            {
                return langCode + "_" + countryCode;
            }
        }
        else if (script.length() > 0)
        {
            return langCode + "_" + script;
        }
        return langCode;
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
            if (encoding.length() > 0)
            {
            	b.append(", ");
            	b.append(encoding);
            }
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
    public String [] toArray()
    {
    	return new String[] { langCode, countryCode, variant, script, encoding };
    }
}
