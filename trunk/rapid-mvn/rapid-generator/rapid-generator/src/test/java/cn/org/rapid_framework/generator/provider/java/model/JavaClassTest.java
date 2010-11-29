package cn.org.rapid_framework.generator.provider.java.model;

import java.lang.reflect.Method;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.Generator;
import cn.org.rapid_framework.generator.provider.java.model.JavaClass.MavenHelper;
import cn.org.rapid_framework.generator.provider.java.model.testservicebean.BlogServiceBean;

public class JavaClassTest extends TestCase {

	public void test() {
		JavaClass c = new JavaClass(JavaClass.class);
		System.out.println(c.getClassFile());
		assertEquals(c.getClassName(),"JavaClass");
		for(Method m : JavaClass.class.getMethods()) {
			System.out.println(m.getDeclaringClass().getName());
		}
	}
	
	public void test_getClassName() {
	    assertEquals(JavaClass.getClassName("User$Blog"),"Blog");
	}
	
	public void test_isHasDefaultConstructor() {
        JavaClass c = new JavaClass(JavaClass.class);
        assertFalse(c.isHasDefaultConstructor());
        
        assertFalse(c.isHasDefaultConstructor());
        assertTrue(new JavaClass(Object.class).isHasDefaultConstructor());
        
    }
	
	public void test_getMavenJavaTestSourceFile() {
	    assertEquals("src/test/java/abcTest.java",MavenHelper.getMavenJavaTestSourceFile("target/classes/abc.class"));
	    assertEquals("src/test/java/abcTest.java",MavenHelper.getMavenJavaTestSourceFile("target/test-classes/abc.class"));
	    assertEquals("c:/src/test/java/abcTest.java",MavenHelper.getMavenJavaTestSourceFile("c:/target\\classes/abc.class"));
	    assertEquals("c:/rapid/src/test/java/abcTest.java",MavenHelper.getMavenJavaTestSourceFile("c:/rapid/target\\classes/abc.class"));
	    
	    assertEquals(null,MavenHelper.getMavenJavaTestSourceFile(""));
	    assertEquals(null,MavenHelper.getMavenJavaTestSourceFile(null));
	    assertEquals(null,MavenHelper.getMavenJavaTestSourceFile("t1arget/classes/abc.class"));
	}
	
	public void test_getMavenJavaSourceFile() {
	    assertEquals("src/main/java/abc.java",MavenHelper.getMavenJavaSourceFile("target/classes/abc.class"));
	    assertEquals("c:/src/main/java/abc.java",MavenHelper.getMavenJavaSourceFile("c:/target\\classes/abc.class"));
	    assertEquals("c:/rapid/src/main/java/abc.java",MavenHelper.getMavenJavaSourceFile("c:/rapid/target\\classes/abc.class"));
	    
	    assertEquals(null,MavenHelper.getMavenJavaSourceFile(""));
	    assertEquals(null,MavenHelper.getMavenJavaSourceFile(null));
	    assertEquals(null,MavenHelper.getMavenJavaSourceFile("t1arget/classes/abc.class"));
	}
	
	public void test_getMavenJavaSourceFileContent() {
	    String content = new JavaClass(Generator.class).getMavenJavaSourceFileContent();
		assertTrue(content.indexOf("package cn.org.rapid_framework") >=0);
		
		content = new JavaClass(BlogServiceBean.class).getMavenJavaSourceFileContent();
		assertTrue(content.indexOf("package cn.org.rapid_framework") >=0);
	}
}
