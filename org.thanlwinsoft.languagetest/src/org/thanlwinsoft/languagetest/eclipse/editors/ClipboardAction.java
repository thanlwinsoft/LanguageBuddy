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
package org.thanlwinsoft.languagetest.eclipse.editors;

import org.apache.xmlbeans.XmlException;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.schemas.languagetest.module.ForeignLangType;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType;
import org.thanlwinsoft.schemas.languagetest.module.NativeLangType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;
/**
 * @author keith
 *
 */
public class ClipboardAction extends Action implements SelectionListener
{
    //private final String  id;
    private TestItemEditor editor;
    private Display display;
    public static final int CUT = 0;
    public static final int COPY = 1;
    public static final int PASTE = 2;
    private final int mode;
    private String langCode = null;
    
    private Clipboard clipboard;
    public ClipboardAction(TestItemEditor editor, int mode)
    {
        this.display = editor.getSite().getPage().getWorkbenchWindow().getShell().getDisplay();
        this.editor = editor;
        this.mode = mode;
        
        clipboard = new Clipboard(display);
        switch (mode)
        {
        case CUT:
            setText(MessageUtil.getString("Cut"));
            break;
        case COPY:
            setText(MessageUtil.getString("Copy"));
            break;
        case PASTE:
            setText(MessageUtil.getString("Paste"));
            break;
        }
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run()
    {
        Transfer xmlTransfer = HTMLTransfer.getInstance();
        Transfer textTransfer = TextTransfer.getInstance();
        // if the langCode is empty we copy the whole text item
        // otherwise, we just copy the requested item
        if (mode != PASTE)
        {
            TestItemType [] items = editor.getSelectedItems();
            if (items.length == 0) return; 
            
            StringBuilder textBuilder = new StringBuilder();
            LanguageModuleDocument doc = 
                LanguageModuleDocument.Factory.newInstance();
            LanguageModuleType module = doc.addNewLanguageModule();
            for (int i = 0; i < items.length; i++)
            {
                TestItemType tit = module.addNewTestItem();
                if (langCode == null)
                    module.setTestItemArray(i, items[i]);
                for (int j = 0; j < items[i].sizeOfNativeLangArray(); j++)
                {
                    NativeLangType lt = items[i].getNativeLangArray(j);
                    if (langCode != null && !langCode.equals(lt.getLang()))
                    {
                        continue;
                    }
                    textBuilder.append('\"');
                    textBuilder.append(lt.getStringValue());
                    textBuilder.append('\"');
                    if (langCode == null) textBuilder.append('\t');
                    else
                    {
                        NativeLangType nlt = tit.addNewNativeLang();
                        nlt.set(lt);
                        if (mode == CUT) lt.setStringValue("");
                    }
                }
                for (int j = 0; j < items[i].sizeOfForeignLangArray(); j++)
                {
                    ForeignLangType lt = items[i].getForeignLangArray(j);
                    if (langCode != null && !langCode.equals(lt.getLang()))
                    {
                        continue;
                    }
                    textBuilder.append('\"');
                    textBuilder.append(lt.getStringValue());
                    textBuilder.append('\"');
                    if (langCode == null) textBuilder.append('\t');
                    else 
                    {
                        ForeignLangType flt = tit.addNewForeignLang();
                        flt.set(lt);
                        if (mode == CUT) lt.setStringValue("");
                    }
                }
                if (items[i].isSetImg()&& 
                    (langCode == null || 
                     langCode.equals(TestItemEditor.PICTURE_COL))) 
                {
                    textBuilder.append('\"');
                    textBuilder.append(items[i].getImg());
                    textBuilder.append('\"');
                    if (mode == CUT) items[i].setImg("");
                }
                if (langCode == null) textBuilder.append('\t');
                if (items[i].isSetSoundFile() && 
                    (langCode == null || 
                     langCode.equals(TestItemEditor.SOUND_COL))) 
                {
                    textBuilder.append('\"');
                    textBuilder.append(items[i].getSoundFile().getStringValue());
                    textBuilder.append('\"');
                    if (mode == CUT) 
                        items[i].getSoundFile().setStringValue("");
                }
                    
                textBuilder.append('\n');
            }
            Transfer[] transfers = new Transfer[]{textTransfer, xmlTransfer};
            String xmlText = doc.xmlText();
            Object[] data = new Object[]{textBuilder.toString(), xmlText};
            clipboard.setContents(data, transfers);
            if (mode == CUT)
            {
                if (langCode == null) editor.deleteSelection();
                setDirty();
            }
        }
        else // Paste
        {
            String textData = (String)clipboard.getContents(textTransfer);
            //if (textData != null) System.out.println("Text is "+textData);
            String xmlData = (String)clipboard.getContents(xmlTransfer);
            if (xmlData != null)
            {
                try
                {
                    LanguageModuleDocument doc = LanguageModuleDocument.Factory.parse(xmlData);
                    //System.out.println("Paste has " + doc.getLanguageModule().sizeOfTestItemArray() + " test items");
                    if (doc.getLanguageModule() != null)
                    {
                        if (langCode == null)
                            editor.pasteItems(doc.getLanguageModule().getTestItemArray());
                        else
                            editor.pasteItems(doc.getLanguageModule().getTestItemArray(), langCode);
                        setDirty();
                        return;
                    }
                } 
                catch (XmlException e)
                {
                	// it may be opendoc or some other xml
                    //e.printStackTrace();
                }
            }
            if (textData != null)
            {
                String [] lines = null;
                // if the lines start with quotes assume they all do
                if (textData.startsWith("\"") && textData.endsWith("\""))
                {
                    lines = textData.substring(1, textData.length() - 1).split("[\"\\r\\n]+\"");
                }
                else lines = textData.split("[\\r\\n]+");
                
                if (langCode != null && lines.length > 0)
                {
                    editor.pasteItems(lines, langCode);
                    setDirty();
                }
            }
        }
    }
    
    protected void setDirty()
    {
        editor.getParent().setDirty(true);
        editor.getParent().firePropertyChange(TestItemEditor.PROP_DIRTY);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#runWithEvent(org.eclipse.swt.widgets.Event)
     */
    public void runWithEvent(Event event)
    {
        display = event.display;
        run();
    }
    
    public void dispose()
    {
        clipboard.dispose();
        clipboard = null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
        // TODO Auto-generated method stub
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        run();
    }
    public void setLangCode(String code)
    {
        langCode = code;
    }
}
