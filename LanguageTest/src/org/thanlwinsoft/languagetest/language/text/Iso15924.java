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
public final class Iso15924
{
    private static Iso15924 instance;
    private HashMap codeMap = null;
    private HashMap idMap = null;
    private Vector scriptList = null;
    private static final String CODE_RE = "([\\w]+)";
    private static final String ID_RE = "([\\d]+)";
    private static final String DESC_RE = "(.*)";
    private static final String LINE_RE = 
        CODE_RE + "\t" + ID_RE + "\t" + DESC_RE;
    /** Creates a new instance of Iso639 */
    public Iso15924()
    {
        URL isoDataUrl = 
            this.getClass().getResource("/org/thanlwinsoft/languagetest/language/text/iso15924-2000.txt");
        codeMap = new HashMap();
        idMap = new HashMap();
        scriptList = new Vector();
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
            instance = new Iso15924();
        }        
        return instance.getDescriptionFromCode(code);
    }
    
    public static Vector getScripts()
    {
        if (instance == null)
        {
            instance = new Iso15924();
        }        
        return instance.scriptList;
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
                try
                {
                    Integer id = new Integer(Integer.parseInt(m.group(2)));
                    IsoScript is = new IsoScript(m.group(1), id.intValue(), m.group(3));
                
                    codeMap.put(m.group(1), is);
                    idMap.put(id, is);
                    scriptList.add(is);
                }
                // should only occur during development
                catch (NumberFormatException e)
                {
                    System.out.println(e);
                }
            }
            line = r.readLine();
        }
        r.close();
    }
    
    protected String getDescriptionFromCode(String code)
    {
        if (codeMap.containsKey(code))
        {
            IsoScript il = (IsoScript)codeMap.get(code);
            return il.getDescription();
        }
        return "Unknown Script: " + code;
    }
    
    public static String getDescriptionFromId(int id)
    {
        if (instance == null)
        {
            instance = new Iso15924();
        }   
        Integer theId = new Integer(id);
        if (instance.idMap.containsKey(theId))
        {
            IsoScript il = (IsoScript)instance.idMap.get(theId);
            return il.getDescription();
        }
        return "Unknown Script Id: " + id;
    }
    
    public static IsoScript getScript(String code)
    {
        if (instance == null)
        {
            instance = new Iso15924();
        }   
        if (instance.codeMap.containsKey(code))
        {
            IsoScript is = (IsoScript)(instance.codeMap.get(code));
            return is;
        }
        return null;
    }
    
    
    public final class IsoScript
    {
        String enDescription = null;
        int id = 0;
        String code = null;
        public IsoScript(String code, int id, String enDescription)
        {
            this.enDescription = enDescription;
            this.id = id;
            this.code = code;
        }
        public boolean equals(Object o)
        {
            if (o == null) return false;
            if (o instanceof IsoScript)
            {
                return ((IsoScript)o).getCode().equals(code);       
            }
            return false;
        }
        /**
         * This returns the default code for the language. If a 2 letter code
         * exists then it is returned in favour of any 3 letter codes.
         */
        public String toString()
        {
            return getDescription();           
        }
        public String getDescription()
        {
            return enDescription;
        }
        public String getCode()
        {
            return code;
        }
    }
}
