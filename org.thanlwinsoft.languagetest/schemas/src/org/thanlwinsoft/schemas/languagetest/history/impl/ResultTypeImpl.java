/*
 * XML Type:  ResultType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/history
 * Java type: org.thanlwinsoft.schemas.languagetest.history.ResultType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.history.impl;
/**
 * An XML ResultType(@http://www.thanlwinsoft.org/schemas/languagetest/history).
 *
 * This is an atomic type that is a restriction of org.thanlwinsoft.schemas.languagetest.history.ResultType.
 */
public class ResultTypeImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.thanlwinsoft.schemas.languagetest.history.ResultType
{
    
    public ResultTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType, true);
    }
    
    protected ResultTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
    {
        super(sType, b);
    }
    
    private static final javax.xml.namespace.QName PASS$0 = 
        new javax.xml.namespace.QName("", "pass");
    private static final javax.xml.namespace.QName TIME$2 = 
        new javax.xml.namespace.QName("", "time");
    
    
    /**
     * Gets the "pass" attribute
     */
    public boolean getPass()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PASS$0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "pass" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetPass()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PASS$0);
            return target;
        }
    }
    
    /**
     * True if has "pass" attribute
     */
    public boolean isSetPass()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(PASS$0) != null;
        }
    }
    
    /**
     * Sets the "pass" attribute
     */
    public void setPass(boolean pass)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PASS$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PASS$0);
            }
            target.setBooleanValue(pass);
        }
    }
    
    /**
     * Sets (as xml) the "pass" attribute
     */
    public void xsetPass(org.apache.xmlbeans.XmlBoolean pass)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PASS$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PASS$0);
            }
            target.set(pass);
        }
    }
    
    /**
     * Unsets the "pass" attribute
     */
    public void unsetPass()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(PASS$0);
        }
    }
    
    /**
     * Gets the "time" attribute
     */
    public long getTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIME$2);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "time" attribute
     */
    public org.apache.xmlbeans.XmlLong xgetTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(TIME$2);
            return target;
        }
    }
    
    /**
     * True if has "time" attribute
     */
    public boolean isSetTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(TIME$2) != null;
        }
    }
    
    /**
     * Sets the "time" attribute
     */
    public void setTime(long time)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TIME$2);
            }
            target.setLongValue(time);
        }
    }
    
    /**
     * Sets (as xml) the "time" attribute
     */
    public void xsetTime(org.apache.xmlbeans.XmlLong time)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(TIME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_attribute_user(TIME$2);
            }
            target.set(time);
        }
    }
    
    /**
     * Unsets the "time" attribute
     */
    public void unsetTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(TIME$2);
        }
    }
}
