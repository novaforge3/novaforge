<?xml version='1.0'?>
 
<!DOCTYPE xsl:stylesheet [
<!ENTITY lowercase "'abcdefghijklmnopqrstuvwxyz'">
<!ENTITY uppercase "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'">
 ]>

<!--
  * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
  *
  * This file is free software: you may redistribute and/or modify it under
  * the terms of the GNU Affero General Public License as published by the
  * Free Software Foundation, version 3 of the License.
  *
  * This file is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty
  * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  * See the GNU Affero General Public License for more details.
  * You should have received a copy of the GNU Affero General Public License
  * along with this program. If not, see http://www.gnu.org/licenses.
  *
  * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
  *
  * If you modify this Program, or any covered work, by linking or combining
  * it with libraries listed in COPYRIGHT file at the top-level directory of
  * this distribution (or a modified version of that libraries), containing parts
  * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
  * of this Program grant you additional permission to convey the resulting work.
  -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:fo="http://www.w3.org/1999/XSL/Format"
		version='1.0'
		xmlns="http://www.w3.org/TR/xhtml1/transitional"
		exclude-result-prefixes="#default">

<xsl:import href="http://docbook.sourceforge.net/release/xsl/1.72.0/fo/docbook.xsl"/>
<xsl:import href="http://docbook.sourceforge.net/release/xsl/1.72.0/fo/graphics.xsl"/>
<xsl:import href="redhat.xsl"/>
<xsl:param name="alignment">left</xsl:param>
<xsl:param name="use.extensions" select="0"/>
<xsl:param name="tablecolumns.extensions" select="0"/>
<xsl:param name="fop.extensions" select="1"/>
<xsl:param name="fop1.extensions" select="0"/>
<xsl:param name="img.src.path"/>
<xsl:param name="confidential" select="0"/>
<xsl:param name="qandadiv.autolabel" select="0"/>

<xsl:param name="hyphenation-character">-</xsl:param>

<!--xsl:param name="hyphenate.verbatim" select="0"/-->
<xsl:param name="hyphenate">false</xsl:param>
<!--xsl:param name="ulink.hyphenate" select="1"/-->

<xsl:param name="line-height" select="1.5"/>

<xsl:attribute-set name="xref.properties">
  <xsl:attribute name="font-style">italic</xsl:attribute>
  <xsl:attribute name="color">
	<xsl:choose>
		<xsl:when test="ancestor::note or ancestor::caution or ancestor::important or ancestor::warning or ancestor::tip">
			<xsl:text>#aee6ff</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text>#0066cc</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
  </xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="monospace.properties">
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="font-family">
		<xsl:value-of select="$monospace.font.family"/>
	</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="monospace.verbatim.properties" use-attribute-sets="verbatim.properties monospace.properties">
	<xsl:attribute name="text-align">start</xsl:attribute>
	<xsl:attribute name="wrap-option">wrap</xsl:attribute>
	<xsl:attribute name="hyphenation-character">&#x25BA;</xsl:attribute>
</xsl:attribute-set>

<xsl:param name="shade.verbatim" select="1"/>
<xsl:attribute-set name="shade.verbatim.style">
  <xsl:attribute name="wrap-option">wrap</xsl:attribute>
  <xsl:attribute name="background-color">
	<xsl:choose>
		<xsl:when test="ancestor::note or ancestor::caution or ancestor::important or ancestor::warning or ancestor::tip">
			<xsl:text>#333333</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text>#CCFF99</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
  </xsl:attribute>
  <xsl:attribute name="color">
	<xsl:choose>
		<xsl:when test="ancestor::note or ancestor::caution or ancestor::important or ancestor::warning or ancestor::tip">
			<xsl:text>white</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text>black</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
  </xsl:attribute>
	<xsl:attribute name="padding-left">12pt</xsl:attribute>
	<xsl:attribute name="padding-right">12pt</xsl:attribute>
	<xsl:attribute name="padding-top">6pt</xsl:attribute>
	<xsl:attribute name="padding-bottom">6pt</xsl:attribute>
	<xsl:attribute name="margin-left">
		<xsl:value-of select="$title.margin.left"/>
	</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="verbatim.properties">
  <xsl:attribute name="space-before.minimum">0.8em</xsl:attribute>
  <xsl:attribute name="space-before.optimum">1em</xsl:attribute>
  <xsl:attribute name="space-before.maximum">1.2em</xsl:attribute>
  <xsl:attribute name="space-after.minimum">0.8em</xsl:attribute>
  <xsl:attribute name="space-after.optimum">1em</xsl:attribute>
  <xsl:attribute name="space-after.maximum">1.2em</xsl:attribute>
  <xsl:attribute name="hyphenate">false</xsl:attribute>
  <xsl:attribute name="wrap-option">wrap</xsl:attribute>
  <xsl:attribute name="white-space-collapse">false</xsl:attribute>
  <xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
  <xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
  <xsl:attribute name="text-align">start</xsl:attribute>
</xsl:attribute-set>

<!-- Admonitions -->
<xsl:param name="admon.graphics" select="1"/>
<xsl:param name="admon.graphics.path">
	<xsl:if test="$img.src.path != ''"><xsl:value-of select="$img.src.path"/></xsl:if><xsl:text>images/</xsl:text>
</xsl:param>
<xsl:param name="admon.graphics.extension" select="'.svg'"/>
<xsl:attribute-set name="admonition.title.properties">
	<xsl:attribute name="font-size">13pt</xsl:attribute>
	<xsl:attribute name="color">white</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="hyphenate">false</xsl:attribute>
	<xsl:attribute name="keep-with-next.within-column">always</xsl:attribute>

</xsl:attribute-set>

<!--xsl:attribute-set name="admonition.properties"></xsl:attribute-set-->

<xsl:attribute-set name="graphical.admonition.properties">
	<xsl:attribute name="color">white</xsl:attribute>
	<xsl:attribute name="background-color">#404040</xsl:attribute>
	<xsl:attribute name="space-before.optimum">1em</xsl:attribute>
	<xsl:attribute name="space-before.minimum">0.8em</xsl:attribute>
	<xsl:attribute name="space-before.maximum">1.2em</xsl:attribute>
	<xsl:attribute name="space-after.optimum">1em</xsl:attribute>
	<xsl:attribute name="space-after.minimum">0.8em</xsl:attribute>
	<xsl:attribute name="space-after.maximum">1em</xsl:attribute>
	<xsl:attribute name="padding-bottom">12pt</xsl:attribute>
	<xsl:attribute name="padding-top">12pt</xsl:attribute>
	<xsl:attribute name="padding-right">12pt</xsl:attribute>
	<xsl:attribute name="padding-left">12pt</xsl:attribute>
	<xsl:attribute name="margin-left">
		<xsl:value-of select="$title.margin.left"/>
	</xsl:attribute>
</xsl:attribute-set>

<xsl:param name="generate.toc">
set toc
book toc
article toc
</xsl:param>

<xsl:param name="toc.section.depth">3</xsl:param>
<xsl:param name="section.autolabel" select="1"/>

<xsl:param name="callout.graphics.path">
    <xsl:if test="$img.src.path != ''"><xsl:value-of select="$img.src.path"/></xsl:if><xsl:text>images/</xsl:text>
</xsl:param>

<!-- Format Variable Lists as Blocks (prevents horizontal overflow). -->
<xsl:param name="variablelist.as.blocks">1</xsl:param>

<!-- The horrible list spacing problems, this is much better. -->
<xsl:attribute-set name="list.block.spacing">
	<xsl:attribute name="space-before.optimum">2em</xsl:attribute>
	<xsl:attribute name="space-before.minimum">1em</xsl:attribute>
	<xsl:attribute name="space-before.maximum">3em</xsl:attribute>
	<xsl:attribute name="space-after.optimum">0.1em</xsl:attribute>
	<xsl:attribute name="space-after.minimum">0.1em</xsl:attribute>
	<xsl:attribute name="space-after.maximum">0.1em</xsl:attribute>
</xsl:attribute-set>

<!-- Some padding inside tables -->
<xsl:attribute-set name="table.cell.padding">
<xsl:attribute name="padding-left">4pt</xsl:attribute>
<xsl:attribute name="padding-right">4pt</xsl:attribute>
<xsl:attribute name="padding-top">2pt</xsl:attribute>
<xsl:attribute name="padding-bottom">2pt</xsl:attribute>
</xsl:attribute-set>

<!-- Only hairlines as frame and cell borders in tables -->
<xsl:param name="table.frame.border.thickness">0.3pt</xsl:param>
<xsl:param name="table.cell.border.thickness">0.15pt</xsl:param>
<xsl:param name="table.cell.border.color">#5c5c4f</xsl:param>
<xsl:param name="table.frame.border.color">#5c5c4f</xsl:param>
<xsl:param name="table.cell.border.right.color">white</xsl:param>
<xsl:param name="table.cell.border.left.color">white</xsl:param>
<xsl:param name="table.frame.border.right.color">white</xsl:param>
<xsl:param name="table.frame.border.left.color">white</xsl:param>
<!-- Paper type, no headers on blank pages, no double sided printing -->
<xsl:param name="paper.type" select="'A4'"/>
<xsl:param name="double.sided">1</xsl:param>
<xsl:param name="headers.on.blank.pages">1</xsl:param>
<xsl:param name="footers.on.blank.pages">1</xsl:param>
<!--xsl:param name="header.column.widths" select="'1 4 1'"/-->
<xsl:param name="header.column.widths" select="'1 0 1'"/>
<xsl:param name="footer.column.widths" select="'1 1 1'"/>
<xsl:param name="header.rule" select="1"/>

<!-- Space between paper border and content (chaotic stuff, don't touch) -->
<xsl:param name="page.margin.top">15mm</xsl:param>
<xsl:param name="region.before.extent">10mm</xsl:param>
<xsl:param name="body.margin.top">15mm</xsl:param>

<xsl:param name="body.margin.bottom">15mm</xsl:param>
<xsl:param name="region.after.extent">10mm</xsl:param>
<xsl:param name="page.margin.bottom">15mm</xsl:param>

<xsl:param name="page.margin.outer">30mm</xsl:param>
<xsl:param name="page.margin.inner">30mm</xsl:param>

<!-- No intendation of Titles -->
<xsl:param name="title.margin.left">0pc</xsl:param>

<xsl:param name="title.color">#77B900</xsl:param>

<xsl:attribute-set name="section.title.level1.properties">
	<xsl:attribute name="color"><xsl:value-of select="$title.color"/></xsl:attribute>
	<xsl:attribute name="font-size">
		<xsl:value-of select="$body.font.master * 1.6"/>
		<xsl:text>pt</xsl:text>
	</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="section.title.level2.properties">
	<xsl:attribute name="color"><xsl:value-of select="$title.color"/></xsl:attribute>
	<xsl:attribute name="font-size">
		<xsl:value-of select="$body.font.master * 1.4"/>
		<xsl:text>pt</xsl:text>
	</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="section.title.level3.properties">
	<xsl:attribute name="color"><xsl:value-of select="$title.color"/></xsl:attribute>
	<xsl:attribute name="font-size">
		<xsl:value-of select="$body.font.master * 1.3"/>
		<xsl:text>pt</xsl:text>
	</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="section.title.level4.properties">
	<xsl:attribute name="color"><xsl:value-of select="$title.color"/></xsl:attribute>
	<xsl:attribute name="font-size">
		<xsl:value-of select="$body.font.master * 1.2"/>
		<xsl:text>pt</xsl:text>
	</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="section.title.level5.properties">
	<xsl:attribute name="color"><xsl:value-of select="$title.color"/></xsl:attribute>
	<xsl:attribute name="font-size">
		<xsl:value-of select="$body.font.master * 1.1"/>
		<xsl:text>pt</xsl:text>
	</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="section.title.level6.properties">
	<xsl:attribute name="color"><xsl:value-of select="$title.color"/></xsl:attribute>
	<xsl:attribute name="font-size">
		<xsl:value-of select="$body.font.master"/>
		<xsl:text>pt</xsl:text>
	</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="section.title.properties">
	<xsl:attribute name="font-family">
		<xsl:value-of select="$title.font.family"/>
	</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<!-- font size is calculated dynamically by section.heading template -->
	<xsl:attribute name="keep-with-next.within-column">always</xsl:attribute>
	<xsl:attribute name="space-before.minimum">0.8em</xsl:attribute>
	<xsl:attribute name="space-before.optimum">1.0em</xsl:attribute>
	<xsl:attribute name="space-before.maximum">1.2em</xsl:attribute>
	<xsl:attribute name="text-align">left</xsl:attribute>
	<xsl:attribute name="start-indent"><xsl:value-of select="$title.margin.left"/></xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="book.titlepage.recto.style">
	<xsl:attribute name="font-family">
		<xsl:value-of select="$title.fontset"/>
	</xsl:attribute>
	<xsl:attribute name="color"><xsl:value-of select="$title.color"/></xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
	<xsl:attribute name="text-align">center</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="component.title.properties">
	<xsl:attribute name="keep-with-next.within-column">always</xsl:attribute>
	<xsl:attribute name="space-before.optimum"><xsl:value-of select="concat($body.font.master, 'pt')"/></xsl:attribute>
	<xsl:attribute name="space-before.minimum"><xsl:value-of select="concat($body.font.master, 'pt')"/></xsl:attribute>
	<xsl:attribute name="space-before.maximum"><xsl:value-of select="concat($body.font.master, 'pt')"/></xsl:attribute>
	<xsl:attribute name="hyphenate">false</xsl:attribute>
	<xsl:attribute name="color">
		<xsl:choose>
			<xsl:when test="not(parent::chapter | parent::article | parent::appendix)"><xsl:value-of select="$title.color"/></xsl:when>
		</xsl:choose>
	</xsl:attribute>
	<xsl:attribute name="text-align">
		<xsl:choose>
			<xsl:when test="((parent::article | parent::articleinfo) and not(ancestor::book) and not(self::bibliography))				 or (parent::slides | parent::slidesinfo)">center</xsl:when>
			<xsl:otherwise>left</xsl:otherwise>
		</xsl:choose>
	</xsl:attribute>
	<xsl:attribute name="start-indent"><xsl:value-of select="$title.margin.left"/></xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="chapter.titlepage.recto.style">
	<xsl:attribute name="color"><xsl:value-of select="$title.color"/></xsl:attribute>
	<xsl:attribute name="background-color">white</xsl:attribute>
	<xsl:attribute name="font-size">
		<xsl:choose>
			<xsl:when test="$l10n.gentext.language = 'ja-JP'">
				<xsl:value-of select="$body.font.master * 1.7"/>
				<xsl:text>pt</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>24pt</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="text-align">left</xsl:attribute>
	<!--xsl:attribute name="wrap-option">no-wrap</xsl:attribute-->
	<xsl:attribute name="padding-left">1em</xsl:attribute>
	<xsl:attribute name="padding-right">1em</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="preface.titlepage.recto.style">
	<xsl:attribute name="font-family">
		<xsl:value-of select="$title.fontset"/>
	</xsl:attribute>
	<xsl:attribute name="color">#4a5d75</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="part.titlepage.recto.style">
  <xsl:attribute name="color"><xsl:value-of select="$title.color"/></xsl:attribute>
  <xsl:attribute name="text-align">center</xsl:attribute>
</xsl:attribute-set>


<!--
From: fo/table.xsl
Reason: Table Header format
Version:1.72
-->
<xsl:template name="table.cell.block.properties">
  <!-- highlight this entry? -->
  <xsl:if test="ancestor::thead or ancestor::tfoot">
    <xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="background-color">#4a5d75</xsl:attribute>
	<xsl:attribute name="color">white</xsl:attribute>
  </xsl:if>
</xsl:template>

<!--
From: fo/table.xsl
Reason: Table Header format
Version:1.72
-->
<!-- customize this template to add row properties -->
<xsl:template name="table.row.properties">
  <xsl:variable name="bgcolor">
    <xsl:call-template name="dbfo-attribute">
      <xsl:with-param name="pis" select="processing-instruction('dbfo')"/>
      <xsl:with-param name="attribute" select="'bgcolor'"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:if test="$bgcolor != ''">
    <xsl:attribute name="background-color">
      <xsl:value-of select="$bgcolor"/>
    </xsl:attribute>
  </xsl:if>
  <xsl:if test="ancestor::thead or ancestor::tfoot">
	<xsl:attribute name="background-color">#4a5d75</xsl:attribute>
  </xsl:if>
</xsl:template>

<!--
From: fo/titlepage.templates.xsl
Reason: Switch to using chapter.titlepage.recto.style
Version:1.72
-->
<xsl:template match="title" mode="appendix.titlepage.recto.auto.mode">
<fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:call-template name="component.title.nomarkup">
<xsl:with-param name="node" select="ancestor-or-self::appendix[1]"/>
</xsl:call-template>
</fo:block>
</xsl:template>

<!--
From: fo/titlepage.templates.xsl
Reason: Remove font size and weight overrides
Version:1.72
-->
<xsl:template match="title" mode="chapter.titlepage.recto.auto.mode">
<fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:value-of select="."/>
</fo:block>
</xsl:template>

<!--
From: fo/titlepage.templates.xsl
Reason: Remove font family, size and weight overrides
Version:1.72
-->
<xsl:template name="preface.titlepage.recto">
	<fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" xsl:use-attribute-sets="preface.titlepage.recto.style" margin-left="{$title.margin.left}">
<xsl:call-template name="component.title.nomarkup">
<xsl:with-param name="node" select="ancestor-or-self::preface[1]"/>
</xsl:call-template></fo:block>
	<xsl:choose>
		<xsl:when test="prefaceinfo/subtitle">
			<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/subtitle"/>
		</xsl:when>
		<xsl:when test="docinfo/subtitle">
			<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/subtitle"/>
		</xsl:when>
		<xsl:when test="info/subtitle">
			<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/subtitle"/>
		</xsl:when>
		<xsl:when test="subtitle">
			<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="subtitle"/>
		</xsl:when>
	</xsl:choose>

	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/corpauthor"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/corpauthor"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/corpauthor"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/authorgroup"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/authorgroup"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/authorgroup"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/author"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/author"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/author"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/othercredit"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/othercredit"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/othercredit"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/releaseinfo"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/releaseinfo"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/releaseinfo"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/copyright"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/copyright"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/copyright"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/legalnotice"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/legalnotice"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/legalnotice"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/pubdate"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/pubdate"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/pubdate"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/revision"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/revision"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/revision"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/revhistory"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/revhistory"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/revhistory"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/abstract"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/abstract"/>
	<xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/abstract"/>
</xsl:template>


<xsl:template name="pickfont-sans">
	<xsl:variable name="font">
		<xsl:choose>
			<xsl:when test="$l10n.gentext.language = 'ja-JP'">
				<xsl:text>KochiMincho,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'ko-KR'">
				<xsl:text>BaekmukBatang,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'zh-CN'">
				<xsl:text>ARPLKaitiMGB,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'bn-IN'">
				<xsl:text>LohitBengali,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'ta-IN'">
				<xsl:text>LohitTamil,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'pa-IN'">
				<xsl:text>LohitPunjabi,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'hi-IN'">
				<xsl:text>LohitHindi,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'gu-IN'">
				<xsl:text>LohitGujarati,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'zh-TW'">
				<xsl:text>ARPLMingti2LBig5,</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:choose>
		<xsl:when test="$fop1.extensions != 0">
		  <xsl:copy-of select="$font"/><xsl:text>DejaVuLGCSans,sans-serif</xsl:text>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:copy-of select="$font"/><xsl:text>sans-serif</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="pickfont-serif">
	<xsl:variable name="font">
		<xsl:choose>
			<xsl:when test="$l10n.gentext.language = 'ja-JP'">
				<xsl:text>KochiMincho,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'ko-KR'">
				<xsl:text>BaekmukBatang,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'zh-CN'">
				<xsl:text>ARPLKaitiMGB,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'bn-IN'">
				<xsl:text>LohitBengali,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'ta-IN'">
				<xsl:text>LohitTamil,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'pa-IN'">
				<xsl:text>LohitPunjabi,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'hi-IN'">
				<xsl:text>LohitHindi,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'gu-IN'">
				<xsl:text>LohitGujarati,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'zh-TW'">
				<xsl:text>ARPLMingti2LBig5,</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:choose>
		<xsl:when test="$fop1.extensions != 0">
		  <xsl:copy-of select="$font"/><xsl:text>DejaVuLGCSans,serif</xsl:text>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:copy-of select="$font"/><xsl:text>serif</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="pickfont-mono">
	<xsl:variable name="font">
		<xsl:choose>
			<xsl:when test="$l10n.gentext.language = 'ja-JP'">
				<xsl:text>KochiMincho,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'ko-KR'">
				<xsl:text>BaekmukBatang,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'zh-CN'">
				<xsl:text>ARPLKaitiMGB,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'bn-IN'">
				<xsl:text>LohitBengali,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'ta-IN'">
				<xsl:text>LohitTamil,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'pa-IN'">
				<xsl:text>LohitPunjabi,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'hi-IN'">
				<xsl:text>LohitHindi,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'gu-IN'">
				<xsl:text>LohitGujarati,</xsl:text>
			</xsl:when>
			<xsl:when test="$l10n.gentext.language = 'zh-TW'">
				<xsl:text>ARPLMingti2LBig5,</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:choose>
		<xsl:when test="$fop1.extensions != 0">
		  <xsl:copy-of select="$font"/><xsl:text>DejaVuLGCSans,monospace</xsl:text>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:copy-of select="$font"/><xsl:text>monospace</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<!--xsl:param name="symbol.font.family">
	<xsl:choose>
		<xsl:when test="$l10n.gentext.language = 'ja-JP'">
			<xsl:text>Symbol,ZapfDingbats</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text>Symbol,ZapfDingbats</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
</xsl:param-->

<xsl:param name="title.font.family">
	<xsl:call-template name="pickfont-sans"/>
</xsl:param>

<xsl:param name="body.font.family">
	<xsl:call-template name="pickfont-sans"/>
</xsl:param>

<xsl:param name="monospace.font.family">
	<xsl:call-template name="pickfont-mono"/>
</xsl:param>

<xsl:param name="sans.font.family">
	<xsl:call-template name="pickfont-sans"/>
</xsl:param>

<!--xsl:param name="callout.unicode.font">
	<xsl:call-template name="pickfont-sans"/>
</xsl:param-->

<!--
From: fo/verbatim.xsl
Reason: Left align address
Version: 1.72
-->

<xsl:template match="address">
	<xsl:param name="suppress-numbers" select="'0'"/>

	<xsl:variable name="content">
		<xsl:choose>
			<xsl:when test="$suppress-numbers = '0'
											and @linenumbering = 'numbered'
											and $use.extensions != '0'
											and $linenumbering.extension != '0'">
				<xsl:call-template name="number.rtf.lines">
					<xsl:with-param name="rtf">
						<xsl:apply-templates/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<fo:block wrap-option='no-wrap'
						white-space-collapse='false'
			white-space-treatment='preserve'
						linefeed-treatment="preserve"
						text-align="start"
						xsl:use-attribute-sets="verbatim.properties">
		<xsl:copy-of select="$content"/>
	</fo:block>
</xsl:template>

<xsl:template name="component.title.nomarkup">
  <xsl:param name="node" select="."/>

  <xsl:variable name="id">
    <xsl:call-template name="object.id">
      <xsl:with-param name="object" select="$node"/>
    </xsl:call-template>
  </xsl:variable>

  <xsl:variable name="title">
    <xsl:apply-templates select="$node" mode="object.title.markup">
      <xsl:with-param name="allow-anchors" select="1"/>
    </xsl:apply-templates>
  </xsl:variable>
  <xsl:copy-of select="$title"/>
</xsl:template>

<!--
From: fo/pagesetup.xsl
Reason: Custom Header
Version: 1.72
-->
<xsl:template name="header.content">
  <xsl:param name="pageclass" select="''"/>
  <xsl:param name="sequence" select="''"/>
  <xsl:param name="position" select="''"/>
  <xsl:param name="gentext-key" select="''"/>
	<xsl:param name="title-limit" select="'30'"/>
<!--
  <fo:block>
    <xsl:value-of select="$pageclass"/>
    <xsl:text>, </xsl:text>
    <xsl:value-of select="$sequence"/>
    <xsl:text>, </xsl:text>
    <xsl:value-of select="$position"/>
    <xsl:text>, </xsl:text>
    <xsl:value-of select="$gentext-key"/>
  </fo:block>
body, blank, left, chapter
-->
    <!-- sequence can be odd, even, first, blank -->
    <!-- position can be left, center, right -->
    <xsl:choose>
      <!--xsl:when test="($sequence='blank' and $position='left' and $gentext-key='chapter')">
			<xsl:variable name="text">
				<xsl:call-template name="component.title.nomarkup"/>
			</xsl:variable>
	      <fo:inline keep-together.within-line="always" font-weight="bold">
  			  <xsl:choose>
		  		<xsl:when test="string-length($text) &gt; '33'">
					<xsl:value-of select="concat(substring($text, 0, $title-limit), '...')"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$text"/>
				</xsl:otherwise>
			  </xsl:choose>
		  </fo:inline>
      </xsl:when-->
	  <xsl:when test="$confidential = 1 and (($sequence='odd' and $position='left') or ($sequence='even' and $position='right'))">
	      <fo:inline keep-together.within-line="always" font-weight="bold">
			<xsl:text>RED HAT CONFIDENTIAL</xsl:text>
		  </fo:inline>
      </xsl:when>
	  <xsl:when test="$sequence = 'blank'">
        <!-- nothing -->
      </xsl:when>
 	  <!-- Extracting 'Chapter' + Chapter Number from the full Chapter title, with a dirty, dirty hack -->
  		<xsl:when test="($sequence='first' and $position='left' and $gentext-key='chapter')">
		<xsl:variable name="text">
			<xsl:call-template name="component.title.nomarkup"/>
		</xsl:variable>
		<xsl:variable name="chapt">
			<xsl:value-of select="substring-before($text, '&#xA0;')"/>
		</xsl:variable>
		<xsl:variable name="remainder">
			<xsl:value-of select="substring-after($text, '&#xA0;')"/>
		</xsl:variable>
		<xsl:variable name="chapt-num">
			<xsl:value-of select="substring-before($remainder, '&#xA0;')"/>
		</xsl:variable>
		<xsl:variable name="text1">
			<xsl:value-of select="concat($chapt, '&#xA0;', $chapt-num)"/>
		</xsl:variable>
        <fo:inline keep-together.within-line="always" font-weight="bold">
 		  <xsl:value-of select="$text1"/>
		</fo:inline>
      </xsl:when>
     <!--xsl:when test="($sequence='odd' or $sequence='even') and $position='center'"-->
      <xsl:when test="($sequence='even' and $position='left')">
        <!--xsl:if test="$pageclass != 'titlepage'"-->
			<xsl:variable name="text">
				<xsl:call-template name="component.title.nomarkup"/>
			</xsl:variable>
	      <fo:inline keep-together.within-line="always" font-weight="bold">
  			  <xsl:choose>
		  		<xsl:when test="string-length($text) &gt; '33'">
					<xsl:value-of select="concat(substring($text, 0, $title-limit), '...')"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$text"/>
				</xsl:otherwise>
			  </xsl:choose>
		  </fo:inline>
        <!--xsl:if-->
      </xsl:when>
      <xsl:when test="($sequence='odd' and $position='right')">
        <!--xsl:if test="$pageclass != 'titlepage'"-->
	      <fo:inline keep-together.within-line="always"><fo:retrieve-marker retrieve-class-name="section.head.marker" retrieve-position="first-including-carryover" retrieve-boundary="page-sequence"/></fo:inline>
        <!--/xsl:if-->
      </xsl:when>
	  <xsl:when test="$position='left'">
        <!-- Same for odd, even, empty, and blank sequences -->
        <xsl:call-template name="draft.text"/>
      </xsl:when>
      <xsl:when test="$position='center'">
        <!-- nothing for empty and blank sequences -->
      </xsl:when>
      <xsl:when test="$position='right'">
        <!-- Same for odd, even, empty, and blank sequences -->
        <xsl:call-template name="draft.text"/>
      </xsl:when>
      <xsl:when test="$sequence = 'first'">
        <!-- nothing for first pages -->
      </xsl:when>
      <xsl:when test="$sequence = 'blank'">
        <!-- nothing for blank pages -->
      </xsl:when>
    </xsl:choose>
</xsl:template>

<!--
From: fo/pagesetup.xsl
Reason: Override colour
Version: 1.72
-->
<xsl:template name="head.sep.rule">
	<xsl:param name="pageclass"/>
	<xsl:param name="sequence"/>
	<xsl:param name="gentext-key"/>

	<xsl:if test="$header.rule != 0">
		<xsl:attribute name="border-bottom-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-color">#4a5d75</xsl:attribute>
	</xsl:if>
</xsl:template>

<!--
From: fo/pagesetup.xsl
Reason: Override colour
Version: 1.72
-->
<xsl:template name="foot.sep.rule">
	<xsl:param name="pageclass"/>
	<xsl:param name="sequence"/>
	<xsl:param name="gentext-key"/>

	<xsl:if test="$footer.rule != 0">
		<xsl:attribute name="border-top-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-top-style">solid</xsl:attribute>
		<xsl:attribute name="border-top-color">#4a5d75</xsl:attribute>
	</xsl:if>
</xsl:template>

<xsl:param name="footnote.font.size">
	<xsl:value-of select="$body.font.master * 0.8"/><xsl:text>pt</xsl:text>
</xsl:param>
<xsl:param name="footnote.number.format" select="'1'"/>
<xsl:param name="footnote.number.symbols" select="''"/>
<xsl:attribute-set name="footnote.mark.properties">
	<xsl:attribute name="font-size">75%</xsl:attribute>
	<xsl:attribute name="font-weight">normal</xsl:attribute>
	<xsl:attribute name="font-style">normal</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="footnote.properties">
	<xsl:attribute name="padding-top">48pt</xsl:attribute>
	<xsl:attribute name="font-family"><xsl:value-of select="$body.fontset"/></xsl:attribute>
	<xsl:attribute name="font-size"><xsl:value-of select="$footnote.font.size"/></xsl:attribute>
	<xsl:attribute name="font-weight">normal</xsl:attribute>
	<xsl:attribute name="font-style">normal</xsl:attribute>
	<xsl:attribute name="text-align"><xsl:value-of select="$alignment"/></xsl:attribute>
	<xsl:attribute name="start-indent">0pt</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="footnote.sep.leader.properties">
	<xsl:attribute name="color">black</xsl:attribute>
	<xsl:attribute name="leader-pattern">rule</xsl:attribute>
	<xsl:attribute name="leader-length">1in</xsl:attribute>
</xsl:attribute-set>

<xsl:template match="author" mode="tablerow.titlepage.mode">
  <fo:table-row>
    <fo:table-cell>
	  <fo:block>
        <xsl:call-template name="gentext">
          <xsl:with-param name="key" select="'Author'"/>
        </xsl:call-template>
	  </fo:block>
    </fo:table-cell>
    <fo:table-cell>
	  <fo:block>
	    <xsl:call-template name="person.name">
          <xsl:with-param name="node" select="."/>
        </xsl:call-template>
	  </fo:block>
    </fo:table-cell>
    <fo:table-cell>
	  <fo:block>
	    <xsl:apply-templates select="email"/>
	  </fo:block>
    </fo:table-cell>
  </fo:table-row>
</xsl:template>

<xsl:template match="author" mode="titlepage.mode">
  <fo:block>
    <xsl:call-template name="person.name">
         <xsl:with-param name="node" select="."/>
    </xsl:call-template>
  </fo:block>
</xsl:template>

<xsl:param name="editedby.enabled">0</xsl:param>

<xsl:template match="editor" mode="tablerow.titlepage.mode">
  <fo:table-row>
    <fo:table-cell>
	  <fo:block>
	    <xsl:call-template name="gentext">
	      <xsl:with-param name="key" select="'Editor'"/>
	    </xsl:call-template>
	  </fo:block>
    </fo:table-cell>
    <fo:table-cell>
	  <fo:block>
        <xsl:call-template name="person.name">
          <xsl:with-param name="node" select="."/>
        </xsl:call-template>
	  </fo:block>
    </fo:table-cell>
    <fo:table-cell>
	  <fo:block>
	    <xsl:apply-templates select="email"/>
	  </fo:block>
    </fo:table-cell>
  </fo:table-row>
</xsl:template>

<xsl:template match="othercredit" mode="tablerow.titlepage.mode">
  <fo:table-row>
    <fo:table-cell>
	  <fo:block>
	    <xsl:call-template name="gentext">
	      <xsl:with-param name="key" select="'translator'"/>
	    </xsl:call-template>
	  </fo:block>
    </fo:table-cell>
    <fo:table-cell>
	  <fo:block>
        <xsl:call-template name="person.name">
          <xsl:with-param name="node" select="."/>
        </xsl:call-template>
	  </fo:block>
    </fo:table-cell>
    <fo:table-cell>
	  <fo:block>
	    <xsl:apply-templates select="email"/>
	  </fo:block>
    </fo:table-cell>
  </fo:table-row>
 </xsl:template>

<!--
From: fo/titlepage.xsl
Reason: 
Version:1.72
-->
<!-- Omitted to get JBossOrg style working - TODO
<xsl:template name="verso.authorgroup">
  <fo:table table-layout="fixed" width="100%">
    <fo:table-column column-number="1" column-width="proportional-column-width(1)"/>
    <fo:table-column column-number="2" column-width="proportional-column-width(1)"/>
    <fo:table-column column-number="3" column-width="proportional-column-width(1)"/>
    <fo:table-body>
      <xsl:apply-templates select="author" mode="tablerow.titlepage.mode"/>
      <xsl:apply-templates select="editor" mode="tablerow.titlepage.mode"/>
      <xsl:apply-templates select="othercredit" mode="tablerow.titlepage.mode"/>
    </fo:table-body>
  </fo:table>
</xsl:template> -->

<xsl:template match="title" mode="book.titlepage.recto.auto.mode">
<fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" xsl:use-attribute-sets="book.titlepage.recto.style" text-align="center" font-size="20pt" space-before="18.6624pt" font-weight="bold" font-family="{$title.fontset}">
<xsl:call-template name="division.title">
<xsl:with-param name="node" select="ancestor-or-self::book[1]"/>
</xsl:call-template>
</fo:block>
</xsl:template>

<xsl:template match="subtitle" mode="book.titlepage.recto.auto.mode">
<fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" xsl:use-attribute-sets="book.titlepage.recto.style" text-align="center" font-size="34pt" space-before="30pt" font-family="{$title.fontset}">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="issuenum" mode="book.titlepage.recto.auto.mode">
<fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" xsl:use-attribute-sets="book.titlepage.recto.style" text-align="center" font-size="16pt" space-before="15.552pt" font-family="{$title.fontset}">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="author" mode="book.titlepage.recto.auto.mode">
  <fo:block xsl:use-attribute-sets="book.titlepage.recto.style" font-size="14pt" space-before="15.552pt">
    <xsl:call-template name="person.name">
         <xsl:with-param name="node" select="."/>
    </xsl:call-template>
  </fo:block>
</xsl:template>

<xsl:template name="book.titlepage.recto">
  <xsl:choose>
    <xsl:when test="bookinfo/title">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/title"/>
    </xsl:when>
    <xsl:when test="info/title">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/title"/>
    </xsl:when>
    <xsl:when test="title">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="title"/>
    </xsl:when>
  </xsl:choose>

  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/issuenum"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/issuenum"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="issuenum"/>

  <xsl:choose>
    <xsl:when test="bookinfo/subtitle">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/subtitle"/>
    </xsl:when>
    <xsl:when test="info/subtitle">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/subtitle"/>
    </xsl:when>
    <xsl:when test="subtitle">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="subtitle"/>
    </xsl:when>
  </xsl:choose>

  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/corpauthor"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/corpauthor"/>

  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/authorgroup/author"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/authorgroup/author"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/author"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/author"/>

  <fo:block xsl:use-attribute-sets="book.titlepage.recto.style" color="black">
    <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/invpartnumber"/>
    <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/invpartnumber"/>
  </fo:block>
  <fo:block xsl:use-attribute-sets="book.titlepage.recto.style" color="black">
    <xsl:call-template name="gentext">
      <xsl:with-param name="key" select="'isbn'"/>
	</xsl:call-template>
	<xsl:text>: </xsl:text>
    <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/isbn"/>
    <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/isbn"/>
  </fo:block>
  <fo:block xsl:use-attribute-sets="book.titlepage.recto.style" color="black"> 
    <xsl:call-template name="gentext">
      <xsl:with-param name="key" select="'pubdate'"/>
	</xsl:call-template>
	<xsl:text>: </xsl:text>
    <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/pubdate"/>
    <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/pubdate"/>
  </fo:block>
</xsl:template>

<xsl:template name="book.titlepage.verso">
  <xsl:choose>
    <xsl:when test="bookinfo/abstract">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/abstract"/>
    </xsl:when>
    <xsl:when test="info/abstract">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/abstract"/>
    </xsl:when>
  </xsl:choose>

</xsl:template>

<xsl:template name="book.titlepage3.recto">
  <xsl:choose>
    <xsl:when test="bookinfo/title">
      <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/title"/>
    </xsl:when>
    <xsl:when test="info/title">
      <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/title"/>
    </xsl:when>
    <xsl:when test="title">
      <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="title"/>
    </xsl:when>
  </xsl:choose>

  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/authorgroup"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/authorgroup"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/author"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/author"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/othercredit"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/othercredit"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/copyright"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/copyright"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/legalnotice"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/legalnotice"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/publisher"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/publisher"/>
</xsl:template>

<xsl:template name="book.titlepage.separator"><fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" break-after="page"/>
</xsl:template>

<xsl:template name="book.titlepage.before.recto">
</xsl:template>

<xsl:template name="book.titlepage.before.verso"><fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" break-after="page"/>
</xsl:template>

<xsl:template name="book.titlepage">
  <fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:call-template name="book.titlepage.before.recto"/>
    <fo:block><xsl:call-template name="book.titlepage.recto"/></fo:block>
    <xsl:call-template name="book.titlepage.separator"/>
    <fo:block><xsl:call-template name="book.titlepage.verso"/></fo:block>
    <xsl:call-template name="book.titlepage.separator"/>
    <fo:block><xsl:call-template name="book.titlepage3.recto"/></fo:block>
    <xsl:call-template name="book.titlepage.separator"/>
  </fo:block>
</xsl:template>

<!--
From: fo/qandaset.xsl
Reason: Id in list-item-label causes fop crash
Version:1.72
-->

<xsl:template match="question">
  <xsl:variable name="id"><xsl:call-template name="object.id"/></xsl:variable>

  <xsl:variable name="entry.id">
    <xsl:call-template name="object.id">
      <xsl:with-param name="object" select="parent::*"/>
    </xsl:call-template>
  </xsl:variable>

  <xsl:variable name="deflabel">
    <xsl:choose>
      <xsl:when test="ancestor-or-self::*[@defaultlabel]">
        <xsl:value-of select="(ancestor-or-self::*[@defaultlabel])[last()]
                              /@defaultlabel"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$qanda.defaultlabel"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <fo:list-item id="{$entry.id}" xsl:use-attribute-sets="list.item.spacing">
    <fo:list-item-label end-indent="label-end()">
      <xsl:choose>
        <xsl:when test="$deflabel = 'none'">
          <fo:block/>
        </xsl:when>
        <xsl:otherwise>
          <fo:block>
            <xsl:apply-templates select="." mode="label.markup"/>
            <xsl:if test="$deflabel = 'number' and not(label)">
              <xsl:apply-templates select="." mode="intralabel.punctuation"/>
            </xsl:if>
          </fo:block>
        </xsl:otherwise>
      </xsl:choose>
    </fo:list-item-label>
    <fo:list-item-body start-indent="body-start()">
      <xsl:choose>
        <xsl:when test="$deflabel = 'none'">
          <fo:block font-weight="bold">
            <xsl:apply-templates select="*[local-name(.)!='label']"/>
          </fo:block>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="*[local-name(.)!='label']"/>
        </xsl:otherwise>
      </xsl:choose>
      <!-- Uncomment this line to get revhistory output in the question -->
      <!-- <xsl:apply-templates select="preceding-sibling::revhistory"/> -->
    </fo:list-item-body>
  </fo:list-item>
</xsl:template>

<!--
From: fo/qandaset.xsl
Reason: Id in list-item-label causes fop crash
Version:1.72
-->
<xsl:template match="answer">
  <xsl:variable name="id"><xsl:call-template name="object.id"/></xsl:variable>
  <xsl:variable name="entry.id">
    <xsl:call-template name="object.id">
      <xsl:with-param name="object" select="parent::*"/>
    </xsl:call-template>
  </xsl:variable>

  <xsl:variable name="deflabel">
    <xsl:choose>
      <xsl:when test="ancestor-or-self::*[@defaultlabel]">
        <xsl:value-of select="(ancestor-or-self::*[@defaultlabel])[last()]
                              /@defaultlabel"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$qanda.defaultlabel"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <fo:list-item xsl:use-attribute-sets="list.item.spacing">
    <fo:list-item-label end-indent="label-end()">
      <xsl:choose>
        <xsl:when test="$deflabel = 'none'">
          <fo:block/>
        </xsl:when>
        <xsl:otherwise>
          <fo:block>
            <xsl:variable name="answer.label">
              <xsl:apply-templates select="." mode="label.markup"/>
            </xsl:variable>
            <xsl:copy-of select="$answer.label"/>
          </fo:block>
        </xsl:otherwise>
      </xsl:choose>
    </fo:list-item-label>
    <fo:list-item-body start-indent="body-start()">
      <xsl:apply-templates select="*[local-name(.)!='label']"/>
    </fo:list-item-body>
  </fo:list-item>
</xsl:template>

</xsl:stylesheet>
