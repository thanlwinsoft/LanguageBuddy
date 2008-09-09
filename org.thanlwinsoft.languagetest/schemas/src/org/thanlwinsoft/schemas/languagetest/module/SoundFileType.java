/*
 * XML Type:  SoundFileType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/module
 * Java type: org.thanlwinsoft.schemas.languagetest.module.SoundFileType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.module;


/**
 * An XML SoundFileType(@http://www.thanlwinsoft.org/schemas/languagetest/module).
 *
 * This is an atomic type that is a restriction of org.thanlwinsoft.schemas.languagetest.module.SoundFileType.
 */
public interface SoundFileType extends org.apache.xmlbeans.XmlString
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SoundFileType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6F3EA0086926FB997C5F1778DC1BD8C6").resolveHandle("soundfiletype259btype");
    
    /**
     * Gets the "start" attribute
     */
    long getStart();
    
    /**
     * Gets (as xml) the "start" attribute
     */
    org.apache.xmlbeans.XmlLong xgetStart();
    
    /**
     * True if has "start" attribute
     */
    boolean isSetStart();
    
    /**
     * Sets the "start" attribute
     */
    void setStart(long start);
    
    /**
     * Sets (as xml) the "start" attribute
     */
    void xsetStart(org.apache.xmlbeans.XmlLong start);
    
    /**
     * Unsets the "start" attribute
     */
    void unsetStart();
    
    /**
     * Gets the "end" attribute
     */
    long getEnd();
    
    /**
     * Gets (as xml) the "end" attribute
     */
    org.apache.xmlbeans.XmlLong xgetEnd();
    
    /**
     * True if has "end" attribute
     */
    boolean isSetEnd();
    
    /**
     * Sets the "end" attribute
     */
    void setEnd(long end);
    
    /**
     * Sets (as xml) the "end" attribute
     */
    void xsetEnd(org.apache.xmlbeans.XmlLong end);
    
    /**
     * Unsets the "end" attribute
     */
    void unsetEnd();
    
    /**
     * Gets the "length" attribute
     */
    long getLength();
    
    /**
     * Gets (as xml) the "length" attribute
     */
    org.apache.xmlbeans.XmlLong xgetLength();
    
    /**
     * True if has "length" attribute
     */
    boolean isSetLength();
    
    /**
     * Sets the "length" attribute
     */
    void setLength(long length);
    
    /**
     * Sets (as xml) the "length" attribute
     */
    void xsetLength(org.apache.xmlbeans.XmlLong length);
    
    /**
     * Unsets the "length" attribute
     */
    void unsetLength();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType newInstance() {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.thanlwinsoft.schemas.languagetest.module.SoundFileType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.thanlwinsoft.schemas.languagetest.module.SoundFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
