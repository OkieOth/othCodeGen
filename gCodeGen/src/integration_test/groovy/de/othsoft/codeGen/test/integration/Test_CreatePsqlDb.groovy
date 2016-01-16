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
package de.othsoft.codeGen.test.integration

import org.junit.BeforeClass
import org.junit.Test
import static org.junit.Assert.*
import de.gCodeGen.test.dao.jdbc.DataFactory_rman
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory
import de.othsoft.codeGen.requirements.jdbc.utils.ISetPagingImpl
import de.othsoft.codeGen.requirements.jdbc.utils.impl.Psql_SetPagingImpl
import de.othsoft.codeGen.requirements.jdbc.utils.impl.SimpleSetFilterValuesImpl
import de.othsoft.codeGen.test.integration.gererated.InsertTestData

/**
 *
 * @author eiko
 */
class Test_CreatePsqlDb {
    private static String vmIp = null;

    def dbUser = 'vagrant'
    def dbPwd = 'vagrant444'
    def driverClass = 'org.postgresql.Driver'
    def testDb = 'g_codegen_v1'

    @BeforeClass
    public static void setUpClass() {
        println 'try to retrieve ip address of vm'
        File whereIAm = new File('.');
        String path2IpAddressFile = whereIAm.canonicalPath + '/src/test/vagrant/ubuntu_psql/run/ip_address.txt'
        new File(path2IpAddressFile).eachLine {
            if (vmIp==null) {
                // the file with the ip addresses contains all interface addresses of the vm ...
                // ... I try on what address a normal ping will success
                def ip = it;
                "ping -W 1 -c 1 $ip".execute().in.eachLine {
                    if (it.indexOf(', 0% packet loss,')!=-1) {
                        println "ip for tests: $ip";
                        vmIp = ip;                        
                    }
                }
            }
        }
    }
    
    private List readSqlStatementsFromFile(String file) {
        File createDbFile = new File(file);        
        
        assertTrue createDbFile.exists()
        
        List sqlStatements = []
        String aktCmd
        
        createDbFile.eachLine { line ->
            if ( line ==~ /^\s*\/?\*\/?.*$/) {
                // it's a comment line
            }
            else {
                // no comment line
                line = line.trim()
                if (line.length()>0) {
                    if (line.startsWith('CREATE ')) {
                        aktCmd = line                        
                    }
                    else if (line.startsWith('INSERT ')) {
                        aktCmd = line                        
                    }
                    else if (line.startsWith('ALTER ')) {
                        aktCmd = line
                    }
                    else if (line.startsWith('UPDATE ')) {
                        aktCmd = line                        
                    }
                    else {
                        aktCmd += ' '
                        aktCmd += line
                    }
                    if (line.endsWith(';')) {
                        // ';' must be trimmed
                        sqlStatements.add aktCmd.substring(0,aktCmd.length()-1)
                    }
                }
            }
        }        
        return sqlStatements
    }
    
    /** not the best method but it works :D */
    private boolean doesDbExist(def dbUser, def dbPwd, def dbName,def driverClass) {
        def sql = null
        try {
            println "test for database $dbName ..."
            sql = groovy.sql.Sql.newInstance("jdbc:postgresql://$vmIp/$dbName",dbUser,dbPwd,driverClass)
            println "test database alredy exists"
            return true
        }
        catch (Exception e) {
            println e.getMessage()+", "+e.getClass().getName()
            e.printStackTrace()
            println "test database not exists :'-("
            return false
        }
        finally {
            if ( sql != null )
                sql.close();
        }
    }

    private boolean dropDb(def dbUser, def dbPwd, def dbName,def driverClass) {
        def sql = null
        try {
            println "drop database $dbName"
            sql = groovy.sql.Sql.newInstance("jdbc:postgresql://$vmIp/postgres",dbUser,dbPwd,driverClass)
            sql.execute "DROP DATABASE $dbName".toString()
        }
        finally {
            if ( sql != null )
                sql.close();
        }
    }

    private ConnectionFactory createConnectionFactory(def dbUser, def dbPwd, String conStr) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setConStr(conStr)
        connectionFactory.setUserStr(dbUser)
        connectionFactory.setPwdStr(dbPwd)
        connectionFactory.setTestStr('select versionsnummer from rman_version')
        return connectionFactory;
    }
    
    private boolean createDb(def dbUser, def dbPwd, String dbName,def driverClass) {
        def sql = null
        try {
            def sqlStr = """CREATE DATABASE ${dbName} WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8'"""
            println sqlStr
            println "create database $dbName"
            sql = groovy.sql.Sql.newInstance("jdbc:postgresql://$vmIp/postgres",dbUser,dbPwd,driverClass)
            sql.execute sqlStr.toString()
            sql.execute "ALTER DATABASE ${dbName} OWNER TO vagrant;".toString()
        }
        finally {
            if ( sql != null )
                sql.close();
        }
    }    

    @Test
    public void test() {
        assertTrue vmIp!=null
        if (doesDbExist(dbUser,dbPwd,testDb,driverClass))
            dropDb(dbUser,dbPwd,testDb,driverClass)
        createDb (dbUser,dbPwd,testDb,driverClass)
        testVersion1()
        testVersion2()
        DataFactory_rman dataFactory = new DataFactory_rman()
        dataFactory.setConnectionFactory(createConnectionFactory (dbUser,dbPwd,"jdbc:postgresql://$vmIp/$testDb"))
        dataFactory.setSetPagingImpl(new Psql_SetPagingImpl())
        dataFactory.setSetFilterValuesImpl(new SimpleSetFilterValuesImpl())
        InsertTestData insertTestData = new InsertTestData()
        insertTestData.dummy(dataFactory)
    }
    
    void testVersion1 () {
        println 'test for createdb version 1'
        List sqlCmds = readSqlStatementsFromFile('src/generated/resources/sql/psql/v1/createDb.sql')
        def sqlTestDb = groovy.sql.Sql.newInstance("jdbc:postgresql://$vmIp/$testDb",dbUser,dbPwd,driverClass)
        try {
            println 'run generated sql statements v1 ...'
            for (sqlCmd in sqlCmds) {
                println "run: $sqlCmd" 
                sqlTestDb.execute sqlCmd
            }
            def rowCount = 0;
            sqlTestDb.eachRow('SELECT versionsnummer FROM rman_version') {
                rowCount++;
                assertEquals ('wrong database version',1,it.versionsnummer)
            }
            assertTrue rowCount==1
        }
        finally {
            sqlTestDb.close()
        }
    }

    void testVersion2 () {
        println 'test for createdb version 2'
        List sqlCmds = readSqlStatementsFromFile('src/generated/resources/sql/psql/v2/updates/upd_2.sql')
        def sqlTestDb = groovy.sql.Sql.newInstance("jdbc:postgresql://$vmIp/$testDb",dbUser,dbPwd,driverClass)
        try {
            println 'run generated sql statements update v2 ...'
            for (sqlCmd in sqlCmds) {
                println "run: $sqlCmd" 
                sqlTestDb.execute sqlCmd
            }
            def rowCount = 0;
            sqlTestDb.eachRow('SELECT versionsnummer FROM rman_version') {
                rowCount++;
                assertEquals ('wrong database version',2,it.versionsnummer)
            }
            assertTrue rowCount==1
        }
        finally {
            sqlTestDb.close()
        }
    }
}
