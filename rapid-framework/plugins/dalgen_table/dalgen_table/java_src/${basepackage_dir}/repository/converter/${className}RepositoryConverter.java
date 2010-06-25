<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.repository.converter;

import ${basepackage}.dal.dataobject.${className}DO;
import ${basepackage}.repository.model.${className};
import ${basepackage}.model.enums.*;

import java.util.ArrayList;
import java.util.List;


public class ${className}RepositoryConverter {

    <@generateConvertMethod "${className}","${className}DO"/>
    <@generateConvertMethod "${className}DO","${className}"/>
    
}
        
<#macro generateConvertMethod sourceClassName targetClassName>
    public static ${targetClassName} convert2${targetClassName}(${sourceClassName} source) {
        ${targetClassName} target = new ${targetClassName}();
    
        <#list table.columns as column>
        <#if column.enumColumn>
        target.set${column.columnName}(source.get${column.columnName}());
        <#else>
        target.set${column.columnName}(source.get${column.columnName}());
        </#if>
        </#list>
        
        return target;
    }

    public static List<${targetClassName}> convert2${targetClassName}List(Iterable<${sourceClassName}> list) {
        List<${targetClassName}> results = new ArrayList();
        for(${sourceClassName} source : list) {
            results.add(convert2${targetClassName}(source));
        }
        return results;
    }

    
</#macro>