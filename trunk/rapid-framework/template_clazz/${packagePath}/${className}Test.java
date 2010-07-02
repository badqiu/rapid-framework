${gg.setOverride(false)}
${gg.setIgnoreOutput(clazz.className?ends_with('Test') || clazz.className?starts_with('Test'))}

package ${clazz.packageName};
import junit.framework.*;
import ${clazz.packageName}.*;
import java.util.*;

public class ${clazz.className}Test extends Base${clazz.lastPackageNameFirstUpper}TestCase{
    <#--
    <#list clazz.fields as field>
    protected ${field.javaType} ${field.fieldName};
    </#list>
    -->
    protected ${clazz.className} ${clazz.className?uncap_first};
    
    <#list clazz.publicMethods as method>
    <#if !(method.methodName?starts_with('get') || method.methodName?starts_with('set') || method.methodName?starts_with('is'))>
    public void test_${method.methodName}() {
        <#list method.parameters as param>
            <#if (param.interface)>
        ${param.javaType} ${param.name} = null;
            <#elseif (param.array)>
        ${param.javaType}[] ${param.name} = null;
            <#elseif (param.paramClass.hasDefaultConstructor)>
        ${param.javaType} ${param.name} <#if !param.primitive>= new ${param.javaType}()</#if>;
            <#else>
        ${param.javaType} ${param.name} = null;
            </#if>
        </#list>
        
        <#if (method.returnType.className=="void")>
        ${clazz.className?uncap_first}.${method.methodName}(<#list method.parameters as param>${param.name} <#if param_has_next>,</#if></#list>);
        <#else>
        ${method.returnType.className} returnValue = ${clazz.className?uncap_first}.${method.methodName}(<#list method.parameters as param>${param.name} <#if param_has_next>,</#if></#list>);
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