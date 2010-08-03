/*
 * Alipay.com Inc.
 * Copyright (c) 2004 All Rights Reserved.
 */
 

/**
 * 
 
 */
public class Ibatis${tableConfig.tableClassName}DAOImplTest {

<#list tableConfig.sqls as sql>
	public void test_${sql.operation}() {
		
	}
</#list>

}



