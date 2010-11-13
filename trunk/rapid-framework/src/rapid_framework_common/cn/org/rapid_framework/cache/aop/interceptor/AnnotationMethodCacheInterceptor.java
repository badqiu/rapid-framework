package cn.org.rapid_framework.cache.aop.interceptor;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import cn.org.rapid_framework.cache.Cache;
import cn.org.rapid_framework.cache.aop.annotation.MethodCache;


public class AnnotationMethodCacheInterceptor implements MethodInterceptor,BeanFactoryAware,InitializingBean {  
  
    private Cache methodCache;  
    private BeanFactory beanFactory;
    private String methodCacheBeanName = null;
  
    public void setMethodCache(Cache methodCache) {  
        this.methodCache = methodCache;  
    }  
  
    public Object invoke(MethodInvocation invocation) throws Throwable {  
        if (invocation.getMethod().isAnnotationPresent(MethodCache.class)) {  
        	String targetName = invocation.getThis().getClass().getName();
            String methodName = invocation.getMethod().getName();  
            Object[] arguments = invocation.getArguments();  
            MethodCache annotation = invocation.getMethod().getAnnotation(MethodCache.class);
            String cacheKey = getCackeKey(targetName, methodName, arguments,annotation);
            int expireSeconds = (int)annotation.timeUnit().toSeconds(annotation.expireTime());
            return getResult(cacheKey, invocation,expireSeconds); 
        } else {  
            return invocation.proceed();  
        }  
    }

	private Object getResult(String cacheKey,MethodInvocation invocation, int expireSeconds) throws Throwable {     
        Object result = methodCache.get(cacheKey); 
        if(result == null) {
        	result = invocation.proceed();
        	methodCache.set(cacheKey, result, expireSeconds);
        }
        return result;
    }   
	
	protected String getCackeKey(String className, String methodName,
			Object[] arguments, MethodCache annotation) {
		String cacheKey = null;
		if(StringUtils.isEmpty(annotation.cacheKey())) {
			cacheKey = getCacheKey(className, methodName, arguments);     
		}else {
			cacheKey = getCacheKeyWithArguments(annotation.cacheKey(),arguments);
		}
		return cacheKey;
	}  
  
    protected String getCacheKeyWithArguments(String cacheKey, Object[] args) {
    	String result = StringUtils.replace(cacheKey, "{args}", ""+StringUtils.join(args,','));
    	return String.format(result, args);
	}
    
	protected String getCacheKey(String className, String methodName,Object[] arguments) {   
        StringBuilder datakey = new StringBuilder();   
        datakey.append(className).append(".").append(methodName).append("(");
        datakey.append(StringUtils.join(arguments,','));
        datakey.append(")");
        return datakey.toString();
    }

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setMethodCacheBeanName(String methodCacheBeanName) {
		this.methodCacheBeanName = methodCacheBeanName;
	}

	public void afterPropertiesSet() throws Exception {
		if(methodCache == null) {
			methodCache = (Cache)beanFactory.getBean(methodCacheBeanName);
		}
		Assert.notNull(methodCache,"'methodCache' must be not null");
	}

}  