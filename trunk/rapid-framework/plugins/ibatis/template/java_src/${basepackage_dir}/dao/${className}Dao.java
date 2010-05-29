<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

<#include "/java_imports.include">

import org.springframework.stereotype.Repository;


@Repository
public class ${className}Dao extends BaseIbatisDao<${className},${table.idColumn.javaType}>{
	
	@Override
	public String getIbatisSqlMapNamespace() {
		return "${className}";
	}
	
	public void saveOrUpdate(${className} entity) {
		if(entity.get${table.idColumn.columnName}() == null) 
			save(entity);
		else 
			update(entity);
	}
	
	public Page findPage(${className}Query query) {
		return pageQuery("${className}.findPage",query);
	}
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return (${className})getSqlMapClientTemplate().queryForObject("${className}.getBy${column.columnName}",v);
	}	
	
	</#if>
	</#list>

}
