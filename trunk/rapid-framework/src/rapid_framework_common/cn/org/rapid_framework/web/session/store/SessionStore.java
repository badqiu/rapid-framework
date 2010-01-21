package cn.org.rapid_framework.web.session.store;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public abstract class SessionStore {
	private boolean isSaveSessionDataOnAttributeChange = false;

	public boolean isSaveSessionDataOnAttributeChange() {
		return isSaveSessionDataOnAttributeChange;
	}
	/**
	 * @param isSaveSessionDataOnAttributeChange default is false
	 */
	public void setSaveSessionDataOnAttributeChange(boolean isSaveSessionDataOnAttributeChange) {
		this.isSaveSessionDataOnAttributeChange = isSaveSessionDataOnAttributeChange;
	}

	public abstract void saveSession(HttpServletResponse response,String sessionId,Map sessionData,int timeoutSeconds);

	public abstract void deleteSession(HttpServletResponse response,String sessionId);

	public abstract Map getSession(HttpServletRequest request,String sessionId,int timeoutMinute);

	public void onSetAttribute(HttpServletResponse response,String sessionId,String key,Map sessionData,int timeoutSeconds) {
		if(isSaveSessionDataOnAttributeChange) {
			saveSession(response, sessionId, sessionData, timeoutSeconds);
		}
	}

	public void onRemoveAttribute(HttpServletResponse response,String sessionId,String key,Map sessionData,int timeoutSeconds) {
		if(isSaveSessionDataOnAttributeChange) {
			saveSession(response, sessionId, sessionData, timeoutSeconds);
		}
	}

}
