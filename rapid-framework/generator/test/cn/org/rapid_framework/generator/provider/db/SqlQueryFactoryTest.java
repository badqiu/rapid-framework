package cn.org.rapid_framework.generator.provider.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.provider.sql.SqlFactory;
import cn.org.rapid_framework.generator.provider.sql.model.Sql;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.FileHelper;

public class SqlQueryFactoryTest extends GeneratorTestCase  {
	public void setUp() throws Exception {
		super.setUp();
		g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_test_select_sql"));
		g.setOutRootDir("./temp/sql");
	}
	
	public void test_with_no_parameers() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select * from user_info");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}

	public void test_with_2_parameers() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select * from user_info where username = :username and password =:password ");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}

	public void test_with_many_parameers() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select * from user_info where username = :username and password =:password and age = :age and sex = :sex and birth_date > :birth_date and birth_date < :birth_date2");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}
	
	public void test_with_many_parameers_with() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select sum(age) sum_age,count(username) cnt from user_info where username = :username and password =:password and age = :age and sex = :sex and birth_date > :birth_date and birth_date < :birth_date2");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}

	public void test_delete_sql() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("delete from user_info where username = :username and password = :password and age=:age and sex=:sex");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}

	public void test_update_sql() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("update user_info set username = :username where password = :password and age=:age and sex=:sex");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}

	public void test_sql_error() throws Exception {
		try {
			Sql selectSql  = new SqlFactory().parseSql("update user_info1 set username = :username where password = :password and age=:age and sex=:sex");
			fail();
		}catch(SQLException e){
			assertTrue(true);
		}
		
		try {
			Sql selectSql  = new SqlFactory().parseSql("delete from user_info where username1 = :username and password = :password and age=:age and sex=:sex");
			fail();
		}catch(SQLException e){
			assertTrue(true);
		}
		
		try {
			Sql selectSql  = new SqlFactory().parseSql("select * from user_info where username = :username and password =:password and age=1:age ");
			fail();
		}catch(SQLException e){
			assertTrue(true);
		}
	}
	
	public static int count;
	public GeneratorModel newFromQuery(Sql query) {
		query.setOperation(getName());
		Map templateModel = new HashMap();
		templateModel.putAll(GeneratorProperties.getProperties());
		templateModel.put("sql", query);
		setShareVars(templateModel);
		
		Map filePathModel = new HashMap();
		filePathModel.putAll(GeneratorProperties.getProperties());
		filePathModel.putAll(BeanHelper.describe(query));
		setShareVars(filePathModel);
		return new GeneratorModel(templateModel,filePathModel);
	}
	
	private static void setShareVars(Map templateModel) {
		templateModel.putAll(System.getProperties());
		templateModel.put("env", System.getenv());
	}
}
