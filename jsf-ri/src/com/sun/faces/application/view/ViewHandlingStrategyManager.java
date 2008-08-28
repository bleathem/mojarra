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
 */

package com.sun.faces.application.view;

import com.sun.faces.config.WebConfiguration;

/**
 * Interface for working with multiple {@link com.sun.faces.application.view.ViewHandlingStrategy}
 * implementations.
 */
public class ViewHandlingStrategyManager {

    // The strategies associate with this instance
    private volatile ViewHandlingStrategy[] strategies;


    // ------------------------------------------------------------- Constructor


    /**
     * Be default, if {@link com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter#DisableFacesPDL}
     * isn't enabled, the strategies available (in order) will be {@link com.sun.faces.application.view.FacesPDLViewHandlingStrategy}
     * and {@link com.sun.faces.application.view.JspViewHandlingStrategy}.  Otherwise, only the
     * {@link com.sun.faces.application.view.JspViewHandlingStrategy} will be available.
     */
    public ViewHandlingStrategyManager() {

        WebConfiguration webConfig = WebConfiguration.getInstance();
        boolean pdlDisabled = webConfig
              .isOptionEnabled(WebConfiguration.BooleanWebContextInitParameter.DisableFacesPDL);
        strategies = ((pdlDisabled)
                      ? new ViewHandlingStrategy[] { new JspViewHandlingStrategy() }
                      : new ViewHandlingStrategy[] { new FacesPDLViewHandlingStrategy(),
                                                     new JspViewHandlingStrategy() });

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>
     * Iterate through the available {@link com.sun.faces.application.view.ViewHandlingStrategy}
     * implementations.  The first one to return true from {@link com.sun.faces.application.view.ViewHandlingStrategy#handlesViewId(String)}
     * will be the {@link com.sun.faces.application.view.ViewHandlingStrategy} returned.
     * <p>
     *
     * @param viewId the viewId to match a {@link com.sun.faces.application.view.ViewHandlingStrategy} to
     *
     * @throws ViewHandlingStrategyNotFoundException if no match is found.
     *
     * @return a {@link com.sun.faces.application.view.ViewHandlingStrategy} for
     *  the specifed <code>viewId</code>
     */
    public ViewHandlingStrategy getStrategy(String viewId) {

        for (ViewHandlingStrategy strategy : strategies) {
            if (strategy.handlesViewId(viewId)) {
                return strategy;
            }
        }
        throw new ViewHandlingStrategyNotFoundException();
        
    }


    /**
     * @return the currently registered {@link com.sun.faces.application.view.ViewHandlingStrategy}
     *  implementations.
     */
    public ViewHandlingStrategy[] getViewHandlingStrategies() {

        return strategies;

    }


    /**
     * Update the {@link com.sun.faces.application.view.ViewHandlingStrategy} implementations
     * to be applied when processing JSF requests.
     * @param stratagies the new view handling strategies
     */
    public synchronized void setViewHandlingStrategies(ViewHandlingStrategy[] stratagies) {

        this.strategies = stratagies;
        
    }


}