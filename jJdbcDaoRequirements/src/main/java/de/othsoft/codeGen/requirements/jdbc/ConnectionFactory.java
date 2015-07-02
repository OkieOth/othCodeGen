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

import de.othsoft.codeGen.requirements.DaoException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author eiko
 */
public class ConnectionFactory {

    protected String conStr;
    protected String userStr;
    protected String pwdStr;
    protected String testStr;

    private PoolingDataSource _dataSource;
    private GenericObjectPool _connectionPool;

    public Connection getCon() throws DaoException {
        initPool();
        try {
            Connection con = _dataSource.getConnection();
            if (log.isDebugEnabled()) {
                int active = _connectionPool.getNumActive();
                int idle = _connectionPool.getNumIdle();
                log.debug("delivere connection -> active=" + active + ", idle=" + idle);
            }
            return con;
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
    }

    public int getPoolActiveConCount() throws DaoException {
        initPool();
        return _connectionPool.getNumActive();
    }

    public int getPoolIdleConCount() throws DaoException {
        initPool();
        return _connectionPool.getNumIdle();
    }

    public int getMaxIdle() throws DaoException {
        initPool();
        return _connectionPool.getMaxIdle();
    }

    public int getMaxActive() throws DaoException {
        initPool();
        return _connectionPool.getMaxActive();
    }

    public void setMaxIdle(int max) throws DaoException {
        initPool();
        _connectionPool.setMaxIdle(max);
    }

    public void setMaxActive(int max) throws DaoException {
        initPool();
        _connectionPool.setMaxActive(max);
    }

    private void initPool() throws DaoException {
        if (_connectionPool != null) {
            return;
        }
        if (conStr == null) {
            throw new DaoException(log,"connection string is needed");
        }
        if (userStr == null) {
            throw new DaoException(log,"user name is needed");
        }
        if (pwdStr == null) {
            throw new DaoException(log,"password is needed");
        }
        if (testStr == null) {
            throw new DaoException(log,"sql statement for connection test is needed");
        }
        String driverClassName = null;
        if (conStr.contains("postgresql")) {
            driverClassName = "org.postgresql.Driver";
	}
	else if (conStr.contains("oracle")) {
            driverClassName = "oracle.jdbc.driver.OracleDriver";
	}
	else {
            throw new DaoException(log,"unknown driver for conStr="+conStr);
        }
        try {
            Class.forName(driverClassName);
        }
        catch(ClassNotFoundException e) {
            throw new DaoException (log,e);
        }

        if (log.isInfoEnabled()) {
            log.info("init connection pool with: user="+userStr+", conStr="+conStr);
        }
        org.apache.commons.dbcp.ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(conStr, userStr, pwdStr);
        _connectionPool = new GenericObjectPool(null);
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, _connectionPool, null, testStr, false, true);
        _dataSource = new PoolingDataSource(_connectionPool);
    }

    public String getConStr() {
        return conStr;
    }

    public void setConStr(String conStr) {
        this.conStr = conStr;
    }

    public String getUserStr() {
        return userStr;
    }

    public void setUserStr(String userStr) {
        this.userStr = userStr;
    }

    public String getPwdStr() {
        return pwdStr;
    }

    public void setPwdStr(String pwdStr) {
        this.pwdStr = pwdStr;
    }

    public String getTestStr() {
        return testStr;
    }

    public void setTestStr(String testStr) {
        this.testStr = testStr;
    }

    
    private static final Logger log = LoggerFactory.getLogger(ConnectionFactory.class);

}
