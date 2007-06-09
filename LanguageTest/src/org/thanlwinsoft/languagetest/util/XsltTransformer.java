/*
 *  Copyright (C) 2004 Keith Stribley <tech@thanlwinsoft.org>
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
 */
package org.thanlwinsoft.languagetest.util;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import java.io.File;
import java.io.IOException;
/**
 *
 * @author  keith
 */
public class XsltTransformer
{
    private Transformer transformer = null;
    
    /** Creates new form XsltTransformer */
    public XsltTransformer()
    {

    }
    

    

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        if (args.length<3)
        {
            System.out.println("Usage: xsl htmlDir xml1 [xml2] [xml3] ....");
            System.exit(1);
        }
        
        XsltTransformer instance = new XsltTransformer();
        File xsltFile = new File(args[0]);
        File htmlDir = new File(args[1]);
        if (instance.createTransformer(xsltFile))
        {
            System.out.println(args[0] + " Loaded");
            for (int f = 2; f<args.length; f++)
            {
                System.out.println("Transforming " + args[f]);
                File xmlFile = new File(args[f]);
                instance.generateInDir(xmlFile, htmlDir);
            }
        }
        System.exit(0);
        //instance.show();
    }
    
    public boolean createTransformer(File xsltFile)
    {
        // Create a transform factory instance.
        TransformerFactory tfactory = TransformerFactory.newInstance();
        // Create a transformer from the stylesheets
        try 
        {
            transformer
                  = tfactory.newTransformer(new StreamSource(xsltFile.getAbsolutePath()));
        }
        catch (TransformerConfigurationException tce)
        {
            System.out.println(tce.toString());
            System.out.println(tce.getLocationAsString());
            return false;
        }
        return true;
    }
    
    public boolean createTransformer(java.io.InputStream xsltFile)
    {
        // Create a transform factory instance.
        TransformerFactory tfactory = TransformerFactory.newInstance();
        // Create a transformer from the stylesheets
        try 
        {
            transformer
                  = tfactory.newTransformer(new StreamSource(xsltFile));
        }
        catch (TransformerConfigurationException tce)
        {
            System.out.println(tce.toString());
            System.out.println(tce.getLocationAsString());
            return false;
        }
        return true;
    }
    
    public File htmlFileFromXml(File xmlFile, File dir)
    {
        return fileFromXml(xmlFile, dir, ".html");
    }
    
    public File fileFromXml(File xmlFile, File dir, String ext)
    {
        File htmlDir = dir;
        // if dir is invalid default to same as xml file
        if (htmlDir == null || !htmlDir.isDirectory()) 
        {
            htmlDir = xmlFile.getParentFile();
        }
        int extIndex = xmlFile.getName().indexOf(".xml");
        String htmlFileName = null;
        if (extIndex > -1)
        {
            htmlFileName = xmlFile.getName().substring(0, extIndex) + ext;
        }
        else
        {
            htmlFileName = xmlFile.getName() + ext;
        }
        return new File(htmlDir, htmlFileName);
    }
    
    
    
    public void generateInDir(File xmlFile, File htmlDir)
    {
        generateToFile(xmlFile, htmlFileFromXml(xmlFile, htmlDir));
    }
    
    public void generateToFile(org.w3c.dom.Document doc, File htmlFile)
    {
        if (doc != null)
        {
            DOMSource domSource = new DOMSource(doc);  
            generateHtml(domSource, htmlFile);
        }
    }
    
    public void generateToFile(File xmlFile, File htmlFile)
    {
        if (xmlFile.exists()==true)
        {
            org.w3c.dom.Document doc = null;
            try
            {
                DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
                doc = docBuilder.parse(new InputSource(xmlFile.getAbsolutePath()));
            }
            catch (ParserConfigurationException pce)
            {
                System.out.println(pce.toString());
            }
            catch (SAXException se)
            {
                System.out.println(se.toString());
            }
            catch (IOException ioe)
            {
                System.out.println(ioe.toString());
            }
            generateToFile(doc, htmlFile);
        }
    }
    
    
    protected boolean generateHtml(DOMSource domSource,File htmlFile)
    {
        boolean ok = true;
        try
        {
            System.out.println(htmlFile.getAbsolutePath());
            // Transform the source XML to System.out.
            transformer.transform( domSource,
                                   new StreamResult(htmlFile));
        }
        catch (TransformerException te)
        {
            System.out.println(te.toString());
            //JOptionPane.showMessageDialog(album,te,"Transformation error using " + xslFile,
            //                        JOptionPane.ERROR_MESSAGE);
            ok = false;
        }
        return ok;
    }


}
