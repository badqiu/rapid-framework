<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.impl;

import ${basepackage}.service.${className}Service;

<#include "/java_imports.include">
@Service("${classNameLower}Service")
public class ${className}ServiceImpl implements ${className}Service{

    private ${className}Dao ${classNameLower}Dao;
    /**增加setXXXX()方法,spring就可以通过autowire自动设置对象属性,请注意大小写*/
    public void set${className}Dao(${className}Dao dao) {
        this.${classNameLower}Dao = dao;
    }
    
    public void create${className}(${className} v) {
        ${classNameLower}Dao.insert(v);
    }
    
    public void update${className}(${className} v) {
        ${classNameLower}Dao.update(v);
    }
    
    public void delete${className}(int id) {
        ${classNameLower}Dao.delete(id);
    }
    
    public void get${className}(int id) {
        ${classNameLower}Dao.queryById(id);
    }
    
    public void pageQuery${className}(${className}Query q) {
        ${classNameLower}Dao.pageQuery$(q);
    }
    
}
