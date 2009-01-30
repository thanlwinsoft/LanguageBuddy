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

import java.util.Locale;
import java.util.TreeSet;
import java.util.HashMap;
/**
 *
 * @author  keith
 */
public final class Iso3166
{
    private HashMap<String,IsoCountry> codeMap = null;
    private TreeSet<IsoCountry> countries = null;
    private static Iso3166 instance = null;
    /** Creates a new instance of Iso3166 */
    public Iso3166()
    {
        codeMap = new HashMap<String,IsoCountry>();
        countries = new TreeSet<IsoCountry>();
        String [] countryCodes = Locale.getISOCountries();
        
        for (int c = 0; c<countryCodes.length; c++)
        {
            Locale tempLocale = new Locale("en",countryCodes[c]);
            IsoCountry ic = new IsoCountry(countryCodes[c],
                tempLocale.getDisplayCountry());
            countries.add(ic);
            //codeMap.put(countryCodes[c], tempLocale.getDisplayCountry());
            codeMap.put(countryCodes[c], ic);
        }
    }
    
    public static IsoCountry[] getCountries()
    {
        if (instance == null)
        {
            instance = new Iso3166();
        }
        return (IsoCountry[])instance.countries.toArray(new IsoCountry[instance.countries.size()]);
    }
    
    public static String getDescriptionFromCode(String code)
    {
        if (instance == null)
        {
            instance = new Iso3166();
        }
        if (instance.codeMap.containsKey(code))
        {
            return instance.codeMap.get(code).toString();
        }
        return "Unknown code: " + code;
    }
    
    public static IsoCountry getCountry(String code)
    {
        if (instance == null)
        {
            instance = new Iso3166();
        }
        if (instance.codeMap.containsKey(code))
        {
            return (IsoCountry)(instance.codeMap.get(code));
        }
        return null;
    }
    
    
    public final class IsoCountry implements Comparable<IsoCountry>
    {
        private String code = null;
        private String description = null;
        public IsoCountry(String code, String description)
        {
            this.code = code; 
            this.description = description;
        }
        public String getCode()
        {
            return code;
        }
        public String getDescription()
        {
            return description;
        }
        public String toString()
        {
            return getDescription();
        }
        public boolean equals(Object o)
        {
            if (o != null && o instanceof IsoCountry)
            {
                return ((IsoCountry)o).getCode().equals(code);       
            }
            return false;
        }

        @Override
		public int compareTo(IsoCountry arg0)
		{
			return description.compareTo(arg0.getDescription());
		}
        
    }
}
