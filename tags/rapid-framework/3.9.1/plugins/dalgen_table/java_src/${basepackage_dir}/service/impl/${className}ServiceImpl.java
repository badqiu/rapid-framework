<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.service.impl;

import java.util.List;

public class ${className}ServiceImpl  implements ${className}Service{
    private ${className}Repository ${classNameLower}Repository;
    public ${className} update(${className} o) {
        return ${classNameLower}Repository.update(o);
    }
    
    public ${className} create(${className} o) {
        return ${classNameLower}Repository.create(o);
    }
    
    public void removeById(${table.idColumn.javaType} id) {
        ${classNameLower}Repository.removeById(id);
    }
    
    public ${className} queryById(${table.idColumn.javaType} id) {
        return ${classNameLower}Repository.queryById(id);
    }
    
    public PageList<${className}> findPage(${className}Query query) {
        return ${classNameLower}Repository.findPage(query);
    }
    
}
