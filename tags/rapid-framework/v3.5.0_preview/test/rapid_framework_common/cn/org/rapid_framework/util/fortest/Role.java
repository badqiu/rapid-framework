/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package cn.org.rapid_framework.util.fortest;

import javacommon.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "role")
public class Role extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "Role";
	public static final String ALIAS_ROLE_ID = "roleId";
	public static final String ALIAS_ROLE_NAME = "roleName";
	public static final String ALIAS_RESOURCE_ID = "resourceId";
	
	//date formats
	

	//columns START
	private java.lang.Long roleId;
	private java.lang.String roleName;
	private java.lang.Long resourceId;
	//columns END


	public Role(){
	}

	public Role(
		java.lang.Long roleId
	){
		this.roleId = roleId;
	}

	

	public void setRoleId(java.lang.Long value) {
		this.roleId = value;
	}
	
	@Id @GeneratedValue(generator="custom-id")
	@GenericGenerator(name="custom-id", strategy = "native")
	@Column(name = "role_id", unique = true, nullable = false, insertable = true, updatable = true, length = 20)
	public java.lang.Long getRoleId() {
		return this.roleId;
	}
	
	@Column(name = "role_name", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public java.lang.String getRoleName() {
		return this.roleName;
	}
	
	public void setRoleName(java.lang.String value) {
		this.roleName = value;
	}
	
	@Column(name = "resource_id", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.Long getResourceId() {
		return this.resourceId;
	}
	
	public void setResourceId(java.lang.Long value) {
		this.resourceId = value;
	}
	
	
	private Resource resource;
	
	public void setResource(Resource resource){
		this.resource = resource;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "resource_id",nullable = false, insertable = false, updatable = false)
	public Resource getResource() {
		return resource;
	}
	
	@Transient
	public String getJava() {
		return "java from Role.java";
	}
	
	@Transient
	public String getReadonly() {
		return "Readonly from Role.java";
	}
	
	@Transient
	public void setWriteonly(String s) {
		 System.out.println("Role.setWriteonly() from Role.java,str:"+s);
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("RoleId",getRoleId())
			.append("RoleName",getRoleName())
			.append("ResourceId",getResourceId())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getRoleId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Role == false) return false;
		if(this == obj) return true;
		Role other = (Role)obj;
		return new EqualsBuilder()
			.append(getRoleId(),other.getRoleId())
			.isEquals();
	}
}

