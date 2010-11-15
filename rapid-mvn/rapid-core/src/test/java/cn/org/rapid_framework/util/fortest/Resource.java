/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package cn.org.rapid_framework.util.fortest;

import java.util.HashSet;
import java.util.Set;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "resource")
public class Resource  {
	
	//alias
	public static final String TABLE_ALIAS = "Resource";
	public static final String ALIAS_RESOURCE_ID = "resourceId";
	public static final String ALIAS_RESOURCE_NAME = "resourceName";
	
	//date formats
	

	//columns START
	private java.lang.Long resourceId;
	private java.lang.String resourceName;
	//columns END


	public Resource(){
	}

	public Resource(
		java.lang.Long resourceId
	){
		this.resourceId = resourceId;
	}

	

	public void setResourceId(java.lang.Long value) {
		this.resourceId = value;
	}
	
	@Id @GeneratedValue(generator="custom-id")
	@GenericGenerator(name="custom-id", strategy = "native")
	@Column(name = "resource_id", unique = true, nullable = false, insertable = true, updatable = true, length = 20)
	public java.lang.Long getResourceId() {
		return this.resourceId;
	}
	
	@Column(name = "resource_name", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public java.lang.String getResourceName() {
		return this.resourceName;
	}
	
	public void setResourceName(java.lang.String value) {
		this.resourceName = value;
	}
	
	
	private Set roles = new HashSet(0);
	public void setRoles(Set<Role> role){
		this.roles = role;
	}
	
	@OneToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY, mappedBy = "resource")
	public Set<Role> getRoles() {
		return roles;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("ResourceId",getResourceId())
			.append("ResourceName",getResourceName())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getResourceId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Resource == false) return false;
		if(this == obj) return true;
		Resource other = (Resource)obj;
		return new EqualsBuilder()
			.append(getResourceId(),other.getResourceId())
			.isEquals();
	}
}

