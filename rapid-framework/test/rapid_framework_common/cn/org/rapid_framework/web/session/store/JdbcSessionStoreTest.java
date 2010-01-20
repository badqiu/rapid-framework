package cn.org.rapid_framework.web.session.store;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import cn.org.rapid_framework.test.hsql.HSQLMemDataSourceUtils;


public class JdbcSessionStoreTest {
	JdbcSessionStore store = new JdbcSessionStore();
	MockHttpServletRequest request = new MockHttpServletRequest();
	MockHttpServletResponse response = new MockHttpServletResponse();
	@Test
	public void test() {
		Map sessionData = new HashMap();
		sessionData.put("empty", "");
		sessionData.put("blank", " ");
		sessionData.put("null", null);
		sessionData.put("string", "string");
		
		String sql = "	CREATE TABLE http_session (session_id char(40) PRIMARY KEY,session_data varchar(4000),expire_date timestamp ) ";
		store.setDataSource(HSQLMemDataSourceUtils.getDataSource(sql));

		Map map = store.getSession(request, "123",100);
		assertEquals(map.size(), 0);
		
		store.saveSession(response, "123", sessionData,100);
		
		
		map = store.getSession(request, "123",5);
		assertEquals(map.size(), 4);
		assertMapEquals(map);
		
		store.deleteSession(response, "123");
		
		map = store.getSession(request, "123",100);
		assertEquals(map.size(), 0);
		
	}
	private void assertMapEquals(Map map) {
		
	}
}
