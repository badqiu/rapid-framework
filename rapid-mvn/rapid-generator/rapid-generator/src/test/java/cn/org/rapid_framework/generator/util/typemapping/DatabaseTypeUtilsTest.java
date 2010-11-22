package cn.org.rapid_framework.generator.util.typemapping;

import junit.framework.TestCase;

public class DatabaseTypeUtilsTest extends TestCase {
	
	public void test() {
		assertEquals("mysql",DatabaseTypeUtils.getDatabaseTypeByJdbcDriver("com.mysql.Driver"));
		assertEquals("abc123",DatabaseTypeUtils.getDatabaseTypeByJdbcDriver("abc123"));
		assertEquals(null,DatabaseTypeUtils.getDatabaseTypeByJdbcDriver(null));
	}
}
