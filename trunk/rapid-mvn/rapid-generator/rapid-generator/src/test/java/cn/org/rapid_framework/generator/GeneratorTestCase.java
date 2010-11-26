package cn.org.rapid_framework.generator;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorModelUtils;
import cn.org.rapid_framework.generator.provider.db.DataSourceProvider;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;

public class GeneratorTestCase extends TestCase{
	protected Generator g;
	public synchronized void setUp()throws Exception {
	    g = new Generator();
		GLogger.logLevel = GLogger.DEBUG;
	    System.setProperty(GeneratorConstants.GG_IS_OVERRIDE, "true");
		
	    runSqlScripts();

//		System.getProperties().list(System.out);
		if(isRuningByMaven()) {
			String tempDir = getTempDir();
			System.out.println("running by maven, set outRootDir to tempDir="+tempDir);
			g.setOutRootDir(tempDir);
		}else {
			if(g.getOutRootDir() == null)
				g.setOutRootDir(".");
		}
	}

	public boolean isRuningByMaven() {
		return System.getProperty("surefire.real.class.path") != null;
	}
	
	static String testDbType = "h2";
	public static void runSqlScripts() throws SQLException, IOException {
	    if("hsql".equals(testDbType)) {
    		GeneratorProperties.setProperty(GeneratorConstants.JDBC_URL, "jdbc:hsqldb:mem:generatorDB"+StringHelper.randomNumeric(20));
    		GeneratorProperties.setProperty(GeneratorConstants.JDBC_DRIVER, "org.hsqldb.jdbcDriver");
	    }else if("h2".equals(testDbType)) {
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_URL, "jdbc:h2:mem:test"+StringHelper.randomNumeric(20));
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_DRIVER, "org.h2.Driver");	        
	    }else if("mysql".equals(testDbType)) {
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_URL, "jdbc:hsqldb:mem:generatorDB");
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_DRIVER, "com.mysql.jdbc.Driver");           
        }else if("oracle".equals(testDbType)) {
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_URL, "jdbc:hsqldb:mem:generatorDB");
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_DRIVER, "oracle.jdbc.driver.OracleDriver");           
        }else if("sqlserver".equals(testDbType)) {
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_URL, "jdbc:hsqldb:mem:generatorDB");
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_DRIVER, "com.microsoft.jdbc.sqlserver.SQLServerDriver");           
        }
	    
		GeneratorProperties.setProperty(GeneratorConstants.JDBC_USERNAME, "sa");
		GeneratorProperties.setProperty(GeneratorConstants.JDBC_PASSWORD, "");
		GeneratorProperties.setProperty(GeneratorConstants.JDBC_SCHEMA, "");
		GeneratorProperties.setProperty(GeneratorConstants.JDBC_CATALOG, "");
		
		runSqlScripts("generator_test_table.sql");
		
	}

	public static void runSqlScripts(String file) throws SQLException, IOException {
		Connection conn = DataSourceProvider.getConnection();
		Connection conn2 = DataSourceProvider.getConnection();
		assertEquals(conn,conn2);
		
//		System.out.println(conn.getCatalog());
		
		Statement stat = conn.createStatement();
		String sqls = IOHelper.readFile(FileHelper.getFileByClassLoader(file),"UTF-8");
		sqls = SqlParseHelper.removeSqlComments(sqls);
		System.out.println(sqls);
		for(String t : sqls.trim().split(";")) {
			stat.execute(t.trim());
		}
		stat.close();
	}
	
	public void generateByTable(Table table) throws Exception {
		GeneratorModel m = GeneratorModelUtils.newFromTable(table);
//		g.setIgnoreTemplateGenerateException(false);
		g.generateBy(m.templateModel, m.filePathModel);
	}
	
	public void generateByTable(Generator g,Table table) throws Exception {
		GeneratorModel m = GeneratorModelUtils.newFromTable(table);
		g.generateBy(m.templateModel, m.filePathModel);
	}
	
	public String getTempDir() {
		String tempDir = System.getProperty("java.io.tmpdir");
		return new File(tempDir,"test_generator_out").getAbsolutePath();
	}
	
}
