package cn.org.rapid_framework.util;

import java.util.UUID;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;

public class MiscTest extends TestCase {
	
	public void test_removeString() {
		assertEquals("abc123456",StringUtils.remove("abc-123-456", "-"));
		assertEquals("abc123456",StringUtils.remove(UUID.randomUUID().toString(), "-"));
	}
	
}
