<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.service.converter;

import ${basepackage}.dal.dataobject.${className}DTO;
import ${basepackage}.repository.model.${className};

import java.util.ArrayList;
import java.util.List;


public class ${className}ServiceConverter {

    <@generateConvertMethod "${className}DTO","${className}"/>
    <@generateConvertMethod "${className}","${className}DTO"/>
    
}
        
<#macro generateConvertMethod sourceClassName targetClassName>
    public static ${targetClassName} convert2${targetClassName}(${sourceClassName} source) {
        ${targetClassName} target = new ${targetClassName}();
    
        <#list table.notPkColumns as column>
        target.set${column.columnName}(source.get${column.columnName}());
        </#list>
        
        return target;
    }

    public static List<${targetClassName}> convert2${targetClassName}List(List<${sourceClassName}> list) {
        List<${targetClassName}> results = new ArrayList();
        for(${sourceClassName} source : list) {
            results.add(convert2${targetClassName}(source));
        }
        return results;
    }

    
</#macro>