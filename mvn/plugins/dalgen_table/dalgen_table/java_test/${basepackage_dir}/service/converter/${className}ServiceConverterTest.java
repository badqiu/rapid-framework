<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.converter;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.test.util.BeanAssert;
import cn.org.rapid_framework.test.util.BeanDefaultValueUtils;

import com.company.project.dal.dataobject.UserInfoDO;
import com.company.project.repository.converter.UserInfoRepositoryConverter;
import com.company.project.repository.model.UserInfo;
import com.company.project.service.converter.UserInfoServiceConverter;
import com.company.project.service.dto.UserInfoDTO;

import junit.framework.TestCase;

<#include "/java_imports.include">
public class ${className}ServiceConverterTest extends TestCase {

    public void test_convert2UserInfoDO() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        UserInfo source = new UserInfo();
        BeanDefaultValueUtils.setBeanProperties(source);
        UserInfoDTO target = UserInfoServiceConverter.convert2UserInfoDTO(source);
        BeanAssert.assertPropertiesNotNull(target);
        System.out.println(BeanUtils.describe(target));
    }
    
    public void test_convert2UserInfo() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        UserInfoDTO source = new UserInfoDTO();
        BeanDefaultValueUtils.setBeanProperties(source);
        UserInfo target = UserInfoServiceConverter.convert2UserInfo(source);
        BeanAssert.assertPropertiesNotNull(target);
        System.out.println(BeanUtils.describe(target));
    }
    
}
