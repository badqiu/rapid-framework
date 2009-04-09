package cn.org.rapid_framework.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class CollectionUtilsTest extends TestCase {
	List values = new ArrayList();
	public void setUp() {
		for(int i = 0; i < 10; i++) {
			values.add(i);
		}
	}
	
	public void testMin() {
		assertEquals(new Integer(0),(Integer)CollectionUtils.min(values));
		assertEquals(null,CollectionUtils.min(null));
	}
	
	public void testMax() {
		assertEquals(new Integer(9),(Integer)CollectionUtils.max(values));
		assertEquals(null,CollectionUtils.max(null));
	}
	
	public void testSum() {
		assertEquals(45,(long)CollectionUtils.sum(values));
		assertEquals(0,(long)CollectionUtils.sum(new ArrayList()));
		
		assertEquals(0,(long)CollectionUtils.sum(null));

	}
	
	public void testAvg() {
		assertEquals(4.5,(double)CollectionUtils.avg(values));
		assertEquals(0,(long)CollectionUtils.avg(new ArrayList()));
		
		assertEquals(0,(long)CollectionUtils.avg(null));
	}
}
