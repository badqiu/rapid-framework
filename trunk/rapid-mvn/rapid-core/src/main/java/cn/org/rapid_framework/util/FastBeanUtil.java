package cn.org.rapid_framework.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

/**
 * 与apache commons BeanUtils相比，这个类的速度更快
 * @author badqiu
 *
 */
public class FastBeanUtil {

	public static Map<String,Object> describe(Object obj) {
		if (obj instanceof Map)
			return (Map<String,Object>) obj;
		
		Map<String,Object>  map = new HashMap<String,Object>();
		PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(obj.getClass());
		for(int i = 0; i < descriptors.length; i++ ) {
			String name = descriptors[i].getName();
			
            Method readMethod = descriptors[i].getReadMethod();
			if (readMethod != null) {
				try {
					Object value = readMethod.invoke(obj);
					map.put(name, value);
				} catch (Exception e) {
					throw new RuntimeException("error on read property:"+name+" on class:"+obj.getClass());
				}
            }
		}
		return map;
	}
	
	
    /**
     * <p>Populate the JavaBeans properties of the specified bean, based on
     * the specified name/value pairs.  This method uses Java reflection APIs
     * to identify corresponding "property setter" method names, and deals
     * with setter arguments of type <code>String</code>, <code>boolean</code>,
     * <code>int</code>, <code>long</code>, <code>float</code>, and
     * <code>double</code>.  In addition, array setters for these types (or the
     * corresponding primitive types) can also be identified.</p>
     * 
     * <p>The particular setter method to be called for each property is
     * determined using the usual JavaBeans introspection mechanisms.  Thus,
     * you may identify custom setter methods using a BeanInfo class that is
     * associated with the class of the bean itself.  If no such BeanInfo
     * class is available, the standard method name conversion ("set" plus
     * the capitalized name of the property in question) is used.</p>
     * 
     * <p><strong>NOTE</strong>:  It is contrary to the JavaBeans Specification
     * to have more than one setter method (with different argument
     * signatures) for the same property.</p>
     *
     * <p><strong>WARNING</strong> - The logic of this method is customized
     * for extracting String-based request parameters from an HTTP request.
     * It is probably not what you want for general property copying with
     * type conversion.  For that purpose, check out the
     * <code>copyProperties()</code> method instead.</p>
     *
     * @param bean JavaBean whose properties are being populated
     * @param properties Map keyed by property name, with the
     *  corresponding (String or String[]) value(s) to be set
     *
     * @exception IllegalAccessException if the caller does not have
     *  access to the property accessor method
     * @exception InvocationTargetException if the property accessor method
     *  throws an exception
     */
    public static void populate(Object bean, Map properties) {

        if ((bean == null) || (properties == null)) {
            return;
        }

        // Loop through the property name/value pairs to be set
        Iterator entries = properties.entrySet().iterator();
        while (entries.hasNext()) {

            // Identify the property name and value(s) to be assigned
            Map.Entry entry = (Map.Entry)entries.next();
            String name = (String) entry.getKey();
            if (name == null) {
                continue;
            }
			try {
				// Perform the assignment for this property
				setProperty(bean, name, entry.getValue());
			} catch (Throwable ex) {
				throw new FatalBeanException("Could not set property:"+ name + "=" + entry.getValue()+" on class:"+bean.getClass(), ex);
			}
        }
    }

    
	private static void setProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(bean.getClass(), name);
		if(pd != null && pd.getWriteMethod() != null) {
			setProperty(bean, pd, value);
		}
	}


	public static void copyProperties(Object source, Object target,String... ignoreProperties) {
		copyProperties(source, target,null, ignoreProperties);
	}
	
	/**
	 * Copy the property values of the given source bean into the given target bean.
	 * <p>Note: The source and target classes do not have to match or even be derived
	 * from each other, as long as the properties match. Any bean properties that the
	 * source bean exposes but the target bean does not will silently be ignored.
	 * @param source the source bean
	 * @param target the target bean
	 * @param editable the class (or interface) to restrict property setting to
	 * @param ignoreProperties array of property names to ignore
	 * @throws BeansException if the copying failed
	 * @see BeanWrapper
	 */
	private static void copyProperties(Object source, Object target, Class<?> editable, String... ignoreProperties)
			throws BeansException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();
		if (editable != null) {
			if (!editable.isInstance(target)) {
				throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
						"] not assignable to Editable class [" + editable.getName() + "]");
			}
			actualEditable = editable;
		}
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
		Collection<String> ignoreList = (ignoreProperties != null) ? new HashSet(Arrays.asList(ignoreProperties)) : null;

		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null &&
					(ignoreList == null || (!ignoreList.contains(targetPd.getName())))) {
				Object value = null;
				try {
					if (source instanceof Map) {
						Map map = (Map) source;
						if (map.containsKey(targetPd.getName())) {
							value = map.get(targetPd.getName());
							setProperty(target, targetPd, value);
						}
					} else {
						PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
						if (sourcePd != null&& sourcePd.getReadMethod() != null) {
							value = getProperty(source, sourcePd);
							setProperty(target, targetPd, value);
						}
					}
				}catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target,property:"+(targetPd == null ? "" : targetPd.getName())+" value:"+value, ex);
				}
			}
		}
	}

	private static void setProperty(Object target, PropertyDescriptor targetPd,
			Object value) throws IllegalAccessException,
			InvocationTargetException {
		Method writeMethod = targetPd.getWriteMethod();
		if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
			writeMethod.setAccessible(true);
		}
		Object targetValue = FastConvertUtil.convert(writeMethod.getParameterTypes()[0],value);
		writeMethod.invoke(target, targetValue);
	}

	private static Object getProperty(Object source, PropertyDescriptor sourcePd)
			throws IllegalAccessException, InvocationTargetException {
		Object value;
		Method readMethod = sourcePd.getReadMethod();
		if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
			readMethod.setAccessible(true);
		}
		value = readMethod.invoke(source);
		return value;
	}


}
