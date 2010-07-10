package com.company.project.ws.result;

import com.company.project.service.dto.UserInfoDTO;
import com.company.project.service.dto.result.base.WSResult;

public class UserInfoResult extends WSResult{
    private UserInfoDTO userInfo;

    public UserInfoResult() {
        super();
    }
    
    public UserInfoResult(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfoResult(String errorCode,String errorDetails) {
        super(errorCode, errorDetails);
    }
    
    public UserInfoDTO getUserInfo() {
    	return userInfo;
    }
    
}
