package cn.org.rapid_framework.jdbc.sqlgenerator;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.org.rapid_framework.jdbc.sqlgenerator.metadata.Column;
import cn.org.rapid_framework.jdbc.sqlgenerator.metadata.Table;

public class SqlGeneratorTest {
	Table table = new Table("user",new Column("user_id","userId",true),new Column("user_name","userName"),new Column("pwd","pwd"));
	SqlGenerator singleGenerator = new SpringNamedSqlGenerator(table);

	Table multiKeyTable = new Table("user",new Column("user_id","userId",true),new Column("group_id","groupId",true),new Column("user_name","userName"),new Column("pwd","pwd"));
	SqlGenerator multiGenerator = new CacheSqlGenerator( new SpringNamedSqlGenerator(multiKeyTable));

	@Test
	public void getInsertSql(){
		assertEquals("INSERT user (user_id,user_name,pwd ) VALUES ( :userId,:userName,:pwd ) ",singleGenerator.getInsertSql());
		assertEquals("INSERT user (user_id,group_id,user_name,pwd ) VALUES ( :userId,:groupId,:userName,:pwd ) ",multiGenerator.getInsertSql());
	}
	@Test
	public void getUpdateByPkSql(){
		assertEquals("UPDATE user SET (user_id = :userId,user_name = :userName,pwd = :pwd ) WHERE user_id = ?",singleGenerator.getUpdateByPkSql());
		assertEquals("UPDATE user SET (user_id = :userId,group_id = :groupId,user_name = :userName,pwd = :pwd ) WHERE user_id = :userId AND group_id = :groupId",multiGenerator.getUpdateByPkSql());
	}
	@Test
	public void getDeleteByPkSql(){
		assertEquals("DELETE FROM user WHERE user_id = ?",singleGenerator.getDeleteByPkSql());
		assertEquals("DELETE FROM user WHERE user_id = :userId AND group_id = :groupId",multiGenerator.getDeleteByPkSql());
	}
	@Test
	public void getSelectByPkSql(){
		assertEquals("SELECT user_id userId,user_name userName,pwd pwd FROM user WHERE user_id = ?",singleGenerator.getSelectByPkSql());
		assertEquals("SELECT user_id userId,group_id groupId,user_name userName,pwd pwd FROM user WHERE user_id = :userId AND group_id = :groupId",multiGenerator.getSelectByPkSql());
	}
	@Test
	public void getColumnsSql(){
		assertEquals("user_id userId,user_name userName,pwd pwd",singleGenerator.getColumnsSql());
		assertEquals("user_id userId,group_id groupId,user_name userName,pwd pwd",multiGenerator.getColumnsSql());
	}
	
	Table t3 = new Table("user",new Column("user_id","userId",true),new Column("user_name","userName",false,true,false,false),new Column("pwd","pwd",false,false,true,false));
	SqlGenerator t3g = new SpringNamedSqlGenerator(t3);
	Table t4 = new Table("user",new Column("user_id","userId",true),new Column("group_id","groupId",true),new Column("user_name","userName",false,false,false,false));
	SqlGenerator t4g = new SpringNamedSqlGenerator(t4);
	@Test
	public void test_insertable_and_updatable() {
		assertEquals("INSERT user (user_id,pwd ) VALUES ( :userId,:pwd ) ",t3g.getInsertSql());
		assertEquals("UPDATE user SET (user_id = :userId,user_name = :userName ) WHERE user_id = ?",t3g.getUpdateByPkSql());
		
		assertEquals("INSERT user (user_id,group_id ) VALUES ( :userId,:groupId ) ",t4g.getInsertSql());
		assertEquals("UPDATE user SET (user_id = :userId,group_id = :groupId ) WHERE user_id = :userId AND group_id = :groupId",t4g.getUpdateByPkSql());
	}
}
