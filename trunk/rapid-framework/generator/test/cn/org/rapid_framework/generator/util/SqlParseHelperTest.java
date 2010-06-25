package cn.org.rapid_framework.generator.util;

import java.util.Set;

import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;

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
		
	    tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user,user_role,blog");
	    System.out.println(tableNames);
	    verifyTableNames(tableNames,"user","user_role","blog");
	}

    public void test_getTableNamesByQuery_with_multi_table2() {
        Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u,role r where abc=123");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user","role");
        
        tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u,role r,blog b");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user","role","blog");
    }
	   
    public void test_getTableNamesByQuery_with_join() {
        Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u left join role r");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user","role");
        
        tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u inner join role r");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user","role");
        
        tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u left join role r");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user","role");
    }
    
    public void test_verify_delete() {
    	Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("delete from user_Info where username=:abc");
    	 verifyTableNames(tableNames,"user_Info");
    }

    public void test_verify_update() {
    	Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("update user_info set username = :username where password = :password and age=:age and sex=:sex");
    	verifyTableNames(tableNames,"user_info");
    }

    public void test_verify_insert() {
    	Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("insert into user_info values(:username,:password,:age,:sex,:userid,:blog)");
    	verifyTableNames(tableNames,"user_info");
    }
    
	private void verifyTableNames(Set<String> tableNames,String... expectedTableNames) {
		for(int i = 0; i < expectedTableNames.length; i++) {
			assertTrue("actual tableNames:"+tableNames.toString(),tableNames.contains(expectedTableNames[i]));
		}
	}
	
	public void test_get_sql() {
	    String t = SqlParseHelper.getParameterClassName("select * from user where username = :username|Integer and pwd = :pwd|SexEnum", "username");
	    assertEquals(t,"Integer");
	    
	    t = SqlParseHelper.getParameterClassName("select * from user where username = :username|Integer and pwd = :pwd|SexEnum", "pwd");
	    assertEquals(t,"SexEnum");
	}
	
}
