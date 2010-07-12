package cn.org.rapid_framework.generator.provider.java.model;

import java.lang.reflect.Method;

import junit.framework.TestCase;

public class JavaClassTest extends TestCase {

	public void test() {
		JavaClass c = new JavaClass(JavaClass.class);
		assertEquals(c.getClassName(),"JavaClass");
		for(Method m : JavaClass.class.getMethods()) {
			System.out.println(m.getDeclaringClass().getName());
		}
	}
	
}
