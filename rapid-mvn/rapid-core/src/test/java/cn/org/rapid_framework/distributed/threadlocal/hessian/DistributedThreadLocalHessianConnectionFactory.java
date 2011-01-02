package cn.org.rapid_framework.distributed.threadlocal.hessian;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.org.rapid_framework.distributed.threadlocal.DistributedThreadLocal;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianConnectionFactory;
import com.caucho.hessian.client.HessianProxyFactory;

public class DistributedThreadLocalHessianConnectionFactory implements
		HessianConnectionFactory {
	
	private static Log log = LogFactory.getLog(DistributedThreadLocalHessianConnectionFactory.class);
	
	private HessianConnectionFactory delegate;

	public DistributedThreadLocalHessianConnectionFactory(
			HessianConnectionFactory delegate) {
		this.delegate = delegate;
	}

	public HessianConnection open(URL url) throws IOException {
		DistributedThreadLocal.onBeforeRemoteCall();
		
		HessianConnection conn = delegate.open(url);
		Map<String, String> map = DistributedThreadLocal.getMap();
		Set<String> keySet = map.keySet();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			conn.addHeader(DistributedThreadLocal.DISTRIBUTED_THREAD_LOCAL_KEY_PREFIX+entry.getKey(),entry.getValue());
		}
		
		if(log.isDebugEnabled()) {
			log.debug("set hessian http headers for DistributedThreadLocal:"+map);
		}
		return conn;
	}

	public void setHessianProxyFactory(HessianProxyFactory factory) {
		delegate.setHessianProxyFactory(factory);
	}

}