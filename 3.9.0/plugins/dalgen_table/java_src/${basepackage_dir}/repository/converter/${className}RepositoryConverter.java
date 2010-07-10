<#include "/macro.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.repository.converter;

import java.awt.List;

public class ${className}RepositoryConverter {

    <@generateConvertMethod "","DO"/>
    <@generateConvertMethod "DO",""/>
    
}
        
<#macro generateConvertMethod sourceSuffix targetSuffix>
    public static ${className}${targetSuffix} convert2${className}${targetSuffix}(${className}${sourceSuffix} source) {
        ${className}${targetSuffix} target = new ${className}${targetSuffix}();
    
        <#list table.notPkColumns as column>
        target.set${column.columnName}(source.get${column.columnName}());
        </#list>
        
        return target;
    }

    public static List<${className}${targetSuffix}> convert2${className}${targetSuffix}List(List<${className}${sourceSuffix}> list) {
        List<${className}${targetSuffix}> results = new ArrayList();
        for(${className}${sourceSuffix} source : list) {
            results.add(convert2${className}${targetSuffix}(source));
        }
        return results;
    }
    
</#macro>