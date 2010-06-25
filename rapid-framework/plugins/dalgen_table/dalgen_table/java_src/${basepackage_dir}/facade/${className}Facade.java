<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.facade;

import com.company.project.service.dto.UserInfoDTO;
import com.company.project.service.dto.query.UserInfoQueryDTO;
import com.company.project.service.dto.result.base.WSResult;
import com.company.project.ws.result.UserInfoPageQueryResult;
import com.company.project.ws.result.UserInfoResult;

import cn.org.rapid_framework.util.PageList;

import ${basepackage}.facade.${className}Facade;
import ${basepackage}.service.${className}Service;
import ${basepackage}.service.dto.${className}DTO;
import ${basepackage}.service.dto.query.${className}QueryDTO;

public interface UserInfoFacade {
    
    public UserInfoResult createUserInfo(UserInfoDTO userInfo);
    
    public WSResult updateUserInfo(UserInfoDTO userInfo);
    
    public WSResult removeUserInfo(Long id);
    
    public UserInfoResult queryUserInfoById(Long id);
    
    public UserInfoPageQueryResult findPage(UserInfoQueryDTO query);
    
}
