<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import org.springframework.stereotype.Component;

<#include "/java_imports.include">
@Component
public class ${className}Manager extends BaseManager<${className},${table.idColumn.javaType}>{

	private ${className}Dao ${classNameLower}Dao;
	/**增加setXXXX()方法,spring就可以通过autowire by name自动设置对象属性*/
	public void set${className}Dao(${className}Dao dao) {
		this.${classNameLower}Dao = dao;
	}
	public EntityDao getEntityDao() {
		return this.${classNameLower}Dao;
	}
	public Page findByPageRequest(PageRequest pr) {
		return ${classNameLower}Dao.findByPageRequest(pr);
	}
	
<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return ${classNameLower}Dao.getBy${column.columnName}(v);
	}	
	</#if>
</#list>
}
