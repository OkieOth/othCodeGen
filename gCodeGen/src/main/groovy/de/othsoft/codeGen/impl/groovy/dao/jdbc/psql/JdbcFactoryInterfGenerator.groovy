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

import de.othsoft.codeGen.types.DataModel
import de.othsoft.codeGen.types.ICodeGenImpl
import groovy.text.SimpleTemplateEngine
import de.othsoft.codeGen.impl.helper.FileHelper

import de.othsoft.codeGen.requirements.AttribType

/**
 *
 * @author eiko
 */
class JdbcFactoryInterfGenerator implements ICodeGenImpl {
    void genCode(DataModel model,Map params) {
        String packageName=addGenPackageName(params.packageName)
        String beanPackage=params.packageName+".beans"
        String destPathRoot=params.destPathRoot
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

        File file=new File("${destPath}IJdbcDataFactory_${model.shortName}.java")        
        //file.write(removeEmptyLines(ergebnis.toString()))
        file.write(ergebnis.toString())

    }

    void genTestCode(DataModel model,Map params) {
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

import de.othsoft.codeGen.requirements.CmdData;
import de.othsoft.codeGen.requirements.DaoException;
import java.util.List;

public interface IJdbcDataFactory_${model.shortName} extends IDataFactory_${model.shortName} {
    CmdData startTransaction() throws DaoException;
}
'''

}

