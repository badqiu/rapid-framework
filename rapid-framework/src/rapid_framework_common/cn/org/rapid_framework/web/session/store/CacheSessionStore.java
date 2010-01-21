package cn.org.rapid_framework.web.session.store;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.org.rapid_framework.cache.Cache;

public class CacheSessionStore extends SessionStore{
	public void deleteSession(HttpServletResponse response,String sessionId) {
		Cache.delete(sessionId);
	}

	public Map getSession(HttpServletRequest request, String sessionId,int timeoutMinute) {
		String sessionData = (String)Cache.get(sessionId);
		return SessionDataUtils.decode(sessionData);
	}

	public void saveSession(HttpServletResponse response, String sessionId,Map sessionData,int timeoutMinute) {
		Cache.replace(sessionId,SessionDataUtils.encode(sessionData));
	}


}
