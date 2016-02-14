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

package de.othsoft.codeGen.tests.models

import de.othsoft.codeGen.types.*
import de.othsoft.codeGen.requirements.AttribType
/**
 *
 * @author eiko
 */
class TestVisKeyModel  extends DataModel {
    public TestVisKeyModel() {
        version=1
        shortName='EMv1'
        
        entity ( name:'Tab1').
                descr = 'Das ist meine erste Tabelle'
            attrib ( name:'name', type:AttribType.t_string,
                    visKey:true,
                    since:1 )
            attrib ( name:'caption1', type:AttribType.t_string )
            attrib ( name:'caption2', type:AttribType.t_string )

        entity (name:'Tab2' ).
                descr = 'Hier ist Tabelle #2'
            attrib ( name:'text1', type:AttribType.t_string ).
                descr = 'Ein sehr interessante Spalte'
            attrib ( name:'text2', type:AttribType.t_string,
                    visKey:true)
            
            attrib ( name:'testStatus', type:AttribType.t_int )
            ref ( refName:'Tab1' ).
                descr = 'Auch eine Reference kann interessant sein'

        entity ( name:'Tab3' )
            attrib ( name:'start', type:AttribType.t_timestamp )
            attrib ( name:'ende', type:AttribType.t_timestamp )
            ref ( refName:'Tab2', name:'refauftab2' )

        // wrong, has two visKey columns
        entity ( name:'Tab4').
                descr = 'Das ist meine vierte Tabelle'
            attrib ( name:'name', type:AttribType.t_string,
                    visKey:true,
                    since:1 )
            attrib ( name:'caption1', type:AttribType.t_string,
                    visKey:true)
            attrib ( name:'caption2', type:AttribType.t_string )
    }
}

