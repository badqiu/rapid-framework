package ${clazz.packageName};
import junit.framework.*;
import ${clazz.packageName}.*;
import java.util.*;

public class ${clazz.className}Test extends TestCase{
    <#--
    <#list clazz.fields as field>
    protected ${field.javaType} ${field.fieldName};
    </#list>
    -->
    protected ${clazz.className} ${clazz.className?uncap_first};
    
    <#list clazz.methods as method>
    public void test_${method.methodName}() {
        <#list method.parameters as param>
        <#if (param.interface || param.array)>
        
        <#else>
        ${param.javaType} ${param.name} <#if !param.primitive>= new ${param.javaType}()</#if>;
        </#if>
        </#list>
        <#if !(method.returnType.className=="void")>
        ${method.returnType.className} ${method.returnType.className?uncap_first} = </#if>
        ${clazz.className?uncap_first}.${method.methodName}(
           <#list method.parameters as param>
               ${param.name} <#if param_has_next>,</#if>
           </#list>
        );
        
        <#if !(method.returnType.className=="void")>
        assertNotNull(${method.returnType.className?uncap_first});
        </#if>
    }
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