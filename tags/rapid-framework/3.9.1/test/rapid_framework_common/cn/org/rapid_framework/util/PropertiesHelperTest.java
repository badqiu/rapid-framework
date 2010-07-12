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
	
	public void testOverrideNever() {
		assertEquals(null,p.getProperty("user.home"));
	}
	
	public void testOverrideMode() {
		p = new PropertiesHelper(p.getProperties(),PropertiesHelper.SYSTEM_PROPERTIES_MODE_OVERRIDE);
		
		p.setProperty("PATH", "1");
		p.setProperty("user.home", "1");
		
		assertFalse("1".equals(p.getProperty("user.home")));
	}
	
	public void testFallbackMode() {
		p = new PropertiesHelper(p.getProperties(),PropertiesHelper.SYSTEM_PROPERTIES_MODE_FALLBACK);
		assertNotNull(p.getProperty("user.home"));
	}
}
