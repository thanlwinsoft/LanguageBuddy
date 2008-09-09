/*
 * XML Type:  TestItemType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/module
 * Java type: org.thanlwinsoft.schemas.languagetest.module.TestItemType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.module;


/**
 * An XML TestItemType(@http://www.thanlwinsoft.org/schemas/languagetest/module).
 *
 * This is a complex type.
 */
public interface TestItemType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TestItemType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6F3EA0086926FB997C5F1778DC1BD8C6").resolveHandle("testitemtype2d27type");
    
    /**
     * Gets array of all "NativeLang" elements
     */
    org.thanlwinsoft.schemas.languagetest.module.NativeLangType[] getNativeLangArray();
    
    /**
     * Gets ith "NativeLang" element
     */
    org.thanlwinsoft.schemas.languagetest.module.NativeLangType getNativeLangArray(int i);
    
    /**
     * Returns number of "NativeLang" element
     */
    int sizeOfNativeLangArray();
    
    /**
     * Sets array of all "NativeLang" element
     */
    void setNativeLangArray(org.thanlwinsoft.schemas.languagetest.module.NativeLangType[] nativeLangArray);
    
    /**
     * Sets ith "NativeLang" element
     */
    void setNativeLangArray(int i, org.thanlwinsoft.schemas.languagetest.module.NativeLangType nativeLang);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "NativeLang" element
     */
    org.thanlwinsoft.schemas.languagetest.module.NativeLangType insertNewNativeLang(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "NativeLang" element
     */
    org.thanlwinsoft.schemas.languagetest.module.NativeLangType addNewNativeLang();
    
    /**
     * Removes the ith "NativeLang" element
     */
    void removeNativeLang(int i);
    
    /**
     * Gets array of all "ForeignLang" elements
     */
    org.thanlwinsoft.schemas.languagetest.module.ForeignLangType[] getForeignLangArray();
    
    /**
     * Gets ith "ForeignLang" element
     */
    org.thanlwinsoft.schemas.languagetest.module.ForeignLangType getForeignLangArray(int i);
    
    /**
     * Returns number of "ForeignLang" element
     */
    int sizeOfForeignLangArray();
    
    /**
     * Sets array of all "ForeignLang" element
     */
    void setForeignLangArray(org.thanlwinsoft.schemas.languagetest.module.ForeignLangType[] foreignLangArray);
    
    /**
     * Sets ith "ForeignLang" element
     */
    void setForeignLangArray(int i, org.thanlwinsoft.schemas.languagetest.module.ForeignLangType foreignLang);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "ForeignLang" element
     */
    org.thanlwinsoft.schemas.languagetest.module.ForeignLangType insertNewForeignLang(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "ForeignLang" element
     */
    org.thanlwinsoft.schemas.languagetest.module.ForeignLangType addNewForeignLang();
    
    /**
     * Removes the ith "ForeignLang" element
     */
    void removeForeignLang(int i);
    
    /**
     * Gets the "SoundFile" element
     */
    org.thanlwinsoft.schemas.languagetest.module.SoundFileType getSoundFile();
    
    /**
     * True if has "SoundFile" element
     */
    boolean isSetSoundFile();
    
    /**
     * Sets the "SoundFile" element
     */
    void setSoundFile(org.thanlwinsoft.schemas.languagetest.module.SoundFileType soundFile);
    
    /**
     * Appends and returns a new empty "SoundFile" element
     */
    org.thanlwinsoft.schemas.languagetest.module.SoundFileType addNewSoundFile();
    
    /**
     * Unsets the "SoundFile" element
     */
    void unsetSoundFile();
    
    /**
     * Gets the "Img" element
     */
    java.lang.String getImg();
    
    /**
     * Gets (as xml) the "Img" element
     */
    org.apache.xmlbeans.XmlString xgetImg();
    
    /**
     * True if has "Img" element
     */
    boolean isSetImg();
    
    /**
     * Sets the "Img" element
     */
    void setImg(java.lang.String img);
    
    /**
     * Sets (as xml) the "Img" element
     */
    void xsetImg(org.apache.xmlbeans.XmlString img);
    
    /**
     * Unsets the "Img" element
     */
    void unsetImg();
    
    /**
     * Gets array of all "Tag" elements
     */
    org.thanlwinsoft.schemas.languagetest.module.TagType[] getTagArray();
    
    /**
     * Gets ith "Tag" element
     */
    org.thanlwinsoft.schemas.languagetest.module.TagType getTagArray(int i);
    
    /**
     * Returns number of "Tag" element
     */
    int sizeOfTagArray();
    
    /**
     * Sets array of all "Tag" element
     */
    void setTagArray(org.thanlwinsoft.schemas.languagetest.module.TagType[] tagArray);
    
    /**
     * Sets ith "Tag" element
     */
    void setTagArray(int i, org.thanlwinsoft.schemas.languagetest.module.TagType tag);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Tag" element
     */
    org.thanlwinsoft.schemas.languagetest.module.TagType insertNewTag(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Tag" element
     */
    org.thanlwinsoft.schemas.languagetest.module.TagType addNewTag();
    
    /**
     * Removes the ith "Tag" element
     */
    void removeTag(int i);
    
    /**
     * Gets the "creationTime" attribute
     */
    long getCreationTime();
    
    /**
     * Gets (as xml) the "creationTime" attribute
     */
    org.apache.xmlbeans.XmlLong xgetCreationTime();
    
    /**
     * True if has "creationTime" attribute
     */
    boolean isSetCreationTime();
    
    /**
     * Sets the "creationTime" attribute
     */
    void setCreationTime(long creationTime);
    
    /**
     * Sets (as xml) the "creationTime" attribute
     */
    void xsetCreationTime(org.apache.xmlbeans.XmlLong creationTime);
    
    /**
     * Unsets the "creationTime" attribute
     */
    void unsetCreationTime();
    
    /**
     * Gets the "creator" attribute
     */
    java.lang.String getCreator();
    
    /**
     * Gets (as xml) the "creator" attribute
     */
    org.apache.xmlbeans.XmlString xgetCreator();
    
    /**
     * True if has "creator" attribute
     */
    boolean isSetCreator();
    
    /**
     * Sets the "creator" attribute
     */
    void setCreator(java.lang.String creator);
    
    /**
     * Sets (as xml) the "creator" attribute
     */
    void xsetCreator(org.apache.xmlbeans.XmlString creator);
    
    /**
     * Unsets the "creator" attribute
     */
    void unsetCreator();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType newInstance() {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.thanlwinsoft.schemas.languagetest.module.TestItemType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.thanlwinsoft.schemas.languagetest.module.TestItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
