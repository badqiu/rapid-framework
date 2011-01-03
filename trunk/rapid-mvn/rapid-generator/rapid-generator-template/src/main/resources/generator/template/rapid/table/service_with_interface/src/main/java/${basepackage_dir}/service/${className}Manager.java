<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

<#include "/java_imports.include">

/**
<#include "/java_description.include">
 */
public interface ${className}Manager {

	/** 
	 * 创建${className}
	 **/
	public ${className} save(${className} ${classNameLower});
	
	/** 
	 * 更新${className}
	 **/	
    public ${className} update(${className} ${classNameLower});
    
	/** 
	 * 删除${className}
	 **/
    public void removeById(${table.idColumn.javaType} id);
    
	/** 
	 * 根据ID得到${className}
	 **/    
    public ${className} getById(${table.idColumn.javaType} id);
    
	/** 
	 * 分页查询: ${className}
	 **/      
	public Page findPage(${className}Query query);
	
<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} param);
	
	</#if>
</#list>
    
}
