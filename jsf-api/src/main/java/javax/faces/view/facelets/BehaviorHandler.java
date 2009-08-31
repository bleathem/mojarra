/*
 * $Id$
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
 */

package javax.faces.view.facelets;

import javax.faces.view.BehaviorHolderAttachedObjectHandler;

/**
 * <p class="changed_added_2_0">The {@link FaceletHandler} that
 * corresponds to attached objects that represent an instance of {@link
 * javax.faces.component.behavior.ClientBehavior} that must be added to
 * the parent component, which must implement {@link
 * javax.faces.component.behavior.ClientBehaviorHolder}, with a call to
 * {@link
 * javax.faces.component.behavior.ClientBehaviorHolder#addClientBehavior}.
 * The current specification defines one Facelet element for this sort
 * of attached object, <code>&lt;f:ajax&gt;</code>.</p>
 */ 

public class BehaviorHandler extends FaceletsAttachedObjectHandler implements BehaviorHolderAttachedObjectHandler {

    private final TagAttribute event;
    
    private String behaviorId;

    private TagHandlerDelegate helper;

    public BehaviorHandler(BehaviorConfig config) {
        super(config);
        this.behaviorId = config.getBehaviorId();
        this.event = this.getAttribute("event");
        if (null != event && !event.isLiteral()){
            throw new TagException(this.tag, "The 'event' attribute for behavior tag must be a literal");
        }
    }
    
    public TagAttribute getEvent() {
        return this.event;
    }
    
    public String getEventName() {
        if (null != getEvent()) {
            return getEvent().getValue();
        }
        return null;
    }
    
    @Override
    protected TagHandlerDelegate getTagHandlerDelegate() {
        if (null == helper) {
            helper = delegateFactory.createBehaviorHandlerDelegate(this);
        }
        return helper;
    }

    public String getBehaviorId() {
        return behaviorId;
    }

}