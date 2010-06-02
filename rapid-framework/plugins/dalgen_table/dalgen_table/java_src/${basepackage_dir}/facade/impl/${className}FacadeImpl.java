

package com.company.project.facade.impl;

import cn.org.rapid_framework.util.PageList;

import com.company.project.facade.UserInfoFacade;
import com.company.project.service.UserInfoService;
import com.company.project.service.dto.UserInfoDTO;
import com.company.project.service.dto.query.UserInfoQueryDTO;

public class UserInfoFacadeImpl implements UserInfoFacade {
    UserInfoService userInfoService;
    public UserInfoDTO createUserInfo(UserInfoDTO userInfo){
        return userInfoService.createUserInfo(userInfo);
    }
    
    public void updateUserInfo(UserInfoDTO userInfo){
        userInfoService.updateUserInfo(userInfo);
    }
    
    public void removeUserInfo(Long id) {
        userInfoService.deleteUserInfoById(id);
    }
    
    public UserInfoDTO queryUserInfoById(Long id) {
        return userInfoService.getUserInfoById(id);
    }
    
    public PageList<UserInfoDTO> findPage(UserInfoQueryDTO query) {
        return userInfoService.findPage(query);
    }
    
}
