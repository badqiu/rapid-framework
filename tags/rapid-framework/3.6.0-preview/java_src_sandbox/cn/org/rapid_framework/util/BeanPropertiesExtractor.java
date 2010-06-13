package cn.org.rapid_framework.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ReflectionUtils;

public class BeanPropertiesExtractor {

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
	   
	   //TODO cannot get public field value
	   private static Object getPropertyValue(String keyProperty, Object obj){
	       try {
	           return BeanUtils.getProperty(obj,keyProperty);
	       } catch (Exception e) {
	           ReflectionUtils.handleReflectionException(e);
	           return null;
	       }
	   }
	   
}
