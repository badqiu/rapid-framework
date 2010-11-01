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
	
	public void testGetStringArray() {
	    p.setProperty("array", "1,2 3\t4\n5");
	    String[] array = p.getStringArray("array");
	    assertEquals(array[0],"1");
	    assertEquals(array[1],"2");
	    assertEquals(array[2],"3");
	    assertEquals(array[3],"4");
	    assertEquals(array[4],"5");
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
