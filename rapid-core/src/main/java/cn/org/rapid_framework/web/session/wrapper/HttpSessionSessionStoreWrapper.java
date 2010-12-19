package cn.org.rapid_framework.web.session.wrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpSession;

import cn.org.rapid_framework.web.session.store.SessionStore;
@SuppressWarnings("unchecked")
public class HttpSessionSessionStoreWrapper extends HttpSessionWrapper{
	String sessionId;
	Map sessionData;
	SessionStore store;

	public HttpSessionSessionStoreWrapper(HttpSession session,SessionStore store,String sessionId,Map sessionData) {
		super(session);
		this.store = store;
		this.sessionId = sessionId;
		this.sessionData = sessionData;
	}

	@Override
	public void invalidate() {
		sessionData.clear();
		store.deleteSession(getId());
	}

	@Override
	public String getId() {
		return sessionId;
	}

	@Override
	public Object getAttribute(String key) {
		return this.sessionData.get(key);
	}

	@Override
	public Enumeration getAttributeNames() {
		return Collections.enumeration(sessionData.keySet());
	}

	@Override
	public void removeAttribute(String key) {
		sessionData.remove(key);
		store.onRemoveAttribute(sessionId,key,sessionData,getMaxInactiveInterval());
	}

	@Override
	public void setAttribute(String key, Object value) {
		sessionData.put(key, value);
		store.onSetAttribute(sessionId,key,sessionData,getMaxInactiveInterval());
	}

}
