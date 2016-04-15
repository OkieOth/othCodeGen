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

package de.othsoft.codeGen.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import de.othsoft.codeGen.types.DataModel
import de.othsoft.codeGen.types.ICodeGenImpl

/**
 *
 * @author eiko
 */
class OthCodeGen implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create("othCodeGen", OthCodeGenPluginExtension)
        project.task('doCodeGen') << {
            println "\nstart code generation"
            println "Übergebene Modelle: ${project.othCodeGen.codeGenContList.size()}"
        }
    }
}

class OthCodeGenPluginExtension {
    HashMap<DataModel,List<ICodeGenImpl>> codeGenContList    
}




