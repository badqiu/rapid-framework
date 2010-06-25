package cn.org.rapid_framework.web.session.store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.SerializingTranscoder;

import org.springframework.beans.factory.InitializingBean;
/**
 * 用于将session存储在memcached中
 * @author badqiu
 *
 */
@SuppressWarnings("all")
public class MemcachedSessionStore extends SessionStore implements InitializingBean{
	private MemcachedClient client;
	private SerializingTranscoder serializingTranscoder = new SerializingTranscoder();
	private String hosts = null;
	
	public void afterPropertiesSet() throws Exception {
		System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.Log4JLogger");
        if(client == null)
        	client = new MemcachedClient(AddrUtil.getAddresses(hosts));
	}
	
	public void setSerializingTranscoder(SerializingTranscoder serializingTranscoder) {
		this.serializingTranscoder = serializingTranscoder;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public void setClient(MemcachedClient memcachedClient) {
		this.client = memcachedClient;
	}

	public void deleteSession(String sessionId) {
		Future<Boolean> future = client.delete(sessionId);
	}

	public Map getSession(String sessionId,int timeoutSeconds) {
		Map result = (Map)get(sessionId);
		if(result == null){
			result = new HashMap();
		}
		return result;
	}

	private Object get(String sessionId) {
		Future f = client.asyncGet(sessionId, serializingTranscoder);
		try {
            return f.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            f.cancel(false);
        }
		return null;
	}

	public void saveSession(String sessionId,Map sessionData,int timeoutSeconds) {
		Future<Boolean> future = client.set(sessionId, timeoutSeconds, sessionData,serializingTranscoder);
	}


}
