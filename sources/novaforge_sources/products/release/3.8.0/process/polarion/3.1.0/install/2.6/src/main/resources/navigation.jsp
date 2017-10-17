<%--
  - Copyright (c) 2004, 2005 Polarion Software, All rights reserved.
  - Copyright (C) 2011-2014, BULL SAS, NovaForge Version 3 and above.
  - Email: community@polarion.org
  -
  - This program and the accompanying materials are made available under the
  - terms of the Apache License, Version 2.0 (the "License"). You may not use
  - this file except in compliance with the License. Copy of the License is
  - located in the file LICENSE.txt in the project distribution. You may also
  - obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
  -
  -
  - POLARION SOFTWARE MAKES NO REPRESENTATIONS OR WARRANTIES
  - ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR IMPLIED,
  - INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
  - FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. POLARION SOFTWARE
  - SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
  - OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
  --%>

<%@ page import="org.polarion.svnwebclient.web.model.Navigation,
                 org.polarion.svnwebclient.web.model.Link,
                 org.polarion.svnwebclient.web.model.Button,
                 java.util.Iterator,
                 org.polarion.svnwebclient.web.model.PathSwitch,
                 org.polarion.svnwebclient.web.model.PageInfo"%>

<jsp:useBean id="bean" scope="request" type="org.polarion.svnwebclient.web.controller.AbstractBean"/>
<%
    Navigation navigation = bean.getNavigation();
    if (navigation != null) {   
        PageInfo currentPageInfo = bean.getCurrentPageInfo();
      
        String params = currentPageInfo.getParameters();        
        if (params == null) {
            params = "";
        }
%>

<table cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr class="path_node">
        <td nowrap="true">
            <span class="pathFormat">
                Path&nbsp;
<%
        if (navigation.getCurrentRevision() != null) {
%>             
                [revision <%=navigation.getCurrentRevision()%>]&nbsp;
<%
        }                
%>    
                :&nbsp;
            </span>
        </td>
        <td width="100%" nowrap="true">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
<%	
		int count = 0;
        for (Iterator i = navigation.getPath().iterator(); i.hasNext(); ) {
            Link pathElement = (Link) i.next();
			if (count == 0) {
				count ++;
%>			
					<td nowrap="true">
                    	 <a class="path_t" href="<%=pathElement.getUrl()%>"><span class="path_text">@BASE_URL@/svn-default/svn/<%=pathElement.getName()%></span></a>        
                    </td>
<%
			} else {						
%>				
					<td nowrap="true">
                    	 <a class="path_t" href="<%=pathElement.getUrl()%>"><%=pathElement.getName()%></a>        
                    </td>
<%                    
			}
            if (i.hasNext()) {
%>
                    <td style="padding-left:1px;padding-right:1px;">
                        <span class="path_t">/</span>
                    </td>
<%
            }                
        }
%>    
                    <td width="100%"/>
                </tr>
            </table>
        </td>
        <script type="text/javascript">
        	browserPageLoaded("<%=currentPageInfo.getPage()%>", "<%=currentPageInfo.getPath()%>", "<%=params%>");
        </script>
<%
        if (navigation.isShowPathSwitch()) {
            PathSwitch pathSwitch = navigation.getPathSwitch();
%>
        <td align="right" style="padding-top:0;padding-bottom:0;padding-left:5px;padding-right:5px;">
            <select name="pathswitch" class="combo" style="font-size:10px;font-family:Arial" onchange="if (this.options[this.selectedIndex].value != 'null') { window.location.href=(this.options[this.selectedIndex].value) }">
<%
            for (Iterator i = pathSwitch.getElements().iterator(); i.hasNext(); ) {
                PathSwitch.Element element = (PathSwitch.Element) i.next();
                if (element.isSelected()) {
%>            
                <option value="<%=element.getUrl()%>" selected>      
<%
                } else {
%>                                                
                <option value="<%=element.getUrl()%>">      
<%
                }                
%>            
                    <%=element.getName()%>
                </option>
<%
            }                          
%>        
            </select>
        </td>        
<%
        }        
%>
<%
        for (Iterator i = navigation.getButtons().iterator(); i.hasNext(); ) {
            Button button = (Button) i.next();
            if (i.hasNext() == false) {
%>
        <td align="right" style="padding-right:1px;">
<%
            } else {
%>
        <td align="right">
<%
        }
%>        
            <a href="<%=button.getUrl()%>">
                <img src="<%=button.getImage()%>" alt="<%=button.getCaption()%>" title="<%=button.getCaption()%>" style="padding:0 0 0 0; margin:0 0 0 0; cursor:pointer;" border="0"/>
            </a>
        </td>        
<%
        }
%>
    </tr>
</table>
<%
    }
%>
