/*
 * Alipay.com Inc.
 * Copyright (c) 2010 All Rights Reserved.
 */
package ${basepackage}.common.facade;

import ${basepackage}.biz.service.${tableConfig.className}Service;
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
public class ${tableConfig.className}FacadeImpl implements  ${tableConfig.className}Facade{
	private ${tableConfig.className}Service ${tableConfig.className?uncap_first}Service;
	
	public void set${tableConfig.className}Service(${tableConfig.className}Service Service) {
		this.${tableConfig.className?uncap_first}Service = Service;
	}
	
<#list tableConfig.sqls as sql>

	<#if isUseParamObject(sql) >
	public <@generateResultClassName sql/> ${sql.operation}(${sql.parameterClassName} param) throws DataAccessException{
		return ${tableConfig.className?uncap_first}Service.${sql.operation}(param);
	}
	<#else>
	public <@generateResultClassName sql/> ${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>) throws DataAccessException {
		return ${tableConfig.className?uncap_first}Service.${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>);
	}
	</#if>
</#list>

}



