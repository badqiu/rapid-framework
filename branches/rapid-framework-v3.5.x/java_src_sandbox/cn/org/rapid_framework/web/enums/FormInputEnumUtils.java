package cn.org.rapid_framework.web.enums;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ReflectionUtils;

/**
 * 将一组FormInputEnum[]转变为map
 * @author badqiu
 *
 */
public class FormInputEnumUtils {
	
	/**
	 * key为FormInputEnum.getFormInputKey(), value为FormInputEnum.getFormInputLabel()
	 * @param arrays
	 * @return
	 */
	public static LinkedHashMap<Object,String> toMap(FormInputEnum... arrays) {
		if(arrays == null) return new LinkedHashMap(0);
		return toMap(Arrays.asList(arrays));
	}
	
	public static LinkedHashMap<Object,String> toMap(Collection<FormInputEnum> list) {
		if(list == null) return new LinkedHashMap(0);
		LinkedHashMap map = new LinkedHashMap();
		for(FormInputEnum v : list) {
			map.put(v.getFormInputKey(), v.getFormInputLabel());
		}
		return map;
	}
	
	public static LinkedHashMap<Object,String> extractKeyValue(String keyProperty,String valueProperty,Object... arrays) {
		if(arrays == null) return new LinkedHashMap(0);
		return extractKeyValue(keyProperty,valueProperty,Arrays.asList(arrays));
	}
	
	public static LinkedHashMap<Object,String> extractKeyValue(String keyProperty,String valueProperty,List arrays) {
		if(arrays == null) return new LinkedHashMap(0);
		LinkedHashMap<Object,String> map = new LinkedHashMap();
		for(Object obj : arrays) {
			BeanWrapper bw = new BeanWrapperImpl(obj);
			Object key = getPropertyValue(keyProperty, obj);
			Object value = getPropertyValue(valueProperty, obj);
			map.put(key,value == null ? "" : value.toString());
		}
		return map;
	}

	private static Object getPropertyValue(String keyProperty, Object obj){
		try {
			return BeanUtils.getProperty(obj,keyProperty);
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
			return null;
		}
	}
}
