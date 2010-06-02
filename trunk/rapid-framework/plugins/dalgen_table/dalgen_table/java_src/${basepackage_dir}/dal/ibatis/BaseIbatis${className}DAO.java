<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dal.ibatis;

import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import cn.org.rapid_framework.util.PageList;
import javacommon.base.BaseIbatisDao;

import ${basepackage}.dal.dataobject.UserInfoDO;
import ${basepackage}.dal.query.UserInfoQuery;

import org.springframework.stereotype.Component;


@Component
public class BaseIbatis${className}DAO  extends SqlMapClientDaoSupport{

    public ${table.pkColumn.javaType} insert(${className}DO obj) {
        return (${table.pkColumn.javaType})getSqlMapClientTemplate().insert("${className}.insert",obj);
    }
    
    public void update(${className}DO obj) {
        getSqlMapClientTemplate().update("${className}.update",obj);
    }
    
    public ${className}DO getById(Long id) {
        return (${className}DO)getSqlMapClientTemplate().insert("${className}.getById",id);
    }
	
	public PageList<${className}DO> findPage(${className}Query query) {
		return pageQuery(getSqlMapClientTemplate(),"${className}.findPage",query);
	}
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className}DO getBy${column.columnName}(${column.javaType} v) {
		return (${className}DO)getSqlMapClientTemplate().queryForObject("${className}.getByUsername",v);
	}	
	</#if>
	</#list>

    public static PageList pageQuery(SqlMapClientTemplate sqlMapClientTemplate,String string, UserInfoQuery query) {
        return null;
    }
}
