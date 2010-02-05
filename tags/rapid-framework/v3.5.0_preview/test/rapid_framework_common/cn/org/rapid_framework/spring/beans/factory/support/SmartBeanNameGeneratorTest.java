package cn.org.rapid_framework.spring.beans.factory.support;

import cn.org.rapid_framework.spring.beans.factory.support.SmartBeanNameGenerator;
import junit.framework.TestCase;

public class SmartBeanNameGeneratorTest extends TestCase {

	SmartBeanNameGenerator g = new SmartBeanNameGenerator();
	public void testGenerateBeanName() {
		
		assertEquals("foo",g.generateBeanName("Foo"));
		assertEquals("foo",g.generateBeanName("foo"));
		
		assertEquals("foo",g.generateBeanName("fooImpl"));
		
		assertEquals("fooImplAbc",g.generateBeanName("fooImplAbc"));
		assertEquals("mysqlFoo",g.generateBeanName("mysqlFooImpl"));
		
		g.setRemovedPrefix("mysql");
		assertEquals("foo",g.generateBeanName("mysqlFoo"));
		assertEquals("foo",g.generateBeanName("MysqlFoo"));
		
	}
}
