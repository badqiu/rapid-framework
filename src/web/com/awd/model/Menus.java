/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javacommon.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */


@Entity
@Table(name = "MENUS")
public class Menus extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "Menus";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_JSBH = "jsbh";
	public static final String ALIAS_DESCN = "descn";
	public static final String ALIAS_URL = "url";
	public static final String ALIAS_IMAGE = "image";
	public static final String ALIAS_NAME = "name";
	public static final String ALIAS_THE_SORT = "theSort";
	public static final String ALIAS_QTIP = "qtip";
	public static final String ALIAS_PARENT_ID = "parentId";
	
	//date formats
	

	//columns START
	private java.lang.Long id;
	private java.lang.Integer jsbh;
	private java.lang.String descn;
	private java.lang.String url;
	private java.lang.String image;
	private java.lang.String name;
	private java.lang.Short theSort;
	private java.lang.String qtip;
	private java.lang.Long parentId;
	private Set<Users> users = new LinkedHashSet<Users>();
	//columns END


	public Menus(){
	}

	public Menus(
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
	
	@Column(name = "JSBH", unique = false, nullable = true, insertable = true, updatable = true, length = 9)
	public java.lang.Integer getJsbh() {
		return this.jsbh;
	}
	
	public void setJsbh(java.lang.Integer value) {
		this.jsbh = value;
	}
	
	@Column(name = "DESCN", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public java.lang.String getDescn() {
		return this.descn;
	}
	
	public void setDescn(java.lang.String value) {
		this.descn = value;
	}
	
	@Column(name = "URL", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public java.lang.String getUrl() {
		return this.url;
	}
	
	public void setUrl(java.lang.String value) {
		this.url = value;
	}
	
	@Column(name = "IMAGE", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public java.lang.String getImage() {
		return this.image;
	}
	
	public void setImage(java.lang.String value) {
		this.image = value;
	}
	
	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public java.lang.String getName() {
		return this.name;
	}
	
	public void setName(java.lang.String value) {
		this.name = value;
	}
	
	@Column(name = "THE_SORT", unique = false, nullable = true, insertable = true, updatable = true, length = 4)
	public java.lang.Short getTheSort() {
		return this.theSort;
	}
	
	public void setTheSort(java.lang.Short value) {
		this.theSort = value;
	}
	
	@Column(name = "QTIP", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public java.lang.String getQtip() {
		return this.qtip;
	}
	
	public void setQtip(java.lang.String value) {
		this.qtip = value;
	}
	
	@Column(name = "PARENT_ID", unique = false, nullable = true, insertable = true, updatable = true, length = 21)
	public java.lang.Long getParentId() {
		return this.parentId;
	}
	
	public void setParentId(java.lang.Long value) {
		this.parentId = value;
	}
	@ManyToMany(mappedBy = "menus")
	public Set<Users> getUsers()
	{
		return users;
	}

	public void setUsers(Set<Users> users)
	{
		this.users = users;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Id",getId())
			.append("Jsbh",getJsbh())
			.append("Descn",getDescn())
			.append("Url",getUrl())
			.append("Image",getImage())
			.append("Name",getName())
			.append("TheSort",getTheSort())
			.append("Qtip",getQtip())
			.append("ParentId",getParentId())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.append(getJsbh())
			.append(getDescn())
			.append(getUrl())
			.append(getImage())
			.append(getName())
			.append(getTheSort())
			.append(getQtip())
			.append(getParentId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Menus == false) return false;
		if(this == obj) return true;
		Menus other = (Menus)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.append(getJsbh(),other.getJsbh())
			.append(getDescn(),other.getDescn())
			.append(getUrl(),other.getUrl())
			.append(getImage(),other.getImage())
			.append(getName(),other.getName())
			.append(getTheSort(),other.getTheSort())
			.append(getQtip(),other.getQtip())
			.append(getParentId(),other.getParentId())
			.isEquals();
	}


}

