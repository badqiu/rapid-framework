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


import java.io.Serializable;
import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserinfoDaoJdbc extends BaseSpringJdbcDao<Userinfo,java.lang.Long>{
	
	static final String SELECT_PREFIX = "select ID,USERNAME,USERPHONE from USERINFO ";
	
	public Class getEntityClass() {
		return Userinfo.class;
	}
	
	public String getIdentifierPropertyName() {
		return "id";
	}
	
	/**
	 * return sql for deleteById();
	 */
	public String getDeleteByIdSql() {
		return "delete from USERINFO where ID=?";
	}
	
	/**
	 * return sql for getById();
	 */
	public String getFindByIdSql() {
		return SELECT_PREFIX + " where ID=? ";
	}
	
	public void save(Userinfo entity) {
		String sql = "insert into USERINFO " 
			 + " (ID,USERNAME,USERPHONE) " 
			 + " values "
			 + " (:id,:username,:userphone)";
		insertWithIdentity(entity,sql); //for sqlserver and mysql
		
		//其它主键生成策略
		//insertWithOracleSequence(entity,"sequenceName",sql); //oracle sequence: 
		//insertWithDB2Sequence(entity,"sequenceName",sql); //db2 sequence:
		//insertWithUUID(entity,sql); //uuid
		//insertWithAssigned(entity,sql) //手工分配
	}
	
	public void update(Userinfo entity) {
		String sql = "update USERINFO set "
					+ " ID=:id,USERNAME=:username,USERPHONE=:userphone "
					+ " where ID=:id";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}
	
	public List findAll() {
		String sql = SELECT_PREFIX ;
		return getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(getEntityClass()));
	}

	public Page findByPageRequest(PageRequest pageRequest) {
		//XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
		// $column$为字符串拼接, #column#为使用占位符. 以下为图方便采用sql拼接,适用性能要求不高的应用,使用占位符方式可以优化性能. 
		// $column$ 为PageRequest.getFilters()中的key
		String sql = SELECT_PREFIX + " as a where 1=1 "
				+ "/~ and a.USERNAME = '$username$' ~/"
				+ "/~ and a.USERPHONE = '$userphone$' ~/"
				+ "/~ order by #sortColumns# ~/";
		return pageQuery(sql,pageRequest);
	}
	

}
