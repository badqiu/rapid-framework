package cn.org.rapid_framework.util;

import java.util.Properties;

import junit.framework.TestCase;

public class PropertiesHelperTest extends TestCase {
	
	PropertiesHelper p = new PropertiesHelper(new Properties());
	public void setUp() {
		p.setProperty("empty", "");
		p.setProperty("blank", "   ");
		p.setProperty("1", "1");
	}
	public void test() {
		assertEquals(null,p.getNullIfBlank("blank"));
		assertEquals(null,p.getNullIfBlank("empty"));
		assertNotNull(p.getAndTryFromSystem("user.home")); //test System.getProperty()
		assertNotNull(p.getAndTryFromSystem("PATH")); //test System.getenv()
		
		assertEquals(1,p.getRequiredInt("1"));
	}
}
