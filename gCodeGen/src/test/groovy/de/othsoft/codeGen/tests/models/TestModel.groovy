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
 * @author hulk
 */
class TestModel_v1 extends DataModel {
    /**
     * Das ist der zweite Versuch eine sprechende und erweiterbare Modellbeschreibung hinzubekommen
     */
    TestModel_v1() {
        // Der Name des Modell ergibt sich aus dem Namen der Klasse
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
            attrib ( name:'text2', type:AttribType.t_string )
            attrib ( name:'testStatus', type:AttribType.t_int )
            ref ( refName:'Tab1' ).
                descr = 'Auch eine Reference kann interessant sein'


        entity ( name:'Tab3' )
            attrib ( name:'start', type:AttribType.t_timestamp )
            attrib ( name:'ende', type:AttribType.t_timestamp )
            ref ( refName:'Tab2', name:'refauftab2' )

        m2nRel ( refName1:'Tab1', refName2:'Tab2' ).
                descr = 'Verknüpft irgend etwas'
        m2nRel ( refName1:'Tab3', refName2:'Tab1',name:'MeinZweitesM2N' )
        
        view( name:'TestView' ).
                descr = 'Ja, ich habe auch einen View'
            attrib ( name:'col1', type:AttribType.t_timestamp )
            attrib ( name:'col2', type:AttribType.t_timestamp )
            
        // Dieser Aufruf wird benötigt um die Referenzen mit den zugehörigen Objekten zu verknüpfen.
        // Zusätzlich überprüft er ob sich alle Referenzen auf auflösen lassen.
        resolveReferences()
    }
}

class TestModel_WithoutEntities extends DataModel {
    TestModel_WithoutEntities() {
        version=1
        shortName='EMv1'
        
        view( name:'TestView' )
            attrib ( name:'col1', type:AttribType.t_timestamp )
            attrib ( name:'col2', type:AttribType.t_timestamp )
            
        resolveReferences()
    }
}

class TestModel_WithoutM2N extends DataModel {
    TestModel_WithoutM2N() {
        version=1
        shortName='EMv1'
        
        entity ( name:'Tab1')
            attrib ( name:'name', type:AttribType.t_string,
                    visKey:true,
                    since:1 )
            attrib ( name:'caption1', type:AttribType.t_string )
            attrib ( name:'caption2', type:AttribType.t_string )

        entity (name:'Tab2' )
            attrib ( name:'text1', type:AttribType.t_string )
            attrib ( name:'text2', type:AttribType.t_string )
            attrib ( name:'testStatus', type:AttribType.t_int )
            ref ( refName:'Tab1' )


        entity ( name:'Tab3' )
            attrib ( name:'start', type:AttribType.t_timestamp )
            attrib ( name:'ende', type:AttribType.t_timestamp )
            ref ( refName:'Tab2', name:'refauftab2' )

        view( name:'TestView' )
            attrib ( name:'col1', type:AttribType.t_timestamp )
            attrib ( name:'col2', type:AttribType.t_timestamp )
            
        resolveReferences()
    }
}

class TestModel_WithoutViews extends DataModel {
    TestModel_WithoutViews() {
        version=1
        shortName='EMv1'
        
        entity ( name:'Tab1')
            attrib ( name:'name', type:AttribType.t_string,
                    visKey:true,
                    since:1 )
            attrib ( name:'caption1', type:AttribType.t_string )
            attrib ( name:'caption2', type:AttribType.t_string )

        entity (name:'Tab2' )
            attrib ( name:'text1', type:AttribType.t_string )
            attrib ( name:'text2', type:AttribType.t_string )
            attrib ( name:'testStatus', type:AttribType.t_int )
            ref ( refName:'Tab1' )


        entity ( name:'Tab3' )
            attrib ( name:'start', type:AttribType.t_timestamp )
            attrib ( name:'ende', type:AttribType.t_timestamp )
            ref ( refName:'Tab2', name:'refauftab2' )

        m2nRel ( refName1:'Tab1', refName2:'Tab2' )
        m2nRel ( refName1:'Tab3', refName2:'Tab1',name:'MeinZweitesM2N' )
                    
        resolveReferences()
    }
}

class TestModel_ViewsOnly extends DataModel {
    TestModel_ViewsOnly() {
        version=1
        shortName='EMv1'
        
        view( name:'TestView' )
            attrib ( name:'col1', type:AttribType.t_timestamp )
            attrib ( name:'col2', type:AttribType.t_timestamp )
            
        resolveReferences()
    }
}

class TestModel_MissingViewAttribType extends DataModel {
    TestModel_MissingViewAttribType() {
        // Der Name des Modell ergibt sich aus dem Namen der Klasse
        version=1
        shortName='EMv1'
        
        view( name:'TestView' )
            attrib ( name:'col1' ) // type fehlt
            attrib ( name:'col2', type:AttribType.t_timestamp )
            
        resolveReferences()
    }
}

class TestModel_MissingViewAttribName extends DataModel {
    TestModel_MissingViewAttribName() {
        // Der Name des Modell ergibt sich aus dem Namen der Klasse
        version=1
        shortName='EMv1'
        
        view( name:'TestView' )
            attrib ( type:AttribType.t_string ) // type fehlt
            attrib ( name:'col2', type:AttribType.t_timestamp )
            
        resolveReferences()
    }
}

class TestModel_MissingViewName extends DataModel {
    TestModel_MissingViewName() {
        version=1
        shortName='EMv1'
        
        view( since:1) // View hat keinen Namen
            attrib ( name:'col1', type:AttribType.t_timestamp )
            attrib ( name:'col2', type:AttribType.t_timestamp )
            
        resolveReferences()
    }
}


class TestModel_MissingEntityAttribName extends DataModel {
    TestModel_MissingEntityAttribName () {
        version=1
        shortName='EMv1'
        
        entity ( name:'Tab1')
            attrib ( name:'name', type:AttribType.t_string,
                    visKey:true,
                    since:1 )
            attrib ( type:AttribType.t_string ) // hat keinen Namen
            attrib ( name:'caption2', type:AttribType.t_string )

        resolveReferences()
    }
}

class TestModel_MissingEntityAttribType extends DataModel {
    TestModel_MissingEntityAttribType () {
        version=1
        shortName='EMv1'
        
        entity ( name:'Tab1')
            attrib ( name:'name', type:AttribType.t_string,
                    visKey:true,
                    since:1 )
            attrib ( name: 'test' ) // hat keinen Typ
            attrib ( name:'caption2', type:AttribType.t_string )

        resolveReferences()
    }
}


class TestModel_MissingEntityName extends DataModel {
    TestModel_MissingEntityName () {
        version=1
        shortName='EMv1'
        
        entity ( since:1 )
            attrib ( name:'name', type:AttribType.t_string,
                    visKey:true,
                    since:1 )
            attrib ( name:'caption1', type:AttribType.t_string )
            attrib ( name:'caption2', type:AttribType.t_string )

        resolveReferences()
    }
}

class TestModel_WrongListType extends DataModel {
    TestModel_WrongListType () {
        version=1
        shortName='EMv1'
        
        entity ( name:'e1', since:1 )
            attrib ( name:'name', type:AttribType.t_string,
                    visKey:true,
                    since:1 )
            attrib ( name:'caption1', type:AttribType.t_string )
            attrib ( name:'caption2', type:AttribType.t_string )
            attrib ( name:'some', type:AttribType.t_str_list )

        view ( name:'v1', since:1 )
            attrib ( name:'name', type:AttribType.t_string,
                    visKey:true,
                    since:1 )
            attrib ( name:'caption1', type:AttribType.t_string )
            attrib ( name:'caption2', type:AttribType.t_string )
            attrib ( name:'other', type:AttribType.t_str_list )
        
        resolveReferences()
    }
}

class TestModel_RightListType extends DataModel {
    TestModel_RightListType () {
        version=1
        shortName='EMv1'
        
        entity ( name:'e1', since:1 )
            attrib ( name:'name', type:AttribType.t_string,
                    visKey:true,
                    since:1 )
            attrib ( name:'caption1', type:AttribType.t_string )
            attrib ( name:'caption2', type:AttribType.t_string )
            attrib ( name:'some', type:AttribType.t_str_list )

        view ( name:'v1', since:1 )
            attrib ( name:'name', type:AttribType.t_string,
                    visKey:true,
                    since:1 )
            attrib ( name:'caption1', type:AttribType.t_string )
            attrib ( name:'caption2', type:AttribType.t_string )
            attrib ( name:'other', type:AttribType.t_string )
        
        resolveReferences()
    }
}
