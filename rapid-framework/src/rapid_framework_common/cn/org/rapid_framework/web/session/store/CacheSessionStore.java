package cn.org.rapid_framework.web.session.store;

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

	public void deleteSession(HttpServletResponse response,String sessionId) {
		cache.delete(sessionId);
	}

	public Map getSession(HttpServletRequest request, String sessionId,int timeoutSeconds) {
		String sessionData = (String)cache.get(sessionId);
		return SessionDataUtils.decode(sessionData);
	}

	public void saveSession(HttpServletResponse response, String sessionId,Map sessionData,int timeoutSeconds) {
		cache.replace(sessionId,SessionDataUtils.encode(sessionData),timeoutSeconds);
	}


}
