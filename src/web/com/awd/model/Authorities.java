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
@Table(name = "AUTHORITIES")
public class Authorities extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "Authorities";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_NAME = "name";
	public static final String ALIAS_DISPLAY_NAME = "displayName";
	
	//date formats
	

	//columns START
	private java.lang.Long id;
	private java.lang.String name;
	private java.lang.String displayName;
	//columns END


	public Authorities(){
	}

	public Authorities(
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
	
	@Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public java.lang.String getName() {
		return this.name;
	}
	
	public void setName(java.lang.String value) {
		this.name = value;
	}
	
	@Column(name = "DISPLAY_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public java.lang.String getDisplayName() {
		return this.displayName;
	}
	
	public void setDisplayName(java.lang.String value) {
		this.displayName = value;
	}
	
	

	public String toString() {
		return new ToStringBuilder(this)
			.append("Id",getId())
			.append("Name",getName())
			.append("DisplayName",getDisplayName())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.append(getName())
			.append(getDisplayName())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Authorities == false) return false;
		if(this == obj) return true;
		Authorities other = (Authorities)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.append(getName(),other.getName())
			.append(getDisplayName(),other.getDisplayName())
			.isEquals();
	}
}

