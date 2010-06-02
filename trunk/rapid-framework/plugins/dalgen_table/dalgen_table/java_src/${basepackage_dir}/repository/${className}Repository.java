<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   

package ${basepackage}.repository;

import cn.org.rapid_framework.util.PageList;

import ${basepackage}.dal.query.${className}Query;
import ${basepackage}.repository.model.${className};

public interface ${className}Repository  {
    
    public void update${className}(${className} ${classNameLower});
    
    public void create${className}(${className} ${classNameLower});
    
    public void remove${className}ById(Long id);
    
    public void query${className}ById(Long id);
    
    public PageList<${className}> findPage(${className}Query query);
    
}
