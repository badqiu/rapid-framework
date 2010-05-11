package cn.org.rapid_framework.web.util;

import java.util.Map;

import javax.servlet.http.Cookie;

import org.junit.Assert;
import org.junit.Test;


public class CookieUtilsTest {

	@Test
	public void toMap() {
		Cookie[] cs = new Cookie[]{new Cookie("empty",""),new Cookie("blank"," ")};
		Map<String,Cookie> map = CookieUtils.toMap(cs);
		Assert.assertNotNull(map.get("empty"));
		Assert.assertNull(map.get("null"));
		Assert.assertEquals(map.size(),2);
	}
}
