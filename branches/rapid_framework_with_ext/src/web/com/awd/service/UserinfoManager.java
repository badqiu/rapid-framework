/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.service;

import org.springframework.stereotype.Component;

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

@Component
public class UserinfoManager extends BaseManager<Userinfo,java.lang.Long>{

	private UserinfoDao userinfoDao;
	/**通过spring注入UserinfoDao*/
	public void setUserinfoDao(UserinfoDao dao) {
		this.userinfoDao = dao;
	}
	public EntityDao getEntityDao() {
		return this.userinfoDao;
	}
	public Page findByPageRequest(PageRequest pr) {
		return userinfoDao.findByPageRequest(pr);
	}
	
}
