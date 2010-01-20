package cn.org.rapid_framework.web.session.store;

import java.util.HashMap;
import java.util.Map;

import cn.org.rapid_framework.web.mvc.Scope;
import cn.org.rapid_framework.web.session.SessionStore;

public class CookieSessionStore implements SessionStore{

	public void deleteSession(SessionContext context) {
		Scope.Session.save(context.response, new HashMap());
	}

	public Map getSession(SessionContext context) {
		return Scope.Session.restore(context.request);
	}

	public void saveSession(SessionContext context) {

	}

}
