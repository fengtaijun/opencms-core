/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/workplace/list/CmsListDateFormatter.java,v $
 * Date   : $Date: 2005/06/23 11:11:43 $
 * Version: $Revision: 1.5 $
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

package org.opencms.workplace.list;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Simple formatter for dates.<p>
 * 
 * @author Michael Moossen 
 * 
 * @version $Revision: 1.5 $ 
 * 
 * @since 6.0.0 
 */
public class CmsListDateFormatter implements I_CmsListFormatter {

    private int m_dateStyle;

    private int m_timeStyle;

    /**
     * Default constructor.<p>
     * 
     * Use medium style.<p>
     */
    public CmsListDateFormatter() {

        m_dateStyle = DateFormat.MEDIUM;
        m_timeStyle = DateFormat.MEDIUM;
    }

    /**
     * Customizable constructor.<p>
     * 
     * @param dateStyle the style for the date part
     * @param timeStyle the style for the time part
     * 
     * @see DateFormat
     */
    public CmsListDateFormatter(int dateStyle, int timeStyle) {

        m_dateStyle = dateStyle;
        m_timeStyle = timeStyle;
    }

    /**
     * @see org.opencms.workplace.list.I_CmsListFormatter#format(java.lang.Object, java.util.Locale)
     */
    public String format(Object data, Locale locale) {

        if (data == null || !(data instanceof Date)) {
            return "";
        }
        DateFormat dateFormat = DateFormat.getDateTimeInstance(m_dateStyle, m_timeStyle);
        return dateFormat.format(data);
    }
}
