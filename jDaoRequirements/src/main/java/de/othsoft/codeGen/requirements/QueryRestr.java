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

package de.othsoft.codeGen.requirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author eiko
 */
public class QueryRestr<T> {
    String id;
    RestrType type;
    List<T> filterValues;
    String rawSql;

    public QueryRestr() {
    }

    public QueryRestr(String id, RestrType type, List<T> filterValues) {
        this.id = id;
        this.type = type;
        this.filterValues = filterValues;
    }

    public QueryRestr(String id, RestrType type,T filterValue) {
        this.id = id;
        this.type = type;
        this.filterValues = new ArrayList();
        this.filterValues.add(filterValue);        
    }    

    public QueryRestr(String id, RestrType type,String rawSql, List rawValues) {
        this.id = id;
        this.type = type;
        this.rawSql = rawSql;
        this.filterValues = rawValues;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RestrType getType() {
        return type;
    }

    public void setType(RestrType type) {
        this.type = type;
    }

    public List getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(List filterValues) {
        this.filterValues = filterValues;
    }

    public String getRawSql() {
        return rawSql;
    }

    public void setRawSql(String rawSql) {
        this.rawSql = rawSql;
    }
}
