<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
 Contributor(s):
 
 If you wish your version of this file to be governed by only the CDDL or
 only the GPL Version 2, indicate your decision by adding "[Contributor]
 elects to include this software in this distribution under the [CDDL or GPL
 Version 2] license."  If you don't indicate a single choice of license, a
 recipient has the option to distribute your version of this file under
 either the CDDL, the GPL Version 2 or to extend the choice of license to
 its licensees as provided above.  However, if you add GPL Version 2 code
 and therefore, elected the GPL Version 2 license, then the option applies
 only if the new code is made subject to such option by the copyright
 holder.
--%>

<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>test</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>test</h1>

<f:view>

  <%@ include file="printComponent.jsp" %>

  <h:form id="form" prependId="false">

    <%@ include file="printComponent.jsp" %>

    <h:panelGrid id="panelGroup" columns="2" border="1">

      <h:panelGroup id="r1c1">
        <%@ include file="printComponent.jsp" %>
        <p>row 1 col 1</p>
        <h:inputText id="r1c1_input"
                     converter="#{requestBean.converter}"
                     validator="#{requestBean.validator}"
                     valueChangeListener="#{requestBean.valueChangeListener}"/>
      </h:panelGroup>

      <h:panelGroup id="r1c2">
        <%@ include file="printComponent.jsp" %>
        <p>row 1 col 2</p>
        <h:inputText id="r1c2_input"
                     converter="#{requestBean.converter}"
                     validator="#{requestBean.validator}"
                     valueChangeListener="#{requestBean.valueChangeListener}"/>
      </h:panelGroup>

      <h:panelGroup id="r2c1">
        <%@ include file="printComponent.jsp" %>
        <p>row 2 col 1</p>
        <h:inputText id="r2c1_input"
                     converter="#{requestBean.converter}"
                     validator="#{requestBean.validator}"
                     valueChangeListener="#{requestBean.valueChangeListener}"/>
      </h:panelGroup>

      <h:panelGroup id="r2c2">
        <%@ include file="printComponent.jsp" %>
        <p>row 2 col 2</p>
        <h:inputText id="r2c2_input"
                     converter="#{requestBean.converter}"
                     validator="#{requestBean.validator}"
                     valueChangeListener="#{requestBean.valueChangeListener}"/>
      </h:panelGroup>

    </h:panelGrid>

    <p>Accrued request messages: 
       <h:outputText escape="false" 
                     value="#{requestBean.requestMessages}" /></p>

    <p><h:commandButton value="reload"/></p>

    <h:messages />

  </h:form>

</f:view>

  </body>
</html>