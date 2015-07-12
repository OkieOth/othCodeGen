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
import de.othsoft.codeGen.impl.java.JavaBeanGeneratorBase
import groovy.text.SimpleTemplateEngine

/**
 * generates bean classes that work with standard jdbc connections. This beans are returned by jdbc data factories
 * @see de.othsoft.codeGen.tests.generators.JdbcBeanGenerator_Test
 * @author eiko
 */
class JdbcBeanGenerator extends JavaBeanGeneratorBase implements ICodeGenImpl {
    void genCode(DataModel model,Map params) {
        String packageName=addGenPackageName(params.packageName)
        String baseClassPackage=basePackageName(params.packageName)
        String destPathRoot=params.destPathRoot
        if (!destPathRoot.endsWith(File.separator))
            destPathRoot+=File.separator
        String packagePath=FileHelper.packageToDirName(packageName)
        String destPath = destPathRoot + packagePath
        if (!destPath.endsWith(File.separator))
        destPath+=File.separator
        FileHelper.createDirIfNotExists(destPath)
        createListBeans(destPath, packageName, model,baseClassPackage)
        createEntityBeans(destPath, packageName, model,baseClassPackage)
        createM2NBeans(destPath, packageName, model,baseClassPackage)
        createViewBeans(destPath, packageName, model,baseClassPackage)
    }
    void genTestCode(DataModel model,Map params) {
        // TODO
    }

    private void createListBeans(String destPath, String destPackage, DataModel model,String baseClassPackage) {
        model.entities*.value.attribs*.findAll { it.type == AttribType.t_str_list }*.each { attrib ->
            def entityName = "${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}"
            def descr = attrib.descr
            String baseClassName="${baseClassPackage}.${entityName}"
            genResultAndWriteToFile(templateListBean,"Jdbc_${entityName}", model, destPackage, destPath,descr,attrib,baseClassName)            
        }
    }
    
    private void createEntityBeans(String destPath, String destPackage, DataModel model,String baseClassPackage) {
        model.entities*.each {
            def entityName=it.value.getNameWithFirstLetterUpper()
            def descr = it.value.descr
            String baseClassName="${baseClassPackage}.${entityName}"
            genResultAndWriteToFile(templateEntityBean,"Jdbc_${entityName}", model, destPackage, destPath,descr,it.value,baseClassName)
        }        
    }

    private void createM2NBeans(String destPath, String destPackage, DataModel model,String baseClassPackage) {
        model.m2nRelations*.each {
            def entityName=it.value.getNameWithFirstLetterUpper()
            def descr = it.value.descr
            String baseClassName="${baseClassPackage}.${entityName}"
            genResultAndWriteToFile(templateM2NBean,"Jdbc_${entityName}", model, destPackage, destPath,descr,it.value,baseClassName)
        }
    }

    private void createViewBeans(String destPath, String destPackage, DataModel model,String baseClassPackage) {
        model.views*.each {
            def viewName=it.value.getNameWithFirstLetterUpper()
            def descr = it.value.descr
            String baseClassName="${baseClassPackage}.${viewName}"
            genResultAndWriteToFile(templateViewBean,"Jdbc_${viewName}", model, destPackage, destPath,descr,it.value,baseClassName)
        }        
    }

    private String addGenPackageName(String packageName) {
        if (!packageName)
            return 'dao.jdbc.beans'
        else 
            return "${packageName}.dao.jdbc.beans"
    }

    private String basePackageName(String packageName) {
        if (!packageName)
            return 'beans'
        else 
            return "${packageName}.beans"
    }
    
    def templateEntityBean='''
package ${destPackage};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.JdbcBeanGenerator
 */

import de.othsoft.codeGen.requirements.jdbc.utils.ISQLQueryWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.ISQLInsWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.ISQLUpdWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.ISQLDelWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.SQLExecWrapper;
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.QueryRestr;
import de.othsoft.codeGen.requirements.QuerySort;
import de.othsoft.codeGen.requirements.CmdData;
import de.othsoft.codeGen.requirements.jdbc.JdbcCmdData;
import de.othsoft.codeGen.requirements.UserData;
import de.othsoft.codeGen.requirements.jdbc.utils.StringConsts;
import de.othsoft.codeGen.requirements.RestrType;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ${className} extends ${baseClassName} {
    ConnectionFactory connectionFactory;
    private static final Logger log = LoggerFactory.getLogger(${className}.class);

    public ${className} (ConnectionFactory connectionFactory,boolean changeble) {
        super();
        this.changeble = changeble;
        this.connectionFactory = connectionFactory;
    }

    public static ${baseClassName} byId(ConnectionFactory connectionFactory,boolean changeble,UserData userData,CmdData cmdData,int id) throws DaoException {
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        ${baseClassName} ret = wrapper.byId(wrapperUser,connectionFactory,userData,cmdData,id);
        if (ret!=null && changeble)
            ret.setChangeble(changeble);
        return ret;
    }

    public static List<${baseClassName}> get(ConnectionFactory connectionFactory,boolean changeble,UserData userData,CmdData cmdData,List<QueryRestr> restr,List<QuerySort> sort,int offset,int count) throws DaoException {
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        List<${baseClassName}> retList = wrapper.get(wrapperUser,connectionFactory,userData,cmdData,restr,sort,offset,count);
        if (changeble) {
            for (${baseClassName} ret : retList) {
                ret.setChangeble(changeble);
            }
        }
        return retList;
    }

    public static int count(ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,List<QueryRestr> restr) throws DaoException {
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        return wrapper.count(wrapperUser,connectionFactory,userData,cmdData,restr);
    }

    <% aktElem.attribs.each { attrib -> if ( attrib.type == strListType ) { 
        def tmpPackage = "${baseClassName}".substring(0,"${baseClassName}".lastIndexOf('.'))
        def tmpClassName = "${className}".substring(5)+"_${attrib.getNameWithFirstLetterUpper()}"
        def tmpClass = "${tmpPackage}.${tmpClassName}"
       %>
    private void init${attrib.getNameWithFirstLetterUpper()}IdWithTxt(CmdData cmdData,UserData userData,String origValue) throws DaoException {
        if (origValue==null && ${attrib.getNameWithFirstLetterLower()}IdTxt == null) return;
        if (origValue!=null && ${attrib.getNameWithFirstLetterLower()}IdTxt != null && origValue.equals(${attrib.getNameWithFirstLetterLower()}IdTxt)) return;
        if (origValue!=null && ${attrib.getNameWithFirstLetterLower()}IdTxt == null) {
            ${attrib.getNameWithFirstLetterLower()}Id = null; 
            return;
        }
        SQLExecWrapper<${tmpClass}> wrapper = new SQLExecWrapper(log);
        List<QueryRestr> restr = new ArrayList();
        restr.add(new QueryRestr(${tmpClass}.ID_BEZ,RestrType.EQUAL,${attrib.getNameWithFirstLetterLower()}IdTxt));
        List<${tmpClass}> refList = wrapper.get(Jdbc_${tmpClassName}.wrapperUser,connectionFactory,userData,cmdData,restr,null,0,0);
        int refListSize = refList.size();
        if (refListSize==0) {
            Jdbc_${tmpClassName} newElem = new Jdbc_${tmpClassName}(connectionFactory,true);
            newElem.setBez(${attrib.getNameWithFirstLetterLower()}IdTxt);
            newElem.insert(cmdData, userData);
            ${attrib.getNameWithFirstLetterLower()}Id = newElem.getId();
        }
        else {
            ${attrib.getNameWithFirstLetterLower()}Id = refList.get(0).getId();
            if (refListSize>1) {
                log.error("${className}.init${attrib.getNameWithFirstLetterUpper()}IdWithTxt - viskey not unique: "+${attrib.getNameWithFirstLetterLower()}IdTxt);
            }
        }
    }
    <% } } %>

    <% aktElem.refs.each { ref -> if (ref.entity.hasVisKey()) { 
        def tmpPackage = "${baseClassName}".substring(0,"${baseClassName}".lastIndexOf('.'))
        def tmpClassName = "${ref.entity.getNameWithFirstLetterUpper()}"
        def tmpClass = "${tmpPackage}.${tmpClassName}"
        def upperVisKeyCol = ref.entity.getVisKey().name.toUpperCase()
    %>
    private void init${ref.getUpperCamelCaseName()}WithTxt(CmdData cmdData,UserData userData,String origValue) throws DaoException {
        if (origValue==null && ${ref.getLowerCamelCaseName()}Txt == null) return;
        if (origValue!=null && ${ref.getLowerCamelCaseName()}Txt != null && origValue.equals(${ref.getLowerCamelCaseName()}Txt)) return;
        if (origValue!=null && ${ref.getLowerCamelCaseName()}Txt == null) {
            ${ref.getLowerCamelCaseName()} = null; 
            return;
        }
        SQLExecWrapper<${tmpClass}> wrapper = new SQLExecWrapper(log);
        List<QueryRestr> restr = new ArrayList();
        restr.add(new QueryRestr(${tmpClass}.ID_${upperVisKeyCol},RestrType.EQUAL,${ref.getLowerCamelCaseName()}Txt));
        List<${tmpClass}> refList = wrapper.get(Jdbc_${tmpClassName}.wrapperUser,connectionFactory,userData,cmdData,restr,null,0,0);
        int refListSize = refList.size();
        if (refListSize==0) {
            ${ref.getLowerCamelCaseName()} = null;
        }
        else {
            ${ref.getLowerCamelCaseName()} = refList.get(0).getId();
            if (refListSize>1) {
                log.error(getClass().getName()+".${ref.getLowerCamelCaseName()} - viskey not unique: "+${ref.getLowerCamelCaseName()}Txt);
            }
        }
    }
    <% } } %>

    @Override
    public void insert(CmdData cmdData,UserData userData) throws DaoException {
        if (!changeble) throw new DaoException ("this object is not changeble");
    <% aktElem.attribs.each { attrib -> if ( attrib.type == strListType ) { %>
        init${attrib.getNameWithFirstLetterUpper()}IdWithTxt(cmdData,userData,null);
    <% } } %>
    <% aktElem.refs.each { ref -> if (ref.entity.hasVisKey()) { %>
        init${ref.getUpperCamelCaseName()}WithTxt(cmdData,userData,null);
    <% } } %>
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        this.id = wrapper.insert(wrapperUser,this,connectionFactory,userData,cmdData);
    }

    @Override
    public void delete(CmdData cmdData,UserData userData) throws DaoException {
        if (!changeble) throw new DaoException ("this object is not changeble");
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        wrapper.delete(wrapperUser,getId(),connectionFactory,userData,cmdData);
    }

    @Override
    public void update(CmdData cmdData,UserData userData) throws DaoException {
        if (!changeble) throw new DaoException ("this object is not changeble");
        if (!hasChanged()) return;
    <% aktElem.attribs.each { attrib -> if ( attrib.type == strListType ) { %>
        init${attrib.getNameWithFirstLetterUpper()}IdWithTxt(cmdData,userData,origState.get${attrib.getNameWithFirstLetterUpper()}IdTxt());
    <% } } %>
    <% aktElem.refs.each { ref -> if (ref.entity.hasVisKey()) { %>
        init${ref.getUpperCamelCaseName()}WithTxt(cmdData,userData,origState.get${ref.getUpperCamelCaseName()}Txt());
    <% } } %>
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        wrapper.update(wrapperUser,this,connectionFactory,userData,cmdData);
    }

    public final static ${className}_User wrapperUser = new ${className}_User();
}

class ${className}_User implements ISQLQueryWrapperUser<${baseClassName}>,
        ISQLInsWrapperUser<${baseClassName}>,ISQLUpdWrapperUser<${baseClassName}>, ISQLDelWrapperUser {
    @Override
    public String getSelectBaseSql() {
        String sql = StringConsts.SQL_SELECT_BASE + "${aktElem.id}.id AS ${aktElem.id}_a0";
    <% aktElem.attribs.each { attrib -> if ( attrib.type == strListType ) { %>
        sql += ",${aktElem.id}.${attrib.name}_id AS ${attrib.id}";
        sql += ",${attrib.id}.bez AS ${attrib.id}Txt";
    <% } else { %>
        sql += ",${aktElem.id}.${attrib.name} AS ${attrib.id}";
    <% } } %>
    <% aktElem.refs.each { ref -> %>
        sql += ",${aktElem.id}.${ref.name} AS ${ref.id}";
    <% if (ref.entity.hasVisKey()) { %>
        sql += ",${ref.id}.${ref.entity.getVisKey().name} AS ${ref.id}Txt";
    <% } } %>
        sql += " FROM ${model.shortName}_${aktElem.name} ${aktElem.id}"; 
    <% aktElem.attribs.each { attrib -> if ( attrib.type == strListType ) { %>
        sql += " LEFT OUTER JOIN ${model.shortName}_${aktElem.name}_${attrib.name} ${attrib.id} ON ${attrib.id}.id = ${aktElem.id}.${attrib.name}_id";
    <% } } %>
    <% aktElem.refs.each { ref -> if (ref.entity.hasVisKey()) { %>
        sql += " LEFT OUTER JOIN ${model.shortName}_${ref.entity.name} ${ref.id} ON ${ref.id}.id = ${aktElem.id}.${ref.name}";
    <% } } %>
        return sql;
    }
    
    @Override
    public String appendFilterToSql(String sql,List<QueryRestr> restr) throws DaoException {
        return null; // TODO
    }

    @Override
    public String appendPagingToSql(String sql,int offset,int count) {
        if (offset==0 && count==0)
            return sql;
        return null; // TODO
    }

    @Override
    public String addCountToSql(String sql) {
        return StringConsts.SQL_COUNT_PART_1+sql+StringConsts.SQL_COUNT_PART_2;
    }

    @Override
    public void setFilterValues(PreparedStatement ps, List<QueryRestr> restr) throws SQLException {
        // TODO
    }

    @Override
    public ${baseClassName} initFromResultSet(ResultSet rs) throws SQLException {
        ${baseClassName} ret = new ${baseClassName}();        
        int i=1;
        ret.setId(rs.getInt(i));
    <% aktElem.attribs.each { attrib -> if ( attrib.type == strListType ) 
    { %>    i++;
        ret.set${attrib.getNameWithFirstLetterUpper()}Id(rs.getInt(i));
        i++;
        ret.set${attrib.getNameWithFirstLetterUpper()}IdTxt(rs.getString(i));
    <% } else 
    { %>    i++;
        ret.set${attrib.getNameWithFirstLetterUpper()}(rs.getObject(i,${typeConvert(attrib.type)}.class));
    <% } } %>
    <% aktElem.refs.each { ref -> 
    %>    i++;
        ret.set${ref.getUpperCamelCaseName()}(rs.getInt(i));
        <% if (ref.entity.hasVisKey())
        { %>i++;
        ret.set${ref.getUpperCamelCaseName()}Txt(rs.getString(i));
    <% } } %>
        return ret;
    }

    @Override
    public String getInsSql() {
        return INSERT_SQL;
    }

    @Override
    public void setInsValues(PreparedStatement ps,${baseClassName} data) throws SQLException {
        int i=0;
    <% aktElem.attribs.each { attrib -> if ( attrib.type == strListType ) 
    { %>    i++;
        ps.setObject(i,data.get${attrib.getNameWithFirstLetterUpper()}Id());
    <% } else 
    { %>    i++;
        ps.setObject(i,data.get${attrib.getNameWithFirstLetterUpper()}());
    <% } } %>
    <% aktElem.refs.each { ref -> 
    %>    i++;
        ps.setObject(i,data.get${ref.getUpperCamelCaseName()}());
    <% } %>
    }

    @Override
    public String getUpdSql(${baseClassName} data) {
        String colPart=null;
    <% aktElem.attribs.each { attrib -> if ( attrib.type == strListType ) 
    { %>if (SQLExecWrapper.isChanged(data.getOrigState().get${attrib.getNameWithFirstLetterUpper()}Id(),data.get${attrib.getNameWithFirstLetterUpper()}Id())) {
            if (colPart!=null) colPart+=",";
            colPart+="${attrib.getNameWithFirstLetterLower()}Id=?";
        }
    <% } else 
    { %>if (SQLExecWrapper.isChanged(data.getOrigState().get${attrib.getNameWithFirstLetterUpper()}(),data.get${attrib.getNameWithFirstLetterUpper()}())) {
            if (colPart!=null) colPart+=",";
            colPart+="${attrib.getNameWithFirstLetterLower()}=?";
        }
    <% } } %>
    <% aktElem.refs.each { ref -> 
    %>if (SQLExecWrapper.isChanged(data.getOrigState().get${ref.getUpperCamelCaseName()}(),data.get${ref.getUpperCamelCaseName()}())) {
            if (colPart!=null) colPart+=",";
            colPart+="${ref.getLowerCamelCaseName()}=?";
        }
    <% } %>

        return UPDATE_SQL_BASE+colPart+StringConsts.WHERE_ID_SQL;
    }

    @Override
    public void setUpdValues(PreparedStatement ps,${baseClassName} data) throws SQLException {
        int i=0;
    <% aktElem.attribs.each { attrib -> if ( attrib.type == strListType ) 
    { %>    
        if (SQLExecWrapper.isChanged(data.getOrigState().get${attrib.getNameWithFirstLetterUpper()}Id(),data.get${attrib.getNameWithFirstLetterUpper()}Id())) {
            i++;
            ps.setObject(i,data.get${attrib.getNameWithFirstLetterUpper()}Id());
        }
    <% } else 
    { %>

        if (SQLExecWrapper.isChanged(data.getOrigState().get${attrib.getNameWithFirstLetterUpper()}(),data.get${attrib.getNameWithFirstLetterUpper()}())) {
            i++;
            ps.setObject(i,data.get${attrib.getNameWithFirstLetterUpper()}());
        }
    <% } } %>
    <% aktElem.refs.each { ref -> 
    %>
        if (SQLExecWrapper.isChanged(data.getOrigState().get${ref.getUpperCamelCaseName()}(),data.get${ref.getUpperCamelCaseName()}())) {
            i++;
            ps.setObject(i,data.get${ref.getUpperCamelCaseName()}());
        }
    <% } %>
    }

    @Override
    public String getDelSql() {
        return DEL_SQL;
    }

    private static String getInsColumnList() {
        return <% aktElem.attribs.each { attrib -> 
            def colName =  attrib.type == strListType ? "${attrib.name.toLowerCase()}_id" : "${attrib.name.toLowerCase()}"
            if ( attrib != aktElem.attribs.last()) { 
        %> "${colName}," +
        <% } else { %> "${colName}"<% } }%>
        <% aktElem.refs.each { ref -> %> 
            + ",${ref.name.toLowerCase()}"<% } 
        %>;
    }

    private static String getInsParameterStr() {
        return "<% aktElem.attribs.each { attrib -> if ( attrib != aktElem.attribs.last()) { 
        %>?,<% } else { %>?<% }} %><% aktElem.refs.each { ref -> %>,?<% } %>";
    }

    @Override
    public ${className} create(ConnectionFactory connectionFactory,boolean changeble) {
        return new ${className}(connectionFactory,changeble);
    }

    static {
        UPDATE_SQL_BASE="UPDATE ${model.shortName}_${baseClassName.substring(baseClassName.lastIndexOf('.')+1)} SET ";
        INSERT_SQL = "INSERT INTO ${model.shortName}_${baseClassName.substring(baseClassName.lastIndexOf('.')+1)} (" +
            getInsColumnList() + ") VALUES (" + getInsParameterStr() +")";
    }

    private final static String UPDATE_SQL_BASE;
    private final static String INSERT_SQL;
    private final static String DEL_SQL="DELETE FROM ${model.shortName}_${baseClassName.substring(baseClassName.lastIndexOf('.')+1)} WHERE id=?";
}
'''    

    def templateListBean='''
package ${destPackage};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.JdbcBeanGenerator
 */

import de.othsoft.codeGen.requirements.jdbc.utils.ISQLQueryWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.ISQLInsWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.ISQLUpdWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.ISQLDelWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.SQLExecWrapper;
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.QueryRestr;
import de.othsoft.codeGen.requirements.QuerySort;
import de.othsoft.codeGen.requirements.UserData;
import de.othsoft.codeGen.requirements.CmdData;
import de.othsoft.codeGen.requirements.jdbc.JdbcCmdData;
import de.othsoft.codeGen.requirements.jdbc.utils.StringConsts;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ${className} extends ${baseClassName} {
    ConnectionFactory connectionFactory;
    private static final Logger log = LoggerFactory.getLogger(${className}.class);

    public ${className} (ConnectionFactory connectionFactory,boolean changeble) {
        super();
        this.changeble = changeble;
        this.connectionFactory = connectionFactory;
    }

    public static ${baseClassName} byId(ConnectionFactory connectionFactory,boolean changeble,UserData userData,CmdData cmdData,int id) throws DaoException {
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        ${baseClassName} ret = wrapper.byId(wrapperUser,connectionFactory,userData,cmdData,id);
        if (ret!=null && changeble)
            ret.setChangeble(changeble);
        return ret;
    }

    public static List<${baseClassName}> get(ConnectionFactory connectionFactory,boolean changeble,UserData userData,CmdData cmdData,List<QueryRestr> restr,List<QuerySort> sort,int offset,int count) throws DaoException {
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        List<${baseClassName}> retList = wrapper.get(wrapperUser,connectionFactory,userData,cmdData,restr,sort,offset,count);
        if (changeble) {
            for (${baseClassName} ret : retList) {
                ret.setChangeble(changeble);
            }
        }
        return retList;
    }

    public static int count(ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,List<QueryRestr> restr) throws DaoException {
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        return wrapper.count(wrapperUser,connectionFactory,userData,cmdData,restr);
    }

    @Override
    public void insert(CmdData cmdData,UserData userData) throws DaoException {
        if (!changeble) throw new DaoException ("this object is not changeble");
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        this.id = wrapper.insert(wrapperUser,this,connectionFactory,userData,cmdData);
    }

    @Override
    public void delete(CmdData cmdData,UserData userData) throws DaoException {
        if (!changeble) throw new DaoException ("this object is not changeble");
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        wrapper.delete(wrapperUser,getId(),connectionFactory,userData,cmdData);
    }

    @Override
    public void update(CmdData cmdData,UserData userData) throws DaoException {
        if (!changeble) throw new DaoException ("this object is not changeble");
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        wrapper.update(wrapperUser,this,connectionFactory,userData,cmdData);
    }

    public final static ${className}_User wrapperUser = new ${className}_User();
}

class ${className}_User implements ISQLQueryWrapperUser<${baseClassName}>,
        ISQLInsWrapperUser<${baseClassName}>,ISQLUpdWrapperUser<${baseClassName}>, ISQLDelWrapperUser {
    @Override
    public String getSelectBaseSql() {
        return null; // TODO
    }
    
    @Override
    public String appendFilterToSql(String sql,List<QueryRestr> restr) throws DaoException {
        return null; // TODO
    }

    @Override
    public String appendPagingToSql(String sql,int offset,int count) {
        if (offset==0 && count==0)
            return sql;
        return null; // TODO
    }

    @Override
    public String addCountToSql(String sql) {
        return StringConsts.SQL_COUNT_PART_1+sql+StringConsts.SQL_COUNT_PART_2;
    }

    @Override
    public void setFilterValues(PreparedStatement ps, List<QueryRestr> restr) throws SQLException {
        // TODO
    }
    
    @Override
    public ${baseClassName} initFromResultSet(ResultSet rs) {
        return null; // TODO
    }

    @Override
    public String getInsSql() {
        return null;
    }

    @Override
    public void setInsValues(PreparedStatement ps,${baseClassName} data) throws SQLException {
        // TODO
    }

    @Override
    public String getUpdSql(${baseClassName} data) {
        return null; // TODO
    }

    @Override
    public void setUpdValues(PreparedStatement ps,${baseClassName} data) throws SQLException {
        // TODO
    }

    @Override
    public ${className} create(ConnectionFactory connectionFactory,boolean changeble) {
        return new ${className}(connectionFactory,changeble);
    }

    @Override
    public String getDelSql() {
        return DEL_SQL;
    }

    private final static String DEL_SQL="DELETE FROM ${model.shortName}_${baseClassName.substring(baseClassName.lastIndexOf('.')+1)} WHERE id=?";
}
'''    

    def templateViewBean='''
package ${destPackage};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.JdbcBeanGenerator
 */

import de.othsoft.codeGen.requirements.jdbc.utils.ISQLQueryWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.ISQLInsWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.ISQLUpdWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.ISQLDelWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.SQLExecWrapper;
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.QueryRestr;
import de.othsoft.codeGen.requirements.QuerySort;
import de.othsoft.codeGen.requirements.UserData;
import de.othsoft.codeGen.requirements.CmdData;
import de.othsoft.codeGen.requirements.jdbc.JdbcCmdData;
import de.othsoft.codeGen.requirements.jdbc.utils.StringConsts;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ${className} extends ${baseClassName} {
    ConnectionFactory connectionFactory;
    private static final Logger log = LoggerFactory.getLogger(${className}.class);

    public ${className} (ConnectionFactory connectionFactory) {
        super();
        this.connectionFactory = connectionFactory;
    }

    public static ${baseClassName} byId(ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,int id) throws DaoException {
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        return wrapper.byId(wrapperUser,connectionFactory,userData,cmdData,id);
    }

    public static List<${baseClassName}> get(ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,List<QueryRestr> restr,List<QuerySort> sort,int offset,int count) throws DaoException {
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        return wrapper.get(wrapperUser,connectionFactory,userData,cmdData,restr,sort,offset,count);
    }

    public static int count(ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,List<QueryRestr> restr) throws DaoException {
        SQLExecWrapper<${baseClassName}> wrapper = new SQLExecWrapper(log);
        return wrapper.count(wrapperUser,connectionFactory,userData,cmdData,restr);
    }

    private final static ${className}_User wrapperUser = new ${className}_User();
}

class ${className}_User implements ISQLQueryWrapperUser<${baseClassName}> {
    @Override
    public String getSelectBaseSql() {
        return null; // TODO
    }
    
    @Override
    public String appendFilterToSql(String sql,List<QueryRestr> restr) throws DaoException {
        return null; // TODO
    }

    @Override
    public String appendPagingToSql(String sql,int offset,int count) {
        if (offset==0 && count==0)
            return sql;
        return null; // TODO
    }

    @Override
    public String addCountToSql(String sql) {
        return StringConsts.SQL_COUNT_PART_1+sql+StringConsts.SQL_COUNT_PART_2;
    }

    @Override
    public void setFilterValues(PreparedStatement ps, List<QueryRestr> restr) throws SQLException {
        // TODO
    }
    
    @Override
    public ${baseClassName} initFromResultSet(ResultSet rs) {
        return null; // TODO
    }
}
'''    

    def templateM2NBean='''
package ${destPackage};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.JdbcBeanGenerator
 */

import de.othsoft.codeGen.requirements.jdbc.utils.ISQLM2NWrapperUser;
import de.othsoft.codeGen.requirements.jdbc.utils.SQLExecM2NWrapper;
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.UserData;
import de.othsoft.codeGen.requirements.CmdData;
import de.othsoft.codeGen.requirements.jdbc.JdbcCmdData;
import de.othsoft.codeGen.requirements.jdbc.utils.StringConsts;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ${className} extends ${baseClassName} {
    ConnectionFactory connectionFactory;
    private static final Logger log = LoggerFactory.getLogger(${className}.class);

    public ${className} (ConnectionFactory connectionFactory) {
        super();
        this.connectionFactory = connectionFactory;
    }

    public static List<${baseClassName}> by${aktElem.ref1.refName}(ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,int id) throws DaoException {
        SQLExecM2NWrapper<${baseClassName}> wrapper = new SQLExecM2NWrapper(log);
        return wrapper.byRef1(wrapperUser,connectionFactory,userData,cmdData,id);
    }

    public static int countBy${aktElem.ref1.refName}(ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,int id) throws DaoException {
        SQLExecM2NWrapper<${baseClassName}> wrapper = new SQLExecM2NWrapper(log);
        return wrapper.countByRef1(wrapperUser,connectionFactory,userData,cmdData,id);
    }
    public static List<${baseClassName}> by${aktElem.ref2.refName}(ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,int id) throws DaoException {
        SQLExecM2NWrapper<${baseClassName}> wrapper = new SQLExecM2NWrapper(log);
        return wrapper.byRef2(wrapperUser,connectionFactory,userData,cmdData,id);
    }

    public static int countBy${aktElem.ref2.refName}(ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,int id) throws DaoException {
        SQLExecM2NWrapper<${baseClassName}> wrapper = new SQLExecM2NWrapper(log);
        return wrapper.countByRef1(wrapperUser,connectionFactory,userData,cmdData,id);
    }

    public static ${baseClassName} byIds(ConnectionFactory connectionFactory,UserData userData,CmdData cmdData,int id${aktElem.ref1.refName},int id${aktElem.ref2.refName}) throws DaoException {
        SQLExecM2NWrapper<${baseClassName}> wrapper = new SQLExecM2NWrapper(log);
        return wrapper.byIds(wrapperUser,connectionFactory,userData,cmdData,id${aktElem.ref1.refName},id${aktElem.ref2.refName});
    }

    @Override
    public void insert(CmdData cmdData,UserData userData) throws DaoException {
        SQLExecM2NWrapper<${baseClassName}> wrapper = new SQLExecM2NWrapper(log);
        wrapper.insert(wrapperUser,this,connectionFactory,userData,cmdData);
    }

    @Override
    public void delete(CmdData cmdData,UserData userData) throws DaoException {
        SQLExecM2NWrapper<${baseClassName}> wrapper = new SQLExecM2NWrapper(log);
        wrapper.delete(wrapperUser,get${aktElem.ref1.getUpperCamelCaseName()}(),get${aktElem.ref2.getUpperCamelCaseName()}(),connectionFactory,userData,cmdData);
    }    

    private final static ${className}_User wrapperUser = new ${className}_User();
}

class ${className}_User implements ISQLM2NWrapperUser<${baseClassName}> {
    @Override
    public String getSelectSqlRef1() {
        return null; // TODO
    }

    @Override
    public String getSelectSqlRef2() {
        return null; // TODO
    }

    @Override
    public String getSelectByIdsSql() {
        return null; // TODO
    }

    @Override
    public String addCountToSql(String sql) {
        return null; // TODO
    }

    @Override
    public String getInsSql() {
        return null; // TODO
    }

    @Override
    public void setInsValues(PreparedStatement ps,${baseClassName} data) throws SQLException {
        // TODO
    }

    @Override
    public String getDelSql() {
        return null; // TODO
    }

    @Override
    public ${baseClassName} initFromResultSet(ResultSet rs) {
        return null; // TODO
    }
}
'''    
}


