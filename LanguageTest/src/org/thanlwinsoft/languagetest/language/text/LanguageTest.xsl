<?xml version="1.0" encoding="UTF-8"?>

<!--
    File:          $Source: /home/keith/cvsroot/projects/LanguageAids/uk/co/dabsol/stribley/language/text/LanguageTest.xsl,v $
    Version:       $Revision: 1.3 $
    Last Modified: $Date: 2004/12/18 05:11:26 $

    This is the XSLT file to transform the LanguageTest XML format into
    HTML for viewing in a web browser.
    
    Copyright (C) 2003 Keith Stribley <jungleglacier@snc.co.uk>
      
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

<xsl:template match="/">
	<html>
        <head>
            <title>
            <xsl:for-each select="LanguageModule/Lang[@type='native']">
            <xsl:value-of select="."/>
            </xsl:for-each>
            </title>
            <style>
	        body {
                    background-color: B0B0F0;
                }
                .Native {
                    border: 1px solid black;
                    width: 40%;
                }
                .Foreign {
                    border: 1px solid black;
                    width: 40%;   
                    background-color: B0B0F0;             
                }
                p.footer {
                    font-size: xx-small;
                    text-align: right;
                }
                <xsl:for-each select="LanguageModule/Lang">
                .<xsl:call-template name="stripBrackets">
                    <xsl:with-param name="lang"><xsl:value-of select="@lang"/></xsl:with-param>
                 </xsl:call-template> {
                    font-family: <xsl:value-of select="@font"/>;
                }
		</xsl:for-each>
                .Img {
                    border: 1px solid black;
                    width: 20%;                    
                    }
                table {
                    border-collapse: collapse;
                    border: 3px solid black;
                    width: 100%;
                    text-align:center;
                    rules: cols;
                    background-color: C0C0F0;
                    }
                th {
                    border: 1px solid black;
                }
            </style>
        </head>
        <body>
            <h1>
                <xsl:for-each select="LanguageModule/Lang[@type='native']">
                <xsl:value-of select="."/><br />
                </xsl:for-each>
            </h1>
            <table>
                <tr><th class="Native">
                <xsl:for-each select="LanguageModule/Lang[@type='native']">
                <xsl:value-of select="@lang"/>
                </xsl:for-each>
                </th><th class="Foreign">
                <xsl:for-each select="LanguageModule/Lang[@type='foreign']">
                <xsl:value-of select="@lang"/>
                </xsl:for-each>
                </th><th>Image</th></tr>
                <xsl:apply-templates select="LanguageModule" />
            </table>
            <p class="footer">This file was generated from a LanguageTest module</p>
        </body>
	</html>

</xsl:template>

<xsl:template match="LanguageModule">
    <xsl:for-each select="TestItem" xml:space="preserve" >
        <tr>
            <td class="Native">
	    <xsl:for-each select="NativeLang">
            <xsl:element name="span" xml:space="default">
                <xsl:attribute name="class">
                <xsl:call-template name="stripBrackets">
                    <xsl:with-param name="lang"><xsl:value-of select="@lang"/></xsl:with-param>
                </xsl:call-template>
                </xsl:attribute>
                   <xsl:value-of select="."/>
	    </xsl:element><br />
	    </xsl:for-each>
	    </td>
            <td class="Foreign">
                <xsl:for-each select="ForeignLang">
                <xsl:element name="span" xml:space="default">
                    <xsl:attribute name="class">
                    <xsl:call-template name="stripBrackets">
                    <xsl:with-param name="lang"><xsl:value-of select="@lang"/></xsl:with-param>
                    </xsl:call-template>
                    </xsl:attribute>
                    <xsl:choose>
                        <xsl:when test="count(../SoundFile)>0">
                            <xsl:element name="a" xml:space="default">
                                <xsl:attribute name="href"><xsl:value-of select="../SoundFile"/></xsl:attribute>
                                <xsl:value-of select="."/>
                            </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="."/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:element><br />
                </xsl:for-each>
            </td>
            <xsl:if test="count(Img)>0">
                <td class="Img">
                    <xsl:element name="img" xml:space="default">
                        <xsl:attribute name="src"><xsl:value-of select="Img"/></xsl:attribute>
                    </xsl:element>
                </td>
            </xsl:if>
        </tr>
    </xsl:for-each>
</xsl:template>

<xsl:template name="stripBrackets">
<xsl:param name="lang"/>
<xsl:value-of select="translate($lang,'[]','__')"/>
</xsl:template>

</xsl:stylesheet>
