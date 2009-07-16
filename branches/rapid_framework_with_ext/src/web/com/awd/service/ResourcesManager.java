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

import com.awd.dao.ResourcesDao;
import com.awd.model.Resources;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */

@Component
public class ResourcesManager extends BaseManager<Resources,java.lang.Long>{

	private ResourcesDao resourcesDao;
	/**通过spring注入ResourcesDao*/
	public void setResourcesDao(ResourcesDao dao) {
		this.resourcesDao = dao;
	}
	public EntityDao getEntityDao() {
		return this.resourcesDao;
	}
	public Page findByPageRequest(PageRequest pr) {
		return resourcesDao.findByPageRequest(pr);
	}

	
}
