package cn.org.rapid_framework.generator.util;

import java.util.List;

import junit.framework.TestCase;


public class SqlParseHelperTest extends TestCase{
	
	public void test_getTableNamesByQuery_with_single_table() {
		List<String> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user");
		System.out.println(tableNames);
		verifyTableNames(tableNames,"user");
	}
	
	public void test_getTableNamesByQuery_with_multi_table() {
		List<String> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user,role");
		System.out.println(tableNames);
		verifyTableNames(tableNames,"user","role");
	}

	private void verifyTableNames(List<String> tableNames,String... expectedTableNames) {
		
		for(int i = 0; i < expectedTableNames.length; i++) {
			assertEquals(expectedTableNames[i],tableNames.get(i));
		}
//		assertEquals("user",tableNames.get(0));
//		assertEquals("role",tableNames.get(1));
	}
	
}
