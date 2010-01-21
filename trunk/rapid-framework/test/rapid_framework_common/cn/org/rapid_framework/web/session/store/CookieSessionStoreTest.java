package cn.org.rapid_framework.web.session.store;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;





public class CookieSessionStoreTest {
	List<Cookie> cookies = new ArrayList();
	JdbcSessionStore store = new JdbcSessionStore();
	MockHttpServletRequest request = new MockHttpServletRequest(){
		@Override
		public Cookie[] getCookies() {
			return cookies.toArray(new Cookie[]{});
		}
	};
	MockHttpServletResponse response = new MockHttpServletResponse() {
		public void addCookie(Cookie cookie) {
			cookies.add(cookie);
		};
	};
	@Test
	public void testSave() {
		Map sessionData = SessionDataUtilsTest.createForTestMap();
		SessionStore store = new CookieSessionStore();
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
		SessionDataUtilsTest.assertForTestMap(map);
	}
}
