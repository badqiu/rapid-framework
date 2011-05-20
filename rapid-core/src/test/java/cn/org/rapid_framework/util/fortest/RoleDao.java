/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package cn.org.rapid_framework.util.fortest;


import org.springframework.stereotype.Component;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

@Component
public class RoleDao extends BaseHibernateDao{

	public Class getEntityClass() {
		return Role.class;
	}
	
	public Page findByPageRequest(PageRequest pageRequest) {
		//XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
		String sql = "from Role as a where 1=1 "
				+ "/~ and a.roleName = '[roleName]' ~/"
				+ "/~ and a.resourceId = '[resourceId]' ~/"
				+ "/~ order by [sortingColumn] [sortingDirection] ~/";
		return pageQuery(sql,pageRequest);
	}
	

}
