package cn.org.rapid_framework.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
/**
 * 混合两个对象为一个Map对象
 * MapAndObject.get()方法将在两个对象取值,Map如果取值为null,则再在Bean中取值ֵ
 * @author badqiu
 */
public class MapAndObject implements Map{
    private Map<?,?> map;
    private Object bean;

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
                Method readMethod = getReadMethod(bean, propertyName);
                if(readMethod == null) {
                    return null;
                }
                return readMethod.invoke(bean);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(
                        "cannot get property value by property:" + propertyName + " on class:" + bean.getClass(), e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(
                        "cannot get property value by property:" + propertyName + " on class:" + bean.getClass(), e.getTargetException());
            }
        }

        private static Method getReadMethod(Object bean, String propertyName) {
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(bean.getClass(), propertyName);
            if(pd == null) return null;
            return pd.getReadMethod();
        }
        
    }
}
