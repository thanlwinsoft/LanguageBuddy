/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/editors/PictureCellEditor.java $
 *  Revision        $LastChangedRevision: 852 $
 *  Last Modified:  $LastChangedDate: 2007-06-09 16:02:23 +0700 (Sat, 09 Jun 2007) $
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

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
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
		if (value != null)
		{
		    Label label = this.getDefaultLabel(); 
		    label.setText(value.toString());
		    label.setToolTipText(value.toString());
		}
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
