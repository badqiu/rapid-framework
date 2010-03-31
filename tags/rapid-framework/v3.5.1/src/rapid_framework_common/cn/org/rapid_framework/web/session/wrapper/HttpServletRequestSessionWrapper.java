package cn.org.rapid_framework.web.session.wrapper;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpServletRequestSessionWrapper extends javax.servlet.http.HttpServletRequestWrapper {
	HttpSession session;

	public HttpServletRequestSessionWrapper(HttpServletRequest request,HttpSession session) {
		super(request);
		this.session = session;
	}

	public HttpSession getSession(boolean create) {
		return session;
	}

	public HttpSession getSession() {
		return session;
	}

}