/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.tag.jsf;

import com.sun.faces.facelets.tag.composite.*;
import com.sun.faces.facelets.tag.*;
import com.sun.faces.facelets.tag.jsf.ComponentConfig;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 *
 * @author edburns
 */
public class CompositeComponentTagLibrary extends AbstractTagLibrary {
    
    public CompositeComponentTagLibrary(String ns) {
        super(ns);
        if (null == ns) {
            throw new NullPointerException();
        }
        this.ns = ns;
    }
    
    public CompositeComponentTagLibrary(String ns, String compositeLibraryName) {
        super(ns);
        if (null == ns) {
            throw new NullPointerException();
        }
        this.ns = ns;
        if (null == compositeLibraryName) {
            throw new NullPointerException();
        }
        this.compositeLibraryName = compositeLibraryName;
        
    }
    
    private String ns = null;
    private String compositeLibraryName;

    public boolean containsFunction(String ns, String name) {
        return false;
    }

    public boolean containsNamespace(String ns) {
        return (null != ns && ns.equals(this.ns));
    }

    public boolean containsTagHandler(String ns, String localName) {
        boolean result = false;

        Resource compositeComponentResource = null;
                        
        if (null != (compositeComponentResource = 
                getCompositeComponentResource(ns, localName))) {
            InputStream componentStream = null;
            try {
                componentStream = compositeComponentResource.getInputStream();
            } catch (IOException ex) {
                Logger.getLogger(CompositeComponentTagLibrary.class.getName()).log(Level.SEVERE, null, ex);
            }
            result = null != componentStream;
        }
        return result;
    }
    
    private Resource getCompositeComponentResource(String ns, String localName) {
        Resource compositeComponentResource = null;
        if (ns.equals(this.ns)) {
            FacesContext context = FacesContext.getCurrentInstance();
            String libraryName = getCompositeComponentLibraryName(this.ns);
            if (null != libraryName) {
                String compositeComponentName = localName + ".xhtml";
                // PENDING: there has to be a cheaper way to test for existence
                ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
                compositeComponentResource = resourceHandler.
                        createResource(compositeComponentName, libraryName);
            }
        }
        return compositeComponentResource;
    }

    public Method createFunction(String ns, String name) {
        return null;
    }

    public TagHandler createTagHandler(String ns, String localName, TagConfig tag) throws FacesException {
        TagHandler result = null;
        
        ComponentConfig componentConfig = new ComponentConfigWrapper(tag,
                CompositeComponentImpl.TYPE, null);
        result = new CompositeComponentTagHandler(
                getCompositeComponentResource(ns, localName),
                componentConfig);

        return result;
    }
    
    private static final String NS_COMPOSITE_COMPONENT_PREFIX = 
            "http://java.sun.com/jsf/composite/";
    
    public boolean tagLibraryForNSExists(String toTest) {
        boolean result = false;
        
        String resourceId = null;
        if (null != (resourceId = getCompositeComponentLibraryName(toTest))) {
            result = FacesContext.getCurrentInstance().getApplication().
                    getResourceHandler().libraryExists(resourceId);
        }
        
        return result;
    }
    
    public static boolean scriptComponentForResourceExists(FacesContext context,
            Resource componentResource) {
        boolean result = false;

        Resource scriptComponentResource = context.getApplication().getPageDeclarationLanguage().getScriptComponentResource(context, 
                componentResource);
        try {
            result = (null != scriptComponentResource) && (null != scriptComponentResource.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(CompositeComponentTagLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    private String getCompositeComponentLibraryName(String toTest) {
        String resourceId = null;
        if (null != compositeLibraryName) {
            resourceId = compositeLibraryName;
        }
        else {
            int resourceIdIndex;
            if (-1 != (resourceIdIndex = toTest.indexOf(NS_COMPOSITE_COMPONENT_PREFIX))) {
                resourceIdIndex += NS_COMPOSITE_COMPONENT_PREFIX.length();
                if (resourceIdIndex < toTest.length()) {
                    resourceId = toTest.substring(resourceIdIndex);
                }
            }
        }
        
        return resourceId;
    }

}