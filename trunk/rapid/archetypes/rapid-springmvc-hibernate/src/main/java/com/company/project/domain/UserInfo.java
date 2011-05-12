/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2011
 */

package com.company.project.domain;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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


public class UserInfo implements java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "用户信息表";
	public static final String ALIAS_USER_ID = "www用户ID";
	public static final String ALIAS_USERNAME = "wwww用户名";
	public static final String ALIAS_PASSWORD = "用户密码";
	public static final String ALIAS_BIRTH_DATE = "生日";
	public static final String ALIAS_SEX = "sex";
	public static final String ALIAS_AGE = "age";
	
	//date formats
	public static final String FORMAT_BIRTH_DATE =DateFormats.DATE_FORMAT;
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	@Max(9223372036854775807L)
	private java.lang.Long userId;
	@NotBlank @Length(max=50)
	private java.lang.String username;
	@Length(max=50)
	private java.lang.String password;
	
	private java.util.Date birthDate;
	@Max(9999999999L)
	private java.lang.Integer sex;
	@Max(9999999999L)
	private java.lang.Integer age;
	//columns END

	public UserInfo(){
	}

	public UserInfo(
		java.lang.Long userId
	){
		this.userId = userId;
	}

	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}
	
	public java.lang.Long getUserId() {
		return this.userId;
	}
	public void setUsername(java.lang.String value) {
		this.username = value;
	}
	
	public java.lang.String getUsername() {
		return this.username;
	}
	public void setPassword(java.lang.String value) {
		this.password = value;
	}
	
	public java.lang.String getPassword() {
		return this.password;
	}
	public String getBirthDateString() {
		return DateConvertUtils.format(getBirthDate(), FORMAT_BIRTH_DATE);
	}
	public void setBirthDateString(String value) {
		setBirthDate(DateConvertUtils.parse(value, FORMAT_BIRTH_DATE,java.util.Date.class));
	}
	
	public void setBirthDate(java.util.Date value) {
		this.birthDate = value;
	}
	
	public java.util.Date getBirthDate() {
		return this.birthDate;
	}
	public void setSex(java.lang.Integer value) {
		this.sex = value;
	}
	
	public java.lang.Integer getSex() {
		return this.sex;
	}
	public void setAge(java.lang.Integer value) {
		this.age = value;
	}
	
	public java.lang.Integer getAge() {
		return this.age;
	}

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("UserId",getUserId())
			.append("Username",getUsername())
			.append("Password",getPassword())
			.append("BirthDate",getBirthDate())
			.append("Sex",getSex())
			.append("Age",getAge())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getUserId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof UserInfo == false) return false;
		if(this == obj) return true;
		UserInfo other = (UserInfo)obj;
		return new EqualsBuilder()
			.append(getUserId(),other.getUserId())
			.isEquals();
	}
}

