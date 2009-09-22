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
import org.netbeans.jellytools.actions.Action;

import java.io.IOException;
import junit.framework.Test;
import junit.textui.TestRunner;
import org.netbeans.jellytools.EditorOperator;

import org.netbeans.jellytools.JellyTestCase;
import org.netbeans.jellytools.ProjectsTabOperator;
import org.netbeans.jellytools.actions.OpenAction;
import org.netbeans.jellytools.nodes.Node;
import org.netbeans.jellytools.nodes.SourcePackagesNode;
import org.netbeans.jemmy.EventTool;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.junit.NbModuleSuite;

/**
 *
 * @author Filip Zamboj
 */
public class DebuggingBreakpointsActionsTest extends JellyTestCase{

     public DebuggingBreakpointsActionsTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return NbModuleSuite.create(
            NbModuleSuite.createConfiguration(DebuggingBreakpointsActionsTest.class).addTest(
            "testToggleBreakpoints",
            "testRemoveBreakpoint"
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

    public void testToggleBreakpoints() throws Throwable {
        try {
            Node projectNode = ProjectsTabOperator.invoke().getProjectRootNode(Utilities.testProjectName);
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            new Action(null, Utilities.setMainProjectAction) {}.perform(new ProjectsTabOperator().getProjectRootNode(Utilities.testProjectName));
            new EventTool().waitNoEvent(1000);
            EditorOperator eo = new EditorOperator("MemoryView.java");
            //place breakpoint
            Utilities.toggleBreakpoint(eo, 80);
            assertTrue("Breakpoint annotation is not displayed", Utilities.checkAnnotation(eo, 80, "Breakpoint"));
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }

    static int lastLineNumber = 0;



    public void testRemoveBreakpoint() throws Throwable {
        try {
            Node projectNode = ProjectsTabOperator.invoke().getProjectRootNode(Utilities.testProjectName);
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            new Action(null, Utilities.setMainProjectAction).perform(new ProjectsTabOperator().getProjectRootNode(Utilities.testProjectName));
            new EventTool().waitNoEvent(1000);
            EditorOperator eo = new EditorOperator("MemoryView.java");
            Utilities.toggleBreakpoint(eo, 80);
            //remove breakpoint
            Utilities.toggleBreakpoint(eo, 80, false);
            assertFalse("Breakpoint annotation is not removed from line 80", Utilities.checkAnnotation(eo, 80, "Breakpoint"));
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }














}
