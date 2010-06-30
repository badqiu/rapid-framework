/**
 * project:pomer
 * 
 * Copyright 2008 [pomer], Inc. All rights reserved.
 * Website: http://www.pomer.org.cn/
 * 
 */
package cn.org.rapid_framework.generator.provider.java.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.org.rapid_framework.generator.util.StringHelper;

/**
 * 
 * @author badqiu,Linlin Yu
 */
public class JavaMethod {
	Method method;
	private JavaClass clazz; //与method相关联的class
	
	
	public JavaMethod(Method method, JavaClass clazz) {
		super();
		this.method = method;
		this.clazz = clazz;
	}

	public JavaClass getClazz() {
		return clazz;
	}

	public String getMethodName() {
		return method.getName();
	}

	public JavaClass getReturnType() {
		return new JavaClass(method.getReturnType());
	}

	public Annotation[] getAnnotations() {
		return method.getAnnotations();
	}

	public boolean isBridge() {
		return method.isBridge();
	}

	public boolean isSynthetic() {
		return method.isSynthetic();
	}

	public boolean isVarArgs() {
		return method.isVarArgs();
	}

	public List<MethodParameter> getParameters() {
		Class[] parameters  = method.getParameterTypes();
		List<MethodParameter> results = new ArrayList<MethodParameter>();
		for(int i = 0; i < parameters.length; i++) {
			results.add(new MethodParameter(i+1,this,new JavaClass(parameters[i])));
		}
		return results;
	}
	
	public String getMethodNameUpper() {
		return StringHelper.capitalize(getMethodName());
	}
	
	public String toString() {
		return "JavaClass:"+clazz+" JavaMethod:"+getMethodName();
	}
}
