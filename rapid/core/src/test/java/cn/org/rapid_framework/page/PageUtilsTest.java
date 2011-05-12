package cn.org.rapid_framework.page;

import junit.framework.TestCase;

public class PageUtilsTest extends TestCase {

	public void testComputeLastPageNumber() {
		assertEquals(1,PageUtils.computeLastPageNumber(10, 10));
		assertEquals(2,PageUtils.computeLastPageNumber(11, 10));
		assertEquals(11,PageUtils.computeLastPageNumber(101, 10));
	}
}
