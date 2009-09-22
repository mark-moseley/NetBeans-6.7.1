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

package org.netbeans.modules.debugger.ui.actions;

import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.modules.debugger.ui.Utils;
import org.netbeans.spi.debugger.ui.AttachType;
import org.netbeans.spi.debugger.ui.Controller;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;


/**
* Connects debugger to some currently running VM.
* This class is final only for performance reasons,
* can be happily unfinaled if desired.
*
* @author   Jan Jancura
*/
public final class ConnectAction extends AbstractAction {
    
    private Dialog dialog;
    private JButton bOk;
    private JButton bCancel;

    
    public ConnectAction () {
        putValue (
            Action.NAME, 
            NbBundle.getMessage (
                ConnectAction.class, 
                "CTL_Connect"
            )
        );
        putValue (
            "iconBase", // NOI18N
            "org/netbeans/modules/debugger/resources/actions/Attach.gif" // NOI18N
        );
    }

    public boolean isEnabled() {
        List attachTypes = DebuggerManager.getDebuggerManager ().lookup (
            null, AttachType.class
        );
        return attachTypes.size() > 0;
    }
    
    public void actionPerformed (ActionEvent evt) {
        bOk = new JButton (NbBundle.getMessage (ConnectAction.class, "CTL_Ok")); // NOI18N
        bCancel = new JButton (NbBundle.getMessage (ConnectAction.class, "CTL_Cancel")); // NOI18N
        bOk.getAccessibleContext ().setAccessibleDescription (NbBundle.getMessage (ConnectAction.class, "ACSD_CTL_Ok")); // NOI18N
        bCancel.getAccessibleContext ().setAccessibleDescription (NbBundle.getMessage (ConnectAction.class, "ACSD_CTL_Cancel")); // NOI18N
        ConnectorPanel cp = new ConnectorPanel ();
        DialogDescriptor descr = new DialogDescriptor (
            cp,
            NbBundle.getMessage (ConnectAction.class, "CTL_Connect_to_running_process"),
            true, // modal
            new ConnectListener (cp)
        );
        descr.setOptions (new JButton[] {
            bOk, bCancel
        });
        descr.setClosingOptions (new Object [0]);
        dialog = DialogDisplayer.getDefault ().createDialog (descr);
        dialog.setVisible(true);
    }


    // innerclasses ............................................................
    private class ConnectListener implements ActionListener, PropertyChangeListener {
        
        ConnectorPanel connectorPanel;
        Controller controller;
        
        ConnectListener (ConnectorPanel connectorPanel) {
            this.connectorPanel = connectorPanel;
            startListening();
            setValid();
            connectorPanel.addPropertyChangeListener(this);
        }
        
        public void actionPerformed (ActionEvent e) {
            boolean okPressed = bOk.equals (e.getSource ());
            boolean close = false;
            if (okPressed) {
                close = connectorPanel.ok ();
            } else {
                close = connectorPanel.cancel ();
            }
            if (!close) return;
            connectorPanel.removePropertyChangeListener (this);
            stopListening ();
            dialog.setVisible (false);
            dialog.dispose ();
            dialog = null;
        }
        
        void startListening () {
            controller = connectorPanel.getController ();
            if (controller == null) return;
            controller.addPropertyChangeListener (this);
        }
        
        void stopListening () {
            if (controller == null) return;
            controller.removePropertyChangeListener (this);
            controller = null;
        }

        void setValid () {
            Controller controller = connectorPanel.getController ();
            if (controller == null) {
                bOk.setEnabled (false);
                return;
            }
            bOk.setEnabled (controller.isValid ());
        }

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName () == ConnectorPanel.PROP_TYPE) {
                stopListening ();
                setValid ();
                startListening ();
            } else if (evt.getPropertyName () == Controller.PROP_VALID) {
                setValid ();
            }
        }
        
    }
}


