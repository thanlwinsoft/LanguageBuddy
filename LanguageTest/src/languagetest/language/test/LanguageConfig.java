/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/test/LanguageConfig.java,v $
 *  Version:       $Revision: 1.4 $
 *  Last Modified: $Date: 2004/05/06 16:25:09 $
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
public class LanguageConfig
{
    private UniversalLanguage nativeLocale = null;
    private UniversalLanguage foreignLocale = null;
    private static LanguageConfig currentConfig = null;
    /** Creates a new instance of LanguageConfig */
    public LanguageConfig(UniversalLanguage nativeLocale, UniversalLanguage foreignLocale)
    {
        this.nativeLocale = nativeLocale;
        this.foreignLocale = foreignLocale;
        System.out.println(nativeLocale.getDescription() + ":" +
                           foreignLocale.getDescription());
    }
    public String getForeignLanguageName()
    {
        return foreignLocale.getDescription();
    }
    public String getNativeLanguageName()
    {
        return nativeLocale.getDescription();
    }
    public String getForeignLangCode()
    {
        return foreignLocale.getCode();
    }
    public String getNativeLangCode()
    {
        return nativeLocale.getCode();
    }
    public static LanguageConfig getCurrent()
    {
        return currentConfig;
    }
    public UniversalLanguage getNativeLanguage()
    {
        return nativeLocale;
    }
    public UniversalLanguage getForeignLanguage()
    {
        return foreignLocale;
    }
    public void load() throws TestHistoryStorageException
    {
        currentConfig = this;
        // notify user config that language has changed so history can be
        // changed accordingly
        if (UserConfig.getCurrent() != null)
        {
            UserConfig.getCurrent().reloadTestHistory();
        }
    }
    
    public void unload()  throws TestHistoryStorageException
    {
        if (currentConfig != null && UserConfig.getCurrent() != null)
        {   
            UserConfig.getCurrent().getTestHistory().savePermanent();
        }
        currentConfig = null;
    }
}
