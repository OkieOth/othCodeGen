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

package de.othsoft.codeGen.impl.java

import de.othsoft.codeGen.types.DataModel
import groovy.text.SimpleTemplateEngine
import de.othsoft.codeGen.types.AttribType

/**
 *
 * @author eiko
 */
class JavaBeanGeneratorBase {
    protected def typeConvert = { type ->
        switch (type) {
        case AttribType.t_int : return 'Integer'
        case AttribType.t_long : return 'Long'
        case AttribType.t_string : return 'String'
        case AttribType.t_key : return 'Integer'
        case AttribType.t_boolean : return 'Boolean'
        case AttribType.t_date : return 'java.util.Date'
        case AttribType.t_timestamp : return 'java.util.Date'
        case AttribType.t_geo : return '???'
        case AttribType.t_money : return 'java.math.BigDecimal'
        case AttribType.t_meters : return 'Double'
        case AttribType.t_milimeters : return 'Double'
        case AttribType.t_kilometers : return 'Double'
        case AttribType.t_kmh : return 'Double'
        case AttribType.t_prozent : return 'Double'
        case AttribType.t_hour : return 'Integer'
        case AttribType.t_min : return 'Integer'
        case AttribType.t_sec : return 'Integer'
        case AttribType.t_time : return '???'
        case AttribType.t_volt : return 'Double'
        case AttribType.t_float : return 'Double'
        case AttribType.t_str_list : return 'Integer'
        default:
            return '???'
        }
    }
    
    String removeEmptyLines (String genResult) {
        String s=genResult.replaceAll(/s*\n/,'\n')
        return s.replaceAll(/;\s*\n/,';\n')
    }

    protected void genResultAndWriteToFile(String templateName, String className, DataModel model, String destPackage, 
        String destPath, String descr,def aktElem) {
        genResultAndWriteToFile(templateName,className,model,destPackage,destPath,descr,aktElem,null)
    }
    
    protected void genResultAndWriteToFile(String templateName, String className, DataModel model, String destPackage, 
        String destPath, String descr,def aktElem,String baseClassName) {
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(templateName)
        def daten = [
            model:model,
            aktElem:aktElem,
            typeConvert:typeConvert,
            strListType:AttribType.t_str_list,
            className:className,
            descr:descr,
            destPackage:destPackage]
        if (baseClassName)
            daten.baseClassName = baseClassName
        def ergebnis = template.make(daten)

        File file=new File("${destPath}${className}.java")        
        //file.write(removeEmptyLines(ergebnis.toString()))
        file.write(ergebnis.toString())
    }
}

