/*
 * XML Type:  ModuleHistoryType
 * Namespace: http://www.thanlwinsoft.org/schemas/languagetest/history
 * Java type: org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType
 *
 * Automatically generated - do not modify.
 */
package org.thanlwinsoft.schemas.languagetest.history;


/**
 * An XML ModuleHistoryType(@http://www.thanlwinsoft.org/schemas/languagetest/history).
 *
 * This is a complex type.
 */
public interface ModuleHistoryType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ModuleHistoryType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6F3EA0086926FB997C5F1778DC1BD8C6").resolveHandle("modulehistorytype7a88type");
    
    /**
     * Gets array of all "Item" elements
     */
    org.thanlwinsoft.schemas.languagetest.history.ItemType[] getItemArray();
    
    /**
     * Gets ith "Item" element
     */
    org.thanlwinsoft.schemas.languagetest.history.ItemType getItemArray(int i);
    
    /**
     * Returns number of "Item" element
     */
    int sizeOfItemArray();
    
    /**
     * Sets array of all "Item" element
     */
    void setItemArray(org.thanlwinsoft.schemas.languagetest.history.ItemType[] itemArray);
    
    /**
     * Sets ith "Item" element
     */
    void setItemArray(int i, org.thanlwinsoft.schemas.languagetest.history.ItemType item);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Item" element
     */
    org.thanlwinsoft.schemas.languagetest.history.ItemType insertNewItem(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Item" element
     */
    org.thanlwinsoft.schemas.languagetest.history.ItemType addNewItem();
    
    /**
     * Removes the ith "Item" element
     */
    void removeItem(int i);
    
    /**
     * Gets the "path" attribute
     */
    java.lang.String getPath();
    
    /**
     * Gets (as xml) the "path" attribute
     */
    org.apache.xmlbeans.XmlString xgetPath();
    
    /**
     * True if has "path" attribute
     */
    boolean isSetPath();
    
    /**
     * Sets the "path" attribute
     */
    void setPath(java.lang.String path);
    
    /**
     * Sets (as xml) the "path" attribute
     */
    void xsetPath(org.apache.xmlbeans.XmlString path);
    
    /**
     * Unsets the "path" attribute
     */
    void unsetPath();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType newInstance() {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
