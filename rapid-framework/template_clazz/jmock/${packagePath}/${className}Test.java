${gg.setIgnoreOutput(clazz.className?ends_with('Test') || clazz.className?starts_with('Test'))}

package ${clazz.packageName};

import junit.framework.*;
import ${clazz.packageName}.*;
import java.util.*;

import static org.junit.Assert.*;

import java.util.Date;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

<#list clazz.importClasses as importClass>
import ${importClass.javaType};
</#list>

<#assign classVar = clazz.className?uncap_first>

@RunWith(JMock.class)
public class ${clazz.className}Test{

    private Mockery  context = new JUnit4Mockery();
    
    protected ${clazz.simpleJavaType} ${clazz.className?uncap_first} = new ${clazz.simpleJavaType}();
    
    @Before
    public void setUp() throws Exception {
        <#list clazz.properties as prop>
            <#if prop.propertyType.interface>
        final ${prop.propertyType.className} ${prop.name?uncap_first} = context.mock(${prop.propertyType.className}.class);
            <#else>
        final ${prop.propertyType.className} ${prop.name?uncap_first} = null;
            </#if>
        ${classVar}.set${prop.name?cap_first}(${prop.name?uncap_first});
        
        </#list>
        
        <#list clazz.properties as prop>
            <#if prop.propertyType.interface>
                <#list prop.propertyType.publicMethods as method>
                    
        context.checking(new Expectations() {
            {
                <#if (method.returnType.className!="void")>
                ${genNewJavaTypeExpr(method.returnType,'first'+method.returnType.className)}
                ${genNewJavaTypeExpr(method.returnType,'second'+method.returnType.className)}
                </#if>
                
                allowing(${prop.name?uncap_first}).${method.methodName}(<#list method.parameters as param>with(any(${param.paramClass.simpleJavaType}.class))<#if param_has_next>,</#if></#list>);
                <#if (method.returnType.className!="void")>
                will(onConsecutiveCalls(returnValue(first${method.returnType.className}), returnValue(second${method.returnType.className})));
                </#if>
            }
        });
               </#list>
            
           </#if>
        </#list>
        
    }
    
    @After
    public void tearDown() throws Throwable{
    }
    
    <#list clazz.publicMethods as method>
    <#if isNotPropertyMethod(method.methodName)>
    @Test
    public void test_${method.methodName}() throws Throwable{
        <#list method.parameters as param>
        ${genNewJavaTypeExpr(param.paramClass,param.name)}
        </#list>
        
        <#if (method.returnType.className=="void")>
        ${clazz.className?uncap_first}.${method.methodName}(<#list method.parameters as param>${param.name}<#if param_has_next>, </#if></#list>);
        <#elseif (method.returnType.clazz.array)>
        ${method.returnType.simpleJavaType}[] result = ${clazz.className?uncap_first}.${method.methodName}(<#list method.parameters as param>${param.name} <#if param_has_next>,</#if></#list>);
        <#else>
        ${method.returnType.simpleJavaType?replace("$", ".")} result = ${clazz.className?uncap_first}.${method.methodName}(<#list method.parameters as param>${param.name} <#if param_has_next>,</#if></#list>);
        assertNotNull(result);
        </#if>
    }
    
    </#if>
    </#list>
    
}

<#function genNewJavaTypeExpr clazz varName>
    <#local result>
        <#compress>
            <#if (clazz.interface)>
                ${clazz.simpleJavaType} ${varName} = new ${clazz.simpleJavaType}();
            <#elseif (clazz.array)>
                ${clazz.simpleJavaType}[] ${varName} = new ${clazz.simpleJavaType}()[];
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