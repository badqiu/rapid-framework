package cn.org.rapid_framework.web.session.store;

import java.util.Map;

@SuppressWarnings("unchecked")
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

	public abstract void saveSession(String sessionId,Map sessionData,int timeoutSeconds);

	public abstract void deleteSession(String sessionId);

	public abstract Map getSession(String sessionId,int timeoutSeconds);

	public void onSetAttribute(String sessionId,String key,Map sessionData,int timeoutSeconds) {
		if(isSaveSessionDataOnAttributeChange) {
			saveSession(sessionId, sessionData, timeoutSeconds);
		}
	}

	public void onRemoveAttribute(String sessionId,String key,Map sessionData,int timeoutSeconds) {
		if(isSaveSessionDataOnAttributeChange) {
			saveSession(sessionId, sessionData, timeoutSeconds);
		}
	}

}
