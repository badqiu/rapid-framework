package cn.org.rapid_framework.generator.provider.db.sql.model;

import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;

public class SqlTest extends GeneratorTestCase {
	Sql sql = new Sql();
	
	public void setUp() {
		
	}
	
	public void test_getOperationResultClassName() {
		sql.setOperationResultClass("com.badqiu.User");
		sql.setOperationParameterClass("com.badqiu.UserParam");
		assertEquals("User",sql.getOperationResultClassName());
		assertEquals("UserParam",sql.getOperationParameterClassName());
	}
	
	public void test_getOperationParameterClassName() {
		sql.setSourceSql("select * from user_info");
		sql.setExecuteSql("select * from user_info");
		sql.setOperation("findPage");
		assertEquals("FindPageResult",sql.getOperationResultClassName());
		assertEquals("FindPageQuery",sql.getOperationParameterClassName());
		
		sql.setSourceSql("delete from user_info");
		assertEquals("FindPageParameter",sql.getOperationParameterClassName());
	}
	
	public void test_getOperationResultClass() {
		Sql sql = SqlFactory.parseSql("select username from user_info");
		assertEquals("String",sql.getOperationResultClass());
		
		sql = SqlFactory.parseSql("select username,password from user_info");
		assertEquals("UserInfo",sql.getOperationResultClass());
		
		sql = SqlFactory.parseSql("select username as user,password as pwd from user_info");
		assertEquals("UserInfo",sql.getOperationResultClass());
		
		sql = SqlFactory.parseSql("select count(username) cnt_username,count(password) cnt_pwd from user_info");
		sql.setOperation("op1");
		assertEquals("Op1Result",sql.getOperationResultClass());
		
		sql.setOperationResultClass("test_by_set_result");
		assertEquals("test_by_set_result",sql.getOperationResultClass());
	}
	
	public void test_getOperationParameterClass() {
		sql = SqlFactory.parseSql("select count(username) cnt_username,count(password) cnt_pwd from user_info");
		sql.setOperation("findPage");
		assertEquals("FindPageQuery",sql.getOperationParameterClass());
		
		sql = SqlFactory.parseSql("insert into user_info(username) values (:username)");
		sql.setOperation("insertUsername");
		assertEquals("InsertUsernameParameter",sql.getOperationParameterClass());
		
		sql.setOperationParameterClass("set_by_user");
		assertEquals("set_by_user",sql.getOperationParameterClass());
		
		sql.setOperation(null);
		sql.setOperationParameterClass(null);
		assertEquals(null,sql.getOperationParameterClass());
	}
	
	public void test_getTableName() {
		sql = SqlFactory.parseSql("select count(username) cnt_username,count(password) cnt_pwd from user_info");
		assertNull(sql.getTableName());
		
		sql.setTableName("t1_abc_blog_123");
		assertEquals("t1_abc_blog_123",sql.getTableName());
		
		assertEquals("T1AbcBlog123",sql.getTableClassName());
	}
}
