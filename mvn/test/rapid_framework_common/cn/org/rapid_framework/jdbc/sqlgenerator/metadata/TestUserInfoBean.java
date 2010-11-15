/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2010
 */

package cn.org.rapid_framework.jdbc.sqlgenerator.metadata;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Max;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import cn.org.rapid_framework.util.DateConvertUtils;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */


public class TestUserInfoBean implements java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "UserInfo";
	public static final String ALIAS_USER_ID = "userId";
	public static final String ALIAS_USERNAME = "username";
	public static final String ALIAS_PASSWORD = "password";
	public static final String ALIAS_BIRTH_DATE = "birthDate";
	public static final String ALIAS_SEX = "sex";
	public static final String ALIAS_AGE = "age";
	
	
	//columns START
	
	private java.lang.Long userId;
	@NotBlank @Length(max=0)
	private String username;
	@Length(max=0)
	private String password;
	
	private java.util.Date birthDate;
	@Max(127)
	private Integer sex;
	
	private Integer age;
	//columns END
	
	//注意： spring_jdbc的MetadataCreateUtils.fromTable(Entity.class) 可以读取JPA annotation的标注信息
	//现支持 @Id,@Column,@Table标注

	public TestUserInfoBean(){
	}

	public TestUserInfoBean(
		java.lang.Long userId
	){
		this.userId = userId;
	}

	@Column(insertable=false)
	@Id
	public java.lang.Long getUserId() {
		return this.userId;
	}
	
	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String value) {
		this.username = value;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String value) {
		this.password = value;
	}
	
	@Transient
	public String getBirthDateString() {
		return DateConvertUtils.format(getBirthDate(), "yyyy-MM-dd");
	}
	public void setBirthDateString(String value) {
		setBirthDate(DateConvertUtils.parse(value, "yyyy-MM-dd",java.util.Date.class));
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
	
	public Integer getAge() {
		return this.age;
	}
	
	public void setAge(Integer value) {
		this.age = value;
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
		if(obj instanceof TestUserInfoBean == false) return false;
		if(this == obj) return true;
		TestUserInfoBean other = (TestUserInfoBean)obj;
		return new EqualsBuilder()
			.append(getUserId(),other.getUserId())
			.isEquals();
	}
}

