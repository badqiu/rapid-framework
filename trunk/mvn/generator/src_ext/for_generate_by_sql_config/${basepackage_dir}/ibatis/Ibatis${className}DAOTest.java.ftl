/*
 * Alipay.com Inc.
 * Copyright (c) 2010 All Rights Reserved.
 */
 
package ${basepackage}.ibatis;

import ${basepackage}.operation.*;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;

import com.iwallet.biz.common.util.PageList;
import com.iwallet.biz.common.util.money.Money;
import ${basepackage}.dataobject.${tableConfig.className}DO;
import ${basepackage}.daointerface.${tableConfig.className}DAO;

import org.nuxeo.runtime.test.autowire.annotation.XAutoWire;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 *
 */
public class Ibatis${tableConfig.className}DAOTest extends BaseDaoTestCase {
	@XAutoWire(XAutoWire.BY_NAME)
	protected Ibatis${tableConfig.className}DAO ${tableConfig.className}DAO;

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }
        	
<#list tableConfig.sqls as sql>
	public void test_${sql.operation}() {
		
	}
</#list>

}



