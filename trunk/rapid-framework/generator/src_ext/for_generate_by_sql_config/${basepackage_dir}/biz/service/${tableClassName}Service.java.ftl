/*
 * Alipay.com Inc.
 * Copyright (c) 2004 All Rights Reserved.
 */
package ${basepackage}.biz.service;
import org.springframework.dao.DataAccessException;
import ${basepackage}.query.*;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import com.iwallet.biz.common.util.PageList;
import com.iwallet.biz.common.util.money.Money;

/**
 * 
 *
 */
public interface ${tableConfig.tableClassName}Service {

<#list tableConfig.sqls as sql>
	/**
	 * ${sql.remarks}
	 */
	<#if (sql.params?size > 4) >
	public <@generateResultClassName sql/> ${sql.operation}(${sql.parameterClassName} param) throws DataAccessException;
	<#else>
	public <@generateResultClassName sql/> ${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>) throws DataAccessException;
	</#if>
</#list>

}



