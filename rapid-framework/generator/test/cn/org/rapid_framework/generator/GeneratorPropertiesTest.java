package cn.org.rapid_framework.generator;

import junit.framework.TestCase;

public class GeneratorPropertiesTest extends TestCase {
	public void setUp() {
		GeneratorProperties.getProperties().setProperty("blank", "  ");
		GeneratorProperties.getProperties().setProperty("empty", "");
	}
	
	public void testgetNullIfBlankProperty() {
		assertEquals(null,GeneratorProperties.getNullIfBlankProperty("blank"));
		assertEquals(null,GeneratorProperties.getNullIfBlankProperty("empty"));
		assertEquals(null,GeneratorProperties.getNullIfBlankProperty("null"));
	}
	
	public void testgetRequiredProperty() {
		try {
			assertEquals(null,GeneratorProperties.getRequiredProperty("blank"));
			fail();
		}catch(Exception e) {
		}
		try {
			assertEquals(null,GeneratorProperties.getRequiredProperty("empty"));
			fail();
		}catch(Exception e) {
		}
		try {
			assertEquals(null,GeneratorProperties.getRequiredProperty("null"));
			fail();
		}catch(Exception e) {
		}
	}
}
