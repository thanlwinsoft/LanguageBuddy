/*
 * Iso3166.java
 *
 * Created on April 13, 2004, 7:32 PM
 */

package languagetest.language.text;

import java.util.Locale;
import java.util.TreeSet;
import java.util.HashMap;
/**
 *
 * @author  keith
 */
public final class Iso3166
{
    private HashMap codeMap = null;
    private TreeSet countries = null;
    private static Iso3166 instance = null;
    /** Creates a new instance of Iso3166 */
    public Iso3166()
    {
        codeMap = new HashMap();
        countries = new TreeSet();
        String [] countryCodes = Locale.getISOCountries();
        
        for (int c = 0; c<countryCodes.length; c++)
        {
            Locale tempLocale = new Locale("en",countryCodes[c]);
            IsoCountry ic = new IsoCountry(countryCodes[c],
                tempLocale.getDisplayCountry());
            countries.add(ic);
            codeMap.put(countryCodes[c],  tempLocale.getDisplayCountry());
        }
    }
    
    public static Object[] getCountries()
    {
        if (instance == null)
        {
            instance = new Iso3166();
        }
        return instance.countries.toArray();
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
    
    
    public final class IsoCountry implements Comparable
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
        
        public int compareTo(Object o)
        {
            IsoCountry ic = (IsoCountry)o;
            return description.compareTo(ic.getDescription());
        }
        
    }
}
