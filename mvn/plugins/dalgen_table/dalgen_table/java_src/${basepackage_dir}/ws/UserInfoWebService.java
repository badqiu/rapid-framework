/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.company.project.ws;

import com.company.project.service.dto.UserInfoDTO;
import com.company.project.service.dto.query.UserInfoQueryDTO;
import com.company.project.service.dto.result.base.WSResult;
import com.company.project.ws.result.UserInfoPageQueryResult;
import com.company.project.ws.result.UserInfoResult;

public interface UserInfoWebService {


    public UserInfoResult createUserInfo(UserInfoDTO userInfo);
    
    public WSResult updateUserInfo(UserInfoDTO userInfo);

    public WSResult deleteUserInfoById(java.lang.Long id);
    
    public UserInfoResult getUserInfoById(java.lang.Long id);
    
    public UserInfoPageQueryResult findPage(UserInfoQueryDTO query);
    
/*    
    @Transactional(readOnly=true)
    public UserInfoResult getByUsername(java.lang.String v);
    
    @Transactional(readOnly=true)
    public UserInfoResult getByAge(java.lang.Integer v);
    
*/
    
}
