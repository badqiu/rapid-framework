package cn.org.rapid_framework.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.lang.RandomStringUtils;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.util.Partition.PartitionModel;

public class PartitionTest extends TestCase {
	
	public void test() {
		Partition p = new Partition("name","age");
		
		Map map = p.parseRartition("badqiu/321");
		assertEquals(map.get("name"),"badqiu");
		assertEquals(map.get("age"),"321");
		map.put("sex", "sex123");
		
		String result = p.getPartitionString(map);
		assertEquals("badqiu/321",result);
		
		map = p.parseRartition("badqiu");
		assertEquals(map.get("name"),"badqiu");
		assertEquals(map.get("age"),null);
	}
	
	public void testWithPrefix() {
		Partition p = new Partition("d:/temp/",new String[]{"name","age"});
		
		Map map = p.parseRartition("d:/temp/badqiu/321");
		assertEquals(map.get("name"),"badqiu");
		assertEquals(map.get("age"),"321");
		map.put("sex", "sex123");
		
		String result = p.getPartitionString(map);
		assertEquals("d:/temp/badqiu/321",result);
		
		map = p.parseRartition("d:/temp/badqiu");
		assertEquals(map.get("name"),"badqiu");
		assertEquals(map.get("age"),null);
	}
	
	Partition fp = new Partition("c:/temp",new String[]{"username","password","age"});
	public void testparseRartition() {
		Map map = fp.parseRartition("c:/temp/badqiu/123/21/system.txt");
		assertEquals(map.get("username"),"badqiu");
		assertEquals(map.get("password"),"123");
		assertEquals(map.get("age"),"21");
	}
	
	public void testgetPartitionString() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Map map = new HashMap();
		String str = fp.getPartitionString(map);
		assertEquals("c:/tempnull/null/null",str);
		
		map.put("username", "jane");
		map.put("password", "pwd");
		map.put("age", "38");
		str = fp.getPartitionString(map);
		assertEquals("c:/tempjane/pwd/38",str);
		
		assertEquals(BeanUtils.getProperty(map, "password"),"pwd");
	}
	
	public void testToString() {
		System.out.println(fp.toString());
	}
	
	public void testQuery() {
		Partition p = new Partition("c:/temp",new String[]{"username","password","age"});
		
		List list = p.query("password = '123' && username='badqiu' && age='12'", newPartitionModel());
		System.out.println("query: password = '123' && username='badqiu' && age='12':"+list);
		assertEquals(list.size(),49);
		
		list = p.query("password = '123'", newPartitionModel());
		System.out.println("query: password = '123':"+list);
		assertEquals(list.size(),49);
	}

	private PartitionModel newPartitionModel() {
		PartitionModel model = new PartitionModel() {
			int currentLine = 1;
			public String nextLine() {
				if(currentLine  % 2 == 1)
					return "c:/temp/badqiu/123/12";
				else 
					return "c:/temp/badqiu/"+RandomStringUtils.randomNumeric(2)+"/12";
			}
			public boolean hasNext() {
				return currentLine++ < 100;
			}
		};
		return model;
	}
}
