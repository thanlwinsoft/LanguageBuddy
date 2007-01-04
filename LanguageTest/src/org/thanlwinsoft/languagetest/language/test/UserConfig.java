/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/test/UserConfig.java,v $
 *  Version:       $Revision: 1.6 $
 *  Last Modified: $Date: 2005/03/25 04:50:35 $
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

package org.thanlwinsoft.languagetest.language.test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.swt.graphics.FontData;


/**
 *
 * @author  keith
 */
public class UserConfig
{
    public final static String PREF_NODE = "org.thanlwinsoft.languagetest";
    private final String BROWSE_MODULE_PREF = "BrowseModulePath";
    public static final String PROFILES = "Profiles";  
    public static final String LAST_USED = "lastUsed";
    private final static String DEFAULT_SOUND_FORMAT = "SoundExtension";
    public static final String LANGUAGE_CONFIG_FILE = "LangConfig.xml";
    public static final String LANG_CONFIG_ELEMENT = "LangConfig";
    public static final String MAX_FLIP_REPEATS = "MaxFlipRepeats";
    public static final String FLIP_PERIOD = "FlipPeriod";
    public static final String LANG_LAST_USED_ATTRIB = "lastUsed";
    public static final String LANG_XML_COMMENT = 
        "Language Configuration for LanguageTest Program\nCreated on";
    private static final int DEFAULT_FONT_SIZE = 12;
    private static String DEFAULT_SOUND_EXT = ".wav";
    private String userName = "Unknown";
    private TestHistory testHistory = null;
    private final static long MS_IN_HOUR = 3600000;
    private final static long MS_IN_DAY = MS_IN_HOUR * 24;
    private File modulePath = null;
    private File configPath = null;
    private int[] learntPassCount = null;
    private long [] minRetestPeriod = null;
    private long [] initialRevisionPeriod = null;
    private long [] shortTermPeriod = null;
    private long [] shortTermRevisionPeriod = null;
    private long [] longTermPeriod = null;
    private long [] longTermRevisionPeriod = null;
    private int maxFlipRepeats = 10;
    private int flipPeriodSec = 5;
    private String defaultSoundExtension = DEFAULT_SOUND_EXT;
    private static Vector listeners = null;
    private final static String LEARNT_PASS_COUNT = "learntPassCount";
    private final static String MIN_RETEST_PERIOD = "minRetestPeriod";
    private final static String INITIAL_REV_PERIOD = "initialRevPeriod";
    private final static String SHORT_TERM_PERIOD = "shortTermPeriod";
    private final static String SHORT_TERM_REV_PERIOD = "shortTermRevPeriod";
    private final static String LONG_TERM_PERIOD = "longTermPeriod";
    private final static String LONG_TERM_REV_PERIOD = "longTermRevPeriod";
    private final static String PREF_DIRECTORY = "LanguageTest"; 
    private final static String CONFIG_PATH = "ConfigPath";
    private final static String MAX_NUM_TEST_ITEMS = "maxNumTestItems";
    private Preferences userNode = null;
    private HashMap [] languages = null;
    private HashMap nativeLanguages = null;
    private HashMap foreignLanguages = null;
    private IProject userProject = null;
//    private RecentFilesList recentFilesList = null;
    private int maxNumTestItems = -1;
    /** Creates a new instance of UserConfig  - this is only used to
     *  get the package name internally 
     */
    
    public UserConfig(IProject userProject) 
        throws IllegalArgumentException
        //, IOException, 
        //TestHistoryStorageException
    {
        this.userProject = userProject;
        this.userName = userProject.getName();
        //this.recentFilesList = rfList;
        IScopeContext projectScope = new ProjectScope(userProject);
        IEclipsePreferences projectNode = projectScope.getNode(PREF_NODE);
        if (projectNode != null && projectNode instanceof Preferences) 
        {
            userNode = (Preferences) projectNode;
        }
        else
        {
            throw new IllegalArgumentException("Failed to get projectNode");
        }
        
            
            // write a key so that it will be detected by userExists
            long lastUsed = userNode.getLong(LAST_USED, new Date().getTime());
            userNode.putLong(LAST_USED,lastUsed);
            // default test types
            learntPassCount = new int[TestType.NUM_TEST_TYPES];
            minRetestPeriod = new long[TestType.NUM_TEST_TYPES];
            initialRevisionPeriod = new long[TestType.NUM_TEST_TYPES];
            shortTermPeriod = new long[TestType.NUM_TEST_TYPES];
            shortTermRevisionPeriod = new long[TestType.NUM_TEST_TYPES];
            longTermPeriod = new long[TestType.NUM_TEST_TYPES];
            longTermRevisionPeriod = new long[TestType.NUM_TEST_TYPES];
            readTestParameters();
            
//            modulePath = new File(userNode.get(BROWSE_MODULE_PREF,""));
//            defaultSoundExtension = 
//                userNode.get(DEFAULT_SOUND_FORMAT,DEFAULT_SOUND_EXT);
//            if (!modulePath.exists()) modulePath = null;
//            try
//            {
//                if (userNode.nodeExists(CONFIG_PATH))
//                {
//                    configPath = new File(userNode.get(CONFIG_PATH, null));
//                }
//                else
//                {
//                    configPath = createConfigPath(userName);
//                }
//            }
            //catch (BackingStoreException e)
            //{
            //    throw new IOException("Error saving config date:\n"
            //                          + e.getMessage());
            //}
            // create listeners object before creating test history
            if (listeners == null)
            {
                listeners = new Vector();
            }
//            if (testHistory.getModuleCount() == 0)
//            {
//                cloneHistory();
//            }
            languages = new HashMap[2];
            nativeLanguages = new HashMap();
            foreignLanguages = new HashMap();
            languages[UniversalLanguage.NATIVE_LANG] = nativeLanguages;
            languages[UniversalLanguage.FOREIGN_LANG] = foreignLanguages;
            // parse site config first, then user config
            // any duplicates will be overwritten by the user config settings
//            URL siteConfig = this.getClass().getResource(
//                "/" + LANGUAGE_CONFIG_FILE);
//            if (siteConfig != null)
//            {
//                parseLanguageConfig(siteConfig.openStream());
//            }
//            File userLangFile = new File(configPath, LANGUAGE_CONFIG_FILE);
//            if (userLangFile.exists() && userLangFile.canRead())
//            {
//                InputStream uLIS = 
//                    new BufferedInputStream(new FileInputStream(userLangFile));
//                parseLanguageConfig(uLIS);
//                uLIS.close();
//            }
            flipPeriodSec = userNode.getInt(FLIP_PERIOD, 5);
            maxFlipRepeats = userNode.getInt(MAX_FLIP_REPEATS, 10);
            maxNumTestItems = userNode.getInt(MAX_NUM_TEST_ITEMS, -1);
            // lang config may now be known, so set history if possible
            //reloadTestHistory();
        
    }
    
    public void reload()
    {
        
    }
    /*
    public RecentFilesList getRecentFilesList()
    {
        return recentFilesList;
    }
    */
//    public void reloadTestHistory() throws TestHistoryStorageException
//    {
//        
//        if (LanguageConfig.getCurrent() != null)
//        {
//            if (testHistory != null) 
//            {   
//                testHistory.savePermanent();                
//            }
//            testHistory = new XmlTestHistory(this, LanguageConfig.getCurrent());
//        }
//    }
    
    public Set getLanguages(int langType)
    {
        return languages[langType].keySet();
    }
    
    public Set getNativeLanguages()
    {
        return nativeLanguages.keySet();
    }
    
    public Set getForeignLanguages()
    {
        return foreignLanguages.keySet();
    }
    
    public void removeNativeLanguage(UniversalLanguage ul)
    {
        if (nativeLanguages.containsKey(ul))
        {
            nativeLanguages.remove(ul);
        }
    }
    
    public void removeForeignLanguage(UniversalLanguage ul)
    {
        if (foreignLanguages.containsKey(ul))
        {
            foreignLanguages.remove(ul);
        }
    }
    
    public void addLanguage(UniversalLanguage ul, int langType, FontData font)
    {
        if (langType == UniversalLanguage.FOREIGN_LANG)
        {
            addForeignLanguage(ul, font);
        }
        else
        {
            addNativeLanguage(ul, font);
        }
    }
    
    public void addForeignLanguage(UniversalLanguage ul, FontData font)
    {
        foreignLanguages.put(ul,font);
    }
    
    public void addNativeLanguage(UniversalLanguage ul, FontData font)
    {
        nativeLanguages.put(ul,font);
    }
    
    public FontData getNativeDefaultFont(UniversalLanguage ul)
    {
        if (nativeLanguages.containsKey(ul))
        {
            return (FontData)nativeLanguages.get(ul);
        }
        return null;
    }
    
    public FontData getForeignDefaultFont(UniversalLanguage ul)
    {
        if (foreignLanguages.containsKey(ul))
        {
            return (FontData)foreignLanguages.get(ul);
        }
        return null;
    }
    
    public boolean containsNativeLanguage(UniversalLanguage ul)
    {
        return nativeLanguages.containsKey(ul);
    }
    
    public boolean containsForeignLanguage(UniversalLanguage ul)
    {
        return foreignLanguages.containsKey(ul);
    }
    
    public void setDefaultSoundExtension(String format)
    {
        defaultSoundExtension = format;
    }
    
//    public javax.sound.sampled.Mixer.Info getRecordingMixer(LineController lc)
//    {
//        return null;        
//    }
//    
//    public javax.sound.sampled.Mixer.Info getPlayingMixer(LineController lc)
//    {
//        return null;
//    }
    
//    protected void parseLanguageConfig(InputStream xmlIs) 
//        throws TestHistoryStorageException
//    {
//        if (xmlIs != null)
//        {
//            org.w3c.dom.Document doc = null;
//            try 
//            {
//                DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
//                doc = docBuilder.parse(new InputSource(xmlIs));
//                
//            }
//            catch (ParserConfigurationException pce)
//            {
//                System.out.println(pce.getMessage());
//            }
//            catch (SAXException se)
//            {
//                System.out.println(se.getMessage());
//            }
//            catch (IOException ioe)
//            {
//                System.out.println(ioe.getMessage());
//            }
//            if (doc != null)
//            {
//                doc.normalize(); // join adjacent text nodes
//                NodeList top = doc.getElementsByTagName(LANG_CONFIG_ELEMENT);
//                
//                if (top.getLength()>0)
//                {
//                    Element configElement = (Element)top.item(0);
//                    NodeList langs = configElement.getChildNodes();
//                    UniversalLanguage nul = null;
//                    UniversalLanguage ful = null;
//                    for (int i = 0; i<langs.getLength(); i++)
//                    {
//                        if (langs.item(i).getNodeType() != Node.ELEMENT_NODE)
//                            
//                        {
//                            continue;                        
//                        }
//                        if (langs.item(i).getNodeName()
//                            .equals(TestModule.LANG_TAG))
//                        {                        
//                            Element lang = (Element)langs.item(i);
//                            UniversalLanguage ul = 
//                                new UniversalLanguage(lang.getAttribute
//                                    (TestModule.LANG_CODE_ATTRIB));
//                            if (lang.hasChildNodes() && 
//                                lang.getFirstChild().getNodeType() == Node.TEXT_NODE &&
//                                lang.getFirstChild().getNodeValue().length() > 0)
//                            {
//                                ul.setDescription(lang.getFirstChild().getNodeValue());
//                            }
//                            Font defaultFont = TestModule.readFont(lang, 
//                                TestModule.FONT_NAME_ATTRIB, 
//                                TestModule.FONT_STYLE_ATTRIB, 
//                                TestModule.FONT_SIZE_ATTRIB, DEFAULT_FONT_SIZE);
//                            String langType = 
//                                lang.getAttribute(TestModule.LANG_TYPE_ATTRIB);
//                            if (langType.equals(TestModule.LANG_NATIVE))
//                            {
//                                nativeLanguages.put(ul,defaultFont);
//                                if (lang.hasAttribute(LANG_LAST_USED_ATTRIB))
//                                {
//                                    nul = ul;
//                                }
//                            }
//                            else if (langType.equals(TestModule.LANG_FOREIGN))
//                            {
//                                foreignLanguages.put(ul,defaultFont);
//                                if (lang.hasAttribute(LANG_LAST_USED_ATTRIB))
//                                {
//                                    ful = ul;
//                                }
//                            }
//                        }/*
//                        else if (langs.item(i).getNodeName()
//                            .equals(RecentFilesList.RECENT_FILES) && 
//                            recentFilesList != null)
//                        {
//                            recentFilesList.parseXml((Element)langs.item(i));
//                        }
//                        */
//                    }
//                    if (ful != null && nul != null)
//                    {
//                        // replace language config
//                        new LanguageConfig(nul,ful).load();
//                        
//                    }
//                }                
//            }
//        }
//    }
//        
//    protected boolean saveLanguageConfig(File file)
//    {
//        org.w3c.dom.Document doc = null;
//        try
//        {
//            // reuse factory objects between module instances
//            DocumentBuilderFactory dfactory = 
//                DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
//            
//            doc = docBuilder.newDocument();
//            // add a comment to explain file
//            org.w3c.dom.Comment comment = 
//                doc.createComment(LANG_XML_COMMENT + new Date().toString());
//            doc.insertBefore(comment,null);
//            // add a style sheet link so can be viewed in web browser
//            //org.w3c.dom.ProcessingInstruction style = 
//            //    doc.createProcessingInstruction(STYLE_SHEET, XSL_LINK);
//            //doc.insertBefore(style,null);   
//            // create top level element
//            org.w3c.dom.Element top = doc.createElement(LANG_CONFIG_ELEMENT);
//            createLangTags(doc, top, nativeLanguages, TestModule.LANG_NATIVE);
//            createLangTags(doc, top, foreignLanguages, TestModule.LANG_FOREIGN);
//            
//            //if (recentFilesList != null)
//            //{
//            //    top.insertBefore(recentFilesList.saveToXml(doc), null);
//            //}
//            doc.insertBefore(top, null);
//            
//        }
//        catch (ParserConfigurationException e)
//        {
//            System.out.println(e.getMessage());
//            // from copy of xsl file so not worth giving false for this
//            return false;
//        }
//        try
//        {
//            TransformerFactory tfactory = TransformerFactory.newInstance(); 
//
//            // This creates a transformer that does a simple identity transform, 
//            // and thus can be used for all intents and purposes as a serializer.
//            Transformer serializer = tfactory.newTransformer();
//
//            Properties oprops = new Properties();
//            oprops.put(OutputKeys.METHOD, "xml");
//            oprops.put(OutputKeys.INDENT, "yes");
//            serializer.setOutputProperties(oprops);
//            
//            DOMSource domSource = new DOMSource(doc);
//            // now output the result
//            serializer.transform(domSource, 
//                                 new StreamResult(file));
//        }
//        catch (IllegalArgumentException iae)
//        {
//            System.out.println(iae.toString());
//            return false;
//        }
//        catch (TransformerConfigurationException tce)
//        {
//            System.out.println(tce.toString());
//            return false;
//        }
//        catch (TransformerException te)
//        {
//            System.out.println(te.toString());
//            return false;
//        }
//        return true;
//    }
//    
//    protected void createLangTags(org.w3c.dom.Document doc, 
//                                  org.w3c.dom.Element top, 
//                                  HashMap fonts, 
//                                  String langType)
//    {
//        // iterate over languages
//        Iterator f = fonts.keySet().iterator();
//        while (f.hasNext())
//        {
//            UniversalLanguage ul = (UniversalLanguage)f.next();
//            org.w3c.dom.Element lang = doc.createElement(TestModule.LANG_TAG);
//            lang.setAttribute(TestModule.LANG_TYPE_ATTRIB, langType);
//            lang.setAttribute(TestModule.LANG_CODE_ATTRIB, ul.getCode());
//            Font font = (Font)fonts.get(ul);
//            if (font != null)
//            {
//                lang.setAttribute(TestModule.FONT_NAME_ATTRIB, font.getName());
//                lang.setAttribute(TestModule.FONT_STYLE_ATTRIB, 
//                    Integer.toString(font.getStyle()));
//                lang.setAttribute(TestModule.FONT_SIZE_ATTRIB, 
//                    Integer.toString(font.getSize()));                
//            }
//            if (langType.equals(TestModule.LANG_NATIVE) &&
//                ul.equals(LanguageConfig.getCurrent().getNativeLanguage()))
//            {
//                lang.setAttribute(LANG_LAST_USED_ATTRIB, "true");
//            }
//            else if (langType.equals(TestModule.LANG_FOREIGN) &&
//                ul.equals(LanguageConfig.getCurrent().getForeignLanguage()))
//            {
//                lang.setAttribute(LANG_LAST_USED_ATTRIB, "true");
//            }
//            if (ul.getDescription() != null && ul.getDescription().length()>0)
//            {
//                lang.insertBefore(doc.createTextNode(ul.getDescription()),null);
//            }
//            top.insertBefore(lang, null);
//        }
//    }
    
    public static String generateConfigPath(String newUserName)
    {
        File userDir = new File(System.getProperty("user.home"));
        // if Application Data directory exists (as it does in XP) then use
        // that directory. Otherwise default to main directory
        File settingsDir = new File(userDir,"Application Data");
        if (!settingsDir.exists()) settingsDir = userDir;
        String path = settingsDir.getAbsolutePath() +
            File.separator + PREF_DIRECTORY + 
            File.separator + newUserName;
        return path;
    }
    
    protected File createConfigPath(String newUserName) throws IOException
    {
        File newConfigPath = new File(generateConfigPath(newUserName));
        if (!newConfigPath.exists())
        {
            if (!newConfigPath.mkdirs())
            {
                throw new IOException("Cannot create " + 
                    newConfigPath.getAbsolutePath());
            }
        }
        // check that the path is writable - otherwise its fairly useless
        if (!newConfigPath.canWrite())
        {
            throw new IOException("Cannot write to " +
                newConfigPath.getAbsolutePath());
        }
        return newConfigPath;
    }
    /*
    protected void cloneHistory() throws IOException
    {
        // perhaps an old history exists in Prefs format
        TestHistory oldHistory = new PrefsTestHistory(userName);
        this.load(); // currentConfig needs to be set before loading modules
        if (oldHistory.getModuleCount() > 0)
        {
            TestType type = null;
            try
            {
            for (int tt = 0; tt<TestType.NUM_TEST_TYPES; tt++)
            {
                TestModule module = null;
                type = TestType.getById(tt);
                Iterator i = oldHistory.iterator(type);
                while (i.hasNext())
                {
                    try
                    {
                        ItemHistory ih = (ItemHistory)i.next();
                        if (module == null || 
                            !module.getFile().equals(ih.getModuleFile()))
                        {
                            module = new TestModule(ih.getModuleFile());
                        }
                        TestItem testItem = 
                            module.getItemByCreationTime(ih.getCreationTime(), 
                                                         ih.getCreator());

                        if (testItem != null)
                        {
                            for (int t = 0; t<ih.getTestCount(); t++)
                            {
                                testHistory.saveResult(testItem, type, 
                                    ((Date)(ih.getResultTable()[t][0]))
                                    .getTime(),
                                    ((Boolean)(ih.getResultTable()[t][1]))
                                    .booleanValue());
                            }
                        }
                    }
                    catch (FileNotFoundException fnfe)
                    {
                        System.out.println(fnfe);                        
                    }
                    catch (TestModule.ParseException pe)
                    {
                        System.out.println(pe);
                    }
                }
            }
            testHistory.savePermanent();
            }
            catch (TestHistoryStorageException thse)
            {
                throw new IOException(thse.getMessage());
            }
        }
    }
    */
    
//    public boolean rename(String newName) 
//    {
//        String oldName = userName;
//        try
//        {
//            // check whether new name already exists
//            if (userExists(newName)) return false;
//            File newPath = createConfigPath(newName);
//            // before we do anything make sure test history is saved
//            testHistory.savePermanent();
//            // try to copy files
//            File [] files = configPath.listFiles();
//            boolean renameOk = true;
//            for (int f = 0; f < files.length; f++)
//            {
//                renameOk = files[f].renameTo(new File(newPath, 
//                    files[f].getName()));
//                if (renameOk == false) 
//                {
//                    System.out.println("Rename failed " + 
//                        files[f].getAbsolutePath() + "\n" + 
//                        newPath.getAbsolutePath());
//                    break;
//                }
//            }
//            // set new config path if copy successful
//            if (renameOk)
//            {
//                // there is a small chance of losing date here if an error
//                // occurred half way through the copy
//                
//                // delete old path
//                configPath.delete();
//                configPath = newPath;                
//            }
//            // set new userNode
//            Preferences oldUserNode = userNode;
//            userNode = Preferences.userNodeForPackage(this.getClass())
//                .node(PROFILES + "/" + newName); 
//            // write a key so that it will be detected by userExists
//            userNode.putLong(LAST_USED, new Date().getTime());
//            // save parameters
//            
//            try
//            {
//                save();
//            }
//            catch (TestHistoryStorageException e)
//            {
//                // at this stage ignore TestHistoryStorageException
//                // since we have gone too far to backtrack
//            }
//            userName = newName;
//            // update history path in TestHistory
//            // finally remove the node
//            oldUserNode.removeNode();
//            // notify listeners
//            notifyConfigListeners();
//        }
//        catch (IOException e)
//        {
//            userName = oldName;
//            System.out.println(e);
//            return false;
//        }
//        catch (BackingStoreException e)
//        {
//            userName = oldName;
//            System.out.println(e);
//            return false;
//        }
//        catch (TestHistoryStorageException e)
//        {
//            userName = oldName;
//            System.out.println(e);
//            return false;
//        }
//        return true;
//    }
    public String getDefaultSoundExtension() { return defaultSoundExtension; }
    public File getModulePath() { return modulePath; }
    public void setModulePath(File newPath) { modulePath = newPath; }
    public File getConfigPath() { return configPath; }
    public boolean setConfigPath(File newPath) throws IOException
    {
        // test new path
        if (!newPath.exists()) 
        {   
            if (!newPath.mkdirs()) return false;
        }
        if (!newPath.isDirectory()) return false;
        if (!newPath.canWrite()) return false;
        // try to copy old files to new path
        if (configPath.exists())
        {
            String copyErrors = "";
            File [] fileList = configPath.listFiles();
            for (int i = 0; i<fileList.length; i++)
            {
                if (!fileList[i].renameTo(new File(newPath, fileList[i].getName())))
                {
                    copyErrors += "Failed to copy " 
                        + fileList[i].getAbsolutePath() + "\n";
                }
            }
            if (copyErrors.length() > 0)
            {
                throw new IOException(copyErrors);
            }
        }
        configPath = newPath;
        return true;
    }
    
    public String getUserName()
    {
        return userName;
    }
    public void load()
    {
        notifyConfigListeners();
    }
    
    
    public TestHistory getTestHistory()
    {
        return testHistory;
    }
    
    public int getLearntPassCount(TestType tt)
    {
        return learntPassCount[tt.getId()];
    }
    public long getInitialRevisionPeriod(TestType tt)
    {
        return initialRevisionPeriod[tt.getId()];
    }
    public long getShortTermPeriod(TestType tt)
    {
        return shortTermPeriod[tt.getId()];
    }
    public long getShortTermRevisionPeriod(TestType tt)
    {
        return shortTermRevisionPeriod[tt.getId()];
    }
    public long getLongTermPeriod(TestType tt)
    {
        return longTermPeriod[tt.getId()];
    }
    public long getLongTermRevisionPeriod(TestType tt)
    {
        return longTermRevisionPeriod[tt.getId()];
    }
    public long getMinRetestPeriod(TestType tt)
    {
        return minRetestPeriod[tt.getId()];
    }
    public void setLearntPassCount(TestType tt, int value)
    {
        learntPassCount[tt.getId()] = value;
    }
    public void setInitialRevisionPeriod(TestType tt, long value)
    {
        initialRevisionPeriod[tt.getId()]  = value;
    }
    public void setShortTermPeriod(TestType tt, long value)
    {
        shortTermPeriod[tt.getId()] = value;
    }
    public void setShortTermRevisionPeriod(TestType tt, long value)
    {
        shortTermRevisionPeriod[tt.getId()] = value;
    }
    public void setLongTermPeriod(TestType tt, long value)
    {
        longTermPeriod[tt.getId()] = value;
    }
    public void setLongTermRevisionPeriod(TestType tt, long value)
    {
        longTermRevisionPeriod[tt.getId()] = value;
    }
    public void setMinRetestPeriod(TestType tt, long value)
    {
        minRetestPeriod[tt.getId()] = value;
    }
    public void setMaxFlipRepeats(int max)
    {
        maxFlipRepeats = max;
    }
    public int getMaxFlipRepeats()
    {
        return maxFlipRepeats;
    }
    public void setFlipPeriod(int period)
    {
        flipPeriodSec = period;
    }
    public int getFlipPeriod()
    {
        return flipPeriodSec;
    }
    public int getMaxNumTestItems()
    {
        return maxNumTestItems;
    }
    public void setMaxNumTestItems(int num)
    {
        maxNumTestItems = num;
    }
    protected void readTestParameters()
    {
        for (int tt = 0; tt < TestType.NUM_TEST_TYPES; tt++)
        {
            Preferences ttNode = userNode.node(TestType.getById(tt).getCode());
            learntPassCount[tt] = ttNode.getInt(LEARNT_PASS_COUNT, 2);
            minRetestPeriod[tt] = ttNode.getLong(MIN_RETEST_PERIOD, 
                                                 MS_IN_HOUR * 12);
            initialRevisionPeriod[tt] = ttNode.getLong(INITIAL_REV_PERIOD, 
                                                  MS_IN_DAY *7);
            shortTermPeriod[tt] = ttNode.getLong(SHORT_TERM_PERIOD, 
                                                 MS_IN_DAY * 30);
            shortTermRevisionPeriod[tt] = ttNode.getLong(SHORT_TERM_REV_PERIOD, 
                                                    MS_IN_DAY * 30);
            longTermPeriod[tt] = ttNode.getLong(LONG_TERM_PERIOD, 
                                                 MS_IN_DAY * 90);
            longTermRevisionPeriod[tt] = ttNode.getLong(LONG_TERM_REV_PERIOD, 
                                                   MS_IN_DAY * 180);       
        }
    }
    public void save() throws TestHistoryStorageException
    {
        if (modulePath != null)
        {
            userNode.put(BROWSE_MODULE_PREF, modulePath.getAbsolutePath());
        }
        userNode.put(DEFAULT_SOUND_FORMAT, defaultSoundExtension);
        userNode.putInt(FLIP_PERIOD, flipPeriodSec);
        userNode.putInt(MAX_FLIP_REPEATS, maxFlipRepeats);
        userNode.putInt(MAX_NUM_TEST_ITEMS, maxNumTestItems);
        saveTestParameters();
        try
        {
            userNode.flush();
        }
        catch (BackingStoreException e)
        {
            throw new TestHistoryStorageException(e);
        }
        //saveLanguageConfig(new File(configPath, LANGUAGE_CONFIG_FILE));
        if (testHistory != null) testHistory.savePermanent();
    }
    protected void saveTestParameters()
    {
        for (int tt = 0; tt < TestType.NUM_TEST_TYPES; tt++)
        {
            Preferences ttNode = userNode.node(TestType.getById(tt).getCode());
            ttNode.putInt(LEARNT_PASS_COUNT, learntPassCount[tt]);
            ttNode.putLong(MIN_RETEST_PERIOD, minRetestPeriod[tt]);
            ttNode.putLong(INITIAL_REV_PERIOD, initialRevisionPeriod[tt]);
            ttNode.putLong(SHORT_TERM_PERIOD,shortTermPeriod[tt]);
            ttNode.putLong(SHORT_TERM_REV_PERIOD,shortTermRevisionPeriod[tt]);
            ttNode.putLong(LONG_TERM_PERIOD,longTermPeriod[tt]);
            ttNode.putLong(LONG_TERM_REV_PERIOD, longTermRevisionPeriod[tt]); 
        }
    }
    protected void notifyConfigListeners()
    {
        Iterator l = listeners.iterator();
        while (l.hasNext())
        {
            ((UserConfigListener)l.next())
                .userConfigChanged(this);
        }
    }
    public static void addListener(UserConfigListener ucl)
    {
        if (listeners == null) listeners = new Vector();
        listeners.add(ucl);
    }
    public static void removeListener(UserConfigListener ucl)
    {
        if (listeners != null) listeners.remove(ucl);
    }
}
