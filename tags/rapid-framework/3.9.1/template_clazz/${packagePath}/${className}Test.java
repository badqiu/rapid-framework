${gg.setOverride(false)}
${gg.setIgnoreOutput(clazz.className?ends_with('Test') || clazz.className?starts_with('Test'))}

package ${clazz.packageName};

import org.nuxeo.runtime.test.autowire.annotation.XAutoWire;
import org.nuxeo.runtime.test.autowire.annotation.XMode;
import org.springframework.beans.factory.annotation.Autowired;
import junit.framework.*;
import ${clazz.packageName}.*;
import java.util.*;

import org.nuxeo.runtime.test.autowire.annotation.XAutoWire;
import com.alipay.test.base.BaseSofaTestCase;

<#list clazz.importClasses as importClass>
import ${importClass.javaType};
</#list>


public class ${clazz.className}Test extends BaseSofaTestCase{
    <#--
    <#list clazz.fields as field>
    protected ${field.javaType} ${field.fieldName};
    </#list>
    -->
    
    @Autowired
    @XAutoWire(XAutoWire.BY_NAME)
    protected ${clazz.javaType} ${clazz.className?uncap_first};
    
    public void setUp() throws Exception {
        super.setUp();
        assertNotNull("${clazz.className?uncap_first} must be not null",${clazz.className?uncap_first});
    }
    
    <#list clazz.publicMethods as method>
    <#if isNotPropertyMethod(method.methodName)>
    public void test_${method.methodName}${method_index}() throws Throwable{
        <#list method.parameters as param>
            <#if (param.interface)>
        ${param.javaType} ${param.name} = null;
            <#elseif (param.array)>
        ${param.javaType}[] ${param.name} = null;
            <#elseif (param.primitive)>
        ${param.javaType} ${param.name} = (byte)1;        
            <#elseif (param.paramClass.hasDefaultConstructor)>
        ${param.javaType} ${param.name} <#if !param.primitive>= new ${param.javaType}()</#if>;
            <#else>
        ${param.javaType?replace("$", ".")} ${param.name} = null;
            </#if>
        </#list>
        
        <#if (method.returnType.className=="void")>
        ${clazz.className?uncap_first}.${method.methodName}(<#list method.parameters as param>${param.name}<#if param_has_next>, </#if></#list>);
        <#elseif (method.returnType.clazz.array)>
        ${method.returnType.javaType}[] returnValue = ${clazz.className?uncap_first}.${method.methodName}(<#list method.parameters as param>${param.name} <#if param_has_next>,</#if></#list>);
        <#else>
        ${method.returnType.clazz.name?replace("$", ".")} returnValue = ${clazz.className?uncap_first}.${method.methodName}(<#list method.parameters as param>${param.name} <#if param_has_next>,</#if></#list>);
        assertNotNull(returnValue);
        </#if>
    }
    </#if>
    </#list>
    
    public void set${clazz.className}(${clazz.className} ${clazz.className?uncap_first}) {
        this.${clazz.className?uncap_first} = ${clazz.className?uncap_first};
    }
        
    <#--
    <#list clazz.fields as field>
    public void set${field.fieldName?cap_first}(${field.javaType} ${field.fieldName}) {
        this.${field.fieldName} = ${field.fieldName};
    }
    </#list>
    -->
}

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