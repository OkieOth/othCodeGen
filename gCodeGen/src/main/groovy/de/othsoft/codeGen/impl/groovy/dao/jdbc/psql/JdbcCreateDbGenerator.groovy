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

package de.othsoft.codeGen.impl.groovy.dao.jdbc.psql

import de.othsoft.codeGen.impl.groovy.sql.psql.CreateDatabaseSqlGenerator
import de.othsoft.codeGen.types.CodeGenException
import de.othsoft.codeGen.types.DataModel
import de.othsoft.codeGen.requirements.AttribType
import de.othsoft.codeGen.types.ICodeGenImpl
import de.othsoft.codeGen.impl.helper.FileHelper
import groovy.text.SimpleTemplateEngine

/**
 *
 * @author eiko
 */
class JdbcCreateDbGenerator implements ICodeGenImpl {
    void genCode(DataModel model,Map params) {
        genCodeCreateDb(model,params);
        genCodeCreateExtraInterf(model,params);
        genCodeCreateAll(model,params);
        genCodeCreateUpd(model,params);
    }

    void genCodeCreateAll(DataModel model,Map params) {
        String packageName=addGenPackageName(params.packageName)+".all";
        def className = "CreateDb_${model.shortName}"
        genCodeBase(model,params,packageName,className,createAllTemplate)
    }

    void genCodeCreateUpd(DataModel model,Map params) {
        String packageName=addGenPackageName(params.packageName)+".upd_"+model.version;
        def className = "UpdateDb_${model.shortName}"
        genCodeBase(model,params,packageName,className,createUpdTemplate)
    }

    void genCodeCreateDb(DataModel model,Map params) {
        String packageName=addGenPackageName(params.packageName)
        def className = "DbCreate_${model.shortName}"
        genCodeBase(model,params,packageName,className,createDbTemplate)
    }
    
    void genCodeBase(DataModel model,Map params,String packageName,String className,def templateTxt) {
        String destPath = getDestPath (params,packageName);
        
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(templateTxt)
        def daten = [
            model:model,
            strListType:AttribType.t_str_list,
           packageName:packageName,
           typeConvert: new CreateDatabaseSqlGenerator().typeConvert,
           neededVersion: model.version,
           className:className]
        def ergebnis = template.make(daten)

        File file=new File("${destPath}${className}.java")        
        file.write(ergebnis.toString())        
    }
    
    void genTestCode(DataModel model,Map params) {
        // not needed this time
    }

    void genCodeCreateExtraInterf(DataModel model,Map params) {
        String packageName=addGenPackageName(params.packageName)
        def className = "IDbCreateExtras_${model.shortName}"
        genCodeBase(model,params,packageName,className,createExtrasInterfTemplate)
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

    private String addGenPackageName(String packageName) {
        if (!packageName)
            return 'dao.jdbc.dbcreate.psql'
        else 
            return "${packageName}.dao.jdbc.dbcreate.psql"
    }

    def createExtrasInterfTemplate = '''
package ${packageName};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.psql.JdbcCreateDbGenerator
 */

import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;

public interface IDbCreateExtras_${model.shortName} {
    void create(ConnectionFactory factory) throws DaoException;
}
'''
    def createDbTemplate = '''
package ${packageName};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.psql.JdbcCreateDbGenerator
 */

import java.lang.reflect.Constructor;
import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbCreate_${model.shortName} {
    private final static int VERSION=${model.version};
    private final static String SELECT_VERSION_SQL="SELECT versionsnummer FROM ${model.shortName}_Version";

    void create(ConnectionFactory factory) throws DaoException {
        createBase(factory,".all","CreateDb_${model.shortName}");
        createBase(factory,".all","CreateExtras_${model.shortName}");
    }
    
    public void update(ConnectionFactory factory) throws DaoException {
        int aktVersion = getCurrentVersion(factory);
        for (int i=aktVersion+1;i<=VERSION;i++) {
            createBase(factory,".upd_"+i,"UpdateDb_${model.shortName}");
            createBase(factory,".upd_"+i,"UpdateExtras_${model.shortName}");
        }
    }

    private void createBase(ConnectionFactory factory,String loadPackage,String className) throws DaoException {
        String packageName=PACKAGE_NAME+loadPackage;
        Class c = this.getClass();
        ClassLoader cl = c.getClassLoader();
        try {
            Class createExtrasClass = cl.loadClass(packageName + "." + className + "_${model.shortName}");
            
            Class[] interfArray = createExtrasClass.getInterfaces();
            boolean found = false;
            for (Class interf:interfArray) {
                if (interf.getName().equals("${packageName}.IDbCreateExtras_${model.shortName}")) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new DaoException(log,"update class found, but don't implement needed interface: ${packageName}.IDbCreateExtras_${model.shortName}");
            }
            Constructor constructor = createExtrasClass.getConstructor();
            IDbCreateExtras_${model.shortName} obj = (IDbCreateExtras_${model.shortName})constructor.newInstance();
            obj.create(factory);
        }
        catch(NoSuchMethodException e) {
            log.info("no default constructor for update class: "+ packageName + "." + className + "_${model.shortName}");
        }
        catch(NoClassDefFoundError e) {
            throw new DaoException(log,"no update class found: " + packageName + "." + className + "_${model.shortName}");
        }
        catch(Exception e) {
            log.info("error while instantiate: " + packageName + "." + className + "_${model.shortName} - ["+e.getClass().getName()+"] "+e.getMessage());
        }
    }

    public int getCurrentVersion (ConnectionFactory connectionFactory) throws DaoException {
        Connection con = connectionFactory.getCon();
        try {
            PreparedStatement ps=con.prepareStatement(SELECT_VERSION_SQL);            
            if (log.isDebugEnabled())
                log.debug(SELECT_VERSION_SQL);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
        finally {   
            closeCon(con);
        }        
    }

    private void closeCon(Connection con) throws DaoException {
        try {
            con.close();
        }
        catch(SQLException se) {
            throw new DaoException(log,se);
        }        
    }

    private static final String PACKAGE_NAME="${packageName}";
    private static final Logger log = LoggerFactory.getLogger(${className}.class);
}
'''

    def createAllTemplate = '''
package ${packageName};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.psql.JdbcCreateDbGenerator
 */

<% if (!model.views.isEmpty()) { %>
/**
 * Place the creation of the views in class ${packageName}.CreateExtas_${model.shortName}
 */

import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ${className} implements ${packageName.substring(0,packageName.lastIndexOf('.'))}.IDbCreateExtras_${model.shortName} {
    private final static int VERSION=${model.version};
    private static final Logger log = LoggerFactory.getLogger(${className}.class);

    public void create(ConnectionFactory factory) throws DaoException {
<% model.entities*.value.attribs*.findAll { it.type == strListType }*.each { attrib -> %>
        createBase(factory, getCreate${attrib.parent.getNameWithFirstLetterUpper()}${attrib.getNameWithFirstLetterUpper()}Sql());
<% } %>
<% model.entities.each { entity -> %>
        createBase(factory, getCreate${entity.value.getNameWithFirstLetterUpper()}Sql());
<% } %> <% model.m2nRelations.each { m2n -> %>
        createBase(factory, getCreate${m2n.value.getNameWithFirstLetterUpper()}Sql());
<% } %>
        createBase(factory, getInsVersionSql());
    }

    private void createBase(ConnectionFactory factory,String sql) throws DaoException {
        Connection con = factory.getCon();
        try {
            PreparedStatement ps=con.prepareCall(sql);
            ps.execute();
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
        finally {
            try {
                con.close();
            }
            catch(SQLException ee) {
                throw new DaoException(log,ee,"error while close connection");
            }
        }
    }
    
    private String getCreateVersionSql () { 
        return "CREATE TABLE IF NOT EXISTS ${model.shortName}_Version ( " + 
                    "versionsnummer int NOT NULL, " + 
                    "datum   timestamp)";
    }

    private String getInsVersionSql() {
        return "INSERT INTO ${model.shortName}_Version (versionsnummer,datum) VALUES (${model.version},current_timestamp)";
    }

<% model.entities*.value.attribs*.findAll { it.type == strListType }*.each { attrib -> %>
    private String getCreate${attrib.parent.getNameWithFirstLetterUpper()}${attrib.getNameWithFirstLetterUpper()}Sql() {
        return "CREATE TABLE IF NOT EXISTS ${model.shortName}_${attrib.parent.name}_${attrib.getNameWithFirstLetterUpper()} ( " + 
                    "id SERIAL " + 
                    ", bez text " + 
                    ", lang text " + 
                    ", aktiv boolean " + 
                    ", reihenf integer " + 
                    ",CONSTRAINT pk_${model.shortName}_${attrib.parent.name.toLowerCase()}_${attrib.name} PRIMARY KEY (id))";
    }
<% } %>
<% model.entities.each { entity -> %>
    private String getCreate${entity.value.getNameWithFirstLetterUpper()}Sql() {
        return "CREATE TABLE IF NOT EXISTS ${model.shortName}_${entity.value.name} ( " + 
                    "id SERIAL " + 
<% entity.value.attribs.each { attrib ->  if ( attrib.type == strListType ) 
{ %>                    ",${attrib.name}_id ${typeConvert(attrib.type)} " + 
<% } else { %>                    ",${attrib.name} ${typeConvert(attrib.type)} " + 
<% } } %> <% entity.value.refs.each { ref -> %>
                    ",${ref.name} integer " + <% } %>
                    ",CONSTRAINT pk_${model.shortName}_${entity.value.name.toLowerCase()} PRIMARY KEY (id))";
    }
<% } %> <% model.m2nRelations.each { m2n -> %>
    private String getCreate${m2n.value.getNameWithFirstLetterUpper()}Sql() {
        return "CREATE TABLE IF NOT EXISTS ${model.shortName}_${m2n.value.name} ( " + 
                    "${m2n.value.ref1.name} integer " + 
                    ",${m2n.value.ref2.name} integer)";
    }
<% } %>
<% } %>
}'''
    def createUpdTemplate = '''
package ${packageName};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.psql.JdbcCreateDbGenerator
 */

import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ${className} implements ${packageName.substring(0,packageName.lastIndexOf('.'))}.IDbCreateExtras_${model.shortName} {
    private final static int VERSION=${model.version};
    private static final Logger log = LoggerFactory.getLogger(${className}.class);

    public void create(ConnectionFactory factory) throws DaoException {
<% model.entities*.value.attribs*.findAll { it.since == neededVersion }*.each { attrib -> %>
        createBase(factory, getUpd${attrib.parent.getNameWithFirstLetterUpper()}${attrib.parent.getNameWithFirstLetterUpper()} ());
<% } %><% model.entities*.value.refs*.findAll { it.since == neededVersion }*.each { ref -> %>
        createBase(factory, getUpd${ref.parent.getNameWithFirstLetterUpper()}${ref.entity.getNameWithFirstLetterUpper()} ());
<% } %><% model.entities*.findAll { it.value.since==neededVersion }*.each { entity -> %>
        createBase(factory, getCreate${entity.value.getNameWithFirstLetterUpper()}Sql());
    <% entity.value.attribs*.findAll { it.type == strListType }*.each { attrib -> %>
        createBase(factory, getCreate${attrib.parent.getNameWithFirstLetterUpper()}${attrib.getNameWithFirstLetterUpper()}Sql());
    <% } %><% } %><% model.m2nRelations*.findAll { it.value.since==neededVersion }*.each { m2n -> %>
        createBase(factory, getCreate${m2n.value.getNameWithFirstLetterUpper()}Sql());
<% } %>
        createBase(factory, getUpdateVersionSql());
    }

    private void createBase(ConnectionFactory factory,String sql) throws DaoException {
        Connection con = factory.getCon();
        try {
            PreparedStatement ps=con.prepareCall(sql);
            ps.execute();
        }
        catch(SQLException e) {
            throw new DaoException(log,e);
        }
        finally {
            try {
                con.close();
            }
            catch(SQLException ee) {
                throw new DaoException(log,ee,"error while close connection");
            }
        }
    }

<% model.entities*.value.attribs*.findAll { it.since == neededVersion }*.each { attrib -> %>
    private String getUpd${attrib.parent.getNameWithFirstLetterUpper()}${attrib.parent.getNameWithFirstLetterUpper()} () {
        return "ALTER TABLE ${model.shortName}_${attrib.parent.name} ADD COLUMN ${attrib.name}  ${typeConvert(attrib.type)}";
    }
<% } %>

<% model.entities*.value.refs*.findAll { it.since == neededVersion }*.each { ref -> %>
    private String getUpd${ref.parent.getNameWithFirstLetterUpper()}${ref.entity.getNameWithFirstLetterUpper()} () {
        return "ALTER TABLE ${model.shortName}_${ref.parent.name} ADD COLUMN ${ref.entity.getNameWithFirstLetterLower()}_id integer";
    }
<% } %>

<% model.entities*.findAll { it.value.since==neededVersion }*.each { entity -> %>
    private String getCreate${entity.value.getNameWithFirstLetterUpper()}Sql() {
        return "CREATE TABLE IF NOT EXISTS ${model.shortName}_${entity.value.name} ( " + 
                    "id SERIAL " + 
<% entity.value.attribs.each { attrib ->  if ( attrib.type == strListType ) 
{ %>                    ",${attrib.name}_id ${typeConvert(attrib.type)} " + 
<% } else { %>                    ",${attrib.name} ${typeConvert(attrib.type)} " + 
<% } } %> <% entity.value.refs.each { ref -> %>
                    ",${ref.name} integer " + <% } %>
                    ",CONSTRAINT pk_${model.shortName}_${entity.value.name.toLowerCase()} PRIMARY KEY (id))";
    }
    <% entity.value.attribs*.findAll { it.type == strListType }*.each { attrib -> %>
    private String getCreate${attrib.parent.getNameWithFirstLetterUpper()}${attrib.getNameWithFirstLetterUpper()}Sql() {
        return "CREATE TABLE IF NOT EXISTS ${model.shortName}_${attrib.parent.name}_${attrib.getNameWithFirstLetterUpper()} ( " + 
                    "id SERIAL " + 
                    ", bez text " + 
                    ", lang text " + 
                    ", aktiv boolean " + 
                    ", reihenf integer " + 
                    ",CONSTRAINT pk_${model.shortName}_${attrib.parent.name.toLowerCase()}_${attrib.name} PRIMARY KEY (id))";
    }
    <% } %>
<% } %>

<% model.m2nRelations*.findAll { it.value.since==neededVersion }*.each { m2n -> %>
    private String getCreate${m2n.value.getNameWithFirstLetterUpper()}Sql() {
        return "CREATE TABLE IF NOT EXISTS ${model.shortName}_${m2n.value.name} ( " + 
                    "${m2n.value.ref1.name} integer " + 
                    ",${m2n.value.ref2.name} integer)";
    }
<% } %>
    private String getUpdateVersionSql() {
        return "UPDATE ${model.shortName}_Version SET versionsnummer=${neededVersion},datum=current_timestamp";
    }
}'''
}

