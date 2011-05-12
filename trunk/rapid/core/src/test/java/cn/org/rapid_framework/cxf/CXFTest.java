package cn.org.rapid_framework.cxf;

import org.apache.log4j.MDC;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import cn.org.rapid_framework.distributed.threadlocal.DistributedThreadLocal;
import cn.org.rapid_framework.hessian.HessianTest.Hello;
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "/cxf_service/applicationContext-ws-client.xml" })
public class CXFTest {
	@Autowired
	Hello hello;
	@Test
	public void test() throws Exception {
		CXFJettyServer.start();
		hello.say();
		CXFJettyServer.stop();
	}
	
	@Test
	public void test_remote() throws Exception {
		DistributedThreadLocal.put("username", "username");
		DistributedThreadLocal.put("password", "123");
		hello.say();
	}
}
