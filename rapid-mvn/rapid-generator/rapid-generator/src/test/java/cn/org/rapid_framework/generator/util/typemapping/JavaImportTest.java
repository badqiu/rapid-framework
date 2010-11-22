package cn.org.rapid_framework.generator.util.typemapping;

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
}
