/*
 * Alipay.com Inc.
 * Copyright (c) 2004 All Rights Reserved.
 */
 
package ${basepackage}.ibatis;

import ${basepackage}.query.*;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.iwallet.biz.common.util.PageList;
import com.iwallet.biz.common.util.money.Money;
import ${basepackage}.dataobject.${tableConfig.tableClassName}DO;
import ${basepackage}.daointerface.${tableConfig.tableClassName}DAO;

/**
 * ${tableConfig.tableClassName}DAO
 * database table: ${tableConfig.table.sqlName}
 */
public class Ibatis${tableConfig.tableClassName}DAO extends SqlMapClientDaoSupport implements ${tableConfig.tableClassName}DAO {

<#list tableConfig.sqls as sql>

	/**
	 * ${sql.remarks!}
	 * sql: 
	 * <pre>
	 * ${sql.executeSql}
	 * </pre>
	 */
	<#if (sql.params?size > 4) >
	public <@generateResultClassName sql/> ${sql.operation}(${sql.parameterClassName} param) throws DataAccessException {
		<@generateOperationMethodBody sql/>
	}
	<#else>
	@SuppressWarnings("unchecked")
	public <@generateResultClassName sql/> ${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>) throws DataAccessException {
		<#if (sql.params?size > 1)>
		Map<String,Object> param = new HashMap<String,Object>();
		<#list sql.params as param>
		param.put("${param.paramName}",${param.paramName});
		</#list>
		</#if>		
		<@generateOperationMethodBody sql/>
	}
	</#if>
</#list>

}

<#macro generateOperationMethodBody sql>
	<#local ibatisNamespace = sql.tableClassName+".">
	<#if sql.params?size == 0>
		<#local paramName = 'null'>
	<#elseif sql.params?size == 1>
		<#local paramName = sql.params?first.paramName>
	<#else>
		<#local paramName = "param">
	</#if>
	<#if sql.selectSql>
		<#if sql.paging || sql.multiplicity = 'paging'>
		return (<@generateResultClassName sql/>)PageQueryUtils.pageQuery(getSqlMapClientTemplate(),"${ibatisNamespace}${sql.operation}",${paramName});
		<#elseif sql.multiplicity = 'one'>
		return (<@generateResultClassName sql/>)getSqlMapClientTemplate().queryForObject("${ibatisNamespace}${sql.operation}",${paramName});
		<#else>
		return (<@generateResultClassName sql/>)getSqlMapClientTemplate().queryForList("${ibatisNamespace}${sql.operation}",${paramName});
		</#if>
	</#if>
	<#if sql.deleteSql>
		<#if sql.paramType = 'object'>
		if(${paramName} == null) {
			throw new IllegalArgumentException("Can't delete by a null data object.");
		}
		</#if>	
		return getSqlMapClientTemplate().delete("${ibatisNamespace}${sql.operation}", ${paramName});
	</#if>
	<#if sql.insertSql>
		<#if sql.paramType = 'object'>
		if(${paramName} == null) {
			throw new IllegalArgumentException("Can't insert a null data object into db.");
		}
		getSqlMapClientTemplate().insert("${ibatisNamespace}${sql.operation}", ${paramName});
		return ${paramName}.get${tableConfig.table.pkColumn.columnName}();
		<#else>
		return getSqlMapClientTemplate().insert("${ibatisNamespace}${sql.operation}", ${paramName});
		</#if>    
	</#if>
	<#if sql.updateSql>
		<#if sql.paramType = 'object'>
		if(${paramName} == null) {
			throw new IllegalArgumentException("Can't update by a null data object.");
		}
		</#if>
		return getSqlMapClientTemplate().update("${ibatisNamespace}${sql.operation}", ${paramName});
	</#if>
</#macro>