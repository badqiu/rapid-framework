package cn.org.rapid_framework.generator.ext.tableconfig.model;

import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.provider.db.DataSourceProvider;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;
import cn.org.rapid_framework.generator.util.SqlExecutorHelper;

/**
public class InsertAndUpdateSqlTest extends GeneratorTestCase{

	public InsertAndUpdateSqlTest() {
		testDbType = "mysql";
	}
	
	public void test_insert() {
		new SqlFactory().parseSql("insert into user_info (user_id) values (99999999999999999999999999999999)");
		new SqlFactory().parseSql("insert into user_info (user_id,password) values (9999998,'9999999999999999999999999999999999999999999999999999999999999999999999999999999')");
		new SqlFactory().parseSql("insert into user_info (user_id,password) values (123,'123')");
		new SqlFactory().parseSql("insert into user_info (user_id,birth_date) values (5555555555,'1999-10-10')");
		
		new SqlFactory().parseSql("insert into role_permission (role_id,permissoin_id) values (9525,9525)");
		new SqlFactory().parseSql("insert into role_permission (role_id,permissoin_id) values (123,9525)");
		
		new SqlFactory().parseSql("insert into role_permission (role_id,permissoin_id) values (9525,9525)");
		
		try {
		new SqlFactory().parseSql("insert into user_info (user_id,birth_date) values (5555555555,'1999')");
		fail("错误的日期格式");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void test_update() {
		try {
			SqlExecutorHelper.execute(DataSourceProvider.getDataSource(), "insert into user_info (user_id,password) values (1111,'2222')");
			SqlExecutorHelper.execute(DataSourceProvider.getDataSource(), "insert into user_info (user_id,password) values (2222,'2222')");
			SqlExecutorHelper.execute(DataSourceProvider.getDataSource(), "insert into role (role_id,created,modified) values (9999,now(),now())");
			SqlExecutorHelper.execute(DataSourceProvider.getDataSource(), "insert into permission (permissoin_id,created,modified) values (9999,now(),now())");
			
			new SqlFactory().parseSql("update user_info set user_id = 99999999999999999999999999999999");
			new SqlFactory().parseSql("update user_info set user_id = 9999998 ,password = '9999999999999999999999999999999999999999999999999999999999999999999999999999999' ");
			new SqlFactory().parseSql("update user_info set user_id = 123,password = '123'");
			new SqlFactory().parseSql("update user_info set user_id = 5555555555 , birth_date = '1999-10-10'");
			
			new SqlFactory().parseSql("update role_permission set  role_id = 9525,permissoin_id=9525");
			new SqlFactory().parseSql("update role_permission set  role_id = 123,permissoin_id = 9525");
			
			new SqlFactory().parseSql("update role_permission set role_id = 9525,permissoin_id=9525");
			
			try {
			new SqlFactory().parseSql("update user_info set (user_id = 5555555555,birth_date = 1999)");
			fail("错误的日期格式");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}finally {
			SqlExecutorHelper.execute(DataSourceProvider.getDataSource(), "delete from user_info where user_id = 1111");
			SqlExecutorHelper.execute(DataSourceProvider.getDataSource(), "delete from user_info where user_id = 2222");
			SqlExecutorHelper.execute(DataSourceProvider.getDataSource(), "delete from role where role_id = 9999");
			SqlExecutorHelper.execute(DataSourceProvider.getDataSource(), "delete from permission where permissoin_id = 9999");
		}
	}
}**/
