package cn.org.rapid_framework.generator.provider.sql.model;

import junit.framework.TestCase;

public class SqlTest extends TestCase {
	
	public void test() {
		assertTrue(newSql("update user_info set password=:pwd").isUpdateSql());
		assertTrue(newSql("UPDATE user_info set password=:pwd").isUpdateSql());
		
		assertTrue(newSql("select * from user_info").isSelectSql());
		assertTrue(newSql("select * FROM user_info").isSelectSql());
		
		assertTrue(newSql("delete from username where abc=:diy").isDeleteSql());
		assertTrue(newSql("delete FrOM username where abc=:diy").isDeleteSql());
		
		assertTrue(newSql("inSert InTO user_info values(:pwd)").isInsertSql());
	}

	private Sql newSql(String string) {
		Sql sql = new Sql();
		sql.setSourceSql(string);
		return sql;
	}
}
