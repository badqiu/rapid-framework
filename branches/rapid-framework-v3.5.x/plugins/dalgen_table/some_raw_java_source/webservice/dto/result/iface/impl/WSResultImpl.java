package com.company.project.facade.dto.result.iface.impl;

import com.company.project.facade.dto.result.iface.WSResult;

import cn.org.rapid_framework.exception.ErrorCode;

public class WSResultImpl implements WSResult {
	ErrorCode errorCode;
	
	public WSResultImpl() {
		super();
	}
	
	public WSResultImpl(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public boolean isError() {
		return errorCode != null;
	}

}
