/*
 * XML Type:  SoundFileType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/module
 * Java type: org.thanlwinsoft.schemas.languagetest.module.SoundFileType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.module.impl;
/**
 * An XML SoundFileType(@http://www.thanlwinsoft.org/schemas/languagetest/module).
 *
 * This is an atomic type that is a restriction of org.thanlwinsoft.schemas.languagetest.module.SoundFileType.
 */
public class SoundFileTypeImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.thanlwinsoft.schemas.languagetest.module.SoundFileType
{
    
    public SoundFileTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType, true);
    }
    
    protected SoundFileTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
    {
        super(sType, b);
    }
    
    private static final javax.xml.namespace.QName START$0 = 
        new javax.xml.namespace.QName("", "start");
    private static final javax.xml.namespace.QName END$2 = 
        new javax.xml.namespace.QName("", "end");
    private static final javax.xml.namespace.QName LENGTH$4 = 
        new javax.xml.namespace.QName("", "length");
    
    
    /**
     * Gets the "start" attribute
     */
    public long getStart()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(START$0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "start" attribute
     */
    public org.apache.xmlbeans.XmlLong xgetStart()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(START$0);
            return target;
        }
    }
    
    /**
     * True if has "start" attribute
     */
    public boolean isSetStart()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(START$0) != null;
        }
    }
    
    /**
     * Sets the "start" attribute
     */
    public void setStart(long start)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(START$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(START$0);
            }
            target.setLongValue(start);
        }
    }
    
    /**
     * Sets (as xml) the "start" attribute
     */
    public void xsetStart(org.apache.xmlbeans.XmlLong start)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(START$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_attribute_user(START$0);
            }
            target.set(start);
        }
    }
    
    /**
     * Unsets the "start" attribute
     */
    public void unsetStart()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(START$0);
        }
    }
    
    /**
     * Gets the "end" attribute
     */
    public long getEnd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(END$2);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "end" attribute
     */
    public org.apache.xmlbeans.XmlLong xgetEnd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(END$2);
            return target;
        }
    }
    
    /**
     * True if has "end" attribute
     */
    public boolean isSetEnd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(END$2) != null;
        }
    }
    
    /**
     * Sets the "end" attribute
     */
    public void setEnd(long end)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(END$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(END$2);
            }
            target.setLongValue(end);
        }
    }
    
    /**
     * Sets (as xml) the "end" attribute
     */
    public void xsetEnd(org.apache.xmlbeans.XmlLong end)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(END$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_attribute_user(END$2);
            }
            target.set(end);
        }
    }
    
    /**
     * Unsets the "end" attribute
     */
    public void unsetEnd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(END$2);
        }
    }
    
    /**
     * Gets the "length" attribute
     */
    public long getLength()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LENGTH$4);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "length" attribute
     */
    public org.apache.xmlbeans.XmlLong xgetLength()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(LENGTH$4);
            return target;
        }
    }
    
    /**
     * True if has "length" attribute
     */
    public boolean isSetLength()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(LENGTH$4) != null;
        }
    }
    
    /**
     * Sets the "length" attribute
     */
    public void setLength(long length)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LENGTH$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(LENGTH$4);
            }
            target.setLongValue(length);
        }
    }
    
    /**
     * Sets (as xml) the "length" attribute
     */
    public void xsetLength(org.apache.xmlbeans.XmlLong length)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(LENGTH$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_attribute_user(LENGTH$4);
            }
            target.set(length);
        }
    }
    
    /**
     * Unsets the "length" attribute
     */
    public void unsetLength()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(LENGTH$4);
        }
    }
}
