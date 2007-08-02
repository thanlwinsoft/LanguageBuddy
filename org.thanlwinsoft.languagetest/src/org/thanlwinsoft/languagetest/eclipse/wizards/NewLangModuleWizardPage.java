/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/wizards/NewLangModuleWizardPage.java $
 *  Revision        $LastChangedRevision: 936 $
 *  Last Modified:  $LastChangedDate: 2007-08-03 05:14:14 +0700 (Fri, 03 Aug 2007) $
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
package org.thanlwinsoft.languagetest.eclipse.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.thanlwinsoft.languagetest.MessageUtil;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (lm.xml).
 */

public class NewLangModuleWizardPage extends WizardPage {
	private Text containerText;

	private Text fileText;
	public final static String EXTENSION = "xml";
    public final static String XML = "xml";
	private ISelection selection;
	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NewLangModuleWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle(MessageUtil.getString("NewLangModuleTitle"));
		setDescription(MessageUtil.getString("NewLangModuleDesc"));
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText("&Container:");

		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText(MessageUtil.getString("Browse"));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText(MessageUtil.getString("FileName"));

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		int count = 1;
		final String extension = "." + EXTENSION;
		NumberFormat nf = new DecimalFormat("000");
		String fileName = MessageUtil.getString("NewLanguageModule","001") + extension;
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				containerText.setText(container.getFullPath().toString());
				if (container.exists(new Path(fileName)))
				{
					fileName = MessageUtil.getString("NewLanguageModule", 
							nf.format(++count)) + extension;
				}
			}
		}
		fileText.setText(fileName);
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		String fileName = getFileName();

		if (getContainerName().length() == 0) {
			updateStatus(MessageUtil.getString("NewLangModule_ContainerMustBeSpecified"));
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus(MessageUtil.getString("NewLangModule_ContainerMustExist"));
			return;
		}
		if (!container.isAccessible()) {
			updateStatus(MessageUtil.getString("NewLangModule_ProjectMustBeWritable"));
			return;
		}
		if (fileName.length() == 0) {
			updateStatus(MessageUtil.getString("NewLangModule_FileNameMustBeSpecified"));
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus(MessageUtil.getString("NewLangModule_FileNameMustBeValid"));
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');        
		if (dotLoc > 0) {
            int secondDot = fileName.lastIndexOf('.', dotLoc - 1);
            if (secondDot != -1)
            {
    			String ext = fileName.substring(secondDot + 1);
    			if (ext.equalsIgnoreCase(EXTENSION) == false) 
                {
    				updateStatus(MessageUtil.getString("NewLangModule_FileExtension",
                            EXTENSION));
    				return;
    			}
            }
            else
            {
                String ext = fileName.substring(dotLoc + 1);
                if (ext.equalsIgnoreCase(EXTENSION) == false) 
                {
                    updateStatus(MessageUtil.getString("NewLangModule_FileExtension",
                        EXTENSION));
                    return;
                }
            }
		}
        else
        {
            updateStatus(MessageUtil.getString("NewLangModule_FileExtension",
                    EXTENSION));
            return;
        }
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getFileName() {
		return fileText.getText();
	}
}