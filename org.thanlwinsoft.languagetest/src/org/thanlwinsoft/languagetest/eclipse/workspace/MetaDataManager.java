/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
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
/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.workspace;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.language.test.meta.MetaNode;
import org.thanlwinsoft.schemas.languagetest.module.ConfigType;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.module.MetaDataType;

/**
 * @author keith
 *
 */
public class MetaDataManager
{
    public final static String DEFAULT_LANG_CONFIG = 
        "/org/thanlwinsoft/languagetest/language/text/DefaultLangConfig.xml";
    public MetaDataManager() {}
    
    public static ConfigType [] loadConfig()
    {
        Map <ConfigType, ConfigDetails> map = loadConfigFromProjects();
        return map.keySet().toArray(new ConfigType[map.size()]);
    }
    protected static Map <ConfigType, ConfigDetails> loadConfigFromProjects()
    {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject [] projects = workspace.getRoot().getProjects();
        Map <ConfigType, ConfigDetails> config = 
            new HashMap<ConfigType, ConfigDetails>();
        for (IProject p : projects)
        {
            if (!p.isOpen()) continue;
            IFile file = p.getFile(WorkspaceLanguageManager.LANG_FILE);
            LanguageModuleDocument doc;
            try
            {
                doc = LanguageModuleDocument.Factory.parse(file.getContents(), 
                        getXmlOptions());
                if (doc == null || doc.getLanguageModule() == null ||
                        doc.getLanguageModule().isSetConfig() == false)
                    continue;
                ConfigType c = doc.getLanguageModule().getConfig();
                if (c != null)
                {
                    ConfigDetails cd = new MetaDataManager().new ConfigDetails(file, doc);
                    config.put(c,  cd);
                }
            }
            catch (XmlException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, 
                        "XML Error loading MetaData config", e);
            }
            catch (IOException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, 
                        "IO Error loading MetaData config", e);
            }
            catch (CoreException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, 
                        "Core Error loading MetaData config", e);
            }
            
        }
        try
        {
            InputStream is = LanguageTestPlugin.getDefault().getBundle()
                .getResource(DEFAULT_LANG_CONFIG).openStream();
            XmlOptions options = getXmlOptions();
            LanguageModuleDocument langDoc = 
                LanguageModuleDocument.Factory.parse(is, options);
            if (langDoc != null && langDoc.getLanguageModule() != null && 
                langDoc.getLanguageModule().isSetConfig())
            {
                ConfigType c = langDoc.getLanguageModule().getConfig();
                ConfigDetails cd = new MetaDataManager().new 
                    ConfigDetails(null, langDoc);
                config.put(c, cd);
            }
        }
        catch (IOException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, 
                    "IO Error loading MetaData config", e);
        }
        catch (XmlException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, 
                    "XML Error loading MetaData config", e);
        }
        return config;
    }
    /**
     * Try to save the node to the most appropriate Project.
     * If the top level node already exists in one of the projects save it
     * there. Otherwise, try the Project of the current selection, then the user 
     * project finally the first available open project.
     * @param node
     */
    public static boolean saveNode(MetaNode node)
    {
        Map <ConfigType, ConfigDetails> map = loadConfigFromProjects();
        IPath nodePath = node.toPath();
        if (nodePath.segmentCount() == 0)
        {
            LanguageTestPlugin.log(IStatus.INFO, 
                    "MetaNode has no segments to save.");
            return false;
        }
        String seg = node.toPath().segment(0);
        for (ConfigType c : map.keySet())
        {
            for (MetaDataType md : c.getMetaDataArray())
            {
                if (md.getMetaId().equals(seg))
                {
                    ConfigDetails cd = map.get(c);
                    if (cd.file != null)
                    {
                        IProject project = cd.file.getProject();
                        if (project.isAccessible())
                        {
                            
                            return saveNodeToProject(node, project);
                        }
                    }
                }
            }
        }
        
        // try to find the active project and save it to that
        IProject p = getActiveProject();
        if (p == null)
        {
            p = WorkspaceLanguageManager.getUserProject();
        }
        if (p == null)
        {
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IProject [] projects = workspace.getRoot().getProjects();
            // finally try to find any open projects
            for (IProject pTest : projects)
            {
                if (pTest.isOpen()) 
                {
                    p = pTest;
                    continue;
                }
            }
        }
        if (p == null)
        {
            LanguageTestPlugin.log(IStatus.WARNING, 
                    "Unable to save MetaNode because no project was found");
            return false;
        }
        return saveNodeToProject(node, p);
    }
    private static boolean saveNodeToProject(MetaNode node, IProject project)
    {
        boolean success = false;
        try
        {
            final IFile configResource = 
                project.getFile(WorkspaceLanguageManager.LANG_FILE);
            InputStream is = configResource.getContents();
            final XmlOptions options = getXmlOptions();
            final LanguageModuleDocument langDoc = 
                LanguageModuleDocument.Factory.parse(is);
            if (langDoc != null && langDoc.getLanguageModule() != null)
            {
                ConfigType c = null;
                if (langDoc.getLanguageModule().isSetConfig())
                    c = langDoc.getLanguageModule().getConfig();
                else
                    c = langDoc.getLanguageModule().addNewConfig();
                IPath nodePath = node.toPath();
                MetaDataType levelData = null;
                for (MetaDataType md : c.getMetaDataArray())
                {
                    if (md.getMetaId().equals(nodePath.segment(0)))
                    {
                        levelData = md;
                        break;
                    }
                }
                if (levelData == null)
                {
                    levelData = c.addNewMetaData();
                    levelData.setMetaId(nodePath.segment(0));
                }
                if (nodePath.segmentCount() == 1)
                {
                    levelData.set(node.getData());
                }
                MetaDataType prevData = levelData;
                levelData = null;
                for (int i = 1; i < nodePath.segmentCount(); i++)
                {
                    for (MetaDataType md : prevData.getMetaDataArray())
                    {
                        if (md.getMetaId().equals(nodePath.segment(i)))
                        {
                            levelData = md;
                            break;
                        }
                    }
                    if (levelData == null)
                    {
                        levelData = prevData.addNewMetaData();
                        levelData.setMetaId(nodePath.segment(i));
                    }
                    if (nodePath.segmentCount() == i + 1)
                    {
                        levelData.set(node.getData());
                    }
                    prevData = levelData;
                    levelData = null;
                }
                PlatformUI.getWorkbench().getProgressService().run(false, false, 
                        new IRunnableWithProgress()
                {
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                    {
                        try
                        {
                            InputStream is = langDoc.newInputStream(options);
                            if (configResource.exists())
                                configResource.setContents(is, IFile.NONE, monitor);
                            else
                                configResource.create(is, IFile.NONE, monitor);
                            configResource.refreshLocal(1, monitor);
                        }
                        catch (CoreException e)
                        {
                            LanguageTestPlugin.log(IStatus.ERROR, 
                                    "Core Error saving MetaNode", e);
                        }
                    }
                });
                success = true;
            }
        }
        catch (XmlException e)
        {
            LanguageTestPlugin.log(IStatus.ERROR, 
                    "XML Error before saving MetaNode", e);
        }
        catch (IOException e)
        {
            LanguageTestPlugin.log(IStatus.ERROR, 
                    "IO Error saving MetaNode", e);
        }
        catch (CoreException e)
        {
            LanguageTestPlugin.log(IStatus.ERROR, 
                    "Core Error before saving MetaNode", e);
        }
        catch (InvocationTargetException e)
        {
            LanguageTestPlugin.log(IStatus.ERROR, 
                    "Invocation Error saving MetaNode", e);
        }
        catch (InterruptedException e)
        {
            LanguageTestPlugin.log(IStatus.ERROR, 
                    "Interrupted while saving MetaNode", e);
        }
        return success;
    }
    private static IProject getActiveProject()
    {
        IProject p = null;
        ISelection s = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
            .getActivePage().getSelection();
        if (s instanceof IStructuredSelection)
        {
            IStructuredSelection ss = (IStructuredSelection)s;
            if (ss.getFirstElement() instanceof IResource)
            {
                p = ((IResource)ss.getFirstElement()).getProject();
            }
        }
        return p;
    }
    /**
     * Examine the different Config elements to try to find the given metanode
     * @param mn
     * @return
     */
    private static MetaDetails findExistingMetaData(MetaNode mn)
    {
        MetaDetails details = null;
        IPath path = mn.toPath();
        if (path.segmentCount() == 0) 
            return null;
        Map <ConfigType, ConfigDetails> map = loadConfigFromProjects();
        int leafIndex = 0;
        int depth = -1;
        for (ConfigType c : map.keySet())
        {
            depth = -1;            
            leafIndex = 0;
            for (MetaDataType mdTop : c.getMetaDataArray())
            {
                if (mdTop.getMetaId().equals(path.segment(0)))
                {
                    depth = 0;
                    MetaDataType prev = mdTop;
                    boolean found  = true;
                    for (int i = 1; found && i < path.segmentCount(); i++)
                    {
                        found = false;
                        leafIndex = 0;
                        for (MetaDataType md : prev.getMetaDataArray())
                        {
                            if (md.getMetaId().equals(path.segment(i)))
                            {
                                if (i + 1 != path.segmentCount()) 
                                    prev = md;
                                found = true;
                                depth = i;
                                break;
                            }
                            leafIndex++;
                        }
                    }
                    ConfigDetails cd = map.get(c);
                    if (found)
                    {
                        // TODO consider how to handle duplicates better
                        // we can't modify it if it is hard coded 
                        if (cd.file != null)
                        {
                            if (details != null)
                            {
                                LanguageTestPlugin.log(IStatus.INFO, 
                                    "Duplicate Tag " + 
                                    path.toPortableString() + " "
                                    + details.file.getProject().getName() 
                                    + " & " + cd.file.getProject().getName());
                            }
                        }
                        details = new MetaDataManager().new 
                            MetaDetails(cd.file, cd.doc, depth, prev, leafIndex);
                    }
                    else if (depth > -1 && 
                             (details == null || details.depth < depth))
                    {
                        details = new MetaDataManager().new 
                            MetaDetails(cd.file, cd.doc, depth, prev, leafIndex);
                    }
                    break;
                }
                leafIndex++;
            }
        }
        return details;
    }
    private static XmlOptions getXmlOptions()
    {
        final XmlOptions options = new XmlOptions();
        options.setCharacterEncoding("UTF-8");
        options.setLoadUseDefaultResolver();
        options.setSavePrettyPrint();
        options.setDocumentType(LanguageModuleDocument.type);
        return options;
    }
    /**
     * @param mn
     */
    public static boolean deleteNode(MetaNode mn)
    {
        final MetaDetails details = findExistingMetaData(mn);
        boolean success = false;
        if (details != null)
        {
            // check that the MetaNode was found down to the correct depth
            if (details.depth + 1 < mn.toPath().segmentCount())
            {
                LanguageTestPlugin.log(IStatus.INFO, "Could not find " + 
                        mn.getPath() + " to delete or it may be read only.");
                return false;
            }
            if (details.parentData == null)
            {
                details.doc.getLanguageModule().getConfig().removeMetaData(details.leafIndex);
            }
            else
            {
                details.parentData.removeMetaData(details.leafIndex);
            }
            final InputStream is = details.doc.newInputStream(getXmlOptions());
            try
            {
                PlatformUI.getWorkbench().getProgressService().run(false, false, 
                        new IRunnableWithProgress()
                {
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                    {
                        try
                        {
                            details.file.setContents(is, IFile.NONE, monitor);
                            details.file.refreshLocal(1, monitor);
                        }
                        catch (CoreException e)
                        {
                            LanguageTestPlugin.log(IStatus.ERROR, 
                                    "Error deleting MetaNode", e);
                        }
                    }
                });
                success = true;
            }
            catch (InvocationTargetException e)
            {
                LanguageTestPlugin.log(IStatus.ERROR, 
                        "Invocation Error deleting MetaNode", e);
            }
            catch (InterruptedException e)
            {
                LanguageTestPlugin.log(IStatus.ERROR, 
                        "Interrupted deleting MetaNode", e);
            }
        }
        return success;
    }
    /**
     * Structure to hold the results from findExistingMetaData()
     * @author keith
     *
     */
    private class MetaDetails
    {
        final IFile file;
        final LanguageModuleDocument doc;
        final int depth;
        final MetaDataType parentData;
        final int leafIndex;
        public MetaDetails(IFile file, LanguageModuleDocument doc, int depth,
                MetaDataType parentData, int leafIndex)
        {
            this.file = file;
            this.doc = doc;
            this.depth = depth;
            this.parentData = parentData;
            this.leafIndex = leafIndex;
        }
    }
    /**
     * Structure to hold the results from loadConfigFromProject()
     * @author keith
     *
     */
    protected class ConfigDetails
    {
        final IFile file;
        final LanguageModuleDocument doc;
        public ConfigDetails(IFile file, LanguageModuleDocument doc)
        {
            this.file = file;
            this.doc = doc;
        }
    }
}
