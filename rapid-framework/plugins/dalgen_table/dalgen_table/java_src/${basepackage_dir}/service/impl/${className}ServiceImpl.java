<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.org.rapid_framework.util.PageList;

import ${basepackage}.dal.query.${className}Query;
import ${basepackage}.repository.model.${className};
import ${basepackage}.repository.${className}Repository;
import ${basepackage}.service.UserInfoService;

@Service
@Transactional
public class ${className}ServiceImpl implements ${className}Service {

    private ${className}Repository ${classNameLower}Repository;
    /**增加setXXXX()方法,spring就可以通过autowire自动设置对象属性,请注意大小写*/
    public void set${className}Repository(${className}Repository dao) {
        this.${classNameLower}Repository = dao;
    }

    /** */
    public ${className} create${className}(${className} ${classNameLower}) {
        Assert.notNull(${classNameLower},"'${classNameLower}' must be not null");
        initDefaultValuesForCreate(${classNameLower});
        new ${className}Checker().checkCreate${className}(${classNameLower});
        this.${classNameLower}Repository.create${className}(${classNameLower});
        return ${classNameLower};
    }
    
    public ${className} update${className}(${className} ${classNameLower}) {
        Assert.notNull(${classNameLower},"'${classNameLower}' must be not null");
        new ${className}Checker().checkUpdate${className}(${classNameLower});
        this.${classNameLower}Repository.update${className}(${classNameLower});
        return ${classNameLower};
    }   

    public void delete${className}ById(${table.idColumn.javaType} id) {
        Assert.notNull(id,"'id' must be not null");
        this.${classNameLower}Repository.remove${className}ById(id);
    }
    
    public ${className} get${className}ById(${table.idColumn.javaType} id) {
        Assert.notNull(id,"'id' must be not null");
        return this.${classNameLower}Repository.query${className}ById(id);
    }
    
    @Transactional(readOnly=true)
    public PageList<UserInfo> findPage(${className}Query query) {
        Assert.notNull(query,"'query' must be not null");
        return ${classNameLower}Repository.findPage(query);
    }
/*    
<#list table.columns as column>
    <#if column.unique && !column.pk>
    @Transactional(readOnly=true)
    public ${className} getBy${column.columnName}(${column.javaType} v) {
        return ${classNameLower}Repository.getBy${column.columnName}(v);
    }   
    
    </#if>
</#list>
*/
    
    private void initDefaultValuesForCreate(${className} v) {
    }
    
    public class ${className}Checker {
        /**可以在此检查只有更新才需要的特殊检查 */
        public void checkUpdate${className}(${className} v) {
            check${className}(v);
        }
    
        /**可以在此检查只有创建才需要的特殊检查 */
        public void checkCreate${className}(${className} v) {
            check${className}(v);
        }
        
        /** 检查到有错误请直接抛异常，不要使用 return errorCode的方式 */
        public void check${className}(${className} v) {
            //各个属性的检查一般需要分开写几个方法，如 checkProperty1(v),checkProperty2(v)
        }
    }
}
