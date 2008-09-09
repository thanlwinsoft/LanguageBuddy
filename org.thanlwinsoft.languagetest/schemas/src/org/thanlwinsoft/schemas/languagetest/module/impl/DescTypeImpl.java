/*
 * XML Type:  DescType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/module
 * Java type: org.thanlwinsoft.schemas.languagetest.module.DescType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.module.impl;
/**
 * An XML DescType(@http://www.thanlwinsoft.org/schemas/languagetest/module).
 *
 * This is an atomic type that is a restriction of org.thanlwinsoft.schemas.languagetest.module.DescType.
 */
public class DescTypeImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.thanlwinsoft.schemas.languagetest.module.DescType
{
    
    public DescTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType, true);
    }
    
    protected DescTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
    {
        super(sType, b);
    }
    
    private static final javax.xml.namespace.QName LANG$0 = 
        new javax.xml.namespace.QName("", "lang");
    
    
    /**
     * Gets the "lang" attribute
     */
    public java.lang.String getLang()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LANG$0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "lang" attribute
     */
    public org.thanlwinsoft.schemas.languagetest.module.DescType.Lang xgetLang()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.DescType.Lang target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.DescType.Lang)get_store().find_attribute_user(LANG$0);
            return target;
        }
    }
    
    /**
     * Sets the "lang" attribute
     */
    public void setLang(java.lang.String lang)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LANG$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(LANG$0);
            }
            target.setStringValue(lang);
        }
    }
    
    /**
     * Sets (as xml) the "lang" attribute
     */
    public void xsetLang(org.thanlwinsoft.schemas.languagetest.module.DescType.Lang lang)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.DescType.Lang target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.DescType.Lang)get_store().find_attribute_user(LANG$0);
            if (target == null)
            {
                target = (org.thanlwinsoft.schemas.languagetest.module.DescType.Lang)get_store().add_attribute_user(LANG$0);
            }
            target.set(lang);
        }
    }
    /**
     * An XML lang(@).
     *
     * This is an atomic type that is a restriction of org.thanlwinsoft.schemas.languagetest.module.DescType$Lang.
     */
    public static class LangImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.thanlwinsoft.schemas.languagetest.module.DescType.Lang
    {
        
        public LangImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected LangImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
