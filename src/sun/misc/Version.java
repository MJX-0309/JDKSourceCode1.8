/*
 * Copyright (c) 1999, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package sun.misc;
import java.io.PrintStream;

public class Version {


    private static final String launcher_name =
        "openjdk";

    private static final String java_version =
        "1.8.0_322";

    private static final String java_runtime_name =
        "OpenJDK Runtime Environment";

    private static final String java_profile_name =
        "";

    private static final String java_runtime_version =
        "1.8.0_322-b01";

    private static final String java_distro_name =
        "Alibaba Dragonwell";

    private static final String java_distro_version =
        "8.10.11";

    static {
        init();
    }

    public static void init() {
        System.setProperty("java.version", java_version);
        System.setProperty("java.runtime.version", java_runtime_version);
        System.setProperty("java.runtime.name", java_runtime_name);
    }

    private static boolean versionsInitialized = false;
    private static int jvm_major_version = 0;
    private static int jvm_minor_version = 0;
    private static int jvm_micro_version = 0;
    private static int jvm_update_version = 0;
    private static int jvm_build_number = 0;
    private static String jvm_special_version = null;
    private static int jdk_major_version = 0;
    private static int jdk_minor_version = 0;
    private static int jdk_micro_version = 0;
    private static int jdk_update_version = 0;
    private static int jdk_build_number = 0;
    private static String jdk_special_version = null;

    /**
     * In case you were wondering this method is called by java -version.
     * Sad that it prints to stderr; would be nicer if default printed on
     * stdout.
     */
    public static void print() {
        print(System.err);
    }

    /**
     * This is the same as print except that it adds an extra line-feed
     * at the end, typically used by the -showversion in the launcher
     */
    public static void println() {
        print(System.err);
        System.err.println();
    }

    /**
     * Give a stream, it will print version info on it.
     */
    public static void print(PrintStream ps) {
        boolean isHeadless = false;

        /* Report that we're running headless if the property is true */
        String headless = System.getProperty("java.awt.headless");
        if ( (headless != null) && (headless.equalsIgnoreCase("true")) ) {
            isHeadless = true;
        }

        /* First line: platform version. */
        ps.println(launcher_name + " version \"" + java_version + "\"");

        /* Second line: runtime version (ie, libraries). */

        ps.print(java_runtime_name +
                 " (" + java_distro_name + " " + java_distro_version + ")" +
                 " (build " + java_runtime_version);

        if (java_profile_name.length() > 0) {
            // profile name
            ps.print(", profile " + java_profile_name);
        }

        if (java_runtime_name.indexOf("Embedded") != -1 && isHeadless) {
            // embedded builds report headless state
            ps.print(", headless");
        }
        ps.println(')');

        /* Third line: JVM information. */
        String java_vm_name    = System.getProperty("java.vm.name");
        String java_vm_version = System.getProperty("java.vm.version");
        String java_vm_info    = System.getProperty("java.vm.info");
        ps.println(java_vm_name +
                   " (" + java_distro_name + " " + java_distro_version + ")" +
                   " (build " + java_vm_version + ", " +
                   java_vm_info + ")");
    }


    /**
     * Returns the major version of the running JVM if it's 1.6 or newer
     * or any RE VM build. It will return 0 if it's an internal 1.5 or
     * 1.4.x build.
     *
     * @since 1.6
     */
    public static synchronized int jvmMajorVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jvm_major_version;
    }

    /**
     * Returns the minor version of the running JVM if it's 1.6 or newer
     * or any RE VM build. It will return 0 if it's an internal 1.5 or
     * 1.4.x build.
     * @since 1.6
     */
    public static synchronized int jvmMinorVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jvm_minor_version;
    }


    /**
     * Returns the micro version of the running JVM if it's 1.6 or newer
     * or any RE VM build. It will return 0 if it's an internal 1.5 or
     * 1.4.x build.
     * @since 1.6
     */
    public static synchronized int jvmMicroVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jvm_micro_version;
    }

    /**
     * Returns the update release version of the running JVM if it's
     * a RE build. It will return 0 if it's an internal build.
     * @since 1.6
     */
    public static synchronized int jvmUpdateVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jvm_update_version;
    }

    public static synchronized String jvmSpecialVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        if (jvm_special_version == null) {
            jvm_special_version = getJvmSpecialVersion();
        }
        return jvm_special_version;
    }
    public static native String getJvmSpecialVersion();

    /**
     * Returns the build number of the running JVM if it's a RE build
     * It will return 0 if it's an internal build.
     * @since 1.6
     */
    public static synchronized int jvmBuildNumber() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jvm_build_number;
    }

    /**
     * Returns the major version of the running JDK.
     *
     * @since 1.6
     */
    public static synchronized int jdkMajorVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jdk_major_version;
    }

    /**
     * Returns the minor version of the running JDK.
     * @since 1.6
     */
    public static synchronized int jdkMinorVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jdk_minor_version;
    }

    /**
     * Returns the micro version of the running JDK.
     * @since 1.6
     */
    public static synchronized int jdkMicroVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jdk_micro_version;
    }

    /**
     * Returns the update release version of the running JDK if it's
     * a RE build. It will return 0 if it's an internal build.
     * @since 1.6
     */
    public static synchronized int jdkUpdateVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jdk_update_version;
    }

    public static synchronized String jdkSpecialVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        if (jdk_special_version == null) {
            jdk_special_version = getJdkSpecialVersion();
        }
        return jdk_special_version;
    }
    public static native String getJdkSpecialVersion();

    /**
     * Returns the build number of the running JDK if it's a RE build
     * It will return 0 if it's an internal build.
     * @since 1.6
     */
    public static synchronized int jdkBuildNumber() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jdk_build_number;
    }

    // true if JVM exports the version info including the capabilities
    private static boolean jvmVersionInfoAvailable;
    private static synchronized void initVersions() {
        if (versionsInitialized) {
            return;
        }
        jvmVersionInfoAvailable = getJvmVersionInfo();
        if (!jvmVersionInfoAvailable) {
            // parse java.vm.version for older JVM before the
            // new JVM_GetVersionInfo is added.
            // valid format of the version string is:
            // n.n.n[_uu[c]][-<identifer>]-bxx
            CharSequence cs = System.getProperty("java.vm.version");
            if (cs.length() >= 5 &&
                Character.isDigit(cs.charAt(0)) && cs.charAt(1) == '.' &&
                Character.isDigit(cs.charAt(2)) && cs.charAt(3) == '.' &&
                Character.isDigit(cs.charAt(4))) {
                jvm_major_version = Character.digit(cs.charAt(0), 10);
                jvm_minor_version = Character.digit(cs.charAt(2), 10);
                jvm_micro_version = Character.digit(cs.charAt(4), 10);
                cs = cs.subSequence(5, cs.length());
                if (cs.charAt(0) == '_' && cs.length() >= 3) {
                    int nextChar = 0;
                    if (Character.isDigit(cs.charAt(1)) &&
                        Character.isDigit(cs.charAt(2)) &&
                        Character.isDigit(cs.charAt(3)))
                    {
                        nextChar = 4;
                    } else if (Character.isDigit(cs.charAt(1)) &&
                        Character.isDigit(cs.charAt(2)))
                    {
                        nextChar = 3;
                    }

                    try {
                        String uu = cs.subSequence(1, nextChar).toString();
                        jvm_update_version = Integer.valueOf(uu).intValue();
                        if (cs.length() >= nextChar + 1) {
                            char c = cs.charAt(nextChar);
                            if (c >= 'a' && c <= 'z') {
                                jvm_special_version = Character.toString(c);
                                nextChar++;
                            }
                        }
                    } catch (NumberFormatException e) {
                        // not conforming to the naming convention
                        return;
                    }
                    cs = cs.subSequence(nextChar, cs.length());
                }
                if (cs.charAt(0) == '-') {
                    // skip the first character
                    // valid format: <identifier>-bxx or bxx
                    // non-product VM will have -debug|-release appended
                    cs = cs.subSequence(1, cs.length());
                    String[] res = cs.toString().split("-");
                    for (String s : res) {
                        if (s.charAt(0) == 'b' && s.length() == 3 &&
                            Character.isDigit(s.charAt(1)) &&
                            Character.isDigit(s.charAt(2))) {
                            jvm_build_number =
                                Integer.valueOf(s.substring(1, 3)).intValue();
                            break;
                        }
                    }
                }
            }
        }
        getJdkVersionInfo();
        versionsInitialized = true;
    }

    // Gets the JVM version info if available and sets the jvm_*_version fields
    // and its capabilities.
    //
    // Return false if not available which implies an old VM (Tiger or before).
    private static native boolean getJvmVersionInfo();
    private static native void getJdkVersionInfo();
}

// Help Emacs a little because this file doesn't end in .java.
//
// Local Variables: ***
// mode: java ***
// End: ***
