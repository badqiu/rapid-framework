${gg.setOverride(true)}
${gg.setIgnoreOutput(clazz.className?ends_with('Test') || clazz.className?starts_with('Test'))}
<#if clazz.mavenJavaTestSourceFile??>
${gg.setOutputFile(clazz.mavenJavaTestSourceFile)}
</#if>

package ${clazz.packageName};

import ${clazz.packageName}.*;

import java.util.*;
import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

<#list clazz.propertiesImportClasses as importClass>
import ${importClass.javaType?replace("$", ".")};
</#list>

<#assign classVar = clazz.className?uncap_first>

@RunWith(JMock.class)
public class ${clazz.className}Test{

    private Mockery  context = new JUnit4Mockery(){
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    protected ${genNewJavaTypeExpr(clazz,clazz.className?uncap_first)}

    
    //dependence class
    <#list clazz.properties as prop>
    <#if prop.hasWriteMethod && !prop.propertyType.primitive>
        <#if !prop.propertyType.javaType?starts_with("java")>
    ${prop.propertyType.className} ${prop.name?uncap_first} = context.mock(${prop.propertyType.className}.class);
        <#else>
    ${genNewJavaTypeExpr(prop.propertyType, prop.name?uncap_first)}
        </#if>
    </#if>
    </#list>
    
    @Before
    public void setUp() throws Exception {
        //请将 context.checking(new Expectations(){ }) 相关方法迁移至具体的各个测试方法中.
        
        <#list clazz.properties as prop>
        <#if prop.hasWriteMethod && !prop.propertyType.primitive>
        ${classVar}.set${prop.name?cap_first}(${prop.name?uncap_first});
        </#if>
        </#list>
        
        <#list clazz.properties as prop>
            <#if !prop.propertyType.javaType?starts_with("java")>
                <#list prop.propertyType.publicMethods as method>

        <@genJmockContextChecking prop.name?uncap_first method/>
               </#list>
           </#if>
        </#list>
        
    }
    
    @After
    public void tearDown() throws Throwable{
        context.assertIsSatisfied();
    }
    
    <#list clazz.publicMethods as method>
    <#if isNotPropertyMethod(method.methodName)>
    @Test
    public void test_${method.methodName}() throws Throwable{
        
        <#list method.fieldMethodInvocationSequences as fieldInvoke>
        <@genJmockContextChecking fieldInvoke.field.fieldName fieldInvoke.method/>
        
        </#list>
        
        <#list method.parameters as param>
        ${genNewJavaTypeExpr(param.paramClass,param.name)}
        </#list>
        
        <#if (method.returnType.className=="void")>
        ${clazz.className?uncap_first}.${method.methodName}(<#list method.parameters as param>${param.name}<#if param_has_next>, </#if></#list>);
        <#elseif (method.returnType.array)>
        ${method.returnType.simpleJavaType}[] result = ${clazz.className?uncap_first}.${method.methodName}(<#list method.parameters as param>${param.name} <#if param_has_next>,</#if></#list>);
        <#else>
        ${method.returnType.simpleJavaType?replace("$", ".")} result = ${clazz.className?uncap_first}.${method.methodName}(<#list method.parameters as param>${param.name} <#if param_has_next>,</#if></#list>);
        </#if>
        
        <@genJunitAssert method.returnType />
    }
    
    </#if>
    </#list>
    
}
<#macro genJunitAssert returnType>
    <#if (returnType.className=="void")>
        <#return>
    </#if>
    <#if returnType.booleanType>
        assertTrue(result);
        <#return>
    </#if>
        assertNotNull(result);
    <#if (returnType.array)>
        assertTrue("must be not empty",result.length > 0);
    <#return>
    </#if>
    <#if (returnType.interface)>
        <#if returnType?ends_with("java.util.List")>
        assertFalse("must be not empty",result.isEmpty());
        <#elseif returnType?ends_with("java.util.Map")>
        assertFalse("must be not empty",result.isEmpty());
        <#elseif returnType?ends_with("java.util.Queue")>
        assertFalse("must be not empty",result.isEmpty());      
        <#elseif returnType?ends_with("java.util.Set")>
        assertFalse("must be not empty",result.isEmpty());      
        </#if>
    </#if>
</#macro>

<#macro genJmockContextChecking fieldName method>
		context.checking(new Expectations() {
		    {
		        <#if (method.returnType.className!="void")>
		        ${genNewJavaTypeExpr(method.returnType,'first')}
		        </#if>
		        
		        allowing(${fieldName}).${method.methodName}(<#list method.parameters as param><#if param.paramClass.array>with(any(${param.paramClass.simpleJavaType}[].class))<#else>with(any(${param.paramClass.simpleJavaType}.class))</#if><#if param_has_next>,</#if></#list>);
		        <#if (method.returnType.className!="void")>
		        will(returnValue(first));
		        </#if>
		    }
		});
</#macro>

<#function genNewJavaTypeExpr clazz varName>
    <#local result>
        <#compress>
            <#if (clazz.interface)>
                <#if clazz?ends_with("java.util.List")>
                List ${varName} = new ArrayList();
                <#elseif clazz?ends_with("java.util.Map")>
                Map ${varName} = new HashMap();
                <#elseif clazz?ends_with("java.util.Set")>
                Set ${varName} = new HashSet();
                <#elseif clazz?ends_with("java.util.Queue")>
                Queue ${varName} = new LinkedList();
                <#else>
                ${clazz.simpleJavaType} ${varName} = null;
                </#if>
            <#elseif (clazz.booleanType)>
                boolean ${varName} = true;
            <#elseif clazz?ends_with("java.lang.String")>
            	String ${varName} = "";
            <#elseif (clazz.array)>
                ${clazz.simpleJavaType}[] ${varName} = new ${clazz.simpleJavaType}[]{};
            <#elseif (clazz.primitive)>
                ${clazz.simpleJavaType} ${varName} = 1;        
            <#elseif (clazz.hasDefaultConstructor)>
                ${clazz.simpleJavaType} ${varName} <#if !clazz.primitive>= new ${clazz.simpleJavaType}()</#if>;
            <#else>
                ${clazz.simpleJavaType?replace("$", ".")} ${varName} = null;
            </#if>
        </#compress>
    </#local>
    <#return result>
</#function>

<#function isNotPropertyMethod methodName>
    <#if methodName?starts_with('get') || methodName?starts_with('set') || methodName?starts_with('is') >
        <#list clazz.fields as field>
            <#if methodName?lower_case?ends_with(field.fieldName?lower_case) >
                <#return false>
            </#if>
        </#list>
    </#if>
    <#return true>
</#function>