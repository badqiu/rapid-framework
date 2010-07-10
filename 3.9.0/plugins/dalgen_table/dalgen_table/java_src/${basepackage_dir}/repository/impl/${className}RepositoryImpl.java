<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>  

package ${basepackage}.repository.impl;

import java.util.List;

import cn.org.rapid_framework.util.PageList;

import ${basepackage}.dal.daointerface.UserInfoDAO;
import ${basepackage}.dal.dataobject.UserInfoDO;
import ${basepackage}.dal.query.UserInfoQuery;
import ${basepackage}.repository.UserInfoRepository;
import ${basepackage}.repository.converter.UserInfoRepositoryConverter;
import ${basepackage}.repository.model.UserInfo;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */


public class ${className}RepositoryImpl implements ${className}Repository {
    
    private ${className}DAO ${classNameLower}DAO;
        
    public void update${className}(${className} ${classNameLower}) {
        ${className}DO target = ${className}RepositoryConverter.convert2${className}DO(${classNameLower});
        ${classNameLower}DAO.update(target);        
    }
    
    public ${className} create${className}(${className} ${classNameLower}){
        ${className}DO target = ${className}RepositoryConverter.convert2${className}DO(${classNameLower});
        ${classNameLower}.set${table.pkColumn.columnName}(${classNameLower}DAO.insert(target));
        return ${classNameLower};
    }
    
    public void remove${className}ById(Long id) {
        ${classNameLower}DAO.deleteById(id);
    }
    
    public ${className} query${className}ById(Long id) {
        return ${className}RepositoryConverter.convert2${className}(${classNameLower}DAO.queryById(id));
    }
    
    public PageList<${className}> findPage(${className}Query query) {
        PageList<${className}DO> sourceList = ${classNameLower}DAO.findPage(query);    
        List<${className}> targetList = ${className}RepositoryConverter.convert2${className}List(sourceList);
        return new PageList(targetList,sourceList.getPageSize(),sourceList.getPageNo(),sourceList.getTotalCount());
    }
    
    <#list table.columns as column>
    <#if column.unique && !column.pk>
    public ${className} getBy${column.columnName}(${column.javaType} v) {
        return ${className}RepositoryConverter.convert2${className}(${classNameLower}DAO.getBy${column.columnName}(v));
    }
    
    </#if>
    </#list>    
}
