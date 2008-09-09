/*
 * XML Type:  LangType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/module
 * Java type: org.thanlwinsoft.schemas.languagetest.module.LangType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.module;


/**
 * An XML LangType(@http://www.thanlwinsoft.org/schemas/languagetest/module).
 *
 * This is a complex type.
 */
public interface LangType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(LangType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6F3EA0086926FB997C5F1778DC1BD8C6").resolveHandle("langtypec550type");
    
    /**
     * Gets array of all "Desc" elements
     */
    org.thanlwinsoft.schemas.languagetest.module.DescType[] getDescArray();
    
    /**
     * Gets ith "Desc" element
     */
    org.thanlwinsoft.schemas.languagetest.module.DescType getDescArray(int i);
    
    /**
     * Returns number of "Desc" element
     */
    int sizeOfDescArray();
    
    /**
     * Sets array of all "Desc" element
     */
    void setDescArray(org.thanlwinsoft.schemas.languagetest.module.DescType[] descArray);
    
    /**
     * Sets ith "Desc" element
     */
    void setDescArray(int i, org.thanlwinsoft.schemas.languagetest.module.DescType desc);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Desc" element
     */
    org.thanlwinsoft.schemas.languagetest.module.DescType insertNewDesc(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Desc" element
     */
    org.thanlwinsoft.schemas.languagetest.module.DescType addNewDesc();
    
    /**
     * Removes the ith "Desc" element
     */
    void removeDesc(int i);
    
    /**
     * Gets the "font" attribute
     */
    java.lang.String getFont();
    
    /**
     * Gets (as xml) the "font" attribute
     */
    org.apache.xmlbeans.XmlString xgetFont();
    
    /**
     * True if has "font" attribute
     */
    boolean isSetFont();
    
    /**
     * Sets the "font" attribute
     */
    void setFont(java.lang.String font);
    
    /**
     * Sets (as xml) the "font" attribute
     */
    void xsetFont(org.apache.xmlbeans.XmlString font);
    
    /**
     * Unsets the "font" attribute
     */
    void unsetFont();
    
    /**
     * Gets the "fontSize" attribute
     */
    java.math.BigDecimal getFontSize();
    
    /**
     * Gets (as xml) the "fontSize" attribute
     */
    org.apache.xmlbeans.XmlDecimal xgetFontSize();
    
    /**
     * True if has "fontSize" attribute
     */
    boolean isSetFontSize();
    
    /**
     * Sets the "fontSize" attribute
     */
    void setFontSize(java.math.BigDecimal fontSize);
    
    /**
     * Sets (as xml) the "fontSize" attribute
     */
    void xsetFontSize(org.apache.xmlbeans.XmlDecimal fontSize);
    
    /**
     * Unsets the "fontSize" attribute
     */
    void unsetFontSize();
    
    /**
     * Gets the "fontStyle" attribute
     */
    java.math.BigDecimal getFontStyle();
    
    /**
     * Gets (as xml) the "fontStyle" attribute
     */
    org.apache.xmlbeans.XmlDecimal xgetFontStyle();
    
    /**
     * True if has "fontStyle" attribute
     */
    boolean isSetFontStyle();
    
    /**
     * Sets the "fontStyle" attribute
     */
    void setFontStyle(java.math.BigDecimal fontStyle);
    
    /**
     * Sets (as xml) the "fontStyle" attribute
     */
    void xsetFontStyle(org.apache.xmlbeans.XmlDecimal fontStyle);
    
    /**
     * Unsets the "fontStyle" attribute
     */
    void unsetFontStyle();
    
    /**
     * Gets the "lang" attribute
     */
    java.lang.String getLang();
    
    /**
     * Gets (as xml) the "lang" attribute
     */
    org.apache.xmlbeans.XmlString xgetLang();
    
    /**
     * Sets the "lang" attribute
     */
    void setLang(java.lang.String lang);
    
    /**
     * Sets (as xml) the "lang" attribute
     */
    void xsetLang(org.apache.xmlbeans.XmlString lang);
    
    /**
     * Gets the "type" attribute
     */
    org.thanlwinsoft.schemas.languagetest.module.LangTypeType.Enum getType();
    
    /**
     * Gets (as xml) the "type" attribute
     */
    org.thanlwinsoft.schemas.languagetest.module.LangTypeType xgetType();
    
    /**
     * Sets the "type" attribute
     */
    void setType(org.thanlwinsoft.schemas.languagetest.module.LangTypeType.Enum type);
    
    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.thanlwinsoft.schemas.languagetest.module.LangTypeType type);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.thanlwinsoft.schemas.languagetest.module.LangType newInstance() {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.thanlwinsoft.schemas.languagetest.module.LangType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.thanlwinsoft.schemas.languagetest.module.LangType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
