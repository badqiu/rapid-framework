/*
 * Alipay.com Inc.
 * Copyright (c) 2004 All Rights Reserved.
 */
package ${basepackage}.common.facade;

import ${basepackage}.biz.service.${tableConfig.tableClassName}Service;
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
 */
public class ${tableConfig.tableClassName}FacadeImpl implements  ${tableConfig.tableClassName}Facade{
	private ${tableConfig.tableClassName}Service ${tableConfig.tableClassName?uncap_first}Service;
	
	public void set${tableConfig.tableClassName}Service(${tableConfig.tableClassName}Service Service) {
		this.${tableConfig.tableClassName?uncap_first}Service = Service;
	}
	
<#list tableConfig.sqls as sql>

	<#if (sql.params?size > 4) >
	public <@generateResultClassName sql/> ${sql.operation}(${sql.parameterClassName} param) throws DataAccessException{
		return ${tableConfig.tableClassName?uncap_first}Service.${sql.operation}(param);
	}
	<#else>
	public <@generateResultClassName sql/> ${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>) throws DataAccessException {
		return ${tableConfig.tableClassName?uncap_first}Service.${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>);
	}
	</#if>
</#list>

}



