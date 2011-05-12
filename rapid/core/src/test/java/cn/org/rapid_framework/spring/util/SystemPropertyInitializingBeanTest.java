package cn.org.rapid_framework.spring.util;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Test;


public class SystemPropertyInitializingBeanTest {
	@Test
	public void test() throws Exception{
		SystemPropertyInitializingBean b = new SystemPropertyInitializingBean();
		b.afterPropertiesSet();
		System.out.println(System.getProperty("hostname"));
	}
	
}
