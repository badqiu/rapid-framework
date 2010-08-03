/*
 * Alipay.com Inc.
 * Copyright (c) 2004 All Rights Reserved.
 */
 

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;

import com.iwallet.biz.common.util.PageList;
import com.iwallet.biz.common.util.money.Money;
import ${basepackage}.dataobject.${tableConfig.tableClassName}DO;
import ${basepackage}.daointerface.${tableConfig.tableClassName}DAO;

/**
 * ${tableConfig.tableClassName}DAO
 * database table: ${tableConfig.table.sqlName}
 */
public class ${tableConfig.tableClassName}DAOImpl extends SqlMapClientDaoSupport implements ${tableConfig.tableClassName}DAO {

<#list tableConfig.sqls as sql>

	/**
	 * ${sql.remarks!}
	 * sql: ${sql.executeSql}
	 */
	<#if (sql.params?size > 4) >
	public <@generateResultClassName sql/> ${sql.operation}(${sql.parameterClassName} param) throws DataAccessException {
		<@generateOperationMethodBody sql/>
	}
	<#else>
	@SuppressWarnings("unchecked")
	public <@generateResultClassName sql/> ${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>) throws DataAccessException {
		Map<String,Object> param = new HashMap<String,Object>();
		<#list sql.params as param>
		param.put("${param.paramName}",${param.paramName});
		</#list>
		<@generateOperationMethodBody sql/>
	}
	</#if>
</#list>

}

<#macro generateOperationMethodBody sql>
	<#local ibatisNamespace = sql.tableClassName+".">
	<#if sql.selectSql>
		<#if sql.paging || sql.multiplicity = 'paging'>
		return (<@generateResultClassName sql/>)pageQuery(getSqlMapClientTemplate(),"${ibatisNamespace}${sql.operation}",param);
		<#elseif sql.multiplicity = 'one'>
		return (<@generateResultClassName sql/>)getSqlMapClientTemplate().queryForObject("${ibatisNamespace}${sql.operation}",param);
		<#else>
		return (<@generateResultClassName sql/>)getSqlMapClientTemplate().queryForList("${ibatisNamespace}${sql.operation}",param);
		</#if>
	</#if>
	<#if sql.deleteSql>
		return getSqlMapClientTemplate().delete("${ibatisNamespace}${sql.operation}", param);
	</#if>
	<#if sql.insertSql>
		return getSqlMapClientTemplate().insert("${ibatisNamespace}${sql.operation}", param);    
	</#if>
	<#if sql.updateSql>
		return getSqlMapClientTemplate().update("${ibatisNamespace}${sql.operation}", param);
	</#if>
</#macro>