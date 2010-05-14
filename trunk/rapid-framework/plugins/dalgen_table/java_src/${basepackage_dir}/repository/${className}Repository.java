<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.repository;

import java.util.List;


<#include "/java_imports.include">

public interface ${className}Repository  {
	
	public ${className} update(${className} o);
	
	public ${className} create(${className} o);
	
	public void removeById(${table.idColumn.javaType} id);
	
	public ${className} queryById(${table.idColumn.javaType} id);
	
	public PageList<${className}> findPage(${className}Query query);
	
}
