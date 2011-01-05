package com.company.project.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.flex.messaging.io.CglibBeanProxy;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import flex.messaging.io.BeanProxy;
import flex.messaging.io.PropertyProxyRegistry;

@SuppressWarnings(value="all")
public class FlexServiceUtil {
	
	static {
	    ConvertRegisterHelper.registerConverters();
		//注册所有Serializable.class的对象都includeReadOnly bean properties,对只读对象也序列化
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

	public static <T> T copyProperties(Class<T> destClass,Object orig) {
		return BeanUtils.copyProperties(destClass, orig);
	}
	
	public static void copyProperties(Object target,Object source) {
		BeanUtils.copyProperties(target, source);
	}
	
	public static <T extends PageRequest> T newQuery(Class<T> queryClazz,PageRequest pr) {
		PageRequest query = org.springframework.beans.BeanUtils
				.instantiateClass(queryClazz);
		copyProperties(query, pr.getFilters());
		query.setPageNumber(pr.getPageNumber());
		query.setPageSize(pr.getPageSize());
		query.setSortColumns(pr.getSortColumns());
		return (T) query;
	}
}
