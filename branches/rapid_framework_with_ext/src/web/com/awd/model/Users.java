/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javacommon.base.BaseEntity;
import javacommon.util.ReflectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "USERS")
public class Users extends BaseEntity
{

	// alias
	public static final String TABLE_ALIAS = "Users";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_LOGIN_NAME = "loginName";
	public static final String ALIAS_PASSWORD = "password";
	public static final String ALIAS_NAME = "name";
	public static final String ALIAS_EMAIL = "email";

	// date formats

	// columns START
	private java.lang.Long id;
	private java.lang.String loginName;
	private java.lang.String password;
	private java.lang.String name;
	private java.lang.String email;
	private Set<Roles> roles = new LinkedHashSet<Roles>(); // 有序的关联对象集�
	private Set<Menus> menus = new LinkedHashSet<Menus>(); // 有序的关联对象集�

	// columns END

	public Users()
	{
	}

	public Users(java.lang.Long id)
	{
		this.id = id;
	}

	public void setId(java.lang.Long value)
	{
		this.id = value;
	}

	@Id
	@GeneratedValue(generator = "custom-id")
	@GenericGenerator(name = "custom-id", strategy = "increment")
	@Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true, length = 10)
	public java.lang.Long getId()
	{
		return this.id;
	}

	@Column(name = "LOGIN_NAME", unique = true, nullable = false, insertable = true, updatable = true, length = 20)
	public java.lang.String getLoginName()
	{
		return this.loginName;
	}

	public void setLoginName(java.lang.String value)
	{
		this.loginName = value;
	}

	@Column(name = "PASSWORD", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getPassword()
	{
		return this.password;
	}

	public void setPassword(java.lang.String value)
	{
		this.password = value;
	}

	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getName()
	{
		return this.name;
	}

	public void setName(java.lang.String value)
	{
		this.name = value;
	}

	@Column(name = "EMAIL", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public java.lang.String getEmail()
	{
		return this.email;
	}

	public void setEmail(java.lang.String value)
	{
		this.email = value;
	}

	// 多对多定义，cascade操作避免定义CascadeType.REMOVE
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	// 中间表定�?,表名采用默认命名规则
	@JoinTable(joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	// 集合按id排序.
	@OrderBy("id")
	public Set<Roles> getRoles()
	{
		return roles;
	}

	public void setRoles(Set<Roles> roles)
	{
		this.roles = roles;
	}

	/**
	 * *
	 * 
	 * @return menus.
	 */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "MENU_ID") })
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	// 集合按id排序.
	@OrderBy("id")
	public Set<Menus> getMenus()
	{
		return menus;
	}

	public void setMenus(Set<Menus> menus)
	{
		this.menus = menus;
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("Id", getId()).append("LoginName", getLoginName()).append("Password",
				getPassword()).append("Name", getName()).append("Email", getEmail()).toString();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getId()).append(getLoginName()).append(getPassword()).append(getName())
				.append(getEmail()).toHashCode();
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof Users == false)
			return false;
		if (this == obj)
			return true;
		Users other = (Users) obj;
		return new EqualsBuilder().append(getId(), other.getId()).append(getLoginName(), other.getLoginName()).append(
				getPassword(), other.getPassword()).append(getName(), other.getName()).append(getEmail(),
				other.getEmail()).isEquals();
	}

	// 非持久化属�性.
	@Transient
	@SuppressWarnings("unchecked")
	public List<Long> getRoleIds() throws Exception
	{
		return ReflectionUtils.fetchElementPropertyToList(roles, "id");
	}
	// 非持久化属�性.
	@Transient
	@SuppressWarnings("unchecked")
	public List<Long> getMenuIds() throws Exception
	{
		return ReflectionUtils.fetchElementPropertyToList(menus, "id");
	}
}
