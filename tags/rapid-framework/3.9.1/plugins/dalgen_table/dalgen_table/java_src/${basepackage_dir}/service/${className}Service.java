<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.org.rapid_framework.util.PageList;

import ${basepackage}.service.dto.query.${className}QueryDTO;
import ${basepackage}.service.dto.${className}DTO;

public interface ${className}Service {


    public ${className}DTO create${className}(${className}DTO ${classNameLower});
    
    public void update${className}(${className}DTO ${classNameLower});

    public void delete${className}ById(${table.idColumn.javaType} id);
    
    public ${className}DTO get${className}ById(${table.idColumn.javaType} id);
    
    public PageList<${className}DTO> findPage(${className}QueryDTO query);

    <#list table.columns as column>
    <#if column.unique && !column.pk>
    @Transactional(readOnly=true)
    public ${className}DTO getBy${column.columnName}(${column.javaType} v);
    
    </#if>
    </#list>
    
}
