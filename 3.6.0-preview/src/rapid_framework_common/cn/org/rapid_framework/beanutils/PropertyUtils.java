package cn.org.rapid_framework.beanutils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

/**
 * apache PropertyUtils的等价类，只是将check exception改为uncheck exception
 * @author badqiu
 *
 */
public class PropertyUtils {

	private void handleException(Exception e) {
		ReflectionUtils.handleReflectionException(e);
	}
	
	public void clearDescriptors() {
		org.apache.commons.beanutils.PropertyUtils.clearDescriptors();
	}

	public void copyProperties(Object dest, Object orig){
		try {
			org.apache.commons.beanutils.PropertyUtils.copyProperties(dest, orig);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public Map describe(Object bean) {
		try {
			return org.apache.commons.beanutils.PropertyUtils.describe(bean);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public Object getIndexedProperty(Object bean, String name, int index){
		try {
			return org.apache.commons.beanutils.PropertyUtils.getIndexedProperty(bean, name, index);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public Object getIndexedProperty(Object bean, String name) {
		try {
			return org.apache.commons.beanutils.PropertyUtils.getIndexedProperty(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public Object getMappedProperty(Object bean, String name, String key) {
		try {
			return org.apache.commons.beanutils.PropertyUtils.getMappedProperty(bean, name, key);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public Object getMappedProperty(Object bean, String name) {
		try {
			return org.apache.commons.beanutils.PropertyUtils.getMappedProperty(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public Object getNestedProperty(Object bean, String name) {
		try {
			return org.apache.commons.beanutils.PropertyUtils.getNestedProperty(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public Object getProperty(Object bean, String name) {
		try {
			return org.apache.commons.beanutils.PropertyUtils.getProperty(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public PropertyDescriptor getPropertyDescriptor(Object bean, String name) {
		try {
			return org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptor(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public PropertyDescriptor[] getPropertyDescriptors(Class beanClass) {
		return org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(beanClass);
	}

	public PropertyDescriptor[] getPropertyDescriptors(Object bean) {
		return org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(bean);
	}

	public Class getPropertyEditorClass(Object bean, String name) {
		try {
			return org.apache.commons.beanutils.PropertyUtils.getPropertyEditorClass(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public Class getPropertyType(Object bean, String name) {
		try {
			return org.apache.commons.beanutils.PropertyUtils.getPropertyType(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public Method getReadMethod(PropertyDescriptor descriptor) {
		return org.apache.commons.beanutils.PropertyUtils.getReadMethod(descriptor);
	}

	public Object getSimpleProperty(Object bean, String name){
		try {
			return org.apache.commons.beanutils.PropertyUtils.getSimpleProperty(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public Method getWriteMethod(PropertyDescriptor descriptor) {
		return org.apache.commons.beanutils.PropertyUtils.getWriteMethod(descriptor);
	}


	public boolean isReadable(Object bean, String name) {
		return org.apache.commons.beanutils.PropertyUtils.isReadable(bean, name);
	}

	public boolean isWriteable(Object bean, String name) {
		return org.apache.commons.beanutils.PropertyUtils.isWriteable(bean, name);
	}

	public void setIndexedProperty(Object bean, String name, int index,Object value) {
		try {
		org.apache.commons.beanutils.PropertyUtils.setIndexedProperty(bean, name, index, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public void setIndexedProperty(Object bean, String name, Object value){
		try {
		org.apache.commons.beanutils.PropertyUtils.setIndexedProperty(bean, name, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public void setMappedProperty(Object bean, String name, Object value){
		try {
		org.apache.commons.beanutils.PropertyUtils.setMappedProperty(bean, name, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public void setMappedProperty(Object bean, String name, String key,Object value) {
		try{
		org.apache.commons.beanutils.PropertyUtils.setMappedProperty(bean, name, key, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public void setNestedProperty(Object bean, String name, Object value){
		try {
		org.apache.commons.beanutils.PropertyUtils.setNestedProperty(bean, name, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public void setProperty(Object bean, String name, Object value){
		try {
		org.apache.commons.beanutils.PropertyUtils.setProperty(bean, name, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public void setSimpleProperty(Object bean, String name, Object value){
		try {
		org.apache.commons.beanutils.PropertyUtils.setSimpleProperty(bean, name, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	
}
