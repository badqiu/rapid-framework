package cn.org.rapid_framework.cache.aop.interceptor;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.jaxen.function.StringFunction;

import cn.org.rapid_framework.cache.Cache;
import cn.org.rapid_framework.cache.aop.annotation.MethodCache;
import cn.org.rapid_framework.generator.util.StringHelper;


public class AnnotationMethodCacheInterceptor implements MethodInterceptor {  
  
    private Cache methodCache;  
  
    public void setMethodCache(Cache methodCache) {  
        this.methodCache = methodCache;  
    }  
  
    public Object invoke(MethodInvocation invocation) throws Throwable {  
        if (invocation.getMethod().isAnnotationPresent(MethodCache.class)) {  
        	String targetName = invocation.getThis().getClass().getName();
            String methodName = invocation.getMethod().getName();  
            Object[] arguments = invocation.getArguments();  
            MethodCache methodCache = invocation.getMethod().getAnnotation(MethodCache.class);
            String cacheKey = getCackeKey(targetName, methodName, arguments,methodCache);
            return getResult(cacheKey, invocation,methodCache.expireTime()); 
        } else {  
            return invocation.proceed();  
        }  
    }



	private Object getResult(String cacheKey,MethodInvocation invocation, int expiration) throws Throwable {     
        Object result = methodCache.get(cacheKey); 
        if(result == null) {
        	result = invocation.proceed();
        	methodCache.set(cacheKey, result, expiration);
        }
        return result;
    }   
	
	protected String getCackeKey(String className, String methodName,
			Object[] arguments, MethodCache methodCache) {
		String cacheKey = null;
		if(StringUtils.isEmpty(methodCache.cacheKey())) {
			cacheKey = getCacheKey(className, methodName, arguments);     
		}else {
			cacheKey = getCacheKeyWithArguments(methodCache.cacheKey(),arguments);
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
}  