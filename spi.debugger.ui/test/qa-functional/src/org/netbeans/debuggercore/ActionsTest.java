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
 * The Original Software is NetBeans.
 * The Initial Developer of the Original Software is Sun Microsystems, Inc.
 * Portions created by Sun Microsystems, Inc. are Copyright (C) 2005
 * All Rights Reserved.
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
 * Contributor(s): Sun Microsystems, Inc.
 */

package org.netbeans.debuggercore;

import java.awt.event.KeyEvent;
import java.io.IOException;
import junit.framework.Test;
import junit.textui.TestRunner;
import org.netbeans.jellytools.Bundle;
import org.netbeans.jellytools.EditorOperator;
import org.netbeans.jellytools.JellyTestCase;
import org.netbeans.jellytools.MainWindowOperator;
import org.netbeans.jellytools.ProjectsTabOperator;
import org.netbeans.jellytools.actions.Action;
import org.netbeans.jellytools.actions.DebugProjectAction;
import org.netbeans.jellytools.actions.OpenAction;
import org.netbeans.jellytools.nodes.Node;
import org.netbeans.jellytools.nodes.SourcePackagesNode;
import org.netbeans.jemmy.EventTool;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import org.netbeans.junit.NbModuleSuite;

/**
 *
 * @author cincura, ehucka, Jiri Vagner, cyhelsky
 */
public class ActionsTest extends JellyTestCase {

    public ActionsTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return NbModuleSuite.create(
            NbModuleSuite.createConfiguration(ActionsTest.class).addTest(
            "testCheckEnabledActions",
            "testCheckEnabledActionsDebugging"
            ).enableModules(".*").clusters(".*"));
    }

    /** setUp method  */
    public void setUp() throws IOException {



        openDataProjects(Utilities.testProjectName);
        System.out.println("########  " + getName() + "  #######");
    }

    public void tearDown() {
        JemmyProperties.getCurrentOutput().printTrace("\nteardown\n");
        Utilities.endAllSessions();
        Utilities.deleteAllBreakpoints();
    }

    public void testCheckEnabledActions() throws Throwable {
        try {
            Node projectNode = ProjectsTabOperator.invoke().getProjectRootNode(Utilities.testProjectName);
            new Action(null, Utilities.setMainProjectAction).perform(new ProjectsTabOperator().getProjectRootNode(Utilities.testProjectName));
            new EventTool().waitNoEvent(1000);
            Utilities.verifyPopup(projectNode, new String[]{Bundle.getString("org.netbeans.modules.java.j2seproject.ui.Bundle", "LBL_BuildAction_Name"),
            Bundle.getString("org.netbeans.modules.java.j2seproject.ui.Bundle", "LBL_RunAction_Name"),
            Bundle.getString("org.netbeans.modules.debugger.ui.actions.Bundle", "LBL_DebugProjectActionOnProject_Name")});

            //main menu actions
            //check main menu debug main project action
            assertTrue(Utilities.runMenu + "|" + Utilities.debugMainProjectItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.debugMainProjectItem, true));
            //Step into
            assertTrue(Utilities.runMenu + "|" + Utilities.stepIntoItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.stepIntoItem, true));
            //new breakpoint
            assertTrue(Utilities.runMenu + "|" + Utilities.newBreakpointItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.newBreakpointItem, true));
            //new watch
            assertTrue(Utilities.runMenu + "|" + Utilities.newWatchItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.newWatchItem, true));
            //main menu actions disabled
            //check finish debugger
            assertFalse(Utilities.runMenu + "|" + Utilities.finishSessionsItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.finishSessionsItem, false));
            //pause
            assertFalse(Utilities.runMenu + "|" + Utilities.pauseItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.pauseItem, false));
            //continue
            assertFalse(Utilities.runMenu + "|" + Utilities.continueItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.continueItem, false));
            //step over
            assertFalse(Utilities.runMenu + "|" + Utilities.stepOverItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.stepOverItem, false));
            //step over expression
            assertFalse(Utilities.runMenu + "|" + Utilities.stepOverExpresItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.stepOverExpresItem, false));
            //step out
            assertFalse(Utilities.runMenu + "|" + Utilities.stepOutItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.stepOutItem, false));
            //run to cursor
            assertFalse(Utilities.runMenu + "|" + Utilities.runToCursorItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.runToCursorItem, false));
            //run into method
            //assertFalse(Utilities.runMenu + "|" + Utilities.runIntoMethodItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.runIntoMethodItem, false));
            //apply code changes
            assertFalse(Utilities.runMenu + "|" + Utilities.applyCodeChangesItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.applyCodeChangesItem, false));
            //toggle breakpoint
            assertFalse(Utilities.runMenu + "|" + Utilities.toggleBreakpointItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.toggleBreakpointItem, false));
            //evaluate expression
            assertTrue(Utilities.runMenu + "|" + Utilities.evaluateExpressionItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.evaluateExpressionItem, true));
            MainWindowOperator.getDefault().pushKey(KeyEvent.VK_ESCAPE);

            //open source file
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            EditorOperator eo = new EditorOperator("MemoryView.java");
            try {
                eo.clickMouse(50,50,1);
            } catch (Throwable t) {
                System.err.println(t.getMessage());
            }
            Utilities.setCaret(eo, 80);
            new EventTool().waitNoEvent(1000); //because of issue 70731
            //main menu file actions
            //check debug file action
            String debugActionName = Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "LBL_DebugSingleAction_Name", new Object[]{new Integer(1), "MemoryView.java"});
            assertTrue(Utilities.runMenu + "|" + Utilities.runFileMenu + "|" + debugActionName + " is not enabled", Utilities.verifyMainMenu(Utilities.runMenu + "|" + debugActionName, true));

            //run to cursor
            assertTrue(Utilities.runMenu + "|" + Utilities.runToCursorItem + " is not enabled", Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.runToCursorItem, true));

            //toggle breakpoint
            assertTrue(Utilities.runMenu + "|" + Utilities.toggleBreakpointItem + " is not enabled", Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.toggleBreakpointItem, true));
            MainWindowOperator.getDefault().pushKey(KeyEvent.VK_ESCAPE);
            MainWindowOperator.getDefault().pushKey(KeyEvent.VK_ESCAPE);

            //source popup menu actions
            JPopupMenuOperator operator = new JPopupMenuOperator(JPopupMenuOperator.callPopup(eo, 50, 50));
            Utilities.verifyPopup(operator,
                    new String[]{
                        debugActionName,
                        Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "CTL_New_Watch"),
                        Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "CTL_Toggle_breakpoint")
                    }
            );

            //tools menu
            //debug is not visible
            for (int i = 0; i < MainWindowOperator.getDefault().getToolbarCount(); i++) {
                assertFalse("Debug toolbar is visible", MainWindowOperator.getDefault().getToolbarName(i).equals(Utilities.debugToolbarLabel));
            }
            //run
            ContainerOperator tbrop = MainWindowOperator.getDefault().getToolbar(Bundle.getString("org.netbeans.modules.project.ui.Bundle", "Toolbars/Build"));
            assertTrue("Debug Main Project toolbar action is not enabled", MainWindowOperator.getDefault().getToolbarButton(tbrop, Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "LBL_DebugMainProjectAction_Name")).isEnabled());

            eo.close();
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }

    public void testCheckEnabledActionsDebugging() throws Throwable {
        try {
            Node projectNode = ProjectsTabOperator.invoke().getProjectRootNode(Utilities.testProjectName);
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            new Action(null, Utilities.setMainProjectAction).perform(new ProjectsTabOperator().getProjectRootNode(Utilities.testProjectName));
            new EventTool().waitNoEvent(1000);
            EditorOperator eo = new EditorOperator("MemoryView.java");
            //place breakpoint
            Utilities.toggleBreakpoint(eo, 104);
            //start debugging
            new DebugProjectAction().perform(projectNode);
            Utilities.getDebugToolbar().waitComponentVisible(true);
            //wait for breakpoint
            Utilities.waitStatusText("Thread main stopped at MemoryView.java:104");
            //check actions
            //main menu actions
            //check main menu debug main project action
            assertTrue(Utilities.runMenu + "|" + Utilities.debugMainProjectItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.debugMainProjectItem, true));
            //Step into
            assertTrue(Utilities.runMenu + "|" + Utilities.stepIntoItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.stepIntoItem, true));
            //new breakpoint
            assertTrue(Utilities.runMenu + "|" + Utilities.newBreakpointItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.newBreakpointItem, true));
            //new watch
            assertTrue(Utilities.runMenu + "|" + Utilities.newWatchItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.newWatchItem, true));
            //check finish debugger
            assertTrue(Utilities.runMenu + "|" + Utilities.finishSessionsItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.finishSessionsItem, true));
            //pause
            String pausePath=Utilities.runMenu + "|" + Utilities.pauseItem;
            if (MainWindowOperator.getDefault().menuBar().showMenuItem(pausePath).isEnabled()) {
                    new Action(pausePath, null).performMenu();
            }
            MainWindowOperator.getDefault().menuBar().closeSubmenus();
            new EventTool().waitNoEvent(500);
            assertFalse(pausePath, Utilities.verifyMainMenu(pausePath, false));
            //continue
            assertTrue(Utilities.runMenu + "|" + Utilities.continueItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.continueItem, true));
            //step over
            assertTrue(Utilities.runMenu + "|" + Utilities.stepOverItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.stepOverItem, true));
            //step over expression
            assertTrue(Utilities.runMenu + "|" + Utilities.stepOverExpresItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.stepOverExpresItem, true));
            //step out
            assertTrue(Utilities.runMenu + "|" + Utilities.stepOutItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.stepOutItem, true));
            //run to cursor
            assertTrue(Utilities.runMenu + "|" + Utilities.runToCursorItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.runToCursorItem, true));
            //run into method
            //assertTrue(Utilities.runMenu + "|" + Utilities.runIntoMethodItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.runIntoMethodItem, true));
            //apply code changes 
            assertTrue(Utilities.runMenu + "|" + Utilities.applyCodeChangesItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.applyCodeChangesItem, true));
            //toggle breakpoint
            assertTrue(Utilities.runMenu + "|" + Utilities.toggleBreakpointItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.toggleBreakpointItem, true));
            //evaluate expression
            assertTrue(Utilities.runMenu + "|" + Utilities.evaluateExpressionItem, Utilities.verifyMainMenu(Utilities.runMenu + "|" + Utilities.evaluateExpressionItem, true));
            MainWindowOperator.getDefault().pushKey(KeyEvent.VK_ESCAPE);

            //debug toolbar
            ContainerOperator debugToolbarOper = Utilities.getDebugToolbar();
            assertTrue("Toolbar action Finish is not enabled", MainWindowOperator.getDefault().getToolbarButton(debugToolbarOper, Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "CTL_KillAction_name")).isEnabled());
            assertFalse("Toolbar action Pause is not disabled", MainWindowOperator.getDefault().getToolbarButton(debugToolbarOper, Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "CTL_Pause_action_name")).isEnabled());
            assertTrue("Toolbar action Continue is not enabled", MainWindowOperator.getDefault().getToolbarButton(debugToolbarOper, Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "CTL_Continue_action_name")).isEnabled());
            //step
            assertTrue("Toolbar action Step ovet is not enabled", MainWindowOperator.getDefault().getToolbarButton(debugToolbarOper, Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "CTL_Step_over_action_name")).isEnabled());
            assertTrue("Toolbar action Step over expression is not enabled", MainWindowOperator.getDefault().getToolbarButton(debugToolbarOper, Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "CTL_Step_operation_action_name")).isEnabled());
            assertTrue("Toolbar action Step into is not enabled", MainWindowOperator.getDefault().getToolbarButton(debugToolbarOper, Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "CTL_Step_into_action_name")).isEnabled());
            assertTrue("Toolbar action Step out is not enabled", MainWindowOperator.getDefault().getToolbarButton(debugToolbarOper, Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "CTL_Step_out_action_name")).isEnabled());
            //run to cursor
            assertTrue("Toolbar action Run to cursor is not enabled", MainWindowOperator.getDefault().getToolbarButton(debugToolbarOper, Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "CTL_Run_to_cursor_action_name")).isEnabled());
            assertTrue("Toolbar action Apply code changes is enabled", MainWindowOperator.getDefault().getToolbarButton(debugToolbarOper, Bundle.getStringTrimmed("org.netbeans.modules.debugger.ui.actions.Bundle", "CTL_Fix_action_name")).isEnabled());

            //remove breakpoint
            Utilities.deleteAllBreakpoints();
            //finish debugging
            Utilities.endAllSessions();
            //close sources
            eo.close();
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }

}
