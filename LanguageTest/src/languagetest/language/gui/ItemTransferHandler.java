/*
 * -----------------------------------------------------------------------
 *  File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/gui/ItemTransferHandler.java,v $
 *  Version:       $Revision: 1.2 $
 *  Last Modified: $Date: 2004/06/20 11:50:46 $
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

package languagetest.language.gui;

import java.util.Vector;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Date;
import java.io.IOException;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JTree;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;


import languagetest.language.test.UserConfig;
import languagetest.language.test.TestHistoryStorageException;
import languagetest.language.test.TestItem;
import languagetest.language.test.TestHistory;
import languagetest.language.test.TestComponent;
import languagetest.language.test.TestType;
import languagetest.language.test.TestModule;
import languagetest.language.test.ItemHistory;
/**
 *
 * @author  keith
 */
public class ItemTransferHandler extends javax.swing.TransferHandler 
{
    public final static DataFlavor FLAVOR = 
            new DataFlavor(TestItemTransferable.class,
            "TestItems");
    Vector items = null;
    TreePath destPath = null;
    HashSet changedNodes = null;
    boolean dragging = false;
    /** Creates a new instance of ItemTransferHandler */
    public ItemTransferHandler()
    {
        items = new Vector();
    }
    
    public boolean isDragging()
    {
        return dragging;
    }
    
    public boolean canImport(javax.swing.JComponent jComponent, java.awt.datatransfer.DataFlavor[] dataFlavor)
    {
        if (jComponent instanceof JTree) return true;
        if (destPath != null)
        {
            TestModule newMod = moduleFromPath(destPath);

            if (newMod != null && items.size()>0)
            {            
                // assume only items from one module selected
                TestItem firstItem = (TestItem)items.elementAt(0);
                if (!firstItem.getModule().equals(newMod))
                {
                    // only return true if we are over a different module
                    return true;
                }
                else
                {
                    System.out.println("Can't move within a module");
                }
            }
            else
            {
                System.out.println("Destination must be a module");
            }
        }
        else
        {
            System.out.println("Empty dest");
        }
        return false;
    }
    
    public int getSourceActions(JComponent c)
    {
        if (c instanceof JTree) return TransferHandler.MOVE;
        return TransferHandler.NONE;
    }
    
    public boolean importData(javax.swing.JComponent jComponent, java.awt.datatransfer.Transferable transferable)
    {
        if (jComponent instanceof JTree)
        {
            // get list of selections
            
            JTree tree = (JTree)jComponent;
            TreePath path = tree.getSelectionPath();
            if (path != null)
            {
                
                TestModule newMod = moduleFromPath(path);
                if (newMod != null)
                {                
                    try
                    {
                        Vector items = (Vector)
                            transferable.getTransferData
                            (transferable.getTransferDataFlavors()[0]);
                        TestItem item = null;
                        changedNodes = new HashSet();
                        changedNodes.add(newMod.getTreeNode());
                        TestHistory h = UserConfig.getCurrent().getTestHistory();
                            
                        for (int i=0; i<items.size(); i++)
                        {
                            item = (TestItem)items.elementAt(i);
                            changedNodes.add(item.getModule().getTreeNode());
                            moveItem(item, newMod, h);
                        }
                        
                        return true;
                    }
                    catch (UnsupportedFlavorException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    catch (IOException e)
                    {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return false;
    }
    
    protected void exportDone(JComponent source, Transferable data, int action) 
    {
        System.out.println("Export finished");
        dragging = false;
        super.exportDone(source, data, action);
        
        try
        {
            if (data == null) return;
            Vector items = (Vector)
                data.getTransferData
                (data.getTransferDataFlavors()[0]);
            if (items.size() > 0 && items.elementAt(0) instanceof TestItem &&
                source instanceof JTree)
            {
                JTree tree = (JTree)source;
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();

                TestItem item = (TestItem)items.elementAt(0);
                Iterator i = changedNodes.iterator();
                model.reload();
                while (i.hasNext())
                {                                
                    TreeNode tNode = (TreeNode)i.next();
                    tree.expandPath
                        (new TreePath(model.getPathToRoot(tNode)));
                }
                TreePath newPath = 
                    new TreePath(model.getPathToRoot(item.getTreeNode()));
                tree.setSelectionPath(newPath);
                // make history changes permanent
                UserConfig.getCurrent().getTestHistory().savePermanent();
            }
        }
        catch (TestHistoryStorageException e)
        {
            System.out.println(e.getMessage());
            // TBD flag to user
        }
        catch (UnsupportedFlavorException e)
        {
            System.out.println(e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }            
        
        changedNodes = null;
    }
    
    protected void moveItem(TestItem item, TestModule newMod, TestHistory h)
    {
        Vector oldH = new Vector();
        for (int t = 0; t<TestType.NUM_TEST_TYPES; t++)
        {            
            ItemHistory ih = null;
            try
            {
                ih = h.getHistoryItem(item, TestType.getById(t));                   
            }
            catch (TestHistoryStorageException e)
            {
                System.out.println(e);
            }
            oldH.add(ih);
        }                       
        
        try
        {
            // if at least one history item existed delete them now
            h.deleteItem(item);     
        }
        catch (TestHistoryStorageException e)
        {
            System.out.println(e);
        }
        //((DefaultTreeModel)tree.getModel())
         //   .removeNodeFromParent(item.getTreeNode());
        item.moveTo(newMod);
        for (int t = 0; t<TestType.NUM_TEST_TYPES; t++)
        {
            ItemHistory hItem = (ItemHistory)oldH.elementAt(t);
            if (hItem == null) continue;
            Object [][] results = hItem.getResultTable();
            System.out.println("Moving " + TestType.getById(t)
                + results.length +
                " results");
            for (int r = 0; r<results.length; r++)
            {
                
                long testTime = ((Date)results[r][0]).getTime();
                boolean pass = ((Boolean)results[r][1]).booleanValue();
                try
                {
                    h.saveResult(item, TestType.getById(t), testTime, pass);
                    System.out.println(item + " " + testTime + " " + pass);
                }
                catch (TestHistoryStorageException e)
                {
                    System.out.println(e);
                }
            }
        }
        System.out.println("Moved " + item);
    }
    
    protected TestModule moduleFromPath(TreePath path)
    {
        
        if (path.getLastPathComponent() instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    path.getLastPathComponent();
            if (node != null && node.getUserObject() instanceof TestComponent)
            {
                TestModule newMod = 
                    ((TestComponent)node.getUserObject()).getModule();
                return newMod;
            }                
        }
        
        return null;
    }
    
    public void exportAsDrag(JComponent comp, InputEvent e, int action) 
    {
        items.clear();
        if (comp instanceof JTree)
        {
            // get list of selections
            JTree tree = (JTree)comp;
            TreePath[] paths = tree.getSelectionPaths();
            for (int i=0; i<paths.length; i++)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    paths[i].getLastPathComponent();
                if (node.getUserObject() instanceof TestItem)
                {
                    items.add(node.getUserObject());
                }
            }
        }
        // ignore drags if no item found
        if (items.size() > 0)
        {
            super.exportAsDrag(comp,e,action);
        }
    }
    
    
    
    protected Transferable createTransferable(JComponent comp)
    {
        if (comp instanceof JTree && items.size()>0)
        {
            System.out.println("Creating transferable");
            dragging = true;
            return new TestItemTransferable(items);
        }
        return null;
    }
    

    
    protected void mouseEvent(java.awt.event.MouseEvent e)
    {
        if (e.getComponent() instanceof JTree)
        {
            JTree tree = (JTree)e.getComponent();
            TreePath newPath = tree.getPathForLocation(e.getX(), e.getY());      
            if (newPath != null && !newPath.equals(destPath))
            {
                destPath = newPath;
                System.out.println(destPath + e.toString());
            }
        }
    }
    
    public class TestItemTransferable implements Transferable
    {
        private Vector items = null;
        
        public TestItemTransferable(Vector items)
        {
            this.items = items;
        }
        public Object getTransferData(java.awt.datatransfer.DataFlavor flavor) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException
        {
            if (flavor.equals(ItemTransferHandler.FLAVOR)) return items;
            return null;
        }
        
        public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors()
        {
            return new DataFlavor [] {FLAVOR};
        }
        
        public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor)
        {
            return flavor.equals(ItemTransferHandler.FLAVOR);
        }
        
    }
}
