package cn.org.rapid_framework.beanutils;

import java.util.Map;

import org.springframework.util.ReflectionUtils;

/**
 * apache BeanUtils的等价类，只是将check exception改为uncheck exception
 * @author badqiu
 *
 */
public class BeanUtils {
	
	private static void handleReflectionException(Exception e) {
		ReflectionUtils.handleReflectionException(e);
	}
	
	public static Object cloneBean(Object bean){
		try {
			return org.apache.commons.beanutils.BeanUtils.cloneBean(bean);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static <T> T copyProperties(Class<T> destClass,Object orig) {
		try {
			Object target = destClass.newInstance();
			copyProperties((Object)target, orig);
			return (T)target;
		}catch(Exception e) {
			handleReflectionException(e);
			return null;
		}
	}
	
	public static void copyProperties(Object dest, Object orig) {
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	public static void copyProperty(Object bean, String name, Object value) {
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperty(bean, name, value);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	public static Map describe(Object bean) {
		try {
			return org.apache.commons.beanutils.BeanUtils.describe(bean);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String[] getArrayProperty(Object bean, String name){
		try {
			return org.apache.commons.beanutils.BeanUtils.getArrayProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getIndexedProperty(Object bean, String name, int index){
		try {
			return org.apache.commons.beanutils.BeanUtils.getIndexedProperty(bean, name, index);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getIndexedProperty(Object bean, String name){
		try {
			return org.apache.commons.beanutils.BeanUtils.getIndexedProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getMappedProperty(Object bean, String name, String key){
		try {
			return org.apache.commons.beanutils.BeanUtils.getMappedProperty(bean, name, key);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getMappedProperty(Object bean, String name){
		try {
			return org.apache.commons.beanutils.BeanUtils.getMappedProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getNestedProperty(Object bean, String name){
		try {
			return org.apache.commons.beanutils.BeanUtils.getNestedProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getProperty(Object bean, String name){
		try {
			return org.apache.commons.beanutils.BeanUtils.getProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getSimpleProperty(Object bean, String name) {
		try {
			return org.apache.commons.beanutils.BeanUtils.getSimpleProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static void populate(Object bean, Map properties) {
		try {
			org.apache.commons.beanutils.BeanUtils.populate(bean, properties);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	public static void setProperty(Object bean, String name, Object value) {
		try {
			org.apache.commons.beanutils.BeanUtils.setProperty(bean, name, value);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

}
