package cn.org.rapid_framework.hessian.HessianTest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.org.rapid_framework.distributed.threadlocal.DistributedThreadLocal;

public class HelloImpl implements Hello {
	private Log log = LogFactory.getLog(Hello.class);
	public void say() {
		log.info("say(): "+DistributedThreadLocal.getMap());
		System.out.println("say() threadlocal:"+DistributedThreadLocal.getMap());
	}
	
	public void exception(){
		throw new RuntimeException("server error");
	}
}