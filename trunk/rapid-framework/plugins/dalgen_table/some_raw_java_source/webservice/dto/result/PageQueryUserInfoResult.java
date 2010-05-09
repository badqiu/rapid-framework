package com.company.project.facade.dto.result;

import java.util.List;

import com.company.project.facade.dto.UserInfoDTO;
import com.company.project.facade.dto.result.iface.impl.PageQueryResultImpl;

public class PageQueryUserInfoResult extends PageQueryResultImpl{
	
	public List<UserInfoDTO> userInfoList;
	
	public PageQueryUserInfoResult() {
	}
	
	public PageQueryUserInfoResult(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
	}
	
	public List<UserInfoDTO> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List<UserInfoDTO> userInfoList) {
		this.userInfoList = userInfoList;
	}
	
}
