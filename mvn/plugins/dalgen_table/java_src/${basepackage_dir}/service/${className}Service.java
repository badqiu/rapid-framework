<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.service;

import java.util.List;

public interface ${className}Service  {
    
    public ${className} update(${className} o);
    
    public ${className} create(${className} o);
    
    public void removeById(${table.idColumn.javaType} id);
    
    public ${className} queryById(${table.idColumn.javaType} id);
    
    public PageList<${className}> findPage(${className}Query query);
    
}
