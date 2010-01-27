package cn.org.rapid_framework.web.session.store;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import cn.org.rapid_framework.test.hsql.HSQLMemDataSourceUtils;


public class MemcachedSessionStoreTest {
	
	MemcachedSessionStore store = new MemcachedSessionStore();
	Map sessionData = new HashMap();
	Process memcachedProcess;
	@Before
	public void setUp() throws Exception {
		startMemcachedServer(11633);
		sessionData.put("empty", "");
		sessionData.put("blank", " ");
		sessionData.put("null", null);
		sessionData.put("string", "string");
		
		store.setHosts("localhost:11633");
		store.afterPropertiesSet();
	}

	@After
	public void tearDown() {
		if(memcachedProcess != null) memcachedProcess.destroy();
	}
	
	private void startMemcachedServer(int port) throws FileNotFoundException,
			IOException {
		File file = ResourceUtils.getFile("classpath:fortest_memcached/memcached.exe");
		String cmd = "cmd.exe /c "+file.getAbsolutePath()+" -p "+port;
		System.out.println("exec:"+cmd);
		memcachedProcess = Runtime.getRuntime().exec(cmd);
	}
	
	@Test
	public void test_get_and_delete() {

		Map map = store.getSession("123",100);
		assertEquals(map.size(), 0);
		
		store.saveSession("123", sessionData,100);
		
		//test get
		map = store.getSession("123",5);
		assertEquals(map.size(), 4);
		
		//test delete
		store.deleteSession("123");
		map = store.getSession("123",100);
		assertEquals(map.size(), 0);
		
	}
	
	@Test
	public void test_timeout() throws InterruptedException {
		
		store.saveSession("for_test_timeout_1", sessionData,3);
		
		Thread.sleep(5 * 1000);
		//test get
		Map map = store.getSession("for_test_timeout_1",0);
		assertEquals(0,map.size());
		
	}
}
