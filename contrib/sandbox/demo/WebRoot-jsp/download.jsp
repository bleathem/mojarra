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

<%@ include file="header.inc" %>
    <f:view>
        <table>
            <tr>
                <td colspan="2">
                    <h3>
                        Method: None
                    </h3>
                </td>
            </tr>
            <tr>
                <td>
                    <h4>Default url variable</h4>
                    <risb:download mimeType="image/png" fileName="sample.png" data="#{testBean.image}">
                        <h:graphicImage url="#{downloadUrl}" width="250px" />
                    </risb:download>
                    <h4>User-defined url variable</h4>
                    <risb:download mimeType="image/png" fileName="sample.png" data="#{testBean.image}" urlVar="foo">
                        <h:graphicImage url="#{foo}" width="250px" />
                    </risb:download>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <h3>
                        Method: Download
                    </h3>
                </td>
            </tr>
            <tr>
                <td>
                    <risb:download method="download" mimeType="application/pdf" fileName="sample.pdf"
                        data="#{testBean.pdf}">
                        <h:graphicImage alt="Download" url="/download.jpg">
                        </h:graphicImage>
                    </risb:download>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <h3>
                        Method: Inline
                    </h3>
                </td>
            </tr>
            <tr>
                <td>
                    <risb:download method="inline" mimeType="application/pdf" fileName="HelloWorld.pdf"
                        data="#{testBean.pdf}" width="750px" height="500px" />
                </td>
            </tr>
        </table>
    </f:view>
<%@ include file="footer.inc" %>