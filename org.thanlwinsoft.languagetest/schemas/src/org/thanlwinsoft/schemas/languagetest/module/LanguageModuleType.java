/*
 * XML Type:  LanguageModuleType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/module
 * Java type: org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.module;


/**
 * An XML LanguageModuleType(@http://www.thanlwinsoft.org/schemas/languagetest/module).
 *
 * This is a complex type.
 */
public interface LanguageModuleType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(LanguageModuleType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6F3EA0086926FB997C5F1778DC1BD8C6").resolveHandle("languagemoduletype5b06type");
    
    /**
     * Gets array of all "Lang" elements
     */
    org.thanlwinsoft.schemas.languagetest.module.LangType[] getLangArray();
    
    /**
     * Gets ith "Lang" element
     */
    org.thanlwinsoft.schemas.languagetest.module.LangType getLangArray(int i);
    
    /**
     * Returns number of "Lang" element
     */
    int sizeOfLangArray();
    
    /**
     * Sets array of all "Lang" element
     */
    void setLangArray(org.thanlwinsoft.schemas.languagetest.module.LangType[] langArray);
    
    /**
     * Sets ith "Lang" element
     */
    void setLangArray(int i, org.thanlwinsoft.schemas.languagetest.module.LangType lang);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Lang" element
     */
    org.thanlwinsoft.schemas.languagetest.module.LangType insertNewLang(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Lang" element
     */
    org.thanlwinsoft.schemas.languagetest.module.LangType addNewLang();
    
    /**
     * Removes the ith "Lang" element
     */
    void removeLang(int i);
    
    /**
     * Gets array of all "TestItem" elements
     */
    org.thanlwinsoft.schemas.languagetest.module.TestItemType[] getTestItemArray();
    
    /**
     * Gets ith "TestItem" element
     */
    org.thanlwinsoft.schemas.languagetest.module.TestItemType getTestItemArray(int i);
    
    /**
     * Returns number of "TestItem" element
     */
    int sizeOfTestItemArray();
    
    /**
     * Sets array of all "TestItem" element
     */
    void setTestItemArray(org.thanlwinsoft.schemas.languagetest.module.TestItemType[] testItemArray);
    
    /**
     * Sets ith "TestItem" element
     */
    void setTestItemArray(int i, org.thanlwinsoft.schemas.languagetest.module.TestItemType testItem);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "TestItem" element
     */
    org.thanlwinsoft.schemas.languagetest.module.TestItemType insertNewTestItem(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "TestItem" element
     */
    org.thanlwinsoft.schemas.languagetest.module.TestItemType addNewTestItem();
    
    /**
     * Removes the ith "TestItem" element
     */
    void removeTestItem(int i);
    
    /**
     * Gets the "Config" element
     */
    org.thanlwinsoft.schemas.languagetest.module.ConfigType getConfig();
    
    /**
     * True if has "Config" element
     */
    boolean isSetConfig();
    
    /**
     * Sets the "Config" element
     */
    void setConfig(org.thanlwinsoft.schemas.languagetest.module.ConfigType config);
    
    /**
     * Appends and returns a new empty "Config" element
     */
    org.thanlwinsoft.schemas.languagetest.module.ConfigType addNewConfig();
    
    /**
     * Unsets the "Config" element
     */
    void unsetConfig();
    
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
     * Gets the "id" attribute
     */
    java.lang.String getId();
    
    /**
     * Gets (as xml) the "id" attribute
     */
    org.apache.xmlbeans.XmlID xgetId();
    
    /**
     * True if has "id" attribute
     */
    boolean isSetId();
    
    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);
    
    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlID id);
    
    /**
     * Unsets the "id" attribute
     */
    void unsetId();
    
    /**
     * Gets the "formatVersion" attribute
     */
    java.lang.String getFormatVersion();
    
    /**
     * Gets (as xml) the "formatVersion" attribute
     */
    org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion xgetFormatVersion();
    
    /**
     * True if has "formatVersion" attribute
     */
    boolean isSetFormatVersion();
    
    /**
     * Sets the "formatVersion" attribute
     */
    void setFormatVersion(java.lang.String formatVersion);
    
    /**
     * Sets (as xml) the "formatVersion" attribute
     */
    void xsetFormatVersion(org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion formatVersion);
    
    /**
     * Unsets the "formatVersion" attribute
     */
    void unsetFormatVersion();
    
    /**
     * An XML formatVersion(@).
     *
     * This is an atomic type that is a restriction of org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType$FormatVersion.
     */
    public interface FormatVersion extends org.apache.xmlbeans.XmlString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(FormatVersion.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6F3EA0086926FB997C5F1778DC1BD8C6").resolveHandle("formatversion335dattrtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion newValue(java.lang.Object obj) {
              return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion) type.newValue( obj ); }
            
            public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion newInstance() {
              return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType.FormatVersion) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType newInstance() {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.thanlwinsoft.schemas.languagetest.module.LanguageModuleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
