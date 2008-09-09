/*
 * XML Type:  TestType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/history
 * Java type: org.thanlwinsoft.schemas.languagetest.history.TestType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.history.impl;
/**
 * An XML TestType(@http://www.thanlwinsoft.org/schemas/languagetest/history).
 *
 * This is a complex type.
 */
public class TestTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.thanlwinsoft.schemas.languagetest.history.TestType
{
    
    public TestTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESULT$0 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/history", "Result");
    private static final javax.xml.namespace.QName DISABLED$2 = 
        new javax.xml.namespace.QName("", "disabled");
    
    
    /**
     * Gets array of all "Result" elements
     */
    public org.thanlwinsoft.schemas.languagetest.history.ResultType[] getResultArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(RESULT$0, targetList);
            org.thanlwinsoft.schemas.languagetest.history.ResultType[] result = new org.thanlwinsoft.schemas.languagetest.history.ResultType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Result" element
     */
    public org.thanlwinsoft.schemas.languagetest.history.ResultType getResultArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.ResultType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.ResultType)get_store().find_element_user(RESULT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Result" element
     */
    public int sizeOfResultArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RESULT$0);
        }
    }
    
    /**
     * Sets array of all "Result" element
     */
    public void setResultArray(org.thanlwinsoft.schemas.languagetest.history.ResultType[] resultArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(resultArray, RESULT$0);
        }
    }
    
    /**
     * Sets ith "Result" element
     */
    public void setResultArray(int i, org.thanlwinsoft.schemas.languagetest.history.ResultType result)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.ResultType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.ResultType)get_store().find_element_user(RESULT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(result);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Result" element
     */
    public org.thanlwinsoft.schemas.languagetest.history.ResultType insertNewResult(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.ResultType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.ResultType)get_store().insert_element_user(RESULT$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Result" element
     */
    public org.thanlwinsoft.schemas.languagetest.history.ResultType addNewResult()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.ResultType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.ResultType)get_store().add_element_user(RESULT$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Result" element
     */
    public void removeResult(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RESULT$0, i);
        }
    }
    
    /**
     * Gets the "disabled" attribute
     */
    public boolean getDisabled()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DISABLED$2);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "disabled" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetDisabled()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(DISABLED$2);
            return target;
        }
    }
    
    /**
     * True if has "disabled" attribute
     */
    public boolean isSetDisabled()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(DISABLED$2) != null;
        }
    }
    
    /**
     * Sets the "disabled" attribute
     */
    public void setDisabled(boolean disabled)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DISABLED$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DISABLED$2);
            }
            target.setBooleanValue(disabled);
        }
    }
    
    /**
     * Sets (as xml) the "disabled" attribute
     */
    public void xsetDisabled(org.apache.xmlbeans.XmlBoolean disabled)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(DISABLED$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(DISABLED$2);
            }
            target.set(disabled);
        }
    }
    
    /**
     * Unsets the "disabled" attribute
     */
    public void unsetDisabled()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(DISABLED$2);
        }
    }
}
