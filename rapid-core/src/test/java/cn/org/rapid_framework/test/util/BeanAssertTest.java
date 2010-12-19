package cn.org.rapid_framework.test.util;

import junit.framework.TestCase;
import cn.org.rapid_framework.jdbc.sqlgenerator.metadata.CommontBean;
import cn.org.rapid_framework.test.util.testbean.Bean1;

public class BeanAssertTest extends TestCase {
	
	public void test1() {
		Bean1 b = new Bean1();
		try {
		BeanAssert.assertPropertiesNotNull(b);
		fail();
		}catch(AssertionError e) {
			e.getMessage().contains("AssertionFailedError, [TestBean.userName] must be not null");
		}
	}

	public void test2() {
		Bean1 b = new Bean1();
		BeanDefaultValueUtils.setBeanProperties(b);
		BeanAssert.assertPropertiesNotNull(b);
	}

	public void test3() {
		CommontBean b = new CommontBean();
		b.setUserName("2");
		b.setAge(1);
		b.setPassword("p");
		BeanAssert.assertPropertiesNotNull(b);
	}

	public void test3_ignore_properties() {
		CommontBean b = new CommontBean();
		b.setAge(1);
		BeanAssert.assertPropertiesNotNull(b,new String[]{"password","userName"});
		b.setUserName("usr");
		BeanAssert.assertPropertiesNotNull(b,new String[]{"password"});
		
		try {
			BeanAssert.assertPropertiesNotNull(b);
			fail();
		}catch(Error e) {
			assertTrue(e.getMessage(),e.getMessage().contains("[CommontBean.password] must be not null"));
		}
	}

//	public void test2_assertNumberPropertiesNotNull() {
//		if (true)
//			throw new RuntimeException("not yet implements");
//		Bean1 b = new Bean1();
//		BeanDefaultValueUtils.setBeanProperties(b);
//		BeanAssert.assertNumberPropertiesEquals(b, "1", new String[] {});
//	}
//
//	public void test2_assertPrimitivePropertiesEquals() {
//		if (true)
//			throw new RuntimeException("not yet implements");
//		Bean1 b = new Bean1();
//		BeanDefaultValueUtils.setBeanProperties(b);
//		BeanAssert.assertPrimitivePropertiesEquals(b, "1", new String[] {});
//	}
}
