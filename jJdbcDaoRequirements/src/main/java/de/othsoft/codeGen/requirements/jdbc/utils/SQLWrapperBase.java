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
import de.othsoft.codeGen.requirements.QueryRestr;
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
    protected String addCountToSql(String sql) {
        return StringConsts.SQL_COUNT_PART_1+sql+StringConsts.SQL_COUNT_PART_2;
    }
    
    protected String addFilter2Sql(String colName, QueryRestr r, String sql) {
        StringBuilder sb;
        switch(r.getType()) {
            case EQUAL:
                sb = new StringBuilder(colName);
                sb.append(SQL_STR_EQUAL);
                sb.append(SQL_STR_FRAGEZ);
                break;
            case NULL:
                sb = new StringBuilder(colName);
                sb.append(SQL_STR_NULL);
                break;
            case LARGER:
                sb = new StringBuilder(colName);
                sb.append(SQL_STR_LARGER);
                sb.append(SQL_STR_FRAGEZ);
                break;
            case SMALLER:
                break;
            case LARGEREQUAL:
                break;
            case SMALLEREQUAL:
                break;
            case LIKE:
                break;
            case IN:
                break;
            case LARGER_AND_SMALLEREQUAL:
                break;
            case LARGEREQUAL_AND_SMALLER:
                break;
            case NOT_EQUAL:
                break;
            case NOT_NULL:
                break;
            case NOT_LIKE:
                break;
            case NOT_IN:
                break;
            case NOT_LARGER_AND_SMALLEREQUAL:
                break;
            case NOT_LARGEREQUAL_AND_SMALLER:
                break;
            case VALUE_OR_NULL:
                break;
            case RAW:
                break;
            default:
                log.error("unknown restriction type",r.getType());
        }
        
    }

    private final static String SQL_STR_EQUAL = " = ";
    private final static String SQL_STR_NULL = " IS NULL";
    private final static String SQL_STR_LARGER = " > ";
    private final static String SQL_STR_SMALLER = " < ";
    private final static String SQL_STR_LARGEREQUAL = " >= ";
    private final static String SQL_STR_SMALLEREQUAL = " <= ";
    private final static String SQL_STR_LIKE = " LIKE ";
    private final static String SQL_STR_IN = " IN (";
    private final static String SQL_STR_NOT_EQUAL = " <> ";
    private final static String SQL_STR_NOT_NULL = " IS NOT NULL ";
    private final static String SQL_STR_NOT_LIKE = " NOT LIKE ";
    private final static String SQL_STR_NOT_IN = " NOT IN (";
/*
    private final static String SQL_STR_LARGER_AND_SMALLEREQUAL;
    private final static String SQL_STR_LARGEREQUAL_AND_SMALLER;
    private final static String SQL_STR_NOT_LARGER_AND_SMALLEREQUAL;
    private final static String SQL_STR_NOT_LARGEREQUAL_AND_SMALLER;
    private final static String SQL_STR_VALUE_OR_NULL;
*/
    private final static String SQL_STR_FRAGEZ = "?";
    private final static String SQL_STR_KLAMMER_ZU = ")";
}
