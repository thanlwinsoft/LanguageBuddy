<?xml version="1.0" encoding="UTF-8"?>

<!--
    File:          $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/xslt/LanguageTest.xsl $
    Version:       $Revision: 1390 $
    Last Modified: $Date: 2009-01-31 19:41:46 +0700 (Sat, 31 Jan 2009) $

    This is the XSLT file to transform the LanguageTest XML format into
    HTML for viewing in a web browser.
    
 *  Copyright (C) 2003,2007 Keith Stribley <tech@thanlwinsoft.org>
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

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"  xmlns:lan="http://www.thanlwinsoft.org/schemas/languagetest/module">

<xsl:template match="/">
	<html>
        <head>
            <title><xsl:text>Language Test Module</xsl:text>
            <!-- <xsl:for-each select="lan:LanguageModule/lan:Lang[@type='native']">
            <xsl:value-of select="."/>
            </xsl:for-each>
             -->
            </title>
            <style>
	        body {
                    background-color: #B0F0B0;
					margin: 1em;
                }
                .Native {
                    border: 1px solid black;
                    width: 40%;
                }
                .Foreign {
                    border: 1px solid black;
                    width: 40%;   
                    background-color: #B0F0B0;             
                }
                p.footer {
                    font-size: xx-small;
                    text-align: right;
                }
				
                <xsl:for-each select="lan:LanguageModule/lan:Lang">
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
                    background-color: #C0F0C0;
					clear: both;
                    }
                th {
                    border: 1px solid black;
					cursor: n-resize;
                }
				td {
					cursor: help;
				}
				.toggle {
					text-align: center;
					width: 1.2em;
					height: 1.2em;
					margin: 0px;
					padding: 0px;
					left: -1em;
					top: 0em;
					position: relative;
					/*
					float: left;
					margin-bottom: 0em;
					border: 1px solid black;
					*/
					font-size: large;
					cursor: pointer;
				}
				.noPrint {
					font-size: xx-small;
					cursor: pointer;
				}
				 @media print { .toggle, .noPrint { display: none; }}
            </style>
			<script type="text/javascript">
			function setTitle(element, author, time)
			{
				var date = new Date();
				date.setTime(time);
				element.setAttribute("title","Created by:" + author + " on " + date.toLocaleString());
			}
			function colFromRow(row, col)
			{
				var cells = row.getElementsByTagName("td");
				if (cells.length &lt;= col)
					return null;
				return cells.item(col);
			}
			
			function hideColumn(col, hide)
			{
				var displayStyle = "visible";
				if (hide) displayStyle = "hidden";
				var table = document.getElementById("langTable");
				if (table)
				{
					var rows = table.getElementsByTagName("tr");
					for (var i = 0; i &lt; rows.length; i++)
					{
						var row = rows.item(i);
						var cells = row.getElementsByTagName("td");
						if (cells.length &lt;= col) continue;
						var spans = cells.item(col).getElementsByTagName("span");
						for (var j = 0; j &lt;  spans.length; j++)
						{
							spans.item(j).style.visibility = displayStyle;
						}
					}
				}
			}
			function toggle(element)
			{
				var spans = element.getElementsByTagName("span");
				for (var i = 0; i &lt; spans.length; i++)
				{
					if (spans.item(i).style.visibility == "hidden")
					{
						spans.item(i).style.visibility = "visible";
					}
					else
					{
						spans.item(i).style.visibility = "hidden";
					}
				}
			}
			function toggleDiv(toggle, divId)
			{
				var div = document.getElementById(divId);
				if (div)
				{
					if (div.style.display == "none")
					{
						div.style.display = "";
						toggle.innerHTML = "&#x229F;";
					}
					else
					{
						div.style.display = "none";
						toggle.innerHTML = "&#x229E;";
					}
				}
			}
			function cfRow(a, b) { return (a[0] &lt; b[0])? -1 : ((a[0] &gt; b[0])? 1 : 0)}
			
			function randomizeRows()
			{
				sortRows(-1);
			}
			function sortRows(column)
			{
				var table = document.getElementById("langTable");
				if (table)
				{
					var rows = table.getElementsByTagName("tr");
					var sortArray = new Array(rows.length); 
					for (var i = rows.length - 1; i &gt;= 0; i--)
					{
						sortArray[i] = new Array();
						if (column == -1)
						{
							sortArray[i][0] = Math.random();
						}
						else 
						{
							var cell = colFromRow(rows.item(i), column);
							var text = new String(cell.textContent);
							if (text == "undefined") 
							{
								text = cell.getElementsByTagName("span").item(0).innerHTML;
							}
							sortArray[i][0] = text.toLowerCase();
						}
						sortArray[i][1] = rows.item(i).cloneNode(true);
						
						var parent = rows.item(i).parentNode;
						parent.removeChild(rows.item(i));
					}
					if (column > -1)
						sortArray.sort();
					else
						sortArray.sort(cfRow);
					
					for (var i = 0; i &lt; sortArray.length; i++)
					{
						var node = sortArray[i][1];
						table.appendChild(node);
					}
				}
			}
			function setTitle()
			{
				var name = new String(document.location);
            	var lastSlash = name.lastIndexOf('/');
            	if (lastSlash == -1)
            		lastSlash = name.lastIndexOf('\\');
            	var lastDot = name.lastIndexOf('.');
            	name = name.substring(lastSlash + 1, lastDot);
            	document.getElementById('title').innerHTML = name;
			}
			</script>
        </head>
        <body onload="setTitle()">
            <h1 id="title">
            	<!-- 
                <xsl:for-each select="lan:LanguageModule/lan:Lang[@type='native']">
                <xsl:value-of select="."/><br />
                </xsl:for-each>
                -->
            </h1>
            <div>
			<p onclick="toggleDiv(this,'instructions')" class="toggle">&#x229F;</p>
			</div>
			<div id="instructions">
			<p>This is a Test Module for <a href="http://www.thanlwinsoft.org/">Language Buddy</a>. 
			If you want the full features of Language Buddy, please reopen inside the Language Buddy software.</p>
			<p>Once you have learnt these words you can test yourself:</p>
			<p>
			<ol><li><a href="javascript:randomizeRows()">Randomize the word order</a></li>
			<li>Hide a column in one language by clicking [Hide] in the column title.</li>
			<li>Try to remember what the first hidden word was.</li>
			<li>Click on the table cell to reveal the answer.</li>
			<li>If you get answer wrong, click to hide it again and go to the next word.</li>
			<li>Go down the list trying to remember the words.</li>
			<li>When you get to the bottom <a href="javascript:randomizeRows()">randomize the order</a> again and go back over the words that are still hidden.</li>
			</ol>
			</p>
			</div>
            <table>
				<thead>
	                <tr><th class="Native" onclick="sortRows(0)" title="Click to sort">
					<xsl:for-each select="lan:LanguageModule/lan:Lang[@type='native']">
	                <xsl:value-of select="@lang"/>
	                </xsl:for-each>
					<xsl:text> </xsl:text>
	                <span onclick="hideColumn(0, true)" class="noPrint" title="Hide Column">[Hide]</span>
					<span onclick="hideColumn(0, false)" class="noPrint" title="Show Column">[Show]</span>
	                </th><th class="Foreign" onclick="sortRows(1)" title="Click to sort">
					<xsl:for-each select="lan:LanguageModule/lan:Lang[@type='foreign']">
	                <xsl:value-of select="@lang"/>
	                </xsl:for-each>
					<xsl:text> </xsl:text>
	                <span onclick="hideColumn(1, true)" class="noPrint" title="Hide Column">[Hide]</span>
					<span onclick="hideColumn(1, false)" class="noPrint" title="Show Column">[Show]</span>
	                </th><th>Image</th></tr>
				</thead>
				<tbody id="langTable"> 
					<xsl:apply-templates select="lan:LanguageModule" />
				</tbody>
            </table>
			
            <p class="footer">This page was generated from a LanguageTest module</p>
        </body>
	</html>

</xsl:template>

<xsl:template match="lan:LanguageModule">
    <xsl:for-each select="lan:TestItem" xml:space="preserve" >
        <tr>
            <td class="Native" onclick="toggle(this)">
	    <xsl:for-each select="lan:NativeLang">
            <xsl:element name="span" xml:space="default">
                <xsl:attribute name="class">
                <xsl:call-template name="stripBrackets">
                    <xsl:with-param name="lang"><xsl:value-of select="@lang"/></xsl:with-param>
                </xsl:call-template>
                </xsl:attribute>
                <xsl:attribute name="title">
                	<xsl:text>Created by </xsl:text><xsl:value-of select="../@creator"/>
                </xsl:attribute>
				<xsl:attribute name="onmouseover">
                	<xsl:text>setTitle(this,'</xsl:text><xsl:value-of select="../@creator"/><xsl:text>','</xsl:text>
					<xsl:value-of select="../@creationTime"/><xsl:text>')</xsl:text>
                </xsl:attribute>
                   <xsl:value-of select="."/>
	    	</xsl:element><br />
	    </xsl:for-each>
	    </td>
            <td class="Foreign" onclick="toggle(this)">
                <xsl:for-each select="lan:ForeignLang">
                <xsl:element name="span" xml:space="default">
                    <xsl:attribute name="class">
                    <xsl:call-template name="stripBrackets">
                    <xsl:with-param name="lang"><xsl:value-of select="@lang"/></xsl:with-param>
                    </xsl:call-template>
                    </xsl:attribute>
                    <xsl:choose>
                        <xsl:when test="count(../lan:SoundFile)>0">
                            <xsl:element name="a" xml:space="default">
                                <xsl:attribute name="href"><xsl:value-of select="../lan:SoundFile"/></xsl:attribute>
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
            <xsl:if test="count(lan:Img)>0">
                <td class="Img">
                    <xsl:element name="img" xml:space="default">
                        <xsl:attribute name="src"><xsl:value-of select="lan:Img"/></xsl:attribute>
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
