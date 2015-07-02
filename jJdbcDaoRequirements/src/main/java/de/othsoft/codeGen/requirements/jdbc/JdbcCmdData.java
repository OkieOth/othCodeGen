/*
Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.
See the NOTICE file distributed with this work for additional information regarding copyright ownership.  
The ASF licenses this file to you under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
specific language governing permissions and limitations under the License.
*/

package de.othsoft.codeGen.requirements.jdbc;

import de.othsoft.codeGen.requirements.CmdData;
import de.othsoft.codeGen.requirements.DaoException;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author eiko
 */
public class JdbcCmdData extends CmdData {
    private Connection con;
    
    public JdbcCmdData(Connection con) {
        super();
        this.con = con;
    }
    
    @Override
    public void commit() throws DaoException {
        try {
            con.commit();
        }
        catch(SQLException e) {
            // TODO Logging
            throw new DaoException(LOG,e);
        }
    }

    @Override
    public void rollback() throws DaoException {
        try {
            con.rollback();
        }
        catch(SQLException e) {
            throw new DaoException(LOG,e);
        }
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
    
    private static final Logger LOG = LoggerFactory.getLogger(JdbcCmdData.class.getName());
}
