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

package de.othsoft.codeGen.types

/**
 * This types descripe a common datamodel used for SQL databases
 */

enum AttribType {
    t_int,
    t_long,
    t_string,
    t_key,
    t_boolean,
    t_date,
    t_timestamp,
    t_geo,
    t_money,
    t_meters,
    t_milimeters,
    t_kilometers,
    t_kmh,
    t_prozent,
    t_hour,
    t_min,
    t_sec,
    t_time,
    t_volt,
    t_float,
    t_str_list
}

/**
 * Base container for the model entries
 * @author hulk
 */

/**
 * Dummy-Interface for typing the last used Item of the datamodel ...
 * ... make the definition of models easier
 */
interface ITopLeveEntry {
    String getId()
}

interface IAttribCont extends ITopLeveEntry {
    int attribCount()
    List getAttribs()
}

interface IRefCont extends IAttribCont {
    int refCount()
    List getRefs()
}

class DataModel {
    protected int version
    protected String shortName
    protected String descr
    
    /** Kompfort-Ding ... wird bei Attributen kein Typ angegeben, wird über diese Map versucht der Typ anhand eines Namenspatterns zu ermittlen */
    protected Map defaultTypes=[:]
    
    Map entities=[:]
    Map m2nRelations=[:]
    Map views=[:]
    
    ITopLeveEntry lastUsed

    /**
     * add a new entity to the data model
     */
    def entity(Map params) {
        String name = initNeededOrThrowException(params,'name')
        int since = params.since ? params.since : 1
        String id = "e${entities.size() + 1}"

        if (entities."$name" || m2nRelations."$name" || views."$name")
            throw new CheckModelException("entity name '$name' is not unique")

        Entity newEntity = new Entity( name:name, id:id, since:since)
        entities."$name"=newEntity
        lastUsed = newEntity
        newEntity
    }

    /**
     * add a new m2n relation to the data model
     */
    def m2nRel(Map params) {
        String refName1 = initNeededOrThrowException(params,'refName1')
        String refName2 = initNeededOrThrowException(params,'refName2')
        int since = params.since ? params.since : 1
        String name=params.name ? params.name : "$refName1$refName2"
        String id = "m${m2nRelations.size() + 1}"

        if (entities."$name" || m2nRelations."$name" || views."$name")
            throw new CheckModelException("m2n name '$name' is not unique")

        EntityReference r1 = new EntityReference(name:refName1.toLowerCase() + '_id',refName:refName1, id: "${id}_r1", since: since);
        EntityReference r2 = new EntityReference(name:refName2.toLowerCase() + '_id',refName:refName2, id: "${id}_r2", since: since);
        M2NRelation newM2N = new M2NRelation( name:name, id:id , ref1:r1, ref2:r2, since:since )
        m2nRelations."$name" = newM2N
        newM2N
    }

    /**
     * add a new view to the data model
     */
    def view(Map params) {
        String name = initNeededOrThrowException(params,'name')
        int since = params.since ? params.since : 1
        String id = "v${views.size() + 1}"

        if (entities."$name" || m2nRelations."$name" || views."$name")
            throw new CheckModelException("entity name '$name' is not unique")

        View newView = new View( name:name, id:id, since:since)
        views."$name"=newView
        lastUsed = newView
        newView
    }
    
    def attrib(Map params) {
        if (!lastUsed)
            throw new CheckModelException("no parent for attrib ${params}")

        if (! lastUsed instanceof IAttribCont)
            throw new CheckModelException("parent of ref ${params} is not IAttribCont")

        String name = initNeededOrThrowException(params,'name')
        if (name == 'id')
            throw new CheckModelException("attrib name 'id' ist not allowed")
        String type = params.type
        if (!type)
            type = getDefaultTypeOrThrowException(name)
            
        if ((type == "t_str_list") && (!(lastUsed instanceof IRefCont )))
            throw new CheckModelException("AttribType.t_str_list of ${name} is only for entities allowed")
        boolean visKey = params.visKey ? true : false
        boolean needed = params.needed ? true : false
        int since = params.since ? params.since : 1
        String id = "${lastUsed.id}_a${lastUsed.attribCount() +1}"
        Attrib newAttrib = new Attrib ( name:name, id:id, type:type, visKey:visKey, since:since, parent:lastUsed, needed:needed )        
        lastUsed.attribs.add(newAttrib)
        newAttrib
    }

    def ref(Map params) {
        if (!lastUsed)
            throw new CheckModelException("no parent for ref ${params}")
            
        if (! lastUsed instanceof IRefCont)
            throw new CheckModelException("parent of ref $params is not IRefCont")

        String refName = initNeededOrThrowException(params,'refName')
        String name = params.name ? params.name : refName.toLowerCase() + '_id'
        boolean needed = params.needed ? true : false
        int since = params.since ? params.since : 1
        String id = "${lastUsed.id}_r${lastUsed.refCount() +1}"
        EntityReference newRef = new EntityReference ( name:name, id:id, refName:refName, since:since, parent:lastUsed, needed:needed )        
        lastUsed.refs.add(newRef)
        newRef
    }

    protected void linkReferenceWithEntity(EntityReference ref) {
        String refName=ref.refName
        List referencedEntityList=entities*.value.findAll({
            it.name==refName
        })
        if (!referencedEntityList) {
            throw new CheckModelException("reference error - no entity with name $refName found")                        
        }
        else {
            ref.entity=referencedEntityList[0]
        }        
    }
    
    /**
     * Links the namly referenced object to the related references
     */
    void resolveReferences() {
        entities*.value.refs.findAll{ it.id }.each({
                // das Ergebnis ist ein Array ... also auf die Array-Elemente referenzieren ...
                it.each({
                    linkReferenceWithEntity(it)
                })
        })
        m2nRelations.each({
                EntityReference ref=it.value.ref1
                def test = ref.dump()
                linkReferenceWithEntity (ref)
                ref=it.value.ref2
                linkReferenceWithEntity (ref)
        })
    }
    
    private AttribType getDefaultTypeOrThrowException(String name) {
        String keyForName=defaultTypes*.key.find({
                name.indexOf(it)!=-1
        })
        if (!keyForName) 
            throw new CheckModelException ("default type needed for attrib='$name', $defaultTypes")
        AttribType type=defaultTypes[keyForName]
        type                    
    }
        
    private def initNeededOrThrowException(Map map,key) throws CheckModelException {
        if (!map."$key")
            throw new CheckModelException ("value needed for key='$key', $map")
        map."$key"
    }    
}

class BaseModelEntry {
    /**
     * internal ID - used for access without names (filter and stuff)
     */
    protected String id
    /**
     * know the real name an you can control it
     */
    protected String name
    /**
     * since what version the entry contains to the model
     */
    protected int since
    
    protected boolean needed
    
    String getId() {
        return id
    }
    
    String getNameWithFirstLetterUpper() {
        return name[0].toUpperCase()+name.substring(1)
    }
    
    String getNameWithFirstLetterLower() {
        return name[0].toLowerCase()+name.substring(1)
    }

    String descr
}

/**
 * Container for the table of the data model
 */
class Entity extends BaseModelEntry implements IRefCont {
    List attribs=[]
    List refs=[]
    
    int attribCount() {
        attribs.size()
    }
    
    int refCount() {
        refs.size()
    }
    
    boolean hasVisKey() {
        def visKeyAttribs = attribs.findAll {attrib -> attrib.visKey==true }
        def visKeyCount = visKeyAttribs.size();
        if (visKeyCount==0) return false;
        if (visKeyCount==1) return true;
        throw new CheckModelException("entity '$name' has more than one visKay")
    }
    
    Attrib getVisKey () {
        def visKeyAttribs = attribs.findAll {attrib -> attrib.visKey==true }
        def visKeyCount = visKeyAttribs.size();
        if (visKeyCount==0) return null;
        if (visKeyCount==1) return visKeyAttribs[0];
        throw new CheckModelException("entity '$name' has more than one visKay")
    }
}

/**
 * container for
 */
class Attrib extends BaseModelEntry {
    AttribType type
    boolean visKey    
    BaseModelEntry parent
}

class EntityReference extends BaseModelEntry {
    String refName
    Entity entity
    BaseModelEntry parent
    
    String getLowerCamelCaseName() {
        if (!name)
            return ''
        String s = name.replaceAll(/_id$/,'Id')
        return s.substring(0,1).toLowerCase()+s.substring(1)
    }

    String getUpperCamelCaseName() {
        if (!name)
            return ''
        String s = name.replaceAll(/_id$/,'Id')
        return s.substring(0,1).toUpperCase()+s.substring(1)
    }
}

class M2NRelation extends BaseModelEntry implements ITopLeveEntry {
    EntityReference ref1
    EntityReference ref2
}

/**
 * Container for the Views of the data model. Some views are only for lazy access 
 * of complex data structures and some views are representing parts auf the user
 * frontend
 */
class View extends BaseModelEntry implements IAttribCont {
    List<Attrib> attribs=[]

    int attribCount() {
        attribs.size()
    }
}
