/*
 * XML Type:  MetaDataType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/module
 * Java type: org.thanlwinsoft.schemas.languagetest.module.MetaDataType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.module.impl;
/**
 * An XML MetaDataType(@http://www.thanlwinsoft.org/schemas/languagetest/module).
 *
 * This is a complex type.
 */
public class MetaDataTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.thanlwinsoft.schemas.languagetest.module.MetaDataType
{
    
    public MetaDataTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DESC$0 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "Desc");
    private static final javax.xml.namespace.QName METADATA$2 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "MetaData");
    private static final javax.xml.namespace.QName METAID$4 = 
        new javax.xml.namespace.QName("", "metaId");
    
    
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
     * Gets array of all "MetaData" elements
     */
    public org.thanlwinsoft.schemas.languagetest.module.MetaDataType[] getMetaDataArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(METADATA$2, targetList);
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
            target = (org.thanlwinsoft.schemas.languagetest.module.MetaDataType)get_store().find_element_user(METADATA$2, i);
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
            return get_store().count_elements(METADATA$2);
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
            arraySetterHelper(metaDataArray, METADATA$2);
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
            target = (org.thanlwinsoft.schemas.languagetest.module.MetaDataType)get_store().find_element_user(METADATA$2, i);
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
            target = (org.thanlwinsoft.schemas.languagetest.module.MetaDataType)get_store().insert_element_user(METADATA$2, i);
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
            target = (org.thanlwinsoft.schemas.languagetest.module.MetaDataType)get_store().add_element_user(METADATA$2);
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
            get_store().remove_element(METADATA$2, i);
        }
    }
    
    /**
     * Gets the "metaId" attribute
     */
    public java.lang.String getMetaId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(METAID$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "metaId" attribute
     */
    public org.thanlwinsoft.schemas.languagetest.module.MetaIdType xgetMetaId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.MetaIdType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.MetaIdType)get_store().find_attribute_user(METAID$4);
            return target;
        }
    }
    
    /**
     * Sets the "metaId" attribute
     */
    public void setMetaId(java.lang.String metaId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(METAID$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(METAID$4);
            }
            target.setStringValue(metaId);
        }
    }
    
    /**
     * Sets (as xml) the "metaId" attribute
     */
    public void xsetMetaId(org.thanlwinsoft.schemas.languagetest.module.MetaIdType metaId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.MetaIdType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.MetaIdType)get_store().find_attribute_user(METAID$4);
            if (target == null)
            {
                target = (org.thanlwinsoft.schemas.languagetest.module.MetaIdType)get_store().add_attribute_user(METAID$4);
            }
            target.set(metaId);
        }
    }
}
