package cn.org.rapid_framework.web.session.store;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;


public class MemcachedSessionStoreTest {
	
	MemcachedSessionStore store = new MemcachedSessionStore();
	Map sessionData = new HashMap();
	Process memcachedProcess;
	List<Process> process = new ArrayList();
	@Before
	public void setUp() throws Exception {
		startMemcachedServer(11633);
		startMemcachedServer(11933);
		Thread.sleep(1000);
		System.out.println("memcached started");
		
		sessionData.put("empty", "");
		sessionData.put("blank", " ");
		sessionData.put("null", null);
		sessionData.put("string", "string");
		
		store.setHosts("localhost:11633 localhost:11933");
		store.afterPropertiesSet();
	}

	@After
	public void tearDown() throws Exception {
		for(Process p : process) {
			p.destroy();
			p.waitFor();
			System.out.println(" exit:"+p.exitValue());
		}
		Thread.sleep(1000);
	}
	
	private void startMemcachedServer(int port) {
		try {
			File file = ResourceUtils.getFile("classpath:fortest_memcached/memcached.exe");
			String cmd = file.getAbsolutePath()+" -p "+port;
			System.out.println("exec:"+cmd);
			process.add(Runtime.getRuntime().exec(cmd));
		}catch(Exception e) {
			throw new IllegalStateException("start memcached error",e);
		}
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
