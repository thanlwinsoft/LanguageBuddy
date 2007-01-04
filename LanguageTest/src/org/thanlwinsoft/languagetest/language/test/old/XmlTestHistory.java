/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/test/XmlTestHistory.java,v $
 *  Version:       $Revision: 704 $
 *  Last Modified: $Date: 2007-01-05 05:50:38 +0700 (Fri, 05 Jan 2007) $
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

import java.util.Iterator;
import java.util.Date;
import java.util.Properties;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;

/**
 *
 * @author  keith
 */
public class XmlTestHistory implements TestHistory, UserConfigListener
{
    org.w3c.dom.Document currentDoc = null;
    File currentHistoryFile = null;
    File historyPath = null;
    FileFilter filter = null;
    private long currentModuleCreationTime = -1;
    private int currentModuleId = -1;
    private static final String HISTORY_DIR = "TestHistory";
    private static final String MODULE_TAG = "ModuleHistory";
    private static final String ITEM_TAG = "Item";
    private static final String RESULT_TAG = "Result";
    private static final String MOD_PATH_ATTRIB = "path";
    private static final String ITEM_TIME_ATTRIB = "created";
    private static final String ITEM_AUTHOR = "author";
    private static final String RESULT_TIME_ATTRIB = "time";
    private static final String RESULT_PASS_ATTRIB = "pass";
    private static final String TYPE_DISABLED_ATTRIB = "disabled";
    private static final String HISTORY_EXT = ".xml";
    private static final String ID_DELIMIT = "-";
    private static final String LANG_DELIMIT = "-";
    private DocumentBuilderFactory dfactory = null;
    private DocumentBuilder docBuilder = null;
    boolean historyDirty = false;
    /** Creates a new instance of XmlTestHistory */
    public XmlTestHistory(UserConfig userConfig, LanguageConfig langConfig)
    {
        try
        {
            dfactory = DocumentBuilderFactory.newInstance();
            docBuilder = dfactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException pce)
        {
            System.out.println(pce.toString());
        }
        setHistoryPath(userConfig);
        // register for config changes
        UserConfig.addListener(this);

        filter = new FileFilter()
        {
            public boolean accept(File file)
            {
                if (file.getName().endsWith(HISTORY_EXT)) return true;
                // case insensitive version
                if (file.getName().toLowerCase().endsWith(HISTORY_EXT.toLowerCase())) return true;
                return false;
            }
        };
    }
    
    public void userConfigChanged(UserConfig userConfig)
    {
        setHistoryPath(userConfig);
    }
    
    protected void setHistoryPath(UserConfig userConfig)
    {
        historyPath = 
            new File(userConfig.getConfigPath(),
                 LanguageConfig.getCurrent().getNativeLangCode()
                 + LANG_DELIMIT
                 + LanguageConfig.getCurrent().getForeignLangCode()
                 + File.separator + HISTORY_DIR);
        if (!historyPath.exists()) historyPath.mkdirs();        
    }
    
    public ItemHistory getHistoryItem(TestItem item, TestType type)
        throws TestHistoryStorageException
    {
        File moduleFile = fileFromItem(item);
        // if the module is not already open, open it now
        if (!moduleFile.equals(currentHistoryFile))
        {
            if (!moduleFile.exists() || !openModule(moduleFile))
            {
                return null;
            }
        }
        // does the item have a history?
        Element itemElement = findItem(item);
        if (itemElement == null) return null;
        // find tag for this test type
        NodeList testTypes = itemElement.getChildNodes();
        Node eType = null;
        int i = 0;
        for (i=0; i<testTypes.getLength(); i++)
        {
            eType = testTypes.item(i);
            if (eType.getNodeType() == Node.ELEMENT_NODE &&
                eType.getNodeName().equals(type.getCode())) 
            {
                break;
            }
        }
        if (i >= testTypes.getLength())
        {
            // type not found - no history exists for this test type
            return null;
        }
        ItemHistory ih = new XmlItemHistory((Element)eType);
        return ih;
    }
    
    
    public int getModuleCount()
    {
        return historyPath.listFiles(filter).length;
    }
    
    public Iterator iterator(TestType type)
    {
        
        return new ItemIterator(historyPath.listFiles(filter), type);
    }
    
    protected Element getItemNodeFromItem(TestItemProperties item)
        throws TestHistoryStorageException
    {
        Element top = null;
        File moduleFile = fileFromItem(item);
        // if the module is not already open, open it now
        if (!moduleFile.equals(currentHistoryFile))
        {
            if (currentHistoryFile != null && historyDirty) saveCurrentHistory();
            if (!moduleFile.exists()||!openModule(moduleFile))
            {
                // create a new file
                
                currentDoc = docBuilder.newDocument();
                currentHistoryFile = moduleFile;
                top = currentDoc.createElement(MODULE_TAG);
                String modPath = "";
                try
                {
                    modPath = item.getModuleFile().getCanonicalPath();
                }
                catch (IOException ioe)
                {
                    System.out.println(ioe);
                    throw new TestHistoryStorageException(ioe.getMessage());
                }
                top.setAttribute(MOD_PATH_ATTRIB, modPath);
                currentDoc.insertBefore(top, null);
            }
        }
        top = currentDoc.getDocumentElement();
        // does the item already have a history?
        Element itemElement = findItem(item);
        if (itemElement == null)
        {
            itemElement = currentDoc.createElement(ITEM_TAG);
            itemElement.setAttribute(ITEM_TIME_ATTRIB,
                                     Long.toString(item.getCreationTime()));
            itemElement.setAttribute(ITEM_AUTHOR,
                                     item.getCreator());
            top.appendChild(itemElement);
        }
        return itemElement;   
    }
    
    protected Node getTypeNodeFromItem(TestItemProperties item, TestType type)
        throws TestHistoryStorageException
    {
        Element itemElement = getItemNodeFromItem(item);
        NodeList testTypes = itemElement.getChildNodes();
        Node eType = null;
        int i = 0;
        for (i=0; i<testTypes.getLength(); i++)
        {
            eType = testTypes.item(i);
            if (eType.getNodeType() == Node.ELEMENT_NODE &&
                eType.getNodeName().equals(type.getCode())) 
            {
                break;
            }
        }
        if (i >= testTypes.getLength())
        {
            // type not found so create it now
            eType = currentDoc.createElement(type.getCode());
            itemElement.appendChild(eType);
        }
        return eType;
    }
    
    public void saveResult(TestItem item, TestType type, long testTime, boolean pass)
        throws TestHistoryStorageException
    {
        Node eType = getTypeNodeFromItem(item, type);
        Element result = currentDoc.createElement(RESULT_TAG);
        result.setAttribute(RESULT_TIME_ATTRIB, Long.toString(testTime));
        result.setAttribute(RESULT_PASS_ATTRIB, Boolean.toString(pass));
        eType.appendChild(result);
        // record that history has changed
        historyDirty = true;
    }
    protected Element findItem(TestItemProperties item)
    {
        Element e = null;
        String refTime = Long.toString(item.getCreationTime());
        NodeList items = currentDoc.getElementsByTagName(ITEM_TAG);
        for (int i = 0; i<items.getLength(); i++)
        {
            e = (Element)items.item(i);
            if (e.getAttribute(ITEM_TIME_ATTRIB).equals(refTime) &&
                e.getAttribute(ITEM_AUTHOR).equals(item.getCreator()))
            {
                return e; // item found
            }
        }
        return null;
    }
    protected File fileFromItem(TestItemProperties item)
    {
        return new File(historyPath, 
            Integer.toHexString(item.getModuleId())  + ID_DELIMIT + 
            Long.toString(item.getModuleCreationTime()) + HISTORY_EXT);
    }
    protected int moduleIdFromFile(File file)
    {
        int modId = -1;
        int idEnd = file.getName().indexOf(ID_DELIMIT);
        if (idEnd > 0)
        {
            try
            {
                modId = Integer.parseInt(file.getName().substring(0,idEnd),16);
            }
            catch (NumberFormatException nfe)
            {
                System.out.println("Corrupt mod id in file name " + file.getName());
            }
        }
        else
        {
            System.out.println("Corrupt mod id in file name " + file.getName());
        }
        return modId;
    }
    protected long moduleCreationTimeFromFile(File file)
    {
        long time = -1;
        int idEnd = file.getName().indexOf(ID_DELIMIT);
        int timeEnd = file.getName().indexOf(HISTORY_EXT);
        // check indexes are sensible
        if (idEnd > 0 && timeEnd > idEnd)
        {        
            try
            {
                time = Long.parseLong(file.getName().substring(idEnd + 1,timeEnd));
            }
            catch (NumberFormatException nfe)
            {
                System.out.println("Corrupt mod time in file name " + file.getName());
            }
        }
        else
        {
            System.out.println("Corrupt mod time in file name " + file.getName());
        }
        return time;
    }
    protected boolean openModule(File xmlFile) throws TestHistoryStorageException
    {
        if (historyDirty && currentHistoryFile != null)
        {
            saveCurrentHistory();
        }
        historyDirty = false;
        currentDoc = null;
        currentModuleId = -1;
        currentModuleCreationTime = -1;
        try 
        {
            if (dfactory == null || docBuilder == null)
            {
                dfactory = DocumentBuilderFactory.newInstance();
                dfactory.setIgnoringElementContentWhitespace(true);
                docBuilder = dfactory.newDocumentBuilder();
            }
            currentDoc = docBuilder.parse(new InputSource("file:" + xmlFile.getAbsolutePath()));
        }
        catch (ParserConfigurationException pce)
        {
            System.out.println(pce.getMessage());
            throw new TestHistoryStorageException(pce);
        }
        catch (SAXException se)
        {
            System.out.println(se.getMessage());
            throw new TestHistoryStorageException(se);
        }
        catch (IOException ioe)
        {
            System.out.println(ioe.getMessage());
            throw new TestHistoryStorageException(ioe);
        }
        if (currentDoc != null)
        {
            currentHistoryFile = xmlFile;
            currentModuleId = moduleIdFromFile(xmlFile);
            currentModuleCreationTime = moduleCreationTimeFromFile(xmlFile);
            return true;
        }
        return false;
    }
    boolean saveCurrentHistory() throws TestHistoryStorageException
    {
        try
        {
            // now output the result
            TransformerFactory tfactory = TransformerFactory.newInstance(); 

            // This creates a transformer that does a simple identity transform, 
            // and thus can be used for all intents and purposes as a serializer.
            Transformer serializer = tfactory.newTransformer();

            Properties oprops = new Properties();
            oprops.put(OutputKeys.METHOD, "xml");
            oprops.put(OutputKeys.INDENT, "yes");
            serializer.setOutputProperties(oprops);
            DOMSource domSource = new DOMSource(currentDoc);

                serializer.transform(domSource, 
                                     new StreamResult(currentHistoryFile));
        }
        catch (IllegalArgumentException iae)
        {
            System.out.println(iae.toString());
            throw new TestHistoryStorageException(iae);
        }
        catch (TransformerConfigurationException tce)
        {
            System.out.println(tce.toString());
            throw new TestHistoryStorageException(tce);
        }
        catch (TransformerException te)
        {
            System.out.println(te.toString());
            throw new TestHistoryStorageException(te);
        }
        
        
        historyDirty = false;
        return true;
    }
    
    public void savePermanent() throws TestHistoryStorageException
    {
        if (historyDirty && currentHistoryFile != null)
        {
            saveCurrentHistory();
        }
        historyDirty = false;
        currentDoc = null;
        currentModuleId = -1;
        currentModuleCreationTime = -1; 
        currentHistoryFile = null;
    }
    
    public void deleteItem(TestItemProperties item) throws TestHistoryStorageException
    {
        Element eItem = getItemNodeFromItem(item);
        if (eItem != null)
        {
            eItem.getParentNode().removeChild(eItem);
            historyDirty = true;
        }
    }
    
    public void ignoreItem(TestItemProperties item, TestType type, boolean ignore) throws TestHistoryStorageException
    {
        Node eType = getTypeNodeFromItem(item, type);
        if (eType != null)
        {
            Element itemElement = (Element)eType;
            if (ignore) 
            {
                itemElement.setAttribute(TYPE_DISABLED_ATTRIB,"true");
                historyDirty = true;
            }
            else if(itemElement.hasAttribute(TYPE_DISABLED_ATTRIB))
            {
                itemElement.removeAttribute(TYPE_DISABLED_ATTRIB);
                historyDirty = true;
            }
        }
    }
    
    public void deleteItemType(TestItemProperties item, TestType type) throws TestHistoryStorageException
    {
        Node eType = getTypeNodeFromItem(item, type);
        if (eType != null)
        {
            eType.getParentNode().removeChild(eType);
            historyDirty = true;
        }
    }
    
    protected class XmlItemHistory extends ItemHistory
    {
        /*
        protected File moduleFile = null;
        protected int moduleId = 0;
        protected long creationTime = -1;
        protected long moduleCreationTime = -1;
        protected long lastFailDate = -1;
        protected long lastPassDate = -1;
        protected long lastTestDate = -1;
        protected int testCount = 0;
        protected int passCount = 0;
        protected String author = "Unknown";
        protected int consecutivePassCount = 0;
        protected Object [][] resultTable = null;
         */
        private Element typeElement = null;
        public XmlItemHistory(Element typeTag)
        {
            this.typeElement = typeTag;
            Element itemElement = (Element)typeTag.getParentNode();
            Element modElement = (Element)itemElement.getParentNode();
            moduleId = currentModuleId;
            moduleCreationTime = currentModuleCreationTime;
            moduleFile = new File(modElement.getAttribute(MOD_PATH_ATTRIB));
            author = itemElement.getAttribute(ITEM_AUTHOR);
            try
            {
                creationTime = 
                    Long.parseLong(itemElement.getAttribute(ITEM_TIME_ATTRIB));
            }
            catch (NumberFormatException nfe)
            {
                System.out.println(nfe);
                creationTime = -1;
            }
            if (typeTag.hasAttribute(TYPE_DISABLED_ATTRIB)) disabled = true;
            NodeList resultNodes = typeTag.getChildNodes();
            // count number of result nodes
            int numResults = 0;
            for (int i = 0; i<resultNodes.getLength(); i++)
            {
                if (resultNodes.item(i).getNodeType() == Node.ELEMENT_NODE &&
                    resultNodes.item(i).getNodeName().equals(RESULT_TAG))
                {
                    numResults++;
                }
            }
            resultTable = new Object [numResults][2];
            long firstTestDate = -1;
            int resultId = 0;
            // assumes results are in time order, oldest first
            for (int i = 0; i<resultNodes.getLength(); i++)
            {
                if (resultNodes.item(i).getNodeType() == Node.ELEMENT_NODE &&
                    resultNodes.item(i).getNodeName().equals(RESULT_TAG))
                {
                    Element result = (Element)resultNodes.item(i);
                    try
                    {
                        lastTestDate = Long.parseLong(result
                            .getAttribute(RESULT_TIME_ATTRIB));
                        resultTable[resultId] [0] = new Date(lastTestDate);
                        testCount++;
                        if (Boolean.valueOf(result
                            .getAttribute(RESULT_PASS_ATTRIB)).booleanValue())
                        {
                            passCount++;
                            lastPassDate = lastTestDate;
                            consecutivePassCount++;
                            resultTable[resultId] [1] = new Boolean(true);
                        }
                        else
                        {
                            lastFailDate = lastTestDate;
                            consecutivePassCount = 0;
                            resultTable[resultId] [1] = new Boolean(false);
                        }
                        // initialise firstTestDate on first iteration
                        if (i == 1) firstTestDate = lastTestDate;
                        // increment result counter
                        resultId++;
                    }
                    catch (NumberFormatException nfe)
                    {
                        System.out.println(nfe);
                        // ignore result
                    }
                }
            }
            // check that lastFailDate has been initialised, otherwise set it to
            // date of first test
            if (lastFailDate == -1)
            {
                lastFailDate = firstTestDate;
            }
            
        }
        /**
         * This is protected to ensure that the current history module
         * always corresponds to the one for this history item. Otherwise 
         * we might set the dirty flag for the wrong module.
         *
         */
        protected void setDisabled(boolean disable)
        {
            if (disable && disabled == false) 
            {
                typeElement.setAttribute(TYPE_DISABLED_ATTRIB,"true");
                historyDirty = true;
            }
            else if(!disable && disabled == true)
            {
                typeElement.removeAttribute(TYPE_DISABLED_ATTRIB);
                historyDirty = true;
            }
            disabled = disable;
        }
        
        public void removeModuleHistory()
        {
            File moduleHFile = fileFromItem(this);
            moduleHFile.delete();
        }
    }
    
    public class ItemIterator implements java.util.Iterator
    {
        private ItemHistory itemH = null;
        private Document doc;
        private int fileIndex = 0;
        private Element itemElement = null;
        private TestType testType;
        private File [] fileList;
        public ItemIterator(File [] fileList, TestType testType)
        {
            this.testType = testType;
            this.fileList = fileList;
        }
        public boolean hasNext()
        {
            while (itemH == null)
            {
                if (doc == null)
                {
                    if (!nextFile()) return false;    
                    doc = currentDoc;
                }
                Node tempNode = null;
                if (itemElement == null) 
                {
                    tempNode = doc.getDocumentElement().getFirstChild();
                    
                }
                else
                {
                    tempNode = itemElement.getNextSibling();
                }
                while (tempNode != null &&
                       tempNode.getNodeType() != Node.ELEMENT_NODE)
                {
                    tempNode = tempNode.getNextSibling();
                }
                if (tempNode == null)
                {
                    itemElement = null;
                    doc = null;
                }
                else
                {
                    itemElement = (Element)(tempNode);
                    NodeList testTypes = itemElement.getChildNodes();
                    Node eType = null;
                    int i = 0;
                    for (i=0; i<testTypes.getLength(); i++)
                    {
                        eType = testTypes.item(i);
                        if (eType.getNodeType() == Node.ELEMENT_NODE 
                            && eType.getNodeName().equals(testType.getCode()))
                        {
                            itemH = new XmlItemHistory((Element)eType);
                            // skip disabled items
                            if (itemH.isDisabled())
                            {
                                itemH = null;
                            }
                            break;                            
                        }
                    }
                }
            }
            return true;
        }
        
        public Object next()
        {
            if (itemH == null && hasNext() == false)
            {
                throw new NoSuchElementException();
            }
            ItemHistory nextItem = itemH;
            itemH = null;
            return nextItem;
        }
        
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        protected boolean nextFile() 
        {
            boolean openned = false;
            while (fileIndex < fileList.length)
            {
                try
                {
                    if (openModule(fileList[fileIndex]))
                    {
                        openned = true;
                        fileIndex++;
                        break; 
                    }
                }
                catch (TestHistoryStorageException hse)
                {
                    System.out.println(hse);
                }
                fileIndex++;
            }
            return openned;
        }
    }
}
