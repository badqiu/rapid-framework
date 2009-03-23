package cn.org.rapid_framework.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHolder implements ApplicationContextAware,DisposableBean{
	
	private static Log log = LogFactory.getLog(ApplicationContextHolder.class);
	private static ApplicationContext applicationContext;
	private static boolean isDestroyed = false;
	
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		if(this.applicationContext != null) {
			throw new BeanCreationException("ApplicationContextHolder already holded 'applicationContext'.");
		}
		this.isDestroyed = false;
		this.applicationContext = context;
		log.info("holded applicationContext,displayName:"+applicationContext.getDisplayName());
	}
	
	public static ApplicationContext getApplicationContext() {
		if(isDestroyed)
			throw new IllegalStateException("'ApplicationContextHolder already destroyed.");
		if(applicationContext == null)
			throw new IllegalStateException("'applicationContext' property is null,ApplicationContextHolder not yet init.");
		return applicationContext;
	}
	
	public static Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}

	public void destroy() throws Exception {
		log.info("destroy() ,displayName:"+applicationContext.getDisplayName());
		this.isDestroyed = true;
		this.applicationContext = null;
	}
	
}
