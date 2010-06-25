<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dal.ibatis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.util.Assert;

import com.company.project.dal.query.UserInfoQuery;

import cn.org.rapid_framework.util.PageList;
import javacommon.base.BaseIbatisDao;

import ${basepackage}.dal.dataobject.UserInfoDO;
import ${basepackage}.dal.query.UserInfoQuery;


public class BaseIbatis${className}DAO  extends SqlMapClientDaoSupport{

    public ${table.pkColumn.javaType} insert(${className}DO obj) {
        Assert.notNull(obj,"cannot insert null data object into db")
        return (${table.pkColumn.javaType})getSqlMapClientTemplate().insert("${className}.insert",obj);
    }
    
    public void update(${className}DO obj) {
        Assert.notNull(obj,"cannot update by a null data object.")
        getSqlMapClientTemplate().update("${className}.update",obj);
    }
    
    public ${className}DO queryById(Long id) {
        return (${className}DO)getSqlMapClientTemplate().insert("${className}.queryById",id);
    }
    
    public void deleteById(Long id) {
        getSqlMapClientTemplate().delete("${className}.deleteById",id);
    }
	
    @SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
    public static PageList pageQuery(SqlMapClientTemplate sqlMapClientTemplate,String statementName, ${className}Query query) {
        int pageSize = 200;
        int pageNo = 10;
        Number totalCount = (Number)sqlMapClientTemplate.queryForObject(statementName+"-count",query);
        if(totalCount.intValue() > 0) {
            List list = sqlMapClientTemplate.queryForList(statementName,query);
            return new PageList(list,pageSize,pageNo,totalCount.intValue());
        }
        return new PageList(new ArrayList(0),pageSize,pageNo,0);
    }
}
