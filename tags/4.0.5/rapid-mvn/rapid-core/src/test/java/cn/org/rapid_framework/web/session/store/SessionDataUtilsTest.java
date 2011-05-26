package cn.org.rapid_framework.web.session.store;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class SessionDataUtilsTest {
	@Test
	public void encode() throws UnsupportedEncodingException {
		Map map = new HashMap();
		map.put("empty", "");
		map.put("blank", "  ");
		map.put("null", null);
		map.put("abc", "abc");
		String data = JdbcSessionStore.encode(map);
		System.out.println(data);
		
		Map decodeMap = JdbcSessionStore.decode(data);
		System.out.println(decodeMap.get("null"));
		
		assertEquals(decodeMap.get("empty"),"");
		assertEquals(decodeMap.get("blank"),"  ");
		assertEquals(decodeMap.get("abc"),"abc");
		assertEquals(null,decodeMap.get("null"));
//		assertTrue(decodeMap.get("null") == null);
	}
}
