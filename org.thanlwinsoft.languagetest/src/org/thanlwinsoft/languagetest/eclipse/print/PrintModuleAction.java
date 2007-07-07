package org.thanlwinsoft.languagetest.eclipse.print;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.editors.TestModuleEditor;
import org.thanlwinsoft.languagetest.eclipse.workspace.WorkspaceLanguageManager;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.thanlwinsoft.schemas.languagetest.module.LangTypeType;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;

public class PrintModuleAction extends Action 
{
	Shell shell = null;
	@Override
	public void run()
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (shell == null)
			shell = workbench.getActiveWorkbenchWindow().getShell();
		final PrintDialog pd = new PrintDialog(shell);
		final PrinterData data = pd.open();
		if (data != null)
		{
			try
			{
				IEditorPart editor = workbench.getActiveWorkbenchWindow()
					.getActivePage().getActiveEditor();
				if (editor == null || ! (editor instanceof TestModuleEditor))
				{
					return;
				}
				IEditorInput input = editor.getEditorInput();
				LanguageModuleDocument doc = null;
				
				if (!(input instanceof IFileEditorInput)) return;
				IFileEditorInput fei = (IFileEditorInput)input; 
				File f = fei.getFile().getRawLocation().toFile();
				doc = LanguageModuleDocument.Factory.parse(f);
				if (doc == null || doc.getLanguageModule() == null) return;
				final String title = fei.getFile().getFullPath().removeFileExtension().lastSegment();
				final TestItemType [] items = 
					doc.getLanguageModule().getTestItemArray();
//				IProject [] up = WorkspaceLanguageManager.findUserProjects();
//				if (up.length == 0) return;
//				final LangType [] nLangs = 
//					WorkspaceLanguageManager.findUserLanguages(LangTypeType.NATIVE);
//				final LangType [] fLangs = 
//					WorkspaceLanguageManager.findUserLanguages(LangTypeType.FOREIGN);
				
				LangType [] langs = doc.getLanguageModule().getLangArray();
				LanguagePairDialog lp = new LanguagePairDialog(shell, langs);
				if (lp.needDialog())
				{
					lp.open();
				}
				final LangType nLang = lp.getNativeLang();
				final LangType fLang = lp.getForeignLang();

				IRunnableWithProgress runnable = new IRunnableWithProgress()
				{
	
					@Override
					public void run(IProgressMonitor monitor)
							throws InvocationTargetException, InterruptedException
					{
						PrintTestItems printModule = new PrintTestItems(data, 
								title, items, nLang, fLang);
						printModule.doPrint(monitor);
					}
					
				};
				PlatformUI.getWorkbench().getProgressService().run(true, true, runnable);
			} 
			catch (InvocationTargetException e)
			{
				LanguageTestPlugin.log(IStatus.WARNING,"Error printing",e);
			} 
			catch (InterruptedException e)
			{
				LanguageTestPlugin.log(IStatus.WARNING,"Error printing",e);
			} 
			catch (XmlException e)
			{
				LanguageTestPlugin.log(IStatus.WARNING,"Error printing",e);
			} 
			catch (IOException e)
			{
				LanguageTestPlugin.log(IStatus.WARNING,"Error printing",e);
			}
			finally
			{
				
			}
		}
	}

	@Override
	public void runWithEvent(Event event)
	{
        shell = event.display.getActiveShell();
        run();
	}
	
}
