/*
 * XML Type:  LangType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/module
 * Java type: org.thanlwinsoft.schemas.languagetest.module.LangType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.module.impl;
/**
 * An XML LangType(@http://www.thanlwinsoft.org/schemas/languagetest/module).
 *
 * This is a complex type.
 */
public class LangTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.thanlwinsoft.schemas.languagetest.module.LangType
{
    
    public LangTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DESC$0 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "Desc");
    private static final javax.xml.namespace.QName FONT$2 = 
        new javax.xml.namespace.QName("", "font");
    private static final javax.xml.namespace.QName FONTSIZE$4 = 
        new javax.xml.namespace.QName("", "fontSize");
    private static final javax.xml.namespace.QName FONTSTYLE$6 = 
        new javax.xml.namespace.QName("", "fontStyle");
    private static final javax.xml.namespace.QName LANG$8 = 
        new javax.xml.namespace.QName("", "lang");
    private static final javax.xml.namespace.QName TYPE$10 = 
        new javax.xml.namespace.QName("", "type");
    
    
    /**
     * Gets array of all "Desc" elements
     */
    public org.thanlwinsoft.schemas.languagetest.module.DescType[] getDescArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DESC$0, targetList);
            org.thanlwinsoft.schemas.languagetest.module.DescType[] result = new org.thanlwinsoft.schemas.languagetest.module.DescType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Desc" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.DescType getDescArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.DescType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.DescType)get_store().find_element_user(DESC$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Desc" element
     */
    public int sizeOfDescArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DESC$0);
        }
    }
    
    /**
     * Sets array of all "Desc" element
     */
    public void setDescArray(org.thanlwinsoft.schemas.languagetest.module.DescType[] descArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(descArray, DESC$0);
        }
    }
    
    /**
     * Sets ith "Desc" element
     */
    public void setDescArray(int i, org.thanlwinsoft.schemas.languagetest.module.DescType desc)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.DescType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.DescType)get_store().find_element_user(DESC$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(desc);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Desc" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.DescType insertNewDesc(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.DescType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.DescType)get_store().insert_element_user(DESC$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Desc" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.DescType addNewDesc()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.DescType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.DescType)get_store().add_element_user(DESC$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Desc" element
     */
    public void removeDesc(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DESC$0, i);
        }
    }
    
    /**
     * Gets the "font" attribute
     */
    public java.lang.String getFont()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FONT$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "font" attribute
     */
    public org.apache.xmlbeans.XmlString xgetFont()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FONT$2);
            return target;
        }
    }
    
    /**
     * True if has "font" attribute
     */
    public boolean isSetFont()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(FONT$2) != null;
        }
    }
    
    /**
     * Sets the "font" attribute
     */
    public void setFont(java.lang.String font)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FONT$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FONT$2);
            }
            target.setStringValue(font);
        }
    }
    
    /**
     * Sets (as xml) the "font" attribute
     */
    public void xsetFont(org.apache.xmlbeans.XmlString font)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FONT$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(FONT$2);
            }
            target.set(font);
        }
    }
    
    /**
     * Unsets the "font" attribute
     */
    public void unsetFont()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(FONT$2);
        }
    }
    
    /**
     * Gets the "fontSize" attribute
     */
    public java.math.BigDecimal getFontSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FONTSIZE$4);
            if (target == null)
            {
                return null;
            }
            return target.getBigDecimalValue();
        }
    }
    
    /**
     * Gets (as xml) the "fontSize" attribute
     */
    public org.apache.xmlbeans.XmlDecimal xgetFontSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDecimal target = null;
            target = (org.apache.xmlbeans.XmlDecimal)get_store().find_attribute_user(FONTSIZE$4);
            return target;
        }
    }
    
    /**
     * True if has "fontSize" attribute
     */
    public boolean isSetFontSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(FONTSIZE$4) != null;
        }
    }
    
    /**
     * Sets the "fontSize" attribute
     */
    public void setFontSize(java.math.BigDecimal fontSize)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FONTSIZE$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FONTSIZE$4);
            }
            target.setBigDecimalValue(fontSize);
        }
    }
    
    /**
     * Sets (as xml) the "fontSize" attribute
     */
    public void xsetFontSize(org.apache.xmlbeans.XmlDecimal fontSize)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDecimal target = null;
            target = (org.apache.xmlbeans.XmlDecimal)get_store().find_attribute_user(FONTSIZE$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDecimal)get_store().add_attribute_user(FONTSIZE$4);
            }
            target.set(fontSize);
        }
    }
    
    /**
     * Unsets the "fontSize" attribute
     */
    public void unsetFontSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(FONTSIZE$4);
        }
    }
    
    /**
     * Gets the "fontStyle" attribute
     */
    public java.math.BigDecimal getFontStyle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FONTSTYLE$6);
            if (target == null)
            {
                return null;
            }
            return target.getBigDecimalValue();
        }
    }
    
    /**
     * Gets (as xml) the "fontStyle" attribute
     */
    public org.apache.xmlbeans.XmlDecimal xgetFontStyle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDecimal target = null;
            target = (org.apache.xmlbeans.XmlDecimal)get_store().find_attribute_user(FONTSTYLE$6);
            return target;
        }
    }
    
    /**
     * True if has "fontStyle" attribute
     */
    public boolean isSetFontStyle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(FONTSTYLE$6) != null;
        }
    }
    
    /**
     * Sets the "fontStyle" attribute
     */
    public void setFontStyle(java.math.BigDecimal fontStyle)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FONTSTYLE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FONTSTYLE$6);
            }
            target.setBigDecimalValue(fontStyle);
        }
    }
    
    /**
     * Sets (as xml) the "fontStyle" attribute
     */
    public void xsetFontStyle(org.apache.xmlbeans.XmlDecimal fontStyle)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDecimal target = null;
            target = (org.apache.xmlbeans.XmlDecimal)get_store().find_attribute_user(FONTSTYLE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDecimal)get_store().add_attribute_user(FONTSTYLE$6);
            }
            target.set(fontStyle);
        }
    }
    
    /**
     * Unsets the "fontStyle" attribute
     */
    public void unsetFontStyle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(FONTSTYLE$6);
        }
    }
    
    /**
     * Gets the "lang" attribute
     */
    public java.lang.String getLang()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LANG$8);
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
    public org.apache.xmlbeans.XmlString xgetLang()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(LANG$8);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LANG$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(LANG$8);
            }
            target.setStringValue(lang);
        }
    }
    
    /**
     * Sets (as xml) the "lang" attribute
     */
    public void xsetLang(org.apache.xmlbeans.XmlString lang)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(LANG$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(LANG$8);
            }
            target.set(lang);
        }
    }
    
    /**
     * Gets the "type" attribute
     */
    public org.thanlwinsoft.schemas.languagetest.module.LangTypeType.Enum getType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TYPE$10);
            if (target == null)
            {
                return null;
            }
            return (org.thanlwinsoft.schemas.languagetest.module.LangTypeType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "type" attribute
     */
    public org.thanlwinsoft.schemas.languagetest.module.LangTypeType xgetType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.LangTypeType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.LangTypeType)get_store().find_attribute_user(TYPE$10);
            return target;
        }
    }
    
    /**
     * Sets the "type" attribute
     */
    public void setType(org.thanlwinsoft.schemas.languagetest.module.LangTypeType.Enum type)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TYPE$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TYPE$10);
            }
            target.setEnumValue(type);
        }
    }
    
    /**
     * Sets (as xml) the "type" attribute
     */
    public void xsetType(org.thanlwinsoft.schemas.languagetest.module.LangTypeType type)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.LangTypeType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.LangTypeType)get_store().find_attribute_user(TYPE$10);
            if (target == null)
            {
                target = (org.thanlwinsoft.schemas.languagetest.module.LangTypeType)get_store().add_attribute_user(TYPE$10);
            }
            target.set(type);
        }
    }
}
