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
package de.othsoft.codeGen.impl.helper;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * defines the functions needed for the generation of test code
 * @author eiko
 */
public interface ITestDataHelper {
    /**
     * how many entities should be created in test database
     * @param entityName
     * @return 
     */
    int getEntityRowCount(String entityName);
    
    
    /**
     * initialize the attrib given with 'attribName' of Object o with a random value
     * @param o object to initialize
     * @param entityName
     * @param attribName
     */
    void initWithTestData(Object o,String entityName,String attribName);

    Integer getInt(boolean needed);
    Integer getInt(int min, int max, boolean needed);
    Integer getIntFromList(List<Integer> elems, boolean needed);
    Long getLong(boolean needed);
    Long getLong(long min, long max,boolean needed);
    Long getLongFromList(List<Long> elems,boolean needed);
    String getString(boolean needed);
    String getString(int minLen,int maxLen,boolean needed);
    String getStringFromList(List<String> elems,boolean needed);
    Boolean getBool(boolean needed);
    Date getDate(String min,String max,boolean needed,DateFormat f);
    Date getDate(Date min,Date max,boolean needed);
    Date getDate(boolean needed);
    Date getDateFromList(List<Date> elems,boolean needed);
    Date getDateFromList(List<String> elems,boolean needed,DateFormat f);
    Date getTimestamp(String min,String max,boolean needed,DateFormat f);
    Date getTimestamp(Date min,Date max,boolean needed);
    Date getTimestamp(boolean needed);
    Date getTimestampFromList(List<Date> elems,boolean needed);
    Date getTimestampFromList(List<String> elems,boolean needed,DateFormat df);
    BigDecimal getMoney(String min,String max,boolean needed);
    BigDecimal getMoney(BigDecimal min,BigDecimal max,boolean needed);
    BigDecimal getMoney(boolean needed);
    BigDecimal getMoneyFromStrList(List<String> elems,boolean needed);
    BigDecimal getMoneyFromList(List<BigDecimal> elems,boolean needed);
    Double getDouble(boolean needed);
    Double getDouble(Double min, Double max, boolean needed);
    Double getDoubleFromList(List<Double> elems, boolean needed);
}
