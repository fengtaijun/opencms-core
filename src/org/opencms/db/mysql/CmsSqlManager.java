/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/db/mysql/CmsSqlManager.java,v $
 * Date   : $Date: 2005/06/23 11:11:38 $
 * Version: $Revision: 1.22 $
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

package org.opencms.db.mysql;

/**
 * MySQL implementation of the SQL manager.<p>
 * 
 * @author Thomas Weckert 
 * 
 * @version $Revision: 1.22 $
 * 
 * @since 6.0.0 
 */
public class CmsSqlManager extends org.opencms.db.generic.CmsSqlManager {

    /** The filename/path of the SQL query properties. */
    private static final String C_QUERY_PROPERTIES = "org/opencms/db/mysql/query.properties";

    /**
     * @see org.opencms.db.generic.CmsSqlManager#CmsSqlManager()
     */
    public CmsSqlManager() {

        super();
        loadQueryProperties(C_QUERY_PROPERTIES);
    }

}