/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.schemas.languagetest.ForeignLangType;
import org.thanlwinsoft.schemas.languagetest.LangType;
import org.thanlwinsoft.schemas.languagetest.LangTypeType;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleType;
import org.thanlwinsoft.schemas.languagetest.NativeLangType;
import org.thanlwinsoft.schemas.languagetest.TestItemType;
import org.w3c.dom.Node;

/**
 * @author keith
 *
 */
public class TestView extends ViewPart implements ISelectionChangedListener
{
    private TextViewer nativeViewer = null;
    private TextViewer foreignViewer = null;
    private Document nativeDoc = null;
    private Document foreignDoc = null;
    private Label picture = null;
    private TestControlPanel controlPanel = null;
    private Action copyAction = null;
    public final static int NATIVE_ID = 0;
    public final static int FOREIGN_ID = 1;
    private Font nativeFont = null;
    private Font foreignFont = null;
    public TestView()
    {
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent)
    {
        Group mainControl  = new Group(parent, SWT.SHADOW_ETCHED_IN);
        // horizontally: phraseForm | picture | controlPanel
        SashForm horizontalSash = new SashForm(mainControl, SWT.HORIZONTAL);
        
        SashForm phraseForm = new SashForm(horizontalSash, SWT.VERTICAL);
        picture = new Label(horizontalSash, SWT.CENTER);
        
        // phrase viewers top: native, bottom: foreign
        nativeViewer = new TextViewer(phraseForm, SWT.CENTER | SWT.WRAP | SWT.H_SCROLL 
                | SWT.V_SCROLL);
        nativeDoc = new Document();
        nativeViewer.setDocument(nativeDoc);
        nativeViewer.setEditable(false);
        
        foreignViewer = new TextViewer(phraseForm, SWT.CENTER | SWT.WRAP | 
                SWT.H_SCROLL | SWT.V_SCROLL);
        foreignDoc = new Document();
        foreignViewer.setDocument(foreignDoc);
        foreignViewer.setEditable(false);
        
        phraseForm.setWeights(new int[]{1,1});
        horizontalSash.setWeights(new int[]{99,1});
        // control panel should not be resized
        controlPanel = new TestControlPanel(mainControl, SWT.NONE);
        
        FormLayout mainLayout = new FormLayout();
        mainControl.setLayout(mainLayout);

        // layout data
        FormData controlFD = new FormData();
        controlFD.top = new FormAttachment(0,0);
        //controlFD.bottom = new FormAttachment(0,0);
        controlFD.right = new FormAttachment(100,0);
        controlPanel.setLayoutData(controlFD);
        FormData horizontalFD = new FormData();
        horizontalFD.top = new FormAttachment(0,0);
        horizontalFD.bottom = new FormAttachment(100,0);
        //horizontalFD.left = new FormAttachment(0,0);
        horizontalFD.right = new FormAttachment(controlPanel);
        horizontalSash.setLayoutData(horizontalFD);
        
        
        makeActions();
        getViewSite().getActionBars().setGlobalActionHandler(
                ActionFactory.COPY.getId(),
                copyAction);
    }
    
    /**
     * 
     */
    private void makeActions()
    {
        copyAction = new Action() {
            public void run()
            {
                nativeViewer.getTextWidget().copy();
            }
        };
        copyAction.setText(MessageUtil.getString("copy.text"));
        copyAction.setToolTipText(MessageUtil.getString("copy.tooltip"));
        copyAction.setEnabled(false);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus()
    {
        // TODO Auto-generated method stub
        
    }
    
    public void setText(int type, String text, Font font)
    {
        if (type == NATIVE_ID)
            setText(nativeViewer, nativeDoc, text, font);
        else if (type == FOREIGN_ID)
            setText(foreignViewer, foreignDoc, text, font);
        else
            throw new IllegalArgumentException("Unknown type " + type);
    }
    
    protected void setText(TextViewer viewer, Document doc, String text, Font font)
    {
        if (font != null) viewer.getTextWidget().setFont(font);
        doc.set(text);
    }
    public void hide(int type)
    {
        switch (type)
        {
        case NATIVE_ID:
            nativeViewer.getTextWidget().setVisible(false);
            break;
        case FOREIGN_ID:
            foreignViewer.getTextWidget().setVisible(false);
            break;
        default:
            throw new IllegalArgumentException("Unknown type " + type);
        }
    }
    public void show(int type)
    {
        switch (type)
        {
        case NATIVE_ID:
            nativeViewer.getTextWidget().setVisible(true);
            break;
        case FOREIGN_ID:
            foreignViewer.getTextWidget().setVisible(true);
            break;
        default:
            throw new IllegalArgumentException("Unknown type " + type);
        }
    }
    public void setPicture(Image image)
    {
        picture.setImage(image);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event)
    {
        // TODO Auto-generated method stub
        if (event.getSource() instanceof TestItemType)
        {
            TestItemType ti = (TestItemType)event.getSource();
        }
    }
    protected Font getFont(String faceName, int size)
    {
        Display display = getSite().getShell().getDisplay();
        return new Font(display, faceName, size, SWT.NORMAL);
    }
    public void setTestModule(LanguageModuleType module)
    {
        LangType [] langs = module.getLangArray();
        for (int i = 0; i < langs.length; i++)
        {
            if (langs[i].getType().equals(LangTypeType.NATIVE))
            {
                if (nativeFont.getFontData()[0].getName()
                        .equals(langs[i].getFont()) == false)
                {
                    if (nativeFont != null) nativeFont.dispose();
                    nativeFont = getFont(langs[i].getFont(), 
                            langs[i].getFontSize().intValue());
                    nativeViewer.getTextWidget().setFont(nativeFont);
                }
                    
            }
            else if (langs[i].getType().equals(LangTypeType.FOREIGN))
            {
                if (foreignFont.getFontData()[0].getName()
                        .equals(langs[i].getFont()) == false)
                {
                    if (foreignFont != null) foreignFont.dispose();
                    foreignFont = getFont(langs[i].getFont(), langs[i].getFontSize().intValue());
                    foreignViewer.getTextWidget().setFont(foreignFont);
                }
            }
        }
    }
    
    public void setTestItem(TestItemType ti)
    {
        NativeLangType [] nLang = ti.getNativeLangArray();
        setText(NATIVE_ID, nLang[0].getStringValue(), null);
        ForeignLangType [] fLang = ti.getForeignLangArray();
        setText(FOREIGN_ID, fLang[0].getStringValue(), null);
        if (ti.isSetSoundFile())
            controlPanel.player().setFile(ti.getSoundFile().getStringValue());
        else
            controlPanel.player().setFile(null);
        picture.setImage(null);
        if (ti.isSetImg())
        {
            Display display = getSite().getShell().getDisplay();
            
            ImageLoader loader = new ImageLoader();
            ImageData [] imageData = loader.load(ti.getImg());
            if (imageData != null)
            {
                Image image = new Image(display, imageData[0]);
                picture.setImage(image);
            }
        }
            
    }
}
