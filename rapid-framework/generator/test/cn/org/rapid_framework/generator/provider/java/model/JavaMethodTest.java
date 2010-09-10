package cn.org.rapid_framework.generator.provider.java.model;

import java.lang.reflect.Method;

import junit.framework.TestCase;

public class JavaMethodTest extends TestCase {
	
	public void test() throws Exception{
		Method method = String.class.getMethod("valueOf",Object.class);
		JavaMethod m = new JavaMethod(method,new JavaClass(String.class));
		System.out.println(m.getParameters());
		
		assertFalse(m.isPropertyMethod());
	}
	
	public void test_isPropertyMethod() throws Exception{
		Method method = JavaMethod.class.getMethod("isSynthetic");
		JavaMethod m = new JavaMethod(method,new JavaClass(JavaMethod.class));
		System.out.println(m.getParameters());
		
		assertTrue(m.isPropertyMethod());
		
		method = JavaMethodTest.class.getMethod("isVoidMethod");
		m = new JavaMethod(method,new JavaClass(JavaMethodTest.class));
		System.out.println(m.getParameters());
		
		assertFalse(m.isPropertyMethod());
	}
	
	public void isVoidMethod() {
		
	}
}
