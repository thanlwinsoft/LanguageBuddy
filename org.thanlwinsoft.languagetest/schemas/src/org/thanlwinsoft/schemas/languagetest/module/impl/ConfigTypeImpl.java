/*
 * XML Type:  ConfigType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/module
 * Java type: org.thanlwinsoft.schemas.languagetest.module.ConfigType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.module.impl;
/**
 * An XML ConfigType(@http://www.thanlwinsoft.org/schemas/languagetest/module).
 *
 * This is a complex type.
 */
public class ConfigTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.thanlwinsoft.schemas.languagetest.module.ConfigType
{
    
    public ConfigTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName METADATA$0 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "MetaData");
    
    
    /**
     * Gets array of all "MetaData" elements
     */
    public org.thanlwinsoft.schemas.languagetest.module.MetaDataType[] getMetaDataArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(METADATA$0, targetList);
            org.thanlwinsoft.schemas.languagetest.module.MetaDataType[] result = new org.thanlwinsoft.schemas.languagetest.module.MetaDataType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "MetaData" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.MetaDataType getMetaDataArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.MetaDataType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.MetaDataType)get_store().find_element_user(METADATA$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "MetaData" element
     */
    public int sizeOfMetaDataArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(METADATA$0);
        }
    }
    
    /**
     * Sets array of all "MetaData" element
     */
    public void setMetaDataArray(org.thanlwinsoft.schemas.languagetest.module.MetaDataType[] metaDataArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(metaDataArray, METADATA$0);
        }
    }
    
    /**
     * Sets ith "MetaData" element
     */
    public void setMetaDataArray(int i, org.thanlwinsoft.schemas.languagetest.module.MetaDataType metaData)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.MetaDataType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.MetaDataType)get_store().find_element_user(METADATA$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(metaData);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "MetaData" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.MetaDataType insertNewMetaData(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.MetaDataType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.MetaDataType)get_store().insert_element_user(METADATA$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "MetaData" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.MetaDataType addNewMetaData()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.MetaDataType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.MetaDataType)get_store().add_element_user(METADATA$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "MetaData" element
     */
    public void removeMetaData(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(METADATA$0, i);
        }
    }
}
