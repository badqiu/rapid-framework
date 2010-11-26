package cn.org.rapid_framework.generator.provider.java.model;

import java.lang.reflect.Method;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.Generator;

public class JavaClassTest extends TestCase {

	public void test() {
		JavaClass c = new JavaClass(JavaClass.class);
		System.out.println(c.getClassFile());
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
	    assertEquals("src/test/java/abcTest.java",JavaClass.getMavenJavaTestSourceFile("target/classes/abc.class"));
	    assertEquals("c:/src/test/java/abcTest.java",JavaClass.getMavenJavaTestSourceFile("c:/target\\classes/abc.class"));
	    assertEquals("c:/rapid/src/test/java/abcTest.java",JavaClass.getMavenJavaTestSourceFile("c:/rapid/target\\classes/abc.class"));
	    
	    assertEquals(null,JavaClass.getMavenJavaTestSourceFile(""));
	    assertEquals(null,JavaClass.getMavenJavaTestSourceFile(null));
	    assertEquals(null,JavaClass.getMavenJavaTestSourceFile("t1arget/classes/abc.class"));
	}
	
	public void test_getMavenJavaSourceFile() {
	    assertEquals("src/main/java/abc.java",JavaClass.getMavenJavaSourceFile("target/classes/abc.class"));
	    assertEquals("c:/src/main/java/abc.java",JavaClass.getMavenJavaSourceFile("c:/target\\classes/abc.class"));
	    assertEquals("c:/rapid/src/main/java/abc.java",JavaClass.getMavenJavaSourceFile("c:/rapid/target\\classes/abc.class"));
	    
	    assertEquals(null,JavaClass.getMavenJavaSourceFile(""));
	    assertEquals(null,JavaClass.getMavenJavaSourceFile(null));
	    assertEquals(null,JavaClass.getMavenJavaSourceFile("t1arget/classes/abc.class"));
	}
	
	public void test_getMavenJavaSourceFileContent() {
	    assertEquals("src/main/java/abc.java",new JavaClass(Generator.class).getMavenJavaSourceFileContent());
	}
}
