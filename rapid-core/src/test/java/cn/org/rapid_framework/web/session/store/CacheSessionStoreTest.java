package cn.org.rapid_framework.web.session.store;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.org.rapid_framework.cache.MapBackendCache;

import com.thimbleware.jmemcached.MemCacheDaemon;


public class CacheSessionStoreTest {
	
	CacheSessionStore store = new CacheSessionStore();
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
		
		store.setCache(new MapBackendCache());
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
		for(MemCacheDaemon d : daemons) {
		    d.stop();
		}
	}


	List<MemCacheDaemon> daemons = new ArrayList();
	private void startMemcachedServer(int port) throws IOException {
		try {
//	        LRUCacheStorageDelegate cacheStorage = new LRUCacheStorageDelegate(Integer.MAX_VALUE, Integer.MAX_VALUE, 1024000);
//	        MemCacheDaemon daemon = new MemCacheDaemon();
//	        daemon.setCache(new Cache(cacheStorage));
//	        daemon.setAddr(new InetSocketAddress(port));
//	        daemon.setIdleTime(1000 * 600);
//	        daemon.setVerbose(true);
//	        daemon.start();
//	        daemons.add(daemon);
//			File file = ResourceUtils.getFile("classpath:fortest_memcached/memcached.exe");
//			String cmd = file.getAbsolutePath()+" -p "+port;
//			System.out.println("exec:"+cmd);
//			process.add(Runtime.getRuntime().exec(cmd));
		}catch(Error e) {
			throw new IllegalStateException("start memcached error",e);
		}
	}
	
	@Test
	public void test_get_and_delete() {

		Map map = store.getSession("123",100);
		assertEquals(map.size(), 0);
		
		store.saveSession("123", sessionData,1000);
		
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
