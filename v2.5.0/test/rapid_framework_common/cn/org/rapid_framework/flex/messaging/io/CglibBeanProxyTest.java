package cn.org.rapid_framework.flex.messaging.io;

import junit.framework.TestCase;

public class CglibBeanProxyTest extends TestCase {
	
	public void testRemoveCglibClassSuffix() {
		String result = CglibBeanProxy.removeCglibClassSuffix("com.kingsoft.demo.model.Blog$$EnhancerByCGLIB$$addd6f2d");
		assertEquals("com.kingsoft.demo.model.Blog",result);
	}
}
