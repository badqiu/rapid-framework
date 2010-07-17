package cn.org.rapid_framework.generator.provider.db.sql.model;

import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;

public class SqlTest extends GeneratorTestCase {
	Sql sql = new Sql();
	
	public void setUp() {
		
	}
	
	public void test_getOperationResultClassName() {
		sql.setResultClass("com.badqiu.User");
		sql.setParameterClass("com.badqiu.UserParam");
		assertEquals("User",sql.getResultClassName());
		assertEquals("UserParam",sql.getParameterClassName());
	}
	
	public void test_getOperationParameterClassName() {
		sql.setSourceSql("select * from user_info");
		sql.setExecuteSql("select * from user_info");
		sql.setOperation("findPage");
		assertEquals("FindPageResult",sql.getResultClassName());
		assertEquals("FindPageQuery",sql.getParameterClassName());
		
		sql.setSourceSql("delete from user_info");
		assertEquals("FindPageParameter",sql.getParameterClassName());
	}
	
	public void test_getOperationResultClass() {
		Sql sql = new SqlFactory().parseSql("select username from user_info");
		assertEquals("String",sql.getResultClass());
		
		sql = new SqlFactory().parseSql("select username,password from user_info");
		assertEquals("UserInfo",sql.getResultClass());
		
		sql = new SqlFactory().parseSql("select username as user,password as pwd from user_info");
		assertEquals("UserInfo",sql.getResultClass());
		
		sql = new SqlFactory().parseSql("select count(username) cnt_username,count(password) cnt_pwd from user_info");
		sql.setOperation("op1");
		assertEquals("Op1Result",sql.getResultClass());
		
		sql.setResultClass("test_by_set_result");
		assertEquals("test_by_set_result",sql.getResultClass());
	}
	
	public void test_getOperationParameterClass() {
		sql = new SqlFactory().parseSql("select count(username) cnt_username,count(password) cnt_pwd from user_info");
		sql.setOperation("findPage");
		assertEquals("FindPageQuery",sql.getParameterClass());
		
		sql = new SqlFactory().parseSql("insert into user_info(username) values (:username)");
		sql.setOperation("insertUsername");
		assertEquals("InsertUsernameParameter",sql.getParameterClass());
		
		sql.setParameterClass("set_by_user");
		assertEquals("set_by_user",sql.getParameterClass());
		
		sql.setOperation(null);
		sql.setParameterClass(null);
		assertEquals(null,sql.getParameterClass());
	}
	
	public void test_getTableName() {
		sql = new SqlFactory().parseSql("select count(username) cnt_username,count(password) cnt_pwd from user_info");
		assertNull(sql.getTableSqlName());
		
		sql.setTableSqlName("t1_abc_blog_123");
		assertEquals("t1_abc_blog_123",sql.getTableSqlName());
		
		assertEquals("T1AbcBlog123",sql.getTableClassName());
	}
	
	public void test_isColumnsInSameTable() {
		sql = new SqlFactory().parseSql("select username from user_info");
		assertTrue(sql.isColumnsInSameTable());
		
		sql = new SqlFactory().parseSql("select username,password from user_info");
		assertTrue(sql.isColumnsInSameTable());
		
		sql = new SqlFactory().parseSql("select username user,password pwd from user_info");
		assertTrue(sql.isColumnsInSameTable());
		
		sql = new SqlFactory().parseSql("select count(username) cnt_username,count(password) cnt_pwd from user_info");
		assertFalse(sql.isColumnsInSameTable());
		
		sql = new SqlFactory().parseSql("insert into user_info(username) values (:username)");
		assertFalse(sql.isColumnsInSameTable());
	}
}
