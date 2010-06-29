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
	
	public JavaMethod[] getMethods() {
		return toJavaMethods(clazz.getDeclaredMethods());
	}
	
	public JavaProperty[] getProperties() throws Exception {
		List<JavaProperty> result = new ArrayList<JavaProperty>();
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor pd : pds) {
			result.add(new JavaProperty(pd,this));
		}
		return (JavaProperty[])result.toArray(new JavaProperty[0]);
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
	
	public String getCanonicalName() {
		return clazz.getCanonicalName();
	}

	public JavaField getField(String name) throws NoSuchFieldException,SecurityException {
		return new JavaField(clazz.getField(name),this);
	}

	public JavaClass getSuperclass() {
		return new JavaClass(clazz.getSuperclass());
	}

	public boolean isAnnotation() {
		return clazz.isAnnotation();
	}

	public boolean isAnonymousClass() {
		return clazz.isAnonymousClass();
	}

	public boolean isArray() {
		return clazz.isArray();
	}

	public boolean isEnum() {
		return clazz.isEnum();
	}

	public boolean isInstance(Object obj) {
		return clazz.isInstance(obj);
	}

	public boolean isInterface() {
		return clazz.isInterface();
	}

	public boolean isLocalClass() {
		return clazz.isLocalClass();
	}

	public boolean isMemberClass() {
		return clazz.isMemberClass();
	}

	public boolean isPrimitive() {
		return clazz.isPrimitive();
	}

	public boolean isSynthetic() {
		return clazz.isSynthetic();
	}

	public Class getClazz() {
	    return clazz;
	}
	
	private JavaMethod[] toJavaMethods(Method[] declaredMethods) {
		JavaMethod[] methods = new JavaMethod[declaredMethods.length];
		for(int i = 0; i < declaredMethods.length; i++) {
			methods[i] = new JavaMethod(declaredMethods[i],this);
		}
		return methods;
	}
	
	public String toString() {
		return "JavaClass:"+clazz.getName();
	}
}
