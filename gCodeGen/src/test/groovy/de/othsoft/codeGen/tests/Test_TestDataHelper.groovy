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
class Test_TestDataHelper {

    public Test_TestDataHelper() {
    }

    @Test
    public void testGetInt_1() {
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        for (int i=0;i<10;i++) {
            Integer iValue = testDataHelper.getInt(false);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetInt_1_1: nullCount=$nullCount, valCount=$valCount");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        for (int i=0;i<10;i++) {
            Integer iValue = testDataHelper.getInt(true);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetInt_1_2: nullCount=$nullCount, valCount=$valCount");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetInt_2() {
        int min=10;
        int max=500;
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Integer iValue = testDataHelper.getInt(min,max,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetInt_2_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Integer iValue = testDataHelper.getInt(min,max,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetInt_2_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetInt_3() {
        def testList=[1,2,3,4,5,6];
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Integer iValue = testDataHelper.getIntFromList(testList,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetInt_3_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Integer iValue = testDataHelper.getIntFromList(testList,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetInt_3_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetLong_1() {
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        for (int i=0;i<10;i++) {
            Long iValue = testDataHelper.getLong(false);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetLong_1_1: nullCount=$nullCount, valCount=$valCount");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        for (int i=0;i<10;i++) {
            Long iValue = testDataHelper.getLong(true);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetLong_1_2: nullCount=$nullCount, valCount=$valCount");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetLong_2() {
        int min=10;
        int max=500;
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Long iValue = testDataHelper.getLong(min,max,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetLong_2_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Long iValue = testDataHelper.getLong(min,max,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetLong_2_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetLong_3() {
        def testList=[1,2,3,4,5,6];
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Long iValue = testDataHelper.getLongFromList(testList,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetLong_3_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Long iValue = testDataHelper.getLongFromList(testList,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetLong_3_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetDouble_1() {
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        for (int i=0;i<10;i++) {
            Double iValue = testDataHelper.getDouble(false);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetDouble_1_1: nullCount=$nullCount, valCount=$valCount");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        for (int i=0;i<10;i++) {
            Double iValue = testDataHelper.getDouble(true);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetDouble_1_2: nullCount=$nullCount, valCount=$valCount");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetDouble_2() {
        int min=10;
        int max=500;
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Double iValue = testDataHelper.getDouble(min,max,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDouble_2_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Double iValue = testDataHelper.getDouble(min,max,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDouble_2_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetDouble_3() {
        def testList=[1,2,3,4,5,6];
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Double iValue = testDataHelper.getDoubleFromList(testList,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDouble_3_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Double iValue = testDataHelper.getDoubleFromList(testList,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDouble_3_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetMoney_1() {
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        for (int i=0;i<10;i++) {
            BigDecimal iValue = testDataHelper.getMoney(false);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetMoney_1_1: nullCount=$nullCount, valCount=$valCount");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        for (int i=0;i<10;i++) {
            BigDecimal iValue = testDataHelper.getMoney(true);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetMoney_1_2: nullCount=$nullCount, valCount=$valCount");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetMoney_2() {
        int min=10;
        int max=500;
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            BigDecimal iValue = testDataHelper.getMoney(min,max,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetMoney_2_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            BigDecimal iValue = testDataHelper.getMoney(min,max,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetMoney_2_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetMoney_3() {
        def testList=[1,2,3,4,5,6];
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            BigDecimal iValue = testDataHelper.getMoneyFromList(testList,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetMoney_3_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            BigDecimal iValue = testDataHelper.getMoneyFromList(testList,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetMoney_3_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }
    
    // TODO - getStr
    // TODO - getBool
    // TODO - getDate
    // TODO - getTimestamp

}
