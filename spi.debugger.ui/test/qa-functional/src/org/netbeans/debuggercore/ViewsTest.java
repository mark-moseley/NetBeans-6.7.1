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
 * Portions created by Sun Microsystems, Inc. are Copyright (C) 2003
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
import org.netbeans.jellytools.*;
import org.netbeans.jellytools.actions.Action;
import org.netbeans.jellytools.actions.OpenAction;
import org.netbeans.jellytools.nodes.Node;
import org.netbeans.jellytools.nodes.SourcePackagesNode;
import org.netbeans.jemmy.EventTool;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JComboBoxOperator;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import org.netbeans.jemmy.operators.JTableOperator;
import org.netbeans.junit.NbModuleSuite;


public class ViewsTest extends JellyTestCase {
    
    public ViewsTest(String name) {
        super(name);
    }
    
    public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        String vers = System.getProperty("java.version");
        if (vers.startsWith("1.6")) {
            return NbModuleSuite.create(
               NbModuleSuite.createConfiguration(ViewsTest.class).addTest(
                    "testViewsDefaultOpen",
                    "testViewsCallStack",
                    "testViewsHeapWalker1",
                    "testViewsThreads",
                    "testViewsSessions",
                    "testViewsSources",
                    "testViewsClose")
                .enableModules(".*")
                .clusters(".*")
            
            );
        } else {
            return NbModuleSuite.create(
               NbModuleSuite.createConfiguration(ViewsTest.class).addTest(
                    "testViewsDefaultOpen",
                    "testViewsCallStack",
                    "testViewsClasses",
                    "testViewsThreads",
                    "testViewsSessions",
                    "testViewsSources",
                    "testViewsClose")
                .enableModules(".*")
                .clusters(".*")
            
            );
        }
                
    }     
    
    public void setUp() throws IOException {
        openDataProjects(Utilities.testProjectName);
        new Action(null, Utilities.setMainProjectAction).perform(new ProjectsTabOperator().getProjectRootNode(Utilities.testProjectName));
        System.out.println("########  " + getName() + "  #######");        
    }
    
    public void tearDown() {
        JemmyProperties.getCurrentOutput().printTrace("\nteardown\n");
        Utilities.endAllSessions();
        Utilities.deleteAllBreakpoints();
    }
    
    public void testViewsDefaultOpen() throws Throwable {
        try {
            //open source
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            EditorOperator eo = new EditorOperator("MemoryView.java");
            try {
                eo.clickMouse(50,50,1);
            } catch (Throwable t) {
                System.err.println(t.getMessage());
            }
            new EventTool().waitNoEvent(500);
            Utilities.toggleBreakpoint(eo, 92);
            new EventTool().waitNoEvent(1500);
            Utilities.startDebugger();
            Utilities.waitStatusText("Thread main stopped at MemoryView.java:92");
            assertNotNull("Local variables view was not opened after debugger start", TopComponentOperator.findTopComponent(Utilities.localVarsViewTitle, 0));
            assertNotNull("Breakpoints view was not opened after debugger start", TopComponentOperator.findTopComponent(Utilities.breakpointsViewTitle, 0));
            assertNotNull("Watches view was not opened after debugger start", TopComponentOperator.findTopComponent(Utilities.watchesViewTitle, 0));
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }
    
    public void testViewsCallStack() throws Throwable {                
        try {
            //open source
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            EditorOperator eo = new EditorOperator("MemoryView.java");
            new EventTool().waitNoEvent(500);
            Utilities.toggleBreakpoint(eo, 92);
            Utilities.startDebugger();
            Utilities.waitStatusText("Thread main stopped at MemoryView.java:92");
            Utilities.showDebuggerView(Utilities.callStackViewTitle);
            JTableOperator jTableOperator = new JTableOperator(new TopComponentOperator(Utilities.callStackViewTitle));
            assertEquals("MemoryView.updateStatus:92", Utilities.removeTags(jTableOperator.getValueAt(0,0).toString()));
            assertEquals("MemoryView.updateConsumption:80", Utilities.removeTags(jTableOperator.getValueAt(1,0).toString()));
            assertEquals("MemoryView.main:117", Utilities.removeTags(jTableOperator.getValueAt(2,0).toString()));
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }
    
    public void testViewsClasses() throws Throwable {
        try {
            //open source
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            EditorOperator eo = new EditorOperator("MemoryView.java");
            new EventTool().waitNoEvent(500);
            Utilities.toggleBreakpoint(eo, 92);
            Utilities.startDebugger();
            Utilities.waitStatusText("Thread main stopped at MemoryView.java:92");
            Utilities.showDebuggerView(Utilities.classesViewTitle);
            JTableOperator jTableOperator = new JTableOperator(new TopComponentOperator(Utilities.classesViewTitle));
            TreeTableOperator treeTableOperator = new TreeTableOperator((javax.swing.JTable) jTableOperator.getSource());
            new org.netbeans.jellytools.nodes.Node(treeTableOperator.tree(), "Application Class Loader|examples.advanced|MemoryView|1").expand();
            String[] entries = {"System Class Loader", "Application Class Loader", "examples.advanced", "Helper", "MemoryView", "1"};
            for (int i = 0; i < entries.length; i++) {
                assertTrue("Node " + entries[i] + " not displayed in Classes view", entries[i].equals(Utilities.removeTags(treeTableOperator.getValueAt(i, 0).toString())));
            }
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }
    
    public void testViewsHeapWalker1() throws Throwable {
        try {
            //open source
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            EditorOperator eo = new EditorOperator("MemoryView.java");
            new EventTool().waitNoEvent(500);
            Utilities.toggleBreakpoint(eo, 92);
            Utilities.startDebugger();
            Utilities.waitStatusText("Thread main stopped at MemoryView.java:92");
            Utilities.showDebuggerView(Utilities.classesViewTitle);
            TopComponentOperator tco = new TopComponentOperator(Utilities.classesViewTitle);
            JTableOperator jTableOperator = new JTableOperator(tco);
            JComboBoxOperator filter = new JComboBoxOperator(tco);
            filter.clearText();
            filter.enterText("example");
            filter.pushKey(KeyEvent.VK_ENTER);
            new EventTool().waitNoEvent(500);
            assertEquals("MemoryView class is not in classes", "examples.advanced.MemoryView", Utilities.removeTags(jTableOperator.getValueAt(0,0).toString()));
            assertEquals("Instances number is wrong", "1 (0%)", Utilities.removeTags(jTableOperator.getValueAt(0,2).toString()));
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }
    
    public void testViewsHeapWalker2() throws Throwable {
       try {
            //open source
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            EditorOperator eo = new EditorOperator("MemoryView.java");
            new EventTool().waitNoEvent(500);
            Utilities.toggleBreakpoint(eo, 92);
            Utilities.startDebugger();
            Utilities.waitStatusText("Thread main stopped at MemoryView.java:92");
            Utilities.showDebuggerView(Utilities.classesViewTitle);
            
            TopComponentOperator tco = new TopComponentOperator(Utilities.classesViewTitle);
            JTableOperator jTableOperator = new JTableOperator(tco);
            JComboBoxOperator filter = new JComboBoxOperator(tco);
            JPopupMenuOperator popup = new JPopupMenuOperator(jTableOperator.callPopupOnCell(0, 0));
            popup.pushMenuNoBlock("Show in Instances View");
            filter.clearText();
            filter.pushKey(KeyEvent.VK_ENTER);
            new EventTool().waitNoEvent(500);
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }
    
    public void testViewsThreads() throws Throwable {
       try {
            //open source
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            EditorOperator eo = new EditorOperator("MemoryView.java");
            new EventTool().waitNoEvent(500);
            Utilities.toggleBreakpoint(eo, 92);
            Utilities.startDebugger();
            Utilities.waitStatusText("Thread main stopped at MemoryView.java:92");
            Utilities.showDebuggerView(Utilities.threadsViewTitle);
            JTableOperator jTableOperator = new JTableOperator(new TopComponentOperator(Utilities.threadsViewTitle));
            assertTrue("Thread group system is not shown in threads view", "system".equals(Utilities.removeTags(jTableOperator.getValueAt(0,0).toString())));
            assertTrue("Thread group main is not shown in threads view", "main".equals(Utilities.removeTags(jTableOperator.getValueAt(1,0).toString())));
            assertTrue("Thread main is not shown in threads view", "main".equals(Utilities.removeTags(jTableOperator.getValueAt(2,0).toString())));
            assertTrue("Thread Reference Handler is not shown in threads view", "Reference Handler".equals(Utilities.removeTags(jTableOperator.getValueAt(3,0).toString())));
            assertTrue("Thread Finalizer is not shown in threads view", "Finalizer".equals(Utilities.removeTags(jTableOperator.getValueAt(4,0).toString())));
            assertTrue("Thread Signal Dispatcher is not shown in threads view", "Signal Dispatcher".equals(Utilities.removeTags(jTableOperator.getValueAt(5,0).toString())));
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }
    
    public void testViewsSessions() throws Throwable {
try {
            //open source
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            EditorOperator eo = new EditorOperator("MemoryView.java");
            new EventTool().waitNoEvent(500);
            Utilities.toggleBreakpoint(eo, 92);
            Utilities.startDebugger();
            Utilities.waitStatusText("Thread main stopped at MemoryView.java:92");
            Utilities.showDebuggerView(Utilities.sessionsViewTitle);
            JTableOperator jTableOperator = new JTableOperator(new TopComponentOperator(Utilities.sessionsViewTitle));
            assertEquals("examples.advanced.MemoryView", Utilities.removeTags(jTableOperator.getValueAt(0,0).toString()));
            try {
                org.openide.nodes.Node.Property property = (org.openide.nodes.Node.Property)jTableOperator.getValueAt(0,1);
                assertEquals("Stopped", Utilities.removeTags(property.getValue().toString()));
                property = (org.openide.nodes.Node.Property)jTableOperator.getValueAt(0,2);
                assertEquals("org.netbeans.api.debugger.Session localhost:examples.advanced.MemoryView", Utilities.removeTags(property.getValue().toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
                assertTrue(ex.getClass()+": "+ex.getMessage(), false);
            }
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }
    
    public void testViewsSources() throws Throwable {
        try {
            //open source
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            EditorOperator eo = new EditorOperator("MemoryView.java");
            new EventTool().waitNoEvent(500);
            Utilities.toggleBreakpoint(eo, 92);
            Utilities.startDebugger();
            Utilities.waitStatusText("Thread main stopped at MemoryView.java:92");
            Utilities.showDebuggerView(Utilities.sourcesViewTitle);
            JTableOperator jTableOperator = new JTableOperator(new TopComponentOperator(Utilities.sourcesViewTitle));
            String debugAppSource = "debugTestProject" + java.io.File.separator + "src (Project debugTestProject)";
            boolean jdk = false, project = false;
            for (int i=0;i < jTableOperator.getRowCount();i++) {
                String src = Utilities.removeTags(jTableOperator.getValueAt(i,0).toString());
                if (src.endsWith("src.zip")) {
                    jdk=true;
                } else if (src.endsWith(debugAppSource)) {
                    project = true;
                }
            }
            assertTrue("JDK source root is not shown in threads view", jdk);
            assertTrue("MemoryView source root is not shown in threads view", project);
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }
    
    public void testViewsClose() throws Throwable {
        try {
            //open source
            Node beanNode = new Node(new SourcePackagesNode(Utilities.testProjectName), "examples.advanced|MemoryView.java"); //NOI18N
            new OpenAction().performAPI(beanNode); // NOI18N
            EditorOperator eo = new EditorOperator("MemoryView.java");
            new EventTool().waitNoEvent(500);
            Utilities.toggleBreakpoint(eo, 92);
            Utilities.startDebugger();
            Utilities.waitStatusText("Thread main stopped at MemoryView.java:92");
            new TopComponentOperator(Utilities.localVarsViewTitle).close();
            new TopComponentOperator(Utilities.watchesViewTitle).close();
            new TopComponentOperator(Utilities.callStackViewTitle).close();
            new TopComponentOperator(Utilities.classesViewTitle).close();
            new TopComponentOperator(Utilities.sessionsViewTitle).close();
            new TopComponentOperator(Utilities.threadsViewTitle).close();
            new TopComponentOperator(Utilities.sourcesViewTitle).close();
        } catch (Throwable th) {
            Utilities.captureScreen(this);
            throw th;
        }
    }
}
