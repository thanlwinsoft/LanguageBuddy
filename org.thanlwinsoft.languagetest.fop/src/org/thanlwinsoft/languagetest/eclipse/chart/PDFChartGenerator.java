/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
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

package org.thanlwinsoft.languagetest.eclipse.chart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.fop.svg.PDFTranscoder;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;

public class PDFChartGenerator extends SVGChartGenerator
{
    public PDFChartGenerator()
    {
        super("pdf", MessageUtil.getString("File_PDF"));
    }
    
    protected void exportSvg(String svgFileName, String fileName)
    {
    	if (fileName.endsWith(".pdf"))
            exportSvgToPDF(svgFileName, fileName);
    	else super.exportSvg(svgFileName, fileName);
    }
    
    private void exportSvgToPDF(String svnFileName, String fileName)
    {
        try
        {
            Bundle batikBundle = Platform.getBundle("org.apache.fop");
            batikBundle.loadClass("org.apache.fop.svg.PDFTranscoder");
            batikBundle.loadClass("org.apache.batik.transcoder.TranscoderInput");
            batikBundle.loadClass("org.apache.batik.transcoder.TranscoderOutput");
            // Create a PNG transcoder
            PDFTranscoder t = new PDFTranscoder();// throws an exception
            
            // Create the transcoder input.
            String svgURI = new File(svnFileName).toURI().toString();
            
            TranscoderInput input = new TranscoderInput(svgURI);
            
            // Create the transcoder output.
            OutputStream ostream;
        
            ostream = new FileOutputStream(fileName);
            TranscoderOutput output = new TranscoderOutput(ostream);
            // preload class
            batikBundle.loadClass("org.apache.batik.transcoder.TranscoderInput");
            // Save the image.
            t.transcode(input, output);

            // Flush and close the stream.
            ostream.flush();
            ostream.close();
        } 
        catch (FileNotFoundException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        } 
        catch (IOException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        } 
        catch (TranscoderException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        }
        catch (ClassNotFoundException e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        }
        catch (Throwable e)
        {
            LanguageTestPlugin.log(IStatus.WARNING, "ChartGenerator error", e);
        }
        
    }

}
