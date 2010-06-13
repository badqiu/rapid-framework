/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.company.project.facade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

import cn.org.rapid_framework.biz.BizCommand;
import cn.org.rapid_framework.biz.BizTemplate;
import cn.org.rapid_framework.exception.ActionException;
import cn.org.rapid_framework.page.Page;

import com.company.project.facade.converter.UserInfoServiceConverter;
import com.company.project.facade.dto.UserInfoDTO;
import com.company.project.facade.dto.query.UserInfoQuery;
import com.company.project.facade.dto.result.CreateUserInfoResult;
import com.company.project.facade.dto.result.GetUserInfoResult;
import com.company.project.facade.dto.result.PageQueryUserInfoResult;
import com.company.project.facade.dto.result.UpdateUserInfoResult;
import com.company.project.facade.dto.result.iface.WSResult;
import com.company.project.facade.dto.result.iface.impl.WSResultImpl;
import com.company.project.facade.eumns.error.UserInfoErrorEnum;
import com.company.project.facade.validation.UserInfoValidator;
import com.company.project.model.UserInfo;
import com.company.project.service.UserInfoManager;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */

@Service
public class UserInfoService {

	private UserInfoManager userInfoManager;
	
	/**增加setXXXX()方法,spring就可以通过autowire自动设置对象属性,请注意大小写*/
	public void setUserInfoManager(UserInfoManager manager) {
		this.userInfoManager = manager;
	} 
	
	public CreateUserInfoResult createUserInfo(final UserInfoDTO userInfoDTO) {
		final CreateUserInfoResult result = new CreateUserInfoResult();
		return getBizTemplate().execute(result,new BizCommand(){
			public void execute() throws ActionException, Exception {
				Assert.notNull(userInfoDTO,"'userInfoDTO' must be not null");
				
				UserInfo userInfo = UserInfoServiceConverter.convert(userInfoDTO);
				Errors validateErrors = new UserInfoValidator(userInfoManager).validateCreateUserInfo(userInfo);
				if(validateErrors.hasErrors()) {
					result.setValidateErrors(validateErrors, UserInfoErrorEnum.USERINFO_VALIDATION_ERROR);
					return;
				}
				
				userInfoManager.save(userInfo);
				result.setUserId(userInfo.getUserId());
			}
		});
	}
	
	public WSResult updateUserInfo(final UserInfoDTO userInfoDTO) {
		
		final UpdateUserInfoResult result = new UpdateUserInfoResult();
		return getBizTemplate().execute(result,new BizCommand(){

			public void execute() throws ActionException, Exception {
				Assert.notNull(userInfoDTO,"'userInfo' must be not null");
				
				UserInfo userInfo = UserInfoServiceConverter.convert(userInfoDTO);
				
				Errors validateErrors = new UserInfoValidator(userInfoManager).validateUpdateUserInfo(userInfo);
				if(validateErrors.hasErrors()) {
					result.setValidateErrors(validateErrors,UserInfoErrorEnum.USERINFO_VALIDATION_ERROR);
					return;
				}
				
				userInfoManager.update(userInfo);
			}
		});
		
	}
	
	//返回success及不能删除的原因
	public WSResult removeUserInfoById(final Long id) {
		WSResult result = new WSResultImpl();
		return getBizTemplate().execute(result,new BizCommand(){
			public void execute() throws ActionException, Exception {
				Assert.notNull(id);
				userInfoManager.removeById(id);
			}
		});
	}
	
	public GetUserInfoResult getUserInfo(final Long id) {
		final GetUserInfoResult result = new GetUserInfoResult();
		return getBizTemplate().execute(result,new BizCommand(){
			public void execute() throws ActionException, Exception {
				Assert.notNull(id);
				UserInfo source = userInfoManager.getById(id);
				UserInfoDTO target = UserInfoServiceConverter.convert(source);
				result.setUserInfo(target);
			}
		});
	}
	
	public PageQueryUserInfoResult findPage(final UserInfoQuery query) {
		final PageQueryUserInfoResult r = new PageQueryUserInfoResult();
		return getBizTemplate().execute(r,new BizCommand(){
			public void execute() throws ActionException, Exception {
				Page page = userInfoManager.findPage(query);
				List<UserInfoDTO> list = UserInfoServiceConverter.convertFrom(page.getResult());
				
				r.setPageNo(page.getThisPageNumber());
				r.setPageSize(page.getPageSize());
				r.setTotalCount(page.getTotalCount());
				r.setUserInfoList(list);
			}
		});
	}
	
	public GetUserInfoResult getByUsername(final java.lang.String value) {
		final GetUserInfoResult result = new GetUserInfoResult();
		return getBizTemplate().execute(result,new BizCommand(){
			public void execute() throws ActionException, Exception {
				UserInfoDTO target = UserInfoServiceConverter.convert(userInfoManager.getByUsername(value));
				result.setUserInfo(target);
			}
		});
	}	
	
	public GetUserInfoResult getByAge(final java.lang.Integer value) {
		final GetUserInfoResult result = new GetUserInfoResult();
		return getBizTemplate().execute(result,new BizCommand(){
			public void execute() throws ActionException, Exception {
				UserInfoDTO target = UserInfoServiceConverter.convert(userInfoManager.getByAge(value));
				result.setUserInfo(target);
			}
		});
	}	
	
	public BizTemplate getBizTemplate() {
		BizTemplate bizTemplate = new BizTemplate();
		return bizTemplate;
	}
}
