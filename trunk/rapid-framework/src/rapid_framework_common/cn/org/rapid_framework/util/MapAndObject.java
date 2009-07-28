package cn.org.rapid_framework.util;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
/**
 * 用于get(key)方法对Map及Bean混合取值.
 * 如果从map中取值为null,则会从bean中取值
 * @author badqiu
 */
@SuppressWarnings("unchecked")
public class MapAndObject implements Map{
	Map map;
	Object bean;

	public MapAndObject(Map map, Object bean) {
		super();
		this.map = map;
		this.bean = bean;
	}

	public Map getMap() {
		return map;
	}

	public Object getBean() {
		return bean;
	}
	
	public Object get(Object key) {
		return getFromMapOrBean(key);
	}

	Object getFromMapOrBean(Object key) {
		Object result = null;
		if (map != null) {
			result = map.get(key);
		}
		
		if(result == null && bean instanceof Map) {
			return ((Map)bean).get(key);
		}
		
		if (result == null && bean != null && key instanceof String) {
			String propertyName = (String)key;
			return FastPropertyUtils.getBeanPropertyValue(bean,propertyName);
		}
		return result;
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean containsKey(Object key) {
		throw new UnsupportedOperationException();
	}

	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	public Set entrySet() {
		throw new UnsupportedOperationException();
	}

	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	public Set keySet() {
		throw new UnsupportedOperationException();
	}

	public Object put(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map m) {
		throw new UnsupportedOperationException();
	}

	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		throw new UnsupportedOperationException();
	}

	public Collection values() {
		throw new UnsupportedOperationException();
	}
	
	private static class FastPropertyUtils {
		private static Object getBeanPropertyValue(Object bean,String propertyName) {
			if(bean == null) throw new IllegalArgumentException("bean cannot be not null");
			if(propertyName == null) throw new IllegalArgumentException("propertyName cannot be not null");
			
			try {
				ExtendBeanInfo beanInfo = getBeanInfo(bean.getClass());
				PropertyDescriptor pd = beanInfo.getPropertyDescriptor(propertyName);
				if(pd == null || pd.getReadMethod() == null) {
					return null;
				}
				return pd.getReadMethod().invoke(bean);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(
						"cannot get property value by property:" + propertyName + " on class:" + bean.getClass(), e);
			} catch (InvocationTargetException e) {
				throw new IllegalStateException(
						"cannot get property value by property:" + propertyName + " on class:" + bean.getClass(), e);
			} catch (IntrospectionException e) {
				throw new IllegalStateException(
						"cannot get property value by property:" + propertyName + " on class:" + bean.getClass(), e);
			}
		}
		
		private static Map beanInfoCache = Collections.synchronizedMap(new WeakHashMap());
		private static ExtendBeanInfo getBeanInfo(Class clazz) throws IntrospectionException {
			ExtendBeanInfo beanInfo = (ExtendBeanInfo)beanInfoCache.get(clazz);
			if(beanInfo == null) {
				beanInfo = new ExtendBeanInfo(Introspector.getBeanInfo(clazz));
				beanInfoCache.put(clazz, beanInfo);
			}
			return beanInfo;
		}
		
		private static class ExtendBeanInfo implements BeanInfo {
			BeanInfo delegate;
			private Map propertyDescriptorCache = Collections.synchronizedMap(new WeakHashMap());
			public ExtendBeanInfo(BeanInfo delegate) {
				super();
				this.delegate = delegate;
			}
	
			public BeanInfo[] getAdditionalBeanInfo() {
				return delegate.getAdditionalBeanInfo();
			}
	
			public BeanDescriptor getBeanDescriptor() {
				return delegate.getBeanDescriptor();
			}
	
			public int getDefaultEventIndex() {
				return delegate.getDefaultEventIndex();
			}
	
			public int getDefaultPropertyIndex() {
				return delegate.getDefaultPropertyIndex();
			}
	
			public EventSetDescriptor[] getEventSetDescriptors() {
				return delegate.getEventSetDescriptors();
			}
	
			public Image getIcon(int iconKind) {
				return delegate.getIcon(iconKind);
			}
	
			public MethodDescriptor[] getMethodDescriptors() {
				return delegate.getMethodDescriptors();
			}
	
			public PropertyDescriptor[] getPropertyDescriptors() {
				return delegate.getPropertyDescriptors();
			}
			
			public PropertyDescriptor getPropertyDescriptor(String name) {
				if(propertyDescriptorCache.containsKey(name)) {
					return (PropertyDescriptor)propertyDescriptorCache.get(name);
				}
				PropertyDescriptor pd = getPropertyDescriptorFromArray(name);
				propertyDescriptorCache.put(name, pd);
				return pd;
			}
	
			private PropertyDescriptor getPropertyDescriptorFromArray(String name) {
				PropertyDescriptor pd = null;
				PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors();
				for(int i = 0; i < propertyDescriptors.length; i++) {
					PropertyDescriptor temp = propertyDescriptors[i];
					if(temp.getName().equals(name)) {
						pd = propertyDescriptors[i];
					}
				}
				return pd;
			}
		}
	}
}
