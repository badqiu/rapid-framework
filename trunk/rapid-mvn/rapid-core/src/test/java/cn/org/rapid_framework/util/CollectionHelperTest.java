package cn.org.rapid_framework.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class CollectionHelperTest extends TestCase {
	List values = new ArrayList();
	public void setUp() {
		for(int i = 0; i < 10; i++) {
			values.add(i);
		}
	}
	
//	public void testMin() {
//		assertEquals(new Integer(0),(Integer)CollectionUtils.min(values,"class"));
//		
//		assertEquals(null,CollectionUtils.min(null,null));
//	}
//	
//	public void testMax() {
//		assertEquals(new Integer(9),(Integer)CollectionUtils.max(values,"class"));
//		assertEquals(null,CollectionUtils.max(null,null));
//	}
	
	public void testSum() {
		assertEquals(45,(long)CollectionHelper.sum(values));
		assertEquals(0,(long)CollectionHelper.sum(new ArrayList()));
		
		assertEquals(0,(long)CollectionHelper.sum(null));

	}
	
	public void testAvg() {
		assertEquals(4.5,(double)CollectionHelper.avg(values));
		assertEquals(0,(long)CollectionHelper.avg(new ArrayList()));
		
		assertEquals(0,(long)CollectionHelper.avg(null));
	}
}
