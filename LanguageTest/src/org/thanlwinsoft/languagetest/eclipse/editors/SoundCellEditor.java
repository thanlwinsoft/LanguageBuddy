/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.editors;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
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
public class SoundCellEditor extends DialogCellEditor {

	static final String [] SOUND_EXT = { /*"*.mp3|*.wav|*.ogg",*/ "*.mp3", "*.wav", "*.ogg" };
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
        if (getDefaultLabel().getText().length() > 0)
        {
            File f = new File(getDefaultLabel().getText());
            IPath path = new Path(getDefaultLabel().getText());
            if (!f.exists())
            {
                if (moduleFile != null)
                {
                    IPath parent = moduleFile.getRawLocation().removeLastSegments(1);
                    path = parent.append(path);
                }
                else
                {
                    IPath ws = ResourcesPlugin.getWorkspace().getRoot().getRawLocation();
                    path = ws.append(path);
                }
            }
            dialog.setFilterPath(path.removeLastSegments(1).toOSString());
            dialog.setFileName(path.lastSegment());
        }
        else if (moduleFile != null)
		{
            IPath parent = moduleFile.getRawLocation().removeLastSegments(1);
            dialog.setFilterPath(parent.toOSString() + File.separator + "*.*");
		}
        String path = dialog.open();
		if (path == null) path = "";
        return path;
	}
	
}
