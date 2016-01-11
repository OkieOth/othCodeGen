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

package de.othsoft.codeGen.impl.groovy.sql.psql

import de.othsoft.codeGen.types.CodeGenException
import de.othsoft.codeGen.types.DataModel
import de.othsoft.codeGen.types.AttribType
import de.othsoft.codeGen.types.ICodeGenImpl
import de.othsoft.codeGen.impl.helper.FileHelper
import groovy.text.SimpleTemplateEngine

/**}
 * @author hulk
 */
class CreateDatabaseSqlGenerator implements ICodeGenImpl {
    void genCode(DataModel model,Map params) {
        String destPath=params.destPathRoot
        if (!destPath.endsWith(File.separator))
            destPath+=File.separator
        createCreateSql(destPath,model)
        createUpdateSql(destPath,model)
    }
    
    void genTestCode(DataModel model,Map params) {
        // TODO
    }

    void createCreateSql(String destPath,DataModel model) {
        File destDir = new File(destPath)
        if (!destDir.exists())
            destDir.mkdirs()
            
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(templateCreateTable)
        def daten = [
            model:model,
            typeConvert:typeConvert,
            strListType:AttribType.t_str_list]
        def ergebnis = template.make(daten)
        
        File file=new File(destPath+'createDb.sql')        
        file.write(removeEmptyLines(ergebnis.toString()))
    }
    

    void createUpdateSql(String destPath,DataModel model) {
        if (model.version==1) return
        File updDir = new File(destPath+'updates')
        if (!updDir.exists())
            updDir.mkdirs()
            
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(templateUpdateDb)
        def daten = [
            model:model,
            typeConvert:typeConvert,
            strListType:AttribType.t_str_list,
            neededVersion:model.version]
        def ergebnis = template.make(daten)

        String updFilePath=updDir.getCanonicalPath()
        if (!updFilePath.endsWith(File.separator))
            updFilePath+=File.separator
        
        File file=new File(updFilePath+"upd_${model.version}.sql")
        file.write( removeEmptyLines (ergebnis.toString()) )
    }
    
    String removeEmptyLines (String genResult) {
        String s=genResult.replaceAll(/\n\s*\n/,'\n')
        return s.replaceAll(/;\s*\n/,';\n\n')
    }
     
    public def typeConvert = { type ->
        switch (type) {
        case AttribType.t_int : return 'integer'
        case AttribType.t_long : return 'long'
        case AttribType.t_string : return 'text'
        case AttribType.t_key : return 'integer'
        case AttribType.t_boolean : return 'boolean'
        case AttribType.t_date : return 'date'
        case AttribType.t_timestamp : return 'timestamp'
        case AttribType.t_geo : return '???'
        case AttribType.t_money : return 'money'
        case AttribType.t_meters : return 'numeric(8,2)'
        case AttribType.t_milimeters : return 'numeric(8,2)'
        case AttribType.t_kilometers : return 'numeric(8,3)'
        case AttribType.t_kmh : return 'numeric(4,2)'
        case AttribType.t_prozent : return 'numeric(4,2)'
        case AttribType.t_hour : return 'integer'
        case AttribType.t_min : return 'integer'
        case AttribType.t_sec : return 'integer'
        case AttribType.t_time : return '???'
        case AttribType.t_volt : return 'numeric(4,1)'
        case AttribType.t_float : return 'numeric(10,3)'
        case AttribType.t_str_list : return 'integer'
        default:
            return '???'
        }
    }
        
    def templateUpdateDb='''
/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.sql.psql.CreateDatabaseSqlGenerator
 */

<% model.entities*.value.attribs*.findAll { it.since == neededVersion }*.each { attrib -> %>
ALTER TABLE ${model.shortName}_${attrib.parent.name} ADD COLUMN ${attrib.name}  ${typeConvert(attrib.type)};
<% } %>

<% model.entities*.value.refs*.findAll { it.since == neededVersion }*.each { ref -> %>
ALTER TABLE ${model.shortName}_${ref.parent.name} ADD COLUMN ${ref.entity.getNameWithFirstLetterLower()}_id integer;
<% } %>

<% model.entities*.findAll { it.value.since==neededVersion }*.each { entity -> %>
<%if (entity.value.descr) { %>
/**
 * $entity.value.descr
 */<%}%>
CREATE TABLE IF NOT EXISTS ${model.shortName}_${entity.value.name} ( 
    id SERIAL 
    <% entity.value.attribs.each { attrib -> %>
    <% if ( attrib.type == strListType ) { %> 
    ,${attrib.name}_id ${typeConvert(attrib.type)} 
    <% } else { %> 
    ,${attrib.name} ${typeConvert(attrib.type)} 
    <% } %>
    <% } %>
    <% entity.value.refs.each { ref -> %>
    ,${ref.name} integer 
    <% } %>
    ,CONSTRAINT pk_${model.shortName}_${entity.value.name.toLowerCase()} PRIMARY KEY (id));
    <% entity.value.attribs*.findAll { it.type == strListType }*.each { attrib -> %>

CREATE TABLE IF NOT EXISTS ${model.shortName}_${attrib.parent.name}_${attrib.getNameWithFirstLetterUpper()} (
    id SERIAL
    , bez text
    , lang text
    , aktiv boolean
    , reihenf integer
    ,CONSTRAINT pk_${model.shortName}_${attrib.parent.name.toLowerCase()}_${attrib.name} PRIMARY KEY (id));
    <% } %>
<% } %>

<% model.m2nRelations*.findAll { it.value.since==neededVersion }*.each { m2n -> %>
<%if (m2n.value.descr) { %>
/**
 * $m2n.value.descr
 */
<%}%>
CREATE TABLE IF NOT EXISTS ${model.shortName}_${m2n.value.name} (
    ${m2n.value.ref1.name} integer
    ,${m2n.value.ref2.name} integer);
<% } %>
UPDATE ${model.shortName}_Version SET versionsnummer=${neededVersion},datum=current_timestamp;
'''

    
    def templateCreateTable='''
/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.sql.psql.CreateDatabaseSqlGenerator
 */

CREATE TABLE IF NOT EXISTS ${model.shortName}_Version (
    versionsnummer int NOT NULL,
    datum   timestamp);

INSERT INTO ${model.shortName}_Version (versionsnummer,datum) VALUES (${model.version},current_timestamp);

<% model.entities*.value.attribs*.findAll { it.type == strListType }*.each { attrib -> %>
CREATE TABLE IF NOT EXISTS ${model.shortName}_${attrib.parent.name}_${attrib.getNameWithFirstLetterUpper()} (
    id SERIAL
    , bez text
    , lang text
    , aktiv boolean
    , reihenf integer
    ,CONSTRAINT pk_${model.shortName}_${attrib.parent.name.toLowerCase()}_${attrib.name} PRIMARY KEY (id));
<% } %>
<% model.entities.each { entity -> %>
<%if (entity.value.descr) { %>
/**
 * $entity.value.descr
 */
<%}%>
CREATE TABLE IF NOT EXISTS ${model.shortName}_${entity.value.name} ( 
    id SERIAL
<% entity.value.attribs.each { attrib -> %>
<% if ( attrib.type == strListType ) { %> 
    ,${attrib.name}_id ${typeConvert(attrib.type)} <% } else { %> 
    ,${attrib.name} ${typeConvert(attrib.type)} 
<% } %>
<% } %>
<% entity.value.refs.each { ref -> %>
    ,${ref.name} integer 
<% } %>
    ,CONSTRAINT pk_${model.shortName}_${entity.value.name.toLowerCase()} PRIMARY KEY (id));
<% } %>
<% model.m2nRelations.each { m2n -> %>
<%if (m2n.value.descr) { %>
/**
 * $m2n.value.descr
 */
<%}%>
CREATE TABLE IF NOT EXISTS ${model.shortName}_${m2n.value.name} (
    ${m2n.value.ref1.name} integer
    ,${m2n.value.ref2.name} integer);
<% } %>
<% model.views.each { view -> %>
<%if (view.value.descr) { %>
/**
 * $view.value.descr
 */
<%}%>
/*
 * CREATE OR REPLACE VIEW ${model.shortName}_${view.value.name} AS 
 * SELECT
 *  id ???
<% view.value.attribs.each { attrib ->%>
 *   ,${attrib.name} ???
<% } %>
 * FROM
 *   ??
 *;
 */
<% } %>
   '''
}


