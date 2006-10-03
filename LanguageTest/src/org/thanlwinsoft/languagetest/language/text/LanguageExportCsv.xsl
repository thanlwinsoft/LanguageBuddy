<?xml version="1.0" encoding="UTF-8"?>

<!--
    File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/text/LanguageExportCsv.xsl,v $
    Version:       $Revision: 1.3 $
    Last Modified: $Date: 2004/12/18 05:11:26 $

    This is the XSLT file to transform the LanguageTest XML format into
    text delimited format for import into other programs.
    
    Copyright (C) 2004 Keith Stribley <jungleglacier@snc.co.uk>
      
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
  
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" />

<xsl:template match="/"><xsl:apply-templates/>
</xsl:template>

<xsl:template match="LanguageModule">#<xsl:value-of select="Lang[@type='native']" />
#<xsl:value-of select="Lang[@type='native']/@lang"/>,<xsl:value-of select="Lang[@type='foreign']/@lang"/>,Audio,Picture
<xsl:for-each select="TestItem" xml:space="preserve"><xsl:apply-templates select="NativeLang"/>,<xsl:apply-templates select="ForeignLang"/>,<xsl:apply-templates select="SoundFile"/>,<xsl:apply-templates select="Img"/><xsl:text>
</xsl:text></xsl:for-each>
<!--
<xsl:for-each select="TestItem" xml:space="preserve"><xsl:apply-templates select="NativeLang"/>,<xsl:apply-templates select="ForeignLang"/>,<xsl:apply-templates select="SoundFile"/>,<xsl:apply-templates select="Img"/>
</xsl:for-each>
-->

<!--
<xsl:for-each select="TestItem" >"<xsl:apply-templates /><xsl:value-of select="NativeLang" disable-output-escaping="yes"/>","<xsl:value-of select="ForeignLang" disable-output-escaping="yes"/>","<xsl:value-of select="SoundFile" disable-output-escaping="yes"/>"

</xsl:for-each>-->
</xsl:template>


<xsl:template match="NativeLang">"<xsl:apply-templates />"</xsl:template>

<xsl:template match="ForeignLang">"<xsl:apply-templates />"</xsl:template>

<xsl:template match="SoundFile">"<xsl:apply-templates />"</xsl:template>

<xsl:template match="Img">"<xsl:apply-templates />"</xsl:template>


<xsl:template match="text()">
<xsl:call-template name="quoteQuotes">
    <xsl:with-param name="content"><xsl:value-of select="." /></xsl:with-param>
</xsl:call-template>
</xsl:template>

<xsl:template name="quoteQuotes">
<xsl:param name="content"></xsl:param>
<xsl:call-template name="replace">
    <xsl:with-param name="findString">"</xsl:with-param>
    <xsl:with-param name="replaceString">""</xsl:with-param>
    <xsl:with-param name="checked"></xsl:with-param>
    <xsl:with-param name="remaining"><xsl:value-of select="$content"/></xsl:with-param>
</xsl:call-template>
</xsl:template>

<xsl:template name="replace">
<xsl:param name="findString"></xsl:param>
<xsl:param name="replaceString"></xsl:param>
<xsl:param name="checked"></xsl:param>
<xsl:param name="remaining"></xsl:param>
<xsl:choose>
<xsl:when test="contains($remaining,$findString)">
<xsl:call-template name="replace">
    <xsl:with-param name="findString"><xsl:value-of select="$findString"/></xsl:with-param>
    <xsl:with-param name="replaceString"><xsl:value-of select="$replaceString"/></xsl:with-param>
    <xsl:with-param name="checked"><xsl:value-of select="$checked"/><xsl:value-of select="substring-before($remaining,$findString)"/><xsl:value-of select="$replaceString"/></xsl:with-param>
    <xsl:with-param name="remaining"><xsl:value-of select="substring-after($remaining,$findString)"/></xsl:with-param>
</xsl:call-template>
</xsl:when>
<xsl:otherwise><xsl:value-of select="$checked"/><xsl:value-of select="$remaining"/></xsl:otherwise>
</xsl:choose>
</xsl:template>


</xsl:stylesheet>
