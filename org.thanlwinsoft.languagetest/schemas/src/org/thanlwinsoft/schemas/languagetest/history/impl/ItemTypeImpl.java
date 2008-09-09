/*
 * XML Type:  ItemType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/history
 * Java type: org.thanlwinsoft.schemas.languagetest.history.ItemType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.history.impl;
/**
 * An XML ItemType(@http://www.thanlwinsoft.org/schemas/languagetest/history).
 *
 * This is a complex type.
 */
public class ItemTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.thanlwinsoft.schemas.languagetest.history.ItemType
{
    
    public ItemTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FR$0 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/history", "FR");
    private static final javax.xml.namespace.QName FL$2 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/history", "FL");
    private static final javax.xml.namespace.QName NR$4 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/history", "NR");
    private static final javax.xml.namespace.QName AUTHOR$6 = 
        new javax.xml.namespace.QName("", "author");
    private static final javax.xml.namespace.QName CREATED$8 = 
        new javax.xml.namespace.QName("", "created");
    
    
    /**
     * Gets the "FR" element
     */
    public org.thanlwinsoft.schemas.languagetest.history.TestType getFR()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.TestType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.TestType)get_store().find_element_user(FR$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "FR" element
     */
    public boolean isSetFR()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FR$0) != 0;
        }
    }
    
    /**
     * Sets the "FR" element
     */
    public void setFR(org.thanlwinsoft.schemas.languagetest.history.TestType fr)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.TestType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.TestType)get_store().find_element_user(FR$0, 0);
            if (target == null)
            {
                target = (org.thanlwinsoft.schemas.languagetest.history.TestType)get_store().add_element_user(FR$0);
            }
            target.set(fr);
        }
    }
    
    /**
     * Appends and returns a new empty "FR" element
     */
    public org.thanlwinsoft.schemas.languagetest.history.TestType addNewFR()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.TestType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.TestType)get_store().add_element_user(FR$0);
            return target;
        }
    }
    
    /**
     * Unsets the "FR" element
     */
    public void unsetFR()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FR$0, 0);
        }
    }
    
    /**
     * Gets the "FL" element
     */
    public org.thanlwinsoft.schemas.languagetest.history.TestType getFL()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.TestType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.TestType)get_store().find_element_user(FL$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "FL" element
     */
    public boolean isSetFL()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FL$2) != 0;
        }
    }
    
    /**
     * Sets the "FL" element
     */
    public void setFL(org.thanlwinsoft.schemas.languagetest.history.TestType fl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.TestType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.TestType)get_store().find_element_user(FL$2, 0);
            if (target == null)
            {
                target = (org.thanlwinsoft.schemas.languagetest.history.TestType)get_store().add_element_user(FL$2);
            }
            target.set(fl);
        }
    }
    
    /**
     * Appends and returns a new empty "FL" element
     */
    public org.thanlwinsoft.schemas.languagetest.history.TestType addNewFL()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.TestType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.TestType)get_store().add_element_user(FL$2);
            return target;
        }
    }
    
    /**
     * Unsets the "FL" element
     */
    public void unsetFL()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FL$2, 0);
        }
    }
    
    /**
     * Gets the "NR" element
     */
    public org.thanlwinsoft.schemas.languagetest.history.TestType getNR()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.TestType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.TestType)get_store().find_element_user(NR$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "NR" element
     */
    public boolean isSetNR()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(NR$4) != 0;
        }
    }
    
    /**
     * Sets the "NR" element
     */
    public void setNR(org.thanlwinsoft.schemas.languagetest.history.TestType nr)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.TestType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.TestType)get_store().find_element_user(NR$4, 0);
            if (target == null)
            {
                target = (org.thanlwinsoft.schemas.languagetest.history.TestType)get_store().add_element_user(NR$4);
            }
            target.set(nr);
        }
    }
    
    /**
     * Appends and returns a new empty "NR" element
     */
    public org.thanlwinsoft.schemas.languagetest.history.TestType addNewNR()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.history.TestType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.history.TestType)get_store().add_element_user(NR$4);
            return target;
        }
    }
    
    /**
     * Unsets the "NR" element
     */
    public void unsetNR()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(NR$4, 0);
        }
    }
    
    /**
     * Gets the "author" attribute
     */
    public java.lang.String getAuthor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(AUTHOR$6);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "author" attribute
     */
    public org.apache.xmlbeans.XmlString xgetAuthor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(AUTHOR$6);
            return target;
        }
    }
    
    /**
     * True if has "author" attribute
     */
    public boolean isSetAuthor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(AUTHOR$6) != null;
        }
    }
    
    /**
     * Sets the "author" attribute
     */
    public void setAuthor(java.lang.String author)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(AUTHOR$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(AUTHOR$6);
            }
            target.setStringValue(author);
        }
    }
    
    /**
     * Sets (as xml) the "author" attribute
     */
    public void xsetAuthor(org.apache.xmlbeans.XmlString author)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(AUTHOR$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(AUTHOR$6);
            }
            target.set(author);
        }
    }
    
    /**
     * Unsets the "author" attribute
     */
    public void unsetAuthor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(AUTHOR$6);
        }
    }
    
    /**
     * Gets the "created" attribute
     */
    public long getCreated()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CREATED$8);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "created" attribute
     */
    public org.apache.xmlbeans.XmlLong xgetCreated()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(CREATED$8);
            return target;
        }
    }
    
    /**
     * True if has "created" attribute
     */
    public boolean isSetCreated()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(CREATED$8) != null;
        }
    }
    
    /**
     * Sets the "created" attribute
     */
    public void setCreated(long created)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CREATED$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CREATED$8);
            }
            target.setLongValue(created);
        }
    }
    
    /**
     * Sets (as xml) the "created" attribute
     */
    public void xsetCreated(org.apache.xmlbeans.XmlLong created)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(CREATED$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_attribute_user(CREATED$8);
            }
            target.set(created);
        }
    }
    
    /**
     * Unsets the "created" attribute
     */
    public void unsetCreated()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(CREATED$8);
        }
    }
}
