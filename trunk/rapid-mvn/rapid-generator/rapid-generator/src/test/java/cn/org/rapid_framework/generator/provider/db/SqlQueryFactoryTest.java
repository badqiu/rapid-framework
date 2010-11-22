package cn.org.rapid_framework.generator.provider.db;

import java.util.HashMap;
import java.util.Map;

import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory.SqlParametersParser;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.StringHelper;

public class SqlQueryFactoryTest extends GeneratorTestCase  {
	public void setUp() throws Exception {
		super.setUp();
		g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_test_select_sql"));
		g.setOutRootDir("./temp/sql");
	}

	public void test_select_with_between() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select * from user_info where birth_date between #minBirthDate# and #maxBirthDate# and username = :specialUsername and sex <= :specialSex and age >= ${specialAge} and password <> #{password}");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}
	
	public void test_select_with_no_parameers() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select * from user_info");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}

	public void test_select_with_2_parameers() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select * from user_info where username = :username and password =:password ");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}

	public void test_select_with_in_parameers() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select * from user_info where username = :username and password =:password and age in (:age)");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}
	
	public void test_select_with_many_parameers() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select * from user_info where username = :username and password =:password and age = :age and sex = :sex and birth_date > :birth_date and birth_date < :birth_date2");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}
	
	public void test_select_with_cannot_guess_column_type() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select * from user_info where username = :username and password =:password and age = :age and sex = :sex and birth_date > :birth_date and birth_date < :birth_date2");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}
	
	public void test_select_same_params() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select * from user_info where username = :username and username like :username and birth_date between :birthDate and :birthDate");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}

	public void test_select_with_two_columns() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select sum(age) sum_age,count(username) cnt from user_info ");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}
	
	public void test_select_count_and_multi_policy_one() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select count(username) cnt from user_info where username = :username and password =:password and age = :age and sex = :sex and birth_date > :birth_date and birth_date < :birth_date2");
		selectSql.setMultiplicity(Sql.MULTIPLICITY_ONE);
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}
	
	public void test_select_string_and_multi_policy_one() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("select username from user_info where username = :username and password =:password and age = :age and sex = :sex and birth_date > :birth_date and birth_date < :birth_date2");
		selectSql.setMultiplicity(Sql.MULTIPLICITY_ONE);
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}
	
	public void test_delete_sql() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("delete from user_info where username = :username and password = :password and age=:age and sex=:sex");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}
	
	public void test_delete_myisam_user_sql() throws Exception {
//		DataSourceProvider.getConnection().close();
//		GeneratorProperties.reload();
		Sql selectSql  = new SqlFactory().parseSql("delete from user_info ");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}

	public void test_update_sql() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("update user_info set username = :username where password = :password and age=:age and sex=:sex");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}

	public void test_insert_sql() throws Exception {
		Sql selectSql  = new SqlFactory().parseSql("insert into user_info values(:username,:password,:age,:sex,:userid,:blog)");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}
	
	public void test_insert_into() {
		Sql selectSql  = new SqlFactory().parseSql("insert into user_info (username,password,age) values(?,?,?)");
	}
	
	public void test_sql_error() throws Exception {
		try {
			Sql selectSql  = new SqlFactory().parseSql("update user_info1 set username = :username where password = :password and age=:age and sex=:sex");
			fail();
		}catch(Exception e){
			assertTrue(true);
		}
		
		try {
			Sql selectSql  = new SqlFactory().parseSql("insert into user_info (username,password) values(?,?,?)");
			fail();
		}catch(Exception e){
			assertTrue(true);
		}
		
		try {
			Sql selectSql  = new SqlFactory().parseSql("delete from user_info where username1javaeye = :username and password = :password and age=:age and sex=:sex");
			fail();
		}catch(Exception e){
			assertTrue(true);
		}
		
		try {
			Sql selectSql  = new SqlFactory().parseSql("select * from user_info where username = :username and password =:password and age=1:age ");
			fail();
		}catch(Exception e){
			assertTrue(true);
		}
		
		try {
			Sql selectSql  = new SqlFactory().parseSql("insert into userinfo2 values(:username,:password,:age,:sex)");
			fail();
		}catch(Exception e){
			assertTrue(true);
		}
	}
	
	public void test_isMatchListParam() {
		SqlParametersParser sqlParametersParser = new SqlFactory().new SqlParametersParser();
        assertFalse(sqlParametersParser.isMatchListParam("  \n (:username) ", "username"));
        assertFalse(sqlParametersParser.isMatchListParam("  (&username) ", "username"));
        assertFalse(sqlParametersParser.isMatchListParam("  (#username#) ", "username"));
        assertFalse(sqlParametersParser.isMatchListParam("  (#{username}) ", "username"));
        assertFalse(sqlParametersParser.isMatchListParam("  ($username$) ", "username"));
        assertFalse(sqlParametersParser.isMatchListParam("  (${username}) ", "username"));
        
        assertTrue(sqlParametersParser.isMatchListParam("  ${username[index]} ", "username"));
        assertTrue(sqlParametersParser.isMatchListParam("  #{username[index]} ", "username"));
        assertTrue(sqlParametersParser.isMatchListParam("  #{username[a124]} ", "username"));
        assertTrue(sqlParametersParser.isMatchListParam("  ${username[${index}]} ", "username"));
        assertTrue(sqlParametersParser.isMatchListParam("  #{username[${index}]} ", "username"));
        assertTrue(sqlParametersParser.isMatchListParam("  #{username[${a124}]} ", "username"));
        
        assertTrue(sqlParametersParser.isMatchListParam(" in \n (:username) ", "username"));
        assertTrue(sqlParametersParser.isMatchListParam(" not in (&username) ", "username"));
        assertTrue(sqlParametersParser.isMatchListParam(" not in (#username#) ", "username"));
        assertTrue(sqlParametersParser.isMatchListParam(" in \n (#{username}) ", "username"));
        assertTrue(sqlParametersParser.isMatchListParam("  not \n in ($username$) ", "username"));
        assertTrue(sqlParametersParser.isMatchListParam(" not \n in \n (${username}) ", "username"));
        
		assertTrue(sqlParametersParser.isMatchListParam("  #username[]# ", "username"));
		assertTrue(sqlParametersParser.isMatchListParam("  $username[]$ ", "username"));
		assertTrue(sqlParametersParser.isMatchListParam("  #user[].age# ", "user"));
		
		assertFalse(sqlParametersParser.isMatchListParam("  $username[] $ ", "username"));
		assertFalse(sqlParametersParser.isMatchListParam("  #user[].age # ", "user"));
	}
	
	public static int count;
	public GeneratorModel newFromQuery(Sql sql) {
		sql.setOperation(StringHelper.uncapitalize(StringHelper.makeAllWordFirstLetterUpperCase(getName())));
//		sql.setTableSqlName("user_blog_info");
		if(count++ % 2 == 0) sql.setRemarks("Blog操作");
		
		Map templateModel = new HashMap();
		templateModel.putAll(GeneratorProperties.getProperties());
		templateModel.put("sql", sql);
		setShareVars(templateModel);
		
		Map filePathModel = new HashMap();
		filePathModel.putAll(GeneratorProperties.getProperties());
		filePathModel.putAll(BeanHelper.describe(sql));
		setShareVars(filePathModel);
		return new GeneratorModel(templateModel,filePathModel);
	}
	
	private static void setShareVars(Map map) {
		map.putAll(System.getProperties());
		map.put("env", System.getenv());
		map.put("className", "UserInfoBlog");
		map.put("tableClassName", "UserInfoBlog");
		map.put("basepackage", "com.company.project");
	}
}
