/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FontDialog;
import org.thanlwinsoft.languagetest.MessageUtil;

/**
 * @author keith
 *
 */
public class FontCellEditor extends DialogCellEditor {

	IFile moduleFile = null;
	public FontCellEditor(Composite parent, int styles)
	{
		super(parent, styles);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) 
	{
        FontDialog dialog = new FontDialog(cellEditorWindow.getShell());
		dialog.setText(MessageUtil.getString("FontDialogTitle"));
        if (getValue() != null)
        {
            FontData [] oldFont = new FontData[] { 
                    new FontData(getValue().toString(), 12, SWT.NORMAL)
            }; 
            dialog.setFontList(oldFont);
        }
		FontData fontData = dialog.open(); 
		
		return fontData.getName();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#doSetValue(java.lang.Object)
	 */
	protected void doSetValue(Object value) 
	{
		super.doSetValue(value);
		this.getDefaultLabel().setText(value.toString());
		this.getDefaultLabel().setToolTipText(value.toString());
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#updateContents(java.lang.Object)
	 */
	protected void updateContents(Object value) {
		super.updateContents(value);
		if (getDefaultLabel() != null && value != null)
		{
			getDefaultLabel().setText(value.toString());
			getDefaultLabel().setToolTipText(value.toString());
		}
	}
	
	
}
