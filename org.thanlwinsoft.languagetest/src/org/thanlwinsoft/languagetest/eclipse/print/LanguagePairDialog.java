package org.thanlwinsoft.languagetest.eclipse.print;

import java.util.LinkedHashMap;
import java.util.Map;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.thanlwinsoft.schemas.languagetest.module.LangTypeType;

public class LanguagePairDialog extends MessageDialog
{
	Map<String,LangType> nativeLang;
	Map<String,LangType> foreignLang;
	Combo nativeCombo = null;
	Combo foreignCombo = null;
	String nativeId = null;
	String foreignId = null;
	
	static final String[] dialogButtonLabels = new String[]
	{
		MessageUtil.getString("OK"), 
		MessageUtil.getString("Cancel")
	};
	public LanguagePairDialog(Shell parentShell, LangType [] langs)
	{
		super(parentShell, MessageUtil.getString("ChooseLanguages"), 
				LanguageTestPlugin.getImageDescriptor("/icons/FlagOutline32.png").createImage(parentShell.getDisplay()), 
				MessageUtil.getString("ChooseLanguagesMsg"),
				MessageDialog.QUESTION, dialogButtonLabels, 0);
		nativeLang = new LinkedHashMap<String,LangType>();
		foreignLang = new LinkedHashMap<String,LangType>();
		for (LangType l : langs)
		{
			UniversalLanguage ul = new UniversalLanguage(l.getLang());
			if (l.getType().equals(LangTypeType.NATIVE))
			{
				nativeLang.put(ul.getDescription(), (LangType)l.copy());
			}
			if (l.getType().equals(LangTypeType.FOREIGN))
			{
				foreignLang.put(ul.getDescription(), (LangType)l.copy());
			}
		}
	}
	@Override
	protected Control createCustomArea(Composite parent)
	{
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayout(new GridLayout());
		nativeCombo = new Combo(panel, SWT.DROP_DOWN);
		nativeCombo.setItems(nativeLang.keySet().toArray(new String[nativeLang.size()]));
		foreignCombo = new Combo(panel, SWT.DROP_DOWN);
		foreignCombo.setItems(foreignLang.keySet().toArray(new String[foreignLang.size()]));
		nativeCombo.select(0);
		if (nativeLang.size() == 1)
		{
			nativeCombo.setEnabled(false);
		}
		nativeCombo.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				nativeId = nativeCombo.getItem(nativeCombo.getSelectionIndex());
				
			}});
		foreignCombo.select(0);
		if (foreignLang.size() == 1)
		{
			foreignCombo.setEnabled(false);
		}
		foreignCombo.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				foreignId = foreignCombo.getItem(foreignCombo.getSelectionIndex());
				
			}});
		nativeId = nativeLang.keySet().iterator().next();
		foreignId = foreignLang.keySet().iterator().next();
		return panel;
	}
	
	public LangType getNativeLang()
	{
		return nativeLang.get(nativeId);
	}
	public LangType getForeignLang()
	{
		return foreignLang.get(foreignId);
	}
	public boolean needDialog()
	{
		if (nativeLang.size() > 1 || foreignLang.size() > 1)
			return true;
		return false;
	}
}
