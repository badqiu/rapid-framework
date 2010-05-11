package cn.org.rapid_framework.web.enums;

public class RapidEnumUser implements FormInputEnum{
	public String userid;
	public String username;
	
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public RapidEnumUser(String userid, String username) {
		super();
		this.userid = userid;
		this.username = username;
	}

	public String getFormInputKey() {
		return userid;
	}

	public String getFormInputLabel() {
		return username;
	}
	
}
