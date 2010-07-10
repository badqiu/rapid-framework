package cn.org.rapid_framework.jdbc.sqlgenerator;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.org.rapid_framework.jdbc.sqlgenerator.metadata.Column;
import cn.org.rapid_framework.jdbc.sqlgenerator.metadata.Table;


public class SpringNamedSqlGeneratorTest {
 
	Table table = new Table("user",new Column("user_id","userId",true),new Column("user_name","userName"),new Column("pwd","pwd"));
	SpringNamedSqlGenerator t = new SpringNamedSqlGenerator(table);
	
	Table multiKeyTable = new Table("user",new Column("user_id","userId",true),new Column("group_id","groupId",true),new Column("user_name","userName"),new Column("pwd","pwd"));
	SpringNamedSqlGenerator multiKeySqlGenerator = new SpringNamedSqlGenerator(multiKeyTable);
	@Test
	public void insertSql() {
		System.out.println(t.getInsertSql());
		assertEquals("INSERT INTO user (user_id,user_name,pwd ) VALUES ( :userId,:userName,:pwd ) ", t.getInsertSql());
	}
	
	@Test
	public void updateSql() {
		System.out.println(t.getUpdateByPkSql());
		assertEquals("UPDATE user SET user_name = :userName,pwd = :pwd WHERE user_id = :userId", t.getUpdateByPkSql());
		assertEquals("UPDATE user SET user_name = :userName,pwd = :pwd WHERE user_id = :userId AND group_id = :groupId", multiKeySqlGenerator.getUpdateByPkSql());
		
		assertEquals("UPDATE user SET user_name = :userName,pwd = :pwd WHERE user_id = :userId AND group_id = :groupId", multiKeySqlGenerator.getUpdateByPkSql());

	}
	
	@Test
	public void deleteSql() {
		System.out.println(t.getDeleteByMultiPkSql());
		assertEquals("DELETE FROM user WHERE user_id = :userId", t.getDeleteByMultiPkSql());
		
		assertEquals("DELETE FROM user WHERE user_id = :userId AND group_id = :groupId", multiKeySqlGenerator.getDeleteByMultiPkSql());
	}
	
	@Test
	public void getDeleteBySinglePkSql() {
		System.out.println(t.getDeleteBySinglePkSql());
		assertEquals("DELETE FROM user WHERE user_id = ?", t.getDeleteBySinglePkSql());
		
		try {
			multiKeySqlGenerator.getDeleteBySinglePkSql();
			fail();
		}catch(IllegalStateException espected) {
			assertNotNull(espected);
		}
	}
	
	@Test
	public void getSelectByPrimaryKeysSql() {
		System.out.println(t.getSelectByMultiPkSql());
		assertEquals("SELECT user_id userId,user_name userName,pwd pwd FROM user WHERE user_id = :userId", t.getSelectByMultiPkSql());
		
		assertEquals("SELECT user_id userId,group_id groupId,user_name userName,pwd pwd FROM user WHERE user_id = :userId AND group_id = :groupId", multiKeySqlGenerator.getSelectByMultiPkSql());
	}
	
	@Test
	public void getSelectBySinglePkSql() {
		System.out.println(t.getSelectBySinglePkSql());
		assertEquals("SELECT user_id userId,user_name userName,pwd pwd FROM user WHERE user_id = ?", t.getSelectBySinglePkSql());
		
		try {
			multiKeySqlGenerator.getSelectBySinglePkSql();
			fail();
		}catch(IllegalStateException espected) {
			assertNotNull(espected);
		}
	}
	
	@Test
	public void getColumnsSql() {
		System.out.println(t.getColumnsSql());
		assertEquals("user_id userId,user_name userName,pwd pwd", t.getColumnsSql());
	}
	
}
