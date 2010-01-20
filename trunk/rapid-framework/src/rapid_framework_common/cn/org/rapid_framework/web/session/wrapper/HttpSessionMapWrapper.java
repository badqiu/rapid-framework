package cn.org.rapid_framework.web.session.wrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class HttpSessionMapWrapper extends HttpSessionWrapper{
	Map map;

	public HttpSessionMapWrapper(HttpSession session,Map sessionData) {
		super(session);
		this.map = sessionData;
	}

	@Override
	public Object getAttribute(String key) {
		return this.map.get(key);
	}

	@Override
	public Enumeration getAttributeNames() {
		return Collections.enumeration(map.keySet());
	}

	@Override
	public void invalidate() {
		map.clear();
	}

	@Override
	public void removeAttribute(String key) {
		map.remove(key);
	}

	@Override
	public void setAttribute(String key, Object v) {
		map.put(key, v);
	}

}
