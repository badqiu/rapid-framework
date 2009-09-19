package cn.org.rapid_framework.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author badqiu
 */
public class TestBean1 {
	private String username;
	private int age;
	private BigDecimal money;
	private Date birthDate;
	private Timestamp createdDate;
	private TestBean2 testBean2;
	private List beans;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public TestBean2 getTestBean2() {
		return testBean2;
	}
	public void setTestBean2(TestBean2 testBean2) {
		this.testBean2 = testBean2;
	}
	public List getBeans() {
		return beans;
	}
	public void setBeans(List beans) {
		this.beans = beans;
	}
	
	
}
