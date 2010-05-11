package com.company.project.facade.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.EscapedErrors;

import cn.org.rapid_framework.exception.ErrorCodeException;

import com.company.project.facade.dto.result.iface.ValidationWSResult;
import com.company.project.facade.eumns.error.UserInfoErrorEnum;
import com.company.project.facade.exception.ValidationException;
import com.company.project.model.UserInfo;
import com.company.project.service.UserInfoManager;

/** 
 * 进行UserInfo类的相关数据验证
 * 
 * 注意: validator直接new出来使用,不用放进spring容器
 **/
public class UserInfoValidator {
	UserInfoManager userInfoManager;
	
	
	public UserInfoValidator(UserInfoManager userInfoManager) {
		this.userInfoManager = userInfoManager;
	}

	public Errors validateCreateUserInfo(UserInfo userInfo) {
		return validateUserInfo(userInfo);
	}

	public void failIfValidateCreateUserInfo(UserInfo userInfo) {
		Errors errors = validateCreateUserInfo(userInfo);
		if(errors.hasErrors()) {
			throw new ValidationException(errors);
		}
	}
	
	public Errors validateUpdateUserInfo(UserInfo userInfo) {
		return validateUserInfo(userInfo);
	}
	
	public void failIfValidateUpdateUserInfo(UserInfo userInfo) {
		Errors errors = validateUpdateUserInfo(userInfo);
		if(errors.hasErrors()) {
			throw new ValidationException(errors);
		}
	}
	
	public Errors validateUserInfo(UserInfo userInfo) {
		Errors errors = new MapBindingResult(null,null);//TODO fixedme
		ValidationUtils.rejectIfEmpty(errors, "usernmae", "username.error");
		return errors;
	}
	public void validateUserInfo(ValidationWSResult result,Errors validateErrors) {
		if(validateErrors.hasErrors()) {
			result.setValidateErrors(validateErrors);
			throw new ErrorCodeException(UserInfoErrorEnum.USERINFO_VALIDATION_ERROR);
		}
	}
}
