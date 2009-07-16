/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.dao;

import javacommon.base.BaseHibernateDao;

import org.springframework.stereotype.Component;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.awd.model.Roles;

@Component
public class RolesDao extends BaseHibernateDao<Roles,java.lang.Long>{

	public Class getEntityClass() {
		return Roles.class;
	}
	
	public Page findByPageRequest(PageRequest pageRequest) {
		//XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
		// $column$为字符串拼接, #column#为使用占位符. 以下为图方便采用sql拼接,适用性能要求不高的应�??,使用占位符方式可以优化�?�能. 
		// $column$ 为PageRequest.getFilters()中的key
		String sql = "select a from Roles as a where 1=1 "
				+ "/~ and a.name = '$name$' ~/"
				+ "/~ order by $sortColumns$ ~/";
		return findBy(sql,pageRequest);
	}
	
	public Roles getByName(java.lang.String v) {
		return (Roles) findByProperty("name",v);
	}	

}
