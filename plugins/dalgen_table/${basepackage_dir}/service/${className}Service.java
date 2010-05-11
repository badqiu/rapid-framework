<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

<#include "/java_imports.include">
public interface ${className}Service{

    public void create${className}(${className} v);
    
    public void update${className}(${className} v);
    
    public void delete${className}(int id);
    
    public void get${className}(int id);
    
    public void pageQuery${className}(${className}Query q);
    
}
