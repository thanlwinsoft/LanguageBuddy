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
package org.thanlwinsoft.languagetest.eclipse.views;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.views.TestHistoryProvider.XmlFamily;
import org.thanlwinsoft.languagetest.language.test.TestManager;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.history.ItemType;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType;
import org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType;
import org.thanlwinsoft.schemas.languagetest.history.ResultType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;
import org.thanlwinsoft.schemas.languagetest.history.TestType;

/**
 * @author keith
 *
 */
public class TestHistoryLabelProvider extends LabelProvider
{
    private Image correct = null;
    private Image wrong = null;
    private String langModulePath = null;
    private LanguageModuleType langModule = null;
    private ITreeContentProvider treeContentProvider;
    private NumberFormat percentNF = null;
    public TestHistoryLabelProvider(ITreeContentProvider treeContentProvider)
    {
        this.treeContentProvider = treeContentProvider;
        ImageDescriptor id = LanguageTestPlugin.getImageDescriptor("icons/correct16.png");
        correct = id.createImage();
        id = LanguageTestPlugin.getImageDescriptor("icons/wrong16.png");
        wrong = id.createImage();
        percentNF = new DecimalFormat("0%");
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.LabelProvider#dispose()
     */
    public void dispose()
    {
        correct.dispose();
        wrong.dispose();
        
        super.dispose();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
     */
    public Image getImage(Object element)
    {
        if (element instanceof XmlFamily )
        {
            XmlFamily xf = (XmlFamily)element;
            if (xf.child instanceof ResultType)
            {
                ResultType rt = (ResultType)xf.child;
                if (rt.getPass())
                {
                    return correct;
                }
                else return wrong;
            }
        }
        return super.getImage(element);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
     */
    public String getText(Object element)
    {
        if (element instanceof IResource)
        {
        	String name = ((IResource)element).getName();
        	if (name.equals(TestManager.HISTORY_DIR))
            	name = ((IResource)element).getProject().getName();
        	else if (name.contains("-"))
        	{
        		int hyphen = name.indexOf("-");
        		UniversalLanguage ulN = new UniversalLanguage(name.substring(0, hyphen));
        		UniversalLanguage ulF = new UniversalLanguage(name.substring(hyphen + 1));
        		name = ulN.getDescription() + " / " + ulF.getDescription(); 
        	}
        	return name;
        }
        else if (element instanceof TestHistoryProvider.XmlFamily)
        {
            TestHistoryProvider.XmlFamily xf = (TestHistoryProvider.XmlFamily)element;
            
            if (xf.child instanceof ModuleHistoryType)
            {
                IPath path = new Path(((ModuleHistoryType)xf.child).getPath());
                return path.lastSegment();
            }
            else if (xf.child instanceof ItemType)
            {
                ItemType it = (ItemType)xf.child;
                if (it.isSetAuthor() && it.isSetCreated())
                {
                    ModuleHistoryType mht = (ModuleHistoryType)
                        treeContentProvider.getParent(element);
                    
                    IPath path = new Path(mht.getPath());
                    IWorkspace workspace = ResourcesPlugin.getWorkspace();
                    IFile moduleFile = workspace.getRoot().getFile(path);
                    if (!moduleFile.isAccessible())
                    {
                        int wSeg = workspace.getRoot().getRawLocation().segmentCount();
                        if (path.segmentCount() > wSeg) 
                            moduleFile = workspace.getRoot().getFile(path.removeFirstSegments(wSeg));
                    }
                    if (moduleFile.isAccessible())
                    {
                        try
                        {
                            LanguageModuleDocument doc = 
                                LanguageModuleDocument.Factory.parse(moduleFile.getContents());
                            langModulePath = mht.getPath();
                            langModule = doc.getLanguageModule(); 
                            for (int i = 0; i < langModule.sizeOfTestItemArray(); i++)
                            {
                                TestItemType tit = langModule.getTestItemArray(i);
                                if (tit.getCreator().equals(it.getAuthor()) &&
                                     it.getCreated() == tit.getCreationTime())
                                {
                                    if (tit.sizeOfNativeLangArray() > 0)
                                    {
                                        String nativeLang = tit.getNativeLangArray(0).getStringValue();
                                        if (nativeLang != null)
                                            return nativeLang;
                                    }
                                }
                            }
                        }
                        catch (XmlException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        catch (CoreException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        catch (IOException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        
                    }
                }
                return MessageUtil.getString("Unknown");
            }
            else if (xf.child instanceof TestType)
            {
                TestType tt = (TestType)xf.child;
                String nodeName =  tt.getDomNode().getLocalName();
                int passCount = 0;
                for (int i = 0; i < tt.sizeOfResultArray(); i++)
                {
                    if (tt.getResultArray(i).getPass()) passCount++;
                }
                String percent = percentNF.format(((float)passCount)/
                                ((float)tt.sizeOfResultArray()));
                if (nodeName.equals("NR"))
                {
                    return MessageUtil.getString("WritingResults", percent, 
                                    Integer.toString(tt.sizeOfResultArray()));
                }
                else if (nodeName.equals("FL"))
                {
                    return MessageUtil.getString("ListeningResults", percent, 
                                    Integer.toString(tt.sizeOfResultArray()));                    
                }
                else if (nodeName.equals("FR"))
                {
                    return MessageUtil.getString("ReadingResults", percent, 
                                    Integer.toString(tt.sizeOfResultArray()));                    
                }
                return tt.getDomNode().getLocalName() + " " + 
                    tt.sizeOfResultArray();
            }
            else if (xf.child instanceof ResultType)
            {
                ResultType rt = (ResultType)xf.child;
                SimpleDateFormat df = new SimpleDateFormat();
                Date date = new Date(rt.getTime());
                if (rt.getPass())
                {
                    return MessageUtil.getString("PassTime",df.format(date));
                }
                else 
                {
                    return MessageUtil.getString("FailTime",df.format(date));
                }
            }
        }
        return super.getText(element);
    }
    
}
