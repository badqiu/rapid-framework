<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.web.form;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

<#include "/java_imports.include">

/**
<#include "/java_description.include">
 */
public class ${className}Form extends BaseStrutsForm {

<#list table.columns as column>
	<#if column.isDateTimeColumn>
	private String ${column.columnNameLower}String;
	<#else>
	private String ${column.columnNameLower};
	</#if>
</#list>
	
<#list table.columns as column>


<#if column.isDateTimeColumn>
	public void set${column.columnName}String(String value) {
		this.${column.columnNameLower}String = value;
	}
	public String get${column.columnName}String() {
		return this.${column.columnNameLower}String;
	}
<#else>
	public void set${column.columnName}(String value) {
		this.${column.columnNameLower} = value;
	}
	public String get${column.columnName}() {
		return this.${column.columnNameLower};
	}	
</#if>
</#list>

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
}