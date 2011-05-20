package cn.org.rapid_framework.generator.util.typemapping;

import java.util.HashSet;

import junit.framework.TestCase;

public class JavaImportTest extends TestCase {
	
	public void test() {
		JavaImport j = new JavaImport();
		assertFalse(j.isNeedImport("Long"));
		assertFalse(j.isNeedImport("Double"));
		assertFalse(j.isNeedImport("Long"));
		assertFalse(j.isNeedImport("java.lang.Long"));
		assertFalse(j.isNeedImport("int"));
		assertFalse(j.isNeedImport("long"));
		assertFalse(j.isNeedImport("double"));
		
		assertFalse(j.isNeedImport("Blog"));
		assertFalse(j.isNeedImport("Hibernate"));
		
		assertTrue(j.isNeedImport("xxxx.blog.User"));
		assertTrue(j.isNeedImport("aaa.hibern.Blog"));
	}
	
	public void test2() {
	    JavaImport javaImport = new JavaImport();
        javaImport.addImport("abc.diy.User$Info");
        assertEquals(javaImport.getImports().iterator().next(),"abc.diy.User.Info");
	}
	
	public void test2_addImportClass() {
	    HashSet set = new HashSet();
        JavaImport.addImportClass(set,PriviateClass.class );
        assertTrue(set.isEmpty());
        
        JavaImport.addImportClass(set,PackageClass.class );
        assertTrue(set.isEmpty());
        
        JavaImport.addImportClass(set,PublicStaticClass.class );
        assertFalse(set.isEmpty());
    }
	
	private static class PriviateClass {
	}
	
	class PackageClass {
	}
	
	public static class PublicStaticClass{}
}
