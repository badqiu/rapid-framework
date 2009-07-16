/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import javacommon.base.ResourceDetailService;

import com.awd.dao.ResourcesDao;
import com.awd.model.Resources;

/**
 * 从数据库查询URL--授权定义的RequestMapService实现�?.
 * 
 * @author calvin
 */
@Transactional(readOnly = true)
public class ResourceDetailServiceImpl  implements ResourceDetailService {
	@Autowired
	private ResourcesDao resourcesDao;

	/**
	 * @see ResourceDetailService#getRequestMap()
	 */
	public LinkedHashMap<String, String> getRequestMap() throws Exception {
		List<Resources> resourceList = resourcesDao.find(Resources.QUERY_BY_URL_TYPE, Resources.URL_TYPE);
		LinkedHashMap<String, String> requestMap = new LinkedHashMap<String, String>();
		for (Resources resources : resourceList) {
			requestMap.put(resources.getValue(), resources.getAuthNames());
		}
		return requestMap;
	}

}
