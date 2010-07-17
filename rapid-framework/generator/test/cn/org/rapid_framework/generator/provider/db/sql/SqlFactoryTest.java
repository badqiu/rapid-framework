package cn.org.rapid_framework.generator.provider.db.sql;

import java.sql.SQLException;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;


public class SqlFactoryTest extends TestCase {
	SqlFactory parser = new SqlFactory();
	public void test_select() throws SQLException, Exception {
		Sql sql = parser.parseSql0("select * from user_info where user_id = ? and age = ? and password = ? and username like ? or (sex >= ?)");
		verifyParameters(sql,"userId","username","password","age","sex");
	}
	public void test_select_willcard() throws SQLException, Exception {
		Sql sql = parser.parseSql0("select * from user_info where user_id = ? and username = :username");
		verifyParameters(sql,"userId","username");
		String expected = "select USER_ID,USERNAME,PASSWORD,BIRTH_DATE,SEX,AGE from user_info where user_id = #userId# and username = :username";
		assertStringEquals(expected,sql.getIbatisSql());
	}
	
	public void test_escaped() throws SQLException, Exception {
		Sql sql = parser.parseSql0("select * from user_info where user_id > ? and username < :username");
		verifyParameters(sql,"userId","username");
		assertStringEquals("select USER_ID,USERNAME,PASSWORD,BIRTH_DATE,SEX,AGE from user_info where user_id > #userId# and username < :username",sql.getIbatisSql());
		assertStringEquals("select USER_ID,USERNAME,PASSWORD,BIRTH_DATE,SEX,AGE from user_info where user_id > #{userId} and username < :username",sql.getIbatis3Sql());
	}

	public void test_sql_function() throws SQLException, Exception {
		Sql sql = parser.parseSql0("select * from user_info where username = lower(#userId#) and username != :username");
		verifyParameters(sql,"userId","username");
		
		sql = parser.parseSql0("select * from user_info where username = lower(?) and username != :username");
		verifyNoParameters(sql,"userId");
		verifyParameters(sql,"username");
	}
	
	public void test_insert() throws SQLException, Exception {
		Sql sql = parser.parseSql0("insert into user_info(username,password) values (?,?)");
		verifyParameters(sql,"username","password");
	}
	public void test_delete() throws SQLException, Exception {
		Sql sql = parser.parseSql0("delete from user_info where username = ? and password = ? and age = ? or (sex >= ?)");
		verifyParameters(sql,"username","password","age","sex");
		assertEquals("delete from user_info where username = #username# and password = #password# and age = #age# or (sex >= #sex#)",sql.getIbatisSql());
		assertEquals("delete from user_info where username = #{username} and password = #{password} and age = #{age} or (sex >= #{sex})",sql.getIbatis3Sql());
		assertEquals("delete from user_info where username = :username and password = :password and age = :age or (sex >= :sex)",sql.getSpringJdbcSql());
		assertEquals("delete from user_info where username = :username and password = :password and age = :age or (sex >= :sex)",sql.getHql());
	}
	public void test_update() throws SQLException, Exception {
		Sql sql = parser.parseSql0("update user_info set username = ? , password = ? , age = ? , sex = ?");
		verifyParameters(sql,"username","password","age","sex");
		assertEquals("update user_info set username = #username# , password = #password# , age = #age# , sex = #sex#",sql.getIbatisSql());
	}
	
	private void assertStringEquals(String expected, String str) {
		assertEquals(expected.toLowerCase().replaceAll("\\s", ""),str.toLowerCase().replaceAll("\\s", ""));
	}
	private void verifyParameters(Sql sql, String... expectedParameters) {
		for(String param : expectedParameters) {
			assertNotNull("not found param:"+param+" on sql:"+sql.getSourceSql(),sql.getParam(param));
		}
	}
	private void verifyNoParameters(Sql sql, String... expectedParameters) {
		for(String param : expectedParameters) {
			assertNull("not found param:"+param+" on sql:"+sql.getSourceSql(),sql.getParam(param));
		}
	}
}
