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
	public <@generateResultClassName sql/> ${sql.operation}(${sql.parameterClassName} param);
	<#else>
	public <@generateResultClassName sql/> ${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>);
	</#if>
</#list>

}



