package cn.org.rapid_framework.page;

import junit.framework.TestCase;

public class PageRequestTest extends TestCase {

	public void testSqlInjectionSuccess() {
		new PageRequest().setSortColumns("MAL.USERNAME DESC");
		new PageRequest().setSortColumns(null);
	}

	public void testSqlInjectionFail() {
		try {
			new PageRequest().setSortColumns("MAL.USER'NAME DESC");
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try {
			new PageRequest().setSortColumns("MAL.USER\\NAME DESC");
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		assertEquals(new PageRequest().getPageSize(),10);
		assertEquals(new PageRequest("").getPageSize(),10);
	}
}
