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

package de.othsoft.codeGen.tests.generators

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import static org.junit.Assert.*
import de.othsoft.codeGen.tests.models.ResManModel_v1
import de.othsoft.codeGen.tests.models.ResManModel_v2
import de.othsoft.codeGen.impl.groovy.sql.psql.CreateDatabaseSqlGenerator

/**
 *
 * @author hulk
 */
class CreateDatabasePsqlGen_Test {
    @Test
    public void test_v1() {
        def model = new ResManModel_v1()
        def generator = new CreateDatabaseSqlGenerator()
        def params = [destPathRoot:'src/test/generated/resources/sql/psql/v1']
        generator.genCode(model,params)
    }

    @Test
    public void test_v2() {
        def model = new ResManModel_v2()
        def generator = new CreateDatabaseSqlGenerator()
        def params = [destPathRoot:'src/generated/resources/sql/psql/v2']
        generator.genCode(model,params)
    }
}
