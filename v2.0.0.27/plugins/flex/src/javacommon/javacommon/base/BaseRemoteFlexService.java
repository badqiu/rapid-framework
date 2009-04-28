package javacommon.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.flex.messaging.io.CglibBeanProxy;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.util.HibernateBeanSerializer;
import flex.messaging.io.BeanProxy;
import flex.messaging.io.PropertyProxyRegistry;

public class BaseRemoteFlexService <E>{
	
	static {
		//注册所有Serializable.class的对象都includeReadOnly bean properties
		BeanProxy beanProxy = new CglibBeanProxy();
		beanProxy.setIncludeReadOnly(true);
		PropertyProxyRegistry.getRegistry().register(Serializable.class, beanProxy);
	}
	
	public static Page convertPageList2TargetClass(Page page,Class targetClass){
		List list = page.getResult();
		List convertedList = new ArrayList();
		for(Object o : list) {
			convertedList.add(copyProperties(targetClass,o));
		}
		page.setResult(convertedList);
		return page;
	}
	/**
	 * 将Page的list转变为proxy对象,避免hibernate lazy问题
	 * @param page
	 * @return
	 */
	public static Page convertPageList2BeanSerializerProxy(Page page){
		List list = page.getResult();
		List convertedList = new ArrayList();
		for(Object o : list) {
			Object proxy = new HibernateBeanSerializer(o).getProxy();
			convertedList.add(proxy);
		}
		page.setResult(convertedList);
		return page;
	}
	
	public static <T> T copyProperties(Class<T> destClass,Object orig) {
		return BeanUtils.copyProperties(destClass, orig);
	}
	
	public static void copyProperties(Object target,Object source) {
		BeanUtils.copyProperties(target, source);
	}
	
}
