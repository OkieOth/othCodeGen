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

import de.othsoft.codeGen.types.CodeGenException
import de.othsoft.codeGen.types.CheckModelException


/**
 *
 * @author hulk
 */
class CodeGenExceptionTest {

    public CodeGenExceptionTest() {
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
    public void testCodeGenException1() {
        boolean abgefangen=false
        try {
            throw new CodeGenException('test')
            fail('no exception is thrown')
        }
        catch(CodeGenException cge) {
            abgefangen=true
        }
        catch(Exception e) {
            fail('catch wrong exception: '+e.getName())
        }
        assertTrue(abgefangen)
    }

    @Test
    public void testCodeGenException2() {
        boolean abgefangen=false
        try {
            throw new NumberFormatException()
            fail('no exception is thrown')
        }
        catch(CodeGenException cge) {
            fail('no CodeGenException is thrown')
        }
        catch(Exception e) {
            abgefangen=true
        }
        assertTrue(abgefangen)
    }

    @Test
    public void testCheckModelException1() {
        boolean abgefangen=false
        try {
            throw new CheckModelException('test')
            fail('no exception is thrown')
        }
        catch(CheckModelException cme) {
            abgefangen=true
        }
        catch(Exception e) {
            fail('catch wrong exception: '+e.getName())
        }
        assertTrue(abgefangen)
    }
}
