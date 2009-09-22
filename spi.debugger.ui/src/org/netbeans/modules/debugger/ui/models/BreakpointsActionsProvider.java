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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
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

package org.netbeans.modules.debugger.ui.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.modules.debugger.ui.actions.AddBreakpointAction;
import org.netbeans.spi.viewmodel.Models;
import org.netbeans.spi.viewmodel.TreeModel;
import org.netbeans.spi.viewmodel.NodeActionsProvider;
import org.netbeans.spi.viewmodel.ModelListener;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.actions.Presenter;
import org.openide.windows.TopComponent;


/**
 * @author   Jan Jancura
 */
public class BreakpointsActionsProvider implements NodeActionsProvider {
    
    
    private static final Action NEW_BREEAKPOINT_ACTION = new AbstractAction 
        (NbBundle.getBundle (BreakpointsActionsProvider.class).getString
            ("CTL_BreakpointAction_New_Label")) {
            public void actionPerformed (ActionEvent e) {
                new AddBreakpointAction ().actionPerformed (null);
            }
    };
    private static final Action ENABLE_ALL_ACTION = new AbstractAction 
        (NbBundle.getBundle (BreakpointsActionsProvider.class).getString
            ("CTL_BreakpointAction_EnableAll_Label")) {
            public boolean isEnabled () {
                DebuggerManager dm = DebuggerManager.getDebuggerManager ();
                Breakpoint[] bs = dm.getBreakpoints ();
                int i, k = bs.length;
                for (i = 0; i < k; i++) {
                    if (!bs[i].isEnabled()) {
                        return true;
                    }
                }
                return false;
            }
            public void actionPerformed (ActionEvent e) {
                DebuggerManager dm = DebuggerManager.getDebuggerManager ();
                Breakpoint[] bs = dm.getBreakpoints ();
                int i, k = bs.length;
                for (i = 0; i < k; i++)
                    bs [i].enable ();
            }
    };
    private static final Action DISABLE_ALL_ACTION = new AbstractAction 
        (NbBundle.getBundle (BreakpointsActionsProvider.class).getString
            ("CTL_BreakpointAction_DisableAll_Label")) {
            public boolean isEnabled () {
                DebuggerManager dm = DebuggerManager.getDebuggerManager ();
                Breakpoint[] bs = dm.getBreakpoints ();
                int i, k = bs.length;
                for (i = 0; i < k; i++) {
                    if (bs[i].isEnabled()) {
                        return true;
                    }
                }
                return false;
            }
            public void actionPerformed (ActionEvent e) {
                DebuggerManager dm = DebuggerManager.getDebuggerManager ();
                Breakpoint[] bs = dm.getBreakpoints ();
                int i, k = bs.length;
                for (i = 0; i < k; i++)
                    bs [i].disable ();
            }
    };
    private static final Action DELETE_ALL_ACTION = new AbstractAction 
        (NbBundle.getBundle (BreakpointsActionsProvider.class).getString
            ("CTL_BreakpointAction_DeleteAll_Label")) {
            public boolean isEnabled () {
                DebuggerManager dm = DebuggerManager.getDebuggerManager ();
                Breakpoint[] bs = dm.getBreakpoints ();
                return bs.length > 0;
            }
            public void actionPerformed (ActionEvent e) {
                DebuggerManager dm = DebuggerManager.getDebuggerManager ();
                Breakpoint[] bs = dm.getBreakpoints ();
                int i, k = bs.length;
                for (i = 0; i < k; i++)
                    dm.removeBreakpoint (bs [i]);
            }
    };
    private static final Action ENABLE_ACTION = Models.createAction (
        NbBundle.getBundle (BreakpointsActionsProvider.class).getString
            ("CTL_BreakpointAction_Enable_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                return true;
            }
            public void perform (Object[] nodes) {
                int i, k = nodes.length;
                for (i = 0; i < k; i++)
                    ((Breakpoint) nodes [i]).enable ();
            }
        },
        Models.MULTISELECTION_TYPE_ANY
    );
    private static final Action DISABLE_ACTION = Models.createAction (
        NbBundle.getBundle (BreakpointsActionsProvider.class).getString
            ("CTL_BreakpointAction_Disable_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                return true;
            }
            public void perform (Object[] nodes) {
                int i, k = nodes.length;
                for (i = 0; i < k; i++)
                    ((Breakpoint) nodes [i]).disable ();
            }
        },
        Models.MULTISELECTION_TYPE_ANY
    );
    private static final Action DELETE_ACTION = Models.createAction (
        NbBundle.getBundle (BreakpointsActionsProvider.class).getString
            ("CTL_BreakpointAction_Delete_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                return true;
            }
            public void perform (final Object[] nodes) {
                RequestProcessor.getDefault().post(new Runnable() {
                    public void run() {
                        DebuggerManager dm = DebuggerManager.getDebuggerManager ();
                        int i, k = nodes.length;
                        for (i = 0; i < k; i++)
                            dm.removeBreakpoint ((Breakpoint) nodes [i]);
                    }
                });
            }
        },
        Models.MULTISELECTION_TYPE_ANY
    );
    static { 
        DELETE_ACTION.putValue (
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke ("DELETE")
        );
    };
    private static final Action SET_GROUP_NAME_ACTION = Models.createAction (
        NbBundle.getBundle (BreakpointsActionsProvider.class).getString
            ("CTL_BreakpointAction_SetGroupName_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                return true;
            }
            public void perform (Object[] nodes) {
                setGroupName (nodes);
            }
        },
        Models.MULTISELECTION_TYPE_ALL
    );
    private static final Action DELETE_ALL_ACTION_S = Models.createAction (
        NbBundle.getBundle (BreakpointsActionsProvider.class).getString
            ("CTL_BreakpointAction_DeleteAll_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                return true;
            }
            public void perform (Object[] nodes) {
                String groupName = (String) nodes [0];
                DebuggerManager dm = DebuggerManager.getDebuggerManager ();
                Breakpoint[] bs = dm.getBreakpoints ();
                int i, k = bs.length;
                for (i = 0; i < k; i++)
                    if (bs [i].getGroupName ().equals (groupName))
                        dm.removeBreakpoint (bs [i]);
            }
        },
        Models.MULTISELECTION_TYPE_EXACTLY_ONE
    );
    private static final Action ENABLE_ALL_ACTION_S = Models.createAction (
        NbBundle.getBundle (BreakpointsActionsProvider.class).getString
            ("CTL_BreakpointAction_EnableAll_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                String groupName = (String) node;
                DebuggerManager dm = DebuggerManager.getDebuggerManager ();
                Breakpoint[] bs = dm.getBreakpoints ();
                int i, k = bs.length;
                for (i = 0; i < k; i++) {
                    if (bs [i].getGroupName ().equals (groupName)) {
                        if (!bs[i].isEnabled()) {
                            return true;
                        }
                    }
                }
                return false;
            }
            public void perform (Object[] nodes) {
                String groupName = (String) nodes [0];
                Breakpoint[] bs = DebuggerManager.getDebuggerManager ().
                    getBreakpoints ();
                int i, k = bs.length;
                for (i = 0; i < k; i++)
                    if (bs [i].getGroupName ().equals (groupName))
                        bs [i].enable ();
            }
        },
        Models.MULTISELECTION_TYPE_EXACTLY_ONE
    );
    private static final Action DISABLE_ALL_ACTION_S = Models.createAction (
        NbBundle.getBundle (BreakpointsActionsProvider.class).getString
            ("CTL_BreakpointAction_DisableAll_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                String groupName = (String) node;
                DebuggerManager dm = DebuggerManager.getDebuggerManager ();
                Breakpoint[] bs = dm.getBreakpoints ();
                int i, k = bs.length;
                for (i = 0; i < k; i++) {
                    if (bs [i].getGroupName ().equals (groupName)) {
                        if (bs[i].isEnabled()) {
                            return true;
                        }
                    }
                }
                return false;
            }
            public void perform (Object[] nodes) {
                String groupName = (String) nodes [0];
                Breakpoint[] bs = DebuggerManager.getDebuggerManager ().
                    getBreakpoints ();
                int i, k = bs.length;
                for (i = 0; i < k; i++)
                    if (bs [i].getGroupName ().equals (groupName))
                        bs [i].disable ();
            }
        },
        Models.MULTISELECTION_TYPE_EXACTLY_ONE
    );

    
    //private Vector listeners = new Vector ();

    private Action moveIntoGroupAction = new MoveIntoGroupAction();
    
    
    public Action[] getActions (Object node) throws UnknownTypeException {
        if (node == TreeModel.ROOT) 
            return new Action [] {
                NEW_BREEAKPOINT_ACTION,
                null,
                ENABLE_ALL_ACTION,
                DISABLE_ALL_ACTION,
                DELETE_ALL_ACTION,
                null
            };
        if (node instanceof String)
            return new Action [] {
                SET_GROUP_NAME_ACTION,
                null,
                ENABLE_ALL_ACTION_S,
                DISABLE_ALL_ACTION_S,
                DELETE_ALL_ACTION_S,
                null
            };
        if (node instanceof Breakpoint)
            if (((Breakpoint) node).isEnabled ())
                return new Action [] {
                    DISABLE_ACTION,
                    moveIntoGroupAction,
                    null,
                    NEW_BREEAKPOINT_ACTION,
                    null,
                    ENABLE_ALL_ACTION,
                    DISABLE_ALL_ACTION,
                    null,
                    DELETE_ACTION,
                    DELETE_ALL_ACTION,
                    null
                };
            else
                return new Action [] {
                    ENABLE_ACTION,
                    moveIntoGroupAction,
                    null,
                    NEW_BREEAKPOINT_ACTION,
                    null,
                    ENABLE_ALL_ACTION,
                    DISABLE_ALL_ACTION,
                    null,
                    DELETE_ACTION,
                    DELETE_ALL_ACTION,
                    null
                };
        throw new UnknownTypeException (node);
    }
    
    public void performDefaultAction (Object node) throws UnknownTypeException {
        if (node == TreeModel.ROOT) 
            return;
        if (node instanceof String) 
            return;
        if (node instanceof Breakpoint) 
            return;
        throw new UnknownTypeException (node);
    }

    public void addModelListener (ModelListener l) {
        //listeners.add (l);
    }

    public void removeModelListener (ModelListener l) {
        //listeners.remove (l);
    }
    
//    public void fireTreeChanged () {
//        Vector v = (Vector) listeners.clone ();
//        int i, k = v.size ();
//        for (i = 0; i < k; i++)
//            ((TreeModelListener) v.get (i)).treeChanged ();
//    }

    private static void setGroupName (Object[] nodes) {
        NotifyDescriptor.InputLine descriptor = new NotifyDescriptor.InputLine (
            NbBundle.getBundle (BreakpointsActionsProvider.class).getString
                ("CTL_BreakpointAction_GroupDialog_NameLabel"),
            NbBundle.getBundle (BreakpointsActionsProvider.class).getString
                ("CTL_BreakpointAction_GroupDialog_Title")
        );
        if (DialogDisplayer.getDefault ().notify (descriptor) == 
            NotifyDescriptor.OK_OPTION
        ) {
           int i, k = nodes.length;
            String newName = descriptor.getInputText ();
            for (i = 0; i < k; i++) {
                if (nodes [i] instanceof String) {
                    String oldName = (String) nodes [i];
                    Breakpoint[] bs = DebuggerManager.getDebuggerManager ().
                        getBreakpoints ();
                    int j, jj = bs.length;
                    for (j = 0; j < jj; j++)
                        if (bs[j].getGroupName().equals(oldName)) {
                            bs[j].setGroupName(newName);
                        }
                } else if (nodes [i] instanceof Breakpoint) {
                    ((Breakpoint) nodes [i]).setGroupName ( newName );
                }
            }
        }
    }

    private static class MoveIntoGroupAction extends AbstractAction implements Presenter.Popup {

        public MoveIntoGroupAction() {
        }

        public void actionPerformed(ActionEvent e) {
            // Just displays popup menu
        }

        private Breakpoint[] getCurrentBreakpoints() {
            Node[] ns = TopComponent.getRegistry ().getActivatedNodes ();
            int i, k = ns.length;
            List<Breakpoint> bps = new ArrayList<Breakpoint>();
            for (i = 0; i < k; i++) {
                Object node = ns[i].getLookup().lookup(Object.class);
                if (node instanceof Breakpoint) {
                    bps.add((Breakpoint) node);
                }
            }
            return bps.toArray(new Breakpoint[] {});
        }

        private String findCommonBpGroup(Breakpoint[] bps) {
            String g = null;
            for (Breakpoint bp : bps) {
                String gn = bp.getGroupName();
                if (g == null) {
                    g = gn;
                } else {
                    if (!g.equals(gn)) {
                        return null;
                    }
                }
            }
            return g;
        }

        public JMenuItem getPopupPresenter() {
            final Breakpoint[] bps = getCurrentBreakpoints();
            String bpGroup = findCommonBpGroup(bps);

            JMenu moveIntoGroupMenu = new JMenu
                (NbBundle.getMessage(BreakpointsActionsProvider.class, "CTL_BreakpointAction_MoveIntoGroup"));

            Set<String> groupNames = new TreeSet<String>();
            Breakpoint[] bs = DebuggerManager.getDebuggerManager ().getBreakpoints ();
            for (int i = 0; i < bs.length; i++) {
                String gn = bs[i].getGroupName();
                groupNames.add(gn);
            }
            groupNames.add(""); // Assure that the "default" group is there.
            if (bpGroup != null) {
                groupNames.remove(bpGroup);
            }

            for (final String gn : groupNames) {
                JMenuItem group;
                if (gn.length() > 0) {
                    group = new JMenuItem(gn);
                } else {
                    group = new JMenuItem(
                            NbBundle.getMessage(BreakpointsActionsProvider.class,
                                                "CTL_BreakpointAction_MoveIntoDefaultGroup_Label"));
                }
                moveIntoGroupMenu.add(group);
                group.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        for (Breakpoint bp : bps) {
                            bp.setGroupName(gn);
                        }
                    }
                });
            }

            JMenuItem newGroup = new JMenuItem(
                    NbBundle.getMessage(BreakpointsActionsProvider.class,
                                        "CTL_BreakpointAction_MoveIntoNewGroup_Label"));
            newGroup.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setGroupName(bps);
                }
            });
            moveIntoGroupMenu.add(newGroup);
            return moveIntoGroupMenu;
        }
    }
}
