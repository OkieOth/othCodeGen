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
package de.othsoft.codeGen.requirements.jdbc.utils.impl;

import de.othsoft.codeGen.requirements.jdbc.utils.ISetPagingImpl;

/**
 *
 * @author eiko
 */
public class Psql_SetPagingImpl implements ISetPagingImpl {
    @Override
    public String appendPagingToSql(String sql, int offset, int count) {
        if (count>0) {
            sql += (" LIMIT " + count);
        }
        if (offset>0) {
            sql += (" OFFSET " + offset);
        }
        return sql;
    }
}
