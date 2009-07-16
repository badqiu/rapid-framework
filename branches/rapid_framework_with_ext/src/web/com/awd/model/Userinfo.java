/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

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


@Entity
@Table(name = "USERINFO")
public class Userinfo extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "Userinfo";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_USERNAME = "username";
	public static final String ALIAS_USERPHONE = "userphone";
	
	//date formats
	

	//columns START
	private java.lang.Long id;
	private java.lang.String username;
	private java.lang.String userphone;
	//columns END


	public Userinfo(){
	}

	public Userinfo(
		java.lang.Long id
	){
		this.id = id;
	}

	

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	@Id @GeneratedValue(generator="custom-id")
	@GenericGenerator(name="custom-id", strategy = "increment")
	@Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true, length = 21)
	public java.lang.Long getId() {
		return this.id;
	}
	
	@Column(name = "USERNAME", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public java.lang.String getUsername() {
		return this.username;
	}
	
	public void setUsername(java.lang.String value) {
		this.username = value;
	}
	
	@Column(name = "USERPHONE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getUserphone() {
		return this.userphone;
	}
	
	public void setUserphone(java.lang.String value) {
		this.userphone = value;
	}
	

	public String toString() {
		return new ToStringBuilder(this)
			.append("Id",getId())
			.append("Username",getUsername())
			.append("Userphone",getUserphone())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.append(getUsername())
			.append(getUserphone())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Userinfo == false) return false;
		if(this == obj) return true;
		Userinfo other = (Userinfo)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.append(getUsername(),other.getUsername())
			.append(getUserphone(),other.getUserphone())
			.isEquals();
	}
}

