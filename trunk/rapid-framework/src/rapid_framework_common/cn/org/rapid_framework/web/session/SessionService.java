package cn.org.rapid_framework.web.session;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public interface SessionService {

	public Object getAttribute(String sessionId,String key);

	public void removeAttribute(String sessionId,String key);

	public void saveSession(String sessionId,Map session);

	public void invalidate(String sessionId);

	public void getAttributeNames(String sessionId);

}
