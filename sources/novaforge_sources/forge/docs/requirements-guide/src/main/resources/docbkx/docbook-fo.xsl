<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xslthl="http://xslthl.sf.net"
                exclude-result-prefixes="xslthl"
                version='1.0'>

<xsl:template match='xslthl:keyword'>
  <fo:inline font-weight="bold" color="blue"><xsl:apply-templates/></fo:inline>
</xsl:template>

<xsl:template match='xslthl:comment'>
  <fo:inline font-style="italic" color="grey"><xsl:apply-templates/></fo:inline>
</xsl:template>

</xsl:stylesheet>