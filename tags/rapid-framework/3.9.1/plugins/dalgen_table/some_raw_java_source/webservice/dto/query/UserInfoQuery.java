/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.company.project.facade.dto.query;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

import java.util.*;

import javacommon.base.*;
import javacommon.util.*;

import cn.org.rapid_framework.util.*;
import cn.org.rapid_framework.web.util.*;
import cn.org.rapid_framework.page.*;
import cn.org.rapid_framework.page.impl.*;

import com.company.project.query.*;
import com.company.project.model.*;
import com.company.project.dao.*;
import com.company.project.service.*;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */


public class UserInfoQuery extends PageRequest implements Serializable {
	

	/** username */
	private java.lang.String username;
	/** password */
	private java.lang.String password;
	/** birthDate */
	private java.util.Date birthDate;
	/** sex */
	private Integer sex;
	/** age */
	private java.lang.Integer age;
	/** userId */
	private java.lang.Long userId;

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
	
	public java.util.Date getBirthDate() {
		return this.birthDate;
	}
	
	public void setBirthDate(java.util.Date value) {
		this.birthDate = value;
	}
	
	public Integer getSex() {
		return this.sex;
	}
	
	public void setSex(Integer value) {
		this.sex = value;
	}
	
	public java.lang.Integer getAge() {
		return this.age;
	}
	
	public void setAge(java.lang.Integer value) {
		this.age = value;
	}
	
	public java.lang.Long getUserId() {
		return this.userId;
	}
	
	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}
	

	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
}

