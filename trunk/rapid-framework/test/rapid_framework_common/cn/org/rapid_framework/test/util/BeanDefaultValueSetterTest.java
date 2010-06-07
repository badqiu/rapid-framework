package cn.org.rapid_framework.test.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import junit.framework.TestCase;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import cn.org.rapid_framework.test.util.testbean.Bean1;
import cn.org.rapid_framework.test.util.testbean.SexEnum;

public class BeanDefaultValueSetterTest extends TestCase {
    
    public void test() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Bean1 bean = new Bean1();
        setBeanProperties(bean);
        assertEquals(bean.getInt1(),1);
        assertEquals(bean.getInteger1(),new Integer(1));
        assertEquals(bean.getLong1(),new Long(1));
        assertEquals(bean.getSqldate1(),new java.sql.Date(System.currentTimeMillis()));
        assertEquals(bean.getInt1(),1);
        assertEquals(bean.getSex(),SexEnum.F);
        assertEquals(bean.getChar1(),'1');
    }
    
    public static void setBeanProperties(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(obj.getClass());
        for(int i = 0; i < descriptors.length; i++ ) {
            String name = descriptors[i].getName();
            Method writeMethod = descriptors[i].getWriteMethod();
            if (writeMethod != null) {
                for(Class parameterType : writeMethod.getParameterTypes()) {
                    Object defaultValue = getDefaultValue(parameterType);
                    try {
                        writeMethod.setAccessible(true);
                        writeMethod.invoke(obj, defaultValue);
                    }catch(Exception e) {
                        throw new IllegalArgumentException("cannot set property:"+name+" with default value:"+defaultValue);
                    }
                }
            }
        }
    }
    
    public static int DEFAULT_NUMBER_VALUE = 1;
    private static Object getDefaultValue(Class<?> targetType) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if(targetType.isArray()) {
            Class<?> componentType = targetType.getComponentType();
            Object array = java.lang.reflect.Array.newInstance(componentType, DEFAULT_NUMBER_VALUE);
            java.lang.reflect.Array.set(array, 0, getDefaultValue(componentType));
            return array;
        }
        
        if(targetType == String.class) {
            return String.valueOf(DEFAULT_NUMBER_VALUE);
        }
        if(targetType == char.class) {
            return String.valueOf(DEFAULT_NUMBER_VALUE).charAt(0);
        }
        if(targetType == Byte.class || targetType == byte.class) {
            return (byte)DEFAULT_NUMBER_VALUE;
        }
        if(targetType == Short.class || targetType == short.class) {
            return (short)DEFAULT_NUMBER_VALUE;
        }
        if(targetType == Integer.class || targetType == int.class) {
            return DEFAULT_NUMBER_VALUE;
        }
        if(targetType == Long.class || targetType == long.class) {
            return 1L;
        }
        if(targetType == Float.class || targetType == float.class) {
            return (float)DEFAULT_NUMBER_VALUE;
        }
        if(targetType == Double.class || targetType == double.class) {
            return (double)DEFAULT_NUMBER_VALUE;
        }
        if(targetType == BigDecimal.class) {
            return new BigDecimal(DEFAULT_NUMBER_VALUE);
        }
        if(targetType == BigInteger.class) {
            return BigInteger.valueOf(DEFAULT_NUMBER_VALUE);
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
        if(targetType.isEnum()) {
            Method m = ClassUtils.getStaticMethod(targetType, "values", new Class[]{});
            return ((Enum[])m.invoke(null))[0];
        }
        
        if(targetType.isInterface()) {
            return null;
        }
        
        throw new IllegalArgumentException("cannot generate default value for targetType:"+targetType);
    }
    

    
}
