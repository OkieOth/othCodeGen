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

import de.othsoft.codeGen.types.*
import de.othsoft.codeGen.tests.models.TestModel_v1
import de.othsoft.codeGen.tests.models.TestModel_WithoutEntities
import de.othsoft.codeGen.tests.models.TestModel_WithoutM2N
import de.othsoft.codeGen.tests.models.TestModel_WithoutViews
import de.othsoft.codeGen.tests.models.TestModel_ViewsOnly
import de.othsoft.codeGen.tests.models.TestModel_MissingViewAttribType
import de.othsoft.codeGen.tests.models.TestModel_MissingViewAttribName
import de.othsoft.codeGen.tests.models.TestModel_MissingViewName
import de.othsoft.codeGen.tests.models.TestModel_MissingEntityAttribType
import de.othsoft.codeGen.tests.models.TestModel_MissingEntityAttribName
import de.othsoft.codeGen.tests.models.TestModel_MissingEntityName

import de.othsoft.codeGen.types.CheckModelException


/**
 *
 * @author hulk
 */
class DataModelBase {    
    @Test
    public void testTestModel() {
        TestModel_v1 testModel=new TestModel_v1()
        assertEquals ('EMv1',testModel.shortName)
    }    

    @Test
    public void testModelWithoutEntities() {
        TestModel_WithoutEntities testModel=new TestModel_WithoutEntities()
        assertEquals ('EMv1',testModel.shortName)
    }    

    @Test
    public void testModelWithoutM2N() {
        TestModel_WithoutM2N testModel=new TestModel_WithoutM2N()
        assertEquals ('EMv1',testModel.shortName)
    }    

    @Test
    public void testModelWithoutViews() {
        TestModel_WithoutViews testModel=new TestModel_WithoutViews()
        assertEquals ('EMv1',testModel.shortName)
    }    
    
    @Test
    public void testModelViewsOnly() {
        TestModel_ViewsOnly testModel=new TestModel_ViewsOnly()
        assertEquals ('EMv1',testModel.shortName)
    }    

    @Test
    public void testMissingViewAttribType() {
        try {
            TestModel_MissingViewAttribType testModel=new TestModel_MissingViewAttribType()
            fail('no exception is thrown')
        }
        catch(CheckModelException e) {
            assertTrue(e.getMessage().contains('default type needed'))
        }
    }

    @Test
    public void testMissingViewAttribName() {
        try {
            TestModel_MissingViewAttribName testModel=new TestModel_MissingViewAttribName()
            fail('no exception is thrown')
        }
        catch(CheckModelException e) {
            assertTrue(e.getMessage().contains('for key=\'name\''))
        }
    }

    @Test
    public void testMissingViewName() {
        try {
            TestModel_MissingViewName testModel=new TestModel_MissingViewName()
            fail('no exception is thrown')
        }
        catch(CheckModelException e) {
            assertTrue(e.getMessage().contains('for key=\'name\''))
        }
    }

    @Test
    public void testMissingEntityAttribName() {
        try {
            TestModel_MissingEntityAttribName testModel=new TestModel_MissingEntityAttribName()
            fail('no exception is thrown')
        }
        catch(CheckModelException e) {
            assertTrue(e.getMessage().contains('for key=\'name\''))
        }
    }

    @Test
    public void testMissingEntityAttribType() {
        try {
            TestModel_MissingEntityAttribType testModel=new TestModel_MissingEntityAttribType()
            fail('no exception is thrown')
        }
        catch(CheckModelException e) {
            assertTrue(e.getMessage().contains('default type needed'))
        }
    }

    @Test
    public void testMissingEntityName() {
        try {
            TestModel_MissingEntityName testModel=new TestModel_MissingEntityName()
            fail('no exception is thrown')
        }
        catch(CheckModelException e) {
            assertTrue(e.getMessage().contains('for key=\'name\''))
        }
    }
}
