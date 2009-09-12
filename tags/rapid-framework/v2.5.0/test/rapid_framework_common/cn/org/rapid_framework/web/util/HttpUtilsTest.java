package cn.org.rapid_framework.web.util;

import java.util.Map;

import junit.framework.TestCase;
/**
 * @author badqiu
 */
public class HttpUtilsTest extends TestCase {

	public void testParseQueryString() {
		Map m = HttpUtils.parseQueryString("id=06e2e79d1cdc06bc011cdc0d171c000c& ");
		
		m = HttpUtils.parseQueryString("id=06e2e79d1cdc06bc011cdc0d171c000c&");
		assertEquals(m.get("id"),"06e2e79d1cdc06bc011cdc0d171c000c");
		
		m = HttpUtils.parseQueryString("id=1&username=badqiu");
		assertEquals(m.get("id"),"1");
		assertEquals(m.get("username"),"badqiu");
		
		m = HttpUtils.parseQueryString("id=1&id=2&id=3");
		String[] ids = (String[])m.get("id");
		assertEquals("1",ids[0]);
		assertEquals("2",ids[1]);
		assertEquals("3",ids[2]);
	}
	
}
