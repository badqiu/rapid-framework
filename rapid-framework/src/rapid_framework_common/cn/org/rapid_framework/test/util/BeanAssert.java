package cn.org.rapid_framework.test.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;

import edu.emory.mathcs.backport.java.util.Collections;

public class BeanAssert {

	public static void assertPropertiesNotNull(Object bean,String... ignoreProperties) {
		PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(bean.getClass());
		
		List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : Collections.emptyList();
		
		for(int i = 0; i < descriptors.length; i++ ) {
            String name = descriptors[i].getName();
            Method readMethod = descriptors[i].getReadMethod();
            if (readMethod != null && !ignoreList.contains(name)) {
	            	Object propValue;
					try {
						propValue = readMethod.invoke(bean);
						if(propValue == null) {
							String beanClass = bean.getClass().getSimpleName();
		            		throw new Error("AssertionFailedError, ["+beanClass+"."+name+"] must be not null ");
		            	}
					} catch (IllegalArgumentException e) {
						throw new Error("AssertionFailedError, get property value:["+bean.getClass().getSimpleName()+"."+name+"] occer error.",e);
					} catch (IllegalAccessException e) {
						throw new Error("AssertionFailedError, get property value:["+bean.getClass().getSimpleName()+"."+name+"] occer error.",e);
					} catch (InvocationTargetException e) {
						throw new Error("AssertionFailedError, get property value:["+bean.getClass().getSimpleName()+"."+name+"] occer error.",e);
					}
            }
        }
	}
	
}
