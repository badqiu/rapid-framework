package cn.org.rapid_framework.web.session.wrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.jms.core.SessionCallback;

import cn.org.rapid_framework.web.session.store.SessionStore;

public class HttpSessionSessionStoreWrapper extends HttpSessionWrapper{
	HttpServletRequest request;
	HttpServletResponse response;
	String sessionId;
	Map sessionData;
	SessionStore store;

	public HttpSessionSessionStoreWrapper(HttpSession session,HttpServletRequest request,HttpServletResponse response,SessionStore store,String sessionId,Map sessionData) {
		super(session);
		this.store = store;
		this.request = request;
		this.response = response;
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
