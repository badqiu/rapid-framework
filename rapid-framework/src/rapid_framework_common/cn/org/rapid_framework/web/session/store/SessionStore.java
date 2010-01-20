package cn.org.rapid_framework.web.session.store;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public interface SessionStore {

	public void saveSession(HttpServletResponse response,String sessionId,Map sessionData,int timeMinute);

	public void deleteSession(HttpServletResponse response,String sessionId);

	public Map getSession(HttpServletRequest request,String sessionId,int timeoutMinute);

}
