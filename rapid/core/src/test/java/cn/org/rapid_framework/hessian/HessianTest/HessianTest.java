package cn.org.rapid_framework.hessian.HessianTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import cn.org.rapid_framework.distributed.threadlocal.DistributedThreadLocal;
import cn.org.rapid_framework.distributed.threadlocal.hessian.DistributedThreadLocalHessianConnectionFactory;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

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
		factory.setSerializerFactory(new SerializerFactory(){
		});
		Hello hello = (Hello) factory.create(Hello.class, url);
		hello.say();
		try {
		hello.exception();
		}catch(UnsupportedOperationException e) {
			System.out.println("error:"+e);
			e.printStackTrace();
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Hessian2Output out = new Hessian2Output(bos);
			out.startMessage();
			out.writeObject(e);
			out.completeMessage();
			out.close();
			
			Hessian2Input in = new Hessian2Input(new ByteArrayInputStream(bos.toByteArray()));
			in.startMessage();
			Exception fromInput = (Exception)in.readObject();
			in.completeMessage();
			in.close();
			
			fromInput.printStackTrace();
		}
//		JettyServer.stop();
	}

}
