package com.company.project.ws.result;

import java.util.List;

import com.company.project.service.dto.UserInfoDTO;
import com.company.project.service.dto.result.base.PageQueryResult;

public class UserInfoPageQueryResult extends PageQueryResult<UserInfoDTO>{

    private static final long serialVersionUID = -2705354463547382035L;

    public UserInfoPageQueryResult() {
        super();
    }

    public UserInfoPageQueryResult(List<UserInfoDTO> itemList,int pageNo, int pageSize, long totalCount) {
        super(pageNo, pageSize, totalCount);
        setItemList(itemList);
    }

    public UserInfoPageQueryResult(String errorCode,String errorDetails) {
        super(errorCode, errorDetails);
    }
    
}
