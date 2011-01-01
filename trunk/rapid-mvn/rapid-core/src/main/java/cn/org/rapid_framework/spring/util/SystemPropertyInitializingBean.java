package cn.org.rapid_framework.spring.util;

import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * <p>
 * 用于设置系统参数进 System.properties <br/>
 * 
 * 另外还会将hostname=java.net.InetAddress.getLocalHost().getHostName()放进系统参数
 * </p>
 * 
 * @author badqiu
 * 
 */
public class SystemPropertyInitializingBean implements InitializingBean,PriorityOrdered {

	private Properties systemProperties;
	
	private int order = Ordered.HIGHEST_PRECEDENCE;  

	public void setOrder(int order) {
	  this.order = order;
	}

	public int getOrder() {
	  return this.order;
	}
	
	public void setSystemProperties(Properties systemProperties) {
		this.systemProperties = systemProperties;
	}

	public void afterPropertiesSet() throws Exception {
		System.setProperty("hostname", getHostName());
		if (systemProperties != null) {
			System.getProperties().putAll(systemProperties);
		}
	}

	public String getHostName() {
		try {
			java.net.InetAddress localMachine = java.net.InetAddress
					.getLocalHost();
			return localMachine.getHostName();
		} catch (java.net.UnknownHostException e) {
			return "unknown_hostname";
		}
	}
	
}
