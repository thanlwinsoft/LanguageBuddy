/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/wizards/NewLanguagePage.java $
 *  Revision        $LastChangedRevision: 855 $
 *  Last Modified:  $LastChangedDate: 2007-06-10 07:02:09 +0700 (Sun, 10 Jun 2007) $
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

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.Vector;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.languagetest.language.text.Iso15924;
import org.thanlwinsoft.languagetest.language.text.Iso3166;
import org.thanlwinsoft.languagetest.language.text.Iso639;
import org.thanlwinsoft.languagetest.language.text.Iso3166.IsoCountry;
import org.thanlwinsoft.schemas.languagetest.module.LangTypeType;

/** Wizard page to define a language in terms of the main Language name,
 * Country of use, dialect if necessary, Script that is used and encoding if 
 * not Unicode.
 * @author keith
 *
 */
public class NewLanguagePage extends WizardPage implements ModifyListener, SelectionListener
{
    private Shell shell = null;
    private UniversalLanguage ul = null;
    private Label typeLabel = null;
    private Combo typeCombo = null;
    private Combo langCombo = null;
    private Combo countryCombo = null;
    private Combo scriptCombo = null;
    private Combo encodingCombo = null;
    private Text variantText = null;
    private Label ulCode = null;
    private FontData fontData = null;
    private String title = null;
    private String desc = null;
    private LangTypeType.Enum langType = null;

    public NewLanguagePage(String title, String desc, LangTypeType.Enum type) 
    {
        super(title);
        this.title = title;
        this.desc = desc;
        this.langType = type;
    }
    public NewLanguagePage()
    {
        super("NewLanguage");
        this.title = MessageUtil.getString("NewLangPageTitle");
        this.desc = MessageUtil.getString("NewLangPageDesc");
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        setTitle(title);
        setDescription(desc);
        shell = parent.getShell();
        fontData = parent.getDisplay().getSystemFont().getFontData()[0];
        final Group mainControl  = new Group(parent, SWT.SHADOW_ETCHED_IN);
        RowLayout mainLayout = new RowLayout();
        
        mainLayout.type = SWT.VERTICAL;
        mainLayout.fill = true;
        mainControl.setLayout(mainLayout);
        if (langType == null)
        {
            typeLabel = new Label(mainControl, SWT.LEFT);
            typeLabel.setText(MessageUtil.getString("LangTypeLabel"));
            typeCombo = new Combo(mainControl, SWT.DROP_DOWN | SWT.READ_ONLY);
            typeCombo.add(MessageUtil.getString("NativeLanguage"));
            typeCombo.add(MessageUtil.getString("ForeignLanguage"));
        }
        final Label instructionsLabel = new Label(mainControl, SWT.LEFT | SWT.WRAP);
        instructionsLabel.setText(MessageUtil.getString("NewLangInstructions"));
        final Label langLabel = new Label(mainControl, SWT.LEFT | SWT.WRAP);
        langLabel.setText(MessageUtil.getString("LangChoice"));
        langCombo = new Combo(mainControl, SWT.DROP_DOWN | SWT.READ_ONLY);
        Vector langCodes = Iso639.getLanguages(); 
        String [] langNames = new String[langCodes.size()];
        Iterator il = langCodes.iterator();
        int i = 0;
        while (il.hasNext()) 
            langNames[i++] = ((Iso639.IsoLanguage)il.next()).getDescription();
        langCombo.setItems(langNames);
        langCombo.addSelectionListener(this);
        
        final Label countryLabel = new Label(mainControl, SWT.LEFT);
        countryLabel.setText(MessageUtil.getString("CountryChoice"));
        countryCombo = new Combo(mainControl, SWT.DROP_DOWN | SWT.READ_ONLY);
        IsoCountry [] countries = (IsoCountry[]) Iso3166.getCountries();
        String [] countryNames = new String[countries.length];
        for (i = 0; i < countries.length; i++) 
            countryNames[i] = countries[i].getDescription();
        countryCombo.setItems(countryNames);
        countryCombo.addSelectionListener(this);
        
        final Label variantLabel = new Label(mainControl, SWT.LEFT | SWT.WRAP);
        variantLabel.setText(MessageUtil.getString("VariantChoice"));
        variantText = new Text(mainControl, SWT.LEFT);
        variantText.addModifyListener(this);
        
        final Label scriptLabel = new Label(mainControl, SWT.LEFT| SWT.WRAP);
        scriptLabel.setText(MessageUtil.getString("ScriptChoice"));
        scriptCombo = new Combo(mainControl, SWT.DROP_DOWN | SWT.READ_ONLY);
        Vector scripts = Iso15924.getScripts();
        String [] scriptNames = new String[scripts.size()];
        Iterator is = scripts.iterator();
        i = 0;
        while (is.hasNext()) 
            scriptNames[i++] = ((Iso15924.IsoScript)is.next()).getDescription();
        scriptCombo.setItems(scriptNames);
        scriptCombo.addSelectionListener(this);
        
        
        final Label encodingLabel = new Label(mainControl, SWT.LEFT | SWT.WRAP);
        encodingLabel.setText(MessageUtil.getString("EncodingChoice"));
        encodingCombo = new Combo(mainControl, SWT.DROP_DOWN);
        SortedMap charsets = Charset.availableCharsets();
        encodingCombo.setItems((String [])charsets.keySet()
                               .toArray(new String[charsets.size()]));
        encodingCombo.addSelectionListener(this);
        
        final Label fontLabel = new Label(mainControl, SWT.LEFT | SWT.WRAP);
        fontLabel.setText(MessageUtil.getString("FontChoice"));
        final Button fontButton = new Button(mainControl, SWT.NONE);
        fontButton.setText(MessageUtil.getString("Font"));
        fontButton.addSelectionListener(new SelectionListener(){
            public void widgetDefaultSelected(SelectionEvent e) {}
            public void widgetSelected(SelectionEvent e)
            {
                FontDialog dialog = new FontDialog(shell);
                dialog.setText(MessageUtil.getString("FontDialogTitle"));
                FontData [] fontList = new FontData[] { fontData };
                if (fontData != null) dialog.setFontList(fontList);
                fontData = dialog.open();
                fontButton.setToolTipText(fontData.getName());
            }
        });
        
        final Button advancedToggle = new Button(mainControl, SWT.CHECK);
        advancedToggle.setText(MessageUtil.getString("AdvancedOptions"));
        advancedToggle.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {  }
            public void widgetSelected(SelectionEvent e)
            {
                boolean visible = advancedToggle.getSelection();
                countryLabel.setVisible(visible);
                countryCombo.setVisible(visible);
                variantLabel.setVisible(visible);
                variantText.setVisible(visible);
                encodingLabel.setVisible(visible);
                encodingCombo.setVisible(visible);
            }});
        // start with advanced controls hidden
        countryLabel.setVisible(false);
        countryCombo.setVisible(false);
        variantLabel.setVisible(false);
        variantText.setVisible(false);
        encodingLabel.setVisible(false);
        encodingCombo.setVisible(false);
        setControl(mainControl);
        advancedToggle.setSelection(false);
        ulCode = new Label(mainControl, SWT.LEFT | SWT.WRAP);
    }
    /**
     * Check that the entries are valid.
     * @return validation
     */
    protected boolean validatePage()
    {
        try
        {
            String langCode = "";
            if (langCombo.getSelectionIndex() > -1)
                langCode = ((Iso639.IsoLanguage)Iso639.getLanguages()
                .elementAt(langCombo.getSelectionIndex())).getCode(); 
            IsoCountry [] countries = (IsoCountry[]) Iso3166.getCountries();
            String countryCode = "";
            if (countryCombo.getSelectionIndex() > -1)
                countryCode = countries[countryCombo.getSelectionIndex()].getCode();
            String scriptCode = "";
            if (scriptCombo.getSelectionIndex() > -1)
                scriptCode = ((Iso15924.IsoScript)Iso15924.getScripts()
                .elementAt(scriptCombo.getSelectionIndex())).getCode();
            if (langCode.length() > 0)
            {
                ul = new UniversalLanguage(langCode, countryCode, 
                                       variantText.getText(), scriptCode,
                                       getSelection(encodingCombo));
            
                ulCode.setText(ul.getCode());
                ulCode.setToolTipText(ul.getDescription());
            }
            else
            {
                ulCode.setText(MessageUtil.getString("NoLangSpecified"));
            }
        }
        catch (IllegalArgumentException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "Error validating Language Page", e);
            ul = null;
            ulCode.setText(e.getLocalizedMessage());
            ulCode.setToolTipText(e.getLocalizedMessage());
        }
        return (ul != null);
    }
    private String getSelection(Combo combo)
    {
        if (combo.getSelectionIndex() > -1)
            return combo.getItem(combo.getSelectionIndex()).toString();
        else return "";
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
     */
    public void modifyText(ModifyEvent e)
    {
        setPageComplete(validatePage());
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
        setPageComplete(validatePage());
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        setPageComplete(validatePage());
    }
    
    /** retrieve the selected UniversalLanguage or null if none is selected.
     * 
     * @return UniversalLanguage
     */
    public UniversalLanguage getUL()
    {
        return ul;
    }
    /** retrieve the selected UniversalLanguage as a text code 
     * or null if none is selected.
     * 
     * @return code
     */
    public String getLangCode()
    {
        if (ul != null)
            return ul.getCode();
        return null;
    }
    
    public String getFontName()
    {
        if (fontData != null) return fontData.getName();
        return "";
    }
    public int getFontHeight()
    {
        if (fontData != null) return fontData.getHeight();
        return 12;
    }
    public FontData getFontData()
    {
        return fontData;
    }
    public LangTypeType.Enum getLangType()
    {
        if (typeCombo != null)
        {
            if (typeCombo.getSelectionIndex() == 0)
                langType = LangTypeType.NATIVE;
            else 
                langType = LangTypeType.FOREIGN;
        }
        return langType;
    }
}
