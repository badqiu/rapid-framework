<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.facade.impl;

import cn.org.rapid_framework.util.PageList;

import ${basepackage}.facade.${className}Facade;
import ${basepackage}.service.${className}Service;
import ${basepackage}.service.dto.${className}DTO;
import ${basepackage}.service.dto.query.${className}QueryDTO;

public class ${className}FacadeImpl implements ${className}Facade {
    ${className}Service userInfoService;
    public ${className}DTO create${className}(${className}DTO userInfo){
        return userInfoService.create${className}(userInfo);
    }
    
    public void update${className}(${className}DTO userInfo){
        userInfoService.update${className}(userInfo);
    }
    
    public void remove${className}(Long id) {
        userInfoService.delete${className}ById(id);
    }
    
    public ${className}DTO query${className}ById(Long id) {
        return userInfoService.get${className}ById(id);
    }
    
    public PageList<${className}DTO> findPage(${className}QueryDTO query) {
        return userInfoService.findPage(query);
    }
    
}
