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

package javax.faces.event;

import java.util.Map;
import javax.faces.component.UIComponent;

/**
 *
 * <p class="changed_added_2_0">When an instance of this event is passed
 * to {@link SystemEventListener#processEvent} or {@link
 * ComponentSystemEventListener#processEvent}, the listener
 * implementation may assume that the <code>source</code> of this event
 * instance is a {@link UIComponent} instance and that either that
 * instance or an ancestor of that instance was just added to the view.
 * Therefore, the implementation may assume it is safe to call {@link
 * UIComponent#getParent}, {@link UIComponent#getClientId}, and other
 * methods that depend upon the component instance being added into the
 * view.</p>
 *
 * <div class="changed_added_2_0">
 *
 * <p>The implementation must guarantee that {@link
 * javax.faces.application.Application#publishEvent} is called,
 * immediately after any <code>UIComponent</code> instance is added to
 * the view hierarchy <strong>except</strong> in the case where {@link
 * javax.faces.render.ResponseStateManager#isPostback} returns
 * <code>true</code> <strong>at the same time as</strong> {@link
 * javax.faces.context.FacesContext#getCurrentPhaseId} returns {@link
 * javax.faces.event.PhaseId#RESTORE_VIEW}.  When both of those
 * conditions are met, {@link
 * javax.faces.application.Application#publishEvent} must not be called.</p>
 * 
 * </div>
 *
 * @since 2.0
 */
public class AfterAddToViewEvent extends ComponentSystemEvent {

    private static final long serialVersionUID = -5706460518363094948L;

    /**
     * Namespaced key to mark this component having been added
     * to its parent.
     */
    private static final String ADDED_ATTRIBUTE_KEY = "javax.faces.private.ADDED";


    // ------------------------------------------------------------ Constructors


    /**
     * <p class="changed_added_2_0">Instantiate a new
     * <code>AfterAddToParentEvent</code> that indicates the argument
     * <code>component</code> was just added to the view.</p>

     * @param component the <code>UIComponent</code> that has just been
     * added to the view.
     *
     * @throws <code>NullPointerException</code> if the argument is <code>null</code>.
     */
    public AfterAddToViewEvent(UIComponent component) {
        super(component);
    }


    // --------------------------------------- Methods from ComponentSystemEvent

    /**
     * <p class="changed_added_2_0">Returns <code>true</code> if and
     * only if the argument <code>listener</code> is an instance of
     * {@link SystemEventListener}.</p>
     * @param listener
     */
    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        Map<String,Object> attributes = getComponent().getAttributes();
        boolean result = false;
        if (!attributes.containsKey(ADDED_ATTRIBUTE_KEY)) {
            result = (listener instanceof SystemEventListener);
            attributes.put(ADDED_ATTRIBUTE_KEY, Boolean.TRUE);
        }
        return result;
    }
    

}