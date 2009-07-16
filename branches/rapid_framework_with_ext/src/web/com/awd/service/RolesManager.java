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

import com.awd.dao.RolesDao;
import com.awd.model.Roles;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */

@Component
public class RolesManager extends BaseManager<Roles,java.lang.Long>{

	private RolesDao rolesDao;
	/**通过spring注入RolesDao*/
	public void setRolesDao(RolesDao dao) {
		this.rolesDao = dao;
	}
	public EntityDao getEntityDao() {
		return this.rolesDao;
	}
	public Page findByPageRequest(PageRequest pr) {
		return rolesDao.findByPageRequest(pr);
	}
	
	public Roles getByName(java.lang.String v) {
		return rolesDao.getByName(v);
	}

}
