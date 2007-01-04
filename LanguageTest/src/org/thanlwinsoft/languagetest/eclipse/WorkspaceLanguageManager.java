/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.natures.LanguageUserNature;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.LangType;
import org.thanlwinsoft.schemas.languagetest.LangTypeType;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleType;

/** The WorkspaceLanguageManager stores the languages associated with a project.
 * Usually one project is used for one Foreign language being learnt. However,
 * there may be several different scripts used for the same language. The 
 * languages for a project are stored in a hidden file in the root of the 
 * project.
 * @author keith
 *
 */
public class WorkspaceLanguageManager 
{
	private IProject userProject = null;
	private LangType [] userLanguages = null;
	public final static String LANG_FILE = ".languages.xml";
    public final static String WORKSPACE_LANG_ID = "WSLANGID";
    private static WorkspaceLanguageManager instance = null;
	protected WorkspaceLanguageManager()
	{
		
	}
    protected static WorkspaceLanguageManager get()
    {
        if (instance == null)
            instance = new WorkspaceLanguageManager();
        return instance;
    }
    public static void addLanguage(IProject project, LangTypeType.Enum type, 
            UniversalLanguage ul, FontData fontData, IProgressMonitor monitor)
    {
        LangType lang = LangType.Factory.newInstance();
        lang.setLang(ul.getCode());
        lang.setFont(fontData.getName());
        BigDecimal fontSize = BigDecimal.valueOf(fontData.getHeight());
        lang.setFontSize(fontSize);
        lang.setType(type);
        lang.setStringValue(ul.getDescription());
        get().setLanguage(project, lang, monitor);
    }
	public static void addLanguage(IProject project, LangTypeType.Enum type, 
                    UniversalLanguage ul, Font font, IProgressMonitor monitor)
	{
        FontData fontData = font.getFontData()[0];
        addLanguage(project, type, ul, fontData, monitor);
	}
	
	public static void addLanguage(IProject project, LangType lang, IProgressMonitor monitor)
	{
        get().setLanguage(project, lang, monitor);
    }
    protected synchronized void setLanguage(IProject project, LangType lang, IProgressMonitor monitor)
    {
		LanguageModuleDocument doc = getProjectLang(project);
		LanguageModuleType module = doc.getLanguageModule();
        for (int j = 0; j < module.sizeOfLangArray(); j++)
        {
            LangType trialLang = module.getLangArray(j);
            if (trialLang.getLang().equals(lang.getLang()))
            {
                trialLang.setFont(lang.getFont());
                trialLang.setFontSize(lang.getFontSize());
                // is it wise to allow type changes?
                trialLang.setType(lang.getType());
                if (lang.getStringValue().length() > 0)
                    trialLang.setStringValue(lang.getStringValue());
                saveLang(project, doc, monitor);
                return;
            }
        }
        // there is no entry for this language, so we need t create one
		int i = module.sizeOfLangArray();
		if (lang.getType().equals(LangTypeType.NATIVE))
		{
			for (i = 0; i < module.sizeOfLangArray() && 
				 module.getLangArray(i).getType().equals(LangTypeType.NATIVE); 
				 i++) {}
		}
        module.insertNewLang(i);
		module.setLangArray(i, lang);
		saveLang(project, doc, monitor);
	}
    
    protected synchronized void saveLang(IProject project, LanguageModuleDocument doc,
                    IProgressMonitor monitor)
    {
        try
        {
            IFile langFile = project.getFile(LANG_FILE);
            XmlOptions options = new XmlOptions();
            options.setCharacterEncoding("UTF-8");
            options.setSavePrettyPrint();
            if (langFile.exists() == false)
                langFile.create(doc.newInputStream(options), 0, monitor);
            else
                langFile.setContents(doc.newInputStream(options),
                                 0, monitor);
        }
        catch (CoreException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, 
                            MessageUtil.getString("FileNotFound", LANG_FILE),e);
        }
    }
	
	public IProject getProject(UniversalLanguage ul)
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		UniversalLanguage ulNoScript = 
			new UniversalLanguage(ul.getLanguageCode(), ul.getCountryCode(), 
						          ul.getVariant(), "", "");
		IProject project = workspace.getRoot().getProject(ulNoScript.getCode());
		return project;
	}
	
    public static IProject getUserProject()
    {
        if (get().userProject == null)
        {
            findUserProjects();
        }
        return get().userProject;
    }
    
    public static LangType [] findUserLanguages()
    {
        synchronized (get())
        {
            if (getUserProject() != null)
                get().userLanguages = findLanguages(getUserProject());
        }
        return get().userLanguages;
    }
    /**
     * Find the active languages of a given type for a project.
     * User langauges are appended to the project list.
     * @param project
     * @param langType
     * @return mapping of language codes to UniversalLanguage objects
     */
    public static HashMap findActiveLanguages(IProject project, LangTypeType.Enum langType)
    {
        HashMap langs = new HashMap();
        LangType [] userLangs = findUserLanguages();
        if (userLangs != null)
        {
            for (int i = 0; i < userLangs.length; i++)
            {
                if (langType == null || langType.equals(userLangs[i].getType()))
                {
                    langs.put(userLangs[i].getLang(), userLangs[i]);
                }
            }
        }
        LangType [] projectLangs = findLanguages(project);
        for (int j = 0; j < projectLangs.length; j++)
        {
            if (langType == null || langType.equals(projectLangs[j].getType()))
            {
                if (langs.containsKey(projectLangs[j].getLang()) == false)
                    langs.put(projectLangs[j].getLang(), projectLangs[j]);
            }
        }
        return langs;
    }
    
	public static IProject [] findUserProjects()
	{
		int openCount = 0;
		HashSet userProjects = new HashSet();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject [] projects = workspace.getRoot().getProjects();
		for (int i = 0; i < projects.length; i++)
		{
			try
			{
				if (projects[i].isOpen() && projects[i].hasNature(LanguageUserNature.ID))
				{
					userProjects.add(projects[i]);
					if (projects[i].isOpen()) 
					{
						openCount++;
                        synchronized (get())
                        {
                            get().userProject = projects[i];
                        }
					}
				}
			}
			catch (CoreException e)
			{
				e.printStackTrace();
			}
		}
		return (IProject[])(userProjects.toArray(new IProject[userProjects.size()]));
	}
	
	protected LanguageModuleDocument getProjectLang(IProject project)
	{
        if (project == null) return null;
		IFile langFile = project.getFile(LANG_FILE);
        synchronized(get())
        {
		try
		{
            
			InputStream is = langFile.getContents();
	        XmlOptions options = new XmlOptions();
	        options.setCharacterEncoding("UTF-8");
	        options.setLoadUseDefaultResolver();
	        options.setDocumentType(LanguageModuleDocument.type);
	        LanguageModuleDocument langDoc = 
	        	LanguageModuleDocument.Factory.parse(is);
	        if (langDoc.getLanguageModule() == null)
	        	langDoc.addNewLanguageModule();
	        return langDoc;
		}
		catch (CoreException e)
		{
			LanguageTestPlugin.log(IStatus.WARNING, 
							MessageUtil.getString("FileNotFound", LANG_FILE),e);
		}
		catch (XmlException e)
		{
			LanguageTestPlugin.log(IStatus.WARNING, 
							MessageUtil.getString("TestModuleReadErrorMsg", 
											      LANG_FILE), e);
		}
		catch (IOException e)
		{
			LanguageTestPlugin.log(IStatus.WARNING, 
							MessageUtil.getString("TestModuleReadErrorMsg", 
											      LANG_FILE), e);
		}
        }
        LanguageModuleDocument doc = 
            LanguageModuleDocument.Factory.newInstance();
        doc.addNewLanguageModule();
        doc.getLanguageModule().setCreationTime(new Date().getTime());
        doc.getLanguageModule().setId("WORKSPACE_LANG_ID");
		return doc;
	}
	
	public static LangType [] findLanguages(IProject project)
	{
		//IWorkspace workspace = ResourcesPlugin.getWorkspace();
        LanguageModuleDocument doc = get().getProjectLang(project);
        if (doc == null)
        {
            return new LangType[0];
        }
        else
        {
            return get().getProjectLang(project).getLanguageModule().getLangArray();
        }
	}
	
}
