package cn.org.rapid_framework.web.session;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public interface SessionStore {

	public void saveSession(HttpServletResponse response,String sessionId,Map sessionData);

	public void deleteSession(HttpServletResponse response,String sessionId);

	public Map getSession(HttpServletRequest request,String sessionId);

	public static class SessionContext {
		public HttpSession session;
		public Map sessionData;
		public String sessionId;
		public long sessionTimeoutMinutes;
		public HttpServletRequest request;
		public HttpServletResponse response;
	}

}
