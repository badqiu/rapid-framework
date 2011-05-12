/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2011
 */

package com.company.project.query;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

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


public class UserInfoQuery extends PageQuery implements Serializable {
    private static final long serialVersionUID = 3148176768559230877L;
    

	/** www用户ID */
	private java.lang.Long userId;
	/** wwww用户名 */
	private java.lang.String username;
	/** 用户密码 */
	private java.lang.String password;
	/** 生日 */
	private java.util.Date birthDateBegin;
	private java.util.Date birthDateEnd;
	/** sex */
	private java.lang.Integer sex;
	/** age */
	private java.lang.Integer age;

	public java.lang.Long getUserId() {
		return this.userId;
	}
	
	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}
	
	public java.lang.String getUsername() {
		return this.username;
	}
	
	public void setUsername(java.lang.String value) {
		this.username = value;
	}
	
	public java.lang.String getPassword() {
		return this.password;
	}
	
	public void setPassword(java.lang.String value) {
		this.password = value;
	}
	
	public java.util.Date getBirthDateBegin() {
		return this.birthDateBegin;
	}
	
	public void setBirthDateBegin(java.util.Date value) {
		this.birthDateBegin = value;
	}	
	
	public java.util.Date getBirthDateEnd() {
		return this.birthDateEnd;
	}
	
	public void setBirthDateEnd(java.util.Date value) {
		this.birthDateEnd = value;
	}
	
	public java.lang.Integer getSex() {
		return this.sex;
	}
	
	public void setSex(java.lang.Integer value) {
		this.sex = value;
	}
	
	public java.lang.Integer getAge() {
		return this.age;
	}
	
	public void setAge(java.lang.Integer value) {
		this.age = value;
	}
	

	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
}

