package cn.org.rapid_framework.generator;

import java.util.Properties;

import junit.framework.TestCase;

public class GeneratorPropertiesTest extends TestCase {
	public void setUp() {
		GeneratorProperties.getProperties().setProperty("blank", "  ");
		GeneratorProperties.getProperties().setProperty("empty", "");
	}
	
	public void testgetNullIfBlankProperty() {
		assertEquals(null,GeneratorProperties.getNullIfBlank("blank"));
		assertEquals(null,GeneratorProperties.getNullIfBlank("empty"));
		assertEquals(null,GeneratorProperties.getNullIfBlank("null"));
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
	
	public void test() {
//	    GeneratorProperties.setProperty(GeneratorConstants.GENERATOR_TOOLS_CLASS, "StringDiy");
	    GeneratorProperties.setProperties(new Properties());
	    for(GeneratorConstants key : GeneratorConstants.values()) {
	        GeneratorProperties.getBoolean(key);
	        GeneratorProperties.getNullIfBlank(key);
	        GeneratorProperties.getStringArray(key);
	        try {
	        GeneratorProperties.getRequiredProperty(key);
	        fail();
	        }catch(IllegalStateException e) {
	        }
	    }
	}
}
