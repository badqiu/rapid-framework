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

import com.awd.dao.AuthoritiesDao;
import com.awd.model.Authorities;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */

@Component
public class AuthoritiesManager extends BaseManager<Authorities,java.lang.Long>{

	private AuthoritiesDao authoritiesDao;
	/**通过spring注入AuthoritiesDao*/
	public void setAuthoritiesDao(AuthoritiesDao dao) {
		this.authoritiesDao = dao;
	}
	public EntityDao getEntityDao() {
		return this.authoritiesDao;
	}
	public Page findByPageRequest(PageRequest pr) {
		return authoritiesDao.findByPageRequest(pr);
	}
	public Authorities getByName(java.lang.String v) {
		return authoritiesDao.getByName(v);
	}

}
