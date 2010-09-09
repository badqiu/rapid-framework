
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


public class ${clazz.className}Converter {
    
    public ${clazz.className} convert2${clazz.className}(${clazz.className} source) {
        ${clazz.className} target = new ${clazz.className}();
        <#list clazz.properties as prop>
        target.set${prop.name?cap_first}(source.get${prop.name?cap_first}());
        </#list>
    }
    
    public List<${clazz.className}> convert2${clazz.className}(List<${clazz.className}> sourceList) {
        List<${clazz.className}> targetList = new ArrayList();
        for(${clazz.className} source : sourceList) {
            targetList.add(convert2${clazz.className}(source));
        }
        return targetList;
    }
    
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