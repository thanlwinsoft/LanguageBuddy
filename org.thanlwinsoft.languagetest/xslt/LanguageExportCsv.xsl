<?xml version="1.0" encoding="UTF-8"?>

<!--
    File:          $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/xslt/LanguageExportCsv.xsl $
    Version:       $Revision: 1390 $
    Last Modified: $Date: 2009-01-31 19:41:46 +0700 (Sat, 31 Jan 2009) $

    This is the XSLT file to transform the LanguageTest XML format into
    text delimited format for import into other programs.
    
 *  Copyright (C) 2004 Keith Stribley <tech@thanlwinsoft.org>
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
<xsl:output method="text" />

<xsl:template match="/"><xsl:apply-templates/>
</xsl:template>

<xsl:template match="lan:LanguageModule">
<xsl:for-each select="//lan:Lang[@type='native']">
<xsl:text>"</xsl:text><xsl:value-of select="@lang"/><xsl:text>",</xsl:text>
</xsl:for-each>
<xsl:for-each select="//lan:Lang[@type='foreign']">
<xsl:text>"</xsl:text><xsl:value-of select="@lang"/><xsl:text>",</xsl:text>
</xsl:for-each>
<xsl:text>"Audio","Image"
</xsl:text>
<xsl:for-each select="//lan:Lang[@type='native']">
<xsl:text>"</xsl:text><xsl:value-of select="@font"/><xsl:text>",</xsl:text>
</xsl:for-each>
<xsl:for-each select="//lan:Lang[@type='foreign']">
<xsl:text>"</xsl:text><xsl:value-of select="@font"/><xsl:text>",</xsl:text>
</xsl:for-each>
<xsl:text>"",""

</xsl:text>
<xsl:for-each select="lan:TestItem">
<xsl:variable name="item" select="."/>
<xsl:for-each select="//lan:Lang[@type='native']">
<xsl:variable name="lang" select="@lang"/>
<xsl:text>"</xsl:text><xsl:apply-templates select="$item/lan:NativeLang[@lang=$lang]"/><xsl:text>",</xsl:text>
</xsl:for-each>
<xsl:for-each select="//lan:Lang[@type='foreign']">
<xsl:variable name="lang" select="@lang"/>
<xsl:text>"</xsl:text><xsl:apply-templates select="$item/lan:ForeignLang[@lang=$lang]"/><xsl:text>",</xsl:text>
</xsl:for-each>
<xsl:apply-templates select="lan:SoundFile"/><xsl:text>,</xsl:text>
<xsl:apply-templates select="lan:Img"/><xsl:text>
</xsl:text>
</xsl:for-each>
</xsl:template>


<xsl:template match="lan:NativeLang"><xsl:apply-templates /></xsl:template>

<xsl:template match="lan:ForeignLang"><xsl:apply-templates /></xsl:template>

<xsl:template match="lan:SoundFile"><xsl:apply-templates /></xsl:template>

<xsl:template match="lan:Img"><xsl:apply-templates /></xsl:template>


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
