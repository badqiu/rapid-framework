/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2011
 */

package com.company.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@SuppressWarnings(value = "all")
@Service
@Transactional
public class UserInfoManager extends BaseManager<UserInfo,java.lang.Long>{

	private UserInfoDao userInfoDao;
	//增加setXXXX()方法,spring就可以通过autowire自动设置对象属性(默认byName,请注意大小写)
	public void setUserInfoDao(UserInfoDao dao) {
		this.userInfoDao = dao;
	}
	public EntityDao getEntityDao() {
		return this.userInfoDao;
	}
	
	@Transactional(readOnly=true)
	public Page<UserInfo>  findPage(UserInfoQuery query) {
		return userInfoDao.findPage(query);
	}
	
	@Transactional(readOnly=true)
	public UserInfo getByUsername(java.lang.String v) {
		return userInfoDao.getByUsername(v);
	}	
	
	@Transactional(readOnly=true)
	public UserInfo getByAge(java.lang.Integer v) {
		return userInfoDao.getByAge(v);
	}	
	
}
