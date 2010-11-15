<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.repository.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import cn.org.rapid_framework.test.util.BeanDefaultValueSetterTest;

import com.company.project.dal.dataobject.UserInfoDO;
import com.company.project.repository.converter.UserInfoRepositoryConverter;
import com.company.project.repository.model.UserInfo;

import static junit.framework.Assert.*;

<#include "/java_imports.include">

public class ${className}RepositoryConverterTest extends TestCase {
	
    public void test_convert2UserInfoDO() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        UserInfo source = new UserInfo();
        BeanDefaultValueSetterTest.setBeanProperties(source);
        UserInfoDO target = UserInfoRepositoryConverter.convert2UserInfoDO(source);
        System.out.println(BeanUtils.describe(target));
    }
	
}
