package cn.org.rapid_framework.cache;

import java.util.HashMap;
import java.util.Map;

public class MapBackendCache implements Cache {
	private Map map = new HashMap();
	public class Value {
		long gmtCreate = System.currentTimeMillis();
		int expiration;
		Object value;
		public Value(Object value, int expiration) {
			this.value = value;
			this.expiration = expiration;
		}
	}
	public void add(String key, Object value, int expiration) {
		map.put(key, new Value(value,expiration));
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
		Value value = (Value)map.get(key);
		if(value == null) return null;
		boolean isTimeout = (value.gmtCreate + value.expiration) > System.currentTimeMillis();
		if(isTimeout) {
			delete(key);
			return null;
		}else {
			return value.value;
		}
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
		map.put(key, new Value(value,expiration));
	}

	public boolean safeAdd(String key, Object value, int expiration) {
		map.put(key, new Value(value,expiration));
		return true;
	}

	public boolean safeDelete(String key) {
		return map.remove(key) != null;
	}

	public boolean safeReplace(String key, Object value, int expiration) {
		map.put(key, new Value(value,expiration));
		return true;
	}

	public boolean safeSet(String key, Object value, int expiration) {
		map.put(key, new Value(value,expiration));
		return true;
	}

	public void set(String key, Object value, int expiration) {
		map.put(key, new Value(value,expiration));
	}

	public void stop() {
		map.clear();
	}

}
