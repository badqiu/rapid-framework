/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.model;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
@Table(name = "RESOURCES")
public class Resources extends BaseEntity {
	
	public static final String QUERY_BY_URL_TYPE = "FROM Resources WHERE resourceType=? ORDER BY orderNum ASC";
	//resourceType常量
	public static final String URL_TYPE = "url";
	public static final String MENU_TYPE = "menu";
	//alias
	public static final String TABLE_ALIAS = "Resources";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_RESOURCE_TYPE = "resourceType";
	public static final String ALIAS_VALUE = "value";
	public static final String ALIAS_ORDER_NUM = "orderNum";
	
	//date formats
	

	//columns START
	private java.lang.Long id;
	private java.lang.String resourceType;
	private java.lang.String value;
	private java.lang.Long orderNum;
	private Set<Authorities> authorities = new LinkedHashSet<Authorities>(); //可访问该资源的授�?
	//columns END


	public Resources(){
	}

	public Resources(
		java.lang.Long id
	){
		this.id = id;
	}

	

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	@Id @GeneratedValue(generator="custom-id")
	@GenericGenerator(name="custom-id", strategy = "increment")
	@Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true, length = 10)
	public java.lang.Long getId() {
		return this.id;
	}
	
	@Column(name = "RESOURCE_TYPE", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public java.lang.String getResourceType() {
		return this.resourceType;
	}
	
	public void setResourceType(java.lang.String value) {
		this.resourceType = value;
	}
	
	@Column(name = "VALUE", unique = false, nullable = false, insertable = true, updatable = true, length = 255)
	public java.lang.String getValue() {
		return this.value;
	}
	
	public void setValue(java.lang.String value) {
		this.value = value;
	}
	
	@Column(name = "ORDER_NUM", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public java.lang.Long getOrderNum() {
		return this.orderNum;
	}
	
	public void setOrderNum(java.lang.Long value) {
		this.orderNum = value;
	}
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(joinColumns = { @JoinColumn(name = "RESOURCE_ID") }, inverseJoinColumns = { @JoinColumn(name = "AUTHORITY_ID") })
	@Fetch(FetchMode.JOIN)
	@OrderBy("id")
	public Set<Authorities> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authorities> authorities) {
		this.authorities = authorities;
	}
	


	public String toString() {
		return new ToStringBuilder(this)
			.append("Id",getId())
			.append("ResourceType",getResourceType())
			.append("Value",getValue())
			.append("OrderNum",getOrderNum())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.append(getResourceType())
			.append(getValue())
			.append(getOrderNum())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Resources == false) return false;
		if(this == obj) return true;
		Resources other = (Resources)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.append(getResourceType(),other.getResourceType())
			.append(getValue(),other.getValue())
			.append(getOrderNum(),other.getOrderNum())
			.isEquals();
	}
	@Transient
	public String getAuthNames() throws Exception {
		return ReflectionUtils.fetchElementPropertyToString(authorities, "name", ",");
	}
}

