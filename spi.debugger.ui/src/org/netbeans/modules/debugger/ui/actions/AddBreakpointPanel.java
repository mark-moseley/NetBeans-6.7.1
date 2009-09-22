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

import java.awt.Window;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Set;
import java.util.TreeSet;

import org.openide.awt.Mnemonics;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

import org.netbeans.api.debugger.*;
import org.netbeans.api.debugger.Breakpoint.*;
import org.netbeans.spi.debugger.ui.BreakpointType;
import org.netbeans.spi.debugger.ui.Controller;
import org.openide.util.Exceptions;


/**
* New Breakpint Dialog panel.
*
* @author  Jan Jacura
*/
// <RAVE>
// Make the class implement HelpCtx.Provider to be able to get Help for it
// public class AddBreakpointPanel extends javax.swing.JPanel {
// ====
public class AddBreakpointPanel extends javax.swing.JPanel implements HelpCtx.Provider {
// </RAVE>

    public static final String PROP_TYPE = "type";
    
    private static Object lastSelectedCategory;
    
    // variables ...............................................................
    
    private boolean                 doNotRefresh = false;
    /** List of cathegories. */
    private Set                     cathegories = new TreeSet ();
    /** Types in currently selected cathegory. */
    private ArrayList               types = new ArrayList ();
    /** Currently selected type. */
    private BreakpointType          type;

    private JComponent              customizer;
    private javax.swing.JLabel      jLabel1;
    private javax.swing.JLabel      jLabel2;
    private javax.swing.JComboBox   cbCathegory;
    private javax.swing.JComboBox   cbEvents;
    private javax.swing.JPanel      pEvent;
    /** <CODE>HelpCtx</CODE> of the selected breakpoint type's customizer */
    private HelpCtx                 helpCtx;
    private List<? extends BreakpointType> breakpointTypes;


    // init ....................................................................

    /** Creates new form AddBreakpointPanel */
    public AddBreakpointPanel () {
        breakpointTypes = DebuggerManager.getDebuggerManager ().lookup 
            (null, BreakpointType.class);
        String def = null;
        for (BreakpointType bt : breakpointTypes) {
            String dn = bt.getCategoryDisplayName ();
            if (!cathegories.contains (dn)) {
                cathegories.add (dn);
            }
            if (bt.isDefault ())
                def = dn;
        }
        cbCathegory = new javax.swing.JComboBox(cathegories.toArray());
        
        initComponents ();
        if (def != null) {
            cbCathegory.setSelectedItem(def);
            selectCathegory (def);
        } else if (breakpointTypes.size () > 0) {
            if (lastSelectedCategory != null && cathegories.contains(lastSelectedCategory)) {
                cbCathegory.setSelectedItem(lastSelectedCategory);
            } else {
                cbCathegory.setSelectedIndex(0);
            }
            selectCathegory ((String) cbCathegory.getSelectedItem ());
        }
    }


    // public interface ........................................................

    public BreakpointType getType () {
        return type;
    }
    
    Controller getController () {
        if (type != null) {
            Controller c = type.getController();
            if (c == null && customizer instanceof Controller) {
                //Exceptions.printStackTrace(new IllegalStateException("FIXME: JComponent "+customizer+" must not implement Controller interface!"));
                return (Controller) customizer;
            } else {
                return c;
            }
        } else {
            return null;
        }
    }

    boolean isNoValidityController() {
        // This controller must not be asked for isValid()
        return type.getController() == null && customizer instanceof Controller;
    }
    
    
    // other methods ...........................................................
    
    private void initComponents () {
        getAccessibleContext().setAccessibleDescription(NbBundle.getBundle (AddBreakpointPanel.class).getString ("ACSD_AddBreakpointPanel")); // NOI18N
        setLayout (new java.awt.GridBagLayout ());
        java.awt.GridBagConstraints gridBagConstraints1;

        if (cathegories.size () > 1) {
                jLabel1 = new javax.swing.JLabel ();
                Mnemonics.setLocalizedText(jLabel1, NbBundle.getBundle (AddBreakpointPanel.class).
                    getString ("CTL_Breakpoint_cathegory")); // NOI18N
                gridBagConstraints1 = new java.awt.GridBagConstraints ();
                gridBagConstraints1.gridwidth = 2;
                gridBagConstraints1.insets = new java.awt.Insets (12, 12, 0, 0);
            add (jLabel1, gridBagConstraints1);

                cbCathegory.addActionListener (new java.awt.event.ActionListener () {
                    public void actionPerformed (java.awt.event.ActionEvent evt) {
                        cbCathegoryActionPerformed (evt);
                    }
                });
                cbCathegory.getAccessibleContext().setAccessibleDescription(
                NbBundle.getBundle (AddBreakpointPanel.class).getString ("ACSD_CTL_Breakpoint_cathegory")); // NOI18N
                jLabel1.setLabelFor (cbCathegory);
                gridBagConstraints1 = new java.awt.GridBagConstraints ();
                gridBagConstraints1.gridwidth = 2;
                gridBagConstraints1.insets = new java.awt.Insets (12, 12, 0, 0);
                gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
            add (cbCathegory, gridBagConstraints1);
        }

            jLabel2 = new javax.swing.JLabel ();
            Mnemonics.setLocalizedText(jLabel2, NbBundle.getBundle (AddBreakpointPanel.class).
                getString ("CTL_Breakpoint_type")); // NOI18N
            gridBagConstraints1 = new java.awt.GridBagConstraints ();
            gridBagConstraints1.gridwidth = 2;
            gridBagConstraints1.insets = new java.awt.Insets (12, 12, 0, 0);
        add (jLabel2, gridBagConstraints1);
            
            cbEvents = new javax.swing.JComboBox ();
            cbEvents.addActionListener (new java.awt.event.ActionListener () {
                public void actionPerformed (java.awt.event.ActionEvent evt) {
                    cbEventsActionPerformed ();
                }
            });
            cbEvents.getAccessibleContext().setAccessibleDescription(
                NbBundle.getBundle (AddBreakpointPanel.class).getString ("ACSD_CTL_Breakpoint_type")); // NOI18N
            cbEvents.setMaximumRowCount (12);
            gridBagConstraints1 = new java.awt.GridBagConstraints ();
            gridBagConstraints1.gridwidth = 0;
            gridBagConstraints1.insets = new java.awt.Insets (12, 12, 0, 12);
            gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        add (cbEvents, gridBagConstraints1);
            jLabel2.setLabelFor (cbEvents);
            pEvent = new javax.swing.JPanel ();
            pEvent.setLayout (new java.awt.BorderLayout ());
            gridBagConstraints1 = new java.awt.GridBagConstraints ();
            gridBagConstraints1.gridwidth = 0;
            gridBagConstraints1.insets = new java.awt.Insets (9, 9, 0, 9);
            gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.weighty = 1.0;
        add (pEvent, gridBagConstraints1);
    }

    private void cbEventsActionPerformed () {
        // Add your handling code here:
        if (doNotRefresh) return;
        SwingUtilities.invokeLater (new Runnable () {
            public void run () {
                boolean pv = cbEvents.isPopupVisible ();
                int j = cbEvents.getSelectedIndex ();
                if (j < 0) return;
                update ((BreakpointType) types.get (j));
                if (pv)
                    SwingUtilities.invokeLater (new Runnable () {
                        public void run () {
                            cbEvents.setPopupVisible (true);
                        }
                    });
                    //cbEvents.setPopupVisible (true);
            }
        });
    }

    private void cbCathegoryActionPerformed (java.awt.event.ActionEvent evt) {
        // Add your handling code here:
        if (doNotRefresh) return;
        String c = (String) cbCathegory.getSelectedItem ();
        if (c == null) return;
        selectCathegory (c);
    }
    
    private void selectCathegory (String c) {
        lastSelectedCategory = c;
        doNotRefresh = true;
        cbEvents.removeAllItems ();
        types = new ArrayList ();
        int defIndex = 0;
        for (BreakpointType bt : breakpointTypes) {
            if (!bt.getCategoryDisplayName ().equals (c))
                continue;
            cbEvents.addItem (bt.getTypeDisplayName ());
            types.add (bt);
            if (bt.isDefault ())
                defIndex = cbEvents.getItemCount () - 1;
        }
        doNotRefresh = false;
        if (defIndex < cbEvents.getItemCount ())
            cbEvents.setSelectedIndex (defIndex);
    }
    
    /**
     * Returns <CODE>HelpCtx</CODE> of the selected breakpoint type's customizer.
     * It is used in {@link AddBreakpointAction.AddBreakpointDialogManager}.
     */
    // <RAVE>
    // Make getHelpCtx() method public to correctly implement HelpCtx.Provider
    // HelpCtx getHelpCtx() {
    // ====
    public HelpCtx getHelpCtx() {
    // </RAVE>
        return helpCtx;
    }

    private void update (BreakpointType t) {
        if (type == t) return ;
        pEvent.removeAll ();
        DebuggerManager d = DebuggerManager.getDebuggerManager ();
        BreakpointType old = type;
        type = t;
        customizer = type.getCustomizer ();
        if (customizer == null) return;

        //Set HelpCtx. This method must be called _before_ the customizer
        //is added to some container, otherwise HelpCtx.findHelp(...) would
        //query also the customizer's parents.
        // <RAVE>
        // The help IDs for the customizer panels have to be different from the
        // values returned by getHelpCtx() because they provide different help
        // in the 'Add Breakpoint' dialog and when invoked in the 'Breakpoints' view
        // helpCtx = HelpCtx.findHelp (customizer);
        // ====
        String hid = (String) customizer.getClientProperty("HelpID_AddBreakpointPanel"); // NOI18N
        if (hid != null) {
            helpCtx = new HelpCtx(hid);
        } else {
            helpCtx = HelpCtx.findHelp (customizer);
        }
        // </RAVE>

        pEvent.add (customizer, "Center"); // NOI18N
        pEvent.getAccessibleContext ().setAccessibleDescription (
            customizer.getAccessibleContext ().getAccessibleDescription ()
        );
        customizer.getAccessibleContext ().setAccessibleName (
            pEvent.getAccessibleContext ().getAccessibleName ()
        );
        revalidate ();
        Window w = SwingUtilities.windowForComponent (this);
        if (w == null) return;
        w.pack ();
        firePropertyChange (PROP_TYPE, old, type);
    }
}

