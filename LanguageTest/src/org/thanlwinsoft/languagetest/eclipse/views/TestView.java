/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/LanguageTest/src/org/thanlwinsoft/languagetest/eclipse/views/TestView.java $
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
package org.thanlwinsoft.languagetest.eclipse.views;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;
import org.thanlwinsoft.eclipse.widgets.SoundPlayer;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.Perspective;
import org.thanlwinsoft.languagetest.eclipse.editors.TestItemEditor;
import org.thanlwinsoft.languagetest.eclipse.editors.TestModuleEditor;
import org.thanlwinsoft.languagetest.language.test.Test;
import org.thanlwinsoft.languagetest.language.test.TestHistory;
import org.thanlwinsoft.languagetest.language.test.TestHistoryStorageException;
import org.thanlwinsoft.languagetest.language.test.TestItem;
import org.thanlwinsoft.languagetest.language.test.TestManager;
import org.thanlwinsoft.languagetest.language.test.TestType;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.module.ForeignLangType;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.thanlwinsoft.schemas.languagetest.module.LangTypeType;
import org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType;
import org.thanlwinsoft.schemas.languagetest.module.NativeLangType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;

/**
 * @author keith
 *
 */
public class TestView extends ViewPart implements ISelectionChangedListener
{
    public final static String ID = "org.thanlwinsoft.languagetest.TestView";
    private TextViewer nativeViewer = null;
    private TextViewer foreignViewer = null;
    private Document nativeDoc = null;
    private Document foreignDoc = null;
    private Group nativeGroup = null;
    private Group foreignGroup = null;
    private Label picture = null;
    private TestControlPanel controlPanel = null;
    private SashForm horizontalSash = null;
    private Action copyAction = null;
    public final static int HIDE_BOTH = 0;
    public final static int NATIVE_ID = 1;
    public final static int FOREIGN_ID = 2;
    public final static int SHOW_BOTH = NATIVE_ID | FOREIGN_ID;
    private int promptView = 0;
    private Font nativeFont = null;  //  @jve:decl-index=0:
    private Font foreignFont = null;
    private HashSet selectionProviders = null;
    public final static int [] EQUAL_WEIGHT = new int [] { 50, 50};
    private final static int [] NO_PICTURE_WEIGHTS = new int [] {99,1};
    private final static float MIN_WEIGHT = 0.1f;
    private int [] pictureWeights = new int [] {50,50};
    private ImageData imageData = null;
    private Test test = null;  //  @jve:decl-index=0:
    private TestItem currentItem = null;  //  @jve:decl-index=0:
    private TestManager manager = null;
    public final static String FLIP_PERIOD_PREF = "FlipPeriod";
    public final static String FLIP_REPEAT_PREF = "FlipRepeat";
    private SashForm phraseForm = null;
    private boolean centering = false;
    
    public TestView()
    {
        selectionProviders = new HashSet();
        LanguageTestPlugin.getPrefStore().setDefault(FLIP_PERIOD_PREF, 10000);
        LanguageTestPlugin.getPrefStore().setDefault(FLIP_REPEAT_PREF, 5);
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent)
    {
        Group mainControl  = new Group(parent, SWT.SHADOW_ETCHED_IN);
        // horizontally: phraseForm | picture | controlPanel
        horizontalSash = new SashForm(mainControl, SWT.HORIZONTAL | SWT.SMOOTH);
        
        phraseForm = new SashForm(horizontalSash, SWT.VERTICAL | SWT.SMOOTH);
        phraseForm.setLayout(new FillLayout());
        FormLayout mainLayout = new FormLayout();
        mainControl.setLayout(mainLayout);
//      control panel should not be resized
        controlPanel = new TestControlPanel(this, mainControl, SWT.NONE);
        // layout data
        FormData controlFD = new FormData();
        controlFD.top = new FormAttachment(0,0);
        controlFD.bottom = new FormAttachment(100,0);
        controlFD.right = new FormAttachment(100,0);
        controlPanel.setLayoutData(controlFD);
        FormData horizontalFD = new FormData();
        horizontalFD.top = new FormAttachment(0,0);
        horizontalFD.bottom = new FormAttachment(100,0);
        horizontalFD.left = new FormAttachment(0,0);
        horizontalFD.right = new FormAttachment(controlPanel);
        horizontalSash.setLayoutData(horizontalFD);
        
        
        picture = new Label(horizontalSash, SWT.CENTER | SWT.WRAP);
        picture.addControlListener(new ControlListener(){

			public void controlMoved(ControlEvent e) {}

			public void controlResized(ControlEvent e) {
				setPicture();
                //centerViewer(nativeViewer);
                //centerViewer(foreignViewer);
			}
        	
        });
        //picture.setText(MessageUtil.getString("No picture"));
        // phrase viewers top: native, bottom: foreign
        nativeGroup = new Group(phraseForm, SWT.SHADOW_ETCHED_IN);
        nativeGroup.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
//        RowLayout layout = new RowLayout(SWT.VERTICAL);
//        layout.fill = true;
//        layout.justify = true;
        //nativeGroup.setLayout(layout);
        nativeGroup.setLayout(new FillLayout());
        //nativeSpacer = new Composite(nativeGroup, SWT.NONE);
        nativeViewer = new TextViewer(nativeGroup, SWT.WRAP | SWT.H_SCROLL 
                | SWT.V_SCROLL);
        nativeViewer.getTextWidget().setAlignment(SWT.CENTER);
        nativeDoc = new Document();
        nativeViewer.setDocument(nativeDoc);
        nativeViewer.setEditable(false);
        nativeViewer.getTextWidget().setVisible(true);
        
        //Color color = getViewSite().getShell().getDisplay()
        //    .getSystemColor(SWT.COLOR_DARK_BLUE);
        //nativeViewer.getTextWidget().setBackground(color);
        
        //Composite foreignComposite = new Composite(phraseForm, SWT.NONE);
        //foreignComposite.setLayout(new FillLayout());
        foreignGroup = new Group(phraseForm, SWT.SHADOW_ETCHED_IN);
        foreignGroup.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
//        layout = new RowLayout(SWT.VERTICAL);
//        layout.fill = true;
//        layout.justify = true;
        //foreignGroup.setLayout(layout);
        foreignGroup.setLayout(new FillLayout());
        //foreignSpacer = new Composite(foreignGroup, SWT.NONE);
        foreignViewer = new TextViewer(foreignGroup, SWT.WRAP | 
                SWT.H_SCROLL | SWT.V_SCROLL);
        foreignViewer.getTextWidget().setAlignment(SWT.CENTER);
        foreignDoc = new Document();
        foreignViewer.setDocument(foreignDoc);
        foreignViewer.setEditable(false);
        
        phraseForm.setWeights(new int[]{1,1});
        horizontalSash.setWeights(NO_PICTURE_WEIGHTS);
        
        nativeGroup.addControlListener(new ControlListener() {
            public void controlMoved(ControlEvent e) {
                centerViewer(nativeViewer);
            }
            public void controlResized(ControlEvent e) {
                centerViewer(nativeViewer);
            }
        });
        foreignGroup.addControlListener(new ControlListener() {
            public void controlMoved(ControlEvent e) {
                centerViewer(foreignViewer);
            }
            public void controlResized(ControlEvent e) {
                centerViewer(foreignViewer);
            }
        });
        
        makeActions();
        getViewSite().getActionBars().setGlobalActionHandler(
                ActionFactory.COPY.getId(),
                copyAction);
        
        IEditorPart editor = getSite().getPage().getActiveEditor();
        if (editor instanceof TestModuleEditor)
        {
            TestModuleEditor tme = (TestModuleEditor)editor;
            TestItemEditor tie = (TestItemEditor) tme.getAdapter(TestItemEditor.class);
            addSelectionProvider(tie);
        }
        
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
        // compute the new margin before setting the text, 
        // otherwise the new margin value is ignored
        centerViewer(viewer, text, viewer.getTextWidget().getFont());
        viewer.setDocument(doc);
    }
    
    protected void centerViewer(TextViewer viewer)
    {
        centerViewer(viewer, viewer.getTextWidget().getText(), viewer.getTextWidget().getFont());
    }
    
    protected void centerViewer(TextViewer viewer, String text, Font font)
    {
        if (centering) return;
        centering = true;
        FillLayout layout = (FillLayout)viewer.getTextWidget().getParent().getLayout();
        StyledTextContent tc = viewer.getTextWidget().getContent();
        if (tc.getCharCount() > 0)
        {
            //RowLayout layout = (RowLayout)viewer.getTextWidget().getParent().getLayout();
            viewer.getTextWidget().layout();
            //Rectangle textRect = viewer.getTextWidget().getTextBounds(0, tc.getCharCount() - 1);
            Group group = (Group)viewer.getTextWidget().getParent();
            Rectangle availableRect = group.getClientArea();
            int dx = 0;
            int dy = 0;
            if (viewer.getTextWidget().getVerticalBar() != null)
                dx = viewer.getTextWidget().getVerticalBar().getSize().x;
            if (viewer.getTextWidget().getHorizontalBar() != null)
                dy = viewer.getTextWidget().getHorizontalBar().getSize().y;
            
            TextLayout textLayout = new TextLayout(viewer.getTextWidget().getDisplay());
            textLayout.setWidth(availableRect.width - dx);
            textLayout.setFont(font);
            textLayout.setText(text);
            Rectangle textRect = textLayout.getBounds(0, text.length());

//            System.out.println("Phrase form " + phraseForm.getClientArea());
//            System.out.println("Group " + group.getClientArea());
//            System.out.println("Textrect " + textRect + textLayout.getText());

            //Rectangle textBounds = viewer.getTextWidget().getClientArea();
            //int groupBorder = group.getBounds().width - group.getClientArea().width;
            if (textRect.height < availableRect.height)
            {
                layout.marginHeight = (availableRect.height - textRect.height - dy) / 2;
//                System.out.println("available " + availableRect.height + " " + 
//                                textRect.height + " margin " + layout.marginHeight);
                // work around for layout not updating immediately - move the 
                // sash a bit
                int [] weights = horizontalSash.getWeights();
                if (weights.length == 2 && weights[0] > 0 && weights[1] > 0)
                {
                    weights[0]++;
                    weights[1]--;
                    horizontalSash.setWeights(weights);
                    weights[0]--;
                    weights[1]++;
                    horizontalSash.setWeights(weights);
                }
            }
            else
            {
                layout.marginHeight = 0;
            }
            
        }
        else
        {
            layout.marginHeight = 0;
        }
        //System.out.println(" margin " + layout.marginHeight);
        controlPanel.redraw();
        centering = false;
    }
    
    public void hide(int type)
    {
        switch (type)
        {
        case HIDE_BOTH:
            nativeViewer.getTextWidget().setVisible(false);
            foreignViewer.getTextWidget().setVisible(false);
            break;
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
        case HIDE_BOTH:
            nativeViewer.getTextWidget().setVisible(false);
            foreignViewer.getTextWidget().setVisible(false);
            break;
        case NATIVE_ID:
            nativeViewer.getTextWidget().setVisible(true);
            foreignViewer.getTextWidget().setVisible(false);
            break;
        case FOREIGN_ID:
            foreignViewer.getTextWidget().setVisible(true);
            nativeViewer.getTextWidget().setVisible(false);
            break;
        case (NATIVE_ID | FOREIGN_ID):
            nativeViewer.getTextWidget().setVisible(true);
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
    protected void setPicture()
    {
    	if (imageData == null) 
        {
            picture.setImage(null);
            return;
        }
    	ImageData id = imageData;
    	float ratio = ((float)imageData.width) /((float)imageData.height);
    	if (picture.getSize().x < id.width)
    	{
    		id = imageData.scaledTo(picture.getSize().x, 
    				(int)Math.round((float)picture.getSize().x / ratio));
    	}
    	if (picture.getSize().y < id.height)
    	{
    		
    		id = imageData.scaledTo((int)Math.round((float)picture.getSize().y * ratio), 
    				                picture.getSize().y);
    	}
    	Display display = getSite().getShell().getDisplay();
    	Image image = new Image(display, id);
        picture.setImage(image);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event)
    {
        if (phraseForm.isDisposed()) return;
        if (event.getSource() instanceof TableViewer)
        {
            show(NATIVE_ID | FOREIGN_ID);
            IEditorPart editor = 
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            TestModuleEditor tme = (TestModuleEditor)editor.getAdapter(TestModuleEditor.class);
            if (tme != null && tme.getDocument() != null)
            {
                setTestModule(tme.getDocument().getLanguageModule());
            }
        }
        //System.out.println(event.getSource().getClass().getName());
        if (event.getSelection() instanceof StructuredSelection)
        {
            StructuredSelection ss = (StructuredSelection)event.getSelection();
            if (ss.getFirstElement() instanceof TestItemType)
            {
                TestItemType ti = (TestItemType)ss.getFirstElement();
                setTestItem(ti);
            }
        }
    }
    /**
     * A safe way to retreive a font and not worry about disposal
     * @param faceName
     * @param size
     * @return
     */
    protected Font getFont(String faceName, int size)
    {
        FontData fd = new FontData(faceName, size, SWT.NORMAL);
        return LanguageTestPlugin.getFont(fd);
    }
    
    public void setTestModule(LanguageModuleType module)
    {
        if (module == null)
            return;
        LangType [] langs = module.getLangArray();
        for (int i = 0; i < langs.length; i++)
        {
            int fontSize = langs[i].getFontSize().intValue();
            if (langs[i].getType().equals(LangTypeType.NATIVE))
            {
                if (nativeFont == null ||
                    nativeFont.getFontData()[0].getName()
                        .equals(langs[i].getFont()) == false ||
                    nativeFont.getFontData()[0].getHeight() != fontSize)
                {
                    nativeFont = getFont(langs[i].getFont(), fontSize);
                    if (nativeFont != null)
                        nativeViewer.getTextWidget().setFont(nativeFont);
                }
                UniversalLanguage ul = new UniversalLanguage(langs[i].getLang());
                nativeGroup.setText(ul.getDescription());
            }
            else if (langs[i].getType().equals(LangTypeType.FOREIGN))
            {
                if (foreignFont == null ||
                        foreignFont.getFontData()[0].getName()
                        .equals(langs[i].getFont()) == false ||
                        foreignFont.getFontData()[0].getHeight() != fontSize)
                {
                    foreignFont = getFont(langs[i].getFont(), fontSize);
                    if (foreignFont != null)
                        foreignViewer.getTextWidget().setFont(foreignFont);
                }
                UniversalLanguage ul = new UniversalLanguage(langs[i].getLang());
                foreignGroup.setText(ul.getDescription());
            }
        }
    }
    
    private void savePictureWeights()
    {
//      cache old weights before we remove the picture
        if (picture.getImage() != null)
        {
            int [] weights = horizontalSash.getWeights();
            if (weights[1] > MIN_WEIGHT * weights[0])
                pictureWeights = weights;
        }
    }
    
    public void setTestItem(TestItemType ti)
    {
        if (ti == null) return;
        try
        {
            savePictureWeights();
            if (ti.isSetImg() && ti.getImg() != null)
            {
                IEditorPart editor = getSite().getPage().getActiveEditor();
                if (editor != null && editor.getEditorInput() != null &&
                    editor.getEditorInput() instanceof FileEditorInput)
                {
                    FileEditorInput fei = (FileEditorInput)editor.getEditorInput();
                    IContainer basePath = fei.getFile().getParent();
                    ImageLoader loader = new ImageLoader();
                    try
                    {
                        IFile imgFile = null;
                        try
                        {
    	                    if (basePath instanceof IFolder)
    	                    {
    	                        imgFile = ((IFolder)basePath).getFile(ti.getImg());
    	                    }
    	                    else if (basePath instanceof IProject)
    	                    {
    	                        imgFile = ((IProject)basePath).getFile(ti.getImg());
    	                    }
                        }
                        catch (IllegalArgumentException e) {}
                        ImageData [] imageDatas = null;
                        if (imgFile != null && imgFile.exists())
                        {
                            imageDatas = loader.load(imgFile.getRawLocation().toOSString());
                            
                        }
                        else
                        {
                            //picture.setText(MessageUtil.getString("FileNotFound",
                            //        imgFile.toString()));
                        	File file = new File(ti.getImg());
                        	if (file.exists())
                        		imageDatas = loader.load(ti.getImg());
                        }
                        if (imageDatas != null)
                        {
                            horizontalSash.setWeights(pictureWeights);
                            //Image image = new Image(display, imageData[0]);
                        	imageData = imageDatas[0];
                            setPicture();
                        }
                        else
                        {
                            picture.setImage(null);
                        }
                        if (nativeViewer.getTextWidget().isVisible())
                        	picture.setToolTipText(ti.getImg());
                        else
                        	picture.setToolTipText("");
                    }
                    catch (SWTException e)
                    {
                    	picture.setImage(null);
                    	picture.setToolTipText(e.getLocalizedMessage());
                    	horizontalSash.setWeights(NO_PICTURE_WEIGHTS);
                        LanguageTestPlugin.log(IStatus.WARNING, 
                                e.getLocalizedMessage(), e);
                    }
                }
            }
            else
            {
            	picture.setImage(null);
            	picture.setToolTipText("");
            	horizontalSash.setWeights(NO_PICTURE_WEIGHTS);
            }
            
            NativeLangType [] nLang = ti.getNativeLangArray();
            if (nLang.length > 0)
            {
                setText(NATIVE_ID, nLang[0].getStringValue(), null);
            }
            else
            {
                setText(NATIVE_ID, "", null);
            }
            ForeignLangType [] fLang = ti.getForeignLangArray();
            if (fLang.length > 0)
            {
                setText(FOREIGN_ID, fLang[0].getStringValue(), null);
            }
            else
            {
                setText(FOREIGN_ID, "", null);
            }
            if (ti.isSetSoundFile())
                controlPanel.player().setFile(ti.getSoundFile().getStringValue());
            else
                controlPanel.player().setFile(null);
            
            nativeViewer.refresh();
            foreignViewer.refresh();
            //nativeViewer.getTextWidget().redraw();
            picture.redraw();
        }
        catch (org.apache.xmlbeans.XmlRuntimeException xmlE)
        {
            LanguageTestPlugin.log(IStatus.WARNING, 
                    xmlE.getLocalizedMessage(), xmlE);
        }
    }
    public void addSelectionProvider(ISelectionProvider provider)
    {
        provider.addSelectionChangedListener(this);
        selectionProviders.add(provider);
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    public void dispose()
    {
        Iterator i = selectionProviders.iterator();
        while (i.hasNext())
        {
            ISelectionProvider p = (ISelectionProvider)i.next();
            if (p != null)
                p.removeSelectionChangedListener(this);
        }
        super.dispose();
    }
    /**
     * @param test
     */
    public void startTest(TestManager manager, Test test)
    {
        this.manager = manager;
        this.test = test;
        if (test.getType().equals(TestType.LISTENING_FOREIGN_NATIVE))
        {
            promptView = HIDE_BOTH;
            showAnswer(false);
            controlPanel.setTestControlVisible(true);
        }
        else if (test.getType().equals(TestType.READING_FOREIGN_NATIVE))
        {
            promptView = FOREIGN_ID;
            showAnswer(false);
            controlPanel.setTestControlVisible(true);
        }
        else if (test.getType().equals(TestType.READING_NATIVE_FOREIGN))
        {
            promptView = NATIVE_ID;
            showAnswer(false);
            controlPanel.setTestControlVisible(true);
        }
        else if (test.getType().equals(TestType.FLIP_CARD))
        {
            promptView = NATIVE_ID | FOREIGN_ID;
            showAnswer(true);
            controlPanel.setFlipControlVisible(true);
            flipOnTime();
        }
        // shouldn't happen
        else throw new IllegalArgumentException("Unknown test type:" + test);
        UniversalLanguage ul = new UniversalLanguage(manager.getNativeLang());
        nativeGroup.setText(ul.getDescription());
        ul = new UniversalLanguage(manager.getForeignLang());
        foreignGroup.setText(ul.getDescription());
        controlPanel.setStatusVisible(true);
        controlPanel.setStatus(MessageUtil.getString("TestStatus",
                        Integer.toString(test.getPassCount()),
                        Integer.toString(test.getNumTests()),
                        Integer.toString(test.getNumRetests())));
        currentItem = test.getNextItem();
        setTestItem(currentItem);
    }
    protected void flipOnTime()
    {
        final int maxShowCount = LanguageTestPlugin.getPrefStore().getInt(FLIP_REPEAT_PREF);
        
        Runnable runnable = new Runnable() {
            public void run()
            {
                if (controlPanel.isDisposed()) return;
                if (controlPanel.isTestPaused())
                {
                    flipOnTime();
                }
                else
                {
                    test.setPassStatus(false);// increments test count
                    if (currentItem.getTestCount() >= maxShowCount)
                    {
                        test.removeCurrentItem();
                    }
                    currentItem = test.getNextItem();
                    if (currentItem == null)
                    {
                        // finished
                        controlPanel.setStatusVisible(false);
                        controlPanel.setFlipControlVisible(false);
                        restoreView();
                    }
                    else
                    {
                        setTestItem(currentItem);
                        StringBuilder sb = new StringBuilder();
                        sb.append(MessageUtil.getString("FlipCardCount", 
                                        Integer.toString(test.getNumTests())));
                        sb.append("\r\n");
                        sb.append(MessageUtil.getString("FlipCardTotalShows", 
                                  Integer.toString(test.getTestCount())));
                        sb.append("\r\n");
                        sb.append(MessageUtil.getString("FlipCardThisCardShowsOfTotal", 
                                  Integer.toString(currentItem.getTestCount()),
                                  Integer.toString(maxShowCount)));
                        controlPanel.setStatus(sb.toString());
                        flipOnTime();
                    }
                }
            }
        };
        int milliseconds = LanguageTestPlugin.getPrefStore().getInt(FLIP_PERIOD_PREF);
        
        controlPanel.getDisplay().timerExec(milliseconds, runnable);
    }
    protected void testFinished()
    {
        float percent = (float)(test.getNumFirstTimePasses()) / 
                        (float)test.getNumTests();
        NumberFormat nf = new DecimalFormat("0%");
        if (test.getNumRetests() == 0)
        {
            MessageDialog.openInformation(getSite().getShell(), 
                    MessageUtil.getString("TestFinishedTitle"), 
                    MessageUtil.getString("TestFinishedMessage",
                            Integer.toString(test.getNumFirstTimePasses()),
                            Integer.toString(test.getNumTests()),
                            nf.format(percent)));
        }
        else if (MessageDialog.openConfirm(this.getSite().getShell(), 
                MessageUtil.getString("RetestConfirmTitle"), 
                MessageUtil.getString("RetestConfirmMessage",
                        Integer.toString(test.getNumFirstTimePasses()),
                        Integer.toString(test.getNumTests()),
                        nf.format(percent),
                        Integer.toString(test.getNumTests() - test.getNumFirstTimePasses())
                        )))
        {
            test.retestUnknown();
            setTestStatus();
            setTestItem(test.getNextItem());
            return;
        }
        setText(nativeViewer, nativeDoc, "", null);
        setText(foreignViewer, foreignDoc, "", null);
        show(SHOW_BOTH);
        
        restoreView();
        
        test = null;
        controlPanel.setTestControlVisible(false);
        controlPanel.setFlipControlVisible(false);
        controlPanel.setStatusVisible(false);
    }
    protected void restoreView()
    {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow(); 
        IWorkbenchPage page = window.getActivePage(); 
        try
        {
            page.showView(Perspective.TEST_VIEW);
            IViewReference viewRef = (IViewReference)page.getReference(this);
            if (viewRef != null && page.isPageZoomed() == true)
            {
                // should we add a page listener to watch for when the zoom is lost?
                page.toggleZoom(viewRef);
            }
        } 
        catch (PartInitException e)
        {
            e.printStackTrace();
        }
    }
    protected void setTestItem(TestItem ti)
    {
        if (ti == null) 
        {
            testFinished();
            return;
        }
        savePictureWeights();
        
        nativeFont = null;
        if (ti.getNativeFontData() != null)
            nativeFont = LanguageTestPlugin.getFont(ti.getNativeFontData());
        foreignFont = null;
        if (ti.getForeignFontData() != null)
            foreignFont = LanguageTestPlugin.getFont(ti.getForeignFontData());
        
        setText(nativeViewer, nativeDoc, ti.getNativeText(), nativeFont);
        setText(foreignViewer, foreignDoc, ti.getForeignText(), foreignFont);

        
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IFile moduleFile = workspace.getRoot().getFile(ti.getModulePath());
        
        if (ti.getSoundFile() != null)
        {
            if (ti.getSoundFile().toFile().exists())
            {
                controlPanel.player().setFile(ti.getSoundFile().toOSString());
            }
            else if (moduleFile.exists() && 
                     moduleFile.getParent().getFile(ti.getSoundFile()).exists())
            {
                IFile soundFile = moduleFile.getParent().getFile(ti.getSoundFile());
                controlPanel.player().setFile(soundFile.getRawLocation().toOSString());
            }
            else controlPanel.player().setFile(null);
        }
        else
        {
            controlPanel.player().setFile(null);
        }
        
        if (ti.getImagePath() != null)
        {
            ImageLoader loader = new ImageLoader();
            ImageData [] imageDatae = null;
            if (ti.getImagePath().toFile().exists())
            {
                imageDatae = loader.load(ti.getImagePath().toOSString());
            }
            else if (moduleFile.exists())
            {
                IFile file = moduleFile.getParent().getFile(ti.getImagePath()); 
                if (file.exists())
                {
                    imageDatae = loader.load(file.getRawLocation().toOSString());
                }
            }
            if (imageDatae != null && imageDatae.length > 0)
            {
                imageData = imageDatae[0];
                if (foreignViewer.getTextWidget().isVisible())
                    horizontalSash.setWeights(pictureWeights);
            }
            else
            {
                horizontalSash.setWeights(NO_PICTURE_WEIGHTS);
            }
        }
        else 
        {
            horizontalSash.setWeights(NO_PICTURE_WEIGHTS);
            imageData = null;
        }
        setPicture();

        centerViewer(nativeViewer);
        centerViewer(foreignViewer);
    }
    public void markTest(boolean pass)
    {
        test.setPassStatus(pass);
        if (test.isRetest() == false && currentItem.getTestCount() == 1)
        {
            TestHistory history = 
                manager.getTestHistory(Integer.toHexString(currentItem.getModuleId()),
                    currentItem.getModuleCreationTime(), currentItem.getModulePath());
            
            try
            {
                if (history == null)
                {
                    MessageDialog.openWarning(this.getSite().getShell(),
                                MessageUtil.getString("SaveHistoryFailTitle"),
                                MessageUtil.getString("SaveHistoryFailMessage", ""));
                }
                else
                {
                    history.saveResult(currentItem, test.getType(), 
                                   new Date().getTime(), pass);
                }
            }
            catch (TestHistoryStorageException e)
            {
                MessageDialog.openWarning(this.getSite().getShell(),
                        MessageUtil.getString("SaveHistoryFailTitle"),
                        MessageUtil.getString("SaveHistoryFailMessage", e.getLocalizedMessage()));
                LanguageTestPlugin.log(IStatus.WARNING, 
                        e.getLocalizedMessage(), e);
            }
        }
        setTestStatus();
        showAnswer(false);
        currentItem = test.getNextItem();
        // hack to get refresh to work
        phraseForm.setWeights(new int [] { 45, 55});
        phraseForm.setWeights(EQUAL_WEIGHT);
        setTestItem(currentItem);
    }
    /**
     * Set the test status text in the status box.
     *
     */
    protected void setTestStatus()
    {
        controlPanel.setStatus(MessageUtil.getString("TestStatus",
                Integer.toString(test.getPassCount()),
                Integer.toString(test.getNumTests()),
                Integer.toString(test.getNumRetests())));
    }
    public void showAnswer(boolean showAns)
    {
        if (showAns)
        {
            show(SHOW_BOTH);
            if (imageData != null) horizontalSash.setWeights(pictureWeights);
        }
        else 
        {
            show(promptView);
            if (promptView != NATIVE_ID || imageData == null)
            {
                horizontalSash.setWeights(NO_PICTURE_WEIGHTS);
            }
        }
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter)
    {
        if (adapter == this.getClass()) return this;
        if (adapter == SoundPlayer.class)
        {
            return controlPanel.player();
        }
        return super.getAdapter(adapter);
    }
}
