/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.company.project.repository.impl;

import java.util.List;

import cn.org.rapid_framework.util.PageList;

import com.company.project.dal.daointerface.UserInfoDAO;
import com.company.project.dal.dataobject.UserInfoDO;
import com.company.project.dal.query.UserInfoQuery;
import com.company.project.repository.UserInfoRepository;
import com.company.project.repository.converter.UserInfoRepositoryConverter;
import com.company.project.repository.model.UserInfo;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */


public class UserInfoRepositoryImpl implements UserInfoRepository {
    
    private UserInfoDAO userInfoDAO;
        
    public void updateUserInfo(UserInfo userInfo) {
        UserInfoDO target = UserInfoRepositoryConverter.convert2UserInfoDO(userInfo);
        userInfoDAO.update(target);        
    }
    
    public UserInfo createUserInfo(UserInfo userInfo){
        UserInfoDO target = UserInfoRepositoryConverter.convert2UserInfoDO(userInfo);
        userInfo.setUserId(userInfoDAO.insert(target));
        return userInfo;
    }
    
    public void removeUserInfoById(Long id) {
        userInfoDAO.deleteById(id);
    }
    
    public UserInfo queryUserInfoById(Long id) {
        return UserInfoRepositoryConverter.convert2UserInfo(userInfoDAO.queryById(id));
    }
    
    public PageList<UserInfo> findPage(UserInfoQuery query) {
        PageList<UserInfoDO> sourceList = userInfoDAO.findPage(query);    
        List<UserInfo> targetList = UserInfoRepositoryConverter.convert2UserInfoList(sourceList);
        return new PageList(targetList,sourceList.getPageSize(),sourceList.getPageNo(),sourceList.getTotalCount());
    }
    
}
