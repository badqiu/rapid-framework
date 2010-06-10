package cn.org.rapid_framework.generator.util;

import java.util.Set;

import junit.framework.TestCase;


public class SqlParseHelperTest extends TestCase{
	
	public void test_getTableNamesByQuery_with_single_table() {
		Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user");
		System.out.println(tableNames);
		verifyTableNames(tableNames,"user");
	}
	
	public void test_getTableNamesByQuery_with_multi_table() {
	    Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user,role");
		System.out.println(tableNames);
		verifyTableNames(tableNames,"user","role");
	}

	private void verifyTableNames(Set<String> tableNames,String... expectedTableNames) {
		
		for(int i = 0; i < expectedTableNames.length; i++) {
			assertTrue(tableNames.contains(expectedTableNames[i]));
		}
//		assertEquals("user",tableNames.get(0));
//		assertEquals("role",tableNames.get(1));
	}
	
}
