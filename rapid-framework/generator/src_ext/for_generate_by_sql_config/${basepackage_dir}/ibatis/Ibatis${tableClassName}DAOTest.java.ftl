/*
 * Alipay.com Inc.
 * Copyright (c) 2004 All Rights Reserved.
 */
 
package ${basepackage}.ibatis;

import ${basepackage}.query.*;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;

import com.iwallet.biz.common.util.PageList;
import com.iwallet.biz.common.util.money.Money;
import ${basepackage}.dataobject.${tableConfig.tableClassName}DO;
import ${basepackage}.daointerface.${tableConfig.tableClassName}DAO;

/**
 * 
 *
 */
public class Ibatis${tableConfig.tableClassName}DAOTest {

<#list tableConfig.sqls as sql>
	public void test_${sql.operation}() {
		
	}
</#list>

}



