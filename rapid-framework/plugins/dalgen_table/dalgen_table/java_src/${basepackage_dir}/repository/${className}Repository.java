<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   

package ${basepackage}.repository;

import cn.org.rapid_framework.util.PageList;

import ${basepackage}.dal.query.${className}Query;
import ${basepackage}.repository.model.${className};
/**
 * ${table.tableAlias} Repository
 */
public interface ${className}Repository  {
    
    public void update${className}(${className} ${classNameLower});
    
    public ${className} create${className}(${className} ${classNameLower});
    
    public void remove${className}ById(Long id);
    
    public ${className} query${className}ById(Long id);
    
    public PageList<${className}> findPage(${className}Query query);
    
    <#list table.columns as column>
    <#if column.unique && !column.pk>
    public ${className} getBy${column.columnName}(${column.javaType} v);
    
    </#if>
    </#list>
}
