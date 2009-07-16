/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.service;

import javacommon.base.BaseManager;
import javacommon.base.EntityDao;

import org.springframework.stereotype.Component;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.awd.dao.UsersDao;
import com.awd.model.Users;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */

@Component
public class UsersManager extends BaseManager<Users,java.lang.Long>{

	private UsersDao usersDao;
	/**通过spring注入UsersDao*/
	public void setUsersDao(UsersDao dao) {
		this.usersDao = dao;
	}
	public EntityDao getEntityDao() {
		return this.usersDao;
	}
	public Page findByPageRequest(PageRequest pr) {
		return usersDao.findByPageRequest(pr);
	}
	
	public Users getByLoginName(java.lang.String v) {
		return usersDao.getByLoginName(v);
	}

}
