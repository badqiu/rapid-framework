<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.${subpackage}.dao;

<#include "/java_imports.include">

import org.springframework.stereotype.Component;

@Component
public class ${className}Dao extends BaseHibernateDao<${className},${table.idColumn.javaType}>{

	public Class getEntityClass() {
		return ${className}.class;
	}
	
	public Page findByPageRequest(PageRequest pageRequest) {
		//XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
		// $column$为字符串拼接, #column#为使用占位符. 以下为图方便采用sql拼接,适用性能要求不高的应用,使用占位符方式可以优化性能. 
		// $column$ 为PageRequest.getFilters()中的key
		String sql = "select a from ${className} as a where 1=1 "
			<#list table.columns as column>
			  	<#if column.isNotIdOrVersionField>
				+ "/~ and a.${column.columnNameLower} = '$${column.columnNameLower}$' ~/"
				</#if>
			</#list>
				+ "/~ order by $sortColumns$ ~/";
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
