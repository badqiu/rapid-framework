
<#if (sql.params?size > 4) >
	public <@generateResultClassName/> ${sql.operation}(${sql.operation}Query param) {
		<@generateOperationMethodBody />
	}
<#else>
	@SuppressWarnings("unchecked")
	public <@generateResultClassName/> ${sql.operation}(<#list sql.params as param>${param.preferredParameterClassName} ${param.paramName} <#if param_has_next>,</#if></#list>) {
		Map param = new HashMap();
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
			${sql.operationResultClassName}
		<#else>
			List<${sql.operationResultClassName}>
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
		prepareObjectForSaveOrUpdate(entity);
		return getSqlMapClientTemplate().update("${sql.operation}", param);
	</#if>			
</#macro>