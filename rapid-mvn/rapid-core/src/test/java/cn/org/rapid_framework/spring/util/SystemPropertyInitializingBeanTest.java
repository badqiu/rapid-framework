package cn.org.rapid_framework.spring.util;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;


public class SystemPropertyInitializingBeanTest {
	@Test
	public void test() throws Exception{
		SystemPropertyInitializingBean b = new SystemPropertyInitializingBean();
		b.afterPropertiesSet();
		System.out.println(System.getProperty("hostname"));
		assertFalse(StringUtils.isEmpty(System.getProperty("hostname")));
	}
}
