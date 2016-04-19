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
import de.othsoft.codeGen.impl.helper.ITestDataHelper
import de.othsoft.codeGen.requirements.AttribType
import groovy.text.SimpleTemplateEngine

class TestDataGenerator extends JavaBeanGeneratorBase implements ICodeGenImpl {   
    private final static String defDestPath='src/main/java'
    
    private String buildDefPackageName (DataModel model) {
        return "de.gCodeGen.testData.${model.shortName}"
    }
    
    void genCode(DataModel model,Map params) {
        // not needed here
    }

    void genCode(DataModel model) {
        // not needed here
    }

    void genTestCode(DataModel model,Map params) {
        String packageBaseName = params.packageName ? params.packageName : defDestPath
        String destPathRoot = params.testPathRoot ? params.testPathRoot : buildDefPackageName(model)
        genTestCodeNow(model,packageBaseName,destPathRoot)
    }

    void genTestCode(DataModel model) {
        String packageBaseName = buildDefPackageName(model)
        genTestCodeNow(model,packageBaseName,defDestPath)
    }

    private void genTestCodeNow(DataModel model,String packageBaseName,String destPathRoot) {
        String packageName=addGenPackageName(packageBaseName)
        String beanPackageName=addBeanPackageName(packageBaseName)
        String testPackageName=addTestPackageName(packageBaseName)+""
        String destPath = getTestPath (destPathRoot,testPackageName)
        def className = "TestData_${model.shortName}"
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(testDataTemplate)
        def daten = [
            typeConvert:typeConvert,
            packageName:packageName,
            testPackageName:testPackageName,
            beanPackageName:beanPackageName,
            model:model,
            strListType:AttribType.t_str_list,
            className:className]
        def ergebnis = template.make(daten)

        File file=new File("${destPath}${className}.java")        
        file.write(ergebnis.toString())
    }
    
    private String getTestPath(String destPathRoot,String packageName) {
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

    private String addBeanPackageName(String packageName) {
        if (!packageName)
            return 'dao.beans'
        else 
            return "${packageName}.beans"
    }

    def testDataTemplate = '''
package ${testPackageName};

import de.othsoft.codeGen.requirements.DaoException;
import ${packageName}.DataFactory_${model.shortName};
import de.othsoft.codeGen.impl.helper.ITestDataHelper;
import de.othsoft.codeGen.impl.helper.MinMaxCont;
import ${beanPackageName}.*;
import java.util.HashMap;

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.test.TestDataGenerator
 */

public class ${className} {
    private DataFactory_${model.shortName} dataFactory;

    HashMap<String,MinMaxCont> entityMinMaxMap = new HashMap();

    // if you need other test values initialize it with another implementation
    private ITestDataHelper testDataHelper = null;
    
    void setTestDataHelper(ITestDataHelper testDataHelper) {
        this.testDataHelper = testDataHelper;
    }

    public void setDataFactory(DataFactory_${model.shortName} df) {
        this.dataFactory = df;
    }

    public void createTestData() throws DaoException {
        writeEntityTestData();
        writeM2NTestData();
        linkRefData();
        // view data can't be written
    }

<% model.entities*.each { entity -> %>
    private void createDataFor_${entity.value.name}() throws DaoException {
        MinMaxCont minMaxCont = null;
        System.out.println("starting ${entity.value.name}...");
        for (int i=0;i<testDataHelper.getRowCount("${entity.value.name}");i++) {
            ${entity.value.name} v = dataFactory.create_${entity.value.name}();
        <% entity.value.attribs.each { attrib -> 
        %>
            this.testDataHelper.initWithTestData(v,"${entity.value.name}","${attrib.name}",${attrib.needed});<% } %>
            // handle references
            <% entity.value.refs.each { ref -> 
            %>
            // reference
            <% }
            %>
            v.insert(null,null);
            if (minMaxCont==null) {
                minMaxCont = new MinMaxCont();
                minMaxCont.setMin(v.getId());
            }
            minMaxCont.setMax(v.getId());
        }
        entityMinMaxMap.put("${entity.value.name}",minMaxCont);
        System.out.println("finished ${entity.value.name}.");
    }<% } %>

    private void writeEntityTestData() throws DaoException {
    <% model.entities*.each { entity ->
%>    createDataFor_${entity.value.name}();
    <% } %>}



    private void linkRefData() throws DaoException {
<% model.entities*.each { entity -> entity.value.refs*.each { ref -> 
%>        linkRef_${entity.value.name}_${ref.entity.name}();
<% } } %>
    }

<% model.entities*.each { entity -> entity.value.refs*.each { ref -> 
%>    private void linkRef_${entity.value.name}_${ref.entity.name}() throws DaoException {
        MinMaxCont minMax_e = entityMinMaxMap.get("${entity.value.name}");
        MinMaxCont minMax_r1 = entityMinMaxMap.get("${ref.entity.name}");
        if (minMax_r1==null) throw new DaoException ("minMaxCont for '${ref.entity.name}' not foundaktElem");\n\\n\
        System.out.println("start linking ${entity.value.name}->${ref.entity.name} ...");
        int min=minMax_e.getMin();
        int max=minMax_e.getMax();
        for (int i=min;i<=max;i++)  {
            ${entity.value.getNameWithFirstLetterUpper()} e = dataFactory.byId_${entity.value.getNameWithFirstLetterUpper()}(null,null,i);\n\
            if (e==null) continue;
            Integer id_r1 = testDataHelper.getInt(minMax_r1.getMin(),minMax_r1.getMax(),${ref.needed});
            e.set${ref.getUpperCamelCaseName()}(id_r1);\n\
            e.update(null,null);
        }
        System.out.println("linking finished ${entity.value.name}->${ref.entity.name}");
    }
<% } } %>

<% model.m2nRelations.each { m2n -> %>
    private void createDataFor_${m2n.value.name}() throws DaoException {
        System.out.println("starting ${m2n.value.name}...");
        MinMaxCont minMax_r1 = entityMinMaxMap.get("${m2n.value.ref1.entity.name}");
        if (minMax_r1==null) throw new DaoException ("minMaxCont for '${m2n.value.ref1.entity.name}' not foundaktElem");
        MinMaxCont minMax_r2 = entityMinMaxMap.get("${m2n.value.ref2.entity.name}");
        if (minMax_r2==null) throw new DaoException ("minMaxCont for '${m2n.value.ref2.entity.name}' not found");
        for (int i=0;i<testDataHelper.getRowCount("${m2n.value.name}");i++) {
            boolean notFound = true;
            while (notFound) {
                Integer id_r1 = testDataHelper.getInt(minMax_r1.getMin(),minMax_r1.getMax(),true);
                Integer id_r2 = testDataHelper.getInt(minMax_r2.getMin(),minMax_r2.getMax(),true);
                ${m2n.value.name} v = dataFactory.byIds_${m2n.value.name}(null,null,id_r1,id_r2);
                if (v==null) {
                    v = dataFactory.create_${m2n.value.name}();
                    v.set${m2n.value.ref1.getUpperCamelCaseName()}(id_r1);
                    v.set${m2n.value.ref2.getUpperCamelCaseName()}(id_r2);
                    v.insert(null,null);
                    notFound = false;
                }
            }
        }
        System.out.println("finished ${m2n.value.name}");
    }<% } %>
    
    private void writeM2NTestData() throws DaoException {
<% model.m2nRelations.each { m2n -> %>
        createDataFor_${m2n.value.name}();<% } %>
    }
}
'''


}

