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

import de.othsoft.codeGen.requirements.AttribType;

/**
 * This types descripe a common datamodel used for SQL databases
 */

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
    
    /* some comfort stuff ... if no attrib type ist given, then this map ist checked for the right type */
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
        name = name.substring(0,1).toUpperCase()+name.substring(1)
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
        name = name.substring(0,1).toUpperCase()+name.substring(1)

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
        name = name.substring(0,1).toUpperCase()+name.substring(1)
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

        String name = initNeededOrThrowException(params,'name').toLowerCase()
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
        String name = params.name ? params.name : refName + '_id'
        name = name.toLowerCase()
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
    
    static List extractSimpleEntries(List<String> strList,boolean bEntity) {
        if (strList==null) return []
        def ret = []
        strList.each {
            if (!(it ==~ /.*\*.*/ )) {
                def txt = bEntity ? it.substring(0,1).toUpperCase() + it.substring(1) : it.toLowerCase() 
                ret.add(txt)
            }
        }
        return ret
    }
    
    static List extractRegExpEntries(List<String> strList,boolean bEntity) {
        if (strList==null) return []
        def ret = []
        strList.each {
            if (it ==~ /.*\*.*/ ) {
                def txt = bEntity ? it.substring(0,1).toUpperCase() + it.substring(1) : it.toLowerCase() 
                ret.add(txt.replaceAll('\\*','.*'))
            }
        } 
        return ret
    }

    /*
     * clone the currend datamodel
     * @return a copy of current data model
     */
    DataModel cloneAllowedWithDeniedAttribs(List<String> allowedEntities,List<String> deniedAttribs) {
        if (!allowedEntities) throw new CheckModelException('no allowed entities make no sense')
        def regexpEntities=extractRegExpEntries(allowedEntities,true)
        def simpleEntities=extractSimpleEntries(allowedEntities,true)
        def regexpAttribs=extractRegExpEntries(deniedAttribs,false)
        def simpleAttribs=extractSimpleEntries(deniedAttribs,false)
        DataModel ret = cloneBase()
        ret.entities = [:]
        entities.findAll { key, entity ->
            simpleEntities.find { key == it } || regexpEntities.find { key ==~ it }
        }.each { key, entity ->
            ret.entities[key] = entity.cloneDeniedByRestriction(simpleAttribs,regexpAttribs)
        }
        ret.views = [:]
        views.findAll { key, entity ->
            simpleEntities.find { key == it } || regexpEntities.find { key ==~ it }
        }.each {
            ret.views[it.key] = it.value.cloneDeniedByRestriction(simpleAttribs,regexpAttribs)
        }

        ret.m2nRelations = [:]
        m2nRelations.findAll { key, entity ->
            simpleEntities.find { key == it } || regexpEntities.find { key ==~ it }
        }.each {
            // it make no sence to restrict references of m2n relations
            ret.m2nRelations[it.key] = it.value.clone()
        } 
        return ret
    }

    /*
     * clone the currend datamodel
     * @return a copy of current data model
     */
    DataModel cloneAllowedWithAllowedAttribs(List<String> allowedEntities,List<String> allowedAttribs) {
        if (!allowedEntities) throw new CheckModelException('no allowed entities make no sense')
        if (!allowedAttribs) throw new CheckModelException('no allowed attribs make no sense')
        def regexpEntities=extractRegExpEntries(allowedEntities,true)
        def simpleEntities=extractSimpleEntries(allowedEntities,true)
        def regexpAttribs=extractRegExpEntries(allowedAttribs,false)
        def simpleAttribs=extractSimpleEntries(allowedAttribs,false)
        DataModel ret = cloneBase()
        ret.entities = [:]
        entities.findAll { key, entity ->
            simpleEntities.find { key == it } || regexpEntities.find { key ==~ it }
        }.each { key, entity ->
            ret.entities[key] = entity.cloneAllowedByRestriction(simpleAttribs,regexpAttribs)
        }
        ret.views = [:]
        views.findAll { key, entity ->
            simpleEntities.find { key == it } || regexpEntities.find { key ==~ it }
        }.each {
            ret.views[it.key] = it.value.cloneAllowedByRestriction(simpleAttribs,regexpAttribs)
        }

        ret.m2nRelations = [:]
        m2nRelations.findAll { key, entity ->
            simpleEntities.find { key == it } || regexpEntities.find { key ==~ it }
        }.each {
            // it make no sence to restrict references of m2n relations
            ret.m2nRelations[it.key] = it.value.clone()
        } 
        return ret
    }
    
     /*
     * clone the currend datamodel
     * @return a copy of current data model
     */
    DataModel cloneNotDeniedWithDeniedAttribs(List<String> deniedEntities,List<String> deniedAttribs) {
        def regexpEntities=extractRegExpEntries(deniedEntities,true)
        def simpleEntities=extractSimpleEntries(deniedEntities,true)
        def regexpAttribs=extractRegExpEntries(deniedAttribs,false)
        def simpleAttribs=extractSimpleEntries(deniedAttribs,false)
        DataModel ret = cloneBase()
        ret.entities = [:]
        entities.findAll { key, entity ->
            !(simpleEntities.find { key == it } || regexpEntities.find { key ==~ it })
        }.each { key, entity ->
            ret.entities[key] = entity.cloneDeniedByRestriction(simpleAttribs,regexpAttribs)
        }
        ret.views = [:]
        views.findAll { key, entity ->
            !(simpleEntities.find { key == it } || regexpEntities.find { key ==~ it })
        }.each {
            ret.views[it.key] = it.value.cloneDeniedByRestriction(simpleAttribs,regexpAttribs)
        }

        ret.m2nRelations = [:]
        m2nRelations.findAll { key, entity ->
            !(simpleEntities.find { key == it } || regexpEntities.find { key ==~ it })
        }.each {
            // it make no sence to restrict references of m2n relations
            ret.m2nRelations[it.key] = it.value.clone()
        } 
        return ret
    }

    DataModel cloneNotDeniedWithAllowedAttribs(List<String> deniedEntities,List<String> allowedAttribs) {
        def regexpEntities=extractRegExpEntries(deniedEntities,true)
        def simpleEntities=extractSimpleEntries(deniedEntities,true)
        def regexpAttribs=extractRegExpEntries(allowedAttribs,false)
        def simpleAttribs=extractSimpleEntries(allowedAttribs,false)
        DataModel ret = cloneBase()
        ret.entities = [:]
        entities.findAll { key, entity ->
            !(simpleEntities.find { key == it } || regexpEntities.find { key ==~ it })
        }.each { key, entity ->
            ret.entities[key] = entity.cloneAllowedByRestriction(simpleAttribs,regexpAttribs)
        }
        ret.views = [:]
        views.findAll { key, entity ->
            !(simpleEntities.find { key == it } || regexpEntities.find { key ==~ it })
        }.each {
            ret.views[it.key] = it.value.cloneAllowedByRestriction(simpleAttribs,regexpAttribs)
        }

        ret.m2nRelations = [:]
        m2nRelations.findAll { key, entity ->
            !(simpleEntities.find { key == it } || regexpEntities.find { key ==~ it })
        }.each {
            // it make no sence to restrict references of m2n relations
            ret.m2nRelations[it.key] = it.value.clone()
        } 
        return ret
    }
    
    private DataModel cloneBase() {
        DataModel ret = new DataModel()
        ret.version = version
        ret.shortName = shortName
        ret.descr = descr
        ret.defaultTypes = [:]
        defaultTypes.each {
            ret.defaultTypes[it.key] = it.value
        }
        return ret
    }

    /**
     * clone the current datamodel
     */
    DataModel clone() {
        DataModel ret = cloneBase()
        ret.entities = [:]
        entities.each {
            ret.entities[it.key] = it.value.clone()
        }
        ret.views = [:]
        views.each {
            ret.views[it.key] = it.value.clone()
        }

        ret.m2nRelations = [:]
        m2nRelations.each {
            ret.m2nRelations[it.key] = it.value.clone()
        }        
        return ret
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

    boolean equals(def o) {
        if (o==null) return false
        if (!(o instanceof DataModel)) return false        
        if (version!=o.version) return false
        if (shortName!=o.shortName) return false
        if (descr!=o.descr) return false
    
        if (defaultTypes!=o.defaultTypes) return false
        if (entities!=o.entities) return false
        if (m2nRelations!=o.m2nRelations) return false
        if (views!=o.views) return false
        return true
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

    String descr

    String getId() {
        return id
    }
    
    String getNameWithFirstLetterUpper() {
        return name[0].toUpperCase()+name.substring(1)
    }
    
    String getNameWithFirstLetterLower() {
        return name[0].toLowerCase()+name.substring(1)
    }

    boolean equals(def o) {
        if (o==null) 
            return false
        if (!o instanceof BaseModelEntry)
            return false
        if (id!=o.id)
            return false
        if (getNameWithFirstLetterUpper()!=o.getNameWithFirstLetterUpper())
            return false
        return true
    }
}

/**
 * Container for the table of the data model
 */
class Entity extends BaseModelEntry implements IRefCont,Cloneable {
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
    
    private Entity cloneBase() {
        Entity ret = new Entity()
        ret.id = id
        ret.name = name
        ret.since = since
        ret.needed = needed
        ret.descr = descr
        return ret
    }
    
    Entity clone() {
        Entity ret = cloneBase()
        ret.attribs = []
        attribs.each {
            Attrib attrib = it.clone()
            attrib.parent = ret
            ret.attribs.add(attrib)
        }
        ret.refs=[]
        refs.each {
            EntityReference ref = it.clone()
            ref.parent = ret
            ret.refs.add(ref)
            }
        return ret
    }
    
    Entity cloneDeniedByRestriction(List<String> simpleAttribs,List<String> regexpAttribs) {
        Entity ret = cloneBase()
        ret.attribs = []
        ret.refs=[]
        attribs.findAll { attrib ->
            def name = attrib.name
            !(simpleAttribs.find { name == it } || regexpAttribs.find { name ==~ it })
        }.each {
            Attrib attrib = it.clone()
            attrib.parent = ret
            ret.attribs.add(attrib)
        }
        refs.findAll { ref ->
            def name = ref.name
            !(simpleAttribs.find { name == it } || regexpAttribs.find { name ==~ it })
        }.each {
            EntityReference ref = it.clone()
            ref.parent = ret
            ret.refs.add(ref)
            }
        return ret
    }
    
    Entity cloneAllowedByRestriction(List<String> simpleAttribs,List<String> regexpAttribs) {
        Entity ret = cloneBase()
        ret.attribs = []
        ret.refs=[]
        attribs.findAll { attrib ->
            def name = attrib.name
            (simpleAttribs.find { name == it } || regexpAttribs.find { name ==~ it })
        }.each {
            Attrib attrib = it.clone()
            attrib.parent = ret
            ret.attribs.add(attrib)
        }
        refs.findAll { ref ->
            def name = ref.name
            (simpleAttribs.find { name == it } || regexpAttribs.find { name ==~ it })
        }.each {
            EntityReference ref = it.clone()
            ref.parent = ret
            ret.refs.add(ref)
            }
        return ret
    }

    boolean equals(def o) {
        if (o==null) return false
        if (!(o instanceof Entity)) return false        
        if (!super.equals(o)) return false
        if (attribs!=o.attribs) return false
        if (refs!=o.refs) return false
        return true
    }
}

/**
 * container for
 */
class Attrib extends BaseModelEntry implements Cloneable {
    AttribType type
    boolean visKey    
    BaseModelEntry parent

    Attrib clone() {
        Attrib ret = new Attrib()
        ret.id = id
        ret.name = name
        ret.since = since
        ret.needed = needed
        ret.descr = descr
        ret.type = type
        ret.visKey = visKey
        return ret
    }

    boolean equals(def o) {
        if (o==null) return false
        if (!(o instanceof Attrib)) return false
        
        if (!super.equals(o)) return false
        if (type!=o.type) return false
        if (visKey!=o.visKey) return false
        return true
    }
}

class EntityReference extends BaseModelEntry implements Cloneable {
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
    
    EntityReference clone() {
        EntityReference ret = new EntityReference()
        ret.id = id
        ret.name = name
        ret.since = since
        ret.needed = needed
        ret.descr = descr
        ret.refName = refName
        if (entity) {
            ret.entity = entity.clone()
        }
        return ret
    }

    boolean equals(def o) {
        if (o==null) return false
        if (!(o instanceof EntityReference)) return false
        
        if (!super.equals(o)) return false
        if (refName!=o.refName) return false
        if (entity!=o.entity) return false
        return true
    }
}

class M2NRelation extends BaseModelEntry implements ITopLeveEntry, Cloneable {
    EntityReference ref1
    EntityReference ref2

    M2NRelation cloneBase () {
        M2NRelation ret = new M2NRelation()
        ret.id = id
        ret.name = name
        ret.since = since
        ret.needed = needed
        ret.descr = descr
        return ret
    }

    M2NRelation clone() {
        M2NRelation ret = cloneBase ()
        ret.ref1 = ref1.clone()
        ret.ref1.parent = ret
        ret.ref2 = ref2.clone()
        ret.ref2.parent = ret
        return ret
    }

    boolean equals(def o) {
        if (o==null) return false
        if (!(o instanceof M2NRelation)) return false
        
        if (!super.equals(o)) return false
        if (ref1!=o.ref1) return false
        return ref1==o.ref1
    }
}

/**
 * Container for the Views of the data model. Some views are only for lazy access 
 * of complex data structures and some views are representing parts auf the user
 * frontend
 */
class View extends BaseModelEntry implements IAttribCont, Cloneable {
    List<Attrib> attribs=[]

    int attribCount() {
        attribs.size()
    }
    
    View cloneBase() {
        View ret = new View()
        ret.id = id
        ret.name = name
        ret.since = since
        ret.needed = needed
        ret.descr = descr
        return ret
    }
    
    View cloneDeniedByRestriction(List<String> simpleAttribs,List<String> regexpAttribs) {
        View ret = cloneBase()
        ret.attribs = []
        attribs.findAll { attrib ->
            def name = attrib.name
            !(simpleAttribs.find { name == it } || regexpAttribs.find { name ==~ it })
        }.each {
            Attrib attrib = it.clone()
            attrib.parent = ret
            ret.attribs.add(attrib)
        }
        return ret
    }

    View cloneAllowedByRestriction(List<String> simpleAttribs,List<String> regexpAttribs) {
        View ret = cloneBase()
        ret.attribs = []
        attribs.findAll { attrib ->
            def name = attrib.name
            (simpleAttribs.find { name == it } || regexpAttribs.find { name ==~ it })
        }.each {
            Attrib attrib = it.clone()
            attrib.parent = ret
            ret.attribs.add(attrib)
        }
        return ret
    }

    View clone() {
        View ret = cloneBase()
        ret.attribs = []
        attribs.each {
            ret.attribs.add( it.clone() )
        }
        return ret
    }

    boolean equals(def o) {
        if (o==null) return false
        if (!(o instanceof View)) return false
        
        if (!super.equals(o)) return false
        return attribs==o.attribs
    }
}

