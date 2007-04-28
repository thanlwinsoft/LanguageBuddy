/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * -----------------------------------------------------------------------
 */
/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.export;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.xml.sax.SAXException;

/**
 * @author keith
 *
 */
public class FopConfigPage extends WizardPage implements ExporterProperties, IExecutableExtension
{
    private String xsltPath = null;
    private boolean enabled = false;
    private FopFactory fopFactory = null;
    private Button useImagesButton = null;
    private Combo pageSizeCombo = null;
    private Combo columnCombo = null;
    private final static String [] PARAMETERS = new String [] {
        "title", "colCount", "useImage", "pageSize"
    };
    private final static String [] DEFAULTS = new String [] {
        "Language Module", "1", "1", "A4"
    };
    private String [] values = null;
    private final static String [] PAGE_SIZES = new String [] {
        "A4","A5","Letter","Legal"
    };
    /**
     * @param pageName
     */
    public FopConfigPage()
    {
        super("FOP Config");
        values = DEFAULTS;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.eclipse.export.ExporterProperties#convert(java.lang.String, java.lang.String)
     */
    public boolean convert(String source, String target)
    {
//      mustn't access any widgets because this isn't run in a display thread
        System.out.println(source + " " + target);
//      Step 1: Construct a FopFactory
//      (reuse if you plan to render multiple documents!)
        Bundle fopBundle = Platform.getBundle("org.apache.fop");
        boolean success = false;
        try
        {
            fopBundle.loadClass("org.apache.fop.apps.FopFactory");
//            fopBundle.loadClass("org.apache.xmlgraphics.util.Service");
            if (fopFactory == null)
                fopFactory = FopFactory.newInstance();
            File sourceFile = new File(source);
            String title = new Path(source).removeFileExtension().lastSegment();
            //      Step 2: Set up output stream.
            //      Note: Using BufferedOutputStream for performance reasons (helpful with FileOutputStreams).
            OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(target)));
            
            try {
               FOUserAgent userAgent = fopFactory.newFOUserAgent();
//                userAgent.setProducer("Language Test <www.thanlwinsoft.org> using FOP");
//                userAgent.setTitle(title);
                String userHome = System.getProperty("user.home");
//                File fontBase = new File(userHome + File.separator + ".fonts");
//                //userAgent.setFontBaseURL(fontBase.toURI().toURL().toExternalForm());
//                userAgent.setBaseURL(fontBase.toURI().toURL().toExternalForm());
                DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
                InputStream fopConfig = 
                    getClass().getResourceAsStream("/org/thanlwinsoft/languagetest/language/text/fop.xconf");
                //Configuration cfg = cfgBuilder.buildFromFile(new File(userHome + "/.fop/fop.xconf"));
                Configuration cfg = cfgBuilder.build(fopConfig);
                fopFactory.setUserConfig(cfg);
               // Step 3: Construct fop with desired output format

               Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, out);
               //Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            
               // Step 4: Setup JAXP using identity transformer
               TransformerFactory factory = TransformerFactory.newInstance();
               //Transformer transformer = factory.newTransformer(); // identity transformer
               Source xslt = new StreamSource(this.getClass().getResourceAsStream(xsltPath));
               Transformer transformer = factory.newTransformer(xslt);
               
               transformer.setParameter("title", title);
               transformer.setParameter("colCount", values[1]);
               transformer.setParameter("useImage", values[2]);
               transformer.setParameter("pageSize", values[3]);
               // Step 5: Setup input and output for XSLT transformation 
               // Setup input stream
               Source src = new StreamSource(sourceFile);
            
               // Resulting SAX events (the generated FO) must be piped through to FOP
               Result res = new SAXResult(fop.getDefaultHandler());
                         
               // Step 6: Start XSLT transformation and FOP processing
               transformer.transform(src, res);
               success = true;
            } 
            catch (TransformerException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, e.getMessage(), e);
                e.printStackTrace();
            } 
            catch (FOPException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, e.getMessage(), e);
                e.printStackTrace();
            } 
            catch (ConfigurationException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, e.getMessage(), e);
                e.printStackTrace();
            } 
            catch (SAXException e)
            {
                LanguageTestPlugin.log(IStatus.WARNING, e.getMessage(), e);
                e.printStackTrace();
            } 
            finally 
            {
               //Clean-up
               out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            LanguageTestPlugin.log(IStatus.WARNING, e.getMessage(), e);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LanguageTestPlugin.log(IStatus.WARNING, e.getMessage(), e);
        }
        catch (ClassNotFoundException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, e.getMessage(), e);
        }

        return success;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.eclipse.export.ExporterProperties#getParameters()
     */
    public String[] getParameters()
    {
        
        return PARAMETERS;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.eclipse.export.ExporterProperties#getValues()
     */
    public String[] getValues()
    {
        //"title", "colCount", "useImage", "pageSize"
        if (!getControl().isDisposed())
        {
            values[0] = "LanguageTest";
            values[1] = Integer.toString(columnCombo.getSelectionIndex() + 1);
            if (useImagesButton.getSelection())
                values[2] = "1";
            else
                values[2] = "0";
            values[3] = PAGE_SIZES[pageSizeCombo.getSelectionIndex()];
        }
        return values;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.eclipse.export.ExporterProperties#parameterOptions(java.lang.String)
     */
    public String[] parameterOptions(String propertyName)
    {
        if (propertyName.equals("pageSize"))
            return PAGE_SIZES;
        if (propertyName.equals("title"))
            return null;
        if (propertyName.equals("colCount"))
            return new String [] { "1","2","3" };
        if (propertyName.equals("useImage"))
            return new String [] { "1","0"};
        return null;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.eclipse.export.ExporterProperties#setEnabled(boolean)
     */
    public void setEnabled(boolean enable)
    {
        this.enabled = enable;
    }

    /* (non-Javadoc)
     * @see org.thanlwinsoft.languagetest.eclipse.export.ExporterProperties#setXslt(java.lang.String)
     */
    public void setXslt(String xsltPath)
    {
        this.xsltPath = xsltPath;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        //parent.setLayout(new FillLayout());
        setTitle(MessageUtil.getString("FopConfigTitle"));
        setDescription(MessageUtil.getString("FopConfigInstructions"));
        Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
        //GridLayout layout = new GridLayout();
        //layout.numColumns = 2;
        RowLayout layout = new RowLayout();
        layout.type = SWT.VERTICAL;
        layout.fill = true;
        group.setLayout(layout);
        Label t1 = new Label(group, SWT.LEFT | SWT.WRAP);
        t1.setText(MessageUtil.getString("PageSize"));
        t1.setToolTipText(MessageUtil.getString("PageSize"));
        pageSizeCombo = new Combo(group, SWT.DROP_DOWN);
        pageSizeCombo.setItems(PAGE_SIZES);
        pageSizeCombo.select(0);
        Label t2 = new Label(group, SWT.LEFT | SWT.WRAP);
        t2.setText(MessageUtil.getString("NumColumns"));
        columnCombo = new Combo(group, SWT.DROP_DOWN);
        columnCombo.setItems(parameterOptions("colCount"));
        columnCombo.select(0);
        useImagesButton = new Button(group, SWT.CHECK);
        useImagesButton.setText(MessageUtil.getString("ShowImages"));
        useImagesButton.setToolTipText(MessageUtil.getString("ShowImages"));
        this.setControl(group);
        group.pack();
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
     */
    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException
    {
        
    }

}
