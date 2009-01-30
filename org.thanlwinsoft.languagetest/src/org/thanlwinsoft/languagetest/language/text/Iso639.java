/*
 *  Copyright (C) 2004 Keith Stribley <tech@thanlwinsoft.org>
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
 */

package org.thanlwinsoft.languagetest.language.text;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
/**
 *
 * @author  keith
 */
public final class Iso639
{
    private static Iso639 instance;
    private HashMap<String,IsoLanguage> langMap = null;
    private Vector<IsoLanguage> langList = null;
    private static final String LANG_NAME_RE = "([^\\t]+)";
    private static final String THREE_LETTER_RE = "(\\w{3}+)";
    private static final String TWO_LETTER_RE = "(\\w{2}+)";
    private static final String LINE_RE = 
        LANG_NAME_RE + "\\t" + LANG_NAME_RE + "\\t" + THREE_LETTER_RE + 
        "(/" + THREE_LETTER_RE + "\\*)?(\\t" + TWO_LETTER_RE + ")?.*";
    /** Creates a new instance of Iso639 */
    public Iso639()
    {
        URL isoDataUrl = 
            this.getClass().getResource("/org/thanlwinsoft/languagetest/language/text/iso639-2.txt");
        langMap = new HashMap<String,IsoLanguage>();
        langList = new Vector<IsoLanguage>();
        try
        {
            loadIsoData(isoDataUrl);
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        instance = this;
    }
    
    public static String getDescription(String code)
    {
        if (instance == null)
        {
            instance = new Iso639();
        }        
        return instance.getDescriptionFromCode(code);
    }
    
    public static IsoLanguage getLanguage(String code)
    {
        if (instance == null)
        {
            instance = new Iso639();
        }        
        if (instance.langMap.containsKey(code))
        {
            IsoLanguage il = (IsoLanguage)(instance.langMap.get(code));
            return il;
        }
        return null;
    }
    
    
    public static Vector<IsoLanguage> getLanguages()
    {
        if (instance == null)
        {
            instance = new Iso639();
        }        
        return instance.langList;
    }
    
    private void loadIsoData(URL isoDataUrl) throws IOException
    {
        BufferedReader r = 
            new BufferedReader(new InputStreamReader(isoDataUrl.openStream()));
        String line = r.readLine();
        Pattern p = Pattern.compile(LINE_RE);
        Matcher m = null;
        while (line != null)
        {
            m = p.matcher(line);
            if (m.matches())
            {
                IsoLanguage il = new IsoLanguage(m.group(1), m.group(2));
                il.add3LetterCode(m.group(3));
                langMap.put(m.group(3), il);
                if (m.group(4) != null)
                {
                    il.add3LetterCode(m.group(5));
                    langMap.put(m.group(5),il);
                }
                if (m.group(6) != null)
                {
                    il.add2LetterCode(m.group(7));
                    langMap.put(m.group(7), il);
                }
                langList.add(il);
            }
            else
            {
                System.out.println("Didn't match: " + line);
            }
            line = r.readLine();
        }
        r.close();
    }
    
    protected String getDescriptionFromCode(String code)
    {
        if (langMap.containsKey(code))
        {
            IsoLanguage il = (IsoLanguage)langMap.get(code);
            return il.getDescription();
        }
        return "Unknown Language: " + code;
    }
    
    public static boolean isValidCode(String code)
    {
        if (instance == null)
        {
            instance = new Iso639();
        }   
        return instance.langMap.containsKey(code);
    }
    
    public final class IsoLanguage
    {
        String enDescription = null;
        String frDescription = null;
        String twoLetter = null;
        Vector<String> threeLetter = null;
        public IsoLanguage(String enDescription, String frDescription)
        {
            this.enDescription = enDescription;
            this.frDescription = frDescription;
            threeLetter = new Vector<String>();
        }
        public void add2LetterCode(String code)
        {
            twoLetter = code;
        }
        public void add3LetterCode(String code)
        {
            threeLetter.add(code);
        }
        public boolean equals(Object o)
        {
            if (o == null || ! (o instanceof IsoLanguage)) return false;
            IsoLanguage il = (IsoLanguage)o;
            if (il.getCode().length() == 2 && twoLetter != null)
            {
                return il.getCode().equals(twoLetter);
            }
            else
            {
                for (int i = 0; i<threeLetter.size(); i++)
                {
                    if (il.getCode().equals(threeLetter.elementAt(i))) return true;
                }
            }
            return false;
        }
        /**
         * This returns the default code for the language. If a 2 letter code
         * exists then it is returned in favour of any 3 letter codes.
         */
        public String getCode()
        {
            if (twoLetter == null)
            {
                if (threeLetter.size() > 0) 
                {
                    return threeLetter.elementAt(0).toString();
                }
                return "";
            }
            else
            {
                return twoLetter;
            }                
        }
        public String toString()
        {
            return getDescription();
        }
        public String getDescription()
        {
            return enDescription;
        }
    }
    
    public static void main(String args[])
    {
        new Iso639();
    }
}
