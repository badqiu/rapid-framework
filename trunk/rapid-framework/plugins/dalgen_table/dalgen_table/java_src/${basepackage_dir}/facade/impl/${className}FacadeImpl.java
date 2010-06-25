<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.facade.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.company.project.facade.UserInfoFacade;
import com.company.project.facade.impl.UserInfoFacadeImpl;
import com.company.project.service.UserInfoService;
import com.company.project.service.dto.UserInfoDTO;
import com.company.project.service.dto.query.UserInfoQueryDTO;
import com.company.project.service.dto.result.base.WSResult;
import com.company.project.ws.result.UserInfoPageQueryResult;
import com.company.project.ws.result.UserInfoResult;

import cn.org.rapid_framework.exception.ErrorCodeException;
import cn.org.rapid_framework.exception.ErrorCodeImpl;
import cn.org.rapid_framework.util.PageList;

import ${basepackage}.facade.${className}Facade;
import ${basepackage}.service.${className}Service;
import ${basepackage}.service.dto.${className}DTO;
import ${basepackage}.service.dto.query.${className}QueryDTO;

public class UserInfoFacadeImpl implements UserInfoFacade,InitializingBean {
    private static Log log = LogFactory.getLog(UserInfoFacadeImpl.class);
    private UserInfoService userInfoService;
    
    public void afterPropertiesSet() throws Exception {
        if("true".equals(System.getProperty("bean.properties.check","true"))) {
            Assert.notNull(userInfoService,"'userInfoService' must be not null");
        }
    }
    
    public UserInfoResult createUserInfo(UserInfoDTO userInfo){
        try {
            return new UserInfoResult(userInfoService.createUserInfo(userInfo));
        }catch(Exception ex) {
            ErrorCodeException e = handleException(ex);
            return new UserInfoResult(e.getErrorCode(),e.getMessage());
        }
    }
    
    public WSResult updateUserInfo(UserInfoDTO userInfo){
        try {
            userInfoService.updateUserInfo(userInfo);
            return new WSResult();
        }catch(Exception ex) {
            ErrorCodeException e = handleException(ex);
            return new WSResult(e.getErrorCode(),e.getMessage());
        }
    }
    
    public WSResult removeUserInfo(Long id) {
        try {
            userInfoService.deleteUserInfoById(id);
            return new WSResult();
        }catch(Exception ex) {
            ErrorCodeException e = handleException(ex);
            return new WSResult(e.getErrorCode(),e.getMessage());
        }
    }
    
    public UserInfoResult queryUserInfoById(Long id) {
        try {
            return new UserInfoResult(userInfoService.getUserInfoById(id));
        }catch(Exception ex) {
            ErrorCodeException e = handleException(ex);
            return new UserInfoResult(e.getErrorCode(),e.getMessage());
        }
    }
    
    public UserInfoPageQueryResult findPage(UserInfoQueryDTO query) {
        try {
            PageList<UserInfoDTO> page = userInfoService.findPage(query);
            return new UserInfoPageQueryResult(page,page.getPageNo(),page.getPageSize(),page.getTotalCount());
        }catch(Exception ex) {
            ErrorCodeException e = handleException(ex);
            return new UserInfoPageQueryResult(e.getErrorCode(),e.getMessage());
        }
    }
    
    private ErrorCodeException handleException(Exception e) {
        if(e instanceof IllegalArgumentException) {
            throw (IllegalArgumentException)e;
        }
        if(e instanceof IllegalStateException) {
            throw (IllegalStateException)e;
        }
        if(e instanceof ErrorCodeException) {
            return (ErrorCodeException)e;
        }
        
        log.warn("unknow exception",e);
        return new ErrorCodeException(new ErrorCodeImpl(WSResult.SYSTEM_UNKNOW_ERROR,e.getMessage()),e);
    }

}

