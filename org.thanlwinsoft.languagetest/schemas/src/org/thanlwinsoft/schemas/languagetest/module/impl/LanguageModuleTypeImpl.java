/*
 * XML Type:  LanguageModuleType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/module
 * Java type: org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.module.impl;
/**
 * An XML LanguageModuleType(@http://www.thanlwinsoft.org/schemas/languagetest/module).
 *
 * This is a complex type.
 */
public class LanguageModuleTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType
{
    
    public LanguageModuleTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName LANG$0 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "Lang");
    private static final javax.xml.namespace.QName TESTITEM$2 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "TestItem");
    private static final javax.xml.namespace.QName CONFIG$4 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "Config");
    private static final javax.xml.namespace.QName CREATIONTIME$6 = 
        new javax.xml.namespace.QName("", "creationTime");
    private static final javax.xml.namespace.QName ID$8 = 
        new javax.xml.namespace.QName("", "id");
    private static final javax.xml.namespace.QName FORMATVERSION$10 = 
        new javax.xml.namespace.QName("", "formatVersion");
    
    
    /**
     * Gets array of all "Lang" elements
     */
    public org.thanlwinsoft.schemas.languagetest.module.LangType[] getLangArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(LANG$0, targetList);
            org.thanlwinsoft.schemas.languagetest.module.LangType[] result = new org.thanlwinsoft.schemas.languagetest.module.LangType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Lang" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.LangType getLangArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.LangType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.LangType)get_store().find_element_user(LANG$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Lang" element
     */
    public int sizeOfLangArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(LANG$0);
        }
    }
    
    /**
     * Sets array of all "Lang" element
     */
    public void setLangArray(org.thanlwinsoft.schemas.languagetest.module.LangType[] langArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(langArray, LANG$0);
        }
    }
    
    /**
     * Sets ith "Lang" element
     */
    public void setLangArray(int i, org.thanlwinsoft.schemas.languagetest.module.LangType lang)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.LangType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.LangType)get_store().find_element_user(LANG$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(lang);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Lang" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.LangType insertNewLang(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.LangType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.LangType)get_store().insert_element_user(LANG$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Lang" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.LangType addNewLang()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.LangType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.LangType)get_store().add_element_user(LANG$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Lang" element
     */
    public void removeLang(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(LANG$0, i);
        }
    }
    
    /**
     * Gets array of all "TestItem" elements
     */
    public org.thanlwinsoft.schemas.languagetest.module.TestItemType[] getTestItemArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(TESTITEM$2, targetList);
            org.thanlwinsoft.schemas.languagetest.module.TestItemType[] result = new org.thanlwinsoft.schemas.languagetest.module.TestItemType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "TestItem" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.TestItemType getTestItemArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.TestItemType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.TestItemType)get_store().find_element_user(TESTITEM$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "TestItem" element
     */
    public int sizeOfTestItemArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TESTITEM$2);
        }
    }
    
    /**
     * Sets array of all "TestItem" element
     */
    public void setTestItemArray(org.thanlwinsoft.schemas.languagetest.module.TestItemType[] testItemArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(testItemArray, TESTITEM$2);
        }
    }
    
    /**
     * Sets ith "TestItem" element
     */
    public void setTestItemArray(int i, org.thanlwinsoft.schemas.languagetest.module.TestItemType testItem)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.TestItemType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.TestItemType)get_store().find_element_user(TESTITEM$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(testItem);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "TestItem" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.TestItemType insertNewTestItem(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.TestItemType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.TestItemType)get_store().insert_element_user(TESTITEM$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "TestItem" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.TestItemType addNewTestItem()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.TestItemType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.TestItemType)get_store().add_element_user(TESTITEM$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "TestItem" element
     */
    public void removeTestItem(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TESTITEM$2, i);
        }
    }
    
    /**
     * Gets the "Config" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.ConfigType getConfig()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.ConfigType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.ConfigType)get_store().find_element_user(CONFIG$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Config" element
     */
    public boolean isSetConfig()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CONFIG$4) != 0;
        }
    }
    
    /**
     * Sets the "Config" element
     */
    public void setConfig(org.thanlwinsoft.schemas.languagetest.module.ConfigType config)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.ConfigType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.ConfigType)get_store().find_element_user(CONFIG$4, 0);
            if (target == null)
            {
                target = (org.thanlwinsoft.schemas.languagetest.module.ConfigType)get_store().add_element_user(CONFIG$4);
            }
            target.set(config);
        }
    }
    
    /**
     * Appends and returns a new empty "Config" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.ConfigType addNewConfig()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.ConfigType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.ConfigType)get_store().add_element_user(CONFIG$4);
            return target;
        }
    }
    
    /**
     * Unsets the "Config" element
     */
    public void unsetConfig()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CONFIG$4, 0);
        }
    }
    
    /**
     * Gets the "creationTime" attribute
     */
    public long getCreationTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CREATIONTIME$6);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "creationTime" attribute
     */
    public org.apache.xmlbeans.XmlLong xgetCreationTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(CREATIONTIME$6);
            return target;
        }
    }
    
    /**
     * True if has "creationTime" attribute
     */
    public boolean isSetCreationTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(CREATIONTIME$6) != null;
        }
    }
    
    /**
     * Sets the "creationTime" attribute
     */
    public void setCreationTime(long creationTime)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CREATIONTIME$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CREATIONTIME$6);
            }
            target.setLongValue(creationTime);
        }
    }
    
    /**
     * Sets (as xml) the "creationTime" attribute
     */
    public void xsetCreationTime(org.apache.xmlbeans.XmlLong creationTime)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(CREATIONTIME$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_attribute_user(CREATIONTIME$6);
            }
            target.set(creationTime);
        }
    }
    
    /**
     * Unsets the "creationTime" attribute
     */
    public void unsetCreationTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(CREATIONTIME$6);
        }
    }
    
    /**
     * Gets the "id" attribute
     */
    public java.lang.String getId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$8);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "id" attribute
     */
    public org.apache.xmlbeans.XmlID xgetId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$8);
            return target;
        }
    }
    
    /**
     * True if has "id" attribute
     */
    public boolean isSetId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(ID$8) != null;
        }
    }
    
    /**
     * Sets the "id" attribute
     */
    public void setId(java.lang.String id)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ID$8);
            }
            target.setStringValue(id);
        }
    }
    
    /**
     * Sets (as xml) the "id" attribute
     */
    public void xsetId(org.apache.xmlbeans.XmlID id)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(ID$8);
            }
            target.set(id);
        }
    }
    
    /**
     * Unsets the "id" attribute
     */
    public void unsetId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(ID$8);
        }
    }
    
    /**
     * Gets the "formatVersion" attribute
     */
    public java.lang.String getFormatVersion()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FORMATVERSION$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(FORMATVERSION$10);
            }
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "formatVersion" attribute
     */
    public org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion xgetFormatVersion()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion)get_store().find_attribute_user(FORMATVERSION$10);
            if (target == null)
            {
                target = (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion)get_default_attribute_value(FORMATVERSION$10);
            }
            return target;
        }
    }
    
    /**
     * True if has "formatVersion" attribute
     */
    public boolean isSetFormatVersion()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(FORMATVERSION$10) != null;
        }
    }
    
    /**
     * Sets the "formatVersion" attribute
     */
    public void setFormatVersion(java.lang.String formatVersion)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FORMATVERSION$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FORMATVERSION$10);
            }
            target.setStringValue(formatVersion);
        }
    }
    
    /**
     * Sets (as xml) the "formatVersion" attribute
     */
    public void xsetFormatVersion(org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion formatVersion)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion)get_store().find_attribute_user(FORMATVERSION$10);
            if (target == null)
            {
                target = (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion)get_store().add_attribute_user(FORMATVERSION$10);
            }
            target.set(formatVersion);
        }
    }
    
    /**
     * Unsets the "formatVersion" attribute
     */
    public void unsetFormatVersion()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(FORMATVERSION$10);
        }
    }
    /**
     * An XML formatVersion(@).
     *
     * This is an atomic type that is a restriction of org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType$FormatVersion.
     */
    public static class FormatVersionImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion
    {
        
        public FormatVersionImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected FormatVersionImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
