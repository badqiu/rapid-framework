<#include '/java_copyright.include'/>
 
package ${tableConfig.basepackage}.ibatis;

import ${tableConfig.basepackage}.operation.${tableConfig.className?lower_case}.*;
import ${tableConfig.basepackage}.dataobject.*;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.iwallet.biz.common.util.PageList;
import com.iwallet.biz.common.util.money.Money;
import com.alipay.common.ibatis.util.PageQueryUtils;

import ${tableConfig.basepackage}.dataobject.${tableConfig.className}DO;
import ${tableConfig.basepackage}.daointerface.${tableConfig.className}DAO;

/**
 * ${tableConfig.className}DAO
<#include '/java_description.include'/>
 */
public class Ibatis${tableConfig.className}DAO extends <#if (tableConfig.autoSwitchDataSrc)>com.iwallet.biz.dal.common.AutoSwitchDataSrcSqlMapClientDaoSupport<#else>SqlMapClientDaoSupport</#if> implements ${tableConfig.className}DAO {

<#list tableConfig.sqls as sql>

	/**
	 * ${sql.remarks!}
	 * sql: 
	 * <pre>${StringHelper.removeCrlf(sql.executeSql)?trim}</pre>
	 */
	public <@generateResultClassName sql 'DO'/> ${sql.operation}(<@generateOperationArguments sql/>) throws DataAccessException {
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
	<#local ibatisNamespace = appName+"."+tableConfig.className+".">
	<#if sql.params?size == 0>
		<#local paramName = 'null'>
	<#elseif sql.paramType = 'object'>
		<#local paramName = tableConfig.table.classNameFirstLower>		
	<#elseif sql.params?size == 1>
		<#local paramName = sql.params?first.paramName>
	<#else>
		<#local paramName = "param">
	</#if>
	<#if sql.selectSql>
		<#if sql.paging>
			<#if isUseParamObject(sql)>
		return (<@generateResultClassName sql 'DO'/>)PageQueryUtils.pageQuery(getSqlMapClientTemplate(),"${ibatisNamespace}${sql.operation}",${paramName});
			<#else>
		return (<@generateResultClassName sql 'DO'/>)PageQueryUtils.pageQuery(getSqlMapClientTemplate(),"${ibatisNamespace}${sql.operation}",${paramName},pageNum,pageSize);
			</#if>
		<#elseif sql.multiplicity = 'one'>
		return (<@generateResultClassName sql 'DO'/>)getSqlMapClientTemplate().queryForObject("${ibatisNamespace}${sql.operation}",${paramName});
		<#else>
		return (<@generateResultClassName sql 'DO'/>)getSqlMapClientTemplate().queryForList("${ibatisNamespace}${sql.operation}",${paramName});
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
		return ${paramName}.get${tableConfig.pkColumn.columnName}();
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