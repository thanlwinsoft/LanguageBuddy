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
/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.export;

/**
 * @author keith
 *
 */
public interface ExporterProperties
{
    /**
     * Sets the path to the xslt
     * @param xsltPath
     */
    public void setXslt(String xsltPath);
    /** get xslt parameters
     * 
     * @return
     */
    public String [] getParameters();
    /**
     * get xslt values - array must be same length as getParameters()
     * @return
     */
    public String [] getValues();
    /**
     * Retreive the list of valid values for the property.
     * A null return means any value is permissible
     * @param propertyName
     * @return
     */
    public String [] parameterOptions(String propertyName);
    /**
     * Attempt to convert from source path to target path.
     * If the return value is false, a default XSLT conversion will be 
     * attempted.
     * @param source
     * @param target
     * @return true on successful conversion
     */
    public boolean convert(String source, String target, String [] properties, String [] values);
    /**
     * Toggle enable/disable status
     * When disabled the WizardPage must return true for isPageComplete()
     * @param enable
     */
    public void setEnabled(boolean enable);
}
