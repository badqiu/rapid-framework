/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.company.project.ws.impl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.org.rapid_framework.exception.ErrorCodeException;
import cn.org.rapid_framework.util.PageList;

import com.company.project.repository.UserInfoRepository;
import com.company.project.repository.model.UserInfo;
import com.company.project.service.converter.UserInfoServiceConverter;
import com.company.project.service.dto.UserInfoDTO;
import com.company.project.service.dto.query.UserInfoQueryDTO;
import com.company.project.service.dto.result.base.WSResult;
import com.company.project.ws.UserInfoWebService;
import com.company.project.ws.result.UserInfoPageQueryResult;
import com.company.project.ws.result.UserInfoResult;

public class UserInfoWebServiceImpl implements UserInfoWebService {


    private UserInfoRepository userInfoRepository;
    /**增加setXXXX()方法,spring就可以通过autowire自动设置对象属性,请注意大小写*/
    public void setUserInfoRepository(UserInfoRepository dao) {
        this.userInfoRepository = dao;
    }

    public UserInfoResult createUserInfo(UserInfoDTO userInfoDTO) {
        Assert.notNull(userInfoDTO,"'userInfo' must be not null");
        
        try {
            UserInfo userInfo = UserInfoServiceConverter.convert2UserInfo(userInfoDTO);
            initDefaultValuesForCreate(userInfo);
            new UserInfoChecker().checkCreateUserInfo(userInfo);
            
            UserInfoDTO resultDTO = UserInfoServiceConverter.convert2UserInfoDTO(userInfoRepository.createUserInfo(userInfo));
            return new UserInfoResult(resultDTO);
        }catch(IllegalArgumentException e) {
            throw e;
        }catch(ErrorCodeException e){
            return new UserInfoResult(e.getErrorCode(),e.getMessage());
        }catch(Exception e) {
            return new UserInfoResult("SYSTEM_ERROR","系统内部发生错误");
        }
    }
    
    public WSResult updateUserInfo(UserInfoDTO userInfoDTO) {
        Assert.notNull(userInfoDTO,"'userInfo' must be not null");
        try {
            UserInfo userInfo = UserInfoServiceConverter.convert2UserInfo(userInfoDTO);
            new UserInfoChecker().checkUpdateUserInfo(userInfo);
            userInfoRepository.updateUserInfo(userInfo);
            
            return new WSResult();
        }catch(IllegalArgumentException e) {
            throw e;
        }catch(ErrorCodeException e){
            return new WSResult(e.getErrorCode(),e.getMessage());
        }catch(Exception e) {
            return new WSResult("SYSTEM_ERROR","系统内部发生错误");
        }
    }   

    public WSResult deleteUserInfoById(java.lang.Long id) {
        Assert.notNull(id,"'id' must be not null");
        try {
            this.userInfoRepository.removeUserInfoById(id);
            
            return new WSResult();
        }catch(IllegalArgumentException e) {
            throw e;
        }catch(ErrorCodeException e){
            return new WSResult(e.getErrorCode(),e.getMessage());
        }catch(Exception e) {
            return new WSResult("SYSTEM_ERROR","系统内部发生错误");
        }
    }
    
    public UserInfoResult getUserInfoById(java.lang.Long id) {
        Assert.notNull(id,"'id' must be not null");
        try {
            UserInfoDTO resultDTO = UserInfoServiceConverter.convert2UserInfoDTO(userInfoRepository.queryUserInfoById(id));
            
            return new UserInfoResult(resultDTO);
        }catch(IllegalArgumentException e) {
            throw e;
        }catch(ErrorCodeException e){
            return new UserInfoResult(e.getErrorCode(),e.getMessage());
        }catch(Exception e) {
            return new UserInfoResult("SYSTEM_ERROR","系统内部发生错误");
        }
    }
    
    @Transactional(readOnly=true)
    public UserInfoPageQueryResult findPage(UserInfoQueryDTO query) {
        Assert.notNull(query,"'query' must be not null");
        try {
            PageList<UserInfo> list = userInfoRepository.findPage(UserInfoServiceConverter.convert2UserInfoQuery(query));
            return new UserInfoPageQueryResult(UserInfoServiceConverter.convert2UserInfoDTOList(list),list.getPageSize(),list.getPageNo(),list.getTotalCount());
        }catch(IllegalArgumentException e) {
            throw e;
        }catch(ErrorCodeException e){
            return new UserInfoPageQueryResult(e.getErrorCode(),e.getMessage());
        }catch(Exception e) {
            return new UserInfoPageQueryResult("SYSTEM_ERROR","系统内部发生错误");
        }
    }
    
/*    
    @Transactional(readOnly=true)
    public UserInfoDTO getByUsername(java.lang.String v) {
        return UserInfoServiceConverter.convert2UserInfoDTO(userInfoRepository.getByUsername(v));
    }   
    
    @Transactional(readOnly=true)
    public UserInfoDTO getByAge(java.lang.Integer v) {
        return UserInfoServiceConverter.convert2UserInfoDTO(userInfoRepository.getByAge(v));
    }   
    
*/
    
    private void initDefaultValuesForCreate(UserInfo v) {
    }
    
    public class UserInfoChecker {
        /**可以在此检查只有更新才需要的特殊检查 */
        public void checkUpdateUserInfo(UserInfo v) {
            checkUserInfo(v);
        }
    
        /**可以在此检查只有创建才需要的特殊检查 */
        public void checkCreateUserInfo(UserInfo v) {
            checkUserInfo(v);
        }
        
        /** 检查到有错误请直接抛异常，不要使用 return errorCode的方式 */
        public void checkUserInfo(UserInfo v) {
            //各个属性的检查一般需要分开写几个方法，如 checkProperty1(v),checkProperty2(v)
        }
    }
    
}
