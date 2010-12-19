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
	
	public void testToMapWithKeyArray(){
		String[] keys = new String[]{"age", "height", "width", "many"};
		Map map = ArrayUtils.toMap(array, keys);
		assertNotNull(map);
		assertEquals("1", map.get(keys[0]));
		assertEquals("2", map.get(keys[1]));
		assertEquals("3", map.get(keys[2]));
		assertEquals(3, map.size());
	}
}
