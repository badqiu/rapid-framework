<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.repository.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import cn.org.rapid_framework.test.util.BeanAssert;
import cn.org.rapid_framework.test.util.BeanDefaultValueUtils;

import com.company.project.dal.dataobject.UserInfoDO;
import com.company.project.repository.converter.UserInfoRepositoryConverter;
import com.company.project.repository.model.UserInfo;
import junit.framework.TestCase;

import static junit.framework.Assert.*;

<#include "/java_imports.include">

public class ${className}RepositoryConverterTest extends TestCase {
	
    public void test_convert2UserInfoDO() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        UserInfo source = new UserInfo();
        BeanDefaultValueUtils.setBeanProperties(source); //BeanDefaultValueUtils可以为bean属性值设置为1
        UserInfoDO target = UserInfoRepositoryConverter.convert2UserInfoDO(source);
        BeanAssert.assertPropertiesNotNull(target);
        System.out.println(BeanUtils.describe(target));
    }
    
    public void test_convert2UserInfo() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        UserInfoDO source = new UserInfoDO();
        BeanDefaultValueUtils.setBeanProperties(source);
        UserInfo target = UserInfoRepositoryConverter.convert2UserInfo(source);
        BeanAssert.assertPropertiesNotNull(target);
        System.out.println(BeanUtils.describe(target));
    }
	
}
