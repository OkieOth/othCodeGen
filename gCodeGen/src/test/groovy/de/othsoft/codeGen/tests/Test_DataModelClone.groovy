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


package de.othsoft.codeGen.tests

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import static org.junit.Assert.*
import de.othsoft.codeGen.tests.models.ResManModel_v1
import de.othsoft.codeGen.types.DataModel
import de.othsoft.codeGen.requirements.AttribType

/**
 *
 * @author eiko
 */
class Test_DataModelClone {

    public Test_DataModelClone() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test_baseAttribs() {
        ResManModel_v1 model = new ResManModel_v1()
        assertNotNull(model);
        DataModel clonedModel = model.clone();
        assertNotNull(clonedModel)

        assertTrue(clonedModel.version == model.version)
        assertTrue(clonedModel.shortName == model.shortName)

        model.version = 99
        assertFalse(clonedModel.version == model.version)
        assertTrue(clonedModel.shortName == model.shortName)

        model.shortName = 'huhu'
        assertFalse(clonedModel.version == model.version)
        assertFalse(clonedModel.shortName == model.shortName)
    }
    
    @Test
    public void test_defaultTypes_1() {
        ResManModel_v1 model = new ResManModel_v1()
        assertNotNull(model);
        DataModel clonedModel = model.clone();
        assertNotNull(clonedModel)

        assertTrue(clonedModel.defaultTypes == model.defaultTypes)
        
        clonedModel.defaultTypes = null;
        assertFalse(clonedModel.defaultTypes == model.defaultTypes)
    }
      
    @Test
    public void test_defaultTypes_2() {
        ResManModel_v1 model = new ResManModel_v1()
        assertNotNull(model);
        DataModel clonedModel = model.clone();
        assertNotNull(clonedModel)

        assertTrue(clonedModel.defaultTypes == model.defaultTypes)

        clonedModel.defaultTypes.namex = AttribType.t_date
        assertFalse(clonedModel.defaultTypes == model.defaultTypes)
    }

    @Test
    public void test_defaultTypes_3() {
        ResManModel_v1 model = new ResManModel_v1()
        assertNotNull(model);
        DataModel clonedModel = model.clone();
        assertNotNull(clonedModel)

        assertTrue(clonedModel.defaultTypes == model.defaultTypes)

        clonedModel.defaultTypes.name = AttribType.t_date
        assertFalse(clonedModel.defaultTypes == model.defaultTypes)
    }

    @Test
    public void test_defaultTypes_4() {
        ResManModel_v1 model = new ResManModel_v1()
        assertNotNull(model);
        DataModel clonedModel = model.clone();
        assertNotNull(clonedModel)

        assertTrue(clonedModel.entities==model.entities)
        assertTrue(clonedModel.m2nRelations==model.m2nRelations)
        assertTrue(clonedModel.views==model.views)        
    }
    
    @Test
    public void test_defaultTypes_5() {
        ResManModel_v1 model = new ResManModel_v1()
        assertNotNull(model);
        DataModel clonedModel = model.clone();
        assertNotNull(clonedModel)
        
        clonedModel.entities['Mehrwertsteuer'].attribs.remove(0)

        assertTrue(clonedModel.entities!=model.entities)
        assertTrue(clonedModel.m2nRelations==model.m2nRelations)
        assertTrue(clonedModel.views==model.views)        
    }
}
