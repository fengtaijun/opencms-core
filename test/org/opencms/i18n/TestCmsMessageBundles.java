/*
 * File   : $Source: /alkacon/cvs/opencms/test/org/opencms/i18n/TestCmsMessageBundles.java,v $
 * Date   : $Date: 2005/06/23 11:11:43 $
 * Version: $Revision: 1.11 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (c) 2005 Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.i18n;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;

/**
 * Tests for the CmsMessageBundles.<p>
 * 
 * @author Alexander Kandzior 
 * @since 5.7.3
 */
public abstract class TestCmsMessageBundles extends TestCase {

    /**
     * Checks all OpenCms internal message bundles if the are correctly build.<p>
     * 
     * @throws Exception if the test fails
     */
    public final void testMessagesBundleConstants() throws Exception {

        I_CmsMessageBundle[] bundles = getTestMessageBundles();
        for (int i = 0; i < bundles.length; i++) {
            doTestBundle(bundles[i]);
        }
    }
    
    /**
     * Tests if message will be returned in the correct locale.<p>
     * 
     * @throws Exception if the test fails
     */
    public final void testLocale() throws Exception {

        CmsMessages messages = new CmsMessages("org.opencms.i18n.messages", Locale.GERMANY);
        String value = messages.key("LOG_LOCALE_MANAGER_FLUSH_CACHE_1", new Object[]{"TestEvent"});
        assertEquals("Locale manager leerte die Caches nachdem Event TestEvent empfangen wurde.", value);
    }

    /**
     * Tests an individual message bundle.<p>
     * 
     * @param bundle the bundle to test
     * @throws Exception if the test fails
     */
    private void doTestBundle(I_CmsMessageBundle bundle) throws Exception {

        List keys = new ArrayList();

        System.out.println("\nValidating all keys in bundle " + bundle.getBundleName() + ":");

        // use reflection on all member constants
        Field[] fields = bundle.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getType().equals(String.class)) {
                // check all String fields
                String key = field.getName();

                String value;
                try {
                    value = (String)field.get(bundle);
                } catch (IllegalAccessException e) {
                    continue;
                }

                System.out.println("Validating key '" + key + "'");

                // ensure the name id identical to the value
                if (!key.equals(value)) {
                    fail("Key '" + key + "' in bundle " + bundle.getBundleName()
                        + " has bad value '" + value + "'");
                }

                // check if key exists in bundle for constant 
                String message = bundle.key(key, null);
                if (CmsMessages.isUnknownKey(message)) {
                    String bundleName = bundle.getBundleName();
                    if (!bundleName.endsWith(".messages")) {
                        fail("The Message bundle name \""
                            + bundleName
                            + "\" does not end with: \".messages\". \n "
                            + "Change the constant literal (\"private static final String BUNDLE_NAME\")");
                    } else {
                        fail("No message for '" + key + "' in bundle " + bundleName);
                    }
                }

                // ensure key has the form "{ERR|LOG|INIT|GUI|RPT}_KEYNAME_{0-9}";
                if (key.length() < 7) {
                    fail("Key '" + key + "' in bundle " + bundle.getBundleName()
                        + " is to short (length must be at last 7)");
                }
                if (!key.equals(key.toUpperCase())) {
                    fail("Key '" + key + "' in bundle " + bundle.getBundleName()
                        + " must be all upper case");
                }
                if ((key.charAt(key.length() - 2) != '_')
                    || (!key.startsWith("ERR_") && !key.startsWith("LOG_")
                        && !key.startsWith("INIT_") && !key.startsWith("GUI_") && !key.startsWith("RPT_"))) {
                    fail("Key '" + key + "' in bundle " + bundle.getBundleName()
                        + " must have the form {ERR|LOG|INIT|GUI|RPT}_KEYNAME_{0-9}");
                }
                int argCount = Integer.valueOf(key.substring(key.length() - 1)).intValue();

                for (int j = 0; j < argCount; j++) {
                    String arg = "{" + j;
                    int pos = message.indexOf(arg);
                    if (pos < 0) {
                        fail("Message '" + message + "' for key '" + key + "' in bundle "
                            + bundle.getBundleName() + " misses argument {" + j + "}");
                    }
                }
                for (int j = argCount; j < 10; j++) {
                    String arg = "{" + j;
                    int pos = message.indexOf(arg);
                    if (pos >= 0) {
                        fail("Message '" + message + "' for key '" + key + "' in bundle "
                            + bundle.getBundleName() + " containes unused argument {" + j + "}");
                    }
                }

                // store this key for later check against all properties in the bundle
                keys.add(key);
            }
        }

        CmsMessages messages = bundle.getBundle();

        Enumeration bundleKeys = messages.m_resourceBundle.getKeys();
        while (bundleKeys.hasMoreElements()) {
            String bundleKey = (String)bundleKeys.nextElement();
            if (bundleKey.toUpperCase().equals(bundleKey)) {
                // only check keys which are all upper case
                if (!keys.contains(bundleKey)) {
                    fail("Bundle " + bundle.getBundleName() + " contains unreferenced message "
                        + bundleKey);
                }
            } else {
                System.out.println("Additional key in bundle '" + bundleKey + "'");
            }
        }
    }

    /**
     * Template method that has to be overwritten to return the <code>I_CmsMessageBundle</code> 
     * instances that will be tested.<p> 
     * 
     * @return the <code>I_CmsMessageBundle</code> instances to test: these will be the 
     *         singleton instances of the <code>Messages</code> classes residing in every localized package. 
     */
    protected abstract I_CmsMessageBundle[] getTestMessageBundles();
}
