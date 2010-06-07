<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.converter;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.test.util.BeanDefaultValueSetterTest;

import com.company.project.dal.dataobject.UserInfoDO;
import com.company.project.repository.converter.UserInfoRepositoryConverter;
import com.company.project.repository.model.UserInfo;

import junit.framework.TestCase;

<#include "/java_imports.include">
public class ${className}ServiceConverterTest extends TestCase {

    public void test_convert2UserInfoDO() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        UserInfo source = new UserInfo();
        BeanDefaultValueSetterTest.setBeanProperties(source);
        UserInfoDO target = UserInfoRepositoryConverter.convert2UserInfoDO(source);
        System.out.println(BeanUtils.describe(target));
    }
    
}
