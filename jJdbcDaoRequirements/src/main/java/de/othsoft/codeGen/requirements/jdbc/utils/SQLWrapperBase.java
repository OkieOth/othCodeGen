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
package de.othsoft.codeGen.requirements.jdbc.utils;

import de.othsoft.codeGen.requirements.CmdData;
import de.othsoft.codeGen.requirements.jdbc.JdbcCmdData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;

/**
 *
 * @author eiko
 */
public class SQLWrapperBase {
    protected Logger log;
    
    public SQLWrapperBase (Logger log) {
        this.log = log;
    }
    
    protected void ggfCloseStatement(PreparedStatement ps) {
        if (ps == null ) return;
        try {
            ps.close();
        }
        catch(SQLException e) {
            log.error("error while close statement",e);
        }
    }
    protected void ggfCloseResultSet(ResultSet rs) {
        if (rs == null ) return;
        try {
            rs.close();
        }
        catch(SQLException e) {
            log.error("error while close result set",e);
        }
    }

    protected void ggfCloseConnection(CmdData cmdData,Connection con) {
        if (!((cmdData!=null) && (cmdData instanceof JdbcCmdData))) {
            try {
                con.close();
            }
            catch(SQLException e) {
                log.error("error while close connection",e);
            }
        }
    }    
}
