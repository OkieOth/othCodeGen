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
import de.othsoft.codeGen.requirements.UserData;
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import de.othsoft.codeGen.requirements.jdbc.JdbcCmdData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

/**
 *
 * @author eiko
 */
public class SQLExecM2NWrapper<T> extends SQLWrapperBase {
    public SQLExecM2NWrapper (Logger log) {
        super(log);
    }

    public List<T> byRef1(ISQLM2NWrapperUser<T> wrapperUser,IJdbcDataFactoryBase dataFactory,UserData userData,CmdData cmdData,int id) throws DaoException {
        return byRef(true,wrapperUser,dataFactory,userData,cmdData,id);
    }
    
    public List<T> byRef2(ISQLM2NWrapperUser<T> wrapperUser,IJdbcDataFactoryBase dataFactory,UserData userData,CmdData cmdData,int id) throws DaoException {
        return byRef(false,wrapperUser,dataFactory,userData,cmdData,id);
    }

    public int countByRef1(ISQLM2NWrapperUser<T> wrapperUser,IJdbcDataFactoryBase dataFactory,UserData userData,CmdData cmdData,int id) throws DaoException {
        return countByRef(true,wrapperUser,dataFactory,userData,cmdData,id);
    }
    
    public int countByRef2(ISQLM2NWrapperUser<T> wrapperUser,IJdbcDataFactoryBase dataFactory,UserData userData,CmdData cmdData,int id) throws DaoException {
        return countByRef(false,wrapperUser,dataFactory,userData,cmdData,id);
    }

    public void insert(ISQLM2NWrapperUser<T> wrapperUser, T data, IJdbcDataFactoryBase dataFactory,UserData userData,CmdData cmdData) throws DaoException {
        PreparedStatement ps=null;
        Connection con = ((cmdData!=null) && (cmdData instanceof JdbcCmdData))? ((JdbcCmdData)cmdData).getCon() : dataFactory.getConnectionFactory().getCon();
        try {
            String sql = wrapperUser.getInsSql();
            log.info(sql);
            ps=con.prepareStatement(sql);
            wrapperUser.setInsValues(ps,data);
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

    public void delete(ISQLM2NWrapperUser wrapperUser, int refId1,int refId2, IJdbcDataFactoryBase dataFactory,UserData userData,CmdData cmdData) throws DaoException {
        PreparedStatement ps=null;
        Connection con = ((cmdData!=null) && (cmdData instanceof JdbcCmdData))? ((JdbcCmdData)cmdData).getCon() : dataFactory.getConnectionFactory().getCon();
        try {
            String sql = wrapperUser.getDelSql();
            log.info(sql);
            ps=con.prepareStatement(sql);
            ps.setInt(1, refId1);
            ps.setInt(2, refId2);
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

    public T byIds(ISQLM2NWrapperUser<T> wrapperUser,IJdbcDataFactoryBase dataFactory,UserData userData,CmdData cmdData,int refId1,int refId2) throws DaoException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection con = ((cmdData!=null) && (cmdData instanceof JdbcCmdData))? ((JdbcCmdData)cmdData).getCon() : dataFactory.getConnectionFactory().getCon();            
        try {
            String sql = wrapperUser.getSelectByIdsSql();
            sql+=StringConsts.ID_RESTR;
            log.info(sql);
            ps=con.prepareStatement(sql);
            ps.setInt(1, refId1);
            ps.setInt(2, refId2);
            if (log.isDebugEnabled())
                log.debug("refId1="+refId1+", refId2="+refId2);
            rs = ps.executeQuery();
            if (rs.next()) {
                return wrapperUser.initFromResultSet(rs);
            }            
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
    
    private List<T> byRef(boolean ref1,ISQLM2NWrapperUser<T> wrapperUser,IJdbcDataFactoryBase dataFactory,UserData userData,CmdData cmdData,int id) throws DaoException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection con = ((cmdData!=null) && (cmdData instanceof JdbcCmdData))? ((JdbcCmdData)cmdData).getCon() : dataFactory.getConnectionFactory().getCon();            
        try {
            String sql = ref1 ? wrapperUser.getSelectSqlRef1() : wrapperUser.getSelectSqlRef2();
            log.info(sql);
            ps=con.prepareStatement(sql);
            ps.setInt(1, id);
            if (log.isDebugEnabled())
                log.debug("id="+id);
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

    private int countByRef(boolean ref1,ISQLM2NWrapperUser<T> wrapperUser,IJdbcDataFactoryBase dataFactory,UserData userData,CmdData cmdData,int id) throws DaoException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection con = ((cmdData!=null) && (cmdData instanceof JdbcCmdData))? ((JdbcCmdData)cmdData).getCon() : dataFactory.getConnectionFactory().getCon();            
        try {
            String sql = ref1 ? wrapperUser.getSelectSqlRef1() : wrapperUser.getSelectSqlRef2();
            sql=wrapperUser.addCountToSql(sql);
            log.info(sql);
            ps=con.prepareStatement(sql);
            ps.setInt(1, id);
            if (log.isDebugEnabled())
                log.debug("id="+id);
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
}
