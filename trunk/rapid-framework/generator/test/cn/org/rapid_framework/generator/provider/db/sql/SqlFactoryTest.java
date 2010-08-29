package cn.org.rapid_framework.generator.provider.db.sql;

import java.sql.SQLException;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;


public class SqlFactoryTest extends TestCase {
	SqlFactory parser = new SqlFactory();

	public void test_isMatchListParam() {
	    String sql = "length(#username#) and in \n (#pwd#) and not \n in \n (#user#) and blog = #blog[]# and sex = #sex[].value#";
        assertFalse(new SqlFactory().new SqlParametersParser().isMatchListParam(sql, "username"));
        assertFalse(new SqlFactory().new SqlParametersParser().isMatchListParam(sql, "notexist"));
        assertFalse(new SqlFactory().new SqlParametersParser().isMatchListParam(sql, "in"));
        assertFalse(new SqlFactory().new SqlParametersParser().isMatchListParam(sql, "not in"));
        assertTrue(new SqlFactory().new SqlParametersParser().isMatchListParam(sql, "pwd"));
        assertTrue(new SqlFactory().new SqlParametersParser().isMatchListParam(sql, "user"));
        assertTrue(new SqlFactory().new SqlParametersParser().isMatchListParam(sql, "blog"));
        assertTrue(new SqlFactory().new SqlParametersParser().isMatchListParam(sql, "sex"));
	}
	
	public void test_union() throws SQLException, Exception {
		String query = "select * from user_info where user_id = ? and age = ? and password = ? and username like ? or (sex >= ?) ";
		String orderByQuery = "select * from user_info where user_id = ? and age = ? and password = ? and username like ? or (sex >= ?) order by :orderby ";
		Sql sql = parser.parseSql(query+" union " + query);
		verifyParameters(sql,"userId","username","password","age","sex");
		
		sql = parser.parseSql(orderByQuery+" union " + orderByQuery);
		verifyParameters(sql,"userId","username","password","age","sex");
	}
	
	public void test_order_by() throws SQLException, Exception {
		Sql sql = parser.parseSql("select * from user_info where user_id = ? and age = ? and password = ? and username like ? or (sex >= ?) order by :orderby :asc_desc");
		verifyParameters(sql,"userId","username","password","age","sex");
	}
	public void test_unscaped_xml() throws SQLException, Exception {
		try {
			Sql sql = parser.parseSql("select * from user_info where user_id &lt; :user_id");
			fail();
		}catch(RuntimeException e) {
		}
	}
	
	public void test_select() throws SQLException, Exception {
		Sql sql = parser.parseSql("select * from user_info where user_id = ? and age = ? and password = ? and username like ? or (sex >= ?)");
		verifyParameters(sql,"userId","username","password","age","sex");
		verifyColumns(sql,"userId","username","password","BIRTH_DATE","age","sex");
	}
	
	public void test_聚合函数() throws SQLException, Exception {
		Sql sql = parser.parseSql("select DISTINCT(count(*)) DISTINCT_count,count(username) cnt_username,sum(age) sum_age,avg(sex) avg_sex from user_info where user_id = ? and age = ? and password = ? and username like ? or (sex >= ?)");
		verifyParameters(sql,"userId","username","password","age","sex");
		verifyColumns(sql,"DISTINCT_count","cnt_username","sum_age","avg_sex");
	}

	public void test_聚合函数2() throws SQLException, Exception {
		Sql sql = parser.parseSql("select DISTINCT username from user_info where user_id = ? and age = ? and password = ? and username like ? or (sex >= ?)");
		verifyParameters(sql,"userId","username","password","age","sex");
		verifyColumns(sql,"username");
	}

	public void test_group_by() throws SQLException, Exception {
		Sql sql = parser.parseSql("select DISTINCT username from user_info where user_id = ? and age = ? and password = ? and username like ? or (sex >= ?) group by username");
		verifyParameters(sql,"userId","username","password","age","sex");
		verifyColumns(sql,"username");
	}

	public void test_join() throws SQLException, Exception {
		Sql sql = parser.parseSql("select DISTINCT t1.username from user_info t1 inner join user_info t2 on t1.user_id=t2.user_id where t1.user_id = ? and t1.age = ? and t1.password = ? and t1.username like ? or (t1.sex >= ?) group by username");
		verifyParameters(sql,"userId","username","password","age","sex");
		verifyColumns(sql,"username");
	}

	public void test_select_with_table_alias() throws SQLException, Exception {
		Sql sql = parser.parseSql("select * from user_info t where t.user_id = ? and t.age = ? and t.password = ? and t.username like ? or (t.sex >= ?)");
		verifyParameters(sql,"userId","username","password","age","sex");
		verifyColumns(sql,"userId","username","password","BIRTH_DATE","age","sex");
	}
	
	public void test_group_by_having() throws SQLException, Exception {
		Sql sql = parser.parseSql("select sum(age) sum_age from user_info where user_id = ? and age = ? and password = ? and username like ? or (sex >= ?) group by username having sum(age) = :havingUsername");
		verifyParameters(sql,"userId","username","password","age","sex","havingUsername");
		verifyColumns(sql,"sum_age");
	}
	
	public void test_select_willcard() throws SQLException, Exception {
		Sql sql = parser.parseSql("select * from user_info where user_id = ? and username = :username");
		verifyParameters(sql,"userId","username");
		String expected = "select USER_ID,USERNAME,PASSWORD,BIRTH_DATE,SEX,AGE from user_info where user_id = #userId# and username = :username";
		assertStringEquals(expected,sql.getIbatisSql());
	}

	public void test_select_willcard_multi_table() throws SQLException, Exception {
		Sql sql = parser.parseSql("select t1.*,t2.* from user_info t1 inner join role t2 on t1.username=t2.role_name where t1.user_id = ? and t2.role_name = :role_name");
		verifyParameters(sql,"userId","role_name");
		String expected = "select t1.USER_ID,t1.USERNAME,t1.PASSWORD,t1.BIRTH_DATE,t1.SEX,t1.AGE,t2.USER_ID,t2.USERNAME,t2.PASSWORD,t2.BIRTH_DATE,t2.SEX,t2.AGE from user_info where user_id = #userId# and username = :username";
		assertStringEquals(expected,sql.getIbatisSql());
	}
	
	public void test_escaped() throws SQLException, Exception {
		Sql sql = parser.parseSql("select * from user_info where user_id > ? and username < :username");
		verifyParameters(sql,"userId","username");
		assertStringEquals("select USER_ID,USERNAME,PASSWORD,BIRTH_DATE,SEX,AGE from user_info where user_id > #userId# and username < :username",sql.getIbatisSql());
		assertStringEquals("select USER_ID,USERNAME,PASSWORD,BIRTH_DATE,SEX,AGE from user_info where user_id > #{userId} and username < :username",sql.getIbatis3Sql());
	}

	public void test_sql_function() throws SQLException, Exception {
		Sql sql = parser.parseSql("select * from user_info where username = lower(#userId#) and username != :username");
		verifyParameters(sql,"userId","username");
		
		sql = parser.parseSql("select * from user_info where username = lower(?) and username != :username");
		verifyNoParameters(sql,"userId");
		verifyParameters(sql,"username");
	}
	
	public void test_insert() throws SQLException, Exception {
		Sql sql = parser.parseSql("insert into user_info(username,password) values (?,?)");
		verifyParameters(sql,"username","password");
	}
	public void test_delete() throws SQLException, Exception {
		Sql sql = parser.parseSql("delete from user_info where username = ? and password = ? and age = ? or (sex >= ?)");
		verifyParameters(sql,"username","password","age","sex");
		assertEquals("delete from user_info where username = #username# and password = #password# and age = #age# or (sex >= #sex#)",sql.getIbatisSql());
		assertEquals("delete from user_info where username = #{username} and password = #{password} and age = #{age} or (sex >= #{sex})",sql.getIbatis3Sql());
		assertEquals("delete from user_info where username = :username and password = :password and age = :age or (sex >= :sex)",sql.getSpringJdbcSql());
		assertEquals("delete from user_info where username = :username and password = :password and age = :age or (sex >= :sex)",sql.getHql());
	}
	public void test_update() throws SQLException, Exception {
		Sql sql = parser.parseSql("update user_info set username = ? , password = ? , age = ? , sex = ?");
		verifyParameters(sql,"username","password","age","sex");
		assertEquals("update user_info set username = #username# , password = #password# , age = #age# , sex = #sex#",sql.getIbatisSql());
	}
	
	private void assertStringEquals(String expected, String str) {
		if(expected.toLowerCase().replaceAll("\\s", "").equals(str.toLowerCase().replaceAll("\\s", ""))) {
		}else {
			assertEquals(expected,str);
		}
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
	private void verifyColumns(Sql sql, String... expectedColumns) {
		for(String name : expectedColumns) {
			assertNotNull("not found column:"+name+" on sql:"+sql.getSourceSql(),sql.getColumnByName(name));
		}
	}
}
