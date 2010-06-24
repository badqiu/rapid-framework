package cn.org.rapid_framework.generator.provider.db;

import java.util.HashMap;
import java.util.Map;

import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.provider.sql.SqlQueryFactory;
import cn.org.rapid_framework.generator.provider.sql.SqlQueryFactory.SelectSqlMetaData;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.FileHelper;

public class SqlQueryFactoryTest extends GeneratorTestCase  {
	public void setUp() throws Exception {
		super.setUp();
		g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_test_select_sql"));
	}
	
	public void test_with_no_parameers() throws Exception {
		SelectSqlMetaData selectSql  = new SqlQueryFactory().getByQuery("select * from user_info");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}

	public void test_with_2_parameers() throws Exception {
		SelectSqlMetaData selectSql  = new SqlQueryFactory().getByQuery("select * from user_info where username = :username and password =:password ");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}

	public void test_with_many_parameers() throws Exception {
		SelectSqlMetaData selectSql  = new SqlQueryFactory().getByQuery("select * from user_info where username = :username and password =:password and age = :age and sex = :sex and birth_date > :birth_date and birth_date < :birth_date2");
		GeneratorModel gm = newFromQuery(selectSql);
		g.generateBy(gm.templateModel, gm.filePathModel);
	}
	
	public static GeneratorModel newFromQuery(SelectSqlMetaData query) {
		Map templateModel = new HashMap();
		templateModel.putAll(GeneratorProperties.getProperties());
		templateModel.put("query", query);
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
