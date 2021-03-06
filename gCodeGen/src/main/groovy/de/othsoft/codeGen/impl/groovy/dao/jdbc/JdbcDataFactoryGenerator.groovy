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

import de.othsoft.codeGen.impl.java.JavaBeanGeneratorBase
import de.othsoft.codeGen.types.DataModel
import de.othsoft.codeGen.types.ICodeGenImpl
import groovy.text.SimpleTemplateEngine
import de.othsoft.codeGen.impl.helper.FileHelper

import de.othsoft.codeGen.requirements.AttribType

/**
 *
 * @author hulk
 */
class JdbcDataFactoryGenerator implements ICodeGenImpl {
    private final static String defDestPath='src/main/java'
    
    private String buildDefPackageName (DataModel model) {
        return "de.gCodeGen.dao.${model.shortName}"
    }

    void genCode(DataModel model) {
        def params = [packageName:buildDefPackageName(model),
            destPathRoot:defDestPath]
        genCodeNow(model,params)
    }

    void genCode(DataModel model,Map params) {
        if (!params.packageName)
            params.packageName = buildDefPackageName(model)
        if (!params.destPathRoot)
            params.destPathRoot = defDestPath 
        genCodeNow(model,params)
    }

    void genCodeNow(DataModel model,Map params) {
        String packageName=addGenPackageName(params.packageName)
        String beanPackage=params.packageName+".beans"
        String interfPackage=params.packageName+".dao.interf"
        String destPathRoot=params.destPathRoot
        if (!destPathRoot.endsWith(File.separator))
            destPathRoot+=File.separator
        String packagePath=FileHelper.packageToDirName(packageName)
        String destPath = destPathRoot + packagePath
        if (!destPath.endsWith(File.separator))
        destPath+=File.separator
        FileHelper.createDirIfNotExists(destPath)
        
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(templateFactory)
        def daten = [
            model:model,
            strListType:AttribType.t_str_list,
            packageName:packageName,
            interfPackage:interfPackage,
            beanPackage:beanPackage]
        def ergebnis = template.make(daten)

        File file=new File("${destPath}DataFactory_${model.shortName}.java")        
        //file.write(removeEmptyLines(ergebnis.toString()))
        file.write(ergebnis.toString())

    }

    void genTestCode(DataModel model,Map params) {
        // TODO
    }

    void genTestCode(DataModel model) {
        // TODO
    }

    private String addGenPackageName(String packageName) {
        if (!packageName)
            return 'dao.jdbc'
        else 
            return "${packageName}.dao.jdbc"
    }

    
    def templateFactory='''package $packageName;

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.JdbcDataFactoryGenerator
 */

import ${beanPackage}.*;
import ${interfPackage}.IJdbcDataFactory_${model.shortName};
import de.othsoft.codeGen.requirements.CmdData;
import de.othsoft.codeGen.requirements.jdbc.JdbcCmdData;
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.QueryRestr;
import de.othsoft.codeGen.requirements.QuerySort;
import de.othsoft.codeGen.requirements.UserData;
import de.othsoft.codeGen.requirements.jdbc.utils.ISetPagingImpl;
import de.othsoft.codeGen.requirements.jdbc.utils.ISetFilterValuesImpl;
import de.othsoft.codeGen.requirements.jdbc.utils.IJdbcDataFactoryBase;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataFactory_${model.shortName} implements IJdbcDataFactory_${model.shortName}, IJdbcDataFactoryBase {
<% model.entities.each { entity -> %>
    @Override
    public ${entity.value.name} create_${entity.value.name}() {
        return new ${packageName}.beans.Jdbc_${entity.value.name} (this);
    }

    @Override
    public ${entity.value.name} byId_${entity.value.name}(UserData userData,CmdData cmdData,int id) throws DaoException {
        return ${packageName}.beans.Jdbc_${entity.value.name}.byId(this,userData,cmdData,id);
    }

    @Override
    public List<${entity.value.name}> get_${entity.value.name}(UserData userData,CmdData cmdData,List<QueryRestr> restr,List<QuerySort> sort,int offset,int count) throws DaoException {
        return ${packageName}.beans.Jdbc_${entity.value.name}.get(this,userData,cmdData,restr,sort,offset,count);
    }

    @Override
    public int count_${entity.value.name}(UserData userData,CmdData cmdData,List<QueryRestr> restr) throws DaoException {
        return ${packageName}.beans.Jdbc_${entity.value.name}.count(this,userData,cmdData,restr);
    }
<% } %>

<% model.entities*.value.attribs*.findAll { it.type == strListType }*.each { attrib -> %>
    @Override
    public ${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()} create_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}() {
        return new ${packageName}.beans.Jdbc_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()} (this);
    }

    @Override
    public ${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()} byId_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}(UserData userData,CmdData cmdData,int id) throws DaoException {
        return ${packageName}.beans.Jdbc_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}.byId(this,userData,cmdData,id);
    }

    @Override
    public List<${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}> get_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}(UserData userData,CmdData cmdData,List<QueryRestr> restr,List<QuerySort> sort,int offset,int count) throws DaoException {
        return ${packageName}.beans.Jdbc_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}.get(this,userData,cmdData,restr,sort,offset,count);
    }

    @Override
    public int count_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}(UserData userData,CmdData cmdData,List<QueryRestr> restr) throws DaoException {
        return ${packageName}.beans.Jdbc_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}.count(this,userData,cmdData,restr);
    }
<% } %>

<% model.views.each { view -> %>
    @Override
    public ${view.value.name} create_${view.value.name}() {
        return new ${packageName}.beans.Jdbc_${view.value.name} (this);
    }

    @Override
    public ${view.value.name} byId_${view.value.name}(UserData userData,CmdData cmdData,int id) throws DaoException {
        return ${packageName}.beans.Jdbc_${view.value.name}.byId(this,userData,cmdData,id);
    }

    @Override
    public List<${view.value.name}> get_${view.value.name}(UserData userData,CmdData cmdData,List<QueryRestr> restr,List<QuerySort> sort,int offset,int count) throws DaoException {
        return ${packageName}.beans.Jdbc_${view.value.name}.get(this,userData,cmdData,restr,sort,offset,count);
    }

    @Override
    public int count_${view.value.name}(UserData userData,CmdData cmdData,List<QueryRestr> restr) throws DaoException {
        return ${packageName}.beans.Jdbc_${view.value.name}.count(this,userData,cmdData,restr);
    }
<% } %>

<% model.m2nRelations.each { m2n -> %>
    @Override
    public ${m2n.value.name} create_${m2n.value.name}() {
        return new ${packageName}.beans.Jdbc_${m2n.value.name} (this);
    }

    @Override
    public List<${m2n.value.name}> get_${m2n.value.name}_by${m2n.value.ref1.refName}(UserData userData,CmdData cmdData,int refId) throws DaoException {
        return ${packageName}.beans.Jdbc_${m2n.value.name}.by${m2n.value.ref1.refName}(this,userData,cmdData,refId);
    }

    @Override
    public int count_${m2n.value.name}_by${m2n.value.ref1.refName}(UserData userData,CmdData cmdData,int refId) throws DaoException {
        return ${packageName}.beans.Jdbc_${m2n.value.name}.countBy${m2n.value.ref1.refName}(this,userData,cmdData,refId);
    }

    @Override
    public List<${m2n.value.name}> get_${m2n.value.name}_by${m2n.value.ref2.refName}(UserData userData,CmdData cmdData,int refId) throws DaoException {
        return ${packageName}.beans.Jdbc_${m2n.value.name}.by${m2n.value.ref2.refName}(this,userData,cmdData,refId);
    }

    @Override
    public int count_${m2n.value.name}_by${m2n.value.ref2.refName}(UserData userData,CmdData cmdData,int refId) throws DaoException {
        return ${packageName}.beans.Jdbc_${m2n.value.name}.countBy${m2n.value.ref1.refName}(this,userData,cmdData,refId);
    }

    @Override
    public ${m2n.value.name} byIds_${m2n.value.name}(UserData userData,CmdData cmdData,int id${m2n.value.ref1.refName},int id${m2n.value.ref2.refName}) throws DaoException {
        return ${packageName}.beans.Jdbc_${m2n.value.name}.byIds(this,userData,cmdData,id${m2n.value.ref1.refName},id${m2n.value.ref2.refName});
    }
<% } %>

    @Override
    public CmdData startTransaction() throws DaoException {
        Connection con=connectionFactory.getCon();
        return new JdbcCmdData(con);
    }

    @Override
    public int getVersion() {
        return ${model.version};
    }

    @Override
    public void setFilterValues(PreparedStatement ps, List<QueryRestr> restr) throws SQLException, DaoException {
        setFilterValuesImpl.setFilterValues(ps,restr);
    }
    
    public ISetFilterValuesImpl getSetFilterValuesImpl() { return setFilterValuesImpl; }
    public void setSetFilterValuesImpl(ISetFilterValuesImpl setFilterValuesImpl) { this.setFilterValuesImpl = setFilterValuesImpl; }

    public void setConnectionFactory (ConnectionFactory connectionFactory) { this.connectionFactory = connectionFactory; }
    public ConnectionFactory getConnectionFactory () { return this.connectionFactory; }

    public void setSetPagingImpl(ISetPagingImpl setPagingImpl) { this.setPagingImpl = setPagingImpl; }
    public ISetPagingImpl getSetPagingImpl() { return this.setPagingImpl; }

    private ISetFilterValuesImpl setFilterValuesImpl;
    private ISetPagingImpl setPagingImpl;
    private ConnectionFactory connectionFactory;
    private static final Logger log = LoggerFactory.getLogger(DataFactory_${model.shortName}.class);

}
'''
}

