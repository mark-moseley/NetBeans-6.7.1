/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
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
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */

package org.netbeans.debuggercore;

import java.io.IOException;
import junit.framework.Test;
import junit.textui.TestRunner;
import org.netbeans.jellytools.Bundle;
import org.netbeans.jellytools.EditorOperator;
import org.netbeans.jellytools.JellyTestCase;
import org.netbeans.jellytools.NbDialogOperator;
import org.netbeans.jellytools.TopComponentOperator;
import org.netbeans.jellytools.TreeTableOperator;
import org.netbeans.jellytools.actions.OpenAction;
import org.netbeans.jellytools.modules.debugger.actions.ContinueAction;
import org.netbeans.jellytools.nodes.Node;
import org.netbeans.jellytools.nodes.SourcePackagesNode;
import org.netbeans.jemmy.EventTool;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JCheckBoxOperator;
import org.netbeans.jemmy.operators.JComboBoxOperator;
import org.netbeans.jemmy.operators.JEditorPaneOperator;
import org.netbeans.jemmy.operators.JTableOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import org.netbeans.jemmy.operators.Operator.StringComparator;
import org.netbeans.junit.NbModuleSuite;
/**
 *
 * @author felipee
 */
public class LineBreakpointsHitCountTest extends JellyTestCase{

       //MainWindowOperator.StatusTextTracer stt = null;
    /**
     *
     * @param name
     */
    public LineBreakpointsHitCountTest(String name) {
        super(name);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     *
     * @return
     */
    public static Test suite() {
        return NbModuleSuite.create(
                NbModuleSuite.createConfiguration(LineBreakpointsHitCountTest.class).addTest(           
                    "testLineBreakpointsHitCount" 
                )
            .enableModules(".*").clusters(".*"));
    }

    /**
     *
     */
    @Override
    public void setUp() throws IOException {
        openDataProjects(Utilities.testProjectName);
        System.out.println("########  " + getName() + "  ####### ");
    }

    /**
     *
     */
    @Override
    public void tearDown() {
        JemmyProperties.getCurrentOutput().printTrace("\nteardown\n");
        Utilities.endAllSessions();
        Utilities.deleteAllBreakpoints();
    }

     public void testLineBreakpointsHitCount() throws Throwable {
        try {
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode);
            EditorOperator eo = new EditorOperator("MemoryView.java");
            new EventTool().waitNoEvent(500);
            //toggle breakpoints
            Utilities.toggleBreakpoint(eo, 64);
            Utilities.toggleBreakpoint(eo, 65);
            Utilities.toggleBreakpoint(eo, 66);
            //set hit conditions
            Utilities.showDebuggerView(Utilities.breakpointsViewTitle);
            JTableOperator jTableOperator = new JTableOperator(new TopComponentOperator(Utilities.breakpointsViewTitle));
            new JPopupMenuOperator(jTableOperator.callPopupOnCell(0, 0)).pushMenuNoBlock("Properties");
            NbDialogOperator dialog = new NbDialogOperator(Utilities.customizeBreakpointTitle);
            new JCheckBoxOperator(dialog, 1).changeSelection(true);
            new JComboBoxOperator(dialog, 0).selectItem(Bundle.getString("org.netbeans.modules.debugger.jpda.ui.breakpoints.Bundle", "ConditionsPanel.cbWhenHitCount.equals"));
            new JTextFieldOperator(dialog, 2).setText("45");
            dialog.ok();

            new JPopupMenuOperator(jTableOperator.callPopupOnCell(1, 0)).pushMenuNoBlock("Properties");
            dialog = new NbDialogOperator(Utilities.customizeBreakpointTitle);
            new JCheckBoxOperator(dialog, 1).changeSelection(true);
            new JComboBoxOperator(dialog, 0).selectItem(Bundle.getString("org.netbeans.modules.debugger.jpda.ui.breakpoints.Bundle", "ConditionsPanel.cbWhenHitCount.greater"));
            new JTextFieldOperator(dialog, 2).setText("48");
            dialog.ok();

            new JPopupMenuOperator(jTableOperator.callPopupOnCell(2, 0)).pushMenuNoBlock("Properties");
            dialog = new NbDialogOperator(Utilities.customizeBreakpointTitle);
            new JCheckBoxOperator(dialog, 1).changeSelection(true);
            new JComboBoxOperator(dialog, 0).selectItem(Bundle.getString("org.netbeans.modules.debugger.jpda.ui.breakpoints.Bundle", "ConditionsPanel.cbWhenHitCount.multiple"));
            new JTextFieldOperator(dialog, 2).setText("47");
            dialog.ok();

            //start debugging
            Utilities.startDebugger();
            //check values
            StringComparator comp = new StringComparator() {

                public boolean equals(String arg0, String arg1) {
                    return arg0.equals(arg1);
                }
            };
            Utilities.waitStatusText("Thread main stopped at MemoryView.java:64");
            Utilities.showDebuggerView(Utilities.localVarsViewTitle);
            jTableOperator = new JTableOperator(new TopComponentOperator(Utilities.localVarsViewTitle));
            TreeTableOperator treeTableOperator = new TreeTableOperator((javax.swing.JTable) jTableOperator.getSource());
            int row = treeTableOperator.findCellRow("i", comp);
            org.openide.nodes.Node.Property property = (org.openide.nodes.Node.Property) treeTableOperator.getValueAt(row, 2);
            assertEquals("44", property.getValue());
/* THIS PART IS ABOUT TO BE REVIEWED */

            /*
            new ContinueAction().perform();




            try {
                   System.out.println("Debugger Console Status: " + Utilities.getDebuggerConsoleStatus());
                    System.out.println("Last Line is: " + Utilities.getConsoleLastLineText());

                Utilities.waitStatusText("Thread main stopped at MemoryView.java:66");
            } catch (Throwable e) {
                if (!Utilities.checkConsoleLastLineForText(Utilities.runningStatusBarText)) {
                    System.err.println(e.getMessage());
                    //System.out.println("Debugger Console Status: " + Utilities.getDebuggerConsoleStatus());
                    //System.out.println("Last Line is: " + Utilities.getConsoleLastLineText());
                    throw e;
                }
            }

            //Utilities.waitStatusText("Thread main stopped at MemoryView.java:66", 10000);
            Utilities.showDebuggerView(Utilities.localVarsViewTitle);
            jTableOperator = new JTableOperator(new TopComponentOperator(Utilities.localVarsViewTitle));
            treeTableOperator = new TreeTableOperator((javax.swing.JTable) jTableOperator.getSource());
            row = treeTableOperator.findCellRow("i", comp);
            property = (org.openide.nodes.Node.Property) treeTableOperator.getValueAt(row, 2);
            assertEquals("46", property.getValue());
            new ContinueAction().perform();

            Utilities.waitStatusText("Thread main stopped at MemoryView.java:65");
            Utilities.showDebuggerView(Utilities.localVarsViewTitle);
            jTableOperator = new JTableOperator(new TopComponentOperator(Utilities.localVarsViewTitle));
            treeTableOperator = new TreeTableOperator((javax.swing.JTable) jTableOperator.getSource());
            row = treeTableOperator.findCellRow("i", comp);
            property = (org.openide.nodes.Node.Property) treeTableOperator.getValueAt(row, 2);
            assertEquals("47", property.getValue());
            */
            
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }
      protected void setBreakpointType(NbDialogOperator dialog, String type) {
        new JComboBoxOperator(dialog, 0).selectItem("Java");
        new JComboBoxOperator(dialog, 1).selectItem(type);
    }

}
