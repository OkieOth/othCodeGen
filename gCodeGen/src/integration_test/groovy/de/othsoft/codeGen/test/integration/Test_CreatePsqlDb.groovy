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
package de.othsoft.codeGen.tests.integration

import org.junit.BeforeClass
import org.junit.Test
import static org.junit.Assert.*

/**
 *
 * @author eiko
 */
class Test_CreatePsqlDb {
    private static String vmIp = null;

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
                        aktCmd += ''
                        if (line.endsWith(';')) {
                            // ';' must be trimmed
                            line = line.substring(0,line.length()-1)
                            aktCmd += line
                            sqlStatements.add aktCmd
                        }
                        else {
                            aktCmd += line
                        }
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
    void testVersion1 () {
        println 'test for createdb version 1'
        assertTrue vmIp!=null
        List sqlCmds = readSqlStatementsFromFile('src/generated/resources/sql/psql/v1/createDb.sql')
        // try to delete test database
        def dbUser = 'vagrant'
        def dbPwd = 'vagrant444'
        def driverClass = 'org.postgresql.Driver'
        def testDb = 'g_codegen_v1'
        if (doesDbExist(dbUser,dbPwd,testDb,driverClass))
            dropDb(dbUser,dbPwd,testDb,driverClass)
        createDb (dbUser,dbPwd,testDb,driverClass)

        def sqlTestDb = groovy.sql.Sql.newInstance("jdbc:postgresql://$vmIp/$testDb",dbUser,dbPwd,driverClass)
        println 'run generated sql statements v1 ...'
        for (sqlCmd in sqlCmds) {
            println "run: $sqlCmd" 
            sqlTestDb.execute sqlCmd
        }
    }
    
}
