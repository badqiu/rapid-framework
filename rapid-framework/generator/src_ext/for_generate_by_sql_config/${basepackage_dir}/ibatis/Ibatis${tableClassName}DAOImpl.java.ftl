/*
 * Alipay.com Inc.
 * Copyright (c) 2004 All Rights Reserved.
 */
 

/**
 * 
 
 */
public class ${tableConfig.tableClassName}DAO {

<#list tableConfig.sqls as sql>

	/**
	 * ${sql.remarks!}
	 * sql: ${sql.executeSql}
	 */
	<#if (sql.params?size > 4) >
	public <@generateResultClassName sql/> ${sql.operation}(${sql.parameterClassName} param) {
		<@generateOperationMethodBody sql/>
	}
	<#else>
	@SuppressWarnings("unchecked")
	public <@generateResultClassName sql/> ${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>) {
		Map<String,Object> param = new HashMap<String,Object>();
		<#list sql.params as param>
		param.put("${param.paramName}",${param.paramName});
		</#list>
		<@generateOperationMethodBody sql/>
	}
	</#if>
</#list>

}

<#macro generateResultClassName sql>
	<#compress>
	<#if sql.selectSql>
		<#if sql.paging || sql.multiplicity = 'paging'>
			PageList<${sql.resultClassName}>
		<#elseif sql.multiplicity = 'one'>
			${sql.resultClassName}
		<#else>
			List<${sql.resultClassName}>
		</#if>
	<#else>
		int
	</#if>
	</#compress>
</#macro>

<#macro generateOperationMethodBody sql>
	<#if sql.selectSql>
		<#if sql.paging || sql.multiplicity = 'paging'>
		return (<@generateResultClassName sql/>)pageQuery(getSqlMapClientTemplate(),"${sql.tableClassName}.${sql.operation}",param);
		<#elseif sql.multiplicity = 'one'>
		return (<@generateResultClassName sql/>)getSqlMapClientTemplate().queryForObject("${sql.tableClassName}.${sql.operation}",param);
		<#else>
		return (<@generateResultClassName sql/>)getSqlMapClientTemplate().queryForList("${sql.tableClassName}.${sql.operation}",param);
		</#if>
	</#if>
	<#if sql.deleteSql>
		return getSqlMapClientTemplate().delete("${sql.tableClassName}.${sql.operation}", param);
	</#if>
	<#if sql.insertSql>
		return getSqlMapClientTemplate().insert("${sql.tableClassName}.${sql.operation}", param);    
	</#if>
	<#if sql.updateSql>
		return getSqlMapClientTemplate().update("${sql.tableClassName}.${sql.operation}", param);
	</#if>
</#macro>