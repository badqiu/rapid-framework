/*
 * Alipay.com Inc.
 * Copyright (c) 2010 All Rights Reserved.
 */
 
package ${basepackage}.mybatis;

import ${basepackage}.query.*;

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
public class Mybatis${tableConfig.tableClassName}DAO extends BaseIbatis3Dao implements ${tableConfig.tableClassName}DAO {

<#list tableConfig.sqls as sql>

	/**
	 * ${sql.remarks!}
	 * sql: 
	 * <pre>
	 ${sql.executeSql?trim}
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public <@generateResultClassName sql/> ${sql.operation}(<@generateOperationArguments sql/>) throws DataAccessException {
		<#if sql.paramType != "object" && !isUseParamObject(sql)>
			<#if (sql.params?size > 1)>
		Map<String,Object> param = new HashMap<String,Object>();
				<#list sql.params as param>
		param.put("${param.paramName}",${param.paramName});
				</#list>
			</#if>		
		</#if>		
		<@generateOperationMethodBody sql/>
	}
</#list>

}

<#macro generateOperationMethodBody sql>
	<#local ibatisNamespace = sql.tableClassName+".">
	<#if sql.params?size == 0>
		<#local paramName = 'null'>
	<#elseif sql.params?size == 1>
		<#local paramName = sql.params?first.paramName>
	<#elseif sql.paramType = 'object'>
		<#local paramName = tableConfig.table.classNameFirstLower>
	<#else>
		<#local paramName = "param">
	</#if>
	<#if sql.selectSql>
		<#if sql.paging>
			<#if isUseParamObject(sql)>
		return (<@generateResultClassName sql/>)PageQueryUtils.pageQuery(getSqlSessionTemplate(),"${ibatisNamespace}${sql.operation}",${paramName});
			<#else>
		return (<@generateResultClassName sql/>)PageQueryUtils.pageQuery(getSqlSessionTemplate(),"${ibatisNamespace}${sql.operation}",${paramName},pageNo,pageSize);
			</#if>
		<#elseif sql.multiplicity = 'one'>
		return (<@generateResultClassName sql/>)getSqlSessionTemplate().selectOne("${ibatisNamespace}${sql.operation}",${paramName});
		<#else>
		return (<@generateResultClassName sql/>)getSqlSessionTemplate().selectList("${ibatisNamespace}${sql.operation}",${paramName});
		</#if>
	</#if>
	<#if sql.deleteSql>
		<#if sql.paramType = 'object'>
		if(${paramName} == null) {
			throw new IllegalArgumentException("Can't delete by a null data object.");
		}
		</#if>	
		return getSqlSessionTemplate().delete("${ibatisNamespace}${sql.operation}", ${paramName});
	</#if>
	<#if sql.insertSql>
		<#if sql.paramType = 'object'>
		if(${paramName} == null) {
			throw new IllegalArgumentException("Can't insert a null data object into db.");
		}
		getSqlSessionTemplate().insert("${ibatisNamespace}${sql.operation}", ${paramName});
		return ${paramName}.get${tableConfig.table.pkColumn.columnName}();
		<#else>
		return getSqlSessionTemplate().insert("${ibatisNamespace}${sql.operation}", ${paramName});
		</#if>    
	</#if>
	<#if sql.updateSql>
		<#if sql.paramType = 'object'>
		if(${paramName} == null) {
			throw new IllegalArgumentException("Can't update by a null data object.");
		}
		</#if>
		return getSqlSessionTemplate().update("${ibatisNamespace}${sql.operation}", ${paramName});
	</#if>
</#macro>
