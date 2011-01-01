package cn.org.rapid_framework.spring.util;

import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;

/**
 * 用于设置系统参数进 System.properties
 * 
 * @author badqiu
 * 
 */
public class SystemPropertyInitializingBean implements InitializingBean {

	private Properties systemProperties;

	public void setSystemProperties(Properties systemProperties) {
		this.systemProperties = systemProperties;
	}

	public void afterPropertiesSet() throws Exception {
		if (systemProperties != null) {
			System.getProperties().putAll(systemProperties);
		}
		System.setProperty("hostname", getHostName());
	}

	public String getHostName() {
		try {
			java.net.InetAddress localMachine = java.net.InetAddress
					.getLocalHost();
			return localMachine.getHostName();
		} catch (java.net.UnknownHostException e) {
			return "unknown";
		}
	}
}
