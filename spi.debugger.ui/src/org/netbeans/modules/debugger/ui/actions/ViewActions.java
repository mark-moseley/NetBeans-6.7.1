/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */


package org.netbeans.modules.debugger.ui.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.AbstractAction;


import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;


/**
 * Opens View TopComponent.
 *
 * @author Jan Jancura, Martin Entlicher
 */
public class ViewActions extends AbstractAction {
    
    private String viewName;

    private ViewActions (String viewName) {
        this.viewName = viewName;
    }

    public Object getValue(String key) {
        if (key == Action.NAME) {
            return NbBundle.getMessage (ViewActions.class, (String) super.getValue(key));
        }
        Object value = super.getValue(key);
        return value;
    }
    
    public void actionPerformed (ActionEvent evt) {
        openComponent (viewName, true);
    }
    
    static TopComponent openComponent (String viewName, boolean activate) {
        TopComponent view = WindowManager.getDefault().findTopComponent(viewName);
        if (view == null) {
            throw new IllegalArgumentException(viewName);
        }
        view.open();
        if (activate) {
            view.requestActive();
        }
        return view;
    }
    
    
    /**
     * Creates an action that opens Breakpoints TopComponent.
     */
    public static Action createBreakpointsViewAction () {
        ViewActions action = new ViewActions("breakpointsView");
        // When changed, update also mf-layer.xml, where are the properties duplicated because of Actions.alwaysEnabled()
        action.putValue (Action.NAME, "CTL_BreakpointsAction");
        action.putValue ("iconbase",
                "org/netbeans/modules/debugger/resources/breakpointsView/Breakpoint.gif" // NOI18N
        );
        return action;
    }

    /**
     * Creates an action that opens Call Stack TopComponent.
     */
    public static Action createCallStackViewAction () {
        ViewActions action = new ViewActions("callstackView");
        // When changed, update also mf-layer.xml, where are the properties duplicated because of Actions.alwaysEnabled()
        action.putValue (Action.NAME, "CTL_CallStackAction");
        action.putValue ("iconbase",
                "org/netbeans/modules/debugger/resources/callStackView/call_stack_16.png" // NOI18N
        );
        return action;
    }

    /**
     * Creates an action that opens Local Variables TopComponent.
     */
    public static Action createLocalsViewAction() {
        ViewActions action = new ViewActions("localsView");
        // When changed, update also mf-layer.xml, where are the properties duplicated because of Actions.alwaysEnabled()
        action.putValue (Action.NAME, "CTL_LocalVariablesAction");
        action.putValue ("iconbase",
                "org/netbeans/modules/debugger/resources/localsView/local_variable_16.png" // NOI18N
        );
        return action;
    }

    /**
     * Creates an action that opens Sessions TopComponent.
     */
    public static Action createSessionsViewAction () {
        ViewActions action = new ViewActions("sessionsView");
        // When changed, update also mf-layer.xml, where are the properties duplicated because of Actions.alwaysEnabled()
        action.putValue (Action.NAME, "CTL_SessionsAction");
        action.putValue ("iconbase",
                "org/netbeans/modules/debugger/resources/sessionsView/session_16.png" // NOI18N
        );
        return action;
    }

    /**
     * Creates an action that opens Threads TopComponent.
     */
    public static Action createThreadsViewAction () {
        ViewActions action = new ViewActions("threadsView");
        // When changed, update also mf-layer.xml, where are the properties duplicated because of Actions.alwaysEnabled()
        action.putValue (Action.NAME, "CTL_ThreadsAction");
        action.putValue ("iconbase",
                "org/netbeans/modules/debugger/resources/threadsView/ThreadGroup.gif" // NOI18N
        );
        return action;
    }
    
    
    /**
     * Creates an action that opens Watches TopComponent.
     */
    public static Action createWatchesViewAction() {
        ViewActions action = new ViewActions("watchesView");
        // When changed, update also mf-layer.xml, where are the properties duplicated because of Actions.alwaysEnabled()
        action.putValue (Action.NAME, "CTL_WatchesAction");
        action.putValue ("iconbase",
                "org/netbeans/modules/debugger/resources/watchesView/watch_16.png" // NOI18N
        );
        return action;
    }

    /**
     * Creates an action that opens Sources TopComponent.
     */
    public static Action createSourcesViewAction() {
        ViewActions action = new ViewActions("sources");
        // When changed, update also mf-layer.xml, where are the properties duplicated because of Actions.alwaysEnabled()
        action.putValue (Action.NAME, "CTL_SourcesAction");
        action.putValue ("iconbase",
                "org/netbeans/modules/debugger/resources/sourcesView/sources_16.png" // NOI18N
        );
        return action;
    }

}

