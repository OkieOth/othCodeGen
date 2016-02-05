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
/**
 *
 * @author eiko
 */
class TestDataHelper implements ITestDataHelper {
    public def charBase = ['a','b','c','d','e','f','g',' ','h','i','j','k','l',' ','m','n','o','p','q','r','s',' ','t','u','v','w','x','y',' ','z','ä','ö','ü',' ','ß',
                            'A','B','C','D','E','F','G',' ','H','I','J','K','K',' ','M','N','O','P','Q','R','S',' ','T','U','V','W','X','Y',' ','Z','Ä','Ö','Ü',' '];
    private Random random = new Random();

    int defaultMaxStringLength=255;
    long defaultMaxDateDiff=10000;
    
    private int entityRowCount=1000;
    
    void setEntityRowCount(int c) {
        this.entityRowCount = c;
    }
    
    int getEntityRowCount(String entityName) {
        // TODO
        return this.entityRowCount;
    }
    
    void initWithTestData(Object o,String entityName,String attribName) {
        // TODO - initialize the attrib given with 'attribName' of Object o with
        // a random value
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
}