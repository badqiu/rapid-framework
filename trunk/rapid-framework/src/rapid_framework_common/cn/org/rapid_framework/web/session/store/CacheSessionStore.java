package cn.org.rapid_framework.web.session.store;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.org.rapid_framework.web.mvc.Scope;
import cn.org.rapid_framework.web.session.SessionStore;
import cn.org.rapid_framework.web.session.SessionStore.SessionContext;

public class CacheSessionStore implements SessionStore{

	public void deleteSession(HttpServletResponse response,String sessionId) {
		Scope.Session.save(response, new HashMap());
	}

	public Map getSession(HttpServletRequest request, String sessionId) {
		return Scope.Session.restore(request);
	}

	public void saveSession(HttpServletResponse response, String sessionId,Map sessionData) {
		Scope.Session.save(response,sessionData);
	}

}
