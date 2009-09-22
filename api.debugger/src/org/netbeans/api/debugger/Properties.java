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

package org.netbeans.api.debugger;

import java.beans.Customizer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.WeakHashMap;
import org.openide.ErrorManager;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;


/**
 * Utility class hepls store properties.
 *
 * @author Jan Jancura
 */
public abstract class Properties {


    private static Properties defaultProperties;

    /**
     * Returns shared instance of Properties class.
     *
     * @return shared instance of Properties class
     */
    public static synchronized Properties getDefault () {
        if (defaultProperties == null)
            defaultProperties = new PropertiesImpl ();
        return defaultProperties;
    }

    /**
     * Reads String property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */
    public abstract String getString (String propertyName, String defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setString (String propertyName, String value);

    /**
     * Reads int property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */
    public abstract int getInt (String propertyName, int defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setInt (String propertyName, int value);

    /**
     * Reads char property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */
    public abstract char getChar (String propertyName, char defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setChar (String propertyName, char value);

    /**
     * Reads float property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */
    public abstract float getFloat (String propertyName, float defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setFloat (String propertyName, float value);

    /**
     * Reads long property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */
    public abstract long getLong (String propertyName, long defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setLong (String propertyName, long value);

    /**
     * Reads double property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */
    public abstract double getDouble (String propertyName, double defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setDouble (String propertyName, double value);

    /**
     * Reads boolean property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */
    public abstract boolean getBoolean (String propertyName, boolean defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setBoolean (String propertyName, boolean value);

    /**
     * Reads byte property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */
    public abstract byte getByte (String propertyName, byte defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setByte (String propertyName, byte value);

    /**
     * Reads short property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */
    public abstract short getShort (String propertyName, short defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setShort (String propertyName, short value);
    /**
     * Reads Object property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */

    public abstract Object getObject (String propertyName, Object defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setObject (String propertyName, Object value);

    /**
     * Reads array property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */
    public abstract Object[] getArray (String propertyName, Object[] defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setArray (String propertyName, Object[] value);

    /**
     * Reads Collection property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */
    public abstract Collection getCollection (String propertyName, Collection defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setCollection (String propertyName, Collection value);

    /**
     * Reads Map property from storage.
     *
     * @param propertyName a propertyName of property
     * @param defaultValue a default value to be used if property is not set
     * @return a current value of property
     */
    public abstract Map getMap (String propertyName, Map defaultValue);

    /**
     * Sets a new value of property with given propertyName.
     *
     * @param propertyName a propertyName of property
     * @param value the new value of property
     */
    public abstract void setMap (String propertyName, Map value);

    /**
     * Returns Properties instance for some "subfolder" in properties file.
     *
     * @param propertyName a subfolder name
     * @return a Properties instance for some "subfolder" in properties file
     */
    public abstract Properties getProperties (String propertyName);

    /**
     * Add a property change listener to this properties instance.
     * The listener fires a property change event when a new value of some property
     * is set.
     * <p>
     * Please note, that this properties object is not collected from memory
     * sooner than all it's listeners. Therefore it's not necessray to
     * keep a strong reference to this object while holding the listener.
     *
     * @param l The property change listener
     * @throws UnsupportedOperationException if not supported. The default
     * properties implementation retrieved by {@link #getDefault()} supports
     * adding/removing listeners.
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        throw new UnsupportedOperationException("Unsupported listening on "+getClass()+" properties.");
    }

    /**
     * Remove a property change listener from this properties instance.
     * <p>
     * Please note, that this properties object is not collected from memory
     * sooner than all it's listeners. Therefore it's not necessray to
     * keep a strong reference to this object while holding the listener.
     * OTOH it is necessary to remove all listeners or release all strong
     * references to the listeners to allow collection of this properties
     * object.
     *
     * @param l The property change listener
     * @throws UnsupportedOperationException if not supported. The default
     * properties implementation retrieved by {@link #getDefault()} supports
     * adding/removing listeners.
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        throw new UnsupportedOperationException("Unsupported listening on "+getClass()+" properties.");
    }


    // innerclasses ............................................................

    /**
     * This class helps to store and read custom types using
     * {@link Properties#setObject} and {@link Properties#getObject} methods.
     * Implementations of this class should be stored in "META_INF\debugger"
     * folder.
     */
    public interface Reader {

        /**
         * Returns array of classNames supported by this reader.
         *
         * @return array of classNames supported by this reader
         */
        public String[] getSupportedClassNames ();

        /**
         * Reads object with given className.
         *
         * @param className a name of class to be readed
         * @param properties a properties subfloder containing properties
         *        for this object
         * @return a new instance of className class
         */
        public Object read (String className, Properties properties);

        /**
         * Writes given object to given properties subfolder.
         *
         * @param object a object to be saved
         * @param properties a properties subfolder to be used
         */
        public void write (Object object, Properties properties);
    }

    /**
     * Implementing this interface one can define initial values of properties.
     */
    public interface Initializer {

        /**
         * The list of supported property names.
         * @return the property names supported by this initializer
         */
        String[] getSupportedPropertyNames();

        /**
         * Get the default value of property.
         * @param propertyName The name of the property
         * @return The default value
         */
        Object getDefaultPropertyValue(String propertyName);

    }


    private final static class PrimitiveRegister {

        private HashMap properties = new HashMap ();
        private boolean isInitialized = false;


        public String getProperty (String propertyName, String defaultValue) {
            synchronized (this) {
                if (!isInitialized) {
                    load ();
                    isInitialized = true;
                }
                String value = (String) properties.get (propertyName);
                if (value != null) {
                    return value;
                }
            }
            return defaultValue;
        }

        public void setProperty (String propertyName, String value) {
            synchronized (this) {
                if (!isInitialized) {
                    load ();
                    isInitialized = true;
                }
                properties.put (propertyName, value);
            }
            save ();
        }

        private synchronized void load () {
            BufferedReader br = null;
            try {
                FileObject fo = findSettings();
                InputStream is = fo.getInputStream ();
                br = new BufferedReader (new InputStreamReader (is));

                String l = br.readLine ();
                while (l != null) {
                    int i = l.indexOf (':');
                    if (i > 0) {
                        String value = l.substring (i + 1);
                        value = translateSingleLineStringToMultiLine(value);
                        properties.put (l.substring (0, i), value);
                    }
                    l = br.readLine ();
                }
                br.close ();
            } catch (IOException ex) {
                ErrorManager.getDefault().notify(ex);
            }
        }

        // currently waiting / running refresh task
        // there is at most one
        private RequestProcessor.Task task;

        private synchronized void save () {
            if (task == null) {
                task = new RequestProcessor("Debugger Properties Save RP", 1).create(
                        new Runnable() {
                            public void run () {
                                saveIn ();
                            }
                        }
                );
            }
            task.schedule(1000);
        }

        private synchronized void saveIn () {
            PrintWriter pw = null;
            FileLock lock = null;
            try {
                FileObject fo = findSettings();
                lock = fo.lock ();
                OutputStream os = fo.getOutputStream (lock);
                pw = new PrintWriter (os);

                Set s = properties.keySet ();
                ArrayList l = new ArrayList (s);
                Collections.sort (l);
                int i, k = l.size ();
                for (i = 0; i < k; i++) {
                    String key = (String) l.get (i);
                    Object value = properties.get (key);
                    if (value != null) {
                        // Do not write null values
                        value = translateMultiLineStringToSingleLine(value.toString());
                        pw.println ("" + key + ":" + value);
                    }
                }
                pw.flush ();
            } catch (IOException ex) {
                ErrorManager.getDefault().notify(
                        ErrorManager.getDefault().annotate(ex,
                        "Can not save debugger settings."));
            } finally {
                try {
                    if (pw != null) {
                        pw.close ();
                    }
                } finally {
                    if (lock != null) {
                        lock.releaseLock ();
                    }
                }
            }
        }

        private static String translateMultiLineStringToSingleLine(String line) {
            /* Do replace of newline and \\n:
            line = line.replace("\\n", "\\\\n");
            return line.replace("\n", "\\n");
            Do it in one pass and minimize garbage: */
            StringBuilder sb = null;
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == '\n' || c == 'n' && i > 0 && line.charAt(i-1) == '\\') {
                    if (sb == null) {
                        sb = new StringBuilder(line.substring(0, i));
                    }
                    sb.append("\\n"); // Replaces "\n" with "\\n" or "\\n" with "\\\\n"
                } else if (sb != null) {
                    sb.append(c);
                }
            }
            if (sb == null) {
                return line;
            } else {
                return sb.toString();
            }
        }

        private static String translateSingleLineStringToMultiLine(String line) {
            StringBuilder sb = null;
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == 'n' && i > 0 && line.charAt(i-1) == '\\') {
                    if (sb == null) {
                        sb = new StringBuilder(line.substring(0, i - 1));
                    } else {
                        int l = sb.length();
                        sb.delete(l - 1, l); // Delete the last slash
                    }
                    if (i > 1 && line.charAt(i-2) == '\\') {
                        sb.append("n"); // Replaces "\\\\n" to "\\n"
                    } else {
                        sb.append("\n"); // Replaces "\\n" to "\n"
                    }
                } else if (sb != null) {
                    sb.append(c);
                }
            }
            if (sb == null) {
                return line;
            } else {
                return sb.toString();
            }
        }

        private static FileObject findSettings() throws IOException {
            FileObject r = FileUtil.getConfigFile("Services"); // NOI18N
            if (r == null) {
                r = FileUtil.getConfigRoot().createFolder("Services"); // NOI18N
            }
            FileObject fo = r.getFileObject
                ("org-netbeans-modules-debugger-Settings", "properties"); // NOI18N
            if (fo == null) {
                fo = r.createData
                    ("org-netbeans-modules-debugger-Settings", "properties"); // NOI18N
            }
            return fo;
        }
    }

    // package-private because of tests
    static class PropertiesImpl extends Properties {

        private static final Object BAD_OBJECT = new Object ();
        private static final String BAD_STRING = "";
        private static final Map BAD_MAP = new HashMap ();
        private static final Collection BAD_COLLECTION = new ArrayList ();
        private static final Object[] BAD_ARRAY = new Object [0];

        private final Map<String, Reference<Properties>> childProperties = new HashMap<String, Reference<Properties>>();
        private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private final Map<PropertyChangeListener, Properties> propertiesHeldByListener = new WeakHashMap<PropertyChangeListener, Properties>();

        ServicesHolder<Reader>      readers      = new ReaderHolder();
        ServicesHolder<Initializer> initializers = new InitializerHolder();

        private static abstract class ServicesHolder<T> {

            private Class<T> clazz;
            // Holds the list to prevent from garbage-collect. Do not remove!
            private List<? extends T> servicesList;
            protected HashMap<String, T> register;

            public ServicesHolder(Class<T> clazz) {
                this.clazz = clazz;
            }

            private final void init() {
                register = new HashMap<String, T>();
                final List<? extends T> list = DebuggerManager.getDebuggerManager().lookup(null, clazz);
                servicesList = list;
                synchronized (list) {
                    for (T s : list) {
                        registerService(s);
                    }
                }
                ((Customizer) list).addPropertyChangeListener(
                        new PropertyChangeListener() {
                            public void propertyChange(PropertyChangeEvent evt) {
                                synchronized (ServicesHolder.this) {
                                    Set<T> registeredServices = new HashSet<T>(register.values());
                                    synchronized (list) {
                                        for (T s : list) {
                                            if (!registeredServices.remove(s)) {
                                                registerService(s);
                                            }
                                        }
                                    }
                                    for (T s : registeredServices) {
                                        unregisterService(s);
                                    }
                                }
                            }
                        });
                ((Customizer) list).setObject(Lookup.NOTIFY_LOAD_FIRST);
                ((Customizer) list).setObject(Lookup.NOTIFY_UNLOAD_LAST);
            }

            protected abstract void registerService(T s);

            protected abstract void unregisterService(T s);

            public synchronized T find(String name) {
                if (register == null) {
                    init();
                }
                return register.get(name);
            }

        }

        private static final class ReaderHolder extends ServicesHolder<Reader> {

            public ReaderHolder() {
                super(Reader.class);
            }

            @Override
            protected void registerService(Reader r) {
                //System.err.println("registerReader("+r+")");
                String[] ns = r.getSupportedClassNames ();
                int j, jj = ns.length;
                for (j = 0; j < jj; j++) {
                    register.put (ns [j], r);
                }
            }

            @Override
            protected void unregisterService(Reader r) {
                //System.err.println("unregisterReader("+r+")");
                String[] ns = r.getSupportedClassNames ();
                int j, jj = ns.length;
                for (j = 0; j < jj; j++) {
                    register.remove (ns [j]);
                }
            }

            @Override
            public synchronized Reader find(String typeID) {

                Reader r = super.find(typeID);
                if (r != null) return r;

                Class c = null;
                try {
                    c = getClassLoader ().loadClass (typeID);
                } catch (ClassNotFoundException e) {
                    ErrorManager.getDefault().notify(e);
                    return null;
                }
                while ((c != null) && (register.get (c.getName ()) == null)) {
                    c = c.getSuperclass ();
                }
                if (c != null)
                    r = (Reader) register.get (c.getName ());
                return r;
            }

        }

        private static final class InitializerHolder extends ServicesHolder<Initializer> {

            public InitializerHolder() {
                super(Initializer.class);
            }

            @Override
            protected void registerService(Initializer i) {
                //System.err.println("registerInitializer("+i+")");
                String[] ns = i.getSupportedPropertyNames();
                int j, jj = ns.length;
                for (j = 0; j < jj; j++) {
                    register.put (ns [j], i);
                }
            }

            @Override
            protected void unregisterService(Initializer i) {
                //System.err.println("unregisterInitializer("+i+")");
                String[] ns = i.getSupportedPropertyNames ();
                int j, jj = ns.length;
                for (j = 0; j < jj; j++) {
                    register.remove (ns [j]);
                }
            }
        }


        private final PrimitiveRegister impl = new PrimitiveRegister ();


        private <T> T getInitialValue(String propertyName, Class<T> clazz) {
            Initializer initializer = initializers.find(propertyName);
            if (initializer != null) {
                Object value = initializer.getDefaultPropertyValue(propertyName);
                if (value != null && !clazz.isAssignableFrom(value.getClass())) {
                    Exceptions.printStackTrace(new IllegalStateException(
                            "Value ("+value+") of a bad type ("+value.getClass()+") returned by "+initializer+
                            " for property '"+propertyName+"'. It can not be cast to "+clazz));
                    value = null;
                }
                return (T) value;
            } else {
                return null;
            }
        }

        // primitive properties ....................................................................................

        public String getString (String propertyName, String defaultValue) {
            String value = impl.getProperty (propertyName, null);
            if (value == null) {
                String initialValue = getInitialValue(propertyName, String.class);
                if (initialValue == null) {
                    initialValue = defaultValue;
                }
                return initialValue;
            }
            if (!value.startsWith ("\"")) {
                ErrorManager.getDefault().log("Can not read string " + value + ".");
                return defaultValue;
            }
            return value.substring (1, value.length () - 1);
        }

        public void setString (String propertyName, String value) {
            if (value != null) {
                impl.setProperty (propertyName, "\"" + value + "\"");
            } else {
                impl.setProperty (propertyName, value);
            }
            pcs.firePropertyChange(propertyName, null, value);
        }

        public int getInt (String propertyName, int defaultValue) {
            String value = impl.getProperty (propertyName, null);
            if (value == null) {
                Integer initialValue = getInitialValue(propertyName, Integer.class);
                if (initialValue == null) {
                    initialValue = defaultValue;
                }
                return initialValue;
            }
            try {
                int val = Integer.parseInt (value);
                return val;
            } catch (NumberFormatException nfex) {
                return defaultValue;
            }
        }

        public void setInt (String propertyName, int value) {
            impl.setProperty (propertyName, Integer.toString (value));
            pcs.firePropertyChange(propertyName, null, value);
        }

        public char getChar (String propertyName, char defaultValue) {
            String value = impl.getProperty (propertyName, null);
            if (value == null) {
                Character initialValue = getInitialValue(propertyName, Character.class);
                if (initialValue == null) {
                    initialValue = defaultValue;
                }
                return initialValue;
            }
            char val = value.charAt (0);
            return val;
        }

        public void setChar (String propertyName, char value) {
            impl.setProperty (propertyName, Character.toString(value));
            pcs.firePropertyChange(propertyName, null, value);
        }

        public float getFloat (String propertyName, float defaultValue) {
            String value = impl.getProperty (propertyName, null);
            if (value == null) {
                Float initialValue = getInitialValue(propertyName, Float.class);
                if (initialValue == null) {
                    initialValue = defaultValue;
                }
                return initialValue;
            }
            try {
                float val = Float.parseFloat (value);
                return val;
            } catch (NumberFormatException nfex) {
                return defaultValue;
            }
        }

        public void setFloat (String propertyName, float value) {
            impl.setProperty (propertyName, Float.toString (value));
            pcs.firePropertyChange(propertyName, null, value);
        }

        public long getLong (String propertyName, long defaultValue) {
            String value = impl.getProperty (propertyName, null);
            if (value == null) {
                Long initialValue = getInitialValue(propertyName, Long.class);
                if (initialValue == null) {
                    initialValue = defaultValue;
                }
                return initialValue;
            }
            try {
                long val = Long.parseLong (value);
                return val;
            } catch (NumberFormatException nfex) {
                return defaultValue;
            }
        }

        public void setLong (String propertyName, long value) {
            impl.setProperty (propertyName, Long.toString (value));
            pcs.firePropertyChange(propertyName, null, value);
        }

        public double getDouble (String propertyName, double defaultValue) {
            String value = impl.getProperty (propertyName, null);
            if (value == null) {
                Double initialValue = getInitialValue(propertyName, Double.class);
                if (initialValue == null) {
                    initialValue = defaultValue;
                }
                return initialValue;
            }
            try {
                double val = Double.parseDouble (value);
                return val;
            } catch (NumberFormatException nfex) {
                return defaultValue;
            }
        }

        public void setDouble (String propertyName, double value) {
            impl.setProperty (propertyName, Double.toString (value));
            pcs.firePropertyChange(propertyName, null, value);
        }

        public boolean getBoolean (String propertyName, boolean defaultValue) {
            String value = impl.getProperty (propertyName, null);
            if (value == null) {
                Boolean initialValue = getInitialValue(propertyName, Boolean.class);
                if (initialValue == null) {
                    initialValue = defaultValue;
                }
                return initialValue;
            }
            boolean val = value.equals ("true");
            return val;
        }

        public void setBoolean (String propertyName, boolean value) {
            impl.setProperty (propertyName, value ? "true" : "false");
            pcs.firePropertyChange(propertyName, null, value);
        }

        public byte getByte (String propertyName, byte defaultValue) {
            String value = impl.getProperty (propertyName, null);
            if (value == null) {
                Byte initialValue = getInitialValue(propertyName, Byte.class);
                if (initialValue == null) {
                    initialValue = defaultValue;
                }
                return initialValue;
            }
            try {
                byte val = Byte.parseByte (value);
                return val;
            } catch (NumberFormatException nfex) {
                return defaultValue;
            }
        }

        public void setByte (String propertyName, byte value) {
            impl.setProperty (propertyName, Byte.toString (value));
            pcs.firePropertyChange(propertyName, null, value);
        }

        public short getShort (String propertyName, short defaultValue) {
            String value = impl.getProperty (propertyName, null);
            if (value == null) {
                Short initialValue = getInitialValue(propertyName, Short.class);
                if (initialValue == null) {
                    initialValue = defaultValue;
                }
                return initialValue;
            }
            try {
                short val = Short.parseShort (value);
                return val;
            } catch (NumberFormatException nfex) {
                return defaultValue;
            }
        }

        public void setShort (String propertyName, short value) {
            impl.setProperty (propertyName, Short.toString (value));
            pcs.firePropertyChange(propertyName, null, value);
        }

        public Object getObject (String propertyName, Object defaultValue) {
            synchronized(impl) {
                String typeID = impl.getProperty (propertyName, null);
                if (typeID == null) {
                    Object initialValue = getInitialValue(propertyName, Object.class);
                    if (initialValue == null) {
                        initialValue = defaultValue;
                    }
                    return initialValue;
                }
                if (typeID.equals ("# null"))
                    return null;
                if (!typeID.startsWith ("# ")) {
                    if (typeID.startsWith ("\"")) {
                        String s = getString (propertyName, BAD_STRING);
                        if (s == BAD_STRING) return defaultValue;
                        return s;
                    }
                    ErrorManager.getDefault().log("Can not read object " + typeID + ". No reader registered for type " + typeID + ".");
                    return defaultValue;
                }
                typeID = typeID.substring (2);
                Class c = null;
                try {
                    c = Class.forName (typeID);
                } catch (ClassNotFoundException e) {
                }
                if (c != null) {
                    if (Map.class.isAssignableFrom (c)) {
                        Map m = getMap (propertyName, BAD_MAP);
                        if (m == BAD_MAP) return defaultValue;
                        return m;
                    }
                    if (Object [].class.isAssignableFrom (c)) {
                        Object[] os = getArray (propertyName, BAD_ARRAY);
                        if (os == BAD_ARRAY) return defaultValue;
                        return os;
                    }
                    if (Collection.class.isAssignableFrom (c)) {
                        Collection co = getCollection (propertyName, BAD_COLLECTION);
                        if (co == BAD_COLLECTION) return defaultValue;
                        return co;
                    }
                }
                Reader r = readers.find(typeID);
                if (r == null) {
                    ErrorManager.getDefault().log("Can not read object. No reader registered for type " + typeID + ".");
                    return defaultValue;
                }
                return r.read (typeID, getProperties (propertyName));
            }
        }

        public void setObject (String propertyName, Object value) {
            synchronized(impl) {
                if (value == null) {
                    impl.setProperty (propertyName, "# null");
                } else if (value instanceof String) {
                    setString (propertyName, (String) value);
                } else if (value instanceof Map) {
                    setMap (propertyName, (Map) value);
                } else if (value instanceof Collection) {
                    setCollection (propertyName, (Collection) value);
                } else if (value instanceof Object[]) {
                    setArray (propertyName, (Object[]) value);
                } else {

                    // find register
                    Reader r = readers.find(value.getClass ().getName ());
                    if (r == null) {
                        ErrorManager.getDefault().log ("Can not write object " + value);
                        return;
                    }

                    // write
                    r.write (value, getProperties (propertyName));
                    impl.setProperty (propertyName, "# " + value.getClass ().getName ());

                }
            }
            pcs.firePropertyChange(propertyName, null, value);
        }

        public Object[] getArray (String propertyName, Object[] defaultValue) {
            synchronized(impl) {
                String arrayType = impl.getProperty (propertyName + ".array_type", null);
                if (arrayType == null) {
                    Object[] initialValue = getInitialValue(propertyName, Object[].class);
                    if (initialValue == null) {
                        initialValue = defaultValue;
                    }
                    return initialValue;
                }
                Properties p = getProperties (propertyName);
                int l = p.getInt ("length", -1);
                if (l < 0) return defaultValue;
                Object[] os = null;
                try {
                    os = (Object[]) Array.newInstance (
                        getClassLoader ().loadClass (arrayType),
                        l
                    );
                } catch (ClassNotFoundException ex) {
                    ErrorManager.getDefault().notify(ex);
                    os = new Object [l];
                }
                for (int i = 0; i < l; i++) {
                    Object o = p.getObject ("" + i, BAD_OBJECT);
                    if (o == BAD_OBJECT) return defaultValue;
                    os [i] = o;
                }
                return os;
            }
        }

        public void setArray (String propertyName, Object[] value) {
            synchronized (impl) {
                impl.setProperty (propertyName, "# array");
                impl.setProperty (propertyName + ".array_type", value.getClass ().getComponentType ().getName ());
                Properties p = getProperties (propertyName);
                int i, k = value.length;
                p.setInt ("length", k);
                for (i = 0; i < k; i++)
                    p.setObject ("" + i, value [i]);
            }
            pcs.firePropertyChange(propertyName, null, value);
        }

        public Collection getCollection (String propertyName, Collection defaultValue) {
            synchronized(impl) {
                String typeID = impl.getProperty (propertyName, null);
                if (typeID == null) {
                    Collection initialValue = getInitialValue(propertyName, Collection.class);
                    if (initialValue == null) {
                        initialValue = defaultValue;
                    }
                    return initialValue;
                }
                if (!typeID.startsWith ("# ")) return defaultValue;
                Collection c = null;
                try {
                    c = (Collection) Class.forName (typeID.substring (2)).newInstance ();
                } catch (ClassNotFoundException ex) {
                    System.err.println(ex.getLocalizedMessage());
                    ErrorManager.getDefault().log(ex.getLocalizedMessage());
                    ErrorManager.getDefault().notify(ex);
                    return defaultValue;
                } catch (InstantiationException ex) {
                    System.err.println(ex.getLocalizedMessage());
                    ErrorManager.getDefault().log(ex.getLocalizedMessage());
                    ErrorManager.getDefault().notify(ex);
                    return defaultValue;
                } catch (IllegalAccessException ex) {
                    System.err.println(ex.getLocalizedMessage());
                    ErrorManager.getDefault().log(ex.getLocalizedMessage());
                    ErrorManager.getDefault().notify(ex);
                    return defaultValue;
                }
                Properties p = getProperties (propertyName);
                int i, k = p.getInt ("length", 0);
                for (i = 0; i < k; i++) {
                    Object o = p.getObject ("" + i, BAD_OBJECT);
                    if (o == BAD_OBJECT) return defaultValue;
                    c.add (o);
                }
                return c;
            }
        }

        public void setCollection (String propertyName, Collection value) {
            synchronized(impl) {
                if (value == null) {
                    impl.setProperty (propertyName, null);
                } else {
                    impl.setProperty (propertyName, "# " + value.getClass ().getName ());
                    Properties p = getProperties (propertyName);
                    Iterator it = value.iterator ();
                    int i = 0;
                    p.setInt ("length", value.size ());
                    while (it.hasNext ()) {
                        p.setObject ("" + i, it.next ());
                        i++;
                    }
                }
            }
            pcs.firePropertyChange(propertyName, null, value);
        }

        public Map getMap (String propertyName, Map defaultValue) {
            synchronized(impl) {
                String typeID = impl.getProperty (propertyName, null);
                if (typeID == null) {
                    Map initialValue = getInitialValue(propertyName, Map.class);
                    if (initialValue == null) {
                        initialValue = defaultValue;
                    }
                    return initialValue;
                }
                if (!typeID.startsWith ("# ")) return defaultValue;
                Map m = null;
                try {
                    m = (Map) Class.forName (typeID.substring (2)).newInstance ();
                } catch (ClassNotFoundException ex) {
                    ErrorManager.getDefault().log(ex.getLocalizedMessage());
                    return defaultValue;
                } catch (InstantiationException ex) {
                    ErrorManager.getDefault().log(ex.getLocalizedMessage());
                    return defaultValue;
                } catch (IllegalAccessException ex) {
                    ErrorManager.getDefault().log(ex.getLocalizedMessage());
                    return defaultValue;
                }
                Properties p = getProperties (propertyName);
                int i, k = p.getInt ("length", 0);
                for (i = 0; i < k; i++) {
                    Object key = p.getObject ("" + i + "-key", BAD_OBJECT);
                    if (key == BAD_OBJECT) return defaultValue;
                    Object value = p.getObject ("" + i + "-value", BAD_OBJECT);
                    if (value == BAD_OBJECT) return defaultValue;
                    m.put (key, value);
                }
                return m;
            }
        }

        public void setMap (String propertyName, Map value) {
            synchronized(impl) {
                if (value == null) {
                    impl.setProperty (propertyName, null);
                } else {
                    impl.setProperty (propertyName, "# " + value.getClass ().getName ());
                    Properties p = getProperties (propertyName);
                    Iterator it = value.keySet ().iterator ();
                    int i = 0;
                    p.setInt ("length", value.size ());
                    while (it.hasNext ()) {
                        Object o = it.next ();
                        p.setObject ("" + i + "-key", o);
                        p.setObject ("" + i + "-value", value.get (o));
                        i++;
                    }
                }
            }
            pcs.firePropertyChange(propertyName, null, value);
        }

        public Properties getProperties (String propertyName) {
            synchronized (childProperties) {
                Reference<Properties> propRef = childProperties.get(propertyName);
                if (propRef != null) {
                    Properties p = propRef.get();
                    if (p != null) {
                        return p;
                    }
                }
                Properties p = new DelegatingProperties (this, propertyName);
                propRef = new WeakReference<Properties>(p);
                childProperties.put(propertyName, propRef);
                return p;
            }
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener l) {
            pcs.addPropertyChangeListener(l);
            synchronized (propertiesHeldByListener) {
                propertiesHeldByListener.put(l, this);
            }
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener l) {
            pcs.removePropertyChangeListener(l);
            synchronized (propertiesHeldByListener) {
                propertiesHeldByListener.remove(l);
            }
        }


        private static ClassLoader classLoader;
        private static ClassLoader getClassLoader () {
            if (classLoader == null)
                //Thread.currentThread ().getContextClassLoader ();
                classLoader = (ClassLoader) org.openide.util.Lookup.
                    getDefault ().lookup (ClassLoader.class);
            return classLoader;
        }
    }

    private static class DelegatingProperties extends Properties {

        private Properties delegatingProperties;
        private String root;
        private final Map<String, Reference<Properties>> childProperties =
                new HashMap<String, Reference<Properties>>();
        private final Map<PropertyChangeListener, PropertyChangeListener> delegatingListeners =
                new WeakHashMap<PropertyChangeListener, PropertyChangeListener>();


        DelegatingProperties (Properties properties, String root) {
            delegatingProperties = properties;
            this.root = root;
        }

        public String getString (String propertyName, String defaultValue) {
            return delegatingProperties.getString (root + '.' + propertyName, defaultValue);
        }

        public void setString (String propertyName, String value) {
            delegatingProperties.setString (root + '.' + propertyName, value);
        }

        public int getInt (String propertyName, int defaultValue) {
            return delegatingProperties.getInt (root + '.' + propertyName, defaultValue);
        }

        public void setInt (String propertyName, int value) {
            delegatingProperties.setInt (root + '.' + propertyName, value);
        }

        public byte getByte (String propertyName, byte defaultValue) {
            return delegatingProperties.getByte (root + '.' + propertyName, defaultValue);
        }

        public void setByte (String propertyName, byte value) {
            delegatingProperties.setByte (root + '.' + propertyName, value);
        }

        public char getChar (String propertyName, char defaultValue) {
            return delegatingProperties.getChar (root + '.' + propertyName, defaultValue);
        }

        public void setChar (String propertyName, char value) {
            delegatingProperties.setChar (root + '.' + propertyName, value);
        }

        public boolean getBoolean (String propertyName, boolean defaultValue) {
            return delegatingProperties.getBoolean (root + '.' + propertyName, defaultValue);
        }

        public void setBoolean (String propertyName, boolean value) {
            delegatingProperties.setBoolean (root + '.' + propertyName, value);
        }

        public short getShort (String propertyName, short defaultValue) {
            return delegatingProperties.getShort (root + '.' + propertyName, defaultValue);
        }

        public void setShort (String propertyName, short value) {
            delegatingProperties.setShort (root + '.' + propertyName, value);
        }

        public long getLong (String propertyName, long defaultValue) {
            return delegatingProperties.getLong (root + '.' + propertyName, defaultValue);
        }

        public void setLong (String propertyName, long value) {
            delegatingProperties.setLong (root + '.' + propertyName, value);
        }

        public double getDouble (String propertyName, double defaultValue) {
            return delegatingProperties.getDouble (root + '.' + propertyName, defaultValue);
        }

        public void setDouble (String propertyName, double value) {
            delegatingProperties.setDouble (root + '.' + propertyName, value);
        }

        public float getFloat (String propertyName, float defaultValue) {
            return delegatingProperties.getFloat (root + '.' + propertyName, defaultValue);
        }

        public void setFloat (String propertyName, float value) {
            delegatingProperties.setFloat (root + '.' + propertyName, value);
        }

        public Object getObject (String propertyName, Object defaultValue) {
            return delegatingProperties.getObject (root + '.' + propertyName, defaultValue);
        }

        public void setObject (String propertyName, Object value) {
            delegatingProperties.setObject (root + '.' + propertyName, value);
        }

        public Object[] getArray (String propertyName, Object[] defaultValue) {
            return delegatingProperties.getArray (root + '.' + propertyName, defaultValue);
        }

        public void setArray (String propertyName, Object[] value) {
            delegatingProperties.setArray (root + '.' + propertyName, value);
        }

        public Collection getCollection (String propertyName, Collection defaultValue) {
            return delegatingProperties.getCollection (root + '.' + propertyName, defaultValue);
        }

        public void setCollection (String propertyName, Collection value) {
            delegatingProperties.setCollection (root + '.' + propertyName, value);
        }

        public Map getMap (String propertyName, Map defaultValue) {
            return delegatingProperties.getMap (root + '.' + propertyName, defaultValue);
        }

        public void setMap (String propertyName, Map value) {
            delegatingProperties.setMap (root + '.' + propertyName, value);
        }

        public Properties getProperties (String propertyName) {
            synchronized (childProperties) {
                Reference<Properties> propRef = childProperties.get(propertyName);
                if (propRef != null) {
                    Properties p = propRef.get();
                    if (p != null) {
                        return p;
                    }
                }
                Properties p = new DelegatingProperties (delegatingProperties, root + '.' + propertyName);
                propRef = new WeakReference<Properties>(p);
                childProperties.put(propertyName, propRef);
                return p;
            }
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener l) {
            PropertyChangeListener delegate = new DelegatingPropertyChangeListener(l);
            synchronized (delegatingListeners) {
                delegatingListeners.put(l, delegate);
            }
            delegatingProperties.addPropertyChangeListener(delegate);
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener l) {
            PropertyChangeListener delegate;
            synchronized (delegatingListeners) {
                delegate = delegatingListeners.get(l);
            }
            if (delegate != null) {
                delegatingProperties.removePropertyChangeListener(delegate);
            }
        }

        private class DelegatingPropertyChangeListener implements PropertyChangeListener {

            private PropertyChangeListener delegate;
            private String r = root + '.';
            private int rl = r.length();

            public DelegatingPropertyChangeListener(PropertyChangeListener delegate) {
                this.delegate = delegate;
            }

            public void propertyChange(PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if (propertyName.length() <= rl || !propertyName.startsWith(r)) {
                    // not a listener in this root
                    return ;
                }
                PropertyChangeEvent delegateEvt = new PropertyChangeEvent(
                        DelegatingProperties.this,
                        evt.getPropertyName().substring(rl),
                        evt.getOldValue(),
                        evt.getNewValue());
                delegateEvt.setPropagationId(evt.getPropagationId());
                delegate.propertyChange(delegateEvt);
            }
            
        }

    }
}
