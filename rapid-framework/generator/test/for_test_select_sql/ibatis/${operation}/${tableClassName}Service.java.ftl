	/**
	 * sql: ${sql.executeSql}
	 */
<#if (sql.params?size > 4) >
	public <@generateResultClassName/> ${sql.operation}(${sql.parameterClassName} param) {
		return ${sql.tableClassName?uncap_first}DAO.${sql.operation}(param);
	}
<#else>
	@SuppressWarnings("unchecked")
	public <@generateResultClassName/> ${sql.operation}(<#list sql.params as param>${param.preferredParameterClassName} ${param.paramName} <#if param_has_next>,</#if></#list>) {
		return ${sql.tableClassName?uncap_first}DAO.${sql.operation}(<#list sql.params as param>${param.paramName}<#if param_has_next>,</#if></#list>);
	}
</#if>


