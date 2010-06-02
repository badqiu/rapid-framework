<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.org.rapid_framework.util.PageList;

import ${basepackage}.dal.query.${className}Query;
import ${basepackage}.repository.model.${className};
import ${basepackage}.repository.${className}Repository;

public interface ${className}Service {


    public ${className} create${className}(${className} ${classNameLower});
    
    public ${className} update${className}(${className} ${classNameLower});

    public void delete${className}ById(${table.idColumn.javaType} id);
    
    public ${className} get${className}ById(${table.idColumn.javaType} id);
    
    public PageList<UserInfo> findPage(${className}Query query);
/*    
<#list table.columns as column>
    <#if column.unique && !column.pk>
    @Transactional(readOnly=true)
    public ${className} getBy${column.columnName}(${column.javaType} v);
    
    </#if>
</#list>
*/
    
}
