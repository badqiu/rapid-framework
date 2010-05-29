package cn.org.rapid_framework.test.util;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.framework.ProxyFactoryBean;

public class BeanConverterTestUtils {
	

	public static ObjectHolder generateProxy(Object target) {
		MethodInvokeCounter invokeCounter = new MethodInvokeCounter();
		ProxyFactoryBean factory = new ProxyFactoryBean();
		factory.addAdvice(invokeCounter);
		factory.setTarget(target);
		return new ObjectHolder(factory.getObject(),invokeCounter);
	}
	
	public static class ObjectHolder {
		public Object proxy;
		public MethodInvokeCounter counter;
		public ObjectHolder(Object proxy, MethodInvokeCounter counter) {
			super();
			this.proxy = proxy;
			this.counter = counter;
		}
		public int getAnyCount() {
			return counter.getAnyCount();
		}
		public int getGetCount() {
			return counter.getGetCount();
		}
		public int getSetCount() {
			return counter.getSetCount();
		}
		public Object getProxy() {
			return proxy;
		}
	}
	
	public static class MethodInvokeCounter implements AfterReturningAdvice{
		private int setCount = 0;
		private int getCount = 0;
		private int anyCount = 0;
		public void afterReturning(Object result, Method method, Object[] args,Object target) throws Throwable {
			anyCount++;
			System.out.println(target.getClass().getSimpleName()+"."+method.getName()+"() for any:"+anyCount);
			if(method.getName().startsWith("set")) {
				setCount++;
				System.out.println(target.getClass().getSimpleName()+"."+method.getName()+"() for set:"+setCount);
			}
			if(isGetMethod(method)) {
				getCount++;
				System.out.println(target.getClass().getSimpleName()+"."+method.getName()+"() for get"+getCount);
			}
		}
		private boolean isGetMethod(Method method) {
			return method.getName().startsWith("get") || (method.getName().startsWith("is") && method.getReturnType() == Boolean.class)
				|| (method.getName().startsWith("is") && method.getReturnType() == boolean.class)
			;
		}
		public int getSetCount() {
			return setCount;
		}
		public int getGetCount() {
			return setCount;
		}
		public int getAnyCount() {
			return anyCount;
		}
	}
	
}
