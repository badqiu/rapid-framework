/*
 * Alipay.com Inc.
 * Copyright (c) 2010 All Rights Reserved.
 */
package ${basepackage}.biz.service.impl;

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
public class ${tableConfig.className}ServiceImpl implements  ${tableConfig.className}Service{
	private ${tableConfig.className}DAO ${tableConfig.className?uncap_first}DAO;
	
	public void set${tableConfig.className}DAO(${tableConfig.className}DAO dao) {
		this.${tableConfig.className?uncap_first}DAO = dao;
	}
	
<#list tableConfig.sqls as sql>

	<#if isUseParamObject(sql) >
	public <@generateResultClassName sql/> ${sql.operation}(${sql.parameterClassName} param) throws DataAccessException{
		return ${tableConfig.className?uncap_first}DAO.${sql.operation}(param);
	}
	<#else>
	public <@generateResultClassName sql/> ${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>) throws DataAccessException {
		return ${tableConfig.className?uncap_first}DAO.${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>);
	}
	</#if>
</#list>

}



