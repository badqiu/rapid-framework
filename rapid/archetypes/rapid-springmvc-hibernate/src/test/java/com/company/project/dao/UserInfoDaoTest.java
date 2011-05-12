/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2011
 */

package com.company.project.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

import cn.org.rapid_framework.test.context.TestMethodContext;

import static junit.framework.Assert.*;

import java.util.*;
import common.base.*;
import common.util.*;

import cn.org.rapid_framework.util.*;
import cn.org.rapid_framework.web.util.*;
import cn.org.rapid_framework.util.page.*;
import cn.org.rapid_framework.page.*;


import com.company.project.domain.*;
import com.company.project.dao.*;
import com.company.project.service.*;
import com.company.project.query.*;

/**
 * @author badqiu
 * @author hunhun
 * @since 4.0 
 */


public class UserInfoDaoTest extends BaseDaoTestCase{
	
	private UserInfoDao dao;
	
	@Autowired
	public void setUserInfoDao(UserInfoDao dao) {
		this.dao = dao;
	}

	@Override 
	protected String[] getDbUnitDataFiles() {
	    //通过 TestMethodContext.getMethodName() 可以得到当前正在运行的测试方法名称
		return new String[]{"classpath:testdata/common.xml","classpath:testdata/UserInfo.xml",
		                    "classpath:testdata/UserInfo_"+TestMethodContext.getMethodName()+".xml"};
	}
	
	
	static int page = 1;
	static int pageSize = 10;	
	
	
	//数据库单元测试前会开始事务，结束时会回滚事务，所以测试方法可以不用关心测试数据的删除
	@Test
	public void findPage() {
		
		UserInfoQuery query = newUserInfoQuery();
		Page<UserInfo> result = dao.findPage(query);
		assertEquals(pageSize,result.getPageSize());
		assertNotNull(result);
	}
		
	public static UserInfoQuery newUserInfoQuery() {
		UserInfoQuery query = new UserInfoQuery();
		query.setPage(page);
		query.setPageSize(pageSize);	
		
	  	query.setUsername(new String("1"));
	  	query.setPassword(new String("1"));
		query.setBirthDateBegin(new Date(System.currentTimeMillis()));
		query.setBirthDateEnd(new Date(System.currentTimeMillis()));
	  	query.setSex(new Integer("1"));
	  	query.setAge(new Integer("1"));
		return query;
	}
	
}
