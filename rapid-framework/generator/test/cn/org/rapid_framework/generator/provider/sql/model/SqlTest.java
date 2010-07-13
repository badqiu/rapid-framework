package cn.org.rapid_framework.generator.provider.sql.model;

import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import junit.framework.TestCase;

public class SqlTest extends TestCase {
	
	public void test() {
		assertTrue(newSql("update user_info set password=:pwd").isUpdateSql());
		assertTrue(newSql("UPDATE user_info set password=:pwd").isUpdateSql());
		
		assertTrue(newSql("select * from user_info").isSelectSql());
		assertTrue(newSql("select * FROM user_info").isSelectSql());
		assertTrue(newSql("select * FROM user_info").isSelectSql());
		
		assertTrue(newSql("delete from username where abc=:diy").isDeleteSql());
		assertTrue(newSql("delete FrOM username where abc=:diy").isDeleteSql());
		
		assertTrue(newSql(" \n\t\t\t\tINSERT INTO user_info (username,password) values (#username#,#password#)\n\t\t ").isInsertSql());
		assertTrue(newSql("inSert InTO user_info values(:pwd)").isInsertSql());
	}
	
	public void test_is_update() {
	    assertTrue(newSql("\n\n  UPDATE user_info set password=:pwd").isUpdateSql());
	    assertTrue(newSql("\n\n \t UPDATE user_info SET username = ?,password = ? WHERE username = ?").isUpdateSql());
	    assertTrue(newSql("UPDATE user_info SET username = #username#,password = #password# WHERE username = #username#").isUpdateSql());
	    assertTrue(newSql("update user_info set username = #username#,password = #password#").isUpdateSql());
	}

	private Sql newSql(String string) {
		Sql sql = new Sql();
		sql.setSourceSql(string);
		return sql;
	}
}
