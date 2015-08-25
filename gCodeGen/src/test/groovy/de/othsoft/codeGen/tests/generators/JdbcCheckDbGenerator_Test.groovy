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
import de.othsoft.codeGen.tests.models.ResManModel_v2

/**
 *
 * @author hulk
 */
class JdbcCheckDbGenerator_Test {
    @Test
    public void first() {
        def model = new ResManModel_v2()
        def generator = new de.othsoft.codeGen.impl.groovy.dao.jdbc.JdbcCheckDbGenerator()
        def params = [packageName:'de.gCodeGen.test',
            destPathRoot:'src/generated/java',
            testPathRoot:'src/generated/test']
        generator.genCode(model,params)
        generator.genTestCode(model,params)
    }
}
