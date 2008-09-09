/*
 * An XML document type.
 * Localname: ModuleHistory
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/history
 * Java type: org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryDocument
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.history.impl;
/**
 * A document containing one ModuleHistory(@http://www.thanlwinsoft.org/schemas/languagetest/history) element.
 *
 * This is a complex type.
 */
public class ModuleHistoryDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryDocument
{
    
    public ModuleHistoryDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MODULEHISTORY$0 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/history", "ModuleHistory");
    
    
    /**
     * Gets the "ModuleHistory" element
     */
    public org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType getModuleHistory()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType)get_store().find_element_user(MODULEHISTORY$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ModuleHistory" element
     */
    public void setModuleHistory(org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType moduleHistory)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType)get_store().find_element_user(MODULEHISTORY$0, 0);
            if (target == null)
            {
                target = (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType)get_store().add_element_user(MODULEHISTORY$0);
            }
            target.set(moduleHistory);
        }
    }
    
    /**
     * Appends and returns a new empty "ModuleHistory" element
     */
    public org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType addNewModuleHistory()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType)get_store().add_element_user(MODULEHISTORY$0);
            return target;
        }
    }
}
