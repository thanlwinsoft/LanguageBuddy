/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/editors/LanguageTable.java $
 *  Revision        $LastChangedRevision: 1388 $
 *  Last Modified:  $LastChangedDate: 2009-01-31 19:32:00 +0700 (Sat, 31 Jan 2009) $
 *  Last Change by: $LastChangedBy: keith $
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.math.BigDecimal;

import org.apache.xmlbeans.XmlObject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.workspace.WorkspaceLanguageManager;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.module.LangType;

/**
 * @author keith
 *
 */
public class LanguageTable extends Composite 
{
	//private final static int LANG_DEPTH = 5;
	private TreeViewer tableViewer = null;
    private HashMap<String, LangType> availableTypes = null;// String, LangType
    private HashMap<String, XmlObject> inUseTypes = null;// String, LangType
    private HashSet<ModifyListener> modifyListeners = null;
    private final int LANG_NAME_COL = 0;
    private final int IN_USE_COL = 1;
    private final int LANG_FONT_COL = 2;
    private final int FONT_SIZE_COL = 3;
    private final String LANG_NAME = "Lang";
    private final String IN_USE = "InUse";
    private final String LANG_FONT = "Font";
    private final String FONT_SIZE = "FontSize";
    private boolean dirty = false;
    
    private LanguageLabelProvider labelProvider = null;
    private LanguageContentProvider contentProvider = null;
    private UniversalLanguage [] languages = null;
    //private IEditorPart editor = null;
    
	/**
	 * @param parent
	 * @param style
	 */
	public LanguageTable(Composite parent, int style) 
	{
		super(parent, style);
        modifyListeners = new HashSet<ModifyListener>();
        this.setLayout(new FillLayout());
        availableTypes = new HashMap<String, LangType>();
        inUseTypes = new HashMap<String, XmlObject>();
        tableViewer = new TreeViewer(this, SWT.H_SCROLL | SWT.V_SCROLL |
                SWT.SINGLE | SWT.FULL_SELECTION);
        labelProvider = new LanguageLabelProvider();
        contentProvider = new LanguageContentProvider();
        tableViewer.setContentProvider(contentProvider);
        tableViewer.setLabelProvider(labelProvider);
        LanguageCellModifier modifier = new LanguageCellModifier();
        tableViewer.setCellModifier(modifier);
        tableViewer.getTree().setHeaderVisible(true);
        tableViewer.getTree().setSize(SWT.DEFAULT, 200);
        TreeColumn col = new TreeColumn(tableViewer.getTree(), SWT.LEFT);
        col.setResizable(true);
        col.setWidth(200);
        col.setText(MessageUtil.getString("Language"));
        col = new TreeColumn(tableViewer.getTree(), SWT.LEFT);
        col.setResizable(true);
        col.setWidth(100);
        col.setText(MessageUtil.getString("LangEnabled"));
        col = new TreeColumn(tableViewer.getTree(), SWT.LEFT);
        col.setResizable(true);
        col.setWidth(100);
        col.setText(MessageUtil.getString("Font"));
        col = new TreeColumn(tableViewer.getTree(), SWT.LEFT);
        col.setResizable(true);
        col.setWidth(100);
        col.setText(MessageUtil.getString("FontSize"));
        CellEditor[] editors = new CellEditor[] {
                null, new CheckboxCellEditor(tableViewer.getTree(), SWT.NONE), 
                new FontCellEditor(tableViewer.getTree(), SWT.NONE),
                new TextCellEditor(tableViewer.getTree())
        };
        String [] columnProperties = new String [] {
                LANG_NAME, IN_USE, LANG_FONT, FONT_SIZE
        };
        tableViewer.setColumnProperties(columnProperties);
        tableViewer.setCellEditors(editors);
        for (int i = 0; i < editors.length; i++)
        {
            if (editors[i] != null) editors[i].setValidator(modifier);
        }
        
	}
    
    /* (non-Javadoc)
     * @see org.eclipse.swt.widgets.Control#addFocusListener(org.eclipse.swt.events.FocusListener)
     */
    public void addFocusListener(FocusListener listener)
    {
        tableViewer.getTree().addFocusListener(listener);
        super.addFocusListener(listener);
    }
    

    /* (non-Javadoc)
     * @see org.eclipse.swt.widgets.Control#removeFocusListener(org.eclipse.swt.events.FocusListener)
     */
    public void removeFocusListener(FocusListener listener)
    {
        tableViewer.getTree().removeFocusListener(listener);
        super.removeFocusListener(listener);
    }

    public void addModifyListener(ModifyListener ml)
    {
        modifyListeners.add(ml);
    }
    public void removeModifyListener(ModifyListener ml)
    {
        modifyListeners.remove(ml);
    }
    /** 
     * Set the project languages 
     * The project languages are defined in the root of the project tree.
     * @param array of languages
     * */
    public void setProjectLangs(LangType [] langs)
    {
        languages = new UniversalLanguage[langs.length];
        availableTypes.clear();
        for (int i = 0; i < langs.length; i++)
        {
            availableTypes.put(langs[i].getLang(), langs[i]);
            languages[i] = new UniversalLanguage(langs[i].getLang());
        }
        contentProvider.setLanguages(languages);
        tableViewer.setInput(languages);
        tableViewer.getTree().setData(languages);
        tableViewer.expandAll();
        tableViewer.refresh();
    }
    /** 
     * Set the project languages 
     * The project languages are defined in the root of the project tree.
     * @param array of languages
     */
    public void setProjectLangs(HashMap<String, LangType> langs)
    {
        languages = new UniversalLanguage[langs.size()];
        Object [] langKeys = langs.keySet().toArray();
        for (int i = 0; i < languages.length; i++)
        {
            languages[i] = new UniversalLanguage(langKeys[i].toString());
        }
        availableTypes.clear();
        availableTypes = langs;
        if (languages != null && languages.length > 0)
        {
            contentProvider.setLanguages(languages);
            tableViewer.setInput(languages);
            tableViewer.getTree().setData(languages);
            tableViewer.expandAll();
            tableViewer.refresh();
        }
    }
    /** set the module languages that are defined in the module file
     * 
     * @param langs
     */
    public void setModuleLangs(LangType [] langs)
    {
        inUseTypes.clear();
        for (int i = 0; i < langs.length; i++)
        {
            inUseTypes.put(langs[i].getLang(), langs[i]);
        }
        tableViewer.refresh();
        tableViewer.expandAll();
        dirty = false;
    }
    /** Set the module languages that are defined in the module file
     *  Keys are String language codes, Values are LangType
     * @param langs
     */
    public void setModuleLangs(HashMap<String, XmlObject> langs)
    {
        inUseTypes.clear();
        inUseTypes = langs;
        tableViewer.refresh();
        tableViewer.expandAll();
        dirty = false;
    }
    
    public LangType [] getModuleLangs()
    {
        LangType [] array = new LangType[inUseTypes.size()];
        return inUseTypes.values().toArray(array);
    }
    
    public LangType [] getProjectLangs()
    {
        LangType [] array = new LangType[availableTypes.size()];
        return availableTypes.values().toArray(array);
    }
    
    public void saveProjectLangs(IProject project, IProgressMonitor monitor, boolean notInUseOnly)
    {
        Iterator<String> it = availableTypes.keySet().iterator();
        while (it.hasNext())
        {
            String key = it.next().toString();
            if (notInUseOnly && inUseTypes.containsKey(key)) continue;
            LangType lt = availableTypes.get(key);
            WorkspaceLanguageManager.addLanguage(project, lt, monitor);
        }
    }
    
    public LangType getSelectedLang()
    {
        ITreeSelection s = (ITreeSelection)tableViewer.getSelection();
        if (s.getFirstElement() instanceof UniversalLanguage)
        {
            UniversalLanguage ul = (UniversalLanguage)s.getFirstElement();
            return availableTypes.get(ul.getCode());
        }
        return null;
    }
    
    /** Provides the labels for the columns in the language table
     * 
     * @author keith
     *
     */
	protected class LanguageLabelProvider extends LabelProvider 
        implements ITableLabelProvider 
        
	{

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex)
        {
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex)
        {
            UniversalLanguage ul = null;
            if (element instanceof UniversalLanguage)
            {
                ul = (UniversalLanguage)element;
            }
            else if (element instanceof String[])
            {
                String[] langProps = (String[])element;
                ul = new UniversalLanguage(langProps);
                if (columnIndex > 0) return null;
            }
            else if (element instanceof String)
            {
                ul = new UniversalLanguage(element.toString());
                if (columnIndex > 0) return null;
            }
            if (ul != null)
            {
                LangType lt = availableTypes.get(ul.getCode());
                boolean inUse = false;
                if (lt != null && inUseTypes.containsKey(ul.getCode()))
                {
                    // use the module font, not the project font
                    lt = (LangType)inUseTypes.get(ul.getCode());
                    inUse = true;
                }
                switch (columnIndex)
                {
                case LANG_NAME_COL:
                    return ul.getDescription();
                case IN_USE_COL:
                    if (inUse)
                        return MessageUtil.getString("yes");
                    else if (lt != null)
                        return MessageUtil.getString("no");
                    return "";
                case LANG_FONT_COL:
                    if (lt != null)
                    {
                        return lt.getFont();
                    }
                    return "";
                case FONT_SIZE_COL:
                    if (lt != null)
                    {
                        return lt.getFontSize().toString();
                    }
                    return "";
                }
                
            }
            return null;
        }
	}
	/** Provides the content for a Language Table.
     * A UniversalLanguage is used one per column. The top level tree objects
     * are raw language IDs. Scripts and sub classifications are the children.
     * @author keith
     *
	 */
	public class LanguageContentProvider implements ITreeContentProvider
	{
		private UniversalLanguage [] langs = null;
        protected boolean useTree = false;
		public LanguageContentProvider()
        {
            
        }
        public void setLanguages(UniversalLanguage [] langs)
		{
			this.langs = langs;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
		 */
		public Object[] getChildren(Object parentElement) {
			HashSet<UniversalLanguage> set = new HashSet<UniversalLanguage>();
			if (parentElement instanceof String[])
			{
				String [] nodeArray = (String[])parentElement;
                if (nodeArray.length > 1) return null;
				for (int i = 0; i < langs.length; i++)
				{
					String [] langArray = langs[i].toArray();
					if (equalsToDepth(nodeArray.length, nodeArray,
							langArray))
					{
						//String [] child = subArray(nodeArray.length + 1, langArray);
						UniversalLanguage child = langs[i];
						set.add(child);
					}
				}
			}
            else if (parentElement instanceof String)
            {
                for (int i = 0; i < langs.length; i++)
                {
                    if (langs[i].getLanguageCode().equals(parentElement.toString()))
                        set.add(langs[i]);
                }
            }
            //else System.out.println(parentElement.getClass());
			return set.toArray();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
		 */
		public Object getParent(Object element) {
            if (element instanceof UniversalLanguage)
            {
                UniversalLanguage ul = (UniversalLanguage)element;
                return new String[] { ul.getLanguageCode() };
            }
			if (element instanceof String[])
			{
				String [] child = (String[])element;
				String [] parent = subArray(child.length - 1, child);
				return parent;
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
		 */
		public boolean hasChildren(Object element) 
		{
			if (element instanceof String[])
			{
				String [] nodeArray = (String[])element;
                if (nodeArray.length == 1) return true;
//				for (int i = 0; i < langs.length; i++)
//				{
//					if (equalsToDepth(nodeArray.length, nodeArray,
//							langs[i].toArray())) return true;
//				}
			}
            //else System.out.println(element.getClass());
			return false;
		}
		
		protected boolean equalsToDepth(int depth, String[] a, String [] b)
		{
			if (a.length < depth || b.length < depth) return false;
			for (int i = 0; i < depth; i++)
			{
				if (a[i].equals(b[i]) == false) return false;
			}
			return true;
		}
		
		protected String[] subArray(int depth, String[] a)
		{
            if (depth < 0) return null;
			String [] subArray = new String[depth];
			if (a.length < depth) return null;
			for (int i = 0; i < depth; i++)
			{
				subArray[i] = a[i];
			}
			return subArray;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) 
        {
			if (inputElement instanceof String[])
            {
                return (Object[])inputElement;
            }
            if (inputElement instanceof UniversalLanguage [])
            {
                UniversalLanguage [] uls = (UniversalLanguage[])inputElement;
                if (useTree == false)
                {
                    return uls;
                }
                HashSet<String> langSet = new HashSet<String>(uls.length);
                
                for (int i = 0; i < uls.length; i++)
                {
                    if (!langSet.contains(uls[i].getLanguageCode()))
                        langSet.add(uls[i].getLanguageCode());
                }
                String [] elements = new String[langSet.size()];
                Iterator<String> ils = langSet.iterator();
                int i = 0;
                while (ils.hasNext()) elements[i++] = ils.next().toString();
                return elements;
            }
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() 
        {
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
        {
			
		}
		
	}
    /** Modifier for the language cells. This modifies the entries associated
     * with a specific UniversalLanguage.
     * @author keith
     *
     */
    public class LanguageCellModifier implements ICellModifier, 
        ICellEditorValidator
    {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
         */
        public boolean canModify(Object element, String property)
        {
            if (element instanceof UniversalLanguage)
            {
                if (property.equals(IN_USE) ||
                    property.equals(LANG_FONT) ||
                    property.equals(FONT_SIZE))
                    return true;
            }
            return false;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
         */
        public Object getValue(Object element, String property)
        {
            UniversalLanguage ul = null;
            if (element instanceof TableItem)
            {
                TableItem ti = ((TableItem)element);
                if (ti.getData() instanceof UniversalLanguage)
                {
                    ul = (UniversalLanguage)ti.getData();
                }
            }
            else if (element instanceof UniversalLanguage)
            {
                ul = (UniversalLanguage)element;
            }
            if (ul != null)
            {
                if (property.equals(IN_USE))
                    return (inUseTypes.containsKey(ul.getCode()))? Boolean.TRUE : Boolean.FALSE;
                if (property.equals(LANG_FONT))
                    return labelProvider.getColumnText(ul, LANG_FONT_COL);
                if (property.equals(FONT_SIZE))
                    return labelProvider.getColumnText(ul, FONT_SIZE_COL);
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
         */
        public void modify(Object element, String property, Object value)
        {
            if (element instanceof TreeItem)
            {
                TreeItem ti = (TreeItem)element;
                if (ti.getData() instanceof UniversalLanguage)
                {
                    
                    UniversalLanguage ul = (UniversalLanguage)ti.getData();
                    LangType lt = availableTypes.get(ul.getCode());
                    
                    if (property.equals(LANG_FONT))
                    {
                        if (!lt.getFont().equals(value))
                        {
                            lt.setFont(value.toString());
                            if (inUseTypes.containsKey(ul.getCode()))
                            {
                                lt = (LangType)inUseTypes.get(ul.getCode());
                                lt.setFont(value.toString());
                            }
                            setDirty(true);
                            tableViewer.refresh(ul, true);
                        }
                    }
                    else if (property.equals(FONT_SIZE))
                    {
                        try
                        {
                            float size = Float.parseFloat(value.toString());
                            BigDecimal bdSize = BigDecimal.valueOf(size);
                            if (!lt.getFontSize().equals(bdSize))
                            {
                                lt.setFontSize(bdSize);
                                if (inUseTypes.containsKey(ul.getCode()))
                                {
                                    lt = (LangType)inUseTypes.get(ul.getCode());
                                    lt.setFontSize(bdSize);
                                }
                                setDirty(true);
                                tableViewer.refresh(ul, true);
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            LanguageTestPlugin.log(IStatus.WARNING,
                                    e.getLocalizedMessage(),e);
                        }
                    }
                    else if (property.equals(IN_USE))
                    {
                        if (((Boolean)value).booleanValue())
                        {
                            if (!inUseTypes.containsKey(ul.getCode()))
                            {
                                LangType lang = 
                                    availableTypes.get(ul.getCode());
                                inUseTypes.put(ul.getCode(), lang.copy());
                            }
                        }
                        else
                        {
                            inUseTypes.remove(ul.getCode());
                        }
                        setDirty(true);
                        tableViewer.refresh(ul, true);
                    }
                }
            } 
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
         */
        public String isValid(Object value)
        {
            // null means valid
            return null;
        }
        
    }
    public boolean isDirty() { return dirty; }
    protected void setDirty(boolean dirtyNow)
    {
        if (dirtyNow)
        {
            Iterator<ModifyListener> i = modifyListeners.iterator();
            while (i.hasNext())
            {
                ModifyListener ml = i.next();
                Event e = new Event();
                e.widget = tableViewer.getTree();
                
                ModifyEvent me = new ModifyEvent(e);
                
                ml.modifyText(me);
            }
            
        }
        dirty = dirtyNow;
    }
    
}
