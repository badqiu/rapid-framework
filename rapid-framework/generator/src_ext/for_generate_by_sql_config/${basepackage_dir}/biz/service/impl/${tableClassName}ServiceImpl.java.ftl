/*
 * Alipay.com Inc.
 * Copyright (c) 2010 All Rights Reserved.
 */
package ${basepackage}.biz.service.impl;

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
public class ${tableConfig.tableClassName}ServiceImpl implements  ${tableConfig.tableClassName}Service{
	private ${tableConfig.tableClassName}DAO ${tableConfig.tableClassName?uncap_first}DAO;
	
	public void set${tableConfig.tableClassName}DAO(${tableConfig.tableClassName}DAO dao) {
		this.${tableConfig.tableClassName?uncap_first}DAO = dao;
	}
	
<#list tableConfig.sqls as sql>

	<#if (sql.params?size > params2paramObjectLimit) >
	public <@generateResultClassName sql/> ${sql.operation}(${sql.parameterClassName} param) throws DataAccessException{
		return ${tableConfig.tableClassName?uncap_first}DAO.${sql.operation}(param);
	}
	<#else>
	public <@generateResultClassName sql/> ${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>) throws DataAccessException {
		return ${tableConfig.tableClassName?uncap_first}DAO.${sql.operation}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>);
	}
	</#if>
</#list>

}



