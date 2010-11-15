<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

<#include "/java_imports.include">

import org.springframework.stereotype.Component;


@Component
public class ${className}DAO extends BaseIbatisDAO<${className},${table.idColumn.javaType}>{

	public Class getEntityClass() {
		return ${className}.class;
	}
	
	public void saveOrUpdate(${className} entity) {
		if(entity.get${table.idColumn.columnName}() == null) 
			save(entity);
		else 
			update(entity);
	}
	
	public Page findPage(PageRequest pageRequest) {
		return pageQuery("${className}.pageSelect",pageRequest);
	}
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return (${className})getSqlMapClientTemplate().queryForObject("${className}.getByUsername",v);
	}	
	</#if>
	</#list>

}
