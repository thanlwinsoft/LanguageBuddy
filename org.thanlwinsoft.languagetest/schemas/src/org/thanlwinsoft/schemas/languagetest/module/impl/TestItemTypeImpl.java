/*
 * XML Type:  TestItemType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/module
 * Java type: org.thanlwinsoft.schemas.languagetest.module.TestItemType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.module.impl;
/**
 * An XML TestItemType(@http://www.thanlwinsoft.org/schemas/languagetest/module).
 *
 * This is a complex type.
 */
public class TestItemTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.thanlwinsoft.schemas.languagetest.module.TestItemType
{
    
    public TestItemTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NATIVELANG$0 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "NativeLang");
    private static final javax.xml.namespace.QName FOREIGNLANG$2 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "ForeignLang");
    private static final javax.xml.namespace.QName SOUNDFILE$4 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "SoundFile");
    private static final javax.xml.namespace.QName IMG$6 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "Img");
    private static final javax.xml.namespace.QName TAG$8 = 
        new javax.xml.namespace.QName("http://www.thanlwinsoft.org/schemas/languagetest/module", "Tag");
    private static final javax.xml.namespace.QName CREATIONTIME$10 = 
        new javax.xml.namespace.QName("", "creationTime");
    private static final javax.xml.namespace.QName CREATOR$12 = 
        new javax.xml.namespace.QName("", "creator");
    
    
    /**
     * Gets array of all "NativeLang" elements
     */
    public org.thanlwinsoft.schemas.languagetest.module.NativeLangType[] getNativeLangArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(NATIVELANG$0, targetList);
            org.thanlwinsoft.schemas.languagetest.module.NativeLangType[] result = new org.thanlwinsoft.schemas.languagetest.module.NativeLangType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "NativeLang" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.NativeLangType getNativeLangArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.NativeLangType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.NativeLangType)get_store().find_element_user(NATIVELANG$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "NativeLang" element
     */
    public int sizeOfNativeLangArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(NATIVELANG$0);
        }
    }
    
    /**
     * Sets array of all "NativeLang" element
     */
    public void setNativeLangArray(org.thanlwinsoft.schemas.languagetest.module.NativeLangType[] nativeLangArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(nativeLangArray, NATIVELANG$0);
        }
    }
    
    /**
     * Sets ith "NativeLang" element
     */
    public void setNativeLangArray(int i, org.thanlwinsoft.schemas.languagetest.module.NativeLangType nativeLang)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.NativeLangType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.NativeLangType)get_store().find_element_user(NATIVELANG$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(nativeLang);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "NativeLang" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.NativeLangType insertNewNativeLang(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.NativeLangType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.NativeLangType)get_store().insert_element_user(NATIVELANG$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "NativeLang" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.NativeLangType addNewNativeLang()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.NativeLangType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.NativeLangType)get_store().add_element_user(NATIVELANG$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "NativeLang" element
     */
    public void removeNativeLang(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(NATIVELANG$0, i);
        }
    }
    
    /**
     * Gets array of all "ForeignLang" elements
     */
    public org.thanlwinsoft.schemas.languagetest.module.ForeignLangType[] getForeignLangArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(FOREIGNLANG$2, targetList);
            org.thanlwinsoft.schemas.languagetest.module.ForeignLangType[] result = new org.thanlwinsoft.schemas.languagetest.module.ForeignLangType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "ForeignLang" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.ForeignLangType getForeignLangArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.ForeignLangType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.ForeignLangType)get_store().find_element_user(FOREIGNLANG$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "ForeignLang" element
     */
    public int sizeOfForeignLangArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FOREIGNLANG$2);
        }
    }
    
    /**
     * Sets array of all "ForeignLang" element
     */
    public void setForeignLangArray(org.thanlwinsoft.schemas.languagetest.module.ForeignLangType[] foreignLangArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(foreignLangArray, FOREIGNLANG$2);
        }
    }
    
    /**
     * Sets ith "ForeignLang" element
     */
    public void setForeignLangArray(int i, org.thanlwinsoft.schemas.languagetest.module.ForeignLangType foreignLang)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.ForeignLangType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.ForeignLangType)get_store().find_element_user(FOREIGNLANG$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(foreignLang);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "ForeignLang" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.ForeignLangType insertNewForeignLang(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.ForeignLangType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.ForeignLangType)get_store().insert_element_user(FOREIGNLANG$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "ForeignLang" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.ForeignLangType addNewForeignLang()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.ForeignLangType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.ForeignLangType)get_store().add_element_user(FOREIGNLANG$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "ForeignLang" element
     */
    public void removeForeignLang(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FOREIGNLANG$2, i);
        }
    }
    
    /**
     * Gets the "SoundFile" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.SoundFileType getSoundFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.SoundFileType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.SoundFileType)get_store().find_element_user(SOUNDFILE$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "SoundFile" element
     */
    public boolean isSetSoundFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SOUNDFILE$4) != 0;
        }
    }
    
    /**
     * Sets the "SoundFile" element
     */
    public void setSoundFile(org.thanlwinsoft.schemas.languagetest.module.SoundFileType soundFile)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.SoundFileType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.SoundFileType)get_store().find_element_user(SOUNDFILE$4, 0);
            if (target == null)
            {
                target = (org.thanlwinsoft.schemas.languagetest.module.SoundFileType)get_store().add_element_user(SOUNDFILE$4);
            }
            target.set(soundFile);
        }
    }
    
    /**
     * Appends and returns a new empty "SoundFile" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.SoundFileType addNewSoundFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.SoundFileType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.SoundFileType)get_store().add_element_user(SOUNDFILE$4);
            return target;
        }
    }
    
    /**
     * Unsets the "SoundFile" element
     */
    public void unsetSoundFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SOUNDFILE$4, 0);
        }
    }
    
    /**
     * Gets the "Img" element
     */
    public java.lang.String getImg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IMG$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Img" element
     */
    public org.apache.xmlbeans.XmlString xgetImg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(IMG$6, 0);
            return target;
        }
    }
    
    /**
     * True if has "Img" element
     */
    public boolean isSetImg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(IMG$6) != 0;
        }
    }
    
    /**
     * Sets the "Img" element
     */
    public void setImg(java.lang.String img)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IMG$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(IMG$6);
            }
            target.setStringValue(img);
        }
    }
    
    /**
     * Sets (as xml) the "Img" element
     */
    public void xsetImg(org.apache.xmlbeans.XmlString img)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(IMG$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(IMG$6);
            }
            target.set(img);
        }
    }
    
    /**
     * Unsets the "Img" element
     */
    public void unsetImg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(IMG$6, 0);
        }
    }
    
    /**
     * Gets array of all "Tag" elements
     */
    public org.thanlwinsoft.schemas.languagetest.module.TagType[] getTagArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(TAG$8, targetList);
            org.thanlwinsoft.schemas.languagetest.module.TagType[] result = new org.thanlwinsoft.schemas.languagetest.module.TagType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Tag" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.TagType getTagArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.TagType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.TagType)get_store().find_element_user(TAG$8, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Tag" element
     */
    public int sizeOfTagArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TAG$8);
        }
    }
    
    /**
     * Sets array of all "Tag" element
     */
    public void setTagArray(org.thanlwinsoft.schemas.languagetest.module.TagType[] tagArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(tagArray, TAG$8);
        }
    }
    
    /**
     * Sets ith "Tag" element
     */
    public void setTagArray(int i, org.thanlwinsoft.schemas.languagetest.module.TagType tag)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.TagType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.TagType)get_store().find_element_user(TAG$8, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(tag);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Tag" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.TagType insertNewTag(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.TagType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.TagType)get_store().insert_element_user(TAG$8, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Tag" element
     */
    public org.thanlwinsoft.schemas.languagetest.module.TagType addNewTag()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.thanlwinsoft.schemas.languagetest.module.TagType target = null;
            target = (org.thanlwinsoft.schemas.languagetest.module.TagType)get_store().add_element_user(TAG$8);
            return target;
        }
    }
    
    /**
     * Removes the ith "Tag" element
     */
    public void removeTag(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TAG$8, i);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CREATIONTIME$10);
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
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(CREATIONTIME$10);
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
            return get_store().find_attribute_user(CREATIONTIME$10) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CREATIONTIME$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CREATIONTIME$10);
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
            target = (org.apache.xmlbeans.XmlLong)get_store().find_attribute_user(CREATIONTIME$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_attribute_user(CREATIONTIME$10);
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
            get_store().remove_attribute(CREATIONTIME$10);
        }
    }
    
    /**
     * Gets the "creator" attribute
     */
    public java.lang.String getCreator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CREATOR$12);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "creator" attribute
     */
    public org.apache.xmlbeans.XmlString xgetCreator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(CREATOR$12);
            return target;
        }
    }
    
    /**
     * True if has "creator" attribute
     */
    public boolean isSetCreator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(CREATOR$12) != null;
        }
    }
    
    /**
     * Sets the "creator" attribute
     */
    public void setCreator(java.lang.String creator)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CREATOR$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CREATOR$12);
            }
            target.setStringValue(creator);
        }
    }
    
    /**
     * Sets (as xml) the "creator" attribute
     */
    public void xsetCreator(org.apache.xmlbeans.XmlString creator)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(CREATOR$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(CREATOR$12);
            }
            target.set(creator);
        }
    }
    
    /**
     * Unsets the "creator" attribute
     */
    public void unsetCreator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(CREATOR$12);
        }
    }
}
