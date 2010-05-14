<#include "/macro.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

public class ${className}Converter {

    <#list table.notPkColumns as column>
    String ${column.columnNameFirstLower} = group.getField("${column.columnNameFirstLower}").getStringValue();
    </#list>
    <#list table.notPkColumns as column>
    target.set${column.columnName}(${column.columnNameFirstLower});
    </#list>
    
	<@generateConvertMethod "","VO"/>
	<@generateConvertMethod "VO",""/>
	
	<@generateConvertMethod "Form",""/>
	<@generateConvertMethod "","Form"/>
}
        
<#macro generateConvertMethod sourceSuffix targetSuffix>
	public ${className}${targetSuffix} convert2${className}${targetSuffix}(${className}${sourceSuffix} source) {
		${className}${targetSuffix} target = new ${className}${targetSuffix}();
	
		<#list table.notPkColumns as column>
		target.set${column.columnName}(source.get${column.columnName}());
		</#list>
		
		return target;
	}
	
</#macro>