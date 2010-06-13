package cn.org.rapid_framework.test.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
/**
 * 用于为Bean设置属性设置默认值的工具类.
 * 
 * 示例使用:<br />
 * UserBean bean = BeanDefaultValueUtils.setBeanProperties(new UserBean());
 * 
 * <pre>
 * <b>相关默认值:</b>
 * 数字类型: 1
 * 日期类型: 当前时间
 * boolean: true
 * enum类型: Enum.values()[0]
 * </pre>
 * @author badqiu
 *
 */
public class BeanDefaultValueUtils {
	public static int DEFAULT_VALUE = 1;
	
    public static <T> T setBeanProperties(T obj) {
        return setBeanProperties(obj, DEFAULT_VALUE);
    }
    
    public static <T> T setBeanProperties(T obj,int defaultValue) {
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(obj.getClass());
        for(int i = 0; i < descriptors.length; i++ ) {
            String name = descriptors[i].getName();
            Method writeMethod = descriptors[i].getWriteMethod();
            if (writeMethod != null) {
                for(Class parameterType : writeMethod.getParameterTypes()) {
                	Object value = null;
                    try {
                    	value = getDefaultValue0(parameterType,defaultValue);
                        writeMethod.setAccessible(true);
                        writeMethod.invoke(obj, value);
                    }catch(Exception e) {
                        throw new IllegalArgumentException("cannot set property:"+name+" with default value:"+value,e);
                    }
                }
            }
        }
        return obj;
    }
   
    private static Object getDefaultValue(Class<?> targetType) {
    	return getDefaultValue(targetType,DEFAULT_VALUE);
    }
    
    private static Object getDefaultValue(Class<?> targetType,int defaultValue){
    	try {
    		return getDefaultValue0(targetType, defaultValue);
    	}catch(Exception e) {
    		throw new IllegalArgumentException("cannot generate default value for targetType:"+targetType);
    	}
    }

	private static Object getDefaultValue0(Class<?> targetType, int defaultValue)
			throws IllegalAccessException, InvocationTargetException {
		if(targetType.isArray()) {
            Class<?> componentType = targetType.getComponentType();
            Object array = java.lang.reflect.Array.newInstance(componentType, 1);
            java.lang.reflect.Array.set(array, 0, getDefaultValue(componentType,defaultValue));
            return array;
        }
        
        if(targetType == String.class) {
            return String.valueOf(defaultValue);
        }
        if(targetType == char.class) {
            return String.valueOf(defaultValue).charAt(0);
        }
        if(targetType == Byte.class || targetType == byte.class) {
            return (byte)defaultValue;
        }
        if(targetType == Short.class || targetType == short.class) {
            return (short)defaultValue;
        }
        if(targetType == Integer.class || targetType == int.class) {
            return (int)defaultValue;
        }
        if(targetType == Long.class || targetType == long.class) {
            return (long)defaultValue;
        }
        if(targetType == Float.class || targetType == float.class) {
            return (float)defaultValue;
        }
        if(targetType == Double.class || targetType == double.class) {
            return (double)defaultValue;
        }
        if(targetType == BigDecimal.class) {
            return new BigDecimal(defaultValue);
        }
        if(targetType == BigInteger.class) {
            return BigInteger.valueOf(defaultValue);
        }
        if(targetType == Boolean.class || targetType == boolean.class) {
            return true;
        }
        if(targetType == java.util.Date.class) {
            return new Date();
        }
        if(targetType == java.sql.Date.class) {
            return new java.sql.Date(System.currentTimeMillis());
        }
        if(targetType == java.sql.Time.class) {
            return new java.sql.Time(System.currentTimeMillis());
        }
        if(targetType == java.sql.Timestamp.class) {
            return new java.sql.Timestamp(System.currentTimeMillis());
        }
        if(targetType == Calendar.class) {
        	Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			return calendar;
        }
        if(targetType.isEnum()) {
            Method m = ClassUtils.getStaticMethod(targetType, "values", new Class[]{});
            Enum[] enums = (Enum[])m.invoke(null);
			return enums != null && enums.length > 0 ? enums[0] : null;
        }
        
        if(targetType.isInterface()) {
            if(targetType.isAssignableFrom(Set.class)) {
            	return new HashSet();
            }
            if(targetType.isAssignableFrom(List.class)) {
            	return new ArrayList();
            }
            if(targetType.isAssignableFrom(Collection.class)) {
            	return new ArrayList();
            }
            if(targetType.isAssignableFrom(Map.class)) {
            	return new HashMap();
            }
            return null;
        }
        try {
        	return BeanUtils.instantiateClass(targetType);
        }catch(Exception e) {
        	//ignore
        	return null;
        }
        
//        throw new IllegalArgumentException("cannot generate default value for targetType:"+targetType);
	}
}
