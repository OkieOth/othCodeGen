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
import de.othsoft.codeGen.requirements.QuerySort;
import de.othsoft.codeGen.requirements.UserData;
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import de.othsoft.codeGen.requirements.jdbc.JdbcCmdData;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;

/**
 *
 * @author eiko
 */
public class SQLExecWrapper<T> extends SQLWrapperBase {
    public static boolean isChanged(String oldValue,String newValue) {
        if (oldValue==null && newValue!=null) return true;
        if (oldValue!=null && newValue==null) return true;
        if (oldValue==null && newValue==null) return false;
        return oldValue.equals(newValue);
    }

    public static boolean isChanged(Integer oldValue,Integer newValue) {
        if (oldValue==null && newValue!=null) return true;
        if (oldValue!=null && newValue==null) return true;
        if (oldValue==null && newValue==null) return false;
        return oldValue.equals(newValue);
    }

    public static boolean isChanged(Double oldValue,Double newValue) {
        if (oldValue==null && newValue!=null) return true;
        if (oldValue!=null && newValue==null) return true;
        if (oldValue==null && newValue==null) return false;
        return oldValue.equals(newValue);
    }

    public static boolean isChanged(Date oldValue,Date newValue) {
        if (oldValue==null && newValue!=null) return true;
        if (oldValue!=null && newValue==null) return true;
        if (oldValue==null && newValue==null) return false;
        return oldValue.equals(newValue);
    }

    public static boolean isChanged(Boolean oldValue,Boolean newValue) {
        if (oldValue==null && newValue!=null) return true;
        if (oldValue!=null && newValue==null) return true;
        if (oldValue==null && newValue==null) return false;
        return oldValue.equals(newValue);
    }

    public static boolean isChanged(BigDecimal oldValue,BigDecimal newValue) {
        if (oldValue==null && newValue!=null) return true;
        if (oldValue!=null && newValue==null) return true;
        if (oldValue==null && newValue==null) return false;
        return oldValue.equals(newValue);
    }

    public SQLExecWrapper (Logger log) {
        super(log);
    }
        
    public Integer insert(ISQLInsWrapperUser<T> wrapperUser, T data, ConnectionFactory connectionFactory,UserData userData,CmdData cmdData) throws DaoException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection con = ((cmdData!=null) && (cmdData instanceof JdbcCmdData))? ((JdbcCmdData)cmdData).getCon() : connectionFactory.getCon();
        try {
            String sql = wrapperUser.getInsSql();
            log.info(sql);
            ps=con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            wrapperUser.setInsValues(ps,data);
            ps.execute();
            rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
        finally {
            ggfCloseResultSet(rs);
            ggfCloseStatement(ps);
            ggfCloseConnection(cmdData,con);
        }
    }

    public void delete(ISQLDelWrapperUser wrapperUser, int id, ConnectionFactory connectionFactory,UserData userData,CmdData cmdData) throws DaoException {
        PreparedStatement ps=null;
        Connection con = ((cmdData!=null) && (cmdData instanceof JdbcCmdData))? ((JdbcCmdData)cmdData).getCon() : connectionFactory.getCon();
        try {
            String sql = wrapperUser.getDelSql();
            log.info(sql);
            ps=con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
        finally {
            ggfCloseStatement(ps);
            ggfCloseConnection(cmdData,con);
        }
    }

    public void update(ISQLUpdWrapperUser<T> wrapperUser, T data, ConnectionFactory connectionFactory,UserData userData,CmdData cmdData) throws DaoException {
        PreparedStatement ps=null;
        Connection con = ((cmdData!=null) && (cmdData instanceof JdbcCmdData))? ((JdbcCmdData)cmdData).getCon() : connectionFactory.getCon();
        try {
            String sql = wrapperUser.getUpdSql(data);
            log.info(sql);
            ps=con.prepareStatement(sql);
            wrapperUser.setUpdValues(ps,data);
            ps.execute();
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
        finally {
            ggfCloseStatement(ps);
            ggfCloseConnection(cmdData,con);
        }
    }

    
    public int count(ISQLQueryWrapperUser<T> wrapperUser,ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,List<QueryRestr> restr) throws DaoException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection con = ((cmdData!=null) && (cmdData instanceof JdbcCmdData))? ((JdbcCmdData)cmdData).getCon() : connectionFactory.getCon();            
        try {
            String sql = wrapperUser.getSelectBaseSql();
            sql=wrapperUser.appendFilterToSql(sql, restr);
            sql=wrapperUser.addCountToSql(sql);
            log.info(sql);
            ps=con.prepareStatement(sql);
            wrapperUser.setFilterValues(ps,restr);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
        finally {
            ggfCloseResultSet(rs);
            ggfCloseStatement(ps);
            ggfCloseConnection(cmdData,con);
        }
    }

    
    public List<T> get(ISQLQueryWrapperUser<T> wrapperUser,ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,List<QueryRestr> restr,List<QuerySort> sort,int offset,int count)
            throws DaoException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection con = ((cmdData!=null) && (cmdData instanceof JdbcCmdData))? ((JdbcCmdData)cmdData).getCon() : connectionFactory.getCon();            
        try {
            String sql = wrapperUser.getSelectBaseSql();
            sql=wrapperUser.appendFilterToSql(sql, restr);
            sql=wrapperUser.appendPagingToSql(sql,offset,count);
            log.info(sql);
            ps=con.prepareStatement(sql);
            wrapperUser.setFilterValues(ps,restr);
            rs = ps.executeQuery();
            List<T> ret = new ArrayList();
            while (rs.next()) {
                ret.add(wrapperUser.initFromResultSet(rs));
            }
            return ret;
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
        finally {
            ggfCloseResultSet(rs);
            ggfCloseStatement(ps);
            ggfCloseConnection(cmdData,con);
        }
    }

    
    public T byId(ISQLQueryWrapperUser<T> wrapperUser,ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,int id) throws DaoException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection con = ((cmdData!=null) && (cmdData instanceof JdbcCmdData))? ((JdbcCmdData)cmdData).getCon() : connectionFactory.getCon();            
        try {
            String sql = wrapperUser.getSelectBaseSql();
            sql+=StringConsts.ID_RESTR;
            log.info(sql);
            ps=con.prepareStatement(sql);
            ps.setInt(1, id);
            if (log.isDebugEnabled())
                log.debug("id="+id);
            rs = ps.executeQuery();
            if (rs.next())
                return wrapperUser.initFromResultSet(rs);
            else
                return null;
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
        finally {
            ggfCloseResultSet(rs);
            ggfCloseStatement(ps);
            ggfCloseConnection(cmdData,con);
        }
    }

}
