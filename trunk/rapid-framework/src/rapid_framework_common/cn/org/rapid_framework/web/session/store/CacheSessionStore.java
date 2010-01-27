package cn.org.rapid_framework.web.session.store;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.org.rapid_framework.cache.ICache;

public class CacheSessionStore extends SessionStore{
	private ICache cache;
	
	public ICache getCache() {
		return cache;
	}

	public void setCache(ICache cache) {
		this.cache = cache;
	}

	public void deleteSession(String sessionId) {
		cache.delete(sessionId);
	}

	public Map getSession(String sessionId,int timeoutSeconds) {
		Map result =  (Map)cache.get(sessionId);
		if(result == null) {
			return new HashMap();
		}
		return result;
	}

	public void saveSession(String sessionId,Map sessionData,int timeoutSeconds) {
		cache.set(sessionId,sessionData,timeoutSeconds);
	}


}
