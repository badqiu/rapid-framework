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
@Table(name = "ROLES")
public class Roles extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "Roles";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_NAME = "name";
	
	//date formats
	

	//columns START
	private java.lang.Long id;
	private java.lang.String name;
	private Set<Authorities> auths = new LinkedHashSet<Authorities>(); //有序的关联对象集�?
	private Set<Users> users = new LinkedHashSet<Users>();
	//columns END


	public Roles(){
	}

	public Roles(java.lang.Long id){
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
	
	@Column(name = "NAME", unique = true, nullable = false, insertable = true, updatable = true, length = 20)
	public java.lang.String getName() {
		return this.name;
	}
	
	public void setName(java.lang.String value) {
		this.name = value;
	}
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "AUTHORITY_ID") })
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("id")
	public Set<Authorities> getAuthorities() {
		return auths;
	}

	public void setAuthorities(Set<Authorities> auths) {
		this.auths = auths;
	}
	
	@ManyToMany(mappedBy = "roles")
	public Set<Users> getUsers() {
		return users;
	}
	
	public void setUsers(Set<Users> users) {
		this.users = users;
	}
	
	@Transient
	@SuppressWarnings("unchecked")
	public List<Long> getAuthIds() throws Exception {
		return ReflectionUtils.fetchElementPropertyToList(auths, "id");
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Id",getId())
			.append("Name",getName())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.append(getName())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Roles == false) return false;
		if(this == obj) return true;
		Roles other = (Roles)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.append(getName(),other.getName())
			.isEquals();
	}
}

