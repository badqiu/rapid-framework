package cn.org.rapid_framework.generator.provider.sql.model;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;

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
		assertTrue(newSql("INSERT INTO Store_Information (store_name, Sales, Date) SELECT store_name, Sales, Date FROM Sales_Information WHERE Year(Date) = 1998").isInsertSql());
		assertFalse(newSql("INSERT INTO Store_Information (store_name, Sales, Date) SELECT store_name, Sales, Date FROM Sales_Information WHERE Year(Date) = 1998").isSelectSql());
	}
	
	public void test_is_update() {
	    assertTrue(newSql("\n\n  UPDATE user_info set password=:pwd").isUpdateSql());
	    assertTrue(newSql("\n\n \t UPDATE user_info SET username = ?,password = ? WHERE username = ?").isUpdateSql());
	    assertTrue(newSql("UPDATE user_info SET username = #username#,password = #password# WHERE username = #username#").isUpdateSql());
	    assertTrue(newSql("update user_info set username = #username#,password = #password#").isUpdateSql());
	}
	
	public void test_setSqlMap_and_replase_cdata() {
	    Sql s = new Sql();
	    s.setSqlmap("${cdata-start} 123 ${cdata-end}");
	    assertEquals(s.getSqlmap(),"<![CDATA[ 123 ]]>");
	}
	
//	public void test_remove_table_prefix() {
//		GeneratorProperties.setProperty("tableRemovePrefixes", "t_,v_");
//		Sql sql = new Sql();
//		sql.setTableSqlName("t_user_info");
//		assertEquals("UserInfo",sql.getTableClassName());
//		sql.setTableSqlName("v_user");
//		assertEquals("User",sql.getTableClassName());
//		
//		sql.setTableSqlName("diy_user");
//		assertEquals("DiyUser",sql.getTableClassName());
//	}

	private Sql newSql(String string) {
		Sql sql = new Sql();
		sql.setSourceSql(string);
		return sql;
	}
}
