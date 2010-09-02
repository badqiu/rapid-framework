package cn.org.rapid_framework.generator.util.typemapping;

import junit.framework.TestCase;

public class JavaImportTest extends TestCase {
	
	public void test() {
		JavaImport j = new JavaImport();
		assertFalse(j.isNeedImport("Long"));
		assertFalse(j.isNeedImport("Double"));
		assertFalse(j.isNeedImport("Long"));
		assertFalse(j.isNeedImport("java.lang.Long"));
	}
}
