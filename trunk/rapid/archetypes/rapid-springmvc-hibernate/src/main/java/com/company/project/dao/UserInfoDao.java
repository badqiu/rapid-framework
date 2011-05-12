/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2011
 */

package com.company.project.dao;

import java.util.*;
import common.base.*;
import common.util.*;

import cn.org.rapid_framework.util.*;
import cn.org.rapid_framework.web.util.*;
import cn.org.rapid_framework.util.page.*;
import cn.org.rapid_framework.page.*;


import com.company.project.domain.*;
import com.company.project.dao.*;
import com.company.project.service.*;
import com.company.project.query.*;

/**
 * @author badqiu
 * @author hunhun
 * @since 4.0 
 */


import static cn.org.rapid_framework.util.ObjectUtils.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings(value = "all")
@Repository
public class UserInfoDao extends BaseHibernateDao<UserInfo,java.lang.Long>{

	public Class getEntityClass() {
		return UserInfo.class;
	}
	
	public Page<UserInfo> findPage(UserInfoQuery query) {
        
        StringBuilder hqlSb = new StringBuilder("SELECT t FROM  UserInfo t WHERE 1=1 ");
        if(isNotEmpty(query.getUserId())) {
        	hqlSb.append(" AND  t.userId = :userId ");
        }
        if(isNotEmpty(query.getUsername())) {
        	hqlSb.append(" AND  t.username = :username ");
        }
        if(isNotEmpty(query.getPassword())) {
        	hqlSb.append(" AND  t.password = :password ");
        }
        if(isNotEmpty(query.getBirthDateBegin())) {
        	hqlSb.append(" AND  t.birthDate >= :birthDateBegin ");
        }
        if(isNotEmpty(query.getBirthDateEnd())) {
        	hqlSb.append(" AND t.birthDate <= :birthDateEnd ");
        }
        if(isNotEmpty(query.getSex())) {
        	hqlSb.append(" AND  t.sex = :sex ");
        }
        if(isNotEmpty(query.getAge())) {
        	hqlSb.append(" AND  t.age = :age ");
        }
        
		return pageQuery(hqlSb.toString(),query);
	}
	
	public UserInfo getByUsername(java.lang.String v) {
		return (UserInfo) findByProperty("username",v);
	}	
	public UserInfo getByAge(java.lang.Integer v) {
		return (UserInfo) findByProperty("age",v);
	}	

}
