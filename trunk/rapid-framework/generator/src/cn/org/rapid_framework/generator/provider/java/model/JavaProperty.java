package cn.org.rapid_framework.generator.provider.java.model;

import java.beans.PropertyDescriptor;

import cn.org.rapid_framework.generator.util.typemapping.ActionScriptDataTypesUtils;

public class JavaProperty {
	PropertyDescriptor propertyDescriptor;
	JavaClass clazz;
	public JavaProperty(PropertyDescriptor pd, JavaClass javaClass) {
		this.propertyDescriptor = pd;
		this.clazz = javaClass;
	}
	
	public String getName() {
		return propertyDescriptor.getName();
	}
	
	public String getJavaType() {
		return propertyDescriptor.getPropertyType().getName();
	}
	
	public JavaClass getPropertyType() {
		return new JavaClass(propertyDescriptor.getPropertyType());
	}

	public String getDisplayName() {
		return propertyDescriptor.getDisplayName();
	}
	
	public JavaMethod getReadMethod() {
		return new JavaMethod(propertyDescriptor.getReadMethod(),clazz);
	}

	public JavaMethod getWriteMethod() {
		return new JavaMethod(propertyDescriptor.getWriteMethod(),clazz);
	}

	public String getAsType() {
		return ActionScriptDataTypesUtils.getPreferredAsType(propertyDescriptor.getPropertyType().getName());
	}
	
	public JavaClass getClazz() {
		return clazz;
	}

	public String toString() {
		return "JavaClass:"+clazz+" JavaProperty:"+getName();
	}
}
