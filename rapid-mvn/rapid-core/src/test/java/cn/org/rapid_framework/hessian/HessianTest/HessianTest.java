package cn.org.rapid_framework.hessian.HessianTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import cn.org.rapid_framework.distributed.threadlocal.DistributedThreadLocal;
import cn.org.rapid_framework.distributed.threadlocal.hessian.DistributedThreadLocalHessianConnectionFactory;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianProxyFactory;

public class HessianTest {

	@Test
	public void test() throws Exception {
//		JettyServer.start();
		DistributedThreadLocal.put("now", "111"+System.currentTimeMillis());
		DistributedThreadLocal.put("random", RandomStringUtils.randomNumeric(1024));
		DistributedThreadLocal.put("random2", RandomStringUtils.randomNumeric(1024));
		String url = "http://127.0.0.1:8080/hessian";
		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setConnectionFactory(new DistributedThreadLocalHessianConnectionFactory(
						factory.getConnectionFactory()));

		Hello hello = (Hello) factory.create(Hello.class, url);
		hello.say();
//		hello.exception();
//		JettyServer.stop();
	}

}
