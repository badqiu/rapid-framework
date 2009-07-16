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

import com.awd.dao.MenusDao;
import com.awd.model.Menus;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */

@Component
public class MenusManager extends BaseManager<Menus,java.lang.Long>{

	private MenusDao menusDao;
	/**通过spring注入MenusDao*/
	public void setMenusDao(MenusDao dao) {
		this.menusDao = dao;
	}
	public EntityDao getEntityDao() {
		return this.menusDao;
	}
	public Page findByPageRequest(PageRequest pr) {
		return menusDao.findByPageRequest(pr);
	}

	
}
