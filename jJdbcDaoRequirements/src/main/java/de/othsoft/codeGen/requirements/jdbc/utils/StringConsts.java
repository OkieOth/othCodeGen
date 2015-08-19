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
package de.othsoft.codeGen.requirements.jdbc.utils;

/**
 *
 * @author eiko
 */
public class StringConsts {
    public static final String ID_RESTR=" id=?";
    public static final String WHERE_ID_SQL=" WHERE id=?";
    public static final String WHERE_SQL=" WHERE ";
    public static final String AND_SQL=" AND ";
    public static final String SQL_COUNT_PART_1="SELECT count(*) AS c FROM (";
    public static final String SQL_COUNT_PART_2=") c";
    public static final String SQL_SELECT_BASE="SELECT ";
    public static final String SQL_SELECT_FROM=" FROM ";
    public static final String SQL_SELECT_LEFTOUTERJOIN=" LEFT OUTER JOIN ";
}
