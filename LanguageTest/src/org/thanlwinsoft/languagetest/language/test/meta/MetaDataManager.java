/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
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
/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test.meta;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.WorkspaceLanguageManager;
import org.thanlwinsoft.schemas.languagetest.module.ConfigType;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument;

/**
 * @author keith
 *
 */
public class MetaDataManager
{
    public final static String DEFAULT_LANG_CONFIG = 
        "/org/thanlwinsoft/languagetest/language/text/DefaultLangConfig.xml";

    public static ConfigType [] loadConfig()
    {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject [] projects = workspace.getRoot().getProjects();
        Vector <ConfigType> config = new Vector<ConfigType>(projects.length + 1);
        for (IProject p : projects)
        {
            if (!p.isOpen()) continue;
            ConfigType c = WorkspaceLanguageManager.getProjectLangConfig(p);
            if (c != null)
                config.add(c);
        }
        try
        {
            InputStream is = LanguageTestPlugin.getDefault().getBundle()
                .getResource(DEFAULT_LANG_CONFIG).openStream();
            XmlOptions options = new XmlOptions();
            options.setCharacterEncoding("UTF-8");
            options.setLoadUseDefaultResolver();
            options.setDocumentType(LanguageModuleDocument.type);
            LanguageModuleDocument langDoc = 
                LanguageModuleDocument.Factory.parse(is);
            if (langDoc != null && langDoc.getLanguageModule() != null && 
                langDoc.getLanguageModule().isSetConfig())
            {
                ConfigType c = langDoc.getLanguageModule().getConfig();
                config.add(c);
            }
        }
        catch (IOException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, e.getMessage(), e);
        }
        catch (XmlException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, e.getMessage(), e);
        }
        return config.toArray(new ConfigType[config.size()]);
    }
}
