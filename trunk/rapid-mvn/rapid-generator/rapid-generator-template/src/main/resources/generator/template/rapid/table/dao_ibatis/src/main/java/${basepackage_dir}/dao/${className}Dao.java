<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

<#include "/java_imports.include">

import org.springframework.stereotype.Repository;

/**
<#include "/java_description.include">
 */
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
		return pageQuery(getSqlMapClientTemplate(),"${className}.findPage",query);
	}
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} param) {
		return (${className})getSqlMapClientTemplate().queryForObject("${className}.getBy${column.columnName}",param);
	}	
	
	</#if>
	</#list>

}
