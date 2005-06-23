/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/report/A_CmsReport.java,v $
 * Date   : $Date: 2005/06/23 11:11:28 $
 * Version: $Revision: 1.18 $
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

package org.opencms.report;

import org.opencms.i18n.CmsMessageContainer;
import org.opencms.i18n.CmsMessages;
import org.opencms.util.CmsStringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Base report class.<p> 
 * 
 * @author Alexander Kandzior   
 * @author Thomas Weckert  
 * @author Jan Baudisch 
 * 
 * @version $Revision: 1.18 $ 
 * 
 * @since 6.0.0 
 */
public abstract class A_CmsReport implements I_CmsReport {

    /** Contains all error messages generated by the report. */
    private List m_errors = new ArrayList();

    /** The locale this report is written in. */
    private Locale m_locale;

    /** Localized message access object. */
    private List m_messages;

    /** Runtime of the report. */
    private long m_starttime;

    /**
     * @see org.opencms.report.I_CmsReport#addBundle(String)
     */
    public void addBundle(String bundleName) {

        CmsMessages msg = new CmsMessages(bundleName, getLocale());
        if (m_messages.contains(msg)) {
            m_messages.remove(msg);
        }
        m_messages.add(msg);
    }

    /**
     * @see org.opencms.report.I_CmsReport#addError(java.lang.Object)
     */
    public void addError(Object obj) {

        m_errors.add(obj);
    }

    /**
     * @see org.opencms.report.I_CmsReport#formatRuntime()
     */
    public String formatRuntime() {

        return CmsStringUtil.formatRuntime(getRuntime());
    }

    /**
     * @see org.opencms.report.I_CmsReport#getErrors()
     */
    public List getErrors() {

        return m_errors;
    }

    /**
     * @see org.opencms.report.I_CmsReport#getLocale()
     */
    public Locale getLocale() {

        return m_locale;
    }

    /**
     * @see org.opencms.report.I_CmsReport#getRuntime()
     */
    public long getRuntime() {

        return System.currentTimeMillis() - m_starttime;
    }

    /**
     * @see org.opencms.report.I_CmsReport#hasError()
     */
    public boolean hasError() {

        return (m_errors.size() > 0);
    }

    /**
     * @see org.opencms.report.I_CmsReport#key(java.lang.String)
     */
    public String key(String keyName) {

        for (int i = 0, l = m_messages.size(); i < l; i++) {
            CmsMessages msg = (CmsMessages)m_messages.get(i);
            String key = msg.key(keyName, (i < (l - 1)));
            if (key != null) {
                return key;
            }
        }
        // if not found, check in 
        return CmsMessages.formatUnknownKey(keyName);
    }

    /**
     * @see org.opencms.report.I_CmsReport#print(org.opencms.i18n.CmsMessageContainer)
     */
    public synchronized void print(CmsMessageContainer container) {

        print(container.key(getLocale()), C_FORMAT_DEFAULT);
    }

    /**
     * @see org.opencms.report.I_CmsReport#print(org.opencms.i18n.CmsMessageContainer, int)
     */
    public synchronized void print(CmsMessageContainer container, int format) {

        print(container.key(getLocale()), format);
    }

    /**
     * @see org.opencms.report.I_CmsReport#println(org.opencms.i18n.CmsMessageContainer)
     */
    public synchronized void println(CmsMessageContainer container) {

        println(container.key(getLocale()), C_FORMAT_DEFAULT);
    }

    /**
     * @see org.opencms.report.I_CmsReport#println(org.opencms.i18n.CmsMessageContainer, int)
     */
    public synchronized void println(CmsMessageContainer container, int format) {

        println(container.key(getLocale()), format);
    }

    /**
     * @see org.opencms.report.I_CmsReport#printMessageWithParam(org.opencms.i18n.CmsMessageContainer,Object)
     */
    public synchronized void printMessageWithParam(CmsMessageContainer container, Object param) {

        print(container, I_CmsReport.C_FORMAT_NOTE);
        print(Messages.get().container(Messages.RPT_ARGUMENT_1, param));
        print(Messages.get().container(Messages.RPT_DOTS_0));
    }

    /**
     * @see org.opencms.report.I_CmsReport#printMessageWithParam(int,int,org.opencms.i18n.CmsMessageContainer,Object)
     */
    public synchronized void printMessageWithParam(int m, int n, CmsMessageContainer container, Object param) {

        print(
            Messages.get().container(Messages.RPT_SUCCESSION_2, String.valueOf(m), String.valueOf(n)),
            I_CmsReport.C_FORMAT_NOTE);
        printMessageWithParam(container, param);
    }

    /**
     * @see org.opencms.report.I_CmsReport#resetRuntime()
     */
    public void resetRuntime() {

        m_starttime = System.currentTimeMillis();
    }

    /**
     * Initializes some member variables for this report.<p>
     * 
     * @param locale the locale for this report
     */
    protected void init(Locale locale) {

        m_starttime = System.currentTimeMillis();
        m_messages = new ArrayList();
        m_locale = locale;
    }

    /**
     * Prints a String to the report.<p>
     *
     * @param value the String to add
     */
    protected synchronized void print(String value) {

        print(value, C_FORMAT_DEFAULT);
    }

    /**
     * Prints a String to the report, using the indicated formatting.<p>
     * 
     * Use the contants starting with <code>C_FORMAT</code> from this interface
     * to indicate which formatting to use.<p>
     *
     * @param value the message container to add
     * @param format the formatting to use for the output
     */
    protected abstract void print(String value, int format);

    /**
     * Prints a String with line break to the report.<p>
     * 
     * @param value the message container to add
     */
    protected synchronized void println(String value) {

        println(value, C_FORMAT_DEFAULT);
    }

    /**
     * Prints a String with line break to the report, using the indicated formatting.<p>
     * 
     * Use the contants starting with <code>C_FORMAT</code> from this interface
     * to indicate which formatting to use.<p>
     *
     * @param value the String to add
     * @param format the formatting to use for the output
     */
    protected synchronized void println(String value, int format) {

        print(value, format);
        println();
    }
}