package cn.org.rapid_framework.cache;

import java.util.HashMap;
import java.util.Map;

public class MapBackendCache implements Cache {
	private Map map = new HashMap();
	public void add(String key, Object value, int expiration) {
		map.put(key, value);
	}

	public void clear() {
		map.clear();
	}

	public long decr(String key, int by) {
		Long v = (Long)map.get(key);
		if(v == null) {
			v = -(long)by;
		}else {
			v = v - by;
		}
		map.put(key, v);
		return v;
	}

	public void delete(String key) {
		map.remove(key);
	}

	public Object get(String key) {
		return map.get(key);
	}

	public Map<String, Object> get(String[] keys) {
		Map<String,Object> result = new HashMap();
		for(String key : keys) {
			Object value = get(key);
			result.put(key, value);
		}
		return result;
	}

	public long incr(String key, int by) {
		Long v = (Long)map.get(key);
		if(v == null) {
			v = (long)by;
		}else {
			v = v + by;
		}
		map.put(key, v);
		return v;
	}

	public void replace(String key, Object value, int expiration) {
		map.put(key, value);
	}

	public boolean safeAdd(String key, Object value, int expiration) {
		map.put(key, value);
		return true;
	}

	public boolean safeDelete(String key) {
		map.remove(key);
		return true;
	}

	public boolean safeReplace(String key, Object value, int expiration) {
		map.put(key, value);
		return true;
	}

	public boolean safeSet(String key, Object value, int expiration) {
		map.put(key, value);
		return false;
	}

	public void set(String key, Object value, int expiration) {
		map.put(key, value);
	}

	public void stop() {
		map.clear();
	}

}
