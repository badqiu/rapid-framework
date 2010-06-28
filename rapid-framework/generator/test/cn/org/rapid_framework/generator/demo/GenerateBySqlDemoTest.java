package cn.org.rapid_framework.generator.demo;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;

public class GenerateBySqlDemoTest extends GeneratorTestCase{
	GeneratorFacade generatorFacade = new GeneratorFacade();
	public void setUp() throws Exception {
		super.setUp();
		generatorFacade.g = g;
	}
	
	public void test_generate_by_sql() throws Exception {
		Sql sql = SqlFactory.parseSql("select * from user_info where username=#username# and password=#password#");
		sql.setTableSqlName("user_info");
		sql.setMultiPolicy("many");  //many or one
		sql.setOperation("findByUsernameAndPassword");
		sql.setRemarks("根据用户名及密码进行查询");
		generatorFacade.generateBySql(sql, "generator/test/for_test_select_sql");
	}
	
}
