package cn.org.rapid_framework.distributed.threadlocal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/**
 * 分布式 ThreadLocal, 存放在ThreadLocal中的数据可以传输至另外一台机器上
 * 
 * @author badqiu
 *
 */
public class DistributedThreadLocal {
    /**
     * 分布式ThreadLocal的ID 
     **/
    public static String DISTRIBUTED_THREAD_LOCAL_ID = "DISTRIBUTED_THREAD_LOCAL_ID";
    
    
	public static String DISTRIBUTED_THREAD_LOCAL_KEY_PREFIX = "tl_";
	
	public static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<Map<String, String>>();

	public static void putAll(Map<String, String> map) {
	    getMap().putAll(map);
	}
	
	public static void put(String key, String value) {
	    getMap().put(key, value);
	}

	public static String get(String key) {
		return (String) getMap().get(key);
	}

	public static Map<String, String> getMap() {
		Map<String, String> map = threadLocal.get();
		if (map == null) {
			map = new HashMap<String, String>();
			map.put(DISTRIBUTED_THREAD_LOCAL_ID, UUID.randomUUID().toString().replace("-", ""));
			
			threadLocal.set(map);
		}
		return map;
	}
	
	public static void clear() {
		threadLocal.set(null);
	}
	
	public static void onBeforeRemoteCall() {
	}
	
	public static void onReceivedDistributedThreadLocal(){
	}


}