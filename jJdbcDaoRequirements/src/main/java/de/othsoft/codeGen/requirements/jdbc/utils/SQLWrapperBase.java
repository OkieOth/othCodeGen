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
import de.othsoft.codeGen.requirements.DaoException;
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
    
    public static String addFilter2Sql(String colNameWithAlias, QueryRestr r, String sql) throws DaoException {
        StringBuilder sb = new StringBuilder(colNameWithAlias);
        switch(r.getType()) {
            case EQUAL:
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_EQUAL);
                sb.append(SQL_STR_FRAGEZ);
                break;
            case NULL:
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_NULL);
                break;
            case LARGER:
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_LARGER);
                sb.append(SQL_STR_FRAGEZ);
                break;
            case SMALLER:
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_SMALLER);
                sb.append(SQL_STR_FRAGEZ);
                break;
            case LARGEREQUAL:
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_LARGEREQUAL);
                sb.append(SQL_STR_FRAGEZ);
                break;
            case SMALLEREQUAL:
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_SMALLEREQUAL);
                sb.append(SQL_STR_FRAGEZ);
                break;
            case LIKE:
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_LIKE);
                sb.append(SQL_STR_FRAGEZ);
                break;
            case IN:
                // TODO - currently only one value is used, but multi values are better
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_IN);
                sb.append(SQL_STR_FRAGEZ);
                sb.append(SQL_STR_KLAMMER_ZU);
                break;
            case LARGER_AND_SMALLEREQUAL:
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_LARGER);
                sb.append(SQL_STR_FRAGEZ);
                sb.append(SQL_STR_AND);
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_SMALLEREQUAL);
                sb.append(SQL_STR_FRAGEZ);
                break;
            case LARGEREQUAL_AND_SMALLER:
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_LARGEREQUAL);
                sb.append(SQL_STR_FRAGEZ);
                sb.append(SQL_STR_AND);
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_SMALLER);
                sb.append(SQL_STR_FRAGEZ);
                break;
            case NOT_EQUAL:
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_NOT_EQUAL);
                sb.append(SQL_STR_FRAGEZ);
                break;
            case NOT_NULL:
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_NOT_NULL);
                break;
            case NOT_LIKE:
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_NOT_LIKE);
                sb.append(SQL_STR_FRAGEZ);
                break;
            case NOT_IN:
                // TODO - currently only one value is used, but multi values are better
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_NOT_IN);
                sb.append(SQL_STR_FRAGEZ);
                sb.append(SQL_STR_KLAMMER_ZU);
                break;
            case NOT_LARGER_AND_SMALLEREQUAL:
                sb.append(SQL_STR_NOT_KLAMMER_AUF);
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_LARGER);
                sb.append(SQL_STR_FRAGEZ);
                sb.append(SQL_STR_AND);
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_SMALLEREQUAL);
                sb.append(SQL_STR_FRAGEZ);
                sb.append(SQL_STR_KLAMMER_ZU);
                break;
            case NOT_LARGEREQUAL_AND_SMALLER:
                sb.append(SQL_STR_NOT_KLAMMER_AUF);
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_LARGEREQUAL);
                sb.append(SQL_STR_FRAGEZ);
                sb.append(SQL_STR_AND);
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_SMALLER);
                sb.append(SQL_STR_FRAGEZ);
                sb.append(SQL_STR_KLAMMER_ZU);
                break;
            case VALUE_OR_NULL:
                sb.append(SQL_STR_KLAMMER_AUF);
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_EQUAL);
                sb.append(SQL_STR_FRAGEZ);
                sb.append(SQL_STR_OR);
                sb.append(colNameWithAlias);
                sb.append(SQL_STR_NULL);
                sb.append(SQL_STR_KLAMMER_ZU);
                break;
            case RAW:
                sb.append(colNameWithAlias);
                sb.append(r.getRawSql());
                break;
            default:
                throw new DaoException("unknown restriction type"+r.getType());
        }
        return sb.length()==0 ? sql + sb.toString() : sql;
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
    private final static String SQL_STR_FRAGEZ = "?";
    private final static String SQL_STR_KLAMMER_ZU = ")";
    private final static String SQL_STR_NOT_KLAMMER_AUF = " NOT (";
    private final static String SQL_STR_KLAMMER_AUF = " (";
    private final static String SQL_STR_AND = " AND ";
    private final static String SQL_STR_OR = " OR ";
}
