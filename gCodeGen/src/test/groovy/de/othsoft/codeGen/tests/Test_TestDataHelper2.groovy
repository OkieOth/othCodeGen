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
import de.othsoft.codeGen.impl.helper.TestDataHelper

/**
 *
 * @author eiko
 */
class Test_TestDataHelper2 {

    public Test_TestDataHelper2() {
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
     public void initWithTestData_int() {
        DummyClass o = new DummyClass();
        assertNull(o.getIntValue())
        TestDataHelper helper = new TestDataHelper();
        helper.initWithTestData(o,'DummyClass','intValue',true);
        Integer s1 = o.getIntValue();
        assertNotNull(s1);
        helper.initWithTestData(o,'DummyClass','intValue',true);
        Integer s2 = o.getIntValue();
        assertNotNull(s2);
        assertTrue(!s1.equals(s2));
     }

     @Test
     public void initWithTestData_long() {
        DummyClass o = new DummyClass();
        assertNull(o.getLongValue())
        TestDataHelper helper = new TestDataHelper();
        helper.initWithTestData(o,'DummyClass','longValue',true);
        Long s1 = o.getLongValue();
        assertNotNull(s1);
        helper.initWithTestData(o,'DummyClass','longValue',true);
        Long s2 = o.getLongValue();
        assertNotNull(s2);
        assertTrue(!s1.equals(s2));
     }

     @Test
     public void initWithTestData_string() {
        DummyClass o = new DummyClass();
        assertNull(o.getLandIdTxt())
        TestDataHelper helper = new TestDataHelper();
        helper.initWithTestData(o,'DummyClass','land',true);
        String s1 = o.getLandIdTxt();
        assertNotNull(s1);
        helper.initWithTestData(o,'DummyClass','land',true);
        String s2 = o.getLandIdTxt();
        assertNotNull(s2);
        assertTrue(!s1.equals(s2));
     }

     @Test
     public void initWithTestData_money() {
         //
     }

     @Test
     public void initWithTestData_date() {
         //
     }

     @Test
     public void initWithTestData_timestamp() {
         //
     }

     @Test
     public void initWithTestData_double() {
         //
     }
}

/**
 * this class contains attributes, getter and setter in the gCodeGen style
 */
class DummyClass {
    protected String landIdTxt; 
    public String getLandIdTxt() { return this.landIdTxt; }
    public void setLandIdTxt(String v) {
        this.landIdTxt = v;
    }
    
    private Long longValue;
    public Long getLongValue() { return longValue; }
    public void setLongValue(Long l) {
        longValue = l;
    }

    private Integer intValue;
    public Integer getIntValue() { return longValue; }
    public void setIntValue(Integer l) {
        longValue = l;
    }
}
