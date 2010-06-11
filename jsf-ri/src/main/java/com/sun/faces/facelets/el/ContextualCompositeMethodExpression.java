/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
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


package com.sun.faces.facelets.el;

import com.sun.faces.component.CompositeComponentStackManager;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.el.MethodInfo;
import javax.el.ELContext;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.Location;

/**
 * <p>
 * This specialized <code>MethodExpression</code> enables the evaluation of
 * composite component expressions.  Instances of this expression will be created
 * when {@link com.sun.faces.facelets.tag.TagAttributeImpl#getValueExpression(javax.faces.view.facelets.FaceletContext, Class)}
 * is invoked and the expression represents a composite component expression (i.e. #{cc.[properties]}).
 * </p>
 *
 * <p>
 * It's important to note that these <code>MethodExpression</code>s are context
 * sensitive in that they leverage the location in which they were referenced
 * in order to push the proper composite component to the evaluation context
 * prior to evaluating the expression itself.
 * </p>
 *
 * Here's an example:
 *
 * <pre>
 * Using Page test.xhtml
 * ---------------------------------
 *    &lt;ez:comp1 do="#{bean.action}" /&gt;
 *
 *
 * comp1.xhtml
 * ---------------------------------
 * &lt;composite:interface&gt;
 *    &lt;composite:attribute name="do" method-signature="String f()" required="true" /&gt;
 * &lt;/composite:interface&gt;
 * &lt;composite:implementation&gt;
 *    &lt;ez:nesting&gt;
 *       &lt;h:commandButton value="Click Me!" action="#{cc.attrs.do} /&gt;
 *    &lt;/ez:nesting&gt;
 * &lt;/composite:implementation&gt;
 *
 * nesting.xhtml
 * ---------------------------------
 * &lt;composite:interface /&gt;
 * &lt;composite:implementation&gt;
 *    &lt;composite:insertChildren&gt;
 * &lt;/composite:implementation&gt;
 * </pre>
 *
 * When <code>commandButton</code> is clicked, the <code>ContextualCompositeMethodExpression</code>
 * first is looked up by {@link com.sun.faces.facelets.tag.TagAttributeImpl.AttributeLookupMethodExpression}
 * which results an instance of <code>ContextualCompositeMethodExpression</code>.
 * When this <code>ContextualCompositeMethodExpression is invoked, the {@link javax.faces.view.Location}
 * object necessary to find the proper composite component will be derived from
 * source <code>ValueExpression</code> provided at construction time.  Using the
 * derived {@link javax.faces.view.Location}, we can find the composite component
 * that matches 'owns' the template in which the expression was defined in by
 * comparing the path of the Location with the name and library of the {@link javax.faces.application.Resource}
 * instance associated with each composite component.  If a matching composite
 * component is found, it will be made available to the EL by calling {@link CompositeComponentStackManager#push(javax.faces.component.UIComponent)}.
 * </p>
 */
public class ContextualCompositeMethodExpression extends MethodExpression {

    private final MethodExpression delegate;
    private final ValueExpression source;
    private final Location location;


    // -------------------------------------------------------- Constructors


    public ContextualCompositeMethodExpression(ValueExpression source,
                                               MethodExpression delegate) {

        this.delegate = delegate;
        this.source = source;
        this.location = null;

    }


    public ContextualCompositeMethodExpression(Location location,
                                               MethodExpression delegate) {

        this.delegate = delegate;
        this.location = location;
        this.source = null;
    }


    // ------------------------------------------- Methods from MethodExpression


    public MethodInfo getMethodInfo(ELContext elContext) {

        return delegate.getMethodInfo(elContext);

    }

    public Object invoke(ELContext elContext, Object[] objects) {

        FacesContext ctx = (FacesContext) elContext.getContext(FacesContext.class);
        try {
            boolean pushed = pushCompositeComponent(ctx);
            try {
                return delegate.invoke(elContext, objects);
            } finally {
                if (pushed) {
                    popCompositeComponent(ctx);
                }
            }
        } catch (ELException ele) {
            if (source != null) {
                // special handling when an ELException handling.  This is necessary
                // when there are multiple levels of composite component nesting.
                // When this happens, we need to evaluate the source expression
                // to find and invoke the MethodExpression at the next highest
                // nesting level.  Is there a cleaner way to detect this case?
                Object fallback = source.getValue(elContext);
                if (fallback != null && fallback instanceof MethodExpression) {
                    return ((MethodExpression) fallback).invoke(elContext, objects);
                } else {
                    throw ele;
                }
            }
            throw ele;
        }

    }


    // ------------------------------------------------- Methods from Expression


    public String getExpressionString() {

        return delegate.getExpressionString();

    }


    @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
    public boolean equals(Object o) {

        return delegate.equals(o);

    }


    public int hashCode() {

        return delegate.hashCode();

    }


    public boolean isLiteralText() {

        return delegate.isLiteralText();

    }


    // ----------------------------------------------------- Private Methods


    private boolean pushCompositeComponent(FacesContext ctx) {

        CompositeComponentStackManager manager =
              CompositeComponentStackManager.getManager(ctx);
        UIComponent cc = null;

        if (location != null) {
            cc = manager.findCompositeComponentUsingLocation(ctx, location);
        } else {
            // We need to obtain the Location of the source expression in order
            // to find the composite component that needs to be available within
            // the evaluation stack.
            if (source instanceof TagValueExpression) {
                ValueExpression orig = ((TagValueExpression) source).getWrapped();
                if (orig instanceof ContextualCompositeValueExpression) {
                    cc = manager.findCompositeComponentUsingLocation(ctx, ((ContextualCompositeValueExpression) orig).getLocation());
                }
            }
        }

        return manager.push(cc);

    }


    private void popCompositeComponent(FacesContext ctx) {

        CompositeComponentStackManager manager =
              CompositeComponentStackManager.getManager(ctx);
        manager.pop();

    }

} // END ContextualCompositeMethodExpression