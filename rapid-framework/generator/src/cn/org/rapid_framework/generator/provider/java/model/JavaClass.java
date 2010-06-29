package cn.org.rapid_framework.generator.provider.java.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.org.rapid_framework.generator.util.typemapping.ActionScriptDataTypesUtils;

public class JavaClass {
	private Class clazz;
	public JavaClass(Class clazz) {
		this.clazz = clazz;
	}
	
	public String getClassName() {
		return this.clazz.getSimpleName();
	}
	
	public String getPackageName() {
		return clazz.getPackage().getName();
	}
	
	public String getSuperclassName() {
		return clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : null;
	}
	
	public List<JavaMethod> getMethods() {
		Method[] methods = clazz.getDeclaredMethods();
		List result = new ArrayList();
		for(Method m : methods) {
			result.add(new JavaMethod(m,this));
		}
		return result;
	}
	
	public List getProperties() throws Exception {
		List result = new ArrayList();
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor pd : pds) {
			result.add(new JavaProperty(pd,this));
		}
		return result;
	}
	
	public List<JavaField> getFields() {
		Field[] fields = clazz.getDeclaredFields();
		List result = new ArrayList();
		for(Field f : fields) {
			result.add(new JavaField(f,this));
		}
		return result;
	}
	
	public String getPackagePath(){
		return getPackageName().replace(".", "/");
	}
	
	public String getParentPackageName() {
		return getPackageName().substring(0,getPackageName().lastIndexOf("."));
	}

	public String getParentPackagePath() {
		return getParentPackageName().replace(".", "/");
	}
	
	public String getAsType() {
		return ActionScriptDataTypesUtils.getPreferredAsType(clazz.getName());
	}
	
	public String getJavaType() {
		return clazz.getName();
	}
	
	public Class getClazz() {
	    return clazz;
	}
	
	public String toString() {
		return "JavaClass:"+clazz.getName();
	}
}
