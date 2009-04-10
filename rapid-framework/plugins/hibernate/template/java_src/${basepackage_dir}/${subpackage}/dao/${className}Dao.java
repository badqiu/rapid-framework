<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.${subpackage}.dao;

<#include "/java_imports.include">

import org.springframework.stereotype.Component;

@Component
public class ${className}Dao extends BaseHibernateDao<${className}>{

	public Class getEntityClass() {
		return ${className}.class;
	}
	
	public Page findByPageRequest(PageRequest pageRequest) {
		//XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
		String sql = "select a from ${className} as a where 1=1 "
			<#list table.columns as column>
			  	<#if column.isNotIdOrVersionField>
				+ "/~ and a.${column.columnNameLower} = '[${column.columnNameLower}]' ~/"
				</#if>
			</#list>
				+ "/~ order by [sortColumns] ~/";
		return findBy(sql,pageRequest);
	}
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return (${className}) findByProperty("${column.columnNameLower}",v);
	}	
	</#if>
	</#list>

}
