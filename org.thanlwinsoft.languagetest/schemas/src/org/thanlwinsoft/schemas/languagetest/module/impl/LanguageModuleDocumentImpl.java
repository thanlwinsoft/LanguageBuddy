/*
 * An XML document type.
 * Localname: LanguageModule
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/module
 * Java type: org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.module.impl;
/**
 * A document containing one LanguageModule(@http://www.thanlwinsoft.org/schemas/languagetest/module) element.
 *
 * This is a complex type.
 */
public class LanguageModuleDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.thanlwinsoft.schemas.languagetest.module.LanguageModuleDocument
{
    
    public LanguageModuleDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName LANGUAGEMODULE$0 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "LanguageModule");
    
    
    /**
     * Gets the "LanguageModule" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType getLanguageModule()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType)get_store().find_element_user(LANGUAGEMODULE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "LanguageModule" element
     */
    public void setLanguageModule(org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType languageModule)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType)get_store().find_element_user(LANGUAGEMODULE$0, 0);
            if (target == null)
            {
                target = (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType)get_store().add_element_user(LANGUAGEMODULE$0);
            }
            target.set(languageModule);
        }
    }
    
    /**
     * Appends and returns a new empty "LanguageModule" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType addNewLanguageModule()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType)get_store().add_element_user(LANGUAGEMODULE$0);
            return target;
        }
    }
}
