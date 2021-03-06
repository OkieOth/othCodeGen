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

package de.othsoft.codeGen.impl.groovy.dao

import de.othsoft.codeGen.types.DataModel
import de.othsoft.codeGen.types.ICodeGenImpl
import groovy.text.SimpleTemplateEngine
import de.othsoft.codeGen.impl.helper.FileHelper

import de.othsoft.codeGen.requirements.AttribType

/**
 *
 * @author eiko
 * generates the interface for the produced data factory
 * @see de.othsoft.codeGen.tests.generators.FactoryInterfGenerator_Test
 */
class FactoryInterfGenerator implements ICodeGenImpl {
    private final static String defDestPath='src/main/java'
    
    private String buildDefPackageName (DataModel model) {
        return "de.gCodeGen.dao.${model.shortName}"
    }

    void genCode(DataModel model) {
        genCodeNow(model,buildDefPackageName(model),defDestPath)
    }

    void genCode(DataModel model,Map params) {
        String packageBaseName = params.packageName ? params.packageName : buildDefPackageName(model)
        String destRootPath = params.destPathRoot ? params.destPathRoot : defDestPath 
        genCodeNow(model,packageBaseName,destRootPath)
    }
        
    void genCodeNow(DataModel model,String packageBaseName,String destRootPath) {
        String packageName=addGenPackageName(packageBaseName)
        String beanPackage=packageBaseName+".beans"
        String destPathRoot=destRootPath
        if (!destPathRoot.endsWith(File.separator))
            destPathRoot+=File.separator
        String packagePath=FileHelper.packageToDirName(packageName)
        String destPath = destPathRoot + packagePath
        if (!destPath.endsWith(File.separator))
        destPath+=File.separator
        FileHelper.createDirIfNotExists(destPath)
        
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(templateFactoryInterf)
        def daten = [
            model:model,
            strListType:AttribType.t_str_list,
            packageName:packageName,
            beanPackage:beanPackage]
        def ergebnis = template.make(daten)

        File file=new File("${destPath}IDataFactory_${model.shortName}.java")        
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
            return 'dao.interf'
        else 
            return "${packageName}.dao.interf"
    }

    def templateFactoryInterf='''package $packageName;

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.FactoryInterfGenerator
 */

import ${beanPackage}.*;
import de.othsoft.codeGen.requirements.CmdData;
import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.QueryRestr;
import de.othsoft.codeGen.requirements.QuerySort;
import de.othsoft.codeGen.requirements.UserData;

import java.util.List;

public interface IDataFactory_${model.shortName} {
<% model.entities.each { entity -> %>
    ${entity.value.name} create_${entity.value.name}();
    ${entity.value.name} byId_${entity.value.name}(UserData userData,CmdData cmdData,int id) throws DaoException;
    List<${entity.value.name}> get_${entity.value.name}(UserData userData,CmdData cmdData,List<QueryRestr> restr,List<QuerySort> sort,int offset,int count) throws DaoException;
    int count_${entity.value.name}(UserData userData,CmdData cmdData,List<QueryRestr> restr) throws DaoException;
<% } %>

<% model.entities*.value.attribs*.findAll { it.type == strListType }*.each { attrib -> %>
    ${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()} create_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}();
    ${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()} byId_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}(UserData userData,CmdData cmdData,int id) throws DaoException;
    List<${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}> get_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}(UserData userData,CmdData cmdData,List<QueryRestr> restr,List<QuerySort> sort,int offset,int count) throws DaoException;
    int count_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}(UserData userData,CmdData cmdData,List<QueryRestr> restr) throws DaoException;
<% } %>

<% model.views.each { view -> %>
    ${view.value.name} create_${view.value.name}();
    ${view.value.name} byId_${view.value.name}(UserData userData,CmdData cmdData,int id) throws DaoException;
    List<${view.value.name}> get_${view.value.name}(UserData userData,CmdData cmdData,List<QueryRestr> restr,List<QuerySort> sort,int offset,int count) throws DaoException;
    int count_${view.value.name}(UserData userData,CmdData cmdData,List<QueryRestr> restr) throws DaoException;
<% } %>

<% model.m2nRelations.each { m2n -> %>
    ${m2n.value.name} create_${m2n.value.name}();
    List<${m2n.value.name}> get_${m2n.value.name}_by${m2n.value.ref1.refName}(UserData userData,CmdData cmdData,int refId) throws DaoException;
    int count_${m2n.value.name}_by${m2n.value.ref1.refName}(UserData userData,CmdData cmdData,int refId) throws DaoException;
    List<${m2n.value.name}> get_${m2n.value.name}_by${m2n.value.ref2.refName}(UserData userData,CmdData cmdData,int refId) throws DaoException;
    int count_${m2n.value.name}_by${m2n.value.ref2.refName}(UserData userData,CmdData cmdData,int refId) throws DaoException;
    ${m2n.value.name} byIds_${m2n.value.name}(UserData userData,CmdData cmdData,int id${m2n.value.ref1.refName},int id${m2n.value.ref2.refName}) throws DaoException;
<% } %>

    public int getVersion();

}
'''

}

