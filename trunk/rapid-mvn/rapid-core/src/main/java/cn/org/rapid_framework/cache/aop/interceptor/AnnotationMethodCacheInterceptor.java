package cn.org.rapid_framework.cache.aop.interceptor;


import java.util.Iterator;

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
import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 方法缓存的拦截器,拦截有MethodCache标注的方法,并缓存结果.
 * 
 * @author badqiu
 *
 */
public class AnnotationMethodCacheInterceptor implements MethodInterceptor,BeanFactoryAware,InitializingBean {  
  
    private Cache methodCache;  
    private BeanFactory beanFactory;
    private String methodCacheBeanName = null;
    private String cacheKeyPrefix = "";
  
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
	
	protected String getCackeKey(String className, String methodName,Object[] arguments, MethodCache annotation) {
		String cacheKey = null;
		if(StringUtils.isEmpty(annotation.cacheKey())) {
			cacheKey = createDefaultCacheKey(className, methodName, arguments);     
		}else {
			cacheKey = createCacheKey(annotation.cacheKey(),arguments);
		}
		return cacheKeyPrefix + cacheKey;
	}  
  
    protected String createCacheKey(String cacheKey, Object[] args) {
    	String result = cacheKey;
    	if(cacheKey.indexOf("{args}") >= 0) {
    		result = StringUtils.replace(result, "{args}", ""+StringUtils.join(args,','));
    	}
    	return String.format(result, args);
	}
    
	protected String createDefaultCacheKey(String className, String methodName,Object[] arguments) {   
        StringBuilder datakey = new StringBuilder();   
        datakey.append(className).append(".").append(methodName).append("(");
        
        for(int i = 0; i < arguments.length; i++) {
        	Object arg = arguments[i];
        	datakey.append(objectToString(arg));
        	if(i != arguments.length - 1) {
        		datakey.append(",");
        	}
        }
        
        datakey.append(")");
        return datakey.toString();
    }

	private String objectToString(Object arg) {
		String argString = null;
		if(arg == null) {
			argString = "null";
		}else if(arg.getClass().isArray()) {
			argString = "["+StringUtils.join(((Object[])arg), ',')+"]";
		}else if(arg instanceof Iterable) {
			argString = "["+StringUtils.join(((Iterable)arg).iterator(), ',')+"]";
		}else {
			argString = arg.toString();
		}
		return argString;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setMethodCacheBeanName(String methodCacheBeanName) {
		this.methodCacheBeanName = methodCacheBeanName;
	}
	
	public String getCacheKeyPrefix() {
		return cacheKeyPrefix;
	}
	
	/**
	 * cacheKey的前缀,增加在所有的cacheKey中,主要用于不同系统间的cache划用,
	 * @param cacheKeyPrefix
	 */
	public void setCacheKeyPrefix(String cacheKeyPrefix) {
		this.cacheKeyPrefix = cacheKeyPrefix == null ? "" : cacheKeyPrefix;
	}

	public void afterPropertiesSet() throws Exception {
		if(methodCache == null) {
			methodCache = (Cache)beanFactory.getBean(methodCacheBeanName);
		}
		Assert.notNull(methodCache,"'methodCache' must be not null");
	}

}  