package com.company.project.facade.dto.result.iface.impl;

import org.springframework.validation.Errors;

import cn.org.rapid_framework.exception.ErrorCode;

import com.company.project.facade.dto.result.iface.ValidationWSResult;

public class ValidationWSResultImpl extends WSResultImpl implements ValidationWSResult {

	private Errors validateErrors;

	public ValidationWSResultImpl(Errors validateErrors) {
		this.validateErrors = validateErrors;
	}
	
	public ValidationWSResultImpl() {
	}

	public Errors getValidateErrors() {
		return validateErrors;
	}

	public void setValidateErrors(Errors validateErrors) {
		this.validateErrors = validateErrors;
	}
	
	public void setValidateErrors(Errors validateErrors,ErrorCode errorCode) {
		this.validateErrors = validateErrors;
		setErrorCode(errorCode);
	}
	
}
