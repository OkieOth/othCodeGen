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

import de.othsoft.codeGen.types.DataModel
import de.othsoft.codeGen.types.AttribType

/**
 *
 * @author hulk
 */
class ResManModel_v2 extends DataModel {
    ResManModel_v2 () {
        // Der Name des Modell ergibt sich aus dem Namen der Klasse
        version=2
        shortName='rman'
        
        defaultTypes=[
                'name' : AttribType.t_string,
                'bemerkung' : AttribType.t_string,
                'von' : AttribType.t_date,
                'bis' : AttribType.t_date,
                'seit' : AttribType.t_date,
                'datum' : AttribType.t_date,
                'aktiv' : AttribType.t_boolean,
                'preis' : AttribType.t_money,
                'art' : AttribType.t_str_list,
                'prozent' : AttribType.t_prozent
                ]
        
        entity ( name:'Termin').
                descr = 'Alle gespeicherten Termine'
            attrib ( name:'von', type:AttribType.t_timestamp )
            attrib ( name:'bis', type:AttribType.t_timestamp )            
            attrib ( name:'bemerkung' )
            attrib ( name:'periode', type:AttribType.t_str_list )
            ref ( refName:'Kunde' )
            ref ( refName:'KundeKundenGruppe', since:2 )

        entity ( name:'TerminResource').
                descr = 'Welche Resourcen werden mit diesem Termin gebunden'
            attrib ( name:'bemerkung' )
            ref ( refName:'Termin' )
            ref ( refName:'Resource' )

        entity ( name:'Kunde').
                descr = 'Eine Person die eine Resource belegen kann'
            attrib ( name:'aktiv' )
            attrib ( name:'seit' )
            attrib ( name:'bemerkung' )
            ref ( refName:'Person', since:2 )
            
        entity ( name:'KundeKundenGruppe' ).
                descr = 'Zu welchen Gruppen gehört ein Kunde'
            // wird hier noch eine Nummer benötigt?
            attrib ( name:'von' )
            attrib ( name:'bis' )
            attrib ( name:'preis' )
            ref ( refName:'Kunde' )
            ref ( refName:'KundenGruppe' )

        entity ( name:'KundeKundenGruppeZahlung' ).
                descr = 'Wann hat der Kunde seinen Gruppenbeitrag bezahlt'
            attrib ( name:'datum_faellig' )
            attrib ( name:'datum_bezahlt' )
            attrib ( name:'datum_bezahlt_bis' )
            attrib ( name:'preis' )
            ref ( refName:'KundeKundenGruppe' )

        entity ( name:'KundenGruppe').
                descr = 'z.B. Clubmitglieder, 4er-Karte etc'
            attrib ( name:'aktiv' )
            attrib ( name:'name', visKey:true )
            attrib ( name:'bemerkung' )
            attrib ( name:'termin_anzahl', type:AttribType.t_int ).
                descr = 'gibt an zu wie vielen Terminen die bezahlte Mitgliedschaft berechtigt'

        entity ( name:'KundengruppePreis').
                descr = 'Welcher Preis muss für die Mitgliedschaft in der Gruppe bezahlt werden'
            attrib ( name:'von' ).
                    descr = 'von wann gilt dieser Preis'
            attrib ( name:'bis' ).
                    descr = 'bis wann gilt dieser Preis'
            attrib ( name: 'preis_netto' )
            ref ( refName:'Mehrwertsteuer' )

        entity ( name:'Mehrwertsteuer')
            attrib ( name:'von' )
            attrib ( name:'bis' )
            attrib ( name:'prozent' )
        
        entity ( name:'Person')
            attrib ( name:'name' )
            attrib ( name:'zusatz', type:AttribType.t_string)
            attrib ( name:'vorname')
            attrib ( name:'geschlecht', type:AttribType.t_str_list)
            attrib ( name:'geburtsdatum')
            attrib ( name:'bemerkung' )
        
        entity (name:'Adresse').
                descr = 'Adressen zu Personen'
            attrib ( name:'land', type:AttribType.t_str_list )
            attrib ( name:'ort', type:AttribType.t_str_list )
            attrib ( name:'plz', type:AttribType.t_str_list )
            attrib ( name:'str', type:AttribType.t_str_list )
            attrib ( name:'hausnr', type:AttribType.t_string )
            attrib ( name:'zusatz', type:AttribType.t_string )
            attrib ( name:'bemerkung' )
            ref ( refName:'Person' )
            
        entity (name:'Resource').
                descr = 'Zu verwaltende Resource'
            attrib ( name:'name', visKey:true )
            attrib ( name:'art' )
            attrib ( name:'bemerkung' )
            attrib ( name:'aktiv', since:2)

        entity ( name:'Feiertage').
                descr = 'Alle relevanten Feiertage'
            attrib ( name:'datum' )
            attrib ( name:'name' )
            
        entity ( name:'Nutzer', since:2 )
            attrib ( name:'name', visKey:true )
            attrib ( name:'login', type:AttribType.t_string )
            attrib ( name:'pwd', type:AttribType.t_string )
            attrib ( name:'aktiv' )
            
        entity ( name:'Berechtigungsgruppe', since:2 )
            attrib ( name:'name', visKey:true )
            attrib ( name:'bemerkung' )
            attrib ( name:'art' )
            attrib ( name:'aktiv' )
        
        m2nRel ( refName1:'Kunde', refName2:'Person', name: 'Ansprechpartner').
                descr = 'Speichert wer der Ansprechpartner für einen Kunden ist'

        m2nRel ( refName1:'Nutzer', refName2:'Berechtigungsgruppe', since:2 ).
                descr = 'Zu welchen Berechtigungsrollen gehört der Nutzer'

        
        view ( name:'AktiveKunden').
                descr = 'Aktive Kunden'
            attrib ( name:'seit' ).
                descr = 'Seit wann ist die Person Kunde'
            attrib ( name:'bemerkung' )
            
        resolveReferences()
    }
}

