package cn.org.rapid_framework.test.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyValue;

import edu.emory.mathcs.backport.java.util.Collections;

public class BeanAssert {

	public static void assertPropertiesNotNull(Object bean,String... ignoreProperties) {
		List<PropertyValue> values = getPropertyValues(bean, ignoreProperties);
		for(PropertyValue p : values) {
			if(p.getValue() == null) {
				String beanClass = bean.getClass().getSimpleName();
	    		throw new AssertionError("["+beanClass+"."+p.getName()+"] must be not null ");
			}
		}
	}

	public static void assertPropertiesNull(Object bean,String... ignoreProperties) {
		List<PropertyValue> values = getPropertyValues(bean, ignoreProperties);
		for(PropertyValue p : values) {
			if(p.getValue() != null) {
				String beanClass = bean.getClass().getSimpleName();
	    		throw new AssertionError("["+beanClass+"."+p.getName()+"] must be null ");
			}
		}
	}

	public static void assertPropertiesNotEmpty(Object bean,String... ignoreProperties) {
		List<PropertyValue> values = getPropertyValues(bean, ignoreProperties);
		for(PropertyValue p : values) {
			if(isEmpty(p.getValue())) {
				String beanClass = bean.getClass().getSimpleName();
	    		throw new AssertionError("["+beanClass+"."+p.getName()+"] must be not empty");
			}
		}
	}

	public static void assertPropertiesEmpty(Object bean,String... ignoreProperties) {
		List<PropertyValue> values = getPropertyValues(bean, ignoreProperties);
		for(PropertyValue p : values) {
			if(!isEmpty(p.getValue())) {
				String beanClass = bean.getClass().getSimpleName();
	    		throw new AssertionError("["+beanClass+"."+p.getName()+"] must be not empty");
			}
		}
	}
	
	private static List<PropertyValue> getPropertyValues(Object bean,String... ignoreProperties) throws Error {
		PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(bean.getClass());
		
		List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : Collections.emptyList();
		
		List<PropertyValue> propertyValues = new ArrayList();
		for(int i = 0; i < descriptors.length; i++ ) {
            String name = descriptors[i].getName();
            Method readMethod = descriptors[i].getReadMethod();
            if("class".equals(name)) {
            	continue;
            }
            if (readMethod != null && !ignoreList.contains(name)) {
				Object value = invokeMethod(bean, name, readMethod);
				PropertyValue pv = new PropertyValue(name,value);
				propertyValues.add(pv);
            }
        }
		return propertyValues;
	}

	private static Object invokeMethod(Object bean, String name, Method readMethod)throws Error {
		try {
			return readMethod.invoke(bean);
		} catch (IllegalArgumentException e) {
			throw new Error("AssertionError, get property value:["+bean.getClass().getSimpleName()+"."+name+"] occer error.",e);
		} catch (IllegalAccessException e) {
			throw new Error("AssertionError, get property value:["+bean.getClass().getSimpleName()+"."+name+"] occer error.",e);
		} catch (InvocationTargetException e) {
			throw new Error("AssertionError, get property value:["+bean.getClass().getSimpleName()+"."+name+"] occer error.",e);
		}
	}
	
	public static boolean isEmpty(Object o) throws IllegalArgumentException {
		if(o == null) return true;

		if(o instanceof String) {
			if(((String)o).length() == 0){
				return true;
			}
		} else if(o instanceof Collection) {
			if(((Collection)o).isEmpty()){
				return true;
			}
		} else if(o.getClass().isArray()) {
			if(Array.getLength(o) == 0){
				return true;
			}
		} else if(o instanceof Map) {
			if(((Map)o).isEmpty()){
				return true;
			}
		}else {
			return false;
		}

		return false;
	}
	
}
