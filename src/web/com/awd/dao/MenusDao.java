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
public class MenusDao extends BaseHibernateDao<Menus,java.lang.Long>{

	public Class getEntityClass() {
		return Menus.class;
	}
	
	public Page findByPageRequest(PageRequest pageRequest) {
		//XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
		// $column$为字符串拼接, #column#为使用占位符. 以下为图方便采用sql拼接,适用性能要求不高的应�?,使用占位符方式可以优化�?�能. 
		// $column$ 为PageRequest.getFilters()中的key
		String sql = "select a from Menus as a where 1=1 "
				+ "/~ and a.jsbh = '$jsbh$' ~/"
				+ "/~ and a.descn = '$descn$' ~/"
				+ "/~ and a.url = '$url$' ~/"
				+ "/~ and a.image = '$image$' ~/"
				+ "/~ and a.name = '$name$' ~/"
				+ "/~ and a.theSort = '$theSort$' ~/"
				+ "/~ and a.qtip = '$qtip$' ~/"
				+ "/~ and a.parentId = '$parentId$' ~/"
				+ "/~ order by $sortColumns$ ~/";
		return findBy(sql,pageRequest);
	}
	

}
