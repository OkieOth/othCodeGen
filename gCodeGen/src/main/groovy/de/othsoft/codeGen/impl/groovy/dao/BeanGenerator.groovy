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

package de.othsoft.codeGen.impl.groovy.dao

import de.othsoft.codeGen.types.CodeGenException
import de.othsoft.codeGen.types.DataModel
import de.othsoft.codeGen.requirements.AttribType
import de.othsoft.codeGen.types.ICodeGenImpl
import de.othsoft.codeGen.impl.helper.FileHelper
import de.othsoft.codeGen.impl.java.JavaBeanGeneratorBase
import groovy.text.SimpleTemplateEngine

/**
 * @author hulk
 * This generator produces the basic beans, that are uses by the dao factory
 * To see how it works take a look at the test class @see de.othsoft.codeGen.tests.generators.BeanGenerator_Test
 */
class BeanGenerator extends JavaBeanGeneratorBase implements ICodeGenImpl {
    private final static String defDestPath='src/main/java'
    private final static String defTestPath='src/test/java'
    
    private String buildDefPackageName (DataModel model) {
        return "de.gCodeGen.dao.${model.shortName}"
    }

    void genCode(DataModel model) {
        def params = [packageName:buildDefPackageName(model),
            destPathRoot:defDestPath]
        genCodeNow(model,params)
    }

    void genCode(DataModel model,Map params) {
        if (!params.packageName)
            params.packageName = buildDefPackageName(model)
        if (!params.destPathRoot)
            params.destPathRoot = defDestPath 
        genCodeNow(model,params)
    }

    void genTestCode(DataModel model) {
        def params = [packageName:buildDefPackageName(model),
            destPathRoot:defTestPath]
        genTestCodeNow(model,params)
    }

    void genTestCode(DataModel model,Map params) {
        if (!params.packageName)
            params.packageName = buildDefPackageName(model)
        if (!params.destPathRoot)
            params.destPathRoot = defTestPath 
        genTestCodeNow(model,params)
    }

    private String getDestPath(Map params,String packageName) {
        String destPathRoot=params.destPathRoot
        if (!destPathRoot.endsWith(File.separator))
            destPathRoot+=File.separator
        String packagePath=FileHelper.packageToDirName(packageName)
        String destPath = destPathRoot + packagePath
        if (!destPath.endsWith(File.separator))
        destPath+=File.separator
        FileHelper.createDirIfNotExists(destPath)
        return destPath;
    }

    void genCodeNow(DataModel model,Map params) {            
        String packageName=addGenPackageName(params.packageName)
        String destPath = getDestPath (params,packageName)
        createListBeans(destPath, packageName, model)
        createEntityBeans(destPath, packageName, model)
        createM2NBeans(destPath, packageName, model)
        createViewBeans(destPath, packageName, model)
    }
    
    void genTestCodeNow(DataModel model,Map params) {
        String packageName=addGenPackageName(params.packageName)
        String testPackageName=addGenPackageName(params.packageName)+".tests"
        String destPath = getDestPath (params,testPackageName)
        def className = "Test_Beans_IT"
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(beans_IT)
        def daten = [
            packageName:packageName,
            testPackageName:testPackageName,
            model:model,
            strListType:AttribType.t_str_list,
            className:className]
        def ergebnis = template.make(daten)

        File file=new File("${destPath}${className}.java")        
        file.write(ergebnis.toString())
    }

    protected void createListBeans(String destPath, String destPackage, DataModel model) {
        model.entities*.value.attribs*.findAll { it.type == AttribType.t_str_list }*.each { attrib ->
            def entityName = "${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()}"
            def descr = attrib.descr
            genResultAndWriteToFile(templateListBean,entityName, model, destPackage, destPath,descr,attrib)            
        }
    }
    
    protected void createEntityBeans(String destPath, String destPackage, DataModel model) {
        model.entities*.each {
            def entityName=it.value.getNameWithFirstLetterUpper()
            def descr = it.value.descr
            genResultAndWriteToFile(templateEntityBean,entityName, model, destPackage, destPath,descr,it.value)
        }        
    }

    protected void createM2NBeans(String destPath, String destPackage, DataModel model) {
        model.m2nRelations*.each {
            def entityName=it.value.getNameWithFirstLetterUpper()
            def descr = it.value.descr
            genResultAndWriteToFile(templateM2NBean,entityName, model, destPackage, destPath,descr,it.value)
        }
    }

    protected void createViewBeans(String destPath, String destPackage, DataModel model) {
        model.views*.each {
            def viewName=it.value.getNameWithFirstLetterUpper()
            def descr = it.value.descr
            genResultAndWriteToFile(templateViewBean,viewName, model, destPackage, destPath,descr,it.value)
        }        
    }


    protected String addGenPackageName(String packageName) {
        if (!packageName)
            return 'beans'
        else 
            return "${packageName}.beans"
    }
    
    def beans_IT = '''
package ${testPackageName};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.jdbc.JdbcBeanGenerator
 */

import org.junit.Test;
import org.junit.Assert;

public class ${className} {
<% model.entities.each { entity -> %>
    @Test
    public void test_${entity.value.name}() {
        ${packageName}.${entity.value.name} x = new ${packageName}.${entity.value.name} ();
        Assert.assertNotNull(x);
    }
<% } %>

<% model.entities*.value.attribs*.findAll { it.type == strListType }*.each { attrib -> %>
    @Test
    public void test_${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()} () {
        ${packageName}.${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()} x = new ${packageName}.${attrib.parent.getNameWithFirstLetterUpper()}_${attrib.getNameWithFirstLetterUpper()} ();
        Assert.assertNotNull(x);
    }
<% } %>

<% model.views.each { view -> %>
    @Test
    public void test_${view.value.name}() {
        ${packageName}.${view.value.name} x = new ${packageName}.${view.value.name} ();
        Assert.assertNotNull(x);
    }
<% } %>

<% model.m2nRelations.each { m2n -> %>
    @Test
    public void test_${m2n.value.name}() {
        ${packageName}.${m2n.value.name} x = new ${packageName}.${m2n.value.name} ();
        Assert.assertNotNull(x);
    }
<% } %>
}
'''

        
    def templateEntityBean='''
package ${destPackage};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.BeanGenerator
 */

import java.io.Serializable;
import de.othsoft.codeGen.requirements.beans.UpdatableBean;
<%if (aktElem.attribs*.type.toString().contains('date') ||
    aktElem.attribs*.type.toString().contains('timestamp')) {%>import java.util.Date;
<% } %>
<%if (aktElem.attribs*.type.toString().contains('money')) {%>import java.math.BigDecimal;
<% } %>

<%if (descr) { %>
/**
 * $descr
 */<%}%>
public class ${className} extends UpdatableBean<${className}> implements Serializable {
    static final long serialVersionUID = ${model.version}L;\n\

    public final static String ID="${aktElem.id}";
    public final static String ID_ID="${aktElem.id}_a0";
    <% aktElem.attribs.each { attrib -> %>
    public final static String ID_${attrib.name.toUpperCase()}="${attrib.id}"; 
    <% } %>
    <% aktElem.refs.each { ref -> %>
    public final static String ID_${ref.name.toUpperCase()}="${ref.id}";
    <% } %>


    <% aktElem.attribs.each { attrib -> if (attrib.descr) { %>
    /**
     * ${attrib.descr}
     */<% } %>
    <% if ( attrib.type == strListType ) { %>
    protected ${typeConvert(attrib.type)} ${attrib.name}Id;
    protected String ${attrib.name}IdTxt; <% } else { %> 
    protected ${typeConvert(attrib.type)} ${attrib.name}; 
    <% } } %>
    <% aktElem.refs.each { ref -> if (ref.descr) { %>
    /**
     * ${ref.descr}
     */<% } %>
    protected Integer ${ref.getLowerCamelCaseName()};
    <% if (ref.entity.hasVisKey()) { %>
    protected String ${ref.getLowerCamelCaseName()}Txt;
    <% } } %>

    public ${className} () {
        super();
    }
    <% aktElem.attribs.each { attrib -> if ( attrib.type == strListType ) { %>
    public ${typeConvert(attrib.type)} get${attrib.getNameWithFirstLetterUpper()}Id() { return this.${attrib.name}Id; }
    public void set${attrib.getNameWithFirstLetterUpper()}Id(${typeConvert(attrib.type)} v) {
        this.${attrib.name}Id = v;
        setChanged();
    }
    public String get${attrib.getNameWithFirstLetterUpper()}IdTxt() { return this.${attrib.name}IdTxt; }
    public void set${attrib.getNameWithFirstLetterUpper()}IdTxt(String v) {
        this.${attrib.name}IdTxt = v;
        setChanged();
    }
    <% } else { %>
    public ${typeConvert(attrib.type)} get${attrib.getNameWithFirstLetterUpper()}() { return this.${attrib.name}; }
    public void set${attrib.getNameWithFirstLetterUpper()}(${typeConvert(attrib.type)} v) {
        this.${attrib.name} = v;
        setChanged();
    }
    <% } } %>
    <% aktElem.refs.each { ref -> %>
    public Integer get${ref.getUpperCamelCaseName()}() { return this.${ref.getLowerCamelCaseName()}; }
    public void set${ref.getUpperCamelCaseName()}(Integer v) {
        this.${ref.getLowerCamelCaseName()} = v;
        setChanged();
    }
    <% if (ref.entity.hasVisKey()) { %>
    public String get${ref.getUpperCamelCaseName()}Txt() { return this.${ref.getLowerCamelCaseName()}Txt; }
    public void set${ref.getUpperCamelCaseName()}Txt(String v) {
        this.${ref.getLowerCamelCaseName()}Txt = v;
        setChanged();
    }
    <% } } %>

    public ${className} clone() {
        ${className} newObj = new ${className}();
    <% aktElem.attribs.each { attrib -> if ( attrib.type == strListType ) { %>
        newObj.set${attrib.getNameWithFirstLetterUpper()}Id(${attrib.getNameWithFirstLetterLower()}Id);
        newObj.set${attrib.getNameWithFirstLetterUpper()}IdTxt(${attrib.getNameWithFirstLetterLower()}IdTxt);
    <% } else { %>
        newObj.set${attrib.getNameWithFirstLetterUpper()}(${attrib.getNameWithFirstLetterLower()});
    <% } } %>
    <% aktElem.refs.each { ref -> %>
        newObj.set${ref.getUpperCamelCaseName()}(${ref.getLowerCamelCaseName()});
    <% if (ref.entity.hasVisKey()) { %>
        newObj.set${ref.getUpperCamelCaseName()}Txt(${ref.getLowerCamelCaseName()}Txt);
    <% } } %>
        newObj.resetChangedWithoutSaveOriginalState();
        return newObj;
    }
}
'''    

    def templateListBean='''
package ${destPackage};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.BeanGenerator
 */

import java.io.Serializable;
import de.othsoft.codeGen.requirements.beans.UpdatableBean;

<%if (descr) { %>
/**
 * $descr
 */<%}%>
public class ${className} extends UpdatableBean<${className}> implements Serializable {
    static final long serialVersionUID = ${model.version}L;

    public final static String ID="${aktElem.id}";
    public final static String ID_ID="${aktElem.id}_a0";
    public final static String ID_BEZ="${aktElem.id}_a1";
    public final static String ID_LANG="${aktElem.id}_a2";
    public final static String ID_AKTIV="${aktElem.id}_a3";
    public final static String ID_REIHENF="${aktElem.id}_a4";

    /** Bezeichnung */
    protected String bez;

    /** Langbezeichnung */
    protected String lang;

    /** der Listeneintrag kann noch aktiv verwendet werden */
    protected Boolean aktiv;

    /** Reihenfolge in der die Elemente angezeigt werden sollen */
    protected Integer reihenf;

    public ${className}() {
        super();
    }

    public String getBez() {
        return bez;
    }

    public void setBez(String bez) {
        this.bez = bez;
        setChanged();
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
        setChanged();
    }

    public Boolean getAktiv() {
        return aktiv;
    }

    public void setAktiv(Boolean aktiv) {
        this.aktiv = aktiv;
        setChanged();
    }

    public Integer getReihenf() {
        return reihenf;
    }

    public void setReihenf(Integer reihenf) {
        this.reihenf = reihenf;
        setChanged();
    }

    public ${className} clone() {
        ${className} newObj = new ${className}();
        newObj.setBez(bez);
        newObj.setLang(lang);
        newObj.setAktiv(aktiv);
        newObj.setReihenf(reihenf);\n\
        newObj.resetChangedWithoutSaveOriginalState();
        return newObj;
    }
}
'''    

    def templateViewBean='''
package ${destPackage};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.BeanGenerator
 */

import java.io.Serializable;
import de.othsoft.codeGen.requirements.beans.IdBean;
<%if (aktElem.attribs*.type.toString().contains('date') ||
    aktElem.attribs*.type.toString().contains('timestamp')) {%>import java.util.Date;
<% } %>
<%if (aktElem.attribs*.type.toString().contains('money')) {%>import java.math.BigDecimal;
<% } %>

<%if (descr) { %>
/**
 * $descr
 */<%}%>
public class ${className} extends IdBean implements Serializable {
    static final long serialVersionUID = ${model.version}L;
    public final static String ID="${aktElem.id}";
    public final static String ID_ID="${aktElem.id}_a0";
    <% aktElem.attribs.each { attrib -> %>
    public final static String ID_${attrib.name.toUpperCase()}="${attrib.id}"; 
    <% } %>

    <% aktElem.attribs.each { attrib -> if (attrib.descr) { %>
    /**
     * ${attrib.descr}
     */<%}%> 
    protected ${typeConvert(attrib.type)} ${attrib.name}; 
    <% } %>

    public ${className} () {
        super();
    }
    <% aktElem.attribs.each { attrib -> %> 
    public ${typeConvert(attrib.type)} get${attrib.getNameWithFirstLetterUpper()}() { return this.${attrib.name}; }
    public void set${attrib.getNameWithFirstLetterUpper()}(${typeConvert(attrib.type)} v) {
        this.${attrib.name} = v;
        setChanged();
    }
    <% } %>

}
'''    

    def templateM2NBean='''
package ${destPackage};

/*
 * This file is generated. If you change something in this file, the changes are gone away after the next running of the
 * generator.
 * Generator: de.othsoft.codeGen.impl.groovy.dao.BeanGenerator
 */

import java.io.Serializable;
import de.othsoft.codeGen.requirements.CmdData;
import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.UserData;
import de.othsoft.codeGen.requirements.beans.BeanBase;

<%if (descr) { %>
/**
 * $descr
 */<%}%>
public class ${className} extends BeanBase implements Serializable {
    static final long serialVersionUID = ${model.version}L;

    public final static String ID="${aktElem.id}";
    public final static String ID_${aktElem.ref1.name.toUpperCase()}="${aktElem.ref1.id}"; 
    public final static String ID_${aktElem.ref2.name.toUpperCase()}="${aktElem.ref1.id}"; 


    <%if (aktElem.ref1.descr) { %>
    /**
     * ${aktElem.ref1.descr}
     */<%}%>
    protected Integer ${aktElem.ref1.getLowerCamelCaseName()};
    <%if (aktElem.ref2.descr) { %>
    /**
     * ${aktElem.ref2.descr}
     */<%}%>
    protected Integer ${aktElem.ref2.getLowerCamelCaseName()};
    
    public ${className}() {
        super();
    }

    public Integer get${aktElem.ref1.getUpperCamelCaseName()}() {
        return this.${aktElem.ref1.getLowerCamelCaseName()};
    }

    public void set${aktElem.ref1.getUpperCamelCaseName()}(Integer refId) {
        this.${aktElem.ref1.getLowerCamelCaseName()} = refId;
        setChanged();
    }

    public Integer get${aktElem.ref2.getUpperCamelCaseName()}() {
        return this.${aktElem.ref2.getLowerCamelCaseName()};
    }

    public void set${aktElem.ref2.getUpperCamelCaseName()}(Integer refId) {
        this.${aktElem.ref2.getLowerCamelCaseName()} = refId;
        setChanged();
    }

    public void insert(CmdData cmdData,UserData userData) throws DaoException {
        // Dummy-Implementierung - wird in abgeleiteten Klassen überschrieben
    }
    public void delete(CmdData cmdData,UserData userData) throws DaoException {
        // Dummy-Implementierung - wird in abgeleiteten Klassen überschrieben
    }
}
'''    
}


