package com.company.project.facade.dto.result;

import java.util.List;

import com.company.project.facade.dto.UserInfoDTO;
import com.company.project.facade.dto.result.iface.impl.PageQueryResultImpl;
import com.company.project.facade.dto.result.iface.impl.WSResultImpl;

public class FindAllUserInfoResult extends WSResultImpl{
	
	List<UserInfoDTO> userInfoList;
	
	public List<UserInfoDTO> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List<UserInfoDTO> userInfoList) {
		this.userInfoList = userInfoList;
	}
	
}
