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
import ${basepackage}.dataobject.${tableConfig.tableClassName}DO;

import org.nuxeo.runtime.test.autowire.annotation.XAutoWire;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public class ${tableConfig.tableClassName}ServiceImplTest extends BaseBizTestCase{

	@XAutoWire(XAutoWire.BY_NAME)
	protected ${tableConfig.tableClassName}Service ${tableConfig.tableClassName?uncap_first}Service;
	
    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }
    	
<#list tableConfig.sqls as sql>

	<#if isUseParamObject(sql) >
	public void test_${sql.operation}() throws DataAccessException{
		
		//${tableConfig.tableClassName?uncap_first}Service.${sql.operation}(param);
	}
	</#if>
</#list>

}



