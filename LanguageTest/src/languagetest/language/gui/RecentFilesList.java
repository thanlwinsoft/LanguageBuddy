/*
 * RecentFilesList.java
 *
 * NOT FINISHED NOT FINISHED NOT FINISHED NOT FINISHED NOT FINISHED
 *
 * Created on April 24, 2004, 8:22 PM
 */

package languagetest.language.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JMenu;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import languagetest.language.test.TestModule;
import languagetest.language.test.TestModule.ParseException;
/**
 *
 * @author  keith
 */
public class RecentFilesList
{
    public static final int SIZE = 10;
    public static final String RECENT_FILES = "RecentFiles";
    public static final String FILE = "File";
    public static final String ATTRIB = "path";
    private MainFrame mainFrame = null;
    private JMenu menu = null;
    private JPopupMenu popup = null;
    private Vector moduleList = null;
    /** Creates a new instance of RecentFilesList */
    public RecentFilesList(MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
        this.menu = new JMenu("Recent Files");
        this.popup = new JPopupMenu("Recent Files");
        this.moduleList = new Vector();
    }
    
    /** 
     */
    public void add(TestModule module)
    {
        ModuleParam moduleParam = 
            new ModuleParam(module);
        if (!moduleList.contains(moduleParam))
        {
            moduleList.add(moduleParam);
        }
    }
    
    public void remove(TestModule module)
    {
        ModuleParam moduleParam = 
            new ModuleParam(module);
        if (moduleList.contains(moduleParam))
        {
            moduleList.remove(moduleParam);
        }      
    }
    
    public void refresh()
    {
        int mItemCount = 0;
        menu.removeAll();
        popup.removeAll();
        // go through list backwards
        for (int i = moduleList.size() - 1; (i>=0) && (mItemCount < SIZE); i--)
        {
        
            ModuleParam mp = (ModuleParam)moduleList.elementAt(i);
            if (mp.getModule() == null ||
                !mainFrame.getModules().contains(mp.getModule()))
            {
                mItemCount++;
                JMenuItem menuItem = new JMenuItem(mp.getName());
                menuItem.setAction(new FileAction(mp));
                menu.add(menuItem);
                menuItem = new JMenuItem(mp.getName());
                menuItem.setAction(new FileAction(mp));
                popup.add(menuItem);
                mp.setModule(null);                
            }
        }
    }
    
    /**
     * This is only loaded at start up
     */
    protected void add(String name, File file)
    {
        ModuleParam mp = new ModuleParam(name,file);
        if (!moduleList.contains(mp))
        {
            moduleList.add(mp);
        }        
    }
    
    public Element saveToXml(Document doc)
    {
        Element list = doc.createElement(RECENT_FILES);
        int startIndex = moduleList.size() - SIZE;
        if (startIndex < 0) startIndex = 0;
        for (int i = startIndex; i < moduleList.size(); i++)
        {
            ModuleParam mp = (ModuleParam)moduleList.elementAt(i);
            Element eFile = doc.createElement(FILE);
            eFile.setAttribute(ATTRIB, mp.getFile().getAbsolutePath());            
            eFile.insertBefore(doc.createTextNode(mp.getName()), null);
            list.insertBefore(eFile, null);
        }
        return list;
    }
    
    public void parseXml(Element list)
    {
        NodeList nl = list.getChildNodes();
        for (int i = 0; i<nl.getLength(); i++)
        {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE &&
                nl.item(i).getNodeName().equals(FILE))
            {
                Element eFile = (Element)nl.item(i);
                File file = new File(eFile.getAttribute(ATTRIB));
                String description = "";
                if (eFile.hasChildNodes())
                {
                    description = eFile.getFirstChild().getNodeValue();
                }
                add(description, file);
            }
        }
        refresh();
    }
    
    public JMenu getMenu()
    {
        return menu;
    }
    
    public JPopupMenu getPopup()
    {
        return popup;
    }
    
    protected class ModuleParam
    {
        String name; 
        File file;
        TestModule module = null;
        public ModuleParam(TestModule module)
        {
            this.module = module;
            this.name = module.getName();
            this.file = module.getFile();
            if (name == null || name.length() == 0) 
            {
                this.name = file.getName();
            }
        }
        public ModuleParam(String name, File file)
        {
            this.module = null;
            this.name = name;
            this.file = file;
            if (this.name == null || this.name.length() == 0) 
            {
                this.name = file.getName();
            }
        }
        public String getName() { return name; }
        public File getFile() { return file; }
        public TestModule getModule() { return module; }
        public void setModule(TestModule module)
        {
            this.module = module;
        }
        public boolean equals(Object obj)
        {
            if (obj instanceof ModuleParam)
            {
                ModuleParam mp = (ModuleParam)obj;
                if (mp.getFile().equals(file))
                {
                    return true;
                }
            }
            return false;
        }        
    }
    
    public class FileAction extends AbstractAction
    {
        ModuleParam moduleParam = null;
        public FileAction(ModuleParam moduleParam)
        {
            super(moduleParam.getName());
            this.moduleParam = moduleParam;
        }
        
        public void actionPerformed(java.awt.event.ActionEvent e)
        {            
            try
            {
                TestModule module = new TestModule(moduleParam.getFile());
                if (!mainFrame.getModules().contains(module))
                {
                    mainFrame.insertModule(module);
                    moduleParam.setModule(module);
                    refresh();
                }
            }
            catch (FileNotFoundException fnfe)
            {
                System.out.println(fnfe);
            }
            catch (ParseException fnfe)
            {
                System.out.println(fnfe);
            }
        }
        
    }
}
