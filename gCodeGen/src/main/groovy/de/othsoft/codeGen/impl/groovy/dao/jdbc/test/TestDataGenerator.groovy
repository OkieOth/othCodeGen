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


package de.othsoft.codeGen.impl.groovy.dao.jdbc.test

import de.othsoft.codeGen.impl.java.JavaBeanGeneratorBase
import de.othsoft.codeGen.types.DataModel
import de.othsoft.codeGen.types.ICodeGenImpl
import de.othsoft.codeGen.impl.helper.FileHelper
import de.othsoft.codeGen.types.AttribType
import groovy.text.SimpleTemplateEngine

class TestDataGenerator extends JavaBeanGeneratorBase implements ICodeGenImpl {
    void genCode(DataModel model,Map params) {
        // not needed here
    }
    
    void genTestCode(DataModel model,Map params) {
        String packageName=addGenPackageName(params.packageName)
        String testPackageName=addTestPackageName(params.packageName)+""
        String destPath = getTestPath (params,testPackageName)
        def className = "TestData_${model.shortName}"
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(testDataTemplate)
        def daten = [
            typeConvert:typeConvert,
            packageName:packageName,
            testPackageName:testPackageName,
            model:model,
            strListType:AttribType.t_str_list,
            className:className]
        def ergebnis = template.make(daten)

        File file=new File("${destPath}${className}.java")        
        file.write(ergebnis.toString())
    }
    
    private String getTestPath(Map params,String packageName) {
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

    private String addTestPackageName(String packageName) {
        if (!packageName)
            return 'dao.jdbc.test'
        else 
            return "${packageName}.dao.jdbc.test"
    }

    private String addGenPackageName(String packageName) {
        if (!packageName)
            return 'dao.jdbc'
        else 
            return "${packageName}.dao.jdbc"
    }

    def testDataTemplate = '''
package ${testPackageName};

import ${packageName}.DataFactory_${model.shortName};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.test.TestDataGenerator
 */

public class ${className} {
    private DataFactory_${model.shortName} dataFactory;
    public void setDataFactory(DataFactory_${model.shortName} df) {
        this.dataFactory = df;
    }

    public void createTestData() {
        writeListTestData();
        writeEntityTestData();
        writeM2NTestData();
        // view data can't be written
    }

    private void writeListTestData() {
<% model.entities*.value.attribs*.findAll { it.type == strListType }*.each { attrib -> %>

<% } %>
    }

    private void writeEntityTestData() {
<% model.entities*.each { entity -> %>
        dataFactory.create_${entity.value.name}(true);
<% } %>
    }
    
    private void writeM2NTestData() {
<% model.m2nRelations.each { m2n -> %>
<% } %>
    }
}
'''


}

