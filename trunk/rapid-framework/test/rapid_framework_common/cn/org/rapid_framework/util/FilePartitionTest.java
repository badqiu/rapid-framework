package cn.org.rapid_framework.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class FilePartitionTest extends TestCase {
	
	FilePartition fp = new FilePartition("c:/temp","username","password","age");
	
	public void testparseRartition() {
		Map map = fp.parseRartition("c:/temp/badqiu/123/21/system.txt");
		assertEquals(map.get("username"),"badqiu");
		assertEquals(map.get("password"),"123");
		assertEquals(map.get("age"),"21");
	}
	
	public void testgetPartitionString() {
		Map map = new HashMap();
		String str = fp.getPartitionString(map);
		assertEquals("c:/temp/null/null/null",str);
		
		map.put("username", "jane");
		map.put("password", "pwd");
		map.put("age", "38");
		str = fp.getPartitionString(map);
		assertEquals("c:/temp/jane/pwd/38",str);
	}
}
