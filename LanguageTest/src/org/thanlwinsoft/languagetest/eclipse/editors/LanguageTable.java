/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.editors;

import java.util.HashMap;
import java.util.HashSet;
import java.math.BigDecimal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreePathLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeColumn;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.LangType;

/**
 * @author keith
 *
 */
public class LanguageTable extends Composite 
{
	private final static int LANG_DEPTH = 5;
	private TreeViewer tableViewer = null;
    private HashMap availableTypes = null;
    private HashMap inUseTypes = null;
    private final int LANG_NAME_COL = 0;
    private final int IN_USE_COL = 1;
    private final int LANG_FONT_COL = 2;
    private final int FONT_SIZE_COL = 3;
    private final String LANG_NAME = "Lang";
    private final String IN_USE = "InUse";
    private final String LANG_FONT = "Font";
    private final String FONT_SIZE = "FontSize";
    
    private LanguageLabelProvider labelProvider = null;
    private LanguageContentProvider contentProvider = null;
    private UniversalLanguage [] languages = null;
    
	/**
	 * @param parent
	 * @param style
	 */
	public LanguageTable(Composite parent, int style) 
	{
		super(parent, style);
        this.setLayout(new FillLayout());
        availableTypes = new HashMap();
        inUseTypes = new HashMap();
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
                null, null, new FontCellEditor(tableViewer.getTree(), SWT.NONE),
                new TextCellEditor(tableViewer.getTree())
        };
        tableViewer.setCellEditors(editors);
	}
    
    public void setProjectLangs(LangType [] langs)
    {
        languages = new UniversalLanguage[langs.length];
        availableTypes.clear();
        for (int i = 0; i < langs.length; i++)
        {
            availableTypes.put(langs[i].getLang(), langs[i]);
        }
        tableViewer.setInput(languages);
        tableViewer.getTree().setData(languages);
        tableViewer.refresh();
    }
    public void setProjectLangs(HashMap langs)
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
    
    public void setModuleLangs(LangType [] langs)
    {
        inUseTypes.clear();
        for (int i = 0; i < langs.length; i++)
        {
            inUseTypes.put(langs[i].getLang(), langs[i]);
        }
        tableViewer.expandAll();
        tableViewer.refresh();
    }
	
    public void setModuleLangs(HashMap langs)
    {
        inUseTypes.clear();
        inUseTypes = langs;
        tableViewer.expandAll();
        tableViewer.refresh();
    }
    
	protected class LanguageLabelProvider extends LabelProvider 
        implements ITableLabelProvider 
        
	{

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex)
        {
            // TODO Auto-generated method stub
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
            if (ul != null)
            {
                LangType lt = (LangType)availableTypes.get(ul.getCode());
                switch (columnIndex)
                {
                case LANG_NAME_COL:
                    return ul.getDescription();
                case IN_USE_COL:
                    if (inUseTypes.containsKey(ul.getCode()))
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
	
	public class LanguageContentProvider implements ITreeContentProvider
	{
		private UniversalLanguage [] langs = null;
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
			HashSet set = new HashSet();
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
            else System.out.println(parentElement.getClass());
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
            else System.out.println(element.getClass());
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
		public Object[] getElements(Object inputElement) {
			// TODO Auto-generated method stub
            if (inputElement instanceof String[])
            {
                return (Object[])inputElement;
            }
            if (inputElement instanceof UniversalLanguage [])
            {
                UniversalLanguage [] uls = (UniversalLanguage[])inputElement;
                
                Object [] elements = new Object[uls.length];
                for (int i = 0; i < uls.length; i++)
                    elements[i] = new String[] { uls[i].getLanguageCode() };//toArray();
                return elements;
            }
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
        {
			
		}
		
	}
    
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
                if (/*property.equals(IN_USE) ||*/
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
                TableItem ti = (TableItem)element;
                if (ti.getData() instanceof UniversalLanguage)
                {
                    if (property.equals(LANG_FONT))
                        return labelProvider.getColumnText(ti.getData(), LANG_FONT_COL);
                    if (property.equals(FONT_SIZE))
                        return labelProvider.getColumnText(ti.getData(), FONT_SIZE_COL);
                }
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
         */
        public void modify(Object element, String property, Object value)
        {
            if (element instanceof TableItem)
            {
                TableItem ti = (TableItem)element;
                if (ti.getData() instanceof UniversalLanguage)
                {
                    
                    UniversalLanguage ul = (UniversalLanguage)ti.getData();
                    LangType lt = (LangType)availableTypes.get(ul.getCode());
                    if (property.equals(LANG_FONT))
                    {
                        lt.setFont(value.toString());
                    }
                    else if (property.equals(FONT_SIZE))
                    {
                        try
                        {
                            float size = Float.parseFloat(value.toString());
                            lt.setFontSize(BigDecimal.valueOf(size));
                        }
                        catch (NumberFormatException e)
                        {
                            LanguageTestPlugin.log(IStatus.WARNING,
                                    e.getLocalizedMessage(),e);
                        }
                    }
                }
            } 
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
         */
        public String isValid(Object value)
        {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
}
