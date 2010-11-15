package cn.org.rapid_framework.jmx;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.MethodInvoker;

/**
 * 用于jmx 断开连接后的自动重连的拦截器
 * @author badqiu
 *
 */
public class MBeanServerConnectionInterceptor implements MethodInterceptor,FactoryBean{
	private static Log log = LogFactory.getLog(MBeanServerConnectionInterceptor.class);
	
	private MBeanServerConnection connection;
	private String serviceUrl;
	
	public MBeanServerConnectionInterceptor(MBeanServerConnection connection,
			String serviceUrl) {
		super();
		this.connection = connection;
		this.serviceUrl = serviceUrl;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable   {
		try {
			return processed(invocation);
		}catch(IOException e) {
			return reconnectAndRetry(invocation);
		}catch(InvocationTargetException e) {
			Throwable target = e.getTargetException();
			if(target instanceof IOException) {
				return reconnectAndRetry(invocation);
			}
			throw e;
		}catch(Throwable e) {
			throw e;
		}
	}

	private Object reconnectAndRetry(MethodInvocation invocation)
			throws IOException, MalformedURLException, Throwable {
		if(log.isInfoEnabled()) {
			log.info("reconnectAndRetry:"+invocation.getMethod());
		}
		
		reconnect();
		return processed(invocation);
	}

	private void reconnect() throws IOException, MalformedURLException {
		JMXConnector connector = JMXConnectorFactory.connect(new JMXServiceURL(serviceUrl), null);
		this.connection = connector.getMBeanServerConnection();
	}

	private Object processed(MethodInvocation invocation)
			throws Throwable {
		MethodInvoker invoker = new MethodInvoker();
		invoker.setArguments(invocation.getArguments());
		invoker.setTargetObject(connection);
		invoker.setTargetClass(this.connection.getClass());
		invoker.setTargetMethod(invocation.getMethod().getName());
		invoker.prepare();
		return invoker.invoke();
	}

	public Object getObject() throws Exception {
		JMXConnector connector = JMXConnectorFactory.connect(new JMXServiceURL(serviceUrl), null);
		this.connection = connector.getMBeanServerConnection();
		
		ProxyFactory connectionProxy = new ProxyFactory(this.connection);
		connectionProxy.addAdvice(this);
		return connectionProxy.getProxy();
	}

	public Class getObjectType() {
		return MBeanServerConnection.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
