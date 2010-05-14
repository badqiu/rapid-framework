<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
package ${basepackage}.repository.impl;

import java.util.List;

import com.alipay.mcenter.common.dal.share.auto.dataobject.McenterRecoverBatchDO;
import com.alipay.mcenter.common.util.PageList;
import com.alipay.mcenter.core.converter.RecoverBatchConverter;
import com.alipay.mcenter.core.model.entity.RecoverBatch;

import ${basepackage}.repository.converter.${className}RepositoryConverter;

public class ${className}RepositoryImpl implements ${className}Repository{
	
	private ${className}DAO ${classNameFirstLower}DAO;
	
    public ${className} update(${className} o) {
        ${className}DO target = ${className}RepositoryConverter.convert2${className}DO(o);
        ${classNameFirstLower}DAO.update(target);
        return o;
    }
    
    public ${className} create(${className} source) {
        ${className}DO target = ${className}RepositoryConverter.convert2${className}DO(source);
        ${classNameFirstLower}DAO.insert(target);
        return source;
    }
    
    public void removeById(${table.idColumn.javaType} id) {
        ${classNameFirstLower}DAO.delete(id);
    }
    
    public ${className} queryById(${table.idColumn.javaType} id) {
        ${className}DO source = ${classNameFirstLower}DAO.queryById(id);
        return ${className}RepositoryConverter.convert2${className}(source);
    }
    
    public void findPage(${className}Query query) {
        long totalRows = ${classNameFirstLower}Repository.count(
            <#list table.columns as column>
            <#if column.isDateTimeColumn && !column.contains("begin,start,end")>
                query.get${column.columnName}Begin(),
                query.get${column.columnName}End(),
            <#else>
                query.get${column.columnName}() <#if column_has_next>,</#if>
            </#if>
            </#list>            
        );
        if (totalRows <= 0) {
            return new PageList<${className}>();
        }
        List<${className}DO> list = {classNameFirstLower}DAO.findPage(query.getStartIndex(), query.getPageSize(), query.getOrderBy(),
            <#list table.columns as column>
            <#if column.isDateTimeColumn && !column.contains("begin,start,end")>
                query.get${column.columnName}Begin(),
                query.get${column.columnName}End(),
            <#else>
                query.get${column.columnName}() <#if column_has_next>,</#if>
            </#if>
            </#list>
            );
        return new PageList<${className}>(${className}RepositoryConverter.convert2${className}List(list),
                query.getPageSize(), query.getPageNum(), totalRows);
    }
	
    public void set${className}DAO (${className}DAO  v) {
        this.${classNameFirstLower}DAO = v;
    }
}
