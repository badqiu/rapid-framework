package cn.org.rapid_framework.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import cn.org.rapid_framework.util.fortest.Role;

public class MapAndObjectTest extends TestCase {
	Map map = new HashMap();
	Role role = new Role();
	public void setUp() {
		map.put("java", "diy");
		map.put("roleId", "from map");
		map.put(123, 456);
		
		role.setRoleName("manager");
		role.setRoleId(2009L);
	}
	
	public void testWithNull() {
		MapAndObject m = new MapAndObject(null,null);
		assertEquals(null,m.get("1"));
		
		m = new MapAndObject(map,null);
		assertEquals("diy",m.get("java"));
		
		m = new MapAndObject(null,role);
		assertEquals("manager",m.get("roleName"));
	}
	
	public void test() {
		
		MapAndObject m = new MapAndObject(map,role);
		assertEquals("from map",m.get("roleId"));
		
		map.put("roleId", null);
		assertEquals(2009L ,m.get("roleId"));
		
		role.setRoleId(null);
		assertEquals(null ,m.get("roleId"));
		
		try {
			m.get("9527name");
			fail();
		}catch(Exception e) {
		}
		
	}
	
	public void testPropertyUtils() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		map.put("sex", 123L);
		assertEquals(123L,PropertyUtils.getProperty(map, "sex"));
		assertEquals("123",BeanUtils.getProperty(map, "sex"));
	}
}
