<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.company.project.repository.UserInfoRepository;
import com.company.project.repository.model.UserInfo;
import com.company.project.service.converter.UserInfoServiceConverter;
import com.company.project.service.dto.UserInfoDTO;
import com.company.project.service.dto.query.UserInfoQueryDTO;
import com.company.project.service.impl.UserInfoServiceImpl.UserInfoChecker;

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

    public ${className}DTO create${className}(${className}DTO ${classNameLower}DTO) {
        Assert.notNull(${classNameLower}DTO,"'${classNameLower}' must be not null");
        ${className} ${classNameLower} = ${className}ServiceConverter.convert2${className}(${classNameLower}DTO);
        initDefaultValuesForCreate(${classNameLower});
        new ${className}Checker().checkCreate${className}(${classNameLower});
        return ${className}ServiceConverter.convert2${className}DTO(this.${classNameLower}Repository.create${className}(${classNameLower}));
    }
    
    public void update${className}(${className}DTO ${classNameLower}DTO) {
        Assert.notNull(${classNameLower}DTO,"'${classNameLower}' must be not null");
        ${className} ${classNameLower} = ${className}ServiceConverter.convert2${className}(${classNameLower}DTO);
        new ${className}Checker().checkUpdate${className}(${classNameLower});
        ${classNameLower}Repository.update${className}(${classNameLower});
    }   

    public void delete${className}ById(java.lang.Long id) {
        Assert.notNull(id,"'id' must be not null");
        this.${classNameLower}Repository.remove${className}ById(id);
    }
    
    public ${className}DTO get${className}ById(java.lang.Long id) {
        Assert.notNull(id,"'id' must be not null");
        return ${className}ServiceConverter.convert2${className}DTO(${classNameLower}Repository.query${className}ById(id));
    }
    
    @Transactional(readOnly=true)
    public PageList<${className}DTO> findPage(${className}QueryDTO query) {
        Assert.notNull(query,"'query' must be not null");
        PageList<${className}> resultList = ${classNameLower}Repository.findPage(${className}ServiceConverter.convert2${className}Query(query));
        return new PageList<${className}DTO>(${className}ServiceConverter.convert2${className}DTOList(resultList),resultList);
    }
    
    @Transactional(readOnly=true)
    public ${className}DTO getByUsername(java.lang.String v) {
        return ${className}ServiceConverter.convert2${className}DTO(${classNameLower}Repository.getByUsername(v));
    }   
    
    @Transactional(readOnly=true)
    public ${className}DTO getByAge(java.lang.Integer v) {
        return ${className}ServiceConverter.convert2${className}DTO(${classNameLower}Repository.getByAge(v));
    }   
    
    
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
