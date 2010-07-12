<#include "java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

<#include "java_imports.include">

import java.io.Serializable;

import org.springframework.stereotype.Component;


@Component
public class ${className}Manager extends BaseManager<${className}>{

	private ${className}Dao ${classNameLower}Dao;
	/**通过spring注入${className}Dao*/
	public void set${className}Dao(${className}Dao dao) {
		this.${classNameLower}Dao = dao;
	}
	public EntityDao getEntityDao() {
		return this.${classNameLower}Dao;
	}
	/**通过PageRequest查询*/
	public Page findPage(PageRequest info) {
		return ${classNameLower}Dao.findPage(info);
	}
	
	public Object get${className}ById(String id) {
		return super.getById(id);
	}
	public void save${className}(${className} entity) {
		super.save(entity);
	}
	public void remove${className}ById(String id) {
		super.removeById(id);
	}
	public void update${className}(${className} entity) {
		super.update(entity);
	}
	
	
<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return ${classNameLower}Dao.getBy${column.columnName}(v);
	}	
	</#if>
</#list>
}
