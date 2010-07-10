package cn.org.rapid_framework.web.session.wrapper;

import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("all")
public class HttpSessionWrapper implements HttpSession{
	HttpSession delegate;

	public HttpSessionWrapper(HttpSession session) {
		super();
		this.delegate = session;
	}

	public Object getAttribute(String key) {
		return delegate.getAttribute(key);
	}

	public Enumeration getAttributeNames() {
		return delegate.getAttributeNames();
	}

	public long getCreationTime() {
		return delegate.getCreationTime();
	}

	public String getId() {
		return delegate.getId();
	}

	public long getLastAccessedTime() {
		return delegate.getLastAccessedTime();
	}

	public int getMaxInactiveInterval() {
		return delegate.getMaxInactiveInterval();
	}

	public ServletContext getServletContext() {
		return delegate.getServletContext();
	}

	public HttpSessionContext getSessionContext() {
		return delegate.getSessionContext();
	}

	public Object getValue(String key) {
		return getAttribute(key);
	}

	public String[] getValueNames() {
		return (String[])Collections.list(getAttributeNames()).toArray(new String[]{});
	}

	public void invalidate() {
		delegate.invalidate();
	}

	public boolean isNew() {
		return delegate.isNew();
	}

	public void putValue(String key, Object value) {
		setAttribute(key, value);
	}

	public void removeAttribute(String key) {
		delegate.removeAttribute(key);
	}

	public void removeValue(String key) {
		removeAttribute(key);
	}

	public void setAttribute(String key, Object v) {
		delegate.setAttribute(key, v);
	}

	public void setMaxInactiveInterval(int v) {
		delegate.setMaxInactiveInterval(v);
	}

}
