package com.company.project.facade.eumns.error;

import cn.org.rapid_framework.exception.ErrorCode;

public enum UserInfoErrorEnum implements ErrorCode {
	USERINFO_VALIDATION_ERROR("userinfo validation error"),
	ILLEGAL_BLOG("userinfo validation error"),
	SYSTEM_ERROR("userinfo validation error"),
	NOT_FOUND_USERINFO("userinfo validation error"),
	ILLEGAL_PASSWORD("userinfo validation error");
	
	private String desc;
	UserInfoErrorEnum(String desc) {
		this.desc = desc;
	}
	
	public String getErrorCode() {
		return name();
	}

	public String getErrorDetails() {
		return desc;
	}

}
