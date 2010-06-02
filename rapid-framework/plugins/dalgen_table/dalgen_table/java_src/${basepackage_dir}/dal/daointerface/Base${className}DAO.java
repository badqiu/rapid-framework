<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dal.daointerface;


import cn.org.rapid_framework.util.PageList;
import ${basepackage}.dal.dataobject.UserInfoDO;
import ${basepackage}.dal.query.UserInfoQuery;

public interface Base${className}DAO {

    public ${table.pkColumn.javaType} insert(${className}DO obj);
    
    public void update(${className}DO obj);
    
    public ${className}DO queryById(Long id);
    
    public void deleteById(Long id);
    
    public PageList<${className}DO> findPage(${className}Query query);
    
    <#list table.columns as column>
    <#if column.unique && !column.pk>
    public ${className}DO getBy${column.columnName}(${column.javaType} v);
    
    </#if>
    </#list>

}
