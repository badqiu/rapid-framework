<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.repository.converter;

import ${basepackage}.dal.dataobject.${className}DO;
import ${basepackage}.repository.model.${className};
import ${basepackage}.model.enums.*;
import cn.org.rapid_framework.lang.enums.EnumBaseUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;


public class ${className}RepositoryConverter {

    static String[] ignoreProperties = new String[]{<@getIgnoreCopyProperties/>};
    <@generateConvertMethod "${className}","${className}DO"/>
    <@generateConvertMethod "${className}DO","${className}"/>
    
}
        
<#macro generateConvertMethod sourceClassName targetClassName>
    public static ${targetClassName} convert2${targetClassName}(${sourceClassName} source) {
        ${targetClassName} target = new ${targetClassName}();

        BeanUtils.copyProperties(source,target,ignoreProperties);
        
        <#-- enums column -->
        <#list table.columns as column>
        <#if column.enumColumn>
            <#if sourceClassName?ends_with("DO")>
        target.set${column.columnName}(${column.enumClassName}.getByCode(source.get${column.columnName}()));
            <#else>
        target.set${column.columnName}((${column.simpleJavaType})EnumBaseUtils.getCode(source.get${column.columnName}()));
            </#if>
        </#if>
        </#list>

        <#list table.columns as column>
        <#if !column.enumColumn>
        target.set${column.columnName}(source.get${column.columnName}());
        </#if>
        </#list>
        
        return target;
    }

    public static List<${targetClassName}> convert2${targetClassName}List(Iterable<${sourceClassName}> list) {
        List<${targetClassName}> results = new ArrayList<${targetClassName}>();
        for(${sourceClassName} source : list) {
            results.add(convert2${targetClassName}(source));
        }
        return results;
    }

    
</#macro>

<#macro getIgnoreCopyProperties>
<#compress>
<#list table.enumColumns as column>"${column.columnNameFirstLower}"<#if column_has_next>,</#if></#list>
</#compress>
</#macro>