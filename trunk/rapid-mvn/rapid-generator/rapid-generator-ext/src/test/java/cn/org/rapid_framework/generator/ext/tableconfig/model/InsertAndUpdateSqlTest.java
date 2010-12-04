package cn.org.rapid_framework.generator.ext.tableconfig.model;

import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;

public class InsertAndUpdateSqlTest extends GeneratorTestCase{

	public InsertAndUpdateSqlTest() {
		testDbType = "mysql";
	}
	
	public void test() {
		new SqlFactory().parseSql("insert into user_info (user_id) values (99999999999999999999999999999999)");
		new SqlFactory().parseSql("insert into user_info (user_id,password) values (9999998,'9999999999999999999999999999999999999999999999999999999999999999999999999999999')");
		new SqlFactory().parseSql("insert into user_info (user_id,password) values (123,'123')");
		new SqlFactory().parseSql("insert into user_info (user_id,birth_date) values (5555555555,'1999-10-10')");
		
		new SqlFactory().parseSql("insert into role_permission (role_id,permissoin_id) values (9525,9525)");
		new SqlFactory().parseSql("insert into role_permission (role_id,permissoin_id) values (123,9525)");
		
		new SqlFactory().parseSql("insert into role_permission (role_id,permissoin_id) values (9525,9525)");
		
		try {
		new SqlFactory().parseSql("insert into user_info (user_id,birth_date) values (5555555555,'1999')");
		fail();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
