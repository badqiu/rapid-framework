package com.company.project.ws.result;

import com.company.project.service.dto.UserInfoDTO;
import com.company.project.service.dto.result.base.WSResult;

public class UserInfoResult extends WSResult{
    UserInfoDTO userInfo;

    public UserInfoResult(UserInfoDTO userInfo) {
        super(true);
        this.userInfo = userInfo;
    }

    public UserInfoResult() {
        super();
    }

    public UserInfoResult(boolean isSuccess, String errorCode,
                          String errorDetails) {
        super(isSuccess, errorCode, errorDetails);
    }
    
}
