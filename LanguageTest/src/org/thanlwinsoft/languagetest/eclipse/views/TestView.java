/**
 * 
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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.Perspective;
import org.thanlwinsoft.languagetest.language.test.Test;
import org.thanlwinsoft.languagetest.language.test.TestHistory;
import org.thanlwinsoft.languagetest.language.test.TestHistoryStorageException;
import org.thanlwinsoft.languagetest.language.test.TestItem;
import org.thanlwinsoft.languagetest.language.test.TestManager;
import org.thanlwinsoft.languagetest.language.test.TestType;
import org.thanlwinsoft.schemas.languagetest.ForeignLangType;
import org.thanlwinsoft.schemas.languagetest.LangType;
import org.thanlwinsoft.schemas.languagetest.LangTypeType;
import org.thanlwinsoft.schemas.languagetest.LanguageModuleType;
import org.thanlwinsoft.schemas.languagetest.NativeLangType;
import org.thanlwinsoft.schemas.languagetest.TestItemType;

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
    private final static int [] NO_PICTURE_WEIGHTS = new int [] {99,1};
    private final static float MIN_WEIGHT = 0.1f;
    private int [] pictureWeights = new int [] {50,50};
    private ImageData imageData = null;
    private Test test = null;  //  @jve:decl-index=0:
    private TestItem currentItem = null;  //  @jve:decl-index=0:
    private TestManager manager = null;
    public TestView()
    {
        selectionProviders = new HashSet();
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent)
    {
        Group mainControl  = new Group(parent, SWT.SHADOW_ETCHED_IN);
        // horizontally: phraseForm | picture | controlPanel
        horizontalSash = new SashForm(mainControl, SWT.HORIZONTAL);
        
        SashForm phraseForm = new SashForm(horizontalSash, SWT.VERTICAL);
        
        FormLayout mainLayout = new FormLayout();
        mainControl.setLayout(mainLayout);
//      control panel should not be resized
        controlPanel = new TestControlPanel(this, mainControl, SWT.NONE);
        
        // layout data
        FormData controlFD = new FormData();
        controlFD.top = new FormAttachment(0,0);
        //controlFD.bottom = new FormAttachment(0,0);
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

			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void controlResized(ControlEvent e) {
				// TODO Auto-generated method stub
				setPicture();
			}
        	
        });
        //picture.setText(MessageUtil.getString("No picture"));
        // phrase viewers top: native, bottom: foreign
        Group nativeGroup = new Group(phraseForm, SWT.SHADOW_ETCHED_IN);
        nativeGroup.setLayout(new FillLayout());
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
        Group foreignGroup = new Group(phraseForm, SWT.SHADOW_ETCHED_IN);
        foreignGroup.setLayout(new FillLayout());
        foreignViewer = new TextViewer(foreignGroup, SWT.WRAP | 
                SWT.H_SCROLL | SWT.V_SCROLL);
        foreignViewer.getTextWidget().setAlignment(SWT.CENTER);
        foreignDoc = new Document();
        foreignViewer.setDocument(foreignDoc);
        foreignViewer.setEditable(false);
        
        phraseForm.setWeights(new int[]{1,1});
        horizontalSash.setWeights(NO_PICTURE_WEIGHTS);
        
        
        
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
        viewer.setDocument(doc);
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
    	if (picture.getSize().x < id.height)
    	{
    		id = imageData.scaledTo(picture.getSize().x, 
    				(int)Math.round((float)picture.getSize().x / ratio));
    	}
    	if (picture.getSize().y < id.width)
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
        if (event.getSource() instanceof TableViewer)
        {
            show(NATIVE_ID | FOREIGN_ID);
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
            }
        }
    }
    
    public void setTestItem(TestItemType ti)
    {
        if (ti == null) return;
        try
        {
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
            // cache old weights before we remove the picture
            if (picture.getImage() != null)
            {
                int [] weights = horizontalSash.getWeights();
                if (weights[1] > MIN_WEIGHT * weights[0])
                    pictureWeights = weights;
            }
            if (ti.isSetImg() && ti.getImg() != null)
            {
                
                IEditorInput editorInput = getSite().getPage().getActiveEditor().getEditorInput();
                if (editorInput instanceof FileEditorInput)
                {
                    FileEditorInput fei = (FileEditorInput)editorInput;
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
            nativeViewer.refresh();
            foreignViewer.refresh();
            nativeViewer.getTextWidget().redraw();
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
        }
        // shouldn't happen
        else throw new IllegalArgumentException("Unknown test type:" + test);
        
        controlPanel.setStatusVisible(true);
        currentItem = test.getNextItem();
        setTestItem(currentItem);
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
            setTestItem(test.getNextItem());
            return;
        }
        setText(nativeViewer, nativeDoc, "", null);
        setText(foreignViewer, foreignDoc, "", null);
        show(SHOW_BOTH);
        
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
        
        test = null;
        controlPanel.setTestControlVisible(false);
        controlPanel.setFlipControlVisible(false);
        controlPanel.setStatusVisible(false);
    }
    protected void setTestItem(TestItem ti)
    {
        if (ti == null) 
        {
            testFinished();
            return;
        }
        Display display = getSite().getShell().getDisplay();
        nativeFont = null;
        if (ti.getNativeFontData() != null)
            nativeFont = LanguageTestPlugin.getFont(ti.getNativeFontData());
        foreignFont = null;
        if (ti.getForeignFontData() != null)
            foreignFont = LanguageTestPlugin.getFont(ti.getForeignFontData());
        
        setText(nativeViewer, nativeDoc, ti.getNativeText(), nativeFont);
        setText(foreignViewer, foreignDoc, ti.getForeignText(), foreignFont);
        
        // TODO picture and sound 
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
        showAnswer(false);
        currentItem = test.getNextItem();
        setTestItem(currentItem);
    }
    public void showAnswer(boolean showAns)
    {
        if (showAns)
        {
            show(SHOW_BOTH);
        }
        else 
        {
            show(promptView);
        }
    }
}
