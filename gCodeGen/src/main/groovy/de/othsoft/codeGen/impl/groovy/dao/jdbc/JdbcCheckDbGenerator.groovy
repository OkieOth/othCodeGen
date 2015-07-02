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

package de.othsoft.codeGen.impl.groovy.dao.jdbc

import de.othsoft.codeGen.types.CodeGenException
import de.othsoft.codeGen.types.DataModel
import de.othsoft.codeGen.types.AttribType
import de.othsoft.codeGen.types.ICodeGenImpl
import de.othsoft.codeGen.impl.helper.FileHelper
import groovy.text.SimpleTemplateEngine

/**
 *
 * @author eiko
 */
class JdbcCheckDbGenerator implements ICodeGenImpl {
    void genCode(DataModel model,Map params) {
        genCodeCheckDb(model,params);
        genCodeCheckExtraInterf(model,params);
    }
    
    void genCodeCheckExtraInterf(DataModel model,Map params) {
        String packageName=addGenPackageName(params.packageName)
        String destPath = getDestPath (params,packageName);
        def className = "IDbCheckExtras_${model.shortName}"
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(checkExtrasInterfTemplate)
        def daten = [
           model:model,
           packageName:packageName,
           className:className]
        def ergebnis = template.make(daten)

        File file=new File("${destPath}${className}.java")        
        file.write(ergebnis.toString())        
    }
    
    private String getDestPath(Map params,String packageName) {
        String destPathRoot=params.destPathRoot
        if (!destPathRoot.endsWith(File.separator))
            destPathRoot+=File.separator
        String packagePath=FileHelper.packageToDirName(packageName)
        String destPath = destPathRoot + packagePath
        if (!destPath.endsWith(File.separator))
        destPath+=File.separator
        FileHelper.createDirIfNotExists(destPath)
        return destPath;
    }

    void genCodeCheckDb (DataModel model,Map params) {        
        String packageName=addGenPackageName(params.packageName)
        String destPath = getDestPath (params,packageName);
        
        def className = "DbCheck_${model.shortName}"
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(checkDbTemplate)
        def daten = [
            model:model,
            strListType:AttribType.t_str_list,
           packageName:packageName,
           className:className]
        def ergebnis = template.make(daten)

        File file=new File("${destPath}${className}.java")        
        file.write(ergebnis.toString())
    }

    void genTestCode(DataModel model,Map params) {
        // TODO
    }
    
    private String addGenPackageName(String packageName) {
        if (!packageName)
            return 'dao.jdbc.dbcheck'
        else 
            return "${packageName}.dao.jdbc.dbcheck"
    }

    
    def checkDbTemplate='''
package ${packageName};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.JdbcCheckDbGenerator
 */

import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ${className} {
    private final static int VERSION=${model.version};
        
    private final static String SELECT_VERSION_SQL="SELECT versionsnummer FROM ${model.shortName}_Version";
    
    public boolean isDbVersionOkay (ConnectionFactory connectionFactory) throws DaoException {
        Connection con = connectionFactory.getCon();
        try {
            PreparedStatement ps=con.prepareStatement(SELECT_VERSION_SQL);            
            if (log.isDebugEnabled())
                log.debug(SELECT_VERSION_SQL);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int currentVersion=rs.getInt(1);
            return currentVersion==VERSION;
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
        finally {   
            closeCon(con);
        }        
    }

    public void check(ConnectionFactory factory) throws DaoException {
        List<String> missing=new ArrayList<String>();
        checkBase(factory,missing);
        checkExtras(factory,missing);
        if (!missing.isEmpty()) {
            StringBuilder sb=new StringBuilder();
            sb.append("missing database object: ");
            for (String s:missing) {
                sb.append(s);
                sb.append(", ");
            }
            throw new DaoException(log,sb.toString());
        }
    }
    
    public boolean checkTable(String tabName,ConnectionFactory factory,List<String> missing) throws DaoException {
        Connection con=factory.getCon();
        try {
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tabName.toLowerCase(), null);
            if (tables.next()) {
              return true;
            }
            else {
                tables = dbm.getTables(null, null, tabName.toUpperCase(), null);
                if (!tables.next()) {
                    missing.add("table: "+tabName);
                    return false;
                }
                return true;
            }            
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
        finally {
            closeCon(con);
        }
    }

    public boolean checkColumn(String tabName,String columnName,ConnectionFactory factory,List<String> missing) throws DaoException {
        Connection con=factory.getCon();
        try {
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet columns = dbm.getColumns(null,null, tabName.toLowerCase(),columnName.toLowerCase());
            if (columns.next()) {
              return true;
            }
            else {
                columns =dbm.getColumns(null,null, tabName.toUpperCase(),columnName.toUpperCase());
                if (!columns.next()) {
                    missing.add("column: "+tabName+"->"+columnName);
                    return false;
                }
                return true;
            }            
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
        finally {
            closeCon(con);
        }
    }

    private void checkBase(ConnectionFactory factory,List<String> missing) throws DaoException {
        // Tabellen <%
model.entities.each { entity -> 
        def tabName = "${model.shortName}_${entity.value.name}" %>
        checkTable("${tabName}",factory,missing);
    <% entity.value.attribs.each { attrib -> 
%>    checkColumn("$tabName","${attrib.name}",factory,missing);
    <% } } %>
        // Listen <%
model.entities*.value.attribs*.findAll { it.type == strListType }*.each { attrib ->
    def tabName="${model.shortName}_${attrib.parent.name}_${attrib.getNameWithFirstLetterUpper()}" %>
        checkTable("${tabName}",factory,missing);
        checkColumn("${tabName}","id",factory,missing);
        checkColumn("${tabName}","bez",factory,missing);
        checkColumn("${tabName}","lang",factory,missing);
        checkColumn("${tabName}","aktiv",factory,missing);
        checkColumn("${tabName}","reihenf",factory,missing);
<% } %>
        // Views <%
model.views.each { view -> 
        def viewName = "${model.shortName}_${view.value.name}" %>
        checkTable("${viewName}",factory,missing);
    <% view.value.attribs.each { attrib -> 
%>    checkColumn("$viewName","${attrib.name}",factory,missing);
    <% } }%>

        // M2N <%
model.m2nRelations.each { m2n -> \n\
def tabName = "${model.shortName}_${m2n.value.name}" %>\n\
        checkTable("${tabName}",factory,missing);
        checkColumn("${tabName}","${m2n.value.ref1.name}",factory,missing);
        checkColumn("${tabName}","${m2n.value.ref2.name}",factory,missing);
<% } %>
    }
    
    public boolean checkExtras(ConnectionFactory factory,List<String> missing) throws DaoException {
        Class c = this.getClass();
        ClassLoader cl = c.getClassLoader();
        try {
            Class extrasCheckClass = cl.loadClass("${packageName}.extras.CheckExtras_${model.shortName}");
            
            Class[] interfArray = extrasCheckClass.getInterfaces();
            boolean found = false;
            for (Class interf:interfArray) {
                if (interf.getName().equals("${packageName}.IDbCheckExtras_${model.shortName}")) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.info("extra check class found, but don't implement needed interface: ${packageName}.extras.CheckExtras");
                return false;
            }
            Constructor constructor = extrasCheckClass.getConstructor();
            IDbCheckExtras_${model.shortName} obj = (IDbCheckExtras_${model.shortName})constructor.newInstance();
            //Object obj = constructor.newInstance();            \n\
            return obj.check(factory,missing);
        }
        catch(NoSuchMethodException e) {
            log.info("no default constructor for extra check class: ${packageName}.extras.CheckExtras");
        }
        catch(NoClassDefFoundError e) {
            log.info("no extra check class found: ${packageName}.extras.CheckExtras");
        }
        catch(Exception e) {
            log.info("error while instantiate: ${packageName}.extras.CheckExtras - ["+e.getClass().getName()+"] "+e.getMessage());
        }
        return false;
    }
    
    private void closeCon(Connection con) throws DaoException {
        try {
            con.close();
        }
        catch(SQLException se) {
            throw new DaoException(log,se);
        }        
    }
    private static final Logger log = LoggerFactory.getLogger(${className}.class);
}
'''
    
    def checkExtrasInterfTemplate = '''
package ${packageName};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.JdbcCheckDbGenerator
 */

import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import java.util.List;

public interface IDbCheckExtras_${model.shortName} {
    boolean check(ConnectionFactory factory,List<String> missing);
}
'''
}

