package cn.org.rapid_framework.generator.ext.tableconfig.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import cn.org.rapid_framework.generator.GeneratorConstants;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorModelUtils;
import cn.org.rapid_framework.generator.ext.tableconfig.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.StringHelper;
/**
public class OracleTableConfigTest extends GeneratorTestCase {
    
    public void setUp() throws Exception {
        super.setUp();
        GLogger.perfLogLevel = GLogger.DEBUG;
    	GLogger.logLevel = GLogger.DEBUG;
    	GeneratorProperties.setProperty(GeneratorConstants.GG_IS_OVERRIDE.code, "true");
		GeneratorProperties.setProperty(GeneratorConstants.JDBC_URL, "jdbc:oracle:thin:@localhost:1521:xe");
		GeneratorProperties.setProperty(GeneratorConstants.JDBC_DRIVER, "oracle.jdbc.driver.OracleDriver");
		GeneratorProperties.setProperty(GeneratorConstants.JDBC_USERNAME, "test");
		GeneratorProperties.setProperty(GeneratorConstants.JDBC_PASSWORD, "123456");
		GeneratorProperties.setProperty(GeneratorConstants.JDBC_SCHEMA, "TEST");
		GeneratorProperties.setProperty(GeneratorConstants.JDBC_CATALOG, "");
		try {
			runSqlScripts("oracle_generator_test_table.sql");
		}catch(Exception e) {
			if(e.getMessage().contains("ORA-00955")) {
			}else {
				e.printStackTrace();
			}
		}
		
        g.setOutRootDir("./target/temp/generate_by_sql_config");
        GeneratorProperties.setProperty("appName", "rapid");
    }
    
    public void test_genereate_by_sql_config() throws Exception {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql_config"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/oracle_user_info.xml");
        TableConfig t = new TableConfigXmlBuilder().parseFromXML(new FileInputStream(file));
        GeneratorModel gm = newFromTable(t);
        g.generateBy(gm.templateModel, gm.filePathModel);
        
    }

    public void test_generate_by_oracle_user_info() throws Exception {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/oracle_user_info.xml");
        TableConfig t = new TableConfigXmlBuilder().parseFromXML(new FileInputStream(file));
        for(Sql sql : t.getSqls()) {
            GeneratorModel gm = newFromSql(sql,t);
            g.generateBy(gm.templateModel, gm.filePathModel);
        }
    }

    public void test_generate_by_user_info() throws Exception {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/user_info.xml");
        TableConfig t = new TableConfigXmlBuilder().parseFromXML(new FileInputStream(file));
        for(Sql sql : t.getSqls()) {
            GeneratorModel gm = newFromSql(sql,t);
            g.generateBy(gm.templateModel, gm.filePathModel);
        }
    }

	public void test_remove_table_prefix() {
		GeneratorProperties.setProperty(GeneratorConstants.TABLE_REMOVE_PREFIXES, "t_,v_");
		TableConfig tc = new TableConfig();
		tc.setSqlName("t_user_info");
		assertEquals("UserInfo",tc.getClassName());
		tc.setSqlName("v_user");
		assertEquals("User",tc.getClassName());
		
		tc.setSqlName("diy_user");
		assertEquals("DiyUser",tc.getClassName());
	}
	
    
    private GeneratorModel newFromSql(Sql sql, TableConfig t) {
        Map templateModel = new HashMap();
        templateModel.putAll(GeneratorProperties.getProperties());
        templateModel.put("sql", sql);
        templateModel.put("tableConfig", t);
        templateModel.put("basepackage", "com.oracletest");
        setShareVars(templateModel);
        
        Map filePathModel = new HashMap();
        filePathModel.putAll(GeneratorProperties.getProperties());
        filePathModel.putAll(BeanHelper.describe(t));
        filePathModel.putAll(BeanHelper.describe(sql));
        filePathModel.put("basepackage", "com.oracletest");
        setShareVars(filePathModel);
        GeneratorModel gm = new GeneratorModel(templateModel,filePathModel);
        return gm;
    }

    private GeneratorModel newFromTable(TableConfig t) {
        t.setPackage("com.company.project");
        Map templateModel = new HashMap();
        templateModel.putAll(GeneratorProperties.getProperties());
        templateModel.put("tableConfig", t);
        templateModel.put("basepackage", t.getBasepackage());
        setShareVars(templateModel);
        
        Map filePathModel = new HashMap();
        filePathModel.putAll(GeneratorProperties.getProperties());
        filePathModel.putAll(BeanHelper.describe(t));
        setShareVars(filePathModel);
        GeneratorModel gm = new GeneratorModel(templateModel,filePathModel);
        return gm;
    }
    
    private static void setShareVars(Map templateModel) {
    	templateModel.putAll(GeneratorModelUtils.getShareVars());
    	templateModel.put("StringHelper", new StringHelper());
    }
}**/
