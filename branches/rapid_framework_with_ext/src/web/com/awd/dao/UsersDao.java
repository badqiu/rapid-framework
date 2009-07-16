/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.dao;

import java.util.*;

import javacommon.base.*;
import javacommon.util.*;

import cn.org.rapid_framework.util.*;
import cn.org.rapid_framework.web.util.*;
import cn.org.rapid_framework.page.*;
import cn.org.rapid_framework.page.impl.*;

import com.awd.model.*;
import com.awd.dao.*;
import com.awd.service.*;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */


import org.springframework.stereotype.Component;

@Component
public class UsersDao extends BaseHibernateDao<Users,java.lang.Long>{

	public Class getEntityClass() {
		return Users.class;
	}
	
	public Page findByPageRequest(PageRequest pageRequest) {
		//XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
		// $column$为字符串拼接, #column#为使用占位符. 以下为图方便采用sql拼接,适用性能要求不高的应�?,使用占位符方式可以优化�?�能. 
		// $column$ 为PageRequest.getFilters()中的key
		String sql = "select a from Users as a where 1=1 "
				+ "/~ and a.loginName = '$loginName$' ~/"
				+ "/~ and a.password = '$password$' ~/"
				+ "/~ and a.name = '$name$' ~/"
				+ "/~ and a.email = '$email$' ~/"
				+ "/~ order by $sortColumns$ ~/";
		return findBy(sql,pageRequest);
	}
	
	public Users getByLoginName(java.lang.String v) {
		return (Users) findByProperty("loginName",v);
	}	

}
