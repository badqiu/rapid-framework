<#include '/java_copyright.include'/>
 
package ${tableConfig.basepackage}.dao;

import ${tableConfig.basepackage}.operation.${tableConfig.className?lower_case}.*;
import ${tableConfig.basepackage}.dataobject.*;

<#include '/java_import.include'/>

import org.springframework.dao.DataAccessException;

import ${tableConfig.basepackage}.dataobject.${tableConfig.className}DO;
import ${tableConfig.basepackage}.daointerface.${tableConfig.className}DAO;

/**
 * ${tableConfig.className}Dao
<#include '/java_description.include'/>
 */
public class ${tableConfig.className}Dao extends BaseMybatisDao {

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
		return (<@generateResultClassName sql 'DO'/>)PageQueryUtils.pageQuery(getSqlSession(),"${ibatisNamespace}${sql.operation}",${paramName});
			<#else>
		return (<@generateResultClassName sql 'DO'/>)PageQueryUtils.pageQuery(getSqlSession(),"${ibatisNamespace}${sql.operation}",${paramName},pageNum,pageSize);
			</#if>
		<#elseif sql.multiplicity = 'one'>
			<#local resultClassName><@generateResultClassName sql 'DO'/></#local>
			<#if resultClassName=='short' || resultClassName=='byte' || resultClassName == 'int' || resultClassName == 'long' || resultClassName == 'float' || resultClassName == 'double' >
		Number returnObject = (Number)getSqlSession().selectOne("${ibatisNamespace}${sql.operation}",${paramName});
		if(returnObject == null)
			return (${resultClassName})0; 
		else
			return returnObject.${resultClassName}Value();
			<#else>
		return (<@generateResultClassName sql 'DO'/>)getSqlSession().selectOne("${ibatisNamespace}${sql.operation}",${paramName});
			</#if>
		<#else>
		return (<@generateResultClassName sql 'DO'/>)getSqlSession().selectList("${ibatisNamespace}${sql.operation}",${paramName});
		</#if>
	</#if>
	<#if sql.deleteSql>
		<#if sql.paramType = 'object'>
		if(${paramName} == null) {
			throw new IllegalArgumentException("Can't delete by a null data object.");
		}
		</#if>	
		return getSqlSession().delete("${ibatisNamespace}${sql.operation}", ${paramName});
	</#if>
	<#if sql.insertSql>
		<#if sql.paramType = 'object'>
		if(${paramName} == null) {
			throw new IllegalArgumentException("Can't insert a null data object into db.");
		}
		getSqlSession().insert("${ibatisNamespace}${sql.operation}", ${paramName});
		return ${paramName}.get${tableConfig.pkColumn.columnName}();
		<#else>
		return getSqlSession().insert("${ibatisNamespace}${sql.operation}", ${paramName});
		</#if>    
	</#if>
	<#if sql.updateSql>
		<#if sql.paramType = 'object'>
		if(${paramName} == null) {
			throw new IllegalArgumentException("Can't update by a null data object.");
		}
		</#if>
		return getSqlSession().update("${ibatisNamespace}${sql.operation}", ${paramName});
	</#if>
</#macro>