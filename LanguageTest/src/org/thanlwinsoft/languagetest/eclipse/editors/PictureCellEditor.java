/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.editors;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.thanlwinsoft.languagetest.MessageUtil;

/**
 * @author keith
 *
 */
public class PictureCellEditor extends DialogCellEditor {

	static final String [] GRAPHIC_EXT = { "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp" };
	IFile moduleFile = null;
	public PictureCellEditor(Composite parent, int styles, IFile file)
	{
		super(parent, styles);
		this.moduleFile = file;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) 
	{
		FileDialog dialog = new FileDialog(cellEditorWindow.getShell(), SWT.OPEN);
		dialog.setText(MessageUtil.getString("PictureFileDialogTitle"));
		dialog.setFilterExtensions(GRAPHIC_EXT);
        if (getDefaultLabel().getText().length() > 0)
        {
            IPath path = new Path(getDefaultLabel().getText());
            dialog.setFilterPath(path.removeLastSegments(1).toOSString());
            dialog.setFileName(path.lastSegment());
        }
        else if (moduleFile != null)
		{
			IPath parent = moduleFile.getRawLocation().removeLastSegments(1);
            dialog.setFilterPath(parent.toOSString() + File.separator + "*.*");
		}
        
		String filePath = dialog.open(); 
		if (filePath != null)
		{
			this.getDefaultLabel().setText(filePath);
			this.getDefaultLabel().setToolTipText(filePath);
		}
        else filePath = "";
		return filePath;
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
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.DialogCellEditor#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite cell)
    {
        return super.createContents(cell);
    }
	
	
}
