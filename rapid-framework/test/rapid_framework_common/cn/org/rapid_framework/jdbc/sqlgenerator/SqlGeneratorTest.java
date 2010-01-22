package cn.org.rapid_framework.jdbc.sqlgenerator;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import cn.org.rapid_framework.jdbc.sqlgenerator.metadata.Column;
import cn.org.rapid_framework.jdbc.sqlgenerator.metadata.Table;
import cn.org.rapid_framework.test.hsql.HSQLMemDataSourceUtils;

public class SqlGeneratorTest {
	Table table = new Table("user",new Column("user_id","userId",true),new Column("user_name","userName"),new Column("pwd","pwd"));
	SqlGenerator singleGenerator = new SpringNamedSqlGenerator(table);

	Table multiKeyTable = new Table("user",new Column("user_id","userId",true),new Column("group_id","groupId",true),new Column("user_name","userName"),new Column("pwd","pwd"));
	SqlGenerator multiGenerator = new CacheSqlGenerator( new SpringNamedSqlGenerator(multiKeyTable));

	@Test
	public void getInsertSql(){
		assertEquals("INSERT INTO user (user_id,user_name,pwd ) VALUES ( :userId,:userName,:pwd ) ",singleGenerator.getInsertSql());
		assertEquals("INSERT INTO user (user_id,group_id,user_name,pwd ) VALUES ( :userId,:groupId,:userName,:pwd ) ",multiGenerator.getInsertSql());
	}
	@Test
	public void getUpdateByPkSql(){
		assertEquals("UPDATE user SET user_name = :userName,pwd = :pwd WHERE user_id = :userId",singleGenerator.getUpdateByPkSql());
		assertEquals("UPDATE user SET user_name = :userName,pwd = :pwd WHERE user_id = :userId AND group_id = :groupId",multiGenerator.getUpdateByPkSql());
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
		assertEquals("INSERT INTO user (user_id,pwd ) VALUES ( :userId,:pwd ) ",t3g.getInsertSql());
		assertEquals("UPDATE user SET user_name = :userName WHERE user_id = :userId",t3g.getUpdateByPkSql());
		
		assertEquals("INSERT INTO user (user_id,group_id ) VALUES ( :userId,:groupId ) ",t4g.getInsertSql());
		assertEquals("UPDATE user SET  WHERE user_id = :userId AND group_id = :groupId",t4g.getUpdateByPkSql());
	}
	
	DataSource ds = HSQLMemDataSourceUtils.getDataSource("create table user (user_id bigint primary key,user_name varchar(50),pwd varchar(50) )");
	NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ds);
	SimpleJdbcTemplate simpleTemplate = new SimpleJdbcTemplate(ds);
	Table t = new Table("user",new Column("user_id","userId",true),new Column("user_name","userName"),new Column("pwd","pwd"));
	SqlGenerator sg = new SpringNamedSqlGenerator(t);
	Map data = new HashMap();
	@Test
	public void testJdbcWithSqlGenerator() {
		data.put("userId", 1);
		data.put("userName", "badqiu");
		data.put("pwd", "123456");
		
		//insert
		template.update(sg.getInsertSql(),data);
		
		//select by id
		List results = simpleTemplate.queryForList(sg.getSelectByPkSql(),1);
		assertEquals(results.size(),1);
		
		//update
		data.put("pwd", "abc123");
		template.update(sg.getUpdateByPkSql(),data);
		results = simpleTemplate.queryForList(sg.getSelectByPkSql(),1);
		assertEquals(((Map)results.get(0)).get("pwd"),"abc123");
		
		//delete
		simpleTemplate.update(sg.getDeleteByPkSql(), 1);
		results = simpleTemplate.queryForList(sg.getSelectByPkSql(),1);
		assertEquals(results.size() , 0);
		
	}
}
