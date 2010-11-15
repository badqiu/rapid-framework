package cn.org.rapid_framework.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.org.rapid_framework.generator.util.StringHelper;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 提供附加数据的拦截器,拦截getProperty()的执行,如果data中存在该数据则返回数据
 * @author badqiu
 *
 */
public class MapBaseMethodInterceptor implements MethodInterceptor {
	Map data = new HashMap();
	Object target = new Object();
	
	public MapBaseMethodInterceptor(Map data,Object target) {
		super();
		this.data = data;
		this.target = target;
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
//		System.out.println(String.format("args:%s",args));
//		System.out.println(String.format("method:%s,",method));
//		System.out.println(String.format("proxy:%s",proxy));
		Object result = process(obj, method, args, proxy);
//		System.out.println(method.getName()+"() => "+result);
		return result;
	}

	private Object process(Object obj, Method method, Object[] args,MethodProxy proxy) throws Throwable {
		
		String propetyName = getPropertyName(method,args);
		if(propetyName == null) {
			return invokeTarget(obj, args, proxy);
		}
		
		Object result = data.get(propetyName);
		if(isEmptyString(result)) {
			return invokeTarget(obj, args, proxy);
		}else {
			return result;
		}
	}

	private Object invokeTarget(Object obj, Object[] args, MethodProxy proxy) throws Throwable {
		return proxy.invoke(target, args);
	}
	
	private boolean isEmptyString(Object value) {
		if(value == null) return true;
		return value instanceof String && ((String)value).length() == 0;
	}
	
	private String getPropertyName(Method method,Object[] args) {
		if(args != null && args.length > 0) return null;
		
		String name = method.getName();
		if(name.startsWith("get")) {
			return StringHelper.uncapitalize(name.substring(3));
		}else if(name.startsWith("is") && method.getReturnType() == Boolean.class) {
			return StringHelper.uncapitalize(name.substring(2));
		}
		return null;
	}
	
	public static Object createProxy(Class clazz,Object target, Map map) {
		Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
		enhancer.setCallback(new MapBaseMethodInterceptor(map,target));
        return enhancer.create();
	}

}
