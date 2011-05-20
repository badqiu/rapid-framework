package cn.org.rapid_framework.util;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Hibernate;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.StringUtils;

/**
 * 用于Hibernate Object 的序列化,访问延迟加载的属性不会抛出LazyInitializationException,而会返回null值.
 * 
 * 
 * 使用:
 * <pre>
 * Blog proxyBlog = new HibernateBeanSerializer(blog).getProxy();
 * </pre>
 * @author badqiu
 * @param <T>
 */
@SuppressWarnings("all")
public class HibernateBeanSerializer <T> {
	T proxy = null;
	/**
	 */
	public HibernateBeanSerializer(T object,String... excludesProperties) {
		if(object == null) {
			this.proxy = null;
		}else {
			ProxyFactory pf = new ProxyFactory();
			pf.setTargetClass(object.getClass());
			pf.setOptimize(true);
			pf.setTarget(object);
			pf.setProxyTargetClass(true);
			pf.setOpaque(true);
			pf.setExposeProxy(true);
			pf.setPreFiltered(true);
			pf.setInterfaces(new Class[]{HibernateBeanSerializerProxy.class});
			HibernateBeanSerializerAdvice beanSerializerAdvice = new HibernateBeanSerializerAdvice();
			beanSerializerAdvice.setExcludesProperties(excludesProperties);
			pf.addAdvice(beanSerializerAdvice);
			
			this.proxy = (T)pf.getProxy();
		}
	}

	public T getProxy(){
		return this.proxy;
	}
	
	static private class HibernateBeanSerializerAdvice implements MethodInterceptor {
		private String[] excludesProperties = new String[0]; 
		public String[] getExcludesProperties() {
			return excludesProperties;
		}
		public void setExcludesProperties(String[] excludesProperties) {
			this.excludesProperties = excludesProperties == null ? new String[0] : excludesProperties;
		}
		public Object invoke(MethodInvocation mi) throws Throwable {
			String propertyName = getPropertyName(mi.getMethod().getName());
			Class returnType = mi.getMethod().getReturnType();
			
			if(propertyName == null) {
				return mi.proceed();
			}
			if(!Hibernate.isPropertyInitialized(mi.getThis(), propertyName)) {
				return null;
			}
			if(isExclude(mi, propertyName)) {
				return null;
			}
			
			Object returnValue = mi.proceed();
			return processReturnValue(returnType, returnValue);
		}
		
		private Object processReturnValue(Class returnType, Object returnValue) {
			if(returnValue == null)
				return null;
			if(Modifier.isFinal(returnType.getModifiers())) {
				return returnValue;
			}
			//This might be a lazy-collection so we need to double check
			if(!Hibernate.isInitialized(returnValue)) {
				return null;				
			}
			
			//this is Hibernate Object
			if(returnValue instanceof HibernateProxy) {
				return new HibernateBeanSerializer(returnValue).getProxy();
			}else if(returnValue instanceof PersistentCollection) {
				if(returnType.isAssignableFrom(Map.class)) {
					Map proxyMap = new LinkedHashMap();
					Map map = (Map)returnValue;
					Set<Map.Entry> entrySet = map.entrySet();
					for(Map.Entry entry : entrySet) {
						proxyMap.put(entry.getKey(), new HibernateBeanSerializer(entry.getValue()));
					}
					return proxyMap;
				}
				
				Collection proxyCollection = null;
				if(returnType.isAssignableFrom(Set.class)) {
					proxyCollection = new LinkedHashSet();
				}else if(returnType.isAssignableFrom(List.class)) {
					proxyCollection = new ArrayList();
				}else {
					return returnValue;
				}
				
				for(Object o : (Collection)returnValue) {
					proxyCollection.add(new HibernateBeanSerializer(o).getProxy());
				}
				return proxyCollection;
			}else {
				return returnValue;
			}
		}
 
		private boolean isExclude(MethodInvocation mi, String propertyName)
				throws Throwable {
			
			for(String excludePropertyName : excludesProperties) {
				if(propertyName.equals(excludePropertyName)) {
					return true;
				}
			}
			
			return false;
		}
		
		private static String getPropertyName(String methodName) {
			String propertyName = null;
			if(methodName.startsWith("get")) {
				propertyName = methodName.substring("get".length());
			}else if(methodName.startsWith("is")) {
				propertyName = methodName.substring("is".length());
			}else if(methodName.startsWith("set")) {
				propertyName = methodName.substring("set".length());
			}
			return propertyName == null ? null : StringUtils.uncapitalize(propertyName);
		}
	}
}
