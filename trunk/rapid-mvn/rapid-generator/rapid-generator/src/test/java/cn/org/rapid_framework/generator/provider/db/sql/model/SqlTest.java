package cn.org.rapid_framework.generator.provider.db.sql.model;

import java.sql.SQLException;

import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;

public class SqlTest extends GeneratorTestCase {
	Sql sql = new Sql();
	
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
	
	public void test_getOperationResultClass() throws SQLException {
		Sql sql = new SqlFactory().parseSql("select username from user_info");
		assertEquals("String",sql.getResultClass());
		
		sql = new SqlFactory().parseSql("select username,password from user_info");
		assertEquals("UserInfo",sql.getResultClass());
		
		sql = new SqlFactory().parseSql("select username as user,password as pwd from user_info");
		sql.setOperation("findUsername");
		assertEquals("FindUsernameResult",sql.getResultClass());
		
		sql = new SqlFactory().parseSql("select username as user,count(password) as pwd from user_info group by username");
		sql.setOperation("findUsername");
		assertEquals("FindUsernameResult",sql.getResultClass());
		
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
		
		sql = new SqlFactory().parseSql("insert into user_info(user_id,username) values (:userId,:username)");
		sql.setOperation("insertUsername");
		assertEquals("InsertUsernameParameter",sql.getParameterClass());
		
		sql.setParameterClass("set_by_user");
		assertEquals("set_by_user",sql.getParameterClass());
		
		sql.setOperation(null);
		sql.setParameterClass(null);
		assertEquals(null,sql.getParameterClass());
	}
	
//	public void test_getTableName() {
//		sql = new SqlFactory().parseSql("select count(username) cnt_username,count(password) cnt_pwd from user_info");
//		assertNull(sql.getTableSqlName());
//		
//		sql.setTableSqlName("t1_abc_blog_123");
//		assertEquals("t1_abc_blog_123",sql.getTableSqlName());
//		
//		assertEquals("T1AbcBlog123",sql.getTableClassName());
//	}
	
	public void test_isColumnsInSameTable() {
		sql = new SqlFactory().parseSql("select username from user_info");
		assertTrue(sql.isColumnsInSameTable());
		
		sql = new SqlFactory().parseSql("select username,password from user_info");
		assertTrue(sql.isColumnsInSameTable());
		
		//FIXME 应该为true,是同一张表
		sql = new SqlFactory().parseSql("select username user,password pwd from user_info");
		assertFalse(sql.isColumnsInSameTable());
		
		sql = new SqlFactory().parseSql("select count(username) cnt_username,count(password) cnt_pwd from user_info");
		assertFalse(sql.isColumnsInSameTable());
		
		sql = new SqlFactory().parseSql("insert into user_info(username,user_id) values (:username,?)");
		assertFalse(sql.isColumnsInSameTable());
	}
	
	public void test_get_result_class() {
		Sql sql = new SqlFactory().parseSql("select * from user_info /* inner join from blogjava */ ");
		assertEquals("UserInfo",sql.getResultClass());
	}
	
	/** 测试聚集函数colum名称自动转换,示例转换 count(*) => count, max(age) => max_age, sum(income) => sum_income */
	public void test_intergate_function_name_convert() {
	    sql = new SqlFactory().parseSql("select count(*) cnt, count(username) count_username,max(password) max_password,min(password) min_password,avg(sex) avg_sex from user_info");
        String msg = sql.getColumns().toString();
		assertNotNull(msg,sql.getColumnByName("Cnt"));
        assertNotNull(msg,sql.getColumnByName("count_username"));
        assertNotNull(msg,sql.getColumnByName("max_password"));
        assertNotNull(msg,sql.getColumnByName("min_password"));
        assertNotNull(msg,sql.getColumnByName("avg_sex"));
        
        //FIXME 试聚集函数colum名称自动转换,示例转换 count(*) => count, max(age) => max_age, sum(income) => sum_income
        sql = new SqlFactory().parseSql("select count(*) cnt, count(username),max(password),min(password),avg(sex) from user_info");
        msg = sql.getColumns().toString();
		assertNotNull(msg,sql.getColumnByName("Cnt"));
//        assertNotNull(msg,sql.getColumnByName("C2"));
//        assertNotNull(msg,sql.getColumnByName("C3"));
//        assertNotNull(msg,sql.getColumnByName("C4"));
//        assertNotNull(msg,sql.getColumnByName("C5"));
	}

}
