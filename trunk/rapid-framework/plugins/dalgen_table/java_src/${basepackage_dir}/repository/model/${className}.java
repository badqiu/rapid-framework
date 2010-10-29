<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.repository.model;

import ${basepackage}.model.enums;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

<#include "/java_imports.include">

public class ${className} implements java.io.Serializable {
    private static final long serialVersionUID = -2068875613726748764L;
    
	<#list table.columns as column>
	<#if column.enumColumn>
	private ${column.enumClassName} ${column.columnNameLower};
	<#else>
	private ${column.javaType} ${column.columnNameLower};
	</#if>
	</#list>

<@generateConstructor className/>
<@generateJavaColumns/>

}

<#macro generateJavaColumns>
	<#list table.columns as column>	
	<#if column.enumColumn>
    public void set${column.columnName}(${column.enumClassName} value) {
        this.${column.columnNameLower} = value;
    }
    
    public ${column.enumClassName} get${column.columnName}() {
        return this.${column.columnNameLower};
    }	
	<#else>
	public void set${column.columnName}(${column.javaType} value) {
		this.${column.columnNameLower} = value;
	}
	
	public ${column.javaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	</#if>
	</#list>
</#macro>


