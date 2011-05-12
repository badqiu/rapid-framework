/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2011
 */

package com.company.project.service;

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


public class UserInfoManagerTest extends BaseManagerTestCase{

	private UserInfoManager manager;
	
	@Autowired
	public void setUserInfoManager(UserInfoManager manager) {
		this.manager = manager;
	}

    @Override
    protected String[] getDbUnitDataFiles() {
        //通过 TestMethodContext.getMethodName() 可以得到当前正在运行的测试方法名称
        return new String[]{"classpath:testdata/common.xml","classpath:testdata/UserInfo.xml",
                            "classpath:testdata/UserInfo_"+TestMethodContext.getMethodName()+".xml"};
    }

	//数据库单元测试前会开始事务，结束时会回滚事务，所以测试方法可以不用关心测试数据的删除
	@Test
	public void crud() {

		UserInfo obj = newUserInfo();
		manager.save(obj);
		manager.getEntityDao().flush();
		
		manager.update(obj);
		manager.getEntityDao().flush();
		
		assertNotNull(obj.getUserId());
		
		manager.removeById(obj.getUserId());
		manager.getEntityDao().flush();
	
	}
	
	public static UserInfo newUserInfo() {
		UserInfo obj = new UserInfo();
		
	  	obj.setUsername(new java.lang.String("1"));
	  	obj.setPassword(new java.lang.String("1"));
	  	obj.setBirthDate(new java.util.Date(System.currentTimeMillis()));
	  	obj.setSex(new java.lang.Integer("1"));
	  	obj.setAge(new java.lang.Integer("1"));
		return obj;
	}
}
