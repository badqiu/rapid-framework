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
	
	public void test_isHasDefaultConstructor() {
        JavaClass c = new JavaClass(JavaClass.class);
        assertFalse(c.isHasDefaultConstructor());
        
        assertFalse(c.isHasDefaultConstructor());
        assertTrue(new JavaClass(Object.class).isHasDefaultConstructor());
    }
	
	public void test_getMavenJavaTestSourceFile() {
	    assertEquals("src/test/java/abc.java",JavaClass.getMavenJavaTestSourceFile("target/classes/abc.class"));
	    assertEquals("c:/src/test/java/abc.java",JavaClass.getMavenJavaTestSourceFile("c:/target\\classes/abc.class"));
	    assertEquals("c:/rapid/src/test/java/abc.java",JavaClass.getMavenJavaTestSourceFile("c:/rapid/target\\classes/abc.class"));
	}
}
