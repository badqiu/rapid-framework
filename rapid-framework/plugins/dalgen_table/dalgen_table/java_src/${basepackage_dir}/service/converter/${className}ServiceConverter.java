<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.service.converter;

import ${basepackage}.dal.query.${className}Query;
import ${basepackage}.repository.model.${className};
import ${basepackage}.service.dto.${className}DTO;
import ${basepackage}.service.dto.query.${className}QueryDTO;

import java.util.ArrayList;
import java.util.List;


public class ${className}ServiceConverter {

    <@generateConvertMethod "${className}DTO","${className}",false/>
    <@generateConvertMethod "${className}","${className}DTO",false/>
    
    <@generateConvertMethod "${className}QueryDTO","${className}Query",true/>
}
        
<#macro generateConvertMethod sourceClassName targetClassName,isQuery>
    public static ${targetClassName} convert2${targetClassName}(${sourceClassName} source) {
        ${targetClassName} target = new ${targetClassName}();
    
        <#list table.columns as column>
        <#if isQuery && column.isDateTimeColumn>
        target.set${column.columnName}Begin(source.get${column.columnName}Begin());
        target.set${column.columnName}End(source.get${column.columnName}End());
        <#else>
        target.set${column.columnName}(source.get${column.columnName}());
        </#if>
        </#list>
        
        return target;
    }
    
    <#if !isQuery>
    public static List<${targetClassName}> convert2${targetClassName}List(Iterable<${sourceClassName}> list) {
        List<${targetClassName}> results = new ArrayList();
        for(${sourceClassName} source : list) {
            results.add(convert2${targetClassName}(source));
        }
        return results;
    }
    </#if>
    
</#macro>