package cn.org.rapid_framework.generator.demo;

import java.io.File;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;

public class GenerateBySqlDemoTest extends GeneratorTestCase{
	GeneratorFacade generatorFacade = new GeneratorFacade();
	public void setUp() throws Exception {
		super.setUp();
		g.setOutRootDir("temp/"+getClass().getSimpleName()+"/"+getName());
		generatorFacade.setGenerator(g);
	}
	
	public void test_generate_by_sql() throws Exception {
		generatorFacade.getGenerator().addTemplateRootDir(new File("for_test_select_sql"));
		Sql sql = new SqlFactory().parseSql("select * from user_info where username=#username# and password=#password#");
		sql.setMultiplicity("many");  //many or one
		sql.setOperation("findByUsernameAndPassword");
		sql.setRemarks("根据用户名及密码进行查询");
		generatorFacade.generateBySql(sql);
		
	}
	
}
