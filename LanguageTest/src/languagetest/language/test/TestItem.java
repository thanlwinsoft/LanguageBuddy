/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/test/TestItem.java,v $
 *  Version:       $Revision: 1.6 $
 *  Last Modified: $Date: 2004/04/26 03:29:26 $
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

import java.io.File;
import java.util.Date;
import java.util.Set;
import java.util.HashMap;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.DefaultMutableTreeNode;


import languagetest.language.test.TestModule;
/**
 *
 * @author  keith
 */
public class TestItem implements Comparable, TestComponent, TestItemProperties
{
    public final static long DEFAULT_PLAY_START = 0;
    public final static long DEFAULT_PLAY_END = -1;
    private boolean passed = false;
    private int testCount = 0;
    //private String name = null;
    //private String nativeText = "";
    //private String foreignText = "";
    private HashMap nativeMap = null;
    private HashMap foreignMap = null;
    private File soundFile = null;
    private File pictureFile = null;
    private TestModule module;
    private MutableTreeNode treeNode = null;
    private long startPlay = DEFAULT_PLAY_START;
    private long endPlay = DEFAULT_PLAY_END;
    private long soundLength = DEFAULT_PLAY_END;
    private long creationTime = 0;
    private String creator = "";
    private ConversationTest conversation = null;
    
    /** Creates a new instance of Test */
    public TestItem(/*String name, */TestModule module)
    {
        //this.name = name;
        this.module = module;
        nativeMap = new HashMap();
        foreignMap = new HashMap();
        treeNode = new DefaultMutableTreeNode(this,false);
        creationTime = new Date().getTime();
        creator = UserConfig.getCurrent().getUserName();
    }
    public Set getNativeLanguages()
    {
        return nativeMap.keySet();
    }
    public Set getForeignLanguages()
    {
        return foreignMap.keySet();
    }
    public String getNativeText() 
    {
        return getNativeText(LanguageConfig.getCurrent().getNativeLanguage());
    }
    public String getNativeText(UniversalLanguage lang) 
    {
        Object text = nativeMap.get(lang);
        if (text != null && text instanceof String) return (String)text;
        return ""; 
    }
    public String getForeignText() 
    {
        return getForeignText(LanguageConfig.getCurrent().getForeignLanguage());
    }
    public String getForeignText(UniversalLanguage lang) 
    { 
        Object text = foreignMap.get(lang);
        if (text != null && text instanceof String) return (String)text;
        return ""; 
    }
    public File getSoundFile() { return soundFile; }
    public File getPictureFile() { return pictureFile; }
    public TestModule getModule() { return module; }
    public long getPlayStart() { return startPlay; }
    public long getPlayEnd() { return endPlay; }
    public void setPlayStart(long start) { startPlay = start; }
    public void setPlayEnd(long end) { endPlay = end; }
    public void setCreator(String user) { creator = user; }
    public String getCreator() { return creator; }
    public void setCreationTime(long time) { creationTime = time; }
    public long getCreationTime() { return creationTime; }
    public ConversationTest getConversation() { return conversation; }
    public void setSoundLength(long length) { soundLength = length; }
    public long getSoundLength() { return soundLength; }
    /*
    public void setName(String text)
    {
        name = text;
    }
     */
    /**
     * Sets the value of the native text for the specified language or 
     * deletes it if text is empty.
     */
    public void setNative(String text, UniversalLanguage ul)
    {
        // if name changes need to resort items
        if (text != null && text.length() > 0)
        {
            nativeMap.put(ul, text);            
        }
        else
        {
            nativeMap.remove(ul);
        }
        if (ul.equals(LanguageConfig.getCurrent().getNativeLanguage()))
        {
            module.resortItems();
        }
    }
    public void setNative(String text)
    {
        setNative(text, LanguageConfig.getCurrent().getNativeLanguage());        
    }
    /**
     * Sets the value of the foreign text for the specified language or 
     * deletes it if text is empty.
     */
    public void setForeign(String text, UniversalLanguage ul)
    {
        if (text != null && text.length()>0)
        {
            foreignMap.put(ul, text);
        }
        else
        {
            foreignMap.remove(ul);
        }
    }
    public void setForeign(String text)
    {
        setForeign(text, LanguageConfig.getCurrent().getForeignLanguage());
    }
    public void setSound(String text)
    {
        soundFile = setLinkFile(text);
    }
    protected File setLinkFile(String text)
    {
        File file = new File(text);
        if (!file.isAbsolute())
        {
            // if file isn't absolute, then it will be relative to module file
            file = new File(module.getFile().getParent(), text);
        }
        // give warning if file not valid
        if (!file.exists() || !file.canRead())
        {
            System.out.println("Warning: Can't read " + text);
        }
        return file;
    }
    public void setSoundFile(File file)
    {
        soundFile = file;
    }
    public void setPictureFile(File file)
    {
        pictureFile = file;
    }
    public void setPicture(String text)
    {
        pictureFile = setLinkFile(text);
    }
    public String toString()
    {
        return getName();
    }
    /**
     * get the current name
     */
    public String getName()
    {
        if (nativeMap.containsKey(LanguageConfig.getCurrent().getNativeLanguage()))
        {
            return nativeMap.get(LanguageConfig.getCurrent()
                .getNativeLanguage()).toString();
        }
        else if (nativeMap.size()>0)
        {
            return nativeMap.values().iterator().next().toString();
        }
        else
        {
            return "*[Un-named Item]";
        }
    }
    public void reset()
    {
        testCount = 0;
        passed = false;
    }
    public boolean isPassed() { return passed; }
    public void pass()
    {
        testCount++;
        passed = true;
    }
    public void fail()
    {
        testCount++;
    }
    public int getTestCount() { return testCount; }
    
    /** 
     * The comparison is done at several levels. The first priority is on
     * the name of the native text, then the foreign text, then the creation
     * time and finally the name of the creator. 
     * All of these steps are used to ensure that this will only return 0 if 
     * the items are identical.
     */
    public int compareTo(Object obj)
    {
        int comparison = -1;
        if (obj instanceof TestItem)
        {
            TestItem item = (TestItem)obj;
            comparison = getNativeText().compareTo(item.getNativeText());
            if (comparison == 0)
            {                
                comparison = getForeignText().compareTo(item.getForeignText());
                if (comparison == 0)
                {
                    comparison = (int)(item.getCreationTime() - creationTime);
                    if (comparison == 0)
                    {
                        comparison = creator.compareTo(item.getCreator());
                    }
                }
            }
        }
        return comparison;
    }
    
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        else if (obj instanceof TestItem)
        {
            TestItem item = (TestItem)obj;
            if (creationTime == item.getCreationTime() &&
                creator.equals(item.getCreator()) &&
                getNativeText().equals(item.getNativeText()) &&
                getForeignText().equals(item.getForeignText()))
            {
                return true;
            }
        }
        return false;
    }
    
    
    public MutableTreeNode getTreeNode() { return treeNode; }
    
    public long getModuleCreationTime()
    {
        return module.getCreationTime();
    }
    
    public int getModuleId()
    {
        return module.getUniqueId();
    }
    
    public File getModuleFile()
    {
        return module.getFile();
    }
    public void moveTo(TestModule newModule)
    {
        module.removeTestItem(this);
        // discard old node and create a new one ready for instert
        treeNode.removeFromParent();
        newModule.insertTestItem(this);
        module = newModule;
    }
}
