package com.company.project.ws.result;

import java.util.List;

import com.company.project.service.dto.UserInfoDTO;
import com.company.project.service.dto.result.base.QueryResult;

public class UserInfoQueryResult extends QueryResult<UserInfoDTO>{

    private static final long serialVersionUID = -2705354463547382035L;

    public UserInfoQueryResult() {
        super();
    }

    public UserInfoQueryResult(List<UserInfoDTO> itemList) {
        setItemList(itemList);
    }

    public UserInfoQueryResult(String errorCode,String errorDetails) {
        super(errorCode, errorDetails);
    }
    
}
