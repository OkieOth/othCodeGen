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
import java.text.SimpleDateFormat

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

    @Test
    public void testGetBool_1() {
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Boolean iValue = testDataHelper.getBool(false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetBool_1_1: nullCount=$nullCount, valCount=$valCount, values=$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        for (int i=0;i<10;i++) {
            Boolean iValue = testDataHelper.getBool(true);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetBool_1_2: nullCount=$nullCount, valCount=$valCount");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetString_1() {
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        for (int i=0;i<10;i++) {
            String iValue = testDataHelper.getString(false);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetString_1_1: nullCount=$nullCount, valCount=$valCount");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        for (int i=0;i<10;i++) {
            String iValue = testDataHelper.getString(true);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetString_1_2: nullCount=$nullCount, valCount=$valCount");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetString_2() {
        int min=5;
        int max=10;
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            String iValue = testDataHelper.getString(min,max,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetString_2_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            String iValue = testDataHelper.getString(min,max,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetString_2_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetString_3() {
        def testList=['a','b','c','d','e','f'];
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            String iValue = testDataHelper.getStringFromList(testList,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetString_3_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            String iValue = testDataHelper.getStringFromList(testList,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetString_3_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetDate_1() {
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getDate(false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDate_1_1: nullCount=$nullCount, valCount=$valCount, values=$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getDate(true);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetDate_1_2: nullCount=$nullCount, valCount=$valCount");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }
     
    @Test
    public void testGetDate_2() {
        def min='01.01.2000';
        def max='31.12.2000';
        SimpleDateFormat df = new SimpleDateFormat('dd.MM.yyyy');
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getDate(min,max,false,df);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDate_2_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getDate(min,max,true,df);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDate_2_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetDate_2_1() {
        SimpleDateFormat df = new SimpleDateFormat('dd.MM.yyyy');
        Date min=df.parse('01.01.2010');
        Date max=df.parse('31.03.2011');
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getDate(min,max,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDate_2_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getDate(min,max,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDate_2_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    
    @Test
    public void testGetDate_3() {
        SimpleDateFormat df = new SimpleDateFormat('dd.MM.yyyy');
        def testList=['01.01.2012','22.03.2013','14.08.1999'];
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getDateFromList(testList,false,df);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDate_3_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getDateFromList(testList,true,df);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDate_3_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetDate_4() {
        SimpleDateFormat df = new SimpleDateFormat('dd.MM.yyyy');
        def testList=[df.parse('01.01.2010'),df.parse('01.02.2012'),df.parse('01.03.2013')];
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getDateFromList(testList,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDate_4_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getDateFromList(testList,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDate_4_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetTimestamp_1() {
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getTimestamp(false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetDate_1_1: nullCount=$nullCount, valCount=$valCount, values=$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getTimestamp(true);
            if (iValue==null) 
                nullCount++;
            else
                valCount++;
        }
        println ("testGetDate_1_2: nullCount=$nullCount, valCount=$valCount");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }
     
    @Test
    public void testGetTimestamp_2() {
        def min='01.01.2000 01:30:44';
        def max='31.12.2000 01:30:44';
        SimpleDateFormat df = new SimpleDateFormat('dd.MM.yyyy HH:mm:ss');
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getTimestamp(min,max,false,df);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetTimestamp_2_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getTimestamp(min,max,true,df);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetTimestamp_2_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetTimestamp_2_1() {
        SimpleDateFormat df = new SimpleDateFormat('dd.MM.yyyy HH:mm:ss');
        Date min=df.parse('01.01.2010 01:30:44');
        Date max=df.parse('31.03.2011 01:30:44');
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getTimestamp(min,max,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetTimestamp_2_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getTimestamp(min,max,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetTimestamp_2_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    
    @Test
    public void testGetTimestamp_3() {
        SimpleDateFormat df = new SimpleDateFormat('dd.MM.yyyy HH:mm:ss');
        def testList=['01.01.2012 01:30:44','22.03.2013 01:30:44','14.08.1999 01:30:44'];
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getTimestampFromList(testList,false,df);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetTimestamp_3_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getTimestampFromList(testList,true,df);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetTimestamp_3_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

    @Test
    public void testGetTimestamp_4() {
        SimpleDateFormat df = new SimpleDateFormat('dd.MM.yyyy HH:mm:ss');
        def testList=[df.parse('01.01.2010 01:30:44'),df.parse('01.02.2012 01:30:44'),df.parse('01.03.2013 01:30:44')];
        TestDataHelper testDataHelper = new TestDataHelper();
        int nullCount = 0;
        int valCount = 0;
        def values = [];
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getTimestampFromList(testList,false);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetTimestamp_4_1: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertTrue(nullCount>0)
        assertTrue(valCount>0)

        nullCount = 0;
        valCount = 0;
        values = [];        
        for (int i=0;i<10;i++) {
            Date iValue = testDataHelper.getTimestampFromList(testList,true);
            if (iValue==null) 
                nullCount++;
            else {
                valCount++;
                values.add(iValue);
            }
        }
        println ("testGetTimestamp_4_2: nullCount=$nullCount, valCount=$valCount, values:$values");
        assertEquals(0,nullCount)
        assertEquals(10,valCount)
    }

}
