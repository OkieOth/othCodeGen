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


package de.othsoft.codeGen.impl.helper

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Random
import java.math.RoundingMode
import de.othsoft.codeGen.requirements.DaoException
import java.lang.reflect.Method
import java.lang.reflect.Field
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *
 * @author eiko
 */
class TestDataHelper implements ITestDataHelper {
    public def charBase = ['a','b','c','d','e','f','g',' ','h','i','j','k','l',' ','m','n','o','p','q','r','s',' ','t','u','v','w','x','y',' ','z','ä','ö','ü',' ','ß',
                            'A','B','C','D','E','F','G',' ','H','I','J','K','K',' ','M','N','O','P','Q','R','S',' ','T','U','V','W','X','Y',' ','Z','Ä','Ö','Ü',' '];
    private Random random = new Random();
    
    def elemRowCount = [:];
    def elemValueRestr = [:];
    
    private void setRestr(String entityName,String attribName,def restr) {
        def attribRestrList = elemValueRestr[entityName];
        if (!attribRestrList) {
            attribRestrList = [:];
            elemValueRestr[entityName] = attribRestrList;
        }
        attribRestrList[attribName]=restr;
    }
    
    private Object getRestr(String entityName,String attribName) {
        def attribRestrList = elemValueRestr[entityName];
        if (!attribRestrList) return null;
        def restr = attribRestrList[attribName];
        if (restr)
            return restr;
        else
            return null;
    }
    
    void setIntRestr (String entityName,String attribName,int min, int max) {
        Restr_Int_MinMax r = new Restr_Int_MinMax(min,max);
        setRestr(entityName,attribName,r);
    }

    void setIntRestr (String entityName,String attribName,List<Integer> elems) {
        Restr_Int_List r = new Restr_Int_List(elems);
        setRestr(entityName,attribName,r);
    }

    void setLongRestr (String entityName,String attribName,long min, long max) {
        Restr_Long_MinMax r = new Restr_Long_MinMax(min,max);
        setRestr(entityName,attribName,r);
    }

    void setLongRestr (String entityName,String attribName,List<Long> elems) {
        Restr_Long_List r = new Restr_Long_List(elems);
        setRestr(entityName,attribName,r);
    }

    void setStringRestr (String entityName,String attribName,int minLen,int maxLen) {
        Restr_String_MinMaxLen r = new Restr_String_MinMaxLen(minLen,maxLen);
        setRestr(entityName,attribName,r);
    }

    void setStringRestr (String entityName,String attribName,List<String> elems) {
        Restr_String_List r = new Restr_String_List(elems);
        setRestr(entityName,attribName,r);
    }

    void setDateRestr (String entityName,String attribName,String min,String max,DateFormat f) {
        Restr_Date_MinMax_Str r = new Restr_Date_MinMax_Str(min,max,f);
        setRestr(entityName,attribName,r);
    }

    void setDateRestr (String entityName,String attribName,Date min,Date max) {
        Restr_Date_MinMax r = new Restr_Date_MinMax(min,max);
        setRestr(entityName,attribName,r);
    }

    void setDateRestr (String entityName,String attribName,List<Date> elems) {
        Restr_Date_List r = new Restr_Date_List(elems);
        setRestr(entityName,attribName,r);
    }

    void setDateRestr (String entityName,String attribName,List<String> elems,DateFormat f) {
        Restr_Date_List_Str r = new Restr_Date_List_Str(elems,f);
        setRestr(entityName,attribName,r);
    }

    void setTimestampRestr (String entityName,String attribName,String min,String max,DateFormat f) {
        Restr_Timestamp_MinMax_Str r = new Restr_Timestamp_MinMax_Str(min,max,f);
        setRestr(entityName,attribName,r);
    }

    void setTimestampRestr (String entityName,String attribName,Date min,Date max) {
        Restr_Timestamp_MinMax r = new Restr_Timestamp_MinMax(min,max);
        setRestr(entityName,attribName,r);
    }

    void setTimestampRestr (String entityName,String attribName,List<Date> elems) {
        Restr_Timestamp_List r = new Restr_Timestamp_List(elems);
        setRestr(entityName,attribName,r);
    }

    void setTimestampRestr (String entityName,String attribName,List<String> elems,DateFormat f) {
        Restr_Timestamp_List_Str r = new Restr_Timestamp_List_Str(elems,f);
        setRestr(entityName,attribName,r);
    }

    void setMoneyRestr (String entityName,String attribName,String min,String max) {
        Restr_Money_MinMax_Str r = new Restr_Money_MinMax_Str(min,max);
        setRestr(entityName,attribName,r);
    }

    void setMoneyRestr (String entityName,String attribName,BigDecimal min,BigDecimal max) {
        Restr_Money_MinMax r = new Restr_Money_MinMax(min,max);
        setRestr(entityName,attribName,r);
    }

    void setMoneyStrListRestr (String entityName,String attribName,List<String> elems) {
        Restr_Money_List_Str r = new Restr_Money_List_Str(elems);
        setRestr(entityName,attribName,r);
    }

    void setMoneyRestr (String entityName,String attribName,List<BigDecimal> elems) {
        Restr_Money_List r = new Restr_Money_List(elems);
        setRestr(entityName,attribName,r);
    }

    void setDoubleRestr (String entityName,String attribName,double min, double max) {
        Restr_Double_MinMax r = new Restr_Double_MinMax(min,max);
        setRestr(entityName,attribName,r);
    }

    void setDoubleRestr (String entityName,String attribName,List<Double> elems) {
        Restr_Double_List r = new Restr_Double_List(elems);
        setRestr(entityName,attribName,r);
    }

    
    /**
     * set a desired test data count for a spezific model element
     * @param elemName name of entity or m2n
     * @param count number of desired data entries
     */
    void addElemRowCount(String elemName, int count) {
        elemRowCount[elemName]=count;
    }
    
    /**
     * set the desired max string length for test data
     * @param maxLength max length
     */
    void setMaxStringLength(int maxLength) {
        defaultMaxStringLength = maxLength;
    }
    
    /**
     * set default count for test data per model element
     * @param count number of desired test data
     */
    void setDefaultElemCount(int count) {
        defaultRowCount = count;
    }

    int defaultMaxStringLength=255;
    long defaultMaxDateDiff=10000;
    
    int defaultRowCount=1000;
        
    int getRowCount(String entityName) {
        if (elem[entityName])
            return elem[entityName];
        else
            return defaultRowCount;
    }

    void initWithTestData(Object o,String entityName,String attribName,boolean needed) throws DaoException {
        try {
            Class c = o.getClass();
            Field field = c.getDeclaredField(attribName);
            Class attribTypeClass = field.getType();
            String s = attribName.replaceAll(/_id$/,'Id');
            String setterName = 'set' + s.substring(0,1).toUpperCase()+s.substring(1);
            Method method = c.getDeclaredMethod(setterName,attribTypeClass);
            
            def restr = getRestr(entitiyName,attribName);
            def unknownClassStr = null;
            
            Object v = null; // a random value
            switch (attribTypeClass) {
            case String.class:
                if (restr==null) {
                    v = getString(needed);
                }
                else if (restr instanceof Restr_String_MinMaxLen) {
                    Restr_String_MinMaxLen r = (Restr_String_MinMaxLen)restr;
                    v = getString(r.minLen,r.maxLen,needed);
                }
                else if (restr instanceof Restr_String_List) {
                    Restr_String_List r = (Restr_String_List)restr;
                    v = getStringFromList(r.elems,needed);
                }
                else {
                    unknownClassStr = restr.getClass().getName();
                }
                break;
            case Integer.class:
                if (restr==null) {
                    v = getInt(needed);
                }
                else if (restr instanceof Restr_Int_MinMax) {
                    Restr_Int_MinMax r = (Restr_Int_MinMax) restr;
                    v = getInt(r.min,r.max,needed);
                }
                else if (restr instanceof Restr_Int_List) {
                    Restr_Int_List r = (Restr_Int_List)restr;
                    v = getIntFromList(r.elems,needed);
                }
                else {
                    unknownClassStr = restr.getClass().getName();
                }
                break;
            case Long.class:
                if (restr==null) {
                    v = getLong(needed);
                }
                else if (restr instanceof Restr_Long_MinMax) {
                    Restr_Long_MinMax r = (Restr_Long_MinMax)restr;
                    v = getLong(r.min,r.max,needed);
                }
                else if (restr instanceof Restr_Long_List) {
                    Restr_Long_List r = (Restr_Long_List)restr;
                    v = getLongFromList(r.elems,needed);
                }
                else {
                    unknownClassStr = restr.getClass().getName();
                }
                break;
            case Double.class:
                if (restr==null) {
                    v = getDouble(needed);
                }
                else if (restr instanceof Restr_Double_MinMax) {
                    Restr_Double_MinMax r = (Restr_Double_MinMax)restr;
                    v = getDouble(r.min,r.max,needed);
                }
                else if (restr instanceof Restr_Double_List) {
                    Restr_Double_List r = (Restr_Double_List)restr;
                    v = getDoubleFromList(r.elems,needed);
                }
                else {
                    unknownClassStr = restr.getClass().getName();
                }
                break;
            case BigDecimal.class:
                if (restr==null) {
                    v = getMoney(needed);
                }
                else if (restr instanceof Restr_Money_MinMax_Str) {
                    Restr_Money_MinMax_Str r = (Restr_Money_MinMax_Str)restr;
                    v = getMoney(r.min,r.max,needed);
                }
                else if (restr instanceof Restr_Money_MinMax) {
                    Restr_Money_MinMax r = (Restr_Money_MinMax)restr;
                    v = getMoney(r.min,r.max,needed);
                }
                else if (restr instanceof Restr_Money_List_Str) {
                    Restr_Money_List_Str r = (Restr_Money_List_Str)restr;
                    v = getMoneyFromStrList(r.elems,needed);
                }
                else if (restr instanceof Restr_Money_List) {
                    Restr_Money_List r = (Restr_Money_List)restr;
                    v = getMoneyFromList(r.elems,needed);
                }
                else {
                    unknownClassStr = restr.getClass().getName();
                }
                break;
            case Date.class:
                if (restr==null) {
                    // not unique ... date attribute is the same for date and timestamp attribs
                    v = getTimestamp(needed);
                }
                else if (restr instanceof Restr_Date_MinMax_Str) {
                    Restr_Date_MinMax_Str r = (Restr_Date_MinMax_Str)restr;
                    v = getDate(r.min,r.max,needed,r.f);
                }
                else if (restr instanceof Restr_Date_MinMax) {
                    Restr_Date_MinMax r = (Restr_Date_MinMax)restr;
                    v = getDate(r.min,r.max,needed);
                }
                else if (restr instanceof Restr_Date_List_Str) {
                    Restr_Date_List_Str r = (Restr_Date_List_Str)restr;
                    v = getDateFromList(r.elems,needed,r.f);
                }
                else if (restr instanceof Restr_Date_List) {
                    Restr_Date_List r = (Restr_Date_List)restr;
                    v = getDateFromList(r.elems,needed);
                }
                else if (restr instanceof Restr_Timestamp_MinMax_Str) {
                    Restr_Timestamp_MinMax_Str r = (Restr_Timestamp_MinMax_Str)restr;
                    v = getTimestamp(r.min,r.max,needed,r.f);
                }
                else if (restr instanceof Restr_Timestamp_MinMax) {
                    Restr_Timestamp_MinMax r = (Restr_Timestamp_MinMax)restr;
                    v = getTimestamp(r.min,r.max,needed);
                }
                else if (restr instanceof Restr_Timestamp_List_Str) {
                    Restr_Timestamp_List_Str r = (Restr_Timestamp_List_Str)restr;
                    v = getTimestampFromList(r.elems,needed,r.f);
                }
                else if (restr instanceof Restr_Timestamp_List) {
                    Restr_Timestamp_List r = (Restr_Timestamp_List)restr;
                    v = getTimestampFromList(r.elems,needed);
                }
                else {
                    unknownClassStr = restr.getClass().getName();
                }
            break;
            case Boolean.class:
                v = getBoolean(needed);
                break;
            default:
                throw new DaoException(log,"unknown attrib type: $attribTypeClass");
            }
            if (unknownClassStr!=null)
                throw new DaoException(log,"unknown restr class for $entityName.$attribName: ${unknownClassStr}");
                
            method.invokeMethod(o,v);
        }
        catch(DaoException e) {
            throw e;
        }
        catch(Exception e) {
            throw new DaoException(log,e);
        }
    }

    
    /**
     * is separated to override it
     */
    protected boolean shouldSetValue() {
        random.nextBoolean()
    }
        
    protected char getRandomChar(int i) {
        int c = random.nextInt();
        if (c<0)
            c = Math.abs(c);
        int charBaseLen = charBase.size();
        if (c>=charBaseLen)
        c = c % charBaseLen;
        return charBase[c];
    }
    
    // t_int,
    Integer getInt(boolean needed) {
        if (needed || shouldSetValue()) {
            return random.nextInt();
        }
        else
            return null;
    }

    Integer getInt(int min, int max, boolean needed) {
        if (needed || shouldSetValue()) {
            int i = random.nextInt();
            if (i<min || i>max) {
                i = Math.abs(i)
                int offset = i % (max-min);
                return min + offset;
            }
            else
                return i;
        }
        else
            return null;
    }

    Integer getIntFromList(List<Integer> elems, boolean needed) {
        if (elems==null) return null;
            if (needed || shouldSetValue()) {
            int size = elems.size();
            int i = random.nextInt();
            if (i<0)
                i = Math.abs(i)
            i = i % size;
            return elems[i];
        }
        else
            return null;
    }

    // t_long,
    Long getLong(boolean needed) {
        if (needed || shouldSetValue()) {
            return random.nextLong();
        }
        else
            return null;
    }

    Long getLong(long min, long max,boolean needed) {
        if (needed || shouldSetValue()) {
            long i = random.nextLong();
            if (i<min || i>max) {
                i = Math.abs(i)
                return min + (i % (max-min));
            }
            else
                return i;
        }
        else
            return null;
    }
    
    Long getLongFromList(List<Long> elems,boolean needed) {
        if (elems==null) return null;
            if (needed || shouldSetValue()) {
            int size = elems.size();
            int i = random.nextInt();
            if (i<0)
                i = Math.abs(i)
            i = i % size;
            return elems[i];
        }
        else
            return null;
    }

    // t_string,
    String getString(boolean needed) {
        if (needed || shouldSetValue()) {
            int len = random.nextInt();
            if (len<0)
                len = Math.abs(len)
            if (len > defaultMaxStringLength)
            len = len % defaultMaxStringLength;
            if (len==0) return null;
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<len;i++) {
                sb.append(getRandomChar(i));
            }            
            return sb.toString();
        }
        else
            return null;
    }

    String getString(int minLen,int maxLen,boolean needed) {
        if (needed || shouldSetValue()) {
            int len = random.nextInt();
            if (len<0)
                len = Math.abs(len)
            if (len < minLen || len > maxLen) {
                len = minLen + (len % (maxLen - minLen));
            }
            if (len==0) return null;
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<len;i++) {
                sb.append(getRandomChar(i));
            }            
            return sb.toString();
        }
        else
            return null;
    }

    
    String getStringFromList(List<String> elems,boolean needed) {
        if (elems==null) return null;
        if (needed || shouldSetValue()) {
            int size = elems.size();
            int i = random.nextInt();
            if (i<0)
                i = Math.abs(i);
            i = i % size;
            return elems[i];
        }
        else
            return null;
    }

    // t_key - not implemented yet
    
    // t_boolean,
    Boolean getBool(boolean needed) {
        if (needed || shouldSetValue()) {
            return random.nextBoolean();
        }
        else
            return null;
    }

    // t_date,
    Date getDate(String min,String max,boolean needed,DateFormat f) {
        Date d = getTimestamp(min,max,needed,f);
        if (d!=null) {
            d = d.clearTime()
        }
        return d;
    }

    Date getDate(Date min,Date max,boolean needed) {
        Date d = getTimestamp(min,max,needed);
        if (d!=null) {
            d = d.clearTime()
        }
        return d;
    }

    Date getDate(boolean needed) {
        Date d = getTimestamp(needed);
        if (d!=null) {
            d = d.clearTime()
        }
        return d;
    }
    
    Date getDateFromList(List<Date> elems,boolean needed) {
        Date d = getTimestampFromList(elems,needed);
        if (d!=null) {
            d = d.clearTime()
        }
        return d;
    }

    Date getDateFromList(List<String> elems,boolean needed,DateFormat f) {
        Date d = getTimestampFromList(elems,needed,f);
        if (d!=null) {
            d = d.clearTime()
        }
        return d;
    }

    // t_timestamp,
    Date getTimestamp(String min,String max,boolean needed,DateFormat f) {
        if (f==null) return null;
        Date minD = min==null ? null : f.parse(min); 
        Date maxD = max==null ? null : f.parse(max); 
        return getDate(minD,maxD,needed);
    }

    Date getTimestamp(Date min,Date max,boolean needed) {
        if (needed || shouldSetValue()) {
            if (min!=null && max!=null) {
                long lMax = max.getTime();
                long lMin = min.getTime();
                long diff = lMax - lMin;
                long newL = random.nextLong();
                if (newL<0)
                    Math.abs(newL)
                newL = newL % diff;
                Date ret = new Date();
                ret.setTime(lMin+newL);
                return ret;
            }
            else if (max==null && min!=null) {
                long lMin = min.getTime();
                long newL = random.nextLong();
                if (newL<0)
                    Math.abs(newL)
                newL = newL % defaultMaxDateDiff;
                Date ret = new Date();
                ret.setTime(lMin+newL);
                return ret;            
            }
            else if (max!=null && min==null) {
                long lMax = max.getTime();
                long newL = random.nextLong();
                if (newL<0)
                    Math.abs(newL)
                newL = newL % defaultMaxDateDiff;
                Date ret = new Date();
                ret.setTime(lMax-newL);
                return ret;

            }
            else {
                long newL = random.nextLong();
                boolean bSub = newL < 0;
                if (bSub)
                    Math.abs(newL)
                newL = newL % defaultMaxDateDiff;
                Date ret = new Date();
                long l = ret.getTime();
                if (bSub) {
                    ret.setTime(l-newL);
                }
                else {
                    ret.setTime(l+newL);                    
                }
                return ret;                
            }
        }
        else
            return null;
    }

    Date getTimestamp(boolean needed) {
        return getDate(null,null,needed);
    }

    
    Date getTimestampFromList(List<Date> elems,boolean needed) {
        if (elems==null) return null;
        if (needed || shouldSetValue()) {
            int size = elems.size();
            int i = random.nextInt();
            if (i<0)
                i = Math.abs(i);
            i = i % size;
            return elems[i];
        }
        else
            return null;
    }

    Date getTimestampFromList(List<String> elems,boolean needed,DateFormat df) {
        if (elems==null) return null;
        if (needed || shouldSetValue()) {
            int size = elems.size();
            int i = random.nextInt();
            if (i<0)
                i = Math.abs(i);
            i = i % size;
            return df.parse(elems[i]);
        }
        else
            return null;
    }

    // t_geo - not implemented yet
 
    // t_money,
    BigDecimal getMoney(String min,String max,boolean needed) {
        if (needed || shouldSetValue()) {
            BigDecimal minBd = new BigDecimal(min);
            BigDecimal maxBd = new BigDecimal(max);
            
            double d = random.nextDouble();
            if (d<0) {
                d = Math.abs(d);
            }
            BigDecimal ret = new BigDecimal(d);
            BigDecimal diff = maxBd.subtract(minBd);
            if (ret.compareTo(diff)>0) {
                ret = diff.remainder(ret);
                if (ret.compareTo(new BigDecimal(0)) < 0 ) {
                    ret = ret.abs();
                }                    
            }            
            BigDecimal ret2 = minBd.add(ret);
            return ret2.setScale(2);            
        }
        else
            return null;
    }

    BigDecimal getMoney(BigDecimal min,BigDecimal max,boolean needed) {
        if (needed || shouldSetValue()) {
            double d = random.nextDouble();
            if (d<0) {
                d = Math.abs(d);
            }
            BigDecimal ret = new BigDecimal(d);
            BigDecimal diff = max.subtract(min);
            if (ret.compareTo(diff)>0) {
                ret = diff.remainder(ret);
                if (ret.compareTo(new BigDecimal(0)) < 0 ) {
                    ret = ret.abs();
                }                    
            }            
            BigDecimal ret2 = min.add(ret);
            return ret2.setScale(2,RoundingMode.HALF_EVEN);            
        }
        else
            return null;
    }

    BigDecimal getMoney(boolean needed) {
        if (needed || shouldSetValue()) {
            double d = random.nextDouble();
            BigDecimal ret = new BigDecimal(d);
            return ret.setScale(2,RoundingMode.HALF_EVEN);            
        }
        else
            return null;
    }

    BigDecimal getMoneyFromStrList(List<String> elems,boolean needed) {
        if (elems==null) return null;
        if (needed || shouldSetValue()) {
            int size = elems.size();
            int i = random.nextInt();
            if (i<0)
                i = Math.abs(i);
            i = i % size;
            return elems[i];
        }
        else
            return null;
    }

    BigDecimal getMoneyFromList(List<BigDecimal> elems,boolean needed) {
        if (elems==null) return null;
        if (needed || shouldSetValue()) {
            int size = elems.size();
            int i = random.nextInt();
            if (i<0)
                i = Math.abs(i);
            i = i % size;
            return elems[i];
        }
        else
            return null;
    }

    
    // t_meters,
    // t_milimeters,
    // t_kilometers,
    // t_kmh,
    // t_prozent,
    // t_hour,
    // t_min,
    // t_sec,
    // t_time,Int
    // t_volt,
    // t_float,

    Double getDouble(boolean needed) {
        if (needed || shouldSetValue()) {
            return random.nextDouble();
        }
        else
            return null;
    }

    Double getDouble(Double min, Double max, boolean needed) {
        if (needed || shouldSetValue()) {
            int i = random.nextDouble();
            if (i<min || i>max) {
                i = Math.abs(i)            
                return min + (i % (max-min));
            }
            else
                return i;
        }
        else
            return null;
    }

    Double getDoubleFromList(List<Double> elems, boolean needed) {
        if (elems==null) return null;
            if (needed || shouldSetValue()) {
            int size = elems.size();
            int i = random.nextInt();
            if (i<0)
                i = Math.abs(i)
            i = i % size;
            return elems[i];
        }
        else
            return null;
    }
    private static final Logger log = LoggerFactory.getLogger(TestDataHelper.class);
}

class Restr_Int_MinMax {
    int min;
    int max;
    Restr_Int_MinMax(int min, int max) {
      this.min = min;
      this.max = max;
    }
}

class Restr_Int_List {
    List<Integer> elems;
    Restr_Int_List (List<Integer> e) {
        this.elems = e;
    }
}

class Restr_Long_MinMax{
    long min;
    long max;
    Restr_Long_MinMax(long min, long max) {
      this.min = min;
      this.max = max;
    }
}

class Restr_Long_List {
    List<Long> elems;
    Restr_Long_List (List<Long> e) {
        this.elems = e;
    }
}

class Restr_String_MinMaxLen{
    int minLen;
    int maxLen;
    Restr_String_MinMaxLen(int minLen, int maxLen) {
      this.minLen = minLen;
      this.maxLen = maxLen;
    }
}

class Restr_String_List {
    List<String> elems;
    Restr_String_List (List<String> e) {
        this.elems = e;
    }
}

class Restr_Double_MinMax{
    double min;
    double max;
    Restr_Double_MinMax(double min, double max) {
      this.min = min;
      this.max = max;
    }
}

class Restr_Double_List {
    List<Double> elems;
    Restr_Double_List (List<Double> e) {
        this.elems = e;
    }
}

class Restr_Date_MinMax_Str {
    String min;
    String max;
    DateFormat f;

    Restr_Date_MinMax_Str(String min,String max,DateFormat f) {
      this.min = min;
      this.max = max;
      this.f = f;
    }
}

class Restr_Date_MinMax {
    Date min;
    Date max;

    Restr_Date_MinMax(Date min,Date max) {
      this.min = min;
      this.max = max;
    }
}

class Restr_Date_List_Str {
    List<String> elems;
    DateFormat f;

    Restr_Date_List_Str(List<String> elems,DateFormat f) {
      this.elems = elems;
      this.f = f;
    }
}

class Restr_Date_List {
    List<Date> elems;

    Restr_Date_List(List<Date> elems) {
      this.elems = elems;
    }
}

class Restr_Timestamp_MinMax_Str extends Restr_Date_MinMax_Str {
}

class Restr_Timestamp_MinMax extends Restr_Date_MinMax {
}

class Restr_Timestamp_List_Str extends Restr_Date_List_Str {
}

class Restr_Timestamp_List extends Restr_Date_List {
}


class Restr_Money_MinMax_Str {
    String min;
    String max;

    Restr_Money_MinMax_Str(String min,String max) {
      this.min = min;
      this.max = max;
    }
}

class Restr_Money_MinMax {
    BigDecimal min;
    BigDecimal max;

    Restr_Money_MinMax(Date min,Date max) {
      this.min = min;
      this.max = max;
    }
}

class Restr_Money_List_Str {
    List<String> elems;

    Restr_Money_List_Str(List<String> elems) {
      this.elems = elems;
    }
}

class Restr_Money_List {
    List<BigDecimal> elems;

    Restr_Money_List(List<BigDecimal> elems) {
      this.elems = elems;
    }
}
