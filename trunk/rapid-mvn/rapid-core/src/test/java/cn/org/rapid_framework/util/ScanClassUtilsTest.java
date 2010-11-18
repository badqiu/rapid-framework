package cn.org.rapid_framework.util;

import java.util.List;

import junit.framework.TestCase;

public class ScanClassUtilsTest extends TestCase {
	
	public void testScanPackages() {
		List clazzes = null;
		try {
			clazzes = ScanClassUtils.scanPackages(null);
			fail();
		}catch(Exception e) {
			assertTrue(true);
		}
		
		clazzes = ScanClassUtils.scanPackages("cn.org.rapid_framework.util");
		assertFalse(clazzes.isEmpty());
		assertTrue(contains(clazzes, "cn.org.rapid_framework.util"));
		System.out.println(clazzes);
		
		clazzes = ScanClassUtils.scanPackages("cn.org.rapid_framework.util.**.*");
		assertFalse(clazzes.isEmpty());
		assertTrue(contains(clazzes, "cn.org.rapid_framework.util"));
		assertTrue(contains(clazzes, "cn.org.rapid_framework.util.fortest"));
		System.out.println(clazzes);
		
		clazzes = ScanClassUtils.scanPackages("javacommon.**.*,cn.org.*_framework");
		assertFalse(clazzes.isEmpty());
		assertTrue(contains(clazzes, "cn.org.rapid_framework"));
		System.out.println(clazzes);
	}
	
	public boolean contains(List<String> clazzes,String containString) {
		for(String s : clazzes) {
			if(s.contains(containString)) {
				return true;
			}
		}
		return false;
	}
	
}
