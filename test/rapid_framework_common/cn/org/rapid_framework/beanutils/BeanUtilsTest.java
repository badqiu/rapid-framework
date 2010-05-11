package cn.org.rapid_framework.beanutils;

import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import org.apache.commons.beanutils.PropertyUtils;

import cn.org.rapid_framework.util.fortest.Role;

public class BeanUtilsTest extends TestCase {
	
	Role role = new Role();
	public void testSetBeanProperty() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		role.setRoleId(new Long(1));
		BeanUtils.setProperty(role, "roleId", "2");
		assertEquals(role.getRoleId(),new Long(2));
	}
}
