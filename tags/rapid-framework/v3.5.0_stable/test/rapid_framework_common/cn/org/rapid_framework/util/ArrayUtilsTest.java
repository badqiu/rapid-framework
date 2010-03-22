package cn.org.rapid_framework.util;

import java.util.Map;

import junit.framework.TestCase;

public class ArrayUtilsTest extends TestCase {
	String[] array = new String[]{"1","2","3"};
	
	public void testWithOneKey() {
		Map map = ArrayUtils.toMap(array,"age");
		assertNotNull(map);
		assertEquals("1",map.get("age"));
		assertEquals(1,map.size());
		
	}

	public void testWithNullArguments() {
		Map map = ArrayUtils.toMap(null,"age");
		assertNotNull(map);
		assertEquals(0,map.size());
	}
	
	public void testWithManyKey() {
		Map map = ArrayUtils.toMap(array,"age","height","width","many");
		assertNotNull(map);
		assertEquals("1",map.get("age"));
		assertEquals("2",map.get("height"));
		assertEquals("3",map.get("width"));
		assertEquals(3,map.size());
		
	}
	
}
