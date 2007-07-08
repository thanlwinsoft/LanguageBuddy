<?xml version="1.0" encoding="UTF-8"?>

<!--
    File:          $HeadURL: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/text/LanguageTest.xsl,v $
    Version:       $Revision: 711 $
    Last Modified: $Date: 2007-01-16 21:53:08 +0000 (Tue, 16 Jan 2007) $

    This is the XSLT file to transform the LanguageTest XML format into
    XSL-FO for formatting with Apache FOP.
    
 *  Copyright (C) 2007 Keith Stribley <tech@thanlwinsoft.org>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 *  MA 02110-1301 USA
 
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:fo="http://www.w3.org/1999/XSL/Format"  
xmlns:lan="http://www.thanlwinsoft.org/schemas/languagetest/module">

<xsl:output method="xml" indent="yes"/>

<!-- These parameters may be modified according to users wishes -->
<xsl:param name="title">LanguageTest</xsl:param>
<xsl:param name="colCount">1</xsl:param>
<xsl:param name="useImage">1</xsl:param>
<xsl:param name="pageSize">A4</xsl:param>
<xsl:param name="nativeLang"></xsl:param>
<xsl:param name="nativeLangDesc"></xsl:param>
<xsl:param name="foreignLang"></xsl:param>
<xsl:param name="foreignLangDesc"></xsl:param>

<xsl:attribute-set name="default-style">
  <xsl:attribute name="font-size">12pt</xsl:attribute>
  <xsl:attribute name="font-weight">normal</xsl:attribute>
  <xsl:attribute name="line-height">150%</xsl:attribute>
  <xsl:attribute name="text-align">left</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="table-header-style">
  <xsl:attribute name="font-size">12pt</xsl:attribute>
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="line-height">150%</xsl:attribute>
  <xsl:attribute name="text-align">left</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="footer">
  <xsl:attribute name="font-size">8pt</xsl:attribute>
  <xsl:attribute name="font-weight">normal</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="title">
	<xsl:attribute name="border-before-style">solid</xsl:attribute>
	<xsl:attribute name="border-before-width">medium</xsl:attribute>
	<xsl:attribute name="border-before-color">Silver</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">medium</xsl:attribute>
	<xsl:attribute name="border-after-color">Silver</xsl:attribute>
	<xsl:attribute name="font-size">14pt</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="span">all</xsl:attribute>
	<xsl:attribute name="padding-after">12pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="cell-border">
	<xsl:attribute name="border-before-style">solid</xsl:attribute>
	<xsl:attribute name="border-before-width">thin</xsl:attribute>
	<xsl:attribute name="border-before-color">Silver</xsl:attribute>
	<xsl:attribute name="padding">0.2em</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="image">
	<xsl:attribute name="text-align">center</xsl:attribute>
</xsl:attribute-set>

<xsl:template match="/">
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<fo:layout-master-set>
	  <fo:simple-page-master master-name="A4" 
	    page-height="297mm" page-width="210mm"
	    margin-top="1cm"   margin-bottom="1cm"
	    margin-left="1cm"  margin-right="1cm">
	  <fo:region-body region-name="xsl-region-body" margin="1cm" 
	  	column-count="{$colCount}" column-gap="0.5cm"
	  	margin-bottom="2cm" />
	  <fo:region-before extent="2cm"/>
	  <fo:region-after  extent="2cm"/>
	  <fo:region-start  extent="2cm"/>
	  <fo:region-end    extent="2cm"/>
	  
	  
	  </fo:simple-page-master>
	  <fo:simple-page-master master-name="A5" 
	    page-height="210mm" page-width="148mm"
	    margin-top="0.5cm"   margin-bottom="2cm"
	    margin-left="0.5cm"  margin-right="0.5cm">
	  <fo:region-body   margin="1cm" column-count="{$colCount}" 
	    column-gap="0.5cm" margin-bottom="2cm" />
	  <fo:region-before extent="2cm"/>
	  <fo:region-after  extent="2cm"/>
	  <fo:region-start  extent="2cm"/>
	  <fo:region-end    extent="2cm"/>
	  </fo:simple-page-master>
	  
	  <fo:simple-page-master master-name="Letter" 
	    page-height="11in" page-width="8.5in"
	    margin-top="1cm"   margin-bottom="2cm"
	    margin-left="1cm"  margin-right="1cm">
	  <fo:region-body   margin="1cm" column-count="{$colCount}" 
	  column-gap="0.5cm" margin-bottom="2cm" />
	  <fo:region-before extent="2cm"/>
	  <fo:region-after  extent="2cm"/>
	  <fo:region-start  extent="2cm"/>
	  <fo:region-end    extent="2cm"/>
	  </fo:simple-page-master>
	  <fo:simple-page-master master-name="Legal" 
	    page-height="14in" page-width="8.5in"
	    margin-top="1cm"   margin-bottom="2cm"
	    margin-left="1cm"  margin-right="1cm">
	  <fo:region-body   margin="1cm" column-count="{$colCount}" 
	    column-gap="0.5cm" margin-bottom="2cm" />
	  <fo:region-before extent="2cm"/>
	  <fo:region-after  extent="2cm"/>
	  <fo:region-start  extent="2cm"/>
	  <fo:region-end    extent="2cm"/>
	  </fo:simple-page-master>
	</fo:layout-master-set>
	<fo:page-sequence master-reference="{$pageSize}">
	<!-- 
		<fo:static-content flow-name="xsl-footnote-separator">
      		<fo:block text-align-last="justify">
				<fo:leader leader-pattern="rule"/>
  				Creators:
  				<fo:inline></fo:inline>
	        </fo:block>
    	</fo:static-content> -->
		<fo:static-content flow-name="xsl-region-after">
  			<fo:block text-align="center" space-before="1cm" xsl:use-attribute-sets="cell-border footer">
  				<fo:page-number />/<fo:page-number-citation-last ref-id="last"/>
  			</fo:block>
  			<fo:block text-align="center" xsl:use-attribute-sets="footer" 
  				font-style="italic">
  				Generated from a Language Test Module 
  				&lt;<fo:basic-link external-destination="http://www.thanlwinsoft.org/" 
  					text-decoration="underline">
  				<xsl:text>http://www.thanlwinsoft.org/</xsl:text>
  				</fo:basic-link>&gt; using Apache FOP.
  			</fo:block>
  		</fo:static-content>
  		<fo:flow flow-name="xsl-region-body">
  			<fo:block xsl:use-attribute-sets="title">
  				<xsl:value-of select="$title"/>
  			</fo:block>
  			<fo:table table-layout="fixed" width="100%" id="langTable">
  				<xsl:choose>
  				<xsl:when test="$useImage>0 and count(//lan:Img)>0">
	  				<fo:table-column column-width="33%"/>
					<fo:table-column column-width="33%"/>
					<fo:table-column column-width="33%"/>
				</xsl:when>
				<xsl:otherwise>
					<fo:table-column column-width="50%"/>
					<fo:table-column column-width="50%"/>
				</xsl:otherwise>
				</xsl:choose>
				<fo:table-header>
	                <fo:table-row>
		                <fo:table-cell >
			                <fo:block xsl:use-attribute-sets="table-header-style">
			                	<xsl:value-of select="$nativeLangDesc"/>
			            	</fo:block>
		                
						</fo:table-cell>
						<fo:table-cell >
			            	<fo:block xsl:use-attribute-sets="table-header-style">
				                <xsl:value-of select="$foreignLangDesc"/>
			            	</fo:block>
	                	</fo:table-cell>
						<xsl:if test="$useImage>0 and count(//lan:Img)>0">
						<fo:table-cell>
		                	<fo:block xsl:use-attribute-sets="table-header-style">Image</fo:block>
		                </fo:table-cell>	
		                </xsl:if>
						
	                </fo:table-row>
				</fo:table-header>
				<fo:table-body> 
					<xsl:apply-templates select="lan:LanguageModule" />
				</fo:table-body>
            </fo:table>
            <fo:block id="last"></fo:block>
  		</fo:flow>
  	</fo:page-sequence>
</fo:root>
</xsl:template>

<xsl:template match="lan:LanguageModule">
    <xsl:for-each select="lan:TestItem" xml:space="preserve" >
        <fo:table-row>
        	
            <fo:table-cell column-number="1" xsl:use-attribute-sets="cell-border">
                <xsl:element name="fo:block" xml:space="default" 
	            	use-attribute-sets="default-style">
	                <xsl:attribute name="font-family">
	                <xsl:call-template name="findFont">
	                    <xsl:with-param name="lang"><xsl:value-of select="$nativeLang"/></xsl:with-param>
	                </xsl:call-template>
	                </xsl:attribute>
	                <xsl:value-of select="lan:NativeLang[@lang=$nativeLang]"/>
		    	</xsl:element>
	    	</fo:table-cell>
            <fo:table-cell column-number="2" xsl:use-attribute-sets="cell-border">
                <xsl:element name="fo:block" xml:space="default"
                	use-attribute-sets="default-style">
                    <xsl:attribute name="font-family">
                    <xsl:call-template name="findFont">
                    <xsl:with-param name="lang"><xsl:value-of select="$foreignLang"/></xsl:with-param>
                    </xsl:call-template>
                    </xsl:attribute>
	                <xsl:value-of select="lan:ForeignLang[@lang=$foreignLang]"/>
                </xsl:element>
            </fo:table-cell>
            <xsl:if test="$useImage>0 and count(lan:Img)>0">
                <fo:table-cell column-number="3" xsl:use-attribute-sets="cell-border">
	                <fo:block xsl:use-attribute-sets="image">
	                	<fo:external-graphic src="file:{translate(lan:Img,'\','/')}"
	                		content-width="scale-down-to-fit" />
	                </fo:block>
                </fo:table-cell>
            </xsl:if>
        </fo:table-row>
    </xsl:for-each>
</xsl:template>

<xsl:template name="stripBrackets">
<xsl:param name="lang"/>
<xsl:value-of select="translate($lang,'[]','__')"/>
</xsl:template>


<xsl:template name="findFont">
<xsl:param name="lang"/>
<xsl:value-of select="/lan:LanguageModule/lan:Lang[@lang=$lang]/@font"/>
</xsl:template>

<xsl:template match="lan:TestItem">
<fo:block>
    <xsl:value-of select="@creationTime"/>
</fo:block>
</xsl:template>

</xsl:stylesheet>
