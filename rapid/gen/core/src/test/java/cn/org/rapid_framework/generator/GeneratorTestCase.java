package cn.org.rapid_framework.generator;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorModelUtils;
import cn.org.rapid_framework.generator.provider.db.DataSourceProvider;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.util.DBHelper;
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
	    GeneratorProperties.setProperty(GeneratorConstants.GG_IS_OVERRIDE.code, "true");
		
	    try {
	    	runSqlScripts();
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }

//		System.getProperties().list(System.out);
		if(isRuningByMaven()) {
			String tempDir = getTempDir();
			System.out.println("running by maven, set outRootDir to tempDir="+tempDir);
			g.setOutRootDir(tempDir);
		}else {
			if(g.getOutRootDir() == null)
				g.setOutRootDir("target/generator-output");
		}
	}

	public boolean isRuningByMaven() {
		return System.getProperty("surefire.real.class.path") != null;
	}
	
	protected String testDbType = "h2";
	public void runSqlScripts() throws SQLException, IOException {
	    if("hsql".equals(testDbType)) {
    		GeneratorProperties.setProperty(GeneratorConstants.JDBC_URL, "jdbc:hsqldb:mem:generatorDB"+StringHelper.randomNumeric(20));
    		GeneratorProperties.setProperty(GeneratorConstants.JDBC_DRIVER, "org.hsqldb.jdbcDriver");
	    }else if("h2".equals(testDbType)) {
			GeneratorProperties.setProperty(GeneratorConstants.JDBC_USERNAME, "sa");
			GeneratorProperties.setProperty(GeneratorConstants.JDBC_PASSWORD, "");
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_URL, "jdbc:h2:mem:test"+StringHelper.randomNumeric(20)+";DB_CLOSE_DELAY=-1");
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_DRIVER, "org.h2.Driver");	        
	    }else if("mysql".equals(testDbType)) {
			GeneratorProperties.setProperty(GeneratorConstants.JDBC_USERNAME, "root");
			GeneratorProperties.setProperty(GeneratorConstants.JDBC_PASSWORD, "123456");
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_URL, "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8");
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_DRIVER, "com.mysql.jdbc.Driver");           
        }else if("oracle".equals(testDbType)) {
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_URL, "jdbc:hsqldb:mem:generatorDB");
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_DRIVER, "oracle.jdbc.driver.OracleDriver");           
        }else if("sqlserver".equals(testDbType)) {
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_URL, "jdbc:hsqldb:mem:generatorDB");
            GeneratorProperties.setProperty(GeneratorConstants.JDBC_DRIVER, "com.microsoft.jdbc.sqlserver.SQLServerDriver");           
        }else {
        	throw new RuntimeException("请指定数据库类型");
        }
	    

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
		try {
    		String sqls = IOHelper.readFile(FileHelper.getFileByClassLoader(file),"UTF-8");
    		sqls = SqlParseHelper.removeSqlComments(sqls);
    		System.out.println(sqls);
    		for(String t : sqls.trim().split(";")) {
    			stat.execute(t.trim());
    		}
		}finally {
		    DBHelper.close(conn);
		    DBHelper.close(conn2);
		    DBHelper.close(stat);
		}
	}
	
	public void generateByTable(Table table) throws Exception {
		GeneratorModel m = GeneratorModelUtils.newGeneratorModel("table",table);
//		g.setIgnoreTemplateGenerateException(false);
		g.generateBy(m.templateModel, m.filePathModel);
	}
	
	public void generateByTable(Generator g,Table table) throws Exception {
		GeneratorModel m = GeneratorModelUtils.newGeneratorModel("table",table);
		g.generateBy(m.templateModel, m.filePathModel);
	}
	
	public String getTempDir() {
		String tempDir = System.getProperty("java.io.tmpdir");
		return new File(tempDir,"test_generator_out").getAbsolutePath();
	}
	
	public static void assertContains(String str,String... regexArray) {
		for(String regex : regexArray) {
			assertTrue("not match Regex:"+regex+" str:"+str,isStringMatch(str, regex));
		}
	}

	private static boolean isStringMatch(String str, String regex) {
		String noSpaceString = str.replaceAll("\\s*", "");
		if(str.contains(regex) || noSpaceString.contains(regex.replaceAll("\\s*", ""))) {
			return true;
		}
		for(String segment : StringHelper.tokenizeToStringArray(regex, "----")) {
//				segment = segment.replaceAll("file:.*","");
//			List<String> files = getFiles(segment);
			if(!noSpaceString.contains(segment.replaceAll("\\s*", "").replace("file:.*",""))) {
				for(String line : IOHelper.readLines(new StringReader(segment))) {
					if(StringHelper.isBlank(line)) continue;
					if(!noSpaceString.contains(line.replaceAll("\\s*", ""))) {
						throw new RuntimeException("not match on line:\n"+line+" \n\n\n\nstr:\n"+str);
					}
				}
				throw new RuntimeException("not match segment:"+segment+" \n\n\n\nstr:\n"+str);
			}
		}
		return false;
	}

	public static void assertNotContains(String str,String... regexArray) {
		for(String regex : regexArray) {
			assertFalse("not match Regex:"+regex+" str:"+str,str.contains(regex));
		}
	}
}
