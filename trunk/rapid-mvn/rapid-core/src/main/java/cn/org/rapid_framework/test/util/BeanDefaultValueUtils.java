package cn.org.rapid_framework.test.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.beans.BeanUtils;

import edu.emory.mathcs.backport.java.util.Arrays;
/**
 * 用于为Bean设置属性设置默认值的工具类.
 * 示例应用场景: 
 * 1.BeanUtils.copyProperties()的测试,如我们需要验证某个属性值是否有拷贝正确.
 * 这时可以为Bean设置属性值,然后copyProperties(),再验证copy完成后的结果.验证还可以工具类:BeanAssert
 * 
 * 
 * 示例使用:<br />
 * UserBean bean = BeanDefaultValueUtils.setBeanProperties(new UserBean());
 * 
 * <pre>
 * <b>相关默认值:</b>
 * 
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
	//TODO 增加对范型 List<String>及 setFields()
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
   
    public static Object getDefaultValue(Class<?> targetType) {
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
        if(targetType == Currency.class) {
        	return Currency.getInstance(Locale.getDefault());
        }
        if(targetType == Locale.class) {
        	return Locale.getDefault();
        }
        if(targetType.isEnum()) {
            Enum[] enums = (Enum[])targetType.getEnumConstants();;
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
            if(targetType.isAssignableFrom(Queue.class)) {
            	return (Queue)new ArrayBlockingQueue(100);
            }
            if(targetType.isAssignableFrom(Iterator.class)) {
            	return (Iterator)new ArrayList();
            }
            if(targetType.isAssignableFrom(Iterable.class)) {
            	return (Iterable)new ArrayList();
            }
            return null;
        }
        Constructor[] cs = targetType.getConstructors();
        Arrays.sort(cs,new Comparator<Constructor>() {
            public int compare(Constructor o1, Constructor o2) {
                return o2.getParameterTypes().length - o1.getParameterTypes().length;
            }
        });
        for(Constructor c : cs) {
            Object obj = newInstance(c,defaultValue);
            if(obj != null) return obj;
        }
    	return null;

        
//        throw new IllegalArgumentException("cannot generate default value for targetType:"+targetType);
	}

    private static Object newInstance(Constructor c,int defaultValue) {
        c.setAccessible(true);
        Object[] args = new Object[c.getParameterTypes().length];
        for(int i = 0; i < c.getParameterTypes().length; i++) {
            Class type = c.getParameterTypes()[i];
            args[i] = getDefaultValue(type);
        }
        try {
            return c.newInstance(args);
        }catch(Exception e) {
            //ignore 
            return null;
        }
    }
}
