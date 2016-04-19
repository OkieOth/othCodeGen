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

/**
 * this generator creates a puml class diagramm that shows the relations in the
 * datamodel
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
 */
class DataModelPumlGenerator {
    private final static String defDestPath='src/doc'
    
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
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(templateClassDiagramm)
        def daten = [
            model:model,
            strListType:AttribType.t_str_list]
        def ergebnis = template.make(daten)

        String destPath=params.destPathRoot
        if (!destPath.endsWith(File.separator))
        destPath+=File.separator
        FileHelper.createDirIfNotExists(destPath)

        File file=new File("${destPath}DataModel_${model.shortName}.puml")        
        //file.write(removeEmptyLines(ergebnis.toString()))
        file.write(ergebnis.toString())
    }
    
    void genTestCode(DataModel model,Map params) {
        // TODO
    }

    void genTestCode(DataModel model) {
        // TODO
    }

    def templateClassDiagramm='''
@startuml
skinparam class {
	BackgroundColor #ffffff
        BackgroundColor<<list>> #edfffd
        BackgroundColor<<view>> #e7fbe5
        BackgroundColor<<m2n>> #fffeed
}

<% model.entities.each { entity -> %>
class ${entity.value.name} <<entity>> {
    <% entity.value.attribs.each { attrib -> 
    %> $attrib.type $attrib.name
    <% } %>
    <% entity.value.refs.each { ref -> 
    %>t_key $ref.name
    <% if (ref.entity.hasVisKey()) {
    %>t_string ${ref.name}_txt";
    <% } } %>

}

<% entity.value.refs.each { ref -> %>
${entity.value.name} -- ${ref.entity.getNameWithFirstLetterUpper()}
<% } } %>

<% model.entities*.value.attribs*.findAll { it.type == strListType }*.each { attrib -> %>
class ${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()} <<list>> {

}
${attrib.parent.getNameWithFirstLetterUpper()} -- class ${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}
<% } %>

<% model.views.each { view -> %>
class ${view.value.name} <<view>> {
    <% view.value.attribs.each { attrib ->
    %> $attrib.type $attrib.name
    <% } %>

}<% } %>

<% model.m2nRelations.each { m2n -> %>
class ${m2n.value.name} <<m2n>> {
t_key ${m2n.value.ref1.name}
t_key ${m2n.value.ref2.name}
}

${m2n.value.ref1.entity.name} -- ${m2n.value.name}
${m2n.value.name} -- ${m2n.value.ref2.entity.name}
<% } %>

@enduml
    '''
}

