package com.company.project.facade.dto.result;

import com.company.project.facade.dto.UserInfoDTO;
import com.company.project.facade.dto.result.iface.impl.WSResultImpl;

public class GetUserInfoResult extends WSResultImpl{
	private UserInfoDTO userInfo;

	public UserInfoDTO getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfoDTO userInfo) {
		this.userInfo = userInfo;
	}
	
}
