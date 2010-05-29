package cn.org.rapid_framework.beanutils;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.util.ReflectionUtils;

/**
 * BeanUtils的等价类，只是将check exception改为uncheck exception
 * @author badqiu
 *
 */
public class BeanUtils {
	private static BeanUtilsBean b = BeanUtilsBean.getInstance();

	public static Object cloneBean(Object bean){
		try {
			return b.cloneBean(bean);
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
			b.copyProperties(dest, orig);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	public static void copyProperty(Object bean, String name, Object value) {
		try {
			b.copyProperty(bean, name, value);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	public static Map describe(Object bean) {
		try {
			return b.describe(bean);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String[] getArrayProperty(Object bean, String name){
		try {
			return b.getArrayProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static ConvertUtilsBean getConvertUtils() {
		return b.getConvertUtils();
	}

	public static String getIndexedProperty(Object bean, String name, int index){
		try {
			return b.getIndexedProperty(bean, name, index);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getIndexedProperty(Object bean, String name){
		try {
			return b.getIndexedProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getMappedProperty(Object bean, String name, String key){
		try {
			return b.getMappedProperty(bean, name, key);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getMappedProperty(Object bean, String name){
		try {
			return b.getMappedProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getNestedProperty(Object bean, String name){
		try {
			return b.getNestedProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getProperty(Object bean, String name){
		try {
			return b.getProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static PropertyUtilsBean getPropertyUtils() {
		try {
			return b.getPropertyUtils();
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getSimpleProperty(Object bean, String name) {
		try {
			return b.getSimpleProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static void populate(Object bean, Map properties) {
		try {
			b.populate(bean, properties);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	public static void setProperty(Object bean, String name, Object value) {
		try {
			b.setProperty(bean, name, value);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	private static void handleReflectionException(Exception e) {
		ReflectionUtils.handleReflectionException(e);
	}

}
