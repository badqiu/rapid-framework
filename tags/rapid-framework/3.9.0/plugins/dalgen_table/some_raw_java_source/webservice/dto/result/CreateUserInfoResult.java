package com.company.project.facade.dto.result;

import org.springframework.validation.Errors;

import com.company.project.facade.dto.result.iface.impl.ValidationWSResultImpl;
import com.company.project.facade.dto.result.iface.impl.WSResultImpl;

import cn.org.rapid_framework.util.OperationResult;

public class CreateUserInfoResult extends ValidationWSResultImpl{
	public Long userId;
	
	public CreateUserInfoResult() {
		super();
	}

	public void setUserId(Long userId2) {
		this.userId = userId;
	}
	
}
