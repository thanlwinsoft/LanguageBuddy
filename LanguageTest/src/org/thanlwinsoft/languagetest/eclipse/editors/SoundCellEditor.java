/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.editors;

import org.eclipse.core.resources.IFile;
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
public class SoundCellEditor extends DialogCellEditor {

	static final String [] SOUND_EXT = { "*.mp3,*.wav,*.ogg", "*.mp3", "*.wav", "*.ogg" };
	IFile moduleFile = null;
	public SoundCellEditor(Composite parent, int styles, IFile file)
	{
		super(parent, styles);
		this.moduleFile = file;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
		FileDialog dialog = new FileDialog(cellEditorWindow.getShell(), SWT.OPEN);
		dialog.setText(MessageUtil.getString("SoundFileDialogTitle"));
		dialog.setFilterExtensions(SOUND_EXT);
		if (moduleFile != null)
		{
			dialog.setFilterPath(moduleFile.getParent().getLocation() + "/*.*");
		}
		return dialog.open();
	}
	
}
