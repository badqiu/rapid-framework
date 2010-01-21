package cn.org.rapid_framework.web.session.store;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.org.rapid_framework.web.mvc.Scope;

/**
 * 使用cookie存储session
 *
 * 存储session时会计算sign(即摘要信息)防止cookie值被修改,所以需要为CookieSessionStore.secretKey指定一个私匙（即密码）
 * @author badqiu
 *
 */
public class CookieSessionStore extends SessionStore{
	private String secretKey = "application.secret";

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void deleteSession(HttpServletResponse response,String sessionId) {
		Scope.Session.save(response, new HashMap(0),getSecretKey(),null);
	}

	public Map getSession(HttpServletRequest request, String sessionId,int timeoutMinute) {
		return Scope.Session.restore(request,getSecretKey());
	}

	public void saveSession(HttpServletResponse response, String sessionId,Map sessionData,int timeoutMinute) {
		Scope.Session.save(response,sessionData,getSecretKey(),timeoutMinute * 60);
	}

}
