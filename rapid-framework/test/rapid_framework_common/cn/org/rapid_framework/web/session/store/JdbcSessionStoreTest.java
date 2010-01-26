package cn.org.rapid_framework.web.session.store;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import cn.org.rapid_framework.test.hsql.HSQLMemDataSourceUtils;


public class JdbcSessionStoreTest {
	JdbcSessionStore store = new JdbcSessionStore();
	MockHttpServletRequest request = new MockHttpServletRequest();
	MockHttpServletResponse response = new MockHttpServletResponse();
	Map sessionData = new HashMap();
	
	@Before
	public void setUp() {
		sessionData.put("empty", "");
		sessionData.put("blank", " ");
		sessionData.put("null", null);
		sessionData.put("string", "string");
		
		String sql = "	CREATE TABLE http_session_store (session_id char(40) PRIMARY KEY,session_data varchar(4000),expire_date bigint ) ";
		store.setDataSource(HSQLMemDataSourceUtils.getDataSource(sql));
	}
	
	@Test
	public void test_get_and_delete() {

		Map map = store.getSession(request, "123",100);
		assertEquals(map.size(), 0);
		
		store.saveSession(response, "123", sessionData,100);
		
		//test get
		map = store.getSession(request, "123",5);
		assertEquals(map.size(), 4);
		assertMapEquals(map);
		
		//test delete
		store.deleteSession(response, "123");
		map = store.getSession(request, "123",100);
		assertEquals(map.size(), 0);
		
	}
	
	@Test
	public void test_timeout() throws InterruptedException {
		
		store.saveSession(response, "123", sessionData,3);
		
		Thread.sleep(5 * 1000);
		//test get
		Map map = store.getSession(request, "123",0);
		assertEquals(0,map.size());
		
		
	}
	
	
	private void assertMapEquals(Map map) {
		
	}
}
