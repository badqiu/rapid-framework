package cn.org.rapid_framework.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

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
		
		assertNull(m.get("9527name"));
		assertNull(m.get("notexistmethod"));
	}
	
	public void testPerfoemance() {
		MapAndObject m = new MapAndObject(map,role);
		long start = System.currentTimeMillis();
		int count = 10000 * 10;
		for(int i = 0; i < count; i++) {
			String random = RandomStringUtils.randomAlphabetic(2);
			assertNull(m.get(random));
		}
		long cost = System.currentTimeMillis() - start;
		System.out.println("MapAndObject.get() costTime:"+cost+" per request cost:"+(cost/(float)count)+" count="+count);
		assertTrue("MapAndObject.get() costTime:234 per request cost:0.00234 count=100000",cost < 1000);
	}
	
	public void testPerfoemance2() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		long start = System.currentTimeMillis();
		int count = 10000 * 10;
		for(int i = 0; i < count; i++) {
			BeanUtils.describe(role);
		}
		long cost = System.currentTimeMillis() - start;
		System.out.println("BeanUtils.describe(role) costTime:"+cost+" per request cost:"+(cost/(float)count));
		
	}
	
	
	public void testPropertyUtils() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		map.put("sex", 123L);
		assertEquals(123L,PropertyUtils.getProperty(map, "sex"));
		assertEquals("123",BeanUtils.getProperty(map, "sex"));
	}
	
	public void testReadonly() {
		MapAndObject m = new MapAndObject(map,role);
		assertEquals("Readonly from Role.java",m.get("readonly"));
		assertEquals(null,m.get("writeonly"));
	}
}
