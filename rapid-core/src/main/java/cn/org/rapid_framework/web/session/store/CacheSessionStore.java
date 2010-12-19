package cn.org.rapid_framework.web.session.store;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import cn.org.rapid_framework.cache.Cache;
/**
 * Cache sessionStore
 * @author badqiu
 *
 */
public class CacheSessionStore extends SessionStore implements InitializingBean{
	private Cache cache;
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull("cache must be not null");
	}
	
	public void deleteSession(String sessionId) {
		cache.delete(sessionId);
	}

	public Map getSession(String sessionId,int timeoutSeconds) {
		Map result = (Map)get(sessionId);
		if(result == null){
			result = new HashMap();
		}
		return result;
	}

	private Object get(String sessionId) {
		return cache.get(sessionId);
	}

	public void saveSession(String sessionId,Map sessionData,int timeoutSeconds) {
		cache.set(sessionId, sessionData, timeoutSeconds);
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

}
