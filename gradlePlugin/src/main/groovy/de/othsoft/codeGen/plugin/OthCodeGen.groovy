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
import de.othsoft.codeGen.impl.groovy.dao.BeanGenerator
import de.othsoft.codeGen.impl.groovy.dao.DataModelPumlGenerator
import de.othsoft.codeGen.impl.groovy.dao.FactoryInterfGenerator
import de.othsoft.codeGen.impl.groovy.dao.jdbc.JdbcBeanGenerator
import de.othsoft.codeGen.impl.groovy.dao.jdbc.JdbcDataFactoryGenerator
import de.othsoft.codeGen.impl.groovy.dao.jdbc.test.TestDataGenerator
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
            if (project.othCodeGen==null || project.othCodeGen.model==null) {
                def msg = "no model is given, please configure";
                println "\n $msg"
                throw new Exception (msg)
            }
            if (project.othCodeGen.generators==null || project.othCodeGen.generators.isEmpty()) {
                def msg = "no generators are given, so I don't know what's to do :-/";
                println "\n $msg"
                throw new Exception (msg)
            }
            DataModel model = project.othCodeGen.model;
            for (Object o:generators) {
                if (o instanceof ICodeGenImpl) {                    
                    // a normal generator
                    def generator = (ICodeGenImpl)o;
                    generator.genCode(model)
                    generator.genTestCode(model)
                }
                else if (o instanceof String) {
                    // short key for a set of generators with standard configuration
                    handleGeneratorString((String)o,model,null)
                }
                else if (o instanceof List) {
                    // first elem is the desired generator, second elem codeGen params,
                    // a optional third elem are the params for test code generation
                    List list = (List)o;
                    if (list.size()!=2) {
                        throw new Exception('sublist of generators list entry need a size of two [ String or ICodeGenImpl , Map with build params ] ')
                    }
                    Object o_1 = list.get(0);                    
                    Object o_2 = list.get(1);
                    if (o_1==null || o_2==null) {
                        throw new Exception('sublist of generators list entry need a size of two [ String or ICodeGenImpl , Map with build params ] ')
                    }
                    if (!(o_1 instanceof String || o_1 instanceof ICodeGenImpl)) {
                        throw new Exception('first sublist elem of generators list entry has wrong type, needs to be String or ICodeGenImpl')                        
                    }
                    if (!(o_2 instanceof Map)) {
                        throw new Exception('second sublist elem of generators list entry has wrong type, needs to be Map')                        
                    }
                    Map paramMap = (Map)o_2;
                    if (o_1 instanceof String) {
                        handleGeneratorString((String)o_1,model,paramMap)
                    }
                    else {
                        generator.genCode(model,paramMap)
                        generator.genTestCode(model,paramMap)                        
                    }
                }
            }
        }
    }
    
    private void handleGeneratorString(String s, DataModel model,Map paramMap) {
        switch((String)o) {
            case 'db_psql':
                // create database code for postgresql
                def generator = new de.othsoft.codeGen.impl.groovy.sql.psql.CreateDatabaseSqlGenerator()
                if (paramMap!=null)
                    generator.genCode(model,params)
                else
                    generator.genCode(model)
                break;
            case 'dao_base':
                // create datafactory for given model
                BeanGenerator beanGenerator = new BeanGenerator()
                FactoryInterfGenerator factoryInterfGenerator = new FactoryInterfGenerator()
                if (paramMap!=null) {
                    beanGenerator.genCode(model,paramMap)
                    beanGenerator.genTestCode(model,paramMap)
                    factoryInterfGenerator.genCode(model,paramMap)
                    beanGenerator.genTestCode(model,paramMap)
                }
                else {
                    beanGenerator.genCode(model)
                    beanGenerator.genTestCode(model)
                    factoryInterfGenerator.genCode(model)
                    beanGenerator.genTestCode(model)                    
                }
                break;
            case 'dao_jdbc':
                // creates a jdbc datafactory
                JdbcBeanGenerator jdbcBeanGenerator = new JdbcBeanGenerator()
                JdbcDataFactoryGenerator jdbcDataFactoryGenerator = new JdbcDataFactoryGenerator();
                if (paramMap!=null) {
                    jdbcBeanGenerator.genCode(model,paramMap)
                    jdbcBeanGenerator.genTestCode(model,paramMap)
                    jdbcDataFactoryGenerator.genCode(model,paramMap)
                    jdbcDataFactoryGenerator.genTestCode(model,paramMap)
                }
                else {
                    jdbcBeanGenerator.genCode(model)
                    jdbcBeanGenerator.genTestCode(model)
                    jdbcDataFactoryGenerator.genCode(model)
                    jdbcDataFactoryGenerator.genTestCode(model)                    
                }
                break;
            case 'test_data':
                // creates test data
                def generator = new TestDataGenerator()
                if (paramMap!=null)
                    generator.genTestCode(model,paramMap)
                else
                    generator.genTestCode(model);
                break;
            case 'model_puml':
                // creates test data
                def generator = new DataModelPumlGenerator()
                if (paramMap!=null)
                    generator.genCode(model,paramMap)
                else
                    generator.genCode(model);
                break;
            default:
                throw new Exception ('unknown generator string.\ncurrently available: db_psql,dao_base, dao_jdbc, test_data, model_puml')
        }        
    }
}

class OthCodeGenPluginExtension {
    DataModel model
    List generators
}




