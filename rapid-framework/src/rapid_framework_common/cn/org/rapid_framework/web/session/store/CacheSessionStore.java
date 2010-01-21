package cn.org.rapid_framework.web.session.store;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.org.rapid_framework.web.mvc.Scope;

public class CacheSessionStore extends SessionStore{
	public void deleteSession(HttpServletResponse response,String sessionId) {
		Cache.delete(sessionId);
	}

	public Map getSession(HttpServletRequest request, String sessionId,int timeoutMinute) {
		String sessionData = Cache.get(sessionId);
		return SessionDataUtils.decode(sessionData);
	}

	public void saveSession(HttpServletResponse response, String sessionId,Map sessionData,int timeoutMinute) {
		Cache.replace(sessionId,SessionDataUtils.encode(sessionData));
	}

	public static class Cache {
		static Map cache = new HashMap();
		public static void set(String key,String value,long expire_data) {
			cache.put(key, value);
		}

		public static String get(String key) {
			return (String)cache.get(key);
		}

		public static void replace(String key,String value) {
			if(cache.containsKey(key)) {
				cache.put(key, value);
			}
		}

		public static void delete(String key) {
			cache.remove(key);
		}
	}
}
