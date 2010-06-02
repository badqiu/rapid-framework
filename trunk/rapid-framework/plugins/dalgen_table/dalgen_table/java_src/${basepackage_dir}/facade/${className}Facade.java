<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.facade;

import cn.org.rapid_framework.util.PageList;

import ${basepackage}.facade.${className}Facade;
import ${basepackage}.service.${className}Service;
import ${basepackage}.service.dto.${className}DTO;
import ${basepackage}.service.dto.query.${className}QueryDTO;

public interface ${className}Facade {
    
    public ${className}DTO create${className}(${className}DTO userInfo);
    
    public void update${className}(${className}DTO userInfo);
    
    public void remove${className}(Long id);
    
    public ${className}DTO query${className}ById(Long id);
    
    public PageList<${className}DTO> findPage(${className}QueryDTO query);
    
}
