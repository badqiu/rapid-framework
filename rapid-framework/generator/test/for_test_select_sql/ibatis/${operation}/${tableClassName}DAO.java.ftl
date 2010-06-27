	/**
	 * ${sql.comments!}
	 * sql: ${sql.executeSql}
	 */
<#if (sql.params?size > 4) >
	public <@generateResultClassName/> ${sql.operation}(${sql.parameterClassName} param) {
		<@generateOperationMethodBody />
	}
<#else>
	@SuppressWarnings("unchecked")
	public <@generateResultClassName/> ${sql.operation}(<#list sql.params as param>${param.preferredParameterClassName} ${param.paramName} <#if param_has_next>,</#if></#list>) {
		Map<String,Object> param = new HashMap<String,Object>();
		<#list sql.params as param>
		param.put("${param.paramName}",${param.paramName});
		</#list>
		<@generateOperationMethodBody />
	}
</#if>

<#macro generateResultClassName>
	<#compress>
	<#if sql.selectSql>
		<#if sql.multiPolicy = 'one'>
			${sql.resultClassName}
		<#else>
			List<${sql.resultClassName}>
		</#if>
	<#else>
		int
	</#if>
	</#compress>
</#macro>

<#macro generateOperationMethodBody>
	<#if sql.selectSql>
		<#if sql.multiPolicy = 'one'>
		return (<@generateResultClassName/>)getSqlMapClientTemplate().queryForObject("${sql.operation}",param);
		<#else>
		return (<@generateResultClassName/>)getSqlMapClientTemplate().queryForList("${sql.operation}",param);
		</#if>
	</#if>
	<#if sql.deleteSql>
		return getSqlMapClientTemplate().delete("${sql.operation}", param);
	</#if>
	<#if sql.insertSql>
		return getSqlMapClientTemplate().insert("${sql.operation}", param);    
	</#if>
	<#if sql.updateSql>
		return getSqlMapClientTemplate().update("${sql.operation}", param);
	</#if>			
</#macro>